package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.entregables.FlujSuscriKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.FlujSuscri;

public class FlujSuscriDao extends DaoBaseSalidaCalculo implements Map<FlujSuscriKey, FlujSuscri> {
	
	private static final String CACHE_NAME = "flujsuscri";

	public FlujSuscriDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public FlujSuscri get(Object key) {
		if (key instanceof FlujSuscriKey) {
			return (FlujSuscri) getCache().get(key);
		}
		return null;
	}

	@Override
	public FlujSuscri put(FlujSuscriKey key, FlujSuscri value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public FlujSuscri remove(Object key) {
		return (FlujSuscri) this.getCache().remove(key);
	}

	@Override
	public Collection<FlujSuscri> values() {
		return (Collection<FlujSuscri>) this.getCache().values();
	}

	@Override
	public Set<FlujSuscriKey> keySet() {
		return this.getCache().keySet();
	}
	
	@Override
	public Set<java.util.Map.Entry<FlujSuscriKey, FlujSuscri>> entrySet() {
		return (Set<Entry<FlujSuscriKey, FlujSuscri>>) this.getCache().entrySet();
	}
	
	@Override
	protected Comparator exportOrdered() {

		return new Comparator<FlujSuscriKey>() {

			@Override
			public int compare(FlujSuscriKey dc1, FlujSuscriKey dc2) {
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