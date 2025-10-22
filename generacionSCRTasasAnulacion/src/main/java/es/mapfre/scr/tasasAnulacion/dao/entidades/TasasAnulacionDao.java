package es.mapfre.scr.tasasAnulacion.dao.entidades;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import es.mapfre.scr.tasasAnulacion.dao.DaoBase;
import es.mapfre.scr.tasasAnulacion.dominio.entidades.TasasAnulacion;
import es.mapfre.scr.tasasAnulacion.dominio.keys.TasasAnulacionKey;
import net.sf.ehcache.Element;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Direction;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.Results;

public class TasasAnulacionDao extends DaoBase{
	
	private static final String CACHE_NAME = "VTA0";
	
	public TasasAnulacionDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}
	
	public Set<Entry<TasasAnulacionKey, TasasAnulacion>> entrySet() {
		return (Set<Entry<TasasAnulacionKey, TasasAnulacion>>) this.getCache().getAll(getCache().getKeys());
	}
	
	public TasasAnulacion get(Object key) {
		if (!(key instanceof TasasAnulacionKey)) {
			return null;
		} else {
			return (TasasAnulacion) this.getCache().get(key).getObjectValue();
		}
	}
	
	public Set<TasasAnulacionKey> keySet() {
		return (Set<TasasAnulacionKey>) this.getCache().getKeys();
	}
	
	public TasasAnulacion put(TasasAnulacionKey key, TasasAnulacion value) {
		Element element = new Element(key, value);
		this.getCache().put(element);
		return value;
	}
	
	public boolean remove(Object key) {
		return this.getCache().remove(key);
	}
	
	public Collection<Element> values() {
		return (Collection<Element>) this.getAll().values();
	}
	
	public List<TasasAnulacion> getValuesFecha(String fecCierre){
		
		Attribute<String> kfcierre = getCache().getSearchAttribute("kfcierre");
		Attribute<String> ktablaanu = getCache().getSearchAttribute("ktablaanu");
		Attribute<BigDecimal> kaniosdesde = getCache().getSearchAttribute("kaniosdesde");
		
		Query query = getCache().createQuery().includeValues().addCriteria(kfcierre.le(fecCierre));
		query.addOrderBy(kfcierre, Direction.DESCENDING);
		query.addOrderBy(ktablaanu, Direction.ASCENDING);
		query.addOrderBy(kaniosdesde, Direction.ASCENDING);
		Results results = query.execute();
		List<Result> listadoResult = results.all();
		
		List<TasasAnulacion> valores = new ArrayList<TasasAnulacion>();
		
		for (Result res:listadoResult){
			valores.add((TasasAnulacion) res.getValue());
		}
		
		return valores;
	}
}