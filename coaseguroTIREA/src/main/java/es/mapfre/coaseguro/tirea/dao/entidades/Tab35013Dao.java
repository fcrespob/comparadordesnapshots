package es.mapfre.coaseguro.tirea.dao.entidades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import es.mapfre.coaseguro.tirea.dao.DaoBase;
import es.mapfre.coaseguro.tirea.dominio.entidades.Incidencia;
import es.mapfre.coaseguro.tirea.dominio.entidades.Tab35013;
import es.mapfre.coaseguro.tirea.dominio.keys.FlujosRealesKey;
import es.mapfre.coaseguro.tirea.dominio.keys.Tab35013Key;
import es.mapfre.coaseguro.tirea.excepcion.Solvencia2Excepcion;
import es.mapfre.coaseguro.tirea.utils.beanio.BeanIOReader;
import net.sf.ehcache.Element;

public class Tab35013Dao extends DaoBase{

	private static final String CACHE_NAME = "TAB35013";

	private static final int BATCH_SIZE = 1000;
	
	public Tab35013Dao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	public Set<Entry<Tab35013Key, Tab35013>> entrySet() {
		return (Set<Entry<Tab35013Key, Tab35013>>) this.getCache().getAll(getCache().getKeys());
	}

	public Tab35013 get(Object key) {
		if (!(key instanceof Tab35013Key)) {
			return null;
		} else {
			if(null != this.getCache().get(key)){
				return (Tab35013) this.getCache().get(key).getObjectValue();
			}else{
				return null;
			}
		}
	}

	public List<Tab35013Key> keySet() {
		return this.getCache().getKeys();
	}

	public Tab35013 put(Tab35013Key key, Tab35013 tab) {
		 this.getCache().put(new Element(key, tab));
		 return tab;
	}

	public boolean remove(Object key) {
		return this.getCache().remove(key);
	}

	public Collection<Element> values() {
		return (Collection<Element>) this.getAll().values();
	}
	
	public void loadCache(BeanIOReader reader) {
		Tab35013 tab = null;
		Set<Element> valores = new HashSet<Element>();
		int bloque = 0;
		while ((tab = (Tab35013) reader.read()) != null) {
			valores.add(new Element(tab.getKey(), tab));
			bloque++;
			if (bloque % BATCH_SIZE == 0) {
				this.putAll(valores);
				valores.clear();
			}
		}
		if (valores.size() > 0) {
			this.putAll(valores);
			valores.clear();
		}
	}
	
	public List<Tab35013> getValues() {

		Collection<Element> fr = this.values();
		List<Tab35013> lista = new ArrayList<Tab35013>();
		if(fr == null || fr.isEmpty()){
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("10");
			inci.setInfAmpliada("No se han encontrado datos en el catalogo de la tabla 35013");
			throw new Solvencia2Excepcion(inci);
		}
		
		Iterator<Element> itFR= fr.iterator();
		while (itFR.hasNext()) {
			lista.add((Tab35013) itFR.next().getObjectValue());
		}

		return lista;
	}
	
}
