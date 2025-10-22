package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FlujosProbables;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalFlujoProyeccion;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

public class ModuloProvisionMatematicaFormulaCerrada implements Modulo {
	
	private static final Logger LOG = LoggerFactory.getLogger(ModuloProvisionMatematicaFormulaCerrada.class);

	@Override
	public String getNombreServicio() {
		return ConstantsFactorias.MODULO_FORMULA_CERRADA;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object execute(final Object... args) throws Solvencia2Excepcion {
		
		try {
			
			if (ModuloProvisionMatematicaFormulaCerrada.LOG.isTraceEnabled()) {
				ModuloProvisionMatematicaFormulaCerrada.LOG.trace("Inicio de execute en clase ModuloProvisionMatematicaFormulaCerrada");
			}
			
			//Recuperamos los datos que le pasaremos a la función ModuloCalculoFechasPagoYDevengo
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_UMIC_FECHAS];
			final FichaProceso fichaProceso = (FichaProceso) args[ConstantsModulos.PARAM_FIC_PROC_FECHAS];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_BTC_FECHAS];
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_DETALLE_FECHAS];
			final String subProcesoActual = (String) args[ConstantsModulos.PARAM_SUBRPROCESO_FECHAS];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_MAPA_VAR_FECHAS];
			
			
			//Invocamos a la funciónde calculo calcularProvMateFormulaCerrada
			calcularProvMateFormulaCerrada(umic, fichaProceso, btcUmic, proyUmic, subProcesoActual, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloProvisionMatematicaFormulaCerrada.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloProvisionMatematicaFormulaCerrada.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloProvisionMatematicaFormulaCerrada.LOG.isTraceEnabled()) {
			ModuloProvisionMatematicaFormulaCerrada.LOG.trace("Fin de execute en clase ModuloProvisionMatematicaFormulaCerrada");
		}
		
		return BigDecimal.ZERO;
	}
	
	
	/**
	 * Función encargada de obtener la provisión matemática de formula cerrada
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
	private void calcularProvMateFormulaCerrada(final Umic umic, final FichaProceso fichaProceso, final DetalleBaseTecnica detalleBT, 
			final List<DetalleCorriente> lstDetalleCorrien, final String subProcesoActual, final Map<String, Object> mapVariables) {
		//Variables locales
		final IObtenerConfiguracion obtConfi = FachadaServicios.getObtenerConfiguracion();
		final IAlmacenarDatos almacenarDatos = FachadaServicios.getAlmacenarDatos();
		FlujosProbables varConfProv = null;
		TotalFlujoProyeccion varProyMat = null;
		Modulo modulo = null;
		int iteracion = ConstantsFunciones.CTE_1;
		//Fin variables locales
		
		if (ModuloProvisionMatematicaFormulaCerrada.LOG.isTraceEnabled()) {
			ModuloProvisionMatematicaFormulaCerrada.LOG.trace("Inicio función << calcularProvMateFormulaCerrada >> de la clase ModuloProvisionMatematicaFormulaCerrada");
		}
		
		/**
		 *  En primer lugar se recuperará el módulo nominal y el terminal de la corriente  de provisión matemática 
		 *  por fórmula cerrada para la modalidad, garantía, prestación y base técnica de la umic.  
			Si el módulo nominal es NULO no deberá calcularse la provisión para el subproceso, terminando así el programa para la umic.
			Si el módulo nominal es NO NULO si deberá calcularse la provisión para el subproceso, tal y como se detalla a continuación.
			Para cada proyección, proyUmic,  se deberá calcular el importe para la corriente invocando al módulo de cálculo nominal anteriormente recuperado.
			Para ello, todos los módulos de cálculo que intervienen en la cuantía nominal  de la corriente deberán tener 
			los mismos parámetros de entrada/ salida, de cara a independizar la invocación del módulo correspondiente y hacerlo genérico. 
			Si el módulo de cálculo al que se invoque necesita de algún dato que no conste como parámetro de entrada al mismo, 
			será el propio módulo de cálculo el que se encargue de recuperar dicho dato. 
			A continuación, para cada periodo de proyección, el  resultado del módulo de cálculo resultante será escrito en la proyección. 
			Una vez procesados todos los puntos de la proyección, los mismos serán almacenados para su posterior recuperación 
			y generación de la salida  de la umic.
			Si se produce un error en algún punto del programa se registrará dicho error de la umic en el fichero de incidencias, 
			terminando el programa para dicha umic.
			
			10.7.1	obtenerConfiguracion.recuperarConfProv
			Obtiene el módulo nominal  y el terminal a ejecutar en el programa.   
			
			•	varConfProv = obtenerConfiguracion.recuperarConfProv con los parámetros: 
			o	 Umic.datosGenerales.kmodalidad
			o	Umic.datosGenerales.kgarantia
			o	Umic.datosGenerales.kprestacion
			o	btc.basecalc.ktipobt

		 */
		varConfProv = obtConfi.recuperarConfProv(umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), 
				umic.getDatosAdicionales().getPrestCal(), detalleBT.getBaseTec());
		
		
		/**
		 * Cómo se ha descrito anteriormente, se recorrerá la estructura de proyecciones de la umic (ProyUmic)  
		 * desde  la primera proyección hasta  la última. 
			Para cada proyección de la umic se invocará al módulo de cálculo correspondiente (varConfProv.provNominal). 
			Para invocar a dicho módulo varConfProv.provNominal se deberán enviar los parámetros de entrada: 
				•	ProyUmic 
				•	varPeriodoProyeccion  =  J -->  índice del periodo de proyección actual  que se está 
													rocesando en el bucle que recorre las proyecciones de la umic (PROY_UMIC)
				•	fecCalc
				•	umic
				•	btcUmic
				•	codSubproceso
				•	varConfProv.provTerminal
				
				De forma que: 
				•	varProyMat = varConfProv.provNominal (ProyUmic, varPeriodoProyeccion, fecCalc , umic, btcUmic, varConfProv.provTerminal
		 */
		if (null != varConfProv) {			
			final String moduloS = varConfProv.getProvNominal();
			
			if (ConstantsFactorias.MODULO_LEIDO_BTI.equals(moduloS)) {
				final IObtenerDatos servicioDatos = FachadaServicios.getObtenerDatos();
				final List<DetalleCorriente> varProyBTI  = servicioDatos.recuperarProyeccion(ConstantsModulos.CTE_BTI, detalleBT.getFecCierre(), umic.getKey());
				copiarProvNominalProyBTI(lstDetalleCorrien, varProyBTI);
			}else if(ConstantsFactorias.MODULO_LEIDO_BTIPR.equals(moduloS)){
				final IObtenerDatos servicioDatos = FachadaServicios.getObtenerDatos();
				final List<DetalleCorriente> varProyBTI  = servicioDatos.recuperarProyeccion(ConstantsModulos.CTE_VAL_BTI_PROY, detalleBT.getFecCierre(), umic.getKey());
				copiarProvNominalProyBTI(lstDetalleCorrien, varProyBTI);
			}else if(ConstantsFactorias.MODULO_LEIDO_ROSSPCSM.equals(moduloS)){
				final IObtenerDatos servicioDatos = FachadaServicios.getObtenerDatos();
				final List<DetalleCorriente> varProyBTI  = servicioDatos.recuperarProyeccion(ConstantsModulos.CTE_VAL_ROSSPCSM, detalleBT.getFecCierre(), umic.getKey());
				copiarProvNominalProyBTI(lstDetalleCorrien, varProyBTI);
			}else if(ConstantsFactorias.MODULO_LEIDO_ROSSP.equals(moduloS)){
				final IObtenerDatos servicioDatos = FachadaServicios.getObtenerDatos();
				final List<DetalleCorriente> varProyBTI  = servicioDatos.recuperarProyeccion(ConstantsModulos.CTE_VAL_ROSSP, detalleBT.getFecCierre(), umic.getKey());
				copiarProvNominalProyBTI(lstDetalleCorrien, varProyBTI);
			}else {
				final int longiDetalles = lstDetalleCorrien.size();
				modulo = FactoriaModulos.getModulo(moduloS);
				// Se crea un bloque corriente vacío simplemente para pasar las validaciones de los módulos
				BloqueCorriente bloqueCorrienteVacio = new BloqueCorriente();
				for (int i = 0; i < longiDetalles; i++) {
					//TODO: Por el momento se pasa la lista completa, la fecha de efecto de calculo y el subproceso actual
					final DetalleCorriente detalleCorr = lstDetalleCorrien.get(iteracion - 1);
					final TotalFlujoProyeccion totalFlujoProy = new TotalFlujoProyeccion();
					//detalleCorr.setTotalFlujoProyeccion(totalFlujoProy);
					varProyMat = (TotalFlujoProyeccion) modulo.execute(lstDetalleCorrien, bloqueCorrienteVacio, 
							iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables, subProcesoActual, varConfProv.getProvTerminal());
					
					/**
					 * 10.7.3	calcularProyeccion(j)
							Una vez ejecutado el módulo de cálculo para la proyección j, escribiremos los datos de la salida del flujo probable actualizado a calcular de dicha proyección de forma que: 
							
							proyUmic (j).totalFlujoProyeccion.provbtiproy= varProyMat.provbtiproy
							proyUmic (j).totalFlujoProyeccion.terminalAnterior= varProyMat.terminalAnterior
							proyUmic (j).totalFlujoProyeccion.terminalPosterior= varProyMat.terminalPosterior
					 */
					totalFlujoProy.setProvbtiproy(varProyMat.getProvbtiproy().setScale(ConstantsFunciones.CTE_2, RoundingMode.HALF_DOWN));
					totalFlujoProy.setTerminalAnterior(varProyMat.getTerminalAnterior().setScale(ConstantsFunciones.CTE_2, RoundingMode.HALF_DOWN));
					totalFlujoProy.setTerminalPosterior(varProyMat.getTerminalPosterior().setScale(ConstantsFunciones.CTE_2, RoundingMode.HALF_DOWN));
					detalleCorr.setTotalFlujoProyeccion(totalFlujoProy);
					
					iteracion++;
				}
				
			}
			
			
			/**
			 * 10.7.4	almacenarDatos.proyeccion
							Una vez calculado el importe provi para cada periodo de proyección, 
							se procederá a almacenar los datos de la corriente  calculados invocando al correspondiente servicio al efecto, 
							almacenarDatos.proyeccion, con la proyección calculada proyUmic.
			 */
			almacenarDatos.almacenarProyeccion(lstDetalleCorrien);
		}
		
		if (ModuloProvisionMatematicaFormulaCerrada.LOG.isTraceEnabled()) {
			ModuloProvisionMatematicaFormulaCerrada.LOG.trace("Fin función << calcularProvMateFormulaCerrada >> de la clase ModuloProvisionMatematicaFormulaCerrada");
		}
	}
	
	private void copiarProvNominalProyBTI(final List<DetalleCorriente> detallesCorriente, final List<DetalleCorriente> varProyBTI) {
		final int sizeDetalle = detallesCorriente.size();
		for (int i = 0; i < sizeDetalle; i++) {
			detallesCorriente.get(i).setTotalFlujoProyeccion(varProyBTI.get(i).getTotalFlujoProyeccion());
		}
	}
	
	
}
