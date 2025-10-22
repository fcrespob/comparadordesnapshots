package es.mapfre.solvencia.dao.impl.gbt;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.EqualsFilter;
import com.tangosol.util.filter.LessEqualsFilter;

import es.mapfre.solvencia.coherence.keys.gbt.AsigInteresTecnicoKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.gbt.AsigInteresTecnico;

public class AsigInteresTecnicoDao extends DaoBase implements Map<AsigInteresTecnicoKey, AsigInteresTecnico> {

	private static final String CACHE_NAME = "ITG0"; 
	
	private final ValueExtractor fcierreExtractor;
	private final ValueExtractor ktipobtExtractor;
	private final ValueExtractor kgapExtractor;
	private final ValueExtractor kaprosspExtractor;
	private final ValueExtractor kcasadoExtractor;
	
	public AsigInteresTecnicoDao() {
		super();
		super.setCacheName(CACHE_NAME);
		
		fcierreExtractor = createExtractor("getFcierre", Timestamp.class, AsigInteresTecnico.IND_FCIERRE);
		ktipobtExtractor = createExtractor("getKtipobt", String.class, AsigInteresTecnico.IND_KTIPOBT);
		kgapExtractor = createExtractor("getKgap", String.class, AsigInteresTecnico.IND_KGAP);
		kaprosspExtractor = createExtractor("getKaprossp", String.class, AsigInteresTecnico.IND_KAPROSSP);
		kcasadoExtractor = createExtractor("getKcasado", String.class, AsigInteresTecnico.IND_KCASADO);
	}

	@Override
	public Set<java.util.Map.Entry<AsigInteresTecnicoKey, AsigInteresTecnico>> entrySet() {
		return (Set<Entry<AsigInteresTecnicoKey, AsigInteresTecnico>>) this.getCache().entrySet();
	}

	@Override
	public AsigInteresTecnico get(Object arg0) {
		if (!(arg0 instanceof AsigInteresTecnicoKey)) {
			return null;
		} else {
			return (AsigInteresTecnico) this.getCache().get(arg0);
		}
	}

	@Override
	public Set<AsigInteresTecnicoKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public AsigInteresTecnico put(AsigInteresTecnicoKey arg0,
			AsigInteresTecnico arg1) {
		this.getCache().put(arg0, arg1);
		return arg1;
	}

	@Override
	public AsigInteresTecnico remove(Object arg0) {
		return (AsigInteresTecnico) this.getCache().remove(arg0);
	}

	@Override
	public Collection<AsigInteresTecnico> values() {
		return (Collection<AsigInteresTecnico>) this.getCache().values();
	}
	
	private static Comparator<AsigInteresTecnico> ordenadorValores = new Comparator<AsigInteresTecnico>() {
		@Override
		public int compare(AsigInteresTecnico o1, AsigInteresTecnico o2) {
			return o2.getFcierre().compareTo(o1.getFcierre());
		}
	};
	
	public List<AsigInteresTecnico> obtenerIntTecnico(Timestamp fcierre, String ktipobt, String kgap,
			String kaprossp, String kcasado)  {
		
		Filter allFilter = null;
		
		List<Filter> filtros = new ArrayList<Filter>();
		
		filtros.add(new LessEqualsFilter(fcierreExtractor, fcierre.getTime()));
		filtros.add(new EqualsFilter(ktipobtExtractor, ktipobt));
		filtros.add(new EqualsFilter(kgapExtractor, kgap));
		filtros.add(new EqualsFilter(kaprosspExtractor, kaprossp));
		filtros.add(new EqualsFilter(kcasadoExtractor, kcasado));
		
		Filter[] arrayFiltros = filtros.toArray(new Filter[filtros.size()]);
		
		allFilter = new AllFilter(arrayFiltros);
		
		List<AsigInteresTecnico> intTecnico = getIntTecnico(allFilter);
		
		return intTecnico;
	}
	
	private Filter reducirFiltro (Filter[] filter, int contador) {
		
		Filter filtroReducido = null;
		
		Filter[] arrayReducido = new Filter[filter.length-contador];
		
		System.arraycopy(filter, 0, arrayReducido, 0, filter.length-contador);
		
		filtroReducido = new AllFilter(arrayReducido);
		
		return filtroReducido;
	}
	
	public List<AsigInteresTecnico> getIntTecnico(Filter allFilter) {
		
		List<AsigInteresTecnico> intTecnico = new ArrayList<AsigInteresTecnico>();
		
		Set values = this.getCache().entrySet(allFilter,ordenadorValores);
		
			Iterator iter = values.iterator();
			while(iter.hasNext()){
			    Map.Entry entry = (Map.Entry) iter.next();
			    intTecnico.add((AsigInteresTecnico) entry.getValue());
			}
		
		return intTecnico;
	}

}
