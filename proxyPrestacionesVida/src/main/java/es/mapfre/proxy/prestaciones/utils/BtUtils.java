package es.mapfre.proxy.prestaciones.utils;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.proxy.prestaciones.dominio.entidades.Incidencia;
import es.mapfre.proxy.prestaciones.excepcion.Solvencia2Excepcion;

public class BtUtils {

	private static Logger log = LoggerFactory.getLogger(BtUtils.class);
	
	private static String CARGA_FICHEROS_PROPERTIES;
	private static String RUTA_FICHEROS_PROPERTIES;
	
	private Properties cargaFicherosProperties;
	private Properties rutaFicherosProperties;
	
	public String getCargaFicherosProperty(String clave) throws Solvencia2Excepcion{
		if(cargaFicherosProperties == null){
			CARGA_FICHEROS_PROPERTIES = "cargaFicherosProxy.properties";
			cargaFicherosProperties = getProperties(CARGA_FICHEROS_PROPERTIES);
		}
		return cargaFicherosProperties.getProperty(clave);
	}
	
	public String getRutaFicherosProperty(String clave) throws Solvencia2Excepcion{
		if(rutaFicherosProperties == null){
			RUTA_FICHEROS_PROPERTIES = "rutaficherosProxy.properties";
			rutaFicherosProperties = getProperties(RUTA_FICHEROS_PROPERTIES);
		}
		return rutaFicherosProperties.getProperty(clave);
	}
	
	private Properties getProperties(String fichero) throws Solvencia2Excepcion{
		
		Properties properties = null;
		
		try {
			properties = new Properties();
			properties.load(ClassLoader.getSystemResourceAsStream(fichero));
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("21");
			inci.setInfAmpliada("Error al leer el fichero de propiedades.");
			throw new Solvencia2Excepcion(inci);
		}
		
		return properties;
	}
}

