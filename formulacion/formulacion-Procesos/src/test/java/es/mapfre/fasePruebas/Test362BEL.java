package es.mapfre.fasePruebas;

import static org.junit.Assert.fail;

import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.UtilTestProcesos;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;

public class Test362BEL {
	
	private static final Logger LOG = LoggerFactory.getLogger(Test362BEL.class);
	
	private static final String RUTA = "src/test/resources/CARTERA.txt";
	private static final String RUTA_VIDA_FEC001 = "src/test/resources/362_BEL/FLUJOS_362_BEL_VIDA_FEC001.csv";
	private static final String RUTA_VIDA_NOM001 = "src/test/resources/362_BEL/FLUJOS_362_BEL_VIDA_NOM001.csv";
	private static final String RUTA_VIDA_PRB001 = "src/test/resources/362_BEL/FLUJOS_362_BEL_VIDA_PRB001.csv";
	private static final String RUTA_VIDA_NAN001 = "src/test/resources/362_BEL/FLUJOS_362_BEL_VIDA_NAN001.csv";
	private static final String RUTA_VIDA_ACT001 = "src/test/resources/362_BEL/FLUJOS_362_BEL_VIDA_ACT001.csv";
	//private static final String RUTA_VIDA_PVI001 = "src/test/resources/362_BEL/FLUJOS_362_BEL_VIDA_PVI001.csv";
	
