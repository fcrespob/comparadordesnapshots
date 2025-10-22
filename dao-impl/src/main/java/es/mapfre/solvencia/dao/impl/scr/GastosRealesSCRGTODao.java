package es.mapfre.solvencia.dao.impl.scr;

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

import es.mapfre.solvencia.coherence.keys.conversionesBel.GastosRealesKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.conversionesBel.GastosReales;

public class GastosRealesSCRGTODao extends DaoBase implements
		Map<GastosRealesKey, GastosReales> {

	private static final String CACHE_NAME = "SCRGRE0";

	private final ValueExtractor cnegocioExtractor;
	private final ValueExtractor kmodalidadExtractor;
	private final ValueExtractor kramoExtractor;
	private final ValueExtractor ktipobtExtractor;
	private final ValueExtractor ccanalExtractor;

	private final ValueExtractor fecHastaExtractor; 
	private final ValueExtractor fecDesdeExtractor; 

	public GastosRealesSCRGTODao() {
		super();
		super.setCacheName(CACHE_NAME);
		
		cnegocioExtractor = createExtractor("getCnegocio", String.class, GastosReales.IND_CNEGOCIO);
		ccanalExtractor = createExtractor("getCcanal", Integer.class, GastosReales.IND_CCANAL);
		kmodalidadExtractor = createExtractor("getKmodalidad", Integer.class, GastosReales.IND_KMODALIDAD);
		kramoExtractor = createExtractor("getKramo", String.class, GastosReales.IND_KRAMO);
		ktipobtExtractor = createExtractor("getKtipobt", String.class, GastosReales.IND_KTIPOBT);

		fecHastaExtractor  = createExtractor("getFecHasta",Timestamp.class, GastosReales.IND_FECHASTA);
		fecDesdeExtractor  = createExtractor("getFecDesde",Timestamp.class, GastosReales.IND_FECDESDE);

		super.getCache().addIndex(cnegocioExtractor, true, null);
		super.getCache().addIndex(kmodalidadExtractor, true, null);
		super.getCache().addIndex(kramoExtractor, true, null);
		super.getCache().addIndex(ktipobtExtractor, true, null);
		super.getCache().addIndex(ccanalExtractor, true, null);
		
		super.getCache().addIndex(fecHastaExtractor, true, null);
		super.getCache().addIndex(fecDesdeExtractor, true, null);
	}

	@Override
	public Set<Entry<GastosRealesKey, GastosReales>> entrySet() {
		return (Set<Entry<GastosRealesKey, GastosReales>>) this.getCache()
				.entrySet();
	}

	public List<GastosReales> obtenerGastosReales(Integer ccanal, String cnegocio,
			Timestamp fecCierre, Integer kmodalidad,
			String kramo, String ktipobt)  {
		
		List<GastosReales> value = getGastosReales(ccanal, cnegocio, fecCierre, 
				kmodalidad, kramo, ktipobt, true);
		if (value.isEmpty()) {
			value = getGastosReales(ccanal, cnegocio, fecCierre, 
					kmodalidad, kramo, ktipobt, false);
		}
		return value;
	}

	public List<GastosReales> getGastosReales(Integer ccanal, String cnegocio,
			Timestamp fecCierre, Integer kmodalidad,
			String kramo, String ktipobt, Boolean conModalidad)
			 {

		Filter cnegocioFilter = new EqualsFilter(cnegocioExtractor, cnegocio);
		Filter ktipobtFilter = new EqualsFilter(ktipobtExtractor, ktipobt);
		Filter ccanalFilter = new EqualsFilter(ccanalExtractor, ccanal);
		Filter kramoFilter = new EqualsFilter(kramoExtractor, kramo);
		
		Filter fecHastaFilter = new GreaterEqualsFilter(fecHastaExtractor,	fecCierre.getTime());
		Filter fecDesdeFilter = new LessEqualsFilter(fecDesdeExtractor, fecCierre.getTime());

		Filter allFilter = null;
		
		if (conModalidad) {
			Filter kmodalidadFilter = new EqualsFilter(kmodalidadExtractor,
					kmodalidad);
			allFilter = new AllFilter(new Filter[] { cnegocioFilter,
					kmodalidadFilter, kramoFilter, ktipobtFilter, ccanalFilter,
					fecHastaFilter, fecDesdeFilter });
		}
		else {
			allFilter = new AllFilter(new Filter[] { cnegocioFilter,
					kramoFilter, ktipobtFilter, ccanalFilter, fecHastaFilter,
					fecDesdeFilter });
		}

		Set lista = this.getCache().entrySet(allFilter);

		List<GastosReales> values=new ArrayList<GastosReales>();
		
			Iterator<GastosReales> iter = lista.iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				values.add( (GastosReales) entry.getValue());
			}
			
		return values;
	}

	@Override
	public Set<GastosRealesKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public GastosReales put(GastosRealesKey key, GastosReales gastosReales) {
		this.getCache().put(key, gastosReales);
		return gastosReales;
	}

	@Override
	public GastosReales remove(Object key) {
		return (GastosReales) this.getCache().remove(key);
	}

	@Override
	public Collection<GastosReales> values() {
		return (Collection<GastosReales>) this.getCache().values();
	}

	@Override
	public GastosReales get(Object key) {
		return (GastosReales) this.getCache().get(key);
	}

}