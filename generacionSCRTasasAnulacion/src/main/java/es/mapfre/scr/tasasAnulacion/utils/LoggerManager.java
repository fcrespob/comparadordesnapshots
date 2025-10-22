package es.mapfre.scr.tasasAnulacion.utils;

import java.io.File;
import java.io.IOException;

import es.mapfre.scr.tasasAnulacion.dominio.entidades.FichaResultado;
import es.mapfre.scr.tasasAnulacion.dominio.entidades.Incidencia;
import es.mapfre.scr.tasasAnulacion.dominio.entidades.Incidencias;
import es.mapfre.scr.tasasAnulacion.excepcion.Solvencia2Excepcion;
import es.mapfre.scr.tasasAnulacion.gestores.GestorIncidencias;
import es.mapfre.scr.tasasAnulacion.utils.beanio.BeanIOReader;
import es.mapfre.scr.tasasAnulacion.utils.beanio.BeanIOWriter;

public class LoggerManager {
	private String rutaFicheroLog;
	private BeanIOWriter writer;
	private static LoggerManager logger = null;
	FichaResultado logElement = null;
	private String ERROR_LOG_FICHA = "Error en la lectura del log de la ficha";
	private String ERROR_GENERICO = "99";
	
	protected LoggerManager(String bt){
		try {
			switch (bt){
				case ConstantesSolvencia.BT_SCRAEP:
					rutaFicheroLog = ConstantesSolvencia.RUTA_BASE + File.separator + ConstantesSolvencia.LOG_FICHASAEP;
					break;
				case ConstantesSolvencia.BT_SCRAEN:
					rutaFicheroLog = ConstantesSolvencia.RUTA_BASE + File.separator + ConstantesSolvencia.LOG_FICHASAEN;
					break;
				case ConstantesSolvencia.BT_SCRAIP:
					rutaFicheroLog = ConstantesSolvencia.RUTA_BASE + File.separator + ConstantesSolvencia.LOG_FICHASAIP;
					break;
				case ConstantesSolvencia.BT_SCRAIN:
					rutaFicheroLog = ConstantesSolvencia.RUTA_BASE + File.separator + ConstantesSolvencia.LOG_FICHASAIN;
					break;
			}
			// Se lee la ficha de proceso para recuperar los campos clave
			BeanIOReader readerFicha = new BeanIOReader(ConstantesSolvencia.BEANIO_CONFIG_XML, rutaFicheroLog, ConstantesSolvencia.CATALOGO_LOG);
			FichaResultado primeraLineaLog = (FichaResultado)readerFicha.read();
			// Se lee la ficha de proceso para recuperar los campos clave
			writer = new BeanIOWriter(ConstantesSolvencia.BEANIO_CONFIG_XML, rutaFicheroLog, ConstantesSolvencia.CATALOGO_LOG, true);
			logElement = new FichaResultado(primeraLineaLog.getKejecucion(), primeraLineaLog.getKsistema(), primeraLineaLog.getKprotecnico(), primeraLineaLog.getKuejecucion(), primeraLineaLog.getKsecuencia(), primeraLineaLog.getKtipores(), primeraLineaLog.getKsecres(), "", "", "", "", "", "", "", "", "", "");
		} catch (IOException e) {
			GestorIncidencias gi = GestorIncidencias.getInstance(bt);
			Incidencias incidencia = new Incidencias();
			incidencia.setCodigoerror(ERROR_GENERICO);
			incidencia.setDescerror(ERROR_LOG_FICHA);
			gi.write(incidencia);
			Incidencia inci = new Incidencia();
			inci.setInfAmpliada(ERROR_LOG_FICHA);
			inci.setCodigoRetorno(ERROR_GENERICO);
			Solvencia2Excepcion exc = new Solvencia2Excepcion(inci);
			throw exc;
		}
	}
	
	public static LoggerManager getInstance(String bt){
		if(logger==null){
			logger = new LoggerManager(bt);
		}
		
		return logger;
	}
	
	public void writeLog(String descripcion){
		logElement.setGc1Resul(descripcion);
		writer.write(logElement);
	}
	
	public void cerrarWriter(){
		try {
			writer.close();
		} catch (IOException e) {
			return;
		}
	}
}
