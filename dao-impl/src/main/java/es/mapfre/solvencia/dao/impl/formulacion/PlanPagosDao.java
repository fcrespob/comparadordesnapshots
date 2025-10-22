package es.mapfre.solvencia.dao.impl.formulacion;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.formulacion.PlanPagos;

public class PlanPagosDao extends DaoBase implements Map<UmicKey, List<PlanPagos>> {

	private static final String CACHE_NAME = "plan-pagos"; 
	
	public PlanPagosDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}
	
	@Override
	public List<PlanPagos> get(Object key) {
		if (!(key instanceof UmicKey)) {
			return null;
		} else {
			return (List<PlanPagos>) this.getCache().get(key);
		}
	}
	
	@Override
	public List<PlanPagos> put(UmicKey key, List<PlanPagos> value) {
		this.getCache().put(key, value);
		 return value;
	}
	
	@Override
	public List<PlanPagos> remove(Object key) {
		return (List<PlanPagos>) this.getCache().remove(key);
	}

	@Override
	public Set<UmicKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public Collection<List<PlanPagos>> values() {
		return (Collection<List<PlanPagos>>) this.getCache().values();
	}

	@Override
	public Set<java.util.Map.Entry<UmicKey, List<PlanPagos>>> entrySet() {
		return (Set<Entry<UmicKey, List<PlanPagos>>>) this.getCache().entrySet();
	}

}
