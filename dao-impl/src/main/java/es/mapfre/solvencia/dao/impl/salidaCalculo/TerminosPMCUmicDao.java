package es.mapfre.solvencia.dao.impl.salidaCalculo;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.coherence.keys.salidaCalculo.TerminosPMCUmicKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.salidaCalculo.TerminosPMCUmic;

public class TerminosPMCUmicDao extends DaoBaseSalidaCalculo implements Map<TerminosPMCUmicKey, TerminosPMCUmic> {

	private static final String CACHE_NAME = "datos-calculados-umic";
	
	
	public TerminosPMCUmicDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}	
	
	
	@Override
	public TerminosPMCUmic get(Object key) {
		if (!(key instanceof TerminosPMCUmicKey)) {
			return null;
		} else {
			return (TerminosPMCUmic) this.getCache().get(key);
		}
	}
	
	@Override
	public Set<TerminosPMCUmicKey> keySet() {
		return this.getCache().keySet();
	}	
	

	@Override
	public TerminosPMCUmic put(TerminosPMCUmicKey key,
			TerminosPMCUmic value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public TerminosPMCUmic remove(Object key) {
		return (TerminosPMCUmic) this.getCache().remove(key);
	}

	@Override
	public Collection<TerminosPMCUmic> values() {
		return (Collection<TerminosPMCUmic>) this.getCache().values();
	}

	@Override
	public Set<Entry<TerminosPMCUmicKey, TerminosPMCUmic>> entrySet() {
		return (Set<Entry<TerminosPMCUmicKey, TerminosPMCUmic>>) this.getCache().entrySet();
	}
	
	@Override
	protected Comparator exportOrdered() {

		return new Comparator<TerminosPMCUmicKey>() {

			@Override
			public int compare(TerminosPMCUmicKey dc1, TerminosPMCUmicKey dc2) {
				if (dc1 == null && dc2 != null) {
					return 1;
				} else if (dc2 == null) {
					return -1;
				} else if (dc1 != null ) {
					return dc1.compareTo(dc2);
				}
				
				return 0;
			}
		};
	}
	
	public TerminosPMCUmic getValues(UmicKey claveUmic, String bt, Integer iteracion) {
		
		TerminosPMCUmicKey key = new TerminosPMCUmicKey(claveUmic,bt,iteracion);
		
		return (TerminosPMCUmic) this.getCache().get(key);
		
	}
}
			
	