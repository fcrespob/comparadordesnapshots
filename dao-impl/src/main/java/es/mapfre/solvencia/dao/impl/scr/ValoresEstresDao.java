package es.mapfre.solvencia.dao.impl.scr;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.EqualsFilter;
import com.tangosol.util.filter.LessEqualsFilter;

import es.mapfre.solvencia.coherence.keys.scr.ValoresEstresKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.scr.ValoresEstres;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;


public class ValoresEstresDao extends DaoBase implements Map<ValoresEstresKey, ValoresEstres>{

	private static final String CACHE_NAME = "SCR0"; 
	
	private static final int BATCH_SIZE = 1000;
	
	private ValueExtractor fecCierreExtractor;
	private ValueExtractor btExtractor;
	
	public ValoresEstresDao() {
		super();
		super.setCacheName(CACHE_NAME);
		fecCierreExtractor = this.createExtractor("getFeccierre", Timestamp.class, ValoresEstres.IND_FECCIERRE);
		btExtractor = this.createExtractor("getBt", String.class, ValoresEstres.IND_BT);
		super.getCache().addIndex(fecCierreExtractor, true, null);
		super.getCache().addIndex(btExtractor, false, null);
	}
	
	@Override
	public Set<Entry<ValoresEstresKey, ValoresEstres>> entrySet() {
		return (Set<Entry<ValoresEstresKey, ValoresEstres>>) this.getCache().entrySet();
	}

	@Override
	public ValoresEstres get(Object key) {
		return (ValoresEstres) this.getCache().get(key);
	}

	@Override
	public Set<ValoresEstresKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public ValoresEstres put(ValoresEstresKey key, ValoresEstres valoresEstres) {
		 this.getCache().put(key, valoresEstres);
		 return valoresEstres;
	}

	@Override
	public ValoresEstres remove(Object key) {
		return (ValoresEstres) this.getCache().remove(key);
	}

	@Override
	public Collection<ValoresEstres> values() {
		return (Collection<ValoresEstres>) this.getCache().values();
	}

	@Override
	public void loadCache(BeanIOReader reader) {
		ValoresEstres valorEstres = null;
		Map<ValoresEstresKey, ValoresEstres> mapValoresEstres = new HashMap<ValoresEstresKey,ValoresEstres>();
		int bloque = 0;
		while ((valorEstres = (ValoresEstres) reader.read()) != null) {
			ValoresEstresKey key = new ValoresEstresKey(valorEstres.getFeccierre(), valorEstres.getBt(), valorEstres.getVariable());
			mapValoresEstres.put(key, valorEstres);
			bloque++;
			if (bloque % BATCH_SIZE == 0) {
				this.putAll(mapValoresEstres);
				mapValoresEstres.clear();
			}
		}
		if (mapValoresEstres.size() > 0) {
			this.putAll(mapValoresEstres);
			mapValoresEstres.clear();
		}
	}

	public List<ValoresEstres> obtenerValoresEstres(Timestamp lfEfec, String bt){
		
		Filter fecCierreFilter = new LessEqualsFilter(fecCierreExtractor, lfEfec.getTime());
		Filter btFilter = new EqualsFilter(btExtractor, bt);
		Filter allFilter = new AllFilter(new Filter[] {fecCierreFilter,btFilter});
		
		Set lista = this.getCache().entrySet(allFilter);
		
		List<ValoresEstres> valoresEstres =new ArrayList<ValoresEstres>();
		
		Iterator iter = lista.iterator();
		while(iter.hasNext()){
			Map.Entry entry = (Map.Entry) iter.next();
			valoresEstres.add((ValoresEstres) entry.getValue());
		}
		
		//ordenamos por fecha
		Collections.sort(valoresEstres, Collections.reverseOrder(new Comparator<ValoresEstres>(){
			@Override
			public int compare(ValoresEstres o1, ValoresEstres o2) {
				return o1.getFeccierre().compareTo(o2.getFeccierre());
			}
		}));
		
		return valoresEstres;
	}
	
}
