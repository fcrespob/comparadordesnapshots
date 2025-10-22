package es.mapfre.gbt.tablasExperiencia.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.gbt.tablasExperiencia.dominio.entidades.Incidencia;
import es.mapfre.gbt.tablasExperiencia.excepcion.Solvencia2Excepcion;


public class BtUtils {

	private static final String FORMATO_SALIDA_FICHERO = "formato.salida.fichero";

	private static Logger log = LoggerFactory.getLogger(BtUtils.class);
	
	private static final String LITERALES_BT_PROPERTIES = "literalesBT.properties";
	private static final String LITERALES_BT = "literalesBT.";
	private static final String CARGA_FICHEROS_PROPERTIES = "cargaFicherosTExp.properties";
	private static final String SOLVENCIA_MOTOR_PROPERTIES = "solvenciaMotor.properties";
	
	
	private Properties literalesBTs;
	private Properties cargaFicherosProperties;
	private Properties solvenciaMotorProperties;
	
	public List<String> getBts(String cTipoBT) throws Solvencia2Excepcion{
		if(literalesBTs==null){
			literalesBTs = getProperties(LITERALES_BT_PROPERTIES);
		}
		
		String[] btsArray = literalesBTs.getProperty(LITERALES_BT+cTipoBT).split(",");	
		List<String> bts = new ArrayList<String>();
		
		for (int i = 0; i < btsArray.length; i++) {
			bts.add(btsArray[i].trim());
		}
		
		return bts;
		
	}
	
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
			log.error(e.getMessage(),e);
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("21");
			inci.setInfAmpliada("Error al leer el fichero de propiedades.");
			throw new Solvencia2Excepcion(inci);
		}
		
		return properties;
	}
	
	public String getFormatoSalidaFichero() throws Solvencia2Excepcion{
		if(solvenciaMotorProperties == null){
			solvenciaMotorProperties = getProperties(SOLVENCIA_MOTOR_PROPERTIES);
		}
		
		return solvenciaMotorProperties.getProperty(FORMATO_SALIDA_FICHERO);
	}
}

