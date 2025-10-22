package es.mapfre.solvencia.formulacion.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;

public class FuncionesVBX {
	
	/** 
	 * Log funciones auxiliares.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(FuncionesVBX.class);
	
	
	
	/**
	 * Funcion que calcula los dias transcurridos hasta el mes de la fecha indicada en el Parametro de entrada.
	 * 
	 * @param fechaEntrada
	 *            Identifica la fecha sobre la que se efectua el calculo
	 * @param criterioFecha
	 *            Identifica el criterio de fechas para realizar el calculo
	 * @return Identifica el resultado del calculo aplicado a la fecha de entrada
	 */
	public static Integer ddEnero(final Timestamp fechaEntrada, final String criterioFecha) {
		// Variables locales
		int numDias = 0;
		int mesSeleccionado = 0;
		// Fin variables locales

		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio de la Funcion << ddEnero >> de la clase FuncionesVBX, para la entrada fechaEntrada = {} y criterioFecha = {}", fechaEntrada, criterioFecha);
		}

		// INICIO DE VALIDACION DE CAMPOS OBLIGATORIOS
		ValidacionesFuncionesVBX.validacionCamposObligatoriosDDEnero(fechaEntrada, criterioFecha);
		// FIN DE VALIDACION DE CAMPOS OBLIGATORIOS

		// INICIO VALIDACION ATRIBUTO criterioFecha
		ValidacionesFuncionesVBX.validacionCriterioFechaFuncionDDEneroEY(criterioFecha);
		// FIN VALIDACION ATRIBUTO criterioFecha

		/*
		 * obtenemos el mes de la fecha de entrada para obtener posteriormente
		 * el numero de dias
		 */
		mesSeleccionado = UtilFechas.getMes(fechaEntrada);

