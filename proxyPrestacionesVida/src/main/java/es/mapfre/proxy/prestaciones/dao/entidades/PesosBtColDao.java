package es.mapfre.proxy.prestaciones.dao.entidades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import es.mapfre.proxy.prestaciones.dao.DaoBase;
import es.mapfre.proxy.prestaciones.dominio.entidades.PesosBt;
import es.mapfre.proxy.prestaciones.dominio.keys.PesosBtKey;
import net.sf.ehcache.Element;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.Results;

public class PesosBtColDao extends DaoBase{

	private static final String CACHE_NAME = "PESCOL0";

	public PesosBtColDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	public Set<Entry<PesosBtKey, PesosBt>> entrySet() {
		return (Set<Entry<PesosBtKey, PesosBt>>) this.getCache().getAll(getCache().getKeys());
	}

	public PesosBt get(Object key) {
		if (key instanceof PesosBtKey) {
			return (PesosBt) getCache().get(key).getObjectValue();
		}
		return null;
	}

	public List<PesosBtKey> keySet() {
		return this.getCache().getKeys();
	}

	public PesosBt put(PesosBtKey key, PesosBt value) {
		this.getCache().put(new Element(key, value));
		return value;
	}

	public boolean remove(Object key) {
		return this.getCache().remove(key);
	}

	public Collection<Element> values() {
		return (Collection<Element>) this.getAll().values();
	}

	protected Comparator exportOrdered() {
		return new Comparator<PesosBtKey>() {
			@Override
			public int compare(PesosBtKey dc1, PesosBtKey dc2) {
				if (dc1 == null && dc2 != null) {
					return 1;
				} else if (dc1 != null) {
					return dc1.compareTo(dc2);
				} else {
					return -1;
				}
			}
		};
	}
	
	public List<PesosBt> getPesosBt(String modalidad, String numpoliza){

		Attribute<Long> kpoliza = getCache().getSearchAttribute("kpoliza");
		Attribute<Integer> kmodalidad = getCache().getSearchAttribute("kmodalidad");
		
		Query query = getCache().createQuery().includeValues().addCriteria(kpoliza.eq(Long.valueOf(numpoliza))
				.and(kmodalidad.eq(Integer.valueOf(modalidad))));
		
		Results results = query.execute();
		List<Result> listadoResult = results.all();
		
		List<PesosBt> values=new ArrayList<PesosBt>();
		
		for (Result res:listadoResult){
			values.add((PesosBt) res.getValue());
		}
		
		return values;
	}
	
	public List<PesosBt> getPesosBtMod(String modalidad, String numpoliza, String subpoliza, String certificado){

		Attribute<Integer> kmodalidad = getCache().getSearchAttribute("kmodalidad");
		Attribute<Long> kpoliza = getCache().getSearchAttribute("kpoliza");
		Attribute<Integer> ksubpoliza = getCache().getSearchAttribute("ksubpoliza");
		Attribute<Integer> kcertificado = getCache().getSearchAttribute("kcertificado");
		
		Query query = getCache().createQuery().includeValues().addCriteria(kpoliza.eq(Long.valueOf(numpoliza))
				.and(kmodalidad.eq(Integer.valueOf(modalidad)))
				.and(ksubpoliza.eq(Integer.valueOf(subpoliza)))
				.and(kcertificado.eq(Integer.valueOf(certificado))));
		
		Results results = query.execute();
		List<Result> listadoResult = results.all();
		
		List<PesosBt> values=new ArrayList<PesosBt>();
		
		for (Result res:listadoResult){
			values.add((PesosBt) res.getValue());
		}
		
		return values;
	}
}
