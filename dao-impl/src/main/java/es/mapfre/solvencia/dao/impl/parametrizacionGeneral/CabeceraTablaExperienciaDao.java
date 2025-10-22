package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
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

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.CabeceraTablaExperienciaKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.CabeceraTablaExperiencia;

@Portable
public class CabeceraTablaExperienciaDao extends DaoBase implements Map<CabeceraTablaExperienciaKey, CabeceraTablaExperiencia>{
	
	private static final String CACHE_NAME = "TXP0"; 
	
	private final ValueExtractor ktablaExtractor;
	private final ValueExtractor fecAnulaExtractor;
	private final ValueExtractor fecAltaExtractor;


	public CabeceraTablaExperienciaDao() {
		super();
		super.setCacheName(CACHE_NAME);
		ktablaExtractor   = createExtractor("getKtabla",Integer.class, CabeceraTablaExperiencia.IND_KTABLA);
		fecAnulaExtractor = createExtractor("getFecAnula",Timestamp.class, CabeceraTablaExperiencia.IND_FECANULA);
		fecAltaExtractor  = createExtractor("getFecAlta",Timestamp.class, CabeceraTablaExperiencia.IND_FECALTA);
		
		getCache().addIndex(ktablaExtractor, false, null);
		
	}

	@Override
	public Set<Entry<CabeceraTablaExperienciaKey, CabeceraTablaExperiencia>> entrySet() {
		return (Set<Entry<CabeceraTablaExperienciaKey, CabeceraTablaExperiencia>>) this.getCache().entrySet();
	}

	@Override
	public CabeceraTablaExperiencia get(Object key) {
		if (!(key instanceof CabeceraTablaExperienciaKey)) {
			return null;
		} else {
			return (CabeceraTablaExperiencia) this.getCache().get(key);
		}
	}

	@Override
	public Set<CabeceraTablaExperienciaKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public CabeceraTablaExperiencia put(CabeceraTablaExperienciaKey key, CabeceraTablaExperiencia cabeceraTablaExperiencia) {
		 this.getCache().put(key, cabeceraTablaExperiencia);
		 return cabeceraTablaExperiencia;
	}

	@Override
	public CabeceraTablaExperiencia remove(Object key) {
		return (CabeceraTablaExperiencia) this.getCache().remove(key);
	}

	@Override
	public Collection<CabeceraTablaExperiencia> values() {
		return (Collection<CabeceraTablaExperiencia>) this.getCache().values();
	}

	public List<CabeceraTablaExperiencia> getValue(Integer ktabla, Timestamp feccierre) {
		
		Filter ktablaFilter = new EqualsFilter(ktablaExtractor, ktabla);
		
		Filter allFilter = new AllFilter(new Filter[]{ktablaFilter});
		
		
		Set keys = this.getCache().keySet(allFilter);
		Set values = this.getCache().getAll(keys).entrySet();
		
		List<CabeceraTablaExperiencia> cabeceras=new ArrayList<CabeceraTablaExperiencia>();
		
			Iterator iter = values.iterator();
			while(iter.hasNext()){
			    Map.Entry entry = (Map.Entry) iter.next();
			    cabeceras.add( (CabeceraTablaExperiencia) entry.getValue());

			}
		
		return cabeceras;
	}

}
