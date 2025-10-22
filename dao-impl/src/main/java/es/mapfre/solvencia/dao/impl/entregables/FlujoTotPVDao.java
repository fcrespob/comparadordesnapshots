package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.entregables.FlujInf3Key;
import es.mapfre.solvencia.coherence.keys.entregables.FlujInf4Key;
import es.mapfre.solvencia.coherence.keys.entregables.FlujoTotPVKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.FlujInf3;
import es.mapfre.solvencia.dominio.entregables.FlujInf4;
import es.mapfre.solvencia.dominio.entregables.FlujoTotPV;

public class FlujoTotPVDao extends DaoBaseSalidaCalculo
		implements Map<FlujoTotPVKey, FlujoTotPV> {

	private static final String CACHE_NAME = "flujototpv";

	public FlujoTotPVDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public FlujoTotPV get(Object key) {
		if (key instanceof FlujoTotPVKey) {
			return (FlujoTotPV) getCache().get(key);
		}
		return null;
	}

	@Override
	public FlujoTotPV put(FlujoTotPVKey key, FlujoTotPV value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public FlujoTotPV remove(Object key) {
		return (FlujoTotPV) this.getCache().remove(key);
	}

	@Override
	public Collection<FlujoTotPV> values() {
		return (Collection<FlujoTotPV>) this.getCache().values();
	}

	@Override
	public Set<FlujoTotPVKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public Set<java.util.Map.Entry<FlujoTotPVKey, FlujoTotPV>> entrySet() {
		return (Set<Entry<FlujoTotPVKey, FlujoTotPV>>) this.getCache().entrySet();
	}

	@Override
	protected Comparator exportOrdered() {

		return new Comparator<FlujoTotPVKey>() {

			@Override
			public int compare(FlujoTotPVKey dc1, FlujoTotPVKey dc2) {
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