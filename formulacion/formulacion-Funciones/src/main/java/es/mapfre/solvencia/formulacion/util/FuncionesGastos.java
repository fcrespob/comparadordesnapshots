

package es.mapfre.solvencia.formulacion.util;

/**MODIFICACION: TAR00433819
//FECHA: 15/01/2019
//DESCRIP: Se incluye el tratamiento para los nuevos estados de prorrogas U,F, que se tienen que comportar como VI.
*/
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;

public class FuncionesGastos {
	
	/** 
	 * Log funciones de gastos.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(FuncionesGastos.class);
	
	/**
	 * GASTGIVITINI = äx
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
	 * @return resultadoGastgivitini
	 */
	public static BigDecimal gastgivitini(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso,
			final BigDecimal varI1PorcentajeMasUno, final BigDecimal varI2PorcentajeMasUno) {
		// Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		// Fin variables locales

		if (FuncionesGastos.LOG.isTraceEnabled()) {
			FuncionesGastos.LOG.trace("Inicio Funcion << gastgivitini >> de la clase FuncionesGastos, para la entrada proyUmic = {}, bloqueCorriente = {}, iteracion = {}, fcalc = {}, umic = {}, btcUmic = {}, mapVariables = {} y codSubproceso = {}",
					proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
		}

		String tempVit = umic.getRentas().getTempVit();
		if (tempVit.equals(ConstantsFunciones.CTE_RENTA_VITALICIA)){
			resultado = FuncionesSecundarias.aSubX(proyUmic, bloqueCorriente, umic.getFechas().getFecinisus(), umic, btcUmic, mapVariables, codSubproceso);
		}else if (tempVit.equals(ConstantsFunciones.CTE_RENTA_TEMPORAL)){
			resultado = FuncionesActualizacionFinanciera.aSubXD(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
		}else{
			if (FuncionesGastos.LOG.isDebugEnabled()) {
				FuncionesGastos.LOG.debug(Util.errorValidacionA2(tempVit, ConstantsFunciones.CTE_TEMP_VIT));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A2, new String[]{ConstantsFunciones.CTE_TEMP_VIT, tempVit}); 
		}

		if (FuncionesGastos.LOG.isTraceEnabled()) {
			FuncionesGastos.LOG.trace("Fin Funcion << gastgivitini >> de la clase FuncionesGastos, con resultado  = {} ", resultado);
		}

		return resultado;
	}

	/**
	 *                                            
	 *	GASTGIVIT(fcal) = (PARTAÑO(NR)-Ry) +  (lxREN /lzc) * VVIDA(fcal,REN) * äXREN
	 *                                               
	 *
	 * @param fcal Fecha de cálculo
	 * @param fecIniSusc Fecha de inicio de suscripción
	 * @param fecProxRenova Fecha de próxima renovación
	 * @param fecJ Fecha de devengo del periodo J
	 * @param partAnoNR Fracción de año que hay entre la renovación anterior NR y posterior (NR+1)
	 * @param varRY Fracción de año incompleto entre la anterior fecha de renovación y la fecha de cálculo fcal
	 * @param ren Período de tiempo transcurrido desde la fecha de alta del ajuste hasta la fecha de renovación siguiente al momento de cálculo
	 * @param numi1 Primer Interés técnico
	 * @param numi2 Segundo Interés técnico
	 * @param numm Número de años en que aplicamos un primer interés técnico desde fecha suscripción
	 * @param varTCY Resultado de la función naños() en base al criterio establecido entre la fecha de efecto de la suscripción y la fecha de cálculo
	 * @param criterFec Criterio de Fechas para el cálculo
	 * @param lstValoresTabMort Valores de la tabla de mortalidad
	 * @param varw Edad Máxima de la tabla de mortalidad
	 * @param varx Edad del asegurado a Fecha de cálculo
	 * @return resultadoGastgivit
	 */
	public static BigDecimal gastgivit(
			final Timestamp fcal, final Timestamp fecIniSusc, final Timestamp fecProxRenova, final Timestamp fecJ,
			final BigDecimal partAnoNR, final BigDecimal varRY, final BigDecimal ren, final BigDecimal numi1, final BigDecimal numi2, final BigDecimal numm,
			final BigDecimal varTCY, final String criterFec, final List<BigDecimal> lstValoresTabMort, final BigDecimal varw, final BigDecimal varx, final BigDecimal varZc,
			final BigDecimal numi1PorcentajeMasUno, final BigDecimal numi2PorcentajeMasUno) {

		// Variables locales
		BigDecimal gastgivit = BigDecimal.ZERO;
		BigDecimal varXRen = BigDecimal.ZERO;
		BigDecimal varVVida = BigDecimal.ZERO;
		BigDecimal varVprer = BigDecimal.ZERO;
		BigDecimal varLxRenEntero = BigDecimal.ZERO;
		BigDecimal varLxRenEntero1 = BigDecimal.ZERO;
		BigDecimal varLxRen = BigDecimal.ZERO;
		BigDecimal varLzcEntero = BigDecimal.ZERO;
		BigDecimal varLzcEntero1 = BigDecimal.ZERO;
		BigDecimal varLzc = BigDecimal.ZERO;
		BigDecimal varLxRenMasj = BigDecimal.ZERO;
		BigDecimal varLxRenJEntero = BigDecimal.ZERO;
		BigDecimal varLxRenJEntero1 = BigDecimal.ZERO;
		BigDecimal varAsubXRen = BigDecimal.ZERO;
		// Fin variables locales

		if (FuncionesGastos.LOG.isTraceEnabled()) {
			FuncionesGastos.LOG.trace("Inicio Funcion << gastgivit >> de la clase FuncionesGastos, para la entrada " +
					"fcal = {}, fecIniSusc = {}, fecProxRenova = {}, fecJ = {}, partAnoNR = {}, varRY = {}, ren = {}, numi1 = {}, numi2 = {}, numm = {}, varTCY = {}, criterFec = {}, lstValoresTabMort = {}, varw = {} y varx = {} ",
					fcal, fecIniSusc, fecProxRenova, fecJ, partAnoNR, varRY, ren, numi1, numi2, numm, varTCY, criterFec, lstValoresTabMort, varw, varx);
		}

		// Se validan los parámetros de entrada marcados como obligatorios
		ValidacionesFuncionesGastos.validarParamEntradaFuncionGastgivitParte1(fcal, fecIniSusc, fecProxRenova, fecJ, partAnoNR, varRY, ren);
		ValidacionesFuncionesGastos.validarParamEntradaFuncionGastgivitParte2(numi1, numi2, numm, varTCY, criterFec, lstValoresTabMort, varw);
		ValidacionesFuncionesGastos.validarParamEntradaFuncionGastgivitParte3(varx);

		/**
		 * Se calcularán las siguientes variables: 
		 * - VarTc0 = TC(fecIniSusc, fcalc)
		 * - varJ = VarTc0 + nanos(fcalc, fecJ, CriterFec)
		 * - VarZc = X + nanos(fecIniSusc, fecJ, CriterFec)
		 * - VarXRen = X + nanos(fcalc, fecProxRenova, CriterFec)
		 * - varVVida = VVIDA(Ren, I1, I2, M, varTCY)
		 */

		varXRen = varx.add(ren);
		varVVida = FuncionesActualizacionFinanciera.vVida(ren, numi1, numi2, numm, varTCY, numi1PorcentajeMasUno, numi2PorcentajeMasUno);	

		/**
		 * Si ZC < w 
		 * 
		 * -	Si   VarXRen  < W
		 * 			o	varLxRenEntero= valoresTabMort(ENTERO(VarXRen)).wkvalor
						o	varLxRenEntero1= valoresTabMort(ENTERO(VarXRen)+1).wkvalor 
						o	varLxRen = varLxRenEntero + (ParteDecimal(VarXRen) )* (varLxRenEntero1– varLxRenEntero)
				-	Si   VarXRen  >= w
						o	varLxRen = 1
		 * 
		 * 		 	        w-varXRen-1
		 * - varÄxREN = SUMATORIO    [(varLxRen + j) / varLxRen] * varVprer 
		 *                  j=0

					Donde en cada término del sumatorio se obtendrá varLxRen+j como: 
						Si   VarXRen  + 1 < W
							varLxRenJEntero= valoresTabMort(ENTERO(VarXRen +j)).wkvalor
							varLxRenJEntero1= valoresTabMort(ENTERO(VarXRen +j)+1).wkvalor 
							varLxRen+j = varLxRenJEntero + (ParteDecimal(VarXRen +j) )* (varLxRenJEntero1– varLxRenJEntero)

						Si   VarXRen  + 1 >= w
							varLxRen+j = 0
					Finalmente se obtendrá gastgivit como: 
						varLzcEntero= valoresTabMort(ENTERO(Zc)).wkvalor
						varLzcEntero1= valoresTabMort(ENTERO(Zc)+1).wkvalor 
						varLzc = varLzcEntero + (ParteDecimal(Zc) )* (varLzcEntero1 – varLzcEntero)

		 */		


		if (varZc.compareTo(varw) < 0) {
			if (varXRen.compareTo(varw) < 0) {
				int xRen = varXRen.intValue();
				if (xRen > lstValoresTabMort.size()-2){
					varLxRen = BigDecimal.ZERO;
				} else {
					varLxRenEntero = lstValoresTabMort.get(xRen);
					varLxRenEntero1 = lstValoresTabMort.get(xRen + 1);
					varLxRen = Util.interpolaPorEdad(varLxRenEntero, varLxRenEntero1, varXRen);
				}
			}

			if(varLxRen.signum()==0){
				gastgivit = partAnoNR.subtract(varRY);
			} else {

				final int iteracionMaxima = varw.subtract(varXRen.setScale(0, RoundingMode.FLOOR)).intValue();
				final BigDecimal varXRenMasUno = varXRen.add(BigDecimal.ONE);

				for (int j = 0; j < iteracionMaxima; j++) {

					/**
					 * - varVprer:
					 * 		Si varJ < (M - Ren):
					 * 			varVprer = (1 + (I1 * 0.01)) ^ (-J)
					 * 		Si Ren < M y varJ >= (M - Ren):
					 * 			varVprer = ( (1 + (I1 * 0.01)) ^ -(M - REN) ) * ( (1 + I2) ^ -(J - M + REN) )
					 * 		Si varRen >= M:
					 * 			varVprer =(1 + (I2 * 0.01)) ^ -(J)
					 */
					final BigDecimal nummMenosRen = numm.subtract(ren);
					final BigDecimal bdJ = BigDecimal.valueOf(j);


					if (bdJ.compareTo(nummMenosRen) < 0) {
						varVprer = Util.pow(numi1PorcentajeMasUno, bdJ.negate());
					} else if (ren.compareTo(numm) < 0 && bdJ.compareTo(nummMenosRen) >= 0) {
						final BigDecimal operando1 = Util.pow(numi1PorcentajeMasUno, nummMenosRen.negate());
						final BigDecimal operando2 = Util.pow(numi2PorcentajeMasUno, (bdJ.subtract(numm).add(ren)).negate());
						varVprer = operando1.multiply(operando2);
					} else if (ren.compareTo(numm) >= 0) {
						varVprer = Util.pow(numi2PorcentajeMasUno, bdJ.negate());
					}


					if (varXRenMasUno.compareTo(varw) < 0) {
						final BigDecimal varXRenMasJ = varXRen.add(bdJ);
						final int xRenMasJ = varXRenMasJ.intValue();
						if (xRenMasJ > lstValoresTabMort.size()-2){
							varLxRenMasj = BigDecimal.ZERO;
						} else {
							varLxRenJEntero = lstValoresTabMort.get(xRenMasJ);
							varLxRenJEntero1 = lstValoresTabMort.get(xRenMasJ+ 1);
							varLxRenMasj = Util.interpolaPorEdad(varLxRenJEntero, varLxRenJEntero1, varXRenMasJ);
						}

					}


					varAsubXRen = varAsubXRen.add(varLxRenMasj.divide(varLxRen, ConstantsFunciones.MATH_CONTEXT).multiply(varVprer));
					
				}


				/**
				 *  -	varLzcEntero= valoresTabMort(ENTERO(Zc)).wkvalor
					-	varLzcEntero1= valoresTabMort(ENTERO(Zc)+1).wkvalor 
					-	varLzc = varLzcEntero + (ParteDecimal(Zc) )* (varLzcEntero1 – varLzcEntero)
				 */
				final int zc = varZc.intValue();
				if (zc > lstValoresTabMort.size()-2){
					varLzc = BigDecimal.ZERO;
				} else {
					varLzcEntero = lstValoresTabMort.get(zc);
					varLzcEntero1 = lstValoresTabMort.get(zc + 1);
					varLzc = Util.interpolaPorEdad(varLzcEntero, varLzcEntero1, varZc);
				}

				if (varLzc.signum()==0){
					gastgivit = partAnoNR.subtract(varRY);
				} else {
					/**
					 * Finalmente se obtendrá gastgivit como:
					 * gastgivit = (PartAnoNR - varRY) + [ ( (varLxRen) / (varLzc) ) * varVVida * varÄxREN ]
					 */
					gastgivit = partAnoNR.subtract(varRY).add(varLxRen.divide(varLzc, ConstantsFunciones.MATH_CONTEXT).multiply(varVVida).multiply(varAsubXRen));
				}
			}

		} else {
			/**
			 * Si ZC >= w  
			 * 		gastgivit = (PartAnoNR - varRY)
			 */
			gastgivit = partAnoNR.subtract(varRY);
		}


		if (FuncionesGastos.LOG.isTraceEnabled()) {
			FuncionesGastos.LOG.trace("Fin Funcion << gastgivit >> de la clase FuncionesVBX, con resultado: gastgivit = {} ", gastgivit);
		}

		return gastgivit;
	}

	/**
	 * Función que calcula el término GASTGI de la formulación de la corriente de Provisión Matemática por fórmula cerrada.
	 * @param fcalc Fecha de cálculo.
	 * @param fecIniSusc Fecha de inicio de suscripción.
	 * @param fecJ Fecha de devengo del periodo J.
	 * @param partAnoNR Fracción de año que hay entre la renovación anterior NR y posterior (NR+1).
	 * @param ry Fracción de año incompleto entre la anterior fecha de renovación y la fecha de cálculo fcal.
	 * @param ren Período de tiempo transcurrido desde la fecha de alta del ajuste hasta la fecha de renovación siguiente al momento de cálculo.
	 * @param i1 Primer Interés técnico.
	 * @param i2 Segundo Interés técnico.
	 * @param m Número de años en que aplicamos un primer interés técnico desde fecha suscripción.
	 * @param tcy Resultado de la función naños() en base al criterio establecido entre la fecha de efecto de la suscripción y la fecha de cálculo.
	 * @param criterFec Criterio de Fechas para el cálculo.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param w Edad Máxima de la tabla de mortalidad.
	 * @param x Edad del asegurado a Fecha de suscripción.
	 * @param zc Edad del asegurado a Fecha de cálculo.
	 * @param nDap Número de años completos que quedan pendientes desde la siguiente renovación a la fecha de cálculo hasta el vencimiento.
	 * @param nDmv Fracción de año comprendida entre la fecha de renovación de la umic inmediata anterior a la fecha de vencimiento del certificado y dicha fecha de vencimiento.
	 * @param numi1PorcentajeMasUno
	 * @param numi2PorcentajeMasUno
	 * 
	 * @return gastgi
	 */
	public static BigDecimal gastgi(final Timestamp fcalc, final Timestamp fecIniSusc, final Timestamp fecJ, final BigDecimal partAnoNR, final BigDecimal ry,
			final BigDecimal ren, final BigDecimal i1, final BigDecimal i2, final BigDecimal m, final BigDecimal tcy, final String criterFec, 
			final List<BigDecimal> valoresTabMort, final BigDecimal w, final BigDecimal x, final BigDecimal zc, final Integer nDap, final BigDecimal nDmv, final BigDecimal numi1PorcentajeMasUno, final BigDecimal numi2PorcentajeMasUno){
		//Variables locales
		BigDecimal gastgi = BigDecimal.ZERO;
		Integer varTc0;
		BigDecimal varJ;
		BigDecimal varXRen;
		BigDecimal varXRenNDap;
		BigDecimal varXTCy;
		BigDecimal varVVidaRen;
		BigDecimal varVVidaNDapRen;
		BigDecimal varLxRen = BigDecimal.ZERO;
		BigDecimal varLxTCy = BigDecimal.ZERO;
		BigDecimal varLxRenNDap = BigDecimal.ZERO;
		BigDecimal varAxRenNDap = BigDecimal.ZERO;
		BigDecimal varVprer = BigDecimal.ZERO;
		//Fin variables locales
		
		if (FuncionesGastos.LOG.isTraceEnabled()) {
			FuncionesGastos.LOG.trace("Inicio Funcion <<gastgi>> de la clase FuncionesGastos, para la entrada fcalc = {}, fecIniSusc = {},"
					+ " fecJ = {}, partAnoNR = {}, ry = {}, ren = {}, i1 = {}, i2 = {}, m = {}, tcy = {}, criterFec = {}, valoresTabMort = {}, w = {}"
					+ " x = {}, zc = {}, nDap = {}, nDmv = {}",
					fcalc, fecIniSusc, fecJ, partAnoNR, ry, ren, i1, i2, m, tcy, criterFec, valoresTabMort, w, x, zc, nDap, nDmv);
		}
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesGastos.validarParamEntradaFuncionGastgi1(fcalc, fecIniSusc, fecJ, partAnoNR, ry, ren, i1, i2, m);
		ValidacionesFuncionesGastos.validarParamEntradaFuncionGastgi2(tcy, criterFec, valoresTabMort, w, x, zc, nDap, nDmv);
		
		
		BigDecimal nDapBD = BigDecimal.valueOf(nDap);
		
		varTc0 = FuncionesAuxiliares.tc(fecIniSusc, fcalc);
		varJ = BigDecimal.valueOf(varTc0).add(FuncionesAuxiliares.nAnnos(fcalc, fecJ, criterFec));
		varXRen = x.add(ren);
		varXRenNDap = varXRen.add(nDapBD);
		varXTCy = x.add(tcy);
		varVVidaRen = FuncionesActualizacionFinanciera.vVida(ren, i1, i2, m, tcy, numi1PorcentajeMasUno, numi2PorcentajeMasUno);
		varVVidaNDapRen = FuncionesActualizacionFinanciera.vVida(ren.add(nDapBD), i1, i2, m, tcy, numi1PorcentajeMasUno, numi2PorcentajeMasUno);
		
		if (zc.compareTo(w) < 0){
			if (varXRen.compareTo(w) < 0){
				varLxRen = Util.getVarLx(varXRen, valoresTabMort);
			} //Si varXRen >= w -> varLxRen=0
			
			if (varXTCy.compareTo(w) < 0){
				varLxTCy = Util.getVarLx(varXTCy, valoresTabMort);
			} //Si varXTCy >= 0 -> varLxTCy=0
			
			if (varXRenNDap.compareTo(w) < 0){
				varLxRenNDap = Util.getVarLx(varXRenNDap, valoresTabMort);
			} //Si varXRenNDap >= w  -> varLxRenNDap=0
			
			//Integer limitesuperior = w.subtract(varXRenNDap.setScale(0, RoundingMode.FLOOR)).intValue();
			Integer limitesuperior = nDap-1;
			BigDecimal varLxRenMasJ;
			for (int j = 0; j<=limitesuperior; j++){
				varVprer = FuncionesSecundarias.vprer(varJ, m, numi1PorcentajeMasUno, numi2PorcentajeMasUno, ren, j);
				BigDecimal varMasUno = varXRen.add(BigDecimal.valueOf(j));
				if (varMasUno.compareTo(w) < 0){
					varLxRenMasJ = Util.getVarLx(varMasUno, valoresTabMort);
				} else {
					varLxRenMasJ = BigDecimal.ZERO;
				}
				
				BigDecimal division = varLxRenMasJ.divide(varLxRen, ConstantsFunciones.MATH_CONTEXT);
				varAxRenNDap = varAxRenNDap.add(division.multiply(varVprer));
				
			}
			
			//Se calcula gastgi = (PartAnoNR-Ry)+[(varLxRen /varLxTCy )* varVVidaRen * varÄxRenNDap ] +  [(varLxRenNDap /varLxTCy )* NDmv * varVVidaNDapRen  ]
			BigDecimal operador1 = (varLxRen.divide(varLxTCy, ConstantsFunciones.MATH_CONTEXT)).multiply(varVVidaRen).multiply(varAxRenNDap);
			BigDecimal operador2 = (varLxRenNDap.divide(varLxTCy, ConstantsFunciones.MATH_CONTEXT).multiply(nDmv).multiply(varVVidaNDapRen));
			gastgi = (partAnoNR.subtract(ry)).add(operador1).add(operador2);
			
			
		} else {
			//Se calcula gastgi = PartAnoNR - Ry
			gastgi = partAnoNR.subtract(ry);
		}
		
		if (FuncionesGastos.LOG.isTraceEnabled()) {
			FuncionesGastos.LOG.trace("Fin Funcion << gastgi >> de la clase FuncionesGastos, con resultado gastgi = {}", gastgi);
		}
		
		return gastgi;
	}

	
	/**
	 * Función que obtiene un  factor de actualización financiera probabilizado para un periodo j.
	 * @param proyUmic Elemento j de la estructura detalleCorrientes.
	 * @param fcalc Fecha de cálculo.
	 * @param umic Contiene los datos de la Umic que se está procesando.
	 * @param btcUmic Contiene el detalle de la base técnica de cálculo para la umic.
	 * @param codSubproceso Código el subproceso que se está ejecutando.
	 * @param mapVariables
	 * 
	 * @return gastgini
	 */
	public static BigDecimal gastgini(final List<DetalleCorriente> proyUmic, final Timestamp fcalc, final Umic umic, 
			final DetalleBaseTecnica btcUmic, final String codSubproceso, final Map<String, Object> mapVariables,
			final BigDecimal vari1PorcentajeMasUno, final BigDecimal vari2PorcentajeMasUno){
		//Variables locales
		BigDecimal gastgini = BigDecimal.ZERO;
		String varCriterioFec = ConstantsFunciones.CTE_CADENA_VACIA;
		String varCriterEdad = ConstantsFunciones.CTE_CADENA_VACIA;
		final IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
		List<BigDecimal> lstValoresTabMort;
		Timestamp varFechaEfecto = null;
		BigDecimal varDur;
		Integer varn = 0;
		BigDecimal varM;
		BigDecimal varX;
		BigDecimal varLx;
		BigDecimal varVVida;
		BigDecimal varAxN1;
		//Fin variables locales
		
		if (FuncionesGastos.LOG.isTraceEnabled()) {
			FuncionesGastos.LOG.trace("Inicio Funcion <<gastgini>> de la clase FuncionesGastos, para la entrada proyUmic = {}, fcalc = {}, umic = {}, btcUmic = {}, mapVariables = {} y codSubproceso = {}",
					proyUmic, fcalc, umic, btcUmic, mapVariables, codSubproceso);
		}
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesAuxiliares.validarParamEntrada(proyUmic, fcalc, umic, btcUmic);
		
		//Variables de apoyo
		varCriterEdad = (String) servicio.recuperarDefinicionAuxiliar(umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), ConstantsFunciones.CTE_VA_CRIT_EDA);
		varCriterioFec = (String) servicio.recuperarDefinicionAuxiliar(umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), ConstantsFunciones.CTE_VA_CRIT_FEC);
		
		//Validaciones variables de apoyo
		ValidacionesFuncionesAuxiliares.validarVariableDeApoyoVarCriEdad(varCriterEdad);
		ValidacionesFuncionesAuxiliares.validarVariableDeApoyoVarCriterioFecha(varCriterioFec);

		
		if (umic.getDatosGenerales().getCsitupol().equals("AN")){
			if (FuncionesGastos.LOG.isDebugEnabled()) {
				FuncionesGastos.LOG.debug(Util.errorValidacionA7(umic.getIdUmic()));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A7, new String[]{umic.getIdUmic()});
//INI-TAR00433819 
		} else if ((umic.getDatosGenerales().getCsitupol().equals("VI")) || 
				   (umic.getDatosGenerales().getCsitupol().equals("U"))  ||
				   (umic.getDatosGenerales().getCsitupol().equals("F")))		
//FIN-TAR00433819
		{
			varFechaEfecto = umic.getFechas().getFecinisus();
		} else if (umic.getDatosGenerales().getCsitupol().equals("RE")){
			varFechaEfecto = umic.getFechas().getFecefecred();
		}
		
		if (umic.getFechas().getFecefecfin() != null){
			varDur = FuncionesAuxiliares.nAnnos(varFechaEfecto, umic.getFechas().getFecefecfin(), varCriterioFec);
		} else {
			varDur = FuncionesAuxiliares.nAnnos(varFechaEfecto, proyUmic.get(proyUmic.size()-1).getFechaDesde(), varCriterioFec);
		}
		
		Timestamp fecInisusc = umic.getFechas().getFecinisus();
		varn = (varDur.setScale(0, RoundingMode.CEILING)).intValue();
		varM = FuncionesAuxiliares.nAnnos(fecInisusc, umic.getBti().getFecFinTramo1(), varCriterioFec);
		BigDecimal i1 = btcUmic.getItcalc().get(0);
		BigDecimal i2 = btcUmic.getItcalc().get(1);
		Integer eDifer = umic.getDatosGenerales().getEdifer();

		varX = FuncionesAuxiliares.nEdad(fecInisusc, umic.getAsegurados().getFnacAseg1(), varCriterEdad, umic.getRentas().getFecIni(), eDifer);
		//Se obtiene la Tabla de valores de mortalidad (y todas las variables necesarias para ello)
		Integer varAnoNac = UtilFechas.getAnio(umic.getAsegurados().getFnacAseg1());
		lstValoresTabMort = servicio.recuperarValoresExperiencia(umic, btcUmic, Integer.toString(varAnoNac), umic.getAsegurados().getCsexAseg1(), varX.intValue(), "L", IObtenerConfiguracion.OrdenAsegurado.ASEG1); //Se asume que esta función no se llama en BEL (se tendría que utilizar la edad a fecha efecto)
		
		
		
		varLx = Util.getVarLx(varX, lstValoresTabMort);
		
		BigDecimal varXn1 = varX.add(BigDecimal.valueOf(varn-1));
		BigDecimal varLxn1 = Util.getVarLx(varXn1, lstValoresTabMort);
		
		varVVida = FuncionesActualizacionFinanciera.vVida(BigDecimal.valueOf(varn-1), i1, i2, varM, BigDecimal.ZERO, vari1PorcentajeMasUno, vari2PorcentajeMasUno);

		varAxN1 = FuncionesRentas.ax_n_1(proyUmic, fcalc, umic, btcUmic, codSubproceso, mapVariables);
		
		BigDecimal operando1= varDur.subtract(BigDecimal.valueOf(varn-1));
		gastgini = varAxN1.add(operando1.multiply(varLxn1.divide(varLx, ConstantsFunciones.MATH_CONTEXT)).multiply(varVVida));
		
		if (FuncionesGastos.LOG.isTraceEnabled()) {
			FuncionesGastos.LOG.trace("Fin Funcion << gastgini >> de la clase FuncionesGastos, con resultado gastgini = {}", gastgini);
		}
		
		return gastgini;
	}

