package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.entregables.FlujTcasKey;
import es.mapfre.solvencia.coherence.keys.entregables.TotPMaCoaKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.TotPMaCoa;

public class TotPMaCoaDao extends DaoBaseSalidaCalculo implements Map<TotPMaCoaKey, TotPMaCoa> {
	
	
	private static final String CACHE_NAME = "totpmacoa";
	
	public TotPMaCoaDao() {
		super();
		super.setCacheName(CACHE_NAME);				
	}

	
	@Override
	public TotPMaCoa get(Object key) {
		if (key instanceof TotPMaCoaKey) {
			return (TotPMaCoa) getCache().get(key);
		}
		return null;
	}

	@Override
	public TotPMaCoa put(TotPMaCoaKey key, TotPMaCoa value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public TotPMaCoa remove(Object key) {
		return (TotPMaCoa) this.getCache().remove(key);
	}

	@Override
	public Collection<TotPMaCoa> values() {
		return (Collection<TotPMaCoa>) this.getCache().values();
	}

	@Override
	public Set<TotPMaCoaKey> keySet() {
		return this.getCache().keySet();
	}
	
	@Override
	public Set<java.util.Map.Entry<TotPMaCoaKey, TotPMaCoa>> entrySet() {
		return (Set<Entry<TotPMaCoaKey, TotPMaCoa>>) this.getCache().entrySet();
	}
	
	@Override
	protected Comparator exportOrdered() {

		return new Comparator<TotPMaCoaKey>() {

			@Override
			public int compare(TotPMaCoaKey dc1, TotPMaCoaKey dc2) {
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