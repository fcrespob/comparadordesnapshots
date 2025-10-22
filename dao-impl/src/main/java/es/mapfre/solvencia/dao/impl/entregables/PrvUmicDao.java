package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.entregables.FlujCoaSegKey;
import es.mapfre.solvencia.coherence.keys.entregables.PrvUmicKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.PrvUmic;

public class PrvUmicDao extends DaoBaseSalidaCalculo implements Map<PrvUmicKey, PrvUmic> {
	
	private static final String CACHE_NAME = "prvumic";

	public PrvUmicDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public PrvUmic get(Object key) {
		if (key instanceof PrvUmicKey) {
			return (PrvUmic) getCache().get(key);
		}
		return null;
	}

	@Override
	public PrvUmic put(PrvUmicKey key, PrvUmic value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public PrvUmic remove(Object key) {
		return (PrvUmic) this.getCache().remove(key);
	}

	@Override
	public Collection<PrvUmic> values() {
		return (Collection<PrvUmic>) this.getCache().values();
	}

	@Override
	public Set<PrvUmicKey> keySet() {
		return this.getCache().keySet();
	}
	
	@Override
	public Set<java.util.Map.Entry<PrvUmicKey, PrvUmic>> entrySet() {
		return (Set<Entry<PrvUmicKey, PrvUmic>>) this.getCache().entrySet();
	}
	
	@Override
	protected Comparator exportOrdered() {

		return new Comparator<PrvUmicKey>() {

			@Override
			public int compare(PrvUmicKey dc1, PrvUmicKey dc2) {
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