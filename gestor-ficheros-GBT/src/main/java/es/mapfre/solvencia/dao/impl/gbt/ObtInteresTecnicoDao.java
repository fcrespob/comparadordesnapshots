package es.mapfre.solvencia.dao.impl.gbt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tangosol.util.Filter;
import com.tangosol.util.filter.AllFilter;

import es.mapfre.solvencia.coherence.keys.gbt.ObtInteresTecnicoKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.gbt.ObtInteresTecnico;

public class ObtInteresTecnicoDao extends DaoBase implements Map<ObtInteresTecnicoKey, ObtInteresTecnico> {

	private static final String CACHE_NAME = "AIT0"; 
	
	public ObtInteresTecnicoDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public Set<java.util.Map.Entry<ObtInteresTecnicoKey, ObtInteresTecnico>> entrySet() {
		return (Set<Entry<ObtInteresTecnicoKey, ObtInteresTecnico>>) this.getCache().entrySet();
	}

	@Override
	public ObtInteresTecnico get(Object arg0) {
		if (!(arg0 instanceof ObtInteresTecnicoKey)) {
			return null;
		} else {
			return (ObtInteresTecnico) this.getCache().get(arg0);
		}
	}

	@Override
	public Set<ObtInteresTecnicoKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public ObtInteresTecnico put(ObtInteresTecnicoKey arg0,
			ObtInteresTecnico arg1) {
		this.getCache().put(arg0, arg1);
		return arg1;
	}

	@Override
	public ObtInteresTecnico remove(Object arg0) {
		return (ObtInteresTecnico) this.getCache().remove(arg0);
	}

	@Override
	public Collection<ObtInteresTecnico> values() {
		return (Collection<ObtInteresTecnico>) this.getCache().values();
	}
	
	/*
	public List<ObtInteresTecnico> obtenerIntTecnico(String kcriterioit)  {
		
		Filter allFilter = null;
		
		List<Filter> filtros = new ArrayList<Filter>();
		
		filtros.add(new EqualsFilter(kcriterioitExtractor, kcriterioit));
		
		Filter[] arrayFiltros = filtros.toArray(new Filter[filtros.size()]);
		
		allFilter = new AllFilter(arrayFiltros);
		
		List<ObtInteresTecnico> intTecnico = getIntTecnico(allFilter);
		
		return intTecnico;
	}
	*/
	
	private Filter reducirFiltro (Filter[] filter, int contador) {
		
		Filter filtroReducido = null;
		
		Filter[] arrayReducido = new Filter[filter.length-contador];
		
		System.arraycopy(filter, 0, arrayReducido, 0, filter.length-contador);
		
		filtroReducido = new AllFilter(arrayReducido);
		
		return filtroReducido;
	}
	
	public List<ObtInteresTecnico> getIntTecnico(Filter allFilter) {
		
		List<ObtInteresTecnico> intTecnico = new ArrayList<ObtInteresTecnico>();
		
		Set values = this.getCache().entrySet(allFilter);
		
			Iterator iter = values.iterator();
			while(iter.hasNext()){
			    Map.Entry entry = (Map.Entry) iter.next();
			    intTecnico.add((ObtInteresTecnico) entry.getValue());
			}
		
		return intTecnico;
	}
}
