package es.mapfre.gbt.tablasExperiencia.dao.entidades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.ehcache.Element;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Direction;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.Results;
import es.mapfre.gbt.tablasExperiencia.dao.DaoBase;
import es.mapfre.gbt.tablasExperiencia.dominio.entidades.ConvTablasExpReal;
import es.mapfre.gbt.tablasExperiencia.dominio.keys.ConvTablasExpRealKey;
import es.mapfre.gbt.tablasExperiencia.utils.ConstantesSolvencia;
import es.mapfre.gbt.tablasExperiencia.utils.beanio.BeanIOReader;

public class ConvTablasExpRealDao extends DaoBase {

	private static final String CACHE_NAME = "ATR0";

	public ConvTablasExpRealDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	public Set<java.util.Map.Entry<ConvTablasExpRealKey, ConvTablasExpReal>> entrySet() {
		return (Set<Entry<ConvTablasExpRealKey, ConvTablasExpReal>>) this.getCache().getAll(getCache().getKeys());
	}

	public ConvTablasExpReal get(Object key) {
		if (!(key instanceof ConvTablasExpRealKey)) {
			return null;
		} else {
			return (ConvTablasExpReal) this.getCache().get(key).getObjectValue();
		}
	}

	public List<ConvTablasExpRealKey> keySet() {
		return this.getCache().getKeys();
	}

	public ConvTablasExpReal put(ConvTablasExpRealKey key,
			ConvTablasExpReal value) {
		this.getCache().put(new Element(key, value));
		return value;
	}

	public boolean remove(Object key) {
		return this.getCache().remove(key);
	}

	public List<ConvTablasExpReal> orderedValues() {
		Attribute<String> kbasetecAtt = getCache().getSearchAttribute("kbasetec");
		Attribute<Integer> kcompaniaAtt = getCache().getSearchAttribute("kcompania");
		Attribute<String> knegocioAtt = getCache().getSearchAttribute("knegocio");
		Attribute<String> kriesgoAtt = getCache().getSearchAttribute("kriesgo");
		Attribute<String> ksexoAtt = getCache().getSearchAttribute("ksexo");
		Attribute<String> kcategAtt = getCache().getSearchAttribute("kcateg");
		Attribute<Integer> kmodalidadAtt = getCache().getSearchAttribute("kmodalidad");
		Attribute<String> kfdesdeAtt = getCache().getSearchAttribute("kfdesde");
		Attribute<Integer> kedaddesdeAtt = getCache().getSearchAttribute("kedaddesde");
		
		Query query = getCache().createQuery().includeValues().addOrderBy(kbasetecAtt, Direction.ASCENDING)
															.addOrderBy(kcompaniaAtt, Direction.ASCENDING)
															.addOrderBy(knegocioAtt, Direction.ASCENDING)
															.addOrderBy(kriesgoAtt, Direction.ASCENDING)
															.addOrderBy(ksexoAtt, Direction.ASCENDING)
															.addOrderBy(kcategAtt, Direction.ASCENDING)
															.addOrderBy(kmodalidadAtt, Direction.ASCENDING)
															.addOrderBy(kfdesdeAtt, Direction.ASCENDING)
															.addOrderBy(kedaddesdeAtt, Direction.ASCENDING)
															.end();
		
		Results results = query.execute();
		List<Result> listadoResult = results.all();
		
		List<ConvTablasExpReal> valueList = new ArrayList<ConvTablasExpReal>();
		Iterator iter = listadoResult.iterator();
		while(iter.hasNext()){
			Map.Entry entry = (Map.Entry)iter.next();
			valueList.add((ConvTablasExpReal)entry.getValue());
		}
		return valueList;
	}

	public Collection<ConvTablasExpReal> values() {
		return (Collection<ConvTablasExpReal>) this.getCache().getAll(getCache().getKeys());
	}

