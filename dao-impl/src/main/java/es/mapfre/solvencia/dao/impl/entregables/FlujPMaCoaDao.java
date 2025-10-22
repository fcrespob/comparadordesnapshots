package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.entregables.FlujPMaCoaKey;
import es.mapfre.solvencia.coherence.keys.entregables.TotPMaCoaKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.FlujPMaCoa;

public class FlujPMaCoaDao extends DaoBaseSalidaCalculo implements Map<FlujPMaCoaKey, FlujPMaCoa> {
	
	
	private static final String CACHE_NAME = "flujpmacoa";
	
	public FlujPMaCoaDao() {
		super();
		super.setCacheName(CACHE_NAME);			
	}

	
	@Override
	public FlujPMaCoa get(Object key) {
		if (key instanceof FlujPMaCoaKey) {
			return (FlujPMaCoa) getCache().get(key);
		}
		return null;
	}

	@Override
	public FlujPMaCoa put(FlujPMaCoaKey key, FlujPMaCoa value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public FlujPMaCoa remove(Object key) {
		return (FlujPMaCoa) this.getCache().remove(key);
	}

	@Override
	public Collection<FlujPMaCoa> values() {
		return (Collection<FlujPMaCoa>) this.getCache().values();
	}

	@Override
	public Set<FlujPMaCoaKey> keySet() {
		return this.getCache().keySet();
	}
	
	@Override
	public Set<java.util.Map.Entry<FlujPMaCoaKey, FlujPMaCoa>> entrySet() {
		return (Set<Entry<FlujPMaCoaKey, FlujPMaCoa>>) this.getCache().entrySet();
	}
	
	@Override
	protected Comparator exportOrdered() {

		return new Comparator<FlujPMaCoaKey>() {

			@Override
			public int compare(FlujPMaCoaKey dc1, FlujPMaCoaKey dc2) {
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