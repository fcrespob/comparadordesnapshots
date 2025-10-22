package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada de determinar, como importe nominal de una  corriente en un punto dado, 
 * el hallado previamente para ese mismo punto de esa misma corriente en la base técnica BTI.
 * 
 * @author rschacon
 *
 */
public class ModuloHASTANOMINALBTI implements Modulo {
	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloHASTANOMINALBTI.class);

	@Override
	public String getNombreServicio() {
		return ConstantsFactorias.MODULO_HAN_BTI;
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
			if (ModuloHASTANOMINALBTI.LOG.isTraceEnabled()) {
				ModuloHASTANOMINALBTI.LOG.trace("Inicio de execute en clase moduloHASTANOMINALBTI");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloGZC002
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			
			
			resultado = moduloHastaNominalBti(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloHASTANOMINALBTI.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloHASTANOMINALBTI.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloHASTANOMINALBTI.LOG.isTraceEnabled()) {
			ModuloHASTANOMINALBTI.LOG.trace("Fin de execute en clase moduloHASTANOMINALBTI");
		}
		
		return resultado;
	}
	
	/**
	 * El módulo HASTANOMINALBTI determina, como importe nominal de una  corriente en un punto dado, 
	 * el hallado previamente para ese mismo punto de esa misma corriente en la base técnica BTI.
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
	@SuppressWarnings("unchecked")
	private BigDecimal moduloHastaNominalBti(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal hastaNominal = BigDecimal.ZERO;
		List<DetalleCorriente> varUmicProyecBTI = null;
//		final IObtenerDatos servicio = FachadaServicios.getObtenerDatos();
		//Fin variables locales
		
		if (ModuloHASTANOMINALBTI.LOG.isTraceEnabled()) {
			ModuloHASTANOMINALBTI.LOG.trace("Inicio de la función << moduloBtiNominal >> de la clase ModuloHASTANOMINALBTI, para la iteracion = {}", iteracion);
		}
		
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if (iteracion.equals(ConstantsModulos.CTE_FIRST_ITER)) {
			//-	varUmicProyecBTI = obtenerDatos.recuperarProyBTI (proyUmic(j))  --> dejamos la variable disponible en memoria para el  subproceso de la umic
			//TODO: descomentar varUmicProyecBTI = servicio.recuperarProyBTI(periodo.getFcierre(), proyUmic.get(iteracion - 1).getTipoCorriente(), umic.getKey());
			//TODO: eliminar las lineas cuando se revise este módulo
			varUmicProyecBTI = new ArrayList<DetalleCorriente>();
			UtilModulos.getVarUmicProyecBTI(mapVariables, ConstantsFactorias.MODULO_HAN_BTI, varUmicProyecBTI);
			
			if (null == varUmicProyecBTI) {
				if (ModuloHASTANOMINALBTI.LOG.isDebugEnabled()) {
					ModuloHASTANOMINALBTI.LOG.debug(Util.errorValidacionA4(umic.getIdUmic()));
				}
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A4, new String[]{ConstantsFunciones.CTE_CLA_UMIC_REM, umic.getIdUmic()});
			}
		}
		
		// para periodos != 1
		varUmicProyecBTI = UtilModulos.getVarUmicProyecBTI(mapVariables, ConstantsFactorias.MODULO_HAN_BTI, null);
		
		/**
		 * Para cualquier periodo j, se establecerá que:
		 * proyUmic(j).impFlujoNominal = varUmicProyecBTI (j).impFlujoNominal
		 * proyUmic(j).FechaPago = varUmicProyecBTI (j).FechaPago
		 * proyUmic(j).FechaDevengo = varUmicProyecBTI (j).FechaDevengo
		 */
//		final DetalleCorriente det = varUmicProyecBTI.get(iteracion - 1);
//		bloqueCorriente.setImpFlujoNominal(bloqueCorriente.getImpFlujoNominal());
//		bloqueCorriente.setFechaPago(bloqueCorriente.getFechaPago());
//		bloqueCorriente.setFechaDevengo(bloqueCorriente.getFechaDevengo());
		
		if (ModuloHASTANOMINALBTI.LOG.isTraceEnabled()) {
			ModuloHASTANOMINALBTI.LOG.trace("Fin de la función << moduloBtiNominal >> de la clase ModuloHASTANOMINALBTI, para la iteracion = {}, con el detalleCorriente.impFlujoNominal = {}", iteracion, bloqueCorriente.getImpFlujoNominal());
		}
		
		return hastaNominal;
	}
}
