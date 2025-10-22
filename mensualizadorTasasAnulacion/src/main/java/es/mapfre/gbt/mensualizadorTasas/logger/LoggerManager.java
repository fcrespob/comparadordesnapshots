package es.mapfre.gbt.mensualizadorTasas.logger;

import java.io.IOException;

import es.mapfre.gbt.mensualizadorTasas.dominio.FichaResultado;
import es.mapfre.gbt.mensualizadorTasas.exception.GestorIncidencias;
import es.mapfre.gbt.mensualizadorTasas.exception.Incidencia;
import es.mapfre.gbt.mensualizadorTasas.exception.IncidenciaMens;
import es.mapfre.gbt.mensualizadorTasas.exception.Solvencia2Excepcion;
import es.mapfre.gbt.mensualizadorTasas.utils.BeanIOReader;
import es.mapfre.gbt.mensualizadorTasas.utils.BeanIOWriter;
import es.mapfre.gbt.mensualizadorTasas.utils.BtUtils;
import es.mapfre.gbt.mensualizadorTasas.utils.ConstantesMensualizador;

public class LoggerManager {
	BtUtils btUtils = new BtUtils();
	
	private String ERROR_LOG_FICHA = "Error en la lectura del log de la ficha";
	private String ERROR_GENERICO = "99";
	private String rutaFicheroLog = btUtils.getCargaFicherosProperty(ConstantesMensualizador.LOG_FICHAS);
	private BeanIOWriter writer;
	private static LoggerManager logger = null;
	FichaResultado logElement = null;
	
	protected LoggerManager(){
		try {
			// Se lee la ficha de proceso para recuperar los campos clave
			BeanIOReader readerFicha = new BeanIOReader(ConstantesMensualizador.BEANIO_CONFIG_OUT, rutaFicheroLog, ConstantesMensualizador.CACHE_R340T003);
			FichaResultado primeraLineaLog = (FichaResultado)readerFicha.read();
			// Se lee la ficha de proceso para recuperar los campos clave
			writer = new BeanIOWriter(ConstantesMensualizador.BEANIO_CONFIG_OUT, rutaFicheroLog, ConstantesMensualizador.CACHE_R340T003, true);
			logElement = new FichaResultado(primeraLineaLog.getKejecucion(), primeraLineaLog.getKsistema(), primeraLineaLog.getKprotecnico(), primeraLineaLog.getKuejecucion(), primeraLineaLog.getKsecuencia(), primeraLineaLog.getKtipores(), primeraLineaLog.getKsecres(), "", "", "", "", "", "", "", "", "", "");
			//logElement = new FichaResultado(2014110000, "SOLV", "SO10", "GT05", 100, "HIST", 000, "", "", "", "", "", "", "", "", "", "");
		} catch (IOException e) {
			GestorIncidencias gi = GestorIncidencias.getInstance();
			IncidenciaMens incidencia = new IncidenciaMens("","",ERROR_LOG_FICHA);
			gi.write(incidencia);
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
