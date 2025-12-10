package es.mapfre.coaseguro.tirea.dao.entidades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import es.mapfre.coaseguro.tirea.dao.DaoBase;
import es.mapfre.coaseguro.tirea.dominio.entidades.Incidencia;
import es.mapfre.coaseguro.tirea.dominio.entidades.TotPMaCoa;
import es.mapfre.coaseguro.tirea.dominio.entidades.TotPmaCoaBtcoa;
import es.mapfre.coaseguro.tirea.dominio.keys.TotPMaCoaKey;
import es.mapfre.coaseguro.tirea.dominio.keys.TotPmaCoaBtcoaKey;
import es.mapfre.coaseguro.tirea.excepcion.Solvencia2Excepcion;
import es.mapfre.coaseguro.tirea.utils.beanio.BeanIOReader;
import net.sf.ehcache.Element;

public class TotPmaCoaBtcoaDao extends DaoBase{

	private static final String CACHE_NAME = "TOTPMACOABTCOA";
	
	private static final int BATCH_SIZE = 1000;

	public TotPmaCoaBtcoaDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	public Set<Entry<TotPmaCoaBtcoaKey, TotPmaCoaBtcoa>> entrySet() {
		return (Set<Entry<TotPmaCoaBtcoaKey, TotPmaCoaBtcoa>>) this.getCache().getAll(getCache().getKeys());
	}

	public TotPmaCoaBtcoa get(Object key) {
		if (key instanceof TotPmaCoaBtcoaKey) {
			return (TotPmaCoaBtcoa) getCache().get(key).getObjectValue();
		}
		return null;
	}

	public List<TotPmaCoaBtcoaKey> keySet() {
		return this.getCache().getKeys();
	}

	public TotPmaCoaBtcoa put(TotPmaCoaBtcoaKey key, TotPmaCoaBtcoa value) {
		this.getCache().put(new Element(key, value));
		return value;
	}

	public boolean remove(Object key) {
		return this.getCache().remove(key);
	}

	public Collection<Element> values() {
		return (Collection<Element>) this.getAll().values();
	}
	
	public void loadCache(BeanIOReader reader) {
		TotPmaCoaBtcoa totPmaCoa = null;
		Set<Element> valores = new HashSet<Element>();
		int bloque = 0;
		while ((totPmaCoa = (TotPmaCoaBtcoa) reader.read()) != null) {
			valores.add(new Element(totPmaCoa.getKey(), totPmaCoa));
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
	
	public List<TotPmaCoaBtcoa> getValues() {

		Collection<Element> fr = this.values();
		List<TotPmaCoaBtcoa> lista = new ArrayList<TotPmaCoaBtcoa>();
		if(fr == null || fr.isEmpty()){
			es.mapfre.coaseguro.tirea.dominio.entidades.Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("10");
			inci.setInfAmpliada("No se han encontrado prestaciones reales en el fichero correspondiente.");
			throw new Solvencia2Excepcion(inci);
		}
		
		Iterator<Element> itFR= fr.iterator();
		while (itFR.hasNext()) {
			lista.add((TotPmaCoaBtcoa) itFR.next().getObjectValue());
		}

		return lista;
	}
	
}
