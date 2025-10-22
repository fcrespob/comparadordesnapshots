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
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo GZC002.
 * Clase encargada del cálculo que devuelve la cuantía nominal por Gastos de Administración en cada periodo de la Umic
 * La expresión matemática para su determinación es la siguiente:
 * 				GZC(002, ZC) = (Gic/Npp) * CSP(xxx, tc)
 * @author agonzalezgar
 *
 */
public class ModuloGZC002 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloGZC002.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_GZC002;
	
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 
//	private static final String CLAVE_VAR_TCM = ConstantsModulos.CTE_VAR_TCM.concat(CLAVE_MODULO);
//	private static final String CLAVE_VAR_CSP = ConstantsModulos.CTE_VAR_CSP.concat(CLAVE_MODULO);
	// Fin de las variables estáticas usadas para agilizar operaciones.

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
			
			if (ModuloGZC002.LOG.isTraceEnabled()) {
				ModuloGZC002.LOG.trace("Inicio de execute en clase ModuloGZC002");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloGZC002
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			// Fin de la recuperación de los datos.
			
			//Invocamos a la función de calculo moduloGZC002
			resultado = moduloGZC002(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloGZC002.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloGZC002.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloGZC002.LOG.isTraceEnabled()) {
			ModuloGZC002.LOG.trace("Fin de execute en clase ModuloGZC002");
		}
		
		return resultado;
	}
	/**
	 * Modulo de cálculo que devuelve la cuantía nominal por Gastos de Administración en cada periodo de la Umic.
	 * La expresión matemática para su determinación es la siguiente:
	 * 				GZC(002, ZC) = (Gic/Npp) * CSP(xxx, tc)
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
	 * @return BigDecimal
	 */
	private BigDecimal moduloGZC002(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal gzc002 = BigDecimal.ZERO;
		Timestamp varFechaEfecto = null;
		//Fin variables locales
		
		if (ModuloGZC002.LOG.isTraceEnabled()) {
			ModuloGZC002.LOG.trace("Inicio función << moduloGZC002 >> de la clase ModuloGZC002, para la iteracion = {}", iteracion);
		}
		
		if (bloqueCorriente.getFechaDevengo() == null) {
			return gzc002;
		}
		
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		/**
		 * Si estoy en el primer periodo (j=1) se recuperarán los datos de la umic necesarios para el cálculo que no varían por periodo, 
		 * así como las varibales internas que tampoco varían por periodo, y que se dejarán accesibles para su uso en el subproceso 
		 * por los siguientes periodos a calcular. 
		 * 		Variables Módulo
		 * 		-	varGic = btcUmic.gtoRosspCap
		 * 		-	varCsp = proyUmic(size -1).corrienteVida.impFlujoNominal
		 * 		-	varTCm = TCm(umic.fechas.fecinisus, proyUmic(1).fecCalculo)
		 * 	
		 * 	Para cualquier periodo j se calculará GZC002 (j) como:
		 * 		-	Si  proyUmic(j). varBloque.fecDevengo no es nula:
		 * 			-	varβ = TCM(proyUmic(j).varBloque.fecDevengo, proyUmic(j).fecCierre)
		 * 			-	varPP = MOD(varTCm + varβ,12)
		 * 			-	Si varPP = 0 --> GZC002(j) = (varGic/100) * varCsp
		 * 			-	Si varPP <> 0 --> GZC002(j) = 0
		 */
		
		
		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		

		final BigDecimal varCsp = umic.getCapitales().getIcapini();
		

		// Si mes (proyUmic(j). varBloque.fecDevengo) = Mes(varfechaEfecto): varP = 1 --> GZC002 = varGic/100  * varCsp
		if (UtilFechas.getMes(bloqueCorriente.getFechaDevengo()) == UtilFechas.getMes(varFechaEfecto)){
			gzc002 = btcUmic.getGtorosspCap().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01).multiply(varCsp, ConstantsFunciones.MATH_CONTEXT);
			
		} // Sino varP = 0 --> GZC002 = 0
		
		if (ModuloGZC002.LOG.isTraceEnabled()) {
			ModuloGZC002.LOG.trace("Fin función << moduloGZC002 >> de la clase ModuloGZC002, para la iteracion = {} con resultado gzc002 = {}", iteracion, gzc002);
		}
			
		return gzc002;
	}

}
