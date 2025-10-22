package es.mapfre.solvencia.open;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.salidaCalculo.FichaResultadoKey;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.salidaCalculo.FichaResultado;
import es.mapfre.solvencia.open.comun.Constantes;
import es.mapfre.solvencia.open.comun.SolvenciaUtils;
import es.mapfre.solvencia.open.config.ConfigInstancias;
import es.mapfre.solvencia.open.extraer.SolvenciaUtilsExtraerBBDD;
import es.mapfre.solvencia.utils.BtUtils;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;
import es.mapfre.solvencia.utils.beanio.BeanIOWriter;

/**
 * <pre>
 * Esta aplicación realiza varias tareas:
 * 
 * - Verifica la existencia de los ficheros implicados en una ejecución:
 * 	· Fichas de Proceso
 *  · Cartera Técnica
 *  · Catálogo
 *  
 * - Abre el fichero de Fichas de Proceso y comprueba la estructura correcta
 * - Calcula el número de instancias de Amazon necesarias para procesar las fichas
 * - ...
 * 
 * </pre>
 * 
 * @author Indra
 * 
 */
public final class SolvenciaOpen {

	private static Logger log = LoggerFactory.getLogger(SolvenciaOpen.class);

	private static String fecCierre = null;
	private static String rutaCierre = null;
	private static String rutaCierreCatalogo = null;

	private static BtUtils btUtils = new BtUtils();

	private static Map<FichaResultadoKey, List<FichaResultado>> fichasResultado = null;
	private static List<FichaProceso> listaFichasProceso  = null;

	private static BeanIOWriter out = null;

	private static Boolean hayError = Boolean.FALSE;

	public SolvenciaOpen() {

	}

	public static void main(String[] args) {
		SolvenciaOpen app = new SolvenciaOpen();

		app.procesoCalculoYCompresion();
	}

	public int procesoCalculoYCompresion() {
		int numInstancias = 0;
		
		try {
			this.configurar();
			// leer las ficha de resultados
			fichasResultado = SolvenciaUtils.cargarFichasResultadoLog();
			// leer las ficha de proceso
			listaFichasProceso =  SolvenciaUtilsExtraerBBDD.cargarFichasProcesoLog();
			SolvenciaUtils.validarExistenciaFicheros(listaFichasProceso);

			numInstancias = this.dimensionar();

			int nodosPorInstancia = Integer.valueOf(ConfigInstancias.getInstance().getProperty(
					Constantes.PROPERTY_NODOS_POR_INSTANCIA));
			this.comprimirFicheros(numInstancias * nodosPorInstancia);

		} catch (Exception e) {
			log.error("Error ejecutando proceso de dimensionamiento", e);
			hayError = Boolean.TRUE;
		} finally {
			SolvenciaUtils.generarFichasResultadoLog(out, rutaCierre, fichasResultado);
		}

		log.info("Finalizado proceso verificación y dimensionado");
		if (hayError) {
			System.exit(1);
		}
		
		return numInstancias;
	}


	private void configurar() throws Exception {
		fecCierre = SolvenciaUtils.getFechaCierre();

		rutaCierre = SolvenciaUtils.getRutaCierre(true);
		rutaCierreCatalogo = SolvenciaUtils.getRutaCierre(false);

		out = new BeanIOWriter(Constantes.BEANIO_CONFIG_OUT_XML, null, null);
	}

	/**
	 * Método que, a partir de las fichas de proceso y de la configuración de
	 * escenarios de carga, calcula el número de instancias Amazon a levantar
	 * 
	 * @return el número de instancias a levantar
	 * @throws IOException
	 */
	private int dimensionar() throws IOException {
		// Comprobamos las fichas de proceso
		String filePath = rutaCierre + SolvenciaUtils.getRutaRelativaFichero(Constantes.STREAM_FICHAS);

		log.info("Cargando fichas de proceso...");
		List<FichaProceso> fichas = new ArrayList<FichaProceso>();
		List<String> canalesNegocio = new ArrayList<String>();
		BeanIOReader in = new BeanIOReader(Constantes.BEANIO_CONFIG_XML, null, null);
		try {
			in.createReader(filePath, Constantes.STREAM_FICHAS);

			FichaProceso fichaProceso = null;
			DateFormat dfAAAAMM = new SimpleDateFormat("yyyyMM");
			DateFormat dfHHmmssDDMMAAAA = new SimpleDateFormat("HH:mm:ss dd/MM/YYYY");
			Date hoy = new Date();
			while ((fichaProceso = (FichaProceso) in.read()) != null) {
				String fecEfecto = dfAAAAMM.format(fichaProceso.getFefecto());
				String canalNegocio = fichaProceso.getCcanal().toString() + fichaProceso.getCnegocio();
				if (fecEfecto.equals(fecCierre)) {
					if (!canalesNegocio.contains(canalNegocio)) {
						SolvenciaUtils.escribeLog(
								SolvenciaUtils.creaRegistroFichaResultado(fichaProceso, "Leída Ficha de Proceso "
										+ fichaProceso.getKejecucion() + " a las " + dfHHmmssDDMMAAAA.format(hoy)),
								fichasResultado);

						fichas.add(fichaProceso);
						canalesNegocio.add(canalNegocio);
					} else {
						// TODO: Gestionar esta excepción
						throw new RuntimeException("Hay dos fichas distintas con el mismo canal ("
								+ fichaProceso.getCcanal().toString() + ") y negocio (" + fichaProceso.getCnegocio()
								+ ").");
					}
				} else {
					// TODO: Gestionar esta excepción
					throw new RuntimeException("La fecha de efecto " + fecEfecto + " de la ficha de proceso "
							+ fichaProceso.getKejecucion() + " no coincide con la fecha de cierre " + fecCierre + ".");
				}
			}
		} catch (Exception e) {
			log.error("Error cargando las fichas de proceso", e);
			System.exit(-1);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
			}
		}

