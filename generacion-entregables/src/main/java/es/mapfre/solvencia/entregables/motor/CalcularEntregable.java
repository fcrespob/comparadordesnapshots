package es.mapfre.solvencia.entregables.motor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.coherence.patterns.processing.task.ResumableTask;
import com.oracle.coherence.patterns.processing.task.TaskExecutionEnvironment;
import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.entregables.Entregable;
import es.mapfre.solvencia.entregables.services.FactoriaEntregables;

@Portable
public class CalcularEntregable implements ResumableTask {

	private static Logger LOG = LoggerFactory.getLogger(CalcularEntregable.class);
	
	@PortableProperty(0)
	private String nombreEntregable;
	@PortableProperty(1)
	private String kbasetec;
	
	public String getNombreEntregable() {
		return nombreEntregable;
	}

	public String getKbasetec() {
		return kbasetec;
	}

	public CalcularEntregable () {
		super();
	}
	
	public CalcularEntregable (String nombreEntregable, String kbasetec) {
		this.nombreEntregable = nombreEntregable;
		this.kbasetec = kbasetec;		
	}
	
	
	@Override
	public Object run(TaskExecutionEnvironment oEnvironment) {
		
		LOG.info("Calculando entregable {} para BT {}",nombreEntregable,kbasetec);
		
		Entregable entregable = FactoriaEntregables.getEntregable(nombreEntregable);
	
		Long time1 = System.currentTimeMillis();
		
		entregable.execute(kbasetec, oEnvironment);
		
		Long time2 = System.currentTimeMillis();
		
		//LOG.info("Entregable {} calculado en {} ms",nombreEntregable,time2-time1);
		
		return null;

	}

	@Override
	public String toString() {
		return this.kbasetec + "-" + this.nombreEntregable;
	}
	
}
