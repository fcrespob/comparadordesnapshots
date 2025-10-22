package es.mapfre.solvencia.formulacion.programas.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.programas.ProgramaFlujo;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.ConstantsProcesos;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.formulacion.util.UtilProcesos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * El programa NAN001– ESTABLECIMIENTO DE IMPORTE PROBABLE NO ANULADO consiste en el cálculo del importe no anulado de una proyección dada.
 * Como resultado obtendremos una corriente de factores de probabilización e importes no anulados para cada periodo de proyección de la corriente. 
 *
 * @author rschacon
 *
 */
public class ProgramaNAN001 extends ProgramaFlujo {

	private static final Logger LOG = LoggerFactory.getLogger(ProgramaNAN001.class);

	
	@Override
	protected String getTipoElemento() {
		return ConstantsProcesos.CTE_TIPO_ELEM_03;
	}

	@Override
	protected void executeImpl(final Umic umic, final FichaProceso fichaProceso,
			final DetalleBaseTecnica detalleBT, final List<DetalleCorriente> detallesCorriente, final String subProcesoActual) throws Solvencia2Excepcion {
		
		if (ProgramaNAN001.LOG.isTraceEnabled()) {
			ProgramaNAN001.LOG.trace("Inicio función << executeImpl >> de la clase ProgramaNAN001");
		}
		
		try {
			//Llamamos a la función de calculo no anulado
			ejecutarCalculoNoAnulado(umic, fichaProceso, detalleBT, detallesCorriente, subProcesoActual);
		} catch (Solvencia2Excepcion e) {
			ProgramaNAN001.LOG.error(e.getIncidencia().getTextoError(), e);
			UtilProcesos.exceptionControladaPrograma(detalleBT, umic, getNombrePrograma(), e);
		} catch (Exception e) {
			ProgramaNAN001.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ProgramaNAN001.LOG.isTraceEnabled()) {
			ProgramaNAN001.LOG.trace("Fin función << executeImpl >> de la clase ProgramaNAN001");
		}
		
	}
	
