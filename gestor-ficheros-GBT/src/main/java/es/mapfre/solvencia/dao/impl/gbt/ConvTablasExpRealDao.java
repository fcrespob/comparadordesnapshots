//MODIFICACION: TAR00433819
//FECHA: 24/09/2018
//DESCRIP: EL ACCESO A LA TABLA DE CONVERSION REALISTA, SE HACE SIEMPRE CON "BEL", INDEPENDIENTEMENTE DE LA BASE TECNICA.
//

package es.mapfre.solvencia.dao.impl.gbt;

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
import com.tangosol.util.filter.GreaterEqualsFilter;
import com.tangosol.util.filter.LessEqualsFilter;

import es.mapfre.solvencia.coherence.keys.gbt.ConvTablasExpRealKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.gbt.ConvTablasExpReal;

public class ConvTablasExpRealDao extends DaoBase implements Map<ConvTablasExpRealKey, ConvTablasExpReal> {
	
	private static final String CACHE_NAME = "ATR0";
	
	private final ValueExtractor kbasetecExtractor;
	private final ValueExtractor kcompaniaExtractor;
	private final ValueExtractor knegocioExtractor;
	private final ValueExtractor kriesgoExtractor;
	private final ValueExtractor ksexoExtractor;
	private final ValueExtractor kcategExtractor;
	private final ValueExtractor kedadDesdeExtractor;
	private final ValueExtractor edadHastaExtractor;
	private final ValueExtractor kmodalidadExtractor;
	private final ValueExtractor kfdesdeExtractor;
	private final ValueExtractor fhastaExtractor;
	
	public ConvTablasExpRealDao() {
		super();
		super.setCacheName(CACHE_NAME);
		
		kbasetecExtractor = createExtractor("getKbasetec", String.class, ConvTablasExpReal.IND_KBASETEC);
		kcompaniaExtractor = createExtractor("getKcompania", Integer.class, ConvTablasExpReal.IND_KCOMPANIA);
		knegocioExtractor = createExtractor("getKnegocio", String.class, ConvTablasExpReal.IND_KNEGOCIO);
		kriesgoExtractor = createExtractor("getKriesgo", String.class, ConvTablasExpReal.IND_KRIESGO);
		ksexoExtractor = createExtractor("getKsexo", String.class, ConvTablasExpReal.IND_KSEXO);
		kcategExtractor = createExtractor("getKcateg", String.class, ConvTablasExpReal.IND_KCATEG);
		kedadDesdeExtractor = createExtractor("getKedaddesde", Integer.class, ConvTablasExpReal.IND_KEDADDESDE);
		edadHastaExtractor = createExtractor("getEdadhasta", Integer.class, ConvTablasExpReal.IND_EDADHASTA);
		kmodalidadExtractor = createExtractor("getKmodalidad", Integer.class, ConvTablasExpReal.IND_KMODALIDAD);
		kfdesdeExtractor = createExtractor("getKfdesde", Timestamp.class, ConvTablasExpReal.IND_KFDESDE);
		fhastaExtractor = createExtractor("getFhasta", Timestamp.class, ConvTablasExpReal.IND_FHASTA);
	}
	
	@Override
	public Set<java.util.Map.Entry<ConvTablasExpRealKey, ConvTablasExpReal>> entrySet() {
		return (Set<Entry<ConvTablasExpRealKey, ConvTablasExpReal>>) this.getCache().entrySet();
	}

	@Override
	public ConvTablasExpReal get(Object key) {
		if (!(key instanceof ConvTablasExpRealKey)) {
			return null;
		} else {
			return (ConvTablasExpReal) this.getCache().get(key);
		}
	}

	@Override
	public Set<ConvTablasExpRealKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public ConvTablasExpReal put(ConvTablasExpRealKey key,
			ConvTablasExpReal value) {
		this.getCache().put(key, value);
		return value;
	}

	@Override
	public ConvTablasExpReal remove(Object key) {
		return (ConvTablasExpReal) this.getCache().remove(key);
	}

	@Override
	public Collection<ConvTablasExpReal> values() {
		return (Collection<ConvTablasExpReal>) this.getCache().values();
	}
	
	private static Comparator<ConvTablasExpReal> ordenadorValores = new Comparator<ConvTablasExpReal>() {
		@Override
		public int compare(ConvTablasExpReal o1, ConvTablasExpReal o2) {
			return o2.getKfdesde().compareTo(o1.getKfdesde());
		}
	};
	
