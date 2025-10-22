package es.mapfre.solvencia.formulacion.programas.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FlujosProbables;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalFlujoProyeccion;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.programas.ProgramaFlujo;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.ConstantsProcesos;
import es.mapfre.solvencia.formulacion.util.UtilProcesos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * El programa PMRR01– ESTABLECIMIENTO DE PROVISION MATEMATICA RECURRENTE consiste en 
 * el de cálculo proyectado de la provisión matemática. Una vez establecida la provisión 
 * por flujos en la fecha de cálculo, que será la provisión en el momento 0, la provisión 
 * matemática en cada proyección será igual a la provisión anterior capitalizada por un 
 * periodo y teniendo en cuenta la probabilidad de supervivencia en ese periodo de tiempo 
 * concreto
 *
 */
public class ProgramaPMRR01 extends ProgramaFlujo {

	private static final Logger LOG = LoggerFactory.getLogger(ProgramaPMRR01.class);


	@Override
	protected String getTipoElemento() {
		return ConstantsProcesos.CTE_TIPO_ELEM_01;
	}

	@Override
	protected void executeImpl(final Umic umic, final FichaProceso fichaProceso,
			final DetalleBaseTecnica detalleBT, final List<DetalleCorriente> detallesCorriente, final String subProcesoActual) throws Solvencia2Excepcion {
		
		if (ProgramaPMRR01.LOG.isTraceEnabled()) {
			ProgramaPMRR01.LOG.trace("Inicio función << executeImpl >> de la clase ProgramaPMRR01");
		}
		
		try {
			//Invocamos al calculo de provision matematica recurrente
			calcularProvMateRecurrente(umic, fichaProceso, detalleBT, detallesCorriente, subProcesoActual);
		} catch (Solvencia2Excepcion e) {
			ProgramaPMRR01.LOG.error(e.getIncidencia().getTextoError(), e);
			UtilProcesos.exceptionControladaPrograma(detalleBT, umic, getNombrePrograma(), e);
		} catch (Exception e) {
			ProgramaPMRR01.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ProgramaPMRR01.LOG.isTraceEnabled()) {
			ProgramaPMRR01.LOG.trace("Fin función << executeImpl >> de la clase ProgramaPMRR01");
		}
		
	}
	
