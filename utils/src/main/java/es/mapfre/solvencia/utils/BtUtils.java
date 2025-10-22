package es.mapfre.solvencia.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BtUtils {

	private static final String FORMATO_SALIDA_FICHERO = "formato.salida.fichero";

	private static final String EXPORTAR_DETALLE_DISTRIBUIDO = "exportar.detalle.corriente.distribuido";

	private static Logger log = LoggerFactory.getLogger(BtUtils.class);
	
	private static final String LITERALES_BT_PROPERTIES = "literalesBT.properties";
	private static final String LITERALES_BT = "literalesBT.";
	private static final String CARGA_FICHEROS_PROPERTIES = "cargaFicheros.properties";
	private static final String SOLVENCIA_MOTOR_PROPERTIES = "solvenciaMotor.properties";
	
	
	private Properties literalesBTs;
	private Properties cargaFicherosProperties;
	private Properties solvenciaMotorProperties;
	
	public List<String> getBts(String cTipoBT){
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
	
	public String getCargaFicherosProperty(String clave){
		if(cargaFicherosProperties == null){
			cargaFicherosProperties = getProperties(CARGA_FICHEROS_PROPERTIES);
		}
		
		return cargaFicherosProperties.getProperty(clave);
	}
	
	private Properties getProperties(String fichero){
		
		Properties properties = null;
		
		try {
			properties = new Properties();
			properties.load(ClassLoader.getSystemResourceAsStream(fichero));
		} catch (IOException e) {
			log.error(e.getMessage(),e);
			
		}
		
		return properties;
	}
	
	public String getFormatoSalidaFichero(){
		if(solvenciaMotorProperties == null){
			solvenciaMotorProperties = getProperties(SOLVENCIA_MOTOR_PROPERTIES);
		}
		
		return solvenciaMotorProperties.getProperty(FORMATO_SALIDA_FICHERO);
	}
	
	public Boolean getExportarDetalleDistribuido() {
		if(solvenciaMotorProperties == null){
			solvenciaMotorProperties = getProperties(SOLVENCIA_MOTOR_PROPERTIES);
		}
		
		return Boolean.valueOf(solvenciaMotorProperties.getProperty(EXPORTAR_DETALLE_DISTRIBUIDO, "true"));
		
	}
}
