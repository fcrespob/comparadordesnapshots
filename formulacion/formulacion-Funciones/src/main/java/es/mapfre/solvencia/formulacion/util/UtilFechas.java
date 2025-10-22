package es.mapfre.solvencia.formulacion.util;

/* MODIFICACION : SOLVENCIA II - C�LCULO Y GENERACI�N DE FLUJOS FASE V -  TAR00159044-Modificación_Bisiestos 
 * 
 */
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;

import org.joda.time.LocalDateTime;

/**
 * Clase de utilidades para operar con fechas
 */
public final class UtilFechas {

	public static final SimpleTimeZone STZ = new SimpleTimeZone(0,
			ConstantsFunciones.GMT);
	private static final long MILLIS_IN_DAY = 24 * 60 * 60 * 1000;

	public static class Fecha {
		private LocalDateTime dt;
		private int anio = -1;
		private int mes = -1;
		private int dia = -1;

		public Fecha(LocalDateTime dt) {
			super();
			this.dt = dt;
		}

		public int getAnio() {
			if(this.anio == -1) {
				this.anio = dt.getYear();
			}
			return this.anio;
		}

		public int getMes() {
			if(this.mes == -1) {
				this.mes = dt.getMonthOfYear();
			}
			return this.mes;
		}

		public int getDia() {
			if(this.dia == -1) {
				this.dia = dt.getDayOfMonth();
			}
			return this.dia;
		}

		public void setAnio(int anio) {
			this.anio = anio;
			dt = dt.withYear(anio);
		}

		public void setMes(int mes) {
			this.mes = mes;
			dt = dt.withMonthOfYear(mes);
		}

		public void setDia(int dia) {
			this.dia = dia;
			dt = dt.withDayOfMonth(dia);
		}
		
		public boolean after(Fecha f) {
			return this.getMillis() > f.getMillis();			
		}
		
		private long getMillis() {
			return dt.toDateTime().getMillis();
		}
		
		public Timestamp toTimestamp() {
			return new Timestamp(getMillis());
		}

		public void addDays(int i) {
			dt = dt.plusDays(i);
			resetInternal();
		}
		
		private void resetInternal() {
			anio = -1;
			mes = -1;
			dia = -1;			
		}

	}
	
	
	
	public static class FechaFr{
		private LocalDateTime dt;
		private int anio = -1;
		private int mes = -1;
		private int dia = -1;

		public FechaFr(int dia, int mes, int anio) {
			super();
			this.dia = dia;
			this.mes = mes;
			this.anio = anio;
		}
		
		public FechaFr(LocalDateTime dt){
			this.dt = dt;
		}

		public int getAnio() {
			return this.anio;
		}

		public int getMes() {
			return this.mes;
		}

		public int getDia() {
			return this.dia;
		}
		
		public LocalDateTime getFechaAjustada(){
			return dt;
		}
		
//		public LocalDateTime getFechaAjustada(){
//			LocalDateTime ldt  = new LocalDateTime(1970,1,1,0,0);
//			if (mes == ConstantsFunciones.CTE_2 && (dia == ConstantsFunciones.CTE_29 || dia == ConstantsFunciones.CTE_30 || dia == ConstantsFunciones.CTE_31)){ 
//				if (UtilFechas.getMes(fcalc) != 1){
//					mes = mes + 1;
//				} else {
//					dia = 28;
//				}
//			}
//			
//			ldt = ldt.withDayOfMonth(this.dia);
//			ldt = ldt.withMonthOfYear(this.mes);
//			ldt = ldt.withYear(this.anio);
//			
//			this.dt = ldt;
//			
//			return dt;
//		}
		
		public void setFechaAjustada(int dia, int mes, int anio){
			LocalDateTime ldt  = new LocalDateTime(1970,1,1,0,0);
			ldt = ldt.withDayOfMonth(dia);
			ldt = ldt.withMonthOfYear(mes);
			ldt = ldt.withYear(anio);
			
			this.dt = ldt;
		}

		public void setAnio(int anio) {
			this.anio = anio;
		}

		public void setMes(int mes) {
			this.mes = mes;
		}

		public void setDia(int dia) {
			this.dia = dia;
		}
		
		public boolean after(Timestamp f) {
			int day = UtilFechas.getDia(f);
			int month = UtilFechas.getMes(f);
			int year = UtilFechas.getAnio(f);
			
			String fechaFr = String.valueOf(this.anio).concat(ConstantsFunciones.meses[this.mes-1]).concat(String.valueOf(this.dia));
			String fechaComp = String.valueOf(year).concat(ConstantsFunciones.meses[month-1]).concat(String.valueOf(day));
			
			if (fechaFr.compareTo(fechaComp) > 0){
				return true;
			} else {
				return false;
			}
		}
		
