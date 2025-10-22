package es.mapfre.solvencia.entregables;

import com.oracle.coherence.patterns.processing.task.TaskExecutionEnvironment;

import es.mapfre.solvencia.services.Servicio;

/**
 * Interfaz de entregables
 */
public interface Entregable extends Servicio {
	

	/**
	 * @return El nombre del entregable
	 */
	String getNombreEntregable();
	
	
	/**
	 * @param oEnvironment 
	 * 
	 */
	void execute(String kbasetec, TaskExecutionEnvironment oEnvironment);

}
