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
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada del cálculo que devuelve el capital revalorizable aritméticamente.
 * La expresión matemática para su determinación es la siguiente:
 * 		CSP(002,tc) = Capital(0)*(1+(n-1)*PRC/100)
 * 
 * @author apedro
 *
 */
public class ModuloCSP002 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP002.class);


	

	@Override
	public String getNombreServicio() {
		return ConstantsFactorias.MODULO_CSP002;
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
			if (ModuloCSP002.LOG.isTraceEnabled()) {
				ModuloCSP002.LOG.trace("Inicio de execute en clase ModuloCSP002");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloCSP002
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			
			//Invocamos a la función de calculo CSP002
			resultado = moduloCSP002(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
		} catch (Solvencia2Excepcion e) {
			ModuloCSP002.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP002.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP002.LOG.isTraceEnabled()) {
			ModuloCSP002.LOG.trace("Fin de execute en clase ModuloCSP002");
		}
		
		return resultado;
		
	}
	
	/**
	 * Módulo de cálculo para la cuantía del suceso de VIDA de la garantía principal. 
	 * Devuelve un capital revalorizable aritméticamente
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
	private BigDecimal moduloCSP002(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal csp002 = BigDecimal.ZERO;
		Timestamp varfecFin;
		Integer varN=0;
		BigDecimal vcapital;
		//Fin variables locales

		if (ModuloCSP002.LOG.isTraceEnabled()) {
			ModuloCSP002.LOG.trace("Inicio función << moduloCSP002 >> para la iteracion = {}", iteracion);
		}

		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if (null == bloqueCorriente.getFechaDevengo()) {
			return csp002;
		}
		
		
		
		varfecFin = umic.getFechas().getFecefecfin();
		if (varfecFin == null){
			varfecFin = proyUmic.get(proyUmic.size()-1).getFechaDesde();
		}
		
		if (umic.getDatosGenerales().getCsitupol().equals("AN")){
			if (ModuloCSP002.LOG.isDebugEnabled()) {
				ModuloCSP002.LOG.debug(Util.errorValidacionA7(umic.getIdUmic()));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A7, new String[]{umic.getIdUmic()});
		} else if (umic.getDatosGenerales().getCsitupol().equals("VI")){
			varN = umic.getDuraciones().getNdursegano();
		} else if (umic.getDatosGenerales().getCsitupol().equals("RE")){
			varN = FuncionesAuxiliares.tc(varfecFin, umic.getFechas().getFecefecred());
		}
		
		vcapital = umic.getCapitales().getIcapini();
		
		//Se calcula csp002 = vcapital * (1 + (N -1) * ( umic.capitales.porevalcap/100) )
		BigDecimal division = umic.getCapitales().getPorevalcap().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
		
		csp002 = vcapital.multiply(BigDecimal.ONE.add(BigDecimal.valueOf(varN-1).multiply(division)));

		

		if (ModuloCSP002.LOG.isTraceEnabled()) {
			ModuloCSP002.LOG.trace("Fin función << moduloCSP002 >> de la clase ModuloCSP002, para la iteracion = {}, con resultado capitalCartera = {}", iteracion, csp002);
		}
		
		return csp002;
	}

}