		public Timestamp toTimestamp(){
			Timestamp f = new Timestamp(dt.toDateTime().getMillis());
			
			return f;
		}

	}
	
	
	
	

	/**
	 * Constructor privado.
	 */
	private UtilFechas() {
	}

	/**
	 * Función que devuelve el dia de la fecha insertada por parametro.
	 * 
	 * @param fecha
	 *            fecha
	 * @return dia de la fecha
	 */
	public static int getDia(final Timestamp fecha) {
		int resultado;

		if (null == fecha) {
			resultado = 0;
		} else {
			LocalDateTime dt = new LocalDateTime(fecha);
			resultado = dt.getDayOfMonth();
		}

		return resultado;
	}

	/**
	 * Función que devuelve el mes de la fecha insertada por parametro.
	 * 
	 * @param fecha
	 *            fecha
	 * @return mes de la fecha
	 */
	public static int getMes(final Timestamp fecha) {
		int resultado;

		if (null == fecha) {
			resultado = 0;
		} else {
			LocalDateTime dt = new LocalDateTime(fecha);
			resultado = dt.getMonthOfYear();
		}

		return resultado;
	}

	/**
	 * Función que devuelve el año de la fecha insertada por parametro.
	 * 
	 * @param fecha
	 *            fecha
	 * @return anio de la fecha
	 */
	public static int getAnio(final Timestamp fecha) {
		int resultado;

		if (null == fecha) {
			resultado = 0;
		} else {
			LocalDateTime dt = new LocalDateTime(fecha);
			resultado = dt.getYear();
		}

		return resultado;
	}

