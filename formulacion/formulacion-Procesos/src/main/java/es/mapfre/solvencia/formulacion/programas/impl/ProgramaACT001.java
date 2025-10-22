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
 * El programa ACT001– ESTABLECIMIENTO DE IMPORTE ACTUALIZADO consiste en el cálculo del importe probable no anulado actualizado 
 * financieramente de una proyección dada.
 * Como resultado obtendremos una corriente de factores de actualización  e importes actualizados para cada periodo de proyección de la corriente. 
 *
 * @author rschacon
 *
 */
public class ProgramaACT001 extends ProgramaFlujo {
	
	private static final Logger LOG = LoggerFactory.getLogger(ProgramaACT001.class);
	
	@Override
	protected String getTipoElemento() {
		return ConstantsProcesos.CTE_TIPO_ELEM_04;
	}

	@Override
	protected void executeImpl(final Umic umic, final FichaProceso fichaProceso, final DetalleBaseTecnica detalleBT, 
			final List<DetalleCorriente> detallesCorriente, final String subProcesoActual) throws Solvencia2Excepcion {
		
		if (ProgramaACT001.LOG.isTraceEnabled()) {
			ProgramaACT001.LOG.trace("Inicio función << executeImpl >> de la clase ProgramaACT001");
		}
		
		try {
			//Llamamos a la función que obtiene el importe del flujo actualizado
			calcularImporteFlujoActualizado(umic, fichaProceso, detalleBT, detallesCorriente, subProcesoActual);
		} catch (Solvencia2Excepcion e) {
			ProgramaACT001.LOG.error(e.getIncidencia().getTextoError(), e);
			UtilProcesos.exceptionControladaPrograma(detalleBT, umic, getNombrePrograma(), e);
		} catch (Exception e) {
			ProgramaACT001.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ProgramaACT001.LOG.isTraceEnabled()) {
			ProgramaACT001.LOG.trace("Fin función << executeImpl >> de la clase ProgramaACT001");
		}
		
	}
	
