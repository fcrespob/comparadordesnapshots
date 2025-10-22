package es.mapfre.scr.gastos.utils;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.scr.gastos.dominio.entidades.Incidencia;
import es.mapfre.scr.gastos.excepcion.Solvencia2Excepcion;

public class BtUtils {

	private static Logger log = LoggerFactory.getLogger(BtUtils.class);
	
	private static String CARGA_FICHEROS_PROPERTIES;
	
	private Properties cargaFicherosProperties;
	
	public String getCargaFicherosProperty(String clave, String tipo) throws Solvencia2Excepcion{
		if(cargaFicherosProperties == null){
			switch (tipo) {
			case ConstantesSolvencia.CTE_GTO_UMIC:
				CARGA_FICHEROS_PROPERTIES = "cargaFicherosSCRGRE.properties";
				break;
			case ConstantesSolvencia.CTE_IPC:
				CARGA_FICHEROS_PROPERTIES = "cargaFicherosSCRIPC.properties";
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

