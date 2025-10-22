package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.entregables.FlujCoaSegKey;
import es.mapfre.solvencia.coherence.keys.entregables.FlujInf2Key;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.FlujInf2;

public class FlujInf2Dao extends DaoBaseSalidaCalculo implements Map<FlujInf2Key, FlujInf2> {
	
	private static final String CACHE_NAME = "flujinf2";

	public FlujInf2Dao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public FlujInf2 get(Object key) {
		if (key instanceof FlujInf2Key) {
			return (FlujInf2) getCache().get(key);
		}
		return null;
	}

	@Override
	public FlujInf2 put(FlujInf2Key key, FlujInf2 value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public FlujInf2 remove(Object key) {
		return (FlujInf2) this.getCache().remove(key);
	}

	@Override
	public Collection<FlujInf2> values() {
		return (Collection<FlujInf2>) this.getCache().values();
	}

	@Override
	public Set<FlujInf2Key> keySet() {
		return this.getCache().keySet();
	}
	
	@Override
	public Set<java.util.Map.Entry<FlujInf2Key, FlujInf2>> entrySet() {
		return (Set<Entry<FlujInf2Key, FlujInf2>>) this.getCache().entrySet();
	}
	
	@Override
	protected Comparator exportOrdered() {

		return new Comparator<FlujInf2Key>() {

			@Override
			public int compare(FlujInf2Key dc1, FlujInf2Key dc2) {
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