	/**
	 * Función encargada de realizar el calculo del importe del flujo actualizado
	 * 
	 * @param umic
	 * 			Contiene los datos de la Umic que se está procesando.
	 * @param fichaProceso
	 * 			Contiene los datos del proceso necesarios para su ejecución. 
	 * @param detalleBT
	 * 			Contiene el detalle de la base técnica de cálculo para la umic.
	 * @param lstDetalleCorrien
	 * 			Corriente de la umic.
	 * @param subProcesoActual
	 * 			Código el subproceso que se está ejecutando.
	 */
	private void calcularImporteFlujoActualizado(final Umic umic, final FichaProceso fichaProceso, final DetalleBaseTecnica detalleBT, 
			final List<DetalleCorriente> lstDetalleCorrien, 
			final String subProcesoActual) {
		//Variables locales
		Modulo modulo = getModuloCalculo();
		final IAlmacenarDatos almacenarDatos = FachadaServicios.getAlmacenarDatos();
		final IObtenerDatos obtenerDatos = FachadaServicios.getObtenerDatos();
		BigDecimal factor = BigDecimal.ZERO;
		int iteracion = ConstantsFunciones.CTE_1;
		List<DetalleCorriente> varProyBTI = null;
		List<DetalleCorriente> varProyROSSP   = null;
		BigDecimal importeActualizado = BigDecimal.ZERO;
		boolean calcularCadaProyeccion = true;
		boolean varSCR = false;
		//Fin variables locales
		
		if (ProgramaACT001.LOG.isTraceEnabled()) {
			ProgramaACT001.LOG.trace("Inicio función << calcularImporteFlujoActualizado >> de la clase ProgramaACT001");
		}
		
		/**
		 * En primer lugar se recuperará el módulo de cálculo del importe actualizado para la modalidad, garantía, prestación y base técnica de la umic.  
			Para cada proyección recuperada, proyUmic,  se deberá calcular el importe actualizado para la corriente invocando 
			al módulo de cálculo anteriormente recuperado.
			Para ello, todos los módulos de cálculo que intervienen en la cuantía actualizada de la corriente deberán tener 
			los mismos parámetros de entrada/ salida, de cara a independizar la invocación del módulo correspondiente y hacerlo genérico. 
			Si el módulo de cálculo al que se invoque necesita de algún dato que no conste como parámetro de entrada al mismo, 
			será el propio módulo de cálculo el que se encargue de recuperar dicho dato. 
			A continuación, para cada periodo de proyección, el  resultado del módulo de cálculo resultante será aplicado 
			al importe correspondiente de la corriente y escrito en la proyección. 
			Una vez procesados todos los puntos de la proyección, los mismos serán almacenados para su posterior recuperación 
			y generación de la salida  de la umic.
			Si se produce un error en algún punto del programa se registrará dicho error de la umic en el fichero de incidencias, 
			terminando el programa para dicha umic.
		 
		 * 8.7.1	obtenerConfiguracion.recuperarModulo
					Obtiene el módulo de cálculo a ejecutar en el programa.   
					
					•	varModuloCalc = obtenerConfiguracion.recuperarModulo con los parámetros: 
					o	 Umic.datosGenerales.kmodalidad
					o	Umic.datosGenerales.kgarantia
					o	Umic.datosGenerales.kprestacion
					o	fichaProceso.ktipobt
					o	codSubproceso
					o	tipoElemento    = 04
		 *
		 */
		// Esta configuración se recupera en la clase padre ProgramaFlujo
		/**
		 * 8.7.4	obtenerConfiguracion.recuperarProyeccion
				Cuando varModuloCalc es LEIDO, se deberá recuperar la proyección de la UMIC calculada anteriormente bajo la base técnica inicial (BTI). 
				Para ello se llamará a obtenerConfiguracion.recuperarProyeccion con los siguientes parámetros: 
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
		if (ConstantsProcesos.CTE_LEIDO_BTI.equals(modulo.getNombreServicio())) {
			varProyBTI = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_BTI, detalleBT.getFecCierre(), umic.getKey());
			copiarActualizadoProyBTI(subProcesoActual, lstDetalleCorrien, varProyBTI);			
		}else if(ConstantsProcesos.CTE_LEIDO_BTIPR.equals(getModuloCalculo().getNombreServicio())){
			varProyBTI = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_VAL_BTI_PROY, detalleBT.getFecCierre(), umic.getKey());
			copiarActualizadoProyBTI(subProcesoActual, lstDetalleCorrien, varProyBTI);	
		}else if(ConstantsProcesos.CTE_LEIDO_ROSSP.equals(getModuloCalculo().getNombreServicio())){
			varProyROSSP  = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_BT_ROSSP, detalleBT.getFecCierre(), umic.getKey());
			copiarActualizadoProyBTI(subProcesoActual, lstDetalleCorrien, varProyROSSP);
		}else if(ConstantsProcesos.CTE_LEIDO_ROSSPCSM.equals(getModuloCalculo().getNombreServicio())){
			varProyROSSP  = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_BT_ROSSPCSM, detalleBT.getFecCierre(), umic.getKey());
			copiarActualizadoProyBTI(subProcesoActual, lstDetalleCorrien, varProyROSSP);
		} else if (detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRVM)) {
			//Para el estrés de volatilidad de mortalidad recuperaremos los valores de BEL
			varProyBTI = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_BT_BEL, detalleBT.getFecCierre(), umic.getKey());
			copiarActualizadoProyBTI(subProcesoActual,  lstDetalleCorrien, varProyBTI);
		} else { 
			/**
			 * 8.7.2	invocarModulo(proyUmic(j))
						Cuando varModuloCalc es distinto de LEIDO, cómo se ha descrito anteriormente, se recorrerá 
						la estructura de proyecciones de la umic (ProyUmic)  desde  la primera proyección hasta  la última. 
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
						varFactor= varModuloCalc(ProyUmic , varPeriodoProyeccion  , fichaProceso, umic, btcUmic, codSubproceso)
	
						
						Si varModuloCalc es LEIDO llamamos a copiarActualizadoProyBTI
			*/
			if (detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMFE) ||
				detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMMI) ||
				detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLFE) ||
				detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLMI) ||
				detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCF) ||
				detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCI) ||
				detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRINC) ||
				detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRGTO) ||
				detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEN) ||
				detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEP) ||
				detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIN) ||
				detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIP) ||
				detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRANM)) {
				
				//Recuperamos el factor para BEL
				varProyBTI = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_BT_BEL, detalleBT.getFecCierre(), umic.getKey());
				varSCR = true;
			}
			/**
			 * Si codSubproceso = ‘PROY_COMI’ se comproborá previamente si existen fechas de calculo generadas ó  no, para ver si debe invocarse al módulo de cálculo ó no: 
					•	 Si no existe ninguna  proyección con fecha de cálculo y /ó fecha de devengo <>  null, NO se deberá  invocar al módulo correspondiente como en el resto de casos, sino que se hará: 
							proyUmic (j).bloqueCorriente.fpbAtcfin = 0
							proyUmic(j).bloqueCorriente.impFlujoActualizado = 0
					para todos los periodos de la umic, finalizando así el programa para la umic, y se almacenarán los datos invocando a  almacenarDatos.proyeccion
					•	Si  existe algúna proyección con fecha de cálculo y /ó fecha de devengo <>  null, se deberá  invocar al módulo correspondiente como en el resto de casos, tal y como se muestra a continuación.

			 */
			if (ConstantsModulos.CTE_PROY_COMI.equals(subProcesoActual)) {
				calcularCadaProyeccion = UtilModulos.comprobarProyeccionConFechas(lstDetalleCorrien, subProcesoActual);
			}
			
			final int lonDetalCorr = lstDetalleCorrien.size();
			
			for (int i = 0; i < lonDetalCorr; i++) {
				final BloqueCorriente bloqueProyeccion = lstDetalleCorrien.get(i).getBloqueBySubproceso(subProcesoActual);
				//TODO: Por el momento se pasa la lista completa, la fecha de efecto de calculo y el subproceso actual
				if (calcularCadaProyeccion && bloqueProyeccion.getImpFlujoNoAnulado() != null && bloqueProyeccion.getImpFlujoNoAnulado().signum() != 0) {
//				if (calcularCadaProyeccion) {
					if(ConstantsProcesos.CTE_ERROR_BT_ACT.equals(modulo.getNombreServicio())){		
						varProyBTI = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_BTI, detalleBT.getFecCierre(), umic.getKey());
						if(null == varProyBTI || varProyBTI.size() == 0){
							varProyBTI  = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_BTI_PROY, detalleBT.getFecCierre(), umic.getKey());
						}
						factor = varProyBTI.get(i).getBloqueBySubproceso(subProcesoActual).getFpbAtcfin();		
					}else if (varSCR){
						factor = varProyBTI.get(iteracion - 1).getBloqueBySubproceso(subProcesoActual).getFpbAtcfin();
					} else {
						factor = (BigDecimal) modulo.execute(lstDetalleCorrien, lstDetalleCorrien.get(iteracion - 1).getBloqueBySubproceso(subProcesoActual), 
								iteracion, fichaProceso.getFcalc(), umic, detalleBT, this.getMapVariables(), subProcesoActual);
					}
					
					if(null == factor){
						factor = BigDecimal.ZERO;
					}
					
					importeActualizado = bloqueProyeccion.getImpFlujoNoAnulado().multiply(factor);
				} else {
					factor = BigDecimal.ZERO;
					importeActualizado = BigDecimal.ZERO;
				}
				/**
				 * 8.7.3	calcularProyeccion(j)
					Una vez ejecutado el módulo de cálculo para la proyección j, escribiremos los datos de la salida del flujo probable actualizado a calcular 
					de dicha proyección de forma que: 
					
					Si varModuloCalc es distinto de LEIDO
						proyUmic (j).bloqueCorriente.fpbAtcfin = varFactor
						proyUmic(j).bloqueCorriente.impFlujoActualizado = proyUmic(j).bloqueCorriente. impFlujoNoAnulado* varFactor
					Si varModuloCalc es LEIDO
						proyUmic(j).bloqueCorriente.impFlujoActualizado = varActualizado(j)
						proyUmic(j).bloqueCorriente. fpbAtcfin = varfpbAtcfin(j)
				 */
				bloqueProyeccion.setFpbAtcfin(factor);
				bloqueProyeccion.setImpFlujoActualizado(importeActualizado.setScale(ConstantsFunciones.CTE_2, RoundingMode.HALF_DOWN));
				
				iteracion++;
			}
		}
		/**
		 * 8.7.4 almacenarDatos.proyeccion
			Una vez calculados el factor de probabilización (fpbAtcfin) y el importe probable actualizado para cada periodo de proyección, 
			se procederá a almacenar los datos de la corriente  calculados invocando al correspondiente servicio al efecto, almacenarDatos.proyeccion, 
			con la proyección calculada proyUmic.
		 */
		almacenarDatos.almacenarProyeccion(lstDetalleCorrien);
		
		if (ProgramaACT001.LOG.isTraceEnabled()) {
			ProgramaACT001.LOG.trace("Fin función << calcularImporteFlujoActualizado >> de la clase ProgramaACT001");
		}
	}
	
	/**
	 * Función encargada de obtener el importeActulizado y el fpbAtcFin de la iteración j, cuando el modulo tiene el valor LEIDO
	 * 
	 * @param subProcesoActual
	 * @param varProyBTI
	 */
	private void copiarActualizadoProyBTI(final String subProcesoActual, final List<DetalleCorriente> detallesCorriente, final List<DetalleCorriente> varProyBTI) {		
		final int sizeDetalle = detallesCorriente.size();
		for (int i = 0; i < sizeDetalle; i++) {
			final DetalleCorriente nuevoDetalle = detallesCorriente.get(i);
			final BloqueCorriente nuevoBloque = nuevoDetalle.getBloqueBySubproceso(subProcesoActual);

			final DetalleCorriente detalleBTI = varProyBTI.get(i);
			final BloqueCorriente bloqueBTI = detalleBTI.getBloqueBySubproceso(subProcesoActual);
			
			nuevoBloque.setFpbAtcfin(bloqueBTI.getFpbAtcfin());
			nuevoBloque.setImpFlujoActualizado(bloqueBTI.getImpFlujoActualizado());
		}
		
	}
	
	
	@Override
	public String getNombrePrograma() {
		return ConstantsFactorias.PROGRAMA_ACT001;
	}


}
