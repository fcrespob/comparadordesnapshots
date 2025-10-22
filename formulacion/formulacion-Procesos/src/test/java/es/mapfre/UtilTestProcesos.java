package es.mapfre;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.beanio.InvalidRecordException;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.programas.Programa;
import es.mapfre.solvencia.programas.services.FactoriaProgramas;
import es.mapfre.solvencia.util.ConstantsFactorias;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;

public final class UtilTestProcesos {
	
	private static final Logger LOG = LoggerFactory.getLogger(UtilTestProcesos.class);
	
	private static final boolean COMPRIMIR = false;
	public static final String BEANIO_CONFIG_XML = "beanio/beanio-config.xml";
	private static final String ZIPPED_DATA = "datos.txt.gz";
	
	private UtilTestProcesos() {
		
	}
	
	/**
	 * Devuelve las lineas de una fichero en una {@link List}<{@link String}>
	 * @param ruta
	 * @return
	 */
	public static List<String> obtenerFicheroLineas(final String rutaFichero) throws IOException {
		
		final List<String> salida = new ArrayList<String>();
		
		InputStreamReader isr = null;
		BufferedReader buffReader = null;
		try {
			final File archivo = new File(rutaFichero);
			isr = new InputStreamReader(new FileInputStream(archivo), "UTF8");
			buffReader = new BufferedReader(isr);
			
			try {
				String linea = null;
				while ((linea = (String) buffReader.readLine()) != null) {
					salida.add(linea);
				}
			} catch (InvalidRecordException ire) {
				UtilTestProcesos.LOG.error("Error parseando fichero: {}", ire.toString());
			}
		} catch (Exception e) {
			UtilTestProcesos.LOG.error("Error ", e);
		} finally {
			if (buffReader != null) {
				buffReader.close();
			}
		}
		
		return salida;
	}
	
	public static List<Umic> obtenerUMICFichero(final String rutaFichero, final String beanConfig) throws IOException {
		
		final List<Umic> lstUmics = new ArrayList<Umic>();
		
		BeanIOReader inBeanIOReader = null;
		try {
			inBeanIOReader = new BeanIOReader(beanConfig, COMPRIMIR ? ZIPPED_DATA : rutaFichero, "X880JI01");
						
			try {
				Umic umic = null;
				while ((umic = (Umic) inBeanIOReader.read()) != null) {
					lstUmics.add(umic);
				}
			} catch (InvalidRecordException ire) {
				UtilTestProcesos.LOG.error("Error parseando fichero: {}", ire.toString());
			}
		} catch (Exception e) {
			UtilTestProcesos.LOG.error("Error ", e);
		} finally {
			if (inBeanIOReader != null) {
				inBeanIOReader.close();
			}
		}
		
		return lstUmics;
	}
	
	public static FichaProceso crearFichaProceso() {
		final FichaProceso fichaProceso = new FichaProceso();
		
		// Fecha de calculo 1/1/2014 para todos los casos
		fichaProceso.setFcalc(new Timestamp(new GregorianCalendar(2014, 0, 1, 0, 0, 0).getTimeInMillis()));
		
		return fichaProceso;
	}
	
	public static DetalleBaseTecnica crearDetalleBaseTecnica() {
		final DetalleBaseTecnica detalleBT = new DetalleBaseTecnica();
		detalleBT.setBaseTec(ConstantsModulos.CTE_BTI);
		return detalleBT;
	}
	
	public static void setValoresUmic(final Umic umic) {
		umic.getBti().setTabla1Aseg1("740");
		umic.getBti().setFecIniTramo2(new Timestamp(new GregorianCalendar(2014, 11, 1, 0, 0, 0).getTimeInMillis()));
		umic.getRentas().setCformaRevrenta("C");
		umic.getRentas().setCtipoRevrenta("1");
		umic.getFechas().setFecefecred(new Timestamp(new GregorianCalendar(2014, 2, 10, 0, 0, 0).getTimeInMillis()));
		umic.getAsegurados().setFnacAseg1(new Timestamp(new GregorianCalendar(1950, 6, 1, 0, 0, 0).getTimeInMillis()));
	}
	
