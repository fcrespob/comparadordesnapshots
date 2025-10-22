package es.mapfre.solvencia.dao.impl.conversionesBel;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.aggregator.LongMax;
import com.tangosol.util.extractor.ChainedExtractor;
import com.tangosol.util.extractor.PofExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.AndFilter;
import com.tangosol.util.filter.EqualsFilter;
import com.tangosol.util.filter.LessEqualsFilter;

import es.mapfre.solvencia.coherence.keys.conversionesBel.ValoresCurvaTipoKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.conversionesBel.ValoresCurvaTipo;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.AsigCurvasTipoUOA;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;

public class ValoresCurvaTipoDao extends DaoBase  implements Map<ValoresCurvaTipoKey, ValoresCurvaTipo> {
	
	private static final String CACHE_NAME = "VCI0"; 
	
	private static final int BATCH_SIZE = 1000;
	
	private ValueExtractor fecEfecExtractor; //= new PofExtractor(Timestamp.class, ValoresCurvaTipo.IND_FECEFECCURVA);
	private ValueExtractor codCurvaTiposExtractor; //= new PofExtractor(String.class, ValoresCurvaTipo.IND_CODCURVATIPOS);
	
	private static Comparator<ValoresCurvaTipo> ordenadorValores = new Comparator<ValoresCurvaTipo>() {
		@Override
		public int compare(ValoresCurvaTipo o1, ValoresCurvaTipo o2) {
			return o1.getDiasPlazo().compareTo(o2.getDiasPlazo());
		}
	};
	
	public ValoresCurvaTipoDao() {
		super();
		super.setCacheName(CACHE_NAME);
		fecEfecExtractor = this.createExtractor("getFecEfecCurva", Timestamp.class, ValoresCurvaTipo.IND_FECEFECCURVA);
		codCurvaTiposExtractor = this.createExtractor("getCodCurvaTipos", String.class, ValoresCurvaTipo.IND_CODCURVATIPOS);
		super.getCache().addIndex(fecEfecExtractor, true, null);
		super.getCache().addIndex(codCurvaTiposExtractor, false, null);
	}
	

	@Override
	public Set<Entry<ValoresCurvaTipoKey, ValoresCurvaTipo>> entrySet() {
		return (Set<Entry<ValoresCurvaTipoKey, ValoresCurvaTipo>>) this.getCache().entrySet();
	}

	@Override
	public ValoresCurvaTipo get(Object key) {
		return (ValoresCurvaTipo) this.getCache().get(key);
	}

	@Override
	public Set<ValoresCurvaTipoKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public ValoresCurvaTipo put(ValoresCurvaTipoKey key, ValoresCurvaTipo valoresCurvaTipo) {
		 this.getCache().put(key, valoresCurvaTipo);
		 return valoresCurvaTipo;
	}

	@Override
	public ValoresCurvaTipo remove(Object key) {
		return (ValoresCurvaTipo) this.getCache().remove(key);
	}

	@Override
	public Collection<ValoresCurvaTipo> values() {
		return (Collection<ValoresCurvaTipo>) this.getCache().values();
	}

	@Override
	public void loadCache(BeanIOReader reader) {
		ValoresCurvaTipo valorCurvaTipo = null;
		Map<ValoresCurvaTipoKey, ValoresCurvaTipo> mapValoresCurvasTipos = new HashMap<ValoresCurvaTipoKey, ValoresCurvaTipo>();
		int bloque = 0;
		while ((valorCurvaTipo = (ValoresCurvaTipo) reader.read()) != null) {
			
			ValoresCurvaTipoKey key = new ValoresCurvaTipoKey(valorCurvaTipo.getCodCurvaTipos(),valorCurvaTipo.getFecEfecCurva(),valorCurvaTipo.getDiasPlazo());
			
			mapValoresCurvasTipos.put(key, valorCurvaTipo);
			bloque++;
			if (bloque % BATCH_SIZE == 0) {
				this.putAll(mapValoresCurvasTipos);
				mapValoresCurvasTipos.clear();
			}
		}
		if (mapValoresCurvasTipos.size() > 0) {
			this.putAll(mapValoresCurvasTipos);
			mapValoresCurvasTipos.clear();
		}
		
	}
	
	
	public List<ValoresCurvaTipo> getByFecha(String codCurvaTipo, Timestamp lfEfec){
		

		LessEqualsFilter fechaMenorFilter = new LessEqualsFilter(fecEfecExtractor, lfEfec.getTime());
				
		EqualsFilter codCurvaFilter = new EqualsFilter(codCurvaTiposExtractor, codCurvaTipo);
		AndFilter andFilter = new AndFilter(fechaMenorFilter, codCurvaFilter);
		
		Long maxDate = (Long) this.getCache().aggregate(andFilter, new LongMax("getFecEfecCurva.getTime"));

		EqualsFilter fechaDefinitivaFilter = new EqualsFilter(fecEfecExtractor, maxDate);
		AndFilter resultFilter = new AndFilter(fechaDefinitivaFilter, codCurvaFilter);

		// El resultado lo ordenamos en Coherence
		Set<Entry<ValoresCurvaTipoKey, ValoresCurvaTipo>> resultado = (Set<Entry<ValoresCurvaTipoKey, ValoresCurvaTipo>>) this.getCache().entrySet(resultFilter, ordenadorValores);
		
		List<ValoresCurvaTipo> resultados = new ArrayList<ValoresCurvaTipo>();
		
		for (Entry<ValoresCurvaTipoKey, ValoresCurvaTipo> valoresCurvaTipo : resultado) {
			resultados.add(valoresCurvaTipo.getValue());
		}
		
		return  resultados;
	}
	
