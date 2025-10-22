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
import com.tangosol.util.filter.LessEqualsFilter;

import es.mapfre.solvencia.coherence.keys.gbt.PeriodosAdKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.gbt.PeriodosAd;

public class PeriodosAdDao extends DaoBase implements Map<PeriodosAdKey, PeriodosAd> {

	private static final String CACHE_NAME = "MAD0";
	
	private final ValueExtractor kmadaptacExtractor;
	private final ValueExtractor finiExtractor;
	private final ValueExtractor ffinExtractor;
	
	public PeriodosAdDao() {
		super();
		super.setCacheName(CACHE_NAME);
		
		kmadaptacExtractor = createExtractor("getKmadaptac", String.class, PeriodosAd.IND_KMADAPTAC);
		finiExtractor = createExtractor("getFini", Timestamp.class, PeriodosAd.IND_FINI);
		ffinExtractor = createExtractor("getFfin", Timestamp.class, PeriodosAd.IND_FFIN);
	}

	@Override
	public Set<java.util.Map.Entry<PeriodosAdKey, PeriodosAd>> entrySet() {
		return (Set<Entry<PeriodosAdKey, PeriodosAd>>) this.getCache().entrySet();
	}

	@Override
	public PeriodosAd get(Object key) {
		if (!(key instanceof PeriodosAdKey)) {
			return null;
		} else {
			return (PeriodosAd) this.getCache().get(key);
		}
	}

	@Override
	public Set<PeriodosAdKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public PeriodosAd put(PeriodosAdKey key, PeriodosAd periodosAd) {
		this.getCache().put(key, periodosAd);
		return periodosAd;
	}

	@Override
	public PeriodosAd remove(Object key) {
		return (PeriodosAd) this.getCache().remove(key);
	}

	@Override
	public Collection<PeriodosAd> values() {
		return (Collection<PeriodosAd>) this.getCache().values();
	}
	
	public List<PeriodosAd> obtenerPeriodosAd(String kmadaptac, Timestamp fcierre)  {
		
		Filter allFilter = null;
		
		List<Filter> filtros = new ArrayList<Filter>();
		
		filtros.add(new EqualsFilter(kmadaptacExtractor, kmadaptac));
		filtros.add(new LessEqualsFilter(finiExtractor, fcierre.getTime()));
		filtros.add(new GreaterEqualsFilter(ffinExtractor, fcierre.getTime()));
		
		Filter[] arrayFiltros = filtros.toArray(new Filter[filtros.size()]);
		
		allFilter = new AllFilter(arrayFiltros);
		
		List<PeriodosAd> periodoaAd = getPeriodosAd(allFilter);
		
		return periodoaAd;
	}
	
	// Se obtienen los periodos unicamente por metodo, sin tener en cuenta la fecha
	public List<PeriodosAd> obtenerPeriodosAd(String kmadaptac)  {
		
		Filter allFilter = null;
		
		List<Filter> filtros = new ArrayList<Filter>();
		
		filtros.add(new EqualsFilter(kmadaptacExtractor, kmadaptac));
		
		Filter[] arrayFiltros = filtros.toArray(new Filter[filtros.size()]);
		
		allFilter = new AllFilter(arrayFiltros);
		
		List<PeriodosAd> periodoaAd = getPeriodosAd(allFilter);
		
		return periodoaAd;
	}
	
	private Filter reducirFiltro (Filter[] filter, int contador) {
		
		Filter filtroReducido = null;
		
		Filter[] arrayReducido = new Filter[filter.length-contador];
		
		System.arraycopy(filter, 0, arrayReducido, 0, filter.length-contador);
		
		filtroReducido = new AllFilter(arrayReducido);
		
		return filtroReducido;
	}
	
	public List<PeriodosAd> getPeriodosAd(Filter allFilter) {
		
		List<PeriodosAd> tablasExp=new ArrayList<PeriodosAd>();
		
		Set values = this.getCache().entrySet(allFilter);
		
			Iterator iter = values.iterator();
			while(iter.hasNext()){
			    Map.Entry entry = (Map.Entry) iter.next();
			    tablasExp.add((PeriodosAd) entry.getValue());
			}
		
		return tablasExp;
	}
}
