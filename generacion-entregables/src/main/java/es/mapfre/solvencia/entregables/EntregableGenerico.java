package es.mapfre.solvencia.entregables;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.coherence.patterns.processing.task.TaskExecutionEnvironment;

import es.mapfre.solvencia.entregables.motor.ProgresoEntregables;

/**
 * Implementacion de la interfaz de Entregables
 * 
 */
public abstract class EntregableGenerico implements Entregable {

	private static final Logger LOG = LoggerFactory.getLogger(EntregableGenerico.class);
	
	public String getNombreServicio() {
		return getNombreEntregable();
	}
	
	TaskExecutionEnvironment oEnvironment;
	ProgresoEntregables progreso;
	
	protected void initProgress(TaskExecutionEnvironment oEnvironment) {
		this.oEnvironment = oEnvironment;
		progreso = new ProgresoEntregables();
		progreso.setTotales(-1);
		progreso.setTerminadas(0);
		progreso.setTimestamp(System.currentTimeMillis());
		oEnvironment.reportProgress(progreso);
	}
	
	protected void setProgress(int totales, int terminadas) {
		progreso.setTotales(totales);
		progreso.setTerminadas(terminadas);
		progreso.setTimestamp(System.currentTimeMillis());
		oEnvironment.reportProgress(progreso);
		//LOG.info("Avance: {}/{} -> {}%", terminadas, totales, progreso.getAvance());
	}

	@Override
	public void execute(String kbasetec, TaskExecutionEnvironment oEnvironment) {
		ProgresoEntregables progreso = new ProgresoEntregables();
		
		if (EntregableGenerico.LOG.isTraceEnabled()) {
			EntregableGenerico.LOG.trace("Inicio función << execute >> de la clase EntregableGenerico");
		}
		
		progreso.setTotales(0);
		progreso.setTerminadas(0);
		progreso.setTimestamp(System.currentTimeMillis());
		oEnvironment.reportProgress(progreso);
		
		if (EntregableGenerico.LOG.isTraceEnabled()) {
			EntregableGenerico.LOG.trace("Fin función << execute >> de la clase EntregableGenerico");
		}
	}	

}
