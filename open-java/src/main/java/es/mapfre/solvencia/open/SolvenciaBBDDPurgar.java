/**
 * 
 */
package es.mapfre.solvencia.open;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.salidaCalculo.FichaResultadoKey;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.salidaCalculo.FichaResultado;
import es.mapfre.solvencia.open.cargar.SolvenciaUtilsCargaBBDD;
import es.mapfre.solvencia.open.comun.Constantes;
import es.mapfre.solvencia.open.comun.SolvenciaUtils;
import es.mapfre.solvencia.open.extraer.SolvenciaUtilsExtraerBBDD;
import es.mapfre.solvencia.utils.beanio.BeanIOWriter;

/**
 * La aplicación Java lo que hará será leer la ficha de proceso y en función de
 * qué tipo de ejecución sea (cierre, reproceso, etc.) llamará a unos proc.
 * almacenados de Oracle u otros
 * 
 * @author amdepedro
 * 
 */
public final class SolvenciaBBDDPurgar {

	private static Logger logger = LoggerFactory.getLogger(SolvenciaBBDDPurgar.class);
	
	private static Map<FichaResultadoKey, List<FichaResultado>> fichasResultado = null;
	
	private static List<FichaProceso> listaFichasProceso  = null;
	
	private static String fechaCierre = null;

	private static String rutaCierre = null;
	
	private static BeanIOWriter out = null;
	
	private static Boolean hayError = Boolean.FALSE;
	
	private SolvenciaBBDDPurgar() {
		
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			SolvenciaBBDDPurgar app = new SolvenciaBBDDPurgar();
			app.configurar();
			//// leer las ficha de resultados
			fichasResultado = SolvenciaUtils.cargarFichasResultadoLogPurgado();
			// leer las ficha de proceso
			listaFichasProceso =  SolvenciaUtilsExtraerBBDD.cargarFichasProcesoLogPurgado();
			// Verificamos si nos invocan para registrar un error
			if (!app.registrarError()) {
				logger.info("Inicio purgado BBDD");			
				//procesamiento de ficheros a cargar
				SolvenciaUtilsCargaBBDD.procesarPurgadoFicheros(listaFichasProceso, fichasResultado);			
				logger.info("Fin purgado BBDD");
			}
		} catch (Exception e) {
			logger.error("Error en la ejecución: ", e);
			hayError = Boolean.TRUE;
		} finally {
			SolvenciaUtils.generarFichasResultadoLogPurgado(out, rutaCierre, fichasResultado);			
		}
		logger.info("Finalizado proceso purgado.");
		if (hayError) {
			System.exit(1);
		}
	}
	
	private void configurar() throws IOException {  
		fechaCierre = SolvenciaUtils.getFechaCierre();
		rutaCierre = SolvenciaUtils.getRutaCierre(true);
		out = new BeanIOWriter(Constantes.BEANIO_CONFIG_OUT_XML, null, null);
	}
	
	private boolean registrarError() {
		boolean hayError = false;
		String codigoError = System.getProperty(Constantes.PARAM_CODIGO_ERROR);
		
		if (codigoError != null && codigoError.length() > 0) {
			hayError = true;
			logger.warn("SolvenciaBBDD invocado con parámetro de error. Código {}", codigoError);
			// Marcamos como erróneas todas las fichas de proceso
			for (FichaProceso fichaProceso : listaFichasProceso) {
				SolvenciaUtils.escribeLog(
					SolvenciaUtils.creaRegistroFichaResultado(fichaProceso, "Error técnico: "
							+ codigoError), fichasResultado);
			}
		}
		
		return hayError;
	}
	
	
}
