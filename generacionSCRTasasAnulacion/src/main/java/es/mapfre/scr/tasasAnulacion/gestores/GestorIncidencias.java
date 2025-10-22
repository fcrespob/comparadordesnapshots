package es.mapfre.scr.tasasAnulacion.gestores;

import java.io.FileWriter;
import java.text.SimpleDateFormat;

import es.mapfre.scr.tasasAnulacion.dominio.entidades.Incidencia;
import es.mapfre.scr.tasasAnulacion.dominio.entidades.Incidencias;
import es.mapfre.scr.tasasAnulacion.excepcion.Solvencia2Excepcion;
import es.mapfre.scr.tasasAnulacion.utils.BtUtils;
import es.mapfre.scr.tasasAnulacion.utils.ConstantesSolvencia;
import es.mapfre.scr.tasasAnulacion.utils.beanio.BeanIOWriter;

public class GestorIncidencias {
	
	private static GestorIncidencias INSTANCE = null;
	private static int NUM_INCI;
	
	private BtUtils btUtils = new BtUtils();
	// Constantes
	private String NOMBRE_FICH_INCIDENCIAS;
	
	//Atributos
	private BeanIOWriter writer = null;
	
	private GestorIncidencias(String bt){
		try {
			NOMBRE_FICH_INCIDENCIAS = btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_INCIDENCIAS, bt);
			FileWriter fwErr = new FileWriter(NOMBRE_FICH_INCIDENCIAS);
			fwErr.close();
			
			writer = new BeanIOWriter(ConstantesSolvencia.BEANIO_CONFIG_XML, NOMBRE_FICH_INCIDENCIAS, ConstantesSolvencia.CATALOGO_INCIDENCIAS,true);
			NUM_INCI = 0;
		} catch (Exception e) {}
	}
	
	private synchronized static void createInstance(String bt){
		if( INSTANCE == null ){
			INSTANCE = new GestorIncidencias(bt);
		}
	}
	
	public static GestorIncidencias getInstance(String bt){
		if( INSTANCE == null ){
			createInstance(bt);
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
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		Incidencias incidencia = new Incidencias();
		incidencia.setBt(inci.getBt());
		if (null != inci.getFecCierre()){
			incidencia.setFeccierre(sf.format(inci.getFecCierre()));
		} else {
			incidencia.setFeccierre("00000000");
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
