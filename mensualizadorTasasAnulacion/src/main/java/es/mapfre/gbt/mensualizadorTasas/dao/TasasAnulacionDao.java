package es.mapfre.gbt.mensualizadorTasas.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import net.sf.ehcache.Element;
import es.mapfre.gbt.mensualizadorTasas.cache.CacheBase;
import es.mapfre.gbt.mensualizadorTasas.dominio.EntidadBase;
import es.mapfre.gbt.mensualizadorTasas.dominio.TasasAnulacion;
import es.mapfre.gbt.mensualizadorTasas.key.TasasAnulacionKey;
import es.mapfre.gbt.mensualizadorTasas.utils.BeanIOReader;
import es.mapfre.gbt.mensualizadorTasas.utils.ConstantesMensualizador;

public class TasasAnulacionDao extends CacheBase{
	
	private static final String CACHE_NAME = "VTA0";
	
	public TasasAnulacionDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}
	
	public Set<Element> entrySet() {
		return (Set<Element>) this.getAll().values();
	}
	
	public TasasAnulacion get(Object key) {
		if (!(key instanceof TasasAnulacionKey)) {
			return null;
		} else {
			return (TasasAnulacion) this.getEhcache().get(key).getObjectValue();
		}
	}
	
	public Set<TasasAnulacionKey> keySet() {
		return (Set<TasasAnulacionKey>) this.getEhcache().getKeys();
	}
	
	public TasasAnulacion put(TasasAnulacionKey key, TasasAnulacion value) {
		Element element = new Element(key, value);
		this.getEhcache().put(element);
		return value;
	}
	
	public boolean remove(Object key) {
		return this.getEhcache().remove(key);
	}
	
	public Collection<Element> values() {
		return (Collection<Element>) this.getAll().values();
	}
	
	public void loadCache(BeanIOReader reader, String fecha) {
		EntidadBase valor = null;
		Collection<Element> valores = new ArrayList<Element>();
		int bloque = 0;
		while ((valor = (EntidadBase) reader.read()) != null) {
			TasasAnulacion elem = (TasasAnulacion) valor;
			if(elem.getKfcierre().compareTo(fecha)>=0){
				Element element = new Element(valor.getKey(), valor);
				valores.add(element);
				bloque++;
				if (bloque % ConstantesMensualizador.BATCH_SIZE == 0) {
					this.putAll(valores);
					valores.clear();
				}
			}
		}
		if (valores.size() > 0) {
			this.putAll(valores);
			valores.clear();
		}
	}
}