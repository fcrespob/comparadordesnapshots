package es.mapfre.coaseguro.tirea.dao.entidades;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import es.mapfre.coaseguro.tirea.dao.DaoBase;
import es.mapfre.coaseguro.tirea.dominio.entidades.Incidencia;
import es.mapfre.coaseguro.tirea.dominio.entidades.TotPMaCoa;
import es.mapfre.coaseguro.tirea.dominio.keys.PesosBtKey;
import es.mapfre.coaseguro.tirea.dominio.keys.TotPMaCoaKey;
import es.mapfre.coaseguro.tirea.excepcion.Solvencia2Excepcion;
import es.mapfre.coaseguro.tirea.utils.beanio.BeanIOReader;
import net.sf.ehcache.Element;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.Results;

public class TotPMaCoaDao extends DaoBase{

	private static final String CACHE_NAME = "TOTPMACOA";
	
	private static final int BATCH_SIZE = 1000;

	public TotPMaCoaDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	public Set<Entry<TotPMaCoaKey, TotPMaCoa>> entrySet() {
		return (Set<Entry<TotPMaCoaKey, TotPMaCoa>>) this.getCache().getAll(getCache().getKeys());
	}

	public TotPMaCoa get(Object key) {
		if (key instanceof TotPMaCoaKey) {
			return (TotPMaCoa) getCache().get(key).getObjectValue();
		}
		return null;
	}

	public List<TotPMaCoaKey> keySet() {
		return this.getCache().getKeys();
	}

	public TotPMaCoa put(TotPMaCoaKey key, TotPMaCoa value) {
		this.getCache().put(new Element(key, value));
		return value;
	}

	public boolean remove(Object key) {
		return this.getCache().remove(key);
	}

	public Collection<Element> values() {
		return (Collection<Element>) this.getAll().values();
	}
	
	public void loadCache(BeanIOReader reader) {
		TotPMaCoa totPmaCoa = null;
		Set<Element> valores = new HashSet<Element>();
		int bloque = 0;
		while ((totPmaCoa = (TotPMaCoa) reader.read()) != null) {
			valores.add(new Element(totPmaCoa.getKey(), totPmaCoa));
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
	
	public List<TotPMaCoa> getValues() {

		Collection<Element> fr = this.values();
		List<TotPMaCoa> lista = new ArrayList<TotPMaCoa>();
		if(fr == null || fr.isEmpty()){
			es.mapfre.coaseguro.tirea.dominio.entidades.Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("10");
			inci.setInfAmpliada("No se han encontrado registros para el fichero TotPmaCoa.");
			throw new Solvencia2Excepcion(inci);
		}
		
		Iterator<Element> itFR= fr.iterator();
		while (itFR.hasNext()) {
			lista.add((TotPMaCoa) itFR.next().getObjectValue());
		}

		return lista;
	}
}
