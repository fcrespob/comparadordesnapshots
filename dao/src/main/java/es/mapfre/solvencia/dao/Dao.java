package es.mapfre.solvencia.dao;

import com.tangosol.net.NamedCache;

import es.mapfre.solvencia.services.Servicio;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;
import es.mapfre.solvencia.utils.beanio.BeanIOWriter;

/**
 * Interfaz de los DAO
 * 
 * @author Indra
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
	NamedCache getCache();

	/**
	 * Carga la caché empleando el lector de BeanIO pasado como parámetro. El
	 * lector debe estar correctamente inicializado.
	 * 
	 * @param reader
	 */
	void loadCache(BeanIOReader reader);

	/**
	 * Exporta el contenido de la caché a fichero, empleando el writer pasado
	 * como parámetro. El writer ha de estar correctamente inicializado.
	 * 
	 * @param writer
	 */
	void exportCache(BeanIOWriter writer);

	/**
	 * Vacía el contenido de la caché
	 */
	void clear();
}