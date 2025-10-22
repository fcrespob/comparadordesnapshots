package es.mapfre.solvencia.entregables;

import com.oracle.coherence.patterns.processing.task.TaskExecutionEnvironment;

public class EntregableTest implements Entregable {

	private static final String NOMBRE_ENTREGABLE = "TEST";
	
	@Override
	public String getNombreServicio() {
		return getNombreEntregable();
	}

	@Override
	public String getNombreEntregable() {
		return NOMBRE_ENTREGABLE;
	}

	@Override
	public void execute(String kbasetec, TaskExecutionEnvironment oEnvironment) {
		// TODO Auto-generated method stub
		
	}



}
