package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

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
import com.tangosol.util.filter.LessFilter;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.ComisionesParticipadasCOMKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.ComisionesParticipadasCOM;

public class ComisionesParticipadasCOMDao extends DaoBase implements Map<ComisionesParticipadasCOMKey, ComisionesParticipadasCOM>{

private static final String CACHE_NAME = "COM0"; 

private final ValueExtractor kCarteOrigExtractor;
private final ValueExtractor kModalidadExtractor;
private final ValueExtractor kGarantiaExtractor;
private final ValueExtractor kFvigDesExtractor;
private final ValueExtractor kFvigHasExtractor;
private final ValueExtractor faltaDesExtractor;
private final ValueExtractor faltaHasExtractor;
	
	public ComisionesParticipadasCOMDao() {
		super();
		super.setCacheName(CACHE_NAME);
		
		kCarteOrigExtractor = createExtractor("getkCarteOrig", Integer.class, ComisionesParticipadasCOM.IND_KCARTEORIG);
		kModalidadExtractor = createExtractor("getkModalidad", Integer.class, ComisionesParticipadasCOM.IND_KMODALIDAD);
		kGarantiaExtractor = createExtractor("getkGarantia", Integer.class, ComisionesParticipadasCOM.IND_KGARANTIA);
		kFvigDesExtractor = createExtractor("getkFvigDes", Timestamp.class, ComisionesParticipadasCOM.IND_KFVIGDES);
		kFvigHasExtractor = createExtractor("getkFvigHas", Timestamp.class, ComisionesParticipadasCOM.IND_KFVIGHAS);
		faltaDesExtractor = createExtractor("getFaltaDes", Timestamp.class, ComisionesParticipadasCOM.IND_FALTADES);
		faltaHasExtractor = createExtractor("getFaltaHas", Timestamp.class, ComisionesParticipadasCOM.IND_FALTAHAS);
	}

	@Override
	public Set<Entry<ComisionesParticipadasCOMKey, ComisionesParticipadasCOM>> entrySet() {
		return (Set<Entry<ComisionesParticipadasCOMKey, ComisionesParticipadasCOM>>) this.getCache().entrySet();
	}

	@Override
	public ComisionesParticipadasCOM get(Object key) {
		if (!(key instanceof ComisionesParticipadasCOMKey)) {
			return null;
		} else {
			return (ComisionesParticipadasCOM) this.getCache().get(key);
		}
	}

	@Override
	public Set<ComisionesParticipadasCOMKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public ComisionesParticipadasCOM put(ComisionesParticipadasCOMKey key, ComisionesParticipadasCOM ComisionesParticipadasCOM) {
		 this.getCache().put(key, ComisionesParticipadasCOM);
		 return ComisionesParticipadasCOM;
	}

	@Override
	public ComisionesParticipadasCOM remove(Object key) {
		return (ComisionesParticipadasCOM) this.getCache().remove(key);
	}

	@Override
	public Collection<ComisionesParticipadasCOM> values() {
		return (Collection<ComisionesParticipadasCOM>) this.getCache().values();
	}
	
	public List<ComisionesParticipadasCOM> obtenerCominisionParticipada(Integer kCarteOrig, Integer kModalidad,
			Integer kGarantia, Timestamp fcalc, Timestamp fecIni)  {
		
		Filter allFilter = null;
		
		List<Filter> filtros = new ArrayList<Filter>();
		
		filtros.add(new EqualsFilter(kCarteOrigExtractor, kCarteOrig));
		filtros.add(new EqualsFilter(kModalidadExtractor, kModalidad));
		filtros.add(new EqualsFilter(kGarantiaExtractor, kGarantia));
		filtros.add(new LessEqualsFilter(kFvigDesExtractor, fcalc.getTime()));
		filtros.add(new GreaterEqualsFilter(kFvigHasExtractor, fcalc.getTime()));
		filtros.add(new LessEqualsFilter(faltaDesExtractor, fecIni.getTime()));
		filtros.add(new GreaterEqualsFilter(faltaHasExtractor, fecIni.getTime()));
		
		Filter[] arrayFiltros = filtros.toArray(new Filter[filtros.size()]);
		
		allFilter = new AllFilter(arrayFiltros);
		
		List<ComisionesParticipadasCOM> cominPart = getCominisionParticipada(allFilter);
		
		return cominPart;
	}
	
	private Filter reducirFiltro (Filter[] filter, int contador) {
		
		Filter filtroReducido = null;
		
		Filter[] arrayReducido = new Filter[filter.length-contador];
		
		System.arraycopy(filter, 0, arrayReducido, 0, filter.length-contador);
		
		filtroReducido = new AllFilter(arrayReducido);
		
		return filtroReducido;
	}
	
	public List<ComisionesParticipadasCOM> getCominisionParticipada(Filter allFilter) {
		
		List<ComisionesParticipadasCOM> cominPart = new ArrayList<ComisionesParticipadasCOM>();
		
		Set values = this.getCache().entrySet(allFilter);
		
			Iterator iter = values.iterator();
			while(iter.hasNext()){
			    Map.Entry entry = (Map.Entry) iter.next();
			    cominPart.add((ComisionesParticipadasCOM) entry.getValue());
			}
		
		return cominPart;
	}

}
