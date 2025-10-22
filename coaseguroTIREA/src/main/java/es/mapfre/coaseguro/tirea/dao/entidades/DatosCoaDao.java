package es.mapfre.coaseguro.tirea.dao.entidades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import es.mapfre.coaseguro.tirea.dao.DaoBase;
import es.mapfre.coaseguro.tirea.dominio.entidades.DatosCoa;
import es.mapfre.coaseguro.tirea.dominio.entidades.Incidencia;
import es.mapfre.coaseguro.tirea.dominio.entidades.Tab35012;
import es.mapfre.coaseguro.tirea.dominio.keys.DatosCoaKey;
import es.mapfre.coaseguro.tirea.dominio.keys.Tab35012Key;
import es.mapfre.coaseguro.tirea.excepcion.Solvencia2Excepcion;
import es.mapfre.coaseguro.tirea.utils.beanio.BeanIOReader;
import net.sf.ehcache.Element;

public class DatosCoaDao extends DaoBase{

	private static final String CACHE_NAME = "DATOSCOA";

	private static final int BATCH_SIZE = 1000;
	
	public DatosCoaDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	public Set<Entry<DatosCoaKey, DatosCoa>> entrySet() {
		return (Set<Entry<DatosCoaKey, DatosCoa>>) this.getCache().getAll(getCache().getKeys());
	}

	public DatosCoa get(Object key) {
		if (!(key instanceof DatosCoaKey)) {
			return null;
		} else {
			return (DatosCoa) this.getCache().get(key).getObjectValue();
		}
	}

	public List<DatosCoaKey> keySet() {
		return this.getCache().getKeys();
	}

	public DatosCoa put(DatosCoaKey key, DatosCoa tab) {
		 this.getCache().put(new Element(key, tab));
		 return tab;
	}

	public boolean remove(Object key) {
		return this.getCache().remove(key);
	}

	public Collection<Element> values() {
		return (Collection<Element>) this.getAll().values();
	}
	
	public void loadCache(BeanIOReader reader) {
		DatosCoa tab = null;
		Set<Element> valores = new HashSet<Element>();
		int bloque = 0;
		while ((tab = (DatosCoa) reader.read()) != null) {
			valores.add(new Element(tab.getKey(), tab));
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
	
	public List<DatosCoa> getValues() {

		Collection<Element> fr = this.values();
		List<DatosCoa> lista = new ArrayList<DatosCoa>();
		if(fr == null || fr.isEmpty()){
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("10");
			inci.setInfAmpliada("No se han encontrado datos en el catalogo de datos de cuadros de coaseguro");
			throw new Solvencia2Excepcion(inci);
		}
		
		Iterator<Element> itFR= fr.iterator();
		while (itFR.hasNext()) {
			lista.add((DatosCoa) itFR.next().getObjectValue());
		}

		return lista;
	}
	
}
