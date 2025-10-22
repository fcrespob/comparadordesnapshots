package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.DatosEspecificos;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
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
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada del cálculo que devuelve los Capitales de Prestación Diferido. 
 * Se trata de una versión del actual módulo CSP001 que se encarga de devolver el capital almacenado en cartera, 
 * con una pequeña particularidad, este módulo nos valdrá para indicar si en la proyección calculada hay derecho o no 
 * a la prestación.       
 * La expresión matemática para su determinación es la siguiente:
 * 		CSP(006) = CSP001
 * 
 * @author apedro
 *
 */
public class ModuloCSP006 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP006.class);
	
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP006;
	private static final String CLAVE_VAR_FDIFER = ConstantsModulos.CTE_VAR_FDIFER.concat(CLAVE_MODULO);

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
			if (ModuloCSP006.LOG.isTraceEnabled()) {
				ModuloCSP006.LOG.trace("Inicio de execute en clase ModuloCSP006");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloGZC006
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			//Invocamos a la función de calculo CSP006
			resultado = moduloCSP006(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
		} catch (Solvencia2Excepcion e) {
			ModuloCSP006.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP006.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP006.LOG.isTraceEnabled()) {
			ModuloCSP006.LOG.trace("Fin de execute en clase ModuloCSP006");
		}
		
		return resultado;
		
	}
	
	/**
	 * Modulo de cálculo que devuelve los Capitales de Prestación Diferido.
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
	private BigDecimal moduloCSP006(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal csp006 = BigDecimal.ZERO;
		Timestamp Fdiferimiento;
		String varFdiferimiento;
		Modulo moduloCSP001 = null;
		//Fin variables locales

		if (ModuloCSP006.LOG.isTraceEnabled()) {
			ModuloCSP006.LOG.trace("Inicio función << moduloCSP006 >> para la iteracion = {}", iteracion);
		}

		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if (null == bloqueCorriente.getFechaDevengo()) {
			return csp006;
		}
		
		DatosGenerales datosGenerales = umic.getDatosGenerales();
				
		varFdiferimiento = UtilModulos.getDatosEspecificosUmic(mapVariables, 
				   CLAVE_VAR_FDIFER,
				   umic.getDatosGenerales().getKpoliza()	 , 
				   umic.getDatosGenerales().getKsubpoliza()  ,
				   umic.getDatosGenerales().getKcertificado(),
				   umic.getDatosGenerales().getNsuscri(),
				   "C1FDI");
		
		if(null == varFdiferimiento){
			
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{ConstantsModulos.CTE_VAR_FDIFER});
			
		}else{
			varFdiferimiento = varFdiferimiento.substring(5, 13);
			SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyyMMdd");
			try {
				Date fechaParseada = formatoFecha.parse(varFdiferimiento);
				Fdiferimiento = new Timestamp(fechaParseada.getTime());

			} catch (ParseException e) {
				//Si el formato no es el esperado se lanza excepción.
				throw Solvencia2ExcepcionHelper.crearExcepcion("AP", new String[]{varFdiferimiento, "yyyyMMdd"});
			}
		}
		
		if (bloqueCorriente.getFechaDevengo().after(Fdiferimiento)){
			
			moduloCSP001 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP001);
			csp006 = (BigDecimal) moduloCSP001.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic);
		}

		if (ModuloCSP006.LOG.isTraceEnabled()) {
			ModuloCSP006.LOG.trace("Fin función << moduloCSP006 >> de la clase ModuloCSP006, para la iteracion = {}, con resultado csp006 = {}", iteracion, csp006);
		}
		
		return csp006;
	}

}