	private static final String RUTA_FALL_FEC001 = "src/test/resources/362_BEL/FLUJOS_362_BEL_FALL_FEC001.csv";
	private static final String RUTA_FALL_NOM001 = "src/test/resources/362_BEL/FLUJOS_362_BEL_FALL_NOM001.csv";
	private static final String RUTA_FALL_PRB001 = "src/test/resources/362_BEL/FLUJOS_362_BEL_FALL_PRB001.csv";
	private static final String RUTA_FALL_NAN001 = "src/test/resources/362_BEL/FLUJOS_362_BEL_FALL_NAN001.csv";
	private static final String RUTA_FALL_ACT001 = "src/test/resources/362_BEL/FLUJOS_362_BEL_FALL_ACT001.csv";
	//private static final String RUTA_FALL_PVI001 = "src/test/resources/362_BEL/FLUJOS_362_BEL_FALL_PVI001.csv";
	
//	@Test
	public void test() {
		//Variables locales
		List<Umic> lstUmics = null;
//		final String corrientes[] = {ConstantsModulos.CTE_PROY_VIDA, ConstantsModulos.CTE_PROY_FALL, ConstantsModulos.CTE_PROY_GTOS,
//				ConstantsModulos.CTE_PROY_RESC, ConstantsFunciones.CTE_CADENA_VACIA, ConstantsFunciones.CTE_CADENA_VACIA};
		final String corrientes[] = {ConstantsModulos.CTE_PROY_VIDA, ConstantsModulos.CTE_PROY_FALL, ConstantsFunciones.CTE_CADENA_VACIA,
				ConstantsFunciones.CTE_CADENA_VACIA, ConstantsFunciones.CTE_CADENA_VACIA, ConstantsFunciones.CTE_CADENA_VACIA};
		final FichaProceso fichaProceso = new FichaProceso();
		final DetalleBaseTecnica detalleBT = new DetalleBaseTecnica();
		//Fin variables locales
		
		try {
			//1- LEEMOS EL FICHERO DE UMIC y RECUPERAMOS UNA UNICA UMIC
			lstUmics = UtilTestProcesos.obtenerUMICFichero(RUTA, UtilTestProcesos.BEANIO_CONFIG_XML);
			
			final Umic dejar1Umic = lstUmics.get(25);
			lstUmics.clear();
			lstUmics.add(dejar1Umic);
			
			// Detalle base tecnica
			detalleBT.setBaseTec(ConstantsModulos.CTE_BT_BEL);
			
			// Ficha proceso
			fichaProceso.setCtipobt(ConstantsModulos.CTE_BT_BEL);
			
			// Fecha de calculo 1/1/2014 para todos los casos
			fichaProceso.setFcalc(new Timestamp(new GregorianCalendar(2014, 0, 1, 0, 0, 0).getTimeInMillis()));
			
			// Se crean los mapas de ficheros de prueba por corriente
			final Map<String, List<String>> mapFec = new HashMap<String, List<String>>();
			final Map<String, List<String>> mapNom = new HashMap<String, List<String>>();
			final Map<String, List<String>> mapPrb = new HashMap<String, List<String>>();
			final Map<String, List<String>> mapNan = new HashMap<String, List<String>>();
			final Map<String, List<String>> mapAct = new HashMap<String, List<String>>();
			//final Map<String, List<String>> mapPmc = new HashMap<String, List<String>>();
			//final Map<String, List<String>> mapPvi = new HashMap<String, List<String>>();
			
			// Cargar de los ficheros de VIDA
			mapFec.put(ConstantsModulos.CTE_PROY_VIDA, UtilTestProcesos.obtenerFicheroLineas(RUTA_VIDA_FEC001));
			mapNom.put(ConstantsModulos.CTE_PROY_VIDA, UtilTestProcesos.obtenerFicheroLineas(RUTA_VIDA_NOM001));
			mapPrb.put(ConstantsModulos.CTE_PROY_VIDA, UtilTestProcesos.obtenerFicheroLineas(RUTA_VIDA_PRB001));
			mapNan.put(ConstantsModulos.CTE_PROY_VIDA, UtilTestProcesos.obtenerFicheroLineas(RUTA_VIDA_NAN001));
			mapAct.put(ConstantsModulos.CTE_PROY_VIDA, UtilTestProcesos.obtenerFicheroLineas(RUTA_VIDA_ACT001));
			//mapPvi.put(ConstantsModulos.CTE_PROY_VIDA, UtilTestProcesos.obtenerFicheroLineas(RUTA_VIDA_PVI001));
			
			// Cargar de los ficheros de FALLECIMIENTO
			mapFec.put(ConstantsModulos.CTE_PROY_FALL, UtilTestProcesos.obtenerFicheroLineas(RUTA_FALL_FEC001));
			mapNom.put(ConstantsModulos.CTE_PROY_FALL, UtilTestProcesos.obtenerFicheroLineas(RUTA_FALL_NOM001));
			mapPrb.put(ConstantsModulos.CTE_PROY_FALL, UtilTestProcesos.obtenerFicheroLineas(RUTA_FALL_PRB001));
			mapNan.put(ConstantsModulos.CTE_PROY_FALL, UtilTestProcesos.obtenerFicheroLineas(RUTA_FALL_NAN001));
			mapAct.put(ConstantsModulos.CTE_PROY_FALL, UtilTestProcesos.obtenerFicheroLineas(RUTA_FALL_ACT001));
			//mapPvi.put(ConstantsModulos.CTE_PROY_FALL, UtilTestProcesos.obtenerFicheroLineas(RUTA_FALL_PVI001));
			
			for (int i = 0; i < lstUmics.size(); i++) {
				// Cargamos la umic en el array
				final Umic umic = lstUmics.get(i);
				
				//Cargamos los DetalleCorriente en el array para la umic
				final List<DetalleCorriente> lstDetCorriente = UtilTestProcesos.obtenerPeriodos(umic, fichaProceso, detalleBT, ConstantsModulos.CTE_PROY_VIDA);
				
				for (String corrienteActual : corrientes) {
					if (!corrienteActual.isEmpty()) {
						//3º Lanzamos el programa FEC001
						UtilTestProcesos.obtenerFechaPagoDevengo(umic, fichaProceso, detalleBT, lstDetCorriente, corrienteActual);
						UtilTestProcesos.validarFechas(umic, lstDetCorriente, corrienteActual, mapFec);
						
						//4º Lanzamos el programa NOM001
						UtilTestProcesos.obtenerFlujoNominal(umic, fichaProceso, detalleBT, lstDetCorriente, corrienteActual);
						UtilTestProcesos.validarFlujoNominal(umic, lstDetCorriente, corrienteActual, mapNom);
						
						//5º Lanzamos el programa PRB001
						UtilTestProcesos.obtenerFlujoProbable(umic, fichaProceso, detalleBT, lstDetCorriente, corrienteActual);
						UtilTestProcesos.validarFlujoProbable(umic, lstDetCorriente, corrienteActual, mapPrb);
						
						//6º Lanzamos el programa NAN001
						UtilTestProcesos.obtenerFlujoNoAnulado(umic, fichaProceso, detalleBT, lstDetCorriente, corrienteActual);
						UtilTestProcesos.validarFlujoNoAnulado(umic, lstDetCorriente, corrienteActual, mapNan);
						
						//7º Lanzamos el programa ACT001
						UtilTestProcesos.obtenerFlujoActualizado(umic, fichaProceso, detalleBT, lstDetCorriente, corrienteActual);
						UtilTestProcesos.validarFlujoActualizado(umic, lstDetCorriente, corrienteActual, mapAct);
						
						//8º Lanzamos el programa PMC001
						//UtilTestProcesos.obtenerFlujoFormulaCerrada(umic, fichaProceso, detalleBT, lstDetCorriente, corrienteActual);
						//UtilTestProcesos.validarFlujoFormulaCerrada(umic, lstDetCorriente, corrienteActual, mapPmc);
						
						//9º Lanzamos el programa PVI001
						//UtilTestProcesos.obtenerFlujoProvi(umic, fichaProceso, detalleBT, lstDetCorriente, corrienteActual);
						//UtilTestProcesos.validarFlujoProvi(umic, lstDetCorriente, corrienteActual, mapPvi);
						
						Test362BEL.LOG.debug("Fin corriente: " + corrienteActual);
					}
				}
				
				//10º Lanzamos el programa PRV001 para la provisión matematica
				//UtilTestProcesos.obtenerProvisionMatematica(umic, fichaProceso, detalleBT, lstDetCorriente, ConstantsModulos.CTE_PROY_VIDA);
			}
			
		} catch (Solvencia2Excepcion e) {
			Test362BEL.LOG.debug(e.getMessage(), e);
			fail();
		} catch (Throwable e) {
			Test362BEL.LOG.debug(e.getMessage(), e);
		}
	}
}
