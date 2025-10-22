package es.mapfre.solvencia.dao.impl.formulacion;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.maestro.PeriodosFallKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.formulacion.PeriodosFall;;

public class PeriodosFallDao extends DaoBase implements Map<PeriodosFallKey, List<PeriodosFall>> {

	private static final String CACHE_NAME = "periodos-fall"; 
	
	public PeriodosFallDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PeriodosFall> get(Object key) {
		if (!(key instanceof PeriodosFallKey)) {
			return null;
		} else {
			return (List<PeriodosFall>) this.getCache().get(key);
		}
	}
	
	@Override
	public List<PeriodosFall> put(PeriodosFallKey key, List<PeriodosFall> value) {
		this.getCache().put(key, value);
		 return value;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PeriodosFall> remove(Object key) {
		return (List<PeriodosFall>) this.getCache().remove(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<PeriodosFallKey> keySet() {
		return this.getCache().keySet();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<List<PeriodosFall>> values() {
		return (Collection<List<PeriodosFall>>) this.getCache().values();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<java.util.Map.Entry<PeriodosFallKey, List<PeriodosFall>>> entrySet() {
		return (Set<Entry<PeriodosFallKey, List<PeriodosFall>>>) this.getCache().entrySet();
	}

}
