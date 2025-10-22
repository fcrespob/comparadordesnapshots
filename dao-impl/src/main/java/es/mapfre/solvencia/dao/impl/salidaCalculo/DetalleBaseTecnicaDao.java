package es.mapfre.solvencia.dao.impl.salidaCalculo;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.extractor.ChainedExtractor;
import com.tangosol.util.extractor.PofExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.EqualsFilter;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.coherence.keys.salidaCalculo.DetalleBaseTecnicaKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;

public class DetalleBaseTecnicaDao extends DaoBaseSalidaCalculo  implements Map<DetalleBaseTecnicaKey, DetalleBaseTecnica>{

private static final String CACHE_NAME = "detalle-basetecnicas"; 

	private final ValueExtractor fcierreExtractor = new ChainedExtractor(new PofExtractor(Timestamp.class, DetalleBaseTecnica.IND_FECCIERRE), new ReflectionExtractor("getTime"));
	private final ValueExtractor btExtractor = new PofExtractor(String.class, DetalleBaseTecnica.IND_BASETEC);
	private final ValueExtractor umicKeyExtractor = new PofExtractor(UmicKey.class, DetalleBaseTecnica.IND_UMICKEY);
//	private final ValueExtractor cnegocioExtractor = new PofExtractor(String.class, DetalleBaseTecnica.IND_CNEGOCIO);
//	private final ValueExtractor ccanalExtractor = new PofExtractor(Integer.class, DetalleBaseTecnica.IND_CCANAL);
//	private final ValueExtractor ccarteraExtractor = new PofExtractor(Integer.class, DetalleBaseTecnica.IND_CCARTERA);

	
	public DetalleBaseTecnicaDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	@Override
	public Set<Entry<DetalleBaseTecnicaKey, DetalleBaseTecnica>> entrySet() {
		return (Set<Entry<DetalleBaseTecnicaKey, DetalleBaseTecnica>>) this.getCache().entrySet();
	}

	@Override
	public DetalleBaseTecnica get(Object key) {
		if (!(key instanceof DetalleBaseTecnicaKey)) {
			return null;
		} else {
			return (DetalleBaseTecnica) this.getCache().get(key);
		}
	}

	@Override
	public Set<DetalleBaseTecnicaKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public DetalleBaseTecnica put(DetalleBaseTecnicaKey key, DetalleBaseTecnica value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public DetalleBaseTecnica remove(Object key) {
		return (DetalleBaseTecnica) this.getCache().remove(key);
	}

	@Override
	public Collection<DetalleBaseTecnica> values() {
		return (Collection<DetalleBaseTecnica>) this.getCache().values();
	}
	
	@SuppressWarnings("rawtypes")
	public DetalleBaseTecnica getValues(String BT, Timestamp fcierre, UmicKey umicKey){
		DetalleBaseTecnica detBT = null;
		
		Filter fcierreFilter = new EqualsFilter(fcierreExtractor, fcierre.getTime());
		Filter btFilter = new EqualsFilter(btExtractor, BT);
		Filter umickeyFilter = new EqualsFilter(umicKeyExtractor, umicKey);
		
		Filter[] filtrosArray = new Filter[]{fcierreFilter, btFilter, umickeyFilter};
		
		Filter allFilter = new AllFilter(filtrosArray);
		
		Set values = this.getCache().entrySet(allFilter);
		
		if(values.size()==1){
			Map.Entry entry = (Map.Entry) values.iterator().next();
			detBT = (DetalleBaseTecnica) entry.getValue();
		}
		
		return detBT;
	}

	@Override
	protected Comparator exportOrdered() {

		return new Comparator<DetalleBaseTecnicaKey>() {

			@Override
			public int compare(DetalleBaseTecnicaKey dc1, DetalleBaseTecnicaKey dc2) {
				if (dc1 == null && dc2 != null) {
					return 1;
				} else if (dc2 == null) {
					return -1;
				} else if (dc1 != null ) {
					return dc1.compareTo(dc2);
				}
				
				return 0;
			}
			
		};
	}
}