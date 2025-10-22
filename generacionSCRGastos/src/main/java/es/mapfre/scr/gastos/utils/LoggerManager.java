package es.mapfre.scr.gastos.utils;

import java.io.File;
import java.io.IOException;

import es.mapfre.scr.gastos.dominio.entidades.FichaResultado;
import es.mapfre.scr.gastos.dominio.entidades.Incidencia;
import es.mapfre.scr.gastos.dominio.entidades.Incidencias;
import es.mapfre.scr.gastos.excepcion.Solvencia2Excepcion;
import es.mapfre.scr.gastos.gestores.GestorIncidencias;
import es.mapfre.scr.gastos.utils.beanio.BeanIOReader;
import es.mapfre.scr.gastos.utils.beanio.BeanIOWriter;

public class LoggerManager {
	private String rutaFicheroLog;
	private BeanIOWriter writer;
	private static LoggerManager logger = null;
	FichaResultado logElement = null;
	private String ERROR_LOG_FICHA = "Error en la lectura del log de la ficha";
	private String ERROR_GENERICO = "99";
	
	protected LoggerManager(String tipo){
		try {
			switch (tipo){
				case ConstantesSolvencia.CTE_GTO_UMIC:
					rutaFicheroLog = ConstantesSolvencia.RUTA_BASE + File.separator + ConstantesSolvencia.LOG_FICHASGRE;
					break;
				case ConstantesSolvencia.CTE_IPC:
					rutaFicheroLog = ConstantesSolvencia.RUTA_BASE + File.separator + ConstantesSolvencia.LOG_FICHASIPC;
					break;
			}
			
			// Se lee la ficha de proceso para recuperar los campos clave
			BeanIOReader readerFicha = new BeanIOReader(ConstantesSolvencia.BEANIO_CONFIG_XML, rutaFicheroLog, ConstantesSolvencia.CATALOGO_LOG);
			FichaResultado primeraLineaLog = (FichaResultado)readerFicha.read();
			// Se lee la ficha de proceso para recuperar los campos clave
			writer = new BeanIOWriter(ConstantesSolvencia.BEANIO_CONFIG_XML, rutaFicheroLog, ConstantesSolvencia.CATALOGO_LOG, true);
			logElement = new FichaResultado(primeraLineaLog.getKejecucion(), primeraLineaLog.getKsistema(), primeraLineaLog.getKprotecnico(), primeraLineaLog.getKuejecucion(), primeraLineaLog.getKsecuencia(), primeraLineaLog.getKtipores(), primeraLineaLog.getKsecres(), "", "", "", "", "", "", "", "", "", "");
			//logElement = new FichaResultado(2014110000, "SOLV", "SO10", "GT05", 100, "HIST", 000, "", "", "", "", "", "", "", "", "", "");
		} catch (IOException e) {
			GestorIncidencias gi = GestorIncidencias.getInstance(tipo);
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
	
	public static LoggerManager getInstance(String tipo){
		if(logger==null){
			logger = new LoggerManager(tipo);
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
			//e.printStackTrace();
			return;
		}
	}
}
