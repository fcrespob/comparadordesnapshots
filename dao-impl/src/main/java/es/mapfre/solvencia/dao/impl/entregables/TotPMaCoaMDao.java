package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.entregables.TotPMaCoaMKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.TotPMaCoaM;

public class TotPMaCoaMDao extends DaoBaseSalidaCalculo implements Map<TotPMaCoaMKey, TotPMaCoaM> {
	
	
	private static final String CACHE_NAME = "totpmacoam";
	
	public TotPMaCoaMDao() {
		super();
		super.setCacheName(CACHE_NAME);				
	}

	
	@Override
	public TotPMaCoaM get(Object key) {
		if (key instanceof TotPMaCoaMKey) {
			return (TotPMaCoaM) getCache().get(key);
		}
		return null;
	}

	@Override
	public TotPMaCoaM put(TotPMaCoaMKey key, TotPMaCoaM value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public TotPMaCoaM remove(Object key) {
		return (TotPMaCoaM) this.getCache().remove(key);
	}

	@Override
	public Collection<TotPMaCoaM> values() {
		return (Collection<TotPMaCoaM>) this.getCache().values();
	}

	@Override
	public Set<TotPMaCoaMKey> keySet() {
		return this.getCache().keySet();
	}
	
	@Override
	public Set<java.util.Map.Entry<TotPMaCoaMKey, TotPMaCoaM>> entrySet() {
		return (Set<Entry<TotPMaCoaMKey, TotPMaCoaM>>) this.getCache().entrySet();
	}
	
	@Override
	protected Comparator exportOrdered() {

		return new Comparator<TotPMaCoaMKey>() {

			@Override
			public int compare(TotPMaCoaMKey dc1, TotPMaCoaMKey dc2) {
				if (dc1 == null && dc2 != null) {
					return 1;
				} else if (dc1 != null) {
					return dc1.compareTo(dc2);
				} else if (dc1 == null) {
					return -1;
				}
				return 0;
			}
			
		};
	}
	
	
}