	/**
	 * Función encargada de obtener la provisión matemática recurrente
	 * 
	 * @param umic 
	 * 				Contiene los datos de la Umic que se está procesando.
	 * @param fichaProceso
	 * 				Contiene los datos del proceso necesarios para su ejecución. 
	 * @param detalleBT
	 * 				Contiene el detalle de la base técnica de cálculo para la umic.
	 * @param detallesCorriente
	 * 				Corriente de la umic.
	 * @param subProcesoActual
	 * 				Código el subproceso que se está ejecutando.
	 */
	private void calcularProvMateRecurrente(final Umic umic, final FichaProceso fichaProceso,
			final DetalleBaseTecnica detalleBT, final List<DetalleCorriente> detallesCorriente, final String subProcesoActual) {
		//Variables locales
		Modulo modFormRecurrente = null;
		TotalFlujoProyeccion totalFlujoProy;
		final IAlmacenarDatos almacenarDatos = FachadaServicios.getAlmacenarDatos();
		final IObtenerDatos obtenerDatos = FachadaServicios.getObtenerDatos();
		List<DetalleCorriente> varProyBTI = null;
		final IObtenerConfiguracion obtConfi = FachadaServicios.getObtenerConfiguracion();
		FlujosProbables varConfProv = null;

		
		//Fin variables locales
		
		if (ProgramaPMRR01.LOG.isTraceEnabled()) {
			ProgramaPMRR01.LOG.trace("Inicio función << calcularProvMateRecurrente >> de la clase ProgramaPMRR01");
		}
		
		varConfProv = obtConfi.recuperarConfProv(umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), 
				umic.getDatosAdicionales().getPrestCal(), detalleBT.getBaseTec());
		if (null != varConfProv) {

			final String moduloS = varConfProv.getProvNominal();
			if (ConstantsFactorias.MODULO_LEIDO_BTI.equals(moduloS)) {
				final IObtenerDatos servicioDatos = FachadaServicios.getObtenerDatos();
				varProyBTI = servicioDatos.recuperarProyeccion(ConstantsModulos.CTE_BTI, detalleBT.getFecCierre(),
						umic.getKey());
				copiarProvNominalProyBTI(detallesCorriente, varProyBTI);
			}else if(ConstantsFactorias.MODULO_LEIDO_BTIPR.equals(moduloS)){
				final IObtenerDatos servicioDatos = FachadaServicios.getObtenerDatos();
				varProyBTI = servicioDatos.recuperarProyeccion(ConstantsModulos.CTE_VAL_BTI_PROY, detalleBT.getFecCierre(),
						umic.getKey());
				copiarProvNominalProyBTI(detallesCorriente, varProyBTI);
			}else if(ConstantsFactorias.MODULO_LEIDO_ROSSPCSM.equals(moduloS)){
				final IObtenerDatos servicioDatos = FachadaServicios.getObtenerDatos();
				varProyBTI = servicioDatos.recuperarProyeccion(ConstantsModulos.CTE_VAL_ROSSPCSM, detalleBT.getFecCierre(),
						umic.getKey());
				copiarProvNominalProyBTI(detallesCorriente, varProyBTI);
			}else if(ConstantsFactorias.MODULO_LEIDO_ROSSP.equals(moduloS)){
				final IObtenerDatos servicioDatos = FachadaServicios.getObtenerDatos();
				varProyBTI = servicioDatos.recuperarProyeccion(ConstantsModulos.CTE_VAL_ROSSP, detalleBT.getFecCierre(),
						umic.getKey());
				copiarProvNominalProyBTI(detallesCorriente, varProyBTI);
			}else {

				if (moduloS.equals(ConstantsFactorias.MODULO_PMRR02)) {
					modFormRecurrente = FactoriaModulos.getModulo(moduloS);
				} else {
					modFormRecurrente = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_PMRR01);
				}

				final int lonDetalCorr = detallesCorriente.size();
								
				for (int i = lonDetalCorr - 1; i > -1; i--) {
					final BloqueCorriente bloqueProyeccion = detallesCorriente.get(i)
							.getBloqueBySubproceso(subProcesoActual);
					final DetalleCorriente detalleCorr = detallesCorriente.get(i);
					totalFlujoProy = (TotalFlujoProyeccion) modFormRecurrente.execute(detallesCorriente,
							bloqueProyeccion, i + 1, fichaProceso.getFcalc(), umic, detalleBT, this.getMapVariables(),
							subProcesoActual);
					TotalFlujoProyeccion tfp = detallesCorriente.get(i).getTotalFlujoProyeccion();
					tfp.setProvbtiproy(totalFlujoProy.getProvbtiproy());
					tfp.setTerminalAnterior(totalFlujoProy.getTerminalAnterior());
					tfp.setTerminalPosterior(totalFlujoProy.getTerminalPosterior());
					detalleCorr.setTotalFlujoProyeccion(tfp);
				}
			}
		}
		almacenarDatos.almacenarProyeccion(detallesCorriente);
		
		if (ProgramaPMRR01.LOG.isTraceEnabled()) {
			ProgramaPMRR01.LOG.trace("Inicio función << calcularProvMateRecurrente >> de la clase ProgramaPMRR01");
		}
		
	}

	@Override
	public String getNombrePrograma() {
		return ConstantsFactorias.PROGRAMA_PMRR01;
	}

	/**
	 * Función encargada de obtener el valor del importe de la provision de la proyección de la UMIC calculada anteriormente bajo la base técnica inicial (BTI)
	 * 
	 * @param subProcesoActual Subproceso actual
	 * @param detallesCorriente Corriente de la umic.
	 * @param varProyBTI Proyección de la UMIC calculada anteriormente bajo la base técnica inicial (BTI)
	 */
	private void copiarProvNominalProyBTI(final List<DetalleCorriente> detallesCorriente, final List<DetalleCorriente> varProyBTI) {
		final int sizeDetalle = detallesCorriente.size();
		for (int i = 0; i < sizeDetalle; i++) {
			detallesCorriente.get(i).setTotalFlujoProyeccion(varProyBTI.get(i).getTotalFlujoProyeccion());
		}
	}
	
	
}
