package es.mapfre.solvencia.dao;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.extractor.ChainedExtractor;
import com.tangosol.util.extractor.PofExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;
import es.mapfre.solvencia.utils.beanio.BeanIOWriter;

public abstract class DaoBase implements Dao {
	private String cacheName;

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
	public NamedCache getCache() {
		return CacheFactory.getCache(getCacheName());
	}

	/**
	 * @return el tamaño en elementos de la caché
	 */
	public int size() {
		return getCache().size();
	}

	/**
	 * @return si la caché está vacía
	 */
	public boolean isEmpty() {
		return getCache().isEmpty();
	}

	/**
	 * @param key
	 *            La clave a buscar
	 * @return Si la clave está presente en la caché
	 */
	public boolean containsKey(Object key) {
		return getCache().containsKey(key);
	}

	/**
	 * @param value
	 *            El valor a buscar
	 * @return Si el valor está presente en la caché
	 */
	public boolean containsValue(Object value) {
		return getCache().containsValue(value);
	}

	/**
	 * Limpia el contenido de la caché
	 */
	public void clear() {
		getCache().clear();
	}

	/**
	 * Carga los valores referenciados por las claves
	 * 
	 * @param keys
	 *            las claves a cargar
	 * @return los elementos encontrados
	 */
	public Map getAll(Set keys) {
		return getCache().getAll(keys);
	}

	/**
	 * Guarda los valores almacenados en el mapa referenciados por su clave.
	 * 
	 * @param values
	 *            Los registros a guardar, en forma de clave-valor
	 */
	public void putAll(Map entries) {
		getCache().putAll(entries);
	}

	/**
	 * Busca el set de claves de entradas a partir de un filtro
	 * 
	 * @param filter
	 * @return Las claves encontradas
	 */
	public Set keySet(Filter filter) {
		return getCache().keySet(filter);
	}

	/**
	 * Carga la caché empleando el lector de BeanIO pasado como parámetro. El
	 * lector debe estar correctamente inicializado.
	 * 
	 * @param reader
	 */
	public void loadCache(BeanIOReader reader) {
		EntidadBase valor = null;
		Map<Object, Object> valores = new HashMap<Object, Object>();
		int bloque = 0;
		while ((valor = (EntidadBase) reader.read()) != null) {
			valores.put(valor.getKey(), valor);
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
		for (Object value : getCache().values()) {
			writer.write(value);
		}
		writer.flush();
	}

	/**
	 * Método de utilidad para conocer si la caché es de tipo Replicated
	 * 
	 * @return true si Replicated, False en cualquier otro caso
	 */
	protected boolean isReplicated() {
		return this.getCache().getCacheService().getInfo().getServiceType().equals(ConstantesSolvencia.REPLICATED_CACHE);
	}

	/**
	 * Método de utilidad para diferenciar entre dos tipos de Extractor
	 * dependiendo del tipo de Caché
	 * 
	 * @param pofExtractor
	 * @param valueExtractor
	 * @return el Extractor adecuado
	 */
	protected ValueExtractor getCorrectExtractor(ValueExtractor pofExtractor, ValueExtractor valueExtractor) {
		return isReplicated() ? valueExtractor : pofExtractor;
	}

	/**
	 * Crea un extractor teniendo en cuenta si el tipo de caché es raplicated.
	 * 
	 * @param sMethod
	 *            Método a emplear en caso de extractor de tipo
	 *            ReflectionExtractor
	 * @param clz
	 *            Clase a emplear en caso de usar PofExtractor
	 * @param iProp
	 *            Propiedad a emplear en caso de usar PofExtractor
	 * @return ValueExtractor adecuado para la caché
	 */
	protected ValueExtractor createExtractor(String sMethod, Class clz, int iProp) {

		ValueExtractor aux = null;

		if (isReplicated()) {
			if (clz == Timestamp.class) {
				aux = new ChainedExtractor(new ReflectionExtractor(sMethod), new ReflectionExtractor("getTime"));
			} else {
				aux = new ReflectionExtractor(sMethod);
			}

		} else {

			if (clz == Timestamp.class) {
				aux = new ChainedExtractor(new PofExtractor(Timestamp.class, iProp), new ReflectionExtractor("getTime"));
			} else {
				aux = new PofExtractor(clz, iProp);
			}

		}

		return aux;
	}
}
