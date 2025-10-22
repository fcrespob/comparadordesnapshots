package es.mapfre;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;


public class TestModulos {
	private static final Logger LOG = LoggerFactory.getLogger(TestModulos.class);
	
	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config.xml";
	// Cambiado por CAV porque se están cargando decenas de miles de registros en cada método del test, provocando que Jenkins se quede sin recursos.
	// Los tests han de emplear pocos registros y conocidos.
	// Se debería cargar una única vez el fichero en todo el TestCase
	private static final String DATA_FILE_NAME = "src/test/resources/Maestro_Host_Pruebas.txt";
	private static Umic[] arrUmics = null;
	private static FichaProceso fichaProceso = null;
	private static DetalleBaseTecnica detalleBT = null;
	private static List<DetalleCorriente>[] arrDetCorriente = null;
	
	/**
	 * Inicialización para todas las pruebas. Para que se ejecute, la clase no debe extender de TestCase.
	 * A diferencia de @Before, que se ejecuta una vez justo antes de cada //@Test,
	 * @BeforeClass se ejecuta una sola vez para todo el archivo de pruebas.
	 */
	
	@AfterClass
	public static void end() {
		TestModulos.LOG.debug("Fin pruebas");
	}

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void init() {
		TestModulos.LOG.debug("Inicio pruebas");
		try {
			// Carga de fichero con la umic
			final List<Umic> lstUmics = UtilTestProcesos.obtenerUMICFichero(DATA_FILE_NAME, BEANIO_CONFIG_XML);
			
			final Umic dejar1Umic = lstUmics.get(0);
			lstUmics.clear();
			lstUmics.add(dejar1Umic);
			
			// Carga de la ficha proceso
			fichaProceso = UtilTestProcesos.crearFichaProceso();
			// Carga del detalle base tecnica
			detalleBT = crearDetalleBaseTecnica();
			
			arrUmics = new Umic[lstUmics.size()];
			arrDetCorriente = new List[lstUmics.size()];
			for (int i = 0; i < lstUmics.size(); i++) {
				// Cargamos la umic en el array
				final Umic umic = lstUmics.get(i);
				setDatosUmic(umic);
				arrUmics[i] = umic;
				
				//Cargamos los DetalleCorriente en el array para la umic
				final List<DetalleCorriente> lstDetCorriente = UtilTestProcesos.obtenerPeriodos(umic, fichaProceso, detalleBT, ConstantsModulos.CTE_PROY_VIDA);
				arrDetCorriente[i] = lstDetCorriente;
				
				//establecemos la fecha de pago y devengo
				UtilTestProcesos.obtenerFechaPagoDevengo(umic, fichaProceso, detalleBT, lstDetCorriente, ConstantsModulos.CTE_PROY_VIDA);
			}
			
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModATC() {
		//Variables locales
		Modulo modATC = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modATC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_ATC);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modATC.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModVZC() {
		//Variables locales
		Modulo modVZC = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modVZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modVZC.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables, ConstantsModulos.CTE_PROY_VIDA);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModVZREVER() {
		//Variables locales
		Modulo modVZREVER = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modVZREVER = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZREVER);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modVZREVER.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables, ConstantsModulos.CTE_PROY_VIDA);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModCSP003() {
		//Variables locales
		Modulo modCSP003 = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modCSP003 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP003);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modCSP003.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModCSP001() {
		//Variables locales
		Modulo modCSP001 = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modCSP001 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP001);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modCSP001.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModCSP238() {
		//Variables locales
		Modulo modCSP238 = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modCSP238 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP238);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modCSP238.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModCSP071() {
		//Variables locales
		Modulo modCSP071 = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modCSP071 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP071);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modCSP071.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModACT001() {
		//Variables locales
		Modulo modACT001 = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modACT001 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_ACT001);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modACT001.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables, ConstantsModulos.CTE_PROY_VIDA);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModACTBEL() {
		//Variables locales
		Modulo modACTBEL = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modACTBEL = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_ACTBEL);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modACTBEL.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModHastaBtiNominal() {
		//Variables locales
		Modulo modBtiNom = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modBtiNom = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_HAN_BTI);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modBtiNom.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModCSP064() {
		//Variables locales
		Modulo modCSP064 = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modCSP064 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP064);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modCSP064.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModCSP005() {
		//Variables locales
		Modulo modCSP005 = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modCSP005 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP005);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modCSP005.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModCSP034() {
		//Variables locales
		Modulo modCSP034 = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modCSP034 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP034);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modCSP034.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModCSP051() {
		//Variables locales
		Modulo modCSP051 = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modCSP051 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP051);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modCSP051.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModCSP037() {
		//Variables locales
		Modulo modCSP037 = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modCSP037 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP037);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modCSP037.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModCSP362() {
		//Variables locales
		Modulo modCSP362 = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modCSP362 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP362);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modCSP362.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModFPTOZC() {
		//Variables locales
		Modulo modFPTOZC = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modFPTOZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FPTOZC);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modFPTOZC.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModPRI003() {
		//Variables locales
		Modulo modPri003 = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modPri003 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_PRI003);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modPri003.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModPRI007() {
		//Variables locales
		Modulo modPri007 = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modPri007 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_PRI007);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modPri007.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	/*
	//@Test
	public void testModGZC002() {
		//Variables locales
		Modulo modGZC002 = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modGZC002 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_GZC002);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					bloqueCorriente.setImpFlujoNominal(BigDecimal.ONE);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modGZC002.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}*/
	