	/**
	 * Función que retorna la diferencia en dias entre las 2 fechas.
	 * 
	 * @param fechaInicial
	 *            fechaInicial
	 * @param fechaFinal
	 *            fechaFinal
	 * @return diferencia en dias
	 */
	public static int diferenciasDeFechas(final Timestamp fechaInicial,
			final Timestamp fechaFinal) {

		final GregorianCalendar date1 = new GregorianCalendar();
		final GregorianCalendar date2 = new GregorianCalendar();

		// Para no tener problemas con los horarios de verano
		date1.setTimeZone(UtilFechas.STZ);
		date2.setTimeZone(UtilFechas.STZ);

		date1.setTime(fechaInicial);
		date2.setTime(fechaFinal);

		date1.set(date1.get(Calendar.YEAR), date1.get(Calendar.MONTH),
				date1.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		date2.set(date2.get(Calendar.YEAR), date2.get(Calendar.MONTH),
				date2.get(Calendar.DAY_OF_MONTH), 0, 0, 0);

		final long fechaInicialMs = date1.getTimeInMillis();
		final long fechaFinalMs = date2.getTimeInMillis();
		final long diferencia = fechaFinalMs - fechaInicialMs;
		final double dias = Math.floor(diferencia
				/ ConstantsFunciones.CTE_MILIS_DIA);
		return ((int) dias);
	}

	/**
	 * Función encargada de incrementar un numero determinado de años.
	 * 
	 * @param fecha
	 *            fecha
	 * @param anyos
	 *            anyos
	 * @return fecha con anio incrementado
	 */
	public static Timestamp incrAnyo(final Timestamp fecha, final int anyos) {
		final LocalDateTime dt = new LocalDateTime(fecha.getTime());
		return new Timestamp(dt.plusYears(anyos).toDateTime().getMillis());
	}

	/**
	 * Función encargada de incrementar un numero determinado de meses.
	 * 
	 * @param fecha
	 *            fecha
	 * @param fechaModelo
	 *            fecha a cuyo día hay que ajustarse
	 * @param meses
	 *            meses
	 * @param trasladarAFinalDeMes
	 *            indica si hay que trasladar la fehca a final de mes
	 * @return fecha con meses incrementados
	 */
	public static Timestamp incrMeses(final Timestamp fecha,
			final Timestamp fechaModelo, final int meses,
			final boolean trasladarAFinalDeMes) {
		LocalDateTime dt;
		if(meses != 0) {
			dt = new LocalDateTime(fecha.getTime()).plusMonths(meses);
		} else {
			dt = new LocalDateTime(fecha.getTime());			
		}
		
		if (trasladarAFinalDeMes) {
			dt = dt.dayOfMonth().withMaximumValue();
		} else if (fechaModelo != null) {
			int diaAjuste = new LocalDateTime(fechaModelo.getTime())
					.getDayOfMonth();
			if (diaAjuste > dt.dayOfMonth().getMaximumValue()) {
				dt = dt.dayOfMonth().withMaximumValue();
			} else {
				dt = dt.withDayOfMonth(diaAjuste);
			}
		}

		if (dt.getDayOfMonth() == ConstantsFunciones.CTE_29
				&& dt.getMonthOfYear() == ConstantsFunciones.CTE_2) {
			dt = dt.withDayOfMonth(ConstantsFunciones.CTE_28);
		}

		return new Timestamp(dt.toDateTime().getMillis());
	}
	
	public static Timestamp incrMesesPLANI(final Timestamp fecha,
			final Timestamp fechaModelo, final int meses,
			final boolean trasladarAFinalDeMes) {
		LocalDateTime dt;
		if(meses != 0) {
			dt = new LocalDateTime(fecha.getTime()).plusMonths(meses);
		} else {
			dt = new LocalDateTime(fecha.getTime());			
		}
		
		int diaFechaHasta = new LocalDateTime(fechaModelo.getTime()).getDayOfMonth();
		
		if (trasladarAFinalDeMes) {
			dt = dt.dayOfMonth().withMaximumValue();
		} else {
//			int diaAjuste = new LocalDateTime(fechaModelo.getTime())
//					.getDayOfMonth();
			if (diaFechaHasta > dt.dayOfMonth().getMaximumValue()) {
				dt = dt.dayOfMonth().withMaximumValue();
			}else{
				dt = dt.withDayOfMonth(diaFechaHasta);
			}
//			else {
//				dt = dt.withDayOfMonth(diaAjuste);
//			}
		}

		if ((dt.getDayOfMonth() == ConstantsFunciones.CTE_29 || dt.getDayOfMonth() == ConstantsFunciones.CTE_30 || dt.getDayOfMonth() == ConstantsFunciones.CTE_31) 
				&& dt.getMonthOfYear() == ConstantsFunciones.CTE_2) {
			dt = dt.withDayOfMonth(ConstantsFunciones.CTE_28);
		}

		return new Timestamp(dt.toDateTime().getMillis());
	}
 
																												  
										
				   
				   
															 
		  
										   
   
	
																				   
										  
																																					 
  
														  
										   
		
										 
   

																										 
													   
														 
													 
   

													
  

	/**
	 * Función encargada de incrementar dias a una fecha.
	 * 
	 * @param fecha
	 *            fecha
	 * @param dias
	 *            dias
	 * @return fecha con dias incrementado
	 */
	public static Timestamp incrDias(final Timestamp fecha, final int dias) {
		final LocalDateTime dt = new LocalDateTime(fecha.getTime());
		return new Timestamp(dt.plusDays(dias).toDateTime().getMillis());
	}

	/**
	 * Función encargada de decrementar dias a una fecha.
	 * 
	 * @param fecha
	 *            fecha
	 * @param dias
	 *            dias
	 * @return fecha con dias decrementados
	 */
	public static Timestamp decreDias(final Timestamp fecha, final int dias) {
		final LocalDateTime dt = new LocalDateTime(fecha.getTime());
		return new Timestamp(dt.minusDays(dias).toDateTime().getMillis());
	}
	
	/**
	 * Función encargada de decrementar meses a una fecha.
	 * 
	 * @param fecha
	 *            fecha
	 * @param meses
	 *            meses
	 * @return fecha con meses decrementados
	 */
	public static Timestamp decreMeses(final Timestamp fecha, final int meses) {
		final LocalDateTime dt = new LocalDateTime(fecha.getTime());
		return new Timestamp(dt.minusMonths(meses).toDateTime().getMillis());
	}

	/**
	 * Función encargada de incrementar meses a una fecha.
	 * 
	 * @param fecha
	 *            fecha
	 * @param meses
	 *            meses
	 * @return fecha con meses decrementados
	 */
	public static Timestamp plusMeses(final Timestamp fecha, final int meses) {
		final LocalDateTime dt = new LocalDateTime(fecha.getTime());
		return new Timestamp(dt.plusMonths(meses).toDateTime().getMillis());
	}
	
	/**
	 * Función encargada de decrementar anios a una fecha.
	 * 
	 * @param fecha
	 *            fecha
	 * @param dias
	 *            dias
	 * @return fecha con dias decrementados
	 */
	public static Timestamp decreAnios(final Timestamp fecha, final int anios) {
		final LocalDateTime dt = new LocalDateTime(fecha.getTime());
		return new Timestamp(dt.minusYears(anios).toDateTime().getMillis());
	}

	/**
	 * Función encargada de obtener el el primer dia de un mes.
	 * 
	 * @param fecha
	 *            fecha
	 * @return fecha con el primer dia del mes
	 */
	public static Timestamp getPrimerDiaDelMes(final Timestamp fecha) {
		final LocalDateTime dt = new LocalDateTime(fecha.getTime());
		return new Timestamp(dt.dayOfMonth().withMinimumValue().toDateTime()
				.getMillis());
	}

	/**
	 * Función encargada de obtener el ultimo dia del mes.
	 * 
	 * @param fecha
	 *            fecha
	 * @return fecha con el último dia del mes
	 */
	public static Timestamp getUltimoDiaDelMes(final Timestamp fecha) {
		LocalDateTime dt = new LocalDateTime(fecha.getTime());

		if (dt.dayOfMonth().getMaximumValue() == ConstantsFunciones.CTE_29) {
			dt = dt.withDayOfMonth(ConstantsFunciones.CTE_28);
		} else {
			dt = dt.dayOfMonth().withMaximumValue();
		}
		return new Timestamp(dt.toDateTime().getMillis());
	}

	/**
	 * Función encargada de obtener la fecha que hay en mitad de 2 dadas,
	 * redondeando por exceso.
	 * 
	 * @param fecha1
	 *            fecha1
	 * @param fecha2
	 *            fecha2
	 * @return resultado
	 */
	public static Timestamp obtenerFechaMitad(final Timestamp fecha1,
			final Timestamp fecha2) {
		// Variables locales
		long milisFecha1;
		long milisFecha2;
		long diferenciaMilis;
		//long bisiestosMilis;
		// Fin variables locales

		// obtenemos los milis de las fechas
		milisFecha1 = fecha1.getTime();
		// Incrementamos un día en la segunda fecha para que el último dio
		// también se cuente en el calculo
		milisFecha2 = fecha2.getTime() + MILLIS_IN_DAY;

		// Realizamos el calculo de la fecha que buscamos
		diferenciaMilis = milisFecha2 - milisFecha1;

		/*
		 * El siguiente bloque detecta si la division entre dos devuelve un dia
		 * a las 00:00. Si la hora es 00, se resta un día. En caso contrario, se
		 * resetea la hora a 00, pero no se restan dias.
		 */
		LocalDateTime gcAjustarDia = new LocalDateTime(milisFecha1 + (diferenciaMilis >> 1));

		if (gcAjustarDia.getHourOfDay() == 0) {

			// Si para los rangos de fechas con un numero de dias pares tiene
			// que redondear por arriba,
			// eliminar esta condicion y dejar solo la parte del else que reseta
			// la hora
			gcAjustarDia = gcAjustarDia.plusDays(-1);
		} else {
			gcAjustarDia = gcAjustarDia.withHourOfDay(0);
		}
		
//PYAM0001-TAR00159044-Modificación_Bisiestos-INI
		// Buscamos cuantos bisiestos hay entre las fechas solicitadas
		// para sumarle esos dias a la fecha calculada.
		// 
		// 
		int anio = getAnio(fecha1);
		int mes = getMes(fecha1);
		int anio2 = getAnio(fecha2);
		int mes2 = getMes(fecha2);
		int dia2 =getDia(fecha2);
		int bisiestos = ConstantsFunciones.CTE_0;
		
		if (mes > ConstantsFunciones.CTE_2) {
		 anio = anio + 1;
		    }
		
		if (mes2 < ConstantsFunciones.CTE_2 || (mes2 == ConstantsFunciones.CTE_2 && dia2 <= ConstantsFunciones.CTE_28 ) ) {
			 anio2 = anio2 - 1;
			}
		
		for (int i = anio; i <= anio2 ; i++) {
			if (i % 4==0 && i % 100 != 0 || i % 400 == 0){
				bisiestos = bisiestos  + 1;
				}	
		}
		
		gcAjustarDia = gcAjustarDia.plusDays(bisiestos);
//PYAM0001-TAR00159044-Modificación_Bisiestos-FIN
		
		return new Timestamp(gcAjustarDia.toDateTime().getMillis());
	}

	/**
	 * Función encargada de obtener la mayor fecha entre 2 fechas dadas
	 * 
	 * @param fecha1
	 * @param fecha2
	 * @return Timestamp
	 */
	public static Timestamp obtenerFechaMayor(final Timestamp fecha1,
			final Timestamp fecha2) {
		if (fecha1.after(fecha2)) {
			return fecha1;
		} else {
			return fecha2;
		}
	}

	public static String obtenerStringFechaConFormato(final Timestamp fecha1) {
		String fecha = ConstantsFunciones.CTE_CADENA_VACIA;
		String anio = ConstantsFunciones.CTE_CADENA_VACIA;
		String mes = ConstantsFunciones.CTE_CADENA_VACIA;
		String dia = ConstantsFunciones.CTE_CADENA_VACIA;

		anio = String.valueOf(getAnio(fecha1));
		mes = String.valueOf(getMes(fecha1));
		dia = String.valueOf(getDia(fecha1));

		if (mes.length() == 1) {
			mes = "0" + mes;
		}

		if (dia.length() == 1) {
			dia = "0" + dia;
		}

		fecha = dia + "/" + mes + "/" + anio;

		return fecha;
	}

	public static Fecha getFecha(Timestamp ts) {
		LocalDateTime dt = new LocalDateTime(ts.getTime());
		return new UtilFechas.Fecha(dt);
	}
}
