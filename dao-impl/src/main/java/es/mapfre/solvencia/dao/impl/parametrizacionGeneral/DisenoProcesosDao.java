package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.EqualsFilter;
import com.tangosol.util.filter.IsNullFilter;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.DisenoProcesosKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DisenoProcesos;

public class DisenoProcesosDao extends DaoBase implements Map<DisenoProcesosKey, DisenoProcesos>{

	private static final String CACHE_NAME = "DIP0"; 
	
	
	private final ValueExtractor kcompaniaExtractor;
	private final ValueExtractor kramoExtractor;
	private final ValueExtractor kmodalidadExtractor;
	private final ValueExtractor kgarantiaExtractor;
	private final ValueExtractor kbasetecExtractor;
	private final ValueExtractor kclaveadicExtractor; 
	private final ValueExtractor gprocesoExtractor; 
	
	public DisenoProcesosDao() {
		super();
		super.setCacheName(CACHE_NAME);
		 kcompaniaExtractor  = createExtractor("getKcompania",Integer.class, DisenoProcesos.IND_KCOMPANIA);
		 kramoExtractor      = createExtractor("getKramo",String.class, DisenoProcesos.IND_KRAMO);
		 kmodalidadExtractor = createExtractor("getKmodalidad",Integer.class, DisenoProcesos.IND_KMODALIDAD);
		 kgarantiaExtractor  = createExtractor("getKgarantia",Integer.class, DisenoProcesos.IND_KGARANTIA);
		 kbasetecExtractor   = createExtractor("getKbasetec",String.class, DisenoProcesos.IND_KBASETEC);
		 kclaveadicExtractor = createExtractor("getKclaveadic",String.class, DisenoProcesos.IND_KCLAVEADIC); 
		 gprocesoExtractor   = createExtractor("getGproceso",String.class, DisenoProcesos.IND_GPROCESO); 
		 
		 getCache().addIndex(gprocesoExtractor, false, null);
		 getCache().addIndex(kcompaniaExtractor, false, null);
		 getCache().addIndex(kramoExtractor, false, null);
		 getCache().addIndex(kmodalidadExtractor, false, null);
		 getCache().addIndex(kgarantiaExtractor, false, null);
		 getCache().addIndex(kbasetecExtractor, false, null);
		 getCache().addIndex(kclaveadicExtractor, false, null);
	}

	@Override
	public Set<Entry<DisenoProcesosKey, DisenoProcesos>> entrySet() {
		return (Set<Entry<DisenoProcesosKey, DisenoProcesos>>) this.getCache().entrySet();
	}

	@Override
	public DisenoProcesos get(Object key) {
		if (!(key instanceof DisenoProcesosKey)) {
			return null;
		} else {
			return (DisenoProcesos) this.getCache().get(key);
		}
	}

	@Override
	public Set<DisenoProcesosKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public DisenoProcesos put(DisenoProcesosKey key, DisenoProcesos disenoProcesos) {
		 this.getCache().put(key, disenoProcesos);
		 return disenoProcesos;
	}

	@Override
	public DisenoProcesos remove(Object key) {
		return (DisenoProcesos) this.getCache().remove(key);
	}

	@Override
	public Collection<DisenoProcesos> values() {
		return (Collection<DisenoProcesos>) this.getCache().values();
	}
	
	public List<DisenoProcesos> obtenerDisenoProcesos(String gproceso, Integer kcompania, String kramo, Integer kmodalidad, 
			Integer kgarantia, String kbasetec, String kclaveadic)  {
		
		Filter allFilter = null;
		
		List<Filter> filtros = new ArrayList<Filter>();
		
		//Se construye una lista con los parametros de búsqueda (distintos a null)
		List<Object> parametros = new ArrayList<Object>();
		
		if(gproceso!=null) {
			parametros.add(gproceso);
			
			if(kcompania!=null) {
				parametros.add(kcompania);
				
				if(kramo!=null) {
					parametros.add(kramo);
					
					if(kmodalidad!=null) {
						parametros.add(kmodalidad);
						
						if(kgarantia!=null) {
							parametros.add(kgarantia);
							
							if(kbasetec!=null) {
								parametros.add(kbasetec);
								
								if(kclaveadic!=null) {
									parametros.add(kclaveadic);
								} 
							} 
						} 
					} 
				} 
			} 
		}	
		
		//Una vez construida la lista de parámetros, se utiliza para construir la lista de filtros para la búsqueda 
		for (Object parameter: parametros)	{
			if (parameter != null)	{
				filtros.add(new EqualsFilter(getValueExtratorByIndex(filtros.size()), parameter));	
			}
		}
		
		//Se añaden filtros para los parametros iguales a null
		for (int i = filtros.size();i<7;i++)	{
			if (i == DisenoProcesos.IND_KMODALIDAD || i == DisenoProcesos.IND_KGARANTIA){
				filtros.add(new EqualsFilter(getValueExtratorByIndex(filtros.size()), 0));
			} else {
				filtros.add(new IsNullFilter(getParamNameByIndex(filtros.size())));
			}
		}
		
		Filter[] arrayFiltros = filtros.toArray(new Filter[filtros.size()]);

		allFilter = new AllFilter(arrayFiltros);

		//Se realiza la búsqueda del diseño de procesos con la lista de filtros.
		List<DisenoProcesos> disenoProcesos = getDisenoProcesos(allFilter);
		
		int contador = 1;
		// Si no se obtiene resultado, se reduce la lista de filtros y se realiza de nuevo la búsqueda
		// Se repite esta operación hasta que sólo quede el primer filtro, caso en el que se devuelve el resultado aunque sea null
		while ((disenoProcesos.isEmpty()) && (contador < filtros.size()-1)) {
			parametros.remove(parametros.size()-1);
			filtros = construirFiltro(parametros);
			arrayFiltros = filtros.toArray(new Filter[filtros.size()]);

			allFilter = new AllFilter(arrayFiltros);
			disenoProcesos = getDisenoProcesos(allFilter);
			contador++;
		}
		
		return disenoProcesos;
	}
	
