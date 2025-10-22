package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.entregables.BasetecKey;
import es.mapfre.solvencia.coherence.keys.entregables.FlujCoaSegKey;
import es.mapfre.solvencia.coherence.keys.entregables.PrvInf1Key;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.Basetec;

public class BasetecDao extends DaoBaseSalidaCalculo implements Map<BasetecKey, Basetec> {
	
	private static final String CACHE_NAME = "basetec";

	public BasetecDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public Basetec get(Object key) {
		if (key instanceof PrvInf1Key) {
			return (Basetec) getCache().get(key);
		}
		return null;
	}

	@Override
	public Basetec put(BasetecKey key, Basetec value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public Basetec remove(Object key) {
		return (Basetec) this.getCache().remove(key);
	}

	@Override
	public Collection<Basetec> values() {
		return (Collection<Basetec>) this.getCache().values();
	}

	@Override
	public Set<BasetecKey> keySet() {
		return this.getCache().keySet();
	}
	
	@Override
	public Set<java.util.Map.Entry<BasetecKey, Basetec>> entrySet() {
		return (Set<Entry<BasetecKey, Basetec>>) this.getCache().entrySet();
	}
	
	@Override
	protected Comparator exportOrdered() {

		return new Comparator<BasetecKey>() {

			@Override
			public int compare(BasetecKey dc1, BasetecKey dc2) {
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