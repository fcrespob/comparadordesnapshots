package es.mapfre.solvencia.formulacion.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.TestCase;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilFechasTest {
	
	private static final Logger LOG = LoggerFactory.getLogger(UtilFechasTest.class);
	
	/**
	 * Inicialización para todas las pruebas. Para que se ejecute, la clase no debe extender de TestCase.
	 * A diferencia de @Before, que se ejecuta una vez justo antes de cada @Test,
	 * @BeforeClass se ejecuta una sola vez para todo el archivo de pruebas.
	 */
	@BeforeClass
	public static void init() {
		UtilFechasTest.LOG.debug("Inicializando pruebas");
	}
	
	@Test
	public void testDiferenciasDeFechas() {
		
		// Ejecutar para un rango de fechas entre los que existan cambios de hora y años bisiestos
		final List<int[]> dias = obtenerDias(10, 3, 2004, 5, 4, 2020);
		
		// Con los dias validos se ejecuta el metodo varias veces con distintas formas de generar fechas
		long difAnt = 0;
		Timestamp fechaAnterior = null;
		final int[] arrayUltimaFecha = dias.get(dias.size() - 1);
		
		// Fecha de fin para diferenciasDeFechas()
		final Calendar calUltimaFecha = new GregorianCalendar(arrayUltimaFecha[0], arrayUltimaFecha[1], arrayUltimaFecha[2], 0, 0, 0);
		calUltimaFecha.set(Calendar.MILLISECOND, 0);
		final Timestamp ultimaFecha = new Timestamp(calUltimaFecha.getTimeInMillis());
		
		boolean primerCaso = true;
		for (int[] arrayFechaActual : dias) {
			
			// Fecha de inicio para diferenciasDeFechas()
			final Calendar calFechaActual = new GregorianCalendar(arrayFechaActual[0], arrayFechaActual[1], arrayFechaActual[2], 0, 0, 0);
			calFechaActual.set(Calendar.MILLISECOND, 0);
			final Timestamp fechaActual = new Timestamp(calFechaActual.getTimeInMillis());
			
			final int dif = UtilFechas.diferenciasDeFechas(fechaActual, ultimaFecha);
			
			final long restaDiferencias = difAnt - dif;
			
			// No comprobar para el primer elemento
			if (!primerCaso) {
				
				/*
				 * Se puede comprobar que este caso falla si se eliminan las filas:
				 * date.setTimeZone(UtilFechas.STZ) del método UtilFechasTest.diferenciasDeFechas
				 * Este error se va a reproducir aunque se marque a 0 las horas, minutos, segundos y milisegundos
				 */
				TestCase.assertEquals("diferenciasDeFechas() no valido. Las comprobaciones de rangos no devuelve diferencias de un día" +
						"entre el rango actual y el rango anterior:\nRango anterior: De " +
						fechaAnterior + " a " + ultimaFecha + ": " + difAnt + " dias.\nRango actual: De " +
						fechaActual + " a " + ultimaFecha + ": " + dif + " dias.\n",
						1, restaDiferencias);
			}
			
			primerCaso = false;
			difAnt = dif;
			fechaAnterior = fechaActual;
		}
		
	}
	
	@Test
	public void testObtenerFechaMitad() {
		
		// Ejecutar para un rango de fechas entre los que existan cambios de hora y años bisiestos
		final List<int[]> dias = obtenerDias(10, 3, 2004, 5, 4, 2020);
		
		boolean primerCaso = true;
		for (int[] arrayFechaActual : dias) {
			
			// Fecha actual
			final Calendar calFechaActual = new GregorianCalendar(arrayFechaActual[0], arrayFechaActual[1], arrayFechaActual[2], 0, 0, 0);
			calFechaActual.set(Calendar.MILLISECOND, 0);
			final Timestamp fechaActual = new Timestamp(calFechaActual.getTimeInMillis());
			
			// TODO pendiente de duda, si finalmente hay que redondear por arriba, usar el dia: arrayFechaActual[2] + 5
			// Fecha mitad esperada para los rangos de fechas pares
			final Calendar calFechaMitadExpPar = new GregorianCalendar(arrayFechaActual[0], arrayFechaActual[1], arrayFechaActual[2] + 4, 0, 0, 0);
			calFechaMitadExpPar.set(Calendar.MILLISECOND, 0);
			final Timestamp fechaMitadExpPar = new Timestamp(calFechaMitadExpPar.getTimeInMillis());
			
			// Fecha mitad esperada para los rangos de fechas impares
			final Calendar calFechaMitadExpImp = new GregorianCalendar(arrayFechaActual[0], arrayFechaActual[1], arrayFechaActual[2] + 5, 0, 0, 0);
			calFechaMitadExpImp.set(Calendar.MILLISECOND, 0);
			final Timestamp fechaMitadExpImp = new Timestamp(calFechaMitadExpImp.getTimeInMillis());
			
			// Fecha de fin para diferencias entre dias pares
			final Calendar calFechaFinPares = new GregorianCalendar(arrayFechaActual[0], arrayFechaActual[1], arrayFechaActual[2] + 9, 0, 0, 0);
			calFechaFinPares.set(Calendar.MILLISECOND, 0);
			final Timestamp fechaFinPares = new Timestamp(calFechaFinPares.getTimeInMillis());
			
			// Fecha de fin para diferencias entre dias impares
			final Calendar calFechaFinImpares = new GregorianCalendar(arrayFechaActual[0], arrayFechaActual[1], arrayFechaActual[2] + 10, 0, 0, 0);
			calFechaFinImpares.set(Calendar.MILLISECOND, 0);
			final Timestamp fechaFinImpares = new Timestamp(calFechaFinImpares.getTimeInMillis());
			
			// No comprobar para el primer elemento
			if (!primerCaso) {
				
				Timestamp fechaMitadPares = UtilFechas.obtenerFechaMitad(fechaActual, fechaFinPares);
				Timestamp fechaMitadImpares = UtilFechas.obtenerFechaMitad(fechaActual, fechaFinImpares);
				
				// TODO por ahora, pendiente de duda, se fuerza el reseteo de las horas a 0:
				final GregorianCalendar gcFechaMitadPares = new GregorianCalendar();
				gcFechaMitadPares.setTime(fechaMitadPares);
				gcFechaMitadPares.set(gcFechaMitadPares.get(Calendar.YEAR), gcFechaMitadPares.get(Calendar.MONTH), gcFechaMitadPares.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
				gcFechaMitadPares.set(Calendar.MILLISECOND, 0);
				fechaMitadPares = new Timestamp(gcFechaMitadPares.getTimeInMillis());
				
				// TODO por ahora, pendiente de duda, se fuerza el reseteo de las horas a 0:
				final GregorianCalendar gcFechaMitadImpares = new GregorianCalendar();
				gcFechaMitadImpares.setTime(fechaMitadImpares);
				gcFechaMitadImpares.set(gcFechaMitadImpares.get(Calendar.YEAR), gcFechaMitadImpares.get(Calendar.MONTH), gcFechaMitadImpares.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
				gcFechaMitadImpares.set(Calendar.MILLISECOND, 0);
				fechaMitadImpares = new Timestamp(gcFechaMitadImpares.getTimeInMillis());
				
				// Comprobacion para rangos de fechas con dias pares
				TestCase.assertEquals("obtenerFechaMitad() no valido para rangos de fechas con dias pares. Fecha de inicio: " +
						fechaActual + ", fecha de fin: " + fechaFinPares + ", fecha mitad " + fechaMitadPares,
						fechaMitadExpPar, fechaMitadPares);
				
				// Comprobacion para rangos de fechas con dias impares
				TestCase.assertEquals("obtenerFechaMitad() no valido para rangos de fechas con dias impares. Fecha de inicio: " +
						fechaActual + ", fecha de fin: " + fechaFinImpares + ", fecha mitad " + fechaMitadImpares,
						fechaMitadExpImp, fechaMitadImpares);
			}
			
			primerCaso = false;
		}
	}
	
	/**
	 * Devuelve una lista de fechas válidas, en formato int[] {año, mes, dia}, para el rango de entrada introducido,
	 * desde dia1-mes1-anio1, hasta dia2-mes2-anio2. Este método se usa para probar que las modificaciones realizadas
	 * en la clase UtilFechas no devuelve datos erroneos para los cambios en los horarios de verano o en años bisiestos.
	 * 
	 * @param dia1
	 * @param mes1
	 * @param anio1
	 * @param dia2
	 * @param mes2
	 * @param anio2
	 * @return
	 */
	public static List<int[]> obtenerDias(
			final int dia1, final int mes1, final int anio1, final int dia2, final int mes2, final int anio2) {
		
		boolean continuar = true;
		int diaActual = dia1;
		int mesActual = mes1;
		int anioActual = anio1;
		final List<int[]> salida = new ArrayList<int[]>();
		
		try {
			
			// Primero se crea la fecha de fin
			final Calendar calFecha2 = new GregorianCalendar();
			calFecha2.setLenient(false);
			calFecha2.set(anio2, mes2, dia2);
			
			// Comprueba si la fecha es correcta, con lenient=false o devuelve una exception para continuar con el siguiente día
			calFecha2.getTime();
			
			while (continuar) {
				
				try {
					
					// Despues se van creando los dias desde el inicio hasta la fecha de fin, y los dias válidos se devuelven en la salida
					final Calendar calFecha1 = new GregorianCalendar();
					calFecha1.setLenient(false);
					calFecha1.set(anioActual, mesActual, diaActual);
					
					// Comprueba si la fecha es correcta, con lenient=false o devuelve una exception para continuar con el siguiente día
					calFecha1.getTime();
					
					salida.add(new int[] {anioActual, mesActual, diaActual});
					
					diaActual++;
				} catch (Exception e) {
					
					/**
					 * Para evitar problemas con las horas, cuando se produce un cambio en la hora de verano, hay que evitar incrementar
					 * los dias usando las funciones de GregorianCalendar o Timestamp, ya que al usar estas funciones, el objeto fecha obtenido no va a tener
					 * siempre la hora en 00 y esto va a dar problemas cuando se realicen calculos con fechas que si tienen la hora a 00.
					 * El ejemplo más normal para ver esto es realizar una diferencia de fechas entre días que estén en horarios diferentes. En este caso,
					 * la diferencia entre un día y el siguiente, no va a ser de 1 día, sino 0 días o 2 días según el caso.
					 */
					diaActual = 1;
					mesActual++;
					if (mesActual > 11) {
						mesActual = 0;
						anioActual++;
					}
				}
				
				continuar = diaActual <= dia2 || mesActual < mes2 || anioActual < anio2;
			}
			
		} catch (Exception e) {
			LOG.debug("Error en fecha2");
		}
		
		return salida;
	}
}
