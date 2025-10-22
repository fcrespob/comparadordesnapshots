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
import es.mapfre.coaseguro.tirea.dominio.entidades.Tab35012;
import es.mapfre.coaseguro.tirea.dominio.keys.FlujosRealesKey;
import es.mapfre.coaseguro.tirea.dominio.keys.Tab35012Key;
import es.mapfre.coaseguro.tirea.excepcion.Solvencia2Excepcion;
import es.mapfre.coaseguro.tirea.utils.beanio.BeanIOReader;
import net.sf.ehcache.Element;

public class Tab35012Dao extends DaoBase{

	private static final String CACHE_NAME = "TAB35012";

	private static final int BATCH_SIZE = 1000;
	
	public Tab35012Dao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	public Set<Entry<Tab35012Key, Tab35012>> entrySet() {
		return (Set<Entry<Tab35012Key, Tab35012>>) this.getCache().getAll(getCache().getKeys());
	}

	public Tab35012 get(Object key) {
		if (!(key instanceof Tab35012Key)) {
			return null;
		} else {
			return (Tab35012) this.getCache().get(key).getObjectValue();
		}
	}

	public List<Tab35012Key> keySet() {
		return this.getCache().getKeys();
	}

	public Tab35012 put(Tab35012Key key, Tab35012 tab) {
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
		Tab35012 tab = null;
		Set<Element> valores = new HashSet<Element>();
		int bloque = 0;
		while ((tab = (Tab35012) reader.read()) != null) {
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
	
	public List<Tab35012> getValues() {

		Collection<Element> fr = this.values();
		List<Tab35012> lista = new ArrayList<Tab35012>();
		if(fr == null || fr.isEmpty()){
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("10");
			inci.setInfAmpliada("No se han encontrado datos en el catalogo ded la tabla 35012");
			throw new Solvencia2Excepcion(inci);
		}
		
		Iterator<Element> itFR= fr.iterator();
		while (itFR.hasNext()) {
			lista.add((Tab35012) itFR.next().getObjectValue());
		}

		return lista;
	}
	
}
