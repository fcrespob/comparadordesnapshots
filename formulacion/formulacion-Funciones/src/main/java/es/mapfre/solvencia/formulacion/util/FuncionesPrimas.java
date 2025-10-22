package es.mapfre.solvencia.formulacion.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.UtilFechas.Fecha;

public class FuncionesPrimas {

	/** 
	 * Log funciones de primas.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(FuncionesPrimas.class);

	/**
	 * Función que calcula este término de la formulación de la corriente de Provisión Matemática por fórmula cerrada.
	 * 
	 * OBADORPRIM(fcal)=CMORPEND(fcal)+CFALLEC(fcal)+  CFALLECVT(fcal)
	 * 
	 * @param fcal Fecha de cálculo
	 * @param fecIniSusc Fecha de inicio de suscripción 
	 * @param fecAntRenova Fecha de anterior renovación 
	 * @param fecProxRenova Fecha de próxima renovación 
	 * @param fecIni Fecha de inicio de cobro de la renta
	 * @param fecvto Fecha de vencimiento
	 * @param fecJ Fecha de devengo del periodo J 
	 * @param varj Corresponde al período transcurrido desde  la fecha alta ajuste o suscripción  hasta la fecha “j”
	 * @param partAnoNR Fracción de año que hay entre la renovación anterior NR y posterior (NR+1).
	 * @param varRY Fracción de año incompleto entre la anterior fecha de renovación y la fecha de cálculo fcal.
	 * @param vari1 Primer Interés técnico 
	 * @param vari2 Segundo Interés técnico 
	 * @param varm Número de años en que aplicamos un primer interés técnico desde fecha suscripción
	 * @param varTc Anualidades completas transcurridas desde la fecha de efecto hasta la fecha de cálculo.
	 * @param criterFec Criterio de Fechas para el cálculo
	 * @param lstValoresTabMort Valores de la tabla de mortalidad
	 * @param varw Edad Máxima de la tabla de mortalidad
	 * @param varx Edad del asegurado a Fecha de Efecto
	 * @param varzc Edad del asegurado a Fecha de cálculo
	 * @param vary Dato a nivel ajuste o suscripción que representa la fracción de año incompleto desde la fecha de alta del ajuste a la fecha de cálculo fcal
	 * @param difercol Diferimiento en cobrar la renta
	 * @param modBeta Variable de apoyo MODBETA
	 * @param beta1 Variable Beta1
	 * @param beta2 Variable Beta2
	 * @param varRen Variable varRen
	 * @return obadorprim
	 */
	public static BigDecimal obadorprim(final Timestamp fcal, final Timestamp fecIniSusc, final Timestamp fecAntRenova, final Timestamp fecProxRenova, final Timestamp fecIni, final Timestamp fecvto, final Timestamp fecJ,
			final BigDecimal varj, final BigDecimal partAnoNR, final BigDecimal varRY, final BigDecimal vari1, final BigDecimal vari2, final BigDecimal varm, final Integer varTc, final String criterFec, final List<BigDecimal> lstValoresTabMort,
			final BigDecimal varw, final BigDecimal varx, final BigDecimal varzc, final BigDecimal vary, final BigDecimal difercol, final String modBeta, final BigDecimal beta1, final BigDecimal beta2,
			final BigDecimal varn, final BigDecimal i1Bti, final BigDecimal i2Bti, final BigDecimal varRen, final BigDecimal varI1PorcentajeMasUno, final BigDecimal varI2PorcentajeMasUno) {
		//Variables locales
		BigDecimal obadorprim = BigDecimal.ZERO;
		BigDecimal varXRen;
		int varNDap = 0;
		int varNIRP = 0;
		BigDecimal varNirmv;
		BigDecimal varTcy;
		BigDecimal varVmort;
		BigDecimal varCMorpend = BigDecimal.ZERO;
		BigDecimal varVVida;
		BigDecimal varCfallec = BigDecimal.ZERO;
		BigDecimal varCfallecVT = BigDecimal.ZERO;
		//Fin variables locales

		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Inicio función << obadorprim >> de la clase FuncionesPrimas con la entrada fcal = {}, fecIniSusc = {}, fecAntRenova = {}, fecProxRenova = {}, fecIni = {}, fecvto = {}, fecJ = {}, varj = {}, partAnoNR = {}, varRY = {}, vari1 = {}, vari2 = {}, varm = {}, varTc = {}, criterFec = {}, lstValoresTabMort = {}, varw = {}, varx = {}, varzc = {}, vary = {}, difercol = {}, modBeta = {}, beta1 = {}, beta2 = {} y varn = {}",
					fcal, fecIniSusc, fecAntRenova, fecProxRenova, fecIni, fecvto, fecJ, varj, partAnoNR, varRY, vari1, vari2, varm, varTc, criterFec, lstValoresTabMort, varw, varx, varzc, vary, difercol, modBeta, beta1, beta2, varn);
		}

		//Validamos los campos de entrada
		ValidacionesFuncionesPrimas.validarParamentradaFuncionObadorprimParte1(fcal, fecIniSusc, fecAntRenova, fecProxRenova, fecIni, fecJ, varj);
		ValidacionesFuncionesPrimas.validarParamentradaFuncionObadorprimParte2(partAnoNR, varRY, vari1, vari2, varm, varTc, criterFec);
		ValidacionesFuncionesPrimas.validarParamentradaFuncionObadorprimParte3(lstValoresTabMort, varw, varx, varzc, vary, difercol);
		ValidacionesFuncionesPrimas.validarParamentradaFuncionObadorprimParte4(modBeta, beta1, beta2,varn, i1Bti, i2Bti);

		/**
		 * -	varXRen = X + nanos(fcalc, fecProxRenova, CriterFec)
			-	varNDaP =  Entero[N - TC - vary -partAnoNR + varRY]
			-	varNIRP =  Entero[Difercol - TC - vary -partAnoNR + varRY]
			-	varREN=  TC + vary + partAnoNR - varRY
			-	varNIRmv = Difercol – TC - vary - partAnoNR + varRY - varNIRP
			-	varTCY =   TC + vary
			-	varVMort = VMORT(I1,I2,M,N,TCY,PartAnoNR,varRY)
			-	varCMorpend  = CMORPEND (fecIni,fecvto,fecAntRenova, varNDaP, varNIRP,valoresTabMort,X, varXRen, varVMort,beta1,beta2,i1Bti,i2Bti)
			-	varVVida = VVIDA (J, I1,I2,M, varTCY)
			-	varCfallec = CFALLEC (fecIni,fecAntRenova,M,N, varNIRP,valoresTabMort,ZC,REN, varVVida,beta1,beta2,i1Bti,i2Bti)

			-	varCfallecVT = CFALLECVT (fecIni,fecAntRenova,M,N, varNIRP, varNIRmv,valoresTabMort,X,ZC, varXRen,REN, varVMort, varVVida,difercol,modBeta,beta1,beta2,i1Bti,i2Bti)

			Finalmente se obtendrá OBADORPRIM como: 

			obadorprim = varCMorpend   + varCfallec + varCfallecVT
		 */

		final BigDecimal varTcBD = BigDecimal.valueOf(varTc);
		varNDap = FuncionesAuxiliares.tc(fecProxRenova, fecvto);
		if (fecJ.before(fecIni)){
			BigDecimal varNIR = difercol.subtract(varTcBD).subtract(vary).subtract(partAnoNR).add(varRY);
			varNIRP =  varNIR.intValue(); 
			varNirmv = varNIR.subtract(varNIR.setScale(0, RoundingMode.DOWN));				
		}else{
			return obadorprim;
		}

		varXRen = varx.add(varRen);
		varTcy = varTcBD.add(vary);
		varVmort = FuncionesActualizacionFinanciera.vmort(vari1, vari2, varm, varn, varTcy, partAnoNR, varRY, varI1PorcentajeMasUno, varI2PorcentajeMasUno);
		if (ConstantsFunciones.CTE_SR.equals(modBeta)){
			varCMorpend = BigDecimal.ZERO;
		}else{
			varCMorpend = FuncionesActualizacionFinanciera.cmorpend(fecIni, fecvto, fecProxRenova, varNDap, varNIRP, lstValoresTabMort, varzc, varXRen, varVmort);
		}
		varVVida = FuncionesActualizacionFinanciera.vVida(varRen, vari1, vari2, varm, varTcy, varI1PorcentajeMasUno, varI2PorcentajeMasUno);
		varCfallec = FuncionesFallecimiento.cfallec(fecIni, fecProxRenova, varm, varn, varNIRP, lstValoresTabMort, varzc, varRen, varXRen, varVVida, beta1, beta2, i1Bti, i2Bti, vari1, vari2, varI1PorcentajeMasUno, varI2PorcentajeMasUno);
		varCfallecVT = FuncionesFallecimiento.cfallecvt(fcal, fecIni, fecProxRenova, varm, varn, varNIRP, varNirmv, lstValoresTabMort, varx, varzc, varXRen, varRen, varVmort, varVVida, difercol, modBeta, beta1, beta2, i1Bti, i2Bti, vari1, vari2, varI1PorcentajeMasUno);

		obadorprim = varCMorpend.add(varCfallec).add(varCfallecVT);


		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Fin función << obadorprim >> de la clase FuncionesPrimas con resultado obadorprim = {}", obadorprim);
		}

		return obadorprim;
	}

	/**
	 * Función que calcula el término OBFUTADORPRIM de la formulación de la corriente de Provisión Matemática por fórmula cerrada.
	 * @param fcalc Fecha de cálculo.
	 * @param fecProxRenova Fecha de próxima renovación.
	 * @param frpdgact Fecha de Renovación posterior a la fecha de cierre.
	 * @param fecvto Fecha de vencimiento.
	 * @param fecJ Fecha de devengo del periodo J.
	 * @param j Corresponde al período transcurrido desde  la fecha alta ajuste o suscripción  hasta la fecha “j”.
	 * @param partAnoNR Fracción de año que hay entre la renovación anterior NR y posterior (NR+1).
	 * @param ry Fracción de año incompleto entre la anterior fecha de renovación y la fecha de cálculo fcal.
	 * @param i1 Primer Interés técnico.
	 * @param i2 Segundo Interés técnico.
	 * @param m Número de años en que aplicamos un primer interés técnico desde fecha suscripción.
	 * @param n Duración real del ajuste o suscripción.
	 * @param tcy Distancia en Años entre la fecha de suscricpión y la fecha de cálculo.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param x Edad del asegurado a Fecha de Efecto.
	 * @param zc Edad del asegurado a Fecha de cálculo.
	 * @param nDaP Número de años completos que quedan pendientes desde la siguiente renovación a la fecha de cálculo hasta el vencimiento.
	 * @param nDmv Fracción de año comprendida entre la fecha de renovación de la umic inmediata anterior a la fecha de vencimiento del certificado y dicha fecha de vencimiento.
	 * @param modBeta Variable de apoyo MODBETA.
	 * @param beta1 Variable Beta1.
	 * @param beta2 Variable Beta2.
	 * @param i1Bti Primer Interés técnico según Bases Técnicas Iniciales.
	 * @param i2Bti Segundo Interés técnico según Bases Técnicas Iniciales.
	 * @param ren Período de tiempo transcurrido desde la fecha de alta del ajuste hasta la fecha de renovación siguiente al momento de cálculo.
	 * @param numi1PorcentajeMasUno
	 * @param numi2PorcentajeMasUno
	 * 
	 * @return obfutadorprim
	 */
	public static BigDecimal obfutadorprim (final Timestamp fcalc, final Timestamp fecProxRenova,
			final Timestamp frpdgact, final Timestamp fecvto, final Timestamp fecJ, final BigDecimal partAnoNR, final BigDecimal ry,
			final BigDecimal i1, final BigDecimal i2, final BigDecimal m, final BigDecimal n, final BigDecimal tcy, final List<BigDecimal> valoresTabMort,
			final BigDecimal x, final BigDecimal zc, final Integer nDaP, final BigDecimal nDmv, final String modBeta,
			final BigDecimal beta1, final BigDecimal beta2, final BigDecimal i1Bti, final BigDecimal i2Bti, final BigDecimal ren, final BigDecimal tcyJ,
			final BigDecimal numi1PorcentajeMasUno, final BigDecimal numi2PorcentajeMasUno){
		//Variables locales
		BigDecimal obfutadorprim = BigDecimal.ZERO;
		BigDecimal varXRen;
		Integer varNIRP = 0; //Siempre cero para esta tipología de productos
		BigDecimal varVMort;
		BigDecimal varCMorpend = BigDecimal.ZERO;
		BigDecimal varVVida;
		BigDecimal varCfallecpag;
		BigDecimal varFallecpagVT;
		//Fin variables locales

		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Inicio Funcion <<obfutadorprim>> de la clase FuncionesPrimas, para la entrada fcal = {},"
					+ " fecProxRenova = {}, frpdgact = {}, fecvto = {}, fecJ = {}, partAnoNR = {}, ry = {}, i1 = {}, i2 = {}, m = {}"
					+ " n = {}, tcy = {}, valoresTabMort = {}, x = {}, zc = {}, nDaP = {}, nDmv = {}, modBeta = {}, beta1 = {}, beta2 = {}"
					+ " i1Bti = {}, i2Bti = {}, ren = {}",
					fcalc, fecProxRenova, frpdgact, fecvto, fecJ, partAnoNR, ry, i1, i2, m, n, tcy, valoresTabMort, x, zc, nDaP, nDmv, modBeta,
					beta1, beta2, i1Bti, i2Bti, ren);
		}

		//Validaciones parametros de entrada
		ValidacionesFuncionesPrimas.validarParamEntradaFuncionObfutadorprim1(fcalc, fecProxRenova, frpdgact, fecvto, fecJ, partAnoNR, ry, i1, i2, m, n);
		ValidacionesFuncionesPrimas.validarParamEntradaFuncionObfutadorprim2(tcy, valoresTabMort, x, zc, nDaP, nDmv, modBeta, beta1, beta2, i1Bti, i2Bti, ren);

		varXRen = x.add(ren);

		varVMort = FuncionesActualizacionFinanciera.vmort(i1, i2, m, n, tcy, partAnoNR, ry, numi1PorcentajeMasUno, numi2PorcentajeMasUno);

		if (!modBeta.equals(ConstantsFunciones.CTE_SR)){
			varCMorpend = FuncionesActualizacionFinanciera.cmorpend(fecvto, fecvto, fecProxRenova, nDaP, varNIRP, valoresTabMort, zc, varXRen, varVMort);
		}

		varVVida = FuncionesActualizacionFinanciera.vVida(ren, i1, i2, m, tcyJ, numi1PorcentajeMasUno, numi2PorcentajeMasUno);
		varCfallecpag = FuncionesFallecimiento.cfallecpag(m, n, nDaP, valoresTabMort, zc, varXRen, ren, varVVida, beta1, beta2, i1Bti, i2Bti, i1, i2, numi1PorcentajeMasUno, numi2PorcentajeMasUno);
		varFallecpagVT = FuncionesFallecimiento.fallecpagvt(m, n, nDaP, nDmv, frpdgact, valoresTabMort, x, zc, varXRen, varVMort, varVVida, tcyJ, modBeta, beta1, beta2, i1Bti, i2Bti, i1, i2, fecvto, numi1PorcentajeMasUno, numi2PorcentajeMasUno);

		//Se obtiene OBFUTADORPRIM como: obfutadorprim = varCMorpend   + varCFALLECPAG + varFALLECPAGVT
		obfutadorprim = varCMorpend.add(varCfallecpag).add(varFallecpagVT);

		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Fin Funcion << obfutadorprim >> de la clase FuncionesPrimas, con resultado obfutadorprim = {}", obfutadorprim);
		}

		return obfutadorprim;
	}

	/**
	 * La función RECARGOFRO calcula el recargo por fraccionamiento en función del número de periodos en que se fraccione la prima.
	 * 
	 * @param numm
	 *            numm
	 * @param numi
	 *            numi
	 * @return recargo
	 */
	public static BigDecimal recargofro(final BigDecimal numPagos, final BigDecimal interes) {
		// Variables locales
		BigDecimal recargo = BigDecimal.ZERO;
		BigDecimal varl = BigDecimal.ONE;
		BigDecimal varA = BigDecimal.ZERO;
		// Fin variables locales

		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Inicio Funcion << recargofro >> de la clase FuncionesPrimas, para la entrada numPagos = {} y interes = {}", numPagos, interes);
		}

		//Validamos los parametros de entrada
		ValidacionesFuncionesPrimas.validarParamEntradaFuncionRecargoFro(numPagos, interes);

		//Calculamos varl como --> ((1 + interes) ^ (1/numPagos)) -1 
		varl = Util.pow(BigDecimal.ONE.add(interes), BigDecimal.ONE.divide(numPagos, ConstantsFunciones.MATH_CONTEXT)).subtract(BigDecimal.ONE);

		//Calculamos varA como --> (1 - (1 + varl)^-numPagos) / varl
		varA =  BigDecimal.ONE.subtract(Util.pow(BigDecimal.ONE.add(varl), -numPagos.intValue())).divide(varl, ConstantsFunciones.MATH_CONTEXT);

		recargo = numPagos.divide(BigDecimal.ONE.add(varl).multiply(varA), ConstantsFunciones.MATH_CONTEXT).subtract(BigDecimal.ONE);

		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Fin Funcion << recargofro >> de la clase FuncionesPrimas, con resultado recargo = {}", recargo);
		}

		return recargo;
	}

	/**
	 * Función que calcula el término ARI_CRI:FALLEC. Este término identifica la valoración del rembolso de primas en productos 
	 * ARITMETICOS no reducidos con rembolso de intereses por años transcurridos.
	 * @param x Edad x del asegurado.
	 * @param p Desplazamiento de la renta.
	 * @param n Duración del seguro en años.
	 * @param ifal Variable de apoyo IFAL.
	 * @param i1 Interés 1 de la base contable tratada.
	 * @param prp Porcentaje de revalorización de Primas.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param pas Variable de Apoyo PAS.
	 * @param fut Variable de Apoyo FUT.
	 * 
	 * @return ariCriFallec Valoracion Reembolso de primas.
	 */
	public static BigDecimal ariCriFallec(final Integer x, final Integer p, final Integer n, final BigDecimal ifal, final BigDecimal i1,
			final BigDecimal prp, final List<BigDecimal> valoresTabMort, final BigDecimal pas, final BigDecimal fut){
		//Inicio variables locales
		BigDecimal ariCriFallec = BigDecimal.ZERO;
		BigDecimal lXMasJ = BigDecimal.ZERO;
		BigDecimal lXMasP = BigDecimal.ZERO;
		BigDecimal lXMasJMas1 = BigDecimal.ZERO;
		BigDecimal operando1 = BigDecimal.ZERO;
		BigDecimal operando2 = BigDecimal.ZERO;
		BigDecimal exponente = BigDecimal.ZERO;
		//Fin variables locales

		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Inicio Funcion << ARI_CRI:FALLEC >> de la clase FuncionesPrimas, para la entrada x = {}, p = {}, n = {}, ifal = {}, i1 = {}, prp ={}, valoresTabMort = {}, pas = {} fut = {} ",
					x, p, n, ifal, i1, prp, valoresTabMort, pas, fut);
		}

		//Validaciones parametros entrada
		ValidacionesFuncionesFallecimiento.validarCamposEntradaFuncionGeoAriCriFallecParte1(x, p, n, ifal, i1);
		ValidacionesFuncionesFallecimiento.validarCamposEntradaFuncionGeoAriCriFallecParte2(prp, valoresTabMort, pas, fut);

		// (1+Ifal/100)
		final BigDecimal unoIfalDiv100 = BigDecimal.ONE.add(ifal.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		// (PRP/100)
		final BigDecimal prpDiv100 = prp.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
		// (1+I1/100)
		final BigDecimal unoVari1Div100 = BigDecimal.ONE.add(i1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));


		//L(x+p) = valoresTabMort(x+p).wkvalor
		lXMasP = valoresTabMort.get(x + p);

		for (int j = p; j < n; j++) {
			BigDecimal valorIteracionPASop1 = Util.pow(unoIfalDiv100, j);
			BigDecimal valorIteracionPASop2 = BigDecimal.ZERO;
			BigDecimal sumatorioPAS = valorIteracionPASop1; //La segunda parte del sumatorio queda 1+0, por lo tanto no hace falta multiplicar por 1
			BigDecimal valorIteracionFUT = prpDiv100.multiply(BigDecimal.valueOf(j + 1));
			BigDecimal sumatorioFUT = valorIteracionFUT.add(BigDecimal.ONE);

			//Calculamos L(X+J), L(X+J+1) 
			//L(x+j) = valoresTabMort(x+j).wkvalor
			lXMasJ = valoresTabMort.get(x + j);
			//L(x+j+1) = valoresTabMort(x+j+1).wkvalor
			lXMasJMas1 = valoresTabMort.get(x + j + 1);


			for (int h = 1; h <= j; h++) {
				//sumatorioPAS <-- (1+Ifal/100)^(J-H)*(1+ (PRP/100 * H))
				valorIteracionPASop1 = valorIteracionPASop1.divide(unoIfalDiv100, ConstantsFunciones.MATH_CONTEXT);
				valorIteracionPASop2 = valorIteracionPASop2.add(prpDiv100);
				sumatorioPAS = sumatorioPAS.add(valorIteracionPASop1.multiply(valorIteracionPASop2.add(BigDecimal.ONE)));
			}

			if (fut.signum() != 0){
				for (int h = j + 2; h < n; h++) {
					//sumatorioFUT <-- (1+ (PRP/100 * H))
					valorIteracionFUT = valorIteracionFUT.add(prpDiv100, ConstantsFunciones.MATH_CONTEXT);
					sumatorioFUT = sumatorioFUT.add(valorIteracionFUT.add(BigDecimal.ONE));
				}
				//operando1 <-- pas * sumatorioPAS + fut * sumatorioFUT
				operando1 = pas.multiply(sumatorioPAS).add(fut.multiply(sumatorioFUT));
			} else {
				//Si fut = 0, operando1 <-- pas * sumatorioPAS
				operando1 = pas.multiply(sumatorioPAS);
			}

			//exponente <-- -(J+0.5-p)
			exponente =  BigDecimal.valueOf(j).add(ConstantsFunciones.CTE_OPER_0_PUNTO_5).subtract(BigDecimal.valueOf(p)).negate();

			//operando2 <--(1+I1/100)^exponente/l(X+p)  *  ((l(X+J)- l(X+J+1))/(l(X+p))
			operando2 = Util.pow(unoVari1Div100, exponente).multiply(lXMasJ.subtract(lXMasJMas1).divide(lXMasP, ConstantsFunciones.MATH_CONTEXT));

			//ariCri <-- ariCri + (operando1 * operando2)
			ariCriFallec = ariCriFallec.add(operando1.multiply(operando2));
		}

		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Fin Funcion << ARI_CRI:FALLEC >> de la clase FuncionesPrimas, con resultado ariCriFallec = {}", ariCriFallec);
		}

		return ariCriFallec;
	}

	/**
	 * Función que calcula la suma de primas devengadas desde el efecto de la suscripción hasta la anualidad inmediatamente 
	 * anterior a la fecha de cálculo, capitalizadas financieramente por años transcurridos de seguro. 
	 * @param x Edad a fecha de cálculo.
	 * @param t Resultado de la función naños() en base al criterio establecido.
	 * @param ifal Variable de Apoyo IFAL.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param w Edad Máxima de la tabla de mortalidad.
	 * @param n Duración del seguro en años.
	 * @param i1 Interés del primer tramo.
	 * 
	 * @return pasadaca
	 */
	public static BigDecimal pasadaca(final Integer x, final Integer t, final BigDecimal ifal, final List<BigDecimal> valoresTabMort,
			final Integer w, final Integer n, final BigDecimal i1){
		//Inicio variables locales
		BigDecimal pasadaca = BigDecimal.ZERO;
		BigDecimal varLFRACt;
		BigDecimal varLFRACj;
		BigDecimal varLFRACj1;
		BigDecimal lfracJMenosJ1;
		BigDecimal potenciaIfal = BigDecimal.ZERO;
		BigDecimal potenciaI1 = BigDecimal.ZERO;
		//Fin variables locales	

		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Inicio Funcion << PASADACA >> de la clase FuncionesPrimas, para la entrada  x = {}, t = {}, ifal = {},"
					+" valoresTabMort = {}, w = {}, n = {}, i1 = {}", 
					x, t, ifal, valoresTabMort, w, n, i1);
		}

		//Validaciones parametros de entrada
		ValidacionesFuncionesFallecimiento.validarParamEntradaFuncionesPasadacaPascap(x, t, ifal, valoresTabMort, w, n, i1);

		/**
		 * Se calcula PASADACA como:
		 * PASADACA = Sumatorio desde(j=T) hasta (n-1): [(varLFRACj-varLFRACj1)* 〖(1+ I1/100)〗^(-(j-T+0,5))* 〖(1+ ifal/100)〗^((j-T)) ]/varLFRACt
		 */

		//Variables que no varían entre términos del sumatorio
		BigDecimal unoMasI1Entre100 = BigDecimal.ONE.add(i1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		BigDecimal unoMasIfalEntre100 = BigDecimal.ONE.add(ifal.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		varLFRACt = FuncionesAuxiliares.lfrac(x, t, BigDecimal.ZERO, valoresTabMort, w);

		//SUMATORIO (desde j=T hasta n-1)
		for (int j=t; j<n; j++){
			varLFRACj = FuncionesAuxiliares.lfrac(x, j, BigDecimal.ZERO, valoresTabMort, w);
			varLFRACj1 = FuncionesAuxiliares.lfrac(x, j+1, BigDecimal.ZERO, valoresTabMort, w);

			lfracJMenosJ1 = varLFRACj.subtract(varLFRACj1);

			if (j==t){
				//Se calcula el primer término del sumatorio (j=T) para optimizar el resto de operaciones del bucle
				potenciaIfal = BigDecimal.ONE; //En el primer j, esta potencia está elevada a 0 (j=T - T), por lo tanto el reusltado es 1
				potenciaI1 = Util.pow(unoMasI1Entre100, -0.5);
			} else {
				potenciaI1 = potenciaI1.divide(unoMasI1Entre100, ConstantsFunciones.MATH_CONTEXT);
				potenciaIfal = potenciaIfal.multiply(unoMasIfalEntre100);
			}

			pasadaca = pasadaca.add((lfracJMenosJ1.multiply(potenciaI1).multiply(potenciaIfal)).divide(varLFRACt, ConstantsFunciones.MATH_CONTEXT));
		}

		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Fin Funcion << PASADACA >> de la clase FuncionesPrimas, con resultado pasadaca = {} ", pasadaca);
		}

		return pasadaca;
	}

	/**
	 * Función que calcula la suma de los términos una progresión geométrica de razón (1+prp) cuyo primer término es t y 
	 * el último n, desde el término j al término n. Esta suma hace referencia a las primas de un término de 
	 * la expresión FUTURAS.
	 * @param t Primer térnimo de la progresión.
	 * @param j Término j ésimo de la progresión.
	 * @param n Último término de la progresión.
	 * @param prp Porcentaje de  revalorización.
	 * 
	 * @return sumaf Suma de los términos la progresión geométrica.
	 */
	public static BigDecimal sumaf(final Integer t, final Integer j, final Integer n, final BigDecimal prp){
		//Inicio variables locales
		BigDecimal sumaf = BigDecimal.ZERO;
		//Fin variables locales

		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Inicio Funcion << SUMAF >> de la clase FuncionesPrimas, para la entrada  t = {}, j = {}, n = {},"
					+" prp = {}", t, j, n, prp);
		}

		//Validaciones parametros de entrada
		ValidacionesFuncionesPrimas.validarParamEntradaFuncionSumaf(t, j, n, prp);

		if (t <= n){
			if (prp.signum() == 0){
				sumaf = BigDecimal.valueOf(n - j - 1);
			} else {
				BigDecimal varRazon = BigDecimal.ONE.add(prp.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));

				BigDecimal potencia1 = Util.pow(varRazon, n-t);
				BigDecimal potencia2 = Util.pow(varRazon, j-t+1);

				//sumaf = (potencia1.subtract(potencia2)).divide(varRazon, ConstantsFunciones.MATH_CONTEXT);
				sumaf = (potencia1.subtract(potencia2)).divide(prp.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01), ConstantsFunciones.MATH_CONTEXT);
			}
		}


		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Fin Funcion << SUMAF >> de la clase FuncionesPrimas, con resultado sumaf = {} ", sumaf);
		}

		return sumaf;
	}

	/**
	 * Función que recoge las obligaciones del asegurado relacionadas con el pago de primas.
	 * @param x Edad a fecha de cálculo.
	 * @param t
	 * @param alfa Fracción de año pendiente entre el momento del cálculo y la siguiente anualidad del seguro.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param w Edad Máxima de la tabla de mortalidad.
	 * @param n Duración del seguro en años.
	 * @param i1 Interés del primer tramo.
	 * @param gepc Gastos de gestión externa.
	 * @param prp Porcentaje de revalorización.
	 * 
	 * @return fobligado
	 */
	public static BigDecimal fobligado(final Integer x, final Integer t, final BigDecimal alfa, final List<BigDecimal> valoresTabMort,
			final Integer w, final Integer n, final BigDecimal i1, final BigDecimal gepc, final BigDecimal prp){
		//Inicio variables locales
		BigDecimal fobligado = BigDecimal.ZERO;
		BigDecimal varAxcg = BigDecimal.ZERO;
		BigDecimal varLFRACt;
		BigDecimal varLFRACt1;
		//Fin variables locales	

		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Inicio Funcion << FOBLIGADO >> de la clase FuncionesPrimas, para la entrada  x = {}, t = {}, alfa = {},"
					+" valoresTabMort = {}, w = {}, n = {}, i1 = {}, gepc = {}, prp = {}", 
					x, t, alfa, valoresTabMort, w, n, i1, gepc, prp);
		}

		//Validaciones parametros de entrada
		ValidacionesFuncionesPrimas.validarParamEntradaFuncionFobligado(x, t, alfa, valoresTabMort, w, n, i1, gepc, prp);

		varAxcg = FuncionesRentas.axcg(0, x, t+1, n, i1, prp, valoresTabMort); 
		varLFRACt = FuncionesAuxiliares.lfrac(x, t, alfa, valoresTabMort, w);
		varLFRACt1 = FuncionesAuxiliares.lfrac(x, t+1, BigDecimal.ZERO, valoresTabMort, w);

		/**
		 * Se calcula FOBLIGADO como:
		 * FOBLIGADO =   [〖 (varLFRACt1 )/(varLFRACt )*(1+ I1/100)〗^(-(1-alfa))  ] * 
		 *    [ ((1-gepc/100)* varAxcg )/〖(1+ prp/100)〗^T ]   
		 */

		BigDecimal lfract1EntreT = varLFRACt1.divide(varLFRACt, ConstantsFunciones.MATH_CONTEXT);
		BigDecimal unoMasI1Entre100 = BigDecimal.ONE.add(i1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		BigDecimal unoMasPrpEntre100 = BigDecimal.ONE.add(prp.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		BigDecimal unoMenosGepcEntre100 = BigDecimal.ONE.subtract(gepc.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));

		BigDecimal potencia = Util.pow(unoMasI1Entre100, (BigDecimal.ONE.subtract(alfa)).negate());
		BigDecimal divisor = Util.pow(unoMasPrpEntre100, t);

		//[〖 (varLFRACt1 )/(varLFRACt )*(1+ I1/100)〗^(-(1-alfa)) ]
		BigDecimal operador1 = lfract1EntreT.multiply(potencia);
		//[ ((1-gepc/100)* varAxcg )/〖(1+ prp/100)〗^T ]
		BigDecimal operador2 = unoMenosGepcEntre100.multiply(varAxcg).divide(divisor, ConstantsFunciones.MATH_CONTEXT);

		fobligado = operador1.multiply(operador2);

		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Fin Funcion << FOBLIGADO >> de la clase FuncionesPrimas, con resultado fobligado = {} ", fobligado);
		}

		return fobligado;
	}

	/**
	 * Función que calcula la suma de primas devengadas desde el origen del contrato hasta el momento de cálculo, en la cual 
	 * se aplica un interés de capitalización Ifal por anualidades completas transcurridas. Se utiliza en la formulación como 
	 * un término de la expresión FUTURAS.
	 * @param j Término j ésimo de la progresión.
	 * @param t
	 * @param prp Porcentaje de  revalorización.
	 * @param ifal Variable de Apoyo IFAL.
	 * 
	 * @return sumapc
	 */
	public static BigDecimal sumapc(final Integer j, final Integer t, final BigDecimal prp, final BigDecimal ifal){
		//Inicio variables locales
		BigDecimal sumapc = BigDecimal.ZERO;
		//Fin variables locales

		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Inicio Funcion << SUMAPC >> de la clase FuncionesPrimas, para la entrada  j = {}, t = {}, prp = {},"
					+" ifal = {}", j, t, prp, ifal);
		}

		//Validaciones parametros de entrada
		ValidacionesFuncionesPrimas.validarParamEntradaFuncionSumapc(j, t, prp, ifal);

		BigDecimal unoMasIfalEntre100 = BigDecimal.ONE.add(ifal.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));

		if (ifal.compareTo(prp) == 0){

			/**
			 * Se calcula SUMAPC como:
			 * SUMAPC = (j-T+1) * 〖(1+ ifal/100)〗^((j-t))
			 */
			BigDecimal potencia = Util.pow(unoMasIfalEntre100, j-t);
			sumapc = (BigDecimal.valueOf(j-t+1)).multiply(potencia);

		} else {

			/**
			 * Se calcula SUMAPC como:
			 * SUMAPC =   (〖(1+ ifal/100)〗^((j-t+1)) * 〖(1+ prp/100)〗^((j-T+1)))  /  (( ifal/100)- (prp/100)) 
			 */

			BigDecimal ifalEntre100 = ifal.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
			BigDecimal prpEntre100 = prp.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
			BigDecimal potenciaIfal = Util.pow(unoMasIfalEntre100, j-t+1); //〖(1+ ifal/100)〗^((j-t+1))
			BigDecimal potenciaPrp = Util.pow(prpEntre100.add(BigDecimal.ONE), j-t+1); //〖(1+ prp/100)〗^((j-T+1)))

			sumapc = potenciaIfal.subtract(potenciaPrp).divide(ifalEntre100.subtract(prpEntre100), ConstantsFunciones.MATH_CONTEXT);
		}

		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Fin Funcion << PASCAP >> de la clase FuncionesPrimas, con resultado sumapc = {} ", sumapc);
		}

		return sumapc;
	}

	/**
	 * Función que calcula la valoración actuarial del reembolso de las primas futuras en caso de fallecimiento. 
	 * @param x Edad a fecha de cálculo.
	 * @param t
	 * @param n Duración del seguro en años.
	 * @param fut Variable de Apoyo FUT.
	 * @param prp Porcentaje de revalorización.
	 * @param ifal Variable de Apoyo IFAL.
	 * @param i1 Interés del primer tramo.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param w Edad Máxima de la tabla de mortalidad.
	 * 
	 * @return futuras
	 */
	public static BigDecimal futuras(final Integer x, final Integer t, final Integer n, final BigDecimal fut, final BigDecimal prp,
			final BigDecimal ifal, final BigDecimal i1, final List<BigDecimal> valoresTabMort, final Integer w){
		//Inicio variables locales
		BigDecimal futuras = BigDecimal.ZERO;
		BigDecimal varLFRACt;
		BigDecimal varLFRACj;
		BigDecimal varLFRACj1;
		BigDecimal varSumaPC;
		BigDecimal varSumaF = BigDecimal.ZERO;
		BigDecimal lfracJMenosJ1;
		BigDecimal potenciaI1 = BigDecimal.ZERO;
		//Fin variables locales	

		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Inicio Funcion << FUTURAS >> de la clase FuncionesPrimas, para la entrada  x = {}, t = {}, n = {},"
					+" fut = {}, prp = {}, ifal = {}, i1 = {}, valoresTabMort = {}, w = {}", 
					x, t, n, fut, prp, ifal, i1, valoresTabMort, w);
		}

		//Validaciones parametros de entrada
		ValidacionesFuncionesPrimas.validarParamEntradaFuncionFuturas(x, t, n, fut, prp, ifal, i1, valoresTabMort, w);

		/**
		 * Se calcula FUTURAS como:
		 * FUTURAS = Sumatorio (desde j=T hasta n-1)[(〖  (varLFRACj - varLFRACj1 )* (1+ I1/100)〗^(-(j-T+0,5))/(varLFRACt ))  *
		 *  ( varSumaPC+fut*varSumaF  )] 
		 */

		//Variables que no varían entre términos del sumatorio
		BigDecimal unoMasI1Entre100 = BigDecimal.ONE.add(i1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		varLFRACt = FuncionesAuxiliares.lfrac(x, t, BigDecimal.ZERO, valoresTabMort, w);

		//SUMATORIO (desde j=T hasta n-1)
		for (int j=t; j<n; j++){

			varLFRACj = FuncionesAuxiliares.lfrac(x, j, BigDecimal.ZERO, valoresTabMort, w);
			varLFRACj1 = FuncionesAuxiliares.lfrac(x, j+1, BigDecimal.ZERO, valoresTabMort, w);
			varSumaPC = sumapc(j, t, prp, ifal);
			varSumaF = sumaf(t, j, n, prp);

			lfracJMenosJ1 = varLFRACj.subtract(varLFRACj1);

			//Se diferencia el primer término del resto para optimizar los cálculos
			if (j==t){
				potenciaI1 = Util.pow(unoMasI1Entre100, -0.5);
			} else {
				potenciaI1 = potenciaI1.divide(unoMasI1Entre100, ConstantsFunciones.MATH_CONTEXT);
			}

			//(〖  (varLFRACj - varLFRACj1 )* (1+ I1/100)〗^(-(j-T+0,5))
			BigDecimal primerOperando = (lfracJMenosJ1.multiply(potenciaI1));
			BigDecimal segundoOperando = varSumaPC.add(fut.multiply(varSumaF)); //  ( varSumaPC+fut*varSumaF )

			futuras = futuras.add(primerOperando.multiply(segundoOperando));
		}

		//Se saca el divisor fuera del sumatorio para optimizar operaciones: Sumatorio/varLFRACt
		futuras = futuras.divide(varLFRACt, ConstantsFunciones.MATH_CONTEXT);

		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Fin Funcion << FUTURAS >> de la clase FuncionesPrimas, con resultado futuras = {} ", futuras);
		}

		return futuras;
	}

	/**
	 * Función que calcula la suma de fracciones de prima satisfechas en la anualidad en curso (T) desde la fecha de 
	 * aniversario hasta el momento de cálculo, teniendo en cuenta que las primas (o las fracciones de esta en caso de pago 
	 * fraccionado, coincidentes con el cálculo se consideran pendientes).
	 * @param tc Anualidad en curso.
	 * @param t Anualidad a calcular.
	 * @param pprUmic Primas Satisfechas en la anualidad T.
	 * 
	 * @return ppr
	 */
	public static BigDecimal ppr(final Integer tc, final Integer t, final BigDecimal pprUmic){
		//Inicio variables locales
		BigDecimal ppr = BigDecimal.ZERO;
		//Fin variables locales

		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Inicio Funcion << PPR >> de la clase FuncionesPrimas, para la entrada  tc = {}, t = {}, pprUmic = {}", 
					tc, t, pprUmic);
		}

		//Validaciones parametros de entrada
		ValidacionesFuncionesPrimas.validarParamEntradaFuncionPpr(tc, t, pprUmic);

		if (t == tc){
			ppr = pprUmic;
		} else if (t > tc){
			ppr = BigDecimal.ZERO;
		} else {
			//Se devuelve error funcional AKXX – Error: La Anualidad de cálculo no puede ser anterior a la anualidad en curso.
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AK);
		}

		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Fin Funcion << PPR >> de la clase FuncionesPrimas, con resultado ppr = {} ", ppr);
		}

		return ppr;
	}

	/**
	 * Función que calcula el número de recibos pendiente a la fecha de cálculo, expresado como fracción del año en curso. Cuando un recibo coincide con 
	 * la fecha de cierre, por definición lo consideramos pendiente.
	 * @param cformapago Código forma de pago de la prima.
	 * @param tc Anualidad en curso.
	 * @param t Anualidad a calcular.
	 * @param pprUmic Primas Satisfechas en la anualidad T.
	 * @param primatotal Prima Total.
	 * @param primaIni Prima Inicial.
	 * 
	 * @return pendpa
	 */
	public static BigDecimal pendpa(final String cformapago, final Integer tc, final Integer t, final BigDecimal pprUmic,
			final BigDecimal primatotal, final BigDecimal primaIni){
		//Inicio variables locales
		BigDecimal pendpa = BigDecimal.ZERO;
		BigDecimal varPrimapdte;
		BigDecimal prima = primatotal;
		//Fin variables locales

		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Inicio Funcion << PENDPA >> de la clase FuncionesPrimas, para la entrada  cformapago = {}, tc = {}, t = {}, pprUmic = {}, primatotal = {}, primaIni = {}", 
					cformapago, tc, t, pprUmic, primatotal, primaIni);
		}

		//Validaciones parametros de entrada
		ValidacionesFuncionesPrimas.validarParamEntradaFuncionesPendpaPagadpa(cformapago, tc, t, pprUmic, primatotal, primaIni);

		if (!cformapago.equals("9")){
			if(t > tc){
				pendpa = BigDecimal.ZERO;
			} else if(t == tc){

				if (pprUmic.signum() == 0){
					pendpa = BigDecimal.ONE;
				} else{

					if (primatotal.signum() == 0){
						prima = primaIni;
					}

					varPrimapdte = prima.subtract(pprUmic);

					pendpa = varPrimapdte.divide(prima, ConstantsFunciones.MATH_CONTEXT);
				}

			} else {
				//Se devuelve error funcional AKXX – Error: La Anualidad de cálculo no puede ser anterior a la anualidad en curso.
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AK);
			}
		}

		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Fin Funcion << PENDPA >> de la clase FuncionesPrimas, con resultado pendpa = {} ", pendpa);
		}

		return pendpa;
	}

	/**
	 * Función que calcula el número de recibos pagados a la fecha de cálculo expresado como fracción del año en curso, 
	 * siendo el complementario de PENDPA.
	 * @param fantani 
	 * @param feccierre
	 * @param fecJ
	 * @param cformapago Código forma de pago de la prima.
	 * @param tc Anualidad en curso.
	 * @param t Anualidad a calcular.
	 * 
	 * @return pagadpa
	 */
	public static BigDecimal pagadpa(final String cformapago, final Integer tc, final Integer t, final BigDecimal pprUmic,
			final BigDecimal primaTotal, final BigDecimal primaIni){
		//Inicio variables locales
		BigDecimal pagadpa = BigDecimal.ZERO;
		BigDecimal varPendpa = BigDecimal.ZERO;
		//Fin variables locales

		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Inicio Funcion << PAGADPA >> de la clase FuncionesPrimas, para la entrada  cformapago = {}, tc = {}, t = {}, pprUmic = {}, primaTotal = {}, primaIni = {}", 
					cformapago, tc, t, pprUmic, primaTotal, primaIni);
		}

		//Validaciones parametros de entrada
		ValidacionesFuncionesPrimas.validarParamEntradaFuncionesPendpaPagadpa(cformapago, tc, t, pprUmic, primaTotal, primaIni);

		varPendpa = FuncionesPrimas.pendpa(cformapago, tc, t, pprUmic, primaTotal, primaIni);

		pagadpa = BigDecimal.ONE.subtract(varPendpa);


		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Fin Funcion << PAGADPA  >> de la clase FuncionesPrimas, con resultado pagadpa = {} ", pagadpa);
		}

		return pagadpa;
	}

	/**
	 * Función que calcula los Intereses de la prima anual. En este término se recogen las primas y los intereses de estas 
	 * primas,  que a la fecha de cierre, debido a su forma de pago, no se han satisfecho aun pero se han incluido en la 
	 * provisión de balance, al calcularse esta como prima anual.recoge las obligaciones del asegurado relacionadas con 
	 * el pago de primas.
	 * @param x Edad a fecha de cálculo.
	 * @param t
	 * @param bbeta Porción de año restante hasta el próximo aniversario de la póliza.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param w Edad Máxima de la tabla de mortalidad.
	 * @param gepc Gastos de gestión externa.
	 * @param cformapago Código forma de pago de la prima.
	 * @param tc0 Anualidad en curso.
	 * @param pnatc Prima Tarada.
	 * @param ifal Variable de Apoyo IFAL.
	 * @param pprUmic Primas Satisfechas en la anualidad T.
	 * 
	 * @return paeint
	 */
	public static BigDecimal paeint(final Integer x, final Integer t, final BigDecimal bbeta, final List<BigDecimal> valoresTabMort,
			final Integer w, final BigDecimal gepc, final String cformapago, final Integer tc0, final BigDecimal pnatc,
			final BigDecimal ifal, final BigDecimal pprUmic, final BigDecimal primaIni){
		//Inicio variables locales
		BigDecimal paeint = BigDecimal.ZERO;
		BigDecimal varLFRACt;
		BigDecimal varLFRACtbeta;
		BigDecimal varPendpa = BigDecimal.ZERO;
		//Fin variables locales	

		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Inicio Funcion << PAEINT >> de la clase FuncionesPrimas, para la entrada  x = {}, t = {}, bbeta = {},"
					+" valoresTabMort = {}, w = {}, gepc = {}, cformapago = {}, tc0 = {}, pnatc = {}, ifal = {}, pprUmic = {}", 
					x, t, bbeta, valoresTabMort, w, gepc, cformapago, tc0, pnatc, ifal, pprUmic);
		}

		//Validaciones parametros de entrada
		ValidacionesFuncionesPrimas.validarParamEntradaFuncionPaeintParte1(x, t, bbeta, valoresTabMort, w, gepc);
		ValidacionesFuncionesPrimas.validarParamEntradaFuncionPaeintParte2(cformapago, tc0, pnatc, ifal, pprUmic);

		varLFRACt = FuncionesAuxiliares.lfrac(x, t, BigDecimal.ZERO, valoresTabMort, w);
		varLFRACtbeta = FuncionesAuxiliares.lfrac(x, t, bbeta, valoresTabMort, w);
		varPendpa = pendpa(cformapago, tc0, t, pprUmic, pnatc, primaIni);

		/**
		 * Se calcula PAEINT como:
		 * PAEINT =  (PNAtc*(1-  Gepc/100)*  varPendpa) * [(varLFRACt )/(varLFRACtbeta )* 〖(1+ ifal/100)〗^bbeta  ]
		 */

		BigDecimal unoMasIfalEntre100 = BigDecimal.ONE.add(ifal.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		BigDecimal unoMenosGepcEntre100 = BigDecimal.ONE.subtract(gepc.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));

		//(PNAtc*(1-  Gepc/100)*  varPendpa)
		BigDecimal operador1 = pnatc.multiply(unoMenosGepcEntre100).multiply(varPendpa);
		//[(varLFRACt )/(varLFRACtbeta )* 〖(1+ ifal/100)〗^bbeta  ]
		BigDecimal operador2 = (varLFRACt.divide(varLFRACtbeta, ConstantsFunciones.MATH_CONTEXT)).multiply(Util.pow(unoMasIfalEntre100, bbeta));

		paeint = operador1.multiply(operador2);

		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Fin Funcion << PAEINT >> de la clase FuncionesPrimas, con resultado paeint = {} ", paeint);
		}

		return paeint;
	}

	/**
	 * Función que calcula la  suma de primas pagas y capitalizadas por años completos. 
	 * @param tc Anualidad de cálculo.
	 * @param tcm Meses completos transcurridos desde fecha de efecto a fecha de cálculo(fecha de cierre).
	 * @param ttm Número de meses transcurridos desde el efecto a la fecha de vencimiento.
	 * @param beta Meses transcurridos desde la fecha de cálculo (Fecha de cierre)  hasta la fecha de proyección.
	 * @param pNAtc Prima Tarada.
	 * @param pas Variable de Apoyo PAS.
	 * @param fut Variable de Apoyo FUT.
	 * @param prp Porcentaje de revalorización de Primas.
	 * @param tit1 Interés del primer tramo.
	 * 
	 * @return futFlex
	 */
	public static BigDecimal fut_flex(final Integer tc, final Integer tcm, final Integer ttm, final Integer beta, final BigDecimal pNAtc,
			final BigDecimal pas, final BigDecimal fut, final BigDecimal prp, final BigDecimal tit1){
		//Inicio variables locales
		BigDecimal futFlex = BigDecimal.ZERO;
		BigDecimal varFUTPG = BigDecimal.ZERO;
		BigDecimal varSUMPG = BigDecimal.ZERO;
		//Fin variables locales

		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Inicio Funcion << PAEINT >> de la clase FuncionesPrimas, para la entrada  tc = {}, tcm = {}, ttm = {},"
					+" beta = {}, pNAtc = {}, pas = {}, fut = {}, prp = {}, tit1 = {}", 
					tc, tcm, ttm, beta, pNAtc, pas, fut, prp, tit1);
		}

		//Validaciones parametros de entrada
		ValidacionesFuncionesPrimas.validarParamEntradaFuncionFutflexParte1(tc, tcm, ttm, beta, pNAtc);
		ValidacionesFuncionesPrimas.validarParamEntradaFuncionFutflexParte2(pas, fut, prp, tit1);

		if (tcm+beta < ttm){
			varFUTPG = FuncionesSecundarias.futpg(tc, tcm, ttm, beta, prp);
			//if (tcm/12 > tc.intValue()+1){
			varSUMPG = sumpg(tc, tcm, beta, prp, tit1);

			//} //Si entero (tcm/12) <= tc +1, varSUMPG = 0

			//Se calcula FUT_FLEX = PNAtc * [(PAS*varSumPG )+ (FUT*varFUTPG  )]
			BigDecimal opPas = pas.multiply(varSUMPG);
			BigDecimal opFut = fut.multiply(varFUTPG);

			futFlex = pNAtc.multiply(opPas.add(opFut));

		} // Si tcm + beta >= ttm, FUT_FLEX = 0


		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Fin Funcion << FUT_FLEX >> de la clase FuncionesPrimas, con resultado futFlex = {} ", futFlex);
		}

		return futFlex;
	}

	/**
	 * Función que calcula la  suma de primas pagas y capitalizadas por años completos. 
	 * @param tc Anualidad de cálculo.
	 * @param tcm Meses completos transcurridos desde fecha de efecto a fecha de cálculo(fecha de cierre).
	 * @param beta Meses transcurridos desde la fecha de cálculo (Fecha de cierre)  hasta la fecha de proyección.
	 * @param prp Porcentaje de revalorización de Primas.
	 * @param tit1 Interés del primer tramo.
	 * 
	 * @return sumpg
	 */
	private static BigDecimal sumpg(final Integer tc, final Integer tcm, final Integer beta, final BigDecimal prp, 
			final BigDecimal tit1){
		//Inicio variables locales
		BigDecimal sumpg = BigDecimal.ZERO;
		BigDecimal terminoPrp;
		BigDecimal terminoTit1;
		Integer varLimSuperior;
		//Fin variables locales

		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Inicio Funcion << PAEINT >> de la clase FuncionesPrimas, para la entrada  tc = {}, tcm = {},"
					+" beta = {}, prp = {}, tit1 = {}", 
					tc, tcm, beta, prp, tit1);
		}

		//Validaciones parametros de entrada
		ValidacionesFuncionesPrimas.validarParamEntradaFuncionSumpg(tc, tcm, beta, prp, tit1);


		varLimSuperior = (tcm+beta)/12;

		if (varLimSuperior >= tc+1){

			/**
			 * Se calcula SUMPG como:
			 * SUMPG = Sumatorio(desde (j=tc+1) hasta varLimSuperior): 〖(1+ prp/100)〗^((j-tc-1)) * 〖
			 *               (1 + tit1/100)〗^(entero((tcm+beta)/12)-j)
			 */

			BigDecimal unoMasPrpEntre100 = BigDecimal.ONE.add(prp.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
			BigDecimal unoMasTit1Entre100 = BigDecimal.ONE.add(tit1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));

			//Se calcula el primer término del sumatorio para optimizar operaciones dentro del bucle
			terminoPrp = BigDecimal.ONE; //En el primer término la potencia tiene como exponente 0, por lo tanto el resultado es 1
			terminoTit1 = Util.pow(unoMasTit1Entre100, varLimSuperior - (tc+ 1));
			sumpg = terminoTit1; //El término prp es 1 por lo tanto no hace falta multiplicar

			// SUMATORIO desde (j=tc+2) hasta varLimSuperior
			for (int j = tc+2; j<= varLimSuperior;j++){
				terminoPrp = terminoPrp.multiply(unoMasPrpEntre100);
				terminoTit1 = terminoTit1.divide(unoMasTit1Entre100, ConstantsFunciones.MATH_CONTEXT);

				sumpg = sumpg.add(terminoPrp.multiply(terminoTit1));
			}

		} // Si varLimSuperior < tc+1 --> SUMPG = 0


		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Fin Funcion << SUMPG >> de la clase FuncionesPrimas, con resultado sumpg = {} ", sumpg);
		}

		return sumpg;
	}

	/**
	 * Función que calcula suma de primas pagas y capitalizadas por años completos. 
	 * @param tc Anualidad a calcular.
	 * @param tcm Meses completos transcurridos desde fecha de efecto a fecha de cálculo(fecha de cierre).
	 * @param ttm Número de meses transcurridos desde el efecto a la fecha de vencimiento.
	 * @param beta Meses transcurridos desde la fecha de cálculo (Fecha de cierre)  hasta la fecha de proyección.
	 * @param tc0 Anualidad en curso.
	 * @param ifal Variable de Apoyo IFAL.
	 * @param ppcapUmic Primas Periodicas Capitalizadas hasta la anualidad en curso.
	 * @param n Duración del seguro en años.
	 * @param pprUmic Primas Satisfechas en la anualidad T.
	 * @param pNAtc Prima Tarada.
	 * @param cformapago Código forma de pago de la prima.
	 * @param prp Porcentaje de revalorización de Primas.
	 * @param pas Variable de Apoyo PAS.
	 * @param fut Variable de Apoyo FUT.
	 * @param tit1 Interés del primer tramo
	 * @param primaIni Prima Inicial.
	 * 
	 * @return ppcap
	 */
	public static BigDecimal ppcap(final Integer tc, final Integer tcm, final Integer ttm, final Integer beta, final Integer tc0,
			final BigDecimal ifal, final BigDecimal ppcapUmic, final Integer n, final BigDecimal pprUmic, final BigDecimal pNAtc,
			final String cformapago, final BigDecimal prp, final BigDecimal pas, final BigDecimal fut, final BigDecimal tit1,
			final BigDecimal primaIni){
		//Inicio variables locales
		BigDecimal ppcap = BigDecimal.ZERO;
		BigDecimal varPagadpa = BigDecimal.ZERO;
		BigDecimal var1;
		BigDecimal varFutFlex = BigDecimal.ZERO;
		BigDecimal varPPR = BigDecimal.ZERO;
		//Fin variables locales

		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Inicio Funcion << PPCAP >> de la clase FuncionesPrimas, para la entrada  tc = {}, tcm = {}, ttm = {},"
					+" beta = {}, tc0 = {}, ifal = {}, ppcapUmic = {}, n = {}, pprUmic = {}, pNAtc = {},  cformapago = {}"
					+" prp = {}, pas = {}, fut = {}, tit1 = {}", 
					tc, tcm, ttm, beta, tc0, ifal, ppcapUmic, n, pprUmic, pNAtc, cformapago, prp, pas, fut, tit1);
		}

		//Validaciones parametros de entrada
		ValidacionesFuncionesPrimas.validarParamEntradaFuncionPpcapParte1(tc, tcm, ttm, beta, tc0);
		ValidacionesFuncionesPrimas.validarParamEntradaFuncionPpcapParte2(ifal, ppcapUmic, n, pprUmic, pNAtc);
		ValidacionesFuncionesPrimas.validarParamEntradaFuncionPpcapParte3(cformapago, prp, pas, varFutFlex, tit1, primaIni);

		if (tc < n){

			if (tc == tc0){
				ppcap = ppcapUmic;
			} else {

				varPagadpa = pagadpa(cformapago, tc0, tc0, pprUmic, pNAtc, primaIni);
				var1 = pNAtc.multiply(BigDecimal.ONE.subtract(varPagadpa));
				varFutFlex = fut_flex(tc0, tcm, ttm, beta, pNAtc, pas, fut, prp, tit1);
				varPPR = pprUmic;

				/**
				 * Se calcula PPCAP como:
				 * PPCAP = (ppcapUmic+ varPPR +var1)* 〖(1+ ifal/100)〗^(entero((tcm+beta)/12)-tc) + [(1+ prp/(100 )) * varFutFlex ]
				 */

				BigDecimal unoMasIfalEntre100 = BigDecimal.ONE.add(ifal.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
				BigDecimal unoMasPrpEntre100 = BigDecimal.ONE.add(prp.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));

				Integer exponente = ((tcm+beta)/12) -tc0;
				BigDecimal potencia = Util.pow(unoMasIfalEntre100, exponente); //〖(1+ ifal/100)〗^(entero((tcm+beta)/12)-tc)

				// (ppcapUmic+ varPPR +var1)* 〖(1+ ifal/100)〗^(entero((tcm+beta)/12)-tc)
				BigDecimal operando1 = (ppcapUmic.add(varPPR).add(var1)).multiply(potencia); 
				// [(1+ prp/(100 )) * varFutFlex ]
				BigDecimal operando2 = unoMasPrpEntre100.multiply(varFutFlex); 

				ppcap = operando1.add(operando2);
			}
			
		} //Si tc >= n, PPCAP = 0

		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Fin Funcion << PPCAP >> de la clase FuncionesPrimas, con resultado ppcap = {} ", ppcap);
		}

		return ppcap;
	}

	/**
	 * Función que calcula el Movimiento de prima
	 * @param t Periodo mensual de proyección
	 * @param ttm Número de meses transcurridos desde el efecto a la fecha de vencimiento
	 * @param pna Prima del periodo
	 * @param np Forma de pago de la prima 
	 * @param prp Porcentaje de revalorización de Primas
	 * @param mescie Mes de la fecha de cierre
	 * @param diaVto Día de vencimiento
	 * @param diaPrima Día de pago de la prima
	 * @param tcm 
	 * @return prima Componente de prima
	 */
	public static BigDecimal prima (final Integer t, final Integer ttm, final BigDecimal pna, final Integer np, final BigDecimal prp, final Integer mescie,
			final Integer diaVto, final Integer diaPrima, final Integer tcm) {
		
		//Inicio variables locales
		BigDecimal prima = BigDecimal.ZERO;
		//Fin variables locales
		
		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Inicio Funcion << PRIMA >> de la clase FuncionesPrimas, para la entrada  t = {}, ttm = {}, pna = {},"
					+" np = {}, prp = {}, mescie = {}, diaVto = {}, diaPrima, tcm = {}",
					t, ttm, pna, np, prp, mescie, diaVto, diaPrima, tcm);
		}
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesPrimas.validarParamEntradaFuncionPrimaParte1(diaVto, ttm, pna, np);
		ValidacionesFuncionesPrimas.validarParamEntradaFuncionPrimaParte2(prp, mescie, diaVto, diaPrima);
		
		if (np != ConstantsFunciones.CTE_0) {
			int pp = ConstantsFunciones.CTE_0;
			int mesesPago = ConstantsFunciones.CTE_12 / np;
			
			pp = t % mesesPago;
			
			if (pp == ConstantsFunciones.CTE_0 && !(t == ttm && diaVto <= diaPrima)) {
				BigDecimal op1 = pna.divide(BigDecimal.valueOf(np),ConstantsFunciones.MATH_CONTEXT);
				BigDecimal op2 = BigDecimal.ONE.add(prp.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
				
				int exponente = ((t - tcm + ConstantsFunciones.CTE_12 - mescie - ConstantsFunciones.CTE_1) / ConstantsFunciones.CTE_12) + ConstantsFunciones.CTE_1;
				
				prima = op1.multiply(Util.pow(op2, exponente));
			}			
		}
		
		
		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Fin Funcion << PRIMA >> de la clase FuncionesPrimas, con resultado prima = {} ", prima);
		}

		return prima;		
	}
	
	/**
	 * Función que calcula el Movimiento de intereses de prima
	 * @param t Periodo mensual de proyección
	 * @param ttm Número de meses transcurridos desde el efecto a la fecha de vencimiento 
	 * @param prima Prima
	 * @param diaVto Día de vencimiento
	 * @param diaprima Día de pago de la prima
	 * @param i1 Tramo de interés 1
	 * @return intp Componentes de gastos de la prima
	 */
	public static BigDecimal intp (final Integer t, final Integer ttm, final BigDecimal prima, final Integer diaVto, final Integer diaPrima, final BigDecimal i1,
			final Timestamp fecEfect) {
		//Inicio variables locales
		BigDecimal intp;
		Integer varDia = null;
		BigDecimal varExponente;
		BigDecimal unoMasI1Entre100;
		//Fin variables locales
		
		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Inicio Funcion << INTP >> de la clase FuncionesPrimas, para la entrada  t = {}, ttm = {}, prima = {},"
					+"diaVto = {}, diaPrima = {}, i1 = {}",
					t, ttm, prima, diaVto, diaPrima, i1);
		}

		//Validaciones parametros de entrada
		ValidacionesFuncionesPrimas.validarParamEntradaFuncionIntp(t, ttm, prima, diaVto, diaPrima, i1);
		
		if (t == ttm) {
			varDia = diaVto;			
		}
		else {
			Fecha efectoIncr = UtilFechas.getFecha(UtilFechas.incrMeses(fecEfect, null, t, true));
			varDia = efectoIncr.getDia();
		}
		
		// 	varExponente = (dia-diaPrima + 1)/365
		varExponente = BigDecimal.valueOf(varDia - diaPrima + ConstantsFunciones.CTE_1).multiply(ConstantsFunciones.CTE_OPER_1_PARTIDO_365);
		unoMasI1Entre100 = BigDecimal.ONE.add(i1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		
		// INTP   = prima* (1+ i1/100)^(varExponente -1)  
		intp = prima.multiply(Util.pow(unoMasI1Entre100, varExponente).subtract(BigDecimal.ONE));
		
		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Fin Funcion << INTP >> de la clase FuncionesPrimas, con resultado intp = {} ", intp);
		}
		
		return intp;
	}
	
	/**
	 * Función que calcula el Movimiento de intereses de fondo
	 * @param t Periodo mensual de proyección
	 * @param ttm Número de meses transcurridos desde el efecto a la fecha de vencimiento
	 * @param diaVto Día de vencimiento
	 * @param gast Gasto de gestión
	 * @param cfall Componente de gastos sobre el fondo
	 * @param i1 Tramo de interés 1
	 * @param CC06 Costo de exoneración pago primas por sorteo
	 * @param bxAnterior Saldo inicial del periodo
	 * @return intg Componente de gastos del fondo
	 */
	public static BigDecimal intg (final Integer t, final Integer ttm, final Integer diaVto, final BigDecimal gast, final BigDecimal cfall,
			final BigDecimal i1, final BigDecimal CC06, final BigDecimal bxAnterior) {
		
		//Inicio variables locales
		BigDecimal intg;
		BigDecimal unoMasI1Entre100;
		BigDecimal varD2;
		//Fin variables locales
		
		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Inicio Funcion << INTG >> de la clase FuncionesPrimas, para la entrada  t = {}, ttm = {}, diaVto = {},"
					+"gast = {}, cfall = {}, i1 = {}, CC06 , bxAnterior = {}",
					t, ttm, diaVto, gast, cfall, i1, CC06, bxAnterior);
		}

		if (t == ttm) {
			varD2 = BigDecimal.valueOf(diaVto).multiply(ConstantsFunciones.CTE_OPER_1_PARTIDO_365);
		} else {
			varD2 = ConstantsFunciones.CTE_OPER_1ENTRE12;
		}
		
		// INTG = (bxAnterior-gast-cfall-cc06)* [(1+ i1/100)^(varD2)  -1]
		BigDecimal op1 = bxAnterior.subtract(gast).subtract(cfall).subtract(CC06);
		unoMasI1Entre100 = BigDecimal.ONE.add(i1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		
		intg = op1.multiply(Util.pow(unoMasI1Entre100, varD2).subtract(ConstantsFunciones.CTE_OPER_1));
		
		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Fin Funcion << INTG >> de la clase FuncionesPrimas, con resultado intg = {} ", intg);
		}
		
		return intg;		
	}
	
	/** 
	 * Función Auxiliar para el cálculo de la cuantía nominal de de la prestación a pagar en caso de fallecimiento o invalidez de un seguro COMPLETO.
	 * @param pnaTc La prima neta anual actual a fecha de cierre
	 * @param cformapago Codigo forma de pago de la prima
	 * @param TC Anualidad en curso
	 * @param T Anualidad a calcular
	 * @param pprUmic Primas Satisfechas en la anualidad T
	 * @param primaTarada Prima Tarada
	 * @param primaIni Prima Inicial
	 * @return PPTppal Identifica el resultado del cálculo realizado
	 * */
	
	public static BigDecimal PPTppal(final BigDecimal pnaTc,final String cformapago,final Integer TC, final Integer T, final BigDecimal pprUmic,final BigDecimal primaTarada, final BigDecimal primaIni){
		//Inicio variables locales
		BigDecimal PPTppal = BigDecimal.ZERO;
		BigDecimal varPendpa = BigDecimal.ZERO;
		//Fin variables locales
		
		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Inicio de la Funcion << PPTppal >> de la clase FuncionesAuxiliares, para la entrada pnaTc = {}, cformapago = {}," 
					+ " TC = {}, T = {}, pprUmic = {}, primaTarada = {}, primaIni = {}", pnaTc, cformapago,TC,T,pprUmic,primaTarada,primaIni);
		}
		
		//Validacion parametros de entrada
		ValidacionesFuncionesPrimas.validarParamEntradaFuncionPPTppal(pnaTc, cformapago, TC, T, pprUmic, primaTarada,primaIni);
	
		varPendpa = pendpa(cformapago, TC, T, pprUmic, primaTarada, primaIni);
		
		PPTppal = varPendpa.multiply(pnaTc);
		
		if (FuncionesPrimas.LOG.isTraceEnabled()) {
			FuncionesPrimas.LOG.trace("Fin Funcion << PPTppal >> de la clase FuncionesAuxiliares, con resultado PPTppal = {} ", PPTppal);
		}
		
		return PPTppal;
	}
	
	
}
