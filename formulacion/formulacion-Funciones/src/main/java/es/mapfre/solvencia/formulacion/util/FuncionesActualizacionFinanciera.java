package es.mapfre.solvencia.formulacion.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;

public class FuncionesActualizacionFinanciera {
	
	/** 
	 * Log funciones de actualización financiera.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(FuncionesActualizacionFinanciera.class);
	
	/**
	 * La función calcularTipoInteres obtiene el tipo de interés en la curva de tipos de interés recibida en base al criterio establecido al efecto.
	 * 
	 * @param criterioInteres
	 * 				Identifica el criterio de interpolación para la obtención del interés a aplicar.
	 * @param durAnt
	 * 				Duración inmediatamente anterior a la duración requerida.
	 * @param intAnt
	 * 				Tipo de interés asociado a la duración inmediatamente anterior a la duración requerida.
	 * @param durPost
	 * 				Duración inmediatamente posterior a la duración requerida.
	 * @param intPost
	 * 				Tipo de interés asociado a la duración inmediatamente posterior a la duración requerida.
	 * @param durJ
	 * 				Duración requerida
	 * @return intCT
	 */
	public static BigDecimal calcularTipoInteres(final String criterioInteres, final Integer durAnt, final BigDecimal intAnt,final Integer durPost,
			final BigDecimal intPost,final Integer durJ) {
		//Variables locales
		BigDecimal intCT = BigDecimal.ZERO;
		BigDecimal div;
		//Fin variables locales

		if (FuncionesActualizacionFinanciera.LOG.isTraceEnabled()) {
			FuncionesActualizacionFinanciera.LOG.trace("Inicio de la función << calcularTipoInteres >> de la clase FuncionesActualizacionFinanciera, para la entrada criterioInteres = {}, durAnt = {}, intAnt = {}, durPost = {}, intPost = {} y durJ = {} ", 
					criterioInteres, durAnt, intAnt, durPost, intPost, durJ);
		}

		//Validamos los campos de entrada
		ValidacionesFuncionesActualizacionFinanciera.validacionObligatoriosFuncionCalcularTiposInteres(criterioInteres, durAnt, intAnt, durPost, intPost, durJ);
		ValidacionesFuncionesActualizacionFinanciera.validarCriterioInteresFuncionCalcularTipoInteres(criterioInteres);


		if (ConstantsFunciones.CTE_CRI_IN_ME_DIA.equals(criterioInteres)) {
			//intCT<-- intAnt + ((durJ - durAnt) / (durPost - durAnt)) * (intPost - intAnt)
			div = BigDecimal.valueOf(durJ - durAnt).divide(BigDecimal.valueOf(durPost - durAnt), ConstantsFunciones.MATH_CONTEXT);
			intCT = intAnt.add(div.multiply(intPost.subtract(intAnt)));
		}

		if (FuncionesActualizacionFinanciera.LOG.isTraceEnabled()) {
			FuncionesActualizacionFinanciera.LOG.trace("Función de la función << calcularTipoInteres >> de la clase FuncionesActualizacionFinanciera, con resultado intCT = {}", intCT);
		}

		return intCT;
	}

	/**
	 * Este término identifica un diferimiento actuarial. Actualiza actuarialmente una cantidad desde el punto n hasta el p.
	 * 
	 * @param varx Edad x del asegurado
	 * @param varp Desplazamiento de la renta
	 * @param varn Duración del seguro
	 * @param varI1 Tramo de interés 1
	 * @param valoresTabMort Valores de la tabla de mortalidad
	 * @return	resultadoDif
	 */
	public static BigDecimal dif(final Integer varx, final Integer varp, final Integer varn, final BigDecimal varI1, final List<BigDecimal> lstValoresTabMort) {
		// Variables locales
		BigDecimal resultadoDif = BigDecimal.ZERO;
		BigDecimal lXMasN = BigDecimal.ZERO;
		BigDecimal lXMasP = BigDecimal.ONE;
		BigDecimal operando2Num = BigDecimal.ZERO;
		// Fin variables locales

		if (FuncionesActualizacionFinanciera.LOG.isTraceEnabled()) {
			FuncionesActualizacionFinanciera.LOG.trace("Inicio Funcion << dif >> de la clase FuncionesActualizacionFinanciera, para la entrada varx = {}, varp = {}, varn = {} , varI1 = {} y lstValoresTabMort = {}", varx, varp, varn, varI1, lstValoresTabMort);
		}

		//Validamos lo parametros de entrada
		//La misma validación que la funcion ax
		ValidacionesFuncionesActualizacionFinanciera.validarParamEntradaFuncionDif(varx, varp, varn, varI1, lstValoresTabMort); 

		if ((varx + varn)>lstValoresTabMort.size()-2){
			lXMasN = BigDecimal.ZERO;
			resultadoDif = BigDecimal.ZERO;
		}else{
			//L(x+n) = valoresTabMort(x+n).wkvalor
			lXMasN = lstValoresTabMort.get(varx + varn);
			//L(x+p) = valoresTabMort(x+p).wkvalor
			lXMasP = lstValoresTabMort.get(varx + varp);

			// operando2Num <-- (1+I1/100)^-(m-p)
			operando2Num = Util.pow(BigDecimal.ONE.add(varI1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)), -(varn - varp));

			//resultadoDif <-- l(X+n) * operando2Num / l(X+p)
			resultadoDif = lXMasN.multiply(operando2Num).divide(lXMasP, ConstantsFunciones.MATH_CONTEXT);
		}

