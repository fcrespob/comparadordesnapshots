package es.mapfre.coaseguro.tirea.gestores;

import java.io.File;
import java.io.FileWriter;

import es.mapfre.coaseguro.tirea.dominio.entidades.Incidencia;
import es.mapfre.coaseguro.tirea.dominio.entidades.Incidencias;
import es.mapfre.coaseguro.tirea.excepcion.Solvencia2Excepcion;
import es.mapfre.coaseguro.tirea.utils.BtUtils;
import es.mapfre.coaseguro.tirea.utils.ConstantesSolvencia;
import es.mapfre.coaseguro.tirea.utils.beanio.BeanIOWriter;

public class GestorIncidenciasGen {
	
	private static GestorIncidenciasGen INSTANCE = null;
	private static int NUM_INCI;
	
	private BtUtils btUtils = new BtUtils();
	// Constantes
	private String NOMBRE_FICH_INCIDENCIAS;
	
	//Atributos
	private BeanIOWriter writer = null;
	
	private GestorIncidenciasGen(){
		try {
						
			NOMBRE_FICH_INCIDENCIAS = "/mnt/solv2vida/" +  "CIERRES" + File.separator + "FICHATIREA" + File.separator + ConstantesSolvencia.RUTA_FICHAS + btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_INC);
			FileWriter fwErr = new FileWriter(NOMBRE_FICH_INCIDENCIAS);
			fwErr.close();
			
			//writer = new BeanIOWriter(ConstantesSolvencia.BEANIO_CONFIG_XML, NOMBRE_FICH_INCIDENCIAS, ConstantesSolvencia.STREAM_INCIDENCIAS);
			NUM_INCI = 0;
		} catch (Exception e) {
			Incidencia incidencia = new Incidencia();
			incidencia.setCodigoRetorno("10");
			incidencia.setInfAmpliada("Se ha producido un error al cargar el gestor de incidencias. - Error: " + e.getMessage());
			Solvencia2Excepcion solv = new Solvencia2Excepcion(incidencia);
			throw solv;
		}
	}
	
	private synchronized static void createInstance(){
		if( INSTANCE == null ){
			INSTANCE = new GestorIncidenciasGen();
		}
	}
	
	public static GestorIncidenciasGen getInstance(){
		if( INSTANCE == null ){
			createInstance();
		}
		return INSTANCE;
	}

	public void write(Solvencia2Excepcion e) {
		NUM_INCI++;
		Incidencias incidencia = new Incidencias();
		incidencia.setCodigoerror(e.getIncidencia().getCodigoRetorno());
		incidencia.setDescerror(e.getIncidencia().getInfAmpliada());
		writer.write(incidencia);
		writer.flush();
	}
	
	public void write(Incidencia inci) {
		NUM_INCI++;
		Incidencias incidencia = new Incidencias();
		incidencia.setCanal(inci.getCcanal());
		if (null == inci.getFecCierre()){
			incidencia.setFeccierre("00000000");
		} else {
			incidencia.setFeccierre(inci.getFecCierre());
		}
		incidencia.setCodigoerror(inci.getCodigoRetorno());
		incidencia.setDescerror(inci.getInfAmpliada());
		writer.write(incidencia);
		writer.flush();
	}
	
	public void write(Incidencias inci) {
		NUM_INCI++;
		writer.write(inci);
		writer.flush();
	}
	
	public int getNumInci(){
		return NUM_INCI;
	}

	public void cerrarConector() {
		try {
			writer.close();
		} catch (Exception e) {}
	}
	
}
