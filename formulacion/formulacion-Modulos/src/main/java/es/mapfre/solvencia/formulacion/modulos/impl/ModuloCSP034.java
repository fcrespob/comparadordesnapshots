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
 * Clase encargada del calculo que devuelve la Valoracion del reembolso de primas en productos GEOMETRICOS 
 * no reducidos con reembolso de intereses por años transcurridos.
 * La expresión matemática para su determinación es la siguiente:
 *			CSP(034,tc)=PNA(0)*(PAS*[SUMATORIO{H=0..Tc,(1+Ifal/100)^(Tc-H)*(1+PRP/100)^H}] + FUT*[SUMATORIO{H=Tc+1..n-1,(1+PRP/100)^ H}]
 *
 * @author rschacon
 *
 */
public class ModuloCSP034 implements Modulo {
	
	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP034.class);
	
	// Variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP034;
	
	private static final String CLAVE_IFAL = ConstantsModulos.CTE_VA_IFAL;
	private static final String CLAVE_VAR_IFAL = CLAVE_IFAL.concat(CLAVE_MODULO);
	
	private static final String CLAVE_PAS = ConstantsModulos.CTE_VA_PAS;
	private static final String CLAVE_VAR_PAS = CLAVE_PAS.concat(CLAVE_MODULO);
	
	private static final String CLAVE_FUT = ConstantsModulos.CTE_VA_FUT;
	private static final String CLAVE_VAR_FUT = CLAVE_FUT.concat(CLAVE_MODULO);
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 
	private static final String CLAVE_VAR_PN0 = ConstantsModulos.CTE_VAR_PN0.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_UNO_MAS_VAR_IFAL = ConstantsModulos.CTE_UNO_MAS_VAR_IFAL.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_UNO_MAS_VAR_PRP = ConstantsModulos.CTE_UNO_MAS_VAR_PRP.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PRP_DIV_IFAL = ConstantsModulos.CTE_PRP_DIV_IFAL.concat(CLAVE_MODULO);
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
			if (ModuloCSP034.LOG.isTraceEnabled()) {
				ModuloCSP034.LOG.trace("Inicio de execute en clase ModuloCSP034");
			}
			
			//Recuperación de los datos que se pasarán a la función moduloCSP034.
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			//Fin de la recuperación de los datos que se pasarán a la función moduloCSP034.
			
			//Invocación de la función moduloCSP034
			resultado = moduloCSP034(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloCSP034.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP034.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP034.LOG.isTraceEnabled()) {
			ModuloCSP034.LOG.trace("Fin de execute en clase ModuloCSP034");
		}
		
		return resultado;
	}
	
	/**
	 * Modulo de calculo que devuelve la Valoracion del reembolso de primas en productos GEOMETRICOS 
	 * no reducidos con reembolso de intereses por años transcurridos.
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
	private BigDecimal moduloCSP034(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables locales
		Timestamp varFechaEfecto = null;
		int varNumAnualidades = 0;
		BigDecimal varIFal = BigDecimal.ZERO;
		BigDecimal varPas = BigDecimal.ZERO;
		BigDecimal varFut = BigDecimal.ZERO;
		BigDecimal csp034 = BigDecimal.ZERO;
		String varCriFec = ConstantsFunciones.CTE_CADENA_VACIA;
		//Fin variables locales
		
		if (ModuloCSP034.LOG.isTraceEnabled()) {
			ModuloCSP034.LOG.trace("Inicio de la función << moduloCSP034 >> para la iteración = {}", iteracion);
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
			- Se calcula csp034 como -- > csp034 = calculoFlujoNominal (...).
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
			return csp034;
		}
		
		if(bloqueCorriente.getFechaDevengo().after(umic.getFechas().getFecefecfin()) || !proyUmic.get(iteracion - 1).getFechaDesde().before(umic.getFechas().getFecefecfin())) {
			return csp034;
		}
		
		// Cálculo de las variables auxiliares necesarias para entrar en el cálculo del flujo nominal.
		
		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
			
		// Se emplea la variable auxiliar varNumAnualidades para facilitar la lectura del código.
		varNumAnualidades = FuncionesAuxiliares.tc(varFechaEfecto, proyUmic.get(iteracion-1).getFechaDesde());
		
		csp034 = calculoFlujoNominal(varNumAnualidades, varIFal, varPas, varFut, mapVariables, umic.getPrimas().getPrevprima(), umic.getDuraciones().getNdursegano(), umic.getPrimas().getIprimatarada());
		
		if (ModuloCSP034.LOG.isTraceEnabled()) {
			ModuloCSP034.LOG.trace("Fin de la función << moduloCSP034 >> para la iteración = {}, con resultado csp034 = {}", iteracion, csp034);
		}
		
		return csp034;
	}
	
	/**
	 *
	 * Función encargada de realizar el siguiente calculo del modulo csp034:
	 * 		CSP034(j) = varPN0 * (
	 *				VarPas [SUMATORIO(h= 0) hasta (varNumAnualidades  )[(1+varIfal/100)^(h-varNumAnualidades )]* [(1+varPRP/100)^h] ]
	 *				+
	 *				varFut [SUMATORIO(h=varNumAnualidades  +1 ) hasta (varDuracion -1) (1+ varPRP/100)^h ] 
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
	 * @return csp034
	 */
	private BigDecimal calculoFlujoNominal(final Integer varNumAnualidades, final BigDecimal varIFal, final BigDecimal varPas, final BigDecimal varFut, final Map<String, Object> mapVariables, final BigDecimal varPrp, final Integer varDur, final BigDecimal varPn0) {
		//Variables locales
		BigDecimal sumatorioVarPas = BigDecimal.ZERO;
		BigDecimal sumatorioVarFut = BigDecimal.ZERO;
		BigDecimal csp034 = BigDecimal.ZERO;
		//Fin variables locales
		
		/** Consultar funcional modulo CSP034
		 * CSP034(j) = varPN0 * (
					VarPas [SUMATORIO(h= 0) hasta (varNumAnualidades  )[(1+varIfal/100)^(h-varNumAnualidades )]* [(1+varPRP/100)^h] ]
					+
					varFut [SUMATORIO(h=varNumAnualidades  +1 ) hasta (varDuracion -1) (1+ varPRP/100)^h ] 
					)
		 */
		if (ModuloCSP034.LOG.isTraceEnabled()) {
			ModuloCSP034.LOG.trace("Inicio de la función << calculoFlujoNominal >> de la clase ModuloCSP034");
		}
		
		// Para mejorar rendimiento se expresa como "multiply" y no como "divide".
		final BigDecimal unoMasVarIFal = UtilModulos.getVarUnoMasVarIFal(mapVariables, ModuloCSP034.CLAVE_VAR_UNO_MAS_VAR_IFAL, varIFal);
		final BigDecimal unoMasVarPRP = UtilModulos.getVarUnoMasPrp(mapVariables, ModuloCSP034.CLAVE_VAR_UNO_MAS_VAR_PRP, varPrp);
		final BigDecimal prpDivIfal = UtilModulos.getPrpDivIfal(mapVariables, ModuloCSP034.CLAVE_VAR_PRP_DIV_IFAL, unoMasVarPRP, unoMasVarIFal);
		
		
		if (varPrp.signum()==0 && varIFal.signum()==0){
			sumatorioVarPas = BigDecimal.ONE;
		}else{
			BigDecimal valorIteracionPAS = Util.pow(unoMasVarIFal, varNumAnualidades);
			sumatorioVarPas = valorIteracionPAS;
			// Cálculo del sumatorio de varpas.
			for (int h = 1; h <= varNumAnualidades; h++) {
				valorIteracionPAS = valorIteracionPAS.multiply(prpDivIfal, ConstantsFunciones.MATH_CONTEXT);				
				sumatorioVarPas = sumatorioVarPas.add(valorIteracionPAS);
			}
		}
	
		if (varPrp.signum()==0){
			sumatorioVarFut = BigDecimal.ONE;
		}else{
			BigDecimal valorIteracionFUT = Util.pow(unoMasVarPRP, varNumAnualidades + 1);
			sumatorioVarFut = valorIteracionFUT;
			// Cálculo del sumatorio de varFut
			for (int h = varNumAnualidades + 2; h < varDur; h++) {
				valorIteracionFUT = valorIteracionFUT.multiply(unoMasVarPRP, ConstantsFunciones.MATH_CONTEXT);
				sumatorioVarFut = sumatorioVarFut.add(valorIteracionFUT);
			}
		}
		
		csp034 = UtilModulos.getVarPN0(mapVariables, CLAVE_VAR_PN0, varPn0).multiply(
				varPas.multiply(sumatorioVarPas).add(varFut.multiply(sumatorioVarFut)));
		
		if (ModuloCSP034.LOG.isTraceEnabled()) {
			ModuloCSP034.LOG.trace("Fin de la función << calculoFlujoNominal >> de la clase ModuloCSP034");
		}
		
		return csp034;
	}
}
