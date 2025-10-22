package es.mapfre.gbt.tablasExperiencia.dao;

import net.sf.ehcache.Cache;
import es.mapfre.gbt.tablasExperiencia.services.Servicio;
import es.mapfre.gbt.tablasExperiencia.utils.beanio.BeanIOReader;
import es.mapfre.gbt.tablasExperiencia.utils.beanio.BeanIOWriter;

/**
 * Interfaz de los DAO
 * 
 * @author Everis
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