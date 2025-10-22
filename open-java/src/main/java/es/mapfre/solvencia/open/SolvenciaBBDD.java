/**
 * 
 */
package es.mapfre.solvencia.open;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.salidaCalculo.FichaResultadoKey;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.ErrorSolvencia2;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.salidaCalculo.FichaResultado;
import es.mapfre.solvencia.open.cargar.SolvenciaUtilsCargaBBDD;
import es.mapfre.solvencia.open.comun.Constantes;
import es.mapfre.solvencia.open.comun.SolvenciaUtils;
import es.mapfre.solvencia.open.extraer.SolvenciaUtilsExtraerBBDD;
import es.mapfre.solvencia.utils.BtUtils;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;
import es.mapfre.solvencia.utils.beanio.BeanIOWriter;

/**
 * La aplicación Java lo que hará será leer la ficha de proceso y en función de
 * qué tipo de ejecución sea (cierre, reproceso, etc.) llamará a unos proc.
 * almacenados de Oracle u otros
 * 
 * @author amdepedro
 * 
 */
public final class SolvenciaBBDD {

	private static Logger logger = LoggerFactory.getLogger(SolvenciaBBDD.class);
	
	private static Map<FichaResultadoKey, List<FichaResultado>> fichasResultado = null;
	
	private static List<FichaProceso> listaFichasProceso  = null;
	
	private static String rutaCierre = null;
	
	private static BeanIOWriter out = null;
	
	private BtUtils btUtils = new BtUtils();
	
	private static Boolean hayError = Boolean.FALSE;
	
	private SolvenciaBBDD() {
		
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			SolvenciaBBDD app = new SolvenciaBBDD();
			app.configurar();
			//// leer las ficha de resultados
			fichasResultado = SolvenciaUtils.cargarFichasResultadoLog();
			// leer las ficha de proceso
			listaFichasProceso =  SolvenciaUtilsExtraerBBDD.cargarFichasProcesoLog();
			// Verificamos si nos invocan para registrar un error
			if (!app.registrarError()) {
				SolvenciaUtils.validarExistenciaFicheros(listaFichasProceso);
				logger.info("Inicio carga ficheros");			
				//procesamiento de ficheros a cargar
				SolvenciaUtilsCargaBBDD.procesarFicherosCargar(listaFichasProceso, fichasResultado);			
				logger.info("Fin carga ficheros");
				logger.info("Inicio extracción de ficheros");			
				SolvenciaUtilsExtraerBBDD.procesarFicherosExtraer(listaFichasProceso, fichasResultado);			
				SolvenciaUtilsExtraerBBDD.recuperarBoteDeIncidencias(listaFichasProceso, fichasResultado);			
				logger.info("Fin extracción de ficheros");
			}
			
			hayError = SolvenciaUtilsCargaBBDD.getHayError() || SolvenciaUtilsExtraerBBDD.getHayError();
		} catch (Exception e) {
			logger.error("Error en la ejecución: ", e);
			hayError = Boolean.TRUE;
		} finally {
			SolvenciaUtils.generarFichasResultadoLog(out, rutaCierre, fichasResultado);
			SolvenciaUtilsCargaBBDD.shutdown();
		}
		logger.info("Finalizado proceso carga y extracción.");
		if (hayError) {
			System.exit(1);
		}
	}
	
	private void configurar() throws IOException {  
		rutaCierre = SolvenciaUtils.getRutaCierre(true);
		out = new BeanIOWriter(Constantes.BEANIO_CONFIG_OUT_XML, null, null);
	}
	
	private boolean registrarError() {
		boolean hayError = false;
		String codigoError = System.getProperty(Constantes.PARAM_CODIGO_ERROR);
		
		if (codigoError != null && codigoError.length() > 0) {
			hayError = true;
			String textoError = null;
			// Cargamos el código de error
			String filePath;
			String streamName;
			String[] daos = btUtils.getCargaFicherosProperty(ConstantesSolvencia.MENSAJES_ERRORES).split(",");

			try {
				BeanIOReader reader = new BeanIOReader(Constantes.BEANIO_CONFIG_XML, null, null);

				if (daos != null && daos.length > 0) {
					// Cargamos el fichero genérico
					URL recursoFicheroErrores = ClassLoader.getSystemResource("CTLG_TB880ERR0.TXT");
					if (recursoFicheroErrores != null) {
						filePath = "CTLG_TB880ERR0.TXT";// recursoFicheroErrores.getFile();
						streamName = daos[0];
						reader.createReader(filePath, streamName, StandardCharsets.UTF_8.name());

						// TODO Buscamos el código de error
						ErrorSolvencia2 error = null;
						while ((error = (ErrorSolvencia2) reader.read()) != null) {
							if (codigoError.equals(error.getKretorno())) {
								textoError = error.getGdesc();
								break;
							}
						}
					}
				}
			} catch (IOException e) {
				logger.error("Error cargando fichero de descripción de errores: {}", e.getMessage());
			}

			
			logger.warn("SolvenciaBBDD invocado con parámetro de error. Código {}: {}", codigoError, textoError);
			// Marcamos como erróneas todas las fichas de proceso
			for (FichaProceso fichaProceso : listaFichasProceso) {
				SolvenciaUtils.escribeLog(
					SolvenciaUtils.creaRegistroFichaResultado(fichaProceso, "Error técnico: "
							+ codigoError + "-" + textoError), fichasResultado);
			}
		}
		
		return hayError;
	}
	
	
}
