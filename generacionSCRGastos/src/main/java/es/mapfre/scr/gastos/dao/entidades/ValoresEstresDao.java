package es.mapfre.scr.gastos.dao.entidades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import es.mapfre.scr.gastos.dao.DaoBase;
import es.mapfre.scr.gastos.dominio.entidades.ValoresEstres;
import es.mapfre.scr.gastos.dominio.keys.ValoresEstresKey;
import es.mapfre.scr.gastos.utils.beanio.BeanIOReader;
import net.sf.ehcache.Element;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.Results;

public class ValoresEstresDao extends DaoBase{

	private static final String CACHE_NAME = "SCR0"; 
	
	private static final int BATCH_SIZE = 1000;
	
	public ValoresEstresDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}
	
	public Set<Entry<ValoresEstresKey, ValoresEstres>> entrySet() {
		return (Set<Entry<ValoresEstresKey, ValoresEstres>>) this.getCache().getAll(getCache().getKeys());
	}

	public ValoresEstres get(Object key) {
		if (!(key instanceof ValoresEstresKey)) {
			return null;
		} else {
			return (ValoresEstres) this.getCache().get(key).getObjectValue();
		}
	}

	public Set<ValoresEstresKey> keySet() {
		return (Set<ValoresEstresKey>) this.getCache().getKeys();
	}

	public ValoresEstres put(ValoresEstresKey key, ValoresEstres valoresEstres) {
		 this.getCache().put(new Element(key, valoresEstres));
		 return valoresEstres;
	}

	public boolean remove(Object key) {
		return this.getCache().remove(key);
	}

	public Collection<Element> values() {
		return (Collection<Element>) this.getAll().values();
	}

	public void loadCache(BeanIOReader reader) {
		ValoresEstres valorEstres = null;
		Set<Element> valores = new HashSet<Element>();
		int bloque = 0;
		while ((valorEstres = (ValoresEstres) reader.read()) != null) {
			valores.add(new Element(valorEstres.getKey(), valorEstres));
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

	public List<ValoresEstres> obtenerValoresEstres(String lfEfec, String bt){
		
		Attribute<String> feccierre = getCache().getSearchAttribute("feccierre");
		Attribute<String> basetec = getCache().getSearchAttribute("bt");

		//Obtenemos resultados
		Query query = getCache().createQuery().includeValues().addCriteria(feccierre.eq(lfEfec).and(basetec.eq(bt)));
		Results results = query.execute();
		List<Result> listadoResult = results.all();
		
		List<ValoresEstres> valoresEstres =new ArrayList<ValoresEstres>();
		
		for(Result res:listadoResult)
			valoresEstres.add((ValoresEstres) res.getValue());
		
		return valoresEstres;
	}
	
}
