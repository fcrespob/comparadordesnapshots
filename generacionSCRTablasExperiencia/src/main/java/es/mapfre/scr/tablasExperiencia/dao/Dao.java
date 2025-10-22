package es.mapfre.scr.tablasExperiencia.dao;


import es.mapfre.scr.tablasExperiencia.services.Servicio;
import es.mapfre.scr.tablasExperiencia.utils.beanio.BeanIOReader;
import es.mapfre.scr.tablasExperiencia.utils.beanio.BeanIOWriter;
import net.sf.ehcache.Cache;

/**
 * Interfaz de los DAO
 * 
 */
public interface Dao extends Servicio {

	/**
	 * @return el nombre de la caché
	 */
	String getCacheName();

	/**
	 * Devuelve la caché asociada al dao
	 * 
	 * @return la caché asociada al dao
	 */
	Cache getCache();

	/**
	 * Carga la caché empleando el lector de BeanIO pasado como parÃ¡metro. El
	 * lector debe estar correctamente inicializado.
	 * 
	 * @param reader
	 */
	void loadCache(BeanIOReader reader);

	/**
	 * Exporta el contenido de la caché a fichero, empleando el writer pasado
	 * como parÃ¡metro. El writer ha de estar correctamente inicializado.
	 * 
	 * @param writer
	 */
	void exportCache(BeanIOWriter writer);

	/**
	 * VacÃ­a el contenido de la caché
	 */
	void clear();
}