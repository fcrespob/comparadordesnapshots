package es.mapfre.solvencia.formulacion.modulos.impl;

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
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada del calculo que devuelve la cuantía de fallecimiento de la garantía principal de la póliza con estado en vigor. 
 * La expresión matemática para su determinación es la siguiente:
 *			CSP(033,tc)=PNA(0)*(PAS*[SUMATORIO{H=0..Tc,(1+Ifal/100)^(Tc-H)*(1+H*PRP/100) }] + FUT*[SUMATORIO{H=Tc+1..n-1,(1+H*PRP/100)}])
 *
 * @author apedro
 *
 */
public class ModuloCSP033 implements Modulo {
	
	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP033.class);
	
	// Variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP033;
	
	private static final String CLAVE_IFAL = ConstantsModulos.CTE_VA_IFAL;
	private static final String CLAVE_VAR_IFAL = CLAVE_IFAL.concat(CLAVE_MODULO);
	
	private static final String CLAVE_PAS = ConstantsModulos.CTE_VA_PAS;
	private static final String CLAVE_VAR_PAS = CLAVE_PAS.concat(CLAVE_MODULO);
	
	private static final String CLAVE_FUT = ConstantsModulos.CTE_VA_FUT;
	private static final String CLAVE_VAR_FUT = CLAVE_FUT.concat(CLAVE_MODULO);
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_PN0 = ConstantsModulos.CTE_VAR_PN0.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_UNO_MAS_VAR_IFAL = ConstantsModulos.CTE_UNO_MAS_VAR_IFAL.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PRP_ENTRE100 = ConstantsModulos.CTE_PRP_ENTRE100.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 
	// Fin de las variables estáticas para agilizar operaciones.
	
	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}
	
	/**
	 * Función encargada de obtener los parámetros necesarios y de realizar la llamada a la función que realiza los calculos del modulo.
	 */
	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {
		//Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		//Fin variables locales
		
		try {
			if (ModuloCSP033.LOG.isTraceEnabled()) {
				ModuloCSP033.LOG.trace("Inicio de execute en clase ModuloCSP033");
			}
			
			//Recuperación de los datos que se pasarán a la función moduloCSP033.
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			//Fin de la recuperación de los datos que se pasarán a la función moduloCSP033.
			
			//Invocación de la función moduloCSP033
			resultado = moduloCSP033(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloCSP033.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP033.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP033.LOG.isTraceEnabled()) {
			ModuloCSP033.LOG.trace("Fin de execute en clase ModuloCSP033");
		}
		
		return resultado;
	}
	
	/**
	 * Modulo de calculo que devuelve la cuantía de fallecimiento de la garantía principal de la póliza con estado en vigor. 
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
	 */
	private BigDecimal moduloCSP033(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables locales
		Timestamp varFechaEfecto = null;
		int varNumAnualidades = 0;
		BigDecimal varIFal = BigDecimal.ZERO;
		BigDecimal varPas = BigDecimal.ZERO;
		BigDecimal varFut = BigDecimal.ZERO;
		BigDecimal csp033 = BigDecimal.ZERO;
		String varCriFec = ConstantsFunciones.CTE_CADENA_VACIA;
		//Fin variables locales
		
		if (ModuloCSP033.LOG.isTraceEnabled()) {
			ModuloCSP033.LOG.trace("Inicio de la función << moduloCSP034 >> para la iteración = {}", iteracion);
		}
				
		//Validación de los campos de entrada.
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		/**
		 	Para todas las iteraciones el proceso es el mismo.
		 	- Variables de Apoyo ( Si alguna de las variables de apoyo  retornadas es nulo se devuelve error funcional 005 - No se ha encontrado la Variable de Apoyo &NombreVariableApoyo, finalizando el proceso para la UMIC.)
				-	VarIfal --> obtenerConfiguracion.recuperarVariableApoyo(IFAL)
				-	VarPas --> obtenerConfiguracion.recuperarVariableApoyo(PAS)
				-	VarFut --> obtenerConfiguracion.recuperarVariableApoyo(FUT)
				-	VarCriterFec -->    obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
			- Se calcula csp033 como -- > csp033 = calculoFlujoNominal (...).
				- A calculoFlujoNominal(...) se le introducen como parámetros:
				-	varNumAnualidades = varTC +  naños(fcalc, proyUmic(j).varBloque.fecDevengo, VarCriterFec).
				-	VarIfal --> obtenerConfiguracion.recuperarVariableApoyo(IFAL)
		   		-	VarPas --> obtenerConfiguracion.recuperarVariableApoyo(PAS)
		   		-	VarFut --> obtenerConfiguracion.recuperarVariableApoyo(FUT)
		   		-	mapVariables
				-	varPN0 = umic.primas.iprimanetaini
				-	varPRP = umic.primas.prevprima
			- Otras variables del módulos:
				- varfechaEfecto = umic.fechas fecinisus (se almacena para su uso posterior).
				- varDuracion = umic.duraciones.ndursegano (se almacena para su uso posterior).
				- varTC = TC(varfechaEfecto, fcalc) (se almacena para su uso posterior).
				- varNumAnualidades= varTC +  naños(fcalc, proyUmic(j).varBloque.fecDevengo, VarCriterFec).		
		 */
		
		
		// Definición de variables auxiliares para agilizar las operaciones (se llaman varias veces).
		final Integer ccartera = umic.getDatosGenerales().getCcartera();
		final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		final Integer kgarantia = umic.getDatosGenerales().getKgarantia();
		// Fin de la definición de las variables auxiliares.
		
		// Cálculo de las variables de apoyo.
		varIFal = UtilModulos.getVarIFal(mapVariables, CLAVE_VAR_IFAL, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_IFAL, umic.getBti().getPintertecnI1());
		varPas = UtilModulos.getVarPas(mapVariables, CLAVE_VAR_PAS, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_PAS);
		varFut = UtilModulos.getVarFut(mapVariables, CLAVE_VAR_FUT, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_FUT);
		varCriFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIFEC);
		// Fin del cálculo de las variables de apoyo.
		
		// Validación de las variables de apoyo.
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarIFal(varIFal);
			ValidacionesComunesModulos.validarVariableDeApoyoVarPas(varPas);
			ValidacionesComunesModulos.validarVariableDeApoyoVarFut(varFut);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriFec);
		} // Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
		// Fin de la validación de las variables de apoyo.

		if (bloqueCorriente.getFechaDevengo()== null){
			return csp033;
		}
		
		if(bloqueCorriente.getFechaDevengo().after(umic.getFechas().getFecefecfin()) || !proyUmic.get(iteracion - 1).getFechaDesde().before(umic.getFechas().getFecefecfin())) {
			return csp033;
		}
		
		// Cálculo de las variables auxiliares necesarias para entrar en el cálculo del flujo nominal.
		
		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
			
		// Se emplea la variable auxiliar varNumAnualidades para facilitar la lectura del código.
		varNumAnualidades = FuncionesAuxiliares.tc(varFechaEfecto, proyUmic.get(iteracion-1).getFechaDesde());
		
		csp033 = calculoFlujoNominal(varNumAnualidades, varIFal, varPas, varFut, mapVariables, umic.getPrimas().getPrevprima(), umic.getDuraciones().getNdursegano(), umic.getPrimas().getIprimatarada());
		
		if (ModuloCSP033.LOG.isTraceEnabled()) {
			ModuloCSP033.LOG.trace("Fin de la función << moduloCSP033 >> para la iteración = {}, con resultado csp033 = {}", iteracion, csp033);
		}
		
		return csp033;
	}
	
	/**
	 *
	 * Función encargada de realizar el siguiente calculo del modulo csp033:
	 * 		CSP033(j) = varPN0 * (
	 *				VarPas [SUMATORIO(h= 0) hasta (varNumAnualidades  )[(1+varIfal/100)^(varNumAnualidades -h)]* [1+ h* (varPRP/100)] ]
	 *				+
	 *				varFut [SUMATORIO(h=varNumAnualidades  +1 ) hasta (varDuracion -1) 1 + h * (varPRP/100)] 
	 *				)
	 * y que posteriormente será asignado al flujo nominal.
	 * 
	 * @param varNumAnualidades
	 * 					Numero de anualidades
	 * @param varIFal
	 * 					Variable de apoyo --> obtenerConfiguracion.recuperarVariableApoyo(IFAL)
	 * @param varPas
	 * 					Variable de apoyo --> obtenerConfiguracion.recuperarVariableApoyo(PAS)
	 * @param varFut
	 * 					Variable de apoyo --> obtenerConfiguracion.recuperarVariableApoyo(FUT)
	 * @param mapVariables
	 * 					mapa con las variables de memoria necesarias
	 * @return csp033
	 */
	private BigDecimal calculoFlujoNominal(final Integer varNumAnualidades, final BigDecimal varIFal, final BigDecimal varPas, final BigDecimal varFut, final Map<String, Object> mapVariables, final BigDecimal varPrp, final Integer varDur, final BigDecimal varPn0) {
		//Variables locales
		BigDecimal sumatorioVarPas = BigDecimal.ZERO;
		BigDecimal sumatorioVarFut = BigDecimal.ZERO;
		BigDecimal valorIteracionFUT = BigDecimal.ZERO;
		BigDecimal csp033 = BigDecimal.ZERO;
		//Fin variables locales
		
		/** Consultar funcional modulo CSP033
		 * CSP033(j) = varPN0 * (
					VarPas [SUMATORIO(h= 0) hasta (varNumAnualidades  )[(1+varIfal/100)^(varNumAnualidades - h)]* [(1 + h* (varPRP/100))] ]
					+
					varFut [SUMATORIO(h=varNumAnualidades  +1 ) hasta (varDuracion -1) (1 + h* (varPRP/100)) ] 
					)
		 */
		if (ModuloCSP033.LOG.isTraceEnabled()) {
			ModuloCSP033.LOG.trace("Inicio de la función << calculoFlujoNominal >> de la clase ModuloCSP033");
		}
		
		// Para mejorar rendimiento se expresa como "multiply" y no como "divide".
		final BigDecimal unoMasVarIFal = UtilModulos.getVarUnoMasVarIFal(mapVariables, ModuloCSP033.CLAVE_VAR_UNO_MAS_VAR_IFAL, varIFal);
		final BigDecimal varPrpEntre100 = UtilModulos.getPorcentaje(mapVariables, CLAVE_VAR_PRP_ENTRE100, varPrp);
		
		
		
		if (varPrp.signum()==0 && varIFal.signum()==0){
			sumatorioVarPas = BigDecimal.ONE;
		} else {
			BigDecimal valorIteracionPASop1 = Util.pow(unoMasVarIFal, varNumAnualidades);
			BigDecimal valorIteracionPASop2 = BigDecimal.ZERO;
			sumatorioVarPas = valorIteracionPASop1; //La segunda parte del sumatorio queda 1+0, por lo tanto no hace falta multiplicar por 1
			// Cálculo del sumatorio de varpas.
			for (int h = 1; h <= varNumAnualidades; h++) {	
				valorIteracionPASop1 = valorIteracionPASop1.divide(unoMasVarIFal, ConstantsFunciones.MATH_CONTEXT);
				valorIteracionPASop2 = valorIteracionPASop2.add(varPrpEntre100);
				sumatorioVarPas = sumatorioVarPas.add(valorIteracionPASop1.multiply(valorIteracionPASop2.add(BigDecimal.ONE)));
			}
		}
	
		if (varPrp.signum()==0){
			sumatorioVarFut = BigDecimal.ONE;
		} else {
			if (varNumAnualidades +1 < varDur){
				valorIteracionFUT = varPrpEntre100.multiply(BigDecimal.valueOf(varNumAnualidades + 1));
				sumatorioVarFut = valorIteracionFUT.add(BigDecimal.ONE);
				
				// Cálculo del sumatorio de varFut
				for (int h = varNumAnualidades + 2; h < varDur; h++) {
					valorIteracionFUT = valorIteracionFUT.add(varPrpEntre100);
					sumatorioVarFut = sumatorioVarFut.add(valorIteracionFUT.add(BigDecimal.ONE));
				}
			}
			
		}
		
		csp033 = UtilModulos.getVarPN0(mapVariables, CLAVE_VAR_PN0, varPn0).multiply(
				varPas.multiply(sumatorioVarPas).add(varFut.multiply(sumatorioVarFut)));
		
		if (ModuloCSP033.LOG.isTraceEnabled()) {
			ModuloCSP033.LOG.trace("Fin de la función << calculoFlujoNominal >> de la clase ModuloCSP033");
		}
		
		return csp033;
	}
}