	public List<ValoresCurvaTipo> getByBtNIFF17(String codCurvaTipo,Timestamp lfEfec){
		
		LessEqualsFilter fechaMenorFilter = new LessEqualsFilter(fecEfecExtractor, lfEfec.getTime());
				
		EqualsFilter codCurvaFilter = new EqualsFilter(codCurvaTiposExtractor, codCurvaTipo);
		AndFilter andFilter = new AndFilter(fechaMenorFilter, codCurvaFilter);
		
		Long maxDate = (Long) this.getCache().aggregate(andFilter, new LongMax("getFecEfecCurva.getTime"));

		EqualsFilter fechaDefinitivaFilter = new EqualsFilter(fecEfecExtractor, maxDate);
		AndFilter resultFilter = new AndFilter(fechaDefinitivaFilter, codCurvaFilter);

		// El resultado lo ordenamos en Coherence
		Set<Entry<ValoresCurvaTipoKey, ValoresCurvaTipo>> resultado = (Set<Entry<ValoresCurvaTipoKey, ValoresCurvaTipo>>) this.getCache().entrySet(resultFilter, ordenadorValores);
		
		List<ValoresCurvaTipo> resultados = new ArrayList<ValoresCurvaTipo>();
		
		for (Entry<ValoresCurvaTipoKey, ValoresCurvaTipo> valoresCurvaTipo : resultado) {
			resultados.add(valoresCurvaTipo.getValue());
		}
		
		return  resultados;
	}
	
	public List<ValoresCurvaTipo> getByCurvaNIFF17(String codCurvaTipo){
		
		Filter allFilter = null;
		
		List<Filter> filtros = new ArrayList<Filter>();
		
		filtros.add(new EqualsFilter(codCurvaTiposExtractor, codCurvaTipo));
		//filtros.add(new LessEqualsFilter(kFecCierreCurv, fcierre.getTime()));
		//filtros.add(new EqualsFilter(kCarInv17, kcarinv));
		
		Filter[] arrayFiltros = filtros.toArray(new Filter[filtros.size()]);
		
		allFilter = new AllFilter(arrayFiltros);
		
		List<ValoresCurvaTipo> curvTipo = getCurvaTipoNIIF17(allFilter);
		
		return curvTipo;
		
		//LessEqualsFilter fechaMenorFilter = new LessEqualsFilter(fecEfecExtractor, lfEfec.getTime());
//				
//		EqualsFilter codCurvaFilter = new EqualsFilter(codCurvaTiposExtractor, codCurvaTipo);
//		//AndFilter andFilter = new AndFilter(fechaMenorFilter, codCurvaFilter);
//		
//		//Long maxDate = (Long) this.getCache().aggregate(codCurvaFilter, new LongMax("getFecEfecCurva.getTime"));
//
//		//EqualsFilter fechaDefinitivaFilter = new EqualsFilter(fecEfecExtractor, maxDate);
//		//AndFilter resultFilter = new AndFilter(fechaDefinitivaFilter, codCurvaFilter);
//
//		// El resultado lo ordenamos en Coherence
//		Set<Entry<ValoresCurvaTipoKey, ValoresCurvaTipo>> resultado = (Set<Entry<ValoresCurvaTipoKey, ValoresCurvaTipo>>) this.getCache().entrySet(codCurvaFilter, ordenadorValores);
//		
//		List<ValoresCurvaTipo> resultados = new ArrayList<ValoresCurvaTipo>();
//		
//		for (Entry<ValoresCurvaTipoKey, ValoresCurvaTipo> valoresCurvaTipo : resultado) {
//			resultados.add(valoresCurvaTipo.getValue());
//		}
//		
//		return  resultados;
	}
	
	public List<ValoresCurvaTipo> getCurvaTipoNIIF17(Filter allFilter) {
		
		 List<ValoresCurvaTipo> curvTipo = new ArrayList<ValoresCurvaTipo>();
		
		Set values = this.getCache().entrySet(allFilter);
		
			Iterator iter = values.iterator();
			while(iter.hasNext()){
			    Map.Entry entry = (Map.Entry) iter.next();
			    curvTipo.add((ValoresCurvaTipo) entry.getValue());
			}
		
		return curvTipo;
	}
	
//	public List<AsigCurvasTipoUOA> obtenerCurvaTipoCINIIF17(String bt, String UOA,
//			String kcarinv, Timestamp fcierre){
//		
//		Filter allFilter = null;
//		
//		List<Filter> filtros = new ArrayList<Filter>();
//		
//		filtros.add(new EqualsFilter(kUOA, UOA.trim()));
//		filtros.add(new LessEqualsFilter(kFecCierreCurv, fcierre.getTime()));
//		filtros.add(new EqualsFilter(kCarInv17, kcarinv));
//		
//		Filter[] arrayFiltros = filtros.toArray(new Filter[filtros.size()]);
//		
//		allFilter = new AllFilter(arrayFiltros);
//		
//		List<AsigCurvasTipoUOA> curvTipo = getCurvaTipoNIIF17(allFilter);
//		
//		return curvTipo;
//	}
	

}
