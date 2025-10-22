package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.entregables.SwCobroComKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.SwCobroCom;

public class SwCobroComDao extends DaoBaseSalidaCalculo 
        implements Map<SwCobroComKey, SwCobroCom> {

	private static final String CACHE_NAME = "swcobrocom";

	public SwCobroComDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public Set<Entry<SwCobroComKey, SwCobroCom>> entrySet() {
		return (Set<Entry<SwCobroComKey, SwCobroCom>>) this.getCache().entrySet();
	}

	@Override
	public SwCobroCom get(Object key) {
		if (key instanceof SwCobroComKey) {
			return (SwCobroCom) getCache().get(key);
		}
		return null;
	}

	@Override
	public Set<SwCobroComKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public SwCobroCom put(SwCobroComKey key, SwCobroCom value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public SwCobroCom remove(Object key) {
		return (SwCobroCom) this.getCache().remove(key);
	}

	@Override
	public Collection<SwCobroCom> values() {
		return (Collection<SwCobroCom>) this.getCache().values();
	}

	protected Comparator exportOrdered() {
		return new Comparator<SwCobroComKey>() {
			@Override
			public int compare(SwCobroComKey dc1, SwCobroComKey dc2) {
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