package es.mapfre.solvencia.dao.impl.maestro;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;

public class UmicsBoteDao extends DaoBase implements Map<UmicKey, Incidencia>{
	
private static final String CACHE_NAME = "umicsBote"; 
	
	public UmicsBoteDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}
	
	@Override
	public Set<Entry<UmicKey, Incidencia>> entrySet() {
		return (Set<Entry<UmicKey, Incidencia>>) this.getCache().entrySet();
	}

	@Override
	public Incidencia get(Object key) {
		return (Incidencia) this.getCache().get(key);
	}

	@Override
	public Set<UmicKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public Incidencia put(UmicKey key, Incidencia value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		 return value;
	}

	@Override
	public Incidencia remove(Object key) {
		return (Incidencia) this.getCache().remove(key);
	}

	@Override
	public Collection<Incidencia> values() {
		return (Collection<Incidencia>) this.getCache().values();
	}
	
	@Override
	public void loadCache(BeanIOReader reader) {
		Incidencia valor = null;
		Map<Object, Object> valores = new HashMap<Object, Object>();
		int bloque = 0;
		while ((valor = (Incidencia) reader.read()) != null) {
			valores.put(valor.getKey().getUmicKey(), valor);
			bloque++;
			if (bloque % ConstantesSolvencia.BATCH_SIZE == 0) {
				this.putAll(valores);
				valores.clear();
			}
		}
		if (valores.size() > 0) {
			this.putAll(valores);
			valores.clear();
		}
	}
	
}