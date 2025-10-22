package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.EqualsFilter;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.TablaExperienciaKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.TablaExperiencia;

@Portable
public class TablaExperienciaDao extends DaoBase implements Map<TablaExperienciaKey, TablaExperiencia>{
	
	
	private static final String CACHE_NAME = "CTM0"; 
	
	private final ValueExtractor ktablaExtractor;
	private final ValueExtractor k2tipovalortExtractor;
	private final ValueExtractor kanacimientotExtractor;
	private final ValueExtractor kinteresExtractor;
	private final ValueExtractor ksobremortExtractor;
	private final ValueExtractor ksobreriesExtractor;

	public TablaExperienciaDao() {
		super();
		super.setCacheName(CACHE_NAME);
		
		ktablaExtractor = createExtractor("getKtabla",Integer.class, TablaExperiencia.IND_KTABLA);
		k2tipovalortExtractor = createExtractor("getK2tipovalor",String.class, TablaExperiencia.IND_K2TIPOVALOR);
		kanacimientotExtractor = createExtractor("",String.class, TablaExperiencia.IND_KANACIMIENTO);
		kinteresExtractor= createExtractor("getKinteres",java.math.BigDecimal.class, TablaExperiencia.IND_KINTERES);
		ksobremortExtractor = createExtractor("getKsobremort",java.math.BigDecimal.class, TablaExperiencia.IND_KSOBREMORT);
		ksobreriesExtractor = createExtractor("getKanacimientogetKsobreries",java.math.BigDecimal.class, TablaExperiencia.IND_KSOBRERIES);
		
		this.getCache().addIndex(ktablaExtractor, false, null);
		this.getCache().addIndex(k2tipovalortExtractor, false, null);
		this.getCache().addIndex(kanacimientotExtractor, false, null);
		this.getCache().addIndex(kinteresExtractor, false, null);
		this.getCache().addIndex(ksobremortExtractor, false, null);
		this.getCache().addIndex(ksobreriesExtractor, false, null);
	}

	@Override
	public Set<Entry<TablaExperienciaKey, TablaExperiencia>> entrySet() {
		return (Set<Entry<TablaExperienciaKey, TablaExperiencia>>) this.getCache().entrySet();
	}

	@Override
	public TablaExperiencia get(Object key) {
		if (!(key instanceof TablaExperienciaKey)) {
			return null;
		} else {
			return (TablaExperiencia) this.getCache().get(key);
		}
	}

	@Override
	public Set<TablaExperienciaKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public TablaExperiencia put(TablaExperienciaKey key, TablaExperiencia tablaExperiencia) {
		 this.getCache().put(key, tablaExperiencia);
		 return tablaExperiencia;
	}

	@Override
	public TablaExperiencia remove(Object key) {
		return (TablaExperiencia) this.getCache().remove(key);
	}

	@Override
	public Collection<TablaExperiencia> values() {
		return (Collection<TablaExperiencia>) this.getCache().values();
	}
	
	
	private Filter createFilters(TablaExperiencia configValoresTabla){
		
		Filter allFilter = null;
		if(configValoresTabla != null){
			List<Filter> filtros = new ArrayList<Filter>();
			
			if(configValoresTabla.getKtabla()!=null){
				filtros.add(new EqualsFilter(ktablaExtractor, configValoresTabla.getKtabla()) );
			}
			
			if(configValoresTabla.getK2tipovalor()!=null){
				filtros.add(new EqualsFilter(k2tipovalortExtractor, configValoresTabla.getK2tipovalor()) );
			}
			
			if(configValoresTabla.getKanacimiento()!=null){
				filtros.add(new EqualsFilter(kanacimientotExtractor, configValoresTabla.getKanacimiento()) );
			}
			
			if(configValoresTabla.getKinteres()!=null){
				filtros.add(new EqualsFilter(kinteresExtractor, configValoresTabla.getKinteres()) );
			}
			
			if(configValoresTabla.getKsobremort()!=null){
				filtros.add(new EqualsFilter(ksobremortExtractor, configValoresTabla.getKsobremort()) );
			}
			
			if(configValoresTabla.getKsobreries()!=null){
				filtros.add(new EqualsFilter(ksobreriesExtractor, configValoresTabla.getKsobreries()) );
			}
			
			Filter[] arrayFiltros = new Filter[filtros.size()];
			
			for (int i = 0; i < arrayFiltros.length; i++) {
				arrayFiltros[i] = filtros.get(i);
			}
			
			allFilter = new AllFilter(arrayFiltros);
		
		}
		
		return allFilter;
		
	}
	
	public List<TablaExperiencia> getValue(TablaExperiencia configValoresTabla) {
		
		List<TablaExperiencia> tablas= new ArrayList<TablaExperiencia>();

		Set entries = this.getCache().entrySet(createFilters(configValoresTabla));
		Iterator iter = entries.iterator();
		while(iter.hasNext()){
		    Map.Entry entry = (Map.Entry) iter.next();
		    tablas.add( (TablaExperiencia) entry.getValue() );
		}
		return tablas;
	}
}