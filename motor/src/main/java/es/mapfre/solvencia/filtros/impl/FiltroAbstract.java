package es.mapfre.solvencia.filtros.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.filter.AndFilter;
import com.tangosol.util.filter.EqualsFilter;
import com.tangosol.util.filter.GreaterFilter;
import com.tangosol.util.filter.LessEqualsFilter;
import com.tangosol.util.filter.NotEqualsFilter;
import com.tangosol.util.filter.OrFilter;

import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.filtros.Filtro;

// JBMARTA - PYAM0025 - INI
public abstract class FiltroAbstract implements Filtro {

	public Filter getFiltroEqualsString(final String valor, ValueExtractor extractor, Boolean inclusivo)
			throws Solvencia2Excepcion {

		Filter filter = null;

		if (inclusivo) {
			filter = new EqualsFilter(extractor, valor);
		} else {
			filter = new NotEqualsFilter(extractor, valor);
		}
		return filter;
	}

	public Filter getFiltroEqualsLong(final Long valor, ValueExtractor extractor, Boolean inclusivo)
			throws Solvencia2Excepcion {

		Filter filter = null;

		if (inclusivo) {
			filter = new EqualsFilter(extractor, valor);
		} else {
			filter = new NotEqualsFilter(extractor, valor);
		}
		return filter;
	}
	
	public Filter getFiltroEqualsInteger(final Integer valor, ValueExtractor extractor, Boolean inclusivo)
			throws Solvencia2Excepcion {

		Filter filter = null;

		if (inclusivo) {
			filter = new EqualsFilter(extractor, valor);
		} else {
			filter = new NotEqualsFilter(extractor, valor);
		}
		return filter;
	}
	
	public Filter getFiltroGreaterBigDecimal(final BigDecimal valor, ValueExtractor extractor, Boolean inclusivo)
			throws Solvencia2Excepcion {

		Filter filter = null;

		if (inclusivo) {
			filter = new GreaterFilter(extractor, valor);
		} else {
			filter = new LessEqualsFilter(extractor, valor);
		}
		return filter;
	}

	public Filter getFiltroGreaterTimestamp(final Timestamp valor, ValueExtractor extractor, Boolean inclusivo)
			throws Solvencia2Excepcion {

		Filter filter = null;

		if (inclusivo) {
			filter = new GreaterFilter(extractor, valor);
		} else {
			filter = new LessEqualsFilter(extractor, valor);
		}
		return filter;
	}
	
	protected Filter createOrFilter(Filter filterAc, Filter filterAd) {

		Filter returnedFilter = null;

		if (filterAc == null) {
			returnedFilter = filterAd;
		} else {
			returnedFilter = new OrFilter(filterAc, filterAd);
		}
		return returnedFilter;
	}

	protected Filter createAndFilter(Filter filterAc, Filter filterAd) {

		Filter returnedFilter = null;

		if (filterAc == null) {
			returnedFilter = filterAd;
		} else {
			returnedFilter = new AndFilter(filterAc, filterAd);
		}
		return returnedFilter;
	}
}
// JBMARTA - PYAM0025 - FIN