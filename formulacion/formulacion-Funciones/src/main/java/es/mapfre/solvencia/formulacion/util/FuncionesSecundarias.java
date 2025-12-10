package es.mapfre.solvencia.formulacion.util;

import java.math.BigDecimal;
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
import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

public class FuncionesSecundarias {
	
	/** 
	 * Log funciones secundarias.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(FuncionesSecundarias.class);
	
	/**
	 * Funcion que añade la edadMaxima a los años de una fecha.
	 * 
	 * @param fnac
	 *            Identifica la fecha de nacimiento del asegurado
	 * @param edadMax
	 *            Identifica la edad maxima de tabla de mortalidad
	 * @return Identifica la fecha de fin de la prestacion vitalicia
	 */
	public static Timestamp ffInvitalicia(final Timestamp fnac, final Integer edadMax) {
		// Variables locales
		Timestamp ffInproy;
		// Fin variables locales

		if (FuncionesSecundarias.LOG.isTraceEnabled()) {
			FuncionesSecundarias.LOG.trace("Inicio de la Funcion << ffInvitalicia >> de la clase FuncionesSecundarias, para la entrada fnac = {} y edadMax = {}", fnac, edadMax);

		}
		// INICIO VALIDACION CAMPOS OBLIGATORIOS
		ValidacionesFuncionesSecundarias.validacionCampoObligatoriosFuncionFFInvitalicia(fnac, edadMax);
		// FIN VALIDACION CAMPOS OBLIGATORIOS

		// ffinproy = add(fnac.YEAR, edadMax),
		// Incrementamos a la fecha de nacimiento la edadMaxima
		ffInproy = UtilFechas.incrAnyo(fnac, edadMax);

		if (FuncionesSecundarias.LOG.isTraceEnabled()) {
			FuncionesSecundarias.LOG.trace("Fin de la Funcion << ffInvitalicia >> de la clase FuncionesSecundarias, con resultado ffInproy = {}", ffInproy);
		}
		return ffInproy;
	}

