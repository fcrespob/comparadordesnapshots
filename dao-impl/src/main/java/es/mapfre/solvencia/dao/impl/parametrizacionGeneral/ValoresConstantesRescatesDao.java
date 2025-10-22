package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.EqualsFilter;
import com.tangosol.util.filter.GreaterEqualsFilter;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.ValoresConstantesRescateKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.ValoresConstantesRescate;

public class ValoresConstantesRescatesDao extends DaoBase implements Map<ValoresConstantesRescateKey, ValoresConstantesRescate> {
	private static final String CACHE_NAME = "VCR0";

	private final ValueExtractor codigoExtractor; 
	private final ValueExtractor duracionExtractor; 

	public ValoresConstantesRescatesDao() {
		super();
		super.setCacheName(CACHE_NAME);

		codigoExtractor = createExtractor("getKk1", String.class, ValoresConstantesRescate.IND_KK1);
		duracionExtractor = createExtractor("getKduracion", Integer.class, ValoresConstantesRescate.IND_KDURACION);
		super.getCache().addIndex(codigoExtractor, true, null);
	}

	@Override
	public ValoresConstantesRescate get(Object key) {
		if (!(key instanceof ValoresConstantesRescateKey)) {
			return null;
		} else {
			return (ValoresConstantesRescate) this.getCache().get(key);
		}
	}

	public java.math.BigDecimal recuperarCteRescate(String codigo, Integer kduracion) {

		ValoresConstantesRescateKey key = new ValoresConstantesRescateKey(codigo,
				kduracion);

		ValoresConstantesRescate valorCteRescate = this.get(key);

		if (valorCteRescate != null) {
			return valorCteRescate.getPorckonst();
		}
		return null;
	}

	public List<ValoresConstantesRescate> recuperarCtesRescate(String codigo, Integer duracion) {

		Filter filtroCodigo = new EqualsFilter(codigoExtractor, codigo);
		Filter filtroDuracion = new GreaterEqualsFilter(duracionExtractor, duracion);
		Set<Entry<ValoresConstantesRescateKey, ValoresConstantesRescate>> entries = (Set<Entry<ValoresConstantesRescateKey, ValoresConstantesRescate>>) this.getCache().entrySet(new AllFilter(new Filter[]{filtroCodigo, filtroDuracion}), constantesRTEOrdered);

		List<ValoresConstantesRescate> valores = new ArrayList<ValoresConstantesRescate>();
		if (entries != null) {
			for (Entry<ValoresConstantesRescateKey, ValoresConstantesRescate> entry : entries) {
				valores.add(entry.getValue());
			}
		}
		return valores;
	}

	@Override
	public ValoresConstantesRescate put(ValoresConstantesRescateKey key,
			ValoresConstantesRescate value) {
		this.getCache().put(key, value);
		return value;

	}

	@Override
	public ValoresConstantesRescate remove(Object key) {
		return (ValoresConstantesRescate) this.getCache().remove(key);
	}

	@Override
	public Set<ValoresConstantesRescateKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public Collection<ValoresConstantesRescate> values() {
		return (Collection<ValoresConstantesRescate>) this.getCache().values();
	}

	@Override
	public Set<java.util.Map.Entry<ValoresConstantesRescateKey, ValoresConstantesRescate>> entrySet() {
		return (Set<Entry<ValoresConstantesRescateKey, ValoresConstantesRescate>>) this
				.getCache().entrySet();
	}
	
	private static Comparator<ValoresConstantesRescate> constantesRTEOrdered = new Comparator<ValoresConstantesRescate>() {
		@Override
		public int compare(ValoresConstantesRescate o1, ValoresConstantesRescate o2) {
			return o1.getKduracion().compareTo(o2.getKduracion());
		}
	};
}