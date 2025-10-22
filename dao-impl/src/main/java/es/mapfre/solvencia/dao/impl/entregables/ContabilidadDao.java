/* MODIFICACION:TAR00302248-Contabilidad SolvenciaII en SSAA 
   FECHA: 25/09/2017
   AUTOR: INDRA
 */

package es.mapfre.solvencia.dao.impl.entregables;


import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.entregables.ContabilidadKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.Contabilidad;

public class ContabilidadDao extends DaoBaseSalidaCalculo implements Map<ContabilidadKey, Contabilidad> {
	
	private static final String CACHE_NAME = "contab";

	public ContabilidadDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public Contabilidad get(Object key) {
		if (key instanceof ContabilidadKey) {
			return (Contabilidad) getCache().get(key);
		}
		return null;
	}

	@Override
	public Contabilidad put(ContabilidadKey key, Contabilidad value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public Contabilidad remove(Object key) {
		return (Contabilidad) this.getCache().remove(key);
	}

	@Override
	public Collection<Contabilidad> values() {
		return (Collection<Contabilidad>) this.getCache().values();
	}

	@Override
	public Set<ContabilidadKey> keySet() {
		return this.getCache().keySet();
	}
	
	@Override
	public Set<java.util.Map.Entry<ContabilidadKey, Contabilidad>> entrySet() {
		return (Set<Entry<ContabilidadKey, Contabilidad>>) this.getCache().entrySet();
	}
	
	@Override
	protected Comparator exportOrdered() {

		return new Comparator<ContabilidadKey>() {

			@Override
			public int compare(ContabilidadKey dc1, ContabilidadKey dc2) {
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