	public static void setValoresDePrueba(final List<DetalleCorriente> lstDetCorriente) {
		// TODO como no reciben fecha de pago y devengo, se pasan a mano
		for (DetalleCorriente detalleCorriente: lstDetCorriente) {
			detalleCorriente.getBloqueVida().setFechaPago(new Timestamp(new GregorianCalendar(2014, 6, 1, 0, 0, 0).getTimeInMillis()));
			detalleCorriente.getBloqueVida().setFechaDevengo(new Timestamp(new GregorianCalendar(2014, 6, 1, 0, 0, 0).getTimeInMillis()));
			detalleCorriente.getBloqueVida().setFpbAtcfin(BigDecimal.ONE);
			detalleCorriente.getBloqueVida().setImpFlujoNoAnulado(BigDecimal.ONE);
		}
	}
	
	/**
	 * Obtiene un Timestamp a partir de una fecha en formato dd/MM/yyyy cargada desde un fichero csv de pruebas
	 * @param fecha
	 * @return
	 */
	public static Timestamp getFechaCsv(final String fecha) {
		return new Timestamp(new GregorianCalendar(Integer.valueOf(fecha.substring(6, 10)), Integer.valueOf(fecha.substring(3, 5)) - 1,
				Integer.valueOf(fecha.substring(0, 2)), 0, 0, 0).getTimeInMillis());
	}
	
	/**
	 * Obtiene un BigDecimal a partir de un numero cargado desde un fichero csv de pruebas
	 * @param numero
	 * @param scale
	 * @param rounding
	 * @return
	 */
	public static BigDecimal getBigDecimalCsv(final String numero, final Integer scale, final RoundingMode rounding) {
		BigDecimal salida = BigDecimal.ZERO;
		
		if (scale == null) {
			salida = new BigDecimal(numero.replaceAll(",", "."));
		} else if (rounding == null) {
			salida = new BigDecimal(numero.replaceAll(",", ".")).setScale(scale);
		} else {
			salida = new BigDecimal(numero.replaceAll(",", ".")).setScale(scale, rounding);
		}
		
		return salida;
	}
	
	/**
	 * Carga un String desde un array de String de forma segura, para evitar dar Index out of bounds cuando el csv no tiene todas las columnas completas
	 * @param arrayCampos
	 * @return
	 */
	public static String cargarColumnaCsv(final String[] arrayCampos, final int posicion) {
		String salida = null;
		
		if (posicion < arrayCampos.length) {
			salida = arrayCampos[posicion];
		}
		
		return salida;
	}
	
	/**
	 * Compara el valor de un BigDecimal cargado a partir de datos reales con el valor esperado cargado en un fichero csv
	 * @param corrienteActual
	 * @param iteracion
	 * @param nombreCampo
	 * @param expected
	 * @param scaleExpected
	 * @param roundingExpected
	 * @param valor
	 * @param scaleValor
	 * @param roundingValor
	 */
	public static void validarBigDecimal(final String corrienteActual, final int iteracion, final String nombreCampo,
			final String expected, final Integer scaleExpected, final RoundingMode roundingExpected,
			final BigDecimal valor, final Integer scaleValor, final RoundingMode roundingValor) {
		
		if (expected != null) {
			try {
				BigDecimal valorValidar;
				if (scaleValor == null) {
					valorValidar = valor;
				} else if (roundingValor == null) {
					valorValidar = valor.setScale(scaleValor);
				} else {
					valorValidar = valor.setScale(scaleValor, roundingValor);
				}
				
				Assert.assertEquals("Corriente: " + corrienteActual + ". Campo: " + nombreCampo + ". Fallo en el elemento " + iteracion + ".",
						UtilTestProcesos.getBigDecimalCsv(expected, scaleExpected, roundingExpected), valorValidar);
			} catch (AssertionError e) {
				// No lanzar la exception, solo recuperar el mensaje de error
				UtilTestProcesos.LOG.debug(e.getMessage());
			}
		}
	}
	
	/**
	 * Compara el valor de un BigDecimal cargado a partir de datos reales con el valor esperado cargado en un fichero csv
	 * @param corrienteActual
	 * @param iteracion
	 * @param nombreCampo
	 * @param expected
	 * @param valor
	 */
	public static void validarTimestamp(final String corrienteActual, final int iteracion, final String nombreCampo,
			final String expected, final Timestamp valor) {
		
		if (expected != null) {
			try {
				Assert.assertEquals("Corriente: " + corrienteActual + ". Campo: " + nombreCampo + ". Fallo en el elemento " + iteracion + ".", UtilTestProcesos.getFechaCsv(expected), valor);
			} catch (AssertionError e) {
				// No lanzar la exception, solo recuperar el mensaje de error
				UtilTestProcesos.LOG.debug(e.getMessage());
			}
		}
	}
	
