package es.mapfre.solvencia.dao.impl.scr;

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

import es.mapfre.solvencia.coherence.keys.conversionesBel.ValoresAnulacionKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.conversionesBel.ValoresAnulacion;

public class ValoresAnulacionNF17AENDao extends DaoBase implements
		Map<ValoresAnulacionKey, ValoresAnulacion> {

	private static final String CACHE_NAME = "NF17AEN0";

	private final ValueExtractor ktablaExtractor;
	private final ValueExtractor feccierreExtractor;
	private final Comparator<ValoresAnulacion> comparatorByAniosDesde;

	public ValoresAnulacionNF17AENDao() {
		super();
		super.setCacheName(CACHE_NAME);
		ktablaExtractor = createExtractor("getCodTabla", String.class,
				ValoresAnulacion.IND_CODTABLA);
		feccierreExtractor = createExtractor("getFecCierre", Timestamp.class,
				ValoresAnulacion.IND_FECCIERRE);
		comparatorByAniosDesde = new Comparator<ValoresAnulacion>() {
			@Override
			public int compare(ValoresAnulacion v1,
					ValoresAnulacion v2) {
				return v1.getAniosDesde().compareTo(v2.getAniosDesde());
			}
		};
		super.getCache().addIndex(ktablaExtractor, false, null);
		super.getCache().addIndex(feccierreExtractor, false, null);
	}

	@Override
	public ValoresAnulacion get(Object key) {
		return (ValoresAnulacion) this.getCache().get(key);
	}

	@Override
	public ValoresAnulacion put(ValoresAnulacionKey key,
			ValoresAnulacion value) {
		this.getCache().put(key, value);
		return value;
	}

	@Override
	public ValoresAnulacion remove(Object key) {
		return (ValoresAnulacion) this.getCache().remove(key);
	}

	@Override
	public Set<ValoresAnulacionKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public Collection<ValoresAnulacion> values() {
		return (Collection<ValoresAnulacion>) this.getCache().values();
	}

	@Override
	public Set<java.util.Map.Entry<ValoresAnulacionKey, ValoresAnulacion>> entrySet() {
		return (Set<Entry<ValoresAnulacionKey, ValoresAnulacion>>) this
				.getCache().entrySet();
	}

	public List<ValoresAnulacion> getValues(String ktabla,
			Timestamp feccierre) {

		List<Filter> filtros = new ArrayList<Filter>();

		filtros.add(new EqualsFilter(ktablaExtractor, ktabla));
		filtros.add(new EqualsFilter(feccierreExtractor, feccierre.getTime()));

		Filter[] filtrosArray = new Filter[filtros.size()];
		for (int i = 0; i < filtrosArray.length; i++) {
			filtrosArray[i] = filtros.get(i);
		}

		Filter allFilter = new AllFilter(filtrosArray);

		Set lista = this.getCache().entrySet(allFilter, comparatorByAniosDesde);
		ArrayList<ValoresAnulacion> values = new ArrayList<ValoresAnulacion>();

		Iterator iter = lista.iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			values.add((ValoresAnulacion) entry.getValue());
		}

		return values;
	}
}