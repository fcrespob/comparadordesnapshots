package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.entregables.FlujInfSCRKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.FlujInfSCR;

public class FlujInfSCRDao extends DaoBaseSalidaCalculo
		implements Map<FlujInfSCRKey, FlujInfSCR> {

	private static final String CACHE_NAME = "flujinfscr";

	public FlujInfSCRDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public FlujInfSCR get(Object key) {
		if (key instanceof FlujInfSCRKey) {
			return (FlujInfSCR) getCache().get(key);
		}
		return null;
	}

	@Override
	public FlujInfSCR put(FlujInfSCRKey key, FlujInfSCR value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public FlujInfSCR remove(Object key) {
		return (FlujInfSCR) this.getCache().remove(key);
	}

	@Override
	public Collection<FlujInfSCR> values() {
		return (Collection<FlujInfSCR>) this.getCache().values();
	}

	@Override
	public Set<FlujInfSCRKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public Set<java.util.Map.Entry<FlujInfSCRKey, FlujInfSCR>> entrySet() {
		return (Set<Entry<FlujInfSCRKey, FlujInfSCR>>) this.getCache().entrySet();
	}

	@Override
	protected Comparator exportOrdered() {

		return new Comparator<FlujInfSCRKey>() {

			@Override
			public int compare(FlujInfSCRKey dc1, FlujInfSCRKey dc2) {
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