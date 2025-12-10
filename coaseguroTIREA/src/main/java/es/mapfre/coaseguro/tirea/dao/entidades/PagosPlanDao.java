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
import es.mapfre.coaseguro.tirea.dominio.entidades.Incidencia;
import es.mapfre.coaseguro.tirea.dominio.entidades.PagosPlan;
import es.mapfre.coaseguro.tirea.dominio.keys.PagosPlanKey;
import es.mapfre.coaseguro.tirea.excepcion.Solvencia2Excepcion;
import es.mapfre.coaseguro.tirea.utils.beanio.BeanIOReader;
import net.sf.ehcache.Element;

public class PagosPlanDao extends DaoBase{

	private static final String CACHE_NAME = "PAGOSPLAN";

	private static final int BATCH_SIZE = 1000;
	
	public PagosPlanDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	public Set<Entry<PagosPlanKey, PagosPlan>> entrySet() {
		return (Set<Entry<PagosPlanKey, PagosPlan>>) this.getCache().getAll(getCache().getKeys());
	}

	public PagosPlan get(Object key) {
		if (!(key instanceof PagosPlanKey)) {
			return null;
		} else {
			return (PagosPlan) this.getCache().get(key).getObjectValue();
		}
	}

	public List<PagosPlanKey> keySet() {
		return this.getCache().getKeys();
	}

	public PagosPlan put(PagosPlanKey key, PagosPlan tab) {
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
		PagosPlan tab = null;
		Set<Element> valores = new HashSet<Element>();
		int bloque = 0;
		while ((tab = (PagosPlan) reader.read()) != null) {
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
	
	public List<PagosPlan> getValues() {

		Collection<Element> fr = this.values();
		List<PagosPlan> lista = new ArrayList<PagosPlan>();
		if(fr == null || fr.isEmpty()){
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("10");
			inci.setInfAmpliada("No se han encontrado datos en el catalogo de pagos planificados");
			throw new Solvencia2Excepcion(inci);
		}
		
		Iterator<Element> itFR= fr.iterator();
		while (itFR.hasNext()) {
			lista.add((PagosPlan) itFR.next().getObjectValue());
		}

		return lista;
	}
	
	public List<PagosPlan> getValues(Object key) {

		Collection<Element> fr = this.values();
		List<PagosPlan> lista = new ArrayList<PagosPlan>();
		if(fr == null || fr.isEmpty()){
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("10");
			inci.setInfAmpliada("No se han encontrado datos en el catalogo de pagos planificados");
			throw new Solvencia2Excepcion(inci);
		}
		
		Iterator<Element> itFR= fr.iterator();
		while (itFR.hasNext()) {
			if (key.equals(itFR.next().getObjectKey())) {
				lista.add((PagosPlan) itFR.next().getObjectValue());
			}
			
		}

		return lista;
	}
	
	
}
