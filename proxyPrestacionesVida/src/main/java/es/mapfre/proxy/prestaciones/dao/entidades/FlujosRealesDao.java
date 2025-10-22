package es.mapfre.proxy.prestaciones.dao.entidades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import es.mapfre.proxy.prestaciones.dao.DaoBase;
import es.mapfre.proxy.prestaciones.dominio.entidades.FlujosReales;
import es.mapfre.proxy.prestaciones.dominio.entidades.Incidencia;
import es.mapfre.proxy.prestaciones.dominio.keys.FlujosRealesKey;
import es.mapfre.proxy.prestaciones.excepcion.Solvencia2Excepcion;
import es.mapfre.proxy.prestaciones.utils.beanio.BeanIOReader;
import net.sf.ehcache.Element;

public class FlujosRealesDao extends DaoBase{

	private static final String CACHE_NAME = "REA0";

	private static final int BATCH_SIZE = 1000;
	
	public FlujosRealesDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	public Set<Entry<FlujosRealesKey, FlujosReales>> entrySet() {
		return (Set<Entry<FlujosRealesKey, FlujosReales>>) this.getCache().getAll(getCache().getKeys());
	}

	public FlujosReales get(Object key) {
		if (!(key instanceof FlujosRealesKey)) {
			return null;
		} else {
			return (FlujosReales) this.getCache().get(key).getObjectValue();
		}
	}

	public List<FlujosRealesKey> keySet() {
		return this.getCache().getKeys();
	}

	public FlujosReales put(FlujosRealesKey key, FlujosReales flujosReales) {
		 this.getCache().put(new Element(key, flujosReales));
		 return flujosReales;
	}

	public boolean remove(Object key) {
		return this.getCache().remove(key);
	}

	public Collection<Element> values() {
		return (Collection<Element>) this.getAll().values();
	}
	
	public void loadCache(BeanIOReader reader) {
		FlujosReales flujosReales = null;
		Set<Element> valores = new HashSet<Element>();
		int bloque = 0;
		while ((flujosReales = (FlujosReales) reader.read()) != null) {
			valores.add(new Element(flujosReales.getKey(), flujosReales));
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
	
	public List<FlujosReales> getValues() {

		Collection<Element> fr = this.values();
		List<FlujosReales> lista = new ArrayList<FlujosReales>();
		if(fr == null || fr.isEmpty()){
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("10");
			inci.setInfAmpliada("No se han encontrado prestaciones reales en el fichero correspondiente.");
			throw new Solvencia2Excepcion(inci);
		}
		
		Iterator<Element> itFR= fr.iterator();
		while (itFR.hasNext()) {
			lista.add((FlujosReales) itFR.next().getObjectValue());
		}

		return lista;
	}
	
}