	/**
	 * Metodo encargado de recibir la {@link List}<{@link DetalleCorriente}> y comparar los valores obtenidos con los valores esperados
	 * 
	 * @param umic
	 * @param lstDetCorriente
	 * @param corrienteActual
	 * @param mapDatos
	 */
	public static void validarFechas(final Umic umic, final List<DetalleCorriente> lstDetCorriente, final String corrienteActual,
			final Map<String, List<String>> mapDatos) {
		
		final List<String> lstFichero = mapDatos.get(corrienteActual);
		
		if (lstDetCorriente != null && lstFichero != null) {
			
			if (lstDetCorriente.size() == lstFichero.size()) {
				
				UtilTestProcesos.LOG.debug("validarFechas. Corriente: " + corrienteActual);
				
				for (int i = 0; i < lstDetCorriente.size(); i++) {
					
					final DetalleCorriente detCorriente = lstDetCorriente.get(i);
					final BloqueCorriente bloqueProyeccion = detCorriente.getBloqueBySubproceso(corrienteActual);
					final String[] arrCamposFichero = lstFichero.get(i).split(";");
					final String poliza = UtilTestProcesos.cargarColumnaCsv(arrCamposFichero, 6);
					
					// Validar solo si coincide la poliza
					if (poliza.equals(String.valueOf(umic.getKey().getKpoliza()))) {
						
						final String fechaDesde = UtilTestProcesos.cargarColumnaCsv(arrCamposFichero, 15);
						final String fechaHasta = UtilTestProcesos.cargarColumnaCsv(arrCamposFichero, 16);
						final String fechaDevengo = UtilTestProcesos.cargarColumnaCsv(arrCamposFichero, 19);
						final String fechaPago = UtilTestProcesos.cargarColumnaCsv(arrCamposFichero, 20);
						
						UtilTestProcesos.validarTimestamp(corrienteActual, i, "fechaDesde", fechaDesde, detCorriente.getFechaDesde());
						UtilTestProcesos.validarTimestamp(corrienteActual, i, "fechaHasta", fechaHasta, detCorriente.getFechaHasta());
						UtilTestProcesos.validarTimestamp(corrienteActual, i, "fechaDevengo", fechaDevengo, bloqueProyeccion.getFechaDevengo());
						UtilTestProcesos.validarTimestamp(corrienteActual, i, "fechaPago", fechaPago, bloqueProyeccion.getFechaPago());
					}
				}
				
			} else {
				UtilTestProcesos.LOG.debug("validarFechas. Corriente: " + corrienteActual + ". La lista de DetalleCorriente tiene " + lstDetCorriente.size() + " periodos y el listado del fichero tiene " +
						lstFichero.size() + ". Corriente actual: " + corrienteActual);
			}
		}
	}
	
	/**
	 * Metodo encargado de recibir la {@link List}<{@link DetalleCorriente}> y comparar los valores obtenidos con los valores esperados
	 * 
	 * @param umic
	 * @param lstDetCorriente
	 * @param corrienteActual
	 * @param mapDatos
	 */
	public static void validarFlujoNominal(final Umic umic, final List<DetalleCorriente> lstDetCorriente, final String corrienteActual,
			final Map<String, List<String>> mapDatos) {

		final List<String> lstFichero = mapDatos.get(corrienteActual);
		
		if (lstDetCorriente != null && lstFichero != null) {
			
			if (lstDetCorriente.size() == lstFichero.size()) {
				
				UtilTestProcesos.LOG.debug("validarFlujoNominal. Corriente: " + corrienteActual);
				
				for (int i = 0; i < lstDetCorriente.size(); i++) {
					
					final DetalleCorriente detCorriente = lstDetCorriente.get(i);
					final BloqueCorriente bloqueProyeccion = detCorriente.getBloqueBySubproceso(corrienteActual);
					final String[] arrCamposFichero = lstFichero.get(i).split(";");
					final String poliza = UtilTestProcesos.cargarColumnaCsv(arrCamposFichero, 6);
					
					// Validar solo si coincide la poliza
					if (poliza.equals(String.valueOf(umic.getKey().getKpoliza()))) {
						
						final String impPago = UtilTestProcesos.cargarColumnaCsv(arrCamposFichero, 18);
						final String impFlujoNominal = UtilTestProcesos.cargarColumnaCsv(arrCamposFichero, 21);
						
						UtilTestProcesos.validarBigDecimal(corrienteActual, i, "impPago", impPago, ConstantsFunciones.CTE_2, null, detCorriente.getImpPago(), ConstantsFunciones.CTE_2, RoundingMode.HALF_DOWN);
						UtilTestProcesos.validarBigDecimal(corrienteActual, i, "impFlujoNominal", impFlujoNominal, ConstantsFunciones.CTE_2, null, bloqueProyeccion.getImpFlujoNominal(), null, null);
					}
				}
				
			} else {
				UtilTestProcesos.LOG.debug("validarFlujoNominal. Corriente: " + corrienteActual + ". La lista de DetalleCorriente tiene " + lstDetCorriente.size() + " periodos y el listado del fichero tiene " +
						lstFichero.size() + ". Corriente actual: " + corrienteActual);
			}
		}
	}
	