		if (ConstantsFunciones.CTE_CRI_FECHA_01.equals(criterioFecha)) {
			numDias = ConstantsFunciones.ARRAY_BASE_365[mesSeleccionado - 1];
		} else if (ConstantsFunciones.CTE_CRI_FECHA_02.equals(criterioFecha)) {
			numDias = ConstantsFunciones.ARRAY_BASE_360[mesSeleccionado - 1];
		}

		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Fin de la Funcion << ddEnero >> de la clase FuncionesVBX, con resultado numDias = {} ", numDias);
		}

		return numDias;
	}

	
	/**
	 * La función PUCCAPdifer(fcal) obtiene la Aportación Unica (de las modalidades de Rentas de Jubilacion) 
	 * del ajuste capitalizada en las fechas de renovacion de la garantia AU hasta el momento (TC,fracAnioInc) incluido. 
	 * 
	 * @param varPu
	 * 				Aportación unica del ajuste o de la suscripcion
	 * @param i1BTI
	 * 				Primer Interes tecnico segun Bases Tecnicas Iniciales
	 * @param i2BTI
	 * 				Segundo Interes tecnico segun Bases Tecnicas Iniciales
	 * @param varNr
	 * 				Nº. de renovaciones transcurridas desde la fecha de alta del ajuste o suscripcion hasta TC+fracAnioInc
	 * @param varNrm
	 * 				Nº. de renovaciones transcurridas desde la fecha de alta el ajuste hasta la fecha de fin de casamiento (M), dicho dia incluido
	 * @param varm
	 * 				Numero de años en que aplicamos un primer interes tecnico
	 * @param beta1
	 * 				Variable Beta1
	 * @param beta2
	 * 				Variable Beta2
	 * @param difercol
	 * 				Diferimiento en cobrar la renta
	 * @param varTCY
	 * 				Resultado de la función naños() en base al criterio establecido entre la fecha de efecto de la suscripción y la fecha de cálculo
	 * @return puccapDifer
	 */
	public static BigDecimal puccapDifer(final BigDecimal varPu, final BigDecimal i1BTI, final BigDecimal i2BTI, final Integer varNr, final Integer varNrm, final BigDecimal varm,
			final BigDecimal beta1, final BigDecimal beta2, final BigDecimal difercol, final BigDecimal varTCY) {
		// Variables locales
		BigDecimal puccapDifer = BigDecimal.ZERO;
		// Fin variables locales 

		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio de la funcion << puccapDifer >> de la clase FuncionesVBX, para la entrada varPu = {}, i1BTI = {}, i2BTI = {}, varNr = {}, varNrm = {}, varm = {}, beta1 = {}, beta2 = {}, difercol = {} y varTCY = {} ",
					varPu, i1BTI, i2BTI, varNr, varNrm, varm, beta1, beta2, difercol, varTCY);
		}

		//Validamos los parametros de entrada
		ValidacionesFuncionesVBX.validarParamEntradaFuncionPuccapDiferParte1(varPu, i1BTI, i2BTI, varNr, varNrm, varm);
		ValidacionesFuncionesVBX.validarParamEntradaFuncionPuccapDiferParte2(beta1, beta2, difercol, varTCY);
		/**
		 * 	Si DIFERCOL <= M se obtendrá PUCCAPdifer  como: 
				PUCCAPdifer(fcal)= PU * (1+(I1BTI * 0.01) - beta1)^NR 

					Si DIFERCOL > M:
					Si TC + fracAnioInc  <= M: 

				PUCCAPdifer(fcal)= PU * (1+(I1BTI * 0.01) - beta1)NR

					Si TC + fracAnioInc  > M :

				PUCCAPdifer(fcal)= PU*( (1+(I1BTI * 0.01) - beta1)^NRM* (1+(I2BTI*0.01) - beta2)^(NR-NRM))
		 */

		// (1 + I1BTI - beta1)
		final BigDecimal i1BTIBeta = BigDecimal.ONE.add(i1BTI.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)).subtract(beta1);

		if (difercol.compareTo(varm) <= 0) {
			//PUCCAPdifer(fcal)= PU * (1+I1BTI - beta1)^NR 
			puccapDifer = varPu.multiply(Util.pow(i1BTIBeta, varNr));

		} else {
			if (varTCY.compareTo(varm) <= 0) {
				puccapDifer = varPu.multiply(Util.pow(i1BTIBeta, varNr));
			} else {
				puccapDifer = varPu.multiply(Util.pow(i1BTIBeta, varNrm).multiply(Util.pow(BigDecimal.ONE.add(i2BTI.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)).subtract(beta2), varNr - varNrm)));
			}
		}

		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Fin de la funcion << puccapDifer >> de la clase FuncionesVBX, con resultado = {}", puccapDifer);
		}

		return puccapDifer;
	}

	/**
	 * Fraccion de año transcurrida desde efecto anterior hasta la fecha de
	 * cierre. En el cociente tenemos en cuenta que el cierre se situa a las 24
	 * horas. Se utiliza en el calculo de provisiones de balance.
	 * 
	 * @param fantRenovacion
	 *            Identifica la fecha de efecto o renovación mensual anterior
	 * @param fproxRenovacion
	 *            Identifica la fecha de la próxima renovación mensual.
	 * @param fcierre
	 *            Identifica la fecha de cierre
	 * @param tipo
	 *           Tipo del cálculo: PRORRATA o FORFAIT
	 * @param criterioFecha
	 * 			 Identifica el criterio de fechas para realizar el cálculo
	 * @return alfa
	 */
	public static BigDecimal mg001Alfa(final Timestamp fantRenovacion, final Timestamp fproxRenovacion,
			final Timestamp fcierre, final String tipo, final String criterioFecha) {
		// Variables locales
		BigDecimal alfa = BigDecimal.ZERO;
		int numdc = 0;
		int numdr = 0;
		String criterioFechaCalc = criterioFecha;
		final IAlmacenarDatos almacenarDatos = FachadaServicios.getAlmacenarDatos();
		// Fin variables

		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio Funcion << mg001Alfa >> de la clase FuncionesVBX, para la entrada fantRenovacion = {}, fproxRenovacion = {}, fcierre = {} , tipo = {} y criterioFecha ) {}",
					fantRenovacion, fproxRenovacion, fcierre, tipo, criterioFecha);
		}
		ValidacionesFuncionesVBX.validarCriteriosFuncionMg001Alfa(fantRenovacion, fproxRenovacion, fcierre, tipo, criterioFecha);

		/*
		 * Si criterioFecha es ( 03 ó 04 ) se asumirá criterioFecha = 01 (BASE365) y se generará una incidencia de tipo "Informativa"
		 * indicando que se asume ese criterio 365 para una baseMensual.
		 */
		if (ConstantsFunciones.CTE_CRI_FECHA_03.equals(criterioFecha) || ConstantsFunciones.CTE_CRI_FECHA_04.equals(criterioFecha)) {
			criterioFechaCalc = ConstantsFunciones.CTE_CRI_FECHA_01;

			// Devolver incidencia informativa indicando que, para el criterio de fecha utilizado, se asume 365 para una base mensual
			final Incidencia incidencia = Solvencia2ExcepcionHelper.crearAviso(ConstantsFunciones.CTE_COD_ERROR_I000, new Object[] {criterioFecha});

			almacenarDatos.almacenarIncidencias(incidencia);
		}

		if (ConstantsFunciones.CTE_CAL_PRORRATA.equals(tipo)) {
			/*
			 * Dc = numero de dias que median entre la fecha de efecto o
			 * renovacion anterior y la fecha de cierre
			 */
			numdc = FuncionesAuxiliares.nDias(fantRenovacion, fcierre, criterioFechaCalc);

			/*
			 * Dr = diferencia en dias entre la fecha de la anterior renovacion
			 * y la fecha de la proxima renovacion.
			 */
			numdr = FuncionesAuxiliares.nDias(fantRenovacion ,fproxRenovacion, criterioFechaCalc);

			alfa = new BigDecimal(numdc + 1).divide(new BigDecimal(numdr), ConstantsFunciones.MATH_CONTEXT);
		} else if (ConstantsFunciones.CTE_CAL_FORFAIT.equals(tipo)) {
			alfa = BigDecimal.valueOf(0.5);
		}

		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Fin Funcion << mg001Alfa >> de la clase FuncionesVBX, con resultado alfa = {}", alfa);
		}

		return alfa;
	}
	
	
	/**
	 * Fracción de mes transcurrida desde efecto mensual anterior hasta la fecha de cierre.
	 * En el cociente tenemos en cuenta que el cierre se sitúa a las 24 horas.
	 * Se utiliza en el cálculo de provisiones de balance,
	 * 
	 * @param fantRenovacion Identifica la fecha de efecto o renovación mensual anterior
	 * @param fproxRenovacion Identifica la fecha de la próxima renovación mensual.
	 * @param fcierre Identifica la fecha de cierre
	 * @param tipoCalculo Tipo del cálculo: PRORRATA
	 * @param criterioFecha Identifica el criterio de fechas para realizar el cálculo
	 * @return alfam Fracción de mes transcurrida desde efecto mensual anterior hasta la fecha de cierre
	 */
	public static BigDecimal mg00xAlfam(final Timestamp fantRenovacion, final Timestamp fproxRenovacion,
			final Timestamp fcierre, final String tipoCalculo, final String criterioFecha) {
		// Variables locales
		BigDecimal alfam = BigDecimal.ZERO;
		int numdcm = 0;
		String criterioFechaCalc = criterioFecha;
		final IAlmacenarDatos almacenarDatos = FachadaServicios.getAlmacenarDatos();
		// Fin variables

		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio Funcion << mg00xAlfam >> de la clase FuncionesVBX, para la entrada fantRenovacion = {}, fproxRenovacion = {}, fcierre = {} , tipoCalculo = {} y criterioFecha ) {}",
					fantRenovacion, fproxRenovacion, fcierre, tipoCalculo, criterioFecha);
		}

		// Dentro de esta validación se esta comprobando si tipoCalculo = "FORFAIT" devolviendo error 003
		ValidacionesFuncionesVBX.validarCriteriosFuncionMg00xAlfam(fantRenovacion, fproxRenovacion, fcierre, tipoCalculo, criterioFecha);

		/*
		 * Si criterioFecha es ( 03 ó 04 ) se asumirá criterioFecha = 01 (BASE365) y se generará una incidencia de tipo "Informativa"
		 * indicando que se asume ese criterio 365 para una baseMensual.
		 */
		if (ConstantsFunciones.CTE_CRI_FECHA_03.equals(criterioFecha) || ConstantsFunciones.CTE_CRI_FECHA_04.equals(criterioFecha)) {
			criterioFechaCalc = ConstantsFunciones.CTE_CRI_FECHA_01;

			// Devolver incidencia informativa indicando que, para el criterio de fecha utilizado, se asume 365 para una base mensual
			final Incidencia incidencia = Solvencia2ExcepcionHelper.crearAviso(ConstantsFunciones.CTE_COD_ERROR_I000, new Object[] {criterioFecha});

			almacenarDatos.almacenarIncidencias(incidencia);
		}

		if (ConstantsFunciones.CTE_CAL_PRORRATA.equals(tipoCalculo)) {
			/*
			 * Dcm = numero de dias que median entre la fecha de efecto o
			 * renovacion anterior y la fecha de cierre
			 */
			numdcm = FuncionesAuxiliares.nDias(fantRenovacion, fcierre, criterioFechaCalc);

			//			/*
			//			 * Drm = diferencia en dias entre la fecha de la anterior renovacion
			//			 * y la fecha de la proxima renovacion.
			//			 */
			//			numdrm = nDias(fantRenovacion, fproxRenovacion, criterioFechaCalc);
			//			alfam = new BigDecimal(numdcm).divide(new BigDecimal(numdrm), ConstantsFunciones.MATH_CONTEXT);
			alfam = new BigDecimal(numdcm).multiply(ConstantsFunciones.CTE_OPER_1_PARTIDO_365, ConstantsFunciones.MATH_CONTEXT);			

		}

		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Fin Funcion << mg00xAlfam >> de la clase FuncionesVBX, con resultado alfam = {}", alfam);
		}

		return alfam;
	}

	
	
	/**
	 * La función PUCCAP (fcal) Aportación Única de las modalidades Flexibles de Capitales Diferidos capitalizada en 
	 * las fechas de renovación hasta el momento fcal incluido. 
	 * @param pu Aportación única del ajuste o de la suscripción.
	 * @param n Duración real del ajuste o suscripción.
	 * @param i1bti Primer Interés técnico según Bases Técnicas Iniciales.
	 * @param i2bti Segundo Interés técnico según Bases Técnicas Iniciales.
	 * @param nr Nº. de renovaciones transcurridas desde la fecha de alta del ajuste o suscripción hasta TC+y
	 * @param nrm Nº. de renovaciones transcurridas desde la fecha de alta el ajuste hasta la fecha de fin de casamiento (M), dicho día incluido.
	 * @param m Número de años en que aplicamos un primer interés técnico.
	 * @param beta1 Variable Beta1.
	 * @param beta2 Variable Beta2.
	 * @param fcalc Fecha de Cálculo.
	 * @param fFinTramo1 Fecha Fin Tramo1.
	 * 
	 * @return puccap
	 */
	public static BigDecimal puccapcol(final BigDecimal pu, final BigDecimal n, final BigDecimal i1bti, final BigDecimal i2bti, final Integer nr,
			final Integer nrm, final BigDecimal m, final BigDecimal beta1, final BigDecimal beta2, final Timestamp fcalc, final Timestamp fFinTramo1,
			final Map<String, Object> mapVariables, final String modulo){
		//Inicio variables locales
		BigDecimal puccapcol = BigDecimal.ZERO;
		BigDecimal operador1;
		BigDecimal operador2;
		//Fin variables locales
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio Funcion <<puccapcol>> de la clase FuncionesVBX, para la entrada pu = {}, n = {},"
					+ " i1BTI = {}, i2BTI = {}, nr = {}, nrm = {}, m = {}, beta1 = {}, beta2 = {}, fcalc = {}, fFinTramo1 = {}",
					pu, n, i1bti, i2bti, nr, nrm, m, beta1, beta2, fcalc, fFinTramo1);
		}
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesVBX.validarParamEntradaFuncionPuccapcol(pu, n, i1bti, i2bti, nr, nrm, m, beta1, beta2, fcalc, fFinTramo1);
		
		// Claves de las variables almacenadas en memoria para optimizar el cálculo
		String clave1 = "puccapcol1"+modulo+nr;
		String clave2 = "puccapcol2"+modulo+nr;
		
		operador1 = BigDecimal.ONE.add(i1bti).subtract(beta1);
				
		if (m.compareTo(n) >= 0){
			puccapcol = (BigDecimal) mapVariables.get(clave1);
			if (puccapcol  == null){
				puccapcol = pu.multiply(Util.pow(operador1, nr));
				mapVariables.put(clave1, puccapcol); // Se almacena para optimizar
			}
			
		} else {
			if (!fcalc.after(fFinTramo1)){
				puccapcol = (BigDecimal) mapVariables.get(clave1);
				if (puccapcol == null){
					puccapcol = pu.multiply(Util.pow(operador1, nr));
					mapVariables.put(clave1, puccapcol); // Se almacena para optimizar
				}
			} else {
				puccapcol = (BigDecimal) mapVariables.get(clave2);
				if (puccapcol == null){
					operador2 = BigDecimal.ONE.add(i2bti).subtract(beta2);
					puccapcol = pu.multiply(Util.pow(operador1, nrm)).multiply(Util.pow(operador2, nr-nrm));
					mapVariables.put(clave2, puccapcol); // Se almacena para optimizar
				}
				
			}
		}
		
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Fin Funcion << puccapcol >> de la clase FuncionesVBX, con resultado puccapcol = {}", puccapcol);
		}
		
		return puccapcol;
		
	}
	

	/**
	 * Función que calcula el término OBFUTADOR de la formulación de la corriente de Provisión Matemática por fórmula cerrada.
	 * @param fcal Fecha de cálculo.
	 * @param fecIniSusc Fecha de inicio de suscripción.
	 * @param i1 Primer Interés técnico.
	 * @param i2 Segundo Interés técnico.
	 * @param m Número de años en que aplicamos un primer interés técnico desde fecha suscripción.
	 * @param tcYResultado de la función naños() en base al criterio establecido entre la fecha de efecto de la suscripción y la fecha de cálculo.
	 * @param criterFec Criterio de Fechas para el cálculo.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param w Edad Máxima de la tabla de mortalidad.
	 * @param x Edad del asegurado a Fecha de suscripción.
	 * @param n Duración real del ajuste o suscripción.
	 * @param gic Gastos de gestión interna sobre Capital.
	 * @param gastGi
	 * @param numi1PorcentajeMasUno
	 * @param numi2PorcentajeMasUno
	 * 
	 * @return obfutadorc
	 */
	public static BigDecimal obfutadorc (final Timestamp fcal, final Timestamp fecIniSusc, final BigDecimal i1, 
			final BigDecimal i2, final BigDecimal m, final BigDecimal tcY, final String criterFec, final List<BigDecimal> valoresTabMort, 
			final BigDecimal w, final BigDecimal x, final BigDecimal n, final BigDecimal gic, final BigDecimal gastGi, 
			final BigDecimal numi1PorcentajeMasUno, final BigDecimal numi2PorcentajeMasUno){
		//Inicio variables locales
		BigDecimal obfutadorc = BigDecimal.ZERO;
		BigDecimal varXN;
		BigDecimal varXTCy;
		BigDecimal varVVidaN;
		BigDecimal varLxN = BigDecimal.ZERO;
		BigDecimal varLxTCy = BigDecimal.ZERO;
		BigDecimal varTCy;
		//Fin variables locales
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio Funcion <<obfutadorc>> de la clase FuncionesVBX, para la entrada "
					+ " fcal = {}, fecIniSusc = {}, i1 = {}, i2 = {}, m = {}, tcY = {}, criterFec = {}, valoresTabMort = {},"
					+ " w = {} x = {}, n = {}, gic = {}, gastGi = {}",
					fcal, fecIniSusc, i1, i2, m, tcY, criterFec, valoresTabMort, w, x, n, gic, gastGi);
		}
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesVBX.validarParamEntradaFuncionObfutadorc(fcal, fecIniSusc, i1, i2, m, tcY, criterFec, valoresTabMort, w, x, n, gic, gastGi);
		
		varTCy = FuncionesAuxiliares.nAnnos(fecIniSusc, fcal, criterFec);
		
		if (varTCy.equals(n)){
			obfutadorc = BigDecimal.ONE;
		} else {
		
			varXN = x.add(n);
			varXTCy = x.add(varTCy);
			varVVidaN = FuncionesActualizacionFinanciera.vVida(n, i1, i2, m, tcY, numi1PorcentajeMasUno, numi2PorcentajeMasUno);
			
			if (varXN.compareTo(w) < 0){
				varLxN = Util.getVarLx(varXN, valoresTabMort);
			} 
			
			if (varXTCy.compareTo(w) < 0){
				varLxTCy = Util.getVarLx(varXTCy, valoresTabMort);
			}
			
			//Se calcula obfutadorc = ((varLxN/varLxTCy) * varVVidaN ) +  (gic * GastGi)
			BigDecimal division = varLxN.divide(varLxTCy, ConstantsFunciones.MATH_CONTEXT);
			obfutadorc = (division.multiply(varVVidaN)).add(gic.multiply(gastGi));
		
		}
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Fin Funcion << obfutadorc >> de la clase FuncionesVBX, con resultado obfutadorc = {}", obfutadorc);
		}
		
		return obfutadorc;
	}


	/**
	 * Función que calcula el diferimiento actuarial por años incompletos, incluyendo la actualización de los gastos 
	 * de gestión interna del seguro en productos flexibles de prima periódica.
	 * @param x Edad a fecha de cálculo.
	 * @param tc Anualidades completas transcurridas desde la fecha de efecto hasta la fecha de cálculo.
	 * @param alfa Fracción de mes transcurrida desde efecto mensual anterior hasta la fecha de cierre.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param w Edad Máxima de la tabla de mortalidad.
	 * @param n Duración del seguro en años.
	 * @param i1 Interés del primer tramo.
	 * @param gic Gastos de gestión interna sobre Capital.
	 * 
	 * @return obfutadorcpp301
	 */
	public static BigDecimal obfutadorcpp301(final Integer x, final Integer tc, final BigDecimal alfa, final List<BigDecimal> valoresTabMort,
			final Integer w, final Integer n, final BigDecimal i1, final BigDecimal gic){
		//Inicio variables locales
		BigDecimal obfutadorcpp301 = BigDecimal.ZERO;
		//Fin variables locales
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio Funcion << OBFUTADORCpp301 >> de la clase FuncionesVBX, para la entrada  x = {}, tc = {}, alfa = {},"
					+" valoresTabMort = {}, w = {}, n = {}, i1 = {}, gic = {}", 
					x, tc, alfa, valoresTabMort, w, n, i1, gic);
		}
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesVBX.validarParamEntradaFuncionObfutadorcpp301(x, tc, alfa, valoresTabMort, w, n, i1, gic);
		
		//Se calcula OBFUTADORCpp301 como: OBFUTADORCpp301 = VIDAPA (x,TC,Alfa,valoresTabMort,W,n,I1,gic)
		obfutadorcpp301 = FuncionesSecundarias.vidapa(x, tc, alfa, valoresTabMort, w, n, i1, gic);
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Fin Funcion << OBFUTADORCpp301 >> de la clase FuncionesVBX, con resultado obfutadorcpp301 = {} ", obfutadorcpp301);
		}
		
		return obfutadorcpp301;
	}
	
	/**
	 * Función que calcula el diferimiento actuarial por años incompletos, incluyendo la actualización de los gastos 
	 * de gestión interna del seguro en productos flexibles de prima periódica.
	 * @param x Edad a fecha de cálculo.
	 * @param t Resultado de la función naños() en base al criterio establecido.
	 * @param beta Fracción de año pendiente entre el momento del cálculo y la siguiente anualidad del seguro.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param w Edad Máxima de la tabla de mortalidad.
	 * @param n Duración del seguro en años.
	 * @param i1 Interés del primer tramo.
	 * @param gic Gastos de gestión interna sobre Capital.
	 * 
	 * @return vidapu
	 */
	public static BigDecimal vidapu(final Integer x, final Integer t, final BigDecimal beta, final List<BigDecimal> valoresTabMort,
			final Integer w, final Integer n, final BigDecimal i1, final BigDecimal gic){
		//Inicio variables locales
		BigDecimal vidapu = BigDecimal.ZERO;
		BigDecimal varValor = BigDecimal.ZERO;
		BigDecimal varLFRACn;
		BigDecimal varLFRACt;
		BigDecimal varLFRACt1;
		BigDecimal operador2 = BigDecimal.ZERO;
		//Fin variables locales
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio Funcion << VIDAPU >> de la clase FuncionesVBX, para la entrada  x = {}, t = {}, beta = {},"
					+" valoresTabMort = {}, w = {}, n = {}, i1 = {}, gic = {}", 
					x, t, beta, valoresTabMort, w, n, i1, gic);
		}
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesVBX.validarParamEntradaFuncionVidapu(x, t, beta, valoresTabMort, w, n, i1, gic);
		
		if (t == n){
			vidapu = BigDecimal.ONE;
		} else {
			
			varLFRACn = FuncionesAuxiliares.lfrac(x, n, BigDecimal.ZERO, valoresTabMort, w);
			varLFRACt = FuncionesAuxiliares.lfrac(x, t, beta, valoresTabMort, w);
			varLFRACt1 = FuncionesAuxiliares.lfrac(x, t+1, BigDecimal.ZERO, valoresTabMort, w);
			
			/** 
			 * Se calcula VIDAPU = [〖 (varLFRACn )/(varLFRACt )*(1+ I1/100)〗^(-(n-T-beta))]+ gic/100* [(1-beta) + 
			 *  ((varLFRACt1 /varLFRACt)*varValor * (1+ I1/100)^(-(1-beta)) )] 
			 **/
			BigDecimal unoMasI1Entre100 = BigDecimal.ONE.add(i1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
			BigDecimal lfracNEntreT = varLFRACn.divide(varLFRACt, ConstantsFunciones.MATH_CONTEXT);
			BigDecimal lfracT1EntreT = varLFRACt1.divide(varLFRACt, ConstantsFunciones.MATH_CONTEXT);
			
			// 〖 (varLFRACn )/(varLFRACt )*(1+ I1/100)〗^(-(n-T-beta))]
			BigDecimal operador1 = lfracNEntreT.multiply(Util.pow(unoMasI1Entre100, (BigDecimal.valueOf(n-t).subtract(beta)).negate()));
			BigDecimal gicEntre100 = gic.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
			
			
			if (n - t -1 > 0){
				varValor = FuncionesRentas.ax(0, x, t+1, n, i1, valoresTabMort);
				
				// ((varLFRACt1 /varLFRACt) * varValor * (1+ I1/100)^(-(1-beta)) )
				operador2 = lfracT1EntreT.multiply(varValor).multiply(Util.pow(unoMasI1Entre100, (BigDecimal.ONE.subtract(beta).negate())));
			} //Si no se cumple la condición, varValor = 0 y por lo tanto operador2=0
			

			vidapu = operador1.add(gicEntre100.multiply(BigDecimal.ONE.subtract(beta).add(operador2)));
		}
		
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Fin Funcion << VIDAPU >> de la clase FuncionesVBX, con resultado vidapu = {} ", vidapu);
		}
		
		return vidapu;
	}

	/**
	 * Función que calcula la valoración del fallecimiento por primas pendientes. 
	 * @param x Edad a fecha de cálculo.
	 * @param t
	 * @param n Duración del seguro en años.
	 * @param fut Variable de Apoyo FUT.
	 * @param prp Porcentaje de revalorización.
	 * @param ifal Variable de Apoyo IFAL.
	 * @param i1 Interés del primer tramo.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param w Edad Máxima de la tabla de mortalidad.
	 * @param alfa Fracción de año pendiente entre el momento del cálculo y la siguiente anualidad del seguro.
	 * 
	 * @return obfutadorpap301
	 */
	public static BigDecimal obfutadorpap301(final Integer x, final Integer t, final Integer n, final BigDecimal fut, final BigDecimal prp,
			final BigDecimal ifal, final BigDecimal i1, final List<BigDecimal> valoresTabMort, final Integer w, final BigDecimal alfa){
		//Inicio variables locales
		BigDecimal obfutadorpap301 = BigDecimal.ZERO;
		BigDecimal varMordpend;
		BigDecimal varSumaF = BigDecimal.ZERO;
		BigDecimal varLFRACt;
		BigDecimal varLFRACt1;
		BigDecimal varFuturas;
		//Fin variables locales
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio Funcion << OBFUTADORpap301 >> de la clase FuncionesVBX, para la entrada  x = {}, t = {}, n = {},"
					+" fut = {}, prp = {}, ifal = {}, i1 = {}, valoresTabMort = {}, w = {}, alfa = {}", 
					x, t, n, fut, prp, ifal, i1, valoresTabMort, w, alfa);
		}
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesVBX.validarParamEntradaFuncionObfutadorpap301(x, t, n, fut, prp, ifal, i1, valoresTabMort, w, alfa);
		
		varMordpend = FuncionesFallecimiento.morpend(x, t, alfa, valoresTabMort, w, n, i1);
		varSumaF = FuncionesPrimas.sumaf(t+1, t, n, prp);
		varLFRACt = FuncionesAuxiliares.lfrac(x, t, alfa, valoresTabMort, w);
		varLFRACt1 = FuncionesAuxiliares.lfrac(x, t+1, BigDecimal.ZERO, valoresTabMort, w);
		varFuturas = FuncionesPrimas.futuras(x, t+1, n, fut, prp, ifal, i1, valoresTabMort, w);
		
		/**
		 * Se calcula OBFUTADORpap301 como:
		 * OBFUTADORpap301 = varMordpend * [fut* (1+ prp/100)* varSumaF  ] +  
		 *     ((varLFRACt1 * (1+ I1/(100 ))^(–(1-alfa)))/varLFRACt)* [(1+ prp/100)* varFuturas  ]
		 */
		
		BigDecimal unoMasI1Entre100 = BigDecimal.ONE.add(i1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		BigDecimal unoMasPrpEntre100 = BigDecimal.ONE.add(prp.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		BigDecimal potencia = Util.pow(unoMasI1Entre100, (BigDecimal.ONE.subtract(alfa).negate()));
		
		//[fut* (1+ prp/100)* varSumaF ]
		BigDecimal operando1 = fut.multiply(unoMasPrpEntre100).multiply(varSumaF);
		//((varLFRACt1 * (1+ I1/(100 ))^(–(1-alfa)))/varLFRACt)
		BigDecimal operando2 = (varLFRACt1.multiply(potencia)).divide(varLFRACt, ConstantsFunciones.MATH_CONTEXT);
		//[(1+ prp/100)* varFuturas  ]
		BigDecimal operando3 = unoMasPrpEntre100.multiply(varFuturas);
		
		obfutadorpap301 = (varMordpend.multiply(operando1)).add(operando2.multiply(operando3));
		
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Fin Funcion << OBFUTADORpap301 >> de la clase FuncionesVBX, con resultado obfutadorpap301 = {} ", obfutadorpap301);
		}
		
		return obfutadorpap301;
	}


	
	/**
	 * Función que calcula una Renta actuarial una cabeza, prepagable, creciente en progresión ARITMETICA, desplazada (p)  
	 * y duración n.
	 * @param x Edad a fecha de cálculo.
	 * @param t
	 * @param n Duración del seguro en años.
	 * @param fut Variable de Apoyo FUT.
	 * @param ifal Variable de Apoyo IFAL.
	 * @param i1 Interés del primer tramo.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param w Edad Máxima de la tabla de mortalidad.
	 * @param alfa Fracción de año pendiente entre el momento del cálculo y la siguiente anualidad del seguro.
	 * @param tc0 Anualidad en curso.
	 * @param pNAtc Prima Tarada.
	 * @param pprUmic Primas Satisfechas en la anualidad T.
	 * @param fantani Fecha Aniversario Anterior.
	 * @param feccierre Fecha de cierre.
	 * @param fecJ Fecha del periodo a calcular.
	 * @param cformapago Código forma de pago de la prima.
	 * @param tcm Meses completos transcurridos desde fecha de efecto a fecha de cálculo(fecha de cierre).
	 * @param ttm Número de meses transcurridos desde el efecto a la fecha de vencimiento.
	 * @param ppcapUmic Primas Periodicas Capitalizadas hasta la anualidad en curso.
	 * @param pas Variable de Apoyo PAS.
	 * @param prp Porcentaje de revalorización de Primas.
	 * @param beta Meses transcurridos desde la fecha de cálculo (Fecha de cierre)  hasta la fecha de proyección.
	 * 
	 * @return obfutadorpa301
	 */
	public static BigDecimal obfutadorpa301(final Integer x, final Integer t, final Integer n, final BigDecimal fut, final BigDecimal ifal,
			final BigDecimal i1, final List<BigDecimal> valoresTabMort, final Integer w, final BigDecimal alfa, final Integer tc0,
			final BigDecimal pNAtc, final BigDecimal pprUmic, final String cformapago, final Integer tcm, final Integer ttm, 
			final BigDecimal ppcapUmic, final BigDecimal pas, final BigDecimal prp, final Integer beta, final BigDecimal primaIni){
		//Inicio variables locales
		BigDecimal obfutadorpa301 = BigDecimal.ZERO;
		BigDecimal varMordpend;
		BigDecimal varLFRACt;
		BigDecimal varLFRACt1;
		BigDecimal varPasadaca;
		BigDecimal varPPR;
		BigDecimal varPPCAPtc = BigDecimal.ZERO;
		BigDecimal varPendpa;
		//Fin variables locales
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio Funcion << OBFUTADORpa301 >> de la clase FuncionesVBX, para la entrada  x = {}, t = {}, n = {},"
					+" fut = {}, ifal = {}, i1 = {}, valoresTabMort = {}, w = {}, alfa = {}, tc0 = {}, pNAtc = {}, pprUmic = {}, cformapago = {}, tcm = {}"
					+" ttm = {}, ppcapUmic = {}, pas = {}, prp = {}", 
					x, t, n, fut, ifal, i1, valoresTabMort, w, alfa, tc0, pNAtc, pprUmic, cformapago, tcm, ttm, ppcapUmic, pas, prp);
		}
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesVBX.validarParamEntradaFuncionObfutadorpa301Parte1(x, t, n, fut, ifal, i1, valoresTabMort);
		ValidacionesFuncionesVBX.validarParamEntradaFuncionObfutadorpa301Parte2(w, alfa, tc0, pNAtc, pprUmic, cformapago);
		ValidacionesFuncionesVBX.validarParamEntradaFuncionObfutadorpa301Parte3(tcm, ttm, ppcapUmic, pas, prp, beta);
		
		varMordpend = FuncionesFallecimiento.morpend(x, t, alfa, valoresTabMort, w, n, i1);
		varLFRACt = FuncionesAuxiliares.lfrac(x, t, alfa, valoresTabMort, w);
		varLFRACt1 = FuncionesAuxiliares.lfrac(x, t+1, BigDecimal.ZERO, valoresTabMort, w);
		varPasadaca = FuncionesPrimas.pasadaca(x, t, ifal, valoresTabMort, w, n, i1);
		varPPR = FuncionesPrimas.ppr(tc0, t, pprUmic);
		varPPCAPtc = FuncionesPrimas.ppcap(t, tcm, ttm, beta, tc0, ifal, ppcapUmic, n, pprUmic, pNAtc, cformapago, prp, pas, fut, i1, primaIni);
		varPendpa = FuncionesPrimas.pendpa(cformapago, tc0, t, pprUmic, pNAtc, primaIni);
		
		BigDecimal unoMasIfalEntre100 = BigDecimal.ONE.add(ifal.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		BigDecimal unoMasI1Entre100 = BigDecimal.ONE.add(i1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		
		/**
		 * Se calcula OBFUTADORpa301 como:
		 * OBFUTADORpa301 = [varMordpend  + ((varLFRACt1 * (1+ I1/(100 ))^(–(1-alfa)))/(varLFRACt *( 1+ ifal/100)))*varPasadaca]
		 *    * [varPPCAPtc +varPPR +(PNAtc*varPendpa  ) ]
		 */
		BigDecimal dividendo = varLFRACt1.multiply(Util.pow(unoMasI1Entre100, (BigDecimal.ONE.subtract(alfa)).negate()));
		BigDecimal divisor = varLFRACt.multiply(unoMasIfalEntre100);
		
		BigDecimal operador1 = varMordpend.add(dividendo.divide(divisor, ConstantsFunciones.MATH_CONTEXT).multiply(varPasadaca));
		BigDecimal operador2 = varPPCAPtc.add(varPPR).add(pNAtc.multiply(varPendpa));
		
		obfutadorpa301 = operador1.multiply(operador2);
		
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Fin Funcion << OBFUTADORpa301 >> de la clase FuncionesVBX, con resultado obfutadorpa301 = {} ", obfutadorpa301);
		}
		
		return obfutadorpa301;
	}

	/**
	 * Función que calcula la suma de aportaciones extraordinarias capitalizadas por años completos.
	 * @param tc Anualidad de cálculo.
	 * @param tcm Meses completos transcurridos desde fecha de efecto a fecha de cálculo(fecha de cierre).
	 * @param beta Meses transcurridos desde la fecha de cálculo (Fecha de cierre)  hasta la fecha de proyección.
	 * @param tc0 Anualidad en curso (fecha cálculo).
	 * @param ifal Variable de Apoyo IFAL.
	 * @param ppcap Primas Periodicas Capitalizadas hasta la anualidad en curso.
	 * 
	 * @return puccap
	 */
	public static BigDecimal puccap(final Integer tc, final Integer tcm, final Integer beta, final Integer tc0, 
			final BigDecimal ifal, final BigDecimal ppcap, final BigDecimal ppr){
		//Inicio variables locales
		BigDecimal puccap = BigDecimal.ZERO;
		//Fin variables locales
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio Funcion << PAGADPA >> de la clase FuncionesVBX, para la entrada  tc = {}, tcm = {}, beta = {},"
					+" tc0 = {}, ifal = {}, ppcap = {}", tc, tcm, beta, tc0, ifal, ppcap);
		}
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesVBX.validarParamEntradaFuncionPuccap(tc, tcm, beta, tc0, ifal, ppcap);
		
		BigDecimal unoMasIfalEntre100 = BigDecimal.ONE.add(ifal.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		
		if (tc == tc0){
			//puccap = ppcap;
			puccap = ppcap.add(ppr);
		} else if (tc > tc0){
			// Se calcula PUCCAP = (ppcap + ppr) * 〖(1+ifal/100  )〗^(entero [((tcm+beta)/12)- tc0] )
			Integer exponente = ((tcm+beta)/12) -tc0;
			BigDecimal potencia = Util.pow(unoMasIfalEntre100, exponente);
			
			puccap = (ppcap.add(ppr)).multiply(potencia);
		} else {
			//Se devuelve error funcional AKXX – Error: La Anualidad de cálculo no puede ser anterior a la anualidad en curso.
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AK);
		}
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Fin Funcion << PUCCAP >> de la clase FuncionesVBX, con resultado puccap = {} ", puccap);
		}
		
		return puccap;
		
	}
	
	/**
	 * Función que calcula el término del terminal VTXVEPU.
	 * @param x Edad actuarial a fecha de efecto.
	 * @param t Anualidad del cálculo.
	 * @param i Tipo de interés.
	 * @param edadCob Edad futura, generalmente la edad de jubilación.
	 * @param lstValoresTabMort Valores de la tabla de mortalidad.
	 * @return viveDifer
	 */
	public static BigDecimal viveDifer(final BigDecimal x, final Integer t, final BigDecimal i, final BigDecimal edadCob, 
			final List<BigDecimal> lstValoresTabMort){
		//Inicio variables locales
		BigDecimal viveDifer = BigDecimal.ONE;
		BigDecimal varIniCob;
		BigDecimal varFactorI;
		BigDecimal varLxEdadCob;
		BigDecimal varXt;
		BigDecimal varLXt;
		BigDecimal varViveDifer = BigDecimal.ONE;
		//Fin variables locales	
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio Funcion << VIVEDIFER >> de la clase FuncionesVBX, para la entrada  x = {}, t = {},"
					+" i = {}, edadCob = {}, lstValoresTabMort = {}", 
					x, t, i, edadCob, lstValoresTabMort);
		}
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesVBX.validarParamEntradaFuncionViveDifer(x, t, i, edadCob, lstValoresTabMort);
		
		if (edadCob.equals(BigDecimal.valueOf(-1))){
			return viveDifer;
		}
		
		varIniCob = edadCob.subtract(x.add(BigDecimal.valueOf(t)));
		//VarFactorI = 1/〖(1+ i/100 )〗^varIniCob  
		varFactorI = BigDecimal.ONE.divide(Util.pow(BigDecimal.ONE.add(i.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)), varIniCob), ConstantsFunciones.MATH_CONTEXT);
		
		varLxEdadCob = Util.getVarLx(edadCob, lstValoresTabMort);
		
		varXt = x.add(BigDecimal.valueOf(t));
		varLXt = Util.getVarLx(varXt, lstValoresTabMort);
		
		if (!varLXt.setScale(34, RoundingMode.HALF_UP).equals(BigDecimal.ZERO.setScale(34, RoundingMode.HALF_UP)) && !varLxEdadCob.equals(BigDecimal.ZERO)){
			varViveDifer = (varLxEdadCob.divide(varLXt, ConstantsFunciones.MATH_CONTEXT)).multiply(varFactorI);
		}

		viveDifer = varViveDifer.min(BigDecimal.ONE);
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Fin Funcion << VIVEDIFER >> de la clase FuncionesVBX, con resultado viveDifer = {} ", viveDifer);
		}
		
		return viveDifer;
	}
	
	/**
	 * La función VPRET calcula este térnimo de la función ärx+t
	 * @param j Corresponde al período transcurrido desde  la fecha alta ajuste o suscripción  hasta la fecha “j” 
	 * @param m Número de años en que aplicamos un primer interés técnico
	 * @param i1 Primer Interés técnico
	 * @param i2 Segundo Interés técnico
	 * @param t Anualidad del cálculo
	 * @return
	 */
	public static BigDecimal vpret(final Integer j, final BigDecimal m, final BigDecimal i1, final BigDecimal i2, final Integer t){
		//Inicio variables locales
		BigDecimal vpret = BigDecimal.ZERO;
		BigDecimal unoMasI1Entre100;
		BigDecimal unoMasI2Entre100;
		//Fin variables locales	
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio Funcion << VPRET >> de la clase FuncionesVBX, para la entrada  j = {}, m = {},"
					+" i1 = {}, i2 = {}, t = {}",  j, m, i1, i2, t);
		}
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesVBX.validarParamEntradaFuncionVpret(j, m, i1, i2, t);
		
		unoMasI1Entre100 = BigDecimal.ONE.add(i1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		unoMasI2Entre100 = BigDecimal.ONE.add(i2.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		
		BigDecimal tBD = BigDecimal.valueOf(t);
		BigDecimal jBD = BigDecimal.valueOf(j);
		
		if (t<=j && BigDecimal.valueOf(j).compareTo(m) <0){
			vpret = Util.pow(unoMasI1Entre100, -(j-t));
		} else if (tBD.compareTo(m)<0 && m.compareTo(jBD) <= 0){
			vpret = Util.pow(unoMasI1Entre100, (m.subtract(tBD)).negate()).multiply(Util.pow(unoMasI2Entre100, (jBD.subtract(m)).negate()));
		} else if (tBD.compareTo(m) >= 0){
			vpret = Util.pow(unoMasI2Entre100, -(j-t));
		}
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Fin Funcion << VPRET >> de la clase FuncionesVBX, con resultado vpret = {} ", vpret);
		}
		
		return vpret;
	}
	
	/**
	 * Función ärx+t. Función auxiliar de la provisión VBX160.
	 * @param j2 Corresponde al período transcurrido desde  la fecha alta ajuste o suscripción  hasta la fecha “j” 
	 * @param difer Diferimiento de la renta.
	 * @param x Edad a fecha de cálculo
	 * @param w Límite de la tabla de mortalidad
	 * @param ppr Porcentaje revalorización de la renta
	 * @param t Anualidad del cálculo
	 * @param lstValoresTabMort Valores de la tabla de mortalidad
	 * @param m Número de años en que aplicamos un primer interés técnico
	 * @param i1 Primer Interés técnico
	 * @param i2 Segundo Interés técnico
	 * @return
	 */
	public static BigDecimal aarxt(final Integer difer, final Integer x, final Integer w, final BigDecimal ppr,
			final Integer t, final List<BigDecimal> lstValoresTabMort, final BigDecimal m, final BigDecimal i1, final BigDecimal i2){
		//Inicio variables locales
		BigDecimal aarxt = BigDecimal.ZERO;
		Integer varLimInf;
		BigDecimal varXj;
		BigDecimal varLxj;
		BigDecimal varXT;
		BigDecimal varLxT;
		BigDecimal varVpretj;
		//Fin variables locales	
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio Funcion << ärx+t >> de la clase FuncionesVBX, para la entrada  difer = {},"
					+" x = {}, w = {}, ppr = {}, t = {}, lstValoresTabMort = {}, m = {}, i1 = {}, i2 = {}",  
					difer, x, w, ppr, t, lstValoresTabMort, m, i1, i2);
		}
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesVBX.validarParamEntradaFuncionAarxtyAfrxtParte1(difer, x, w, ppr, t);
		ValidacionesFuncionesVBX.validarParamEntradaFuncionAarxtParte2(lstValoresTabMort, m, i1, i2);
		
		if (t < difer) {
			varLimInf = difer;
		} else {
			varLimInf = t;
		}
		
		BigDecimal opPpr = BigDecimal.ONE.add(ppr.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		BigDecimal potencia = Util.pow(opPpr, varLimInf-difer);
		
		varXT = BigDecimal.valueOf(x + t);
		varLxT = Util.getVarLx(varXT, lstValoresTabMort);
		
		/**
		 * Se calcula el Sumatorio desde j= difer hasta j = w-x:
		 * 	   (varLxj *〖(1+ppr/100)^ (j-difer) 〗 * varVpretj) /varLxT
		 */
		for (int j=varLimInf; j<=(w-x); j++){
			varXj = BigDecimal.valueOf(x + j);
			varLxj = Util.getVarLx(varXj, lstValoresTabMort);
			
			varVpretj = vpret(j, m, i1, i2, t);
			
			if (j > varLimInf){
				potencia = potencia.multiply(opPpr);
			} 
			
			BigDecimal dividendo = varLxj.multiply(potencia).multiply(varVpretj);
			
			if (!dividendo.equals(BigDecimal.ZERO) && !varLxT.equals(BigDecimal.ZERO)){
				aarxt = aarxt.add(dividendo.divide(varLxT, ConstantsFunciones.MATH_CONTEXT));
			}
			
		}
		
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Fin Funcion << ärx+t >> de la clase FuncionesVBX, con resultado aarxt = {} ", aarxt);
		}
		
		return aarxt;
	}
	
	
	/**
	 * La función VPOST calcula este término de la función arx+t
	 * @param j Corresponde al periodo transcurrido desde la fecha alta ajuste o suscripción hasta la fecha j
	 * @param m Número de años en que aplicamos un primer interés técnico
	 * @param i1 Primer interés técnico
	 * @param i2 Segundp interés técnico 
	 * @param t Anualidad del cálculo
	 * @return vpost Porcentaje de actualización
	 */
	public static BigDecimal vpost (final Integer j, final BigDecimal m, final BigDecimal i1, final BigDecimal i2,
			final Integer t) {
		// Inicio variables locales
		BigDecimal vpost = BigDecimal.ZERO;
		BigDecimal unoMasI1Entre100;
		BigDecimal unoMasI2Entre100;
		// Fin variables locales
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio Funcion << VPOST >> de la clase FuncionesVBX, para la entrada  j = {}, M = {},"
					+" I1 = {}, I2 = {}, T = {}", 
					j, m, i1, i2, t);
		}
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesVBX.validarParamEntradaFuncionVpost(j, m, i1, i2, t);
		
		unoMasI1Entre100 = BigDecimal.ONE.add(i1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		unoMasI2Entre100 = BigDecimal.ONE.add(i2.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		
		BigDecimal tbd = BigDecimal.valueOf(t);
		BigDecimal jbd = BigDecimal.valueOf(j);
		
		if (j >= t && j < m.intValue()) {
			vpost = Util.pow(unoMasI1Entre100, -(j-t+ConstantsFunciones.CTE_1));
		}
		else if (m.intValue() > t && m.intValue() <= j ) {
			vpost = Util.pow(unoMasI1Entre100, m.subtract(tbd).negate()).multiply(
					Util.pow(unoMasI2Entre100, (jbd.subtract(m).add(ConstantsFunciones.CTE_OPER_1)).negate()));
		}
		else if (t >= m.intValue()) {
			vpost = Util.pow(unoMasI2Entre100, -(j - t + ConstantsFunciones.CTE_1));
		}
			
		if (FuncionesVBX.LOG.isTraceEnabled()) {
				FuncionesVBX.LOG.trace("Fin Funcion << VPOST >> de la clase FuncionesVBX, con resultado vpost = {} ", vpost);
		}
		
		return vpost;		
	}
	
	/**
	 * Función auxiliar de la provisión VBX160
	 * @param difer Diferimiento de la renta
	 * @param x Edad a fecha de cálculo
	 * @param w Límite de la tabla de mortalidad
	 * @param ppr Porcentaje revalorización de la renta
	 * @param t Anualidad del cálculo 
	 * @param valoresTabMort Valores de la tabla de mortalidad
	 * @param m Número de años en que aplicamos un primer interés tecnico 
	 * @param i1 Primer interés técnico
	 * @param i2 Segundo interés técnico
	 * @return arxt Renta actuarial a una cabeza
	 */
	public static BigDecimal arxt (final Integer difer, final Integer x, final Integer w, final BigDecimal ppr,
			final Integer t, final List<BigDecimal> valoresTabMort, BigDecimal m, BigDecimal i1, BigDecimal i2 ) {
				
		// Inicio variables locales
		BigDecimal arxt = BigDecimal.ZERO;
		Integer varLimInf;
		Integer varXj;
		BigDecimal varLxj1;
		Integer varXT;
		BigDecimal varLxT;
		BigDecimal varVpostj;
		// Fin variables locales
			
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio Funcion << ARXT >> de la clase FuncionesVBX, para la entrada difer = {}, x = {},"
					+ "w = {}, ppr = {}, T = {}, valoresTabMort = {}, M = {}, I1 = {}, I2 = {}", 
					difer, x, w, ppr, t, valoresTabMort, m, i1, i2);
			}
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesVBX.validarParamEntradaFuncionArxt(difer, x, w, ppr, t, valoresTabMort, m, i1, i2);
		
		if (t < difer) {
			varLimInf = difer;			
		} else {
			varLimInf = t;
		}
		
		//Se calcula la primera potencia fuera del bucle para optimizar operaciones
		BigDecimal opPpr = BigDecimal.ONE.add(ppr.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		BigDecimal potencia = Util.pow(opPpr, varLimInf-difer);
		
		/** Se calcula el valor de arxt como el sumatorio desde j=  varLimInf hasta j = w – x de:
		 * 		(varLxj1〖*(1+ppr/100)〗^((j-difer)) * varVpostj)  / varLxT
		 */
		for (int j=varLimInf; j<=w-x; j++) {
			
			varXj = x + j + ConstantsFunciones.CTE_1;
			varLxj1 = Util.getVarLxEntero(varXj, valoresTabMort);
			
			varXT = x + t;
			varLxT = Util.getVarLxEntero(varXT, valoresTabMort);
			
			if (j > varLimInf){
				potencia = potencia.multiply(opPpr);
			} 
			
			varVpostj = vpost(j, m, i1, i2, t);
			
			BigDecimal dividendo = varLxj1.multiply(potencia).multiply(varVpostj);
			
			if (!varLxT.equals(BigDecimal.ZERO) && !dividendo.equals(BigDecimal.ZERO)){
				arxt = arxt.add(dividendo.divide(varLxT, ConstantsFunciones.MATH_CONTEXT));
			}
					
		}
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Fin Funcion << ARXT >> de la clase FuncionesVBX, con resultado arxt = {} ", arxt);
		}
				
		return arxt;	
	}
	
	/**
	 * Función a(f)rx+t. Función auxiliar de la provisión VBX160.
	 * @param difer Diferimiento de la renta
	 * @param x Edad a fecha de cálculo
	 * @param w Límite de la tabla de mortalidad
	 * @param ppr Porcentaje revalorización de la renta
	 * @param t Anualidad del cálculo
	 * @param lstValoresTabMort Valores de la tabla de mortalidad
	 * @param m Número de años en que aplicamos un primer interés técnico
	 * @param i1 Primer Interés técnico
	 * @param i2 Segundo Interés técnico
	 * @param f Forma de pago de la renta
	 * @return
	 */
	public static BigDecimal afrxt(final Integer difer, final Integer x, final Integer w, final BigDecimal ppr,
			final Integer t, final List<BigDecimal> lstValoresTabMort, final BigDecimal m, final BigDecimal i1, final BigDecimal i2,
			final Integer f){
		//Inicio variables locales
		BigDecimal afrxt = BigDecimal.ZERO;
		BigDecimal vararxt = BigDecimal.ZERO;
		BigDecimal varArxt = BigDecimal.ZERO;
		//Fin variables locales	
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio Funcion << a(f)rx+t >> de la clase FuncionesVBX, para la entrada difer = {},"
					+" x = {}, w = {}, ppr = {}, t = {}, lstValoresTabMort = {}, m = {}, i1 = {}, i2 = {}, f = {}",  
					difer, x, w, ppr, t, lstValoresTabMort, m, i1, i2, f);
		}
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesVBX.validarParamEntradaFuncionAarxtyAfrxtParte1(difer, x, w, ppr, t);
		ValidacionesFuncionesVBX.validarParamEntradaFuncionAfrxtParte2(lstValoresTabMort, m, i1, i2, f);
		
		vararxt = arxt (difer, x, w, ppr, t, lstValoresTabMort, m, i1, i2);
		varArxt = aarxt(difer, x, w, ppr, t, lstValoresTabMort, m, i1, i2);

		//Se halla el valor de a(f)rx+t= (F+1)*vararxt + (F-1)*varärxt
		afrxt = (BigDecimal.valueOf(f+1).multiply(vararxt)).add(BigDecimal.valueOf(f-1).multiply(varArxt));
		
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Fin Funcion << a(f)rx+t >> de la clase FuncionesVBX, con resultado afrxt = {} ", afrxt);
		}
		
		return afrxt;
	}

    /**
     * Función auxiliar agtr de la provisión VBX162
     * @param difer Diferimiento de la renta
     * @param x Edad a fecha de cálculo
     * @param w Límite de la tabla de mortalidad
     * @param ppr Porcentaje revalorización de la renta
     * @param t Anualidad del cálculo
     * @param valoresTabMort Valores de la tabla de mortalidad
     * @param m Número de años en que aplicamos un primer interés técnico
     * @param i1 Primer interés técnico
     * @param i2 Segundo interés técnico
     * @param f Forma de pago de la renta
     * @param agp Número de periodos garantizados, en años completos redondeando por exceso
     * @return agtr Renta actuarial a una cabeza
     */
	public static BigDecimal agtr (Integer difer, Integer x, Integer w, BigDecimal ppr, Integer t, List<BigDecimal> valoresTabMort,
			BigDecimal m, BigDecimal i1, BigDecimal i2, Integer f, Integer agp) {
		// Inicio variables locales
		BigDecimal agtr = BigDecimal.ZERO;
		Integer varLimInferior;
		BigDecimal opVarVpretj;
		BigDecimal opVarVpostj;
		BigDecimal opPpr;
		BigDecimal potencia;
		// Fin variables locales
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio Funcion << AGTR >> de la clase FuncionesVBX, para la entrada difer = {}, x = {},"
					+ "w = {}, ppr = {}, t = {}, valoresTabMort = {}, M = {}, I1 = {}, I2 = {}, F = {}, AGP = {}", 
					difer, x, w, ppr, t, valoresTabMort, m, i1, i2, f, agp);
			}
		
		// Validaciones parámetros de entrada
		ValidacionesFuncionesVBX.validarParamEntradaFuncionAgtr(difer, x, w, ppr, t, valoresTabMort, m, i1, i2, f, agp);
		
		if (t < difer) {
			varLimInferior = difer;
		}
		else {
			varLimInferior = t;
		}
		
		BigDecimal fMenos1 = BigDecimal.valueOf(f - ConstantsFunciones.CTE_1);
		BigDecimal fMas1 = BigDecimal.valueOf(f + ConstantsFunciones.CTE_1);
		
		
		
		//Se calcula la primera potencia fuera del bucle para optimizar operaciones
		opPpr = BigDecimal.ONE.add(ppr.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		potencia = Util.pow(opPpr, varLimInferior-difer);
		
		/**
		 * Se calcula el valor de AGTr como el sumatorio desde j = difer hasta j = difer+agp-1 de:
		 * 		((1+ppr/100)^((j-difer)) * [varVpretj*(F-1) +  varVpostj*(F+1 ) ])  /  2
		 */
		for (int j=varLimInferior; j<=difer+agp-ConstantsFunciones.CTE_1; j++) {
			
			if (j > varLimInferior){
				potencia = potencia.multiply(opPpr);
			}
			
			opVarVpretj = FuncionesVBX.vpret(j, m, i1, i2, t).multiply(fMenos1);
			opVarVpostj = FuncionesVBX.vpost(j, m, i1, i2, t).multiply(fMas1);
			
			agtr = agtr.add(potencia.multiply(opVarVpretj.add(opVarVpostj)).multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_5));	
	
		}
		
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Fin Funcion << AGTR >> de la clase FuncionesVBX, con resultado AGTR = {} ", agtr);
		}
		
		return agtr;
		
	}
	
	/**
	 * Función auxiliar arx+difer+AGP  de la provisión VBX162. 
	 * @param difer Diferimiento de la renta
	 * @param x Edad a fecha de cálculo
	 * @param w Límite de la tabla de mortalidad
	 * @param ppr Porcentaje revalorización de la renta
	 * @param t Anualidad del cálculo
	 * @param valoresTabMort Valores de la tabla de mortalidad
	 * @param m Número de años en que aplicamos un primer interés técnico
	 * @param i1 Primer interés técnico
	 * @param i2 Segundo interés técnico
	 * @param agp Número de periodos garantizados, en años completos redondeados por exceso
	 * @param varLimSuperior 
	 * @return arxDiferAGP 	Renta actuarial a una cabeza
	 */
	public static BigDecimal arxDiferAGP (final Integer difer, final Integer x, final Integer w, final BigDecimal ppr, final Integer t,
			final List<BigDecimal> valoresTabMort, final BigDecimal m, final BigDecimal i1, final BigDecimal i2, final Integer agp, Integer varLimSuperior) {
		// Inicio variables locales
		BigDecimal arxDiferAGP = BigDecimal.ZERO;
		BigDecimal opPpr;
		BigDecimal varXj;
		BigDecimal varLxj1;
		BigDecimal varXDiferAGP;
		BigDecimal varLxDiferAGP;
		BigDecimal varVpostj; 
		BigDecimal potencia;
		// Fin variables locales
			
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio Funcion << arxDiferAGP >> de la clase FuncionesVBX, para la entrada difer = {}, x = {},"
					+ "w = {}, ppr = {}, t = {}, valoresTabMort = {}, m = {}, i1 = {}, i2 = {}, agp = {}, varLimSuperior = {}", 
					difer, x, w, ppr, t, valoresTabMort, m, i1, i2, agp, varLimSuperior);
			}
		
		// Validaciones parámetros de entrada
		ValidacionesFuncionesVBX.validarParamEntradaFuncionArxDiferAGPyAarxDiferAGP(difer, x, w, ppr, t, valoresTabMort, m, i1, i2, agp, varLimSuperior);

		//Se calcula la primera potencia fuera del bucle para optimizar operaciones
		opPpr = BigDecimal.ONE.add(ppr.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		potencia = Util.pow(opPpr, agp);
		
		/**
		 * Se calcula el valor de arxDiferAGP como el sumatorio desde j=  difer + AGP  hasta j = w – x de:
		 *  (varLxj1〖*(1+ppr/100)〗^((j-difer))*varVpostj)/varLxDiferAGP
		 */
		
		varXDiferAGP = BigDecimal.valueOf(x + difer + agp);
		varLxDiferAGP = Util.getVarLx(varXDiferAGP, valoresTabMort);
		
		if (!varLxDiferAGP.equals(BigDecimal.ZERO)){
			
			for (int j=difer+agp; j<=varLimSuperior; j++) {

				if (j > difer+agp){
					potencia = potencia.multiply(opPpr);
				}

				varXj = BigDecimal.valueOf(x + j + ConstantsFunciones.CTE_1);
				varLxj1 = Util.getVarLx(varXj, valoresTabMort);
				varVpostj = vpost(j, m, i1, i2, t);

				BigDecimal terminoArxt = varLxj1.multiply(potencia).multiply(varVpostj).divide(varLxDiferAGP,ConstantsFunciones.MATH_CONTEXT);
			
				arxDiferAGP = arxDiferAGP.add(terminoArxt);
				
			}
		}
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Fin Funcion << arxDiferAGP >> de la clase FuncionesVBX, con resultado arxDiferAGP = {} ", arxDiferAGP);
		}
		
		return arxDiferAGP;
		
	}
	
	
	/** 
	 * FFunción auxiliar aarx+ difer+AGP  de la provisión VBX162  
	 * @param difer Diferimiento de la renta
	 * @param x Edad a fecha de cálculo
	 * @param w Límite de la tabla de mortalidad
	 * @param ppr Porcentaje revalorización de la renta
	 * @param t Anualidad del cálculo
	 * @param valoresTabMort Valores de la tabla de mortalidad
	 * @param m Número de años en que aplicamos un primer interés técnico
	 * @param i1 Primer interés técnico
	 * @param i2 Segundo interés técnico
	 * @param agp Número de periodos garantizados, en años completos redondeados por exceso
	 * @param varLimSuperior 
	 * @return arxDiferAGP 	Renta actuarial a una cabeza
	 */
	public static BigDecimal aarxDiferAGP (final Integer difer, final Integer x, final Integer w, final BigDecimal ppr, final Integer t,
			final List<BigDecimal> valoresTabMort, final BigDecimal m, final BigDecimal i1, final BigDecimal i2, final Integer agp, Integer varLimSuperior) {
		// Inicio variables locales
		BigDecimal aarxDiferAGP = BigDecimal.ZERO;
		BigDecimal varXj;
		BigDecimal varLxj;
		BigDecimal varXDiferAGP;
		BigDecimal varLxDiferAGP;
		BigDecimal varVpretj;
		BigDecimal opPpr;
		BigDecimal potencia;
		// Fin variables locales
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio Funcion << aarxDiferAGP >> de la clase FuncionesVBX, para la entrada difer = {}, x = {},"
					+ "w = {}, ppr = {}, t = {}, valoresTabMort = {}, m = {}, i1 = {}, i2 = {}, agp = {}", 
					difer, x, w, ppr, t, valoresTabMort, m, i1, i2, agp);
			}
		
		//Validaciones parámetros de entrada
		ValidacionesFuncionesVBX.validarParamEntradaFuncionArxDiferAGPyAarxDiferAGP(difer, x, w, ppr, t, valoresTabMort, m, i1, i2, agp, varLimSuperior);
		
		//Se calcula la primera potencia fuera del bucle para optimizar operaciones
		opPpr = BigDecimal.ONE.add(ppr.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		//potencia = BigDecimal.ONE; //Como en el primer termino del sumatorio opPpr se eleva a j(=difer) - difer, el resultado de la potencia es 1
		potencia = Util.pow(opPpr, agp);
		
		/**
		 * Se calcula el valor de aarxDiferAGP como el sumatorio desde j=  difer  hasta j = w – x de:
		 * (varLxj〖*(1+ppr/100)〗^((j-difer))*varVpretj)/varLxDiferAGP		
		 */
		
		varXDiferAGP = BigDecimal.valueOf(x + difer + agp);
		varLxDiferAGP = Util.getVarLx(varXDiferAGP, valoresTabMort);
		
		for (int j=difer+agp; j<=varLimSuperior; j++) {
			
			if (j > difer){
				potencia = potencia.multiply(opPpr);
			}
			
			varXj = BigDecimal.valueOf(x + j);
			varLxj = Util.getVarLx(varXj, valoresTabMort);
			varVpretj = vpret(j, m, i1, i2, t);
			
			BigDecimal terminoAartx = varLxj.multiply(potencia).multiply(varVpretj).divide(varLxDiferAGP, ConstantsFunciones.MATH_CONTEXT);
			
			aarxDiferAGP = aarxDiferAGP.add(terminoAartx);
			
		}
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Fin Funcion << aarxDiferAGP >> de la clase FuncionesVBX, con resultado aarxDiferAGP = {} ", aarxDiferAGP);
		}
		
		
		
		return aarxDiferAGP;
	}
	
	/**
	 * Función auxiliar a(f)rx+difer+AGP  de la provisión VBX162
	 * @param difer Diferimiento de la renta
	 * @param x Edad a fecha de cálculo
	 * @param w Límite de la tabla de mortalidad 
	 * @param ppr Porcentaje revalorización de la renta
	 * @param t Anualidad del cálculo
	 * @param valoresTabMort Valores de la tabla de mortalidad
	 * @param m Número de años en que aplicamos un primer interés técnico
	 * @param i1 Primer interés técnico
	 * @param i2 Segundo interés técnico
	 * @param f Forma de pago de la renta
	 * @param ag Número de periodos garantizados, en años completos
	 * @param agp Número de periodos garantizados, en años completos redondeando por exceso
	 * @param limSuperior Límite superior para las funciones arxDiferAGP y aarxDiferAGP 
	 * @return afrxDiferAGP Renta actuarial a una cabeza
	 */
	public static BigDecimal afrxDiferAGP (final Integer difer, final Integer x, final Integer w, final BigDecimal ppr, final Integer t,
			final List<BigDecimal> valoresTabMort, final BigDecimal m, final BigDecimal i1, final BigDecimal i2, final Integer f,
			final Integer ag, final Integer agp, final Integer varLimSuperior) {
		
		// Inicio variables locales
		BigDecimal afrxDiferAGP;
		BigDecimal vararxDiferAGP;
		BigDecimal varaarxDiferAGP; 
		// Fin variables locales
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio Funcion << afrxDiferAGP >> de la clase FuncionesVBX, para la entrada difer = {}, x = {},"
					+ "w = {}, ppr = {}, t = {}, valoresTabMort = {}, m = {}, i1 = {}, i2 = {}, f = {}, ag = {},  agp = {}, varLimSuperior = {}", 
					difer, x, w, ppr, t, valoresTabMort, m, i1, i2, f, ag, agp, varLimSuperior);
		}
		
		//Validaciones parámetros de entrada
		ValidacionesFuncionesVBX.validarParamEntradaFuncionAfrxDiferAGP(difer, x, w, ppr, t, valoresTabMort, m, i1, i2, f, ag, agp, varLimSuperior);
		
		/**
		 * Se calcula el valor de afrxDiferAGP como: (F+1)*varArxDiferAGP+(F-1)*varAarxDiferAGP 		
		 */
		
		vararxDiferAGP = arxDiferAGP(difer, x, w, ppr, t, valoresTabMort, m, i1, i2, agp, varLimSuperior);
		varaarxDiferAGP = aarxDiferAGP(difer, x, w, ppr, t, valoresTabMort, m, i1, i2, agp, varLimSuperior);
		
		afrxDiferAGP = BigDecimal.valueOf(f + ConstantsFunciones.CTE_1).multiply(vararxDiferAGP).add(
				BigDecimal.valueOf(f - ConstantsFunciones.CTE_1).multiply(varaarxDiferAGP));
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Fin Funcion << afrxDiferAGP >> de la clase FuncionesVBX, con resultado afrxDiferAGP = {} ", afrxDiferAGP);
		}
		
		return afrxDiferAGP;
	}	
	
	/**
	 * Función auxiliar arxy+t de la provisión VBX164
	 * @param difer Diferimiento de la renta
	 * @param x Edad primera cabeza a fecha de cálculo 
	 * @param wx Límite de la tabla de mortalidad
	 * @param y Edad segunda cabeza a fecha de cálculo
	 * @param wy Límite de la tabla de mortalidad segunda cabeza
	 * @param ppr Porcentaje revalorización de la renta
	 * @param t Anualidad del cálculo
	 * @param valoresTabMortX Valores de la tabla de mortalidad primera cabeza
	 * @param valoresTabMortY Valores de la tabla de mortalidad segunda cabeza
	 * @param m Número de años en que aplicamos un primer interés técnico
	 * @param i1 Primer interés técnico
	 * @param i2 Segundo interés técnico
	 * @return arxyt Renta actuarial a dos cabezas
	 */
	public static BigDecimal arxyt (final Integer difer, final Integer x, final Integer wx, final Integer y, final Integer wy,
			final BigDecimal ppr, final Integer t, final List<BigDecimal> valoresTabMortX, final List<BigDecimal> valoresTabMortY,
			final BigDecimal m, final BigDecimal i1, final BigDecimal i2) {
		// Inicio variables locales
		BigDecimal arxyt = BigDecimal.ZERO;
		Integer varLimInferior;
		Integer varLimSuperior;
		BigDecimal varXj;
		BigDecimal varLxj1;
		BigDecimal varXT;
		BigDecimal varLxT;
		BigDecimal varYj;
		BigDecimal varLyj1;
		BigDecimal varYt;
		BigDecimal varLyT;	
		BigDecimal varVpostj;
		BigDecimal opPpr = BigDecimal.ZERO;
		BigDecimal unoMasPprEntre100;
		// Fin variables locales
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio Funcion << arxyt >> de la clase FuncionesVBX, para la entrada difer = {}, x = {}, wx = {},"
					+ "y = {}, wy = {}, ppr = {}, t = {}, valoresTabMortX = {}, valoresTabMortY = {}, m = {}, i1 = {}, i2 = {}", 
					difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY, m, i1, i2);
		}
		
		// Validación de los parámetros de entrada
		ValidacionesFuncionesVBX.validarParamEntradaFuncionArxytAarxyt(difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY, m, i1, i2);
		
		
		 if (t < difer) {
			 varLimInferior = difer;
		 } else {
			 varLimInferior = t;
		 }
		 
		 varLimSuperior = Math.max(wx, wy) - Math.max(x, y);
		 
		 varXT = BigDecimal.valueOf(x + t);
		 varLxT = Util.getVarLx(varXT, valoresTabMortX);
		 
		 varYt = BigDecimal.valueOf(y+ t);
		 varLyT = Util.getVarLx(varYt, valoresTabMortY);
		 
		 /**
		  *  Se calcula el valor de arxyt como el sumatorio desde j=varLimInferior hasta j=varLimsuperior de:
		  *  (varLxj1* varLyj1〖*(1+ppr/100)〗^((j-difer))*varVpostj)/(varLxT* varLyT)
		  */
		 
		 if (!varLxT.equals(BigDecimal.ZERO) && !varLyT.equals(BigDecimal.ZERO)){
			 
			 BigDecimal divisor = varLxT.multiply(varLyT);
			 
			 unoMasPprEntre100 = BigDecimal.ONE.add(ppr.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		 
			 for (int j=varLimInferior; j<=varLimSuperior; j++) {			 
				 //Se calcula la potencia para el primer término del sumatorio y luego se va incrementando 
				 //para optimizar operaciones
				 if (j==varLimInferior){
					 opPpr = Util.pow(unoMasPprEntre100, j-difer);
				 } else {
					 opPpr = opPpr.multiply(unoMasPprEntre100);
				 }
				 
				 varXj = BigDecimal.valueOf(x + j + ConstantsFunciones.CTE_1);
				 varLxj1 = Util.getVarLx(varXj, valoresTabMortX);
				 
				 varYj = BigDecimal.valueOf(y + j + ConstantsFunciones.CTE_1);
				 varLyj1 = Util.getVarLx(varYj, valoresTabMortY);
				 
				 varVpostj = FuncionesVBX.vpost(j, m, i1, i2, t);
				 
				 BigDecimal dividendo = varLxj1.multiply(varLyj1).multiply(opPpr).multiply(varVpostj);
				 
				 if (!BigDecimal.ZERO.equals(dividendo)){
					 arxyt = arxyt.add(dividendo.divide(divisor, ConstantsFunciones.MATH_CONTEXT));
				 }
				 
			 }
		 
		 }
		
				
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Fin Funcion << arxyt >> de la clase FuncionesVBX, con resultado arxyt = {} ", arxyt);
		}
		
		return arxyt;
	}


	/**
	 * Función auxiliar aarxyt de la provisión VBX164
	 * @param difer Diferimiento de la renta
	 * @param x Edad primera cabeza a fecha de cálculo
	 * @param wx Límite de la tabla de mortalidad primera cabeza
	 * @param y Edad segunda cabeza a fecha de cálculo
	 * @param wy Límite de la tabla de mortalidad segunda cabeza
	 * @param ppr Porcentaje revalorización de la renta
	 * @param t Anualidad de cálculo
	 * @param valoresTabMortX Valores de la tabla de mortalidad primera cabeza 
	 * @param valoresTabMortY Valores de la tabla de mortalidad segunda cabeza
	 * @param m Número de años en que aplicamos un primer interés técnico
	 * @param i1 Primer interés técnico
	 * @param i2 Segundo interés técnico
	 * @return aarxyt Renta actuarial a dos cabezas
	 */
	public static BigDecimal aarxyt (final Integer difer, final Integer x, final Integer wx, final Integer y, final Integer wy,
			final BigDecimal ppr, final Integer t, final List<BigDecimal> valoresTabMortX, List<BigDecimal> valoresTabMortY,
			final BigDecimal m, final BigDecimal i1, final BigDecimal i2) {
		// Inicio variables locales
		BigDecimal aarxyt = BigDecimal.ZERO;
		Integer varLimInferior;
		Integer varLimSuperior;
		BigDecimal varXj;
		BigDecimal varLxj;
		BigDecimal varXT;
		BigDecimal varLxT;
		BigDecimal varYj;
		BigDecimal varLyj;
		BigDecimal varYT;
		BigDecimal varLyT;
		BigDecimal varVpretj;
		BigDecimal opPpr = BigDecimal.ZERO;
		BigDecimal unoMasPprEntre100;
		// Fin variables locales
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio Funcion << aarxyt >> de la clase FuncionesVBX, para la entrada difer = {}, x = {}, wx = {},"
					+ "y = {}, wy = {}, ppr = {}, t = {}, valoresTabMortX = {}, valoresTabMortY = {}, m = {}, i1 = {}, i2 = {}",
					difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY, m, i1, i2);
		}
		
		// Validaciones parámetros de entrada
		ValidacionesFuncionesVBX.validarParamEntradaFuncionArxytAarxyt(difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY, m, i1, i2);
		
		
		if (t < difer) {
			varLimInferior = difer;
		} else {
			varLimInferior = t;
		}
		
		varLimSuperior = Math.max(wx, wy) - Math.max(x, y);
		
		varXT = BigDecimal.valueOf(x + t);
		varLxT = Util.getVarLx(varXT, valoresTabMortX);
		
		varYT = BigDecimal.valueOf(y + t);
		varLyT = Util.getVarLx(varYT, valoresTabMortY);
		
		/**
		 * Se calcula el valor de aarxyt como el sumatorio desde j=varLimInferior hasta j=varLimsuperior de:
		 *   (varLxj〖*varLyj*(1+ppr/100)〗^((j-difer))*varVpretj)/(varLxT* varLyT)
		 */
		
		if (!varLxT.equals(BigDecimal.ZERO) && !varLyT.equals(BigDecimal.ZERO)){
			
			BigDecimal divisor = varLxT.multiply(varLyT);
		
			unoMasPprEntre100 = BigDecimal.ONE.add(ppr.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
			
			for (int j=varLimInferior; j<=varLimSuperior; j++) {
				 //Se calcula la potencia para el primer término del sumatorio y luego se va incrementando 
				 //para optimizar operaciones
				 if (j==varLimInferior){
					 opPpr = Util.pow(unoMasPprEntre100, j-difer);
				 } else {
					 opPpr = opPpr.multiply(unoMasPprEntre100);
				 }
				
				varXj = BigDecimal.valueOf(x + j);
				varLxj = Util.getVarLx(varXj, valoresTabMortX);
				
				varYj = BigDecimal.valueOf(y + j);
				varLyj = Util.getVarLx(varYj, valoresTabMortY);
				
				varVpretj = FuncionesVBX.vpret(j, m, i1, i2, t);
				
				BigDecimal dividendo = varLxj.multiply(varLyj).multiply(opPpr).multiply(varVpretj);
				
				if (!BigDecimal.ZERO.equals(dividendo)){
					aarxyt = aarxyt.add(dividendo.divide(divisor, ConstantsFunciones.MATH_CONTEXT));
				}
	
			}
		
		}
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Fin Funcion << aarxyt >> de la clase FuncionesVBX, con resultado aarxyt = {} ", aarxyt);
		}
		
		return aarxyt;
	}
	
	
	/**
	 * Función auxiliar a(f)rxy+t de la provisión VBX164
	 * @param difer Diferimiento de la renta
	 * @param x Edad primera cabeza a fecha de cálculo
	 * @param wx Límite de la tabla de mortalidad primera cabeza
	 * @param y Edad segunda cabeza a fecha de cálculo
	 * @param wy Límite de la tabla de mortalidad segunda cabeza
	 * @param ppr Porcentaje revaloriación de la renta
	 * @param t Anualidad del cálculo
	 * @param valoresTabMortX Valores de la tabla de mortalidad primera cabeza
	 * @param valoresTabMortY Valores de la tabla de mortalidad segunda cabeza
	 * @param m Número de años en que aplicamos un primer interés técnico
	 * @param i1 Primer interés técnico
	 * @param i2 Segundo interés técnico
	 * @param f Forma de pago de la renta 
	 * @return afrxyt Renta actuarial a dos cabezas
	 */
	public static BigDecimal afrxyt (final Integer difer, final Integer x, final Integer wx, final Integer y, final Integer wy,
			final BigDecimal ppr, final Integer t, final List<BigDecimal> valoresTabMortX, final List<BigDecimal> valoresTabMortY,
			final BigDecimal m, final BigDecimal i1, final BigDecimal i2, final Integer f) {
		// Inicio varaibles locales
		BigDecimal afrxyt;
		BigDecimal vararxyt;
		BigDecimal varaarxyt;
		// Fin variables locales
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio Funcion << afrxyt >> de la clase FuncionesVBX, para la entrada difer = {}, x = {}, wx = {},"
					+ "y = {}, wy = {}, ppr = {}, t = {}, valoresTabMortX = {}, valoresTabMortY = {}, m = {}, i1 = {}, i2 = {}, f = {}",
					difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY, m, i1, i2, f);
		}
		
		/**
		 *  Se calcula el valor de afrxyt como: (F+1)*vararxyt + (F-1)*varaarxyt 
		 */
		
		// Validación parámetros de entrada
		ValidacionesFuncionesVBX.validarParamEntradaFuncionAfrxyt(difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY, m, i1, i2, f);
		
		vararxyt = FuncionesVBX.arxyt(difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY, m, i1, i2);
		varaarxyt = FuncionesVBX.aarxyt(difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY, m, i1, i2);
		
		// a(f)rx+t=(F+1)*vararxt +(F-1)*varärxt 
		afrxyt = BigDecimal.valueOf(f + ConstantsFunciones.CTE_1).multiply(vararxyt)
				.add(BigDecimal.valueOf(f - ConstantsFunciones.CTE_1).multiply(varaarxyt));
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Fin Funcion << afrxyt >> de la clase FuncionesVBX, con resultado afrxyt = {} ", afrxyt);
		}
		
		return afrxyt;	
	}
	
	/**
	 * Función auxiliar arxy+difer+AGP  de la provisión VBX165. 
	 * @param difer Diferimiento de la renta.
	 * @param x Edad  primera cabeza a fecha de cálculo
	 * @param wx Límite de la tabla de mortalidad primera cabeza
	 * @param y Edad segunda cabeza a fecha de cálculo
	 * @param wy Límite de la tabla de mortalidad segunda cabeza
	 * @param ppr Porcentaje revalorización de la renta
	 * @param t Anualidad del cálculo
	 * @param valoresTabMortX Valores de la tabla de mortalidad primera cabeza
	 * @param valoresTabMortY Valores de la tabla de mortalidad segunda cabeza
	 * @param m Número de años en que aplicamos un primer interés técnico
	 * @param i1 Primer Interés técnico
	 * @param i2 Segundo Interés técnico
	 * @param agp Número de periodos garantizados, en años completos redondeado por exceso
	 * @return arxyDiferAGP Renta Actuarial a dos cabeza
	 */
	public static BigDecimal arxyDiferAGP (final Integer difer, final Integer x, final Integer wx, final Integer y, 
			final Integer wy, final BigDecimal ppr, final Integer t, final List<BigDecimal> valoresTabMortX, final List<BigDecimal> valoresTabMortY,
			final BigDecimal m, final BigDecimal i1, final BigDecimal i2, final Integer agp){
		// Inicio varaibles locales
		BigDecimal arxyDiferAGP = BigDecimal.ZERO;
		Integer varLimSup;
		Integer varXDiferAGP;
		BigDecimal varLxDiferAGP;
		Integer varYDiferAGP;
		BigDecimal varLyDiferAGP;
		Integer varXj;
		Integer varYj;
		BigDecimal varLxj1;
		BigDecimal varLyj1;
		BigDecimal varPrp = BigDecimal.ONE;
		BigDecimal varVpostj;
		// Fin variables locales
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio Funcion << arxyDiferAGP >> de la clase FuncionesVBX, para la entrada difer = {}, x = {}, wx = {},"
					+ "y = {}, wy = {}, ppr = {}, t = {}, valoresTabMortX = {}, valoresTabMortY = {}, m = {}, i1 = {}, i2 = {}, agp = {}",
					difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY, m, i1, i2, agp);
		}
		
		//Validaciones parámetros de entrada
		ValidacionesFuncionesVBX.validarParamEntradaFuncionesArxyDiferAGP(difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY, m, i1, i2, agp);
		
		//Se calcula el limite superior del sumatorio
		varLimSup = Math.max(wx, wy) - Math.max(x, y) - agp - difer;
		
		//Se calculan las variables que no varían entre témrinos del sumatorio
		BigDecimal unoMasPrpEntre100 = BigDecimal.ONE.add(ppr.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		
		varXDiferAGP = x + difer + agp;
		varLxDiferAGP = Util.getVarLxEntero(varXDiferAGP, valoresTabMortX);
		
		varYDiferAGP = y + difer + agp;
		varLyDiferAGP = Util.getVarLxEntero(varYDiferAGP, valoresTabMortY);
		
		BigDecimal divisor = varLxDiferAGP.multiply(varLyDiferAGP);
		
		
		/**
		 * Se calcula arxyDiferAGP como el Sumatorio desde j=difer + agp hasta j=varLimSup:
		 * 		(varLxj1 * varLyj1 *〖(1+ppr/100)〗^((j-difer)) * varVpostj) / (varLxDiferAGP* varLyDiferAGP)
		 */
		for (int j=difer+agp; j<= varLimSup; j++){
			varXj = x + j + 1;
			varLxj1 = Util.getVarLxEntero(varXj, valoresTabMortX);
			
			varYj = y + j + 1;
			varLyj1 = Util.getVarLxEntero(varYj, valoresTabMortY);
			
			varVpostj = vpost(j, m, i1, i2, t);
			
			//Se calcula la potencia para el primer término del sumatorio y luego se va incrementando 
			//para optimizar operaciones
			if (j == difer+agp){
				varPrp = Util.pow(unoMasPrpEntre100, j-difer);
			} else {
				varPrp = varPrp.multiply(unoMasPrpEntre100);
			}
			
			BigDecimal dividendo = varLxj1.multiply(varLyj1).multiply(varPrp).multiply(varVpostj);
			
			BigDecimal terminoArxyt = dividendo.divide(divisor, ConstantsFunciones.MATH_CONTEXT);
			
			arxyDiferAGP = arxyDiferAGP.add(terminoArxyt);
			
		}
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Fin Funcion << arxyDiferAGP >> de la clase FuncionesVBX, con resultado arxyDiferAGP = {} ", arxyDiferAGP);
		}
		
		return arxyDiferAGP;
	}
	
	/**
	 * Función auxiliar ärxy+ difer+AGP  de la provisión VBX165 
	 * @param difer Diferimiento de la renta.
	 * @param x Edad  primera cabeza a fecha de cálculo
	 * @param wx Límite de la tabla de mortalidad primera cabeza
	 * @param y Edad segunda cabeza a fecha de cálculo
	 * @param wy Límite de la tabla de mortalidad segunda cabeza
	 * @param ppr Porcentaje revalorización de la renta
	 * @param t Anualidad del cálculo
	 * @param valoresTabMortX Valores de la tabla de mortalidad primera cabeza
	 * @param valoresTabMortY Valores de la tabla de mortalidad segunda cabeza
	 * @param m Número de años en que aplicamos un primer interés técnico
	 * @param i1 Primer Interés técnico
	 * @param i2 Segundo Interés técnico
	 * @param agp Número de periodos garantizados, en años completos redondeado por exceso
	 * @return aarxyDiferAGP Renta Actuarial a dos cabezas
	 */
	public static BigDecimal aarxyDiferAGP (final Integer difer, final Integer x, final Integer wx, final Integer y, 
			final Integer wy, final BigDecimal ppr, final Integer t, final List<BigDecimal> valoresTabMortX, final List<BigDecimal> valoresTabMortY,
			final BigDecimal m, final BigDecimal i1, final BigDecimal i2, final Integer agp){
		// Inicio varaibles locales
		BigDecimal aarxyDiferAGP = BigDecimal.ZERO;
		Integer varLimSup;
		Integer varXDiferAGP;
		BigDecimal varLxDiferAGP;
		Integer varYDiferAGP;
		BigDecimal varLyDiferAGP;
		Integer varXj;
		Integer varYj;
		BigDecimal varLxj;
		BigDecimal varLyj;
		BigDecimal varPrp = BigDecimal.ONE;
		BigDecimal varVpretj;
		// Fin variables locales
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio Funcion << aarxyDiferAGP >> de la clase FuncionesVBX, para la entrada difer = {}, x = {}, wx = {},"
					+ "y = {}, wy = {}, ppr = {}, t = {}, valoresTabMortX = {}, valoresTabMortY = {}, m = {}, i1 = {}, i2 = {}, agp = {}",
					difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY, m, i1, i2, agp);
		}
		
		//Validaciones parámetros de entrada
		ValidacionesFuncionesVBX.validarParamEntradaFuncionesArxyDiferAGP(difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY, m, i1, i2, agp);
		
		//Se calcula el limite superior del sumatorio
		varLimSup = Math.max(wx, wy) - Math.max(x, y) - agp - difer;
		
		//Se calculan las variables que no varían entre témrinos del sumatorio
		BigDecimal unoMasPrpEntre100 = BigDecimal.ONE.add(ppr.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		
		varXDiferAGP = x + difer + agp;
		varLxDiferAGP = Util.getVarLxEntero(varXDiferAGP, valoresTabMortX);
		
		varYDiferAGP = y + difer + agp;
		varLyDiferAGP = Util.getVarLxEntero(varYDiferAGP, valoresTabMortY);
		
		BigDecimal divisor = varLxDiferAGP.multiply(varLyDiferAGP);
		
		
		/**
		 * Se calcula aarxyDiferAGP como el Sumatorio desde j=difer + agp hasta j=varLimSup:
		 * 		(varLxj * varLyj *〖(1+ppr/100)〗^((j-difer)) * varVpretj) / (varLxDiferAGP* varLyDiferAGP)
		 */
		for (int j=difer+agp; j<= varLimSup; j++){
			varXj = x + j;
			varLxj = Util.getVarLxEntero(varXj, valoresTabMortX);
			
			varYj = y + j;
			varLyj = Util.getVarLxEntero(varYj, valoresTabMortY);
			
			varVpretj = vpret(j, m, i1, i2, t);
			
			//Se calcula la potencia para el primer término del sumatorio y luego se va incrementando 
			//para optimizar operaciones
			if (j == difer+agp){
				varPrp = Util.pow(unoMasPrpEntre100, j-difer);
			} else {
				varPrp = varPrp.multiply(unoMasPrpEntre100);
			}
			
			BigDecimal dividendo = varLxj.multiply(varLyj).multiply(varPrp).multiply(varVpretj);
			
			BigDecimal terminoAarxyt = dividendo.divide(divisor, ConstantsFunciones.MATH_CONTEXT);
			
			aarxyDiferAGP = aarxyDiferAGP.add(terminoAarxyt);
			
		}
		
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Fin Funcion << aarxyDiferAGP >> de la clase FuncionesVBX, con resultado aarxyDiferAGP = {} ", aarxyDiferAGP);
		}
		
		return aarxyDiferAGP;
	}
	
	/**
	 * Función auxiliar a(f)rxy+Difer + AGP  de la provisión VBX165. 
	 * @param difer Diferimiento de la renta.
	 * @param x Edad  primera cabeza a fecha de cálculo
	 * @param wx Límite de la tabla de mortalidad primera cabeza
	 * @param y Edad segunda cabeza a fecha de cálculo
	 * @param wy Límite de la tabla de mortalidad segunda cabeza
	 * @param ppr Porcentaje revalorización de la renta
	 * @param t Anualidad del cálculo
	 * @param valoresTabMortX Valores de la tabla de mortalidad primera cabeza
	 * @param valoresTabMortY Valores de la tabla de mortalidad segunda cabeza
	 * @param m Número de años en que aplicamos un primer interés técnico
	 * @param i1 Primer Interés técnico
	 * @param i2 Segundo Interés técnico
	 * @param agp Número de periodos garantizados, en años completos redondeado por exceso
	 * @param f Forma de pago de la renta
	 * @return afrxyDiferAGP Renta Actuarial a una cabeza
	 */
	public static BigDecimal afrxyDiferAGP (final Integer difer, final Integer x, final Integer wx, final Integer y, 
			final Integer wy, final BigDecimal ppr, final Integer t, final List<BigDecimal> valoresTabMortX, final List<BigDecimal> valoresTabMortY,
			final BigDecimal m, final BigDecimal i1, final BigDecimal i2, final Integer agp, final Integer f){
		// Inicio varaibles locales
		BigDecimal afrxyDiferAGP = BigDecimal.ZERO;
		BigDecimal vararxyDiferAGP;
		BigDecimal varaarxyDiferAGP;
		// Fin variables locales
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio Funcion << afrxyDiferAGP >> de la clase FuncionesVBX, para la entrada difer = {}, x = {}, wx = {},"
					+ "y = {}, wy = {}, ppr = {}, t = {}, valoresTabMortX = {}, valoresTabMortY = {}, m = {}, i1 = {}, i2 = {}, agp = {}, f = {}",
					difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY, m, i1, i2, agp, f);
		}
		
		//Validaciones parámetros de entrada
		ValidacionesFuncionesVBX.validarParamEntradaFuncionAfrxyDiferAGP(difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY, m, i1, i2, agp, f);
		
		
		vararxyDiferAGP = arxyDiferAGP(difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY, m, i1, i2, agp);
		varaarxyDiferAGP = aarxyDiferAGP(difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY, m, i1, i2, agp);
		
		// Se calcula a(f)rx+difer+AGP  = (F+1) * vararxyDiferAGP + (F-1) * varärxyDiferAGP
		afrxyDiferAGP = (BigDecimal.valueOf(f + 1).multiply(vararxyDiferAGP)).add(BigDecimal.valueOf(f-1).multiply(varaarxyDiferAGP));
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Fin Funcion << afrxyDiferAGP >> de la clase FuncionesVBX, con resultado afrxyDiferAGP = {} ", afrxyDiferAGP);
		}
		
		return afrxyDiferAGP;
	}
	
	
	/**
	 * Función auxiliar af,rxy+t:AGP-t de la provisión VBX165 . 
	 * @param difer Diferimiento de la renta
	 * @param x Edad a fecha de cálculo
	 * @param wx Límite de la tabla de mortalidad primera cabeza
	 * @param y Edad segunda cabeza a fecha de cálculo
	 * @param wy Límite de la tabla de mortaldad segunda cabeza 
	 * @param ppr Porcentaje revalorización de la renta 
	 * @param t Anualidad del cálculo
	 * @param valoresTabMortX Valores de la tabla de mortalidad primera cabeza
	 * @param valoresTabMortY Valores de la tabla de mortalidad segunda cabeza
	 * @param m Número de años en que aplicamos un primer interés técnico
	 * @param i1 Primer interés técnico 
	 * @param i2 Segundo interés técnico
	 * @param f Forma de pago de la renta
	 * @param agp Número de periodos garantizados, en años completos redondeado por exceso
	 * @return Renta actuarial a dos cabezas 
	 */
	public static BigDecimal afrxytAGPt (final Integer difer, final Integer x, final Integer wx, final Integer y, final Integer wy,
			final BigDecimal ppr, final Integer t, final List<BigDecimal> valoresTabMortX, final List<BigDecimal> valoresTabMortY,
			final BigDecimal m, final BigDecimal i1, final BigDecimal i2, final Integer f, final Integer agp) {
		// Inicio variables locales
		BigDecimal afrxytAGPt = null;
		BigDecimal vararxytAGPt = null;
		BigDecimal varaarxytAGPt = null;
		// Fin variables locales
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio Funcion << afrxyt >> de la clase FuncionesVBX, para la entrada difer = {}, x = {}, wx = {},"
					+ "y = {}, wy = {}, ppr = {}, t = {}, valoresTabMortX = {}, valoresTabMortY = {}, m = {}, i1 = {}, i2 = {}, f = {}, agp = {}",
					difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY, m, i1, i2, f, agp);
		}
		
		/**
		 * Se calcula el valor de afrxytAGPt como: (F+1)*vararxytAGPt + (F-1)*varaarxytAGPt 
		 */
		
		// validación parámetros de entrada
		ValidacionesFuncionesVBX.validarParamEntradaFuncionAfrxytAGPt(difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY, m, i1, i2, f, agp);
		
		vararxytAGPt = FuncionesVBX.arxytAGPt(difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY, m, i1, i2, agp);
		varaarxytAGPt = FuncionesVBX.aarxytAGPt(difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY, m, i1, i2, agp);
	
		afrxytAGPt = BigDecimal.valueOf(f + ConstantsFunciones.CTE_1).multiply(vararxytAGPt).
				add(BigDecimal.valueOf(f - ConstantsFunciones.CTE_1).multiply(varaarxytAGPt));
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Fin Funcion << afrxytAGPt >> de la clase FuncionesVBX, con resultado afrxyt = {} ", afrxytAGPt);
		}
		
		return afrxytAGPt;
	}	
	
	
	/**
	 * Función auxiliar arxy+t:AG-t  de la provisión VBX165
	 * @param difer Diferimiento de la renta
	 * @param x Edad primera cabeza a fecha de cálculo
	 * @param wx Límite de la tabla de mortalidad
	 * @param y Edad segunda cabeza a fecha de cálculo
	 * @param wy Límite de la tabla de mortalidad segunda cabeza
	 * @param ppr Porcentaje revalorización de la renta
	 * @param t Anualidad del cálculo
	 * @param valoresTabMortX Valores de la tabla de mortalidad primera cabeza
	 * @param valoresTabMortY Valores de la tabla de mortalidad segunda cabeza
	 * @param m Número de años en que aplicamos un primer interés técnico
	 * @param i1 Primer interés técnico
	 * @param i2 Segundo interés técnico
	 * @param agp Número de periodos garantizados, en años completos redondeados por exceso
	 * @return Renta actuarial a dos cabezas
	 */
	public static BigDecimal arxytAGPt (final Integer difer, final Integer x, final Integer wx, final Integer y, final Integer wy, 
			final BigDecimal ppr, final Integer t, List<BigDecimal> valoresTabMortX, List<BigDecimal> valoresTabMortY,
			final BigDecimal m, final BigDecimal i1, final BigDecimal i2, final Integer agp) {
		// Inicio variables locales
		BigDecimal arxytAGPt = BigDecimal.ZERO;
		Integer varLimInferior;
		BigDecimal varXT;
		BigDecimal varLxT;
		BigDecimal varYT;
		BigDecimal varLyT;		
		BigDecimal varXj;
		BigDecimal varLxj;
		BigDecimal varYj;
		BigDecimal varLyj;
		BigDecimal varVpostj;
		BigDecimal unoMasPprEntre100;
		BigDecimal opPpr = BigDecimal.ZERO;
		// Fin variables locales
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio Funcion << arxytAGPt >> de la clase FuncionesVBX, para la entrada difer = {}, x = {}, wx = {},"
					+ "y = {}, wy = {}, ppr = {}, t = {}, valoresTabMortX = {}, valoresTabMortY = {}, m = {}, i1 = {}, i2 = {}, agp = {}",
					difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY, m, i1, i2, agp);
		}
		
		
		// Validación parámetros de entrada
		ValidacionesFuncionesVBX.validarParamEntradaFuncionArxytAGPtAarxytAGPt(difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY,
				m, i1, i2, agp);
		
		if (t < difer) {
			varLimInferior = difer;
		} else {
			varLimInferior = t;
		}
		
		//Variables que no cambian dentro del sumatorio
		unoMasPprEntre100 = BigDecimal.ONE.add(ppr.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		
		varXT = BigDecimal.valueOf(x + t);
		varLxT = Util.getVarLx(varXT, valoresTabMortX);
		
		varYT = BigDecimal.valueOf(y + t);
		varLyT = Util.getVarLx(varYT, valoresTabMortY);
		
		BigDecimal dividendo = varLxT.multiply(varLyT);
		
		/**
		 * Se calcula el valor de arxytAGPt como el sumatorio desde j=varLimInferior hasta j=AGP-1 de:
		 *  (varLxj1 * varLyj1 * (1+ppr/100)^((j-difer)) * varVpostj) / (varLxT * varLyT)
		 */
		if (!dividendo.equals(BigDecimal.ZERO)){
			for (int j=varLimInferior; j<=agp-1; j++) {
				
				//Se calcula la potencia para el primer término del sumatorio y luego se va incrementando 
				//para optimizar operaciones
				if (j==varLimInferior){
					opPpr = Util.pow(unoMasPprEntre100, j-difer);
				} else {
					opPpr = opPpr.multiply(unoMasPprEntre100);
				}
				
				varXj = BigDecimal.valueOf(x + j + ConstantsFunciones.CTE_1);
				varLxj = Util.getVarLx(varXj, valoresTabMortX);
				
				varYj = BigDecimal.valueOf(y + j + ConstantsFunciones.CTE_1);
				varLyj = Util.getVarLx(varYj, valoresTabMortY);
				
				varVpostj = vpost(j, m, i1, i2, t);
				
				arxytAGPt = arxytAGPt.add((varLxj.multiply(varLyj).multiply(opPpr).multiply(varVpostj))
						.divide(dividendo, ConstantsFunciones.MATH_CONTEXT));
			}
		}
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Fin Funcion << arxytAGPt >> de la clase FuncionesVBX, con resultado afrxyt = {} ", arxytAGPt);
		}
		
		return arxytAGPt;
	}
	
	/**
	 * Función auxiliar ärxy+t:AGP- t de la provisión VBX165
	 * @param difer Diferimiento de la renta
	 * @param x Edad primera cabeza a fecha de cálculo
	 * @param wx Límite de la tabla de mortalidad primera cabeza
	 * @param y Edad segunda cabeza a fecha de cálculo
	 * @param wy Límite de la tabla de mortalidad segunda cabeza
	 * @param ppr Porcentaje revalorización de la renta
	 * @param t Anualidad del cálculo
	 * @param valoresTabMortX Valores de la tabla de mortalidad primera cabeza
	 * @param valoresTabMortY Valores de la tabla de mortalidad segunda cabeza
	 * @param m Número de años en que aplicamos un primer interés técnico
	 * @param i1 Primer interés técnico
	 * @param i2 Segundo interés técnico
	 * @param agp Número de periodos garantizados, en años completos redondeados por exceso
	 * @return Renta actuarial a dos cabezas
	 */
	public static BigDecimal aarxytAGPt (final Integer difer, final Integer x, final Integer wx, final Integer y, final Integer wy,
			final BigDecimal ppr, final Integer t, final List<BigDecimal> valoresTabMortX, final List<BigDecimal> valoresTabMortY,
			final BigDecimal m, final BigDecimal i1, final BigDecimal i2, final Integer agp) {
		// Inicio variables locales
		BigDecimal aarxytAGPt = BigDecimal.ZERO;
		Integer varLimInferior;
		BigDecimal varXT;
		BigDecimal varLxT;
		BigDecimal varYT;
		BigDecimal varLyT;
		BigDecimal varXj;
		BigDecimal varLxj;
		BigDecimal varYj;
		BigDecimal varLyj;
		BigDecimal varVpretj;
		BigDecimal unoMasPprEntre100;
		BigDecimal opPpr = BigDecimal.ZERO;
		// Fin variables locales
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio Funcion << aarxytAGPt >> de la clase FuncionesVBX, para la entrada difer = {}, x = {}, wx = {},"
					+ "y = {}, wy = {}, ppr = {}, t = {}, valoresTabMortX = {}, valoresTabMortY = {}, m = {}, i1 = {}, i2 = {}, agp = {}",
					difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY, m, i1, i2, agp);
		}
		
		
		
		// Validación parámetros de entrada
		ValidacionesFuncionesVBX.validarParamEntradaFuncionArxytAGPtAarxytAGPt(difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY,
				m, i1, i2, agp);
	
		
		if (t < difer) {
			varLimInferior = difer;
		} else {
			varLimInferior = t;
		}
		
		//Variables que no varían en el sumatorio
		varXT = BigDecimal.valueOf(x + t);
		varLxT = Util.getVarLx(varXT, valoresTabMortX);
		
		varYT = BigDecimal.valueOf(y + t);
		varLyT = Util.getVarLx(varYT, valoresTabMortY);
		
		unoMasPprEntre100 = BigDecimal.ONE.add(ppr.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		
		BigDecimal dividendo = varLxT.multiply(varLyT);
		
		/**
		 * Se calcula el valor de aarxytAGPt como el sumatorio desde j=varLimInferior hasta j=AGP+difer-1 de:
		 *  (varLxj * varLyj * (1+ppr/100)^((j-difer)) * varVpretj) / (varLxT * varLyT)
		 */
		
		if (!dividendo.equals(BigDecimal.ZERO)){
			for (int j=varLimInferior; j<=agp+difer-1; j++) {
				
				//Se calcula la potencia para el primer término del sumatorio y luego se va incrementando 
				//para optimizar operaciones
				if (j==varLimInferior){
					opPpr = Util.pow(unoMasPprEntre100, j-difer);
				} else {
					opPpr = opPpr.multiply(unoMasPprEntre100);
				}
				
				varXj = BigDecimal.valueOf(x + j);
				varLxj = Util.getVarLx(varXj, valoresTabMortX);
				
				varYj = BigDecimal.valueOf(y + j);
				varLyj = Util.getVarLx(varYj, valoresTabMortY);
				
				varVpretj = FuncionesVBX.vpret(j, m, i1, i2, t);
				
				aarxytAGPt = aarxytAGPt.add((varLxj.multiply(varLyj).multiply(opPpr).multiply(varVpretj))
						.divide(dividendo, ConstantsFunciones.MATH_CONTEXT));
						
			}
		}
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Fin Funcion << aarxytAGPt >> de la clase FuncionesVBX, con resultado aarxytAGPt = {} ", aarxytAGPt);
		}
		
		return aarxytAGPt;
		
	}

	
	/**
	 * Función auxiliar VIDA de la provixión VBX462 que calcula la Probabilidad de supervivencia
	 * @param xc Edad a fecha de cálculo
	 * @param xj Edad a fecha del periodo
	 * @param w Límite de la tabla de mortalidad
	 * @param valoresTabMortX Valores de la tabla de mortalidad
	 * @return Probabilidad de supervivencia
	 */
	public static BigDecimal vida (final BigDecimal xc, final BigDecimal xj, final Integer w, final List<BigDecimal> valoresTabMortX) {
		// Variables locales
		BigDecimal vida;
		BigDecimal lxj;
		BigDecimal lxc;
		// Fin variables locales
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio Funcion << VIDA >> de la clase FuncionesVBX, para la entrada xc = {}, xj = {}, w = {},"
					+ "valoresTabMortX = {}",
					xc, xj, w, valoresTabMortX);
		}
		
		// Validación parámetros de entrada
		ValidacionesFuncionesVBX.validarParamEntradaFuncionVida(xc, xj, w, valoresTabMortX);
		
		lxj = Util.getVarLx(xj, valoresTabMortX);
		lxc = Util.getVarLx(xc, valoresTabMortX);
		
		//Se calcula el valor de la función como Vida = Lxj /Lxc 
		vida = lxj.divide(lxc, ConstantsFunciones.MATH_CONTEXT);
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Fin Funcion << VIDA >> de la clase FuncionesVBX, con resultado vida = {} ", vida);
		}
		
		return vida;
	}
	
	
	/**
	 * Función auxiliar VT de la provisión VBX462 que calcula la Probabilidad de supervivencia
	 * @param durcierre Número de años existente entre la fecha de cierre y la fecha de efecto de la póliza. Si es provisión de balance,
	 * 					consideramos el cierre a las 24 horas.
	 * @param actj Número de años existente entre la fecha de cierre y cada uno de los sucesivos vencimientos de las rentas C(j)
	 * @param durit1 Número de años que aplica el primer interés técnico
	 * @param i1 Primer interés técnico
	 * @param i2 Ssegundo interés técnico
	 * @return Resultado de la función
	 */
	public static BigDecimal vt (final BigDecimal durcierre, final BigDecimal actj, final Integer durit1, 
			final BigDecimal i1, final BigDecimal i2) {
		// Variables locales
		BigDecimal vt;
		BigDecimal varExp1;
		BigDecimal varExp2;
		BigDecimal varExp3;
		// Fin variables locales
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio Funcion << vt >> de la clase FuncionesVBX, para la entrada durcierre = {}, actj = {},"
					+ " durit1 = {}, i1 = {}, i2 = {}",
					durcierre, actj, durit1, i1, i2);
		}
		
		//Validación parámetros de entrada
		ValidacionesFuncionesVBX.validarParamEntradaFuncionVt(durcierre, actj, durit1, i1, i2);
		
		varExp1 = actj;
		varExp2 = BigDecimal.valueOf(durit1).subtract(durcierre);
		varExp3 = varExp1.subtract(varExp2);
		BigDecimal unoMasI1Entre100 = BigDecimal.ONE.add(i1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		BigDecimal unoMasI2Entre100 = BigDecimal.ONE.add(i2.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		
		if (durcierre.compareTo(BigDecimal.valueOf(durit1)) <= ConstantsFunciones.CTE_0 ) {
			if (durcierre.add(actj).compareTo(BigDecimal.valueOf(durit1)) <= ConstantsFunciones.CTE_0) {
				vt = Util.pow(unoMasI1Entre100, varExp1.negate());
			} else {
				vt = Util.pow(unoMasI1Entre100, varExp2.negate()).multiply(Util.pow(unoMasI2Entre100, varExp3.negate()));
			}
		} else {
			vt = Util.pow(unoMasI2Entre100, varExp1.negate());
		}
		
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Fin Funcion << vt >> de la clase FuncionesVBX, con resultado vt = {} ", vt);
		}
		
		return vt;
		
	
	}
	
	
	/**
	 * Función auxiliar VTR de la provisión VBX462 que calcula la probabilidad de supervivencia
	 * @param j Periodo de cálculo
	 * @param durcierre Número de años existentes entre la fecha de cierre y la fecha de efecto de la póliza. Si es provisión de balance,
	 * 					consieramos el cierre a las 24 horas
	 * @param actj Número de años existente entre la fecha de cierre y cada uno de los sucesivos vencimientos de las rentas C(j)
	 * @param actjant Número de años existente entre la fecha de cierre y el periodo anterior
	 * @param durit1 Número de años que aplica el primer interés técnico
	 * @param i1 Primer interés técnico
	 * @param i2 Segundo interés técnico
	 * @return Resultado de la función
	 */
	public static BigDecimal vtr (final Integer j, final BigDecimal durcierre, final BigDecimal actj, final BigDecimal actjant,
			final Integer durit1, final BigDecimal i1, final BigDecimal i2) {
		// Variables locales
		BigDecimal vtr;
		BigDecimal varExp2;
		BigDecimal varExp4;
		BigDecimal varExp5;
		// Fin variables locales
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Inicio Funcion << VTR >> de la clase FuncionesVBX, para la entrada j = {}, durcierre = {}, actj = {},"
					+ "actjant = {}, durit1 = {}, i1 = {}, i2 = {}",
					j, durcierre, actj, actjant, durit1, i1, i2);
		}
		
		// Validación parámetros de entrada
		ValidacionesFuncionesVBX.validarParamEntradaFuncionVtr(j, durcierre, actj, actjant, durit1, i1, i2);
		
		BigDecimal unoMasI1Entre100 = BigDecimal.ONE.add(i1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		BigDecimal unoMasI2Entre100 = BigDecimal.ONE.add(i2.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		
		varExp2 = BigDecimal.valueOf(durit1).subtract(durcierre);
		
		if (j == ConstantsFunciones.CTE_1) {
			varExp4 = actj.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_5);
		} else {
			varExp4 = actjant.add(actj).multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_5);
		}
		
		varExp5 = varExp4.subtract(varExp2);
		
		if (durcierre.compareTo(BigDecimal.valueOf(durit1)) <= ConstantsFunciones.CTE_0) {
			if (durcierre.add(actj).compareTo(BigDecimal.valueOf(durit1)) <= ConstantsFunciones.CTE_0) {
				vtr = Util.pow(unoMasI1Entre100, varExp4.negate());
			} else {
				vtr = Util.pow(unoMasI1Entre100, varExp2.negate()).multiply(Util.pow(unoMasI2Entre100, varExp5.negate()));
			}
		} else {
			vtr = Util.pow(unoMasI2Entre100, varExp4.negate());
		}
		
		if (FuncionesVBX.LOG.isTraceEnabled()) {
			FuncionesVBX.LOG.trace("Fin Funcion << vtr >> de la clase FuncionesVBX, con resultado vtr = {} ", vtr);
		}
		
		return vtr;
	}
	
}




