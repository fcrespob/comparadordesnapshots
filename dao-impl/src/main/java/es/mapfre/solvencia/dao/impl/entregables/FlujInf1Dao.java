package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.entregables.FlujCoaSegKey;
import es.mapfre.solvencia.coherence.keys.entregables.FlujInf1Key;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.FlujInf1;

public class FlujInf1Dao extends DaoBaseSalidaCalculo implements Map<FlujInf1Key, FlujInf1> {
	
	private static final String CACHE_NAME = "flujinf1";

	public FlujInf1Dao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public FlujInf1 get(Object key) {
		if (key instanceof FlujInf1Key) {
			return (FlujInf1) getCache().get(key);
		}
		return null;
	}

	@Override
	public FlujInf1 put(FlujInf1Key key, FlujInf1 value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public FlujInf1 remove(Object key) {
		return (FlujInf1) this.getCache().remove(key);
	}

	@Override
	public Collection<FlujInf1> values() {
		return (Collection<FlujInf1>) this.getCache().values();
	}

	@Override
	public Set<FlujInf1Key> keySet() {
		return this.getCache().keySet();
	}
	
	@Override
	public Set<java.util.Map.Entry<FlujInf1Key, FlujInf1>> entrySet() {
		return (Set<Entry<FlujInf1Key, FlujInf1>>) this.getCache().entrySet();
	}
	
	@Override
	protected Comparator exportOrdered() {

		return new Comparator<FlujInf1Key>() {

			@Override
			public int compare(FlujInf1Key dc1, FlujInf1Key dc2) {
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