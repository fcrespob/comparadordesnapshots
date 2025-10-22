package es.mapfre.coaseguro.tirea.dao.entidades;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import es.mapfre.coaseguro.tirea.dao.DaoBase;
import es.mapfre.coaseguro.tirea.dominio.entidades.FlujPMaCoa;
import es.mapfre.coaseguro.tirea.dominio.entidades.Incidencia;
import es.mapfre.coaseguro.tirea.dominio.entidades.TotPMaCoa;
import es.mapfre.coaseguro.tirea.dominio.keys.FlujPMaCoaKey;
import es.mapfre.coaseguro.tirea.dominio.keys.PesosBtKey;
import es.mapfre.coaseguro.tirea.excepcion.Solvencia2Excepcion;
import es.mapfre.coaseguro.tirea.utils.beanio.BeanIOReader;
import net.sf.ehcache.Element;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.Results;

public class FlujPMaCoaDao extends DaoBase{

	private static final String CACHE_NAME = "FLUJPMACOA";
	
	private static final int BATCH_SIZE = 1000;


	public FlujPMaCoaDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	public Set<Entry<FlujPMaCoaKey, FlujPMaCoa>> entrySet() {
		return (Set<Entry<FlujPMaCoaKey, FlujPMaCoa>>) this.getCache().getAll(getCache().getKeys());
	}

	public FlujPMaCoa get(Object key) {
		if (key instanceof FlujPMaCoaKey) {
			return (FlujPMaCoa) getCache().get(key).getObjectValue();
		}
		return null;
	}

	public List<FlujPMaCoaKey> keySet() {
		return this.getCache().getKeys();
	}

	public FlujPMaCoa put(FlujPMaCoaKey key, FlujPMaCoa value) {
		this.getCache().put(new Element(key, value));
		return value;
	}

	public boolean remove(Object key) {
		return this.getCache().remove(key);
	}

	public Collection<Element> values() {
		return (Collection<Element>) this.getAll().values();
	}

	protected Comparator exportOrdered() {
		return new Comparator<FlujPMaCoaKey>() {
			@Override
			public int compare(FlujPMaCoaKey dc1, FlujPMaCoaKey dc2) {
				if (dc1 == null && dc2 != null) {
					return 1;
				} else if (dc1 != null) {
					return dc1.compareTo(dc2);
				} else {
					return -1;
				}
			}
		};
	}

	public void loadCache(BeanIOReader reader) {
		FlujPMaCoa flujPMaCoa = null;
		Set<Element> valores = new HashSet<Element>();
		int bloque = 0;
		while ((flujPMaCoa = (FlujPMaCoa) reader.read()) != null) {
			valores.add(new Element(flujPMaCoa.getKey(), flujPMaCoa));
			bloque++;
			if (bloque % BATCH_SIZE == 0) {
				this.putAll(valores);
				valores.clear();
			}
		}
		if (valores.size() > 0) {
			this.putAll(valores);
			valores.clear();
		}
	}
	
	public List<FlujPMaCoa> getValues() {

		Collection<Element> fr = this.values();
		List<FlujPMaCoa> lista = new ArrayList<FlujPMaCoa>();
		if(fr == null || fr.isEmpty()){
			es.mapfre.coaseguro.tirea.dominio.entidades.Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("10");
			inci.setInfAmpliada("No se han encontrado prestaciones reales en el fichero correspondiente.");
			throw new Solvencia2Excepcion(inci);
		}
		
		Iterator<Element> itFR= fr.iterator();
		while (itFR.hasNext()) {
			lista.add((FlujPMaCoa) itFR.next().getObjectValue());
		}

		return lista;
	}
	
}