	/**
	 * Función encargada de encapsular el procedimiento para obtener importe del flujo no anulado de cada proyección.
	 * 
	 * @param umic 
	 * 			Contiene los datos de la Umic que se está procesando.
	 * @param fichaProceso
	 * 			Contiene los datos del proceso necesarios para su ejecución. 
	 * @param detalleBT
	 * 			Contiene el detalle de la base técnica de cálculo para la umic.
	 * @param lstDetalleCorrien
	 * 			Código el subproceso que se está ejecutando.
	 * @param subProcesoActual
	 * 			Corriente de la umic.
	 */
	private void ejecutarCalculoNoAnulado(final Umic umic, final FichaProceso fichaProceso, final DetalleBaseTecnica detalleBT, 
			final List<DetalleCorriente> lstDetalleCorrien, final String subProcesoActual) {
		//Variables locales
		Modulo modulo = getModuloCalculo();
		final IAlmacenarDatos almacenarDatos = FachadaServicios.getAlmacenarDatos();
		final IObtenerDatos obtenerDatos = FachadaServicios.getObtenerDatos();
		String varModuloCalc  = ConstantsFunciones.CTE_CADENA_VACIA;
		BigDecimal factor = BigDecimal.ZERO;
		List<DetalleCorriente> varProyBTI = null;
		List<DetalleCorriente> varProyROSSP = null;
		List<DetalleCorriente> varProyNIIF17 = null;
		BigDecimal importeNoAnulado = BigDecimal.ZERO;
		boolean calcularCadaProyeccion = true;
		boolean varSCR = false;
		//Fin variables locales
		
		if (ProgramaNAN001.LOG.isTraceEnabled()) {
			ProgramaNAN001.LOG.trace("Inicio función << ejecutarCalculoNoAnulado >> de la clase ProgramaNAN001");
		}
		
		/**
		 *  En primer lugar se recuperará el módulo de cálculo del importe probable no anulado para la modalidad, garantía, prestación y base técnica de la umic.  
			Para cada proyección recuperada, proyUmic,  se deberá calcular el importe probable no anulado para la corriente 
			invocando al módulo de cálculo anteriormente recuperado,  si el módulo es distinto de LEIDO y distinto de nulo.
			Para ello, todos los módulos de cálculo que intervienen en la cuantía probable no anulada de la corriente 
			deberán tener los mismos parámetros de entrada/ salida, de cara a independizar la invocación del módulo correspondiente y hacerlo genérico. 
			Si el módulo de cálculo al que se invoque necesita de algún dato que no conste como parámetro de entrada al mismo, 
			será el propio módulo de cálculo el que se encargue de recuperar dicho dato. 
			Si no existe parametrizado módulo de cálculo de cuantía probable no anulada para la modalidad, garantía, prestación 
			y base técnica de la umic, se considerará como importe probable no anulado el mismo que el importe probable calculado para la corriente. 
			Si el módulo de cálculo es LEIDO, se deberá recuperar el detalle de la proyección de la UMIC calculado anteriormente 
			para la UMIC con la base técnica BTI, estableciendo para el importe probable no anulado el mismo importe probable 
			no anulado calculado en BTI y el mismo factor de probabilización.
			A continuación, para cada periodo de proyección, el  resultado del módulo de cálculo resultante será aplicado 
			al importe correspondiente de la corriente y escrito en la proyección. 
			Una vez procesados todos los puntos de la proyección, los mismos serán almacenados para su posterior recuperación 
			y generación de la salida  de la umic.
			Si se produce un error en algún punto del programa se registrará dicho error de la umic en el fichero de incidencias, 
			terminando el programa para dicha umic.
		
		 	* 7.7.1	obtenerConfiguracion.recuperarModulo
					Obtiene el módulo de cálculo a ejecutar en el programa.   
					
					•	varModuloCalc = obtenerConfiguracion.recuperarModulo con los parámetros: 
					o	 Umic.datosGenerales.kmodalidad
					o	Umic.datosGenerales.kgarantia
					o	Umic.datosGenerales.kprestacion
					o	btc.btcalc
					o	codSubproceso
					o	tipoElemento    = 03
		 */
		// Esta configuración se recupera en la clase padre ProgramaFlujo		
		/**
		 * 7.7.4	obtenerConfiguracion.recuperarProyeccion
					Cuando varModuloCalc es LEIDO, se deberá recuperar la proyección de la UMIC calculada anteriormente bajo 
					la base técnica inicial (BTI). Para ello se llamará a obtenerConfiguracion.recuperarProyeccion con los siguientes parámetros: 
					varProyBTI  = obtenerConfiguracion.recuperarProyeccion () con:
					-	feccierre = btcUmic.fecCierre
					-	ktipobt =BTI
					-	claveUmic:
					o	cnegocio = umic.cnegocio
					o	ccanal = umic.ccanal
					o	ccartera = umic.ccanal
					o	kmodalidad = umic.kmodalidad
					o	kpoliza = umic.kpoliza
					o	ksubpoliza = umic.ksubpoliza
					o	kcertificado = umic.kcertificado
					o	nsuscri = umic.nsuscri
					o	norden = umic.norden
					o	kgarantia = umic.kgarantia
					o	kprestacion = umic.kgarantia
					o	kajuste = umic.kgarantia
					o	ctipoaport = umic.kgarantia
		 */
		if((fichaProceso.getCtipobt().equals(ConstantsModulos.CTE_VAL_MULTI4NB) || fichaProceso.getCtipobt().equals(ConstantsModulos.CTE_VAL_MULTI4C) || fichaProceso.getCtipobt().equals(ConstantsModulos.CTE_VAL_MULTI8) || fichaProceso.getCtipobt().equals(ConstantsModulos.CTE_VAL_MULTI8NB)) && !detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_BTI_PROY) && !detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_BTI) && !detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_ROSSP) && !detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_ROSSPCSM) && !detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_ROSSPTI) && !detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_ROSSPTE) && !detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_ROSSPGA) && !detalleBT.getBaseTec().equals(ConstantsModulos.CTE_BT_NIIF17)){
			varProyNIIF17  = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_BT_NIIF17, detalleBT.getFecCierre(), umic.getKey());
			copiarNoAnuladoProyBTI(subProcesoActual, lstDetalleCorrien, varProyNIIF17);
		}else if (ConstantsProcesos.CTE_LEIDO_BTI.equals(getModuloCalculo().getNombreServicio())) {
			varProyBTI = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_BTI, detalleBT.getFecCierre(), umic.getKey());
			copiarNoAnuladoProyBTI(subProcesoActual,  lstDetalleCorrien, varProyBTI);
		}else if(ConstantsProcesos.CTE_LEIDO_BTIPR.equals(getModuloCalculo().getNombreServicio())){
			varProyBTI = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_VAL_BTI_PROY, detalleBT.getFecCierre(), umic.getKey());
			copiarNoAnuladoProyBTI(subProcesoActual,  lstDetalleCorrien, varProyBTI);
		}else if(ConstantsProcesos.CTE_LEIDO_ROSSP.equals(getModuloCalculo().getNombreServicio())){
			varProyROSSP  = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_BT_ROSSP, detalleBT.getFecCierre(), umic.getKey());
			copiarNoAnuladoProyBTI(subProcesoActual, lstDetalleCorrien, varProyROSSP);
		}else if(ConstantsProcesos.CTE_LEIDO_ROSSPCSM.equals(getModuloCalculo().getNombreServicio())){
			varProyROSSP  = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_BT_ROSSPCSM, detalleBT.getFecCierre(), umic.getKey());
			copiarNoAnuladoProyBTI(subProcesoActual, lstDetalleCorrien, varProyROSSP);
		} else if (detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRVM)) {
			//Para el estrés de volatilidad de mortalidad recuperaremos los valores de BEL
			varProyBTI = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_BT_BEL, detalleBT.getFecCierre(), umic.getKey());
			copiarNoAnuladoProyBTI(subProcesoActual,  lstDetalleCorrien, varProyBTI);
		}
		
		/**
		 *  Cuando varModuloCalc es distinto de LEIDO, cómo se ha descrito anteriormente, se recorrerá la estructura de proyecciones de la umic (ProyUmic)  
		 *  desde  la primera proyección hasta  la última. 
			Para cada proyección de la umic se invocará al módulo de probabilización correspondiente (varModuloCalc). 


					•	ProyUmic 
					•	varPeriodoProyeccion  =  J -->  índice del periodo de proyección actual  que se está procesando en el bucle que recorre 
														las proyecciones de la umic (PROY_UMIC)
					•	fichaProceso
					•	umic
					•	btcUmic
					De forma que: 
						varFactor= varModuloCalc(ProyUmic , varPeriodoProyeccion  , fichaProceso, umic, btcUmic)
		 */
		else {
			
			if (detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTIU) ||
					detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTID) ||
					detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMFE) ||
					detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMMI) ||
					detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLFE) ||
					detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLMI) ||
					detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCF) ||
					detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCI) ||
					detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRINC) ||
					detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRGTO)) {
				varProyBTI = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_BT_BEL, detalleBT.getFecCierre(), umic.getKey());
				varSCR = true;
			}
			
			/**
			 * Si codSubproceso = ‘PROY_COMI’ se comproborá previamente si existen fechas de calculo generadas ó  no, para ver si debe invocarse al módulo de cálculo ó no: 
					•	 Si no existe ninguna  proyección con fecha de cálculo y /ó fecha de devengo <>  null, NO se deberá  invocar al módulo correspondiente como en el resto de casos, sino que se hará: 
							proyUmic (j).bloqueCorriente.fpbAtc = 0
							proyUmic(j).bloqueCorriente.impFlujoNoAnulado = 0
					para todos los periodos de la umic, finalizando así el programa para la umic, y se almacenarán los datos invocando a  almacenarDatos.proyeccion
					•	Si  existe algúna proyección con fecha de cálculo y /ó fecha de devengo <>  null, se deberá  invocar al módulo correspondiente como en el resto de casos, tal y como se muestra a continuación.

			 */
			if (ConstantsModulos.CTE_PROY_COMI.equals(subProcesoActual)) {
				calcularCadaProyeccion = UtilModulos.comprobarProyeccionConFechas(lstDetalleCorrien, subProcesoActual);
			}
			
			final int lonDetalCorr = lstDetalleCorrien.size();
		
			for (int i = 0; i < lonDetalCorr; i++) {
				/**
				 * 7.7.3	calcularProyeccion(j)
						Una vez ejecutado el módulo de cálculo para la proyección j, escribiremos los datos de la salida del flujo probable 
						a calcular de dicha proyección de forma que: 
						Si varModuloCalc no es nulo: 
							proyUmic (j).bloqueCorriente.fpbAtc = varFactor
							proyUmic(j).bloqueCorriente.impFlujoNoAnulado = proyUmic(j).bloqueCorriente. impFlujoProbable * varFactor
						
							Si varModuloCalc es nulo: 
								Si proyUmic (j).bloqueCorriente.fechaDevengo es NO nula:
									-	proyUmic (j).bloqueCorriente.fpbAtc = 1
								Si proyUmic (j).bloqueCorriente.fechaDevengo es nula:
									-	proyUmic (j).bloqueCorriente.fpbAtc = 0
							proyUmic(j).bloqueCorriente.impFlujoNoAnulado = proyUmic(j).bloqueCorriente. impFlujoProbable 
							
						si varModuloCalc es LEIDO
							proyUmic(j).bloqueCorriente.impFlujoNoAnulado = varNoAnulado (j)
							proyUmic(j).bloqueCorriente.fpbAtc = varfpbAtc (j)
				 */
					/**
					 * 7.7.2	invocarModulo(proyUmic(j))
	
						Cuando varModuloCalc es distinto de LEIDO, cómo se ha descrito anteriormente, se recorrerá la estructura de proyecciones de la umic (ProyUmic) 
						 desde  la primera proyección hasta  la última. 
						Para cada proyección de la umic se invocará al módulo de probabilización correspondiente (varModuloCalc). 
	
						
						Para invocar a dicho módulo varModuloCalc se deberán enviar los parámetros de entrada: 
							•	ProyUmic 
							•	varPeriodoProyeccion  =  J -->  índice del periodo de proyección actual  que se está 
																procesando en el bucle que recorre las proyecciones de la umic (PROY_UMIC)
							•	fichaProceso.fcalc
							•	umic
							•	btcUmic
							•	codSubproceso
						
						De forma que: 
						varFactor= varModuloCalc(ProyUmic , varPeriodoProyeccion  , fichaProceso.fcalc, umic, btcUmic, codSubproceso)
	
	
					 */
				final BloqueCorriente bloqueProyeccion = lstDetalleCorrien.get(i).getBloqueBySubproceso(subProcesoActual);
				if (calcularCadaProyeccion && bloqueProyeccion.getImpFlujoProbable() != null && bloqueProyeccion.getImpFlujoProbable().signum() != 0) {
//				if (calcularCadaProyeccion) {
					if(ConstantsProcesos.CTE_ERROR_BT_FPNA.equals(modulo.getNombreServicio())){
						varProyBTI = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_BTI, detalleBT.getFecCierre(),umic.getKey());
						if(null == varProyBTI || varProyBTI.size() == 0){
							varProyBTI  = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_BTI_PROY, detalleBT.getFecCierre(), umic.getKey());
						}
						
						factor = varProyBTI.get(i).getBloqueBySubproceso(subProcesoActual).getFpbAtc();		
					}else if (varSCR){
						// Recuperamos el factor de BEL
						factor = varProyBTI.get(i).getBloqueBySubproceso(subProcesoActual).getFpbAtc();
					} else {
						factor = (BigDecimal) modulo.execute(lstDetalleCorrien, bloqueProyeccion, 
								i + 1, fichaProceso.getFcalc(), umic, detalleBT, this.getMapVariables(), subProcesoActual);
					}
					
					//Calcular proyeccion caso que el modulo sea distinto de null
					importeNoAnulado = bloqueProyeccion.getImpFlujoProbable().multiply(factor);
				} else {
					factor = BigDecimal.ZERO;
					importeNoAnulado = BigDecimal.ZERO;
				}
				
				bloqueProyeccion.setFpbAtc(factor);
				bloqueProyeccion.setImpFlujoNoAnulado(importeNoAnulado.setScale(ConstantsFunciones.CTE_2, RoundingMode.HALF_DOWN));
			}
		}
		//Almacenamos la proyeccion
		almacenarDatos.almacenarProyeccion(lstDetalleCorrien);
		
		if (ProgramaNAN001.LOG.isTraceEnabled()) {
			ProgramaNAN001.LOG.trace("Fin función << ejecutarCalculoNoAnulado >> de la clase ProgramaNAN001");
		}
		
	}
	
	/** Función encargada de obtener el importeActulizado y el fpbAtc de la iteración j, cuando el modulo tiene el valor LEIDO
	 * 
	 * @param subProcesoActual Subproceso actual
	 * @param detallesCorriente Corriente de la umic.
	 * @param varProyBTI Proyección de la UMIC calculada anteriormente bajo la base técnica inicial (BTI)
	 */
	private void copiarNoAnuladoProyBTI(final String subProcesoActual, final List<DetalleCorriente> detallesCorriente, final List<DetalleCorriente> varProyBTI) {
		/**
		 * Una vez recuperada la proyección BTI calculada para la UMIC (varProyBTI) se recorrerá la estructura de proyecciones de la umic (ProyUmic)  
		 * desde  la primera proyección hasta  la última. 
			Para cada proyección de la umic se asignarán los valores correspondientes de la corriente, dependiendo del subproceso que 
			haya invocado al programa de forma que: 
			•	Si codSubproceso = ‘PROY_VIDA’
				-	varNoAnulado(j) = varProyBTI(j).corrienteVida.impFlujoNoAnulado
				-	varfpbAtc (j) = varProyBTI(j).corrienteVida.fpbAtc
			•	Si codSubproceso = ‘PROY_FALL’
				-	varNoAnulado (j) = varProyBTI(j).corrienteFallecimiento.impFlujoNoAnulado
				-	varfpbAtc (j) = varProyBTI(j). corrienteFallecimiento.fpbAtc
			•	Si codSubproceso = ‘PROY_COMP ’
				-	varNoAnulado (j) = varProyBTI(j).corrienteComplementario.impFlujoNoAnulado
				-	varfpbAtc (j) = varProyBTI(j). corrienteComplementario.fpbAtc
			•	Si codSubproceso = ‘PROY_PRIMA’
				-	varNoAnulado (j) = varProyBTI(j).corrientePrima.impFlujoNoAnulado
				-	varfpbAtc (j) = varProyBTI(j). corrientePrima.fpbAtc
			•	Si codSubproceso = ‘PROY_GTOS’
				-	varNoAnulado (j) = varProyBTI(j).corrienteGasto.impFlujoNoAnulado
				-	varfpbAtc (j) = varProyBTI(j). corrienteGasto.fpbAtc
			•	Si codSubproceso = ‘PROY_RESC’
				-	varNoAnulado (j) = varProyBTI(j).corrienteRescate.impFlujoNoAnulado
				-	varfpbAtc (j) = varProyBTI(j). corrienteRescate.fpbAtc
			•	Si codSubproceso = ‘PROY_COMI’
				-	varNoAnulado (j) = varProyBTI(j).corrienteComision.impFlujoNoAnulado
				-	varfpbAtc (j) = varProyBTI(j).corrienteComision.fpbAtc

		 */
				
		final int sizeDetalle = detallesCorriente.size();
		for (int i = 0; i < sizeDetalle; i++) {
			final DetalleCorriente nuevoDetalle = detallesCorriente.get(i);
			final BloqueCorriente nuevoBloque = nuevoDetalle.getBloqueBySubproceso(subProcesoActual);

			final DetalleCorriente detalleBTI = varProyBTI.get(i);
			final BloqueCorriente bloqueBTI = detalleBTI.getBloqueBySubproceso(subProcesoActual);
			
			nuevoBloque.setImpFlujoNoAnulado(bloqueBTI.getImpFlujoNoAnulado());
			nuevoBloque.setFpbAtc(bloqueBTI.getFpbAtc());
		}
	}
	
	@Override
	public String getNombrePrograma() {
		return ConstantsFactorias.PROGRAMA_NAN001;
	}

	@Override
	protected void modificarCorriente(Umic umic, FichaProceso fichaProceso,
			DetalleBaseTecnica detBaseTecnica,
			List<DetalleCorriente> detallesCorriente, String subProcesoActual) {
		
		final IAlmacenarDatos almacenarDatos = FachadaServicios.getAlmacenarDatos();
		
		for(DetalleCorriente detalleCorriente : detallesCorriente) {
			final BloqueCorriente bloqueProyeccion = detalleCorriente.getBloqueBySubproceso(subProcesoActual);
			if (null == bloqueProyeccion.getFechaDevengo()) {
				bloqueProyeccion.setFpbAtc(BigDecimal.ZERO);
			} else {
				bloqueProyeccion.setFpbAtc(BigDecimal.ONE);
			}
			bloqueProyeccion.setImpFlujoNoAnulado(bloqueProyeccion.getImpFlujoProbable());		
		}
		//Almacenamos la proyeccion
		almacenarDatos.almacenarProyeccion(detallesCorriente);

	}
		

}
