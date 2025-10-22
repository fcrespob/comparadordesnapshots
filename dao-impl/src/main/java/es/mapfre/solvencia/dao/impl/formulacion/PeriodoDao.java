package es.mapfre.solvencia.dao.impl.formulacion;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tangosol.util.Filter;
import com.tangosol.util.extractor.ChainedExtractor;
import com.tangosol.util.extractor.PofExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.EqualsFilter;

import es.mapfre.solvencia.coherence.keys.formulacion.PeriodoKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.formulacion.Periodo;

public class PeriodoDao extends DaoBase implements Map<PeriodoKey, Periodo> {
	
	private static final String CACHE_NAME = "periodos";
	
	private PofExtractor fecierreExtractor = new PofExtractor(Timestamp.class, Periodo.IND_FCIERRE);
	private PofExtractor btExtractor = new PofExtractor(String.class, Periodo.IND_BT);
	private PofExtractor ctipoaportExtractor = new PofExtractor(String.class, Periodo.IND_CTIPOAPORT);
	private PofExtractor kajusteExtractor = new PofExtractor(Integer.class, Periodo.IND_KAJUSTE);
	private PofExtractor kcertificadoExtractor = new PofExtractor(Integer.class, Periodo.IND_KCERTIFICADO);
	private PofExtractor kgarantiaExtractor = new PofExtractor(Integer.class, Periodo.IND_KGARANTIA);
	private PofExtractor kmodalidadExtractor = new PofExtractor(Integer.class, Periodo.IND_KMODALIDAD);
	private PofExtractor kpolizaExtractor = new PofExtractor(Long.class, Periodo.IND_KPOLIZA);
	private PofExtractor kprestacionExtractor = new PofExtractor(String.class, Periodo.IND_KPRESTACION);
	private PofExtractor ksubpolizaExtractor = new PofExtractor(Integer.class, Periodo.IND_SUBKPOLIZA);
	private PofExtractor nordenExtractor = new PofExtractor(Integer.class, Periodo.IND_NORDEN);
	private PofExtractor nsuscriExtractor = new PofExtractor(Integer.class, Periodo.IND_NSUSCRI);
	
	public PeriodoDao() {
		super();
		super.setCacheName(CACHE_NAME);
		super.getCache().addIndex(fecierreExtractor, true, null);
		super.getCache().addIndex(btExtractor, false, null);
		super.getCache().addIndex(ctipoaportExtractor, false, null);
		super.getCache().addIndex(kajusteExtractor, false, null);
		super.getCache().addIndex(kcertificadoExtractor, false, null);
		super.getCache().addIndex(kgarantiaExtractor, false, null);
		super.getCache().addIndex(kmodalidadExtractor, false, null);
		super.getCache().addIndex(kpolizaExtractor, false, null);
		super.getCache().addIndex(kprestacionExtractor, false, null);
		super.getCache().addIndex(ksubpolizaExtractor, false, null);
		super.getCache().addIndex(nordenExtractor, false, null);
		super.getCache().addIndex(nsuscriExtractor, false, null);
	}

	@Override
	public Periodo get(Object key) {
		if (key instanceof PeriodoKey) {
			return (Periodo) getCache().get(key);
		}
		return null;
	}
	
	public List<Periodo> recuperarPeriodosBTI(Timestamp fechaCierre, String bt, String ctipoaport, Integer kajuste, 
			Integer kcertificado, Integer kgarantia, Integer kmodalidad, Long kpoliza, 
			String kprestacion, Integer ksubpoliza, Integer norden, Integer nsuscri) {
		
		ChainedExtractor timeAsLongExtractor = new ChainedExtractor(fecierreExtractor, new ReflectionExtractor("getTime"));
		EqualsFilter fechaFilter = new EqualsFilter(timeAsLongExtractor, fechaCierre.getTime());
		EqualsFilter btFilter = new EqualsFilter(btExtractor, ctipoaport);
		EqualsFilter ctipoaportFilter = new EqualsFilter(ctipoaportExtractor, bt);
		EqualsFilter kajusteFilter = new EqualsFilter(kajusteExtractor, kajuste);
		EqualsFilter kcertificadoFilter = new EqualsFilter(kcertificadoExtractor, kcertificado);
		EqualsFilter kgarantiaFilter = new EqualsFilter(kgarantiaExtractor, kgarantia);
		EqualsFilter kmodalidadFilter = new EqualsFilter(kmodalidadExtractor, kmodalidad);
		EqualsFilter kpolizaFilter = new EqualsFilter(kpolizaExtractor, kpoliza);
		EqualsFilter kprestacionFilter = new EqualsFilter(kprestacionExtractor, kprestacion);
		EqualsFilter ksubpolizaFilter = new EqualsFilter(ksubpolizaExtractor, ksubpoliza);
		EqualsFilter nordenFilter = new EqualsFilter(nordenExtractor, norden);
		EqualsFilter nsuscriFilter = new EqualsFilter(nsuscriExtractor, nsuscri);
				
		Filter allFilter = new AllFilter(new Filter[]{fechaFilter,btFilter,ctipoaportFilter,kajusteFilter,
				kcertificadoFilter,kgarantiaFilter,kmodalidadFilter,kpolizaFilter,kprestacionFilter,
				ksubpolizaFilter,nordenFilter,nsuscriFilter});
		
		Set resultado = this.getCache().entrySet(allFilter);
		
		return  ((List<Periodo>) resultado);
	}

	@Override
	public Periodo put(PeriodoKey key, Periodo value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
		
	}

	@Override
	public Periodo remove(Object key) {
		return (Periodo) this.getCache().remove(key);
	}

	@Override
	public Set<PeriodoKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public Collection<Periodo> values() {
		return (Collection<Periodo>) this.getCache().values();
	}

	@Override
	public Set<java.util.Map.Entry<PeriodoKey, Periodo>> entrySet() {
		return (Set<Entry<PeriodoKey, Periodo>>) this.getCache().entrySet();
	}
}
