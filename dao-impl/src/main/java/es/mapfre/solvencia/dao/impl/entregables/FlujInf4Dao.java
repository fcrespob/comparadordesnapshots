package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.entregables.FlujInf3Key;
import es.mapfre.solvencia.coherence.keys.entregables.FlujInf4Key;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.FlujInf3;
import es.mapfre.solvencia.dominio.entregables.FlujInf4;

public class FlujInf4Dao extends DaoBaseSalidaCalculo
		implements Map<FlujInf4Key, FlujInf4> {

	private static final String CACHE_NAME = "flujinf4";

	public FlujInf4Dao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public FlujInf4 get(Object key) {
		if (key instanceof FlujInf4Key) {
			return (FlujInf4) getCache().get(key);
		}
		return null;
	}

	@Override
	public FlujInf4 put(FlujInf4Key key, FlujInf4 value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public FlujInf4 remove(Object key) {
		return (FlujInf4) this.getCache().remove(key);
	}

	@Override
	public Collection<FlujInf4> values() {
		return (Collection<FlujInf4>) this.getCache().values();
	}

	@Override
	public Set<FlujInf4Key> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public Set<java.util.Map.Entry<FlujInf4Key, FlujInf4>> entrySet() {
		return (Set<Entry<FlujInf4Key, FlujInf4>>) this.getCache().entrySet();
	}

	@Override
	protected Comparator exportOrdered() {

		return new Comparator<FlujInf4Key>() {

			@Override
			public int compare(FlujInf4Key dc1, FlujInf4Key dc2) {
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