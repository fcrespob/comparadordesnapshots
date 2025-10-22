package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.entregables.FlujTcasKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.FlujTcas;

public class FlujTcasDao extends DaoBaseSalidaCalculo implements Map<FlujTcasKey, FlujTcas> {
	
	private static final String CACHE_NAME = "flujtcas";

	public FlujTcasDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public FlujTcas get(Object key) {
		if (key instanceof FlujTcasKey) {
			return (FlujTcas) getCache().get(key);
		}
		return null;
	}

	@Override
	public FlujTcas put(FlujTcasKey key, FlujTcas value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public FlujTcas remove(Object key) {
		return (FlujTcas) this.getCache().remove(key);
	}

	@Override
	public Collection<FlujTcas> values() {
		return (Collection<FlujTcas>) this.getCache().values();
	}

	@Override
	public Set<FlujTcasKey> keySet() {
		return this.getCache().keySet();
	}
	
	@Override
	public Set<java.util.Map.Entry<FlujTcasKey, FlujTcas>> entrySet() {
		return (Set<Entry<FlujTcasKey, FlujTcas>>) this.getCache().entrySet();
	}
	
	@Override
	protected Comparator exportOrdered() {

		return new Comparator<FlujTcasKey>() {

			@Override
			public int compare(FlujTcasKey dc1, FlujTcasKey dc2) {
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