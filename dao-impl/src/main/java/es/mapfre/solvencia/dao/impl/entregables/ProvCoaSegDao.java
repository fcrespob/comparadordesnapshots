package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.entregables.ProvCoaSegKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.ProvCoaSeg;

public class ProvCoaSegDao extends DaoBaseSalidaCalculo implements Map<ProvCoaSegKey, ProvCoaSeg> {
	
	private static final String CACHE_NAME = "provcoaseg";

	public ProvCoaSegDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public ProvCoaSeg get(Object key) {
		if (key instanceof ProvCoaSegKey) {
			return (ProvCoaSeg) getCache().get(key);
		}
		return null;
	}

	@Override
	public ProvCoaSeg put(ProvCoaSegKey key, ProvCoaSeg value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public ProvCoaSeg remove(Object key) {
		return (ProvCoaSeg) this.getCache().remove(key);
	}

	@Override
	public Collection<ProvCoaSeg> values() {
		return (Collection<ProvCoaSeg>) this.getCache().values();
	}

	@Override
	public Set<ProvCoaSegKey> keySet() {
		return this.getCache().keySet();
	}
	
	@Override
	public Set<java.util.Map.Entry<ProvCoaSegKey, ProvCoaSeg>> entrySet() {
		return (Set<Entry<ProvCoaSegKey, ProvCoaSeg>>) this.getCache().entrySet();
	}
	
	@Override
	protected Comparator exportOrdered() {

		return new Comparator<ProvCoaSegKey>() {

			@Override
			public int compare(ProvCoaSegKey dc1, ProvCoaSegKey dc2) {
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