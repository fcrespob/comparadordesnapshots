package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.EqualsFilter;
import com.tangosol.util.filter.GreaterEqualsFilter;
import com.tangosol.util.filter.LessEqualsFilter;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.LimitesCapitalKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.LimitesCapital;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;

public class LimitesCapitalDao extends DaoBase implements Map<LimitesCapitalKey, LimitesCapital> {

	private static final String CACHE_NAME = "RW06646";
	
	private static Logger log = LoggerFactory.getLogger(LimitesCapitalDao.class);
		
	private final ValueExtractor ntablaExtractor;
	private final ValueExtractor kmodalidadExtractor;
	private final ValueExtractor kgarantiaExtractor;
	private final ValueExtractor fefecfinExtractor;
	private final ValueExtractor kedad1Extractor;
	private final ValueExtractor kedad2Extractor;
	private final ValueExtractor nmeshastaExtractor;
	
	public LimitesCapitalDao() {
		super();
		super.setCacheName(CACHE_NAME);
		
		ntablaExtractor     = createExtractor("getNtabla",String.class, LimitesCapital.IND_NTABLA);
		kmodalidadExtractor = createExtractor("getKmodalidad",String.class, LimitesCapital.IND_KMODALIDAD);
		kgarantiaExtractor  = createExtractor("getKgarantia",String.class, LimitesCapital.IND_KGARANTIA);
		fefecfinExtractor   = createExtractor("getFefecfin",Timestamp.class, LimitesCapital.IND_FEFECFIN);
		kedad1Extractor     = createExtractor("getKedad1",Integer.class, LimitesCapital.IND_KEDAD1);
		kedad2Extractor     = createExtractor("getKedad2",Integer.class, LimitesCapital.IND_KEDAD2);
		nmeshastaExtractor  = createExtractor("getNmeshasta",Integer.class, LimitesCapital.IND_NMESHASTA);
		
		getCache().addIndex(ntablaExtractor, true, null);
		getCache().addIndex(kmodalidadExtractor, true, null);
		getCache().addIndex(kgarantiaExtractor, true, null);
		getCache().addIndex(fefecfinExtractor, true, null);
		getCache().addIndex(kedad1Extractor, true, null);
		getCache().addIndex(kedad2Extractor, true, null);
		getCache().addIndex(nmeshastaExtractor, true, null);
	}

	@Override
	public LimitesCapital get(Object key) {
		if (!(key instanceof LimitesCapitalKey)) {
			return null;
		} else {
			return (LimitesCapital) this.getCache().get(key);
		}
	}

	@Override
	public LimitesCapital put(LimitesCapitalKey key,
			LimitesCapital value) {
		this.getCache().put(key, value);
		return value;

	}

	@Override
	public LimitesCapital remove(Object key) {
		return (LimitesCapital) this.getCache().remove(key);
	}

	@Override
	public Set<LimitesCapitalKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public Collection<LimitesCapital> values() {
		return (Collection<LimitesCapital>) this.getCache().values();
	}

	@Override
	public Set<java.util.Map.Entry<LimitesCapitalKey, LimitesCapital>> entrySet() {
		return (Set<Entry<LimitesCapitalKey, LimitesCapital>>) this
				.getCache().entrySet();
	}
	
	public List<LimitesCapital> getValue(String ntabla, String kmodalidad,
			String kgarantia, Timestamp fecEfecto, Integer edadAsegurado,
			Integer numMeses) {
		
		List<LimitesCapital> limites= new ArrayList<LimitesCapital>();
		Filter[] filtrosArray;
							
		Filter ntablaFilter = new EqualsFilter(ntablaExtractor, ntabla);
		Filter kmodalidadFilter = new EqualsFilter(kmodalidadExtractor, kmodalidad);
		Filter kgarantiaFilter = new EqualsFilter(kgarantiaExtractor, kgarantia);
		Filter fefecfinFilter = new GreaterEqualsFilter(fefecfinExtractor, fecEfecto.getTime());
		Filter kedad1Filter = new LessEqualsFilter(kedad1Extractor, edadAsegurado);
		Filter kedad2Filter = new GreaterEqualsFilter(kedad2Extractor, edadAsegurado);
		Filter nmeshastaFilter = new GreaterEqualsFilter(nmeshastaExtractor, numMeses);
		
		if (numMeses == null){
			filtrosArray = new Filter[]{ntablaFilter,kmodalidadFilter,kgarantiaFilter,fefecfinFilter,
					kedad1Filter,kedad2Filter};
		}else{
			filtrosArray = new Filter[]{ntablaFilter,kmodalidadFilter,kgarantiaFilter,fefecfinFilter,
					kedad1Filter,kedad2Filter,nmeshastaFilter};
		}
	
		Filter allFilter = new AllFilter(filtrosArray);
			
		Set values = this.getCache().entrySet(allFilter,mesesHastaOrdered());
		
			Iterator iter = values.iterator();
			while(iter.hasNext()){
			    Map.Entry entry = (Map.Entry) iter.next();
			    limites.add( (LimitesCapital) entry.getValue() );
			    if(log.isDebugEnabled()){
			    	log.debug(limites.get(limites.size()-1).toString());
			    }
			}
			
		return limites;
	}
	
	private Comparator mesesHastaOrdered() {
		return new Comparator<LimitesCapital>() {

			@Override
			public int compare(LimitesCapital lc1, LimitesCapital lc2) {
				if (lc1.getNmeshasta() != null && lc2.getNmeshasta() != null) {
					return lc1.getNmeshasta().compareTo(lc2.getNmeshasta());
				}
				return 0;
			}
			
		};
	}
	
	
}