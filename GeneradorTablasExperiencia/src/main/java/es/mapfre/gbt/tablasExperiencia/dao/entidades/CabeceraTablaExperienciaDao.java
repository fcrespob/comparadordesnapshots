package es.mapfre.gbt.tablasExperiencia.dao.entidades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.ehcache.Element;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.Results;
import es.mapfre.gbt.tablasExperiencia.dao.DaoBase;
import es.mapfre.gbt.tablasExperiencia.dominio.entidades.CabeceraTablaExperiencia;
import es.mapfre.gbt.tablasExperiencia.dominio.keys.CabeceraTablaExperienciaKey;

public class CabeceraTablaExperienciaDao extends DaoBase{
	
	private static final String CACHE_NAME = "TXP0"; 

	public CabeceraTablaExperienciaDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	public Set<Entry<CabeceraTablaExperienciaKey, CabeceraTablaExperiencia>> entrySet() {
		return (Set<Entry<CabeceraTablaExperienciaKey, CabeceraTablaExperiencia>>) this.getCache().getAll(getCache().getKeys());
	}

	public CabeceraTablaExperiencia get(Object key) {
		if (!(key instanceof CabeceraTablaExperienciaKey)) {
			return null;
		} else {
			return (CabeceraTablaExperiencia) this.getCache().get(key).getObjectValue();
		}
	}

	public List<CabeceraTablaExperienciaKey> keySet() {
		return this.getCache().getKeys();
	}

	public CabeceraTablaExperiencia put(CabeceraTablaExperienciaKey key, CabeceraTablaExperiencia cabeceraTablaExperiencia) {
		 this.getCache().put(new Element(key, cabeceraTablaExperiencia));
		 return cabeceraTablaExperiencia;
	}

	public boolean remove(Object key) {
		return this.getCache().remove(key);
	}

	public Collection<CabeceraTablaExperiencia> values() {
		return (Collection<CabeceraTablaExperiencia>) this.getCache().getAll(getCache().getKeys());
	}

	public List<CabeceraTablaExperiencia> getValue(Integer ktabla, String feccierre) {
		
		Attribute<Integer> ktablaAtt = getCache().getSearchAttribute("ktabla");
		Attribute<String> fecAnulaAtt = getCache().getSearchAttribute("fecAnula");
		Attribute<String> fecAltaAtt = getCache().getSearchAttribute("fecAlta");
		
		Query query = getCache().createQuery().includeValues().addCriteria(ktablaAtt.eq(ktabla).and(fecAnulaAtt.ge(feccierre).and(fecAltaAtt.le(feccierre)))).end();
		Results results = query.execute();
		
		List<Result> listadoResult = results.all();
		
		List<CabeceraTablaExperiencia> cabeceras=new ArrayList<CabeceraTablaExperiencia>();
		
		for(Result res:listadoResult){
			cabeceras.add( (CabeceraTablaExperiencia) res.getValue());
		}
		
		return cabeceras;
	}
	
	public List<CabeceraTablaExperiencia> getValueTabla(Integer ktabla) {
		
		Attribute<Integer> ktablaAtt = getCache().getSearchAttribute("ktabla");
		Query query = getCache().createQuery().includeValues().addCriteria(ktablaAtt.eq(ktabla)).end();
		
		Results results = query.execute();
		
		List<Result> listadoResult = results.all();
		
		List<CabeceraTablaExperiencia> cabeceras=new ArrayList<CabeceraTablaExperiencia>();
		
		for(Result res:listadoResult){
			cabeceras.add( (CabeceraTablaExperiencia) res.getValue());
		}
		
		return cabeceras;
	}

}

