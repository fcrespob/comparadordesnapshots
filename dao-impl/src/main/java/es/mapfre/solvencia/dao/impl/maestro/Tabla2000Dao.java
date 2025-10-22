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

import es.mapfre.solvencia.coherence.keys.maestro.Tabla2000Key;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.maestro.Tabla2000;
import es.mapfre.solvencia.dominio.maestro.ValoresLiquidativos;


public class Tabla2000Dao extends DaoBase implements Map<Tabla2000Key, Tabla2000> {
	
	private static final String CACHE_NAME = "TABLA2000"; 
	
	private final ValueExtractor codigoExtractor;
	
	
	public Tabla2000Dao() {
		super();
		super.setCacheName(CACHE_NAME);
		
		codigoExtractor = createExtractor("getCodigo", String.class,
				Tabla2000.IND_CODIGO);
		
		super.getCache().addIndex(codigoExtractor, false, null);
	}


	@Override
	public Set<Entry<Tabla2000Key, Tabla2000>> entrySet() {
		return (Set<Entry<Tabla2000Key, Tabla2000>>) this.getCache().entrySet();
	}

	@Override
	public Tabla2000 get(Object key) {
		if (!(key instanceof Tabla2000Key)) {
			return null;
		} else {
			return (Tabla2000) this.getCache().get(key);
		}
	}

	@Override
	public Set<Tabla2000Key> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public Tabla2000 put(Tabla2000Key key, Tabla2000 tabla2000) {
		 this.getCache().put(key, tabla2000);
		 return tabla2000;
	}

	@Override
	public Tabla2000 remove(Object key) {
		return (Tabla2000) this.getCache().remove(key);
	}

	@Override
	public Collection<Tabla2000> values() {
		return (Collection<Tabla2000>) this.getCache().values();
	}
	
	public Tabla2000 getValues(String codigo){
		
		List<Filter> filtros = new ArrayList<Filter>();
		
		EqualsFilter codigoFilter = new EqualsFilter(codigoExtractor, codigo);
		
		filtros.add(codigoFilter);
		
		Filter[] arrayFiltros = new Filter[filtros.size()];
		for (int i = 0; i < arrayFiltros.length; i++) {
			arrayFiltros[i] = filtros.get(i);
		}
		
		Filter allFilter = new AllFilter(arrayFiltros);
		Set values = this.getCache().entrySet(allFilter);
		
		List<Tabla2000> tabla2000 = new ArrayList<Tabla2000>();
		
		Iterator iter = values.iterator();
		if (iter.hasNext()) {
		    Map.Entry entry = (Map.Entry) iter.next();
		    tabla2000.add( (Tabla2000) entry.getValue());
		} else {
			return null;
		}
		
		return tabla2000.get(0);
	}
}