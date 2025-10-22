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
 * Clase que implementa el modulo GZC001.
 * Clase encargada del cálculo que devuelve la cuantía nominal por Gastos de Administración en cada periodo de la Umic.
 * La expresión matemática para su determinación es la siguiente:
 * 				GZC(001, ZC) = Gipc * PU/äx:D¬/Npp 
 * @author agonzalezgar
 *
 */
public class ModuloGZC001 implements Modulo {

	

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloGZC001.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_GZC001;
	
	private static final String CLAVE_NPP = ConstantsModulos.CTE_VA_NPP;
	private static final String CLAVE_VAR_NPP = CLAVE_NPP.concat(CLAVE_MODULO);
	
	private static final String CLAVE_ASUBXD = ConstantsModulos.CTE_VAR_ASUBXD;
	private static final String CLAVE_VAR_ASUBXD = CLAVE_ASUBXD.concat(CLAVE_MODULO);
	
	private static final String CLAVE_CALCULO_GZC001 = "gzc001";
	
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
			if (ModuloGZC001.LOG.isTraceEnabled()) {
				ModuloGZC001.LOG.trace("Inicio de execute en clase ModuloGZC001");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloGZC001
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de los datos.
			
			//Se invoca a la función de calculo GZC001
			resultado = moduloGZC001(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloGZC001.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloGZC001.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloGZC001.LOG.isTraceEnabled()) {
			ModuloGZC001.LOG.trace("Fin de execute en clase ModuloGZC001");
		}
		
		return resultado;
	}
	
	/**
	 * Modulo de cálculo que devuelve la cuantía nominal por Gastos de Administración en cada periodo de la Umic.
	 * La expresión matemática para su determinación es la siguiente:
	 *				 GZC(001, ZC) = Gipc * PU/äx:D¬/Npp
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
	private BigDecimal moduloGZC001(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		//Variables locales
		BigDecimal gzc001 = BigDecimal.ZERO;
		BigDecimal varNpp = null;
		BigDecimal varGic;
		BigDecimal varPu;
		BigDecimal varAxD = BigDecimal.ONE;
		//Fin variables locales
		
		if (ModuloGZC001.LOG.isTraceEnabled()) {
			ModuloGZC001.LOG.trace("Inicio función << moduloGZC001 >> de la clase ModuloGZC001, para la  iteracion = {}", iteracion);
		}
		
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if (bloqueCorriente.getFechaDevengo() == null) {
			return BigDecimal.ZERO;
		}
		
		/**
			 * Si estoy en el primer periodo (j=1) se recuperarán los datos de la umic necesarios para el cálculo que no varían por periodo, así como las varibales internas que tampoco varían por periodo, y que se dejarán accesibles para su uso en el subproceso por los siguientes periodos a calcular. 
  
					Variables de Apoyo
						VarNpp --> obtenerConfiguracion.recuperarVariableApoyo(NPP)
						Si  VarNpp así obtenido es nulo ó no se ha recuperado variable, se asignará VarNpp = 12
					
					Variables Módulo
						varGic = btcUmic.gtoRosspCap;
						varPU --> umic.primas.iprimanetaini
					Para cualquier periodo j se obtendrán los datos necesarios:
						-	varäxD = äxD (proyUmic(j), varProyAxD(j), fcalc, umic)
					
					y se calculará GZC001 (j) como: 
						GZC001 (j) = [varGic* (varPU/varäxD)/(VarNpp)]
					Finalmente asignaremos la cuantía nominal del periodo como: 
					
					proyUmic(j).impFlujoNominal = GZC001 (j)
			 */
		if (!mapVariables.containsKey(CLAVE_CALCULO_GZC001)) {
			if (btcUmic.getGtorosspPrima().signum()==0){
				gzc001 = BigDecimal.ZERO;
			} else {
				
				if (umic.getDuraciones().getNdursegano() >= 1){
					//Obtenemos el valor de varAxD
					varAxD = UtilModulos.getVarASubXD(mapVariables, CLAVE_VAR_ASUBXD, proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso);	
				
				} // Si la duracion del seguro es 0, se toma varAxD = 1
				
				varNpp = UtilModulos.getVarNpp(mapVariables, CLAVE_VAR_NPP, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_NPP);
				varGic = btcUmic.getGtorosspPrima().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
				
				
				varPu = umic.getPrimas().getIprimanetaini(); 
				
				
				//GZC001 (j) = [varGic* (varPU/varäxD)/(VarNpp)]
				gzc001 = (varGic.multiply(varPu, ConstantsFunciones.MATH_CONTEXT)).divide(varNpp.multiply(varAxD, ConstantsFunciones.MATH_CONTEXT), ConstantsFunciones.MATH_CONTEXT);
			}
			
			mapVariables.put(CLAVE_CALCULO_GZC001, gzc001);
		} else {
			gzc001 = (BigDecimal) mapVariables.get(CLAVE_CALCULO_GZC001);
		}
		
			
		if (ModuloGZC001.LOG.isTraceEnabled()) {
			ModuloGZC001.LOG.trace("Fin función << moduloGZC001 >> de la clase ModuloGZC001, para la  iteracion = {}, con resultado gzc001 = {}", iteracion, gzc001);
		}
		
		return gzc001;
	}

}
