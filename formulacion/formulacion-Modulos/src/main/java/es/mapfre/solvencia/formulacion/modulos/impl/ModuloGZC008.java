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
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo GZC008.
 * Clase encargada del cálculo que devuelve la cuantía nominal por Gastos de Administración en cada periodo de la Umic.
 * La expresión matemática para su determinación es la siguiente:
 * 			GZC(008, ZC) = Gipc * PU/äx/Npp
 * @author agonzalezgar
 *
 */
public class ModuloGZC008 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloGZC008.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_GZC008;
	private static final String CLAVE_VAR_NPP = ConstantsModulos.CTE_VA_NPP.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_ASUBX = ConstantsModulos.CTE_VAR_ASUBX.concat(CLAVE_MODULO);
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
			
			if (ModuloGZC008.LOG.isTraceEnabled()) {
				ModuloGZC008.LOG.trace("Inicio de execute en clase ModuloGZC008");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloGZC008
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de los datos.
			
			//Invocamos a la función de calculo GZC008
			resultado = moduloGZC008(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloGZC008.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloGZC008.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloGZC008.LOG.isTraceEnabled()) {
			ModuloGZC008.LOG.trace("Fin de execute en clase ModuloGZC008");
		}
		
		return resultado;
	}
	
	/**Modulo de cálculo que devuelve la cuantía nominal por Gastos de Administración en cada periodo de la Umic.
	 * La expresión matemática para su determinación es la siguiente:
	 * 				GZC(008, ZC) = Gipc * PU/äx/Npp
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
	 */
	private BigDecimal moduloGZC008(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		//Variables locales
		BigDecimal gzc008 = BigDecimal.ZERO;
		BigDecimal varNpp = null;
		BigDecimal varGipc;
		BigDecimal varPU;
		BigDecimal varax;
		//Fin variables locales
		
		if (ModuloGZC008.LOG.isTraceEnabled()) {
			ModuloGZC008.LOG.trace("Inicio función << moduloGZC008 >> de la clase ModuloGZC008, para la  iteracion = {}", iteracion);
		}
		
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if (bloqueCorriente.getFechaDevengo() == null) {
			return gzc008;
		}
		
		/**
		 * Si estoy en el primer periodo (j=1) se recuperarán los datos de la umic necesarios para el cálculo que no varían por periodo,
		 * así como las varibales internas que tampoco varían por periodo, y que se dejarán accesibles
		 * para su uso en el subproceso por los siguientes periodos a calcular.
		 * 
		 * 		Variables de Apoyo
		 * 			VarNpp --> obtenerConfiguracion.recuperarVariableApoyo(NPP)
		 * 			Si  VarNpp así obtenido es nulo ó no se ha recuperado variable, se asignará VarNpp = 12
		 * 		
		 * 		Variables Módulo
		 * 			varGipc = btcUmic.gtoRosspPrima
		 * 			varPU --> umic.primas.iprimanetaini
		 * 			varProyAx = proyUmic
		 * 			varäx= äx (varProyAx, j, ,fcalc, umic, btcUmic, codSubproceso)
		 * 
		 * 			y GZC008 se calculará para cualquier periodo j como;
		 * 				GZC008 = [(varGipc * varPU * VarNpp) / (varäx)]
		 */
		gzc008 = (BigDecimal) mapVariables.get(CLAVE_MODULO);
		if (gzc008 == null) {
			varGipc = btcUmic.getGtorosspPrima();
			
			if(varGipc.signum()==0){
				gzc008 = BigDecimal.ZERO;
			} else {
				varPU = umic.getPrimas().getIprimanetaini();
				
				varNpp = UtilModulos.getVarNpp(mapVariables, CLAVE_VAR_NPP, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), ConstantsModulos.CTE_VA_NPP);
						
//				varax = UtilModulos.getVarASubX(mapVariables, CLAVE_VAR_ASUBX, proyUmic, bloqueCorriente, fcalc, umic, btcUmic, codSubproceso);
				varax = UtilModulos.getVarASubX(mapVariables, CLAVE_VAR_ASUBX, proyUmic, bloqueCorriente, umic.getFechas().getFecinisus(), umic, btcUmic, codSubproceso);
				
				//GZC008 = [((varGipc/100) * varPU ) / (varäx * VarNpp)]
				gzc008 = varGipc.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01).multiply(varPU, ConstantsFunciones.MATH_CONTEXT).divide(varax.multiply(varNpp, ConstantsFunciones.MATH_CONTEXT), ConstantsFunciones.MATH_CONTEXT);				
			}
			mapVariables.put(CLAVE_MODULO, gzc008);
		}
		
		if (ModuloGZC008.LOG.isTraceEnabled()) {
			ModuloGZC008.LOG.trace("Fin función << moduloGZC008 >> de la clase ModuloGZC008, para la  iteracion = {}, con resultado gzc008 = {}", iteracion, gzc008);
		}
		
		return gzc008;
	}
}
