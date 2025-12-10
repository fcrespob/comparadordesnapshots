package es.mapfre.coaseguro.tirea.dao.entidades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.Map.Entry;


import es.mapfre.coaseguro.tirea.dao.DaoBase;
import es.mapfre.coaseguro.tirea.dominio.entidades.DatosEspecific;
import es.mapfre.coaseguro.tirea.dominio.entidades.Incidencia;
import es.mapfre.coaseguro.tirea.dominio.keys.DatosEspecificKey;
import es.mapfre.coaseguro.tirea.excepcion.Solvencia2Excepcion;
import es.mapfre.coaseguro.tirea.utils.beanio.BeanIOReader;
import net.sf.ehcache.Element;

public class DatosEspecificDao extends DaoBase{

	private static final String CACHE_NAME = "DATOSESPECIFIC";

	private static final int BATCH_SIZE = 1000;
	
	
	public DatosEspecificDao() {
		super();
		super.setCacheName(CACHE_NAME);
		
	}

	public Set<Entry<DatosEspecificKey, DatosEspecific>> entrySet() {
		return (Set<Entry<DatosEspecificKey, DatosEspecific>>) this.getCache().getAll(getCache().getKeys());
	}

	public DatosEspecific get(Object key) {
		if (!(key instanceof DatosEspecificKey)) {
			return null;
		} else {
			return (DatosEspecific) this.getCache().get(key).getObjectValue();
		}
	}

	public List<DatosEspecificKey> keySet() {
		return this.getCache().getKeys();
	}

	public DatosEspecific put(DatosEspecificKey key, DatosEspecific tab) {
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
		DatosEspecific tab = null;
		Set<Element> valores = new HashSet<Element>();
		int bloque = 0;
		while ((tab = (DatosEspecific) reader.read()) != null) {
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
	
	public List<DatosEspecific> getValues() {

		Collection<Element> fr = this.values();
		List<DatosEspecific> lista = new ArrayList<DatosEspecific>();
		if(fr == null || fr.isEmpty()){
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("10");
			inci.setInfAmpliada("No se han encontrado datos en el catalogo de datos epecificos");
			throw new Solvencia2Excepcion(inci);
		}
		
		Iterator<Element> itFR= fr.iterator();
		while (itFR.hasNext()) {
			lista.add((DatosEspecific) itFR.next().getObjectValue());
		}

		return lista;
	}
	
	public List<DatosEspecific> getValues(Object key) {

		Collection<Element> fr = this.values();
		List<DatosEspecific> lista = new ArrayList<DatosEspecific>();
		if(fr == null || fr.isEmpty()){
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("10");
			inci.setInfAmpliada("No se han encontrado datos en el catalogo de datos epecificos");
			throw new Solvencia2Excepcion(inci);
		}
		
		Iterator<Element> itFR= fr.iterator();
		while (itFR.hasNext()) {
			if (key.equals(itFR.next().getObjectKey())) {
				lista.add((DatosEspecific) itFR.next().getObjectValue());
			}
			
		}

		return lista;
	}
	
	
}
