package es.mapfre.solvencia.ficheros;

import com.oracle.coherence.patterns.processing.task.ResumableTask;

public interface TareaDistribuida extends ResumableTask{
	
	String getNombre();

}
