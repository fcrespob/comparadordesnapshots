package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.entregables.FlujCoaSegKey;
import es.mapfre.solvencia.coherence.keys.entregables.PrvInf2Key;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.PrvInf2;

public class PrvInf2Dao extends DaoBaseSalidaCalculo implements Map<PrvInf2Key, PrvInf2> {
	
	private static final String CACHE_NAME = "prvinf2";

	public PrvInf2Dao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public PrvInf2 get(Object key) {
		if (key instanceof PrvInf2Key) {
			return (PrvInf2) getCache().get(key);
		}
		return null;
	}

	@Override
	public PrvInf2 put(PrvInf2Key key, PrvInf2 value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public PrvInf2 remove(Object key) {
		return (PrvInf2) this.getCache().remove(key);
	}

	@Override
	public Collection<PrvInf2> values() {
		return (Collection<PrvInf2>) this.getCache().values();
	}

	@Override
	public Set<PrvInf2Key> keySet() {
		return this.getCache().keySet();
	}
	
	@Override
	public Set<java.util.Map.Entry<PrvInf2Key, PrvInf2>> entrySet() {
		return (Set<Entry<PrvInf2Key, PrvInf2>>) this.getCache().entrySet();
	}
	
	@Override
	protected Comparator exportOrdered() {

		return new Comparator<PrvInf2Key>() {

			@Override
			public int compare(PrvInf2Key dc1, PrvInf2Key dc2) {
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