	public List<ConvTablasExpReal> obtenerTablasExp(String kbasetec, Integer kcompania,
			String knegocio, String kriesgo, String ksexo, String kcateg,
			Integer kedad, Integer kmodalidad, Timestamp fcierre)  {
		
		Filter allFilter = null;
		
		List<Filter> filtros = new ArrayList<Filter>();

//INI-TAR00433819
//		filtros.add(new EqualsFilter(kbasetecExtractor, kbasetec));
		filtros.add( new EqualsFilter(kbasetecExtractor, "BEL"));
//FIN-TAR00433819
		filtros.add(new EqualsFilter(kcompaniaExtractor, kcompania));
		filtros.add(new EqualsFilter(knegocioExtractor, knegocio));
		filtros.add(new EqualsFilter(kriesgoExtractor, kriesgo));
		filtros.add(new EqualsFilter(ksexoExtractor, ksexo));
		filtros.add(new EqualsFilter(kcategExtractor, kcateg));
		filtros.add(new LessEqualsFilter(kedadDesdeExtractor, kedad));
		filtros.add(new GreaterEqualsFilter(edadHastaExtractor, kedad));
		filtros.add(new EqualsFilter(kmodalidadExtractor, kmodalidad));
		filtros.add(new LessEqualsFilter(kfdesdeExtractor, fcierre.getTime()));
		filtros.add(new GreaterEqualsFilter(fhastaExtractor, fcierre.getTime()));
		
		Filter[] arrayFiltros = filtros.toArray(new Filter[filtros.size()]);
		
		allFilter = new AllFilter(arrayFiltros);
		
		List<ConvTablasExpReal> tablasExp = getTablasExp(allFilter);
		
		return tablasExp;
	}
	
	public List<ConvTablasExpReal> obtenerTablasExpAnt(String kbasetec, Integer kcompania,
			String knegocio, String kriesgo, String ksexo, String kcateg,
			Integer kedad, Integer kmodalidad, Timestamp fcierre)  {
		
		Filter allFilter = null;
		
		List<Filter> filtros = new ArrayList<Filter>();
	
//INI-TAR00433819
//		filtros.add(new EqualsFilter(kbasetecExtractor, kbasetec));
		filtros.add( new EqualsFilter(kbasetecExtractor, "BEL"));
//FIN-TAR00433819
		filtros.add(new EqualsFilter(kcompaniaExtractor, kcompania));
		filtros.add(new EqualsFilter(knegocioExtractor, knegocio));
		filtros.add(new EqualsFilter(kriesgoExtractor, kriesgo));
		filtros.add(new EqualsFilter(ksexoExtractor, ksexo));
		filtros.add(new EqualsFilter(kcategExtractor, kcateg));
		filtros.add(new LessEqualsFilter(kedadDesdeExtractor, kedad));
		filtros.add(new GreaterEqualsFilter(edadHastaExtractor, kedad));
		filtros.add(new EqualsFilter(kmodalidadExtractor, kmodalidad));
		filtros.add(new LessEqualsFilter(kfdesdeExtractor, fcierre.getTime()));
		
		Filter[] arrayFiltros = filtros.toArray(new Filter[filtros.size()]);
		
		allFilter = new AllFilter(arrayFiltros);
		
		List<ConvTablasExpReal> tablasExp = getTablasExp(allFilter);
		
		return tablasExp;
	}
	
	private Filter reducirFiltro (Filter[] filter, int contador) {
		
		Filter filtroReducido = null;
		
		Filter[] arrayReducido = new Filter[filter.length-contador];
		
		System.arraycopy(filter, 0, arrayReducido, 0, filter.length-contador);
		
		filtroReducido = new AllFilter(arrayReducido);
		
		return filtroReducido;
	}
	
	public List<ConvTablasExpReal> getTablasExp(Filter allFilter) {
		
		List<ConvTablasExpReal> tablaExp=new ArrayList<ConvTablasExpReal>();
		
		Set values = this.getCache().entrySet(allFilter,ordenadorValores);
		
			Iterator iter = values.iterator();
			while(iter.hasNext()){
			    Map.Entry entry = (Map.Entry) iter.next();
			    tablaExp.add((ConvTablasExpReal) entry.getValue());
			}
		
		return tablaExp;
	}

}
