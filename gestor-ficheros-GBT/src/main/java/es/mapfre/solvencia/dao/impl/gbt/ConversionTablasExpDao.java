package es.mapfre.solvencia.dao.impl.gbt;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.EqualsFilter;
import com.tangosol.util.filter.GreaterFilter;
import com.tangosol.util.filter.LessEqualsFilter;

import es.mapfre.solvencia.coherence.keys.gbt.ConversionTablasExpKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.gbt.ConversionTablasExp;

public class ConversionTablasExpDao extends DaoBase implements Map<ConversionTablasExpKey, ConversionTablasExp> {

	private static final String CACHE_NAME = "CTE0"; 
	
	private final ValueExtractor ktipobtExtractor;
	private final ValueExtractor ktablaexpExtractor;
	private final ValueExtractor kmodalidadExtractor;
	private final ValueExtractor kgarantiaExtractor;
	private final ValueExtractor finiExtractor;
	private final ValueExtractor ffinExtractor;
	
	public ConversionTablasExpDao() {
		super();
		super.setCacheName(CACHE_NAME);
		
		ktipobtExtractor = createExtractor("getKtipobt", String.class, ConversionTablasExp.IND_KTIPOBT);
		ktablaexpExtractor = createExtractor("getKtablaexp", Integer.class, ConversionTablasExp.IND_KTABLAEXP);
		kmodalidadExtractor = createExtractor("getKmodalidad", Integer.class, ConversionTablasExp.IND_KMODALIDAD);
		kgarantiaExtractor = createExtractor("getKgarantia", Integer.class, ConversionTablasExp.IND_KGARANTIA);
		finiExtractor = createExtractor("getFini", Timestamp.class, ConversionTablasExp.IND_FINI);
		ffinExtractor = createExtractor("getFfin", Timestamp.class, ConversionTablasExp.IND_FFIN);
	}

	@Override
	public Set<java.util.Map.Entry<ConversionTablasExpKey, ConversionTablasExp>> entrySet() {
		return (Set<Entry<ConversionTablasExpKey, ConversionTablasExp>>) this.getCache().entrySet();
	}

	@Override
	public ConversionTablasExp get(Object key) {
		if (!(key instanceof ConversionTablasExpKey)) {
			return null;
		} else {
			return (ConversionTablasExp) this.getCache().get(key);
		}
	}

	@Override
	public Set<ConversionTablasExpKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public ConversionTablasExp put(ConversionTablasExpKey key, ConversionTablasExp conversionTablasExp) {
		this.getCache().put(key, conversionTablasExp);
		return conversionTablasExp;
	}

	@Override
	public ConversionTablasExp remove(Object key) {
		return (ConversionTablasExp) this.getCache().remove(key);
	}

	@Override
	public Collection<ConversionTablasExp> values() {
		return (Collection<ConversionTablasExp>) this.getCache().values();
	}
	
	public List<ConversionTablasExp> obtenerConversionTablasExp(String ktipobt, Integer ktablaexp,
			Integer kmodalidad, Integer kgarantia, Timestamp fcierre)  {
		
		Filter allFilter = null;
		
		List<Filter> filtros = new ArrayList<Filter>();
		
		filtros.add(new EqualsFilter(ktipobtExtractor, ktipobt));
		filtros.add(new EqualsFilter(ktablaexpExtractor, ktablaexp));
		filtros.add(new EqualsFilter(kmodalidadExtractor, kmodalidad));
		filtros.add(new EqualsFilter(kgarantiaExtractor, kgarantia));
		filtros.add(new LessEqualsFilter(finiExtractor, fcierre.getTime()));
		filtros.add(new GreaterFilter(ffinExtractor, fcierre.getTime()));
		
		Filter[] arrayFiltros = filtros.toArray(new Filter[filtros.size()]);
		
		allFilter = new AllFilter(arrayFiltros);
		
		List<ConversionTablasExp> conversionTablasExp = getTablasExp(allFilter);
		
		return conversionTablasExp;
	}
	
	private Filter reducirFiltro (Filter[] filter, int contador) {
		
		Filter filtroReducido = null;
		
		Filter[] arrayReducido = new Filter[filter.length-contador];
		
		System.arraycopy(filter, 0, arrayReducido, 0, filter.length-contador);
		
		filtroReducido = new AllFilter(arrayReducido);
		
		return filtroReducido;
	}
	
	public List<ConversionTablasExp> getTablasExp(Filter allFilter) {
		
		List<ConversionTablasExp> tablasExp=new ArrayList<ConversionTablasExp>();
		
		Set values = this.getCache().entrySet(allFilter);
		
			Iterator iter = values.iterator();
			while(iter.hasNext()){
			    Map.Entry entry = (Map.Entry) iter.next();
			    tablasExp.add((ConversionTablasExp) entry.getValue());
			}
		
		return tablasExp;
	}

}
