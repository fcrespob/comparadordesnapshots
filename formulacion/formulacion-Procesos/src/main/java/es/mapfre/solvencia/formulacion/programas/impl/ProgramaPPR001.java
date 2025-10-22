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
import es.mapfre.solvencia.formulacion.util.UtilProcesos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * El programa PPR001 – CALCULO PERIODOS DE PROYECCION POR CRITERO consiste en el establecimiento de los periodos de proyección  
 * para el cálculo posterior de las distintas corrientes.
 * Como resultado obtendremos una corriente de fechas de desde y hasta por cada periodo de proyección, 
 * donde posteriormente se acoplarán los cálculos de la proyección.
 *
 * @author rschacon
 *
 */
public class ProgramaPPR001 extends ProgramaGenerico {

	private static final Logger LOG = LoggerFactory.getLogger(ProgramaPPR001.class);

	@Override
	public void executeImpl(final Umic umic, final FichaProceso fichProceso, final DetalleBaseTecnica detalleBT, final List<DetalleCorriente> detallesCorriente, final String subProcesoActual) throws Solvencia2Excepcion {

		if (ProgramaPPR001.LOG.isTraceEnabled()) {
			ProgramaPPR001.LOG.trace("Inicio función << executeImpl >> de la clase ProgramaPPR001");
		}
		
		try {
			//Llamamos a la función obtenerPeriodos
			obtenerPeriodos(umic, fichProceso, detalleBT, detallesCorriente, subProcesoActual);
		} catch (Solvencia2Excepcion e) {
			ProgramaPPR001.LOG.error(e.getIncidencia().getTextoError(), e);
			UtilProcesos.exceptionControladaPrograma(detalleBT, umic, getNombrePrograma(), e);
		} catch (Exception e) {
			ProgramaPPR001.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ProgramaPPR001.LOG.isTraceEnabled()) {
			ProgramaPPR001.LOG.trace("Fin función << executeImpl >> de la clase ProgramaPPR001");
		}
		
	}
	
	/**
	 * @param umic
	 * 				Contiene los datos de la Umic que se está procesando.
	 * @param fichProceso 
	 * 				Contiene los datos del proceso necesarios para su ejecución. 
	 * @param detalleBT
	 * 				Contiene el detalle de la base técnica de cálculo para la umic.
	 * @param detallesCorriente
	 * 				Corriente de la umic.
	 * @param codSubproceso
	 * 			Código del subproceso que se está ejecutando
	 */
	private void obtenerPeriodos(final Umic umic, final FichaProceso fichProceso, final DetalleBaseTecnica detalleBT, final List<DetalleCorriente> detallesCorriente, final String subProcesoActual) {
		//Variables locales
		Modulo moduloPeriodo = null;
		//Fin variables locales
		
		//Obtenemos el módulo de periodos a recuperar
		moduloPeriodo = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_PERIODOS);

		moduloPeriodo.execute(detalleBT, umic, fichProceso, detalleBT.getFecCierre(), detallesCorriente, subProcesoActual,this.getMapVariables());
	}
	
	@Override
	public String getNombrePrograma() {
		return ConstantsFactorias.PROGRAMA_PPR001;
	}

}