	/**
	 * Función que calcula este término de la formulación de la corriente de Provisión Matemática por fórmula cerrada.
	 * @param renta
	 * @param Gi gastos sobre la renta.
	 * @param primaUnica Prima única Inicial.
	 * @param gipc gastos sobre la prima comercial.
	 * @param gastgivit
	 * @param gasgivitini
	 * @return gasto Componente de gastos de la provisión matemática.
	 */
	public static BigDecimal gasto(final BigDecimal renta, final BigDecimal gi, final BigDecimal primaUnica, final BigDecimal gipc,
			final BigDecimal gastgivit, final BigDecimal gasgivitini){
		//Variables locales
		BigDecimal gasto= BigDecimal.ZERO;
		//Fin variables locales

		if (FuncionesGastos.LOG.isTraceEnabled()) {
			FuncionesGastos.LOG.trace("Inicio Funcion <<gasto>> de la clase FuncionesGastos, para la entrada renta = {}, Gi = {}, primaUnica = {}, gipc = {}, gastgivit = {}, gasgivitini = {} ",
					renta, gi, primaUnica, gipc, gastgivit, gasgivitini);
		}

		//Validaciones parámetros obligatorios de entrada
		ValidacionesFuncionesGastos.validarParamEntradaFuncionGasto(renta, gi, primaUnica, gipc, gastgivit, gasgivitini);

		// Se realiza el cálculo de la fórmula GASTO = renta * gi + (primaUnica * gipc  * GASTGIVIT/GASTGIVITINI )
		BigDecimal primerOperando = renta.multiply(gi);
		BigDecimal division = gastgivit.divide(gasgivitini, ConstantsFunciones.MATH_CONTEXT);
		BigDecimal segundoOperando = primaUnica.multiply(gipc).multiply(division);

		gasto = primerOperando.add(segundoOperando);

		if (FuncionesGastos.LOG.isTraceEnabled()) {
			FuncionesGastos.LOG.trace("Fin Funcion << gasto >> de la clase FuncionesGastos, con resultado gasto = {}", gasto);
		}

		return gasto;
	}
	
