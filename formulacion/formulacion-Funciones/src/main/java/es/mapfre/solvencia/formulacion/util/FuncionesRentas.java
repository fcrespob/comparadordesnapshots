/**MODIFICACION: TAR00433819
//FECHA: 15/01/2019
//DESCRIP: Se incluye el tratamiento para los nuevos estados de prorrogas U,F, que se tienen que comportar como VI.
*/

package es.mapfre.solvencia.formulacion.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

public class FuncionesRentas {
	
	/** 
	 * Log funciones de rentas.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(FuncionesRentas.class);
	
	
	/**
	 * La función vrta calcula una renta actuarial pospagable desplazada k meses.
	 * 
	 * @param mesesCompletos
	 * 				Meses completos transcurridos desde efecto (técnico) póliza a fecha cálculo
	 * @param diferRenta
	 * 				Diferimiento de la renta (años)
	 * @param duracionRenta
	 * 				Duración de la renta (desde fecha efecto técnico (años)
	 * @param tipoIPrimerTramo
	 * 				Tipo de interés primer tramo
	 * @param duracionPrimerTra
	 * 				Duración primer tramo
	 * @param tipoISegundoTramo
	 * 				Tipo de interés segundo tramo
	 * @param edadActuarial
	 * 				Edad actuarial a fecha efecto técnico.
	 * @param lstValoresTabMort
	 * 				Valores de la tabla de mortalidad
	 * @param numPagos
	 * 				Nº de Pagos por año de la renta
	 * @param porcenRevalRen
	 * 				Porcentaje revalorización de la renta
	 * @param indUL
	 * 				Indicador de modalidades United Linked
	 * @param nmeses
	 * 				Duración de la renta (meses)
	 * @return resultadoVrta
	 */

