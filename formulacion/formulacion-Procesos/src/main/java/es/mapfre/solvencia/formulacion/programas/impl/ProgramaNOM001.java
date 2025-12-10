package es.mapfre.solvencia.formulacion.programas.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.OpcionesGeneracion;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.programas.ProgramaFlujo;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.ConstantsProcesos;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.formulacion.util.UtilProcesos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * El programa NOM001 – CALCULO DE LA CUANTIA NOMINAL se encargará de calcular la cuantía nominal de la modalidad / garantía por cada periodo de proyección.
 * Como resultado obtendremos una corriente de importes nominales para cada periodo de proyección de la corriente. 
 *
 * @author rschacon
 *
 */
public class ProgramaNOM001 extends ProgramaFlujo {
	
	private static final Logger LOG = LoggerFactory.getLogger(ProgramaNOM001.class);


	@Override
	protected String getTipoElemento() {
		return ConstantsProcesos.CTE_TIPO_ELEM_01;
	}

	@Override
	protected void executeImpl(final Umic umic, final FichaProceso fichaProceso,
			final DetalleBaseTecnica detalleBT, final List<DetalleCorriente> detallesCorriente, final String subProcesoActual) throws Solvencia2Excepcion {
		
		if (ProgramaNOM001.LOG.isTraceEnabled()) {
			ProgramaNOM001.LOG.trace("Inicio función << executeImpl >> de la clase ProgramaNOM001");
		}
		
		try {
			//Llamamos a la función que calcula el flujo nominal
			calcularFlujoNominal(umic, fichaProceso, detalleBT, detallesCorriente, subProcesoActual);
		}  catch (Solvencia2Excepcion e) {
			ProgramaNOM001.LOG.error(e.getIncidencia().getTextoError(), e);
			UtilProcesos.exceptionControladaPrograma(detalleBT, umic, getNombrePrograma(), e);
		} catch (Exception e) {
			ProgramaNOM001.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ProgramaNOM001.LOG.isTraceEnabled()) {
			ProgramaNOM001.LOG.trace("Fin función << executeImpl >> de la clase ProgramaNOM001");
		}
		
	}
	
