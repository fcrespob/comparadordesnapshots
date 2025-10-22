package es.mapfre.scr.tablasExperiencia.dao.entidades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import es.mapfre.scr.tablasExperiencia.dao.DaoBase;
import es.mapfre.scr.tablasExperiencia.dominio.entidades.LongevidadModInterno;
import es.mapfre.scr.tablasExperiencia.dominio.keys.LongevidadModInternoKey;
import es.mapfre.scr.tablasExperiencia.utils.beanio.BeanIOReader;
import net.sf.ehcache.Element;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Direction;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.Results;

public class LongevidadModInternoDao extends DaoBase{

	private static final String CACHE_NAME = "LMI0";
	
	private static final int BATCH_SIZE = 1000;
	
	public LongevidadModInternoDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}
	
	public Set<Entry<LongevidadModInternoKey, LongevidadModInterno>> entrySet() {
		return (Set<Entry<LongevidadModInternoKey, LongevidadModInterno>>) this.getCache().getAll(getCache().getKeys());
	}

	public LongevidadModInterno get(Object key) {
		if (!(key instanceof LongevidadModInternoKey)) {
			return null;
		} else {
			return (LongevidadModInterno) this.getCache().get(key).getObjectValue();
		}
	}

	public Set<LongevidadModInternoKey> keySet() {
		return (Set<LongevidadModInternoKey>) this.getCache().getKeys();
	}

	public LongevidadModInterno put(LongevidadModInternoKey key, LongevidadModInterno longevidadModInterno) {
		 this.getCache().put(new Element(key, longevidadModInterno));
		 return longevidadModInterno;
	}

	public boolean remove(Object key) {
		return this.getCache().remove(key);
	}

	public Collection<Element> values() {
		return (Collection<Element>) this.getAll().values();
	}

	public void loadCache(BeanIOReader reader) {
		LongevidadModInterno longevidadModInterno = null;
		Set<Element> valores = new HashSet<Element>();
		int bloque = 0;
		while ((longevidadModInterno = (LongevidadModInterno) reader.read()) != null) {
			valores.add(new Element(longevidadModInterno.getKey(), longevidadModInterno));
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

	public List<LongevidadModInterno> obtenerLongevidadModInterno(String fcierre, String sexo){
		
		Attribute<String> feccierre = getCache().getSearchAttribute("feccierre");
		Attribute<String> ksexo = getCache().getSearchAttribute("ksexo");
		Attribute<String> kedad = getCache().getSearchAttribute("kedadfija");
		
		//Obtenemos resultados
		Query query = getCache().createQuery().includeValues().addCriteria(feccierre.eq(fcierre).and(ksexo.eq(sexo)));
		query.addOrderBy(kedad, Direction.ASCENDING);
		Results results = query.execute();
		List<Result> listadoResult = results.all();
		
		List<LongevidadModInterno> longevidadModInterno =new ArrayList<LongevidadModInterno>();
		
		for(Result res:listadoResult)
			longevidadModInterno.add((LongevidadModInterno) res.getValue());
		
		return longevidadModInterno;
	}
}
