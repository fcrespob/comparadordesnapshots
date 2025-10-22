package es.mapfre.coaseguro.tirea.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.mapfre.coaseguro.tirea.dominio.entidades.FichaResultado;
import es.mapfre.coaseguro.tirea.dominio.entidades.Incidencia;
import es.mapfre.coaseguro.tirea.excepcion.Solvencia2Excepcion;
import es.mapfre.coaseguro.tirea.utils.beanio.BeanIOWriter;

public class LoggerManager {
	private String rutaFicheroLog, rutaFicheroLogResumen;
	private BeanIOWriter writer, writerResumen;
	private static LoggerManager logger = null;
	private String ERROR_LOG_FICHA = "Error en la creación del log.";
	private String ERROR_GENERICO = "10";
	
	protected LoggerManager(){
		try {
			rutaFicheroLog = ConstantesSolvencia.RUTA_BASE + File.separator + ConstantesSolvencia.LOG_FICHAS;
			rutaFicheroLogResumen = ConstantesSolvencia.RUTA_BASE + File.separator + ConstantesSolvencia.LOG_FICHAS_RESU;
			writer = new BeanIOWriter(ConstantesSolvencia.BEANIO_CONFIG_XML, rutaFicheroLog, ConstantesSolvencia.CATALOGO_LOG);
			writerResumen = new BeanIOWriter(ConstantesSolvencia.BEANIO_CONFIG_XML, rutaFicheroLogResumen, ConstantesSolvencia.CATALOGO_LOG);
		} catch (IOException e) {
			Incidencia inci = new Incidencia();
			inci.setInfAmpliada(ERROR_LOG_FICHA);
			inci.setCodigoRetorno(ERROR_GENERICO);
			Solvencia2Excepcion exc = new Solvencia2Excepcion(inci);
			throw exc;
		}
	}
	
	public static LoggerManager getInstance(){
		if(logger==null){
			logger = new LoggerManager();
		}
		
		return logger;
	}
	
	public void writeLog(String descripcion, String tipo, boolean resumen){
		FichaResultado logElement = new FichaResultado();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		logElement.setKfecha(sdf.format(new Date()));
		logElement.setKtipoinfo(tipo);
		logElement.setGc1Resul(descripcion);
		writer.write(logElement);
		if (resumen) {
			writerResumen.write(logElement);
		}
	}
	
	public void cerrarWriter(){
		try {
			writer.close();
			writerResumen.close();
		} catch (IOException e) {
			return;
		}
	}
}
