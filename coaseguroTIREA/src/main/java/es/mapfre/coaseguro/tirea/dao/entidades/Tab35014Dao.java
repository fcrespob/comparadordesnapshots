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
import es.mapfre.coaseguro.tirea.dominio.entidades.Tab35014;
import es.mapfre.coaseguro.tirea.dominio.keys.FlujosRealesKey;
import es.mapfre.coaseguro.tirea.dominio.keys.Tab35014Key;
import es.mapfre.coaseguro.tirea.excepcion.Solvencia2Excepcion;
import es.mapfre.coaseguro.tirea.utils.beanio.BeanIOReader;
import net.sf.ehcache.Element;

public class Tab35014Dao extends DaoBase{

	private static final String CACHE_NAME = "TAB35014";

	private static final int BATCH_SIZE = 1000;
	
	public Tab35014Dao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	public Set<Entry<Tab35014Key, Tab35014>> entrySet() {
		return (Set<Entry<Tab35014Key, Tab35014>>) this.getCache().getAll(getCache().getKeys());
	}

	public Tab35014 get(Object key) {
		if (!(key instanceof Tab35014Key)) {
			return null;
		} else {
			if(null != this.getCache().get(key)){
				return (Tab35014) this.getCache().get(key).getObjectValue();
			}else{
				return null;
			}
		}
	}

	public List<Tab35014Key> keySet() {
		return this.getCache().getKeys();
	}

	public Tab35014 put(Tab35014Key key, Tab35014 tab) {
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
		Tab35014 tab = null;
		Set<Element> valores = new HashSet<Element>();
		int bloque = 0;
		while ((tab = (Tab35014) reader.read()) != null) {
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
	
	public List<Tab35014> getValues() {

		Collection<Element> fr = this.values();
		List<Tab35014> lista = new ArrayList<Tab35014>();
		if(fr == null || fr.isEmpty()){
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("10");
			inci.setInfAmpliada("No se han encontrado datos en el catalogo ded la tabla 35014");
			throw new Solvencia2Excepcion(inci);
		}
		
		Iterator<Element> itFR= fr.iterator();
		while (itFR.hasNext()) {
			lista.add((Tab35014) itFR.next().getObjectValue());
		}

		return lista;
	}
	
}
