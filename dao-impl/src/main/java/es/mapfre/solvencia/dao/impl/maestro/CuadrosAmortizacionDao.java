package es.mapfre.solvencia.dao.impl.maestro;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.maestro.CuadrosAmortizacionKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.maestro.CuadrosAmortizacion;

public class CuadrosAmortizacionDao extends DaoBase implements Map<CuadrosAmortizacionKey, CuadrosAmortizacion> {
	
	private static final String CACHE_NAME = "X880J009"; 
	
	public CuadrosAmortizacionDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}


	@Override
	public Set<Entry<CuadrosAmortizacionKey, CuadrosAmortizacion>> entrySet() {
		return (Set<Entry<CuadrosAmortizacionKey, CuadrosAmortizacion>>) this.getCache().entrySet();
	}

	@Override
	public CuadrosAmortizacion get(Object key) {
		if (!(key instanceof CuadrosAmortizacionKey)) {
			return null;
		} else {
			return (CuadrosAmortizacion) this.getCache().get(key);
		}
	}

	@Override
	public Set<CuadrosAmortizacionKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public CuadrosAmortizacion put(CuadrosAmortizacionKey key, CuadrosAmortizacion cuadrosAmortizacion) {
		 this.getCache().put(key, cuadrosAmortizacion);
		 return cuadrosAmortizacion;
	}

	@Override
	public CuadrosAmortizacion remove(Object key) {
		return (CuadrosAmortizacion) this.getCache().remove(key);
	}

	@Override
	public Collection<CuadrosAmortizacion> values() {
		return (Collection<CuadrosAmortizacion>) this.getCache().values();
	}
}