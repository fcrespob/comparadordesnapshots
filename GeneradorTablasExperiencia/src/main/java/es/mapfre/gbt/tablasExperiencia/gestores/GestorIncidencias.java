package es.mapfre.gbt.tablasExperiencia.gestores;

import java.io.FileWriter;
import java.io.PrintWriter;

import es.mapfre.gbt.tablasExperiencia.dominio.entidades.Incidencia;
import es.mapfre.gbt.tablasExperiencia.dominio.entidades.Incidencias;
import es.mapfre.gbt.tablasExperiencia.excepcion.Solvencia2Excepcion;
import es.mapfre.gbt.tablasExperiencia.utils.BtUtils;
import es.mapfre.gbt.tablasExperiencia.utils.ConstantesSolvencia;
import es.mapfre.gbt.tablasExperiencia.utils.beanio.BeanIOWriter;

public class GestorIncidencias {
	
	private static GestorIncidencias INSTANCE = null;
	private static int NUM_INCI;
	
	private BtUtils btUtils = new BtUtils();
	// Constantes
	private String NOMBRE_FICH_INCIDENCIAS = btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_INCIDENCIAS);
	
	//Atributos
	private BeanIOWriter writer = null;
	
	private GestorIncidencias(){
		try {
			FileWriter fwErr = new FileWriter(NOMBRE_FICH_INCIDENCIAS);
			PrintWriter pwErr = new PrintWriter(fwErr);
			pwErr.println(camposTabString());
			fwErr.close();
			
			writer = new BeanIOWriter(ConstantesSolvencia.BEANIO_CONFIG_XML, NOMBRE_FICH_INCIDENCIAS, ConstantesSolvencia.CATALOGO_INCIDENCIAS,true);
			NUM_INCI = 0;
		} catch (Exception e) {}
	}
	
	private synchronized static void createInstance(){
		if( INSTANCE == null ){
			INSTANCE = new GestorIncidencias();
		}
	}
	
	public static GestorIncidencias getInstance(){
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
	
	private String camposTabString(){
		String separator = "\t";
		return "Base técnica" + separator +
				"Compañía" + separator +
				"Negocio" + separator +
				"Riesgo" + separator +
				"Sexo" + separator +
				"Categoría" + separator +
				"Edad desde" + separator +
				"Edad hasta" + separator +
				"Modalidad" + separator +
				"Fecha desde" + separator +
				"Fecha hasta" + separator +
				"Tabla base" + separator +
				"Código error" + separator +
				"Descripción";
	}
	
}
