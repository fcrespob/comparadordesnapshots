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
import com.tangosol.util.filter.GreaterEqualsFilter;
import com.tangosol.util.filter.GreaterFilter;
import com.tangosol.util.filter.LessEqualsFilter;

import es.mapfre.solvencia.coherence.keys.gbt.DiferencialGastosKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.gbt.DiferencialGastos;

public class DiferencialGastosDao extends DaoBase implements Map<DiferencialGastosKey, DiferencialGastos> {
	
	private static final String CACHE_NAME = "DGI0"; 
	
	private final ValueExtractor ktipobtExtractor;
	private final ValueExtractor kmodalidadExtractor;
	private final ValueExtractor kgarantiaExtractor;
	private final ValueExtractor finiExtractor;
	private final ValueExtractor ffinExtractor;
	
	public DiferencialGastosDao() {
		super();
		super.setCacheName(CACHE_NAME);
		
		ktipobtExtractor = createExtractor("getKtipobt", String.class, DiferencialGastos.IND_KTIPOBT);
		kmodalidadExtractor = createExtractor("getKmodalidad", Integer.class, DiferencialGastos.IND_KMODALIDAD);
		kgarantiaExtractor = createExtractor("getKgarantia", Integer.class, DiferencialGastos.IND_KGARANTIA);
		finiExtractor = createExtractor("getFini", Timestamp.class, DiferencialGastos.IND_FINI);
		ffinExtractor = createExtractor("getFfin", Timestamp.class, DiferencialGastos.IND_FFIN);
	}

	@Override
	public Set<java.util.Map.Entry<DiferencialGastosKey, DiferencialGastos>> entrySet() {
		return (Set<Entry<DiferencialGastosKey, DiferencialGastos>>) this.getCache().entrySet();
	}

	@Override
	public DiferencialGastos get(Object arg0) {
		if (!(arg0 instanceof DiferencialGastosKey)) {
			return null;
		} else {
			return (DiferencialGastos) this.getCache().get(arg0);
		}
	}

	@Override
	public Set<DiferencialGastosKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public DiferencialGastos put(DiferencialGastosKey arg0,
			DiferencialGastos arg1) {
		this.getCache().put(arg0, arg1);
		return arg1;
	}

	@Override
	public DiferencialGastos remove(Object arg0) {
		return (DiferencialGastos) this.getCache().remove(arg0);
	}

	@Override
	public Collection<DiferencialGastos> values() {
		return (Collection<DiferencialGastos>) this.getCache().values();
	}

	public List<DiferencialGastos> obtenerDifGastos(String ktipobt, Integer kmodalidad,
			Integer kgarantia, Timestamp fcierre)  {
		
		Filter allFilter = null;
		
		List<Filter> filtros = new ArrayList<Filter>();
		
		filtros.add(new EqualsFilter(ktipobtExtractor, ktipobt));
		filtros.add(new EqualsFilter(kmodalidadExtractor, kmodalidad));
		filtros.add(new EqualsFilter(kgarantiaExtractor, kgarantia));
		filtros.add(new LessEqualsFilter(finiExtractor, fcierre.getTime()));
		//filtros.add(new GreaterEqualsFilter(ffinExtractor, fcierre.getTime()));
		filtros.add(new GreaterFilter(ffinExtractor, fcierre.getTime()));

		Filter[] arrayFiltros = filtros.toArray(new Filter[filtros.size()]);
		
		allFilter = new AllFilter(arrayFiltros);
		
		List<DiferencialGastos> difGastos = getDifGastos(allFilter);
		
		return difGastos;
	}
	
	private Filter reducirFiltro (Filter[] filter, int contador) {
		
		Filter filtroReducido = null;
		
		Filter[] arrayReducido = new Filter[filter.length-contador];
		
		System.arraycopy(filter, 0, arrayReducido, 0, filter.length-contador);
		
		filtroReducido = new AllFilter(arrayReducido);
		
		return filtroReducido;
	}
	
	public List<DiferencialGastos> getDifGastos(Filter allFilter) {
		
		List<DiferencialGastos> difGastos = new ArrayList<DiferencialGastos>();
		
		Set values = this.getCache().entrySet(allFilter);
		
			Iterator iter = values.iterator();
			while(iter.hasNext()){
			    Map.Entry entry = (Map.Entry) iter.next();
			    difGastos.add((DiferencialGastos) entry.getValue());
			}
		
		return difGastos;
	}
	
}
