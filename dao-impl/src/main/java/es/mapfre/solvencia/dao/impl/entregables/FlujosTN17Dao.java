package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.entregables.FlujosTN17Key;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.FlujosTN17;

public class FlujosTN17Dao extends DaoBaseSalidaCalculo 
        implements Map<FlujosTN17Key, FlujosTN17> {

	private static final String CACHE_NAME = "flujostn17";

	public FlujosTN17Dao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public Set<Entry<FlujosTN17Key, FlujosTN17>> entrySet() {
		return (Set<Entry<FlujosTN17Key, FlujosTN17>>) this.getCache().entrySet();
	}

	@Override
	public FlujosTN17 get(Object key) {
		if (key instanceof FlujosTN17Key) {
			return (FlujosTN17) getCache().get(key);
		}
		return null;
	}

	@Override
	public Set<FlujosTN17Key> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public FlujosTN17 put(FlujosTN17Key key, FlujosTN17 value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public FlujosTN17 remove(Object key) {
		return (FlujosTN17) this.getCache().remove(key);
	}

	@Override
	public Collection<FlujosTN17> values() {
		return (Collection<FlujosTN17>) this.getCache().values();
	}

	protected Comparator exportOrdered() {
		return new Comparator<FlujosTN17Key>() {
			@Override
			public int compare(FlujosTN17Key dc1, FlujosTN17Key dc2) {
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