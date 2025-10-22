package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.entregables.PesosBtKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.PesosBt;

public class PesosBtDao extends DaoBaseSalidaCalculo implements Map<PesosBtKey, PesosBt>{

	private static final String CACHE_NAME = "pesosbt";

	public PesosBtDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public Set<Entry<PesosBtKey, PesosBt>> entrySet() {
		return (Set<Entry<PesosBtKey, PesosBt>>) this.getCache().entrySet();
	}

	@Override
	public PesosBt get(Object key) {
		if (key instanceof PesosBtKey) {
			return (PesosBt) getCache().get(key);
		}
		return null;
	}

	@Override
	public Set<PesosBtKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public PesosBt put(PesosBtKey key, PesosBt value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public PesosBt remove(Object key) {
		return (PesosBt) this.getCache().remove(key);
	}

	@Override
	public Collection<PesosBt> values() {
		return (Collection<PesosBt>) this.getCache().values();
	}

	protected Comparator exportOrdered() {
		return new Comparator<PesosBtKey>() {
			@Override
			public int compare(PesosBtKey dc1, PesosBtKey dc2) {
				if (dc1 == null && dc2 != null) {
					return 1;
				} else if (dc1 != null) {
					return dc1.compareTo(dc2);
				} else {
					return -1;
				}
			}
		};
	}
	
}
