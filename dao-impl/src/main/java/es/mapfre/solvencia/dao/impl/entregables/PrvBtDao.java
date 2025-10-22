package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.entregables.FlujCoaSegKey;
import es.mapfre.solvencia.coherence.keys.entregables.PrvBtKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.PrvBt;

public class PrvBtDao extends DaoBaseSalidaCalculo implements Map<PrvBtKey, PrvBt> {
	
	private static final String CACHE_NAME = "prvbt";

	public PrvBtDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public PrvBt get(Object key) {
		if (key instanceof PrvBtKey) {
			return (PrvBt) getCache().get(key);
		}
		return null;
	}

	@Override
	public PrvBt put(PrvBtKey key, PrvBt value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public PrvBt remove(Object key) {
		return (PrvBt) this.getCache().remove(key);
	}

	@Override
	public Collection<PrvBt> values() {
		return (Collection<PrvBt>) this.getCache().values();
	}

	@Override
	public Set<PrvBtKey> keySet() {
		return this.getCache().keySet();
	}
	
	@Override
	public Set<java.util.Map.Entry<PrvBtKey, PrvBt>> entrySet() {
		return (Set<Entry<PrvBtKey, PrvBt>>) this.getCache().entrySet();
	}
	
	@Override
	protected Comparator exportOrdered() {

		return new Comparator<PrvBtKey>() {

			@Override
			public int compare(PrvBtKey dc1, PrvBtKey dc2) {
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