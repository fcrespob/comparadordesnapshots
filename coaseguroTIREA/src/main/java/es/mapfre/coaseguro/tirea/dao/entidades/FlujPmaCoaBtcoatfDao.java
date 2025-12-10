package es.mapfre.coaseguro.tirea.dao.entidades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import es.mapfre.coaseguro.tirea.dao.DaoBase;
import es.mapfre.coaseguro.tirea.dominio.entidades.FlujPmaCoaBtcoatf;
import es.mapfre.coaseguro.tirea.dominio.entidades.Incidencia;
import es.mapfre.coaseguro.tirea.dominio.keys.FlujPmaCoaBtcoatfKey;
import es.mapfre.coaseguro.tirea.excepcion.Solvencia2Excepcion;
import es.mapfre.coaseguro.tirea.utils.beanio.BeanIOReader;
import net.sf.ehcache.Element;

public class FlujPmaCoaBtcoatfDao extends DaoBase {

	private static final String CACHE_NAME = "FLUJPMACOABTCOATF";
	
	private static final int BATCH_SIZE = 1000;


	public FlujPmaCoaBtcoatfDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	public Set<Entry<FlujPmaCoaBtcoatfKey, FlujPmaCoaBtcoatf>> entrySet() {
		return (Set<Entry<FlujPmaCoaBtcoatfKey, FlujPmaCoaBtcoatf>>) this.getCache().getAll(getCache().getKeys());
	}

	public FlujPmaCoaBtcoatf get(Object key) {
		if (key instanceof FlujPmaCoaBtcoatfKey) {
			return (FlujPmaCoaBtcoatf) getCache().get(key).getObjectValue();
		}
		return null;
	}

	public List<FlujPmaCoaBtcoatfKey> keySet() {
		return this.getCache().getKeys();
	}

	public FlujPmaCoaBtcoatf put(FlujPmaCoaBtcoatfKey key, FlujPmaCoaBtcoatf value) {
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
		return new Comparator<FlujPmaCoaBtcoatfKey>() {
			@Override
			public int compare(FlujPmaCoaBtcoatfKey dc1, FlujPmaCoaBtcoatfKey dc2) {
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
		FlujPmaCoaBtcoatf flujPMaCoa = null;
		Set<Element> valores = new HashSet<Element>();
		int bloque = 0;
		while ((flujPMaCoa = (FlujPmaCoaBtcoatf) reader.read()) != null) {
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
	
	public List<FlujPmaCoaBtcoatf> getValues() {

		Collection<Element> fr = this.values();
		List<FlujPmaCoaBtcoatf> lista = new ArrayList<FlujPmaCoaBtcoatf>();
		if(fr == null || fr.isEmpty()){
			es.mapfre.coaseguro.tirea.dominio.entidades.Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("10");
			inci.setInfAmpliada("No se han encontrado prestaciones reales en el fichero correspondiente.");
			throw new Solvencia2Excepcion(inci);
		}
		
		Iterator<Element> itFR= fr.iterator();
		while (itFR.hasNext()) {
			lista.add((FlujPmaCoaBtcoatf) itFR.next().getObjectValue());
		}

		return lista;
	}
	
	
}