		if (fichas != null && fichas.size() > 0) {
			log.info("Se han cargado {} fichas de proceso.", fichas.size());
		} else {
			throw new RuntimeException("No se han encontrado fichas de proceso correctas.");
		}

		// Calculamos el dimensionamiento
		Double numInstancias = 0.0;
		List<Double> escenarios = new ArrayList<Double>();
		for (FichaProceso ficha : fichas) {
			String canalNegocio = ficha.getCcanal().toString() + ficha.getCnegocio();
			// Obtener el tipo de escenario
			String escenario = ConfigInstancias.getInstance().getProperty(canalNegocio);

			// A partir del tipo de escenario, obtener el valor asignado
			Double instanciasFichaProceso = 1.0;

			if (escenario != null) {
				instanciasFichaProceso = Double.valueOf(ConfigInstancias.getInstance().getProperty(escenario));
			} else {
				log.warn("No existe escenario configurado para el canal {} y negocio {}", ficha.getCcanal(),
						ficha.getCnegocio());
			}
			escenarios.add(instanciasFichaProceso);

			SolvenciaUtils.escribeLog(
					SolvenciaUtils.creaRegistroFichaResultado(ficha, "Instancias necesarias para la ficha: "
							+ (int) Math.ceil(instanciasFichaProceso)), fichasResultado);
		}
		Collections.sort(escenarios, Collections.reverseOrder());
		int numEscenario = 0;
		for (Double valorEscenario : escenarios) {
			numEscenario++;
			numInstancias += valorEscenario / numEscenario;
		}

		numInstancias = Math.min(numInstancias, Integer.valueOf(ConfigInstancias.getInstance().getProperty(
				Constantes.PROPERTY_MAXIMO_NUMERO_INSTANCIAS)));

		// Escribimos el resultado en consola
		int numeroInstanciasFinal = (int) Math.ceil(numInstancias);
		System.out.println("Instancias:" + numeroInstanciasFinal);

		for (FichaProceso ficha : fichas) {
			SolvenciaUtils.escribeLog(
					SolvenciaUtils.creaRegistroFichaResultado(ficha, "Instancias totales levantadas: "
							+ numeroInstanciasFinal), fichasResultado);
		}

		return numeroInstanciasFinal;
	}

	private void comprimirFicheros(int numInstancias) {
		// TODO: Sacar a properties el tamaño mínimo de fichero (en líneas)
		List<Future> tareasCompresion = new ArrayList<Future>();
		try {
			long minSize = Constantes.MIN_SIZE_FICHERO_COMPRIMIR;
			String formatoFinal = Constantes.FORMATO_FICHERO_GZIP;
	
			String[] ficherosAuxiliares = btUtils.getCargaFicherosProperty(Constantes.LISTA_FICHEROS_AUXILIARES).split(",");
	
			
			for (String fichero : ficherosAuxiliares) {
				String filePath = rutaCierre + SolvenciaUtils.getRutaRelativaFichero(fichero);
				SolvenciaUtils.borrarFicherosComprimidosAntiguos(filePath);
				tareasCompresion.add(SolvenciaUtils.partirFichero(filePath, formatoFinal, numInstancias, minSize));
			}
	
			String[] ficherosCatalogo = btUtils.getCargaFicherosProperty(Constantes.LISTA_FICHEROS_CATALOGOS).split(",");
	
			for (String fichero : ficherosCatalogo) {
				String filePath = rutaCierreCatalogo + SolvenciaUtils.getRutaRelativaFichero(fichero);
				SolvenciaUtils.borrarFicherosComprimidosAntiguos(filePath);
				tareasCompresion.add(SolvenciaUtils.partirFichero(filePath, formatoFinal, numInstancias, minSize));
			}
	
			String filePath = rutaCierre + SolvenciaUtils.getRutaRelativaFichero(Constantes.STREAM_UMIC);
			SolvenciaUtils.borrarFicherosComprimidosAntiguos(filePath);
			tareasCompresion.add(SolvenciaUtils.partirFichero(filePath, formatoFinal, numInstancias, minSize));
			
			Boolean terminado = Boolean.FALSE;
			while(!terminado) {
				terminado = Boolean.TRUE;
				for (Future tarea : tareasCompresion) {
					terminado &= tarea.isDone();
				}
			}

		} finally {
			SolvenciaUtils.shutdown();
		}

		log.info("Terminado de comprimir {} ficheros.", tareasCompresion.size());
	}
}
