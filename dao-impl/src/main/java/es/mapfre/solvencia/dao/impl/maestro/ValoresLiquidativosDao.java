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

import es.mapfre.solvencia.coherence.keys.maestro.ValoresLiquidativosKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.maestro.ValoresLiquidativos;


public class ValoresLiquidativosDao extends DaoBase implements Map<ValoresLiquidativosKey, ValoresLiquidativos> {
	
	private static final String CACHE_NAME = "X880J005"; 
	
	private ValueExtractor kpolizaExtractor;
	private ValueExtractor kmodalidadExtractor;
	private ValueExtractor kramoExtractor;
	private ValueExtractor kCertificadoExtractor;
	
	public ValoresLiquidativosDao() {
		super();
		super.setCacheName(CACHE_NAME);
		kpolizaExtractor = this.createExtractor("getKpoliza", Long.class, ValoresLiquidativos.IND_KPOLIZA);
		kmodalidadExtractor = this.createExtractor("getKmodalidad", String.class, ValoresLiquidativos.IND_KMODALIDAD);
		kramoExtractor = this.createExtractor("getKramo", String.class, ValoresLiquidativos.IND_KRAMO);
		kCertificadoExtractor = this.createExtractor("getkCertificado", Integer.class, ValoresLiquidativos.IND_KCERTIFICADO);
		
		getCache().addIndex(kpolizaExtractor, false, null);
		getCache().addIndex(kmodalidadExtractor, false, null);
		getCache().addIndex(kramoExtractor, false, null);
		getCache().addIndex(kCertificadoExtractor, false, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Entry<ValoresLiquidativosKey, ValoresLiquidativos>> entrySet() {
		return (Set<Entry<ValoresLiquidativosKey, ValoresLiquidativos>>) this.getCache().entrySet();
	}

	@Override
	public ValoresLiquidativos get(Object key) {
		if (!(key instanceof ValoresLiquidativosKey)) {
			return null;
		} else {
			return (ValoresLiquidativos) this.getCache().get(key);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<ValoresLiquidativosKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public ValoresLiquidativos put(ValoresLiquidativosKey key, ValoresLiquidativos valoresLiquidativos) {
		 this.getCache().put(key, valoresLiquidativos);
		 return valoresLiquidativos;
	}

	@Override
	public ValoresLiquidativos remove(Object key) {
		return (ValoresLiquidativos) this.getCache().remove(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<ValoresLiquidativos> values() {
		return (Collection<ValoresLiquidativos>) this.getCache().values();
	}
	
	public List<ValoresLiquidativos> getValues(Long poliza, String modalidad, String ramo, Integer certificado){
		
		List<Filter> filtros = new ArrayList<Filter>();
		
		EqualsFilter polizaFilter = new EqualsFilter(kpolizaExtractor, poliza);
		EqualsFilter modalidadFilter = new EqualsFilter(kmodalidadExtractor, modalidad);
		EqualsFilter ramoFilter = new EqualsFilter(kramoExtractor, ramo);
		EqualsFilter certificadoFilter = new EqualsFilter(kCertificadoExtractor, certificado);
		
		filtros.add(polizaFilter);
		filtros.add(modalidadFilter);
		filtros.add(ramoFilter);
		filtros.add(certificadoFilter);
		
		Filter[] arrayFiltros = new Filter[filtros.size()];
		for (int i = 0; i < arrayFiltros.length; i++) {
			arrayFiltros[i] = filtros.get(i);
		}
		
		Filter allFilter = new AllFilter(arrayFiltros);
		Set values = this.getCache().entrySet(allFilter);
		
		List<ValoresLiquidativos> valoresLiq =new ArrayList<ValoresLiquidativos>();
		
		Iterator iter = values.iterator();
		while(iter.hasNext()){
		    Map.Entry entry = (Map.Entry) iter.next();
		    valoresLiq.add( (ValoresLiquidativos) entry.getValue());
		}


		return  valoresLiq;
	}
	
}