	/**
	 * La Funcion Y calcula la fraccion del año incompleta desde efecto anterior, hasta fecha de calculo
	 * 
	 * @param finisusc
	 *            Identifica la fecha de inicio de la suscripcion
	 * @param fcalc
	 *            Identifica la fecha a la que se efectua el calculo
	 * @param criterioFecha
	 *            Identifica el criterio de fechas para realizar el calculo
	 * @return Identifica el resultado del calculo realizado
	 */
	public static BigDecimal y(final Timestamp finisusc, final Timestamp fcalc, final String criterioFecha) {
		// Variables locales
		BigDecimal raccAnno = BigDecimal.ZERO;
		BigDecimal operandoFcalc = BigDecimal.ZERO;
		BigDecimal operandoFinisusc = BigDecimal.ZERO;
		BigDecimal totalOperando = BigDecimal.ZERO;
		// Fin variables locales

		if (FuncionesSecundarias.LOG.isTraceEnabled()) {
			FuncionesSecundarias.LOG.trace("Inicio de la Funcion << y >> de la clase FuncionesSecundarias, para la entrada finisusc = {} , fcalc = {} y criterioFecha = {} ", finisusc, fcalc, criterioFecha);
		}

		// INICIO VALIDACION CAMPOS OBLIGATORIOS
		ValidacionesFuncionesSecundarias.validacionCamposObligatoriosFuncionY(finisusc, fcalc, criterioFecha);
		// FIN VALIDACION CAMPOS OBLIGATORIOS

		// INICIO VALIDACION CAMPO CRITERIO FECHA
		ValidacionesFuncionesVBX.validacionCriterioFechaFuncionDDEneroEY(criterioFecha);
		// FIN VALIDACION CAMPO CRITERIO FECHA


		// operandoFcalc = (año(fcalc)*365 + ddenero(fcalc,ID-TEMPORAL) + dia(fcalc))
		operandoFcalc = BigDecimal.valueOf(UtilFechas.getAnio(fcalc)).multiply(ConstantsFunciones.CTE_OPER_365).add(BigDecimal.valueOf(FuncionesVBX.ddEnero(fcalc, criterioFecha))).add(BigDecimal.valueOf(UtilFechas.getDia(fcalc)));

		// operandoFinisusc = (año(finisusc)*365 + ddenero(finisusc,ID-TEMPORAL) + dia(finisusc))
		operandoFinisusc = BigDecimal.valueOf(UtilFechas.getAnio(finisusc)).multiply(ConstantsFunciones.CTE_OPER_365).add(BigDecimal.valueOf(FuncionesVBX.ddEnero(finisusc, criterioFecha))).add(BigDecimal.valueOf(UtilFechas.getDia(finisusc)));

		totalOperando = operandoFcalc.subtract(operandoFinisusc);

		// raccAnno = totalOperando / 365
		raccAnno =  totalOperando.divide(ConstantsFunciones.CTE_OPER_365, ConstantsFunciones.MATH_CONTEXT);

		if (FuncionesSecundarias.LOG.isTraceEnabled()) {
			FuncionesSecundarias.LOG.trace("Fin de la Funcion << y >> de la clase FuncionesSecundarias, con resultado raccAnno = {}", raccAnno);
		}
		return raccAnno;
	}
	
	
	/**
	 *           w-x
	 *		äx = SUMATORIO Vzc (fcal,j)* actfin(fcal,j)
	 *           j=0 
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
	 * @return resultadoAsubX
	 */
	public static BigDecimal aSubX(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		// Variables locales
		BigDecimal resultadoAsubX = BigDecimal.ZERO;
		// Fin variables locales

		if (FuncionesSecundarias.LOG.isTraceEnabled()) {
			FuncionesSecundarias.LOG.trace("Inicio Funcion << aSubX >> de la clase FuncionesSecundarias, para la entrada proyUmic = {}, bloqueCorriente = {}, iteracion = {}, fcalc = {}, umic = {}, btcUmic = {}, mapVariables = {} y codSubproceso = {}",
					proyUmic, bloqueCorriente, fcalc, umic, btcUmic, mapVariables, codSubproceso);
		}

		resultadoAsubX = aSubXImpl(proyUmic, bloqueCorriente, fcalc, umic, btcUmic, mapVariables, codSubproceso, true);

		if (FuncionesSecundarias.LOG.isTraceEnabled()) {
			FuncionesSecundarias.LOG.trace("Fin Funcion << aSubX >> de la clase FuncionesSecundarias, con resultado resultadoAsubX = {} ", resultadoAsubX);
		}

		return resultadoAsubX;
	}

	
	/**
	 * Función común que aglutina la funcionalidad de las funciones äx y äxd
	 *		äx = SUMATORIO Vzc (fcal,j)* actfin(fcal,j)
	 *           j=0 
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
	 * @param esAx
	 * 			Indica si se trata de la función AX (true) o AXD (false)
	 * @return resultadoAsubX
	 */
	public static BigDecimal aSubXImpl(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso, final boolean esAx) {
		// Variables locales
		String varCriterioFec = ConstantsFunciones.CTE_CADENA_VACIA;
		String varCriterEdad = ConstantsFunciones.CTE_CADENA_VACIA;
		BigDecimal resultadoAsubXImpl = BigDecimal.ZERO;
		BigDecimal varVzc1 = null;
		BigDecimal varVzcI = null;
		BigDecimal varActfinI = BigDecimal.ONE;
		BigDecimal varI1 = BigDecimal.ZERO;
		BigDecimal varI2 = BigDecimal.ZERO;
		BigDecimal varFactor = BigDecimal.ZERO;
		BigDecimal varFactor2 = BigDecimal.ZERO;
		Timestamp varFfincas = null;
		int varAnoffincas = 0; 
		int varNperiodo = 0;
		int varAnoNac;
		String varCsexAseg1 = "0";
		int varEdadCalc = 0;
		List<BigDecimal> lstValoresTabMort = null;
		final IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
		BigDecimal incrFactor2 = BigDecimal.ZERO;
		BigDecimal actFinICambioTramo = BigDecimal.ZERO;
		// Fin variables locales

		if (FuncionesSecundarias.LOG.isTraceEnabled()) {
			FuncionesSecundarias.LOG.trace("Inicio Funcion << aSubXImpl >> de la clase FuncionesSecundarias, para la entrada proyUmic = {}, bloqueCorriente = {}, iteracion = {}, fcalc = {}, umic = {}, btcUmic = {}, mapVariables = {} y codSubproceso = {}",
					proyUmic, bloqueCorriente, fcalc, umic, btcUmic, mapVariables, codSubproceso);
		}

		//Validamos los campos de entrada
		ValidacionesFuncionesAuxiliares.validarParamEntrada(proyUmic, fcalc, umic, btcUmic);
		
		if (umic.getDuraciones().getNdursegano() == 0){
			//Si la duración del seguro es 0 se lanza excepción
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AO, new String[]{umic.getDuraciones().getNdursegano().toString()});
		}

