package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.entregables.FlujPMaCoaKey;
import es.mapfre.solvencia.coherence.keys.entregables.FlujPMdCoaKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.FlujPMdCoa;

public class FlujPMdCoaDao extends DaoBaseSalidaCalculo implements Map<FlujPMdCoaKey, FlujPMdCoa> {
	
	
	private static final String CACHE_NAME = "flujpmdcoa";
	
	public FlujPMdCoaDao() {
		super();
		super.setCacheName(CACHE_NAME);			
	}

	
	@Override
	public FlujPMdCoa get(Object key) {
		if (key instanceof FlujPMdCoaKey) {
			return (FlujPMdCoa) getCache().get(key);
		}
		return null;
	}

	@Override
	public FlujPMdCoa put(FlujPMdCoaKey key, FlujPMdCoa value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public FlujPMdCoa remove(Object key) {
		return (FlujPMdCoa) this.getCache().remove(key);
	}

	@Override
	public Collection<FlujPMdCoa> values() {
		return (Collection<FlujPMdCoa>) this.getCache().values();
	}

	@Override
	public Set<FlujPMdCoaKey> keySet() {
		return this.getCache().keySet();
	}
	
	@Override
	public Set<java.util.Map.Entry<FlujPMdCoaKey, FlujPMdCoa>> entrySet() {
		return (Set<Entry<FlujPMdCoaKey, FlujPMdCoa>>) this.getCache().entrySet();
	}
	
	@Override
	protected Comparator exportOrdered() {

		return new Comparator<FlujPMdCoaKey>() {

			@Override
			public int compare(FlujPMdCoaKey dc1, FlujPMdCoaKey dc2) {
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