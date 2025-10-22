package es.mapfre.solvencia.formulacion.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.parametrizacionGeneral.Tab923;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.UtilFechas.Fecha;
import es.mapfre.solvencia.formulacion.util.UtilFechas.FechaFr;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;


/**
 * @author rschacon
 * 
 */
public final class FuncionesAuxiliares {

	/** 
	 * Log funciones auxiliares.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(FuncionesAuxiliares.class);

	/**
	 * Cte de clase.
	 */
	private static final int TEMP_RENTA = 12;

	/**
	 * Constructor privado.
	 */
	private FuncionesAuxiliares() {
		super();
	}

	

	/**
	 * La Funcion tmc calcula el numero de mensualidades completas transcurridas desde la fecha de efecto hasta la fecha de calculo.
	 * 
	 * @param fefecto
	 *            Identifica la fecha de efecto de la poliza
	 * @param fcalc
	 *            Identifica la fecha a la que se efectua el calculo.
	 * @return Identifica el resultado del calculo realizado
	 */
	public static Integer tcm(final Timestamp fefecto, final Timestamp fcalc) {
		// Variables locales
		int numMensualidades = 0;
		int anioTotal;
		int mesTotal;
		// Fin variables locales

		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Inicio de la Funcion << tcm >> de la clase FuncionesAuxiliares, para la entrada fefecto = {} y fcalc = {} ", fefecto, fcalc);
		}

		// INICIO VALIDACION CAMPOS OBLIGATORIOS
		ValidacionesFuncionesAuxiliares.validacionCamposObligatoriosFuncionTCyTMC(fefecto, fcalc);
		// FIN VALIDACION CAMPOS OBLIGATORIOS

		if(fefecto.equals(fcalc)){
			return numMensualidades;
		}

		Fecha fcalcu = UtilFechas.getFecha(fcalc);
		Fecha fefectou = UtilFechas.getFecha(fefecto);

		// anioTotal = (Anio(fcalc) - Anio(fefecto)) * 12
		anioTotal = Util.mult12(fcalcu.getAnio() - fefectou.getAnio());

		// mesTotal = Mes(fcalc) - Mes(fefecto)
		mesTotal = fcalcu.getMes() - fefectou.getMes();

		numMensualidades = anioTotal + mesTotal;

