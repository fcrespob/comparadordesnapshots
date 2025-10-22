package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.EqualsFilter;
import com.tangosol.util.filter.GreaterEqualsFilter;
import com.tangosol.util.filter.LessEqualsFilter;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.TablaHibridaKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.conversionesBel.TablasExperienciaReales;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.TablaHibrida;

@Portable
public class TablaHibridaDao extends DaoBase implements Map<TablaHibridaKey, TablaHibrida>{
	
	
	private static final String CACHE_NAME = "HTR0"; 
	
	private final ValueExtractor fvalorExtractor; 
	private final ValueExtractor kbasetecExtractor;
	private final ValueExtractor companiaExtractor;
	private final ValueExtractor cnegocioExtractor;
	private final ValueExtractor riesgoActuarialExtractor;
	private final ValueExtractor sexoExtractor;
	private final ValueExtractor kramoExtractor;
	private final ValueExtractor kgarantiaExtractor;
	private final ValueExtractor kmodalidadExtractor;
	private final ValueExtractor tablaBaseExtractor;

	public TablaHibridaDao() {
		super();
		super.setCacheName(CACHE_NAME);
		
		fvalorExtractor = createExtractor("getFvalor", Timestamp.class, TablaHibrida.IND_FVALOR);
		companiaExtractor = createExtractor("getCompania", Integer.class, TablaHibrida.IND_KCOMPANIA);
		cnegocioExtractor = createExtractor("getCnegocio", String.class, TablaHibrida.IND_CNEGOCIO);
		kbasetecExtractor = createExtractor("getKbasetec", String.class, TablaHibrida.IND_KBASETEC);
		kramoExtractor = createExtractor("getKramo", String.class, TablaHibrida.IND_KRAMO);
		kmodalidadExtractor = createExtractor("getKmodalidad", Integer.class, TablaHibrida.IND_KMODALIDAD);
		kgarantiaExtractor = createExtractor("getKmodalidad", Integer.class, TablaHibrida.IND_KGARANTIA);
		riesgoActuarialExtractor = createExtractor("getRiesgoactuarial", String.class, TablaHibrida.IND_RIESGOACTUARIAL);
		sexoExtractor = createExtractor("getSexo", String.class, TablaHibrida.IND_SEXO);
		tablaBaseExtractor = createExtractor("getTmbti", Integer.class, TablaHibrida.IND_TMBTI);
		
		
		getCache().addIndex(fvalorExtractor, true, null);
		getCache().addIndex(companiaExtractor, false, null);
		getCache().addIndex(cnegocioExtractor, false, null);
		getCache().addIndex(kbasetecExtractor, false, null);
		getCache().addIndex(kramoExtractor, false, null);
		getCache().addIndex(kmodalidadExtractor, false, null);
		getCache().addIndex(kgarantiaExtractor, false, null);
		getCache().addIndex(riesgoActuarialExtractor, false, null);
		getCache().addIndex(sexoExtractor, false, null);
		getCache().addIndex(tablaBaseExtractor, false, null);
			
	}

	@Override
	public Set<Entry<TablaHibridaKey, TablaHibrida>> entrySet() {
		return (Set<Entry<TablaHibridaKey, TablaHibrida>>) this.getCache().entrySet();
	}

	@Override
	public TablaHibrida get(Object key) {
		if (!(key instanceof TablaHibrida)) {
			return null;
		} else {
			return (TablaHibrida) this.getCache().get(key);
		}
	}

	@Override
	public Set<TablaHibridaKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public TablaHibrida put(TablaHibridaKey key, TablaHibrida tablaExperiencia) {
		 this.getCache().put(key, tablaExperiencia);
		 return tablaExperiencia;
	}

	@Override
	public TablaHibrida remove(Object key) {
		return (TablaHibrida) this.getCache().remove(key);
	}

	@Override
	public Collection<TablaHibrida> values() {
		return (Collection<TablaHibrida>) this.getCache().values();
	}
	
	public List<TablaHibrida> getValues(Timestamp fvalor,
			Integer compania, String cnegocio, String bt, String kramo, Integer kmodalidad, 
			Integer kgarantia, String riesgoactuarial, String sexo, Integer tablaBase){
		

		List<Filter> filtros = new ArrayList<Filter>();
		
		Timestamp T = new Timestamp(99993112);
		
		filtros.add( new GreaterEqualsFilter(fvalorExtractor, fvalor.getTime()));
		filtros.add( new EqualsFilter(companiaExtractor, compania));
		filtros.add( new EqualsFilter(cnegocioExtractor, cnegocio));
		filtros.add( new EqualsFilter(kbasetecExtractor, bt));
		filtros.add( new EqualsFilter(kramoExtractor, kramo));
		filtros.add( new EqualsFilter(kmodalidadExtractor, kmodalidad));
		filtros.add( new EqualsFilter(kgarantiaExtractor, kgarantia));
		filtros.add( new EqualsFilter(riesgoActuarialExtractor, riesgoactuarial));
		filtros.add( new EqualsFilter(sexoExtractor, sexo));	
		filtros.add( new EqualsFilter(tablaBaseExtractor, tablaBase));
		

		Filter[] filtrosArray = new Filter[filtros.size()];
		for (int i = 0; i < filtrosArray.length; i++) {
			filtrosArray[i]=filtros.get(i);
		}
	
		Filter allFilter = new AllFilter(filtrosArray);
		
		Set lista = this.getCache().entrySet(allFilter);
		ArrayList<TablaHibrida> values= new ArrayList<TablaHibrida>();
		
		Iterator iter = lista.iterator();
		while(iter.hasNext()){
		    Map.Entry entry = (Map.Entry) iter.next();
		    values.add((TablaHibrida) entry.getValue());
		}
			
		//ordenamos por fecha
		Collections.sort(values, Collections.reverseOrder(new Comparator<TablaHibrida>(){
			@Override
			public int compare(TablaHibrida o1, TablaHibrida o2) {
				return o1.getFvalor().compareTo(o2.getFvalor());
			}
		}));
		
		return values;
	}
	
