package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
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
import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.ValoresConstantesRescateKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionesAuxiliares;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.Tab923;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.ValoresConstantesRescate;

public class Tab923Dao extends DaoBase implements Map<Tab923Key, Tab923>{

private static final String CACHE_NAME = "TAB923"; 
private final ValueExtractor modalidadExtractor; 
private final ValueExtractor finipExtractor;
private final ValueExtractor ffinpExtractor;
	
	public Tab923Dao() {		
		super();
		super.setCacheName(CACHE_NAME);
		
		modalidadExtractor = createExtractor("getModalidad", Integer.class, Tab923.IND_MODALIDAD);
		finipExtractor = createExtractor("getFinip", Timestamp.class, Tab923.IND_FINIP);
		ffinpExtractor = createExtractor("getFfinp", Timestamp.class, Tab923.IND_FFINP);
	}

	@Override
	public Set<Entry<Tab923Key, Tab923>> entrySet() {
		return (Set<Entry<Tab923Key, Tab923>>) this.getCache().entrySet();
	}

	@Override
	public Tab923 get(Object key) {
		if (!(key instanceof Tab923Key)) {
			return null;
		} else {
			return (Tab923) this.getCache().get(key);
		}
	}

	@Override
	public Set<Tab923Key> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public Tab923 put(Tab923Key key, Tab923 tab923) {
		 this.getCache().put(key, tab923);
		 return tab923;
	}

	@Override
	public Tab923 remove(Object key) {
		return (Tab923) this.getCache().remove(key);
	}

	@Override
	public Collection<Tab923> values() {
		return (Collection<Tab923>) this.getCache().values();
	}
	
	public List<Tab923> getValue(Integer modalidad, Timestamp finip, Timestamp ffinp) {	
		Filter filtroModalidad = new EqualsFilter(modalidadExtractor, modalidad);
		Filter filtroFinip = new LessEqualsFilter(finipExtractor, finip.getTime());
		Filter filtroFfinp = new LessEqualsFilter(ffinpExtractor, ffinp.getTime());
		
		Set<Entry<Tab923Key, Tab923>> entries = (Set<Entry<Tab923Key, Tab923>>) this.getCache().entrySet(new AllFilter(new Filter[]{filtroModalidad, filtroFinip, filtroFfinp}), constantesRTEOrdered);
		
		List<Tab923> valores = new ArrayList<Tab923>();
		if (entries != null) {
			for (Entry<Tab923Key, Tab923> entry : entries) {
				valores.add(entry.getValue());
			}
		}
		return valores;
	}
	
	private static Comparator<Tab923> constantesRTEOrdered = new Comparator<Tab923>() {
		@Override
		public int compare(Tab923 o1, Tab923 o2) {
			return o1.getFfinp().compareTo(o2.getFfinp());
		}
	};


}