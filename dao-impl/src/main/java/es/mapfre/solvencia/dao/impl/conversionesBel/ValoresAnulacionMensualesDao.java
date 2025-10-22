package es.mapfre.solvencia.dao.impl.conversionesBel;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.EqualsFilter;

import es.mapfre.solvencia.coherence.keys.conversionesBel.ValoresAnulacionMensualesKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.conversionesBel.ValoresAnulacionMensuales;

public class ValoresAnulacionMensualesDao extends DaoBase implements
		Map<ValoresAnulacionMensualesKey, ValoresAnulacionMensuales> {

	private static final String CACHE_NAME = "VMA0";

	private final ValueExtractor ktablaExtractor;
	private final ValueExtractor feccierreExtractor;
	private final Comparator<ValoresAnulacionMensuales> comparatorByMesesDesde;

	public ValoresAnulacionMensualesDao() {
		super();
		super.setCacheName(CACHE_NAME);
		ktablaExtractor = createExtractor("getCodTabla", String.class,
				ValoresAnulacionMensuales.IND_CODTABLA);
		feccierreExtractor = createExtractor("getFecCierre", Timestamp.class,
				ValoresAnulacionMensuales.IND_FECCIERRE);
		comparatorByMesesDesde = new Comparator<ValoresAnulacionMensuales>() {
			@Override
			public int compare(ValoresAnulacionMensuales v1,
					ValoresAnulacionMensuales v2) {
				return v1.getMesesDesde() - v2.getMesesDesde();
			}
		};
		super.getCache().addIndex(ktablaExtractor, false, null);
		super.getCache().addIndex(feccierreExtractor, false, null);
	}

	@Override
	public ValoresAnulacionMensuales get(Object key) {
		return (ValoresAnulacionMensuales) this.getCache().get(key);
	}

	@Override
	public ValoresAnulacionMensuales put(ValoresAnulacionMensualesKey key,
			ValoresAnulacionMensuales value) {
		this.getCache().put(key, value);
		return value;
	}

	@Override
	public ValoresAnulacionMensuales remove(Object key) {
		return (ValoresAnulacionMensuales) this.getCache().remove(key);
	}

	@Override
	public Set<ValoresAnulacionMensualesKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public Collection<ValoresAnulacionMensuales> values() {
		return (Collection<ValoresAnulacionMensuales>) this.getCache().values();
	}

	@Override
	public Set<java.util.Map.Entry<ValoresAnulacionMensualesKey, ValoresAnulacionMensuales>> entrySet() {
		return (Set<Entry<ValoresAnulacionMensualesKey, ValoresAnulacionMensuales>>) this
				.getCache().entrySet();
	}

	public List<ValoresAnulacionMensuales> getValues(String ktabla,
			Timestamp feccierre) {

		List<Filter> filtros = new ArrayList<Filter>();

		filtros.add(new EqualsFilter(ktablaExtractor, ktabla));
		filtros.add(new EqualsFilter(feccierreExtractor, feccierre.getTime()));

		Filter[] filtrosArray = new Filter[filtros.size()];
		for (int i = 0; i < filtrosArray.length; i++) {
			filtrosArray[i] = filtros.get(i);
		}

		Filter allFilter = new AllFilter(filtrosArray);

		Set lista = this.getCache().entrySet(allFilter, comparatorByMesesDesde);
		ArrayList<ValoresAnulacionMensuales> values = new ArrayList<ValoresAnulacionMensuales>();

		Iterator iter = lista.iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			values.add((ValoresAnulacionMensuales) entry.getValue());
		}

		return values;
	}
}