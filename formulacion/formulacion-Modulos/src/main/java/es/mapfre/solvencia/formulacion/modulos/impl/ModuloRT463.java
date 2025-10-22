package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FlujosProbables;
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
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/** 
 *
 * @author Aleksandar Plamenov Nedyalkov
 *
 */

public class ModuloRT463 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloRT463.class);
	
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_RT463;
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 
	private IObtenerConfiguracion obtenerConfiguracion = FachadaServicios.getObtenerConfiguracion();
	private static final String CLAVE_LST_PROY = ConstantsModulos.CTE_LST_PROY.concat(CLAVE_MODULO);

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
			if (ModuloRT463.LOG.isTraceEnabled()) {
				ModuloRT463.LOG.trace("Inicio de execute en clase ModuloRT463");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloRTPMAT
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			
			//Invocamos a la función ModuloRTPMAT
			resultado = moduloRT463(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloRT463.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloRT463.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloRT463.LOG.isTraceEnabled()) {
			ModuloRT463.LOG.trace("Fin de execute en clase ModuloRT463");
		}
		
		return resultado;
	}
	/**
	 * Módulo de cálculo de la cuantía por Rescate. 
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
	private BigDecimal moduloRT463(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {

		BigDecimal rtp463 = BigDecimal.ZERO;
		Timestamp varfechaEfecto;
		List<DetalleCorriente> varProyCsp463 = null;
		Modulo moduloCSP463;
		BigDecimal varCsp463 = BigDecimal.ZERO;
		List<DetalleCorriente> varListaCorrienteUmic = null;
		BigDecimal varProvBti = BigDecimal.ZERO;
		
		if (ModuloRT463.LOG.isTraceEnabled()) {
			ModuloRT463.LOG.trace("Inicio función << ModuloRT463 >> de la clase ModuloRT463, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
        varfechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
        moduloCSP463 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP463);
        
		final String codk1 = umic.getRescates().getKrescate1();
		final Integer durk1 = UtilModulos.obtenerDuracionCodKX(codk1, varfechaEfecto, bloqueCorriente.getFechaDevengo(), 
				umic.getDuraciones().getNdurprima(), umic.getDuraciones().getNdursegmes());
		
		final BigDecimal vark1 = UtilModulos.getCteRescate(mapVariables, codk1, durk1);
		
		// vark1 depende de durk1; durk1 puede variar entre periodos luego la validación hay que hacerla en cada iteración.
		ValidacionesComunesModulos.validarCteRescateVarkx(codk1, vark1);
		
		//Se Recuperará la proyección previamente calculada en BTI para la umic
		varListaCorrienteUmic = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PROY, ConstantsModulos.CTE_BTI, btcUmic.getFecCierre(), umic.getKey());
		
		if (bloqueCorriente.getFechaDevengo() != null){
			
			//Se llama al modulo csp463 para obtener el valor
			varProyCsp463 = proyUmic;
//			varCsp463 = (BigDecimal) moduloCSP463.execute(varProyCsp463, bloqueCorriente, iteracion, fcalc, umic,
//					btcUmic, mapVariables, codSubproceso);
			
			varCsp463 = proyUmic.get(iteracion - 1).getBloqueFall().getImpFlujoNominal();
			
			FlujosProbables flujoProbable = obtenerConfiguracion.recuperarConfProv(umic.getDatosGenerales().getKmodalidad(),
					umic.getDatosGenerales().getKgarantia(), umic.getDatosAdicionales().getPrestCal(), ConstantsModulos.CTE_BTI);
			
			if(null == varListaCorrienteUmic 
					|| varListaCorrienteUmic.isEmpty() 
					|| varListaCorrienteUmic.get(iteracion - 1).getBt().equals(ConstantsModulos.CTE_BTI_PROY)){
				mapVariables.remove(CLAVE_LST_PROY);
				varListaCorrienteUmic = UtilModulos
						.getListaCorrienteUmic(mapVariables, CLAVE_LST_PROY,
								ConstantsModulos.CTE_BTI_PROY, btcUmic.getFecCierre(),
								umic.getKey());
				flujoProbable = obtenerConfiguracion.recuperarConfProv(umic.getDatosGenerales().getKmodalidad(),
						umic.getDatosGenerales().getKgarantia(), umic.getDatosAdicionales().getPrestCal(), ConstantsModulos.CTE_BTI_PROY);
			}
			
			if (flujoProbable.getProvNominal().equals(ConstantsFactorias.MODULO_PMRR01)
					|| flujoProbable.getProvNominal().equals(ConstantsFactorias.MODULO_PMRR02)) {
				varProvBti = varListaCorrienteUmic.get(iteracion - 1).getTotalFlujoProyeccion().getProvbtiproy();
			} else {
				varProvBti = varListaCorrienteUmic.get(iteracion - 1).getTotalFlujoProyeccion().getTerminalAnterior();
			}
			
			if (vark1.equals(null)) {
				if (varCsp463.compareTo(varProvBti) < 0) {
					rtp463 = varCsp463.multiply(BigDecimal.ONE);
				} else {
					rtp463 = varProvBti.multiply(BigDecimal.ONE);
				}
			}else {
				if (varCsp463.compareTo(varProvBti) < 0) {
					rtp463 = varCsp463.multiply(vark1);	
				} else {
					rtp463 = varProvBti.multiply(vark1);	
				}
			}
			
		}	
		
		if (ModuloRT463.LOG.isTraceEnabled()) {
			ModuloRT463.LOG.trace("Fin función << ModuloRT463 >> de la clase ModuloRT463, para la iteracion = {} con resultado rt463 = {}", iteracion, rtp463);
		}
			
		return rtp463;
	}
}
