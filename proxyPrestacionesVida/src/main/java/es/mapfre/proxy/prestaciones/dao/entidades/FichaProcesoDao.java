package es.mapfre.proxy.prestaciones.dao.entidades;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import es.mapfre.proxy.prestaciones.dao.DaoBase;
import es.mapfre.proxy.prestaciones.dominio.entidades.FichaProceso;
import es.mapfre.proxy.prestaciones.dominio.keys.FichaProcesoKey;
import es.mapfre.proxy.prestaciones.utils.beanio.BeanIOReader;
import net.sf.ehcache.Element;

public class FichaProcesoDao extends DaoBase {

	private static final String CACHE_NAME = "FIC0"; 

	private static final int BATCH_SIZE = 1000;

	public FichaProcesoDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}
	
	public Set<Entry<FichaProcesoKey, FichaProceso>> entrySet() {
		return (Set<Entry<FichaProcesoKey, FichaProceso>>) this.getCache().getAll(getCache().getKeys());
	}
	
	public FichaProceso get(Object key) {
		if (!(key instanceof FichaProcesoKey)) {
			return null;
		} else {
			return (FichaProceso) this.getCache().get(key).getObjectValue();
		}
	}

	public List<FichaProcesoKey> keySet() {
		return this.getCache().getKeys();
	}
	
	public FichaProceso put(FichaProcesoKey key, FichaProceso fichaProceso) {
		 this.getCache().put(new Element(key, fichaProceso));
		 return fichaProceso;
	}

	public boolean remove(Object key) {
		return this.getCache().remove(key);
	}

	public Collection<Element> values() {
		return (Collection<Element>) this.getAll().values();
	}
	
	public void loadCache(BeanIOReader reader) {
		FichaProceso fichaProceso = null;
		Set<Element> fichas = new HashSet<Element>();
		int bloque = 0;
		while ((fichaProceso = (FichaProceso) reader.read()) != null) {
			fichas.add(new Element(fichaProceso.getKey(), fichaProceso));
			bloque++;
			if (bloque % BATCH_SIZE == 0) {
				this.putAll(fichas);
				fichas.clear();
			}
		}
		if (fichas.size() > 0) {
			this.putAll(fichas);
			fichas.clear();
		}
	}
	
}