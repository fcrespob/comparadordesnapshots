package es.mapfre.scr.tablasExperiencia.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import es.mapfre.scr.tablasExperiencia.dao.Dao;
import es.mapfre.scr.tablasExperiencia.dao.services.FactoriaDao;
import es.mapfre.scr.tablasExperiencia.dominio.EntidadBase;
import es.mapfre.scr.tablasExperiencia.utils.beanio.BeanIOReader;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class CargaDatos {
	
	private BtUtils btUtils = new BtUtils();
	private String cacheName;
	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config-scrTExp.xml";
	
	public void cargaDatosGeneral(String clave,String fecCierre, String rutaBase, String bt)
		throws FileNotFoundException, IOException,
		ClassNotFoundException, NoSuchMethodException,
		InstantiationException, IllegalAccessException,
		InvocationTargetException {
	
		String filePath;
		String streamName;
		String[] daos = btUtils.getCargaFicherosProperty(clave, bt).split(",");
		for (int i = 0; i < daos.length; i++) {
			streamName = daos[i];
			filePath = btUtils.getCargaFicherosProperty(streamName, bt);
			BeanIOReader reader = new BeanIOReader(BEANIO_CONFIG_XML,rutaBase + File.separator + filePath, streamName);
			Dao dao = FactoriaDao.getDao(streamName);
			dao.loadCache(reader);
			
			reader.close();
		}
	}
	
	public void loadCache(BeanIOReader reader){
		EntidadBase valor = null;
		Set<Element> valores = new HashSet<Element>();
		int bloque = 0;
		while ((valor = (EntidadBase) reader.read()) != null) {
			valores.add(new Element(valor.getKey(), valor));
			bloque++;
			if (bloque % ConstantesSolvencia.BATCH_SIZE == 0) {
				this.putAll(valores);
				valores.clear();
			}
		}
		if (valores.size() > 0) {
			this.putAll(valores);
			valores.clear();
		}
	}
	
	public void putAll(Collection<Element> entries) {
		getCache().putAll(entries);
	}
	
	public String getCacheName() {
		return this.cacheName;
	}
	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

	/**
	 * @return la caché de Coherence referenciada por el nombre
	 */
	public Cache getCache() {
		return CacheManager.getInstance().getCache(getCacheName());
	}
}
