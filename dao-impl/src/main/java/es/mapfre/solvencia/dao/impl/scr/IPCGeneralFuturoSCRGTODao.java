package es.mapfre.solvencia.dao.impl.scr;

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
import com.tangosol.util.filter.GreaterFilter;
import com.tangosol.util.filter.LessEqualsFilter;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.IPCGeneralFuturoKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.IPCGeneralFuturo;

public class IPCGeneralFuturoSCRGTODao extends DaoBase implements Map<IPCGeneralFuturoKey, IPCGeneralFuturo>{

	private static final String CACHE_NAME = "SCRIPC0"; 
	
	private final ValueExtractor finicioExtractor;
	private final ValueExtractor ffinExtractor;

	public IPCGeneralFuturoSCRGTODao() {
		super();
		super.setCacheName(CACHE_NAME);
		
		ffinExtractor  = createExtractor("getFfin",Timestamp.class, IPCGeneralFuturo.IND_FFIN);
		finicioExtractor  = createExtractor("getFinicio",Timestamp.class, IPCGeneralFuturo.IND_FINICIO);
		
		super.getCache().addIndex(finicioExtractor, true, null);
		super.getCache().addIndex(ffinExtractor, true, null);
	}

	@Override
	public Set<Entry<IPCGeneralFuturoKey, IPCGeneralFuturo>> entrySet() {
		return (Set<Entry<IPCGeneralFuturoKey, IPCGeneralFuturo>>) this.getCache().entrySet();
	}

	@Override
	public IPCGeneralFuturo get(Object key) {
		if (!(key instanceof IPCGeneralFuturoKey)) {
			return null;
		} else {
			return (IPCGeneralFuturo) this.getCache().get(key);
		}
	}

	@Override
	public Set<IPCGeneralFuturoKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public IPCGeneralFuturo put(IPCGeneralFuturoKey key, IPCGeneralFuturo ipcGeneralFuturo) {
		 this.getCache().put(key, ipcGeneralFuturo);
		 return ipcGeneralFuturo;
	}

	@Override
	public IPCGeneralFuturo remove(Object key) {
		return (IPCGeneralFuturo) this.getCache().remove(key);
	}

	@Override
	public Collection<IPCGeneralFuturo> values() {
		return (Collection<IPCGeneralFuturo>) this.getCache().values();
	}

	public List<IPCGeneralFuturo> getValues(Timestamp fecCierre)  {
		
		Filter ffinFilter = new GreaterFilter(ffinExtractor, fecCierre.getTime());
		Filter finiFilter = new LessEqualsFilter(finicioExtractor, fecCierre.getTime());
		Filter allFilter = new AllFilter(new Filter[]{ffinFilter, finiFilter});
		
		Set lista = this.getCache().entrySet(allFilter);
		
		List<IPCGeneralFuturo> values=new ArrayList<IPCGeneralFuturo>();
		
		Iterator iter = lista.iterator();
		while(iter.hasNext()){
		    Map.Entry entry = (Map.Entry) iter.next();
		    values.add( (IPCGeneralFuturo) entry.getValue() );
		}
		
		return values;
	}
}
