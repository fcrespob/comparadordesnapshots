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
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada del calculo que devuelve la cuantía del suceso de FALLECIMIENTO de la garantía principal. 
 * La expresión matemática para su determinación es la siguiente:
 * 			Si Estado=Vigor entonces:
 *				CSP(063,tc)= CSP(033,TC)
 *			Si Estado=Reducida entonces:
 *				CSP(063,tc)= CSP(005,TC)
 *
 * @author apedro
 *
 */
public class ModuloCSP063 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP063.class);

	@Override
	public String getNombreServicio() {
		return ConstantsFactorias.MODULO_CSP063;
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
			if (ModuloCSP063.LOG.isTraceEnabled()) {
				ModuloCSP063.LOG.trace("Inicio de execute en clase ModuloCSP063");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloCSP063.
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			
			
			//Invocamos a la función moduloCSP063
			resultado = moduloCSP063(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
		} catch (Solvencia2Excepcion e) {
			ModuloCSP063.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP063.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP063.LOG.isTraceEnabled()) {
			ModuloCSP063.LOG.trace("Fin de execute en clase ModuloCSP063");
		}
		
		return resultado;
	}
	
	/**
	 * Modulo de calculo que devuelve la cuantía del suceso de FALLECIMIENTO de la garantía principal. 
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
	private BigDecimal moduloCSP063(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal csp0063 = BigDecimal.ZERO;
		Modulo moduloNominal = null;
		String moduloRecuperado = ConstantsFunciones.CTE_CADENA_VACIA;
		//Fin variables locales
		
		if (ModuloCSP063.LOG.isTraceEnabled()) {
			ModuloCSP063.LOG.trace("Inicio función << moduloCSP063 >> de la clase ModuloCSP063, para la iteracion = {}", iteracion);
		}
		
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if (bloqueCorriente.getFechaDevengo() == null){
			return csp0063;
		}
		
		/**
		 * Si la póliza está anulada, es decir  umic.datosGenerales.csitupol=  ‘AN’
				o	Se deberá retornar error funcional 004 – Umic en estado Anulado & claveUmic
		   Si la póliza no está reducida, es decir  umic.datosGenerales.csitupol=  ‘VI’
				o	Se invocará al módulo CSP033.
		   Si la póliza está reducida, es decir  umic.datosGenerales.csitupol=  ‘RE’
				o	Se invocará al módulo CSP005.
		 */
		if (ConstantsModulos.CTE_DG_CSITU_ANU.equals(umic.getDatosGenerales().getCsitupol())) {
			if (ModuloCSP063.LOG.isDebugEnabled()) {
				ModuloCSP063.LOG.debug(Util.errorValidacionA7(umic.getIdUmic()));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A7, new String[]{umic.getIdUmic()});
		} else if (ConstantsModulos.CTE_DG_CSI_NO_RED.equals(umic.getDatosGenerales().getCsitupol())) {
			moduloRecuperado = ConstantsFactorias.MODULO_CSP033;
		} else if (ConstantsModulos.CTE_DG_CSITU_RED.equals(umic.getDatosGenerales().getCsitupol())) {
			moduloRecuperado = ConstantsFactorias.MODULO_CSP005;
		}
		
		moduloNominal = FactoriaModulos.getModulo(moduloRecuperado);
		csp0063 = (BigDecimal) moduloNominal.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
		
		if (ModuloCSP063.LOG.isTraceEnabled()) {
			ModuloCSP063.LOG.trace("Fin función << moduloCSP063 >> de la clase ModuloCSP063, para la iteracion = {}, con resultado csp0063 = {}", iteracion, csp0063);
		}
		
		return csp0063;
	}

}