		// Si dia (fefecto) >= dia (fcalc) entonces:
		if ((UtilFechas.getFecha(fefecto).getDia() >= fcalcu.getDia()) && (numMensualidades > 0)) {
			numMensualidades--;
		}

		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Fin de la Funcion << tcm >> de la clase FuncionesAuxiliares, con resultado numMensualidades =  {}", numMensualidades);
		}

		return numMensualidades;
	}

	/**
	 * La Funcion tc calcula el numero de anualidades completas transcurridas desde la fecha de efecto hasta la fecha de calculo.
	 * 
	 * @param fefecto
	 *            Identifica la fecha de efecto de la poliza
	 * @param fcalc
	 *            Identifica la fecha a la que se efectua el calculo.
	 * @return Identifica el resultado del calculo realizado
	 */
	public static Integer tc(final Timestamp fefecto, final Timestamp fcalc) {
		// Variables locales
		int numAnualidades = 0;
		// Fin variables locales
		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Inicio de la Funcion << tc >> de la clase FuncionesAuxiliares, para la entrada fefecto = {} y fcalc = {} ", fefecto, fcalc);
		}

		// INICIO VALIDACION CAMPOS OBLIGATORIOS
		ValidacionesFuncionesAuxiliares.validacionCamposObligatoriosFuncionTCyTMC(fefecto, fcalc);
		// FIN VALIDACION CAMPOS OBLIGATORIOS

		Fecha fcalcu; 
		Fecha fefectou; 

		if(fcalc.after(fefecto)) {
			fcalcu = UtilFechas.getFecha(fcalc);
			fefectou = UtilFechas.getFecha(fefecto);
		} else {
			fcalcu = UtilFechas.getFecha(fefecto);
			fefectou = UtilFechas.getFecha(fcalc);			
		}


		// numAnualidades = Anio(fcalc) - Anio(fefecto)
		numAnualidades = fcalcu.getAnio() - fefectou.getAnio();

		if(numAnualidades > 0) {
			if(fcalcu.getMes() < fefectou.getMes()) {
				numAnualidades--;
			} else if (fcalcu.getMes() == fefectou.getMes()) {
				if(fcalcu.getDia() < fefectou.getDia()) {
					numAnualidades--;
				}
			}
		}

		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Fin de la Funcion << tc >> de la clase FuncionesAuxiliares, con resultado numAnualidades = {} ", numAnualidades);
		}

		return numAnualidades;
	}

	/**
	 * Funcion que calcula la edad del asegurado en la fecha de calculo en base a criterio de edad indicado.
	 * 
	 * @param fcalc
	 *            Identifica la fecha a la cual se efectua el calculo
	 * @param fnac
	 *            Identifica la fecha de nacimiento del asegurado.
	 * @param criterioEdad
	 *            Identifica el criterio de edad que se va a emplear para calcular la edad del asegurado
	 * @return Identifica el resultado del calculo aplicado a las fechas de entrada.
	 */
	public static BigDecimal nEdad(final Timestamp fcalc, final Timestamp fnac, final String criterioEdad, 
			final Timestamp fecIniRenta, final Integer eDifer) {
		// Variables locales
		BigDecimal edadAseg = BigDecimal.ZERO;
		BigDecimal anioTotal = BigDecimal.ZERO;
		BigDecimal mesTotal = BigDecimal.ZERO;
		BigDecimal diaTotal = BigDecimal.ZERO;
		BigDecimal totalAnioMesDia = BigDecimal.ZERO;
		BigDecimal valorRedondeado = BigDecimal.ZERO;
		// Fin variables locales

		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Inicio de la Funcion << nEdad >> de la clase FuncionesAuxiliares, para la entrada fcalc = {}, fnac = {} y criterioEdad = {}", fcalc, fnac, criterioEdad);
		}

		// INICIO VALIDACION CAMPOS OBLIGATORIOS
		ValidacionesFuncionesAuxiliares.validacionCamposObligatoriosFuncionNEdad(fcalc, fnac, criterioEdad, fecIniRenta);
		// FIN VALIDACION CAMPOS OBLIGATORIOS

		// INICIO VALIDACION CAMPO CRITERIO EDAD
		ValidacionesFuncionesAuxiliares.validacionCampoCriterioEdadFuncionNEdad(criterioEdad);
		// FIN VALIDACION CAMPO CRITERIO EDAD

		if (fcalc.before(fnac)) {
			if (FuncionesAuxiliares.LOG.isDebugEnabled()) {
				FuncionesAuxiliares.LOG.debug(Util.errorValidacionA9());
			}			
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A9);
		}

		final Fecha fcalcu;
		if (ConstantsFunciones.CTE_CRI_FECHA_06.equals(criterioEdad)){
			fcalcu = UtilFechas.getFecha(fecIniRenta);
		}else{
			fcalcu= UtilFechas.getFecha(fcalc);
		}
		final Fecha fnacu = UtilFechas.getFecha(fnac);

		// Calculos comunes en ambos casos
		// anioTotal = anio(fcal) - anio(fnac)
		anioTotal =  BigDecimal.valueOf(fcalcu.getAnio() - fnacu.getAnio());

		// mesTotal = (Mes(fcalc) - Mes(fnac)) / 12
		mesTotal =  BigDecimal.valueOf(fcalcu.getMes() - fnacu.getMes()).multiply(ConstantsFunciones.CTE_OPER_1ENTRE12, ConstantsFunciones.MATH_CONTEXT);

		// diaTotal = (Dia(fcalc) - Dia(fnac)) / 365
		diaTotal =  BigDecimal.valueOf(fcalcu.getDia() - fnacu.getDia()).divide(ConstantsFunciones.CTE_OPER_365, ConstantsFunciones.MATH_CONTEXT);

		if (ConstantsFunciones.CTE_CRI_FECHA_02.equals(criterioEdad)) {
			/*
			 * totalAnioMesDia = (anioTotal + mesTotal + diaTotal) * m
			 */
			totalAnioMesDia =  anioTotal.add(mesTotal).add(diaTotal).multiply(BigDecimal.valueOf(FuncionesAuxiliares.TEMP_RENTA));

			// valorRedondeado = redondear(totalAnioMesDia)
			valorRedondeado =  totalAnioMesDia.setScale(0, RoundingMode.HALF_UP);

			// edadAseg = valorRedondeado / m
			edadAseg = valorRedondeado.divide(BigDecimal.valueOf(FuncionesAuxiliares.TEMP_RENTA), ConstantsFunciones.MATH_CONTEXT);

		} else if (ConstantsFunciones.CTE_CRI_FECHA_03.equals(criterioEdad) || ConstantsFunciones.CTE_CRI_FECHA_06.equals(criterioEdad)) {
			// totalAnioMesDia = (anioTotal + mesTotal + diaTotal)
			totalAnioMesDia =  anioTotal.add(mesTotal).add(diaTotal);

			// Redondeamos al numero superior o inferior
			valorRedondeado =  totalAnioMesDia.setScale(0, RoundingMode.HALF_UP);

			// edadAseg = redondear(totalAnioMesDia)
			edadAseg = valorRedondeado;
		} else if (ConstantsFunciones.CTE_CRI_FECHA_05.equals(criterioEdad)){
			edadAseg = nAnnos(fnac, fcalc, ConstantsFunciones.CTE_CRI_FECHA_01);
		}
		
		edadAseg = edadAseg.add(BigDecimal.valueOf(eDifer));

		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Fin de la Funcion << nEdad >> de la clase FuncionesAuxiliares, con resultado edadAseg = {} ",edadAseg);
		}

		return edadAseg;
	}

	/**
	 * La Funcion NANNOS calcula la distancia existente entre dos fechas dadas en base al criterio indicado.
	 * 
	 * @param fecha1
	 *            Identifica la primera fecha sobre la que se efectua el calculo
	 * @param fecha2
	 *            Identifica la segunda fecha sobre la que se efectua el calculo
	 * @param criterioFecha
	 *            Identifica el criterio de fechas para realizar el calculo
	 * @return Identifica el resultado del calculo aplicado a las fechas de entrada
	 */
	public static BigDecimal nAnnos(final Timestamp fecha1, final Timestamp fecha2, final String criterioFecha) {
		// Variables locales
		BigDecimal numeroAnnos = BigDecimal.ZERO;
		int dias1 = 0;
		int dias2 = 0;
		// Fin variables locales

		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Inicio de la Funcion << nAnnos >> de la clase FuncionesAuxiliares, para la entrada fecha1 = {}, fecha2 = {} y criterioFecha = {}", fecha1, fecha2, criterioFecha);
		}

		// INICIO VALIDACION CAMPOS OBLIGATORIOS
		ValidacionesFuncionesAuxiliares.validacionCamposObligatoriosFuncionNANNOS(fecha1, fecha2, criterioFecha);
		// FIN VALIDACION CAMPOS OBLIGATORIOS

		// INICIO VALIDACION CAMPO CRITERIO FECHA
		ValidacionesFuncionesAuxiliares.validacionCampoCriterioFechaFuncionNANNOS(criterioFecha);
		// FIN VALIDACION CAMPO CRITERIO FECHA

		final Fecha fecha1u = UtilFechas.getFecha(fecha1);
		final Fecha fecha2u = UtilFechas.getFecha(fecha2);

		if (ConstantsFunciones.CTE_CRI_FECHA_01.equals(criterioFecha)) {
			//Dias1 = anio(fecha1)*365 + ddenero(fecha1, CriterioFecha) + * dia(fecha1)
			dias1 = fecha1u.getAnio() * ConstantsFunciones.CTE_365 + ConstantsFunciones.ARRAY_BASE_365[fecha1u.getMes() - 1] + fecha1u.getDia();

			//Dias2 = anio(fecha2)*365 + ddenero(fecha2, CriterioFecha) + dia(fecha2)
			dias2 = fecha2u.getAnio() * ConstantsFunciones.CTE_365 + ConstantsFunciones.ARRAY_BASE_365[fecha2u.getMes() - 1] + fecha2u.getDia();

			// numAnnos = (dias2 - dias1) /365 
			//			numeroAnnos = BigDecimal.valueOf(dias2 - dias1).divide(ConstantsFunciones.CTE_OPER_365, ConstantsFunciones.MATH_CONTEXT).abs();
			int diferencia = dias2 - dias1;
			if(diferencia < 0)
				diferencia = -diferencia;
			numeroAnnos = new BigDecimal(String.valueOf(diferencia / 365d)); 
		} else if (ConstantsFunciones.CTE_CRI_FECHA_02.equals(criterioFecha)) {
			//Dias1 = anio(fecha1)*360 + ddenero(fecha1, CriterioFecha) + dia(fecha1)
			dias1 = fecha1u.getAnio() * ConstantsFunciones.CTE_360 + ConstantsFunciones.ARRAY_BASE_360[fecha1u.getMes() - 1] + fecha1u.getDia();

			//Dias2 = anio(fecha2)*360 + ddenero(fecha2, CriterioFecha) + dia(fecha2)
			dias2 = fecha2u.getAnio() * ConstantsFunciones.CTE_360 + ConstantsFunciones.ARRAY_BASE_360[fecha2u.getMes() - 1] + fecha2u.getDia();

			// numAnnos = (dias2 - dias1) /360
			numeroAnnos = BigDecimal.valueOf(dias2 - dias1).divide(ConstantsFunciones.CTE_OPER_360, ConstantsFunciones.MATH_CONTEXT).abs(); 
		} else if (ConstantsFunciones.CTE_CRI_FECHA_03.equals(criterioFecha)) {

			// (Anio(fecha2) - Anio(fecha1))
			final BigDecimal operador1 = BigDecimal.valueOf(fecha2u.getAnio() - fecha1u.getAnio());

			// Si dia(fecha1) <= dia(fecha2), entonces:
			if (fecha1u.getDia() <= fecha2u.getDia()) {

				// ((Mes(fecha2) - Mes(fecha1)) / 12)
				final BigDecimal operador2 = BigDecimal.valueOf(fecha2u.getMes() - fecha1u.getMes()).multiply(ConstantsFunciones.CTE_OPER_1ENTRE12, ConstantsFunciones.MATH_CONTEXT);
				//numeroAnnos = (Anio(fecha2) - Anio(fecha1)) + ((Mes(fecha2) - Mes(fecha1)) / 12)
				numeroAnnos = operador1.add(operador2);

			} else {

				// ((Mes(fecha2) - Mes(fecha1) - 1) / 12)
				final BigDecimal operador2 = BigDecimal.valueOf(fecha2u.getMes() - fecha1u.getMes() - 1).multiply(ConstantsFunciones.CTE_OPER_1ENTRE12, ConstantsFunciones.MATH_CONTEXT);
				// Si dia(fecha1) > dia(fecha2), entonces
				//numeroAnnos = (Anio(fecha2) - Anio(fecha1)) + ((Mes(fecha2) - Mes(fecha1) - 1) / 12)
				numeroAnnos = operador1.add(operador2);
			}
		}

		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Fin de la Funcion << nAnnos >> de la clase FuncionesAuxiliares, con resultado numeroAnnos = {} ", numeroAnnos);
		}

		return numeroAnnos;
	}


	/**
	 * La función ndias calcula la distancia en días existente entre dos fechas dadas en base al criterio indicado.
	 * 
	 * @param fecha1
	 * 				Identifica la primera fecha sobre la que se efectúa el cálculo.
	 * @param fecha2
	 * 				Identifica la segunda fecha sobre la que se efectúa el cálculo.
	 * @param criterioFecha
	 * 				Identifica el criterio de fechas para realizar el cálculo
	 * @return numDias
	 */
	public static Integer nDias(final Timestamp fecha1, final Timestamp fecha2, final String criterioFecha) {
		//Variables locales
		int numDias = 0;
		int dias1 = 0;
		int dias2 = 0;
		//Fin variables locales
		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Inicio de la Funcion << nDias >> de la clase FuncionesAuxiliares, para la entrada fecha1 = {}, fecha2 = {} y criterioFecha = {}",
					fecha1, fecha2, criterioFecha);
		}

		//Realizamos las mismas comprobaciones que para la función nAnnos
		ValidacionesFuncionesAuxiliares.validacionCamposObligatoriosFuncionNANNOS(fecha1, fecha2, criterioFecha);
		ValidacionesFuncionesAuxiliares.validacionCampoCriterioFechaFuncionNANNOS(criterioFecha);

		final Fecha fecha1u = UtilFechas.getFecha(fecha1);
		final Fecha fecha2u = UtilFechas.getFecha(fecha2);

		if (ConstantsFunciones.CTE_CRI_FECHA_01.equals(criterioFecha)) {
			//Dias1 =  año(fecha1)*365 + ddenero(fecha1, CriterioFecha) + dia(fecha1)
			dias1 = fecha1u.getAnio() * ConstantsFunciones.CTE_365 + ConstantsFunciones.ARRAY_BASE_365[fecha1u.getMes() - 1] + fecha1u.getDia();
			//Dias2 =  año(fecha2)*365 + ddenero(fecha2, CriterioFecha) + dia(fecha1)
			dias2 = fecha2u.getAnio() * ConstantsFunciones.CTE_365 + ConstantsFunciones.ARRAY_BASE_365[fecha2u.getMes() - 1] + fecha2u.getDia();
			//numDias (fecha1,fecha2) =Valor Absoluto [ dias2 - dias1]
			numDias = Math.abs(dias2 - dias1);
		} else if (ConstantsFunciones.CTE_CRI_FECHA_02 .equals(criterioFecha)) {
			//Dias1 =  año(fecha1)*360 + ddenero(fecha1, CriterioFecha) + dia(fecha1)
			dias1 = fecha1u.getAnio() * ConstantsFunciones.CTE_360 + ConstantsFunciones.ARRAY_BASE_360[fecha1u.getMes() - 1] + fecha1u.getDia();
			//Dias2 =  año(fecha2)*360 + ddenero(fecha2, CriterioFecha) + dia(fecha2)
			dias2 = fecha2u.getAnio() * ConstantsFunciones.CTE_360 + ConstantsFunciones.ARRAY_BASE_360[fecha2u.getMes() - 1] + fecha2u.getDia();
			//numDias (fecha1,fecha2) =Valor Absoluto [ dias2 - dias1]
			numDias = Math.abs(dias2 - dias1);
		}

		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Fin de la Funcion << nDias >> de la clase FuncionesAuxiliares, con resultado numDias = {} ", numDias);
		}

		return numDias;
	}

	/**
	 * Función que calcula una fecha de referencia en base a la fecha de cálculo y el tipo de referencia 
	 * que indican los parámetros de entrada. 
	 * @param fecVcto Identifica la fecha de vencimiento.
	 * @param fcalc Identifica la fecha de cálculo.
	 * @return salida Fecha de referencia.
	 */
	public static FechaFr fr(final Timestamp fecVcto, final Timestamp fcalc){
		//Variables locales
		int diaFecVcto;
		int diaFcalc;
		int diaRef;
		int diaRefA;
		int mesRef;
		int mesRefA;
		int anioRef;
		FechaFr salida = new FechaFr(0,0,0); 
		//Fin variables

		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Inicio Funcion << fr>> de la clase FuncionesAuxiliares, para la entrada fecVcto = {}, fcalc = {}",
					fecVcto, fcalc);
		}
		//Se validan los parámetros de entrada
		ValidacionesFuncionesAuxiliares.validarParamEntradaFuncionFR(fecVcto, fcalc);

		diaFecVcto = UtilFechas.getDia(fecVcto);
		diaFcalc = UtilFechas.getDia(fcalc);
		
		//Comprobación realizada para evitar el descuadre en el que fechaDesde = 28/02
		if (diaFcalc == ConstantsFunciones.CTE_28 && UtilFechas.getMes(fcalc) == ConstantsFunciones.CTE_2){
			diaFcalc = diaFecVcto;
		}

		diaRef = diaFecVcto;
		anioRef = UtilFechas.getAnio(fcalc);
		if (diaFecVcto > diaFcalc ){
			mesRef = UtilFechas.getMes(fcalc);
		} else {
			mesRef = UtilFechas.getMes(fcalc) +1;
		}
		
		if (mesRef > 12){
			mesRef = mesRef - 12;
			anioRef = anioRef + 1;
		}
		
		mesRefA = mesRef;
		diaRefA = diaRef;
		if (mesRef == ConstantsFunciones.CTE_2 && (diaRef == ConstantsFunciones.CTE_29 || diaRef == ConstantsFunciones.CTE_30 || diaRef == ConstantsFunciones.CTE_31)){ 
			if (UtilFechas.getMes(fcalc) != 1){
				mesRefA = mesRef + 1;
			} else {
				diaRef = ConstantsFunciones.CTE_28;
				diaRefA = diaRef;
			}
		}

		//Se devuelve el dia, mes y año de la fecha de referencia calculada
		salida.setMes(mesRef);
		salida.setDia(diaRef);
		salida.setAnio(anioRef);
		salida.setFechaAjustada(diaRefA, mesRefA, anioRef);
		
		
		if (salida.after(fecVcto)){
			salida.setMes(UtilFechas.getMes(fecVcto));
			salida.setDia(diaFecVcto);
			salida.setAnio(UtilFechas.getAnio(fecVcto));
			salida.setFechaAjustada(diaFecVcto, UtilFechas.getMes(fecVcto), UtilFechas.getAnio(fecVcto));
		}
		

		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Fin Funcion << fr >> de la clase FuncionesAuxiliares, con resultado dia = {}, mes = {}, año = {}", salida.getDia(), salida.getMes(), salida.getAnio());
		}

		return salida;
	}

	/**
	 * Función auxiliar para el cálculo de los módulos BXUL010, BXUL024 y GZC024.
	 * @param fecEfecto Identifica la fecha de efecto de la umic.
	 * @param fecVcto Identifica la fecha de vencimiento de la umic.
	 * @param fcalc Identifica la fecha de cálculo.
	 * @param tipo Tipo de cálculo: 
	 *		-	1: AlfaT
	 *		-	2: Alfa2
	 * @return alfa2T Fracción de mes 
	 */
	public static Integer alfa2T(final Timestamp fecEfecto, final Timestamp fecVcto, final Timestamp fcalc, 
			final Integer tipo){
		//Variables locales
		Integer alfa2T = 0;
		FechaFr varFR;
		int varD;
		int varDc;
		int varMes;
		int varAnio;
		//Fin variables locales

		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Inicio Funcion <<alfa2T>> de la clase FuncionesAuxiliares, para la entrada fecEfecto = {}, fecVcto = {}, fcalc = {}, tipo = {}",
					fecEfecto, fecVcto, fcalc, tipo);
		}

		//Se valida que los parámetros de entrada sean distintos a null y que el valor de tipo sea 1 o 2
		ValidacionesFuncionesAuxiliares.validarParamEntradaFuncionAlfaT2(fecEfecto, fecVcto, fcalc, tipo);

		//Si tipo es 1, alfaT--> se toma como fecha de cálculo la fecha de efecto de la umic.
		if (tipo == 1){ //Si tipo es alfaT
			varFR = fr(fecVcto, fecEfecto);
			varD = UtilFechas.getDia(UtilFechas.getUltimoDiaDelMes(fecEfecto));
			varDc = UtilFechas.getDia(fecEfecto);
			varMes = UtilFechas.getMes(fecEfecto);
			varAnio = UtilFechas.getAnio(fecEfecto);
		} else { //Si tipo es 2, alfa2--> se toma como fecha de cálculo la fecha de cierre.
			varFR = fr(fecVcto, fcalc);
			varD = UtilFechas.getDia(UtilFechas.getUltimoDiaDelMes(fcalc));
			varDc = UtilFechas.getDia(fcalc);
			varMes = UtilFechas.getMes(fcalc);
			varAnio = UtilFechas.getAnio(fcalc);
		}
	
		
		String fec = String.valueOf(varAnio).concat(ConstantsFunciones.meses[varMes-1]);
		String fecRef = String.valueOf(varFR.getAnio()).concat(ConstantsFunciones.meses[varFR.getMes()-1]);
		
		if (fec.compareTo(fecRef)<0){
			alfa2T = (varD - varDc) + varFR.getDia();
		} else {
			alfa2T = varFR.getDia() - varDc;
		}

		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Fin Funcion << alfa2T >> de la clase FuncionesAuxiliares, con resultado alfa2T = {}", alfa2T);
		}

		
		return alfa2T;
	}

	/**
	 * Función auxiliar para el cálculo de los módulos BXUL010 y BXUL024.
	 * @param x Edad actuarial a fecha efecto técnico.
	 * @param lstValoresTabMort Valores de la tabla de mortalidad.
	 * @param N Duración del seguro (desde fecha efecto técnico (en años)).
	 * @param I1 Tipo de interés.
	 * @param gipcPrima Gastos de gestión interna sobre Prima* Prima inicial del seguro.
	 * @param tcm Meses completos transcurridos desde efecto hasta cálculo provisión.
	 * @param alfat 
	 * @param alfat30 Factor = (alfat/30).
	 * @param factorAlfat Factor = [(1+I1)]^(-(Alfat/365))  
	 * @param alfa2
	 * @param alfa230   Factor = (alfa2/30)
	 * @param factorAlfa2 Factor = [(1+I1)]^(-(Alfa2/365))
	 * @param vrta Renta actuarial pospagable desplazada 0 meses.
	 * @return bgmornlgip
	 */
	public static BigDecimal bgmornlgip (final Integer x, final List<BigDecimal> lstValoresTabMort,  
			final Integer n, final BigDecimal i1, final BigDecimal gipcPrima, final Integer tcm, 
			final Integer alfat, final BigDecimal alfat30, final BigDecimal factorAlfat, final Integer alfa2, 
			final BigDecimal alfa230, final BigDecimal factorAlfa2, final BigDecimal vrta, final Integer nmeses){
		//Variables locales
		BigDecimal bgmornlgip = BigDecimal.ZERO;
		BigDecimal varVrta1;
		BigDecimal numerador;
		BigDecimal denominador;
		BigDecimal varPp = BigDecimal.ONE;
		//Fin variables locales

		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Inicio Funcion <<alfa2T>> de la clase FuncionesAuxiliares, para la entrada x = {}, lstValoresTabMort = {}, N = {}, "
					+ "I1 = {}, gipcPrima = {}, tcm = {}, alfat = {}, alfat30 = {}, factorAlfat = {}, alfa2 = {}, alfa230 = {}, factorAlfa2 = {}, vrta = {}",
					x, lstValoresTabMort, n, i1, gipcPrima, tcm, alfat, alfat30, factorAlfat, alfa2, alfa230, factorAlfa2, vrta);
		}

		//Validaciones parámetros obligatorios de entrada
		ValidacionesFuncionesAuxiliares.validarParamEntradaFuncionBgmornlgipParte1(x, lstValoresTabMort, n, i1, gipcPrima, tcm);
		ValidacionesFuncionesAuxiliares.validarParamEntradaFuncionBgmornlgipParte2(alfat, alfat30, factorAlfat, alfa2, alfa230, factorAlfa2, vrta);

		varVrta1 = FuncionesRentas.vrta(tcm+1, 0, n, i1, 99, i1, x, lstValoresTabMort, 12, BigDecimal.ZERO, true, nmeses);
		
		if (tcm>= ((n*12) + nmeses -1)){
			varPp = BigDecimal.ZERO;
		}

		//Se realiza el cálculo de la función bgmornlgip = gipcPrima * [(alfa230+(factorAlfa2*varVrta1))/(alfat30+(factorAlfat* vrta))]
		numerador = alfa230.add(factorAlfa2.multiply(varPp.add(varVrta1)));
		denominador = alfat30.add(factorAlfat.multiply(BigDecimal.ONE.add(vrta)));
		bgmornlgip = 	gipcPrima.multiply(numerador.divide(denominador, ConstantsFunciones.MATH_CONTEXT));
		
		//LOG.warn(alfa230+";"+factorAlfa2+";"+varPp+";"+varVrta1+";"+alfat30+";"+factorAlfat+";"+vrta+";"+numerador+";"+denominador+";"+bgmornlgip);

		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Fin Funcion << bgmornlgip >> de la clase FuncionesAuxiliares, con resultado bgmornlgip = {}", bgmornlgip);
		}

		return bgmornlgip;
	}

	/**
	 * Funnción auxiliar para el cálculo del módulo BXUL010.
	 * @param x Edad actuarial a fecha efecto técnico.
	 * @param lstValoresTabMort Valores de la tabla de mortalidad (probabilidad Vida)
	 * @param valoresQx Valores de la tabla de mortalidad (probabilidad fallecimiento)
	 * @param N Duración del seguro (en años)
	 * @param I1 Tipo de interés
	 * @param tcm Meses completos transcurridos desde efecto hasta cálculo provisión
	 * @param capital Capital Actual de cartera
	 * @param alfa2
	 * @param alfa230 Factor = (alfa2/30)
	 * @param factorAlfa2 Factor = [(1+I1/100)] ^(-(Alfa2/365))  
	 * @param p Periodicidad del cálculo
	 * @param iteracion
	 * @param mapVariables
	 * @return
	 */
	public static BigDecimal bgmornl(final Integer x, final List<BigDecimal> lstValoresTabMort, final List<BigDecimal> valoresQx,
			final Integer n, final BigDecimal i1, final Integer tcm, final BigDecimal capital, final Integer alfa2, final BigDecimal alfa230,
			final BigDecimal factorAlfa2, final Integer p, final Map<String, Object> mapVariables){
		//Variables locales
		BigDecimal bgmornl= BigDecimal.ZERO;
		BigDecimal varLxtcmp;
		BigDecimal varQxtcm;
		BigDecimal varLxjp;
		BigDecimal varQxjp;
		BigDecimal varQentjp = BigDecimal.ZERO;
		BigDecimal varSumatorio = BigDecimal.ZERO;
		//Fin variables locales

		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Inicio Funcion <<alfa2T>> de la clase FuncionesAuxiliares, para la entrada x = {}, lstValoresTabMort = {}, valoresQx = {},"
					+ " N = {}, I1 = {}, gipcPrima = {}, tcm = {}, capital = {}, alfa2 = {}, alfa230 = {}, factorAlfa2 = {}, p = {}",
					x, lstValoresTabMort, valoresQx, n, i1, tcm, capital, alfa2, alfa230, factorAlfa2, p);
		}

		//Validaciones parámetros obligatorios de entrada
		ValidacionesFuncionesAuxiliares.validarParamEntradaFuncionBgmornl(x, lstValoresTabMort, n, i1, tcm, alfa2, alfa230, factorAlfa2, p);

		//Calculo y almacenamiento de 1/p y 1+i1/100 para optimización
		BigDecimal unoEntreP = (BigDecimal) mapVariables.get("unoEntreP"); 
		if (unoEntreP == null){
			if (p == ConstantsFunciones.CTE_12){
				unoEntreP = ConstantsFunciones.CTE_OPER_1ENTRE12;
			}else{
				unoEntreP = BigDecimal.ONE.divide(BigDecimal.valueOf(p), ConstantsFunciones.MATH_CONTEXT);
			}
			mapVariables.put("unoEntreP", unoEntreP);
		}

		BigDecimal unoMasI1Entre100 = (BigDecimal) mapVariables.get("unoMasI1Entre100");
		if (unoMasI1Entre100 == null){
			unoMasI1Entre100 = (i1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)).add(BigDecimal.ONE );
			mapVariables.put("unoMasI1Entre100", unoMasI1Entre100);
		}

		//Para optimizar se multiplica por 1/p. Si tcm es múltiplo de p, se realiza la división, para obtener la precisión correcta.
		BigDecimal tcmEntreP;
		if (tcm%p==0){
			tcmEntreP = BigDecimal.valueOf(tcm).divide(BigDecimal.valueOf(p), ConstantsFunciones.MATH_CONTEXT);
		}else{
			tcmEntreP = BigDecimal.valueOf(tcm).multiply(unoEntreP);
		}
		 
		BigDecimal xMasTcmEntreP = BigDecimal.valueOf(x).add(tcmEntreP);
		varLxtcmp = Util.getVarLx(xMasTcmEntreP, lstValoresTabMort);

		varQxtcm = valoresQx.get(tcmEntreP.intValue() + x);
		BigDecimal pot = Util.pow(BigDecimal.ONE.subtract(varQxtcm), unoEntreP);
		varQxtcm = BigDecimal.ONE.subtract(pot);

		BigDecimal jEntreP;
		Integer jEntrePEntero = -1;
		BigDecimal potencia = BigDecimal.ZERO;
		//Sumatorio {desde j=tcm+1 hasta j=n*12} (1+ i1/100)^((-(j-(tcm+1)+0,5))/p) * varQxjp 
		for(int j = tcm+1; j<=n*12;j++){
			//Para optimizar se multiplica por 1/p. Si j es múltiplo de p, se realiza la división, para obtener la precisión correcta.
			if (j%p == 0){
				jEntreP = BigDecimal.valueOf(j).divide(BigDecimal.valueOf(p),ConstantsFunciones.MATH_CONTEXT);
			}else{
				jEntreP = BigDecimal.valueOf(j).multiply(unoEntreP);
			}
			BigDecimal xMasJEntreP = BigDecimal.valueOf(x).add(jEntreP);
			varLxjp = Util.getVarLx(xMasJEntreP, lstValoresTabMort);

			//Si el entero de jEntreP no cambia, los valores de varQentjp y potencia se mantienen constantes
			if (!jEntrePEntero.equals(jEntreP.intValue())){
				varQentjp = valoresQx.get(jEntreP.intValue() + x);

				BigDecimal unoMenosVarQ = BigDecimal.ONE.subtract(varQentjp);
				potencia = Util.pow(unoMenosVarQ, unoEntreP);

				jEntrePEntero = jEntreP.intValue();
			}

			BigDecimal lxkjpEntreLxtcmp = varLxjp.divide(varLxtcmp, ConstantsFunciones.MATH_CONTEXT);

			//Se calcula varQxjp =  [1- 〖(1-varQentjp)〗^(1/p) ]*  (varLxjp )/(varLxtcmp )
			varQxjp = (BigDecimal.ONE.subtract(potencia)).multiply(lxkjpEntreLxtcmp);

			BigDecimal elevado = BigDecimal.valueOf(-(j-(tcm+1)+0.5)).multiply(unoEntreP);
			BigDecimal primerOperando = Util.pow(unoMasI1Entre100, elevado);

			varSumatorio = varSumatorio.add(primerOperando.multiply(varQxjp));
			
		}

		//Se realiza el cálculo de la función bgmornl = (capital * alfa230 * factorAlfa2^0,5  * varQxtcm) + (capital * factorAlfa2 * varSumatorio)
		BigDecimal potFactor = Util.pow(factorAlfa2, 0.5);
		BigDecimal primerSumando = capital.multiply(alfa230).multiply(potFactor).multiply(varQxtcm);
		BigDecimal segundoSumando = capital.multiply(factorAlfa2).multiply(varSumatorio);

		bgmornl = primerSumando.add(segundoSumando);
				
		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Fin Funcion << bgmornl >> de la clase FuncionesAuxiliares, con resultado bgmornl = {}", bgmornl);
		}

		return bgmornl;
	}
	
			
	/**
	 * Función que calcula el número de personas vivas a una edad no entera (punto intermedio entre dos aniversarios de seguro), 
	 * calculadas como interpolación entre los valores obtenidos de la tabla de mortalidad para la edad inmediatamente anterior 
	 * y la inmediatamente posterior. 
	 * @param x Edad a fecha de cálculo.
	 * @param t Anualidad en curso.
	 * @param beta Número de meses trascurridos desde el cierre hasta el momento de la proyección.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param w Edad Máxima de la tabla de mortalidad.
	 * 
	 * @return lfrac
	 */
	public static BigDecimal lfrac(final Integer x, final Integer t, final BigDecimal beta, final List<BigDecimal> valoresTabMort,
			final Integer w){
		//Inicio variables locales
		BigDecimal lfrac = BigDecimal.ZERO;
		Integer varXT;
		Integer varXT1;
		BigDecimal varLxT = BigDecimal.ZERO;
		BigDecimal varLxT1 = BigDecimal.ZERO;
		//Fin variables locales
		
		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Inicio Funcion << Lfrac >> de la clase FuncionesAuxiliares, para la entrada  x = {}, t = {}, beta = {}, valoresTabMort = {}, w = {}", 
					x, t, beta, valoresTabMort, w);
		}
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesAuxiliares.validarParamEntradaFuncionLfrac(x, t, beta, valoresTabMort, w);
		
		varXT = x+t;
		varXT1 = x+t+1;
		
		if (varXT<w){
			varLxT = valoresTabMort.get(varXT);
		}
		
		if (beta.signum() == 0){
			lfrac = varLxT;
		} else {
		
			if (varXT1<w){
				varLxT1 = valoresTabMort.get(varXT1);
			}
			
			//Se calcula LFRAC como: LFRAC = varLxT + beta * (varLxT1 – varLxT)
			lfrac = varLxT.add(beta.multiply(varLxT1.subtract(varLxT)));
		}
		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Fin Funcion << Lfrac >> de la clase FuncionesAuxiliares, con resultado lfrac = {} ", lfrac);
		}
		
		return lfrac;
	}
						
	
	/**
	 * Función B_VIDA
	 * @param x Edad actuarial a fecha efecto técnico.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param n Duración del seguro desde fecha efecto técnico (en años).
	 * @param i1 Tipo de interés.
	 * @param tcm Meses completos transcurridos desde efecto hasta cálculo provisión.
	 * @param alfat
	 * @param alfa2
	 * 
	 * @return bvida
	 */
	public static BigDecimal bvida(final Integer x, final List<BigDecimal> valoresTabMort, final Integer n, final BigDecimal i1,
			final Integer tcm, final Integer alfat, final Integer alfa2, final BigDecimal icapini){
		//Inicio variables locales
		BigDecimal bvida = BigDecimal.ZERO;
		BigDecimal varXn;
		BigDecimal varLXn;
		BigDecimal varXtcm;
		BigDecimal varLXtcm;
		//Fin variables locales
		
		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Inicio Funcion << B_VIDA >> de la clase FuncionesAuxiliares, para la entrada  x = {}, valoresTabMort = {},"
					+" n = {}, i1 = {}, tcm = {}, alfat = {}, alfa2 = {}", 
					x, valoresTabMort, n, i1, tcm, alfat, alfa2);
		}
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesAuxiliares.validarParamEntradaFuncionBvida(x, valoresTabMort, n, i1, tcm, alfat, alfa2, icapini);
		
		BigDecimal tcmEntre12 = (tcm % 12 == 0) ? BigDecimal.valueOf(tcm/12) : BigDecimal.valueOf(tcm).multiply(ConstantsFunciones.CTE_OPER_1ENTRE12);
		BigDecimal alfa2Entre365 = (alfa2 % 365 == 0) ? BigDecimal.valueOf(alfa2/365) : BigDecimal.valueOf(alfa2).multiply(ConstantsFunciones.CTE_OPER_1_PARTIDO_365);
		
		varXn = BigDecimal.valueOf(x + n).add(BigDecimal.valueOf(alfat).multiply(ConstantsFunciones.CTE_OPER_1_PARTIDO_365));
		varLXn = Util.getVarLx(varXn, valoresTabMort);
		
		varXtcm = BigDecimal.valueOf(x).add(tcmEntre12).add(alfa2Entre365);
		varLXtcm = Util.getVarLx(varXtcm, valoresTabMort);
		
		/**
		 * Se calcula B_VIDA como:
		 * B_VIDA = (varLXn/(varLXtcm ))* 〖(1+ i1/100)〗^(-(N-(tcm/12)+(alfa2/365  )))
		 */
		
		BigDecimal elevado = BigDecimal.valueOf(n).subtract(tcmEntre12).add(alfa2Entre365);
		BigDecimal division = varLXn.divide(varLXtcm, ConstantsFunciones.MATH_CONTEXT);
		BigDecimal unoMasI1Entre100 = BigDecimal.ONE.add(i1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		
		bvida = (division.multiply(Util.pow(unoMasI1Entre100, elevado.negate()))).multiply(icapini);
		
		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Fin Funcion << B_VIDA >> de la clase FuncionesAuxiliares, con resultado bvida = {} ", bvida);
		}
		
		return bvida;
	}
	
	/** 
	 * Función auxiliar que calcula la cuota unitaria en el periodo j de proyección
	 * @param j
	 * @param nc
	 * @param Pc
	 * @param IF
	 * @param delta
	 * @param Cp0
	 * @param na
	 * @return cuotaj
	 */
	
	
	public static BigDecimal cuota(final Integer j, final Integer nc, final Integer pc, final BigDecimal If, final BigDecimal delta, final BigDecimal cp0, final Integer na, final BigDecimal varRES)
	{
		//Inicio variables locales
		BigDecimal cuotaj = BigDecimal.ZERO;
		//Fin variables locales
		
		//Validadcion parametros de entrada
		ValidacionesFuncionesAuxiliares.validarParamEntradaFuncionCuota(j,nc,pc,If,delta,cp0,na);
		//Fin validacion de parametros de entrada

		if(j<=nc){
			cuotaj = ((If.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)).divide(BigDecimal.valueOf(pc), ConstantsFunciones.MATH_CONTEXT)).multiply(cp0);
			
		}
		else if(j==nc+1 || j<=nc+na)
		{
			
			cuotaj = ((BigDecimal.ONE.subtract(varRES)).divide(delta,ConstantsFunciones.MATH_CONTEXT)).multiply(cp0);
			
		}
		
		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Fin Funcion << Cuota >> de la clase FuncionesAuxiliares, con resultado Cuota = {} ", cuotaj);
		}
		
		return cuotaj;	
	}
	
	/**
	 * Función BGBORNL_FAC
	 * @param x Edad actuarial a fecha efecto técnico.
	 * @param valoresIx Valores de la tabla de mortalidad (probabilidad de invalidez).
	 * @param n Duración del seguro (en años).
	 * @param i1 Tipo de interés.
	 * @param tcm Meses completos transcurridos desde efecto hasta cálculo provisión.
	 * @param capital Capital Actual de cartera.
	 * @param alfa2
	 * @param alfa230 Factor = (alfa2/30).
	 * @param factorAlfa2 Factor = 〖(1+I1/100) 〗^(-(Alfa2/365)).
	 * @param p Periodicidad del cálculo.
	 * 
	 * @return bgmornlFac
	 */
	public static BigDecimal bgmornlFac(final Integer x, final List<BigDecimal> valoresIx, final Integer n, final BigDecimal i1,
			final Integer tcm, final BigDecimal capital, final Integer alfa2, final BigDecimal alfa230, 
			final BigDecimal factorAlfa2, final Integer p, final List<BigDecimal> valoresTabMort){
		//Inicio variables locales
		BigDecimal bgmornlFac = BigDecimal.ZERO;
		BigDecimal varIxTcmp;
		BigDecimal varLxTcmp;
		BigDecimal varSumatorio = BigDecimal.ZERO;
		BigDecimal varIxjp;
		BigDecimal varLxjp;
		BigDecimal xtcmp;
		BigDecimal xjp;
		BigDecimal termino = BigDecimal.ZERO;
		//Fin variables locales	
		
		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Inicio Funcion << BGMORNL_FAC >> de la clase FuncionesAuxiliares, para la entrada  x = {}, valoresLx = {}, n = {},"
					+" i1 = {}, tcm = {}, capital = {}, alfa2 = {}, alfa230 = {}, factorAlfa2 = {}, p = {}", 
					x, valoresIx, n, i1, tcm, capital, alfa2, alfa230, factorAlfa2, p);
		}
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesAuxiliares.validarParamEntradaFuncionBgmornlFacParte1(x, valoresIx, n, i1, tcm, capital);
		ValidacionesFuncionesAuxiliares.validarParamEntradaFuncionBgmornlFacParte2(alfa2, alfa230, factorAlfa2, p);
		
		//Variables necesarias para el cálculo
		BigDecimal xBD = BigDecimal.valueOf(x);
		BigDecimal unoEntreP = BigDecimal.ONE.divide(BigDecimal.valueOf(p), ConstantsFunciones.MATH_CONTEXT);
		BigDecimal tcmEntreP = (tcm%p == 0) ? BigDecimal.valueOf(tcm/p) : BigDecimal.valueOf(tcm).multiply(unoEntreP);
		
		xtcmp = xBD.add(tcmEntreP);
		varIxTcmp = (Util.getVarLx(xtcmp, valoresIx)).multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_001);
		varIxTcmp = BigDecimal.ONE.subtract(Util.pow(BigDecimal.ONE.subtract(varIxTcmp), unoEntreP));
		
		varLxTcmp = Util.getVarLx(xtcmp, valoresTabMort);
		
		BigDecimal unoMasI1Entre100 = BigDecimal.ONE.add(i1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		BigDecimal menos0Punto5 = BigDecimal.valueOf(0.5).negate();
		BigDecimal pBD = BigDecimal.valueOf(p);
		//Se calcula la potencia en la primera iteracion del bucle y la potencia por la que hay que multiplicar en las
		// sucesivas iteraciones para optimizar operaciones dentro del bucle
		BigDecimal potenciaIni = Util.pow(unoMasI1Entre100, menos0Punto5.divide(pBD, ConstantsFunciones.MATH_CONTEXT));
		BigDecimal potenciaSum = Util.pow(unoMasI1Entre100, (BigDecimal.ONE.negate()).divide(pBD, ConstantsFunciones.MATH_CONTEXT));
		
		//SUMATORIO (desde j=tcm+1 hasta n*12):〖(1+ i1/100)〗^((-(j-(tcm+1)+0,5))/p) * varIxjp^p 
		for (int j=tcm+1; j<=n*12; j++){
			BigDecimal jEntreP = (j%p == 0) ? BigDecimal.valueOf(j/p) : BigDecimal.valueOf(j).multiply(unoEntreP);
			xjp = xBD.add(jEntreP);
			varIxjp = (Util.getVarLx(xjp, valoresIx)).multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_001);
			varIxjp = BigDecimal.ONE.subtract(Util.pow(BigDecimal.ONE.subtract(varIxjp), unoEntreP));
			
			varLxjp = Util.getVarLx(xjp, valoresTabMort);
			
			if (j == tcm+1){
				termino = potenciaIni;
			} else {
				termino = termino.multiply(potenciaSum, ConstantsFunciones.MATH_CONTEXT);
			}

			BigDecimal division = varLxjp.divide(varLxTcmp, ConstantsFunciones.MATH_CONTEXT);
			varSumatorio = varSumatorio.add(termino.multiply(varIxjp).multiply(division));
			
		}
		
		/**
		 * Se calcula bgmornlfac como:
		 * bgmornlfac =  (capital * alfa230 * 〖factorAlfa2〗^0,5  * varIxtcmp) + (capital * factorAlfa2 * varSumatorio)
		 */
		BigDecimal primerOperando = capital.multiply(alfa230).multiply(Util.pow(factorAlfa2, -0.5)).multiply(varIxTcmp);
		BigDecimal segundoOperando = capital.multiply(factorAlfa2, ConstantsFunciones.MATH_CONTEXT).multiply(varSumatorio, ConstantsFunciones.MATH_CONTEXT);
		
		bgmornlFac = primerOperando.add(segundoOperando);
		
		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Fin Funcion << BGMORNL_FAC >> de la clase FuncionesAuxiliares, con resultado bgmornlFac = {} ", bgmornlFac);
		}
		
		return bgmornlFac;
	}
	
	/**
	 * La Funcion tmcIni calcula el numero de mensualidades completas transcurridas desde la fecha de efecto hasta la fecha de calculo.
	 * 
	 * @param fefecto
	 *            Identifica la fecha de efecto de la poliza
	 * @param fcalc
	 *            Identifica la fecha a la que se efectua el calculo.
	 * @return Identifica el resultado del calculo realizado
	 */
	public static Integer tcmIni(final Timestamp fefecto, final Timestamp fcalc) {
		// Variables locales
		int numMensualidades = 0;
		int anioTotal;
		int mesTotal;
		// Fin variables locales

		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Inicio de la Funcion << tcm >> de la clase FuncionesAuxiliares, para la entrada fefecto = {} y fcalc = {} ", fefecto, fcalc);
		}

		// INICIO VALIDACION CAMPOS OBLIGATORIOS
		ValidacionesFuncionesAuxiliares.validacionCamposObligatoriosFuncionTCyTMC(fefecto, fcalc);
		// FIN VALIDACION CAMPOS OBLIGATORIOS

		if(fefecto.equals(fcalc)){
			return numMensualidades;
		}

		Fecha fcalcu = UtilFechas.getFecha(fcalc);
		Fecha fefectou = UtilFechas.getFecha(fefecto);

		// anioTotal = (Anio(fcalc) - Anio(fefecto)) * 12
		anioTotal = Util.mult12(fcalcu.getAnio() - fefectou.getAnio());

		// mesTotal = Mes(fcalc) - Mes(fefecto)
		mesTotal = fcalcu.getMes() - fefectou.getMes();

		numMensualidades = anioTotal + mesTotal;

		// Si dia (fefecto) > dia (fcalc) entonces:
		if (UtilFechas.getFecha(fefecto).getDia() > fcalcu.getDia()) {
			numMensualidades--;
		}

		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Fin de la Funcion << tcmIni >> de la clase FuncionesAuxiliares, con resultado numMensualidades =  {}", numMensualidades);
		}

		return numMensualidades;
	}
	
	/**
	 * Función que calcula la edad, en función del criterio de edad establecido, a partir del cual se genera el derecho a 
	 * la prestación.
	 * @param fnac Fecha de Nacimiento
	 * @param poliza Póliza
	 * @param subpoliza Subpóliza
	 * @param certificado Certificado
	 * @param nsuscripción suscripcion
	 * @param criterioFecha 
	 * @return
	 */
	public static BigDecimal edadCob(final Timestamp fnac, final Long poliza, final Integer subpoliza, final Integer certificado, final Integer nsuscripcion,
			final String criterioFecha){
		//Inicio variables locales
		BigDecimal edadCob = BigDecimal.ZERO;
		Timestamp varFdiferimiento;
		//Fin variables locales
		
		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Inicio Funcion << EDADCOB >> de la clase FuncionesAuxiliares, para la entrada  fnac = {}, poliza = {},"
					+" subpoliza = {}, certificado = {}, criterioFecha = {}, nsuscripcion = {}", 
					fnac, poliza, subpoliza, certificado, criterioFecha, nsuscripcion);
		}
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesAuxiliares.validarParamEntradaFuncionEdadCob(fnac, poliza, subpoliza, certificado, nsuscripcion, criterioFecha);
		
		IObtenerConfiguracion obtenerConfiguracion = FachadaServicios.getObtenerConfiguracion();
		varFdiferimiento = obtenerConfiguracion.recuperarVarFdiferimiento(poliza, subpoliza, certificado, nsuscripcion);
		
		if (varFdiferimiento == null){
			edadCob = BigDecimal.valueOf(-1);
		} else {	
			edadCob = nAnnos(fnac, varFdiferimiento, criterioFecha);
		}
		
		
		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Fin Funcion << EDADCOB >> de la clase FuncionesAuxiliares, con resultado edadCob = {} ", edadCob);
		}
		
		return edadCob;
	}
	
	/**
	 * Función Auxiliar para el cálculo de la cuantía nominal de vida de Tares con prestación en forma de renta.
	 * @param NRTA Duración en años de las rentas garantizadas
	 * @param FPR Número de pagos al año
	 * @param I1bti Interés del primer tramo
	 * @pram PRR Porcentaje de revalorización de la renta			   
	 * @return
	 */
	public static BigDecimal vacf(final Integer NRTA, final Integer FPR, final BigDecimal I1bti, final BigDecimal PRR){
		//Inicio variables locales
		BigDecimal  vacf = BigDecimal.ZERO;
		Integer lim, j;
		BigDecimal va1,va2,vt = BigDecimal.ZERO,i = BigDecimal.ONE;
		BigDecimal iter, fpr;
		//Fin variables locales
		//Validadcion parametros de entrada
		ValidacionesFuncionesAuxiliares.validarParamEntradaFuncionVacf(NRTA,FPR,I1bti,PRR);
		//Fin validacion de parametros de entrada	
		lim=NRTA*FPR;
		for(j=0;j<=lim-1;j++)
		{
			iter = new BigDecimal(j);
			fpr = new BigDecimal(FPR);
			va1=Util.pow(i.add(I1bti), iter.divide(fpr,ConstantsFunciones.MATH_CONTEXT).negate());
			va2=Util.pow(i.add(PRR), BigDecimal.valueOf(j).divide(BigDecimal.valueOf(FPR), ConstantsFunciones.MATH_CONTEXT));
			vt=vt.add(va1.multiply(va2, ConstantsFunciones.MATH_CONTEXT));
		}
		vacf= vt;
		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Fin Funcion << Vacf >> de la clase FuncionesAuxiliares, con resultado vacf = {} ", vacf);
		}
		return vacf;
	}
	
	/**
	 * Función Auxiliar para el cálculo de la cuantía nominal de vida de Tares 
	 * con prestación en forma de renta.
	 * @param factorI1
	 * @param gic
	 * @param factorGasto
	 * @param probXRenoHasta
	 * @param spCom
	 * @param factorRiesgo
	 * @return Double
	 */
	public static BigDecimal tarifaXRenoHasta (final BigDecimal factorI1      , 
			                                   final BigDecimal gic           ,
			                                   final BigDecimal factorGasto   ,
			                                   final BigDecimal probXRenoHasta,
										       final String     spCom         ,
										       final BigDecimal factorRiesgo  ) {
		BigDecimal tarifa = BigDecimal.ZERO;
		
		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Inicio de la Funcion << tarifaXRenoHasta >> de la clase FuncionesAuxiliares, para la entrada factorI1 = {}, " 
					+ "gic = {}, factorGasto = {}, probXRenoHasta = {}, spcom = {}, factorRiesgo = {} ", 
					factorI1, gic, factorGasto, probXRenoHasta, spCom, factorRiesgo);
		}
		
		// Se realizará la validación de los parámetros de entrada marcados como obligatorios.
		ValidacionesFuncionesAuxiliares.validarParamEntradaFuncionTarifaXRenoHasta(factorI1, gic, factorGasto, probXRenoHasta, spCom);
		if (spCom.equals(ConstantsFunciones.CTE_GARANTIA_PRINCIPAL)){
			tarifa = ConstantsFunciones.CTE_OPER_1000.multiply(gic.add(factorI1.multiply(probXRenoHasta)).divide(factorGasto));
		} else {
			tarifa = ConstantsFunciones.CTE_OPER_1000.multiply(gic.add(factorI1.multiply(factorRiesgo).multiply(probXRenoHasta)).divide(factorGasto));
		}
		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Fin de la Funcion << tarifaXRenoHasta >> de la clase FuncionesAuxiliares, para la entrada factorI1 = {}, " 
					+ "gic = {}, factorGasto = {}, probXRenoHasta = {}, spcom = {}, factorRiesgo = {} ", 
					factorI1, gic, factorGasto, probXRenoHasta, spCom, factorRiesgo);
		}
		return tarifa;
	}
	
	/**
	 * Función auxiliar que calcula el Valor Actual por amortización con pagos extras.
	 * @param npa numero o periodicidad de pagos en la amortización
	 * @param g crecimiento de la cuota amortización
	 * @param mes1 Meses de pagos adicionales. Se considerará su valor como cero (ya que actualmente ningún producto considera pagos extraordinarios).
	 * @param mes2 Meses de pagos adicionales. Se considerará su valor como cero (ya que actualmente ningún producto considera pagos extraordinarios).
	 * @param If interés financiero del préstamo
	 */
	
