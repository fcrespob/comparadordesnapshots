package es.mapfre.solvencia.dao.impl.gbt;

import java.math.BigDecimal;
import java.sql.Timestamp;
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
import com.tangosol.util.filter.GreaterEqualsFilter;
import com.tangosol.util.filter.LessEqualsFilter;

import es.mapfre.solvencia.coherence.keys.gbt.AsigTasasAnulKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.gbt.AsigTasasAnul;

public class AsigTasasAnulDao extends DaoBase implements Map<AsigTasasAnulKey, AsigTasasAnul> {

	private static final String CACHE_NAME = "ATA0"; 
	
	private final ValueExtractor kbasetecExtractor;
	private final ValueExtractor kcompaniaExtractor;
	private final ValueExtractor knegocioExtractor;
	private final ValueExtractor kramoExtractor;
	private final ValueExtractor kpinteresdesdeExtractor;
	private final ValueExtractor pintereshastaExtractor;
	private final ValueExtractor kmodalidadExtractor;
	private final ValueExtractor kfiniconversionExtractor;
	private final ValueExtractor ffinconversionExtractor;
	
	public AsigTasasAnulDao() {
		super();
		super.setCacheName(CACHE_NAME);		
		
		kbasetecExtractor = createExtractor("getKbasetec", String.class, AsigTasasAnul.IND_KBASETEC);
		kcompaniaExtractor = createExtractor("getKcompania", Integer.class, AsigTasasAnul.IND_KCOMPANIA);
		knegocioExtractor = createExtractor("getKnegocio", String.class, AsigTasasAnul.IND_KNEGOCIO);
		kramoExtractor = createExtractor("getKramo", String.class, AsigTasasAnul.IND_KRAMO);
		kpinteresdesdeExtractor = createExtractor("getKpinteresdesde", BigDecimal.class, AsigTasasAnul.IND_KPINTERESDESDE);
		pintereshastaExtractor = createExtractor("getPintereshasta", BigDecimal.class, AsigTasasAnul.IND_PINTERESHASTA);
		kmodalidadExtractor = createExtractor("getKmodalidad", Integer.class, AsigTasasAnul.IND_KMODALIDAD);
		kfiniconversionExtractor = createExtractor("getKfiniconversion", Timestamp.class, AsigTasasAnul.IND_KFINICONVERSION);
		ffinconversionExtractor = createExtractor("getFfinconversion", Timestamp.class, AsigTasasAnul.IND_FFINCONVERSION);
	}

	@Override
	public Set<java.util.Map.Entry<AsigTasasAnulKey, AsigTasasAnul>> entrySet() {
		return (Set<Entry<AsigTasasAnulKey, AsigTasasAnul>>) this.getCache().entrySet();
	}

	@Override
	public AsigTasasAnul get(Object key) {
		if (!(key instanceof AsigTasasAnulKey)) {
			return null;
		} else {
			return (AsigTasasAnul) this.getCache().get(key);
		}
	}

	@Override
	public Set<AsigTasasAnulKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public AsigTasasAnul put(AsigTasasAnulKey key, AsigTasasAnul value) {
		this.getCache().put(key, value);
		return value;
	}

	@Override
	public AsigTasasAnul remove(Object key) {
		return (AsigTasasAnul) this.getCache().remove(key);
	}

