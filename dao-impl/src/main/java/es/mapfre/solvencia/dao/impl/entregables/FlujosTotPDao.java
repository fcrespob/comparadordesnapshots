package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.entregables.FlujosTotPKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.FlujosTotP;

public class FlujosTotPDao extends DaoBaseSalidaCalculo implements Map<FlujosTotPKey, FlujosTotP> {
	
	private static final String CACHE_NAME = "flujostotp";

	public FlujosTotPDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public FlujosTotP get(Object key) {
		if (key instanceof FlujosTotPKey) {
			return (FlujosTotP) getCache().get(key);
		}
		return null;
	}

	@Override
	public FlujosTotP put(FlujosTotPKey key, FlujosTotP value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public FlujosTotP remove(Object key) {
		return (FlujosTotP) this.getCache().remove(key);
	}

	@Override
	public Collection<FlujosTotP> values() {
		return (Collection<FlujosTotP>) this.getCache().values();
	}

	@Override
	public Set<FlujosTotPKey> keySet() {
		return this.getCache().keySet();
	}
	
	@Override
	public Set<java.util.Map.Entry<FlujosTotPKey, FlujosTotP>> entrySet() {
		return (Set<Entry<FlujosTotPKey, FlujosTotP>>) this.getCache().entrySet();
	}
	
	@Override
	protected Comparator exportOrdered() {

		return new Comparator<FlujosTotPKey>() {

			@Override
			public int compare(FlujosTotPKey dc1, FlujosTotPKey dc2) {
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