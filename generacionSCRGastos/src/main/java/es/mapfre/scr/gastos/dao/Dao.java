package es.mapfre.scr.gastos.dao;

import net.sf.ehcache.Cache;
import es.mapfre.scr.gastos.services.Servicio;
import es.mapfre.scr.gastos.utils.beanio.BeanIOReader;
import es.mapfre.scr.gastos.utils.beanio.BeanIOWriter;

/**
 * Interfaz de los DAO
 * 
 */
public interface Dao extends Servicio {

	/**
	 * @return el nombre de la cach�
	 */
	String getCacheName();

	/**
	 * Devuelve la cach� asociada al dao
	 * 
	 * @return la cach� asociada al dao
	 */
	Cache getCache();

	/**
	 * Carga la cach� empleando el lector de BeanIO pasado como parámetro. El
	 * lector debe estar correctamente inicializado.
	 * 
	 * @param reader
	 */
	void loadCache(BeanIOReader reader);

	/**
	 * Exporta el contenido de la cach� a fichero, empleando el writer pasado
	 * como parámetro. El writer ha de estar correctamente inicializado.
	 * 
	 * @param writer
	 */
	void exportCache(BeanIOWriter writer);

	/**
	 * Vacía el contenido de la cach�
	 */
	void clear();
}