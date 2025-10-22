package es.mapfre.solvencia.formulacion.modulos.impl;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;



/**
 * Clase encargada del cálculo que devuelve la Renta del Período conforme al plan de rentas de la Umic.
 * 
 * @author agonzalezgar
 *
 */
public class ModuloCSP238 implements Modulo {

	/** Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP238.class);

	@Override
	public String getNombreServicio() {
		return ConstantsFactorias.MODULO_CSP238;
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
			
			if (ModuloCSP238.LOG.isTraceEnabled()) {
				ModuloCSP238.LOG.trace("Inicio de execute en clase ModuloCSP238");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloCSP238
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			//final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			//final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			// Fin de la recuperación de los datos.
			
			//Invocamos a la funciónde calculo CSP238
			resultado = moduloCSP238(proyUmic, iteracion, fcalc, umic, btcUmic, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloCSP238.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP238.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP238.LOG.isTraceEnabled()) {
			ModuloCSP238.LOG.trace("Fin de execute en clase ModuloCSP238");
		}
		
		return resultado;

	}


	/** 
	 * Modulo de cálculo que devuelve la Renta del Período conforme al plan de rentas de la Umic.
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
	private BigDecimal moduloCSP238(final List<DetalleCorriente> proyUmic, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, String codSubproceso) {
		//Variables locales
		BigDecimal csp238 = BigDecimal.ZERO;
		BigDecimal impPago;
		//Fin variables locales

		if (ModuloCSP238.LOG.isTraceEnabled()) {
			ModuloCSP238.LOG.trace("Inicio función << moduloCSP238 >> de la clase ModuloCSP238, para la iteracion = {}",iteracion);
		}
		
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, fcalc, umic, btcUmic);

		/* csp238 = proyUmic.impPago */
		impPago = proyUmic.get(iteracion - 1).getImpPago();
		if ( null != impPago ) {
			csp238 = impPago;
		}

		
		if(codSubproceso.equals(ConstantsModulos.CTE_PROY_VIDA) && umic.getDatosGenerales().getKmodalidad().equals(ConstantsModulos.MOD_333)
				&& (umic.getDatosGenerales().getKprestacion().equals("REV43") || umic.getDatosGenerales().getKprestacion().equals("REO16") || umic.getDatosGenerales().getKprestacion().equals("REO17"))
				&& (umic.getDatosGenerales().getKbencon().equals(ConstantsModulos.CTE_KBENCON_BNC) || umic.getDatosGenerales().getKbencon().equals("101"))){
			
			csp238 = BigDecimal.ZERO;
		}
		if ((umic.getDatosGenerales().getKprestacion().equals("RS043") || umic.getDatosGenerales().getKprestacion().equals("RS044") || umic.getDatosGenerales().getKprestacion().equals("RS045"))
				&& umic.getDatosGenerales().getKbencon().equals(ConstantsModulos.CTE_KBENCON_BNC)) {
			csp238 = BigDecimal.ZERO;
		}
				
		if (ModuloCSP238.LOG.isTraceEnabled()) {
			ModuloCSP238.LOG.trace("Finc función << moduloCSP238 >> de la clase ModuloCSP238, para la iteracion = {}, con resultado csp238 = {}", iteracion, csp238);
		}
		
		//return csp238.setScale(ConstantsFunciones.CTE_2, RoundingMode.HALF_DOWN);
		return csp238;
	}

}
