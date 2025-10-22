package es.mapfre.scr.gastos.gestores;

import java.io.FileWriter;
import java.text.SimpleDateFormat;

import es.mapfre.scr.gastos.dominio.entidades.Incidencia;
import es.mapfre.scr.gastos.dominio.entidades.Incidencias;
import es.mapfre.scr.gastos.excepcion.Solvencia2Excepcion;
import es.mapfre.scr.gastos.utils.BtUtils;
import es.mapfre.scr.gastos.utils.ConstantesSolvencia;
import es.mapfre.scr.gastos.utils.beanio.BeanIOWriter;

public class GestorIncidencias {
	
	private static GestorIncidencias INSTANCE = null;
	private static int NUM_INCI;
	
	private BtUtils btUtils = new BtUtils();
	// Constantes
	private String NOMBRE_FICH_INCIDENCIAS;
	
	//Atributos
	private BeanIOWriter writer = null;
	
	private GestorIncidencias(String tipo){
		try {
			NOMBRE_FICH_INCIDENCIAS = btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_INCIDENCIAS, tipo);
			
			FileWriter fwErr = new FileWriter(NOMBRE_FICH_INCIDENCIAS);
			fwErr.close();
			
			writer = new BeanIOWriter(ConstantesSolvencia.BEANIO_CONFIG_XML, NOMBRE_FICH_INCIDENCIAS, ConstantesSolvencia.CATALOGO_INCIDENCIAS,true);
			NUM_INCI = 0;
		} catch (Exception e) {}
	}
	
	private synchronized static void createInstance(String tipo){
		if( INSTANCE == null ){
			INSTANCE = new GestorIncidencias(tipo);
		}
	}
	
	public static GestorIncidencias getInstance(String tipo){
		if( INSTANCE == null ){
			createInstance(tipo);
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
		if (null == inci.getFecCierre()){
			incidencia.setFeccierre("00000000");
		} else {
			incidencia.setFeccierre(sf.format(inci.getFecCierre()));
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
