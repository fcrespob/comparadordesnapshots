package es.mapfre.gbt.mensualizadorTasas.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Direction;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.Results;
import es.mapfre.gbt.mensualizadorTasas.cache.CacheBase;
import es.mapfre.gbt.mensualizadorTasas.dominio.TasaMensualizada;
import es.mapfre.gbt.mensualizadorTasas.key.TasaMensualizadaKey;
import es.mapfre.gbt.mensualizadorTasas.utils.BeanIOWriter;

public class TasaMensualizadaDao extends CacheBase {

	private static final String CACHE_NAME = "tasas-mensualizadas";
	public TasaMensualizadaDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	public Set<Element> entrySet() {
		return (Set<Element>) this.getAll().values();
	}

	public TasaMensualizada get(Object key) {
		if (!(key instanceof TasaMensualizadaKey)) {
			return null;
		} else {
			return (TasaMensualizada) this.getEhcache().get(key).getObjectValue();
		}
	}

	public Set<TasaMensualizadaKey> keySet() {
		return (Set<TasaMensualizadaKey>) this.getEhcache().getKeys();
	}

	public TasaMensualizada put(TasaMensualizadaKey key, TasaMensualizada value) {
		Element element = new Element(key, value);
		this.getEhcache().put(element);
		return value;
	}

	public boolean remove(Object key) {
		return this.getEhcache().remove(key);
	}

	public Collection<Element> values() {
		return (Collection<Element>) this.getAll().values();
	}
	
	
	
	private static Comparator<Element> ordenadorValores = new Comparator<Element>() {
		@Override
		public int compare(Element o1, Element o) {
			int result = 0;
			TasaMensualizada o1TM = (TasaMensualizada) o1.getObjectValue();
			TasaMensualizada oTM = (TasaMensualizada) o.getObjectValue();
			int comp = o1TM.getKtablaanu().compareTo(oTM.getKtablaanu());
			if(comp==0){
				comp = o1TM.getKfcierre().compareTo(oTM.getKfcierre());
				if(comp==0){
					comp = o1TM.getKaniosdesde().compareTo(oTM.getKaniosdesde());
					result = comp;
				}else{
					result = comp;
				}
			}else{
				result = comp;
			}
			return result;
		}
	};
	
	@Override
	public void exportCache(BeanIOWriter writer) {
//		Collection<Element> values = this.values();
//		List<Element> listValues = new ArrayList<Element>(values);
		
		List<TasaMensualizada> listValues = obtenerTasasOrdenadas();
		
		//Collections.sort(listValues, ordenadorValores);
		
		//Iterator<Element> iter = listValues.iterator();
		Iterator<TasaMensualizada> iter = listValues.iterator();
		while(iter.hasNext()){
		    //Element entry = iter.next();
		    //TasaMensualizada tasa = (TasaMensualizada) entry.getObjectValue();
			TasaMensualizada tasa = iter.next();
		    writer.write(tasa);
		}
		writer.flush();
	}
	
	public List<TasaMensualizada> obtenerTasasOrdenadas(){
		Ehcache cache = this.getEhcache();
		
		Attribute<String> ktabla = cache.getSearchAttribute("ktablaanu");
		Attribute<String> fcierre = cache.getSearchAttribute("kfcierre");
		Attribute<BigDecimal> anios = cache.getSearchAttribute("kaniosdesde");
		
		Query query = cache.createQuery().includeValues().addOrderBy(ktabla, Direction.ASCENDING).addOrderBy(fcierre, Direction.ASCENDING).addOrderBy(anios, Direction.ASCENDING).end();
		Results results = query.execute();
		
		List<Result> tasas = results.all();
		List<TasaMensualizada> tasaAnul = new ArrayList<TasaMensualizada>();
		
		for(Result res:tasas)
			tasaAnul.add((TasaMensualizada) res.getValue());
		
		return tasaAnul;
	}
	
	public List<TasaMensualizada> obtenerTasasMensual(String ktablaanu, String kfcierre, BigDecimal kaniosdesde)  {
		Ehcache cache = this.getEhcache();
		
		Attribute<String> ktabla = cache.getSearchAttribute("ktablaanu");
		Attribute<String> fcierre = cache.getSearchAttribute("kfcierre");
		Attribute<BigDecimal> anios = cache.getSearchAttribute("kaniosdesde");
		
		Query query = cache.createQuery().includeAttribute(ktabla, fcierre, anios).end();
		Results results = query.execute();
		
		List<Result> tasas = results.all();
		List<TasaMensualizada> tasaAnul = new ArrayList<TasaMensualizada>();
		
		for(Result res:tasas)
			tasaAnul.add((TasaMensualizada) res.getValue());
		
		return tasaAnul;
	}
}