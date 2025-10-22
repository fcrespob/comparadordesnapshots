package es.mapfre.gbt.mensualizadorTasas.exception;

import java.io.FileWriter;
import java.io.PrintWriter;

import es.mapfre.gbt.mensualizadorTasas.utils.BeanIOWriter;
import es.mapfre.gbt.mensualizadorTasas.utils.BtUtils;
import es.mapfre.gbt.mensualizadorTasas.utils.ConstantesMensualizador;

public class GestorIncidencias {
	
	private static GestorIncidencias INSTANCE = null;
	private static int NUM_INCI;
	
	private BtUtils btUtils = new BtUtils();
	// Constantes
	private String NOMBRE_FICH_INCIDENCIAS = btUtils.getCargaFicherosProperty(ConstantesMensualizador.CACHE_INC0);
	
	//Atributos
	private BeanIOWriter writer = null;
	
	private GestorIncidencias(){
		try {
			FileWriter fwErr = new FileWriter(NOMBRE_FICH_INCIDENCIAS);
			PrintWriter pwErr = new PrintWriter(fwErr);
			pwErr.println(camposTabString());
			fwErr.close();
			
			writer = new BeanIOWriter(ConstantesMensualizador.BEANIO_CONFIG_OUT, NOMBRE_FICH_INCIDENCIAS, "INC0",true);
		} catch (Exception e) {}
	}
	
	private synchronized static void createInstance(){
		if( INSTANCE == null ){
			INSTANCE = new GestorIncidencias();
			NUM_INCI = 0;
		}
	}
	
	public static GestorIncidencias getInstance(){
		if( INSTANCE == null ){
			createInstance();
		}
		return INSTANCE;
	}
	
	public void write(IncidenciaMens inci) {
		NUM_INCI++;
		writer.write(inci);
		writer.flush();
	}

	public void cerrarConector() {
		try {
			writer.close();
		} catch (Exception e) {}
	}
	
	public int getNumIncidencias(){
		return NUM_INCI;
	}
	
	private String camposTabString(){
		String separator = "\t";
		return "Tabla" + separator +
				"Fecha" + separator +
				"Descripción";
	}
	
}
