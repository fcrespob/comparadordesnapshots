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
import es.mapfre.coaseguro.tirea.dominio.entidades.TotPmaCoaBtcoatf;
import es.mapfre.coaseguro.tirea.dominio.keys.TotPmaCoaBtcoatfKey;
import es.mapfre.coaseguro.tirea.excepcion.Solvencia2Excepcion;
import es.mapfre.coaseguro.tirea.utils.beanio.BeanIOReader;
import net.sf.ehcache.Element;

public class TotPmaCoaBtcoatfDao extends DaoBase{

private static final String CACHE_NAME = "TOTPMACOABTCOATF";
	
	private static final int BATCH_SIZE = 1000;

	public TotPmaCoaBtcoatfDao() {
		super();
		super.setCacheName(CACHE_NAME);
	}

	public Set<Entry<TotPmaCoaBtcoatfKey, TotPmaCoaBtcoatf>> entrySet() {
		return (Set<Entry<TotPmaCoaBtcoatfKey, TotPmaCoaBtcoatf>>) this.getCache().getAll(getCache().getKeys());
	}

	public TotPmaCoaBtcoatf get(Object key) {
		if (key instanceof TotPmaCoaBtcoatfKey) {
			return (TotPmaCoaBtcoatf) getCache().get(key).getObjectValue();
		}
		return null;
	}

	public List<TotPmaCoaBtcoatfKey> keySet() {
		return this.getCache().getKeys();
	}

	public TotPmaCoaBtcoatf put(TotPmaCoaBtcoatfKey key, TotPmaCoaBtcoatf value) {
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
		TotPmaCoaBtcoatf totPmaCoa = null;
		Set<Element> valores = new HashSet<Element>();
		int bloque = 0;
		while ((totPmaCoa = (TotPmaCoaBtcoatf) reader.read()) != null) {
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
	
	public List<TotPmaCoaBtcoatf> getValues() {

		Collection<Element> fr = this.values();
		List<TotPmaCoaBtcoatf> lista = new ArrayList<TotPmaCoaBtcoatf>();
		if(fr == null || fr.isEmpty()){
			es.mapfre.coaseguro.tirea.dominio.entidades.Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("10");
			inci.setInfAmpliada("No se han encontrado prestaciones reales en el fichero correspondiente.");
			throw new Solvencia2Excepcion(inci);
		}
		
		Iterator<Element> itFR= fr.iterator();
		while (itFR.hasNext()) {
			lista.add((TotPmaCoaBtcoatf) itFR.next().getObjectValue());
		}

		return lista;
	}
	
}
