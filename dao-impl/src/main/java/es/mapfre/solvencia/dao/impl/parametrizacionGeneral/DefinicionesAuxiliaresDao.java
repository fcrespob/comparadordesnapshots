package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

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
import com.tangosol.util.filter.IsNullFilter;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.DefinicionesAuxiliaresKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionesAuxiliares;

public class DefinicionesAuxiliaresDao extends DaoBase implements Map<DefinicionesAuxiliaresKey, DefinicionesAuxiliares>{

private static final String CACHE_NAME = "AUX0"; 

	private final ValueExtractor carteraOrigenExtractor;
	private final ValueExtractor modalidadExtractor;
	private final ValueExtractor garantiaExtractor;
	private final ValueExtractor basetecExtractor;
	private final ValueExtractor nombreVariableExtractor;

	public DefinicionesAuxiliaresDao() {
		super();
		super.setCacheName(CACHE_NAME);
		carteraOrigenExtractor  = createExtractor("getKcarteorig",Integer.class, DefinicionesAuxiliares.IND_KCARTEORIG);
		modalidadExtractor      = createExtractor("getKmodalidad",Integer.class, DefinicionesAuxiliares.IND_KMODALIDAD);
		garantiaExtractor       = createExtractor("getKgarantia",Integer.class, DefinicionesAuxiliares.IND_KGARANTIA);
		basetecExtractor       = createExtractor("getKbasetec",String.class, DefinicionesAuxiliares.IND_KBASETEC);
		nombreVariableExtractor = createExtractor("getCidentivariab",String.class, DefinicionesAuxiliares.IND_CIDENTIVARIAB);
		
		getCache().addIndex(carteraOrigenExtractor, false, null);
		getCache().addIndex(modalidadExtractor, false, null);
		getCache().addIndex(garantiaExtractor, false, null);
		getCache().addIndex(basetecExtractor, false, null);
		getCache().addIndex(nombreVariableExtractor, false, null);
	}

	@Override
	public DefinicionesAuxiliares get(Object key) {
		if (key instanceof DefinicionesAuxiliaresKey) {
			return (DefinicionesAuxiliares) getCache().get(key);
		}else{
			return null;	
		}
	}

	@Override
	public Set<DefinicionesAuxiliaresKey> keySet() {
		return (Set<DefinicionesAuxiliaresKey>) this.getCache().keySet();
	}

	@Override
	public DefinicionesAuxiliares put(DefinicionesAuxiliaresKey key, DefinicionesAuxiliares definicionesAuxiliares) {
		 return (DefinicionesAuxiliares) this.getCache().put(key, definicionesAuxiliares);
	}

	@Override
	public DefinicionesAuxiliares remove(Object key) {
		return (DefinicionesAuxiliares) this.getCache().remove(key);
	}

	@Override
	public Collection<DefinicionesAuxiliares> values() {
		return (Collection<DefinicionesAuxiliares>) this.getCache().values();
	}
	
	@Override
	public Set<Entry<DefinicionesAuxiliaresKey, DefinicionesAuxiliares>> entrySet() {
		return (Set<Entry<DefinicionesAuxiliaresKey, DefinicionesAuxiliares>>) this.getCache().entrySet();
	}
		
	public List<DefinicionesAuxiliares> getValue(Integer carteraOrigen, Integer modalidad, Integer garantia, String basetec, String nombreVariable) {
		List<Filter> filtros = new ArrayList<Filter>();
		
		if(carteraOrigen!= null){
			filtros.add(new EqualsFilter(carteraOrigenExtractor, carteraOrigen));
		} else {
			filtros.add(new IsNullFilter("getKcarteorig"));
		}
		
		if(modalidad!= null){
			filtros.add(new EqualsFilter(modalidadExtractor, modalidad));
		} else {
			filtros.add(new IsNullFilter("getKmodalidad"));
		}
		
		if(garantia!= null){
			filtros.add(new EqualsFilter(garantiaExtractor, garantia));
		} else {
			filtros.add(new IsNullFilter("getKgarantia"));
		}
		
		if(basetec!= null){
			filtros.add(new EqualsFilter(basetecExtractor, basetec));
		} else {
			filtros.add(new IsNullFilter("getKbasetec"));
		}
		
		if(nombreVariable!= null){
			filtros.add(new EqualsFilter(nombreVariableExtractor, nombreVariable));
		}	
		
		Filter[] arrayFiltros = new Filter[filtros.size()];
		for (int i = 0; i < arrayFiltros.length; i++) {
			arrayFiltros[i] = filtros.get(i);
		}
		
		Filter allFilter = new AllFilter(arrayFiltros);
		Set keys = this.getCache().keySet(allFilter);
		Set values = this.getCache().getAll(keys).entrySet();
		
		List<DefinicionesAuxiliares> defAuxs=new ArrayList<DefinicionesAuxiliares>();
		
		Iterator iter = values.iterator();
		while(iter.hasNext()){
		    Map.Entry entry = (Map.Entry) iter.next();
		    defAuxs.add( (DefinicionesAuxiliares) entry.getValue());
		}
	
		return defAuxs;
	}
	
	public DefinicionesAuxiliares getValue(String nombreVariable) {
		
		Filter nombreVariableFilter = new EqualsFilter(nombreVariableExtractor, nombreVariable);
		Set keys = this.getCache().keySet(nombreVariableFilter);
		Set values = this.getCache().getAll(keys).entrySet();

		Iterator iter = values.iterator();
		if (iter.hasNext()){
			Map.Entry entry = (Map.Entry) iter.next();
			return (DefinicionesAuxiliares) entry.getValue();
		} else {
			return null;
		}
		
	}
}