	/**
	 * Metodo encargado de recibir la {@link List}<{@link DetalleCorriente}> y comparar los valores obtenidos con los valores esperados
	 * 
	 * @param umic
	 * @param lstDetCorriente
	 * @param corrienteActual
	 * @param mapDatos
	 */
	public static void validarFlujoProbable(final Umic umic, final List<DetalleCorriente> lstDetCorriente, final String corrienteActual,
			final Map<String, List<String>> mapDatos) {

		final List<String> lstFichero = mapDatos.get(corrienteActual);
		
		if (lstDetCorriente != null && lstFichero != null) {
			
			if (lstDetCorriente.size() == lstFichero.size()) {
				
				UtilTestProcesos.LOG.debug("validarFlujoProbable. Corriente: " + corrienteActual);
				
				for (int i = 0; i < lstDetCorriente.size(); i++) {
					
					final DetalleCorriente detCorriente = lstDetCorriente.get(i);
					final BloqueCorriente bloqueProyeccion = detCorriente.getBloqueBySubproceso(corrienteActual);
					final String[] arrCamposFichero = lstFichero.get(i).split(";");
					final String poliza = UtilTestProcesos.cargarColumnaCsv(arrCamposFichero, 6);
					
					// Validar solo si coincide la poliza
					if (poliza.equals(String.valueOf(umic.getKey().getKpoliza()))) {
						
						final String fpbProbable = UtilTestProcesos.cargarColumnaCsv(arrCamposFichero, 22);
						final String impFlujoProbable = UtilTestProcesos.cargarColumnaCsv(arrCamposFichero, 23);
						
						UtilTestProcesos.validarBigDecimal(corrienteActual, i, "fpbProbable", fpbProbable, ConstantsFunciones.CTE_8, null, bloqueProyeccion.getFpbProbable(), ConstantsFunciones.CTE_8, RoundingMode.HALF_DOWN);
						UtilTestProcesos.validarBigDecimal(corrienteActual, i, "impFlujoProbable", impFlujoProbable, ConstantsFunciones.CTE_2, null, bloqueProyeccion.getImpFlujoProbable(), null, null);
					}
				}
				
			} else {
				UtilTestProcesos.LOG.debug("validarFlujoProbable. Corriente: " + corrienteActual + ". La lista de DetalleCorriente tiene " + lstDetCorriente.size() + " periodos y el listado del fichero tiene " +
			lstFichero.size() + ". Corriente actual: " + corrienteActual);
			}
		}
	}
	
