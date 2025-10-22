package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tangosol.io.pof.annotation.PortableProperty;
import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.EqualsFilter;
import com.tangosol.util.filter.GreaterEqualsFilter;
import com.tangosol.util.filter.LessEqualsFilter;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.AsigCurvasTipoUOAKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.AsigCurvasTipoUOA;

public class AsigCurvasTipoUOADao extends DaoBase implements Map<AsigCurvasTipoUOAKey, AsigCurvasTipoUOA> {
	private static final String CACHE_NAME = "CTU0";

	private final ValueExtractor kUOA;
	private final ValueExtractor kFecCierreCurv;
	private final ValueExtractor kCarInv17;
	private final ValueExtractor kCurva;

	public AsigCurvasTipoUOADao() {
		super();
		super.setCacheName(CACHE_NAME);
		
		kUOA = createExtractor("getkUOA", String.class, AsigCurvasTipoUOA.IND_KUOA);
		kFecCierreCurv = createExtractor("getkFecCierreCurv", Timestamp.class, AsigCurvasTipoUOA.IND_KFCIERRECURV);
		kCarInv17 = createExtractor("getkCarInv17", String.class, AsigCurvasTipoUOA.IND_KCARINVN17);
		kCurva = createExtractor("getkCurva", String.class, AsigCurvasTipoUOA.IND_CCI0_KCURVA);
	
	}

	@Override
	public Set<Entry<AsigCurvasTipoUOAKey, AsigCurvasTipoUOA>> entrySet() {
		return (Set<Entry<AsigCurvasTipoUOAKey, AsigCurvasTipoUOA>>) this.getCache().entrySet();
	}

	@Override
	public AsigCurvasTipoUOA get(Object key) {
		if (!(key instanceof AsigCurvasTipoUOAKey)) {
			return null;
		} else {
			return (AsigCurvasTipoUOA) this.getCache().get(key);
		}
	}

	@Override
	public Set<AsigCurvasTipoUOAKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public AsigCurvasTipoUOA put(AsigCurvasTipoUOAKey key, AsigCurvasTipoUOA asigCurvasTipoUOA) {
		 this.getCache().put(key, asigCurvasTipoUOA);
		 return asigCurvasTipoUOA;
	}

	@Override
	public AsigCurvasTipoUOA remove(Object key) {
		return (AsigCurvasTipoUOA) this.getCache().remove(key);
	}

	@Override
	public Collection<AsigCurvasTipoUOA> values() {
		return (Collection<AsigCurvasTipoUOA>) this.getCache().values();
	}
	
	public List<AsigCurvasTipoUOA> obtenerCurvaTipoCINIIF17(String bt, String UOA,
			String kcarinv/*, Timestamp fcierre*/){
		
		Filter allFilter = null;
		
		List<Filter> filtros = new ArrayList<Filter>();
		
		filtros.add(new EqualsFilter(kUOA, UOA.trim()));
//		if(bt.equals("NIFF17OCI")){
//			filtros.add(new LessEqualsFilter(kFecCierreCurv, fcierre.getTime()));
//		}
		
		filtros.add(new EqualsFilter(kCarInv17, kcarinv));
		
		Filter[] arrayFiltros = filtros.toArray(new Filter[filtros.size()]);
		
		allFilter = new AllFilter(arrayFiltros);
		
		List<AsigCurvasTipoUOA> curvTipo = getCurvaTipoNIIF17(allFilter);
		
		return curvTipo;
	}
	
	public List<AsigCurvasTipoUOA> obtenerCurvaTipoNiif17Lir(String bt, String UOA,
			String kcarinv, Timestamp fsuscripcion, String curvaLir ){
		
		Filter allFilter = null;
		
		List<Filter> filtros = new ArrayList<Filter>();
		
		filtros.add(new EqualsFilter(kUOA, UOA.trim()));
		//filtros.add(new LessEqualsFilter(kFecCierreCurv, fsuscripcion.getTime()));
		filtros.add(new EqualsFilter(kCarInv17, kcarinv));
		filtros.add(new EqualsFilter(kCurva, curvaLir));
		
		Filter[] arrayFiltros = filtros.toArray(new Filter[filtros.size()]);
		
		allFilter = new AllFilter(arrayFiltros);
		
		List<AsigCurvasTipoUOA> curvTipo = getCurvaTipoNIIF17(allFilter);
		
		return curvTipo;
	}
	public List<AsigCurvasTipoUOA> getCurvaTipoNIIF17(Filter allFilter) {
		
		List<AsigCurvasTipoUOA> curvTipo = new ArrayList<AsigCurvasTipoUOA>();
		
		Set values = this.getCache().entrySet(allFilter);
		
			Iterator iter = values.iterator();
			while(iter.hasNext()){
			    Map.Entry entry = (Map.Entry) iter.next();
			    curvTipo.add((AsigCurvasTipoUOA) entry.getValue());
			}
		
		return curvTipo;
	}

}
