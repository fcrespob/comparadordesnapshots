package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.entregables.FlujPMdCoaMKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.FlujPMdCoaM;

public class FlujPMdCoaMDao extends DaoBaseSalidaCalculo implements Map<FlujPMdCoaMKey, FlujPMdCoaM> {
	
	
	private static final String CACHE_NAME = "flujpmdcoam";
	
	public FlujPMdCoaMDao() {
		super();
		super.setCacheName(CACHE_NAME);			
	}

	
	@Override
	public FlujPMdCoaM get(Object key) {
		if (key instanceof FlujPMdCoaMKey) {
			return (FlujPMdCoaM) getCache().get(key);
		}
		return null;
	}

	@Override
	public FlujPMdCoaM put(FlujPMdCoaMKey key, FlujPMdCoaM value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public FlujPMdCoaM remove(Object key) {
		return (FlujPMdCoaM) this.getCache().remove(key);
	}

	@Override
	public Collection<FlujPMdCoaM> values() {
		return (Collection<FlujPMdCoaM>) this.getCache().values();
	}

	@Override
	public Set<FlujPMdCoaMKey> keySet() {
		return this.getCache().keySet();
	}
	
	@Override
	public Set<java.util.Map.Entry<FlujPMdCoaMKey, FlujPMdCoaM>> entrySet() {
		return (Set<Entry<FlujPMdCoaMKey, FlujPMdCoaM>>) this.getCache().entrySet();
	}
	
	@Override
	protected Comparator exportOrdered() {

		return new Comparator<FlujPMdCoaMKey>() {

			@Override
			public int compare(FlujPMdCoaMKey dc1, FlujPMdCoaMKey dc2) {
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