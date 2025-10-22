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
 * Clase que implementa el modulo GZC014.
 * Clase encargada del cálculo que devuelve la cuantía nominal por Gastos de Administración en cada periodo de la Umic
 * La expresión matemática para su determinación es la siguiente:
 * 				GZC(014, ZC) = (Gic/Npp) * CSP(xxx, tc)
 * @author apedro
 *
 */
public class ModuloGZC014 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloGZC014.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_GZC014;
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 
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
			
			if (ModuloGZC014.LOG.isTraceEnabled()) {
				ModuloGZC014.LOG.trace("Inicio de execute en clase ModuloGZC014");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloGZC014
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			// Fin de la recuperación de los datos.
			
			//Invocamos a la función de calculo moduloGZC014
			resultado = moduloGZC014(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloGZC014.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloGZC014.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloGZC014.LOG.isTraceEnabled()) {
			ModuloGZC014.LOG.trace("Fin de execute en clase ModuloGZC014");
		}
		
		return resultado;
	}
	/**
	 * Modulo de cálculo que devuelve la cuantía nominal por Gastos de Administración en cada periodo de la Umic.
	 * La expresión matemática para su determinación es la siguiente:
	 * 				GZC(014, ZC) = (Gic/Npp) * CSP(xxx, tc)
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
	private BigDecimal moduloGZC014(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal gzc014 = BigDecimal.ZERO;
		BigDecimal varGic;
		BigDecimal varCsp;
		Timestamp varFechaEfecto = null;
		//Fin variables locales
		
		if (ModuloGZC014.LOG.isTraceEnabled()) {
			ModuloGZC014.LOG.trace("Inicio función << moduloGZC014 >> de la clase ModuloGZC014, para la iteracion = {}", iteracion);
		}
		
		if (bloqueCorriente.getFechaDevengo() == null) {
			return gzc014;
		}
		
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		
		
		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		varGic = btcUmic.getGtorosspCap();
		varCsp = umic.getCapitales().getIcapact();
		

		// Si mes (proyUmic(j). varBloque.fecDevengo) = Mes(varfechaEfecto): varP = 1 --> GZC014 = varGic/100  * varCsp
		if (UtilFechas.getMes(bloqueCorriente.getFechaDevengo()) == UtilFechas.getMes(varFechaEfecto)){
			gzc014 = varGic.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01).multiply(varCsp, ConstantsFunciones.MATH_CONTEXT);
			
		} // Sino varP = 0 --> GZC014 = 0
		
		if (ModuloGZC014.LOG.isTraceEnabled()) {
			ModuloGZC014.LOG.trace("Fin función << moduloGZC014 >> de la clase ModuloGZC014, para la iteracion = {} con resultado gzc014 = {}", iteracion, gzc014);
		}
			
		return gzc014;
	}

}
