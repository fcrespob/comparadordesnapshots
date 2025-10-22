package es.mapfre.solvencia.dao.impl.maestro;

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

import es.mapfre.solvencia.coherence.keys.maestro.PolizaInstrumentalKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.maestro.PolizaInstrumental;

public class PolizasInstrumentalesDao extends DaoBase implements Map<PolizaInstrumentalKey, PolizaInstrumental>{
	private static final String CACHE_NAME = "X880J007"; 
	
	private ValueExtractor kpolizaExtractor;
	private ValueExtractor kmodalidadExtractor;
	private ValueExtractor ksubpolizaExtractor;
	private ValueExtractor kcertificadoExtractor;
	
	public PolizasInstrumentalesDao() {
		super();
		super.setCacheName(CACHE_NAME);
		kmodalidadExtractor = this.createExtractor("getKmodalidadInstr", Integer.class, PolizaInstrumental.IND_KMODALIDADINSTR);
		kpolizaExtractor = this.createExtractor("getKpolizaInstr", Long.class, PolizaInstrumental.IND_KPOLIZAINSTR);
		ksubpolizaExtractor = this.createExtractor("getKsubpolizaInstr", Integer.class, PolizaInstrumental.IND_KSUBPOLIZAINSTR);
		kcertificadoExtractor = this.createExtractor("getKcertificadoInstr", Integer.class, PolizaInstrumental.IND_KCERTIFICADOINSTR);
		
		getCache().addIndex(kpolizaExtractor, false, null);
		getCache().addIndex(kmodalidadExtractor, false, null);
		getCache().addIndex(ksubpolizaExtractor, false, null);
		getCache().addIndex(kcertificadoExtractor, false, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Entry<PolizaInstrumentalKey, PolizaInstrumental>> entrySet() {
		return (Set<Entry<PolizaInstrumentalKey, PolizaInstrumental>>) this.getCache().entrySet();
	}

	@Override
	public PolizaInstrumental get(Object key) {
		if (!(key instanceof PolizaInstrumentalKey)) {
			return null;
		} else {
			return (PolizaInstrumental) this.getCache().get(key);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<PolizaInstrumentalKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public PolizaInstrumental put(PolizaInstrumentalKey key, PolizaInstrumental polizaInstrumental) {
		 this.getCache().put(key, polizaInstrumental);
		 return polizaInstrumental;
	}

	@Override
	public PolizaInstrumental remove(Object key) {
		return (PolizaInstrumental) this.getCache().remove(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<PolizaInstrumental> values() {
		return (Collection<PolizaInstrumental>) this.getCache().values();
	}
	
	public List<PolizaInstrumental> getValues(Integer modalidad, Long poliza, Integer subpoliza, Integer certificado){
		
		List<Filter> filtros = new ArrayList<Filter>();

		EqualsFilter modalidadFilter = new EqualsFilter(kmodalidadExtractor, modalidad);
		EqualsFilter polizaFilter = new EqualsFilter(kpolizaExtractor, poliza);
		EqualsFilter subpolizaFilter = new EqualsFilter(ksubpolizaExtractor, subpoliza);
		EqualsFilter certificadoFilter = new EqualsFilter(kcertificadoExtractor, certificado);
		
		filtros.add(polizaFilter);
		filtros.add(modalidadFilter);
		filtros.add(subpolizaFilter);
		filtros.add(certificadoFilter);
		
		Filter[] arrayFiltros = new Filter[filtros.size()];
		for (int i = 0; i < arrayFiltros.length; i++) {
			arrayFiltros[i] = filtros.get(i);
		}
		
		Filter allFilter = new AllFilter(arrayFiltros);
		Set values = this.getCache().entrySet(allFilter);
		
		List<PolizaInstrumental> polizaInst =new ArrayList<PolizaInstrumental>();
		
		Iterator iter = values.iterator();
		while(iter.hasNext()){
		    Map.Entry entry = (Map.Entry) iter.next();
		    polizaInst.add( (PolizaInstrumental) entry.getValue());
		}


		return  polizaInst;
	}
}
