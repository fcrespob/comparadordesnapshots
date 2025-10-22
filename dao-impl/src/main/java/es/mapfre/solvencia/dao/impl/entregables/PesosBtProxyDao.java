package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.entregables.PesosBtProxyKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.PesosBtProxy;

public class PesosBtProxyDao extends DaoBaseSalidaCalculo implements Map<PesosBtProxyKey, PesosBtProxy>{

	private static final String CACHE_NAME = "pesosbtproxy";

	public PesosBtProxyDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public Set<Entry<PesosBtProxyKey, PesosBtProxy>> entrySet() {
		return (Set<Entry<PesosBtProxyKey, PesosBtProxy>>) this.getCache().entrySet();
	}

	@Override
	public PesosBtProxy get(Object key) {
		if (key instanceof PesosBtProxyKey) {
			return (PesosBtProxy) getCache().get(key);
		}
		return null;
	}

	@Override
	public Set<PesosBtProxyKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public PesosBtProxy put(PesosBtProxyKey key, PesosBtProxy value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public PesosBtProxy remove(Object key) {
		return (PesosBtProxy) this.getCache().remove(key);
	}

	@Override
	public Collection<PesosBtProxy> values() {
		return (Collection<PesosBtProxy>) this.getCache().values();
	}

	protected Comparator exportOrdered() {
		return new Comparator<PesosBtProxyKey>() {
			@Override
			public int compare(PesosBtProxyKey dc1, PesosBtProxyKey dc2) {
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
