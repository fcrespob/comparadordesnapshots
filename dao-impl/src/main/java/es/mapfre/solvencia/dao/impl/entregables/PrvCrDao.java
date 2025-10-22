package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.entregables.FlujCoaSegKey;
import es.mapfre.solvencia.coherence.keys.entregables.PrvCrKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.PrvCr;

public class PrvCrDao extends DaoBaseSalidaCalculo implements Map<PrvCrKey, PrvCr> {
	
	private static final String CACHE_NAME = "prvcr";

	public PrvCrDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public PrvCr get(Object key) {
		if (key instanceof PrvCrKey) {
			return (PrvCr) getCache().get(key);
		}
		return null;
	}

	@Override
	public PrvCr put(PrvCrKey key, PrvCr value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public PrvCr remove(Object key) {
		return (PrvCr) this.getCache().remove(key);
	}

	@Override
	public Collection<PrvCr> values() {
		return (Collection<PrvCr>) this.getCache().values();
	}

	@Override
	public Set<PrvCrKey> keySet() {
		return this.getCache().keySet();
	}
	
	@Override
	public Set<java.util.Map.Entry<PrvCrKey, PrvCr>> entrySet() {
		return (Set<Entry<PrvCrKey, PrvCr>>) this.getCache().entrySet();
	}
	
	@Override
	protected Comparator exportOrdered() {

		return new Comparator<PrvCrKey>() {

			@Override
			public int compare(PrvCrKey dc1, PrvCrKey dc2) {
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