	/**
	 * Metodo encargado de recibir la {@link List}<{@link DetalleCorriente}> y comparar los valores obtenidos con los valores esperados
	 * 
	 * @param umic
	 * @param lstDetCorriente
	 * @param corrienteActual
	 * @param mapDatos
	 */
	public static void validarFlujoNoAnulado(final Umic umic, final List<DetalleCorriente> lstDetCorriente, final String corrienteActual,
			final Map<String, List<String>> mapDatos) {

		final List<String> lstFichero = mapDatos.get(corrienteActual);
		
		if (lstDetCorriente != null && lstFichero != null) {
			
			if (lstDetCorriente.size() == lstFichero.size()) {
				
				UtilTestProcesos.LOG.debug("validarFlujoNoAnulado. Corriente: " + corrienteActual);
				
				for (int i = 0; i < lstDetCorriente.size(); i++) {
					
					final DetalleCorriente detCorriente = lstDetCorriente.get(i);
					final BloqueCorriente bloqueProyeccion = detCorriente.getBloqueBySubproceso(corrienteActual);
					final String[] arrCamposFichero = lstFichero.get(i).split(";");
					final String poliza = UtilTestProcesos.cargarColumnaCsv(arrCamposFichero, 6);
					
					// Validar solo si coincide la poliza
					if (poliza.equals(String.valueOf(umic.getKey().getKpoliza()))) {
						
						final String fpbAtc = UtilTestProcesos.cargarColumnaCsv(arrCamposFichero, 24);
						final String impFlujoNoAnulado = UtilTestProcesos.cargarColumnaCsv(arrCamposFichero, 25);
						
						UtilTestProcesos.validarBigDecimal(corrienteActual, i, "fpbAtc", fpbAtc, ConstantsFunciones.CTE_6, null, bloqueProyeccion.getFpbAtc(), ConstantsFunciones.CTE_6, RoundingMode.HALF_DOWN);
						UtilTestProcesos.validarBigDecimal(corrienteActual, i, "impFlujoNoAnulado", impFlujoNoAnulado, ConstantsFunciones.CTE_2, null, bloqueProyeccion.getImpFlujoNoAnulado(), null, null);
					}
				}
				
			} else {
				UtilTestProcesos.LOG.debug("validarFlujoNoAnulado. Corriente: " + corrienteActual + ". La lista de DetalleCorriente tiene " + lstDetCorriente.size() + " periodos y el listado del fichero tiene " +
			lstFichero.size() + ". Corriente actual: " + corrienteActual);
			}
		}
	}
	
	/**
	 * Metodo encargado de recibir la {@link List}<{@link DetalleCorriente}> y comparar los valores obtenidos con los valores esperados
	 * 
	 * @param umic
	 * @param lstDetCorriente
	 * @param corrienteActual
	 * @param mapDatos
	 */
	public static void validarFlujoActualizado(final Umic umic, final List<DetalleCorriente> lstDetCorriente, final String corrienteActual,
			final Map<String, List<String>> mapDatos) {

		final List<String> lstFichero = mapDatos.get(corrienteActual);
		
		if (lstDetCorriente != null && lstFichero != null) {
			
			if (lstDetCorriente.size() == lstFichero.size()) {
				
				UtilTestProcesos.LOG.debug("validarFlujoActualizado. Corriente: " + corrienteActual);
				
				for (int i = 0; i < lstDetCorriente.size(); i++) {
					
					final DetalleCorriente detCorriente = lstDetCorriente.get(i);
					final BloqueCorriente bloqueProyeccion = detCorriente.getBloqueBySubproceso(corrienteActual);
					final String[] arrCamposFichero = lstFichero.get(i).split(";");
					final String poliza = UtilTestProcesos.cargarColumnaCsv(arrCamposFichero, 6);
					
					// Validar solo si coincide la poliza
					if (poliza.equals(String.valueOf(umic.getKey().getKpoliza()))) {
						
						final String fpbAtcfin = UtilTestProcesos.cargarColumnaCsv(arrCamposFichero, 26);
						final String impFlujoAct = UtilTestProcesos.cargarColumnaCsv(arrCamposFichero, 27);
						
						UtilTestProcesos.validarBigDecimal(corrienteActual, i, "fpbAtcfin", fpbAtcfin, ConstantsFunciones.CTE_8, null, bloqueProyeccion.getFpbAtcfin(), ConstantsFunciones.CTE_8, RoundingMode.HALF_DOWN);
						UtilTestProcesos.validarBigDecimal(corrienteActual, i, "impFlujoActualizado", impFlujoAct, ConstantsFunciones.CTE_2, null, bloqueProyeccion.getImpFlujoActualizado(), null, null);
					}
				}
				
			} else {
				UtilTestProcesos.LOG.debug("validarFlujoActualizado. Corriente: " + corrienteActual + ". La lista de DetalleCorriente tiene " + lstDetCorriente.size() + " periodos y el listado del fichero tiene " +
			lstFichero.size() + ". Corriente actual: " + corrienteActual);
			}
		}
	}
	
