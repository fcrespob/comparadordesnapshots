package es.mapfre.solvencia.coherence.jmx;

import java.util.Date;
import java.util.GregorianCalendar;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

public class SolvenciaInfo implements SolvenciaInfoMBean {

	private NamedCache getCache() {
		return CacheFactory.getCache("monitorizacionCache");
	}

	@Override
	public Integer getFichasAProcesar() {
		return (Integer) getCache().get(MonitorizacionConstants.NUM_FICHAS);
	}

	@Override
	public Integer getFichasProcesadas() {
		return (Integer) getCache().get(MonitorizacionConstants.NUM_FICHAS_PROCESADAS);
	}

	@Override
	public Date getHoraComienzo() {
		return (Date) getCache().get(MonitorizacionConstants.HORA_COMIENZO);
	}

	@Override
	public Date getHoraFinEstimada() {
		return (Date) getCache().get(MonitorizacionConstants.HORA_FIN_ESTIMADA);
	}

	@Override
	public Date getHoraServidor() {
		return GregorianCalendar.getInstance().getTime();
	}

	@Override
	public String getFichaEnEjecucion() {
		return (String) getCache().get(MonitorizacionConstants.FICHA_EJECUCION);
	}

	@Override
	public Date getHoraComienzoFichaActual() {
		return (Date) getCache().get(MonitorizacionConstants.HORA_COMIENZO_FICHA_EJECUCION);
	}

	@Override
	public Date getHoraFinEstimadaFichaActual() {
		return (Date) getCache().get(MonitorizacionConstants.HORA_FIN_ESTIMADA_FICHA_EJECUCION);
	}

	@Override
	public Integer getUMICPendienteFicha() {
		return (Integer) getCache().get(MonitorizacionConstants.UMIC_PENDIENTE_FICHA);
	}

	@Override
	public Double getAvanceFicha() {
		return (Double) getCache().get(MonitorizacionConstants.AVANCE_FICHA);
	}

	@Override
	public String getProcesoActual() {
		return (String) getCache().get(MonitorizacionConstants.PROCESO_ACTUAL);
	}

	@Override
	public Integer getUMICProcesadasFicha() {
		return (Integer) getCache().get(MonitorizacionConstants.UMIC_PROCESADAS_FICHA);
	}
	
}