	@Override
	public Collection<AsigTasasAnul> values() {
		return (Collection<AsigTasasAnul>) this.getCache().values();
	}
	
	
	public List<AsigTasasAnul> obtenerTasasAnul(String kbasetec, Integer kcompania, String knegocio,
			String kramo,BigDecimal kpinteresdesde, Integer kmodalidad, Timestamp kfiniconversion)  {
		
		Filter allFilter = null;
		
		List<Filter> filtros = new ArrayList<Filter>();
		
		filtros.add(new EqualsFilter(kbasetecExtractor, kbasetec));
		filtros.add(new EqualsFilter(kcompaniaExtractor, kcompania));
		filtros.add(new EqualsFilter(knegocioExtractor, knegocio));
		filtros.add(new EqualsFilter(kramoExtractor, kramo));
		filtros.add(new EqualsFilter(kmodalidadExtractor, kmodalidad));
		filtros.add(new LessEqualsFilter(kpinteresdesdeExtractor, kpinteresdesde));
		filtros.add(new GreaterEqualsFilter(pintereshastaExtractor, kpinteresdesde));
		filtros.add(new LessEqualsFilter(kfiniconversionExtractor, kfiniconversion.getTime()));
		filtros.add(new GreaterEqualsFilter(ffinconversionExtractor,kfiniconversion.getTime()));
		
		Filter[] arrayFiltros = filtros.toArray(new Filter[filtros.size()]);
		
		allFilter = new AllFilter(arrayFiltros);
		
		List<AsigTasasAnul> asigTA = getTasasAnul(allFilter);
		
		return asigTA;
	}
	
	
//	public List<AsigTasasAnul> obtenerTasasAnul(String kbasetec, Integer kcompania, String knegocio,
//			String kramo,BigDecimal kpinteresdesde, Integer kmodalidad, Timestamp kfiniconversion)  {
//		
//		Filter allFilter = null;
//		
//		List<Filter> filtros = new ArrayList<Filter>();
//		
//		filtros.add(new EqualsFilter("getKbasetec", kbasetec));
//		filtros.add(new EqualsFilter("getKcompania", kcompania));
//		filtros.add(new EqualsFilter("getKnegocio", knegocio));
//		filtros.add(new EqualsFilter("getKramo", kramo));
//		filtros.add(new EqualsFilter("getKmodalidad", kmodalidad));
//		filtros.add(new GreaterEqualsFilter("getPintereshasta",kpinteresdesde));
//		filtros.add(new LessEqualsFilter("getKpinteresdesde", kpinteresdesde));
//		filtros.add(new GreaterEqualsFilter("getFfinconversion",kfiniconversion));
//		filtros.add(new LessEqualsFilter("getKfiniconversion", kfiniconversion));
//		
//		Filter[] arrayFiltros = filtros.toArray(new Filter[filtros.size()]);
//		
//		allFilter = new AllFilter(arrayFiltros);
//		
//		List<AsigTasasAnul> asigTA = getTasasAnul(allFilter);
//		
//		return asigTA;
//	}
	
	private Filter reducirFiltro (Filter[] filter, int contador) {
		
		Filter filtroReducido = null;
		
		Filter[] arrayReducido = new Filter[filter.length-contador];
		
		System.arraycopy(filter, 0, arrayReducido, 0, filter.length-contador);
		
		filtroReducido = new AllFilter(arrayReducido);
		
		return filtroReducido;
	}
	
	public List<AsigTasasAnul> getTasasAnul(Filter allFilter) {
		
		List<AsigTasasAnul> tasAnul=new ArrayList<AsigTasasAnul>();
		
		Set values = this.getCache().entrySet(allFilter);
		
			Iterator iter = values.iterator();
			while(iter.hasNext()){
			    Map.Entry entry = (Map.Entry) iter.next();
			    tasAnul.add((AsigTasasAnul) entry.getValue());
			}
		
		return tasAnul;
	}

	public List<AsigTasasAnul> obtenerTasasAnulPrueba(String baseTec, Integer kcompania,
			String negocio, String ramo,Integer kmodalidad, Timestamp kfiniconversion) {
		
		Filter allFilter = null;
		
		List<Filter> filtros = new ArrayList<Filter>();
		
		filtros.add(new EqualsFilter("getKbasetec", baseTec));
		filtros.add(new EqualsFilter("getKcompania", kcompania));
		filtros.add(new EqualsFilter("getKnegocio", negocio));
		filtros.add(new EqualsFilter("getKramo", ramo));
		filtros.add(new EqualsFilter("getKmodalidad", kmodalidad));
		filtros.add(new GreaterEqualsFilter("getFfinconversion",kfiniconversion));
		filtros.add(new LessEqualsFilter("getKfiniconversion", kfiniconversion));
		
		Filter[] arrayFiltros = filtros.toArray(new Filter[filtros.size()]);
		
		allFilter = new AllFilter(arrayFiltros);
		
		List<AsigTasasAnul> asigTA = getTasasAnul(allFilter);
		
		return asigTA;
	}
}