public static BigDecimal delta(final Integer na, final Integer npa, final BigDecimal g, final Integer mes1, final Integer mes2, final BigDecimal If){
		
		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Inicio Funcion << Delta >> de la clase FuncionesAuxiliares, para la entrada na = {}, npa = {}, g = {},"
					+" mes1 = {}, mes2 = {}, If = {}", 
					na, npa, g, mes1, mes2, If);
		}
		// Validación de los parámetros de entrada
			ValidacionesFuncionesAuxiliares.validarParamEntradaFuncionDelta(na, npa, g, mes1, mes2, If);
		// Fin validación parámetros de entrada
			
		// Inicio variables locales
			BigDecimal delta = BigDecimal.ZERO;
			BigDecimal varFactIf = BigDecimal.ZERO;
			Integer varFactNpa = ConstantsFunciones.CTE_0;
			BigDecimal varFactG = BigDecimal.ZERO;
			BigDecimal pp = BigDecimal.ZERO;
			BigDecimal deltaJ;
			BigDecimal varFactNpaAux;
			BigDecimal varFactIfAux;
			BigDecimal varExpAux;
			
			
		// Fin variables locales
			varFactIf = BigDecimal.ONE.add(If.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01).divide(BigDecimal.valueOf(npa), ConstantsFunciones.MATH_CONTEXT));
			varFactNpa = npa/ConstantsFunciones.CTE_12;
			varFactNpaAux = BigDecimal.valueOf(npa).divide(BigDecimal.valueOf(12),ConstantsFunciones.MATH_CONTEXT);
			varFactG = BigDecimal.ONE.add(g.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
			
			BigDecimal calculo1,calculo2;
			
			for(int j = 1; j <= na; j++){
				if(j == mes1 || j == mes2){
					deltaJ = ConstantsFunciones.CTE_OPER_2;
				}
				else{
					deltaJ = BigDecimal.ONE;
				}
				
				//if ((na - j) % ConstantsFunciones.CTE_12/npa == 0){
				if ((j) % (ConstantsFunciones.CTE_12/npa) == 0){
					pp = BigDecimal.ONE;
				}else{
					pp = BigDecimal.ZERO;
				}
				varFactIfAux = BigDecimal.valueOf(Math.pow(varFactIf.doubleValue(), varFactNpaAux.doubleValue()*j));
				calculo1 = BigDecimal.ONE.divide(varFactIfAux, ConstantsFunciones.MATH_CONTEXT);
				varExpAux = BigDecimal.valueOf(j-1).divide(BigDecimal.valueOf(12), ConstantsFunciones.MATH_CONTEXT);
				calculo2 = BigDecimal.valueOf(Math.pow(varFactG.doubleValue(), varExpAux.doubleValue()));
				
				delta = delta.add(((calculo1.multiply(calculo2)).multiply(pp)).multiply(deltaJ));
				
			}
			
			if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
				FuncionesAuxiliares.LOG.trace("Fin Funcion << Delta >> de la clase FuncionesAuxiliares, con resultado delta = {} ", delta);
			}
		return delta;
	}
	
	/**
	 * Función Auxiliar para el cálculo de la cuantía nominal de de la prestación 
	 * a pagar en caso de fallecimiento o invalidez de un seguro COMPLETO.
	 * 
	 * @param varPRP
	 * @param varPrimaIni
	 * @param vartc
	 */
	
	public static BigDecimal PPCppal(final BigDecimal varPRP, final BigDecimal varPrimaIni, final Integer vartc){
		
		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Inicio Funcion << PPCppal >> de la clase FuncionesAuxiliares, para la entrada varPRP = {}, varPrimaIni = {}, vartc = {}", 
					varPRP, varPrimaIni, vartc);
		}
		// Validación de los parámetros de entrada
			ValidacionesFuncionesAuxiliares.validarParamEntradaFuncionPPCppal(varPRP, varPrimaIni, vartc);
		// Fin validación parámetros de entrada
			
		// Inicialización de las variables locales
			BigDecimal PPCppal = BigDecimal.ZERO;
			BigDecimal varPNA = BigDecimal.ZERO;
			BigDecimal varPNATotal = BigDecimal.ZERO;
		// Fin variables locales
			
		for(int j = 0; j<vartc; j++){
			if(j == 0){
					varPNA = varPrimaIni;
				
			} else {
					varPNA = varPNA.multiply(BigDecimal.ONE.add(varPRP.divide(ConstantsFunciones.CTE_OPER_100,ConstantsFunciones.MATH_CONTEXT)));
					
			}
			varPNATotal = varPNATotal.add(varPNA);

		}
		PPCppal = varPNATotal;
			
		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Fin Funcion << PPCppal >> de la clase FuncionesAuxiliares, con resultado PPCppal = {} ", PPCppal); 
		}
		return PPCppal;
	}
	
	/** 
	 * Función auxiliar que calcula la Base reguladora neta mensual en año j
	 * @param j
	 * @param c1bbc
	 * @param edadExp
	 * @return brnm
	 */
	
	
	public static BigDecimal BRNM(final Integer j, final BigDecimal c1bbc, final BigDecimal edadExp)
	{
		//Inicio variables locales
		BigDecimal brnmj = BigDecimal.ZERO;
		
		Integer	edadExpEntero = ConstantsFunciones.CTE_0;
		//Fin variables locales
		
		//Validadcion parametros de entrada
		ValidacionesFuncionesAuxiliares.validarParamEntradaFuncionBRNM(j,c1bbc,edadExp);
		//Fin validacion de parametros de entrada
		
		BigDecimal exponenente = ConstantsFunciones.CTE_OPER_60.subtract(edadExp);
		edadExpEntero = exponenente.intValue();
		
		brnmj = c1bbc.multiply(Util.pow((ConstantsFunciones.CTE_OPER_1_PUNTO_035), new BigDecimal(edadExpEntero)));
		
		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Fin Funcion << BRNM >> de la clase FuncionesAuxiliares, con resultado brnmj = {} ", brnmj);
		}
		
		return brnmj;	
	}
	
	/** 
	 * Función auxiliar que calcula la cuota mensual a cargo del trabajador en el año j 
	 * @param j
	 * @param c1bbcx
	 * @param edadExp
	 * @return cmctj
	 */
	
	public static BigDecimal CMCT(final Integer j, final BigDecimal c1bbc, final BigDecimal edadExp)
	{
		//Inicio variables locales
		BigDecimal cmctj = BigDecimal.ZERO;
		BigDecimal varK1 = ConstantsFunciones.CTE_OPER_72_PUNTO_1274626;
		BigDecimal varK2 = ConstantsFunciones.CTE_OPER_12_PUNTO_0202421;
		BigDecimal varK3 = 	ConstantsFunciones.CTE_OPER_0_PUNTO_20133905;
		BigDecimal varK4 = ConstantsFunciones.CTE_OPER_1_PUNTO_32823675;
		BigDecimal varBrnmj = BigDecimal.ZERO;
		BigDecimal calculoAux = BigDecimal.ZERO;
		
		//Fin variables locales
		
		//Validadcion parametros de entrada
		ValidacionesFuncionesAuxiliares.validarParamEntradaFuncionBRNM(j,c1bbc,edadExp);
		//Fin validacion de parametros de entrada
		varBrnmj = BRNM(j, c1bbc, edadExp);
		calculoAux = (varBrnmj.subtract(varK1)).divide(varK2,ConstantsFunciones.MATH_CONTEXT);
		calculoAux = new BigDecimal(calculoAux.intValue());
		cmctj = ((calculoAux).multiply(varK3)).add(varK4);
		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Fin Funcion << CMCT >> de la clase FuncionesAuxiliares, con resultado cmctj = {} ", cmctj);
		}
		
		return cmctj;	
	}
	
	/** 
	 * Función auxiliar que calcula el Capital Total Asegurado j 
	 * @param j
	 * @param c1bbc
	 * @param edadExp
	 * @return ctaj
	 */
	
	public static BigDecimal CTA(final Integer j, final BigDecimal c1bbc, final BigDecimal edadExp)
	{
		//Inicio variables locales
		BigDecimal ctaj = BigDecimal.ZERO;
		BigDecimal varK3 = ConstantsFunciones.CTE_OPER_0_PUNTO_20133905;
		BigDecimal varK4 = ConstantsFunciones.CTE_OPER_1_PUNTO_32823675;
		BigDecimal varK5 = ConstantsFunciones.CTE_OPER_CTE_300_PUNTO_506052;
		BigDecimal varK6 = ConstantsFunciones.CTE_OPER_2103_PUNTO_54237;
		BigDecimal varCmctj = BigDecimal.ZERO;
		
		//Fin variables locales
		
		//Validadcion parametros de entrada
		ValidacionesFuncionesAuxiliares.validarParamEntradaFuncionBRNM(j,c1bbc,edadExp);
		//Fin validacion de parametros de entrada
		varCmctj = CMCT(j, c1bbc, edadExp);
		ctaj = (new BigDecimal((varCmctj.subtract(varK4).divide(varK3, ConstantsFunciones.MATH_CONTEXT)).intValue()).multiply(varK5)).add(varK6);
		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Fin Funcion << CTA >> de la clase FuncionesAuxiliares, con resultado cta = {} ", ctaj);
		}
		
		return ctaj;	
	}
	
	/** 
	 * Función Auxiliar que calcula la Prima Unitaria Periódica Mensual
	 * @param j
	 * @param valoresTabMort
	 * @param x
	 * @return pupm
	 */
	
	public static BigDecimal PUPM(final Integer j, final List<BigDecimal> lstValoresTabMort, final BigDecimal x, final Timestamp fecJ, final String criterioEdad,  final Timestamp fnac, final Timestamp fecIniRenta)
	{
		//Inicio variables locales
		BigDecimal varDenominador = BigDecimal.ZERO;
		BigDecimal varEdad1 = BigDecimal.ZERO;
		BigDecimal varEdad2 = BigDecimal.ZERO;
		BigDecimal varLxEdad1Entero = BigDecimal.ZERO;
		BigDecimal varLxEdad1Entero1 = BigDecimal.ZERO;
		BigDecimal varLxEdad1 = BigDecimal.ZERO;
		BigDecimal varLxEdad2Entero = BigDecimal.ZERO;
		BigDecimal varLxEdad2Entero2 = BigDecimal.ZERO;
		BigDecimal varLxEdad2 = BigDecimal.ZERO;
		BigDecimal varQxMensual = BigDecimal.ONE;
		BigDecimal bdproyeccion = new BigDecimal(j);
		BigDecimal pupm = BigDecimal.ZERO;
		
		
		//Fin variables locales
		
		//Validadcion parametros de entrada
		ValidacionesFuncionesAuxiliares.validarParamEntradaFuncionPUPM(j,lstValoresTabMort,x,fecJ,criterioEdad,fnac);
		//Fin validacion de parametros de entrada
		varDenominador = BigDecimal.ONE.subtract(ConstantsFunciones.CTE_OPER_CTE_0_PUNTO_03);
		//varEdad1 = x.add(bdproyeccion.multiply(ConstantsFunciones.CTE_OPER_1ENTRE12, ConstantsFunciones.MATH_CONTEXT));
		//varEdad2 = x.add(bdproyeccion.add(BigDecimal.ONE).multiply(ConstantsFunciones.CTE_OPER_1ENTRE12));
		

		varEdad1 = FuncionesAuxiliares.nEdad(UtilFechas.incrMeses(fecJ, null, -1, false), fnac, criterioEdad, fecIniRenta, 0);
		varEdad2 = FuncionesAuxiliares.nEdad(fecJ, fnac, criterioEdad, fecIniRenta, 0);
		
		int varEdad1Entero = varEdad1.intValue();
		if (varEdad1Entero <= lstValoresTabMort.size()- ConstantsFunciones.CTE_2){
			varLxEdad1Entero = lstValoresTabMort.get(varEdad1Entero);
			varLxEdad1Entero1 = lstValoresTabMort.get(varEdad1Entero + 1);
			varLxEdad1 = Util.interpolaPorEdad(varLxEdad1Entero, varLxEdad1Entero1, varEdad1);
		}
		
		int varEdad2Entero = varEdad2.intValue();
		if (varEdad2Entero <= lstValoresTabMort.size() - ConstantsFunciones.CTE_2){
		varLxEdad2Entero = lstValoresTabMort.get(varEdad2Entero);
		varLxEdad2Entero2 = lstValoresTabMort.get(varEdad2Entero + 1);
		varLxEdad2 = Util.interpolaPorEdad(varLxEdad2Entero, varLxEdad2Entero2, varEdad2);
		}
		
		if(!(varLxEdad1.setScale(34, RoundingMode.HALF_UP).equals(BigDecimal.ZERO.setScale(34, RoundingMode.HALF_UP)))){
			varQxMensual = (varLxEdad1.subtract(varLxEdad2)).divide(varLxEdad1, ConstantsFunciones.MATH_CONTEXT);
		}
		pupm = varQxMensual.divide(varDenominador,ConstantsFunciones.MATH_CONTEXT);		
		
		//System.out.println(j + ";" + varEdad1 + ";" + varEdad2 + ";" + varLxEdad1 + ";" + varLxEdad2 + ";" + varQxMensual + ";" + pupm);
		
		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Fin Funcion << PUPM >> de la clase FuncionesAuxiliares, con resultado pupm = {} ", pupm);
		}
		
		return pupm;	
	}
	
	/** 
	 * Función auxiliar que calcula el Capital Asegurado Prima Periódica
	 * @param j
	 * @param c1bbc
	 * @param edadExp
	 * @param valoresTabMort
	 * @param x
	 * @return cpp
	 */
	
	public static BigDecimal CPP(final Integer j, final BigDecimal c1bbc, final BigDecimal edadExp, final List<BigDecimal> lstValoresTabMort, final BigDecimal x, final Timestamp fecJ, final String criterioEdad,  final Timestamp fnac, final Timestamp fecIniRenta, final BigDecimal CMCT)
	{
		//Inicio variables locales
		BigDecimal varCmctj = BigDecimal.ZERO;
		BigDecimal varPupmj = BigDecimal.ZERO;
		BigDecimal cppj = BigDecimal.ZERO;

		//Fin variables locales

		//Validadcion parametros de entrada
		ValidacionesFuncionesAuxiliares.validarParamEntradaFuncionCPP(j,edadExp, lstValoresTabMort, x, fecJ, criterioEdad, fnac);
		//Fin validacion de parametros de entrada
		if(null == CMCT || CMCT.equals(BigDecimal.ZERO)){
			varCmctj = CMCT(j, c1bbc, edadExp);
		}else{
			varCmctj = CMCT;
		}
		varPupmj = PUPM(j, lstValoresTabMort, x, fecJ, criterioEdad, fnac, fecIniRenta);
		if(!varPupmj.setScale(34, RoundingMode.HALF_UP).equals(BigDecimal.ZERO.setScale(34, RoundingMode.HALF_UP))){
			cppj = varCmctj.divide(varPupmj, ConstantsFunciones.MATH_CONTEXT);
		}else{
			cppj = BigDecimal.ZERO;
		}
		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Fin Funcion << CPP >> de la clase FuncionesAuxiliares, con resultado cppj = {} ", cppj);
		}

		return cppj;	
	}
	
	/** 
	 * Función auxiliar que calcula el Capital Asegurado Prima Periódica
	 * @param j
	 * @param fecJ
	 * @param Tabla2000
	 * @param nva
	 * @param kbencon
	 * @param Ainicio
	 * @return incr
	 */
	
	public static BigDecimal INCR(final Integer j, final Timestamp fecJ, final BigDecimal Tabla2000, final String kbencon, Integer aInicio)
	{
		//Inicio variables locales
		Integer varAnoJ = ConstantsFunciones.CTE_0;
		BigDecimal varFactorPorcrecsal = BigDecimal.ZERO;
		BigDecimal varTipozc = BigDecimal.ZERO;
		BigDecimal incr = BigDecimal.ZERO;
		
		//Fin variables locales
		
		//Validadcion parametros de entrada
		ValidacionesFuncionesAuxiliares.validarParamEntradaFuncionINCR(j,fecJ,Tabla2000, kbencon, aInicio);
		//Fin validacion de parametros de entrada
		varAnoJ = UtilFechas.getAnio(fecJ);
		if(varAnoJ == aInicio){
			varFactorPorcrecsal = BigDecimal.ONE.add(ConstantsFunciones.CTE_OPER_TE_3_PUNTO_8.divide(ConstantsFunciones.CTE_OPER_100));
		}
		else if(varAnoJ > aInicio){
			varFactorPorcrecsal = BigDecimal.ONE.add(ConstantsFunciones.CTE_OPER_1_PUNTO_5.divide(ConstantsFunciones.CTE_OPER_100));
		}
		if(kbencon.equals(ConstantsFunciones.CTE_101)){
			varTipozc = BigDecimal.ONE;
		}
		if(kbencon.substring(ConstantsFunciones.CTE_0,  ConstantsFunciones.CTE_2).equals(ConstantsFunciones.CTE_30)){
			varTipozc = (BigDecimal.ONE.add(varFactorPorcrecsal)).pow(aInicio-ConstantsFunciones.CTE_2001);
		}

		incr = (Tabla2000.multiply(ConstantsFunciones.CTE_OPER_1_PUNTO_038)).multiply((varFactorPorcrecsal.pow(varAnoJ-ConstantsFunciones.CTE_2001)).subtract(varTipozc));
		
		//System.out.println(varAnoJ + ";" + aInicio + ";" + varFactorPorcrecsal + ";" + Tabla2000 + ";" + varTipozc + ";" + incr);
		
		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Fin Funcion << INCR >> de la clase FuncionesAuxiliares, con resultado incr = {} ", incr);
		}
		
		return incr;	
	}
	
	public static BigDecimal RNoDep(final Integer varTCm, final Integer varBeta, final Integer x, final List<BigDecimal> lstValoresTabMort, final List<BigDecimal> lstValoresTabMort3, final Integer w, final BigDecimal tit1)
	{
		//Inicio variables locales
				BigDecimal RNoDep = BigDecimal.ONE;
				BigDecimal prob = BigDecimal.ZERO;
				BigDecimal prob1 = BigDecimal.ZERO;
				BigDecimal prob2 = BigDecimal.ZERO;
				BigDecimal act = BigDecimal.ZERO;
				BigDecimal Lx = BigDecimal.ZERO;
				BigDecimal LxEntero = BigDecimal.ZERO;
				BigDecimal LxEntero1 = BigDecimal.ZERO;
				BigDecimal LxEntero2 = BigDecimal.ZERO;
				Integer LxEnt1 = 0;
				BigDecimal Lxi = BigDecimal.ZERO;
				BigDecimal LxiEntero = BigDecimal.ZERO;
				BigDecimal LxiEntero1 = BigDecimal.ZERO;
				BigDecimal LxiEntero2 = BigDecimal.ZERO;
				Integer LxiEnt1 = 0;
				BigDecimal auxJ = BigDecimal.ZERO;

				//Fin variables locales

				//Validadcion parametros de entrada
				ValidacionesFuncionesAuxiliares.validarParamEntradaFuncionRNoDep(varTCm,varBeta, x, lstValoresTabMort, lstValoresTabMort3, w, tit1);
				//Fin validacion de parametros de entrada
				
				if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
					FuncionesAuxiliares.LOG.trace("Fin Funcion << RNoDep >> de la clase FuncionesAuxiliares, con resultado RNoDep = {} ", RNoDep);
				}
				
				for (int j = (varTCm + varBeta + 1); j <= ((w-x)*12 - varTCm + varBeta + 1); j++) {
					LxEntero = lstValoresTabMort.get(x+j/12);
					LxEnt1 = x+j/12;
					LxEntero1 = (new BigDecimal(x).add(new BigDecimal(j-1).divide(new BigDecimal(12), ConstantsFunciones.MATH_CONTEXT))).subtract(new BigDecimal(LxEnt1));
					LxEntero2 = lstValoresTabMort.get(x+1).subtract(lstValoresTabMort.get(x));
					Lx = LxEntero.add(LxEntero1.multiply(LxEntero2));
					prob1 = Lx.divide(lstValoresTabMort.get(x), ConstantsFunciones.MATH_CONTEXT);
					if (x > lstValoresTabMort3.size() - 1) {
						prob2 = BigDecimal.ZERO; 
					} else if((x+j/12) > lstValoresTabMort3.size() - 2) {
						prob2 = BigDecimal.ZERO;
					} else {
						LxiEntero = lstValoresTabMort3.get(x+j/12);
						LxiEnt1 = x+j/12;
						LxiEntero1 = (new BigDecimal(x).add(new BigDecimal(j-1).divide(new BigDecimal(12), ConstantsFunciones.MATH_CONTEXT))).subtract(new BigDecimal(LxiEnt1));
						LxiEntero2 = lstValoresTabMort3.get(x+1).subtract(lstValoresTabMort3.get(x));
						Lxi = LxiEntero.add(LxiEntero1.multiply(LxiEntero2));
						prob2 = Lxi.divide(lstValoresTabMort3.get(x), ConstantsFunciones.MATH_CONTEXT);
					}
					prob = prob1.multiply(prob2);
					auxJ = new BigDecimal(j).divide(new BigDecimal(12), ConstantsFunciones.MATH_CONTEXT);
					act = Util.pow(BigDecimal.ONE.add(tit1.divide(BigDecimal.valueOf(100))), auxJ.negate());
					RNoDep = RNoDep.add(prob.multiply(act));
				}
				

				return RNoDep;	
	}
	
	public static BigDecimal RNoDepC(final Integer varTCm, final Integer varBeta, final Integer x, final List<BigDecimal> lstValoresTabMort, final List<BigDecimal> lstValoresTabMort3, final List<Tab923> tab923, final Integer w, final BigDecimal tit1)
	{
		//Inicio variables locales
				BigDecimal RNoDepC = BigDecimal.ONE;
				BigDecimal prob = BigDecimal.ZERO;
				BigDecimal prob1 = BigDecimal.ZERO;
				BigDecimal prob2 = BigDecimal.ZERO;
				BigDecimal act = BigDecimal.ZERO;

				String L1 = "0";
				String L2 = "0";
				String L3 = "0";
				String L4 = "0";
				String L5 = "0";
				String L6 = "0";
				String PRP1 = "0";
				String PRP2 = "0";
				String PRP3 = "0";
				String PRP4 = "0";
				String PRP5 = "0";
				String PRP6 = "0";
				
				if(null != tab923 && !tab923.isEmpty() && null != tab923.get(0)){
					
					L1 = tab923.get(0).getl1();
					L2 = tab923.get(0).getl2();
					L3 = tab923.get(0).getl3();
					L4 = tab923.get(0).getl4();
					L5 = tab923.get(0).getl5();
					L6 = tab923.get(0).getl6();
					PRP1 = tab923.get(0).getPRP1();
					PRP2 = tab923.get(0).getPRP2();
					PRP3 = tab923.get(0).getPRP3();
					PRP4 = tab923.get(0).getPRP4();
					PRP5 = tab923.get(0).getPRP5();
					PRP6 = tab923.get(0).getPRP6();
				}
				
				BigDecimal varP = BigDecimal.ZERO;
				BigDecimal auxL1, auxL2, auxL3, auxL4, auxL5, auxL6;
				Integer auxEntL2, auxEntL3, auxEntL4, auxEntL5, auxEntL6;
				Integer auxMaxL2, auxMaxL3, auxMaxL4, auxMaxL5, auxMaxL6;
				BigDecimal varPL1, varPL2, varPL3, varPL4, varPL5, varPL6;
				BigDecimal auxJ = BigDecimal.ZERO;
				BigDecimal Lx = BigDecimal.ZERO;
				BigDecimal LxEntero = BigDecimal.ZERO;
				BigDecimal LxEntero1 = BigDecimal.ZERO;
				BigDecimal LxEntero2 = BigDecimal.ZERO;
				Integer LxEnt1 = 0;
				BigDecimal Lxi = BigDecimal.ZERO;
				BigDecimal LxiEntero = BigDecimal.ZERO;
				BigDecimal LxiEntero1 = BigDecimal.ZERO;
				BigDecimal LxiEntero2 = BigDecimal.ZERO;
				Integer LxiEnt1 = 0;
				if (L1 == null) {
					L1 = "0";
				}
				if (L2 == null) {
					L2 = "0";
				}
				if (L3 == null) {
					L3 = "0";
				}
				if (L4 == null) {
					L4 = "0";
				}
				if (L5 == null) {
					L5 = "0";
				}
				if (L6 == null) {
					L6 = "0";
				}
				if (PRP1 == null) {
					PRP1 = "0";
				}
				if (PRP2 == null) {
					PRP2 = "0";
				}
				if (PRP3 == null) {
					PRP3 = "0";
				}
				if (PRP4 == null) {
					PRP4 = "0";
				}
				if (PRP5 == null) {
					PRP5 = "0";
				}
				if (PRP6 == null) {
					PRP6 = "0";
				}
				
				if (L1.equals("99")) {
					L2 = "99";
					L3 = "99";
					L4 = "99";
					L5 = "99";
					L6 = "99";
				}else if (L2.equals("99")) {
					L3 = "99";
					L4 = "99";
					L5 = "99";
					L6 = "99";
				} else if (L3.equals("99")) {
					L4 = "99";
					L5 = "99";
					L6 = "99";
				} else if (L4.equals("99")) {
					L5 = "99";
					L6 = "99";
				} else if (L5.equals("99")) {
					L6 = "99";
				}

				//Fin variables locales

				//Validadcion parametros de entrada
				ValidacionesFuncionesAuxiliares.validarParamEntradaFuncionRNoDep(varTCm,varBeta, x, lstValoresTabMort, lstValoresTabMort3, w, tit1);
				//Fin validacion de parametros de entrada
				
				if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
					FuncionesAuxiliares.LOG.trace("Fin Funcion << RNoDepC >> de la clase FuncionesAuxiliares, con resultado RNoDepC = {} ", RNoDepC);
				}
				
				for (int j = (varTCm+varBeta+1); j <= ((w-x)*12 - varTCm + varBeta + 1); j++) {
					
					if (new BigDecimal(L1).intValue() > (j / ConstantsFunciones.CTE_12)){
						auxL1 = BigDecimal.valueOf(j / ConstantsFunciones.CTE_12);
					} else {
						auxL1 = BigDecimal.valueOf(new BigDecimal(L1).intValue());
					}
					auxMaxL2 = (j / ConstantsFunciones.CTE_12) - new BigDecimal(L1).intValue() + 1;
					if (auxMaxL2 > 0) {
						auxEntL2 = auxMaxL2;
					} else {
						auxEntL2 = 0;
					}
					if (new BigDecimal(L2).intValue() > auxEntL2){
						auxL2 = BigDecimal.valueOf(auxEntL2);
					} else {
						auxL2 = new BigDecimal(L2);
					}
					auxMaxL3 = (j / ConstantsFunciones.CTE_12) - new BigDecimal(L1).intValue() - new BigDecimal(L2).intValue() + 1;
					if (auxMaxL3 > 0) {
						auxEntL3 = auxMaxL3;
					} else {
						auxEntL3 = 0;
					}
					if (new BigDecimal(L3).intValue() > auxEntL3){
						auxL3 = BigDecimal.valueOf(auxEntL3);;
					} else {
						auxL3 =  new BigDecimal(L3);
					}
					auxMaxL4 = (j / ConstantsFunciones.CTE_12) - new BigDecimal(L1).intValue() - new BigDecimal(L2).intValue() - new BigDecimal(L3).intValue() + 1;
					if (auxMaxL4 > 0) {
						auxEntL4 = auxMaxL4;
					} else {
						auxEntL4 = 0;
					}
					if (new BigDecimal(L4).intValue() > (j / ConstantsFunciones.CTE_12)){
						auxL4 = BigDecimal.valueOf(auxEntL4);
					} else {
						auxL4 =  new BigDecimal(L4);
					}
					auxMaxL5 = (j / ConstantsFunciones.CTE_12) - new BigDecimal(L1).intValue() - new BigDecimal(L2).intValue() - new BigDecimal(L3).intValue() - new BigDecimal(L4).intValue() + 1;
					if (auxMaxL5 > 0) {
						auxEntL5 = auxMaxL5;
					} else {
						auxEntL5 = 0;
					}
					if (new BigDecimal(L5).intValue() > auxEntL5){
						auxL5 = BigDecimal.valueOf(auxEntL5);
					} else {
						auxL5 =  new BigDecimal(L5);
					}
					auxMaxL6 = (j / ConstantsFunciones.CTE_12) - new BigDecimal(L1).intValue() - new BigDecimal(L2).intValue() - new BigDecimal(L3).intValue() - new BigDecimal(L4).intValue() - new BigDecimal(L5).intValue() + 1;
					if (auxMaxL6 > 0) {
						auxEntL6 = auxMaxL6;
					} else {
						auxEntL6 = 0;
					}
					if (new BigDecimal(L6).intValue() > auxEntL6){
						auxL6 = BigDecimal.valueOf(auxEntL6);
					} else {
						auxL6 =  new BigDecimal(L6);
					}
					
					if (PRP1 == null) {
						varPL1 = Util.pow(BigDecimal.ONE.divide(BigDecimal.valueOf(100), ConstantsFunciones.MATH_CONTEXT),auxL1);
					} else {
						varPL1 = Util.pow(BigDecimal.ONE.add(new BigDecimal(PRP1).divide(BigDecimal.valueOf(10000))),auxL1);
					}
					if (PRP2 == null) {
						varPL2 = Util.pow(BigDecimal.ONE.divide(BigDecimal.valueOf(100), ConstantsFunciones.MATH_CONTEXT),auxL2);
					} else {
						varPL2 = Util.pow(BigDecimal.ONE.add(new BigDecimal(PRP2).divide(BigDecimal.valueOf(10000))),auxL2);
					}
					if (PRP3 == null) {
						varPL3 = Util.pow(BigDecimal.ONE.divide(BigDecimal.valueOf(100), ConstantsFunciones.MATH_CONTEXT),auxL3);
					} else {
						varPL3 = Util.pow(BigDecimal.ONE.add(new BigDecimal(PRP3).divide(BigDecimal.valueOf(10000))),auxL3);
					}
					if (PRP4 == null) {
						varPL4 = Util.pow(BigDecimal.ONE.divide(BigDecimal.valueOf(100), ConstantsFunciones.MATH_CONTEXT),auxL4);
					} else {
						varPL4 = Util.pow(BigDecimal.ONE.add(new BigDecimal(PRP4).divide(BigDecimal.valueOf(10000))),auxL4);
					}
					if (PRP5 == null) {
						varPL5 = Util.pow(BigDecimal.ONE.divide(BigDecimal.valueOf(100), ConstantsFunciones.MATH_CONTEXT),auxL5);
					} else {
						varPL5 = Util.pow(BigDecimal.ONE.add(new BigDecimal(PRP5).divide(BigDecimal.valueOf(10000))),auxL5);
					}
					if (PRP6 == null) {
						varPL6 = Util.pow(BigDecimal.ONE.divide(BigDecimal.valueOf(100), ConstantsFunciones.MATH_CONTEXT),auxL6);
					} else {
						varPL6 = Util.pow(BigDecimal.ONE.add(new BigDecimal(PRP6).divide(BigDecimal.valueOf(10000))),auxL6);
					}
					varP = varPL1.multiply(varPL2).multiply(varPL3).multiply(varPL4).multiply(varPL5).multiply(varPL6);
					LxEntero = lstValoresTabMort.get(x+j/12);
					LxEnt1 = x+j/12;
					LxEntero1 = (new BigDecimal(x).add(new BigDecimal(j-1).divide(new BigDecimal(12), ConstantsFunciones.MATH_CONTEXT))).subtract(new BigDecimal(LxEnt1));
					LxEntero2 = lstValoresTabMort.get(x+1).subtract(lstValoresTabMort.get(x));
					Lx = LxEntero.add(LxEntero1.multiply(LxEntero2));
					prob1 = Lx.divide(lstValoresTabMort.get(x), ConstantsFunciones.MATH_CONTEXT);
					if (x > lstValoresTabMort3.size() - 1) {
						prob2 = BigDecimal.ZERO; 
						
					} else if((x+j/12) > lstValoresTabMort3.size() - 2) {
						prob2 = BigDecimal.ZERO;
					} else {
						LxiEntero = lstValoresTabMort3.get(x+j/12);
						LxiEnt1 = x+j/12;
						LxiEntero1 = (new BigDecimal(x).add(new BigDecimal(j-1).divide(new BigDecimal(12), ConstantsFunciones.MATH_CONTEXT))).subtract(new BigDecimal(LxiEnt1));
						LxiEntero2 = lstValoresTabMort3.get(x+1).subtract(lstValoresTabMort3.get(x));
						Lxi = LxiEntero.add(LxiEntero1.multiply(LxiEntero2));
						prob2 = Lxi.divide(lstValoresTabMort3.get(x), ConstantsFunciones.MATH_CONTEXT);
					}
					prob = prob1.multiply(prob2);
					auxJ = new BigDecimal(j).divide(new BigDecimal(12), ConstantsFunciones.MATH_CONTEXT);
					act = Util.pow(BigDecimal.ONE.add(tit1.divide(BigDecimal.valueOf(100))), auxJ.negate());
					RNoDepC = RNoDepC.add(prob.multiply(act).multiply(varP));
				}
				

				return RNoDepC;	
	}
	
	public static List<BigDecimal> obtenerLix(final List<BigDecimal> varValoresTabInv, final BigDecimal sobreriesgo) {
		//Variables locales
		BigDecimal varLix = BigDecimal.valueOf(1000000);
		List<BigDecimal> LIX = new ArrayList<BigDecimal>();
		int j = 0;
		//Fin variables locales
		
		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Inicio de la función << obtenerLix >> de la clase FuncionesAuxiliares");
		}
		LIX.add(varLix);
		
		while (!varLix.equals(BigDecimal.ZERO)) {
			j = j +1;
			if (BigDecimal.ONE.subtract((varValoresTabInv.get(j - 1).divide(BigDecimal.valueOf(1000)))) == BigDecimal.ZERO) {
				varLix = BigDecimal.ZERO;
			} else {
				varLix = LIX.get(j - 1).multiply((BigDecimal.ONE.subtract((varValoresTabInv.get(j - 1).divide(BigDecimal.valueOf(1000)))))).multiply((BigDecimal.ONE.add(sobreriesgo)));
			}
			
			if (varLix.compareTo(BigDecimal.ZERO) < 0) {
				varLix = BigDecimal.ZERO;
			}
			LIX.add(varLix);
			
		}
		
		if (FuncionesAuxiliares.LOG.isTraceEnabled()) {
			FuncionesAuxiliares.LOG.trace("Fin de la función << obtenerLix >> de la clase FuncionesAuxiliares, con resultado varLix = {}", varLix);
		}
		
		return LIX;
	}
}