	public static BigDecimal vrta(final Integer mesesCompletos, final Integer diferRenta, final Integer duracionRenta, final BigDecimal tipoIPrimerTramo,
			final Integer duracionPrimerTra, final BigDecimal tipoISegundoTramo, final Integer edadActuarial, final List<BigDecimal> lstValoresTabMort,
			final Integer numPagos, final BigDecimal porcenRevalRen, final boolean indUL, final Integer nmeses) {
		//Variables locales
		BigDecimal resultadoVrta = BigDecimal.ZERO;
		int varLimiteInferior = 0;
		int varLimiteSuperior = 0;
		int uveDoble = 0;
		BigDecimal operando1 = BigDecimal.ZERO;
		BigDecimal operando2 = BigDecimal.ZERO;
		BigDecimal operando3 = BigDecimal.ZERO;
		boolean operando3Calculado = false;
		//Fin variables locales

		if (FuncionesRentas.LOG.isTraceEnabled()) {
			FuncionesRentas.LOG.trace("Inicio función << vRtad >> de la clase FuncionesRentas, para la entrada mesesCompletos = {}, diferRenta = {}, duracionRenta = {}, tipoIPrimerTramo = {}, duracionPrimerTra = {}, duracionTramo1 = {}, tipoISegundoTramo = {}, edadActuarial = {}, lstValoresTabMort = {}, numPagos = {} y porcenRevalRen = {}",
					mesesCompletos, diferRenta, duracionRenta, tipoIPrimerTramo, duracionPrimerTra, tipoISegundoTramo, edadActuarial, lstValoresTabMort, numPagos, porcenRevalRen);
		}

		//Comprobamos que los campos de la función vienen informados
		ValidacionesFuncionesRentas.validacionCamposFuncionVRtaParte1(mesesCompletos, diferRenta, duracionRenta, tipoIPrimerTramo, duracionPrimerTra);
		ValidacionesFuncionesRentas.validacionCamposFuncionVRtaParte2(tipoISegundoTramo, edadActuarial, lstValoresTabMort, numPagos, porcenRevalRen);
		/*
		 * Se calcularán las siguientes variables: 
			varLimInferior
			-	Si k/12 <=d entonces --> varLimInferior =d*12
			-	Si k/12 > d entonces --> varLimInferior = k
			varLimSuperior
			-	Si N=99 entonces -->  varLimSuperior = (w-x)*12 , donde
				o	Para obtener la variable W se buscará en la tabla valoresTabMort la mínima edad en la que wkvalor  se hace cero
			-	Si N <> 99 entonces --> varLimSuperior = N*12
		 */

		//varLimInferior
		varLimiteInferior = Util.mult12(diferRenta);
		if (mesesCompletos > varLimiteInferior) {
			varLimiteInferior = mesesCompletos;
		}

		//varLimSuperior
		if (duracionRenta.equals(ConstantsFunciones.CTE_99)) {

			// varw = Para obtener la variable W se buscará en la tabla valoresTabMort la mínima edad en la que wkvalor  se hace cero
			uveDoble = Util.obtenerEdadPrimerCero(lstValoresTabMort);
			if (indUL){
				varLimiteSuperior = Util.mult12(uveDoble - edadActuarial) -1; 
			}else{
				varLimiteSuperior = Util.mult12(uveDoble - edadActuarial); 
			}

		} else {
			if (indUL){
				varLimiteSuperior = Util.mult12(duracionRenta) + nmeses -1;
			}else{
				varLimiteSuperior = Util.mult12(duracionRenta) + nmeses;
			}
			
		}

		// Se convierte a BigDecimal fuera del bucle
		BigDecimal bdMesesCompletos = new BigDecimal(mesesCompletos);
		BigDecimal bdEdadActuarial = new BigDecimal(edadActuarial);

		// Los intereses se calculan fuera del bucle
		BigDecimal int1 = BigDecimal.ONE.add(tipoIPrimerTramo.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		BigDecimal int2 = BigDecimal.ONE.add(tipoISegundoTramo.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));

		//base <-- (1+prr/100)
		BigDecimal baseOperando2;
		boolean baseOperando2EsUno = false; 

		if(porcenRevalRen.signum() == 0) {
			baseOperando2 = BigDecimal.ONE;
			operando2 = BigDecimal.ONE;
			baseOperando2EsUno = true;
		} else {
			baseOperando2 = BigDecimal.ONE.add(porcenRevalRen.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		}

		/**
		 *    operando1 --> Probabilidad de que una persona de edad x más k meses, viva t-k meses más. 
		 *    Esta probabilidad se determinará con las tablas a utilizar en cada momento para el cálculo de provisiones y se calculará como:
		 * 		varEdad1 = x + (k/12) 
		 * 		varLxEdad1 = valoresTabMort(varEdad1).wkvalor
		 */
		BigDecimal varLxEdad1;
		BigDecimal unoDivvarLxEdad1 = BigDecimal.ZERO;
		BigDecimal varEdad1 = bdEdadActuarial.add(bdMesesCompletos.multiply(ConstantsFunciones.CTE_OPER_1ENTRE12, ConstantsFunciones.MATH_CONTEXT));
		int varEdad1Entero = varEdad1.intValue();
		if (varEdad1Entero <= lstValoresTabMort.size()-2){
			BigDecimal varLxEdad1Entero = lstValoresTabMort.get(varEdad1Entero);
			if(varLxEdad1Entero.signum() != 0) {
				BigDecimal varLxEdad1Entero1 = lstValoresTabMort.get(varEdad1Entero + 1);
				//varLxEdad1 = varLxEdad1Entero.add(varEdad1.remainder(BigDecimal.ONE).multiply(varLxEdad1Entero1.subtract(varLxEdad1Entero)));
				varLxEdad1 = Util.interpolaPorEdad(varLxEdad1Entero, varLxEdad1Entero1, varEdad1);
				unoDivvarLxEdad1 = BigDecimal.ONE.divide(varLxEdad1, ConstantsFunciones.MATH_CONTEXT);				
			} else {
				return resultadoVrta;
			}
		}else{
			return resultadoVrta;
		}


		// k <= L*12
		//boolean kMenorOIgualLPor12 = bdMesesCompletos.compareTo(bdDuracionPrimerTra.multiply(ConstantsFunciones.CTE_OPER_12)) <= 0;
		int duracionPrimerTraMeses = Util.mult12(duracionPrimerTra);

		int pp = 0;
		int exponente = 0;
		int exponenteAnt = -1;
		int mesesPago = 12 / numPagos;
		final BigDecimal int1Incr = Util.pow(int1, BigDecimal.valueOf(mesesPago).multiply(ConstantsFunciones.CTE_OPER_1ENTRE12, ConstantsFunciones.MATH_CONTEXT).negate());
		final BigDecimal int2Incr = Util.pow(int2, BigDecimal.valueOf(mesesPago).multiply(ConstantsFunciones.CTE_OPER_1ENTRE12, ConstantsFunciones.MATH_CONTEXT).negate());

		int enteroVarEdad2Ant = -2;
		BigDecimal probJ = BigDecimal.ZERO;
		BigDecimal diferJ1J = BigDecimal.ZERO;
		BigDecimal probJ1 = BigDecimal.ZERO;

		//Se calculará Vrta como el sumatorio en t desde t= varLimInferior +1  hasta t= varLimSuperior
		for (int t = varLimiteInferior + 1 ; t <= varLimiteSuperior; t++) {

			//Calculamos operando2 --> p
			//operando2 = operando2Vrta(t, mesesCompletos, numPagos, baseOperando2);


			//Cálculo de operando2
			//Calculamos Operando2 --> p
			/**
			 * pp=MOD(t,12/m)
				-	si pp=0 entonces  --> p=(1+prr/100)^ent((t+k-1)/12)
				-	Si pp <> 0 entonces  --> p=0

			 */
			pp = t % mesesPago;


			if (pp == 0) {
				if(!baseOperando2EsUno) {
					//elevado <-- ent((t+k-1)/12)
					exponente = (t + mesesCompletos - 1) / ConstantsFunciones.CTE_12;
					if(exponente > exponenteAnt) {
						if(exponenteAnt == -1) {
							operando2 = Util.pow(baseOperando2, exponente);
						} else {
							operando2 = operando2.multiply(baseOperando2, ConstantsFunciones.MATH_CONTEXT);						
						}
						exponenteAnt = exponente;
					}
				}
				//operando2 <-- base ^ elevado
				//operando2 = Util.pow(baseOperando2, exponente);

				//Calculamos operando1 --> t-k ^P x+k Probabilidad de que una persona de edad x más k meses, viva t-k meses más
				//operando1 = operando1Vrta(iteracion, varEdad1, bdMesesCompletos, lstValoresTabMort, varLxEdad1, tamListaValoresTabMort);

				//Cálculo de operando1
				/**
				 *    operando1 --> Probabilidad de que una persona de edad x más k meses, viva t-k meses más. 
				 *    Esta probabilidad se determinará con las tablas a utilizar en cada momento para el cálculo de provisiones y se calculará como:
				 * 		varEdad1 = x + (k/12) 
				 * 		varEdad2 = varEdad1 + ((t-k)/12)
				 * 		varLxEdad1 = valoresTabMort(varEdad1).wkvalor
				 * 		varLxEdad2 = valoresTabMort(varEdad2).wkvalor
				 */
				BigDecimal varLxEdad2Entero;
				BigDecimal varLxEdad2Entero1;

				BigDecimal tMenosMesesCompletosEntre12 = BigDecimal.valueOf(t - mesesCompletos).multiply(ConstantsFunciones.CTE_OPER_1ENTRE12, ConstantsFunciones.MATH_CONTEXT);

				BigDecimal varEdad2 = varEdad1.add(tMenosMesesCompletosEntre12);

				int enteroVarEdad2 = varEdad2.intValue();

				if (enteroVarEdad2 <= lstValoresTabMort.size()-2){
					varLxEdad2Entero = lstValoresTabMort.get(enteroVarEdad2);
					if(varLxEdad2Entero.signum() != 0) {
						if(enteroVarEdad2Ant != enteroVarEdad2) {
							// La expresión final es algo como esto: varLj2Entero/varLzc2 + (varLj2Entero1-varLj2Entero)*parteDecimal(varEdadJ2)/varLzc2
							// Se calculan los términos constantes para ser reutilizados mientras no cambie la indexación de las tablas de experiencia

							if(enteroVarEdad2 == enteroVarEdad2Ant + 1) {
								// Si el salto es sólo de un índice se establece la probabilidad de J1 como la de J1Sig
								probJ = probJ1; 
							} else {
								probJ = varLxEdad2Entero.multiply(unoDivvarLxEdad1, ConstantsFunciones.MATH_CONTEXT);
							}
							varLxEdad2Entero1 = lstValoresTabMort.get(enteroVarEdad2 + 1);

							probJ1 = varLxEdad2Entero1.multiply(unoDivvarLxEdad1, ConstantsFunciones.MATH_CONTEXT);

							diferJ1J = probJ1.subtract(probJ);		
							enteroVarEdad2Ant = enteroVarEdad2;
						}

						operando1 =  probJ.add(diferJ1J.multiply(varEdad2.subtract(varEdad2.setScale(0, RoundingMode.DOWN)), ConstantsFunciones.MATH_CONTEXT));						
					} else {
						operando1 = BigDecimal.ZERO;						
					}					
				} else {
					operando1 = BigDecimal.ZERO;
				}

				//Calculamos operando 3 --> Vp->k(t) Factor de actualización financiera entre el momento t y el k del seguro
				//operando3 = operando3Vrta(iteracion, bdMesesCompletos, bdDuracionPrimerTra, int1, int2, kMenorOIgualLPor12);

				//Cálculo de operando3
				//Variables locales
				BigDecimal elevado = BigDecimal.ZERO;
				//Fin variables locales

				/**
				 *   Operando 3 -->  Factor de actualización financiera entre el momento t y el k del seguro, 
				 *   calculado con los tipos de interés técnico utilizables en cada momento para el cálculo de provisiones. 
				 *   Vendrá definido como:
				 *   	SI k <= L*12
				 *   		SI t/12 <= L
				 *   			operando3 <-- (i1) ^ -((t-k)/12)
				 *   		SI t/12 > L 
				 *   			operando3 <-- [(i1) ^ -(L- (k/12))] * [(i2) ^ -((t/12) - L)]
				 *   	SI k > L*12
				 *   		operando3 <-- (i2) ^ -((t-k)/12)
				 */
				// k <= L*12
				if (mesesCompletos <= duracionPrimerTraMeses) { 
					//SI t/12 <= L					
					if (t <= duracionPrimerTraMeses) { 
						//base <-- i1
						//elevado <-- ((t-k)/12)
						if(operando3Calculado) {
							operando3 = operando3.multiply(int1Incr, ConstantsFunciones.MATH_CONTEXT);
						} else {
							elevado = tMenosMesesCompletosEntre12;
							operando3 = Util.pow(int1, elevado.negate());
							operando3Calculado = true;
						}
						//						elevado = tMenosMesesCompletosEntre12;
						//						//operando3 <-- base ^ -elevado
						//						operando3 = Util.pow(int1, elevado.negate());
						//SI t/12 > L 
					} else {
						//base1 <-- i1
						//base2 <--i2
						//elevado1 <-- (L- (k/12))
						if(t <= duracionPrimerTraMeses + mesesPago) {
							BigDecimal elevado1 = BigDecimal.valueOf(duracionPrimerTraMeses - mesesCompletos).multiply(ConstantsFunciones.CTE_OPER_1ENTRE12, ConstantsFunciones.MATH_CONTEXT);
							BigDecimal elevado2 = BigDecimal.valueOf(t-duracionPrimerTraMeses).multiply(ConstantsFunciones.CTE_OPER_1ENTRE12, ConstantsFunciones.MATH_CONTEXT);
							operando3 = Util.pow(int1, elevado1.negate()).multiply(Util.pow(int2, elevado2.negate()), ConstantsFunciones.MATH_CONTEXT);							
						} else {
							operando3 = operando3.multiply(int2Incr, ConstantsFunciones.MATH_CONTEXT);
						}

						//						BigDecimal elevado1 = BigDecimal.valueOf(duracionPrimerTraMeses - mesesCompletos).divide(ConstantsFunciones.CTE_OPER_12, ConstantsFunciones.MATH_CONTEXT);
						//						//elevado2 <--  ((t/12) - L)
						//						BigDecimal elevado2 = BigDecimal.valueOf(t-duracionPrimerTraMeses).divide(ConstantsFunciones.CTE_OPER_12, ConstantsFunciones.MATH_CONTEXT);
						//						//operando3 <-- [(1 + i1) ^ -(L- (k/12))] * [(1 + i2) ^ -((t/12) - L)]
						//						operando3 = Util.pow(int1, elevado1.negate()).multiply(Util.pow(int2, elevado2.negate()));
					}
				} else {
					// k > L*12
					//base <-- i2
					//elevado <-- ((t-k)/12)
					if(operando3Calculado) {
						operando3 = operando3.multiply(int2Incr, ConstantsFunciones.MATH_CONTEXT);
					} else {
						elevado = tMenosMesesCompletosEntre12;
						operando3 = Util.pow(int2, elevado.negate());
						operando3Calculado = true;
					}
					//					elevado = tMenosMesesCompletosEntre12;
					//					//operando3 <-- base ^ -elevado
					//					operando3 = Util.pow(int2, elevado.negate());
				}

				resultadoVrta = resultadoVrta.add(operando1.multiply(operando2, ConstantsFunciones.MATH_CONTEXT).multiply(operando3, ConstantsFunciones.MATH_CONTEXT));

			}

		}

		if (FuncionesRentas.LOG.isTraceEnabled()) {
			FuncionesRentas.LOG.trace("Fin función << vRtad >> de la clase FuncionesRentas, con resultado resultadoVrta = {}",resultadoVrta);
		}

		return resultadoVrta;
	}
	
	/**
	 * Este término identifica una Renta Actuarial a una cabeza, prepagable, Constante, desplazada(p), diferida(difer) y de duración M.
	 * @param difer
	 * 				Diferimiento de la renta.
	 * @param varx
	 * @param varp
	 * @param varm
	 * @param vari1
	 * @param lstValoresTabMort
	 * 				Valores de la tabla de mortalidad
	 * @return resultadoAX
	 */
	public static BigDecimal ax(final Integer difer,final Integer varx, final Integer varp, final Integer varm, final BigDecimal vari1, final List<BigDecimal> lstValoresTabMort) {
		//Variables locales
		BigDecimal resultadoAX = BigDecimal.ZERO;
		int limite = 0;
		BigDecimal lXMasJ = BigDecimal.ZERO;
		BigDecimal lXMasP = BigDecimal.ZERO;
		//Fin variables locales

		if (FuncionesRentas.LOG.isTraceEnabled()) {
			FuncionesRentas.LOG.trace("Inicio Funcion << ax >> de la clase FuncionesRentas, para la entrada difer = {} ,varx = {}, varp = {}, varm = {}, vari1 = {} y lstValoresTabMort = {}", difer, varx, varp, varm, vari1, lstValoresTabMort);
		}

		//Validamos los parametros de entrada
		ValidacionesFuncionesRentas.validarParamEntradaFuncionAx(difer, varx, varp, varm, vari1, lstValoresTabMort);

		//Si p < difer --> Se establecerá como límite inferior varLimite=difer
		//Si p >= difer --> Se establecerá como límite inferior varLimite=p
		if (varp.compareTo(difer) < 0) {
			limite = difer;
		} else if (varp.compareTo(difer) >= 0) {
			limite = varp;
		}

		// (1+ I1/100)
		final BigDecimal unoVari1Div100 = BigDecimal.ONE.add(vari1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		final BigDecimal unoEntreUnoVari1Div100 = BigDecimal.ONE.divide(unoVari1Div100, ConstantsFunciones.MATH_CONTEXT);
		BigDecimal factori1 = Util.pow(unoVari1Div100, -(limite - varp));

		// lXMasP se puede sacar del bucle
		//L(x+p) = valoresTabMort(x+p).wkvalor
		lXMasP = lstValoresTabMort.get(varx + varp);

		for (int j = limite ; j < varm; j++ ) {
			//Calculamos L(X+J) y L(X+P) como sigue
			//L(x+j) = valoresTabMort(x+j).wkvalor
			lXMasJ = lstValoresTabMort.get(varx + j);

			//resultadoAX <-- resultadoAX + (l(x+j))/(l(x+p)) (1+ I1/100)^(-(j-p))
			resultadoAX = resultadoAX.add(lXMasJ.divide(lXMasP, ConstantsFunciones.MATH_CONTEXT).multiply(factori1));
			factori1 = factori1.multiply(unoEntreUnoVari1Div100, ConstantsFunciones.MATH_CONTEXT);
		}

		if (FuncionesRentas.LOG.isTraceEnabled()) {
			FuncionesRentas.LOG.trace("Fin Funcion << ax >> de la clase FuncionesRentas, con resultado resultadoAX = {}", resultadoAX);
		}

		return resultadoAX;
	}

	/**
	 *	Este término identifica una renta actuarial de una cabeza, prepagable, creciente en progresión GEOMETRICA, desplazada(p), 
	 *	diferida(difer) y de duración M.
	 *
	 * @param difer Diferimiento de la renta
	 * @param varx
	 * @param varp
	 * @param varm
	 * @param vari1
	 * @param prp Porcentaje de revalorización de Primas
	 * @param valoresTabMort Valores de la tabla de mortalidad 
	 * @return resultadoAxcg
	 */
	public static BigDecimal axcg(final Integer difer,final Integer varx, final Integer varp, final Integer varm, final BigDecimal vari1,  final BigDecimal prp, final List<BigDecimal> lstValoresTabMort) {
		//Variables locales
		BigDecimal resultadoAxcg = BigDecimal.ZERO;
		BigDecimal lXMasJ = BigDecimal.ZERO;
		BigDecimal lXMasP = BigDecimal.ZERO;
		BigDecimal calculoIntermedio = BigDecimal.ZERO;
		int varLimite = 0;
		//Fin variables locales

		if (FuncionesRentas.LOG.isTraceEnabled()) {
			FuncionesRentas.LOG.trace("Inicio Funcion << axcg >> de la clase FuncionesRentas, para la entrada  difer = {}, varx = {}, varp = {}, varm = {}, vari1 = {}, prp = {} y lstValoresTabMort = {}", difer, varx, varp, varm, vari1, prp, lstValoresTabMort);
		}

		//Validamos los parametros de entrada
		ValidacionesFuncionesRentas.validarParamEntradaFuncionAxcg(varx, varp, varm, vari1, difer, prp, lstValoresTabMort);

		//Establecemos por donde empieza a iterar el sumatorio
		if (varp.compareTo(difer) < 0) {
			varLimite = difer;
		} else {
			varLimite = varp;
		}

		//L(x+p) = valoresTabMort(x+p).wkvalor
		lXMasP = lstValoresTabMort.get(varx + varp);

		final BigDecimal unoPrpDiv100 = BigDecimal.ONE.add(prp.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		final BigDecimal unoVari1Div100 = BigDecimal.ONE.add(vari1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));

		for (int j = varLimite; j < varm; j++) {
			//Calculamos L(X+J) y L(X+P) como sigue
			//L(x+j) = valoresTabMort(x+j).wkvalor
			if ((varx + j)>lstValoresTabMort.size()-2){
				lXMasJ = BigDecimal.ZERO;
				calculoIntermedio = BigDecimal.ZERO;
			}else{
				lXMasJ = lstValoresTabMort.get(varx + j);
				if (lXMasJ.signum()==0){
					calculoIntermedio = BigDecimal.ZERO;
				}else{
					/**
					 * AXcg(difer,x,p,M,I1,PRP)= SUMATORIO(j=varLimite) hasta(m-1) [(1+ prp/100)^((j-difer) )* (1+ I1/100)^(-(j-p))* (l (x+j))/(l(x+p))] 
					 */
					calculoIntermedio = Util.pow(unoPrpDiv100, j - difer).multiply(
							Util.pow(unoVari1Div100, -(j - varp)).multiply(lXMasJ.divide(lXMasP, ConstantsFunciones.MATH_CONTEXT)));
				}
			}

			resultadoAxcg = resultadoAxcg.add(calculoIntermedio);
		}

		if (FuncionesRentas.LOG.isTraceEnabled()) {
			FuncionesRentas.LOG.trace("Fin Funcion << axcg >> de la clase FuncionesRentas, con resultado resultadoAxcg = {} ", resultadoAxcg);
		}

		return resultadoAxcg;
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
	 * @param tCy Resultado de la función naños() en base al criterio establecido entre la fecha de efecto de la suscripción y la fecha de cálculo.
	 * @param m Duración en que aplica el primer interés técnico.
	 * @param lstValoresTabMort Valores de la tabla de mortalidad.
	 * @param iteracion
	 * @param varCriterFec
	 * @return renta Término correspondiente al pago de las Rentas futuras de la provisión matemática.
	 */
	public static BigDecimal renta (final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Umic umic,
			final DetalleBaseTecnica btcUmic, final Integer x363, final Integer y363, final BigDecimal lxIni, final BigDecimal lyIni, final BigDecimal tCy,
			final BigDecimal m, final List<BigDecimal> lstValoresTabMort, final Integer iteracion, final String varCriterFec){
		//Variables locales
		BigDecimal renta = BigDecimal.ZERO;
		Timestamp varfecJ1;
		BigDecimal varCj;
		BigDecimal varVVida = BigDecimal.ZERO;
		BigDecimal varProb = BigDecimal.ZERO;
		BigDecimal varJ1;
		BigDecimal varActj363 = BigDecimal.ZERO;
		BigDecimal preversion;
		BigDecimal varEdadXj=null;
		BigDecimal varLxj;
		BigDecimal varLyj = BigDecimal.ZERO;
		boolean actj363IgualA0 = false;
		BigDecimal varEdadXini = BigDecimal.ZERO;
		BigDecimal varEdadYini = BigDecimal.ZERO;
		Timestamp varfecJIni = null;
		BigDecimal varActjIni363 = BigDecimal.ZERO;
		boolean primeraFecDevNoNull = true;
		//Fin variables locales

		if (FuncionesRentas.LOG.isTraceEnabled()) {
			FuncionesRentas.LOG.trace("Inicio Funcion <<renta>> de la clase FuncionesRentas, para la entrada proyUmic = {}, bloqueCorriente = {},"
					+ " btcUmic = {}, x363 = {}, y363 = {}, LxIni = {}, LyIni = {}, TCy = {}, m = {}, lstValoresTabMort = {}",
					proyUmic, bloqueCorriente, umic, btcUmic, x363, y363, lxIni, lyIni, tCy, m, lstValoresTabMort);
		}

		//Validaciones parámetros obligatorios de entrada
		ValidacionesFuncionesRentas.validarParamEntradaFuncionRenta(proyUmic, bloqueCorriente, umic, btcUmic, x363, lxIni, tCy, m, lstValoresTabMort, varCriterFec);

		if (lxIni.equals(BigDecimal.ZERO)){
			return renta;
		}

		//Variables auxiliares para el cálculo
		BigDecimal varI1 = btcUmic.getItcalc().get(0);
		BigDecimal varI2 = btcUmic.getItcalc().get(1);
		BigDecimal numi1PorcentajeMasUno = BigDecimal.ONE.add(varI1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		BigDecimal numi2PorcentajeMasUno = BigDecimal.ONE.add(varI2.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		preversion = umic.getRentas().getPreversion().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
		BigDecimal multipLxy = BigDecimal.ZERO;
		BigDecimal y363bd = BigDecimal.ZERO;
		BigDecimal varLyj0 = BigDecimal.ZERO;

		//Se calculan los valores varLxj y varLyj para el caso de que varEdadXj = x363bd y varEdadYj = y363bd, 
		//situación que se produce cuando varActj363 = 0, por no ser necesario su cálculo en cada término del sumatorio
		BigDecimal x363bd = BigDecimal.valueOf(x363);
		BigDecimal varLxj0 = Util.getVarLx(x363bd, lstValoresTabMort);
		if (y363!=null){
			y363bd = BigDecimal.valueOf(y363);
			varLyj0 = Util.getVarLx(y363bd, lstValoresTabMort);
			multipLxy = lyIni.multiply(lxIni); //Se saca el cálculo fuera del bucle, por ser contsante, para optimizar
		}
		varEdadXini = x363bd;
		varEdadYini = y363bd;

		
		//Sumatorio. Desde el periodo de proyección que se está calculando hasta el último de los periodos.
		for (int j = iteracion-1; j<proyUmic.size(); j++){
			varfecJ1 = proyUmic.get(j).getBloqueVida().getFechaDevengo();
			varCj = proyUmic.get(j).getImpPago();
			
			//Para el primer periodo que exista fecha de devengo se calcula:
			if (primeraFecDevNoNull){
				varfecJIni = proyUmic.get(j).getBloqueVida().getFechaDevengo();
				if (varfecJIni!= null && !varfecJIni.before(umic.getRentas().getFecIni())){
					varActjIni363 = FuncionesAuxiliares.nAnnos(umic.getRentas().getFecIni(), varfecJIni, varCriterFec);
					primeraFecDevNoNull = false;
					varEdadXini = x363bd.add(varActjIni363);
					varEdadYini = y363bd.add(varActjIni363);
				}
				
			}

			if (varCj.signum()!=0){
				varJ1 = FuncionesAuxiliares.nAnnos(umic.getFechas().getFecinisus(), varfecJ1, varCriterFec);
				varVVida = FuncionesActualizacionFinanciera.vVida(varJ1, varI1, varI2, m, tCy,  numi1PorcentajeMasUno, numi2PorcentajeMasUno);

				if (!varfecJ1.before(umic.getRentas().getFecIni())){
					//varActj363 = nAnnos(umic.getRentas().getFecIni(), UtilFechas.decreDias(varfecJ1, 1), varCriterFec);
					varActj363 = FuncionesAuxiliares.nAnnos(varfecJIni, varfecJ1, varCriterFec);
					varEdadXj = varEdadXini.add(varActj363);
					varLxj = Util.getVarLx(varEdadXj, lstValoresTabMort);
				} else {
					//Si varActj363 = 0, se recuperan los valores varLxj y varLyj calculados fuera del bucle por ser constantes
					varLxj = varLxj0;
					varLyj = varLyj0;
					actj363IgualA0 = true;
				}

				if(y363==null || preversion.signum()==0){
					//Se calcula varProb(j) = varLxj/LxIni
					varProb = varLxj.divide(lxIni, ConstantsFunciones.MATH_CONTEXT);
				} else {
					if(!actj363IgualA0){
						BigDecimal varEdadYj = varEdadYini.add(varActj363);
						varLyj = Util.getVarLx(varEdadYj, lstValoresTabMort);
					}//Si varActj363 = 0, no es necesario calcular varLyj. Calculado anteriormente.

					//Se calcula varProb(j) = varLxj/(LxIni )  + umic.rentas.preversion * [(varLyj/(LyIni )) - ((varLyj*varLxj)/(LyIni* LxIni ))]
					BigDecimal divisionLx = varLxj.divide(lxIni, ConstantsFunciones.MATH_CONTEXT);
					BigDecimal divisionLy = varLyj.divide(lyIni, ConstantsFunciones.MATH_CONTEXT);
					BigDecimal divisionLyLx = (varLyj.multiply(varLxj)).divide(multipLxy,ConstantsFunciones.MATH_CONTEXT);

					varProb = divisionLx.add(preversion.multiply((divisionLy).subtract(divisionLyLx)));
				}
				

				//Se realiza el cálculo del sumatorio varRenta = {Sumatorio desde j=periodoProyeccion hasta j= proyUmic.size()-1} varCj * VVida(j) * varProb(j)
				renta = renta.add(varCj.multiply(varVVida).multiply(varProb));

			}// Si varCj = 0, varVVida y varProb se hacen 0, por lo tanto no es necesario añadir el término al sumatorio
			

		}
		
		if (FuncionesRentas.LOG.isTraceEnabled()) {
			FuncionesRentas.LOG.trace("Fin Funcion << renta >> de la clase FuncionesRentas, con resultado renta = {}", renta);
		}

		return renta;
	}
	
	/**
	 * Función que obtiene el valor actual de una renta prepagable. 
	 * @param proyUmic Elemento j de la estructura detalleCorrientes.
	 * @param fcalc Fecha de cálculo.
	 * @param umic Contiene los datos de la Umic que se está procesando.
	 * @param btcUmic Contiene el detalle de la base técnica de cálculo para la umic.
	 * @param codSubproceso Código el subproceso que se está ejecutando.
	 * @param mapVariables
	 * 
	 * @return axN1
	 */
	public static BigDecimal ax_n_1(final List<DetalleCorriente> proyUmic, final Timestamp fcalc, final Umic umic, 
			final DetalleBaseTecnica btcUmic, final String codSubproceso, final Map<String, Object> mapVariables){
		//Variables locales
		BigDecimal axN1 = BigDecimal.ZERO;
		String varCriterioFec = ConstantsFunciones.CTE_CADENA_VACIA;
		String varCriterEdad = ConstantsFunciones.CTE_CADENA_VACIA;
		String varModProb;
		Timestamp varFechaEfecto=null;
		List<BigDecimal> lstValoresTabMort;
		Integer varAnoNac;
		BigDecimal varEdadX;
		BigDecimal varLx;
		BigDecimal varM;
		BigDecimal varDur;
		Integer varN;
		//Fin variables locales
		
		if (FuncionesRentas.LOG.isTraceEnabled()) {
			FuncionesRentas.LOG.trace("Inicio Funcion <<ax_n_1>> de la clase FuncionesRentas, para la entrada proyUmic = {}, fcalc = {}, umic = {}, btcUmic = {}, mapVariables = {} y codSubproceso = {}",
					proyUmic, fcalc, umic, btcUmic, mapVariables, codSubproceso);
		}
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesAuxiliares.validarParamEntrada(proyUmic, fcalc, umic, btcUmic);
		
		//Vaiables de apoyo
		IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
		varCriterEdad = (String) servicio.recuperarDefinicionAuxiliar(umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), ConstantsFunciones.CTE_VA_CRIT_EDA);
		varCriterioFec = (String) servicio.recuperarDefinicionAuxiliar(umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), ConstantsFunciones.CTE_VA_CRIT_FEC);
		
		//Validaciones variables de apoyo
		ValidacionesFuncionesAuxiliares.validarVariableDeApoyoVarCriEdad(varCriterEdad);
		ValidacionesFuncionesAuxiliares.validarVariableDeApoyoVarCriterioFecha(varCriterioFec);
		
		if(btcUmic.getBt().equals(ConstantesSolvencia.BASE_NIIF17) ||
				btcUmic.getBt().equals(ConstantesSolvencia.BASE_NIF17LIR) ||
				btcUmic.getBt().equals(ConstantesSolvencia.BASE_N17LIRIN) ||
				btcUmic.getBt().equals(ConstantesSolvencia.BASE_NIFF17OCI) ||
				btcUmic.getBt().equals(ConstantesSolvencia.BASE_NIIF17IF) ||
				btcUmic.getBt().equals(ConstantesSolvencia.BASE_N17CLIR) ||
				btcUmic.getBt().equals(ConstantesSolvencia.BASE_NF17MFE) ||
				btcUmic.getBt().equals(ConstantesSolvencia.BASE_NF17GTO) ||
				btcUmic.getBt().equals(ConstantesSolvencia.BASE_NF17AEN)){
		varModProb = servicio.recuperarModulo(umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), umic.getDatosAdicionales().getPrestCal(), btcUmic.getBt(), "PROY_VIDA", "02");
		}else{
			varModProb = servicio.recuperarModulo(umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), umic.getDatosAdicionales().getPrestCal(), btcUmic.getBt(), "PROY_VIDA", "02");
	
		}
		Timestamp fecInisusc = umic.getFechas().getFecinisus();
		Integer varEdifer = umic.getDatosGenerales().getEdifer();
		
		if (umic.getDatosGenerales().getCsitupol().equals("AN")){
			if (FuncionesRentas.LOG.isDebugEnabled()) {
				FuncionesRentas.LOG.debug(Util.errorValidacionA7(umic.getIdUmic()));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A7, new String[]{umic.getIdUmic()});
		} else if ((umic.getDatosGenerales().getCsitupol().equals("VI")) ||
//INI-TAR00433819  
				  (umic.getDatosGenerales().getCsitupol().equals("U"))  ||
				  (umic.getDatosGenerales().getCsitupol().equals("F")))		
//FIN-TAR00433819
		
		{
			varFechaEfecto = umic.getFechas().getFecinisus();
		} else if (umic.getDatosGenerales().getCsitupol().equals("RE")){
			varFechaEfecto = umic.getFechas().getFecefecred();
		}
		
		varEdadX = FuncionesAuxiliares.nEdad(fecInisusc, umic.getAsegurados().getFnacAseg1(), varCriterEdad, umic.getRentas().getFecIni(), varEdifer);
		
		if (varModProb.equals(ConstantsFactorias.MODULO_VZCIERTA)){
			lstValoresTabMort = Util.listaProbUno(ConstantsFunciones.CTE_130);
		} else {
			//Se obtiene la Tabla de valores de mortalidad (y todas las variables necesarias para ello)
			varAnoNac = UtilFechas.getAnio(umic.getAsegurados().getFnacAseg1());
			lstValoresTabMort = servicio.recuperarValoresExperiencia(umic, btcUmic, Integer.toString(varAnoNac), umic.getAsegurados().getCsexAseg1(), varEdadX.intValue(), "L", IObtenerConfiguracion.OrdenAsegurado.ASEG1);//Se asume que esta función no se llama en BEL (se tendría que utilizar la edad a fecha efecto)
		}
		
		
		varLx = Util.getVarLx(varEdadX, lstValoresTabMort);
		
		BigDecimal varI1 = btcUmic.getItcalc().get(0);
		BigDecimal varI2 = btcUmic.getItcalc().get(1);
		varM = FuncionesAuxiliares.nAnnos(fecInisusc, umic.getBti().getFecFinTramo1(), varCriterioFec);

		if (umic.getFechas().getFecefecfin() != null){
			varDur = FuncionesAuxiliares.nAnnos(varFechaEfecto, umic.getFechas().getFecefecfin(), varCriterioFec);
		} else {
			varDur = FuncionesAuxiliares.nAnnos(varFechaEfecto, proyUmic.get(proyUmic.size()-1).getFechaDesde(), varCriterioFec);
		}
		varN = (varDur.setScale(0, RoundingMode.CEILING)).intValue();
		
		for (int j=0; j<varN-1; j++){
			
			BigDecimal varXj = varEdadX.add(BigDecimal.valueOf(j));
			BigDecimal varLxJ = Util.getVarLx(varXj, lstValoresTabMort);
			BigDecimal varVpre = FuncionesSecundarias.vpre(j, varM, varI1, varI2);
			
			axN1 = axN1.add((varLxJ.divide(varLx, ConstantsFunciones.MATH_CONTEXT)).multiply(varVpre));

		}
		
		
		if (FuncionesRentas.LOG.isTraceEnabled()) {
			FuncionesRentas.LOG.trace("Fin Funcion << ax_n_1 >> de la clase FuncionesRentas, con resultado axN1 = {}", axN1);
		}
		
		return axN1;
	}
	
	/**
	 * Función que calcula el término AXca. Este término identifica una Renta Actuarial de una cabeza, prepagable, creciente 
	 * en progresión ARITMETICA, desplazada (p), diferida (difer) y de duración M. 
	 * @param difer Diferimiento de la renta.
	 * @param x Edad a fecha de cálculo.
	 * @param p Desplazamiento de la renta.
	 * @param m Número de años en que aplicamos un primer interés técnico desde fecha suscripción.
	 * @param i1 Interés del primer tramo.
	 * @param prp Porcentaje de revalorización de primas.
	 * @param lstValoresTabMort Valores de la tabla de mortalidad.
	 * 
	 * @return axca Renta Actuarial a una cabeza.
	 */
	public static BigDecimal axca(final Integer difer,final Integer x, final Integer p, final Integer m, final BigDecimal i1,  final BigDecimal prp, final List<BigDecimal> lstValoresTabMort) {
		//Inicio variables locales
		BigDecimal axca = BigDecimal.ZERO;
		BigDecimal lXMasJ = BigDecimal.ZERO;
		BigDecimal lXMasP = BigDecimal.ZERO;
		BigDecimal calculoIntermedio = BigDecimal.ZERO;
		int varLimite = 0;
		//Fin variables locales

		if (FuncionesRentas.LOG.isTraceEnabled()) {
			FuncionesRentas.LOG.trace("Inicio Funcion << AXca >> de la clase FuncionesRentas, para la entrada  difer = {}, x = {}, p = {}, m = {}, i1 = {}, prp = {} y lstValoresTabMort = {}", 
					difer, x, p, m, i1, prp, lstValoresTabMort);
		}

		//Validamos los parametros de entrada
		ValidacionesFuncionesRentas.validarParamEntradaFuncionAxcg(x, p, m, i1, difer, prp, lstValoresTabMort);

		//Establecemos por donde empieza a iterar el sumatorio
		if (p.compareTo(difer) < 0) {
			varLimite = difer;
		} else {
			varLimite = p;
		}

		//L(x+p) = valoresTabMort(x+p).wkvalor
		lXMasP = lstValoresTabMort.get(x + p);
		
		final BigDecimal prpDiv100 = prp.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
		final BigDecimal unoVari1Div100 = BigDecimal.ONE.add(i1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));

		//Se calcula el primer término del sumatorio para poder optimizar posteriormente los cálculos del bucle
		BigDecimal operandoPrpDifer = prpDiv100.multiply(BigDecimal.valueOf(varLimite - difer));
		BigDecimal operandoI1P = Util.pow(unoVari1Div100, -(varLimite - p));
		
		if ((x + varLimite)<=lstValoresTabMort.size()-2){
			lXMasJ = lstValoresTabMort.get(x + varLimite); //L(x+j) = valoresTabMort(x+j).wkvalor
			if (lXMasJ.signum()!=0){
				axca = (operandoPrpDifer.add(BigDecimal.ONE)).multiply(operandoI1P).multiply(lXMasJ.divide(lXMasP, ConstantsFunciones.MATH_CONTEXT));
			
			} // Si no, axca = 0
			
		} // Si no, axca = 0 		
		
		//SUMATORIO (j=varLimite) hasta(m-1)
		for (int j = varLimite+1; j < m; j++) {
			//L(x+j) = valoresTabMort(x+j).wkvalor
			if ((x + j)>lstValoresTabMort.size()-2){
				lXMasJ = BigDecimal.ZERO;
				calculoIntermedio = BigDecimal.ZERO;
			} else {
				lXMasJ = lstValoresTabMort.get(x + j);
				if (lXMasJ.signum()==0){
					calculoIntermedio = BigDecimal.ZERO;
				} else {
					// Se calcula el término j del sumatorio: [(1+ prp/100)*((j-difer) )* (1+ I1/100)^(-(j-p))* (l (x+j))/(l(x+p))] 
					operandoPrpDifer = operandoPrpDifer.add(prpDiv100);
					operandoI1P = operandoI1P.divide(unoVari1Div100, ConstantsFunciones.MATH_CONTEXT);
					
					calculoIntermedio = (operandoPrpDifer.add(BigDecimal.ONE)).multiply(operandoI1P).multiply(lXMasJ.divide(lXMasP, ConstantsFunciones.MATH_CONTEXT));
				}
			}

			axca = axca.add(calculoIntermedio);
		}

		if (FuncionesRentas.LOG.isTraceEnabled()) {
			FuncionesRentas.LOG.trace("Fin Funcion << AXca >> de la clase FuncionesRentas, con resultado axca = {} ", axca);
		}

		return axca;
	}
	
