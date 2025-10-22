package es.mapfre.coaseguro.tirea.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class FactoriaServicios<T extends Servicio> {

	private static Logger log = LoggerFactory
			.getLogger(FactoriaServicios.class);

	/**
	 * Constructor de la factoría
	 * 
	 * @param clazz
	 *            La clase para la que se está implementando la factoría
	 */
	protected FactoriaServicios(final Class<T> clazz) {
		init(clazz);
	}

	private void init(final Class<T> clazz) {
		// Pide a la factoría a implementar su mapa de servicios
		Map<String, T> servicios = getServicios();
		if (servicios == null) {
			servicios = new HashMap<String, T>();
			// Asignamos a la factor�a el nuevo mapa de servicios
			setServicios(servicios);
		}
		Map<String, List<String>> serviciosDuplicados = new HashMap<String, List<String>>();

		// Le pedimos al service loader que nos cargue las implementaciones
		ServiceLoader<T> servicioLoader = null;
		servicioLoader = ServiceLoader.load(clazz);

		if (servicioLoader != null) {
			Iterator<? extends T> servicioIterator = servicioLoader.iterator();
			try {
				while (servicioIterator.hasNext()) {
					try {
						T servicio = servicioIterator.next();
						// Le pedimos al servicio qu� Servicio implementa
						String nombreServicio = servicio.getNombreServicio();
						// Si ya lo ten�amos es un servicio con implementaci�n
						// duplicada
						if (servicios.containsKey(nombreServicio)) {
							List<String> clasesDuplicadas = serviciosDuplicados
									.get(nombreServicio);
							if (clasesDuplicadas == null) {
								clasesDuplicadas = new ArrayList<String>();
								clasesDuplicadas.add(servicios
										.get(nombreServicio).getClass()
										.getName());
							}
							clasesDuplicadas.add(servicio.getClass().getName());
							serviciosDuplicados.put(nombreServicio,
									clasesDuplicadas);
						} else {
							// Si no estaba anteriormente, la registramos
							servicios.put(nombreServicio, servicio);
						}
					} catch (ServiceConfigurationError scError) {
						log.warn(
								"Error cargando las implementaciones de servicios: {}",
								scError.getMessage());
					}
				}
			} catch (ServiceConfigurationError scError) {
				log.warn(
						"Error cargando las implementaciones de servicios: {}",
						scError.getMessage());
			}
		} else {
			log.warn("No hay implementaciones de servicio en el entorno.");
		}

		// Mostramos las implementaciones de servicios duplicados
		for (Map.Entry<String, List<String>> servicioConflicto : serviciosDuplicados
				.entrySet()) {
			StringBuilder mensaje = new StringBuilder("El servicio ")
					.append(servicioConflicto.getKey())
					.append(" tiene varias implementaciones, se usar� la primera: ")
					.append(System.getProperty("line.separator"));
			for (String claseServicio : servicioConflicto.getValue()) {
				mensaje.append(" - ").append(claseServicio)
						.append(System.getProperty("line.separator"));
			}
			log.warn(mensaje.toString());
		}
	}

	/**
	 * Devuelve un Servicio en función del nombre de servicio solicitado
	 * 
	 * @param nombreServicio
	 * @return el servicio buscado
	 */
	protected T getServicio(String nombreServicio) {
		return getServicio(nombreServicio, Boolean.FALSE);
	}

	/**
	 * @param nombreServicio
	 *            El nombre del servicio buscado
	 * @param newInstance
	 *            true: se ha de crear una nueva instancia cada vez que se
	 *            devuelve un servicio; false: no se ha de crear
	 * @return el servicio buscado
	 */
	protected T getServicio(String nombreServicio, Boolean newInstance) {
		getServicios();
		T servicio = getServicios().get(nombreServicio);
		if (newInstance && servicio != null) {
			try {
				getServicios().put(nombreServicio,
						(T) servicio.getClass().newInstance());
			} catch (InstantiationException e) {
				log.warn(
						"No se ha podido crear una nueva instancia de la clase {}",
						servicio.getClass());
			} catch (IllegalAccessException e) {
				log.warn(
						"No se ha podido crear una nueva instancia de la clase {}",
						servicio.getClass());
			}
		}
		return servicio;
	}

	/**
	 * M�todo para acceder a los servicios registrados por una implementaci�n de
	 * la factor�a
	 * 
	 * @return Los servicios registrados
	 */
	protected abstract Map<String, T> getServicios();

	/**
	 * Guarda en la implementaci�n de la factor�a los servicios a registrar.
	 * 
	 * @param servicios
	 *            Los servicios a registrar
	 */
	protected abstract void setServicios(Map<String, T> servicios);
}