		varCriterEdad = (String) servicio.recuperarDefinicionAuxiliar(umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), ConstantsFunciones.CTE_VA_CRIT_EDA);
		varCriterioFec = (String) servicio.recuperarDefinicionAuxiliar(umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), ConstantsFunciones.CTE_VA_CRIT_FEC);
		ValidacionesFuncionesAuxiliares.validarVariableDeApoyoVarCriEdad(varCriterEdad);
		ValidacionesFuncionesAuxiliares.validarVariableDeApoyoVarCriterioFecha(varCriterioFec);

		/**
		 * 		Variables de Apoyo
		 * 		VarCriterEdad --> obtenerConfiguracion.recuperarVariableApoyo(ID-CRITERIO)
		 * 		VarCriterFec --> obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
		 * 		
		 * 		Si alguna de las variables de apoyo  retornadas es nulo se devuelve error funcional A5 -  No se ha encontrado la Variable de Apoyo &NombreVariableApoyo, finalizando el proceso para la UMIC.
		 * 		varEdadCalc = Entero(nedad(umic.fechas.fecinisus, umic.asegurados.fnacAseg1, VarCriterEdad))
		 * 		varI1 = btcUmic.itcalc1
		 * 		varTm = btcUmic.tabla1Aseg1
		 * 		varAnoNac = Año (umic.asegurados.fnacAseg1)
		 * 		varValoresTabMort = obtenerConfiguracion. recuperarValoresExperiencia(btcUmic.fecCierre, varTm, varAnoNac, btcUmic. itcalc1, umic.baseTecIni. psobremort, umic.baseTecIni. priesgo)
		 * 		varI2= btcUmic.Itcalc2
		 * 		- Si varI2 es nulo:
		 * 			- varFactor = 1 + (varI1/100)
		 * 		- Si varI2 es no nulo y > 0 --> varffincas = btcUmic.fecIniTramo2
		 * 			- Si fcalc > varffincas entonces:
		 * 				- varFactor = 1 + (varI2/100)
		 * 			- Si fcalc <= varffincas --> varFactor = 0 --> inicializamos a cero y se calculará el factor dentro del  recorrido de varValoresTabMort.
		 */
		varI1 = btcUmic.getItcalc().get(ConstantsFunciones.CTE_0);
		varFfincas = btcUmic.getFecfintramo().get(ConstantsFunciones.CTE_0);
		UtilFechas.Fecha varFfincasu = UtilFechas.getFecha(varFfincas);
		//UtilFechas.Fecha fcalcu = UtilFechas.getFecha(fcalc);
		UtilFechas.Fecha fcalcu = UtilFechas.getFecha(umic.getFechas().getFecinisus());
		varAnoffincas = varFfincasu.getAnio() - fcalcu.getAnio();
		Integer varEdifer = umic.getDatosGenerales().getEdifer();

		varEdadCalc = FuncionesAuxiliares.nEdad(umic.getFechas().getFecinisus(), umic.getAsegurados().getFnacAseg1(), varCriterEdad, umic.getRentas().getFecIni(), varEdifer).intValue();
		String varModProb;
		if(btcUmic.getBt().equals(ConstantesSolvencia.BASE_NIIF17) ||
				btcUmic.getBt().equals(ConstantesSolvencia.BASE_NIF17LIR) ||
				btcUmic.getBt().equals(ConstantesSolvencia.BASE_N17LIRIN) ||
				btcUmic.getBt().equals(ConstantesSolvencia.BASE_NIFF17OCI) ||
				btcUmic.getBt().equals(ConstantesSolvencia.BASE_NIIF17IF) ||
				btcUmic.getBt().equals(ConstantesSolvencia.BASE_N17CLIR) ||
				btcUmic.getBt().equals(ConstantesSolvencia.BASE_NF17MFE) ||
				btcUmic.getBt().equals(ConstantesSolvencia.BASE_NF17AEN) ||
				btcUmic.getBt().equals(ConstantesSolvencia.BASE_NF17GTO)){
			
			varModProb = servicio.recuperarModulo(umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), umic.getDatosAdicionales().getPrestCal(), ConstantesSolvencia.BASE_NIIF17, ConstantsFunciones.CTE_PROY_VIDA, "02");

			
		}else{
		
		varModProb = servicio.recuperarModulo(umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), umic.getDatosAdicionales().getPrestCal(), btcUmic.getBt(), ConstantsFunciones.CTE_PROY_VIDA, "02");
		}
		varCsexAseg1 = umic.getAsegurados().getCsexAseg1();

		int varEdadCalc2 = varEdadCalc;
		if (btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_BEL) || 
				btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_BELCOA) ||
				btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_BELCLR) ||
				btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_SCRTIU) ||
				btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_SCRTID) ||
				btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_SCRGTO) ||
				btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_SCRMFE) ||
				btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_SCRMMI) ||
				btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_SCRMCF) ||
				btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_SCRMCI) ||
				btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_SCRLFE) ||
				btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_SCRLMI) || 
				btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_SCRINC) || 
				btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_SCRVM)  || 
				btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_SCRAEP) || 
				btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_SCRAEN) || 
				btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_SCRAIP) || 
				btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_SCRAIN) || 
				btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_SCRANM)) {
			varEdadCalc2 = FuncionesAuxiliares.nEdad(fcalc, umic.getAsegurados().getFnacAseg1(), varCriterEdad, umic.getRentas().getFecIni(), varEdifer).intValue();			
		}

		if (varModProb == null || !varModProb.equals(ConstantsFactorias.MODULO_VZCIERTA)){
			varAnoNac = UtilFechas.getAnio(umic.getAsegurados().getFnacAseg1());

			lstValoresTabMort = servicio.recuperarValoresExperiencia(umic, btcUmic, Integer.toString(varAnoNac), varCsexAseg1, varEdadCalc2, "L", IObtenerConfiguracion.OrdenAsegurado.ASEG1);
		
		} else {
			lstValoresTabMort = Util.listaProbUno(ConstantsFunciones.CTE_130);
		}
		

		varFactor = BigDecimal.ONE.add(varI1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		varI2 = btcUmic.getItcalc().get(ConstantsFunciones.CTE_1);
		if (null != varI2 && varI2.signum() == 1) {
			// Si varI2 es no nulo y > 0								
			varFactor2 = BigDecimal.ONE.add(varI2.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));;
			incrFactor2 = BigDecimal.ONE.divide(varFactor2, ConstantsFunciones.MATH_CONTEXT);
			// Simplificación del cálculo de la distancia en años basada en que la función está anualizada
			//varAnoffincas = FuncionesAuxiliares.nAnnos(varFfincas, fcalc, varCriterioFec);
			actFinICambioTramo = Util.pow(varFactor, -varAnoffincas);
		}

		final BigDecimal incrFactor = BigDecimal.ONE.divide(varFactor, ConstantsFunciones.MATH_CONTEXT);

		/**
		 * 	Una vez establecidas estas variables, se obtendrá el valor actual de la renta como:
		 * 
		 * 		 i = valoresTabMort.last
		 * 	äx = SUMATORIO		[varVzc(i) * varActfin(i)]
		 * 		 i = varEdadCalc
		 * 	
		 * 	Donde cada término del sumatorio se obtendrá como se detalla a continuación:
		 * 	
		 * 	- Se recorrerán los valores de la tabla de experiencia varValoresTabMort desde el índice  i = varEdadCalc hasta el fin de varValoresTabMort (i= varValoresTabMort.last)
		 * 		Para el índice i = varEdadCalc se hará:
		 * 			varVZC1 = varValoresTabMort(varEdadCalc)
		 * 			varNperiodo = 0
		 * 			varAnoffincas= nannos(varffincas, fcalc, VarCriterFec);
		 *	 	
		 * 		Para cada uno de estos índices de la tabla de valores de experiencia se calculará:
		 * 			varVzc(i) = varValoresTabMort(i) / varVZC1
		 * 			varNperiodo = varNperiodo + 1
		 * 			
		 * 			- Si varFactor <> 0  se hará:
		 * 				- varActfin(i) = (varFactor)^(-varNperiodo)
		 * 			- Si varFactor = 0 se hará:
		 * 				- Si varNperiodo <= varAnoffincas
		 * 					- varFactor = 1 + (varI1/100)
		 * 					- varActfin(i) = (varFactor)^(-varNperiodo )
		 * 				- Si varNperiodo > varAnoffincas:
		 * 					- varFactor = 1 + (varI1/100)
		 * 					- varFactor2 = 1 + (varI2/100)
		 * 					- varActfin(i) = (varFactor)^(-varAnoffincas) * (varFactor2)^(-varNperiodo)
		 */
		varVzc1 = lstValoresTabMort.get(varEdadCalc);

		int limiteSuperior =0;
		int limiteInferior=0;

		if(esAx) {
			if (ConstantsFunciones.CTE_RENTA_TEMPORAL.equals(umic.getRentas().getTempVit())||(umic.getRentas().getTempVit()==null && umic.getDuraciones().getNdursegano()!=null)){
				limiteSuperior = umic.getDuraciones().getNdursegano();
			} else if (ConstantsFunciones.CTE_RENTA_VITALICIA.equals(umic.getRentas().getTempVit())){
				limiteSuperior = lstValoresTabMort.size();
				limiteInferior = varEdadCalc;
			} else {
				if (FuncionesSecundarias.LOG.isDebugEnabled()) {
					FuncionesSecundarias.LOG.debug(Util.errorValidacionA2(umic.getRentas().getTempVit(), ConstantsFunciones.CTE_TEMP_VIT));
				}
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A2, new String[]{umic.getRentas().getTempVit(), ConstantsFunciones.CTE_TEMP_VIT}); 
			}
		} else {
			limiteSuperior = umic.getDuraciones().getNdursegano() + varEdadCalc;
			limiteInferior = varEdadCalc;
		}


		boolean primeraIteracionI2 = true;
		for (int i = limiteInferior; i < limiteSuperior ; i++) {
			if(i<lstValoresTabMort.size() && lstValoresTabMort.get(i).signum() != 0) {					
				// Se calcula varVzcI
				varVzcI = lstValoresTabMort.get(i).divide(varVzc1, ConstantsFunciones.MATH_CONTEXT);

				if (varNperiodo <= varAnoffincas ) {
					// Si varNperiodo <= varAnoffincas
					if(varNperiodo == 0) {
						// Si es la primera iteracion del tramo de interés único
						varActfinI = BigDecimal.ONE;
					} else {
						// Si no se calcula en función de la iteración anterior
						varActfinI = varActfinI.multiply(incrFactor, ConstantsFunciones.MATH_CONTEXT);						
					}
				} else {
					// Si varNperiodo > varAnoffincas:

					if(null != varI2 && varI2.signum() == 0) {
						varActfinI = BigDecimal.ZERO;
					} else {
						if(primeraIteracionI2) {
							// Si es la primera iteracion del interés se calcula
							varActfinI = actFinICambioTramo.multiply(Util.pow(varFactor2, -(varNperiodo-varAnoffincas)), ConstantsFunciones.MATH_CONTEXT);
							primeraIteracionI2 = false;
						} else {
							// Si no se calcula en función de la iteración anterior
							varActfinI = varActfinI.multiply(incrFactor2, ConstantsFunciones.MATH_CONTEXT);							
						}																	
					}
				}
				resultadoAsubXImpl = resultadoAsubXImpl.add(varVzcI.multiply(varActfinI, ConstantsFunciones.MATH_CONTEXT));
			}
			varNperiodo = varNperiodo + 1;

		}

		if (FuncionesSecundarias.LOG.isTraceEnabled()) {
			FuncionesSecundarias.LOG.trace("Fin Funcion << aSubXImpl >> de la clase FuncionesSecundarias, con resultado resultadoAsubX = {} ", resultadoAsubXImpl);
		}

		return resultadoAsubXImpl;
	}

		
	

	/**
	 * Definimos beta como la fraccion de año transcurrida desde efecto
	 * anterior hasta la fecha de cierre pero situamos el cierre a las 0 horas.
	 * Se utiliza en el calculo de suplementos, provisiones al dia y rescates.
	 * 
	 * @param fantRenovacion
	 *           Identifica la fecha de efecto o renovación mensual anterior
	 * @param fproxRenovacion
	 *           Identifica la fecha de la próxima renovación mensual.
	 * @param fcierre
	 *           Identifica la fecha de cierre
	 * @param criterioFecha
	 * 			 Identifica el criterio de fechas para realizar el cálculo
	 * @return beta
	 * 			Fracción de mes transcurrida desde efecto mensual anterior hasta la fecha de cierre
	 */
	
	public static BigDecimal mg001Beta(final Timestamp fantRenovacion, final Timestamp fproxRenovacion, 
			final Timestamp fcierre, final String criterioFecha) {
		// Variables locales
		BigDecimal beta = BigDecimal.ZERO;
		int numdc = 0;
		int numdr = 0;
		String criterioFechaCalc = criterioFecha;
		final IAlmacenarDatos almacenarDatos = FachadaServicios.getAlmacenarDatos();
		// Fin variables locales

		if (FuncionesSecundarias.LOG.isTraceEnabled()) {
			FuncionesSecundarias.LOG.trace("Inicio Funcion << mg001Beta >> de la clase FuncionesSecundarias, para la entrada fantRenovacion = {}, fproxRenovacion = {} , fcierre = {} y criterioFecha = {} ",
					fantRenovacion, fproxRenovacion, fcierre, criterioFecha);
		}

		ValidacionesFuncionesSecundarias.validarCriteriosFuncionMg001Beta(fantRenovacion, fproxRenovacion, fcierre, criterioFecha);

		/*
		 *Si criterioFecha es ( 03 ó 04 ) se asumirá criterioFecha = 01 (BASE365) y se generará una incidencia de tipo "Informativa"
		 * indicando que se asume ese criterio 365 para una baseMensual.
		 */
		if (ConstantsFunciones.CTE_CRI_FECHA_03.equals(criterioFecha) || ConstantsFunciones.CTE_CRI_FECHA_04.equals(criterioFecha)) {
			criterioFechaCalc = ConstantsFunciones.CTE_CRI_FECHA_01;

			// Devolver incidencia informativa indicando que, para el criterio de fecha utilizado, se asume 365 para una base mensual
			final Incidencia incidencia = Solvencia2ExcepcionHelper.crearAviso(ConstantsFunciones.CTE_COD_ERROR_I000, new Object[] {criterioFecha});

			almacenarDatos.almacenarIncidencias(incidencia);
		}

		/*
		 * Dc = numero de dias que median entre la fecha de efecto o renovacion
		 * anterior y la fecha de cierre
		 */
		numdc =  FuncionesAuxiliares.nDias(fantRenovacion, fcierre, criterioFechaCalc);

		/*
		 * Dr = diferencia en dias entre la fecha de la anterior renovacion y la
		 * fecha de la proxima renovacion.
		 */
		numdr = FuncionesAuxiliares.nDias(fantRenovacion, fproxRenovacion, criterioFechaCalc);

		beta = new BigDecimal(numdc).divide(new BigDecimal(numdr), ConstantsFunciones.MATH_CONTEXT);

		if (FuncionesSecundarias.LOG.isTraceEnabled()) {
			FuncionesSecundarias.LOG.trace("Fin Funcion << mg001Beta >> de la clase FuncionesSecundarias, con resultado beta = {}", beta);
		}

		return beta;
	}


		
		
	/**
	 * Función que calcula el término VPRE de la función GASTINI.
	 * @param j Corresponde al período transcurrido desde  la fecha alta ajuste o suscripción  hasta la fecha “j”.
	 * @param m Número de años en que aplicamos un primer interés técnico.
	 * @param i1 Primer Interés técnico.
	 * @param i2 Segundo Interés técnico.
	 * 
	 * @return vpre
	 */
	public static BigDecimal vpre (final Integer j, final BigDecimal m, final BigDecimal i1, final BigDecimal i2){
		//Variables locales
		BigDecimal vpre = BigDecimal.ZERO;
		BigDecimal jBD;
		BigDecimal unoMasI1Entre100;
		//Fin variables locales
		
		if (FuncionesSecundarias.LOG.isTraceEnabled()) {
			FuncionesSecundarias.LOG.trace("Inicio Funcion <<vpre>> de la clase FuncionesSecundarias, para la entrada j = {}, m = {}, i1 = {}, i2 = {}, ",
					j, m, i1, i2);
		}
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesSecundarias.validarParamEntradaFuncionVpre(j, m, i1, i2);
		
		jBD = BigDecimal.valueOf(j);
		unoMasI1Entre100 = (i1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)).add(BigDecimal.ONE);
		
		if (jBD.compareTo(m) < 0){
			vpre = Util.pow(unoMasI1Entre100, jBD.negate());
		} else {
			BigDecimal unoMasI2Entre100 = (i2.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)).add(BigDecimal.ONE);
			BigDecimal primerOper = Util.pow(unoMasI1Entre100, m.negate());
			BigDecimal segundoOper = Util.pow(unoMasI2Entre100, (jBD.subtract(m)).negate());
			vpre = primerOper.multiply(segundoOper);
		}
		
		if (FuncionesSecundarias.LOG.isTraceEnabled()) {
			FuncionesSecundarias.LOG.trace("Fin Funcion << vpre >> de la clase FuncionesSecundarias, con resultado vpre = {}", vpre);
		}
		
		return vpre;
	}
	
	/**
	 * Función que calcula el término VPRER de la función GASTGI.
	 * @param j Corresponde al período transcurrido desde  la fecha alta ajuste o suscripción  hasta la fecha “j”.
	 * @param m Número de años en que aplicamos un primer interés técnico.
	 * @param unoMasI1Entre100 Primer Interés técnico/100  +1.
	 * @param unoMasI2Entre100 Segundo Interés técnico/100  +1.
	 * @param ren Período de tiempo transcurrido desde la fecha de alta del ajuste hasta la fecha de renovación siguiente al momento de cálculo.
	 * 
	 * @return vprer
	 */
	public static BigDecimal vprer(final BigDecimal j, final BigDecimal m, final BigDecimal unoMasI1Entre100, final BigDecimal unoMasI2Entre100, 
			final BigDecimal ren, final Integer j1){
		//Variables locales
		BigDecimal vprer = BigDecimal.ZERO;
		BigDecimal mMenosRen;
		BigDecimal jBD;
		//Fin variables locales
		
		if (FuncionesSecundarias.LOG.isTraceEnabled()) {
			FuncionesSecundarias.LOG.trace("Inicio Funcion <<vprer>> de la clase FuncionesSecundarias, para la entrada j = {}, m = {}, unoMasI1Entre100 = {}, unoMasI2Entre100 = {}, ren = {}",
					j, m, unoMasI1Entre100, unoMasI2Entre100, ren);
		}
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesSecundarias.validarParamEntradaFuncionVprer(j, m, unoMasI1Entre100, unoMasI2Entre100, ren, j1);
		
		mMenosRen = m.subtract(ren);
		jBD = BigDecimal.valueOf(j1);
		
		if (jBD.compareTo(mMenosRen) < 0){
			//vprer =  (1+ (I1/100) )^(-J1)
			vprer = Util.pow(unoMasI1Entre100, jBD.negate());
		} else if (ren.compareTo(m) < 0 && jBD.compareTo(mMenosRen) >= 0){
			BigDecimal operando1 = Util.pow(unoMasI1Entre100, mMenosRen.negate());
			BigDecimal operando2 = Util.pow(unoMasI2Entre100, (jBD.subtract(m).add(ren)).negate());
			// vprer = (1+ (I1/100) )^(-(M-REN))* (1+ (I2/100) )^(-(J1-M+REN))
			vprer = operando1.multiply(operando2);
		} else if (ren.compareTo(m) >= 0){
			//vprer =  (1+ (I2/100) )^(-J1)
			vprer = Util.pow(unoMasI2Entre100, jBD.negate());
		}
		
		if (FuncionesSecundarias.LOG.isTraceEnabled()) {
			FuncionesSecundarias.LOG.trace("Fin Funcion << vprer >> de la clase FuncionesSecundarias, con resultado vprer = {}", vprer);
		}
		
		return vprer;
	}
	
	
	/**
	 * Función que calcula calcula el diferimiento actuarial por años incompletos, incluyendo la actualización de los gastos 
	 * de gestión interna del seguro en productos flexibles de prima periódica.
	 * @param x Edad a fecha de cálculo.
	 * @param t Resultado de la función naños() en base al criterio establecido.
	 * @param alfa Fracción transcurrida desde efecto mensual anterior hasta la fecha de cierre.
	 * @param valoresTabMort Valores de la tabla de mortalidad.
	 * @param w Edad Máxima de la tabla de mortalidad.
	 * @param n Duración del seguro en años.
	 * @param i1 Interés del primer tramo.
	 * @param gic Gastos de gestión interna sobre Capital.
	 * 
	 * @return vidapa
	 */
	public static BigDecimal vidapa(final Integer x, final Integer t, final BigDecimal alfa, final List<BigDecimal> valoresTabMort,
			final Integer w, final Integer n, final BigDecimal i1, final BigDecimal gic){
		//Inicio variables locales
		BigDecimal vidapa = BigDecimal.ONE;
		BigDecimal varLxj = BigDecimal.ZERO;
		BigDecimal varLxt1 = BigDecimal.ZERO;
		BigDecimal varValor = BigDecimal.ZERO;
		BigDecimal varLFRACn;
		BigDecimal varLFRACt;
		BigDecimal varLFRACt1;
		BigDecimal terminoSum = BigDecimal.ZERO;
		BigDecimal lxjEntreLxt1;
		//Fin variables locales
		
		if (FuncionesSecundarias.LOG.isTraceEnabled()) {
			FuncionesSecundarias.LOG.trace("Inicio Funcion << VIDAPA >> de la clase FuncionesSecundarias, para la entrada  x = {}, t = {}, beta = {},"
					+" valoresTabMort = {}, w = {}, n = {}, i1 = {}, gic = {}", 
					x, t, alfa, valoresTabMort, w, n, i1, gic);
		}
		
		//Validaciones parametros entrada
		ValidacionesFuncionesSecundarias.validarParamEntradaFuncionVidapa(x, t, alfa, valoresTabMort, w, n, i1, gic);
		
		BigDecimal unoMasI1Entre100 = BigDecimal.ONE.add(i1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		
		if (t != n){
			if ((n - t -1) > 0){
				Integer varXt1 = x+t+1;
				if (varXt1 < w){
					varLxt1 = valoresTabMort.get(varXt1); //varXt1 entero, no es necesario interpolar
				}
				//SUMATORIO desde j=T+1 hasta n-1
				for (int j=t+1; j<n; j++){
					Integer varXJ = x+j;
					if (varXJ < w){
						varLxj = valoresTabMort.get(varXJ); //varXJ entero, no es necesario interpolar
					}
					
					if (j==t+1){
						//Se calcula el primer término para optimizar el resto de operaciones del bucle
						terminoSum = BigDecimal.ONE; //Primer termino del sumatorio se eleva a 0 por lo tanto el resultado es 1
					} else {
						//A partir del primer término solo es necesario dividir (potencia negativa) el resultado del término 
						//anterior entre la base. Optimización de operaciones
						terminoSum = terminoSum.divide(unoMasI1Entre100, ConstantsFunciones.MATH_CONTEXT);
					}
					
					lxjEntreLxt1 = varLxj.divide(varLxt1, ConstantsFunciones.MATH_CONTEXT);
					varValor = varValor.add(terminoSum.multiply(lxjEntreLxt1));
				}

			} //Si (n - t - 1) <= 0 --> varValor=0
			
			varLFRACn = FuncionesAuxiliares.lfrac(x, n, BigDecimal.ZERO, valoresTabMort, w);
			varLFRACt = FuncionesAuxiliares.lfrac(x, t, alfa, valoresTabMort, w);
			varLFRACt1 = FuncionesAuxiliares.lfrac(x, t+1, BigDecimal.ZERO, valoresTabMort, w);
			
			/**
			 * Se calcula VIDAPA como:
			 * 
			 * VIDAPA =  [〖 (varLFRACn/varLFRACt )*(1+ I1/100)〗^(-(n-T-alfa))  ]+ gic/100 * 
			 *     [ (1-alfa)+ ((varLFRACt1 /varLFRACt )*varValor * (1+ I1/100)^(-(1-alfa)) ) ]   
			 */
			
			BigDecimal divisionLFRACn = varLFRACn.divide(varLFRACt, ConstantsFunciones.MATH_CONTEXT); //(varLFRACn/varLFRACt )
			BigDecimal divisionLFRACt1 = varLFRACt1.divide(varLFRACt, ConstantsFunciones.MATH_CONTEXT); //(varLFRACt1 /varLFRACt )
			
			BigDecimal nMenosTMenosAlfa = BigDecimal.valueOf(n-t).subtract(alfa); //(n-T-alfa)
			
			//[〖 (varLFRACn/varLFRACt )*(1+ I1/100)〗^(-(n-T-alfa))  ]
			BigDecimal primerOperando = divisionLFRACn.multiply(Util.pow(unoMasI1Entre100, nMenosTMenosAlfa.negate()));
			
			BigDecimal gicEntre100 = gic.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
			BigDecimal potencia = Util.pow(unoMasI1Entre100, (BigDecimal.ONE.subtract(alfa)).negate());
			//[ (1-alfa)+ ((varLFRACt1 /varLFRACt )*varValor * (1+ I1/100)^(-(1-alfa)) ) ]
			BigDecimal segundoOperando = BigDecimal.ONE.subtract(alfa).add(divisionLFRACt1.multiply(varValor).multiply(potencia));
			
			vidapa = primerOperando.add(gicEntre100.multiply(segundoOperando));
			
		} //Si T = n --> VIDAPA = 1
		

		if (FuncionesSecundarias.LOG.isTraceEnabled()) {
			FuncionesSecundarias.LOG.trace("Fin Funcion << VIDAPA >> de la clase FuncionesSecundarias, con resultado vidapa = {} ", vidapa);
		}
		
		return vidapa;
	}


	
	/**
	 * Función que calcula parte de la función FUT_FLEX.
	 * @param tc Anualidad de cálculo.
	 * @param tcm Meses completos transcurridos desde fecha de efecto a fecha de cálculo(fecha de cierre).
	 * @param ttm Número de meses transcurridos desde el efecto a la fecha de vencimiento.
	 * @param beta Meses transcurridos desde la fecha de cálculo (Fecha de cierre)  hasta la fecha de proyección.
	 * @param prp Porcentaje de revalorización de Primas.
	 * 
	 * @return futpg
	 */
	public static BigDecimal futpg(final Integer tc, final Integer tcm, final Integer ttm, final Integer beta, 
			final BigDecimal prp){
		//Inicio variables locales
		BigDecimal futpg = BigDecimal.ZERO;
		Integer limiteInferior;
		Integer limiteSuperior;
		BigDecimal terminoSum;
		//Fin variables locales
		
		if (FuncionesSecundarias.LOG.isTraceEnabled()) {
			FuncionesSecundarias.LOG.trace("Inicio Funcion << FUTPG >> de la clase FuncionesSecundarias, para la entrada  tc = {}, tcm = {}, ttm = {},"
					+" beta = {}, prp = {}", tc, tcm, ttm, beta, prp);
		}
		
		//Validaciones parametros de entrada
		ValidacionesFuncionesSecundarias.validarParamEntradaFuncionFutpg(tc, tcm, ttm, beta, prp);
		
		if ((ttm/12)-1 > ((tcm+beta)/12)){

			/**
			 * Se calcula FUTPG como:
			 * FUTPG = Sumatorio(desde entero((tcm+beta)/12)+1) hasta (entero(ttm/12)-1)): 〖(1+ prp/100)〗^(j-tc-1) 
			 */

			BigDecimal unoMasPrpEntre100 = BigDecimal.ONE.add(prp.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
			limiteInferior = ((tcm+beta)/12) +1;
			limiteSuperior = (ttm/12) -1;
			

			//Se calcula el primer término del sumatorio para optimizar operaciones dentro del bucle
			terminoSum = Util.pow(unoMasPrpEntre100, limiteInferior-tc-1);
			futpg = terminoSum;
			
			//SUMATORIO desde entero((tcm+beta)/12)+2) hasta (entero(ttm/12)-1)
			for (int j= limiteInferior+1; j<=limiteSuperior; j++){
				terminoSum = terminoSum.multiply(unoMasPrpEntre100);
				futpg = futpg.add(terminoSum);		
			}
			
		} // Si (ttm/12) -1 <= (tcm+beta/12) +1 --> FUTPG = 0			
		if (FuncionesSecundarias.LOG.isTraceEnabled()) {
			FuncionesSecundarias.LOG.trace("Fin Funcion << FUTPG >> de la clase FuncionesSecundarias, con resultado futpg = {} ", futpg);
		}
		
		return futpg;
	}
	

}