	/**
	 * Función que calcula el Movimiento del gasto de gestión
	 * @param t Perido mensual de proyección 
	 * @param ttm Numero de meses transcurridos desde el efecto de la fecha de vencimiento
	 * @param diaVto Día de vencimiento
	 * @param ggim Gasto sobre la prima (mensualizado)
	 * @param diaprima Día de pago de la prima 
	 * @param bxAnterior Saldo inicial del periodo
	 * @return gast Componente de gastos sobre el fondo
	 */
	public static BigDecimal gast(final Integer t, final Integer ttm, final Integer diaVto, final BigDecimal ggim, final Integer diaprima, final BigDecimal bxAnterior) {
		
		//Inicio variables locales
		BigDecimal vard1 = BigDecimal.ONE;
		BigDecimal gast;
		//Fin variables locales
		
		if (FuncionesGastos.LOG.isTraceEnabled()) {
			FuncionesGastos.LOG.trace("Inicio Funcion << GAST >> de la clase FuncionesGastos, para la entrada t = {}, ttm = {}, diaVto = {}, ggim = {},"
					+ " diaprima = {}, bxAnterior = {} ",
					t, ttm, diaVto, ggim, diaprima, bxAnterior);
		}
		
		//Validaciones parámetros obligatorios de entrada
		ValidacionesFuncionesGastos.validarParamEntradaFuncionGast(t, ttm, diaVto, ggim, diaprima, bxAnterior);
		
		
		if (t == ttm) {
			vard1 = BigDecimal.valueOf(diaVto).divide(ConstantsFunciones.CTE_OPER_30_PUNTO_5,ConstantsFunciones.MATH_CONTEXT);
		}
		
		// Se calcula la función gast como: GAST  = bxAnterior * ggim * vard1   
		gast = bxAnterior.multiply(ggim).multiply(vard1);
		

		if (FuncionesGastos.LOG.isTraceEnabled()) {
			FuncionesGastos.LOG.trace("Fin Funcion << GAST >> de la clase FuncionesGastos, con resultado gast = {}", gast);
		}
		
		return gast;
		
	}