	public static List<DetalleCorriente> obtenerPeriodos(final Umic umic, final FichaProceso fichaProceso, final DetalleBaseTecnica detalleBT, final String codSubproceso) {
		final List<DetalleCorriente> lstDetCorriente = new ArrayList<DetalleCorriente>();
		
		Programa programa = null;
		
		//1º Lanzamos el programa GBT001
		programa = FactoriaProgramas.getPrograma(ConstantsFactorias.PROGRAMA_GBT001);
		programa.execute(umic, fichaProceso, detalleBT, lstDetCorriente, codSubproceso);
		
		//2º Lanzamos el programa PPR001
		programa = FactoriaProgramas.getPrograma(ConstantsFactorias.PROGRAMA_PPR001);
		programa.execute(umic, fichaProceso, detalleBT, lstDetCorriente, codSubproceso);
		
		return lstDetCorriente;
	}
	
	public static void obtenerFechaPagoDevengo(final Umic umic, final FichaProceso fichaProceso,
			final DetalleBaseTecnica detalleBT, final List<DetalleCorriente> lstDetCorriente, final String codSubproceso) {
		Programa programa = null;
		programa = FactoriaProgramas.getPrograma(ConstantsFactorias.PROGRAMA_FEC001);
		programa.execute(umic, fichaProceso, detalleBT, lstDetCorriente, codSubproceso);
	}
	
	public static void obtenerFlujoNominal(final Umic umic, final FichaProceso fichaProceso,
			final DetalleBaseTecnica detalleBT, final List<DetalleCorriente> lstDetalles, final String codSubproceso) {
		Programa programa = null;
		programa = FactoriaProgramas.getPrograma(ConstantsFactorias.PROGRAMA_NOM001);
		programa.execute(umic, fichaProceso, detalleBT, lstDetalles, codSubproceso);
	}
	
	public static void obtenerFlujoProbable(final Umic umic, final FichaProceso fichaProceso,
			final DetalleBaseTecnica detalleBT, final List<DetalleCorriente> lstDetalles, final String codSubproceso) {
		Programa programa = null;
		programa = FactoriaProgramas.getPrograma(ConstantsFactorias.PROGRAMA_PRB001);
		programa.execute(umic, fichaProceso, detalleBT, lstDetalles, codSubproceso);
	}
	
	public static void obtenerFlujoNoAnulado(final Umic umic, final FichaProceso fichaProceso,
			final DetalleBaseTecnica detalleBT, final List<DetalleCorriente> lstDetalles, final String codSubproceso) {
		Programa programa = null;
		programa = FactoriaProgramas.getPrograma(ConstantsFactorias.PROGRAMA_NAN001);
		programa.execute(umic, fichaProceso, detalleBT, lstDetalles, codSubproceso);
	}
	
	public static void obtenerFlujoActualizado(final Umic umic, final FichaProceso fichaProceso,
			final DetalleBaseTecnica detalleBT, final List<DetalleCorriente> lstDetalles, final String codSubproceso) {
		Programa programa = null;
		programa = FactoriaProgramas.getPrograma(ConstantsFactorias.PROGRAMA_ACT001);
		programa.execute(umic, fichaProceso, detalleBT, lstDetalles, codSubproceso);
	}
	
	public static void obtenerFlujoFormulaCerrada(final Umic umic, final FichaProceso fichaProceso,
			final DetalleBaseTecnica detalleBT, final List<DetalleCorriente> lstDetalles, final String codSubproceso) {
		Programa programa = null;
		programa = FactoriaProgramas.getPrograma(ConstantsFactorias.PROGRAMA_PMC001);
		programa.execute(umic, fichaProceso, detalleBT, lstDetalles, codSubproceso);
	}
	
	public static void obtenerFlujoProvi(final Umic umic, final FichaProceso fichaProceso,
			final DetalleBaseTecnica detalleBT, final List<DetalleCorriente> lstDetalles, final String codSubproceso) {
		Programa programa = null;
		programa = FactoriaProgramas.getPrograma(ConstantsFactorias.PROGRAMA_PVI001);
		programa.execute(umic, fichaProceso, detalleBT, lstDetalles, codSubproceso);
	}
	
	public static void obtenerProvisionMatematica(final Umic umic, final FichaProceso fichaProceso,
			final DetalleBaseTecnica detalleBT, final List<DetalleCorriente> lstDetalles, final String codSubproceso) {
		Programa programa = null;
		programa = FactoriaProgramas.getPrograma(ConstantsFactorias.PROGRAMA_PRV001);
		programa.execute(umic, fichaProceso, detalleBT, lstDetalles, codSubproceso);
	}
}