	public List<TablaHibrida> getValuesGarantia(Timestamp fvalor,
			String cnegocio, String bt, String kramo, Integer kmodalidad, 
			Integer kgarantia,String sexo, Integer tablaBase){
		

		List<Filter> filtros = new ArrayList<Filter>();
		
		Timestamp T = new Timestamp(99993112);
		
		filtros.add( new GreaterEqualsFilter(fvalorExtractor, fvalor.getTime()));
		//filtros.add( new EqualsFilter(companiaExtractor, compania));
		filtros.add( new EqualsFilter(cnegocioExtractor, cnegocio));
		filtros.add( new EqualsFilter(kbasetecExtractor, bt));
		filtros.add( new EqualsFilter(kramoExtractor, kramo));
		filtros.add( new EqualsFilter(kmodalidadExtractor, kmodalidad));
		filtros.add( new EqualsFilter(kgarantiaExtractor, kgarantia));
		//filtros.add( new EqualsFilter(riesgoActuarialExtractor, riesgoactuarial));
		filtros.add( new EqualsFilter(sexoExtractor, sexo));	
		filtros.add( new EqualsFilter(tablaBaseExtractor, tablaBase));
		

		Filter[] filtrosArray = new Filter[filtros.size()];
		for (int i = 0; i < filtrosArray.length; i++) {
			filtrosArray[i]=filtros.get(i);
		}
	
		Filter allFilter = new AllFilter(filtrosArray);
		
		Set lista = this.getCache().entrySet(allFilter);
		ArrayList<TablaHibrida> values= new ArrayList<TablaHibrida>();
		
		Iterator iter = lista.iterator();
		while(iter.hasNext()){
		    Map.Entry entry = (Map.Entry) iter.next();
		    values.add((TablaHibrida) entry.getValue());
		}
			
		//ordenamos por fecha
		Collections.sort(values, Collections.reverseOrder(new Comparator<TablaHibrida>(){
			@Override
			public int compare(TablaHibrida o1, TablaHibrida o2) {
				return o1.getFvalor().compareTo(o2.getFvalor());
			}
		}));
		
		return values;
	}
	
	public List<TablaHibrida> getValuesSinGarantia(Timestamp fvalor,
			String cnegocio, String bt, String kramo, Integer kmodalidad, 
			String sexo, Integer tablaBase){
		

		List<Filter> filtros = new ArrayList<Filter>();
		
		Timestamp T = new Timestamp(99993112);
		
		filtros.add( new GreaterEqualsFilter(fvalorExtractor, fvalor.getTime()));
	//	filtros.add( new EqualsFilter(companiaExtractor, compania));
		filtros.add( new EqualsFilter(cnegocioExtractor, cnegocio));
		filtros.add( new EqualsFilter(kbasetecExtractor, bt));
		filtros.add( new EqualsFilter(kramoExtractor, kramo));
		filtros.add( new EqualsFilter(kmodalidadExtractor, kmodalidad));
	//	filtros.add( new EqualsFilter(kgarantiaExtractor, kgarantia));
	//  filtros.add( new EqualsFilter(riesgoActuarialExtractor, riesgoactuarial));
		filtros.add( new EqualsFilter(sexoExtractor, sexo));	
		filtros.add( new EqualsFilter(tablaBaseExtractor, tablaBase));
		

		Filter[] filtrosArray = new Filter[filtros.size()];
		for (int i = 0; i < filtrosArray.length; i++) {
			filtrosArray[i]=filtros.get(i);
		}
	
		Filter allFilter = new AllFilter(filtrosArray);
		
		Set lista = this.getCache().entrySet(allFilter);
		ArrayList<TablaHibrida> values= new ArrayList<TablaHibrida>();
		
		Iterator iter = lista.iterator();
		while(iter.hasNext()){
		    Map.Entry entry = (Map.Entry) iter.next();
		    values.add((TablaHibrida) entry.getValue());
		}
			
		//ordenamos por fecha
		Collections.sort(values, Collections.reverseOrder(new Comparator<TablaHibrida>(){
			@Override
			public int compare(TablaHibrida o1, TablaHibrida o2) {
				return o1.getFvalor().compareTo(o2.getFvalor());
			}
		}));
		
		return values;
	}
	
}