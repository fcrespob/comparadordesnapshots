package es.mapfre.solvencia.formulacion.programas.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FlujosProbables;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.programas.ProgramaFlujo;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsProcesos;
import es.mapfre.solvencia.formulacion.util.UtilProcesos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * El programa PMC001– ESTABLECIMIENTO DE PROVISION MATEMATICA POR FORMULA CERRADA consiste en el cálculo del importe de provisión por fórmula cerrada. 
 * Como resultado obtendremos una corriente importes para cada periodo de proyección de la corriente. Si la provisión tiene también terminal, 
 * entonces se calcularán también el terminal anterior y el posterior de la corriente para cada periodo de proyección.
 * @author rschacon
 *
 */
public class ProgramaPMC001 extends ProgramaFlujo {

	private static final Logger LOG = LoggerFactory.getLogger(ProgramaPMC001.class);


	@Override
	protected String getTipoElemento() {
		return ConstantsProcesos.CTE_TIPO_ELEM_01;
	}

	@Override
	protected void executeImpl(final Umic umic, final FichaProceso fichaProceso,
			final DetalleBaseTecnica detalleBT, final List<DetalleCorriente> detallesCorriente, final String subProcesoActual) throws Solvencia2Excepcion {
		
		if (ProgramaPMC001.LOG.isTraceEnabled()) {
			ProgramaPMC001.LOG.trace("Inicio función << executeImpl >> de la clase ProgramaPMC001");
		}
		
		try {
			//Invocamos al calculo de provision matematica de formula cerrada
			calcularProvMateFormulaCerrada(umic, fichaProceso, detalleBT, detallesCorriente, subProcesoActual);
		} catch (Solvencia2Excepcion e) {
			ProgramaPMC001.LOG.error(e.getIncidencia().getTextoError(), e);
			UtilProcesos.exceptionControladaPrograma(detalleBT, umic, getNombrePrograma(), e);
		} catch (Exception e) {
			ProgramaPMC001.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ProgramaPMC001.LOG.isTraceEnabled()) {
			ProgramaPMC001.LOG.trace("Fin función << executeImpl >> de la clase ProgramaPMC001");
		}
		
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
	 * @param detallesCorriente
	 * 				Corriente de la umic.
	 * @param subProcesoActual
	 * 				Código el subproceso que se está ejecutando.
	 */
	private void calcularProvMateFormulaCerrada(final Umic umic, final FichaProceso fichaProceso,
			final DetalleBaseTecnica detalleBT, final List<DetalleCorriente> detallesCorriente, final String subProcesoActual) {
		//Variables locales
		Modulo modFormCerrada = null;
		IObtenerConfiguracion obtConfi = FachadaServicios.getObtenerConfiguracion();
		FlujosProbables varConfProv;
		String modulo;
		//Fin variables locales
		
		if (ProgramaPMC001.LOG.isTraceEnabled()) {
			ProgramaPMC001.LOG.trace("Inicio función << calcularFechasPagYDev >> de la clase ProgramaPMC001");
		}
		
		modFormCerrada = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FORMULA_CERRADA);
		
		varConfProv = obtConfi.recuperarConfProv(umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), 
				umic.getDatosAdicionales().getPrestCal(), detalleBT.getBaseTec());
		
		modulo = varConfProv.getProvNominal();
		
		if (!modulo.equals(ConstantsFactorias.MODULO_PMRR01)){
			modFormCerrada.execute(umic, fichaProceso, detalleBT, detallesCorriente, subProcesoActual, this.getMapVariables());
		}
		
		if (ProgramaPMC001.LOG.isTraceEnabled()) {
			ProgramaPMC001.LOG.trace("Inicio función << calcularFechasPagYDev >> de la clase ProgramaPMC001");
		}
		
	}

	@Override
	public String getNombrePrograma() {
		return ConstantsFactorias.PROGRAMA_PMC001;
	}

}
