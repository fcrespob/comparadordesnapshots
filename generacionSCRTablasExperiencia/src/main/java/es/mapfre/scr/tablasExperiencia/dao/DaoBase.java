package es.mapfre.scr.tablasExperiencia.dao;

import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.mapfre.scr.tablasExperiencia.dominio.EntidadBase;
import es.mapfre.scr.tablasExperiencia.utils.ConstantesSolvencia;
import es.mapfre.scr.tablasExperiencia.utils.beanio.BeanIOReader;
import es.mapfre.scr.tablasExperiencia.utils.beanio.BeanIOWriter;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public abstract class DaoBase implements Dao {
	private CacheManager singletonManager = null;
	private String cacheName;

	public DaoBase(){
		super();
		if(singletonManager==null){
			InputStream config = ClassLoader.getSystemResourceAsStream("ehcacheConfig/ehcacheSCRTExp.xml");
			singletonManager = CacheManager.create(config);
		}
	}
	/**
	 * @return el nombre de la caché
	 */
	public String getCacheName() {
		return this.cacheName;
	}

	public String getNombreServicio() {
		return this.cacheName;
	}

	/**
	 * Asigna el nombre de la caché a emplear
	 * 
	 * @param cacheName
	 */
	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

	/**
	 * @return la caché de Coherence referenciada por el nombre
	 */
	public Cache getCache() {
		//return CacheManager.getInstance().getCache(getCacheName());
		return singletonManager.getCache(getCacheName());
	}

	/**
	 * @return el tamaÃ±o en elementos de la caché
	 */
	public int size() {
		return getCache().getSize();
	}

	/**
	 * @param key La clave a buscar
	 * @return Si la clave esté presente en la caché
	 */
	public boolean containsKey(Object key) {
		return getCache().isKeyInCache(key);
	}

	/**
	 * @param value
	 *            El valor a buscar
	 * @return Si el valor est presente en la caché
	 */
	public boolean containsValue(Object value) {
		return getCache().isValueInCache(value);
	}

	/**
	 * Limpia el contenido de la caché
	 */
	public void clear() {
		CacheManager.getInstance().clearAllStartingWith(getCacheName());
	}

	/**
	 * Carga los valores referenciados por las claves
	 * 
	 * @param keys
	 *            las claves a cargar
	 * @return los elementos encontrados
	 */
	public Map<Object, Element> getAll() {
		return getCache().getAll(getCache().getKeys());
	}

	/**
	 * Guarda los valores almacenados en el mapa referenciados por su clave.
	 * 
	 * @param values
	 *            Los registros a guardar, en forma de clave-valor
	 */
	public void putAll(Set<Element> entries) {
		Cache cache = getCache();
		cache.putAll(entries);
	}

	

	/**
	 * Carga la caché empleando el lector de BeanIO pasado como parámetro. El
	 * lector debe estar correctamente inicializado.
	 * 
	 * @param reader
	 */
	public void loadCache(BeanIOReader reader) {
		EntidadBase<Object> valor = null;
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

	/**
	 * Exporta el contenido de la caché a fichero, empleando el writer pasado
	 * como parámetro. El writer ha de estar correctamente inicializado.
	 * 
	 * @param writer
	 */
	public void exportCache(BeanIOWriter writer) {
		List keys = getCache().getKeys();
		
		for (Object value : keys) {
			writer.write(getCache().get(value).getObjectValue());
		}
		writer.flush();
	}

}
