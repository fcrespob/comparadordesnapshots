package es.mapfre.solvencia.formulacion.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.formulacion.PeriodosFall;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.LimitesCapital;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.formulacion.util.UtilFechas.Fecha;

public class FuncionesFallecimiento {
	
	/** 
	 * Log funciones de fallecimiento.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(FuncionesFallecimiento.class);
	
	/**
	 * La función vfal calcula un fallecimiento desplazado k meses.
	 * 
	 * @param mesesCompletos 
	 * 				Meses completos transcurridos desde efecto (técnico) póliza a fecha cálculo
	 * @param tipoIPrimerTramo
	 * 			 	Tipo de interés primer tramo
	 * @param duracionPrimerTra 
	 * 				Duración primer tramo
	 * @param tipoISegundoTramo 
	 * 				Tipo de interés segundo tramo
	 * @param edadActuarial 
	 * 				Edad actuarial a fecha efecto técnico.
	 * @param lstValoresTabMort
	 * 				Valores de la tabla de mortalidad
	 * @param prima 
	 * 				prima
	 * @param cRm 
	 * 				Capital en riesgo máximo
	 * @param gamma 
	 * 				% sobre prima para capital fallecimiento
	 * @param mescare 
	 * 				Meses carencia
	 * @param fecnac 
	 * 				Fecha de Nacimiento del asegurado
	 * @return resultadoVfal
	 */
	public static BigDecimal vfal(final Integer mesesCompletos, final BigDecimal tipoIPrimerTramo, final Integer duracionPrimerTra, final BigDecimal tipoISegundoTramo,
			final Integer edadActuarial, final List<BigDecimal> lstValoresTabMort, final BigDecimal prima, final List<LimitesCapital> varListaLimCap, 
			final BigDecimal gamma, final Integer mescare, final boolean varRAgravado, final BigDecimal varCRMax) {
		//Variables locales
		BigDecimal resultadoVfal = BigDecimal.ZERO;
		int uveDoble = 0;
		BigDecimal operando2 = BigDecimal.ZERO;
		BigDecimal operando3 = BigDecimal.ZERO;
		boolean calculadoOperando3 = false;
		BigDecimal operando1 = BigDecimal.ZERO;
		BigDecimal varCrm = BigDecimal.ZERO;
		LimitesCapital varLimCap;
		BigDecimal operando1SinCarencia;
		//Fin variables locales

		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Inicio de la función << vfal >> de la clase FuncionesFallecimiento, para la entrada mesesCompletos = {}, tipoInteresPrimerTramo = {}, duracionPrimerTramo = {}, tipoInteresSegundoTramo = {}, edadActuarial = {}, tablaExperiencia = {}, prima = {}, varLimCap = {}, gamma = {} y mescare = {}",
					mesesCompletos, tipoIPrimerTramo, duracionPrimerTra, tipoISegundoTramo, edadActuarial, lstValoresTabMort, prima, varListaLimCap, gamma, mescare);
		}

		// varw = Para obtener la variable W se buscará en la tabla valoresTabMort la mínima edad en la que wkvalor  se hace cero
		uveDoble = Util.obtenerEdadPrimerCero(lstValoresTabMort);

		//validamos los campos obligatorios
		ValidacionesFuncionesFallecimiento.validacionCamposFuncionVFalParte1(mesesCompletos, tipoIPrimerTramo, duracionPrimerTra, tipoISegundoTramo, edadActuarial, lstValoresTabMort);
		ValidacionesFuncionesFallecimiento.validacionCamposFuncionVFalParte2(prima, gamma, mescare);



		// Calculamos valores que son constantes dentro del bucle
		//varEdad1 = x + (k/12)
		BigDecimal varEdad1 = BigDecimal.valueOf(Util.mult12(edadActuarial) + mesesCompletos).multiply(ConstantsFunciones.CTE_OPER_1ENTRE12, ConstantsFunciones.MATH_CONTEXT);
		//base <-- (1 + i1)
		BigDecimal base1 = BigDecimal.ONE.add(tipoIPrimerTramo.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		//base <-- (1 + i2)
		BigDecimal base2 = BigDecimal.ONE.add(tipoISegundoTramo.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));

		//Se calculará Vfal como el sumatorio en t desde t= k  hasta t= (w-x)*12 -1
		int limiteInferior = mesesCompletos;
		int limiteSuperior = Util.mult12(uveDoble - edadActuarial) - 1;
		boolean calculadoExponente1 = false;

		BigDecimal elevado = BigDecimal.ZERO;
		BigDecimal elevado1 = BigDecimal.ZERO;
		BigDecimal elevado2 = BigDecimal.ZERO;

		final BigDecimal incrInt1 = Util.pow(base1, BigDecimal.ONE.multiply(ConstantsFunciones.CTE_OPER_1ENTRE12, ConstantsFunciones.MATH_CONTEXT).negate());
		final BigDecimal incrInt2 = Util.pow(base2, BigDecimal.ONE.multiply(ConstantsFunciones.CTE_OPER_1ENTRE12, ConstantsFunciones.MATH_CONTEXT).negate());
		final BigDecimal unoDivPrima = BigDecimal.ONE.divide(prima, ConstantsFunciones.MATH_CONTEXT);

		BigDecimal varLxEdad1 = BigDecimal.ZERO;
		BigDecimal unoDivvarLxEdad1 = BigDecimal.ZERO;
		BigDecimal varLxEdad1Entero = BigDecimal.ZERO;
		BigDecimal varLxEdad1Entero1 = BigDecimal.ZERO;
		BigDecimal varLxEdad2Entero = BigDecimal.ZERO;
		BigDecimal varLxEdad2Entero1 = BigDecimal.ZERO;
		BigDecimal varLxEdad2 = BigDecimal.ZERO;

		BigDecimal varLxEdad3Entero = BigDecimal.ZERO;
		BigDecimal varLxEdad3Entero1 = BigDecimal.ZERO;
		BigDecimal varLxEdad3 = BigDecimal.ZERO;	

		int varEdad1Entero = varEdad1.intValue();
		if (varEdad1Entero <= lstValoresTabMort.size()-2){
			varLxEdad1Entero = lstValoresTabMort.get(varEdad1Entero);
			if(varLxEdad1Entero.signum() != 0) {
				varLxEdad1Entero1 = lstValoresTabMort.get(varEdad1Entero + 1);
				varLxEdad1 = Util.interpolaPorEdad(varLxEdad1Entero, varLxEdad1Entero1, varEdad1);
				unoDivvarLxEdad1 = BigDecimal.ONE.divide(varLxEdad1, ConstantsFunciones.MATH_CONTEXT);
			} else {
				return resultadoVfal;
			}
		}else{
			return resultadoVfal;
		}

		int duracionPrimerTraMeses = Util.mult12(duracionPrimerTra);

		//Sumatorio desde t=k hasta t= (w-x)*12 -1
		for (int t = limiteInferior; t <= limiteSuperior; t++) {

			//Calculo operando1

			//t = iteracion

			if (t >= mescare) {
				//Si t >= mescaren  cSubt <-- MIN[(gamma),(1 + (Crm/prima))]
				if (varListaLimCap!= null && !varListaLimCap.isEmpty()){
					varLimCap = varListaLimCap.get(0);
					if (varLimCap.getNmeshasta() < varListaLimCap.get(varListaLimCap.size()-1).getNmeshasta() && t  >= varLimCap.getNmeshasta()){
						for (int i =1; i<varListaLimCap.size();i++){
							if (varListaLimCap.get(i).getNmeshasta() > t){
								varLimCap = varListaLimCap.get(i);
								break;
							}
						}
	
						if (!varRAgravado) {
							varCrm  = varLimCap.getEcaphasta();
						} else{
							varCrm = varLimCap.getEcaphastaAgra();
						}
						
						operando1SinCarencia = BigDecimal.ONE.add(varCrm.multiply(unoDivPrima, ConstantsFunciones.MATH_CONTEXT));
						
					} else {
						operando1SinCarencia = BigDecimal.ZERO;
					}
				} else {
					//Si la lista de capitales no existe o está vacía
					if (varCRMax != null){
						varCrm = varCRMax;
					} else {
						varCrm = BigDecimal.valueOf(999999999);
					}
					
					operando1SinCarencia = BigDecimal.ONE.add(varCrm.multiply(unoDivPrima, ConstantsFunciones.MATH_CONTEXT));
				}
				
				//operando1SinCarencia = BigDecimal.ONE.add(varCrm.multiply(unoDivPrima, ConstantsFunciones.MATH_CONTEXT));
				if(gamma.compareTo(operando1SinCarencia) < 0) {
					operando1SinCarencia = gamma;
				}
				operando1 = operando1SinCarencia;
								
			} else {
				//-	Si  t < mescaren  cSubt <-- 1
				operando1 = BigDecimal.ONE;
			}						

			//Calculo operando 2

			/**
			 * Operando2 --> Probabilidad de que una persona de edad x más k meses, fallezca en el mes comprendido entre el momento (t-k) y el (t-k+1) 
			 * del seguro (considerando meses de operación). Esta probabilidad se calculará con las tablas a utilizar en cada momento para el
			 * 		-	varEdad1 = x + (k/12) 
					-	varEdad2 = x + ((t-k)/12)
					-	varEdad3 = x + ((t-k -1)/12)

			 * 		-	varLxEdad1Entero= valoresTabMort(ENTERO(varEdad1)).wkvalor
					-	varLxEdad1Entero1= valoresTabMort(ENTERO(varEdad1)+1).wkvalor 
					-	varLxEdad1 = varLxEdad1Entero + (ParteDecimal(varEdad1) )* (varLxEdad1Entero1 – varLxEdad1Entero)

			 * 		-	varLxEdad2Entero= valoresTabMort(ENTERO(varEdad2)).wkvalor
					-	varLxEdad2Entero1= valoresTabMort(ENTERO(varEdad2)+1).wkvalor 
					-	varLxEdad2 = varLxEdad2Entero + (ParteDecimal(varEdad2) )* (varLxEdad2Entero1 – varLxEdad2Entero)

			 * 		-	varLxEdad3Entero= valoresTabMort(ENTERO(varEdad3)).wkvalor
					-	varLxEdad3Entero1= valoresTabMort(ENTERO(varEdad3)+1).wkvalor 
					-	varLxEdad3 = varLxEdad2Entero + (ParteDecimal(varEdad3) )* (varLxEdad3Entero1 – varLxEdad3Entero)

			 * 	Entonces:
			 * 		operando2 = operando3 <-- varLxEdad3 - varLxEdad2 / varLxEdad1
			 */


			int resultado = t / ConstantsFunciones.CTE_12;
			int resto = t % ConstantsFunciones.CTE_12;

			int varEdad3Entero = edadActuarial + resultado;		
			if (varEdad3Entero <= lstValoresTabMort.size()-2){			
				if(resto != ConstantsFunciones.CTE_11.intValue() && resto != ConstantsFunciones.CTE_0.intValue() && t > limiteInferior) {
					// Si se sigue en el mismo tramo y varLxEdad2 ya fue calculado en una iteración anterior
					varLxEdad3 = varLxEdad2;
				} else {
					varLxEdad3Entero = lstValoresTabMort.get(varEdad3Entero);
					varLxEdad3Entero1 = lstValoresTabMort.get(varEdad3Entero + 1);						
					varLxEdad3 = varLxEdad3Entero.subtract(ConstantsFunciones.GAMMA[resto].multiply(varLxEdad3Entero.subtract(varLxEdad3Entero1),  ConstantsFunciones.MATH_CONTEXT));						
				}

				resto++;

				int varEdad2Entero = varEdad3Entero;
				if(resto != ConstantsFunciones.CTE_12.intValue()) {
					if (varEdad2Entero <= lstValoresTabMort.size()-2){
						varLxEdad2Entero = lstValoresTabMort.get(varEdad2Entero);
						varLxEdad2Entero1 = lstValoresTabMort.get(varEdad2Entero + 1);
						varLxEdad2 = varLxEdad2Entero.subtract(ConstantsFunciones.GAMMA[resto].multiply(varLxEdad2Entero.subtract(varLxEdad2Entero1),  ConstantsFunciones.MATH_CONTEXT));
					} else {
						varLxEdad2 = BigDecimal.ZERO;							
					}						
				} else {
					varEdad2Entero = varEdad2Entero + 1;
					if (varEdad2Entero <= lstValoresTabMort.size()-2){
						varLxEdad2Entero = lstValoresTabMort.get(varEdad2Entero);
						varLxEdad2Entero1 = lstValoresTabMort.get(varEdad2Entero + 1);
						varLxEdad2 = varLxEdad2Entero;
					} else {
						varLxEdad2 = BigDecimal.ZERO;							
					}						
				}

				//operando2 <-- (varLxEdad3 - varLxEdad2) / varLxEdad1
				operando2 = varLxEdad3.subtract(varLxEdad2).multiply(unoDivvarLxEdad1, ConstantsFunciones.MATH_CONTEXT);									
			} else {
				operando2 = BigDecimal.ZERO;
			}



			//Calculo operando 3

			/**
			 * Operando3 --> Factor de actualización financiera entre los momentos t+1/24 y k del seguro. Se calculará como:
			 * 		SI k < L*12
			 * 			SI  t/12 < L
			 * 				operando3 <--(1 + i1) ^ -(((t - k)*2+1)/24)
			 * 			SI t/12 >= L
			 * 				operando3 <--[(1 + i1) ^ -(L-(k/12))] * [(1 + i2) ^ -((2*t+1)/24)-L]
			 * 		SI k >= L*12
			 * 			operando3 <-- (1 + i2) ^-(((t - k)*2+1)/24)
			 */


			if (mesesCompletos <= duracionPrimerTraMeses) { 
				if (t <= duracionPrimerTraMeses) {
					//t/12 < L
					//elevado <-- (((t - k)*2+1)/24)
					//operando3 <-- base ^ -elevado
					if(calculadoOperando3) {
						operando3 = operando3.multiply(incrInt1, ConstantsFunciones.MATH_CONTEXT);
					} else {
						elevado = BigDecimal.valueOf(((t - mesesCompletos) << 1) + 1).multiply(ConstantsFunciones.CTE_OPER_1ENTRE24, ConstantsFunciones.MATH_CONTEXT);
						operando3 = Util.pow(base1, elevado.negate());		
						calculadoOperando3 = true;
					}
				} else {
					//t/12 >= L
					//elevado1 <-- (L-(k/12))
					if(calculadoExponente1) {
						operando3 = operando3.multiply(incrInt2, ConstantsFunciones.MATH_CONTEXT);
					} else {
						elevado1 = BigDecimal.valueOf(duracionPrimerTraMeses - mesesCompletos).multiply(ConstantsFunciones.CTE_OPER_1ENTRE12, ConstantsFunciones.MATH_CONTEXT);
						calculadoExponente1 = true;
						//elevado2 <-- ((2*t+1)/24)-L
						elevado2 = BigDecimal.valueOf((t << 1) + 1).multiply(ConstantsFunciones.CTE_OPER_1ENTRE24, ConstantsFunciones.MATH_CONTEXT).subtract(BigDecimal.valueOf(duracionPrimerTra));
						//operando3 <-- base1 ^ -eleveado1 * base2 ^ -elevado2
						operando3 = Util.pow(base1, elevado1.negate()).multiply(Util.pow(base2, elevado2.negate()));
					}

				}
			} else {
				//k >= L*12
				//elevado <-- (((t - k)*2+1)/24)
				//operando3 <-- base ^ -elevado
				if(calculadoOperando3) {
					operando3 = operando3.multiply(incrInt2, ConstantsFunciones.MATH_CONTEXT);					
				} else {
					elevado = BigDecimal.valueOf(((t - mesesCompletos) << 1) + 1).multiply(ConstantsFunciones.CTE_OPER_1ENTRE24, ConstantsFunciones.MATH_CONTEXT);
					operando3 = Util.pow(base2, elevado.negate());
					calculadoOperando3 = true;
				}
			}

			resultadoVfal = resultadoVfal.add(operando1.multiply(operando2, ConstantsFunciones.MATH_CONTEXT).multiply(operando3, ConstantsFunciones.MATH_CONTEXT));

		}


		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Fin de la función << vfal >> de la clase FuncionesFallecimiento, con resultado resultadoVfal = {}", resultadoVfal);
		}

		return resultadoVfal;
	}

	/**
	 *	Este término identifica un capital de fallecimiento con reembolso de
	 *	intereses por años transcurridos o anticipados (dependiendo de iant).
	 * 
	 * @param varx Edad x del asegurado
	 * @param varp Desplazamiento de la renta
	 * @param varn Duración del seguro en años
	 * @param ifal Variable de apoyo IFAL
	 * @param vari1 Interés 1 de la base contable tratada
	 * @param iant
	 * @param lstValoresTabMort Valores de la tabla de mortalidad
	 * @return capCri
	 */
	public static BigDecimal capCriFallec(final Integer varx, final Integer varp, final Integer varn, final BigDecimal ifal, 
			final BigDecimal vari1, final BigDecimal iant, final List<BigDecimal> lstValoresTabMort) {
		// Variables locales
		BigDecimal capCri = BigDecimal.ZERO;
		BigDecimal lXMasP = BigDecimal.ONE;
		BigDecimal lXMasJ = BigDecimal.ZERO;
		BigDecimal lXMasJMas1 = BigDecimal.ZERO;
		BigDecimal resulInter = BigDecimal.ZERO;
		// Fin variables locales

		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Inicio Funcion << capCriFallec >> de la clase FuncionesFallecimiento, para la entrada varx = {}, varp = {}, varn = {}, ifal = {}, varn = {}, iant = {}, lstValoresTabMort = {}", varx, varp, varn, ifal, vari1, iant, lstValoresTabMort);
		}

		//Validamos los parametros de entrada
		ValidacionesFuncionesFallecimiento.validarCamposEntradaFuncionCapCriFallecParte1(varx, varp, varn, ifal, vari1);
		ValidacionesFuncionesFallecimiento.validarCamposEntradaFuncionCapCriFallecParte2(iant, lstValoresTabMort);

		// (1+Ifal/100)
		final BigDecimal unoIfalDiv100 = BigDecimal.ONE.add(ifal.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		// (1+I1/100)
		final BigDecimal unoVari1Div100 = BigDecimal.ONE.add(vari1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));

		//L(x+p) = valoresTabMort(x+p).wkvalor
		lXMasP = lstValoresTabMort.get(varx + varp);

		for (int j = varp; j < varn; j++) {
			//Calculamos L(X+J), L(X+J+1) y L(X+P) como sigue
			//L(x+j) = valoresTabMort(x+j).wkvalor
			lXMasJ = lstValoresTabMort.get(varx + j);
			//L(x+j+1) = valoresTabMort(x+j+1).wkvalor
			lXMasJMas1 = lstValoresTabMort.get(varx + j + 1);

			//capCri <-- (1+Ifal/100)^(J+iant)*(1+I1/100)^-(J+0.5-p)*(l(X+J)- l(X+J+1))/l(X+p)

			// (1+Ifal/100)^(J+iant)
			final BigDecimal operador1 = Util.pow(unoIfalDiv100, BigDecimal.valueOf(j).add(iant));
			// (1+I1/100)^-(J+0.5-p)
			final BigDecimal operador2 = Util.pow(unoVari1Div100, BigDecimal.valueOf(j).add(ConstantsFunciones.CTE_OPER_0_PUNTO_5).subtract(BigDecimal.valueOf(varp)).negate());
			// (l(X+J)- l(X+J+1))/l(X+p)
			final BigDecimal operador3 = lXMasJ.subtract(lXMasJMas1).divide(lXMasP, ConstantsFunciones.MATH_CONTEXT);
			resulInter = resulInter.add(operador1.multiply(operador2).multiply(operador3)); 
		}

		capCri = resulInter;

		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Fin Funcion << capCriFallec >> de la clase FuncionesFallecimiento, con resultado capCriFallec = {}", capCri);
		}

		return capCri;
	}

	/**
	 * Este término identifica un capital de fallecimiento con reembolso de 
	 * intereses por años transcurridos o anticipados (dependiendo de iant).
	 * 
	 * @param varx Edad x del asegurado
	 * @param vartc Desplazamiento de la renta
	 * @param varn Duración del seguro en años
	 * @param ifal Variable de apoyo IFAL
	 * @param vari1 Interés 1 de la base contable tratada
	 * @param prp Porcentaje de revalorización de Primas
	 * @param lstValoresTabMort Valores de la tabla de mortalidad
	 * @param pas Variable de Apoyo PAS
	 * @param fut Variable de Apoyo FUT
	 * @return geoCri
	 */
	public static BigDecimal geoCriFallec(final Integer varx, final Integer varp, final Integer varn, final BigDecimal ifal, final BigDecimal vari1,
			final BigDecimal prp, final List<BigDecimal> lstValoresTabMort, final BigDecimal pas, final BigDecimal fut) {
		// Variables locales
		BigDecimal geoCri = BigDecimal.ZERO;
		BigDecimal lXMasJ = BigDecimal.ZERO;
		BigDecimal lXMasP = BigDecimal.ZERO;
		BigDecimal lXMasJMas1 = BigDecimal.ZERO;
		BigDecimal operando1 = BigDecimal.ZERO;
		BigDecimal operando2 = BigDecimal.ZERO;
		BigDecimal exponente = BigDecimal.ZERO;
		// Fin variables locales

		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Inicio Funcion << geoCriFallec >> de la clase FuncionesFallecimiento, para la entrada varx = {}, varp = {}, varn = {}, ifal = {}, vari1 = {}, prp ={}, lstValoresTabMort = {}, pas = {} fut = {} ",
					varx, varp, varn, ifal, vari1, prp, lstValoresTabMort, pas, fut);
		}

		//Validamos los parametros de entrada
		ValidacionesFuncionesFallecimiento.validarCamposEntradaFuncionGeoAriCriFallecParte1(varx, varp, varn, ifal, vari1);
		ValidacionesFuncionesFallecimiento.validarCamposEntradaFuncionGeoAriCriFallecParte2(prp, lstValoresTabMort, pas, fut);

		// (1+Ifal/100)
		final BigDecimal unoIfalDiv100 = BigDecimal.ONE.add(ifal.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		// (1+PRP/100)
		final BigDecimal unoPrpDiv100 = BigDecimal.ONE.add(prp.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		// (1+I1/100)
		final BigDecimal unoVari1Div100 = BigDecimal.ONE.add(vari1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));

		final BigDecimal prpDivIfal = unoPrpDiv100.divide(unoIfalDiv100, ConstantsFunciones.MATH_CONTEXT);

		//L(x+p) = valoresTabMort(x+p).wkvalor
		lXMasP = lstValoresTabMort.get(varx + varp);

		for (int j = varp; j < varn; j++) {
			BigDecimal valorIteracionPAS = Util.pow(unoIfalDiv100, j);
			BigDecimal sumatorioPAS = valorIteracionPAS;
			BigDecimal valorIteracionFUT = Util.pow(unoPrpDiv100, j + 1);
			BigDecimal sumatorioFUT = valorIteracionFUT;

			//Calculamos L(X+J), L(X+J+1) 
			//L(x+j) = valoresTabMort(x+j).wkvalor
			lXMasJ = lstValoresTabMort.get(varx + j);
			//L(x+j+1) = valoresTabMort(x+j+1).wkvalor
			lXMasJMas1 = lstValoresTabMort.get(varx + j + 1);


			for (int h = 1; h <= j; h++) {
				//sumatorioPAS <-- (1+Ifal/100)^(J-H)*(1+PRP/100)^H
				valorIteracionPAS = valorIteracionPAS.multiply(prpDivIfal, ConstantsFunciones.MATH_CONTEXT);
				sumatorioPAS = sumatorioPAS.add(valorIteracionPAS);
			}

			for (int h = j + 2; h < varn; h++) {
				//sumatorioFUT <-- (1+PRP/100)^ H
				valorIteracionFUT = valorIteracionFUT.multiply(unoPrpDiv100, ConstantsFunciones.MATH_CONTEXT);
				sumatorioFUT = sumatorioFUT.add(valorIteracionFUT);
			}

			//operando1 <-- pas * sumatorioPAS + fut * sumatorioFUT
			operando1 = pas.multiply(sumatorioPAS).add(fut.multiply(sumatorioFUT));

			//exponente <-- -(J+0.5-p)
			exponente =  BigDecimal.valueOf(j).add(ConstantsFunciones.CTE_OPER_0_PUNTO_5).subtract(BigDecimal.valueOf(varp)).negate();

			//operando2 <--(1+I1/100)^exponente/l(X+p)  *  ((l(X+J)- l(X+J+1))/(l(X+p))
			operando2 = Util.pow(unoVari1Div100, exponente).multiply(lXMasJ.subtract(lXMasJMas1).divide(lXMasP, ConstantsFunciones.MATH_CONTEXT));

			//geoCri <-- geoCri + (operando1 * operando2)
			geoCri = geoCri.add(operando1.multiply(operando2));
		}

		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Fin Funcion << geoCriFallec >> de la clase FuncionesFallecimiento, con resultado geoCriFallec = {} ", geoCri);
		}

		return geoCri;
	}

	/**
	 * Función que calcula este término de la formulación de la corriente de Provisión Matemática por fórmula cerrada
	 * 
	 * @param fecIni Fecha de inicio de cobro de la renta
	 * @param fecAntRenova Fecha de anterior renovación
	 * @param varm Número de años en que aplicamos un primer interés técnico desde fecha suscripción
	 * @param varn Duración real del ajuste o suscripción
	 * @param nirp Número de años completos que quedan pendientes desde la siguiente renovación a la fecha de cálculo hasta la fecha de inicio de cobro de rentas
	 * @param lstValoresTabMort Valores de la tabla de mortalidad
	 * @param varzc Edad del asegurado a Fecha de cálculo
	 * @param ren Período de tiempo transcurrido desde la fecha de alta del ajuste hasta la fecha de renovación siguiente al momento de cálculo
	 * @param vvida Actualización financiera a fcal desde la fecha de pago de cada renta j.
	 * @param beta1 Variable Beta1
	 * @param beta2 Variable Beta2
	 * @param i1Bti Primer Interés técnico según Bases Técnicas Iniciales
	 * @param i2Bti Segundo Interés técnico según Bases Técnicas Iniciales
	 * @param varI1 Primer Interés técnico 
	 * @param varI2 Segundo Interés técnico 
	 * @return cfallec
	 */
	public static BigDecimal cfallec(final Timestamp fecIni, final Timestamp fecProxRenova, final BigDecimal varm, final BigDecimal varn, final Integer nirp, final List<BigDecimal> lstValoresTabMort, final BigDecimal varzc,
			final BigDecimal ren, final BigDecimal xren, final BigDecimal vvida, final BigDecimal beta1, final BigDecimal beta2, final BigDecimal i1Bti, final BigDecimal i2Bti, final BigDecimal varI1, final BigDecimal varI2, final BigDecimal varI1PorcentajeMasUno, final BigDecimal varI2PorcentajeMasUno) {
		//Variables locales
		BigDecimal cfallec = BigDecimal.ZERO;
		BigDecimal varLzc = BigDecimal.ONE;
		BigDecimal varActuaRen = BigDecimal.ZERO;
		BigDecimal varCPasada = BigDecimal.ZERO;
		BigDecimal varLxRenJ = BigDecimal.ZERO;
		BigDecimal varLxRenJ1 = BigDecimal.ZERO;
		BigDecimal varRFlexj = BigDecimal.ZERO;
		//Fin variables locales

		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Inicio función << cfallec >> de la clase  FuncionesFallecimiento con la entrada fecIni = {}, fecProxRenova = {}, varm = {}, varn = {}, lstValoresTabMort = {}, varzc = {}, ren = {}, vvida = {}, beta1 = {}, beta2 = {}, i1Bti = {}, i2Bti = {}, varI1 = {} y varI2 = {}", fecIni, fecProxRenova, varm, varn, nirp, lstValoresTabMort, varzc, xren, vvida, beta1, beta2, i1Bti, i2Bti, varI1, varI2);
		}

		//Validamos los campos de entrada de la función
		ValidacionesFuncionesFallecimiento.validarParamEntradaFuncionCFallecParte1(fecIni, fecProxRenova, varm, varn, nirp, lstValoresTabMort, varzc);
		ValidacionesFuncionesFallecimiento.validarParamEntradaFuncionCFallecParte2(xren, vvida, beta1, beta2, i1Bti, i2Bti, varI1, varI2);

		/**
		 * 	( Si NIRP <= 0 y además (fecIni=< fecAntRenova) ) o Si NIRP – 1 < 0
				cfallec = 0
			En cualquier otro caso se tendrá: 
				varLzc = valoresTabMort(ZC).wkvalor --> Dato de valoresTabMort para wkedad = ZC
				varActuaRen  = VVida/(varLzc )
				varCPasada = SUMATORIO(j=0)Hasta(NIRP-1)(varLxRenJ-varLxRenJ1) *VarRFlexJ
			Donde cada término del sumatorio se obtendrá como:
				varLxRenJ = valoresTabMort(RenJ).wkvalor --> Dato de valoresTabMort para wkedad = REN + j
				varLxRenJ1= valoresTabMort(RenJ1).wkvalor --> Dato de valoresTabMort para wkedad = REN + j + 1
				varRFlexJ = RFLEX (I1,I2,i1Bti,i2Bti, beta1, beta2,M,N,REN,j)

		 */

		// Si NIRP <= 0 y además (fecIni <= fecAntRenova)
		if ((nirp.intValue() <= 0  && !fecIni.after(fecProxRenova)) || nirp.intValue() - 1 < 0) {
			cfallec = BigDecimal.ZERO;
		} else {
			BigDecimal varLzcEntero = BigDecimal.ZERO;
			BigDecimal varLzcEntero1;

			final int zc = varzc.intValue();
			if(zc > lstValoresTabMort.size()-2){
				varLzc = BigDecimal.ZERO;
			}else{
				varLzcEntero = lstValoresTabMort.get(zc);
				varLzcEntero1 = lstValoresTabMort.get(zc+ 1);
				varLzc = Util.interpolaPorEdad(varLzcEntero, varLzcEntero1, varzc);
			}
			if (!BigDecimal.ZERO.equals(varLzc)) {
				varActuaRen = vvida.divide(varLzc, ConstantsFunciones.MATH_CONTEXT);
			}

			for (int j = 0; j <= nirp.intValue() - 1; j++) {

				BigDecimal bdJ = BigDecimal.valueOf(j);
				BigDecimal xrenMasJ = xren.add(bdJ);
				BigDecimal xrenMasJMasUno = xrenMasJ.add(BigDecimal.ONE);

				final int varRenMasJ = xrenMasJ.intValue();
				if (varRenMasJ > lstValoresTabMort.size()-2){
					varLxRenJ = BigDecimal.ZERO;
					varLxRenJ1 = BigDecimal.ZERO;
				}else{
					BigDecimal varLxRenJEntero = lstValoresTabMort.get(varRenMasJ);
					BigDecimal varLxRenJEntero1 = lstValoresTabMort.get(varRenMasJ + 1);
					varLxRenJ = Util.interpolaPorEdad(varLxRenJEntero, varLxRenJEntero1, xrenMasJ);

					BigDecimal varLxRenJ1Entero1 = lstValoresTabMort.get(xrenMasJ.add(ConstantsFunciones.CTE_OPER_2).intValue());
					varLxRenJ1 = Util.interpolaPorEdad(varLxRenJEntero1, varLxRenJ1Entero1, xrenMasJMasUno);
				}

				varRFlexj = FuncionesActualizacionFinanciera.rflex(varI1, varI2, i1Bti, i2Bti, beta1, beta2, varm, varn, ren, bdJ, varI1PorcentajeMasUno, varI2PorcentajeMasUno);
				varCPasada = varCPasada.add(varLxRenJ.subtract(varLxRenJ1).multiply(varRFlexj));
			}
			//Finalmente se obtendrá CFALLEC como:
			cfallec = varActuaRen.multiply(varCPasada);
		}

		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Fin función << cfallec >> de la clase  FuncionesFallecimiento con resultado cfallec = {}", cfallec);
		}

		return cfallec;
	}

	/**
	 * Función que calcula este término de la formulación de la corriente de Provisión Matemática por fórmula cerrada.
	 * 
	 * @param fcalc Fecha de cálculo
	 * @param fecIni Fecha de inicio de cobro de la renta
	 * @param fecAntRenova Fecha de anterior renovación
	 * @param varm Número de años en que aplicamos un primer interés técnico desde fecha suscripción
	 * @param varn Duración real del ajuste o suscripción
	 * @param nirp Número de años completos que quedan pendientes desde la siguiente renovación a la fecha de cálculo hasta la fecha de inicio de cobro de rentas
	 * @param nirmv Fracción de año comprendida entre la fecha de renovación inmediata anterior a la fecha de inicio de cobro de renta y dicha fecha de inicio cobro renta
	 * @param lstValoresTabMort Valores de la tabla de mortalidad
	 * @param varx Edad del asegurado a Fecha de Efecto
	 * @param varzc Edad del asegurado a Fecha de cálculo
	 * @param xren Edad en la renovación siguiente a la fecha de cálculo fcal
	 * @param ren  Período de tiempo transcurrido desde la fecha de alta del ajuste hasta la fecha de renovación siguiente al momento de cálculo
	 * @param vmort Actualización financiera a fcal a la mitad del período que va hasta la próxima renovación
	 * @param vvida Actualización financiera a fcal desde la fecha de pago de cada renta j.
	 * @param difercol Diferimiento en cobrar la renta
	 * @param modBeta Variable de Apoyo MODBETA
	 * @param beta1 Variable Beta1
	 * @param beta2 Variable Beta2
	 * @param i1Bti Primer Interés técnico según Bases Técnicas Iniciales
	 * @param i2Bti Segundo Interés técnico según Bases Técnicas Iniciales
	 * @param varI1 Primer Interés técnico 
	 * @param varI2 Segundo Interés técnico 
	 * @return cfallecvt
	 */
	public static BigDecimal cfallecvt(final Timestamp fcalc, final Timestamp fecIni, final Timestamp fecProxRenova, final BigDecimal varm, final BigDecimal varn, final Integer nirp, 
			final BigDecimal nirmv, final List<BigDecimal> lstValoresTabMort, final BigDecimal varx, final BigDecimal varzc, final BigDecimal xren, final BigDecimal ren, final BigDecimal vmort, 
			final BigDecimal vvida, final BigDecimal difercol, final String modBeta, final BigDecimal beta1, final BigDecimal beta2, final BigDecimal i1Bti, final BigDecimal i2Bti,
			final BigDecimal varI1, final BigDecimal varI2, final BigDecimal varI1PorcentajeMasUno) {
		//Variables locales
		BigDecimal cfallecvt = BigDecimal.ZERO;
		BigDecimal varLzc = BigDecimal.ZERO;
		BigDecimal varLxDifercol = BigDecimal.ZERO;
		BigDecimal varLxRenNirp = BigDecimal.ZERO;
		BigDecimal varActFall = BigDecimal.ZERO;
		BigDecimal varActuaRen = BigDecimal.ZERO;
		//Fin variables locales

		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Inicio función << cfallecvt >> de la clase FuncionesFallecimiento con la entrada fecIni = {}, fecProxRenova = {}, varm = {}, varn = {}, nirp = {}, nirmv = {}, lstValoresTabMort = {}, varx = {}, varzc = {}, xren = {}, ren = {}, vmort = {}, vvida = {}, difercol = {}, modBeta = {}, beta1 = {}, beta2 = {}, i1Bti = {} ,i2Bti = {}, varI1 = {} y varI2 = {}",
					fecIni, fecProxRenova, varm, varn, nirp, nirmv, lstValoresTabMort, varx, varzc, xren, ren, vmort, vvida, difercol, modBeta, beta1, beta2, i1Bti, i2Bti, varI1, varI2);
		}

		//Validamos los parametros de entrada
		ValidacionesFuncionesFallecimiento.validarParamEntradaFuncionCfallecVtParte1(fecIni, fecProxRenova, varm, varn, nirp, nirmv, lstValoresTabMort);
		ValidacionesFuncionesFallecimiento.validarParamEntradaFuncionCfallecVtParte2(varx, varzc, xren, ren, vmort, vvida, difercol);
		ValidacionesFuncionesFallecimiento.validarParamEntradaFuncionCfallecVtParte3(modBeta, beta1, beta2, i1Bti, i2Bti, varI1, varI2);

		/**
		 * 	Si modBeta = SR 
				cfallecvt = 0
			Si NIRP <= 0 y además (fecIni=< fecAntRenova)
					varLzc = valoresTabMort(ZC).wkvalor --> Dato de valoresTabMort para wkedad = ZC
					varLxDifercol = valoresTabMort(x+ Difercol).wkvalor --> Dato de valoresTabMort para wkedad = x + difercol
				cfallecvt = ((varLzc -varLxDifercol   ))/(varLzc )*VMort

			En cualquier otro caso se tendrá: 
					varLzc = valoresTabMort(ZC).wkvalor --> Dato de valoresTabMort para wkedad = ZC
					varLxRenNirp= valoresTabMort(xREN+NIRP).wkvalor --> Dato de valoresTabMort para wkedad = xren + NIRP
					varLxdifercol= valoresTabMort(x+ difercol).wkvalor --> Dato de valoresTabMort para wkedad = x + difercol
					varActFall = ACTFALL (I1,I2,i1Bti,i2Bti, beta1, beta2,M,N,REN,difercol,NIRP,NIRmv)
					varActuaRen  = VVida/(varLzc )
				Finalmente se obtendrá CFALLECVT como: 
				cfallec = varActuaRen * varActFall * (varLxRenNirp – varLxdifercol)

		 */
		final BigDecimal varXMasDifercol = varx.add(difercol);

		if (ConstantsFunciones.CTE_SR.equals(modBeta) || (nirp.intValue() <= 0 && fecIni.before(fcalc))) {
			cfallecvt = BigDecimal.ZERO;
		} else if (nirp.intValue() <= 0 && !(fecIni.after(fecProxRenova))) {

			final int zc = varzc.intValue();
			if (zc > lstValoresTabMort.size()-2){
				cfallecvt = BigDecimal.ZERO;
				return cfallecvt;
			}else{
				BigDecimal varLzcEntero = lstValoresTabMort.get(zc);
				BigDecimal varLzcEntero1 = lstValoresTabMort.get(zc+ 1);
				varLzc = Util.interpolaPorEdad(varLzcEntero, varLzcEntero1, varzc);
			}

			if (varLzc.signum()==0){
				cfallecvt = BigDecimal.ZERO;
				return cfallecvt;
			}

			final int xMasDifercol = varXMasDifercol.intValue();
			if (xMasDifercol > lstValoresTabMort.size()-2){
				varLxDifercol = BigDecimal.ZERO;
			}else{
				BigDecimal varLxDifercolEntero = lstValoresTabMort.get(xMasDifercol);
				BigDecimal varLxDifercolEntero1 = lstValoresTabMort.get(xMasDifercol + 1);
				
				varLxDifercol = Util.interpolaPorEdad(varLxDifercolEntero, varLxDifercolEntero1, varXMasDifercol);
			}
			cfallecvt = varLzc.subtract(varLxDifercol).divide(varLzc, ConstantsFunciones.MATH_CONTEXT).multiply(vmort);
		} else {


			BigDecimal varLzcEntero = lstValoresTabMort.get(varzc.intValue());

			//Si varLzcEntero <> 0
			if (!BigDecimal.ZERO.equals(varLzcEntero)) {

				final BigDecimal xRenMasNirp = xren.add(BigDecimal.valueOf(nirp));				

				BigDecimal varLzcEntero1 = lstValoresTabMort.get(varzc.intValue() + 1);
				varLzc = Util.interpolaPorEdad(varLzcEntero, varLzcEntero1, varzc);


				final int varXRenMasNrip = xRenMasNirp.intValue();
				if (varXRenMasNrip > lstValoresTabMort.size()-2){
					varLxRenNirp = BigDecimal.ZERO;
				}else{
					BigDecimal varLxRenNirpEntero = lstValoresTabMort.get(varXRenMasNrip);
					BigDecimal varLxRenNirpEntero1 = lstValoresTabMort.get(varXRenMasNrip + 1);
					//varLxRenNirp = varLxRenNirpEntero.add(xRenMasNirp.remainder(BigDecimal.ONE).multiply(varLxRenNirpEntero1.subtract(varLxRenNirpEntero)));
					varLxRenNirp = Util.interpolaPorEdad(varLxRenNirpEntero, varLxRenNirpEntero1, xRenMasNirp);
				}

				final int xMasDifercol = varXMasDifercol.intValue();
				if (xMasDifercol > lstValoresTabMort.size()-2){
					varLxDifercol = BigDecimal.ZERO;
				}else{
					BigDecimal varLxDifercolEntero = lstValoresTabMort.get(xMasDifercol);
					BigDecimal varLxDifercolEntero1 = lstValoresTabMort.get(xMasDifercol + 1);
					varLxDifercol = varLxDifercolEntero.add(varXMasDifercol.remainder(BigDecimal.ONE).multiply(varLxDifercolEntero1.subtract(varLxDifercolEntero)));
				}
				varActFall = FuncionesActualizacionFinanciera.actfall(varI1, varI2, i1Bti, i2Bti, beta1, beta2, varm, ren, difercol, nirp, nirmv, varI1PorcentajeMasUno);
				varActuaRen = vvida.divide(varLzc, ConstantsFunciones.MATH_CONTEXT);

				cfallecvt = varActuaRen.multiply(varActFall).multiply(varLxRenNirp.subtract(varLxDifercol));
			}
		}


		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Fin función << cfallecvt >> de la clase FuncionesFallecimiento con resultado cfallecvt = {}", cfallecvt);
		}

		return cfallecvt;
	}

	/**
	 * Función que calcula el término CFALLECPAG de la formulación de la corriente de Provisión Matemática por fórmula cerrada.
	 * @param m Número de años en que aplicamos un primer interés técnico desde fecha suscripción.
	 * @param n Duración real del ajuste o suscripción.
	 * @param nDap Número de años completos que quedan pendientes desde la siguiente renovación a la fecha de cálculo hasta el vencimiento.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param zc Edad del asegurado a Fecha de cálculo.
	 * @param xRen Edad del asegurado a Fecha de Efecto.
	 * @param ren Período de tiempo transcurrido desde la fecha de alta del ajuste hasta la fecha de renovación siguiente al momento de cálculo.
	 * @param vVida Actualización financiera a fcal desde la fecha de pago de cada renta j.
	 * @param beta1 Variable Beta1.
	 * @param beta2 Variable Beta2.
	 * @param i1Bti Primer Interés técnico según Bases Técnicas Iniciales.
	 * @param i2Bti Segundo Interés técnico según Bases Técnicas Iniciales.
	 * @param i1 Primer Interés técnico.
	 * @param i2 Segundo Interés técnico.
	 * @param vari1PorcentajeMasUno
	 * @param vari2PorcentajeMasUno
	 * 
	 * @return cfallecpag
	 */
	public static BigDecimal cfallecpag(final BigDecimal m, final BigDecimal n, final Integer nDap, final List<BigDecimal> valoresTabMort, final BigDecimal zc, 
			final BigDecimal xRen, final BigDecimal ren, final BigDecimal vVida, final BigDecimal beta1, final BigDecimal beta2, final BigDecimal i1Bti, 
			final BigDecimal i2Bti, final BigDecimal i1, final BigDecimal i2, final BigDecimal vari1PorcentajeMasUno, final BigDecimal vari2PorcentajeMasUno){
		//Varriables locales
		BigDecimal cfallecpag = BigDecimal.ZERO;
		BigDecimal varActuaRen = BigDecimal.ZERO;
		BigDecimal varCPasadaCap = BigDecimal.ZERO;
		BigDecimal varLzc;
		BigDecimal varLxRenJ;
		BigDecimal varLxRenJ1;
		BigDecimal varRFlexJ;
		//Fin de variables locales
		
		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Inicio Funcion <<cfallecpag>> de la clase FuncionesFallecimiento, para la entrada m = {}, n = {},"
					+ " nDap = {}, valoresTabMort = {}, zc = {}, xRen = {}, ren = {}, vVida = {}, beta1 = {}, beta2 = {}, i1Bti = {}, i2Bti = {}"
					+ " i1 = {}, i2 = {}",
					m, n, nDap, valoresTabMort, zc, xRen, ren, vVida, beta1, beta2, i1Bti, i2Bti, i1, i2);
		}
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesFallecimiento.validarParamEntradaFuncionCfallecpag(m, n, nDap, valoresTabMort, zc, xRen, ren, vVida, beta1, beta2, i1Bti, i2Bti, i1, i2);
		
		//Si nDap es menor o igual a 0, cfallecpag será 0.
		if (nDap > 0){

			varLzc = Util.getVarLx(zc, valoresTabMort);

			if (varLzc.intValue() != 0){
				varActuaRen = vVida.divide(varLzc, ConstantsFunciones.MATH_CONTEXT);


				// Se calcula varCpasadaCap como el Sumatorio [desde 0 hasta nDap-1] (varLxRenJ-varLxRenJ1) *VarRFlexJ
				for (int j = 0; j<nDap; j++){
					varLxRenJ = Util.getVarLx(xRen.add(BigDecimal.valueOf(j)), valoresTabMort);
					varLxRenJ1 = Util.getVarLx(xRen.add(BigDecimal.valueOf(j+1)), valoresTabMort);

					varRFlexJ = FuncionesActualizacionFinanciera.rflex(i1, i2, i1Bti, i2Bti, beta1, beta2, m, n, ren, BigDecimal.valueOf(j), vari1PorcentajeMasUno, vari2PorcentajeMasUno);

					varCPasadaCap = varCPasadaCap.add((varLxRenJ.subtract(varLxRenJ1)).multiply(varRFlexJ));
				}

				// Se obtiene cfallecpag como cfallecpag = varActuaRen * varCPasadaCap
				cfallecpag = varActuaRen.multiply(varCPasadaCap);
				
			} //Si varActuaRen=0 -> cfallecpag=0
		}
		
		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Fin Funcion << cfallecpag >> de la clase FuncionesFallecimiento, con resultado cfallecpag = {}", cfallecpag);
		}
		
		return cfallecpag;
	}

	/**
	 * Función que calcula este término de la formulación de la corriente de Provisión Matemática por fórmula cerrada.
	 * @param proyUmic Estructura detalleCorrientes de la umic.
	 * @param bloqueCorriente Indica el periodo de proyección J que se está calculando de entre todos los periodos de proyección de la umic (proyUmic).
	 * @param umic Contiene los datos de la Umic que se está procesando.
	 * @param btcUmic Contiene el detalle de la base técnica de cálculo para la umic.
	 * @param x363 Edad actuarial a la fecha de inicio de pago de renta del titular.
	 * @param y363 Edad actuarial a la fecha de inicio de pago de renta del cónyuge.
	 * @param lxIni Probabilidad supervivencia a  Edad X363ini.
	 * @param lyIni Probabilidad supervivencia a  Edad Y363ini.
	 * @param m Duración en que aplica el primer interés técnico.
	 * @param lstValoresTabMort Valores de la tabla de mortalidad.
	 * @param iteracion
	 * @param varCriterFec
	 * @param mapVariables
	 * @param clave
	 * @return fall Término correspondiente al pago de las Rentas futuras de la provisión matemática.
	 */
	@SuppressWarnings("unchecked")
	public static BigDecimal fall(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Umic umic,
			final DetalleBaseTecnica btcUmic, final Integer x363, final Integer y363, final BigDecimal lxIni, final BigDecimal lyIni,
			final BigDecimal m, final List<BigDecimal> lstValoresTabMort, final Integer iteracion, final String varCriterFec, final Map<String, Object> mapVariables, final String clave){
		//Variables locales
		BigDecimal fall = BigDecimal.ZERO;
		BigDecimal varTCyFall = BigDecimal.ZERO;
		BigDecimal varVVida;
		BigDecimal varProb = BigDecimal.ZERO;
		BigDecimal rentaFallP = BigDecimal.ZERO;
		BigDecimal lxFcalc;
		BigDecimal lyFcalc = null;
		BigDecimal lxF363;
		BigDecimal lyF363 = null;
		BigDecimal cuantiaFall;
		Timestamp fDevFall;
		BigDecimal actJFall;
		BigDecimal actJFallVida;
		BigDecimal actJFcalc;
		BigDecimal actJF363;
		//Fin variables locales

		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Inicio Funcion <<fall>> de la clase FuncionesFallecimiento, para la entrada proyUmic = {}, bloqueCorriente = {},"
					+ " btcUmic = {}, x363 = {}, y363 = {}, LxIni = {}, LyIni = {},  lstValoresTabMort = {}",
					proyUmic, bloqueCorriente, umic, btcUmic, x363, y363, lxIni, lyIni,  lstValoresTabMort);
		}

		//Validaciones parámetros obligatorios de entrada
		ValidacionesFuncionesFallecimiento.validarParamEntradaFuncionFall(proyUmic, bloqueCorriente, umic, btcUmic, x363, lxIni, m, lstValoresTabMort, varCriterFec);


		if (lxIni.equals(BigDecimal.ZERO)){ 
			return fall;
		}

		//Variables auxiliares para el cálculo.
		BigDecimal varI1 = btcUmic.getItcalc().get(0);
		BigDecimal varI2 = btcUmic.getItcalc().get(1);
		BigDecimal numi1PorcentajeMasUno = BigDecimal.ONE.add(varI1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		BigDecimal numi2PorcentajeMasUno = BigDecimal.ONE.add(varI2.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		Timestamp fecInisus = umic.getFechas().getFecinisus();
		Timestamp fecIniRenta = umic.getRentas().getFecIni();
		BigDecimal preversion = umic.getRentas().getPreversion();
		

		//Se calculan los valores varLxj y varLyj para el caso de que varEdadXj = x363bd y varEdadYj = y363bd, 
		//(situación que se produce cuando varActj363 = 0) por no ser necesario su cálculo en cada término del sumatorio
		BigDecimal x363bd = BigDecimal.valueOf(x363);
		BigDecimal y363bd = null;
		BigDecimal varLxj0 = Util.getVarLx(x363bd, lstValoresTabMort); 
		BigDecimal varLyj0 = BigDecimal.ZERO;
		if (y363!= null){
			y363bd = BigDecimal.valueOf(y363);
			varLyj0 = Util.getVarLx(y363bd, lstValoresTabMort);
		}

		//Se calculan los datos necesarios para cada periodo y se almacenan en una estructura, de esta forma solo 
		//se calculan una vez
		List<PeriodosFall> periodosFall = (List<PeriodosFall>) mapVariables.get(clave);
		if (periodosFall == null){
			periodosFall = new ArrayList<PeriodosFall>();
			for (int i=iteracion-1;i<proyUmic.size();i++){
				PeriodosFall pf = new PeriodosFall();
				Timestamp fecVida = proyUmic.get(i).getBloqueVida().getFechaDevengo();
				Timestamp fecDesde = proyUmic.get(i).getFechaDesde();
				if (fecVida==null){
					fecVida=umic.getRentas().getFecIni();
				}
				cuantiaFall = proyUmic.get(i).getImpPago();

				fDevFall = calcFDevFall(umic.getFechas().getFecdesderenova(), fecIniRenta, fecVida, fecDesde, proyUmic, i);
				actJFall = FuncionesAuxiliares.nAnnos(fecInisus, fDevFall, varCriterFec);
				actJFallVida = FuncionesAuxiliares.nAnnos(fecInisus, fecVida, varCriterFec);
				actJFcalc = FuncionesAuxiliares.nAnnos(fecIniRenta, fDevFall, varCriterFec);
				actJF363 = FuncionesAuxiliares.nAnnos(fecIniRenta, fecVida, varCriterFec);
				if (actJFcalc.signum()!=0){
					BigDecimal edadXJFcalc = x363bd.add(actJFcalc);
					lxFcalc = Util.getVarLx(edadXJFcalc, lstValoresTabMort);
				} else {
					//Si actJFcalc = 0, se recuperan los valores varLxj y varLyj calculados fuera del bucle por ser constantes
					lxFcalc = varLxj0;
					lyFcalc = varLyj0;
				}
				
				if (actJF363.signum()!=0){
					BigDecimal edadXJF363 = x363bd.add(actJF363);
					lxF363 = Util.getVarLx(edadXJF363, lstValoresTabMort);
				} else {
					//Si actJF363 = 0, se recuperan los valores varLxj y varLyj calculados fuera del bucle por ser constantes
					lxF363 = varLxj0;
					lyF363 = varLyj0;
				}
				
				if (y363 != null){
					if (lyFcalc == null){
						BigDecimal edadYJFcalc = y363bd.add(actJFcalc);
						lyFcalc = Util.getVarLx(edadYJFcalc, lstValoresTabMort);
					}
					if (lyF363== null){
						BigDecimal edadYJF363 = y363bd.add(actJF363);
						lyF363 = Util.getVarLx(edadYJF363, lstValoresTabMort);
					}
					pf.setLyFcalc(lyFcalc);
					pf.setLyF363(lyF363);
				}


				pf.setFecDesde(fecDesde);
				pf.setFecDevengo(proyUmic.get(i).getBloqueVida().getFechaDevengo());
				pf.setCuantiaFall(cuantiaFall);
				pf.setfDevFall(fDevFall);
				pf.setActJFall(actJFall);
				pf.setActJFallVida(actJFallVida);
				pf.setLxFcalc(lxFcalc);
				pf.setLxF363(lxF363);

				periodosFall.add(pf);
			}
			mapVariables.put(clave, periodosFall);
		}

		varTCyFall = FuncionesAuxiliares.nAnnos(fecInisus, periodosFall.get(0).getFecDesde(), varCriterFec);

		boolean primerPago = true;
		for (int j = 0; j<periodosFall.size();j++){
			if(periodosFall.get(j).getFecDevengo()==null || periodosFall.get(j).getfDevFall().before(fecIniRenta)){
				varProb = BigDecimal.ZERO;
				varVVida = BigDecimal.ZERO;
			} else {

				if (primerPago){
					varProb = (lxIni.subtract(periodosFall.get(j).getLxFcalc())).divide(lxIni, ConstantsFunciones.MATH_CONTEXT);
					primerPago=false;
				} else {
					varProb = (periodosFall.get(j-1).getLxFcalc().subtract(periodosFall.get(j).getLxFcalc())).divide(lxIni, ConstantsFunciones.MATH_CONTEXT);
				}

				varVVida = FuncionesActualizacionFinanciera.vVida(periodosFall.get(j).getActJFall(), varI1, varI2, m, varTCyFall, numi1PorcentajeMasUno, numi2PorcentajeMasUno);

				rentaFallP = rentaFall(j, periodosFall, varI1, varI2, numi1PorcentajeMasUno, numi2PorcentajeMasUno, m, preversion);
				
				//Se realiza el cálculo de fall = {Sumatorio desde periodo actual de proyeccion hasta proyUmic.size()-1} 
				//		varRentaFall *VVida(actjfall)* varProb(actjfall)
				fall = fall.add(rentaFallP.multiply(varProb).multiply(varVVida));
			}
			
		}
		periodosFall.remove(0);
		mapVariables.put(clave, periodosFall);


		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Fin Funcion << fall >> de la clase FuncionesFallecimiento, con resultado fall = {}", fall);
		}
		return fall;

	}

	/**
	 * Función que obtiene la fecha de devengo para el cálculo del término fall
	 * @param fecDesdeRenova
	 * @param fecIniRenta
	 * @param fecDevengo
	 * @param fecDesde
	 * @param proyUmic
	 * @param iteracion
	 * @return fDevFall
	 */
	public static Timestamp calcFDevFall(final Timestamp fecDesdeRenova, final Timestamp fecIniRenta, final Timestamp fecDevengo, 
			final Timestamp fecDesde, final List<DetalleCorriente> proyUmic, final Integer iteracion){
		//Variables locales
		Timestamp fDevFall = null;
		Timestamp fecDevengoAux;
		int varDia;
		Fecha devFall;
		//Fin variables locales
		
		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Inicio Funcion <<calcFDevFall>> de la clase FuncionesFallecimiento, para la entrada fecDesdeRenova = {}, fecIniRenta = {},"
					+ " fecDevengo = {}, fecDesde = {}, proyUmic = {}, iteracion = {}",
					fecDesdeRenova, fecIniRenta, fecDevengo, fecDesde, proyUmic, iteracion);
		}

		//Se obtiene el día del mes al que se llevará la fecha de devengo
		varDia = UtilFechas.getDia(fecDesdeRenova);

		if (fecDevengo != null){
			//Se lleva la fecha de devengo al día del mes obtenido
			devFall = UtilFechas.getFecha(fecDevengo);
			devFall.setDia(varDia);
			fDevFall = devFall.toTimestamp();

			//Si la fecha de inicio de la renta es posterior a la fDevFall calculada, se obtiene fDevFall como fecIniRenta 
			//llevada al día del mes obtenido
			if (fecIniRenta.after(fDevFall)){
				devFall = UtilFechas.getFecha(fecIniRenta);
				devFall.setDia(varDia);
				fDevFall = devFall.toTimestamp();
			}

		} else {
			//Si la fecha de devengo del primer periodo es nula y la fecha desde no es posterior a la de inicio de la renta, 
			//se obtiene fDevFall como fecIniRenta llevada al día del mes obtenido
			if (!fecDesde.after(fecIniRenta)){
				devFall = UtilFechas.getFecha(fecIniRenta);
				devFall.setDia(varDia);
				fDevFall = devFall.toTimestamp();
			} else {
				//Si la fecha de devengo del primer periodo es nula y no se cumple la condicion anterior,
				//se busca la primera fecha no nula y se obtiene fDevFall utilizando dicha fecha
				boolean fecDevengoNula = true;
				for(int j = iteracion-1; j<proyUmic.size() && fecDevengoNula;j++){
					fecDevengoAux = proyUmic.get(j).getBloqueVida().getFechaDevengo();
					if (fecDevengoAux != null){
						devFall = UtilFechas.getFecha(fecDevengoAux);
						devFall.setDia(varDia);
						fDevFall = devFall.toTimestamp();

						if (fecIniRenta.after(fDevFall)){
							devFall = UtilFechas.getFecha(fecIniRenta);
							devFall.setDia(varDia);
							fDevFall = devFall.toTimestamp();
						}

						fecDevengoNula = false;
						
					}
				}
			}
		}
		
		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Fin Funcion << calcFDevFall >> de la clase FuncionesFallecimiento, con resultado fDevFall = {}", fDevFall);
		}

		return fDevFall;
	}

	/**
	 * Funcion que obtiene el término rentaFall utilizado en el cálculo de fall
	 * @param iteracion
	 * @param periodosFall
	 * @param varI1
	 * @param varI2
	 * @param numi1PorcentajeMasUno
	 * @param numi2PorcentajeMasUno
	 * @param m
	 * @param preversion
	 * @return rentaFall
	 */
	public static BigDecimal rentaFall(final Integer iteracion,final List<PeriodosFall> periodosFall, final BigDecimal varI1, final BigDecimal varI2, 
			final BigDecimal numi1PorcentajeMasUno, final BigDecimal numi2PorcentajeMasUno, final BigDecimal m, final BigDecimal preversion ){
		//Variables locales
		BigDecimal rentaFall = BigDecimal.ZERO;
		BigDecimal varTCyZ = BigDecimal.ZERO;
		BigDecimal varCz;
		BigDecimal varLxj = BigDecimal.ZERO;
		BigDecimal varLyj = BigDecimal.ZERO;
		BigDecimal varVVidaZ = BigDecimal.ZERO;
		BigDecimal varProbZ = BigDecimal.ZERO;
		BigDecimal lxIni = BigDecimal.ZERO;
		BigDecimal lyIni = BigDecimal.ZERO;
		BigDecimal multipLxy = BigDecimal.ZERO;
		//Fin variables locales

		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Inicio Funcion <<rentaFall>> de la clase FuncionesFallecimiento, para la entrada iteracion = {}, periodosFall = {},"
					+ " varI1 = {}, varI2 = {}, numi1PorcentajeMasUno = {}, numi2PorcentajeMasUno = {}, m = {}, preversion = {}",
					iteracion, periodosFall, varI1, varI2, numi1PorcentajeMasUno, numi2PorcentajeMasUno, m, preversion);
		}
		
		for (int z = iteracion; z<periodosFall.size();z++){ 
			if (z==iteracion){ //Si z es el primer elemento del bucle: 
				varTCyZ = periodosFall.get(z).getActJFall();
				lxIni = periodosFall.get(z).getLxFcalc();
				lyIni = periodosFall.get(z).getLyFcalc();
				if (lyIni!= null){
					multipLxy= lyIni.multiply(lxIni); //Se calcula solo en la primera iteracion, por ser contsante, para optimizar
				}
			}
			//Para cualquier z:
			varCz = periodosFall.get(z).getCuantiaFall();
			if(varCz.signum()!=0){
				varVVidaZ = FuncionesActualizacionFinanciera.vVida(periodosFall.get(z).getActJFallVida(), varI1, varI2, m, varTCyZ, numi1PorcentajeMasUno, numi2PorcentajeMasUno);
				
				varLxj = periodosFall.get(z).getLxF363();

				if (lyIni == null || preversion.signum()==0){
					if (lxIni.signum()!=0){
						varProbZ = varLxj.divide(lxIni, ConstantsFunciones.MATH_CONTEXT);
					}
				} else {
					varLyj = periodosFall.get(z).getLyF363();
					if (lxIni.signum()!=0 && lyIni.signum()!=0){
						BigDecimal divisionLx = varLxj.divide(lxIni, ConstantsFunciones.MATH_CONTEXT);
						BigDecimal divisionLy = varLyj.divide(lyIni, ConstantsFunciones.MATH_CONTEXT);
						BigDecimal divisionLyLx = (varLyj.multiply(varLxj)).divide(multipLxy,ConstantsFunciones.MATH_CONTEXT);

						varProbZ = divisionLx.add(preversion.multiply(divisionLy).subtract(divisionLyLx));
					} else if(lyIni.signum()!=0){ //Confirmar, si lxIni=0 --> fall=0???
						BigDecimal divisionLy = varLyj.divide(lyIni, ConstantsFunciones.MATH_CONTEXT);
						varProbZ = preversion.multiply(divisionLy);
					} else if (lxIni.signum()!=0){
						varProbZ = varLxj.divide(lxIni, ConstantsFunciones.MATH_CONTEXT);
					}
	
				}
			}
			//Se realiza el cálculo del sumatorio rentaFall = {Sumatorio desde z=varfdevfallpos hasta fecVcto} varCz * VVIda(z) * varProb(z)
			rentaFall = rentaFall.add(varCz.multiply(varVVidaZ).multiply(varProbZ));
			
		}
		
		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Fin Funcion << rentaFall >> de la clase FuncionesFallecimiento, con resultado rentaFall = {}", rentaFall);
		}

		return rentaFall;
	}


	/**
	 * Función que calcula el término FALLECPAGVT de la formulación de la corriente de Provisión Matemática por fórmula cerrada.
	 * @param m Número de años en que aplicamos un primer interés técnico desde fecha suscripción.
	 * @param n Duración real del ajuste o suscripción.
	 * @param nDap Número de años completos que quedan pendientes desde la siguiente renovación a la fecha de cálculo hasta el vencimiento.
	 * @param nDmv Fracción de año comprendida entre la fecha de renovación de la umic inmediata anterior a la fecha de vencimiento del certificado y dicha fecha de vencimiento.
	 * @param frpsgact Fecha de Renovación posterior a la fecha de cierre.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param x Edad del asegurado a Fecha de Efecto.
	 * @param zc Edad del asegurado a Fecha de cálculo.
	 * @param xRen Edad en la renovación siguiente a la fecha de cálculo fcal.
	 * @param ren Período de tiempo transcurrido desde la fecha de alta del ajuste hasta la fecha de renovación siguiente al momento de cálculo.
	 * @param vMort Actualización financiera a fcal a la mitad del período que va hasta la próxima renovación.
	 * @param vVida Actualización financiera a fcal desde la fecha de pago de cada renta j.
	 * @param tcy Distancia en Años entre la fecha de suscricpión y la fecha de cálculo.
	 * @param modBeta Variable de Apoyo MODBETA.
	 * @param beta1 Variable Beta1.
	 * @param beta2 Variable Beta2.
	 * @param i1Bti Primer Interés técnico según Bases Técnicas Iniciales.
	 * @param i2Bti Segundo Interés técnico según Bases Técnicas Iniciales.
	 * @param i1 Primer Interés técnico.
	 * @param i2 Segundo Interés técnico.
	 * @param fecvcto Fecha vencimiento.
	 * @param vari1PorcentajeMasUno
	 * @param vari2PorcentajeMasUno
	 * 
	 * @return fallecpagvt
	 */
	public static BigDecimal fallecpagvt(final BigDecimal m, final BigDecimal n, final Integer nDap, final BigDecimal nDmv, 
			final Timestamp frpsgact, final List<BigDecimal> valoresTabMort, final BigDecimal x, final BigDecimal zc, 
			final BigDecimal xRen, final BigDecimal vMort, final BigDecimal vVida, final BigDecimal tcy, final String modBeta, 
			final BigDecimal beta1, final BigDecimal beta2, final BigDecimal i1Bti, final BigDecimal i2Bti, final BigDecimal i1, 
			final BigDecimal i2, final Timestamp fecvcto, final BigDecimal vari1PorcentajeMasUno, final BigDecimal vari2PorcentajeMasUno){
		//Variables locales
		BigDecimal fallecpagvt = BigDecimal.ZERO;
		BigDecimal varLxN;
		BigDecimal varLxTCy;
		BigDecimal varLzc;
		BigDecimal varLxRenNDap; 
		BigDecimal varActuaRen;
		BigDecimal varRFlex;
		BigDecimal xN;
		BigDecimal xTCy;
		//Fin variables locales
		
		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Inicio Funcion <<fallecpagvt>> de la clase FuncionesFallecimiento, para la entrada "
					+ " m = {}, n = {}, nDap = {}, nDmv = {}, frpsgact = {}, valoresTabMort = {}, x = {}, zc = {}, xRen = {},"
					+ " vMort = {}, vVida = {}, tcy = {}, modBeta = {}, beta1 = {}, beta2 = {}, i1Bti = {}, i2Bti = {} i1 = {}, i2 = {}",
					m, n, nDap, nDmv, frpsgact, valoresTabMort, x, zc, xRen, vMort, vVida, tcy, modBeta, beta1, beta2, i1Bti, i2Bti, i1, i2);
		}
		
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesFallecimiento.validarParamEntradaFuncionFallecpagvt1(m, n, nDap, nDmv, frpsgact, valoresTabMort, x, zc, xRen, vMort);
		ValidacionesFuncionesFallecimiento.validarParamEntradaFuncionFallecpagvt2(vVida, tcy, modBeta, beta1, beta2, i1Bti, i2Bti, i1, i2, fecvcto);
		
		
		if (!modBeta.equals(ConstantsFunciones.CTE_SR)){ 
			xN = x.add(n);
			varLxN = Util.getVarLx(xN, valoresTabMort);
			BigDecimal nDapBD = BigDecimal.valueOf(nDap);
			
			if (nDap <= 0 && !fecvcto.after(frpsgact)){
				
				xTCy = x.add(tcy);
				varLxTCy = Util.getVarLx(xTCy, valoresTabMort);
				
				//Se calcula cfallecpagvt = ((varLxTCy-varLxN)/varLxTCy) * VMort
				BigDecimal resta = varLxTCy.subtract(varLxN);
				fallecpagvt = (resta.divide(varLxTCy, ConstantsFunciones.MATH_CONTEXT)).multiply(vMort);
			} else {
				varLxRenNDap = Util.getVarLx(xRen.add(nDapBD), valoresTabMort);
				varLzc = Util.getVarLx(zc, valoresTabMort);
				
				if (!varLzc.equals(BigDecimal.ZERO)){
					varActuaRen = vVida.divide(varLzc, ConstantsFunciones.MATH_CONTEXT);
					varRFlex = FuncionesActualizacionFinanciera.rflex(i1, i2, i1Bti, i2Bti, beta1, beta2, m, n, nDapBD, nDmv, vari1PorcentajeMasUno, vari2PorcentajeMasUno);
					
					//Se calcula cfallecpagvt = varActuaRen * varRFlex * (varLxRenNDap – varLxN)
					fallecpagvt = varActuaRen.multiply(varRFlex).multiply(varLxRenNDap.subtract(varLxN));
					
				}//Si varLzc= 0, se retorna cfallecpagvt = 0
				
			}
			
		}
		
		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Fin Funcion << fallecpagvt >> de la clase FuncionesFallecimiento, con resultado fallecpagvt = {}", fallecpagvt);
		}
		
		return fallecpagvt;
	}


	/**
	 * Función que calcula la valoración de riesgo de fallecimiento en la fracción de año pendiente entre el momento del cálculo 
	 * y la siguiente anualidad del seguro.
	 * @param x Edad a fecha de cálculo.
	 * @param t Resultado de la función naños() en base al criterio establecido.
	 * @param beta Fracción de año pendiente entre el momento del cálculo y la siguiente anualidad del seguro.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param w Edad Máxima de la tabla de mortalidad.
	 * @param n Duración del seguro en años.
	 * @param i1 Interés del primer tramo.
	 * 
	 * @return morpend
	 */
	public static BigDecimal morpend(final Integer x, final Integer t, final BigDecimal beta, final List<BigDecimal> valoresTabMort,
			final Integer w, final Integer n, final BigDecimal i1){
		//Inicio variables locales
		BigDecimal morpend = BigDecimal.ZERO;
		BigDecimal varLFRACt;
		BigDecimal varLFRACt1;
		//Fin variables locales
		
		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Inicio Funcion << MORPEND >> de la clase FuncionesFallecimiento, para la entrada  x = {}, t = {}, beta = {},"
					+" valoresTabMort = {}, w = {}, n = {}, i1 = {}", 
					x, t, beta, valoresTabMort, w, n, i1);
		}
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesFallecimiento.validarParamEntradaFuncionMorpend(x, t, beta, valoresTabMort, w, n, i1);
		
		if (t != n){
			varLFRACt = FuncionesAuxiliares.lfrac(x, t, beta, valoresTabMort, w);
			varLFRACt1 = FuncionesAuxiliares.lfrac(x, t+1, BigDecimal.ZERO, valoresTabMort, w);
			
			//Se calcula MORPEND = [〖 (varLFRACt -  varLFRACt1)*(1+ I1/100)〗^(-((1-beta)/2))/(varLFRACt )  ]  
			BigDecimal unoMasI1Entre100 = BigDecimal.ONE.add(i1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
			BigDecimal unoMenosBetaEntre2 = (BigDecimal.ONE.subtract(beta)).multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_5);
			BigDecimal ctMenosct1 = varLFRACt.subtract(varLFRACt1);
			
			morpend = (ctMenosct1.multiply(Util.pow(unoMasI1Entre100, unoMenosBetaEntre2.negate()))).divide(varLFRACt, ConstantsFunciones.MATH_CONTEXT);
		}
		
		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Fin Funcion << MORPEND >> de la clase FuncionesFallecimiento, con resultado morpend = {} ", morpend);
		}
		
		return morpend;
	}

	/**
	 * Función que calcula el término correspondiente a años enteros posteriores a la fecha de cálculo de la Valoración 
	 * del fallecimiento en aportaciones extraordinarias. 
	 * @param x Edad a fecha de cálculo.
	 * @param t
	 * @param ifal Variable de Apoyo IFAL.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param w Edad Máxima de la tabla de mortalidad.
	 * @param n Duración del seguro en años.
	 * @param i1 Interés del primer tramo.
	 * 
	 * @return pascap
	 */
	public static BigDecimal pascap(final Integer x, final Integer t, final BigDecimal ifal, final List<BigDecimal> valoresTabMort,
			final Integer w, final Integer n, final BigDecimal i1){
		//Inicio variables locales
		BigDecimal pascap = BigDecimal.ZERO;
		BigDecimal varLFRACt;
		BigDecimal varLFRACj;
		BigDecimal varLFRACj1;
		BigDecimal lfracJMenosJ1;
		BigDecimal potenciaIfal = BigDecimal.ZERO;
		BigDecimal potenciaI1 = BigDecimal.ZERO;
		//Fin variables locales
		
		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Inicio Funcion << PASCAP >> de la clase FuncionesFallecimiento, para la entrada  x = {}, t = {}, ifal = {},"
					+" valoresTabMort = {}, w = {}, n = {}, i1 = {}", 
					x, t, ifal, valoresTabMort, w, n, i1);
		}
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesFallecimiento.validarParamEntradaFuncionesPasadacaPascap(x, t, ifal, valoresTabMort, w, n, i1);
		
		
		/**
		 * Se calcula PASCAP como: 
		 *  PASCAP = Sumatorio (desde j=T hasta n-1) [(varLFRACj-varLFRACj1) * 〖(1+ I1/100)〗^(-(j-T+0,5)) * 〖(1+ ifal/100)〗^((j-T)) ]/varLFRACt
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

			pascap = pascap.add((lfracJMenosJ1.multiply(potenciaI1).multiply(potenciaIfal)).divide(varLFRACt, ConstantsFunciones.MATH_CONTEXT));
		}
		
		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Fin Funcion << PASCAP >> de la clase FuncionesFallecimiento, con resultado pascap = {} ", pascap);
		}
		
		return pascap;
	}
	
	/**
	 * Función que calcula la Valoración del fallecimiento en aportaciones extraordinarias. Se valora con dos partes: 
	 * MORPEND para la fracción de año en curso y PASCAP para años enteros posteriores. 
	 * @param x Edad a fecha de cálculo.
	 * @param t
	 * @param beta Fracción de año pendiente entre el momento del cálculo y la siguiente anualidad del seguro.
	 * @param n Duración del seguro en años.
	 * @param i1 Interés del primer tramo.
	 * @param ifal Variable de Apoyo IFAL.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param w Edad Máxima de la tabla de mortalidad.
	 * 
	 * @return fallecpu
	 */
	public static BigDecimal fallecpu(final Integer x, final Integer t, final BigDecimal beta, final Integer n, final BigDecimal i1,
			final BigDecimal ifal, final List<BigDecimal> valoresTabMort, final Integer w){
		//Inicio variables locales
		BigDecimal fallecpu = BigDecimal.ZERO;
		BigDecimal varLFRACt;
		BigDecimal varLFRACt1;
		BigDecimal varMordpend;
		BigDecimal varPascap;
		//Fin variables locales	
		
		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Inicio Funcion << FALLECPU >> de la clase FuncionesFallecimiento, para la entrada  x = {}, t = {}, beta = {},"
					+" n = {}, i1 = {}, ifal = {}, valoresTabMort = {}, w = {}", 
					x, t, beta, n, i1, ifal, valoresTabMort, w);
		}
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesFallecimiento.validarParamEntradaFuncionFallecpu(x, t, beta, n, i1, ifal, valoresTabMort, w);
		
		//Se obtienen las variables necesarias para el cálculo
		varLFRACt = FuncionesAuxiliares.lfrac(x, t, beta, valoresTabMort, w);
		varLFRACt1 = FuncionesAuxiliares.lfrac(x, t+1, BigDecimal.ZERO, valoresTabMort, w);
		varMordpend = morpend(x, t, beta, valoresTabMort, w, n, i1);
		varPascap = pascap(x, t, ifal, valoresTabMort, w, n, i1);
		
		BigDecimal unoMasI1Entre100 = BigDecimal.ONE.add(i1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)); 
		BigDecimal unoMasIfalEntre100 = BigDecimal.ONE.add(ifal.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		
		/**
		 * Se calcula FALLECPU como:
		 * FALLECPU =  [〖  varLFRACt1 * (1+ I1/100)〗^(-(1-beta))/(varLFRACt )  * (1+ ifal/100)*varPascap  ]    + varMordpend
		 */
		BigDecimal dividendo = varLFRACt1.multiply(Util.pow(unoMasI1Entre100, (BigDecimal.ONE.subtract(beta).negate()))); // varLFRACt1 * (1+ I1/100)〗^(-(1-beta))
		BigDecimal primerOperando = dividendo.divide(varLFRACt, ConstantsFunciones.MATH_CONTEXT).multiply(unoMasIfalEntre100).multiply(varPascap);
		
		fallecpu = primerOperando.add(varMordpend);
		
		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Fin Funcion << FALLECPU >> de la clase FuncionesFallecimiento, con resultado fallecpu = {} ", fallecpu);
		}
		
		return fallecpu;
	}
	
	/**
	 * Función que determina el valor de un seguro de fallecimiento, para una cabeza de edad X al alta, desde el momento T, 
	 * de duración N, creciente de forma geométrica, según el porcentaje indicado por PRG.
	 * @param difer Años de diferimiento hasta el inicio de la prestación.
	 * @param x Edad actuarial a fecha de efecto.
	 * @param t Anualidad del cálculo.
	 * @param n Duración del seguro.
	 * @param prg Porcentaje de la revalorización geométrica.
	 * @param lstValoresTabMort Valores de la tabla de mortalidad.
	 * @param m Número de años en que aplicamos un primer interés técnico.
	 * @param unoMasI1Entre100 Primer Interés técnico mas 1 entre 100.
	 * @param unoMasI2Entre100 Segundo Interés técnico mas 1 entre 100.
	 * @return segeo2it
	 */
	public static BigDecimal segeo2it(final Integer difer, final BigDecimal x, final Integer t, final Integer n, 
			final BigDecimal prg, final List<BigDecimal> lstValoresTabMort, final BigDecimal m, final BigDecimal unoMasI1Entre100, 
			final BigDecimal unoMasI2Entre100, final Map<String, Object> mapVariables){
		//Inicio variables locales
		BigDecimal segeo2it = BigDecimal.ZERO;
		BigDecimal varXj;
		BigDecimal varLxj;
		BigDecimal varXj1;
		BigDecimal varLxj1;
		BigDecimal varPrgj;
		BigDecimal varActVbxj;
		BigDecimal varLxT = BigDecimal.ZERO;
		//Fin variables locales	
		
		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Inicio Funcion << SEGEO_2IT >> de la clase FuncionesFallecimiento, para la entrada  difer = {}, x = {},"
					+" n = {}, prg = {}, lstValoresTabMort = {}, m = {}, unoMasI1Entre100 = {}, unoMasI2Entre100 = {}", 
					difer, x, t, n, prg, lstValoresTabMort, m, unoMasI1Entre100, unoMasI2Entre100);
		}
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesFallecimiento.validarParamEntradaFuncionSegeo2it(difer, x, t, n, prg, lstValoresTabMort, m, unoMasI1Entre100, unoMasI2Entre100);
		
		BigDecimal unoMasPrgEntre100 = BigDecimal.ONE.add(prg.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		
		/**
		 * Se calcula SEGEO_2IT como:
		 * segeo2it = SUMATORIO desde (j=T) hasta(N-1): (varLxj-varLxj1)/varLxj * varActVbxj * varPrgj 
		 */
		
		
		//Se calcula el primer valor de varPrgj para optimizar operaciones dentro del bucle
		varPrgj = Util.pow(unoMasPrgEntre100, (t-difer));
		
		//SUMATORIO desde (j=T) hasta(N-1)
		for (int j=t; j<n; j++){
			varXj = x.add(BigDecimal.valueOf(j));
			varLxj = Util.getVarLx(varXj, lstValoresTabMort);
			
			varXj1 = varXj.add(BigDecimal.ONE);
			varLxj1 = Util.getVarLx(varXj1, lstValoresTabMort);
			
			if (j == t){
				varLxT = varLxj;
			} else {
				varPrgj = varPrgj.multiply(unoMasPrgEntre100);
			}
			
			varActVbxj = FuncionesActualizacionFinanciera.actVbx(BigDecimal.valueOf(t), BigDecimal.valueOf(j+0.5), m, unoMasI1Entre100, unoMasI2Entre100, mapVariables);
			
			if (!varLxT.equals(BigDecimal.ZERO)){
				//Se calcula el término j: (varLxj-varLxj1)/varLxj * varActVbxj * varPrgj
				BigDecimal divisionLx = (varLxj.subtract(varLxj1)).divide(varLxT, ConstantsFunciones.MATH_CONTEXT);
				segeo2it = segeo2it.add(divisionLx.multiply(varActVbxj).multiply(varPrgj));
			}
		}
		
		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Fin Funcion << SEGEO_2IT >> de la clase FuncionesFallecimiento, con resultado segeo2it = {} ", segeo2it);
		}
		
		return segeo2it;
	}

	/**
	 * Función que calcula el Movimiento de coste de riesgo
	 * @param t Periodo mensual de proyección
	 * @param ttm Número de meses transcurridos desde el efecto hasta la fecha de vencimiento
	 * @param x Edad del asegurado
	 * @param ifal Variable de apoyo IFAL
	 * @param gtosF Gastos sobre el fondo del periodo
	 * @param ggim Gastos sobre la prima (mensualizado)
	 * @param k % Capital en riesgo 
	 * @param diaVto Día de vencimiento
	 * @param diaPrima Día de pago de la prima
	 * @param bxAnterior Saldo inicial del periodo
	 * @param w Edad máxima de la tabla de mortalidad
	 * @param CRMax Capital en riesgo máximo
	 * @param valoresTabMort Valores de la tabla de mortalidad
	 * @return cfall Componente de gastos sobre el fondo
	 */
	public static BigDecimal cfall (final Integer t, final Integer ttm, final Integer x, final BigDecimal i1, final BigDecimal gtosF, final BigDecimal ggim,
			final BigDecimal k, final Integer diaVto, final Integer diaPrima, final BigDecimal bxAnterior, final Integer w, final BigDecimal CRMax,
			final List<BigDecimal> valoresTabMort) {
		
		//Inicio variables locales				
		BigDecimal cfall;
		BigDecimal varEdadtb;
		BigDecimal varLxEdadTb;
		BigDecimal varLxEdadTb1;
		BigDecimal varTasaFall;
		BigDecimal vard1;
		BigDecimal varFondo;
		BigDecimal varCFALL;
		//Fin variables locales
		
		
		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Inicio Funcion << CFALL >> de la clase FuncionesFallecimiento, para la entrada  t = {}, ttm = {},"
					+" x = {}, ifal = {}, gtosF = {}, ggim = {}, k = {}, diaVto = {}, diaPrima = {}, bxAnterior = {}, w = {}, CRMax = {}, valoresTabMort = {} ",
					t, ttm, x, i1, gtosF, ggim, k, diaVto, diaPrima, bxAnterior, w, CRMax, valoresTabMort);
		}
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesFallecimiento.validarParamEntradaFuncionCfallParte1(diaVto, ttm, x, i1, gtosF);
		ValidacionesFuncionesFallecimiento.validarParamEntradaFuncionCfallParte2(ggim, k, diaVto, diaPrima, bxAnterior);
		ValidacionesFuncionesFallecimiento.validarParamEntradaFuncionCfallParte3(w, CRMax, valoresTabMort);
		
		varEdadtb = BigDecimal.valueOf(w).min(BigDecimal.valueOf(x+t/12));
		varLxEdadTb = Util.getVarLx(varEdadtb, valoresTabMort);
		varLxEdadTb1 = Util.getVarLx(varEdadtb.add(BigDecimal.ONE), valoresTabMort);
		
		// (varLxEdadTb1/varLxEdadTb)^(1⁄12)
		if(varLxEdadTb != BigDecimal.ZERO){
		
		
		BigDecimal opTasaFall1 = Util.pow(varLxEdadTb1.divide(varLxEdadTb, ConstantsFunciones.MATH_CONTEXT),
					ConstantsFunciones.CTE_OPER_1ENTRE12);
		
		// (1+ i1)^(-(1⁄24) )
		BigDecimal opTasaFall2 = Util.pow(BigDecimal.ONE.add(i1),
				ConstantsFunciones.CTE_OPER_1ENTRE24.negate());
		// (1-gastF)
		BigDecimal opTasaFall3 = BigDecimal.ONE.subtract(gtosF);
		// 	varTasaFall = [1- (varLxEdadTb1/varLxEdadTb)^(1⁄12) ]* [(1+ ifal)^(-(1⁄24) )/((1-gastF) )]
		varTasaFall = (BigDecimal.ONE.subtract(opTasaFall1)).multiply(opTasaFall2.divide(opTasaFall3,ConstantsFunciones.MATH_CONTEXT));
		
		
		if (t == ConstantsFunciones.CTE_0) {
			vard1 = (ConstantsFunciones.CTE_OPER_30_PUNTO_5.subtract(new BigDecimal(diaPrima))).divide(ConstantsFunciones.CTE_OPER_30_PUNTO_5,ConstantsFunciones.MATH_CONTEXT);
		}
		else if (t > 0 && t < ttm) {
			vard1 = BigDecimal.ONE;
		}
		else {
			vard1 = BigDecimal.valueOf(diaVto).divide(ConstantsFunciones.CTE_OPER_30_PUNTO_5,ConstantsFunciones.MATH_CONTEXT);			
		}
			
		// varFondo = K * bxAnterior * ( 1 – ggim)
		varFondo = k.multiply(bxAnterior).multiply(BigDecimal.ONE.subtract(ggim));
		
		// varCFALL =  Mínimo (varFondo, CRmax)  * varTasaFall * vard1
		varCFALL = varFondo.min(CRMax).multiply(varTasaFall).multiply(vard1);
		
		
		// CFALL = Máximo (varCFALL, 0.01)
		cfall = varCFALL.max(ConstantsFunciones.CTE_OPER_0_PUNTO_01);		
		
		}else{
			cfall = BigDecimal.ZERO;
		}
		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Fin Funcion << CFALL >> de la clase FuncionesFallecimiento, con resultado cfall = {} ", cfall);
		}
			
		return cfall;		
	}
	
	
	/**
	 * Función auxiliar CF de la función VBX462 que calcula el Capital de Fallecimiento
	 * @param j Periodo de cálculo
	 * @param proyUmic Estructura detalleCorrientes de la umic
	 * @param cfant Capital de Fallecimiento 
	 * @param pu Aportación única del ajuste o de la suscripción
	 * @param varTcm Número de mensualidades completas transcurridas desde la fecha de efecto hasta la fecha de cálculo
	 * @return Capital de Fallecimiento
	 */
	public static BigDecimal cf (final Integer j, final List<DetalleCorriente> proyUmic, final BigDecimal cfant, final BigDecimal pu,
			final Integer varTcm) {
		// Variables locales
		BigDecimal cf;
		// Fin variables locales
		
		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Inicio Funcion << CF >> de la clase FuncionesFallecimiento, para la entrada  j = {}, proyUmic = {},"
					+" cfant = {}, pu = {}, varTcm = {} ",
					j, proyUmic, cfant, pu, varTcm);
		}
		
		// Validación parámetros de entrada
		ValidacionesFuncionesFallecimiento.validarParamEntradaFuncionCf(j, proyUmic, cfant, pu, varTcm);
		
		if (j == ConstantsFunciones.CTE_1) {
			BigDecimal sumatorio = BigDecimal.ZERO;
			for (int i=1; i<varTcm; i++) {
				sumatorio = sumatorio.add(proyUmic.get(i-ConstantsFunciones.CTE_1).getImpPago());												
			}
			
			cf = pu.subtract(sumatorio);	
		} else {
			cf = cfant.subtract(proyUmic.get(j-ConstantsFunciones.CTE_1).getImpPago());
			
		}
		
		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Fin Funcion << CF >> de la clase FuncionesFallecimiento, con resultado cf = {} ", cf);
		}
		
		return BigDecimal.ZERO.max(cf);
	}
	
	
	/**
	 * Función auxiliar FALLEC de la provisión VBX462 que calcula la probabilidad de fallecimiento
	 * @param j Periodo de cálculo
	 * @param xc Edad a fecha de cálculo
	 * @param xj Edad a fecha de periodo
	 * @param xjant Edad a fecha del periodo anterior
	 * @param valoresTabMortX Valores de la tabla de mortalidad
	 * @return Probabilidad de fallecimiento
	 */
	public static BigDecimal fallec (final Integer j, final BigDecimal xc, final BigDecimal xj, final BigDecimal xjant,
			final List<BigDecimal> valoresTabMortX) {
		// Variables locales
		BigDecimal fallec;
		BigDecimal Lxj;
		BigDecimal Lxc;
		BigDecimal Lxjant;
		// Fin variables locales
		
		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Inicio Funcion << FALLEC >> de la clase FuncionesFallecimiento, para la entrada xc = {}, xj = {},"
					+" xjant = {}, valoresTabMortX = {} ",
					xc, xj, xjant, valoresTabMortX);
		}
		
		// Validación parámetros de entrada
		ValidacionesFuncionesFallecimiento.validarParamEntradaFuncionFallec(j, xc, xj, xjant, valoresTabMortX);
		
		Lxj = Util.getVarLx(xj, valoresTabMortX);
		Lxc = Util.getVarLx(xc, valoresTabMortX);
		Lxjant = Util.getVarLx(xjant, valoresTabMortX);
		
		if (j == ConstantsFunciones.CTE_1) {
			fallec = Lxc.subtract(Lxj).divide(Lxc, ConstantsFunciones.MATH_CONTEXT);
		} else {
			fallec = Lxjant.subtract(Lxj).divide(Lxc, ConstantsFunciones.MATH_CONTEXT);
		}		
		
		if (FuncionesFallecimiento.LOG.isTraceEnabled()) {
			FuncionesFallecimiento.LOG.trace("Fin Funcion << FALLEC >> de la clase FuncionesFallecimiento, con resultado fallec = {} ", fallec);
		}		
		
		return fallec;
	}
	
}