	/**
	 * Devuelve el Extractor del filtro a partir del indice del parámetro
	 * @param index
	 * @return
	 */
	private ValueExtractor getValueExtratorByIndex(Integer index)	{
		
		switch (index){
		case DisenoProcesos.IND_GPROCESO:
			return gprocesoExtractor;
		case DisenoProcesos.IND_KCOMPANIA:
			return kcompaniaExtractor;
		case DisenoProcesos.IND_KRAMO:
			return kramoExtractor;
		case DisenoProcesos.IND_KMODALIDAD:
			return kmodalidadExtractor;
		case DisenoProcesos.IND_KGARANTIA:
			return kgarantiaExtractor;
		case DisenoProcesos.IND_KBASETEC:
			return kbasetecExtractor;
		case DisenoProcesos.IND_KCLAVEADIC:
			return kclaveadicExtractor;
		default:
			return null;	
		}
	}
	
	/**
	 * Devuelve el nombre del método de obtención a partir del nombre del parámetro
	 * @param index
	 * @return
	 */
	private String getParamNameByIndex(Integer index)	{
		
		switch (index){
		case DisenoProcesos.IND_KCOMPANIA:
			return "getKcompania";
		case DisenoProcesos.IND_KRAMO:
			return "getKramo";
		case DisenoProcesos.IND_KMODALIDAD:
			return "getKmodalidad";
		case DisenoProcesos.IND_KGARANTIA:
			return "getKgarantia";
		case DisenoProcesos.IND_KBASETEC:
			return "getKbasetec";
		case DisenoProcesos.IND_KCLAVEADIC:
			return "getKclaveadic";
		default:
			return null;	
		}
	}
	
	/**
	 * Construye los filtros de búsqueda con la lista de parámetros
	 * @param parametros
	 * @return
	 */
	private List<Filter> construirFiltro(List<Object> parametros){
		
		List<Filter> filtros = new ArrayList<Filter>();
		
		for (Object parameter: parametros)	{
			if (parameter != null)	{
				filtros.add(new EqualsFilter(getValueExtratorByIndex(filtros.size()), parameter));	
			}
		}
		
		for (int i = filtros.size();i<7;i++)	{
			if (i == DisenoProcesos.IND_KMODALIDAD || i == DisenoProcesos.IND_KGARANTIA){
				filtros.add(new EqualsFilter(getValueExtratorByIndex(filtros.size()), 0));
			} else {
				filtros.add(new IsNullFilter(getParamNameByIndex(filtros.size())));
			}
		}
		
		
		return filtros;
	}
	
//	private Filter reducirFiltro (Filter[] filter, int contador) {
//		
//		Filter filtroReducido = null;
//		
//		Filter[] arrayReducido = new Filter[filter.length-contador];
//		
//		System.arraycopy(filter, 0, arrayReducido, 0, filter.length-contador);
//		
//		filtroReducido = new AllFilter(arrayReducido);
//		
//		return filtroReducido;
//	}
	
	
	public List<DisenoProcesos> obtenerDisenoProcesos(String gproceso,Integer kcompania, String kbasetec) {
		
		Filter allFilter = null;
		
		List<Filter> filtros = new ArrayList<Filter>();
		if(gproceso!=null) {
				filtros.add(new EqualsFilter(gprocesoExtractor, gproceso));
			if (kcompania != null) {
				filtros.add(new EqualsFilter(kcompaniaExtractor, kcompania));
				if (kbasetec != null) {
					filtros.add(new EqualsFilter(kbasetecExtractor, kbasetec));
				} else {
					return null;
				}
			}	
		} else {
			return null;
		}
		
		Filter[] arrayFiltros = new Filter[filtros.size()];
		
		for (int i = 0; i < arrayFiltros.length; i++) {
			arrayFiltros[i] = filtros.get(i);
		}
		
		allFilter = new AllFilter(arrayFiltros);
		
		List<DisenoProcesos> disenoProcesos = getDisenoProcesos(allFilter);
				
		return disenoProcesos;

	}
	
	public List<DisenoProcesos> getDisenoProcesos(Filter allFilter) {
		
		List<DisenoProcesos> disenos=new ArrayList<DisenoProcesos>();
		
		Set values = this.getCache().entrySet(allFilter);
		
			Iterator iter = values.iterator();
			while(iter.hasNext()){
			    Map.Entry entry = (Map.Entry) iter.next();
			    disenos.add((DisenoProcesos) entry.getValue());
			}
		
		return disenos;
	}
	
}