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
import com.tangosol.util.filter.GreaterFilter;
import com.tangosol.util.filter.LessEqualsFilter;

import es.mapfre.solvencia.coherence.keys.gbt.RentaGAPKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.gbt.RentaGAP;

public class RentaGAPDao extends DaoBase implements Map<RentaGAPKey, RentaGAP> {

	private static final String CACHE_NAME = "GAR0";
	
	private final ValueExtractor kgapExtractor;
	private final ValueExtractor finiExtractor;
	private final ValueExtractor ffinExtractor;
	
	public RentaGAPDao() {
		super();
		super.setCacheName(CACHE_NAME);
		
		kgapExtractor = createExtractor("getKgap", String.class, RentaGAP.IND_KGAP);
		finiExtractor = createExtractor("getFini", Timestamp.class, RentaGAP.IND_FINI);
		ffinExtractor = createExtractor("getFfin", Timestamp.class, RentaGAP.IND_FFIN);
	}

	@Override
	public Set<java.util.Map.Entry<RentaGAPKey, RentaGAP>> entrySet() {
		return (Set<Entry<RentaGAPKey, RentaGAP>>) this.getCache().entrySet();
	}

	@Override
	public RentaGAP get(Object arg0) {
		if (!(arg0 instanceof RentaGAPKey)) {
			return null;
		} else {
			return (RentaGAP) this.getCache().get(arg0);
		}
	}

	@Override
	public Set<RentaGAPKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public RentaGAP put(RentaGAPKey arg0, RentaGAP arg1) {
		this.getCache().put(arg0, arg1);
		return arg1;
	}

	@Override
	public RentaGAP remove(Object arg0) {
		return (RentaGAP) this.getCache().remove(arg0);
	}

	@Override
	public Collection<RentaGAP> values() {
		return (Collection<RentaGAP>) this.getCache().values();
	}
	
	public List<RentaGAP> obtenerRentaGAP(String kgap, Timestamp fCierre)  {
		
		Filter allFilter = null;
		
		List<Filter> filtros = new ArrayList<Filter>();
		
		filtros.add(new EqualsFilter(kgapExtractor, kgap));
		filtros.add(new LessEqualsFilter(finiExtractor, fCierre.getTime()));
		filtros.add(new GreaterFilter(ffinExtractor, fCierre.getTime()));
		
		Filter[] arrayFiltros = filtros.toArray(new Filter[filtros.size()]);
		
		allFilter = new AllFilter(arrayFiltros);
		
		List<RentaGAP> rentaGap = getRentaGAP(allFilter);
		
		return rentaGap;
	}
	
	private Filter reducirFiltro (Filter[] filter, int contador) {
		
		Filter filtroReducido = null;
		
		Filter[] arrayReducido = new Filter[filter.length-contador];
		
		System.arraycopy(filter, 0, arrayReducido, 0, filter.length-contador);
		
		filtroReducido = new AllFilter(arrayReducido);
		
		return filtroReducido;
	}
	
	public List<RentaGAP> getRentaGAP(Filter allFilter) {
		
		List<RentaGAP> rentaGap=new ArrayList<RentaGAP>();
		
		Set values = this.getCache().entrySet(allFilter);
		
			Iterator iter = values.iterator();
			while(iter.hasNext()){
			    Map.Entry entry = (Map.Entry) iter.next();
			    rentaGap.add((RentaGAP) entry.getValue());
			}
		
		return rentaGap;
	}
}
