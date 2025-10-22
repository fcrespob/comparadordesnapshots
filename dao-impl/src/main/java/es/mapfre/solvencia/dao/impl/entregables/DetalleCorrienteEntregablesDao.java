package es.mapfre.solvencia.dao.impl.entregables;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.tangosol.util.ValueExtractor;
import com.tangosol.util.extractor.PofExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;

import es.mapfre.solvencia.coherence.keys.entregables.DetalleCorrienteEntregablesKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dominio.entregables.DetalleCorrienteEntregables;

public class DetalleCorrienteEntregablesDao extends DaoBaseSalidaCalculo implements Map<DetalleCorrienteEntregablesKey, DetalleCorrienteEntregables> {
	
	
	private static final String CACHE_NAME = "detalle-corriente-entregables";
	
	private final ValueExtractor btExtractor = new PofExtractor(String.class, DetalleCorrienteEntregables.IND_BT);
	private final ValueExtractor kcoaseoriExtractor = new ReflectionExtractor("getKcoaseOri");
	private final ValueExtractor swcasadoExtractor = new PofExtractor(String.class, DetalleCorrienteEntregables.IND_SWCASADO);
	
	
	
	
	public DetalleCorrienteEntregablesDao() {
		super();
		super.setCacheName(CACHE_NAME);	
		
		getCache().addIndex(kcoaseoriExtractor, false, null);
		getCache().addIndex(btExtractor, false, null);
		getCache().addIndex(swcasadoExtractor, false, null);			
	}

	
	@Override
	public DetalleCorrienteEntregables get(Object key) {
		if (key instanceof DetalleCorrienteEntregablesKey) {
			return (DetalleCorrienteEntregables) getCache().get(key);
		}
		return null;
	}

	@Override
	public DetalleCorrienteEntregables put(DetalleCorrienteEntregablesKey key, DetalleCorrienteEntregables value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;
	}

	@Override
	public DetalleCorrienteEntregables remove(Object key) {
		return (DetalleCorrienteEntregables) this.getCache().remove(key);
	}

	@Override
	public Collection<DetalleCorrienteEntregables> values() {
		return (Collection<DetalleCorrienteEntregables>) this.getCache().values();
	}

	@Override
	public Set<DetalleCorrienteEntregablesKey> keySet() {
		return this.getCache().keySet();
	}
	
	@Override
	public Set<java.util.Map.Entry<DetalleCorrienteEntregablesKey, DetalleCorrienteEntregables>> entrySet() {
		return (Set<Entry<DetalleCorrienteEntregablesKey, DetalleCorrienteEntregables>>) this.getCache().entrySet();
	}
	
	
}