	private Comparator getComparatorKey() {
		Comparator comparatorByKey = new Comparator<ConvTablasExpReal>() {
			@Override
			public int compare(ConvTablasExpReal v1,
					ConvTablasExpReal v2) {
				int resultado = v1.getKbasetec().compareTo( v2.getKbasetec());
				if(resultado!=0){ return resultado; };
				resultado = Integer.compare(v1.getKcompania(), v2.getKcompania());
				if(resultado!=0){ return resultado; };
				resultado = v1.getKnegocio().compareTo( v2.getKnegocio());
				if(resultado!=0){ return resultado; };
				resultado = v1.getKriesgo().compareTo( v2.getKriesgo());
				if(resultado!=0){ return resultado; };
				resultado = v1.getKsexo().compareTo( v2.getKsexo());
				if(resultado!=0){ return resultado; };
				resultado = v1.getKcateg().compareTo( v2.getKcateg());
				if(resultado!=0){ return resultado; };
				resultado = Integer.compare(v1.getKmodalidad(), v2.getKmodalidad());
				if(resultado!=0){ return resultado; };
				resultado = v1.getKfdesde().compareTo( v2.getKfdesde());
				if(resultado!=0){ return resultado; };
				return Integer.compare(v1.getKedaddesde(), v2.getKedaddesde());
			}
		};
		return comparatorByKey;
	}

	public List<ConvTablasExpReal> obtenerTablasExp(String fecCierre)  {

		Attribute<String> kbasetecAtt = getCache().getSearchAttribute("kbasetec");
		Attribute<Integer> kcompaniaAtt = getCache().getSearchAttribute("kcompania");
		Attribute<String> knegocioAtt = getCache().getSearchAttribute("knegocio");
		Attribute<String> kriesgoAtt = getCache().getSearchAttribute("kriesgo");
		Attribute<String> ksexoAtt = getCache().getSearchAttribute("ksexo");
		Attribute<String> kcategAtt = getCache().getSearchAttribute("kcateg");
		Attribute<Integer> kmodalidadAtt = getCache().getSearchAttribute("kmodalidad");
		Attribute<String> kfdesdeAtt = getCache().getSearchAttribute("kfdesde");
		Attribute<Integer> kedaddesdeAtt = getCache().getSearchAttribute("kedaddesde");
		Attribute<String> fhastaAtt = getCache().getSearchAttribute("fhasta");
		
		Query query = getCache().createQuery().includeValues().addCriteria(kfdesdeAtt.le(fecCierre).and(fhastaAtt.gt(fecCierre)));
		query.addOrderBy(kbasetecAtt, Direction.ASCENDING)
				.addOrderBy(kcompaniaAtt, Direction.ASCENDING)
				.addOrderBy(knegocioAtt, Direction.ASCENDING)
				.addOrderBy(kriesgoAtt, Direction.ASCENDING)
				.addOrderBy(ksexoAtt, Direction.ASCENDING)
				.addOrderBy(kcategAtt, Direction.ASCENDING)
				.addOrderBy(kmodalidadAtt, Direction.ASCENDING)
				.addOrderBy(kfdesdeAtt, Direction.ASCENDING)
				.addOrderBy(kedaddesdeAtt, Direction.ASCENDING)
				.end();
		Results results = query.execute();
		List<Result> listadoResult = results.all();
		
		List<ConvTablasExpReal> tablaExp=new ArrayList<ConvTablasExpReal>();

		
		for(Result res:listadoResult)
			tablaExp.add((ConvTablasExpReal) res.getValue());

		return tablaExp;
	}

	public void loadCache(BeanIOReader reader) {
		ConvTablasExpReal valor = null;
		Set<Element> valores = new HashSet<Element>();
		int bloque = 0;

		while ((valor = (ConvTablasExpReal) reader.read()) != null) {
			valores.add(new Element(valor.getKey(), valor));
			bloque++;
			if (bloque % ConstantesSolvencia.BATCH_SIZE == 0) {
				this.putAll(valores);
				valores.clear();
			}
		}
		if (valores.size() > 0) {
			this.putAll(valores);
			valores.clear();
		}
	}

}
