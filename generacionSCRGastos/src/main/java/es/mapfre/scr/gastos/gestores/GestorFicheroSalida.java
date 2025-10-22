package es.mapfre.scr.gastos.gestores;

import java.io.File;

import es.mapfre.scr.gastos.dominio.entidades.SalidaGRE;
import es.mapfre.scr.gastos.dominio.entidades.SalidaIPC;
import es.mapfre.scr.gastos.utils.BtUtils;
import es.mapfre.scr.gastos.utils.ConstantesSolvencia;
import es.mapfre.scr.gastos.utils.beanio.BeanIOReader;
import es.mapfre.scr.gastos.utils.beanio.BeanIOWriter;

public class GestorFicheroSalida {
	private static GestorFicheroSalida INSTANCE = null;
	
	private BtUtils btUtils = new BtUtils();
	// Constantes
	private String NOMBRE_FICH_AUX;
	private String NOMBRE_FICH_REAL; 
	private String catalogoSalida;
	
	//Atributos
	private BeanIOReader reader = null;
	private BeanIOWriter writer = null;
	
	private GestorFicheroSalida(String tipo){
			crearConectores(tipo);
	}
	
	private void crearConectores(String tipo){
		try {
			
			NOMBRE_FICH_AUX = btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_SALIDA_AUX, tipo);
			NOMBRE_FICH_REAL = btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_SALIDA, tipo);
			
			if (tipo.equals(ConstantesSolvencia.CTE_GTO_UMIC)){
				catalogoSalida = ConstantesSolvencia.CATALOGO_SALIDA_GRE;
			} else {
				catalogoSalida = ConstantesSolvencia.CATALOGO_SALIDA_IPC;
			}
			writer = new BeanIOWriter(ConstantesSolvencia.BEANIO_CONFIG_XML, NOMBRE_FICH_REAL, catalogoSalida, true);
			reader = new BeanIOReader(ConstantesSolvencia.BEANIO_CONFIG_XML, NOMBRE_FICH_REAL, catalogoSalida);
		} catch (Exception e) {}
	}
	
	private synchronized static void createInstance(String tipo){
		if( INSTANCE == null ){
			INSTANCE = new GestorFicheroSalida(tipo);
		}
	}
	
	public static GestorFicheroSalida getInstance(String tipo){
		if( INSTANCE == null ){
			createInstance(tipo);
		}
		return INSTANCE;
	}
	
	public void eliminarDatosAntiguos(String tipo){
		File ficheroSalida = new File(NOMBRE_FICH_REAL);
		File ficheroSalidaAux = new File(NOMBRE_FICH_AUX);
		cerrarConectores();
		ficheroSalida.delete();
		ficheroSalidaAux.renameTo(ficheroSalida);
		crearConectores(tipo);		
	}

	public void cerrarConectores() {
		try {
			reader.close();
			writer.close();
		} catch (Exception e) {}
	}
	
	public void write(SalidaGRE registroSalida){
		writer.write(registroSalida);
	}
	
	public void write(SalidaIPC registroSalida){
		writer.write(registroSalida);
	}

}
