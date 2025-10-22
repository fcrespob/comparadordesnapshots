package es.mapfre.gbt.tablasExperiencia.dao.entidades;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.ehcache.Element;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Direction;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.Results;
import es.mapfre.gbt.tablasExperiencia.dao.DaoBase;
import es.mapfre.gbt.tablasExperiencia.dominio.entidades.TablaExperiencia;
import es.mapfre.gbt.tablasExperiencia.dominio.keys.TablaExperienciaKey;

public class TablaExperienciaDao extends DaoBase {
	
	
	private static final String CACHE_NAME = "CTM0"; 

	public TablaExperienciaDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	public Set<Entry<TablaExperienciaKey, TablaExperiencia>> entrySet() {
		return (Set<Entry<TablaExperienciaKey, TablaExperiencia>>) this.getCache().getAll(getCache().getKeys());
	}

	public TablaExperiencia get(Object key) {
		if (!(key instanceof TablaExperienciaKey)) {
			return null;
		} else {
			return (TablaExperiencia) this.getCache().get(key).getObjectValue();
		}
	}

	public List<TablaExperienciaKey> keySet() {
		return this.getCache().getKeys();
	}

	public TablaExperiencia put(TablaExperienciaKey key, TablaExperiencia tablaExperiencia) {
		 this.getCache().put(new Element(key, tablaExperiencia));
		 return tablaExperiencia;
	}

	public boolean remove(Object key) {
		return this.getCache().remove(key);
	}

	public Collection<TablaExperiencia> values() {
		return (Collection<TablaExperiencia>) this.getCache().getAll(getCache().getKeys());
	}
	
	public List<TablaExperiencia> getValue(TablaExperiencia configValoresTabla) {
		
		Attribute<Integer> ktablaAtt = getCache().getSearchAttribute("ktabla");
		Attribute<String> k2tipovalorAtt = getCache().getSearchAttribute("k2tipovalor");
		Attribute<String> knacimientoAtt = getCache().getSearchAttribute("knacimiento");
		//Attribute<BigDecimal> kinteresAtt = getCache().getSearchAttribute("kinteres");
		//Attribute<BigDecimal> ksobremortAtt = getCache().getSearchAttribute("ksobremort");
		//Attribute<BigDecimal> ksobreriesAtt = getCache().getSearchAttribute("ksobreries");
		
		List<TablaExperiencia> tablas= new ArrayList<TablaExperiencia>();

		//Query query = getCache().createQuery().includeValues().addCriteria(ktablaAtt.eq(configValoresTabla.getKtabla()).and(k2tipovalorAtt.eq(configValoresTabla.getK2tipovalor()).and(knacimientoAtt.eq(configValoresTabla.getKnacimiento()).and(kinteresAtt.eq(configValoresTabla.getKinteres()).and(ksobremortAtt.eq(configValoresTabla.getKsobremort()).and(ksobreriesAtt.eq(configValoresTabla.getKsobreries()))))))).end();
		Query query = getCache().createQuery().includeValues().addCriteria(ktablaAtt.eq(configValoresTabla.getKtabla()).and(k2tipovalorAtt.eq(configValoresTabla.getK2tipovalor()).and(knacimientoAtt.eq(configValoresTabla.getKnacimiento()))));
		query.addOrderBy(ktablaAtt, Direction.ASCENDING)
				.addOrderBy(k2tipovalorAtt, Direction.ASCENDING)
				.addOrderBy(knacimientoAtt, Direction.ASCENDING).end();
		Results results = query.execute();
		
		List<Result> listadoResult = results.all();
		TablaExperiencia tablaExperiencia;
		for(Result res:listadoResult){
			tablaExperiencia = (TablaExperiencia) res.getValue();
			if( configValoresTabla.getKinteres().compareTo( tablaExperiencia.getKinteres() ) == 0 && configValoresTabla.getKsobremort().compareTo( tablaExperiencia.getKsobremort() ) == 0 && configValoresTabla.getKsobreries().compareTo( tablaExperiencia.getKsobreries() ) == 0 ){
				tablas.add( tablaExperiencia );
			}
		}
		
		return tablas;
	}
	
	public List<TablaExperiencia> obtenerRegistro(Integer tablaBase,String tipoValor, BigDecimal interes,
			BigDecimal  sobremortalidad, BigDecimal sobreriesgo) {
		
		Attribute<Integer> ktablaAtt = getCache().getSearchAttribute("ktabla");
		Attribute<String> k2tipovalorAtt = getCache().getSearchAttribute("k2tipovalor");
		Attribute<String> knacimientoAtt = getCache().getSearchAttribute("knacimiento");
		//Attribute<BigDecimal> kinteresAtt = getCache().getSearchAttribute("kinteres");
		//Attribute<BigDecimal> ksobremortAtt = getCache().getSearchAttribute("ksobremort");
		//Attribute<BigDecimal> ksobreriesAtt = getCache().getSearchAttribute("ksobreries");
		
		List<TablaExperiencia> tablas= new ArrayList<TablaExperiencia>();

		//Query query = getCache().createQuery().includeValues().addCriteria(ktablaAtt.eq(tablaBase).and(k2tipovalorAtt.eq(tipoValor).and(kinteresAtt.eq(interes).and(ksobremortAtt.eq(sobremortalidad).and(ksobreriesAtt.eq(sobreriesgo)))))).end();
		Query query = getCache().createQuery().includeValues().addCriteria(ktablaAtt.eq(tablaBase).and(k2tipovalorAtt.eq(tipoValor)));
		query.addOrderBy(ktablaAtt, Direction.ASCENDING)
				.addOrderBy(k2tipovalorAtt, Direction.ASCENDING)
				.addOrderBy(knacimientoAtt, Direction.ASCENDING).end();
		Results results = query.execute();
		
		List<Result> listadoResult = results.all();
		
		TablaExperiencia tablaExperiencia;
		for(Result res:listadoResult){
			tablaExperiencia = (TablaExperiencia) res.getValue();
			if( interes.compareTo( tablaExperiencia.getKinteres() ) == 0 && sobremortalidad.compareTo( tablaExperiencia.getKsobremort() ) == 0 && sobreriesgo.compareTo( tablaExperiencia.getKsobreries() ) == 0 ){
				tablas.add( tablaExperiencia );
			}
		}
		
		return tablas;
	}

	private Comparator<TablaExperiencia> getNacimientoComparator() {
		Comparator<TablaExperiencia> comparatorByKey = new Comparator<TablaExperiencia>() {
			@Override
			public int compare(TablaExperiencia v1,
					TablaExperiencia v2) {
				return v1.getKnacimiento().compareTo( v2.getKnacimiento() );
			}
		};
		return comparatorByKey;
	}
}
