package es.mapfre.scr.tablasExperiencia.utils;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.scr.tablasExperiencia.dominio.entidades.Incidencia;
import es.mapfre.scr.tablasExperiencia.excepcion.Solvencia2Excepcion;

public class BtUtils {

	private static Logger log = LoggerFactory.getLogger(BtUtils.class);
	
	private static String CARGA_FICHEROS_PROPERTIES;
	
	private Properties cargaFicherosProperties;
	
	public String getCargaFicherosProperty(String clave, String bt) throws Solvencia2Excepcion{
		if(cargaFicherosProperties == null){
			switch (bt){
				case ConstantesSolvencia.BT_SCRMFE:
					CARGA_FICHEROS_PROPERTIES = "cargaFicherosSCRMFE.properties";
					break;
				case ConstantesSolvencia.BT_SCRMMI:
					CARGA_FICHEROS_PROPERTIES = "cargaFicherosSCRMMI.properties";
					break;
				case ConstantesSolvencia.BT_SCRMCF:
					CARGA_FICHEROS_PROPERTIES = "cargaFicherosSCRMCF.properties";
					break;
				case ConstantesSolvencia.BT_SCRMCI:
					CARGA_FICHEROS_PROPERTIES = "cargaFicherosSCRMCI.properties";
					break;
				case ConstantesSolvencia.BT_SCRLFE:
					CARGA_FICHEROS_PROPERTIES = "cargaFicherosSCRLFE.properties";
					break;
				case ConstantesSolvencia.BT_SCRLMI:
					CARGA_FICHEROS_PROPERTIES = "cargaFicherosSCRLMI.properties";
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

