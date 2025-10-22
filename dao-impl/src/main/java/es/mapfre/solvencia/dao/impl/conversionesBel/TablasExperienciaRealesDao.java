//MODIFICACION: TAR00433819
//FECHA: 24/09/2018
//DESCRIP: EL ACCESO A LA TABLA DE VALORES REALISTA, SE HACE SIEMPRE CON "BEL", INDEPENDIENTEMENTE DE LA BASE TECNICA.
//         ADEMAS SE INCLUYE EL TRATAMIENTO PARA QUE RECUPERE LOS VALORES ANTERIORES O IGUALES A LA FECHA DE CIERRE, PARA 
//         EN CASO DE NO ENCONTRAR EN EL CIERRE, TOME LOS ANTERIORES.

package es.mapfre.solvencia.dao.impl.conversionesBel;

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

import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.EqualsFilter;
import com.tangosol.util.filter.GreaterEqualsFilter;
import com.tangosol.util.filter.LessEqualsFilter;

import es.mapfre.solvencia.coherence.keys.conversionesBel.TablasExperienciaRealesKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.conversionesBel.TablasExperienciaReales;

public class TablasExperienciaRealesDao extends DaoBase  implements Map<TablasExperienciaRealesKey, TablasExperienciaReales> {
		
	private static final String CACHE_NAME = "VTR0"; 
	
	private final ValueExtractor feccierreExtractor; 
	private final ValueExtractor kbasetecExtractor;
	private final ValueExtractor companiaExtractor;
	private final ValueExtractor cnegocioExtractor;
	private final ValueExtractor riesgoActuarialExtractor;
	private final ValueExtractor sexoExtractor;
	private final ValueExtractor categoriaExtractor;
	private final ValueExtractor edadFijaExtractor;
	private final ValueExtractor kmodalidadExtractor;
	private final ValueExtractor tablaBaseExtractor;
	private final ValueExtractor generacionExtractor;
	
	
	public TablasExperienciaRealesDao() {
		super();
		super.setCacheName(CACHE_NAME);
		feccierreExtractor = createExtractor("getFecCierre", Timestamp.class, TablasExperienciaReales.IND_FECCIERRE);
		kbasetecExtractor = createExtractor("getKbasetec", String.class, TablasExperienciaReales.IND_KBASETEC);
		companiaExtractor = createExtractor("getCompania", Integer.class, TablasExperienciaReales.IND_COMPANIA);
		cnegocioExtractor = createExtractor("getCnegocio", String.class, TablasExperienciaReales.IND_CNEGOCIO);
		riesgoActuarialExtractor = createExtractor("getRiesgoActuarial", String.class, TablasExperienciaReales.IND_RIESGOACTUARIAL);
		sexoExtractor = createExtractor("getSexo", String.class, TablasExperienciaReales.IND_SEXO);		
		categoriaExtractor = createExtractor("getCategoria", String.class, TablasExperienciaReales.IND_CATEGORIA);
		edadFijaExtractor = createExtractor("getEdadFija", Integer.class, TablasExperienciaReales.IND_EDADFIJA);
		kmodalidadExtractor = createExtractor("getKmodalidad", Integer.class, TablasExperienciaReales.IND_KMODALIDAD);
		tablaBaseExtractor = createExtractor("getTablaBase", Integer.class, TablasExperienciaReales.IND_TABLABASE);
		generacionExtractor = createExtractor("getGeneracion", Integer.class, TablasExperienciaReales.IND_GENERACION);
		
		getCache().addIndex(feccierreExtractor, true, null);
		getCache().addIndex(kbasetecExtractor, false, null);
		getCache().addIndex(companiaExtractor, false, null);
		getCache().addIndex(cnegocioExtractor, false, null);
		getCache().addIndex(riesgoActuarialExtractor, false, null);
		getCache().addIndex(sexoExtractor, false, null);
		getCache().addIndex(categoriaExtractor, false, null);
		getCache().addIndex(edadFijaExtractor, false, null);
		getCache().addIndex(kmodalidadExtractor, false, null);
		getCache().addIndex(tablaBaseExtractor, false, null);
		getCache().addIndex(generacionExtractor, false, null);
		
	}

	@Override
	public TablasExperienciaReales get(Object key) {
		return (TablasExperienciaReales) this.getCache().get(key);
	}

	@Override
	public TablasExperienciaReales put(TablasExperienciaRealesKey key, TablasExperienciaReales value) {
		 this.getCache().put(key, value);
		 return value;
	}

	@Override
	public TablasExperienciaReales remove(Object key) {
		return (TablasExperienciaReales) this.getCache().remove(key);
	}

	@Override
	public Set<TablasExperienciaRealesKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public Collection<TablasExperienciaReales> values() {
		return (Collection<TablasExperienciaReales>) this.getCache().values();
	}

	@Override
	public Set<java.util.Map.Entry<TablasExperienciaRealesKey, TablasExperienciaReales>> entrySet() {
		return (Set<Entry<TablasExperienciaRealesKey, TablasExperienciaReales>>) this.getCache().entrySet();
	}
	
	public List<TablasExperienciaReales> getValues(Timestamp fecCierre,
			String kbasetec, Integer compania, String cnegocio,
			String riesgoActuarial, String sexo, String categoria,
			Integer edad, Integer kmodalidad, Integer tablaBase, Integer generacion, Integer anoPoliza,
			BigDecimal sobrerriesgo, BigDecimal sobremortalidad) {

		List<Filter> filtros = new ArrayList<Filter>();
		
//INI-TAR00433819		
//		filtros.add( new EqualsFilter(feccierreExtractor, fecCierre.getTime()));
//		filtros.add( new EqualsFilter(kbasetecExtractor, kbasetec));
		filtros.add( new LessEqualsFilter(feccierreExtractor, fecCierre.getTime()));
		filtros.add( new EqualsFilter(kbasetecExtractor, "BEL"));
//FIN-TAR00433819
		filtros.add( new EqualsFilter(companiaExtractor, compania));
		filtros.add( new EqualsFilter(cnegocioExtractor, cnegocio));
		filtros.add( new EqualsFilter(riesgoActuarialExtractor, riesgoActuarial));
		filtros.add( new EqualsFilter(sexoExtractor, sexo));
		filtros.add( new EqualsFilter(categoriaExtractor, categoria));
		
		filtros.add( new EqualsFilter(edadFijaExtractor, edad));
		filtros.add( new EqualsFilter(kmodalidadExtractor, kmodalidad));
		filtros.add( new EqualsFilter(tablaBaseExtractor, tablaBase));
		filtros.add( new EqualsFilter(generacionExtractor, generacion));
		

		Filter[] filtrosArray = new Filter[filtros.size()];
		for (int i = 0; i < filtrosArray.length; i++) {
			filtrosArray[i]=filtros.get(i);
		}
	
		Filter allFilter = new AllFilter(filtrosArray);
		
		Set lista = this.getCache().entrySet(allFilter);
		ArrayList<TablasExperienciaReales> values= new ArrayList<TablasExperienciaReales>();
		
		Iterator iter = lista.iterator();
		while(iter.hasNext()){
		    Map.Entry entry = (Map.Entry) iter.next();
		    values.add((TablasExperienciaReales) entry.getValue());
		}
			
		//ordenamos por fecha
		Collections.sort(values, Collections.reverseOrder(new Comparator<TablasExperienciaReales>(){
			@Override
			public int compare(TablasExperienciaReales o1, TablasExperienciaReales o2) {
				return o1.getFecCierre().compareTo(o2.getFecCierre());
			}
		}));
		
		return values;
	}
}