	/**
	 * Función que determina el valor de una renta, para una cabeza de edad X al alta, desde el momento T, de duración N, 
	 * creciente de forma geométrica según el porcentaje indicado por PRG. 
	 * @param x Edad actuarial a fecha de efecto.
	 * @param t Anualidad del cálculo.
	 * @param n Duración del seguro.
	 * @param prg Porcentaje de la revalorización geométrica.
	 * @param lstValoresTabMort Valores de la tabla de mortalidad.
	 * @param m Número de años en que aplicamos un primer interés técnico.
	 * @param unoMasI1Entre100 Primer Interés técnico mas 1 entre 100.
	 * @param unoMasI2Entre100 Segundo Interés técnico mas 1 entre 100.
	 * @return rentgeo2it
	 */
	public static BigDecimal rentgeo2it(final BigDecimal x, final Integer t, final Integer n, final BigDecimal prg,
			final List<BigDecimal> lstValoresTabMort, final BigDecimal m, final BigDecimal unoMasI1Entre100, 
			final BigDecimal unoMasI2Entre100, final Map<String, Object> mapVariables){
		//Inicio variables locales
		BigDecimal rentgeo2it = BigDecimal.ZERO;
		BigDecimal varXj;
		BigDecimal varLxj;
		BigDecimal varXT;
		BigDecimal varLxT;
		BigDecimal varPrgj;
		BigDecimal varActVbxj;
		//Fin variables locales	
		
		if (FuncionesRentas.LOG.isTraceEnabled()) {
			FuncionesRentas.LOG.trace("Inicio Funcion << RENTGEO_2IT >> de la clase FuncionesRentas, para la entrada x = {},"
					+" n = {}, prg = {}, lstValoresTabMort = {}, m = {}, unoMasI1Entre100 = {}, unoMasI2Entre100 = {}", 
					x, t, n, prg, lstValoresTabMort, m, unoMasI1Entre100, unoMasI2Entre100);
		}
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesRentas.validarParamEntradaFuncionRentgeo2it(x, t, n, prg, lstValoresTabMort, m, unoMasI1Entre100, unoMasI2Entre100);
		
		//Valores que no cambian en las iteraciones
		BigDecimal unoMasPrgEntre100 = BigDecimal.ONE.add(prg.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		varXT = x.add(BigDecimal.valueOf(t));
		varLxT = Util.getVarLx(varXT, lstValoresTabMort);
		
		/**
		 * Se calcula SEGEO_2IT como:
		 * segeo2it = SUMATORIO desde (j=T) hasta(N-1): (varLxj/varLxT) * varActVbxj * varPrgj 
		 */
		
		//Se calcula el primer valor de varPrgj para optimizar operaciones dentro del bucle
		varPrgj = Util.pow(unoMasPrgEntre100, t);
		
		//SUMATORIO desde (j=T) hasta(N-1)
		for (int j=t; j<n; j++){
			varXj = x.add(BigDecimal.valueOf(j));
			varLxj = Util.getVarLx(varXj, lstValoresTabMort);

			if (j>t){
				varPrgj = varPrgj.multiply(unoMasPrgEntre100);
			}
			
			varActVbxj = FuncionesActualizacionFinanciera.actVbx(BigDecimal.valueOf(t), BigDecimal.valueOf(j), m, unoMasI1Entre100, unoMasI2Entre100, mapVariables);
			
			if (!varLxT.equals(BigDecimal.ZERO)){
				//Se calcula el término j: (varLxj/varLxT) * varActVbxj * varPrgj 
				BigDecimal divisionLx = varLxj.divide(varLxT, ConstantsFunciones.MATH_CONTEXT);
				rentgeo2it = rentgeo2it.add(divisionLx.multiply(varActVbxj).multiply(varPrgj));
				
				//LOG.warn(varLxj+";"+varLxT+";"+divisionLx+";"+varActVbxj+";"+varPrgj+";"+rentgeo2it);
			}
		}
		
		if (FuncionesRentas.LOG.isTraceEnabled()) {
			FuncionesRentas.LOG.trace("Fin Funcion << RENTGEO_2IT >> de la clase FuncionesRentas, con resultado rentgeo2it = {} ", rentgeo2it);
		}
		
		return rentgeo2it;
	}
	
	public static BigDecimal vrtaf(final Integer mesesCompletos, final Integer diferRenta, final Integer duracionRenta, final BigDecimal tipoIPrimerTramo,
			final Integer duracionPrimerTra, final BigDecimal tipoISegundoTramo, 
			final Integer numPagos, final BigDecimal porcenRevalRen, final boolean indUL, final Integer nmeses, 
			final Integer tcm, final Integer ttm, final Integer beta ) {
		//Variables locales
		BigDecimal resultadoVrtaf = BigDecimal.ZERO;
		int varLimiteInferior = 0;
		int varLimiteSuperior = 0;
		int uveDoble = 0;
		BigDecimal operando1 = BigDecimal.ZERO;
		BigDecimal operando2 = BigDecimal.ZERO;
		BigDecimal operando3 = BigDecimal.ZERO;
		boolean operando3Calculado = false;
		//Fin variables locales

		if (FuncionesRentas.LOG.isTraceEnabled()) {
			FuncionesRentas.LOG.trace("Inicio función << vRtaf >> de la clase FuncionesRentas, para la entrada mesesCompletos = {}, diferRenta = {}, duracionRenta = {}, tipoIPrimerTramo = {}, duracionPrimerTra = {}, duracionTramo1 = {}, tipoISegundoTramo = {},  numPagos = {} y porcenRevalRen = {}",
					mesesCompletos, diferRenta, duracionRenta, tipoIPrimerTramo, duracionPrimerTra, tipoISegundoTramo, numPagos, porcenRevalRen);
		}

		//Comprobamos que los campos de la función vienen informados
		ValidacionesFuncionesRentas.validacionCamposFuncionVRtafParte1(mesesCompletos, diferRenta, duracionRenta, tipoIPrimerTramo, duracionPrimerTra);
		ValidacionesFuncionesRentas.validacionCamposFuncionVRtafParte2(tipoISegundoTramo, numPagos, porcenRevalRen);
		ValidacionesFuncionesRentas.validacionCamposFuncionVRtafParte3(tcm, ttm, beta);
		/*
		 * Se calcularán las siguientes variables: 
			varLimInferior
			-	Si k/12 <=d entonces --> varLimInferior =d*12
			-	Si k/12 > d entonces --> varLimInferior = k
			varLimSuperior
			- varLimSuperior = ttm -tcm - beta
		 */

		//varLimInferior
		varLimiteInferior = Util.mult12(diferRenta);
		if (mesesCompletos > varLimiteInferior) {
			varLimiteInferior = mesesCompletos;
		}

		//varLimSuperior
		
		varLimiteSuperior = ttm -tcm - beta;

		// Se convierte a BigDecimal fuera del bucle
		//BigDecimal bdMesesCompletos = new BigDecimal(mesesCompletos);
		//BigDecimal bdEdadActuarial = new BigDecimal(edadActuarial);

		// Los intereses se calculan fuera del bucle
		BigDecimal int1 = BigDecimal.ONE.add(tipoIPrimerTramo.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		BigDecimal int2 = BigDecimal.ONE.add(tipoISegundoTramo.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));

		//base <-- (1+prr/100)
		BigDecimal baseOperando2;
		boolean baseOperando2EsUno = false; 

		if(porcenRevalRen.signum() == 0) {
			baseOperando2 = BigDecimal.ONE;
			operando2 = BigDecimal.ONE;
			baseOperando2EsUno = true;
		} else {
			baseOperando2 = BigDecimal.ONE.add(porcenRevalRen.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		}

		/**
		 *    operando1 --> Probabilidad de que una persona de edad x más k meses, viva t-k meses más. 
		 *    Esta probabilidad se determinará con las tablas a utilizar en cada momento para el cálculo de provisiones y se calculará como:
		 * 		varEdad1 = x + (k/12) 
		 * 		varLxEdad1 = valoresTabMort(varEdad1).wkvalor
		 */
		

		// k <= L*12
		//boolean kMenorOIgualLPor12 = bdMesesCompletos.compareTo(bdDuracionPrimerTra.multiply(ConstantsFunciones.CTE_OPER_12)) <= 0;
		int duracionPrimerTraMeses = Util.mult12(duracionPrimerTra);

		int pp = 0;
		int exponente = 0;
		int exponenteAnt = -1;
		int mesesPago = 12 / numPagos;
		//final BigDecimal int1Incr = Util.pow(int1, BigDecimal.valueOf(mesesPago).multiply(ConstantsFunciones.CTE_OPER_1ENTRE12, ConstantsFunciones.MATH_CONTEXT).negate());
		final BigDecimal int2Incr = Util.pow(int2, BigDecimal.valueOf(mesesPago).multiply(ConstantsFunciones.CTE_OPER_1ENTRE12, ConstantsFunciones.MATH_CONTEXT).negate());
		BigDecimal calculo2 = BigDecimal.ZERO;
		//Se calculará Vrta como el sumatorio en t desde t= varLimInferior +1  hasta t= varLimSuperior
		for (int t = varLimiteInferior + 1 ; t <= varLimiteSuperior; t++) {
			
			Integer exp = (tcm + t) / 12;
			Integer exp2 = tcm/12;
			Integer exp3 = exp - exp2;
			
			BigDecimal calculo1 = Util.pow(baseOperando2, exp3);
			
			BigDecimal elevado = (new BigDecimal(t).divide(new BigDecimal(12),ConstantsFunciones.MATH_CONTEXT)).negate();
			
			//int aux = t / 12;
			
			if(t <= duracionPrimerTraMeses){
				calculo2 =  Util.pow(int1, elevado);
			}else{
				calculo2 = Util.pow(int2, elevado);	
			}	
			resultadoVrtaf = resultadoVrtaf.add(calculo1.multiply(calculo2, ConstantsFunciones.MATH_CONTEXT));
		}

		if (FuncionesRentas.LOG.isTraceEnabled()) {
			FuncionesRentas.LOG.trace("Fin función << vRtad >> de la clase FuncionesRentas, con resultado resultadoVrta = {}",resultadoVrtaf);
		}

		return resultadoVrtaf;
	}

}
