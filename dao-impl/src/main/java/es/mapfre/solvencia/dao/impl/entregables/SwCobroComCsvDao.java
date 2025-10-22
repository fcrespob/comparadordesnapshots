package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import es.mapfre.solvencia.coherence.keys.entregables.SwCobroComCsvKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.SwCobroComCsv;

public class SwCobroComCsvDao extends DaoBaseSalidaCalculo implements Map<SwCobroComCsvKey , SwCobroComCsv> {

	private static final String CACHE_NAME = "swcobrocomcsv";

	public SwCobroComCsvDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public Set<Entry<SwCobroComCsvKey, SwCobroComCsv>> entrySet() {
		return (Set<Entry<SwCobroComCsvKey, SwCobroComCsv>>) this.getCache().entrySet();
	}

	@Override
	public SwCobroComCsv get(Object key) {
		if (key instanceof SwCobroComCsvKey) {
			return (SwCobroComCsv) getCache().get(key);
		}
		return null;
	}

	@Override
	public Set<SwCobroComCsvKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public SwCobroComCsv put(SwCobroComCsvKey key, SwCobroComCsv value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public SwCobroComCsv remove(Object key) {
		return (SwCobroComCsv) this.getCache().remove(key);
	}

	@Override
	public Collection<SwCobroComCsv> values() {
		return (Collection<SwCobroComCsv>) this.getCache().values();
	}

	protected Comparator exportOrdered() {
		return new Comparator<SwCobroComCsvKey>() {
			@Override
			public int compare(SwCobroComCsvKey dc1, SwCobroComCsvKey dc2) {
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