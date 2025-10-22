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
import com.tangosol.util.filter.GreaterEqualsFilter;
import com.tangosol.util.filter.GreaterFilter;
import com.tangosol.util.filter.LessEqualsFilter;

import es.mapfre.solvencia.coherence.keys.gbt.InteresTecnicoKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.gbt.InteresTecnico;

public class InteresTecnicoDao extends DaoBase implements Map<InteresTecnicoKey, InteresTecnico> {

	private static final String CACHE_NAME = "ITR0"; 

	private final ValueExtractor finiExtractor;
	private final ValueExtractor ffinExtractor;
	
	public InteresTecnicoDao() {
		super();
		super.setCacheName(CACHE_NAME);

		finiExtractor = createExtractor("getFini", Timestamp.class, InteresTecnico.IND_FINI);
		ffinExtractor = createExtractor("getFfin", Timestamp.class, InteresTecnico.IND_FFIN);
	}

	@Override
	public Set<java.util.Map.Entry<InteresTecnicoKey, InteresTecnico>> entrySet() {
		return (Set<Entry<InteresTecnicoKey, InteresTecnico>>) this.getCache().entrySet();
	}

	@Override
	public InteresTecnico get(Object arg0) {
		if (!(arg0 instanceof InteresTecnicoKey)) {
			return null;
		} else {
			return (InteresTecnico) this.getCache().get(arg0);
		}
	}

	@Override
	public Set<InteresTecnicoKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public InteresTecnico put(InteresTecnicoKey arg0, InteresTecnico arg1) {
		this.getCache().put(arg0, arg1);
		return arg1;
	}

	@Override
	public InteresTecnico remove(Object arg0) {
		return (InteresTecnico) this.getCache().remove(arg0);
	}

	@Override
	public Collection<InteresTecnico> values() {
		return (Collection<InteresTecnico>) this.getCache().values();
	}

	public List<InteresTecnico> obtenerInteresTencico(Timestamp fcierre)  {
		
		Filter allFilter = null;
		
		List<Filter> filtros = new ArrayList<Filter>();
		
		filtros.add(new LessEqualsFilter(finiExtractor, fcierre.getTime()));
		filtros.add(new GreaterFilter(ffinExtractor, fcierre.getTime()));
		
		Filter[] arrayFiltros = filtros.toArray(new Filter[filtros.size()]);
		
		allFilter = new AllFilter(arrayFiltros);
		
		List<InteresTecnico> intTecnico = getIntTecnico(allFilter);
		
		return intTecnico;
	}
	
	public List<InteresTecnico> obtenerInteresTencicoIT05(Timestamp fcierre)  {
		
		Filter allFilter = null;
		
		List<Filter> filtros = new ArrayList<Filter>();
		
		filtros.add(new LessEqualsFilter(finiExtractor, fcierre.getTime()));
		filtros.add(new GreaterFilter(ffinExtractor, fcierre.getTime()));
		
		Filter[] arrayFiltros = filtros.toArray(new Filter[filtros.size()]);
		
		allFilter = new AllFilter(arrayFiltros);
		
		List<InteresTecnico> intTecnico = getIntTecnico(allFilter);
		
		return intTecnico;
	}
	
	private Filter reducirFiltro (Filter[] filter, int contador) {
		
		Filter filtroReducido = null;
		
		Filter[] arrayReducido = new Filter[filter.length-contador];
		
		System.arraycopy(filter, 0, arrayReducido, 0, filter.length-contador);
		
		filtroReducido = new AllFilter(arrayReducido);
		
		return filtroReducido;
	}
	
	public List<InteresTecnico> obtenerInteresTencicoCoaseguro(Timestamp fcierre)  {
		
		Filter allFilter = null;
		
		List<Filter> filtros = new ArrayList<Filter>();
		
		filtros.add(new LessEqualsFilter(finiExtractor, fcierre.getTime()));
		filtros.add(new GreaterEqualsFilter(ffinExtractor, fcierre.getTime()));
		
		Filter[] arrayFiltros = filtros.toArray(new Filter[filtros.size()]);
		
		allFilter = new AllFilter(arrayFiltros);
		
		List<InteresTecnico> intTecnico = getIntTecnico(allFilter);
		
		return intTecnico;
	}
	
	public List<InteresTecnico> getIntTecnico(Filter allFilter) {
		
		List<InteresTecnico> intTecnico=new ArrayList<InteresTecnico>();
		
		Set values = this.getCache().entrySet(allFilter);
		
			Iterator iter = values.iterator();
			while(iter.hasNext()){
			    Map.Entry entry = (Map.Entry) iter.next();
			    intTecnico.add((InteresTecnico) entry.getValue());
			}
		
		return intTecnico;
	}
}
