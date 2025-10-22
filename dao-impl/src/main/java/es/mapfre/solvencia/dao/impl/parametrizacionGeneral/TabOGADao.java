package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.EqualsFilter;
import com.tangosol.util.filter.GreaterEqualsFilter;
import com.tangosol.util.filter.IsNullFilter;
import com.tangosol.util.filter.LessEqualsFilter;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.Tab923Key;
import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.TabOGAKey;
import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.ValoresConstantesRescateKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionesAuxiliares;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.Tab923;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.TabOGA;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.TablaHibrida;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.ValoresConstantesRescate;

public class TabOGADao extends DaoBase implements Map<TabOGAKey, TabOGA>{

private static final String CACHE_NAME = "TABOGA"; 
private final ValueExtractor kcarteraExtractor; 
	
	public TabOGADao() {		
		super();
		super.setCacheName(CACHE_NAME);
		
		kcarteraExtractor = createExtractor("getCartera", String.class, TabOGA.IND_CARTERA);
		
		getCache().addIndex(kcarteraExtractor, false, null);
	}

	@Override
	public Set<Entry<TabOGAKey, TabOGA>> entrySet() {
		return (Set<Entry<TabOGAKey, TabOGA>>) this.getCache().entrySet();
	}

	@Override
	public TabOGA get(Object key) {
		if (!(key instanceof TabOGAKey)) {
			return null;
		} else {
			return (TabOGA) this.getCache().get(key);
		}
	}

	@Override
	public Set<TabOGAKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public TabOGA put(TabOGAKey key, TabOGA tabOGA) {
		 this.getCache().put(key, tabOGA);
		 return tabOGA;
	}

	@Override
	public TabOGA remove(Object key) {
		return (TabOGA) this.getCache().remove(key);
	}

	@Override
	public Collection<TabOGA> values() {
		return (Collection<TabOGA>) this.getCache().values();
	}
	
	public TabOGA getValue(String cartera, Timestamp fcierre) {	
		Filter filtroCartera = new EqualsFilter(kcarteraExtractor, cartera);
		
		Set<Entry<TabOGAKey, TabOGA>> entries = this.getCache().entrySet();
		
		List<TabOGA> valores = new ArrayList<TabOGA>();
		if (entries != null) {
			for (Entry<TabOGAKey, TabOGA> entry : entries) {
				if (entry.getValue().getCartera().contains(cartera)) {
					valores.add(entry.getValue());
				}
			}
		}
		
		//ordenamos por fecha
		Collections.sort(valores, Collections.reverseOrder(new Comparator<TabOGA>(){
			@Override
			public int compare(TabOGA o1, TabOGA o2) {
				return o1.getFecha().compareTo(o2.getFecha());
			}
		}));
		
		for (int i = 0; i < valores.size(); i++) {
			if (!valores.get(i).getFecha().after(fcierre)) {
				return valores.get(i);
			}
		}
		
		return valores.get(0);
	}


}