	//@Test
	public void testModGZC010() {
		//Variables locales
		Modulo modGZC010 = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modGZC010 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_GZC010);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modGZC010.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModGZC001() {
		//Variables locales
		Modulo modGZC001 = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modGZC001 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_GZC001);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modGZC001.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables, ConstantsModulos.CTE_PROY_VIDA);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModGZC008() {
		//Variables locales
		Modulo modGZC008 = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modGZC008 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_GZC008);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modGZC008.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables, ConstantsModulos.CTE_PROY_VIDA);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModGZC004() {
		//Variables locales
		Modulo modGZC004 = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modGZC004 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_GZC004);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modGZC004.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModGZCRTA() {
		//Variables locales
		Modulo modGZCRTA = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modGZCRTA = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_GZCRTA);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modGZCRTA.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables, ConstantsModulos.CTE_PROY_VIDA);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModGZC005() {
		//Variables locales
		Modulo modGZC005 = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modGZC005 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_GZC005);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modGZC005.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModACT002() {
		//Variables locales
		Modulo modACT002 = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modACT002 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_ACT002);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modACT002.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModVZRTAJUB() {
		//Variables locales
		Modulo modVZRTAJUB = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modVZRTAJUB = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZRTAJUB);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modVZRTAJUB.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModVZCIERTA() {
		//Variables locales
		Modulo modVZRTAJUB = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modVZRTAJUB = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZCIERTA);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modVZRTAJUB.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModCOM001() {
		//Variables locales
		Modulo modCOM001 = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modCOM001 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_COM001);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modCOM001.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModRT002() {
		//Variables locales
		Modulo modRT002 = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modRT002 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_RT002);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modRT002.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModRT008() {
		//Variables locales
		Modulo modRT008 = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modRT008 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_RT008);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modRT008.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables, ConstantsModulos.CTE_PROY_VIDA);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}	
	
	//@Test
	public void testModRT009() {
		//Variables locales
		Modulo modRT009 = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modRT009 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_RT009);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modRT009.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables, ConstantsModulos.CTE_PROY_VIDA);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModRT022() {
		//Variables locales
		Modulo modRT022 = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modRT022 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_RT022);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modRT022.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModVBX001() {
		//Variables locales
		Modulo modVBX001 = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modVBX001 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VBX001);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modVBX001.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables, ConstantsModulos.CTE_PROY_VIDA, "VTX005");
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModVBX009() {
		//Variables locales
		Modulo modVBX009 = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modVBX009 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VBX009);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modVBX009.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables, ConstantsModulos.CTE_PROY_VIDA, "VTX009");
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	////@Test
	public void testModVBX362() {
		//Variables locales
		Modulo modVBX362 = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modVBX362 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VBX362);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modVBX362.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables, ConstantsModulos.CTE_PROY_VIDA, "VTX00X");
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	////@Test
	public void testModRT362() {
		//Variables locales
		Modulo modRT362 = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modRT362 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_RT362);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modRT362.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables, ConstantsModulos.CTE_PROY_VIDA);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModAPTOTC() {
		//Variables locales
		Modulo modAPTOCT = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				//Paso 4 llamada a modulo y que se ejecute sin fallos
				modAPTOCT = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_APTOTC);
				
				int iteracion = 1;
				for (DetalleCorriente detalleActual: lstDetCorriente) {
					
					final BloqueCorriente bloqueCorriente = detalleActual.getBloqueBySubproceso(ConstantsModulos.CTE_PROY_VIDA);
					setDatosBloqueCorriente(bloqueCorriente);
					
					modAPTOCT.execute(lstDetCorriente, bloqueCorriente, iteracion, fichaProceso.getFcalc(), umic, detalleBT, mapVariables);
					iteracion++;
				}
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	//@Test
	public void testModProvisionMatematica() {
		//Variables locales
		Modulo modProvMat = null;
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		final Map<String, Object> mapVariables = new HashMap<String, Object>();
		//Fin variables locales
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				mapVariables.clear();
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				
				modProvMat = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_PROV_MAT);
				modProvMat.execute(lstDetCorriente, fichaProceso.getFcalc(), umic, detalleBT, mapVariables);
			}
		} catch (Exception e) {
			TestModulos.LOG.debug(e.getMessage(),e);
			fail();
		}
	}
	
	private static DetalleBaseTecnica crearDetalleBaseTecnica() {
		final DetalleBaseTecnica baseTecnica = new DetalleBaseTecnica();
		baseTecnica.setBaseTec(ConstantsModulos.CTE_BTI);
		baseTecnica.setTablacalc1aseg1("740");
		baseTecnica.setTablacalc1aseg2("740");
		baseTecnica.setGtorosspPrima(BigDecimal.ONE);
		baseTecnica.setGtorosspCap(BigDecimal.ONE);
		final List<String> scasadoX = new ArrayList<String>();
		scasadoX.add(ConstantsModulos.CTE_S);
		scasadoX.add(ConstantsModulos.CTE_S);
		scasadoX.add(ConstantsModulos.CTE_S);
		scasadoX.add(ConstantsModulos.CTE_S);
		scasadoX.add(ConstantsModulos.CTE_S);
		baseTecnica.setSwcasado(scasadoX);
		final GregorianCalendar ini = new GregorianCalendar(1986, 9, 1);
		final GregorianCalendar fin = new GregorianCalendar();
		final GregorianCalendar finEspe = new GregorianCalendar(2014, 10, 30);
		final GregorianCalendar fecCierre = new GregorianCalendar(2020, 8, 1);
		baseTecnica.setFecCierre(new Timestamp(fecCierre.getTimeInMillis()));
		final List<Timestamp> lstIni = new ArrayList<Timestamp>();
		lstIni.add(new Timestamp(ini.getTimeInMillis()));
		lstIni.add(new Timestamp(ini.getTimeInMillis()));
		lstIni.add(new Timestamp(ini.getTimeInMillis()));
		lstIni.add(new Timestamp(ini.getTimeInMillis()));
		lstIni.add(new Timestamp(ini.getTimeInMillis()));
		baseTecnica.setFecInitramo(lstIni);
		final List<Timestamp> lstFin = new ArrayList<Timestamp>();
		lstFin.add(UtilFechas.incrDias(new Timestamp(fin.getTimeInMillis()),1));
		lstFin.add(UtilFechas.incrDias(new Timestamp(fin.getTimeInMillis()),1));
		lstFin.add(UtilFechas.incrDias(new Timestamp(finEspe.getTimeInMillis()),1));
		lstFin.add(UtilFechas.incrDias(new Timestamp(fin.getTimeInMillis()),1));
		lstFin.add(UtilFechas.incrDias(new Timestamp(fin.getTimeInMillis()),1));
		baseTecnica.setFecfintramo(lstFin);
		
		return baseTecnica;
	}
	
	private static void setDatosUmic(final Umic umic) {
		
		umic.getBti().setTabla1Aseg1("740");
		umic.getBti().setFecIniTramo2(new Timestamp(new GregorianCalendar(2014, 11, 1, 0, 0, 0).getTimeInMillis()));
		umic.getBti().setPintertecnI1(ConstantsFunciones.CTE_OPER_2);
		umic.getBti().setPintertecnI2(ConstantsFunciones.CTE_OPER_2);
		umic.getBti().setPintertecnI3(ConstantsFunciones.CTE_OPER_2);
		umic.getBti().setPintertecnI4(ConstantsFunciones.CTE_OPER_2);
		umic.getBti().setPintertecnI5(ConstantsFunciones.CTE_OPER_2);
		
		umic.getAsegurados().setFnacAseg1(new Timestamp(new GregorianCalendar(1950, 6, 1, 0, 0, 0).getTimeInMillis()));
		
		umic.getDuraciones().setNdursegano(30);
		
		umic.getFechas().setFecefecfin(new Timestamp(new GregorianCalendar(2040, 1, 15, 0, 0, 0).getTimeInMillis()));
		umic.getFechas().setFecefecred(new Timestamp(new GregorianCalendar(2014, 9, 6, 0, 0, 0).getTimeInMillis()));
		umic.getFechas().setFecinisus(new Timestamp(new GregorianCalendar(2012, 9, 6, 0, 0, 0).getTimeInMillis()));
		
		umic.getRentas().setFecIni(new Timestamp(new GregorianCalendar(2014, 4, 30, 0, 0, 0).getTimeInMillis()));
		umic.getRentas().setFecFin(new Timestamp(new GregorianCalendar(2014, 11, 30, 0, 0, 0).getTimeInMillis()));
		umic.getRentas().setForpagrent(1);
		umic.getRentas().setCformaRevrenta("C");
		umic.getRentas().setCpagrenta("1");
		umic.getRentas().setCtipoRevrenta("1");
		
		umic.getPrimas().setCformpago("4");
		umic.getComisiones().setNpericomi1(1);
		umic.getComisiones().setNpericomi2(1);
		umic.getComisiones().setNpericomi3(99);
	}
	
	private void setDatosBloqueCorriente(final BloqueCorriente bloqueCorriente) {
		bloqueCorriente.setFechaPago(new Timestamp(new GregorianCalendar(2014, 6, 1, 0, 0, 0).getTimeInMillis()));
		bloqueCorriente.setFechaDevengo(new Timestamp(new GregorianCalendar(2014, 6, 1, 0, 0, 0).getTimeInMillis()));
	}
}
