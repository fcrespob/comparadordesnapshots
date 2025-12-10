package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.entregables.FlujPMaCoaMKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.FlujPMaCoaM;

public class FlujPMaCoaMDao extends DaoBaseSalidaCalculo implements Map<FlujPMaCoaMKey, FlujPMaCoaM> {
	
	
	private static final String CACHE_NAME = "flujpmacoam";
	
	public FlujPMaCoaMDao() {
		super();
		super.setCacheName(CACHE_NAME);			
	}

	
	@Override
	public FlujPMaCoaM get(Object key) {
		if (key instanceof FlujPMaCoaMKey) {
			return (FlujPMaCoaM) getCache().get(key);
		}
		return null;
	}

	@Override
	public FlujPMaCoaM put(FlujPMaCoaMKey key, FlujPMaCoaM value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public FlujPMaCoaM remove(Object key) {
		return (FlujPMaCoaM) this.getCache().remove(key);
	}

	@Override
	public Collection<FlujPMaCoaM> values() {
		return (Collection<FlujPMaCoaM>) this.getCache().values();
	}

	@Override
	public Set<FlujPMaCoaMKey> keySet() {
		return this.getCache().keySet();
	}
	
	@Override
	public Set<java.util.Map.Entry<FlujPMaCoaMKey, FlujPMaCoaM>> entrySet() {
		return (Set<Entry<FlujPMaCoaMKey, FlujPMaCoaM>>) this.getCache().entrySet();
	}
	
	@Override
	protected Comparator exportOrdered() {

		return new Comparator<FlujPMaCoaMKey>() {

			@Override
			public int compare(FlujPMaCoaMKey dc1, FlujPMaCoaMKey dc2) {
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