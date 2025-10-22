package es.mapfre.solvencia.dao.impl.maestro;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.extractor.ChainedExtractor;
import com.tangosol.util.extractor.PofExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.EqualsFilter;
import com.tangosol.util.filter.GreaterEqualsFilter;

import es.mapfre.solvencia.coherence.keys.maestro.PagosPlanificadosKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.maestro.PagosPlanificados;

public class PagosPlanificadosDao extends DaoBase implements
		Map<PagosPlanificadosKey, PagosPlanificados> {

	private static final String CACHE_NAME = "X880J003";

	private final ValueExtractor cgarantiaExtractor = new PofExtractor(
			Integer.class, PagosPlanificados.IND_CGARANTIA);
	private final ValueExtractor kpolizaExtractor = new PofExtractor(
			Long.class, PagosPlanificados.IND_KPOLIZA);
	private final ValueExtractor ksubpolExtractor = new PofExtractor(
			Integer.class, PagosPlanificados.IND_KSUBPOL);
	private final ValueExtractor kajusteExtractor = new PofExtractor(
			Integer.class, PagosPlanificados.IND_KAJUSTE);
	private final ValueExtractor kcertiExtractor = new PofExtractor(
			Integer.class, PagosPlanificados.IND_KCERTI);
	private final ValueExtractor kgrsusExtractor = new PofExtractor(
			Integer.class, PagosPlanificados.IND_KGRSUS);
	private final ValueExtractor cprestaExtractor = new PofExtractor(
			String.class, PagosPlanificados.IND_CPRESTAENTORNO);
	private final ValueExtractor kprestaExtractor = new PofExtractor(
			String.class, PagosPlanificados.IND_KPRESTA);
	private final ValueExtractor kprestaFictExtractor = new PofExtractor(
			String.class, PagosPlanificados.IND_CPRESTAFICT);
	
	private final ValueExtractor fplreaEfectoExtractor = new ChainedExtractor(
			new PofExtractor(Timestamp.class,
					PagosPlanificados.IND_FPLREAEFECTO),
			new ReflectionExtractor("getTime"));
	private final ValueExtractor nordenExtractor = new PofExtractor(
			String.class, PagosPlanificados.IND_FILLER);

	private Comparator<PagosPlanificados> fplreaEfectoComparator = new Comparator<PagosPlanificados>() {
		@Override
		public int compare(PagosPlanificados pp1, PagosPlanificados pp2) {
			if (pp1.getFplreaEfecto() != null && pp2.getFplreaEfecto() != null) {
				return pp1.getFplreaEfecto().compareTo(pp2.getFplreaEfecto());
			}
			return 0;
		}

	};
	

	public PagosPlanificadosDao() {
		super();
		super.setCacheName(CACHE_NAME);
		super.getCache().addIndex(cgarantiaExtractor, false, null);
		super.getCache().addIndex(kpolizaExtractor, false, null);
		super.getCache().addIndex(ksubpolExtractor, false, null);
		super.getCache().addIndex(kajusteExtractor, false, null);
		super.getCache().addIndex(kcertiExtractor, false, null);
		super.getCache().addIndex(kgrsusExtractor, false, null);
		super.getCache().addIndex(cprestaExtractor, false, null);
		super.getCache().addIndex(kprestaExtractor, false, null);
		super.getCache().addIndex(kprestaFictExtractor, false, null);
		super.getCache().addIndex(fplreaEfectoExtractor, true, null);
		super.getCache().addIndex(nordenExtractor, false, null);
	}

	@Override
	public Set<Entry<PagosPlanificadosKey, PagosPlanificados>> entrySet() {
		return (Set<Entry<PagosPlanificadosKey, PagosPlanificados>>) this
				.getCache().entrySet();
	}

	@Override
	public PagosPlanificados get(Object key) {
		if (!(key instanceof PagosPlanificadosKey)) {
			return null;
		} else {
			return (PagosPlanificados) this.getCache().get(key);
		}
	}

	@Override
	public Set<PagosPlanificadosKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public PagosPlanificados put(PagosPlanificadosKey key,
			PagosPlanificados pagosPlanificados) {
		this.getCache().put(key, pagosPlanificados);
		return pagosPlanificados;
	}

	@Override
	public PagosPlanificados remove(Object key) {
		return (PagosPlanificados) this.getCache().remove(key);
	}

	@Override
	public Collection<PagosPlanificados> values() {
		return (Collection<PagosPlanificados>) this.getCache().values();
	}

	public Set getValues(Integer cgarantia, Long kpoliza, Integer ksubpol,
			Integer kajuste, Integer kcerti, Integer kgrsus, String cpresta,
			String kpresta, Timestamp fplreaEfecto) {

		Filter carterainvFilter = new EqualsFilter(cgarantiaExtractor,
				cgarantia);
		Filter kajusteFilter = new EqualsFilter(kajusteExtractor, kajuste);
		Filter kcertiFilter = new EqualsFilter(kcertiExtractor, kcerti);
		Filter kgrsusFilter = new EqualsFilter(kgrsusExtractor, kgrsus);
		Filter kpolizaFilter = new EqualsFilter(kpolizaExtractor, kpoliza);
		Filter cprestaFilter = new EqualsFilter(cprestaExtractor, cpresta);
		Filter kprestaFilter = new EqualsFilter(kprestaExtractor, kpresta);
		Filter ksubpolFilter = new EqualsFilter(ksubpolExtractor, ksubpol);
		Filter fplreaEfectoFilter = new GreaterEqualsFilter(
				fplreaEfectoExtractor, fplreaEfecto.getTime());
		
		Filter allFilter = new AllFilter(
				new Filter[] { carterainvFilter, kajusteFilter, kcertiFilter,
						kgrsusFilter, kpolizaFilter, cprestaFilter,
						kprestaFilter, ksubpolFilter, fplreaEfectoFilter });

		return this.getCache().entrySet(allFilter, fplreaEfectoComparator);

	}
	
	public Set getValuesLocas(Integer cgarantia, Long kpoliza, Integer ksubpol, Integer kajuste, Integer kcerti,
			Integer kgrsus, String cpresta, String kpresta,String cprestaFict, Timestamp fplreaEfecto,Integer norden) {

		Filter carterainvFilter = new EqualsFilter(cgarantiaExtractor, cgarantia);
		Filter kajusteFilter = new EqualsFilter(kajusteExtractor, kajuste);
		Filter kcertiFilter = new EqualsFilter(kcertiExtractor, kcerti);
		Filter kgrsusFilter = new EqualsFilter(kgrsusExtractor, kgrsus);
		Filter kpolizaFilter = new EqualsFilter(kpolizaExtractor, kpoliza);
		Filter cprestaFilter = new EqualsFilter(cprestaExtractor, cpresta);
		Filter kprestaFilter = new EqualsFilter(kprestaExtractor, kpresta);
		Filter cprestaFictFilter = new EqualsFilter(kprestaFictExtractor, cprestaFict);
		Filter ksubpolFilter = new EqualsFilter(ksubpolExtractor, ksubpol);
		Filter fplreaEfectoFilter = new GreaterEqualsFilter(fplreaEfectoExtractor, fplreaEfecto.getTime());
		Filter nordenFilter = new EqualsFilter(nordenExtractor, norden.toString());

		Filter allFilter = new AllFilter(new Filter[] { carterainvFilter, kajusteFilter, kcertiFilter, kgrsusFilter,
				kpolizaFilter, cprestaFilter, kprestaFilter, ksubpolFilter,cprestaFictFilter, fplreaEfectoFilter,nordenFilter });
		
		return this.getCache().entrySet(allFilter, fplreaEfectoComparator);

	}
	
}