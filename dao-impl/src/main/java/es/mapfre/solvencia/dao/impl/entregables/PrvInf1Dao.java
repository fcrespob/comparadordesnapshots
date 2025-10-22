package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.entregables.PrvInf1Key;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.PrvInf1;

public class PrvInf1Dao extends DaoBaseSalidaCalculo implements Map<PrvInf1Key, PrvInf1> {
	
	private static final String CACHE_NAME = "prvinf1";

	public PrvInf1Dao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public PrvInf1 get(Object key) {
		if (key instanceof PrvInf1Key) {
			return (PrvInf1) getCache().get(key);
		}
		return null;
	}

	@Override
	public PrvInf1 put(PrvInf1Key key, PrvInf1 value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public PrvInf1 remove(Object key) {
		return (PrvInf1) this.getCache().remove(key);
	}

	@Override
	public Collection<PrvInf1> values() {
		return (Collection<PrvInf1>) this.getCache().values();
	}

	@Override
	public Set<PrvInf1Key> keySet() {
		return this.getCache().keySet();
	}
	
	@Override
	public Set<java.util.Map.Entry<PrvInf1Key, PrvInf1>> entrySet() {
		return (Set<Entry<PrvInf1Key, PrvInf1>>) this.getCache().entrySet();
	}
	
	@Override
	protected Comparator exportOrdered() {

		return new Comparator<PrvInf1Key>() {

			@Override
			public int compare(PrvInf1Key dc1, PrvInf1Key dc2) {
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