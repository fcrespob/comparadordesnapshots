package es.mapfre.solvencia.dao.impl.maestro;

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

import es.mapfre.solvencia.coherence.keys.maestro.DatosEspecificosKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.maestro.DatosEspecificos;


public class DatosEspecificosDao extends DaoBase implements Map<DatosEspecificosKey, DatosEspecificos> {
	
	private static final String CACHE_NAME = "X880J004"; 
	
	private final ValueExtractor kpolizaExtractor;
	private final ValueExtractor ksubpolizaExtractor;
	private final ValueExtractor kcertificadoExtractor;
	private final ValueExtractor nsuscripcionExtractor;
	private final ValueExtractor codigoExtractor;
	
	public DatosEspecificosDao() {
		super();
		super.setCacheName(CACHE_NAME);
		
		kpolizaExtractor = createExtractor("getKpoliza", Long.class,
				DatosEspecificos.IND_KPOLIZA);
		ksubpolizaExtractor = createExtractor("getKsubpol", Integer.class,
				DatosEspecificos.IND_KSUBPOL);
		kcertificadoExtractor = createExtractor("getKcerti", Integer.class,
				DatosEspecificos.IND_KCERTI);
		nsuscripcionExtractor = createExtractor("getNsuscri", Integer.class, 
				DatosEspecificos.IND_NSUSCRI);
		codigoExtractor = createExtractor("getCodigo", String.class,
				DatosEspecificos.IND_CODIGO);
		
		super.getCache().addIndex(kpolizaExtractor, false, null);
		super.getCache().addIndex(ksubpolizaExtractor, false, null);
		super.getCache().addIndex(kcertificadoExtractor, false, null);
		super.getCache().addIndex(nsuscripcionExtractor, false,  null);
		super.getCache().addIndex(codigoExtractor, false, null);
	}


	@Override
	public Set<Entry<DatosEspecificosKey, DatosEspecificos>> entrySet() {
		return (Set<Entry<DatosEspecificosKey, DatosEspecificos>>) this.getCache().entrySet();
	}

	@Override
	public DatosEspecificos get(Object key) {
		if (!(key instanceof DatosEspecificosKey)) {
			return null;
		} else {
			return (DatosEspecificos) this.getCache().get(key);
		}
	}

	@Override
	public Set<DatosEspecificosKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public DatosEspecificos put(DatosEspecificosKey key, DatosEspecificos datosEspecificos) {
		 this.getCache().put(key, datosEspecificos);
		 return datosEspecificos;
	}

	@Override
	public DatosEspecificos remove(Object key) {
		return (DatosEspecificos) this.getCache().remove(key);
	}

	@Override
	public Collection<DatosEspecificos> values() {
		return (Collection<DatosEspecificos>) this.getCache().values();
	}
	
	public DatosEspecificos getValues(Long poliza, Integer subpoliza, Integer certificado, Integer nsuscripcion, String codigo){
		
		Filter polizaFilter = new EqualsFilter(kpolizaExtractor, poliza);
		Filter subpolizaFilter = new EqualsFilter(ksubpolizaExtractor, subpoliza);
		Filter certificadoFilter = new EqualsFilter(kcertificadoExtractor, certificado);
		Filter nsuscripcionFilter = new EqualsFilter(nsuscripcionExtractor, nsuscripcion);
		Filter codigoFilter = new EqualsFilter(codigoExtractor, codigo);

		Filter allFilter  = new AllFilter(
				new Filter[] {polizaFilter, subpolizaFilter, certificadoFilter, nsuscripcionFilter, codigoFilter});

		Set values = this.getCache().entrySet(allFilter);
		
		List<DatosEspecificos> datosEspecificos = new ArrayList<DatosEspecificos>();
		
		Iterator iter = values.iterator();
		if (iter.hasNext()) {
		    Map.Entry entry = (Map.Entry) iter.next();
		    datosEspecificos.add( (DatosEspecificos) entry.getValue());
		} else {
			return null;
		}
		
		
		return datosEspecificos.get(0);
	}
}