package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tangosol.util.Filter;
import com.tangosol.util.InvocableMap;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.aggregator.Count;
import com.tangosol.util.aggregator.DoubleSum;
import com.tangosol.util.aggregator.GroupAggregator;
import com.tangosol.util.extractor.MultiExtractor;
import com.tangosol.util.extractor.PofExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.EqualsFilter;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.InfoPtipoKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.InfoPtipo;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;

public class InfoPtipoDao extends DaoBase implements Map<InfoPtipoKey, InfoPtipo> {

	private static final String CACHE_NAME = "infoPtipo";

	public ValueExtractor kmodalidadExtractor;
	public ValueExtractor kgarantiaExtractor;
	public ValueExtractor kprestacionExtractor;

	public InfoPtipoDao() {
		super();
		super.setCacheName(CACHE_NAME);

		kmodalidadExtractor = createExtractor("getKmodalidad", Integer.class, InfoPtipo.IND_KMODALIDAD);
		kgarantiaExtractor = createExtractor("getKgarantia", Integer.class, InfoPtipo.IND_KGARANTIA);
		kprestacionExtractor = createExtractor("getKprestacion", String.class, InfoPtipo.IND_KPRESTACION);

		getCache().addIndex(kmodalidadExtractor, false, null);
		getCache().addIndex(kgarantiaExtractor, false, null);
		getCache().addIndex(kprestacionExtractor, false, null);
	}

	@Override
	public Set<Entry<InfoPtipoKey, InfoPtipo>> entrySet() {
		return (Set<Entry<InfoPtipoKey, InfoPtipo>>) this.getCache().entrySet();
	}

	@Override
	public InfoPtipo get(Object key) {
		return (InfoPtipo) this.getCache().get(key);
	}

	@Override
	public Set<InfoPtipoKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public InfoPtipo put(InfoPtipoKey key, InfoPtipo infoPtipo) {
		this.getCache().put(key, infoPtipo);
		return infoPtipo;
	}

	@Override
	public InfoPtipo remove(Object key) {
		return (InfoPtipo) this.getCache().remove(key);
	}

	@Override
	public Collection<InfoPtipo> values() {
		return (Collection<InfoPtipo>) this.getCache().values();
	}

	public Map<InfoPtipoKey, InfoPtipo> getMap() {

		Map<InfoPtipoKey, InfoPtipo> mapaInfoPtipo = new HashMap<InfoPtipoKey, InfoPtipo>();

		Set values = this.getCache().entrySet();
		Iterator iter = values.iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			mapaInfoPtipo.put((InfoPtipoKey) entry.getKey(), (InfoPtipo) entry.getValue());
		}

		return mapaInfoPtipo;
	}

	public InfoPtipo getValues(Integer modalidad, Integer garantia, String prestacion) {

		List<Filter> filtros = new ArrayList<Filter>();

		EqualsFilter modalidadFilter = new EqualsFilter(kmodalidadExtractor, modalidad);
		EqualsFilter garantiaFilter = new EqualsFilter(kgarantiaExtractor, garantia);
		EqualsFilter prestacionFilter = new EqualsFilter(kprestacionExtractor, prestacion);

		filtros.add(modalidadFilter);
		filtros.add(garantiaFilter);
		filtros.add(prestacionFilter);

		Filter[] arrayFiltros = new Filter[filtros.size()];
		for (int i = 0; i < arrayFiltros.length; i++) {
			arrayFiltros[i] = filtros.get(i);
		}

		Filter allFilter = new AllFilter(arrayFiltros);
		Set values = this.getCache().entrySet(allFilter);

		List<InfoPtipo> infoPtipo = new ArrayList<InfoPtipo>();

		Iterator iter = values.iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			infoPtipo.add((InfoPtipo) entry.getValue());
		}

		if (infoPtipo == null || infoPtipo.isEmpty()) {
			return null;
		} else if (infoPtipo.size() > 1) {
			return null; // TODO: devolver error
		} else {
			return infoPtipo.get(0);
		}

	}

	public Integer getModSize(Integer modalidad) {

		EqualsFilter modalidadFilter = new EqualsFilter(kmodalidadExtractor, modalidad);;

		Set values = this.getCache().keySet(modalidadFilter);

		return values.size();

	}

	public void putInfoPtipo(Map<List<Object>, Integer> result, Integer constante) {

		InfoPtipo ip;
		for (List<Object> l : result.keySet()) {
			ip = new InfoPtipo();
			ip.setKmodalidad((Integer) l.get(0));
			ip.setKgarantia((Integer) l.get(1));
			ip.setKprestacion((String) l.get(2));

			Integer numUmics = (int) Math.round((double) result.get(l) / (double) constante);
			numUmics = numUmics == 0 ? 1 : numUmics;

			ip.setTotalUmics(numUmics);

			this.getCache().put(ip.getKey(), ip);
		}

	}

}
