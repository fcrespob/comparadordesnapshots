package es.mapfre.solvencia.formulacion.programas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ValidacionesProgramas;
import es.mapfre.solvencia.programas.Programa;

/**
 * Implementacion de la interfaz del Programa
 * Contendra un mapa de variables para almacenar los valores de cada una de las variables
 * que se necesiten en los modulos para los diferentes periodos que se calculen
 * @author agonzalezgar
 *
 */
public abstract class ProgramaGenerico implements Programa {
	
	private static final Logger LOG = LoggerFactory.getLogger(ProgramaGenerico.class);
	
	/**
	 * Array con todas las variables de apoyo de los modulos que se ejecutaran en el subproceso
	 */
	private Map<String, Object> mapVariables = new HashMap<String, Object>();

	public Map<String, Object> getMapVariables() {
		return this.mapVariables;
	}
	
	@Override
	public void execute(final Umic umic, final FichaProceso fichaProceso, final DetalleBaseTecnica detBaseTecnica, final List<DetalleCorriente> detallesCorriente, final String subProcesoActual) throws Solvencia2Excepcion {
		
		if (ProgramaGenerico.LOG.isTraceEnabled()) {
			ProgramaGenerico.LOG.trace("Inicio función << execute >> de la clase ProgramaGenerico");
		}
		
		try {
			//Validamos los parametros de entrada
			ValidacionesProgramas.validarParamEntrada(umic, fichaProceso, detBaseTecnica, detallesCorriente, subProcesoActual);
			
			executeImpl(umic, fichaProceso, detBaseTecnica, detallesCorriente, subProcesoActual);
		} catch (Solvencia2Excepcion se) {
			ProgramaGenerico.LOG.error(se.getIncidencia().getTextoError(), se);
			throw se;
		} catch (Throwable th) {
			ProgramaGenerico.LOG.error(th.getMessage(), th);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, th);
		}
		
		if (ProgramaGenerico.LOG.isTraceEnabled()) {
			ProgramaGenerico.LOG.trace("Fin función << execute >> de la clase ProgramaGenerico");
		}
	}
	
	/**
	 * Metodo con la implementacion del execute
	 * @param umic
	 * @param fp
	 * @param btc
	 * @param subProcesoActual
	 * @throws Solvencia2Excepcion
	 */
	protected abstract void executeImpl(final Umic umic, final FichaProceso fichaProceso, final DetalleBaseTecnica detBaseTecnica, final List<DetalleCorriente> detallesCorriente, final String subProcesoActual)
			throws Solvencia2Excepcion;
	
	public String getNombreServicio() {
		return getNombrePrograma();
	}
}