		if (FuncionesActualizacionFinanciera.LOG.isTraceEnabled()) {
			FuncionesActualizacionFinanciera.LOG.trace("Fin Funcion << dif >> de la clase FuncionesActualizacionFinanciera, con resultado resultadoDif = {}", resultadoDif);
		}

		return resultadoDif;
	}


	/**
	 *                  D-1
	 *		äx : D¬ = SUMATORIO Vzc (fcal,j)* actfin(fcal,j)
	 *			        j=0 
	 *
	 * @param proyUmic
	 * 			Estructura detalleCorrientes de la umic
	 * @param bloqueCorriente
	 * 			Bloque de Trabajo de la corriente
	 * @param iteracion
	 * 			Indica el periodo de proyección J que se está calculando de entre todos los periodos de proyección de la umic (proyUmic)
	 * @param fcalc
	 * 			Fecha de calculo
	 * @param umic
	 * 			Contiene los datos de la Umic que se está procesando
	 * @param btcUmic
	 * 			Contiene el detalle de la base técnica de cálculo para la umic
	 * @param mapVariables 
	 * 			mapa con las variables de memoria necesarias
	 * @param codSubproceso
	 * 			Código del subproceso que se está ejecutando
	 * @return resultadoAsubXD
	 */
	public static BigDecimal aSubXD(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		// Variables locales
		BigDecimal resultadoAsubXD = BigDecimal.ZERO;
		// Fin variables locales

		if (FuncionesActualizacionFinanciera.LOG.isTraceEnabled()) {
			FuncionesActualizacionFinanciera.LOG.trace("Inicio Funcion << aSubXD >> de la clase FuncionesActualizacionFinanciera, para la entrada proyUmic = {}, bloqueCorriente = {}, iteracion = {}, fcalc = {}, umic = {}, btcUmic = {}, mapVariables = {} y codSubproceso = {}",
					proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
		}

		resultadoAsubXD = FuncionesSecundarias.aSubXImpl(proyUmic, bloqueCorriente, fcalc, umic, btcUmic, mapVariables, codSubproceso, false);

		if (FuncionesActualizacionFinanciera.LOG.isTraceEnabled()) {
			FuncionesActualizacionFinanciera.LOG.trace("Fin Funcion << aSubXD >> de la clase FuncionesActualizacionFinanciera, con resultado resultadoAsubXD = {} ", resultadoAsubXD);
		}

		return resultadoAsubXD;
	}
	
	/**
	 * VVIDA (fcal, actj): representa la actualizacion financiera a fcal desde
	 * la fecha de pago de cada renta j.
	 * 
	 * @param actj Corresponde al período transcurrido desde  la fecha alta ajuste o suscripción  hasta la fecha "j"
	 * @param numi1 Primer Interés técnico
	 * @param numi2 Segundo Interés técnico
	 * @param numm Número de años en que aplicamos un primer interés técnico desde fecha suscripción
	 * @param varTCY Resultado de la función naños() en base al criterio establecido entre la fecha de efecto de la suscripción y la fecha de cálculo
	 * @return resultadoVVida
	 */
	public static BigDecimal vVida(final BigDecimal actj, final BigDecimal numi1, final BigDecimal numi2, final BigDecimal numm, final BigDecimal varTCY,
			final BigDecimal numi1PorcentajeMasUno, final BigDecimal numi2PorcentajeMasUno) {
		// Variables locales
		BigDecimal resultadoVVida = BigDecimal.ZERO;
		// Fin variables locales

		if (FuncionesActualizacionFinanciera.LOG.isTraceEnabled()) {
			FuncionesActualizacionFinanciera.LOG.trace("Inicio Funcion << vVida >> de la clase FuncionesActualizacionFinanciera, para la entrada actj = {}, numi1 = {}, numi2 = {}, numm = {} y varTCY = {} ",
					actj, numi1, numi2, numm, varTCY);
		}

		ValidacionesFuncionesActualizacionFinanciera.validarParamEntradaFuncionVVida(actj, numi1, numi2, numm, varTCY);

		/**
		 * - Si j <= M:
		 * 		VVIDA = (1 + (I1*0.01)) ^ -(j - varTCY)
		 * - Si j > M y (varTCY) >= M:
		 * 		VVIDA = (1 + (I2*0.01)) ^ -(j - varTCY)
		 * - Si j > M y (varTCY) < M:
		 * 		VVIDA = ( (1 + (I1*0.01)) ^ -(M - varTCY) ) * ( (1 + (I2 * 0.01)) ^ -(j - M) )
		 */
		if (actj.compareTo(numm) <= 0) {
			resultadoVVida = Util.pow(numi1PorcentajeMasUno, actj.subtract(varTCY).negate());
		} else if (actj.compareTo(numm) > 0 && varTCY.compareTo(numm) >= 0) {
			resultadoVVida = Util.pow(numi2PorcentajeMasUno, actj.subtract(varTCY).negate());
		} else if (actj.compareTo(numm) > 0 && varTCY.compareTo(numm) < 0) {
			resultadoVVida =
					Util.pow(numi1PorcentajeMasUno, numm.subtract(varTCY).negate()).multiply(
							Util.pow(numi2PorcentajeMasUno, actj.subtract(numm).negate()));
		}

		if (FuncionesActualizacionFinanciera.LOG.isTraceEnabled()) {
			FuncionesActualizacionFinanciera.LOG.trace("Fin Funcion << vVida >> de la clase FuncionesActualizacionFinanciera, con resultado resultadoVVida = {}", resultadoVVida);
		}

		return resultadoVVida;
	}

	
	/**
	 * MORT(fcal) = representa la actualizacion financiera a fcal a la mitad
	 * del periodo que va hasta la proxima renovacion.
	 * 
	 * @param numi1 Primer Interés técnico
	 * @param numi2 Segundo Interés técnico
	 * @param numm Número de años en que aplicamos un primer interés técnico desde fecha suscripción
	 * @param numn Duración real del ajuste o suscripción
	 * @param varTCY Resultado de la función naños() en base al criterio establecido entre la fecha de efecto de la suscripción y la fecha de cálculo
	 * @param partAnoNR Fracción de año que hay entre la renovación anterior NR y posterior (NR+1).
	 * @param varRY Fracción de año incompleto entre la anterior fecha de renovación y la fecha de cálculo fcal.
	 * @return resultadoVmort
	 */
	public static BigDecimal vmort(final BigDecimal numi1, final BigDecimal numi2, final BigDecimal numm, final BigDecimal numn,
			final BigDecimal varTCY, final BigDecimal partAnoNR, final BigDecimal varRY,
			final BigDecimal numi1PorcentajeMasUno, final BigDecimal numi2PorcentajeMasUno) {
		// Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		// Fin variables locales

		if (FuncionesActualizacionFinanciera.LOG.isTraceEnabled()) {
			FuncionesActualizacionFinanciera.LOG.trace("Inicio Funcion << vmort >> de la clase FuncionesActualizacionFinanciera, para la entrada numi1 = {}, numi2 = {}, numm = {}, numn = {}, varTCY = {}, partAnoNR = {}, varRY = {} ",
					numi1, numi2, numm, numn, varTCY, partAnoNR, varRY);
		}

		//Validamos los parametros de entrada
		ValidacionesFuncionesActualizacionFinanciera.validarParamEntradaFuncionVMort(numi1, numi2, numm, numn, varTCY, partAnoNR, varRY);

		/**
		 * - Si N <= M:
		 * 		VMORT = (1 + (I1* 0.01)) ^ -( (partAnoNR - varRY) / 2 )
		 * - Si N > M y (varTCY) >= M:
		 * 		VMORT = (1 + (I2* 0.01)) ^ -( (partAnoNR - varRY) / 2 )
		 * - Si N > M y (varTCY) < M y (varTCY + ( (partAnoNR - varRY) / 2) ) <= M:
		 * 		VMORT = (1 + (I1* 0.01)) ^ -( (partAnoNR - varRY) / 2 )
		 * - Si N > M y (varTCY) < M y (varTCY + ( (partAnoNR - varRY) / 2) ) > M:
		 * 		VMORT = ( (1 + (I1* 0.01)) ^ -(M - varTCY) ) * ( (1 + (I2* 0.01)) ^ -( ( (partAnoNR - varRY) / 2 ) - (M - varTCY) ) )
		 */


		final BigDecimal divisionNR = partAnoNR.subtract(varRY).divide(ConstantsFunciones.CTE_OPER_2, ConstantsFunciones.MATH_CONTEXT);
		if (numn.compareTo(numm) <= 0) {
			resultado = Util.pow(numi1PorcentajeMasUno, divisionNR.negate());
		} else if (numn.compareTo(numm) > 0 ) {
			if (varTCY.compareTo(numm) >= 0) {
				resultado = Util.pow(numi2PorcentajeMasUno, divisionNR.negate());
			} else if (varTCY.compareTo(numm) < 0) {
				if (varTCY.add(divisionNR).compareTo(numm) <= 0) {
					resultado = Util.pow(numi1PorcentajeMasUno, divisionNR.negate());
				} else if (varTCY.add(divisionNR).compareTo(numm) > 0) {
					final BigDecimal operando1 = Util.pow(numi1PorcentajeMasUno, numm.subtract(varTCY).negate());
					final BigDecimal operando2 = Util.pow(numi2PorcentajeMasUno, divisionNR.subtract(numm.subtract(varTCY)).negate());

					resultado = operando1.multiply(operando2);
				}
			}
		}

		if (FuncionesActualizacionFinanciera.LOG.isTraceEnabled()) {
			FuncionesActualizacionFinanciera.LOG.trace("Fin Funcion << vmort >> de la clase FuncionesActualizacionFinanciera, con resultadovmort = {} ", resultado);
		}

		return resultado;
	}
	
	/**
	 * Función que calcula este término de la formulación de la corriente de Provisión Matemática por fórmula cerrada.
	 * 
	 * @param vari1 Primer Interés técnico 
	 * @param vari2 Segundo Interés técnico 
	 * @param i1Bti Primer Interés técnico según Bases Técnicas Iniciales
	 * @param i2Bti Segundo Interés técnico según Bases Técnicas Iniciales
	 * @param beta1 Variable Beta1
	 * @param beta2 Variable Beta2
	 * @param varm Número de años en que aplicamos un primer interés técnico desde fecha suscripción
	 * @param varn Duración real del ajuste o suscripción
	 * @param ren Período de tiempo transcurrido desde la fecha de alta del ajuste hasta la fecha de renovación siguiente al momento de cálculo
	 * @param varj Corresponde al período transcurrido desde  la fecha alta ajuste o suscripción  hasta la fecha “j” 
	 * @return rflex
	 */
	public static BigDecimal rflex(final BigDecimal vari1, final BigDecimal vari2, final BigDecimal i1Bti, final BigDecimal i2Bti, 
			final BigDecimal beta1, final BigDecimal beta2, final BigDecimal varm, final BigDecimal varn, final BigDecimal ren, final BigDecimal varj,
			final BigDecimal vari1PorcentajeMasUno, final BigDecimal vari2PorcentajeMasUno) {
		//Variables locales
		BigDecimal rflex = BigDecimal.ZERO;
		BigDecimal calNmenorIguM;
		BigDecimal operando1 = BigDecimal.ZERO;
		BigDecimal operando2 = BigDecimal.ZERO;
		BigDecimal numOperando1 = BigDecimal.ZERO;
		BigDecimal numOperando2 = BigDecimal.ZERO;
		BigDecimal denOperando1 = BigDecimal.ZERO;
		BigDecimal denOperando2 = BigDecimal.ZERO;
		//Fin varibles locales

		if (FuncionesActualizacionFinanciera.LOG.isTraceEnabled()) {
			FuncionesActualizacionFinanciera.LOG.trace("Inicio función << rflex >> de la clase FuncionesActualizacionFinanciera con la entrada vari1 = {}, vari2 = {}, i1Bti = {}, i2Bti = {}, beta1 = {}, beta2 = {}, varm = {}, varn = {}, ren = {} y varj = {}", vari1, vari2, i1Bti, i2Bti, beta1, beta2, varm, varn, ren, varj);
		}

		//Validamos campos de entrada
		ValidacionesFuncionesActualizacionFinanciera.validarParamEntradaFuncionRflexParte1(vari1, vari2, i1Bti, i2Bti, beta1);
		ValidacionesFuncionesActualizacionFinanciera.validarParamEntradaFuncionRflexParte2(beta2, varm, varn, ren, varj);

		/**
		 * 	Si  N <= M: 
				RFLEX = (1+(i1Bti*0.01)-beta1 )^(j+1)/(1+I1)^(j+0,5) 

			Si N > M  y además:
				Si REN <= M  y j <= (M – REN – 0’5)  
					RFLEX = (1+(i1Bti*0.01)-beta1 )^(j+1)/(1+I1)^(j+0,5) 
				Si REN <= M  y  j <= (M–REN)  y   j> (M-REN-0’5)
					RFLEX = (1+(i1Bti*0.01)-beta1 )^(j+1)/((1+(I2*0.01))^(j-(M-REN)+O,5)* (1+I1)^((M-REN) ) )

				Si REN <= M y  j >(M-REN)
					RFLEX = ((1+(i2Bti*0.01)-beta2 )^(j-  entero(M-REN))* (1+i1Bti-beta1 )^(ENTERO(M-REN)+1))/((1+(I2*0.01))^(j-(M-REN)+O,5)  *  (1+I1)^((M-REN) ) )

				Si REN > M  
					RFLEX = (1+(i2Bti*0.01)-beta2 )^(j+1)/(1+(I2*0.01))^(j+0,5) 

		 */

		//RFLEX = (1+(i1Bti*0.01)-beta1 )^(j+1)/(1+I1)^(j+0,5) 
		operando1 = Util.pow(BigDecimal.ONE.add(i1Bti.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)).subtract(beta1), varj.add(BigDecimal.ONE));
		operando2 = Util.pow(vari1PorcentajeMasUno, varj.add(ConstantsFunciones.CTE_OPER_0_PUNTO_5));

		calNmenorIguM = operando1.divide(operando2, ConstantsFunciones.MATH_CONTEXT);

		if (varn.compareTo(varm) <= 0) {
			//RFLEX = (1+(i1Bti*0.01)-beta1 )^(j+1)/(1+I1)^(j+0,5) 
			rflex = calNmenorIguM;
		} else {

			// (1+(i1Bti*0.01)-beta1)
			final BigDecimal unoMasI1BtiMenosB1 = BigDecimal.ONE.add(i1Bti.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)).subtract(beta1);
			// (M-REN)
			final BigDecimal varmBDMenosRen = varm.subtract(ren);

			if (ren.compareTo(varm) <= 0 && varj.compareTo(varmBDMenosRen.subtract(ConstantsFunciones.CTE_OPER_0_PUNTO_5)) <= 0) {
				//RFLEX = (1+(i1Bti*0.01)-beta1 )^(j+1)/(1+(I1*0.01))^(j+0,5) 
				rflex = calNmenorIguM;
			} else if (ren.compareTo(varm) <= 0 && varj.compareTo(varmBDMenosRen) <= 0 && varj.compareTo(varmBDMenosRen.subtract(ConstantsFunciones.CTE_OPER_0_PUNTO_5)) > 0) {
				//RFLEX = (1+(i1Bti*0.01)-beta1 )^(j+1)/((1+(I2*0.01))^(j-(M-REN)+O,5)* (1+(I1*0.01))^((M-REN) ) )
				//(1+(i1Bti*0.01)-beta1 )^(j+1)
				operando1 = Util.pow(unoMasI1BtiMenosB1, varj.add(BigDecimal.ONE));
				//(1+(I2*0.01))^(j-(M-REN)+O,5)
				denOperando1 = Util.pow(vari2PorcentajeMasUno, varj.subtract(varmBDMenosRen).add(ConstantsFunciones.CTE_OPER_0_PUNTO_5));
				//(1+(I1*0.01))^((M-REN) )
				denOperando2 = Util.pow(vari1PorcentajeMasUno, varmBDMenosRen);

				rflex = operando1.divide(denOperando1.multiply(denOperando2), ConstantsFunciones.MATH_CONTEXT);
			} else if (ren.compareTo(varm) <= 0 && varj.compareTo(varmBDMenosRen) > 0) {
				//((1+(i2Bti*0.01)-beta2 )^(j-  entero(M-REN))
				numOperando1 = Util.pow(BigDecimal.ONE.add(i2Bti.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)).subtract(beta2), varj.subtract(BigDecimal.valueOf(varmBDMenosRen.intValue())));
				//(1+(i1Bti*0.01)-beta1 )^(ENTERO(M-REN)+1)
				numOperando2 = Util.pow(unoMasI1BtiMenosB1, BigDecimal.valueOf(varmBDMenosRen.intValue()).add(BigDecimal.ONE));
				//(1+(I2*0.01))^(j-(M-REN)+O,5)
				denOperando1 = Util.pow(vari2PorcentajeMasUno, varj.subtract(varmBDMenosRen).add(ConstantsFunciones.CTE_OPER_0_PUNTO_5));
				//(1+(I1*0.01))^((M-REN) )
				denOperando2 = Util.pow(vari1PorcentajeMasUno, varmBDMenosRen);

				//	RFLEX = ((1+i2Bti-beta2 )^(j-  entero(M-REN))* (1+i1Bti-beta1 )^(ENTERO(M-REN)+1))/((1+I2)^(j-(M-REN)+O,5)  *  (1+I1)^((M-REN) ) )
				rflex = numOperando1.multiply(numOperando2).divide(denOperando1.multiply(denOperando2), ConstantsFunciones.MATH_CONTEXT);
			} else if (ren.compareTo(varm) > 0) {
				//(1+(i2Bti*0.01)-beta2 )^(j+1)
				operando1 = Util.pow(BigDecimal.ONE.add(i2Bti.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)).subtract(beta2), varj.add(BigDecimal.ONE));
				//(1+(I2*0.01))^(j+0,5) 
				operando2 = Util.pow(vari2PorcentajeMasUno, varj.add(ConstantsFunciones.CTE_OPER_0_PUNTO_5));

				//RFLEX = (1+(i2Bti*0.01)-beta2 )^(j+1)/(1+I2)^(j+0,5) 
				rflex = operando1.divide(operando2, ConstantsFunciones.MATH_CONTEXT);
			}
		}

		if (FuncionesActualizacionFinanciera.LOG.isTraceEnabled()) {
			FuncionesActualizacionFinanciera.LOG.trace("Fin función << rflex >> de la clase FuncionesActualizacionFinanciera con resultado rflex = {}", rflex);
		}

		return rflex;
	}

	/**
	 * Función que calcula este término de la formulación de la corriente de Provisión Matemática por fórmula cerrada.
	 * 
	 * @param vari1 Primer Interés técnico 
	 * @param vari2 Segundo Interés técnico
	 * @param i1Bti Primer Interés técnico según Bases Técnicas Iniciales
	 * @param i2Bti Segundo Interés técnico según Bases Técnicas Iniciales
	 * @param beta1 Variable Beta1
	 * @param beta2 Variable Beta2
	 * @param varm Número de años en que aplicamos un primer interés técnico desde fecha suscripción
	 * @param ren Período de tiempo transcurrido desde la fecha de alta del ajuste hasta la fecha de renovación siguiente al momento de cálculo
	 * @param difercol Diferimiento en cobrar la renta
	 * @param nirp Número de años completos que quedan pendientes desde la siguiente renovación a la fecha de cálculo hasta la fecha de inicio de cobro de rentas
	 * @param nirmv Fracción de año comprendida entre la fecha de renovación inmediata anterior a la fecha de inicio de cobro de renta y dicha fecha de inicio cobro renta
	 * @return actFall
	 */
	public static BigDecimal actfall(final BigDecimal vari1, final BigDecimal vari2, final BigDecimal i1Bti, final BigDecimal i2Bti, 
			final BigDecimal beta1, final BigDecimal beta2, final BigDecimal varm, final BigDecimal ren, final BigDecimal difercol, final Integer nirp, final BigDecimal nirmv,
			final BigDecimal varI1PorcentajeMasUno) {
		//Variables locales
		BigDecimal actFall = BigDecimal.ZERO;
		BigDecimal diferMenoIguM = BigDecimal.ZERO;
		BigDecimal operando1 = BigDecimal.ZERO;
		BigDecimal operando2 = BigDecimal.ZERO;
		BigDecimal denOperando1 = BigDecimal.ZERO;
		BigDecimal denOperando2 = BigDecimal.ZERO;
		BigDecimal numOperando1 = BigDecimal.ZERO;
		BigDecimal numOperando2 = BigDecimal.ZERO;
		//Fin variables locales

		if (FuncionesActualizacionFinanciera.LOG.isTraceEnabled()) {
			FuncionesActualizacionFinanciera.LOG.trace("Inicio función << actfall >> de la clase FuncionesActualizacionFinanciera con la entrada vari1 = {}, vari2 = {}, i1Bti = {}, i2Bti = {}, beta1 = {}, beta2 = {}, varm = {}, ren = {}, difercol = {}, nirp = {} y nirp = {}", vari1, vari2, i1Bti, i2Bti, beta1, beta2, varm, ren, difercol, nirp, nirmv);
		}

		//Validamos parametros de entrada
		ValidacionesFuncionesActualizacionFinanciera.validarParamEntradaFuncionActFallParte1(vari1, vari2, i1Bti, i2Bti, beta1, beta2);
		ValidacionesFuncionesActualizacionFinanciera.validarParamEntradaFuncionActFallParte2(varm, ren, difercol, nirp, nirmv);

		/**
		 * 	Si  DIFERCOL <= M: 
				actFall = (1+(i1Bti*0.01)-beta1 )^(NIRP+1)/(1+(I1*0.01))^((NIRP+ NRImv/2)) 

			Si DIFERCOL > M  y además:
				Si REN <= M y NIRP <= (M-REN-NIRmv/2):
					actFall = (1+(i1Bti*0.01)-beta1 )^(NIRP+1)/(1+(I1*0.01))^((NIRP+ NRImv/2)) 
				Si REN <= M y NIRP <= (M-REN) y NIRP > (M-REN-(NDmv/2)):
					actFall = (1+(i1Bti*0.01)-beta1 )^(NIRP+1)/((1+(I2*0.01))^(NIRP-(M-REN)+(NIRmv/2))* (1+I1)^((M-REN) ) )
				Si REN <= M y NIRP > (M-REN):
					actFall = ((1+(i2Bti*0.01)-beta2 )^(NIRP-  entero(M-REN))* (1+(i1Bti*0.01)-beta1 )^(ENTERO(M-REN)+1))/((1+I2)^(NIRP-(M-REN)+(NIRmv/2))  *  (1+I1)^((M-REN) ) )				
				Si REN > M  
					actFall = (1+(i2Bti*0.01)-beta2 )^(NIRP+1)/(1+(I2*0.01))^(NIRP+(NIRmv/2)) 
		 */
		//actFall = (1+(i1Bti*0.01)-beta1 )^(NIRP+1)/(1+(I1*0.01))^((NIRP+ NRImv/2)) 
		//(1+(i1Bti*0.01)-beta1)^(NIRP+1)

		final BigDecimal nirpBD = BigDecimal.valueOf(nirp);
		// (1+(i1Bti*0.01)-beta1)
		final BigDecimal unoMasI1BtiMenosB1 = BigDecimal.ONE.add(i1Bti.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)).subtract(beta1);
		// (NIRmv/2)
		final BigDecimal nirmvEntre2 = nirmv.divide(ConstantsFunciones.CTE_OPER_2, ConstantsFunciones.MATH_CONTEXT);

		operando1 = Util.pow(unoMasI1BtiMenosB1, nirpBD.add(BigDecimal.ONE));
		//(1+(I1*0.01))^((NIRP+ NRImv/2))
		operando2 = Util.pow(varI1PorcentajeMasUno, nirpBD.add(nirmvEntre2));

		diferMenoIguM = operando1.divide(operando2, ConstantsFunciones.MATH_CONTEXT);

		if (difercol.compareTo(varm) <= 0) {
			//actFall = (1+(i1Bti*0.01)-beta1 )^(NIRP+1)/(1+(I1*0.01))^((NIRP+ NRImv/2)) 
			actFall = diferMenoIguM;
		} else {

			// (1+(I2*0.01))
			final BigDecimal unoMasVari2 = BigDecimal.ONE.add(vari2.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
			// (M-REN)
			final BigDecimal varmBDMenosRen = varm.subtract(ren);

			if (ren.compareTo(varm) <= 0 && nirpBD.compareTo(varmBDMenosRen.subtract(nirmvEntre2)) <= 0) {
				//actFall = (1+(i1Bti*0.01)-beta1 )^(NIRP+1)/(1+(I1*0.01))^((NIRP+ NRImv/2)) 
				actFall = diferMenoIguM;
			} else if (ren.compareTo(varm) <= 0 && nirpBD.compareTo(varmBDMenosRen) <= 0 
					&& nirpBD.compareTo(varmBDMenosRen.subtract(nirmvEntre2)) > 0) {
				//(1+(i1Bti*0.01)-beta1 )^(NIRP+1)
				operando1 = Util.pow(unoMasI1BtiMenosB1, nirpBD.add(BigDecimal.ONE));
				//((1+(I2*0.01))^(NIRP-(M-REN)+(NIRmv/2))
				denOperando1 = Util.pow(unoMasVari2, nirpBD.subtract(varmBDMenosRen).add(nirmvEntre2));
				//(1+(I1*0.01))^((M-REN) )
				denOperando2 = Util.pow(varI1PorcentajeMasUno, varmBDMenosRen);

				//actFall = (1+(i1Bti*0.01)-beta1 )^(NIRP+1)/((1+(I2*0.01))^(NIRP-(M-REN)+(NIRmv/2))* (1+(I1*0.01))^((M-REN) ) )
				actFall = operando1.divide(denOperando1.multiply(denOperando2, ConstantsFunciones.MATH_CONTEXT), ConstantsFunciones.MATH_CONTEXT);
			} else if (ren.compareTo(varm) <= 0 && nirpBD.compareTo(varmBDMenosRen) > 0) {
				//((1+(i2Bti*0.01)-beta2 )^(NIRP-  entero(M-REN))
				numOperando1 = Util.pow(BigDecimal.ONE.add(i2Bti.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)).subtract(beta2), nirpBD.subtract(BigDecimal.valueOf(varmBDMenosRen.intValue())));
				//(1+(i1Bti*0.01)-beta1 )^(ENTERO(M-REN)+1))
				numOperando2 = Util.pow(unoMasI1BtiMenosB1, BigDecimal.valueOf(varmBDMenosRen.intValue()).add(BigDecimal.ONE));
				//((1+(I2*0.01))^(NIRP-(M-REN)+(NIRmv/2))
				denOperando1 = Util.pow(unoMasVari2, nirpBD.subtract(varmBDMenosRen).add(nirmvEntre2));
				//(1+(I1*0.01))^((M-REN))
				denOperando2 = Util.pow(varI1PorcentajeMasUno, varmBDMenosRen);

				//actFall = ((1+(i2Bti*0.01)-beta2 )^(NIRP-  entero(M-REN))* (1+(i1Bti*0.01)-beta1 )^(ENTERO(M-REN)+1))/((1+(I2*0.01))^(NIRP-(M-REN)+(NIRmv/2))  *  (1+(I1*0.01))^((M-REN) ) )
				actFall = numOperando1.multiply(numOperando2).divide(denOperando1.multiply(denOperando2), ConstantsFunciones.MATH_CONTEXT);
			} else if (ren.compareTo(varm) > 0) {
				//(1+(i2Bti*0.01)-beta2 )^(NIRP+1)
				operando1 = Util.pow(BigDecimal.ONE.add(i2Bti.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)).subtract(beta2), nirpBD.add(BigDecimal.ONE));
				//(1+(I2*0.01))^(NIRP+(NIRmv/2)) 
				operando2 = Util.pow(unoMasVari2, nirpBD.add(nirmvEntre2));

				//actFall = (1+(i2Bti*0.01)-beta2 )^(NIRP+1)/(1+(I2*0.01))^(NIRP+(NIRmv/2)) 
				actFall = operando1.divide(operando2, ConstantsFunciones.MATH_CONTEXT);
			}
		}

		if (FuncionesActualizacionFinanciera.LOG.isTraceEnabled()) {
			FuncionesActualizacionFinanciera.LOG.trace("Fin función << actfall >> de la clase FuncionesActualizacionFinanciera con resultado actFall = {}", actFall);
		}

		return actFall;
	}

	/**
	 * Función que calcula este término de la formulación de la corriente de Provisión Matemática por fórmula cerrada.
	 * CMORPEND (fcal) = (lzc - lxREN) * VMORT(fcal) /lzc
	 * 
	 * @param fecIni Fecha de inicio de cobro de la renta
	 * @param fecvto Fecha de vencimiento
	 * @param fecAntRenova Fecha de anterior renovación
	 * @param ndap Número de años completos que quedan pendientes desde la siguiente renovación a la fecha de cálculo hasta el vencimiento
	 * @param nirp Número de años completos que quedan pendientes desde la siguiente renovación a la fecha de cálculo hasta la fecha de inicio de cobro de rentas
	 * @param lstValoresTabMort Valores de la tabla de mortalidad
	 * @param varzc Edad del asegurado a Fecha de cálculo
	 * @param xren Edad en la renovación siguiente a la fecha de cálculo fcal.
	 * @param vmort Actualización financiera a fcal a la mitad del período que va hasta la próxima renovación
	 * @param modBeta Variable de Apoyo MODBETA
	 * @param i1Bti Primer Interés técnico según Bases Técnicas Iniciales
	 * @param i2bti Segundo Interés técnico según Bases Técnicas Iniciales
	 * @return cmorpend
	 */
	public static BigDecimal cmorpend(final Timestamp fecIni, final Timestamp fecvto, final Timestamp fecProxRenova, final Integer ndap, 
			final Integer nirp, final List<BigDecimal> lstValoresTabMort, final BigDecimal varzc, final BigDecimal xren, final BigDecimal vmort) {
		//Variables locales
		BigDecimal cmorpend = BigDecimal.ZERO;
		BigDecimal varLzc = BigDecimal.ONE;
		BigDecimal varLxren = BigDecimal.ZERO;
		//Fin variables locales

		if (FuncionesActualizacionFinanciera.LOG.isTraceEnabled()) {
			FuncionesActualizacionFinanciera.LOG.trace("Inicio función << cmorpend >> de la clase FuncionesActualizacionFinanciera con entrada fecIni = {}, fecvto = {}, fecProxRenova = {}, ndap = {}, nirp = {}, lstValoresTabMort = {}, varzc = {}, xren = {}, vmort = {}", fecIni, fecvto, fecProxRenova, ndap, nirp, lstValoresTabMort, varzc, xren, vmort);
		}

		//Validamos campos de entrada
		ValidacionesFuncionesActualizacionFinanciera.validarParamEntradaFuncionCmorpendParte1(fecIni, fecProxRenova, ndap, nirp, lstValoresTabMort);
		ValidacionesFuncionesActualizacionFinanciera.validarParamEntradaFuncionCmorpendParte2(varzc, xren, vmort);

		/**
		 * 	Si  NDap <=  0  y además (fecVto <= fecAntRenova)  
				cmorpend = 0
			Si NIRP <= 0 y además (fecIni=< fecAntRenova)
				cmorpend = 0
			Si modBeta = SR 
				cmorpend = 0
			En cualquier otro caso se tendrá: 
				varLzc = valoresTabMort(ZC).wkvalor --> Dato de valoresTabMort para wkedad = ZC
				varLXren = valoresTabMort(Xren).wkvalor --> Dato de valoresTabMort para wkedad = Xren

			Cmorpend = ((varLzc -varLXren   ))/(varLzc )*VMort

		 */

		// Si NDap <= 0 y además (fecVto <= fecAntRenova) o NIRP <= 0 y además (fecIni =< fecAntRenova) o modBeta = SR
		if ((ndap.intValue() <= 0 &&  (fecvto == null || !fecvto.after(fecProxRenova)) || (nirp.intValue() <= 0 && !fecIni.after(fecProxRenova)))) {
			cmorpend = BigDecimal.ZERO;
		} else {

			final int zc = varzc.intValue();
			if (zc > lstValoresTabMort.size()-2){
				varLzc = BigDecimal.ZERO;
				return BigDecimal.ZERO; 
			} else {
				BigDecimal varLzcEntero = lstValoresTabMort.get(zc);
				BigDecimal varLzcEntero1 = lstValoresTabMort.get(zc + 1);
				varLzc = Util.interpolaPorEdad(varLzcEntero, varLzcEntero1, varzc);
			}

			if (varLzc.signum()==0){
				cmorpend = BigDecimal.ZERO;
			} else {

				final int varXren = xren.intValue();
				if (varXren > lstValoresTabMort.size()-2){
					varLxren = BigDecimal.ZERO;
				} else {
					BigDecimal varLXrenEntero = lstValoresTabMort.get(varXren);
					BigDecimal varLXrenEntero1 = lstValoresTabMort.get(varXren+ 1);
					varLxren = Util.interpolaPorEdad(varLXrenEntero, varLXrenEntero1, xren);
				}

				cmorpend = ((varLzc.subtract(varLxren)).divide(varLzc, ConstantsFunciones.MATH_CONTEXT)).multiply(vmort);
			}

		}

		if (FuncionesActualizacionFinanciera.LOG.isTraceEnabled()) {
			FuncionesActualizacionFinanciera.LOG.trace("Fin función << cmorpend >> de la clase FuncionesActualizacionFinanciera con resultado cmorpend = {}", cmorpend);
		}

		return cmorpend;
	}

	/**
	 * Función calcula la actualización financiera genérica utilizada en la formulación de provisión por fórmula cerrada.
	 * @param var1 Duracion periodo de actualización 1.  
	 * @param var2 Duracion periodo de actualización 2.
	 * @param m Número de años en que aplicamos un primer interés técnico.
	 * @param unoMasI1Entre100 Primer Interés técnico mas 1 entre 100.
	 * @param unoMasI2Entre100 Segundo Interés técnico mas 1 entre 100.
	 * @param mapVariables
	 * @return actVbx
	 */
	public static BigDecimal actVbx(final BigDecimal var1, final BigDecimal var2, final BigDecimal m, 
			final BigDecimal unoMasI1Entre100, final BigDecimal unoMasI2Entre100, final Map<String, Object> mapVariables){
		//Inicio variables locales
		BigDecimal actVbx = BigDecimal.ZERO;
		//Fin variables locales	
		
		if (FuncionesActualizacionFinanciera.LOG.isTraceEnabled()) {
			FuncionesActualizacionFinanciera.LOG.trace("Inicio Funcion << ACTVBX >> de la clase FuncionesActualizacionFinanciera, para la entrada  var1 = {}, var2 = {},"
					+" m = {}, unoMasI1Entre100 = {}, unoMasI2Entre100 = {}", 
					var1, var2, m, unoMasI1Entre100, unoMasI2Entre100);
		}
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesActualizacionFinanciera.validarParamEntradaFuncionActVbx(var1, var2, m, unoMasI1Entre100, unoMasI2Entre100);
		
		
		if (var1.compareTo(m) <= 0){
			if (var2.compareTo(m) <= 0){
				actVbx = Util.pow(unoMasI1Entre100, (var2.subtract(var1)).negate());
			} else {
				BigDecimal op1 = (BigDecimal) mapVariables.get("actVbx"+var1+m);
				if (op1 == null){
					op1 =Util.pow(unoMasI1Entre100, (m.subtract(var1)).negate());
					mapVariables.put("actVbx"+var1+m, op1);
				}
				actVbx = op1.multiply(Util.pow(unoMasI2Entre100, (var2.subtract(m)).negate()));
			}
		} else {
			actVbx = Util.pow(unoMasI2Entre100, (var2.subtract(var1)).negate());
		}
		
		if (FuncionesActualizacionFinanciera.LOG.isTraceEnabled()) {
			FuncionesActualizacionFinanciera.LOG.trace("Fin Funcion << ACTVBX >> de la clase FuncionesActualizacionFinanciera, con resultado actVbx = {} ", actVbx);
		}
		
		return actVbx;
	}


}
