package es.mapfre.scr.gastos.dao.entidades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import es.mapfre.scr.gastos.dao.DaoBase;
import es.mapfre.scr.gastos.dominio.entidades.GastosReales;
import es.mapfre.scr.gastos.dominio.keys.GastosRealesKey;
import net.sf.ehcache.Element;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.Results;

public class GastosRealesDao extends DaoBase{
	
	private static final String CACHE_NAME = "GRE0";

	public GastosRealesDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	public Set<Entry<GastosRealesKey, GastosReales>> entrySet() {
		return (Set<Entry<GastosRealesKey, GastosReales>>) this.getCache().getAll(getCache().getKeys());
	}

	public List<GastosReales> getGastosReales(String fecCierre, String ktipobt){

		Attribute<String> bt = getCache().getSearchAttribute("ktipobt");
		Attribute<String> fecHasta = getCache().getSearchAttribute("fecHasta");
		Attribute<String> fecDesde = getCache().getSearchAttribute("fecDesde");
		
		Query query = getCache().createQuery().includeValues().addCriteria(bt.eq(ktipobt)
				.and(fecHasta.ge(fecCierre)).and(fecDesde.le(fecCierre)));
		
		Results results = query.execute();
		List<Result> listadoResult = results.all();
		
		List<GastosReales> values=new ArrayList<GastosReales>();
		
		for (Result res:listadoResult){
			values.add((GastosReales) res.getValue());
		}
		
		return values;
	}

	public Set<GastosRealesKey> keySet() {
		return (Set<GastosRealesKey>) this.getCache().getKeys();
	}

	public GastosReales put(GastosRealesKey key, GastosReales gastosReales) {
		this.getCache().put(new Element(key, gastosReales));
		return gastosReales;
	}

	public boolean remove(Object key) {
		return this.getCache().remove(key);
	}

	public Collection<Element> values() {
		return (Collection<Element>) this.getAll().values();
	}

	public GastosReales get(Object key) {
		if (!(key instanceof GastosRealesKey)) {
			return null;
		} else {
			return (GastosReales) this.getCache().get(key).getObjectValue();
		}
	}
}
