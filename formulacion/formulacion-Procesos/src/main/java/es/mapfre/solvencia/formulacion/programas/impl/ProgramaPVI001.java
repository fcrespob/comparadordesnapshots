package es.mapfre.solvencia.formulacion.programas.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.programas.ProgramaGenerico;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.UtilProcesos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * El programa PVI001– ESTABLECIMIENTO DE IMPORTE PROVI consiste en el cálculo concepto 
 * resultante de la aplicación de una actualización financiera muy similar a la de la “Cuantía Actualizada”, 
 * pero alterando el procedimiento y las fechas a las que se actualizan dichos importes. 
 * Como resultado obtendremos una corriente importes actualizados para cada periodo de 
 * proyección de la corriente.
 *  
 * @author rschacon
 *
 */
public class ProgramaPVI001 extends ProgramaGenerico {

	private static final Logger LOG = LoggerFactory.getLogger(ProgramaPVI001.class);

	@Override
	protected void executeImpl(final Umic umic, final FichaProceso fichaProceso, final DetalleBaseTecnica detalleBT,
			final List<DetalleCorriente> detallesCorriente, final String subProcesoActual)
			throws Solvencia2Excepcion {
		
		if (ProgramaPVI001.LOG.isTraceEnabled()) {
			ProgramaPVI001.LOG.trace("Inicio función << executeImpl >> de la clase ProgramaPVI001");
		}
		
		try {
			
			final IObtenerConfiguracion servicioConfiguracion = FachadaServicios
					.getObtenerConfiguracion();
		
			final String varModuloCalc = servicioConfiguracion.recuperarModulo(umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), 
					umic.getDatosAdicionales().getPrestCal(), detalleBT.getBaseTec(), subProcesoActual, ConstantsModulos.CTE_TIPO_ELEM_05);
			
			if (ConstantsModulos.CTE_S.equals(varModuloCalc)) {				
				//Invocamos a la función que realiza el calculo el importe provi
				efectuarCalculoProvi(umic, fichaProceso, detalleBT, detallesCorriente, subProcesoActual);
			}
		} catch (Solvencia2Excepcion e) {
			ProgramaPVI001.LOG.error(e.getIncidencia().getTextoError(), e);
			UtilProcesos.exceptionControladaPrograma(detalleBT, umic, getNombrePrograma(), e);
		} catch (Exception e) {
			ProgramaPVI001.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ProgramaPVI001.LOG.isTraceEnabled()) {
			ProgramaPVI001.LOG.trace("Fin función << executeImpl >> de la clase ProgramaPVI001");
		}
		
	}
	
	/**
	 * Función que realiza el calculo del importe Provi
	 * 
	 * @param umic Contiene los datos de la Umic que se está procesando.
	 * @param fichaProceso Contiene los datos del proceso necesarios para su ejecución. 
	 * @param detalleBT Contiene el detalle de la base técnica de cálculo para la umic.
	 * @param detallesCorriente Corriente de la umic.
	 * @param subProcesoActual Código el subproceso que se está ejecutando.
	 */
	private void efectuarCalculoProvi(final Umic umic, final FichaProceso fichaProceso, final DetalleBaseTecnica detalleBT,
			final List<DetalleCorriente> detallesCorriente, final String subProcesoActual) {
		//Variables locales
		Modulo moduloCalcProvi = null;
		//Fin variables locales
		
		//Obtenemos el módulo de importe provi a recuperar
		moduloCalcProvi = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_IMP_PROVI);
		moduloCalcProvi.execute(umic, fichaProceso, detalleBT, detallesCorriente, subProcesoActual, this.getMapVariables());
	}
	
	@Override
	public String getNombrePrograma() {
		return ConstantsFactorias.PROGRAMA_PVI001;
	}

}
