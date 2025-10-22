package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.entregables.FlujInf3Key;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.FlujInf3;

public class FlujInf3Dao extends DaoBaseSalidaCalculo
		implements Map<FlujInf3Key, FlujInf3> {

	private static final String CACHE_NAME = "flujinf3";

	public FlujInf3Dao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public FlujInf3 get(Object key) {
		if (key instanceof FlujInf3Key) {
			return (FlujInf3) getCache().get(key);
		}
		return null;
	}

	@Override
	public FlujInf3 put(FlujInf3Key key, FlujInf3 value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public FlujInf3 remove(Object key) {
		return (FlujInf3) this.getCache().remove(key);
	}

	@Override
	public Collection<FlujInf3> values() {
		return (Collection<FlujInf3>) this.getCache().values();
	}

	@Override
	public Set<FlujInf3Key> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public Set<java.util.Map.Entry<FlujInf3Key, FlujInf3>> entrySet() {
		return (Set<Entry<FlujInf3Key, FlujInf3>>) this.getCache().entrySet();
	}

	@Override
	protected Comparator exportOrdered() {

		return new Comparator<FlujInf3Key>() {

			@Override
			public int compare(FlujInf3Key dc1, FlujInf3Key dc2) {
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