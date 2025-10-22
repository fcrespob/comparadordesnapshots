package es.mapfre.solvencia.dao.impl.scr.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.scr.entregables.FactoresVolatilidadKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.scr.entregables.FactoresVolatilidad;

public class FactoresVolatilidadDao extends DaoBaseSalidaCalculo implements Map<FactoresVolatilidadKey, FactoresVolatilidad>{

	private static final String CACHE_NAME = "scrvm";

	public FactoresVolatilidadDao(){
		super();
		super.setCacheName(CACHE_NAME);
	}
	
	@Override
	public Set<java.util.Map.Entry<FactoresVolatilidadKey, FactoresVolatilidad>> entrySet() {
		return (Set<Entry<FactoresVolatilidadKey, FactoresVolatilidad>>) this.getCache().entrySet();
	}

	@Override
	public FactoresVolatilidad get(Object key) {
		if (key instanceof FactoresVolatilidadKey) {
			return (FactoresVolatilidad) getCache().get(key);
		}
		return null;
	}

	@Override
	public FactoresVolatilidad put(FactoresVolatilidadKey key, FactoresVolatilidad value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public FactoresVolatilidad remove(Object key) {
		return (FactoresVolatilidad) this.getCache().remove(key);
	}

	@Override
	public Collection<FactoresVolatilidad> values() {
		return (Collection<FactoresVolatilidad>) this.getCache().values();
	}
	
	@Override
	public Set<FactoresVolatilidadKey> keySet() {
		return this.getCache().keySet();
	}
	
	@Override
	protected Comparator exportOrdered() {
		return new Comparator<FactoresVolatilidadKey>() {
			@Override
			public int compare(FactoresVolatilidadKey dc1, FactoresVolatilidadKey dc2) {
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
