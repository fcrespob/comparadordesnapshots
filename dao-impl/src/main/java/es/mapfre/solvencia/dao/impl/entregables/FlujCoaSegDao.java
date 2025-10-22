package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.entregables.FlujCoaSegKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.FlujCoaSeg;

public class FlujCoaSegDao extends DaoBaseSalidaCalculo implements Map<FlujCoaSegKey, FlujCoaSeg> {
	
	private static final String CACHE_NAME = "flujcoaseg";

	public FlujCoaSegDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public FlujCoaSeg get(Object key) {
		if (key instanceof FlujCoaSegKey) {
			return (FlujCoaSeg) getCache().get(key);
		}
		return null;
	}

	@Override
	public FlujCoaSeg put(FlujCoaSegKey key, FlujCoaSeg value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public FlujCoaSeg remove(Object key) {
		return (FlujCoaSeg) this.getCache().remove(key);
	}

	@Override
	public Collection<FlujCoaSeg> values() {
		return (Collection<FlujCoaSeg>) this.getCache().values();
	}

	@Override
	public Set<FlujCoaSegKey> keySet() {
		return this.getCache().keySet();
	}
	
	@Override
	public Set<java.util.Map.Entry<FlujCoaSegKey, FlujCoaSeg>> entrySet() {
		return (Set<Entry<FlujCoaSegKey, FlujCoaSeg>>) this.getCache().entrySet();
	}
	
	@Override
	protected Comparator exportOrdered() {

		return new Comparator<FlujCoaSegKey>() {

			@Override
			public int compare(FlujCoaSegKey dc1, FlujCoaSegKey dc2) {
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