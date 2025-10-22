package es.mapfre.fasePruebas;

import static org.junit.Assert.fail;

import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.List;

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

public class Test209ROSPGARA {
	
	private static final String RUTA = "src/test/resources/CARTERA.txt";
	
	private static final Logger LOG = LoggerFactory.getLogger(Test209ROSPGARA.class);
	
//	@Test
	public void test() {
		//Variables locales
		List<Umic> lstUmics = null;
		final String corrientes[] = {ConstantsModulos.CTE_PROY_VIDA, ConstantsFunciones.CTE_CADENA_VACIA, ConstantsFunciones.CTE_CADENA_VACIA,
				ConstantsModulos.CTE_PROY_GTOS, ConstantsFunciones.CTE_CADENA_VACIA, ConstantsFunciones.CTE_CADENA_VACIA};
		final FichaProceso fichaProceso = new FichaProceso();
		final DetalleBaseTecnica detalleBT = new DetalleBaseTecnica();
		//Fin variables locales
		
		try {
			//1- LEEMOS EL FICHERO DE UMIC y RECUPERAMOS UNA UNICA UMIC
			lstUmics = UtilTestProcesos.obtenerUMICFichero(RUTA, UtilTestProcesos.BEANIO_CONFIG_XML);
			
			final Umic dejar1Umic = lstUmics.get(2);
			lstUmics.clear();
			lstUmics.add(dejar1Umic);
			
			// Detalle base tecnica
			detalleBT.setBaseTec(ConstantsModulos.CTE_BT_ROSSP);
			
			// Ficha proceso
			fichaProceso.setCtipobt(ConstantsModulos.CTE_BT_ROSSP);
			
			// Fecha de calculo 1/1/2014 para todos los casos
			fichaProceso.setFcalc(new Timestamp(new GregorianCalendar(2014, 0, 1, 0, 0, 0).getTimeInMillis()));
			
			for (int i = 0; i < lstUmics.size(); i++) {
				// Cargamos la umic en el array
				final Umic umic = lstUmics.get(i);
				
				//Cargamos los DetalleCorriente en el array para la umic
				final List<DetalleCorriente> lstDetCorriente = UtilTestProcesos.obtenerPeriodos(umic, fichaProceso, detalleBT, ConstantsModulos.CTE_PROY_VIDA);
				
				for (String corrienteActual : corrientes) {
					if (!corrienteActual.isEmpty()) {
						//3º Lanzamos el programa FEC001
						UtilTestProcesos.obtenerFechaPagoDevengo(umic, fichaProceso, detalleBT, lstDetCorriente, corrienteActual);
						
						//4º Lanzamos el programa NOM001
						UtilTestProcesos.obtenerFlujoNominal(umic, fichaProceso, detalleBT, lstDetCorriente, corrienteActual);
						
						//5º Lanzamos el programa PRB001
						//UtilTestProcesos.obtenerFlujoProbable(umic, fichaProceso, detalleBT, lstDetCorriente, corrienteActual);
						
						//6º Lanzamos el programa NAN001
						//UtilTestProcesos.obtenerFlujoNoAnulado(umic, fichaProceso, detalleBT, lstDetCorriente, corrienteActual);
						
						//7º Lanzamos el programa ACT001
						//UtilTestProcesos.obtenerFlujoActualizado(umic, fichaProceso, detalleBT, lstDetCorriente, corrienteActual);
						
						//8º Lanzamos el programa PMC001
						//UtilTestProcesos.obtenerFlujoFormulaCerrada(umic, fichaProceso, detalleBT, lstDetCorriente, corrienteActual);
						
						//9º Lanzamos el programa PVI001
						//UtilTestProcesos.obtenerFlujoProvi(umic, fichaProceso, detalleBT, lstDetCorriente, corrienteActual);
					}
				}
				
				//10º Lanzamos el programa PRV001 para la provisión matematica
				//UtilTestProcesos.obtenerProvisionMatematica(umic, fichaProceso, detalleBT, lstDetCorriente, ConstantsModulos.CTE_PROY_VIDA);
			}
		} catch (Solvencia2Excepcion e) {
			Test209ROSPGARA.LOG.debug(e.getMessage(), e);
			fail();
		} catch (Throwable e) {
			Test209ROSPGARA.LOG.debug(e.getMessage(), e);
		}
	}
}
