package es.mapfre.solvencia.dao.impl.gbt;

/* MODIFICACION : 100405260 SOLVENCIA II - CÁLCULO Y GENERACIÓN DE FLUJOS FASE V
FECHA : 20/11/2015
DESCRIPCION:SOLV2VIDA Cambio de tipo de dato en kpoliza y GBT 
AUTOR : JVV */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tangosol.util.Filter;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.EqualsFilter;

import es.mapfre.solvencia.coherence.keys.gbt.ErrorOrquestadorKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.gbt.ErrorOrquestador;

public class ErrorOrquestadorDao extends DaoBaseSalidaCalculo implements Map<ErrorOrquestadorKey, ErrorOrquestador> {
	private static final String CACHE_NAME = "error-orquestador"; 
	
	
	public ErrorOrquestadorDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public Set<java.util.Map.Entry<ErrorOrquestadorKey, ErrorOrquestador>> entrySet() {
		return (Set<Entry<ErrorOrquestadorKey, ErrorOrquestador>>) this.getCache().entrySet();
	}

	@Override
	public ErrorOrquestador get(Object key) {
		if (!(key instanceof ErrorOrquestador)) {
			return null;
		} else {
			return (ErrorOrquestador) this.getCache().get(key);
		}
	}

	@Override
	public Set<ErrorOrquestadorKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public ErrorOrquestador put(ErrorOrquestadorKey arg0,
			ErrorOrquestador arg1) {
		this.getCache().put(arg0, arg1);
		return arg1;
	}

	@Override
	public ErrorOrquestador remove(Object arg0) {
		return (ErrorOrquestador) this.getCache().remove(arg0);
	}

	@Override
	public Collection<ErrorOrquestador> values() {
		return (Collection<ErrorOrquestador>) this.getCache().values();
	}
//100405260-INI	
//	public List<ErrorOrquestador> obtenerError(Integer kmodalidad, Integer kpoliza,
//			Integer ksubpoliza, Integer kcertificado, Integer nsuscri,
//			Integer norden, Integer kgarantia, String kprestacion,
//			Integer kajuste, String ctipoaport, String nomModulo, String descripcionErr)  {
	public List<ErrorOrquestador> obtenerError(Integer kmodalidad, Long kpoliza,
			Integer ksubpoliza, Integer kcertificado, Integer nsuscri,
			Integer norden, Integer kgarantia, String kprestacion,
			Integer kajuste, String ctipoaport, String nomModulo, String descripcionErr)  {
//100405260-FIN			
		Filter allFilter = null;
		
		List<Filter> filtros = new ArrayList<Filter>();
		
		filtros.add(new EqualsFilter("getKmodalidad", kmodalidad));
		filtros.add(new EqualsFilter("getKpoliza", kpoliza));
		filtros.add(new EqualsFilter("getKsubpoliza", ksubpoliza));
		filtros.add(new EqualsFilter("getKcertificado", kcertificado));
		filtros.add(new EqualsFilter("getNsuscri", nsuscri));
		filtros.add(new EqualsFilter("getNorden", norden));
		filtros.add(new EqualsFilter("getKgarantia", kgarantia));
		filtros.add(new EqualsFilter("getKprestacion", kprestacion));
		filtros.add(new EqualsFilter("getKajuste", kajuste));
		filtros.add(new EqualsFilter("getCtipoaport", ctipoaport));
		filtros.add(new EqualsFilter("getNomModulo", nomModulo));
		filtros.add(new EqualsFilter("getDescripcionErr", descripcionErr));
		
		Filter[] arrayFiltros = filtros.toArray(new Filter[filtros.size()]);
		
		allFilter = new AllFilter(arrayFiltros);
		
		List<ErrorOrquestador> metAd = getError(allFilter);
		
		return metAd;
	}
	
	private Filter reducirFiltro (Filter[] filter, int contador) {
		
		Filter filtroReducido = null;
		
		Filter[] arrayReducido = new Filter[filter.length-contador];
		
		System.arraycopy(filter, 0, arrayReducido, 0, filter.length-contador);
		
		filtroReducido = new AllFilter(arrayReducido);
		
		return filtroReducido;
	}
	
	public List<ErrorOrquestador> getError(Filter allFilter) {
		
		List<ErrorOrquestador> metAd=new ArrayList<ErrorOrquestador>();
		
		Set values = this.getCache().entrySet(allFilter);
		
			Iterator iter = values.iterator();
			while(iter.hasNext()){
			    Map.Entry entry = (Map.Entry) iter.next();
			    metAd.add((ErrorOrquestador) entry.getValue());
			}
		
		return metAd;
	}
}