	/**
	 * 
	 * Función encargada de encapsular el procedimiento para obtener importe del flujo nominal de cada proyección.
	 * 
	 * @param umic 
	 * 				Contiene los datos de la Umic que se está procesando.
	 * @param fichaProceso
	 * 				Contiene los datos del proceso necesarios para su ejecución. 
	 * @param detalleBT
	 * 				Contiene el detalle de la base técnica de cálculo para la umic.
	 * @param lstDetalleCorrien
	 * 				Corriente de la umic.
	 * @param subProcesoActual
	 * 				Código el subproceso que se está ejecutando.
	 */
	private void calcularFlujoNominal(final Umic umic, final FichaProceso fichaProceso,
			final DetalleBaseTecnica detalleBT, final List<DetalleCorriente> lstDetalleCorrien, final String subProcesoActual) {
		//Variables locales
		Modulo modNominal = null;
		final IAlmacenarDatos almacenarDatos = FachadaServicios.getAlmacenarDatos();
		final IObtenerDatos obtenerDatos = FachadaServicios.getObtenerDatos();
		BigDecimal varNominal = BigDecimal.ZERO;
		List<DetalleCorriente> varProyBTI = null;
		List<DetalleCorriente> varProyROSSP = null;
		List<DetalleCorriente> varProyNIIF17 = null;
		boolean calcularCadaProyeccion = true;
		String proceso;
		//Fin variables locales
		
		if (ProgramaNOM001.LOG.isTraceEnabled()) {
			ProgramaNOM001.LOG.trace("Inicio función << calcularFlunoNominal >> de la clase ProgramaNOM001");
		}
		
		/**
			En primer lugar se recuperará el módulo de cálculo del importe nominal correspondiente al subproceso que se está ejecutando 
			para la modalidad, garantía, prestación y base técnica de la umic.  
			En segundo lugar se recuperará la proyección calculada hasta el momento para la umic y el subproceso que se está ejecutando.  
			Para cada proyección recuperada, proyUmic,  se deberá calcular el importe nominal para la corriente. 
			El cálculo de este importe nominal se realizará invocando al módulo de cálculo anteriormente recuperado, si el módulo es distinto de LEIDO.
			Para ello, todos los módulos de cálculo que intervienen en la cuantía nominal de la corriente deberán tener los mismos 
			parámetros de entrada/ salida, de cara a independizar la invocación del módulo correspondiente y hacerlo genérico. 
			Si el módulo de cálculo al que se invoque necesita de algún dato que no conste como parámetro de entrada al mismo, 
			será el propio módulo de cálculo el que se encargue de recuperar dicho dato. 
			A continuación, para cada periodo de proyección, el  resultado del módulo de cálculo resultante será aplicado 
			al importe correspondiente de la corriente y escrito en la proyección. 
			Si el módulo de cálculo es LEIDO, se deberá recuperar el detalle de la proyección de la UMIC 
			calculado anteriormente para la UMIC con la base técnica BTI, estableciendo para el importe nominal el mismo importe nominal calculado en BTI.
			Una vez procesados todos los puntos de la proyección, los mismos serán almacenados 
			para su posterior recuperación y generación de la salida  de la umic.
			Si se produce un error en algún punto del programa se registrará dicho error de la umic en el fichero de incidencias, 
			terminando el programa para dicha umic.
			
		 * 6.7.1	obtenerConfiguracion.recuperarModulo
			Obtiene el módulo de cálculo a ejecutar en el programa.   
			
			•	varModuloCalc = obtenerConfiguracion.recuperarModulo con los parámetros: 
			o	Umic.datosGenerales.kmodalidad
			o	Umic.datosGenerales.kgarantia
			o	Umic.datosGenerales.kprestacion
			o	fichaProceso.ktipobt
			o	codSubproceso
			o	tipoElemento    = 01
		 */
		// Esta configuración se recupera en la clase padre ProgramaFlujo
		/**
		 * 6.7.4	obtenerConfiguracion.recuperarProyeccion
				Cuando varModuloCalc es LEIDO, se deberá recuperar al proyección de la UMIC calculada anteriormente bajo la base técnica inicial (BTI). 
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

		 */
	
		if((fichaProceso.getCtipobt().equals(ConstantsModulos.CTE_VAL_MULTI4NB) || fichaProceso.getCtipobt().equals(ConstantsModulos.CTE_VAL_MULTI4C) || fichaProceso.getCtipobt().equals(ConstantsModulos.CTE_VAL_MULTI8) || fichaProceso.getCtipobt().equals(ConstantsModulos.CTE_VAL_MULTI8NB)) && !detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_BTI_PROY) && !detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_BTI) && !detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_ROSSP) && !detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_ROSSPCSM) && !detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_ROSSPTI) && !detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_ROSSPTE) && !detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_ROSSPGA) && !detalleBT.getBaseTec().equals(ConstantsModulos.CTE_BT_NIIF17)){
			varProyNIIF17  = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_BT_NIIF17, detalleBT.getFecCierre(), umic.getKey());
			copiarNominalProyBTI(subProcesoActual, lstDetalleCorrien, varProyNIIF17);
		}else if (ConstantsProcesos.CTE_LEIDO_BTI.equals(getModuloCalculo().getNombreServicio())) {
			if(subProcesoActual.equals(ConstantsModulos.CTE_PROY_GTOS)){
				detalleBT.setGtorosspPrima(umic.getBti().getPgastgesin2I());
				detalleBT.setGtorosspCap(umic.getBti().getPgastgesin1I());
				detalleBT.setGtorosspProv(umic.getBti().getPgastgesin3I());
				almacenarDatos.almacenarDetalleBaseTecnica(detalleBT);
			}
			varProyBTI  = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_BTI, detalleBT.getFecCierre(), umic.getKey());
			copiarNominalProyBTI(subProcesoActual, lstDetalleCorrien, varProyBTI);
		}else if(ConstantsProcesos.CTE_LEIDO_BTIPR.equals(getModuloCalculo().getNombreServicio())){
			if(subProcesoActual.equals(ConstantsModulos.CTE_PROY_GTOS)){
				detalleBT.setGtorosspPrima(umic.getBti().getPgastgesin2I());
				detalleBT.setGtorosspCap(umic.getBti().getPgastgesin1I());
				detalleBT.setGtorosspProv(umic.getBti().getPgastgesin3I());
				almacenarDatos.almacenarDetalleBaseTecnica(detalleBT);
			}
			varProyBTI  = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_VAL_BTI_PROY, detalleBT.getFecCierre(), umic.getKey());
			copiarNominalProyBTI(subProcesoActual, lstDetalleCorrien, varProyBTI);
		}else if(ConstantsProcesos.CTE_LEIDO_ROSSP.equals(getModuloCalculo().getNombreServicio())){
			varProyROSSP  = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_BT_ROSSP, detalleBT.getFecCierre(), umic.getKey());
			copiarNominalProyBTI(subProcesoActual, lstDetalleCorrien, varProyROSSP);
		}else if(ConstantsProcesos.CTE_LEIDO_ROSSPCSM.equals(getModuloCalculo().getNombreServicio())){
			varProyROSSP  = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_BT_ROSSPCSM, detalleBT.getFecCierre(), umic.getKey());
			copiarNominalProyBTI(subProcesoActual, lstDetalleCorrien, varProyROSSP);
		} else if (detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRVM)) {
			//Para el estrés de volatilidad de mortalidad recuperaremos los valores de BEL
			varProyBTI = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_BT_BEL, detalleBT.getFecCierre(), umic.getKey());
			copiarNominalProyBTI(subProcesoActual,  lstDetalleCorrien, varProyBTI);
		}else if (((detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMFE) ||
				    detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMMI) ||
				    detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLFE) ||
				    detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLMI) ||
				    detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCF) ||
				    detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCI) ||
				    detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRINC)) && 
						(!subProcesoActual.equals(ConstantsModulos.CTE_PROY_PRIMA) && 
						 !subProcesoActual.equals(ConstantsModulos.CTE_PROY_RESC))) ||
				(detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRGTO) &&
					!subProcesoActual.equals(ConstantsModulos.CTE_PROY_GTOS)) ||
				detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEN) ||
				detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEP) ||
				detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIN) ||
				detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIP) ||
				detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRANM)) {
			varProyBTI = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_BT_BEL, detalleBT.getFecCierre(), umic.getKey());
			copiarNominalProyBTI(subProcesoActual, lstDetalleCorrien, varProyBTI);	
		} else if ((detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17MFE)) ||
				(detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17GTO) &&
						!subProcesoActual.equals(ConstantsModulos.CTE_PROY_GTOS)) ||
					detalleBT.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17AEN)) {
				varProyBTI = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_BT_NIIF17, detalleBT.getFecCierre(), umic.getKey());
				copiarNominalProyBTI(subProcesoActual, lstDetalleCorrien, varProyBTI);	
		}
		
		/**
		 * Cuando varModuloCalc es distinto de LEIDO, cómo se ha descrito anteriormente, se recorrerá la estructura de proyecciones de la umic (ProyUmic)  
		 * desde  la primera proyección hasta  la última. 
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
				varNominal= varModuloCalc(ProyUmic , varPeriodoProyeccion  , fichaProceso.fcalc, umic, btcUmic, codSubproceso)

				
				De forma que: 
				varNominal= varModuloCalc(ProyUmic , varPeriodoProyeccion  , fichaProceso.fcalc, umic, btcUmic, codSubproceso)

		 */
		
		else if(fichaProceso.getKsistema().equals(ConstantesSolvencia.CTE_SISTEMA)
				&& fichaProceso.getKprotecnico().equals(ConstantesSolvencia.CTE_PROCESO_TECNICO_SWCOBROCOMISIONES)
				&& fichaProceso.getKuejecucion().equals(ConstantesSolvencia.CTE_UNID_EJEC_SWCOBROCOMISIONES) && ConstantsModulos.CTE_PROY_COMI.equals(subProcesoActual)){
			
			final int lonDetalCorr = lstDetalleCorrien.size();
			modNominal = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_COM001);
			
			/**
			 * Si codSubproceso = ‘PROY_COMI’ se comproborá previamente si existen fechas de calculo generadas ó  no, para ver si debe invocarse al módulo de cálculo ó no: 
					•	 Si no existe ninguna  proyección con fecha de cálculo y /ó fecha de devengo <>  null, NO se deberá  invocar al módulo correspondiente como en el resto de casos, sino que se hará: 
							proyUmic(j).bloqueCorriente.impFlujoNominal = 0
 							para todos los periodos de la umic, finalizando así el programa para la umic, y se almacenarán los datos invocando a  almacenarDatos.proyeccion
					•	Si  existe algúna proyección con fecha de cálculo y /ó fecha de devengo <>  null, se deberá  invocar al módulo correspondiente como en el resto de casos, tal y como se muestra a continuación.

			 */
			if (ConstantsModulos.CTE_PROY_COMI.equals(subProcesoActual)) {
				calcularCadaProyeccion = UtilModulos.comprobarProyeccionConFechas(lstDetalleCorrien, subProcesoActual);
			}
			for (int i = 0; i < lonDetalCorr; i++) {
				final BloqueCorriente bloqueProyeccion = lstDetalleCorrien.get(i).getBloqueBySubproceso(subProcesoActual);
				
				Boolean swCobroCom = true;
				if (calcularCadaProyeccion) {
					varNominal = (BigDecimal) modNominal.execute(lstDetalleCorrien, bloqueProyeccion, 
							i + 1, fichaProceso.getFcalc(), umic, detalleBT, this.getMapVariables(), subProcesoActual,swCobroCom);
				} else {
					varNominal = BigDecimal.ZERO;
				}
			
				/* TAR00220734 11/2016 */
				/**
				 * En caso de ser una proyección de GASTOS, una vez obtenido el
				 * valor nominal, se prorratea en el número de días que tenga la
				 * proyección en caso de ser la última iteración (no repetida)
				 */
				if(fichaProceso.getKsistema().equals(ConstantesSolvencia.CTE_SISTEMA) && fichaProceso.getKprotecnico().equals(ConstantesSolvencia.CTE_PROYECTO_TECNICO) && fichaProceso.getKuejecucion().equals(ConstantesSolvencia.CTE_UMIC_EJECUCION))						
					proceso = ConstantesSolvencia.CTE_CHECKPRIMA;
				else
					proceso = ConstantesSolvencia.CTE_PROYECCION;
				if (ConstantsModulos.CTE_PROY_GTOS.equals(subProcesoActual)) {
					// prorrateamos todo salvo el primer periodo
					if (i > 0) {
						varNominal = this.prorratearValorNominal(umic,
								lstDetalleCorrien.get(i).getFechaDesde(), lstDetalleCorrien.get(i).getFechaHasta(),
								varNominal, proceso);
					}
				}
				/* FIN TAR00220734 11/2016 */
				
				/**
				 * Una vez ejecutado el módulo de cálculo para la proyección j, escribiremos los datos de la salida del flujo nominal 
				 * a calcular de dicha proyección de forma que: 
	
						proyUmic (j).bloqueCorriente.impFlujoNominal = varNominal
						
						o
						
						proyUmic(j).bloqueCorriente.impFlujoNominal = varNominal(j) (Si el modulo es LEIDO)
				 */
				if(null == varNominal){
					bloqueProyeccion.setImpFlujoNominal(BigDecimal.ZERO);
				}else{
					bloqueProyeccion.setImpFlujoNominal(varNominal.setScale(ConstantsFunciones.CTE_2, RoundingMode.HALF_DOWN));
				}
			}
			
			
		}else {
			final int lonDetalCorr = lstDetalleCorrien.size();
			modNominal = getModuloCalculo();
			
			/**
			 * Si codSubproceso = ‘PROY_COMI’ se comproborá previamente si existen fechas de calculo generadas ó  no, para ver si debe invocarse al módulo de cálculo ó no: 
					•	 Si no existe ninguna  proyección con fecha de cálculo y /ó fecha de devengo <>  null, NO se deberá  invocar al módulo correspondiente como en el resto de casos, sino que se hará: 
							proyUmic(j).bloqueCorriente.impFlujoNominal = 0
 							para todos los periodos de la umic, finalizando así el programa para la umic, y se almacenarán los datos invocando a  almacenarDatos.proyeccion
					•	Si  existe algúna proyección con fecha de cálculo y /ó fecha de devengo <>  null, se deberá  invocar al módulo correspondiente como en el resto de casos, tal y como se muestra a continuación.

			 */
			if (ConstantsModulos.CTE_PROY_COMI.equals(subProcesoActual)) {
				calcularCadaProyeccion = UtilModulos.comprobarProyeccionConFechas(lstDetalleCorrien, subProcesoActual);
			}
			for (int i = 0; i < lonDetalCorr; i++) {
				final BloqueCorriente bloqueProyeccion = lstDetalleCorrien.get(i).getBloqueBySubproceso(subProcesoActual);
				
				if (calcularCadaProyeccion) {
					
					if (modNominal == FactoriaModulos.getModulo(ConstantsFactorias.MODULO_COM001)) {
						Boolean swCobroCom = false;
						varNominal = (BigDecimal) modNominal.execute(lstDetalleCorrien, bloqueProyeccion, 
								i + 1, fichaProceso.getFcalc(), umic, detalleBT, this.getMapVariables(), subProcesoActual,swCobroCom);
					}else {
						varNominal = (BigDecimal) modNominal.execute(lstDetalleCorrien, bloqueProyeccion, 
								i + 1, fichaProceso.getFcalc(), umic, detalleBT, this.getMapVariables(), subProcesoActual);
					}
				} else {
					varNominal = BigDecimal.ZERO;
				}
			
				/* TAR00220734 11/2016 */
				/**
				 * En caso de ser una proyección de GASTOS, una vez obtenido el
				 * valor nominal, se prorratea en el número de días que tenga la
				 * proyección en caso de ser la última iteración (no repetida)
				 */
				if(fichaProceso.getKsistema().equals(ConstantesSolvencia.CTE_SISTEMA) && fichaProceso.getKprotecnico().equals(ConstantesSolvencia.CTE_PROYECTO_TECNICO) && fichaProceso.getKuejecucion().equals(ConstantesSolvencia.CTE_UMIC_EJECUCION))						
					proceso = ConstantesSolvencia.CTE_CHECKPRIMA;
				else
					proceso = ConstantesSolvencia.CTE_PROYECCION;
				if (ConstantsModulos.CTE_PROY_GTOS.equals(subProcesoActual)) {
					// prorrateamos todo salvo el primer periodo
					if (i > 0) {
						varNominal = this.prorratearValorNominal(umic,
								lstDetalleCorrien.get(i).getFechaDesde(), lstDetalleCorrien.get(i).getFechaHasta(),
								varNominal, proceso);
					}
				}
				/* FIN TAR00220734 11/2016 */
				
				/**
				 * Una vez ejecutado el módulo de cálculo para la proyección j, escribiremos los datos de la salida del flujo nominal 
				 * a calcular de dicha proyección de forma que: 
	
						proyUmic (j).bloqueCorriente.impFlujoNominal = varNominal
						
						o
						
						proyUmic(j).bloqueCorriente.impFlujoNominal = varNominal(j) (Si el modulo es LEIDO)
				 */
				if(null == varNominal){
					bloqueProyeccion.setImpFlujoNominal(BigDecimal.ZERO);
				}else{
					bloqueProyeccion.setImpFlujoNominal(varNominal.setScale(ConstantsFunciones.CTE_2, RoundingMode.HALF_DOWN));
				}
			}
		}
		
		/**
		 * 6.7.4 - almacenarDatos.proyeccion
						Una vez calculado el importe nominal para cada periodo de proyección, se procederá a almacenar los datos de la corriente  calculados 
						invocando al correspondiente servicio al efecto, almacenarDatos.proyeccion, con la proyección calculada proyUmic.
		 */
		//Almacenamos la proyeccion
		almacenarDatos.almacenarProyeccion(lstDetalleCorrien);
		
		if (ProgramaNOM001.LOG.isTraceEnabled()) {
			ProgramaNOM001.LOG.trace("Fin función << calcularFlunoNominal >> de la clase ProgramaNOM001");
		}
		
	}
	
	/**
	 * Función encargada de obtener el valor del importe moninal de la proyección de la UMIC calculada anteriormente bajo la base técnica inicial (BTI)
	 * 
	 * @param subProcesoActual Subproceso actual
	 * @param detallesCorriente Corriente de la umic.
	 * @param varProyBTI Proyección de la UMIC calculada anteriormente bajo la base técnica inicial (BTI)
	 */
	private void copiarNominalProyBTI(final String subProcesoActual, final List<DetalleCorriente> detallesCorriente, final List<DetalleCorriente> varProyBTI) {
		/**
		 * Una vez recuperada la proyección BTI calculada para la UMIC (varProyBTI) se recorrerá la estructura de proyecciones de la umic (ProyUmic)  
		 * desde  la primera proyección hasta  la última. 
			Para cada proyección de la umic se asignarán los valores correspondientes de la corriente, dependiendo del subproceso que haya invocado 
			al programa de forma que: 
			•	Si codSubproceso = ‘PROY_VIDA’
					-	varNominal (j) = varProyBTI(j).corrienteVida.impFlujoNominal
			•	Si codSubproceso = ‘PROY_FALL’
					-	varNominal (j) = varProyBTI(j).corrienteFallecimiento.impFlujoNominal
			•	Si codSubproceso = ‘PROY_INVA’
					-	varNominal (j) = varProyBTI(j).corrienteComplementario.impFlujoNominal
			•	Si codSubproceso = ‘PROY_PRIMA’
					-	varNominal (j) = varProyBTI(j).corrientePrima.impFlujoNominal  
			•	Si codSubproceso = ‘PROY_GTOS’
					-	varNominal (j) = varProyBTI(j).corrienteGasto.impFlujoNominal
			•	Si codSubproceso = ‘PROY_RESC’
					-	varNominal (j) = varProyBTI(j).corrienteRescate.impFlujoNominal
			•	Si codSubproceso = ‘PROY_COMI’
					-	varNominal (j) = varProyBTI(j).corrienteComision.impFlujoNominal
		 */
		
		final int sizeDetalle = detallesCorriente.size();
		for (int i = 0; i < sizeDetalle; i++) {
			final DetalleCorriente nuevoDetalle = detallesCorriente.get(i);
			final BloqueCorriente nuevoBloque = nuevoDetalle.getBloqueBySubproceso(subProcesoActual);

			final DetalleCorriente detalleBTI = varProyBTI.get(i);
			final BloqueCorriente bloqueBTI = detalleBTI.getBloqueBySubproceso(subProcesoActual);
			
			nuevoBloque.setImpFlujoNominal(bloqueBTI.getImpFlujoNominal());
		}
	}
	
	@Override
	public String getNombrePrograma() {
		return ConstantsFactorias.PROGRAMA_NOM001;
	}
	
	/* INI TAR00220734 11/2016*/
	private BigDecimal prorratearValorNominal(final Umic umic,
			final Timestamp fechaDesde, final Timestamp fechaHasta, final BigDecimal varNominal, final String proceso) {

		final int dias = UtilFechas.diferenciasDeFechas(fechaDesde, fechaHasta);
		final int diasMesCompleto = UtilFechas.diferenciasDeFechas(fechaDesde,
				this.calcularFechaFinalMesCompleto(fechaDesde, fechaHasta, umic, proceso));

		// Llevamos a cabo el prorrateo
		BigDecimal varNominalProrrateado;
		if (dias == ConstantsFunciones.CTE_0){
			// Si es el último periodo duplicado
			varNominalProrrateado = BigDecimal.ZERO;
		}else if (dias != diasMesCompleto) {
			varNominalProrrateado = (varNominal.multiply(new BigDecimal(dias))).divide(new BigDecimal(diasMesCompleto),
					ConstantsFunciones.MATH_CONTEXT);
		} else {
			varNominalProrrateado = varNominal;
		}
		return varNominalProrrateado;

	}

	private Timestamp calcularFechaFinalMesCompleto(final Timestamp fechaDesde, final Timestamp fechaHasta,
			final Umic umic, String proceso) {

		final Timestamp fechaHastaCompleta;
		final IObtenerConfiguracion obtConfig = FachadaServicios.getObtenerConfiguracion();
		OpcionesGeneracion opGeneracion = obtConfig.recuperarOpcionesGeneracionPrestCal(
				umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), 
				umic.getDatosGenerales().getKprestacion(),umic.getDatosAdicionales().getPrestCal());

		if(proceso == ConstantesSolvencia.CTE_CHECKPRIMA) {
			fechaHastaCompleta = UtilFechas.incrMeses(fechaDesde, null, ConstantsFunciones.CTE_1, false);
		}
		else {
			fechaHastaCompleta = this.calcularFechaFinalMesCompleto(opGeneracion.getPeriodicidad(),
					fechaDesde, umic.getFechas().getFechastarenova(), umic.getFechas().getFecefecfin());
		}
		return fechaHastaCompleta;
	}

	private Timestamp calcularFechaFinalMesCompleto(final String periodicidad, final Timestamp fechaDesde,
			final Timestamp fechaRenovacion, final Timestamp fechaVencimiento) {

		Timestamp fechaHastaCompleta;

		if (ConstantsModulos.CTE_VAL_MNAT.equals(periodicidad)) {
			fechaHastaCompleta = UtilFechas.incrMeses(fechaDesde, null, ConstantsFunciones.CTE_1, false);
		} else if (ConstantsModulos.CTE_VAL_MENSU.equals(periodicidad)) {
			fechaHastaCompleta = UtilFechas.incrMeses(fechaDesde, null, ConstantsFunciones.CTE_1, true);
		} else if (ConstantsModulos.CTE_VAL_RENO.equals(periodicidad)) {
			int diaRenovacion = UtilFechas.getDia(fechaRenovacion);
			if (UtilFechas.getDia(UtilFechas.getUltimoDiaDelMes(fechaRenovacion)) == diaRenovacion) {
				// si el día de renovación coincide con el último día de mes,
				// las proyecciones se harán a fin de mes
				fechaHastaCompleta = UtilFechas.incrMeses(fechaDesde, null, ConstantsFunciones.CTE_1, true);
			} else {
				if (UtilFechas.getMes(fechaDesde) == ConstantsFunciones.CTE_2
						&& ArrayUtils.contains(new Integer[] { ConstantsFunciones.CTE_29, ConstantsFunciones.CTE_30,
								ConstantsFunciones.CTE_31 }, diaRenovacion)) {
					// Si es el mes de la fecha de inicio es febrero, si la el
					// dia
					// de renovación es 29, 30 o 31 hay que intervenir
					Calendar cal = Calendar.getInstance();
					cal.clear();
					cal.set(Calendar.YEAR, UtilFechas.getAnio(fechaDesde));
					cal.set(Calendar.MONTH, UtilFechas.getMes(fechaDesde));
					cal.set(Calendar.DAY_OF_MONTH, diaRenovacion);
					fechaHastaCompleta = new Timestamp(cal.getTimeInMillis());
				} else if (ArrayUtils
						.contains(
								new Integer[] { ConstantsFunciones.CTE_4, ConstantsFunciones.CTE_6,
										ConstantsFunciones.CTE_9, ConstantsFunciones.CTE_11 },
								UtilFechas.getMes(fechaDesde))
						&& diaRenovacion == 31) {
					// si es un mes de 30 dias y la fecha de renovación es de
					// 31,
					// hay que intervenir
					Calendar cal = Calendar.getInstance();
					cal.clear();
					cal.set(Calendar.YEAR, UtilFechas.getAnio(fechaDesde));
					cal.set(Calendar.MONTH, UtilFechas.getMes(fechaDesde));
					cal.set(Calendar.DAY_OF_MONTH, diaRenovacion);
					fechaHastaCompleta = new Timestamp(cal.getTimeInMillis());
				} else if (UtilFechas.getDia(fechaDesde) != diaRenovacion) {
					if (UtilFechas.getDia(fechaDesde) < diaRenovacion) {
						// No hay que sumar un mes
						Calendar cal = Calendar.getInstance();
						cal.clear();
						cal.set(Calendar.YEAR, UtilFechas.getAnio(fechaDesde));
						cal.set(Calendar.MONTH, UtilFechas.getMes(fechaDesde) - ConstantsFunciones.CTE_1);
						cal.set(Calendar.DAY_OF_MONTH, diaRenovacion);
						fechaHastaCompleta = new Timestamp(cal.getTimeInMillis());
					} else {
						// Hay que sumar un mes
						Calendar cal = Calendar.getInstance();
						cal.clear();
						cal.set(Calendar.YEAR, UtilFechas.getAnio(fechaDesde));
						cal.set(Calendar.MONTH, UtilFechas.getMes(fechaDesde));
						cal.set(Calendar.DAY_OF_MONTH, diaRenovacion);
						fechaHastaCompleta = new Timestamp(cal.getTimeInMillis());
					}
				} else {
					fechaHastaCompleta = UtilFechas.incrMeses(fechaDesde, null, ConstantsFunciones.CTE_1, false);
				}
			}
		} else if (ConstantsModulos.CTE_VAL_REVEN.equals(periodicidad)) {
			int diaVencimiento = UtilFechas.getDia(fechaVencimiento);
			if (UtilFechas.getDia(UtilFechas.getUltimoDiaDelMes(fechaVencimiento)) == diaVencimiento) {
				// si el día de renovación coincide con el último día de mes,
				// las proyecciones se harán a fin de mes
				fechaHastaCompleta = UtilFechas.incrMeses(fechaDesde, null, ConstantsFunciones.CTE_1, true);
			} else {
				if (UtilFechas.getMes(fechaDesde) == ConstantsFunciones.CTE_2
						&& ArrayUtils.contains(new Integer[] { ConstantsFunciones.CTE_29, ConstantsFunciones.CTE_30,
								ConstantsFunciones.CTE_31 }, diaVencimiento)) {
					// Si es el mes de la fecha de inicio es febrero, si la el
					// dia
					// de renovación es 29, 30 o 31 hay que intervenir
					Calendar cal = Calendar.getInstance();
					cal.clear();
					cal.set(Calendar.YEAR, UtilFechas.getAnio(fechaDesde));
					cal.set(Calendar.MONTH, UtilFechas.getMes(fechaDesde));
					cal.set(Calendar.DAY_OF_MONTH, diaVencimiento);
					fechaHastaCompleta = new Timestamp(cal.getTimeInMillis());
				} else if (ArrayUtils
						.contains(
								new Integer[] { ConstantsFunciones.CTE_4, ConstantsFunciones.CTE_6,
										ConstantsFunciones.CTE_9, ConstantsFunciones.CTE_11 },
								UtilFechas.getMes(fechaDesde))
						&& diaVencimiento == 31) {
					// si es un mes de 30 dias y la fecha de renovación es de
					// 31,
					// hay que intervenir
					Calendar cal = Calendar.getInstance();
					cal.clear();
					cal.set(Calendar.YEAR, UtilFechas.getAnio(fechaDesde));
					cal.set(Calendar.MONTH, UtilFechas.getMes(fechaDesde));
					cal.set(Calendar.DAY_OF_MONTH, diaVencimiento);
					fechaHastaCompleta = new Timestamp(cal.getTimeInMillis());
				} else if (UtilFechas.getDia(fechaDesde) != diaVencimiento) {
					if (UtilFechas.getDia(fechaDesde) < diaVencimiento) {
						// No hay que sumar un mes
						Calendar cal = Calendar.getInstance();
						cal.clear();
						cal.set(Calendar.YEAR, UtilFechas.getAnio(fechaDesde));
						cal.set(Calendar.MONTH, UtilFechas.getMes(fechaDesde) - ConstantsFunciones.CTE_1);
						cal.set(Calendar.DAY_OF_MONTH, diaVencimiento);
						fechaHastaCompleta = new Timestamp(cal.getTimeInMillis());
					} else {
						// Hay que sumar un mes
						Calendar cal = Calendar.getInstance();
						cal.clear();
						cal.set(Calendar.YEAR, UtilFechas.getAnio(fechaDesde));
						cal.set(Calendar.MONTH, UtilFechas.getMes(fechaDesde));
						cal.set(Calendar.DAY_OF_MONTH, diaVencimiento);
						fechaHastaCompleta = new Timestamp(cal.getTimeInMillis());
					}
				} else {
					fechaHastaCompleta = UtilFechas.incrMeses(fechaDesde, null, ConstantsFunciones.CTE_1, false);
				}
			}
		} else {
			fechaHastaCompleta = null;
		}

		return fechaHastaCompleta;
	}
	/* FIN TAR00220734 11/2016*/
}
