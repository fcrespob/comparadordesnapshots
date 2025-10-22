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

import es.mapfre.solvencia.coherence.keys.gbt.AsigCurvaTipoKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.gbt.AsigCurvaTipo;

public class AsigCurvaTipoDao extends DaoBase implements Map<AsigCurvaTipoKey, AsigCurvaTipo> {

	private static final String CACHE_NAME = "ACI0"; 
	
	private final ValueExtractor kbasetecExtractor;
	private final ValueExtractor kriesgoExtractor;
	private final ValueExtractor kriesgorescateExtractor;
	private final ValueExtractor kpagounicoExtractor;
	private final ValueExtractor kcarterainvExtractor;
	private final ValueExtractor kpbExtractor;
	private final ValueExtractor kapbelExtractor;
	private final ValueExtractor kfdesdeconverExtractor;
	private final ValueExtractor fhastaconverExtractor;
	
	public AsigCurvaTipoDao() {
		super();
		super.setCacheName(CACHE_NAME);
		
		kbasetecExtractor = createExtractor("getKbasetec", String.class, AsigCurvaTipo.IND_KBASETEC);
		kriesgoExtractor = createExtractor("getKriesgo", String.class, AsigCurvaTipo.IND_KRIESGO);
		kriesgorescateExtractor = createExtractor("getKriesgorescate", String.class, AsigCurvaTipo.IND_KRIESGORESCATE);
		kpagounicoExtractor = createExtractor("getKpagounico", String.class, AsigCurvaTipo.IND_KPAGOUNICO);
		kcarterainvExtractor = createExtractor("getKcarterainv", String.class, AsigCurvaTipo.IND_KCARTERAINV);
		kpbExtractor = createExtractor("getKpb", String.class, AsigCurvaTipo.IND_KPB);
		kapbelExtractor = createExtractor("getKapbel", String.class, AsigCurvaTipo.IND_KAPBEL);
		kfdesdeconverExtractor = createExtractor("getKfdesdeconver", Timestamp.class, AsigCurvaTipo.IND_KFDESDECONVER);
		fhastaconverExtractor = createExtractor("getFhastaconver", Timestamp.class, AsigCurvaTipo.IND_FHASTACONVER);
	}

	@Override
	public Set<java.util.Map.Entry<AsigCurvaTipoKey, AsigCurvaTipo>> entrySet() {
		return (Set<Entry<AsigCurvaTipoKey, AsigCurvaTipo>>) this.getCache().entrySet();
	}

	@Override
	public AsigCurvaTipo get(Object key) {
		if (!(key instanceof AsigCurvaTipoKey)) {
			return null;
		} else {
			return (AsigCurvaTipo) this.getCache().get(key);
		}
	}

	@Override
	public Set<AsigCurvaTipoKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public AsigCurvaTipo put(AsigCurvaTipoKey key, AsigCurvaTipo value) {
		this.getCache().put(key, value);
		return value;
	}

	@Override
	public AsigCurvaTipo remove(Object key) {
		return (AsigCurvaTipo) this.getCache().remove(key);
	}

	@Override
	public Collection<AsigCurvaTipo> values() {
		return (Collection<AsigCurvaTipo>) this.getCache().values();
	}

	public List<AsigCurvaTipo> obtenerCurvaTipo(String kbasetec, String kriesgo,
			String kriesgorescate, String kpagounico, String cartera,
			String kpb, String kapbel, Timestamp fcierre)  {
		
		Filter allFilter = null;
		
		List<Filter> filtros = new ArrayList<Filter>();
		
		filtros.add(new EqualsFilter(kbasetecExtractor, kbasetec));
		//filtros.add(new EqualsFilter(kriesgoExtractor, kriesgo));
		//filtros.add(new EqualsFilter(kriesgorescateExtractor, kriesgorescate));
		//filtros.add(new EqualsFilter(kpagounicoExtractor, kpagounico));
		filtros.add(new EqualsFilter(kcarterainvExtractor, cartera));
		//filtros.add(new EqualsFilter(kpbExtractor, kpb));
		//filtros.add(new EqualsFilter(kapbelExtractor, kapbel));
		filtros.add(new LessEqualsFilter(kfdesdeconverExtractor, fcierre.getTime()));
		filtros.add(new GreaterEqualsFilter(fhastaconverExtractor, fcierre.getTime()));
		
		
		Filter[] arrayFiltros = filtros.toArray(new Filter[filtros.size()]);
		
		allFilter = new AllFilter(arrayFiltros);
		
		List<AsigCurvaTipo> curvTipo = getCurvaTipo(allFilter);
		
		return curvTipo;
	}
	
	private Filter reducirFiltro (Filter[] filter, int contador) {
		
		Filter filtroReducido = null;
		
		Filter[] arrayReducido = new Filter[filter.length-contador];
		
		System.arraycopy(filter, 0, arrayReducido, 0, filter.length-contador);
		
		filtroReducido = new AllFilter(arrayReducido);
		
		return filtroReducido;
	}
	
	public List<AsigCurvaTipo> getCurvaTipo(Filter allFilter) {
		
		List<AsigCurvaTipo> curvTipo = new ArrayList<AsigCurvaTipo>();
		
		Set values = this.getCache().entrySet(allFilter);
		
			Iterator iter = values.iterator();
			while(iter.hasNext()){
			    Map.Entry entry = (Map.Entry) iter.next();
			    curvTipo.add((AsigCurvaTipo) entry.getValue());
			}
		
		return curvTipo;
	}
	
}
