package es.mapfre.solvencia.coherence.jmx;

import java.util.Date;

public interface SolvenciaInfoMBean {
	Integer getFichasProcesadas();
	Integer getFichasAProcesar();
	Date getHoraComienzo();
	Date getHoraFinEstimada();
	String getFichaEnEjecucion();
	Date getHoraComienzoFichaActual();
	Date getHoraFinEstimadaFichaActual();
	Date getHoraServidor();
	Integer getUMICPendienteFicha();
	Integer getUMICProcesadasFicha();
	Double getAvanceFicha();
	String getProcesoActual();
}
