package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.entregables.FPSLKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.FPSL;

public class FPSLDao extends DaoBaseSalidaCalculo 
        implements Map<FPSLKey, FPSL> {

	private static final String CACHE_NAME = "fpsl";

	public FPSLDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public Set<Entry<FPSLKey, FPSL>> entrySet() {
		return (Set<Entry<FPSLKey, FPSL>>) this.getCache().entrySet();
	}

	@Override
	public FPSL get(Object key) {
		if (key instanceof FPSLKey) {
			return (FPSL) getCache().get(key);
		}
		return null;
	}

	@Override
	public Set<FPSLKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public FPSL put(FPSLKey key, FPSL value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public FPSL remove(Object key) {
		return (FPSL) this.getCache().remove(key);
	}

	@Override
	public Collection<FPSL> values() {
		return (Collection<FPSL>) this.getCache().values();
	}

	protected Comparator exportOrdered() {
		return new Comparator<FPSLKey>() {
			@Override
			public int compare(FPSLKey dc1, FPSLKey dc2) {
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