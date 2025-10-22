package es.mapfre.scr.tablasExperiencia.gestores;

import java.io.File;

import es.mapfre.scr.tablasExperiencia.dominio.entidades.Salida;
import es.mapfre.scr.tablasExperiencia.utils.BtUtils;
import es.mapfre.scr.tablasExperiencia.utils.ConstantesSolvencia;
import es.mapfre.scr.tablasExperiencia.utils.beanio.BeanIOReader;
import es.mapfre.scr.tablasExperiencia.utils.beanio.BeanIOWriter;

public class GestorFicheroSalida {
	private static GestorFicheroSalida INSTANCE = null;
	
	private BtUtils btUtils = new BtUtils();
	// Constantes
	private String NOMBRE_FICH_AUX;
	private String NOMBRE_FICH_REAL;
	
	//Atributos
	private BeanIOReader reader = null;
	private BeanIOWriter writer = null;
	
	private GestorFicheroSalida(String bt){
			crearConectores(bt);
	}
	
	private void crearConectores(String bt){
		try {
			NOMBRE_FICH_AUX = btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_SALIDA_AUX, bt);
			NOMBRE_FICH_REAL = btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_SALIDA, bt);
				
			writer = new BeanIOWriter(ConstantesSolvencia.BEANIO_CONFIG_XML, NOMBRE_FICH_REAL, ConstantesSolvencia.CATALOGO_SALIDA, true);
			reader = new BeanIOReader(ConstantesSolvencia.BEANIO_CONFIG_XML, NOMBRE_FICH_REAL, ConstantesSolvencia.CATALOGO_SALIDA);
		} catch (Exception e) {}
	}
	
	private synchronized static void createInstance(String bt){
		if( INSTANCE == null ){
			INSTANCE = new GestorFicheroSalida(bt);
		}
	}
	
	public static GestorFicheroSalida getInstance(String bt){
		if( INSTANCE == null ){
			createInstance(bt);
		}
		return INSTANCE;
	}
	
	public void eliminarDatosAntiguos(String bt){
		
		// Solo se podrán eliminar datos si existe el fichero de entrada
		if(reader!=null){
			File ficheroSalida = new File(NOMBRE_FICH_REAL);
			File ficheroSalidaAux = new File(NOMBRE_FICH_AUX);
			cerrarConectores();
			ficheroSalida.delete();
			ficheroSalidaAux.renameTo(ficheroSalida);
			crearConectores(bt);
		}
	}

	public void cerrarConectores() {
		try {
			reader.close();
			writer.close();
		} catch (Exception e) {}
	}
	
	public void write(Salida registroSalida){
		writer.write(registroSalida);
	}

}