	/**
	 * Función que calcula el Movimiento de gastos de prima
	 * @param t Periodo mensual de proyección
	 * @param ttm Número de meses transcurridos desde el efecto a la fecha de vencimiento
	 * @param diaVto Día de vencimiento 
	 * @param ggim Gastos sobre la prima (mensualizado)
	 * @param diaprima Día de pago de la prima 
	 * @param prima
	 * @return gastp Componente de gastos de la prima
	 */
	public static BigDecimal gastp(final Integer t, final Integer ttm, final Integer diaVto, final BigDecimal ggim, final Integer diaprima, final BigDecimal prima) {
		
		//Inicio variables locales
		BigDecimal gastp = BigDecimal.ZERO;
		BigDecimal vard1;
		//Fin variables locales
		
		if (FuncionesGastos.LOG.isTraceEnabled()) {
			FuncionesGastos.LOG.trace("Inicio Funcion << GASTP >> de la clase FuncionesGastos, para la entrada t = {}, ttm = {}, diaVto = {},"
					+ " ggim = {}, diaprima = {}, prima = {} ",
					t, ttm, diaVto, ggim, diaprima, prima);
		}
		
		//Validaciones parámetros obligatorios de entrada
		ValidacionesFuncionesGastos.validarParamEntradaFuncionGastp(t, ttm, diaVto, ggim, diaprima, prima);
		
		// Si t != ttm se calcula la función gast como: GASTP = prima * ggim * vard1
		// otro caso GASTP = 0
		if (t != ttm) {
			vard1 = (ConstantsFunciones.CTE_OPER_30_PUNTO_5.subtract(BigDecimal.valueOf(diaprima))).divide(ConstantsFunciones.CTE_OPER_30_PUNTO_5,ConstantsFunciones.MATH_CONTEXT);
			
			gastp = prima.multiply(ggim).multiply(vard1);			
		}		
		
		if (FuncionesGastos.LOG.isTraceEnabled()) {
			FuncionesGastos.LOG.trace("Fin Funcion << GASTP >> de la clase FuncionesGastos, con resultado gastp = {}", gastp);
		}
		
		return gastp;		
	}
	
	
}
