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
 * 
 * El programa FEC001 – ESTABLECIMIENTO DE FECHAS DE DEVENGO  Y PAGO POR CRITERO consiste en el establecimiento de las fechas de pago y devengo 
 * en función del criterio establecido en la tabla de opciones de generación de proyecciones.
 * Como resultado obtendremos una corriente de fechas de pago y devengo para cada periodo de proyección, 
 * donde posteriormente se acoplarán los cálculos de la proyección.
 * 
 * @author agonzalezgar
 * ProgramaFEC001. Esta clase sera la encargada de invocar al modulo de fecha de pago y devengo
 * y setear estas fechas en la estructura de salida correspondiente
 */
public class ProgramaFEC001 extends ProgramaGenerico {
	
	private static final Logger LOG = LoggerFactory.getLogger(ProgramaFEC001.class);
	
	@Override
	public void executeImpl(final Umic umic, final FichaProceso fichp, final DetalleBaseTecnica detalleBT, final List<DetalleCorriente> detallesCorriente, final String subProcesoActual)
			throws Solvencia2Excepcion {
		
		if (ProgramaFEC001.LOG.isTraceEnabled()) {
			ProgramaFEC001.LOG.trace("Inicio función << executeImpl >> de la clase ProgramaFEC001");
		}
		
		try {
			//llamamos al metodo calcularFechasPagYDev
			calcularFechasPagYDev(umic, fichp, detalleBT, detallesCorriente, subProcesoActual);
		} catch (Solvencia2Excepcion e) {
			ProgramaFEC001.LOG.error(e.getIncidencia().getTextoError(), e);
			UtilProcesos.exceptionControladaPrograma(detalleBT, umic, getNombrePrograma(), e);
		} catch (Exception e) {
			ProgramaFEC001.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ProgramaFEC001.LOG.isTraceEnabled()) {
			ProgramaFEC001.LOG.trace("Fin función << executeImpl >> de la clase ProgramaFEC001");
		}
	}
	
	/**
	 * Función encargada de encapsular las acciones necesarias para el calculo tanto de la fecha de pago como la fecha devengo
	 * 
	 * @param umic datos de la umic
	 * @param fichaProceso ficha proceso
	 * @param btc base tecnica
	 * @param subProcesoActual subproceso ejecutado
	 */
	private void calcularFechasPagYDev(final Umic umic, final FichaProceso fichaProceso, final DetalleBaseTecnica btc, final List<DetalleCorriente> detallesCorriente ,final String subProcesoActual) {
		//Variables locales
		Modulo moduloCalFechas = null;
		//Fin variables locales
		
		if (ProgramaFEC001.LOG.isTraceEnabled()) {
			ProgramaFEC001.LOG.trace("Inicio función << calcularFechasPagYDev >> de la clase ProgramaFEC001");
		}
		
		moduloCalFechas = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FECHAS_PD);
		moduloCalFechas.execute(umic, fichaProceso, btc, detallesCorriente, subProcesoActual, this.getMapVariables());
		
		if (ProgramaFEC001.LOG.isTraceEnabled()) {
			ProgramaFEC001.LOG.trace("Fin función << calcularFechasPagYDev >> de la clase ProgramaFEC001");
		}
		
	}
	
	
	@Override
	public String getNombrePrograma() {
		return ConstantsFactorias.PROGRAMA_FEC001;
	}

	
	
}
