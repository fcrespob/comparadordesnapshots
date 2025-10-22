package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.entregables.PrvFpbKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.PrvFpb;

public class PrvFpbDao extends DaoBaseSalidaCalculo implements Map<PrvFpbKey, PrvFpb> {
	
	private static final String CACHE_NAME = "prvfpb";

	public PrvFpbDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public PrvFpb get(Object key) {
		if (key instanceof PrvFpbKey) {
			return (PrvFpb) getCache().get(key);
		}
		return null;
	}

	@Override
	public PrvFpb put(PrvFpbKey key, PrvFpb value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public PrvFpb remove(Object key) {
		return (PrvFpb) this.getCache().remove(key);
	}

	@Override
	public Collection<PrvFpb> values() {
		return (Collection<PrvFpb>) this.getCache().values();
	}

	@Override
	public Set<PrvFpbKey> keySet() {
		return this.getCache().keySet();
	}
	
	@Override
	public Set<java.util.Map.Entry<PrvFpbKey, PrvFpb>> entrySet() {
		return (Set<Entry<PrvFpbKey, PrvFpb>>) this.getCache().entrySet();
	}
	
	@Override
	protected Comparator exportOrdered() {

		return new Comparator<PrvFpbKey>() {

			@Override
			public int compare(PrvFpbKey dc1, PrvFpbKey dc2) {
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