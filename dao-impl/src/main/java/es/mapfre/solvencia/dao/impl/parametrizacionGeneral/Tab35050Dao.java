package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.EqualsFilter;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.Tab35050Key;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.Tab35050;

public class Tab35050Dao extends DaoBase implements Map<Tab35050Key, Tab35050>{

private static final String CACHE_NAME = "TAB35050"; 
private ValueExtractor kramoExtractor = null;
private ValueExtractor kmodalidadExtractor = null;
	
	public Tab35050Dao() {		
		super();
		super.setCacheName(CACHE_NAME);
		
		kramoExtractor = createExtractor("getKramo", String.class, Tab35050.IND_KRAMO);
		kmodalidadExtractor = createExtractor("getKmodalidad", Integer.class, Tab35050.IND_KMODALIDAD);
		
	}

	@Override
	public Set<Entry<Tab35050Key, Tab35050>> entrySet() {
		return (Set<Entry<Tab35050Key, Tab35050>>) this.getCache().entrySet();
	}

	@Override
	public Tab35050 get(Object key) {
		if (!(key instanceof Tab35050Key)) {
			return null;
		} else {
			return (Tab35050) this.getCache().get(key);
		}
	}

	@Override
	public Set<Tab35050Key> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public Tab35050 put(Tab35050Key key, Tab35050 tab35050) {
		 this.getCache().put(key, tab35050);
		 return tab35050;
	}

	@Override
	public Tab35050 remove(Object key) {
		return (Tab35050) this.getCache().remove(key);
	}

	@Override
	public Collection<Tab35050> values() {
		return (Collection<Tab35050>) this.getCache().values();
	}
	
	public List<Tab35050> getValue(String kramo, Integer kmodalidad) {	
		List<Filter> filtros = new ArrayList<Filter>();
		filtros.add( new EqualsFilter(kramoExtractor, kramo));
		filtros.add( new EqualsFilter(kmodalidadExtractor, kmodalidad));
		
		Filter[] filtrosArray = new Filter[filtros.size()];
		for (int i = 0; i < filtrosArray.length; i++) {
			filtrosArray[i]=filtros.get(i);
		}
	
		Filter allFilter = new AllFilter(filtrosArray);
		
		Set<Entry<Tab35050Key, Tab35050>> entries = (Set<Entry<Tab35050Key, Tab35050>>) this.getCache().entrySet(allFilter);
		List<Tab35050> values = new ArrayList<Tab35050>();
		
		if (entries != null) {
			for (Entry<Tab35050Key, Tab35050> entry : entries) {
				values.add(entry.getValue());
			}
		}
		return values;
	}


}
