package es.mapfre.solvencia.cargaFicherosGBT;

import java.util.Properties;

import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;


public class BtUtilsGBT {
	
	private static final String CARGA_FICHEROS_PROPERTIES = "cargaFicherosGBT.properties";
	
	private Properties cargaFicherosProperties;
	
	public String getCargaFicherosProperty(String clave) throws Solvencia2Excepcion{
		if(cargaFicherosProperties == null){
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
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("21");
			inci.setInfAmpliada("Error al leer el fichero de propiedades.");
			throw new Solvencia2Excepcion(inci);
		}
		
		return properties;
	}
}

