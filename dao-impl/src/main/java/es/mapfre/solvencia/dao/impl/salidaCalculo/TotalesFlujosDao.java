package es.mapfre.solvencia.dao.impl.salidaCalculo;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.salidaCalculo.TotalesFlujosKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos;

public class TotalesFlujosDao extends DaoBaseSalidaCalculo implements Map<TotalesFlujosKey, TotalesFlujos> {
	
	private static final String CACHE_NAME = "totales-flujos";

	public TotalesFlujosDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public TotalesFlujos get(Object key) {
		if (key instanceof TotalesFlujosKey) {
			return (TotalesFlujos) getCache().get(key);
		}
		return null;
	}

	@Override
	public TotalesFlujos put(TotalesFlujosKey key, TotalesFlujos value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public TotalesFlujos remove(Object key) {
		return (TotalesFlujos) this.getCache().remove(key);
	}

	@Override
	public Set<TotalesFlujosKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public Collection<TotalesFlujos> values() {
		return (Collection<TotalesFlujos>) this.getCache().values();
	}

	@Override
	public Set<java.util.Map.Entry<TotalesFlujosKey, TotalesFlujos>> entrySet() {
		return (Set<Entry<TotalesFlujosKey, TotalesFlujos>>) this.getCache().entrySet();
	}
	
	@Override
	protected Comparator exportOrdered() {

		return new Comparator<TotalesFlujosKey>() {

			@Override
			public int compare(TotalesFlujosKey dc1, TotalesFlujosKey dc2) {
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