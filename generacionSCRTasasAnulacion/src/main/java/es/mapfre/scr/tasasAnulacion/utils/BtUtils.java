package es.mapfre.scr.tasasAnulacion.utils;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.scr.tasasAnulacion.dominio.entidades.Incidencia;
import es.mapfre.scr.tasasAnulacion.excepcion.Solvencia2Excepcion;

public class BtUtils {

	private static Logger log = LoggerFactory.getLogger(BtUtils.class);
	
	private static String CARGA_FICHEROS_PROPERTIES;
	
	private Properties cargaFicherosProperties;
	
	public String getCargaFicherosProperty(String clave, String bt) throws Solvencia2Excepcion{
		if(cargaFicherosProperties == null){
			switch (bt){
				case ConstantesSolvencia.BT_SCRAEP:
					CARGA_FICHEROS_PROPERTIES = "cargaFicherosSCRAEP.properties";
					break;
				case ConstantesSolvencia.BT_SCRAEN:
					CARGA_FICHEROS_PROPERTIES = "cargaFicherosSCRAEN.properties";
					break;
				case ConstantesSolvencia.BT_SCRAIP:
					CARGA_FICHEROS_PROPERTIES = "cargaFicherosSCRAIP.properties";
					break;
				case ConstantesSolvencia.BT_SCRAIN:
					CARGA_FICHEROS_PROPERTIES = "cargaFicherosSCRAIN.properties";
					break;
			}
			
			cargaFicherosProperties = getProperties(CARGA_FICHEROS_PROPERTIES);
		}
		return cargaFicherosProperties.getProperty(clave);
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

