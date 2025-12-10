package es.mapfre.solvencia.formulacion.modulos.impl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
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
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * 
 * @author Aleksandar Plamenov Nedyalkov
 *
 */

public class ModuloVARBVIT implements Modulo {
	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVARBVIT.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VARBVIT;
	private static final String CLAVE_VAR_UMIC_COPIA = ConstantsModulos.CTE_VA_UMIC_COPIA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_BTCUMIC_COPIA = ConstantsModulos.CTE_VA_BTCUMIC_COPIA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_UMIC_COPIA2 = ConstantsModulos.CTE_VA_UMIC_COPIA2.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_BTCUMIC_COPIA2 = ConstantsModulos.CTE_VA_BTCUMIC_COPIA2.concat(CLAVE_MODULO);
	private static final String VAR_BTCUMIC_COPIADA = "SW_BTCUMIC";
	private static final String VAR_BTCUMIC2_COPIADA = "SW_BTUMIC2";
	private static final String CLAVE_VAR_BTCUMIC_COPIADA = VAR_BTCUMIC_COPIADA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_BTCUMIC2_COPIADA = VAR_BTCUMIC2_COPIADA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_EDAD_CAL = ConstantsModulos.CTE_EDAD_CAL.concat(CLAVE_MODULO);

	// Fin de las variables estáticas usadas para agilizar operaciones.
	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}

	/**
	 * Función encargada de obtener los parámetros necesarios y de realizar la
	 * llamada a la función que realiza los calculos del modulo.
	 */
	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {
		// Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		// Fin variables locales

		try {
			if (ModuloVARBVIT.LOG.isTraceEnabled()) {
				ModuloVARBVIT.LOG.trace("Inicio de execute en clase ModuloVRTAVIT");
			}

			// Recuperamos los datos que le pasaremos a la función moduloVRTAVIT
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];

			// Invocamos a la función ModuloVRTAVIT
			resultado = moduloVARBVIT(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables,
					codSubproceso);

		} catch (Solvencia2Excepcion e) {
			ModuloVARBVIT.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVARBVIT.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloVARBVIT.LOG.isTraceEnabled()) {
			ModuloVARBVIT.LOG.trace("Fin de execute en clase ModuloVRTAVIT");
		}

		return resultado;
	}

	private BigDecimal moduloVARBVIT(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, Map<String, Object> mapVariables,
			String codSubproceso) {

		BigDecimal varbVIT = BigDecimal.ZERO;
		Umic varUmicCopia;
		DetalleBaseTecnica varbtcUmicCopia;
		Umic varUmicCopia2;
		DetalleBaseTecnica varbtcUmicCopia2;
		
		Integer varIndAsegOrigen = 0;
		Integer varIndAsegDestino = 0;
		Integer varindTablaOrigen = 0;
		Integer varindTablaDestino = 0;
		Integer varIndAsegOrigen2 = 0;
		Integer varIndAsegDestino2 = 0;
		Integer varindTablaOrigen2 = 0;
		Integer varindTablaDestino2 = 0;
		
		String tablaOrigen;
		Timestamp fNacAsegOrigen;
		String sexAsegOrigen;
		Integer edadAsegOrigen;
		
		Modulo moduloVZC;
		BigDecimal varVzc;
		BigDecimal varVzc2;
		BigDecimal varVzc3, varVzc3hijo;
		Modulo moduloFZC;
		BigDecimal varFZC = BigDecimal.ZERO;
		Modulo moduloFzc1Fzc2;
		BigDecimal varFzc1Fzc2;
		Modulo moduloFzc1;
		BigDecimal varFzc1, varFzc2;
		String sw_btcumic_copiada;
		String sw_btcumic2_copiada;
		
		List<DetalleCorriente> varProyVZC;
		List<DetalleCorriente> varProyVZC2;
		List<DetalleCorriente> varProyVZC3, varProyVZC3hijo ;
		List<DetalleCorriente> varProyFZC;
		List<DetalleCorriente> varProyFZC1FZC2;
		List<DetalleCorriente> varProyFZC1;

		if (ModuloVARBVIT.LOG.isTraceEnabled()) {
			ModuloVARBVIT.LOG.trace(
					"Inicio función << ModuloVARBVIT >> de la clase ModuloVARBVIT, para la iteracion = {}", iteracion);
		}

		// Validamos los parametros de entrada 
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		
		if(bloqueCorriente.getFechaDevengo() == null){
			return varbVIT;
		}
		
		varUmicCopia = (Umic) mapVariables.get(CLAVE_VAR_UMIC_COPIA);
		if(null == varUmicCopia){
			varUmicCopia = new Umic();
			try {
				PropertyUtils.copyProperties(varUmicCopia, umic);
			} catch (Exception e) {
				ModuloVARBVIT.LOG.error(e.getMessage());
			}
			mapVariables.put(CLAVE_VAR_UMIC_COPIA, varUmicCopia);

		}
		
		varUmicCopia2 = (Umic) mapVariables.get(CLAVE_VAR_UMIC_COPIA2);
		if(null == varUmicCopia2){
			varUmicCopia2 = new Umic();
			try {
				PropertyUtils.copyProperties(varUmicCopia2, umic);
			} catch (Exception e) {
				ModuloVARBVIT.LOG.error(e.getMessage());
			}
			mapVariables.put(CLAVE_VAR_UMIC_COPIA2, varUmicCopia2);
		}
		
		varbtcUmicCopia = (DetalleBaseTecnica) mapVariables.get(CLAVE_VAR_BTCUMIC_COPIA);
		if(null == varbtcUmicCopia){
			try {
				varbtcUmicCopia = new DetalleBaseTecnica();
				PropertyUtils.copyProperties(varbtcUmicCopia, btcUmic);
			} catch (Exception e) {
				ModuloVARBVIT.LOG.error(e.getMessage());
			}
			mapVariables.put(CLAVE_VAR_BTCUMIC_COPIA, varbtcUmicCopia);

		}
		
		
		varbtcUmicCopia2 = (DetalleBaseTecnica) mapVariables.get(CLAVE_VAR_BTCUMIC_COPIA2);
		if(null == varbtcUmicCopia2){
			try {
				varbtcUmicCopia2 = new DetalleBaseTecnica();
				PropertyUtils.copyProperties(varbtcUmicCopia2, btcUmic);
			} catch (Exception e) {
				ModuloVARBVIT.LOG.error(e.getMessage());
			}
			mapVariables.put(CLAVE_VAR_BTCUMIC_COPIA2, varbtcUmicCopia2);

		}
		
			if (umic.getDatosGenerales().getNorden().toString().substring(0, 1)
					.equals(ConstantsFunciones.CTE_1_STRING)) {
				
				varProyVZC = proyUmic;
				
				moduloVZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);

				varVzc = (BigDecimal) moduloVZC.execute(varProyVZC, bloqueCorriente, iteracion, fcalc, umic, btcUmic,
						mapVariables, codSubproceso);

				varbVIT = varVzc;
			} else if (umic.getDatosGenerales().getNorden().toString().substring(0, 1)
					.equals(ConstantsFunciones.CTE_2_STRING)) {

			if (umic.getDatosGenerales().getNorden().toString().substring(1, 2)
					.equals(ConstantsFunciones.CTE_3_STRING)) {

				//COMPROBAR SI NO HAY SEGUNDO QUE NO HAY ASEGURADO PARA EL CALCULO.
				
					sw_btcumic_copiada = (String) mapVariables.get(CLAVE_VAR_BTCUMIC_COPIADA);
					if (null == sw_btcumic_copiada) {
						
						if(null != btcUmic.getTablacalc1aseg4() && null != umic.getAsegurados().getFnacAseg4() && null != umic.getAsegurados().getCsexAseg4() && null != umic.getAsegurados().getEdadAseg4()){
							
							varIndAsegOrigen = 4;
							varIndAsegDestino = 1;
							tablaOrigen = btcUmic.getTablacalc1aseg4();
							fNacAsegOrigen = umic.getAsegurados().getFnacAseg4();
							sexAsegOrigen = umic.getAsegurados().getCsexAseg4();
							edadAsegOrigen = umic.getAsegurados().getEdadAseg4();
							UtilModulos.fSobreescribirAsegurado(varUmicCopia, varbtcUmicCopia, tablaOrigen,
								varIndAsegOrigen, fNacAsegOrigen, sexAsegOrigen, edadAsegOrigen);
							varbtcUmicCopia.setTablaBaseExpList(btcUmic.getTablaBaseExpList());
							mapVariables.put(CLAVE_VAR_BTCUMIC_COPIA, varbtcUmicCopia);
							mapVariables.put(CLAVE_VAR_BTCUMIC_COPIADA, "S");
							mapVariables.put("VZCASEG", IObtenerConfiguracion.OrdenAsegurado.ASEG4);
							
						}else if(null != btcUmic.getTablacalc1aseg4() && null != umic.getAsegurados().getFnacAseg4() && null != umic.getAsegurados().getCsexAseg4() && null != umic.getAsegurados().getEdadAseg4()){
						
							varIndAsegOrigen = 3;
							varIndAsegDestino = 1;
							tablaOrigen = btcUmic.getTablacalc1aseg3();
							fNacAsegOrigen = umic.getAsegurados().getFnacAseg3();
							sexAsegOrigen = umic.getAsegurados().getCsexAseg3();
							edadAsegOrigen = umic.getAsegurados().getEdadAseg3();
							UtilModulos.fSobreescribirAsegurado(varUmicCopia, varbtcUmicCopia, tablaOrigen,
								varIndAsegOrigen, fNacAsegOrigen, sexAsegOrigen, edadAsegOrigen);
							varbtcUmicCopia.setTablaBaseExpList(btcUmic.getTablaBaseExpList());
							mapVariables.put(CLAVE_VAR_BTCUMIC_COPIA, varbtcUmicCopia);
							mapVariables.put(CLAVE_VAR_BTCUMIC_COPIADA, "S");
							mapVariables.put("VZCASEG", IObtenerConfiguracion.OrdenAsegurado.ASEG3);
						}else{
							varIndAsegOrigen = 2;
							varIndAsegDestino = 1;
							tablaOrigen = btcUmic.getTablacalc1aseg2();
							fNacAsegOrigen = umic.getAsegurados().getFnacAseg2();
							sexAsegOrigen = umic.getAsegurados().getCsexAseg2();
							edadAsegOrigen = umic.getAsegurados().getEdadAseg2();
							UtilModulos.fSobreescribirAsegurado(varUmicCopia, varbtcUmicCopia, tablaOrigen,
								varIndAsegOrigen, fNacAsegOrigen, sexAsegOrigen, edadAsegOrigen);
							varbtcUmicCopia.setTablaBaseExpList(btcUmic.getTablaBaseExpList());
							mapVariables.put(CLAVE_VAR_BTCUMIC_COPIA, varbtcUmicCopia);
							mapVariables.put(CLAVE_VAR_BTCUMIC_COPIADA, "S");
							mapVariables.put("VZCASEG", IObtenerConfiguracion.OrdenAsegurado.ASEG2);
						}
					}
					varProyVZC3hijo = proyUmic;
					moduloVZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);
					varVzc3hijo = (BigDecimal) moduloVZC.execute(varProyVZC3hijo, bloqueCorriente, iteracion, fcalc,
							varUmicCopia, varbtcUmicCopia, mapVariables, codSubproceso);

					if (umic.getOtrosDatos().getCestadoAseg1().equals(ConstantsModulos.CTE_CESTADO_ASEG_A)) {
						varbVIT = varVzc3hijo;
					} else {
						varProyFZC = proyUmic;
						mapVariables.put("VZCASEG", IObtenerConfiguracion.OrdenAsegurado.ASEG1);
						moduloFZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FZC);
						varFZC = (BigDecimal) moduloFZC.execute(varProyFZC, bloqueCorriente, iteracion, fcalc, umic,
								btcUmic, mapVariables, codSubproceso);

						varbVIT = varVzc3hijo.multiply(varFZC);
					}
				
			} else {
				
				if (umic.getDatosGenerales().getNorden().toString().substring(1, 2)
						.equals(ConstantsFunciones.CTE_2_STRING) ) {
					sw_btcumic_copiada = (String) mapVariables.get(CLAVE_VAR_BTCUMIC_COPIADA);
					if (null == sw_btcumic_copiada) {
						
						if(null != umic.getAsegurados().getFnacAseg3() &&
						   null != umic.getAsegurados().getCsexAseg3() && 
						   null != umic.getAsegurados().getEdadAseg3()){
				
							varIndAsegOrigen = 3;
							varIndAsegDestino = 1;
							tablaOrigen = btcUmic.getTablacalc1aseg3();
							fNacAsegOrigen = umic.getAsegurados().getFnacAseg3();
							sexAsegOrigen = umic.getAsegurados().getCsexAseg3();
							edadAsegOrigen = umic.getAsegurados().getEdadAseg3();
							UtilModulos.fSobreescribirAsegurado(varUmicCopia, varbtcUmicCopia, tablaOrigen, varIndAsegOrigen,
									fNacAsegOrigen, sexAsegOrigen, edadAsegOrigen);
							varbtcUmicCopia.setTablaBaseExpList(btcUmic.getTablaBaseExpList());
							mapVariables.put(CLAVE_VAR_BTCUMIC_COPIA, varbtcUmicCopia);
							mapVariables.put(CLAVE_VAR_BTCUMIC_COPIADA, "S");
							mapVariables.put("VZCASEG", IObtenerConfiguracion.OrdenAsegurado.ASEG3);
							
						}else{
							varIndAsegOrigen = 2;
							varIndAsegDestino = 1;
							tablaOrigen = btcUmic.getTablacalc1aseg2();
							fNacAsegOrigen = umic.getAsegurados().getFnacAseg2();
							sexAsegOrigen = umic.getAsegurados().getCsexAseg2();
							edadAsegOrigen = umic.getAsegurados().getEdadAseg2();
							UtilModulos.fSobreescribirAsegurado(varUmicCopia, varbtcUmicCopia, tablaOrigen, varIndAsegOrigen,
									fNacAsegOrigen, sexAsegOrigen, edadAsegOrigen);
							varbtcUmicCopia.setTablaBaseExpList(btcUmic.getTablaBaseExpList());
							mapVariables.put(CLAVE_VAR_BTCUMIC_COPIA, varbtcUmicCopia);
							mapVariables.put(CLAVE_VAR_BTCUMIC_COPIADA, "S");
							mapVariables.put("VZCASEG", IObtenerConfiguracion.OrdenAsegurado.ASEG2);
						}
					}
					varProyVZC2 = proyUmic;
					moduloVZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);
					varVzc2 = (BigDecimal) moduloVZC.execute(varProyVZC2, bloqueCorriente, iteracion, fcalc, varUmicCopia,
							varbtcUmicCopia, mapVariables, codSubproceso);

					if (umic.getOtrosDatos().getCestadoAseg1().equals(ConstantsModulos.CTE_CESTADO_ASEG_A)) {
						varbVIT = varVzc2;
					} else {
						varProyFZC = proyUmic;
						mapVariables.put("VZCASEG", IObtenerConfiguracion.OrdenAsegurado.ASEG1);
						moduloFZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FZC);
						varFZC = (BigDecimal) moduloFZC.execute(varProyFZC, bloqueCorriente, iteracion, fcalc, umic,
								btcUmic, mapVariables, codSubproceso);

						varbVIT = varVzc2.multiply(varFZC);
					}
				} else {
					sw_btcumic_copiada = (String) mapVariables.get(CLAVE_VAR_BTCUMIC_COPIADA);
					if (null == sw_btcumic_copiada) {
						varIndAsegOrigen = 2;
						varIndAsegDestino = 1;
						tablaOrigen = btcUmic.getTablacalc1aseg2();
						fNacAsegOrigen = umic.getAsegurados().getFnacAseg2();
						sexAsegOrigen = umic.getAsegurados().getCsexAseg2();
						edadAsegOrigen = umic.getAsegurados().getEdadAseg2();
						UtilModulos.fSobreescribirAsegurado(varUmicCopia, varbtcUmicCopia, tablaOrigen, varIndAsegOrigen,
								fNacAsegOrigen, sexAsegOrigen, edadAsegOrigen);
						varbtcUmicCopia.setTablaBaseExpList(btcUmic.getTablaBaseExpList());
						mapVariables.put(CLAVE_VAR_BTCUMIC_COPIA, varbtcUmicCopia);
						mapVariables.put(CLAVE_VAR_BTCUMIC_COPIADA, "S");
						mapVariables.put("VZCASEG", IObtenerConfiguracion.OrdenAsegurado.ASEG2);
					}
					varProyVZC2 = proyUmic;
					moduloVZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);
					varVzc2 = (BigDecimal) moduloVZC.execute(varProyVZC2, bloqueCorriente, iteracion, fcalc, varUmicCopia,
							varbtcUmicCopia, mapVariables, codSubproceso);

					if (umic.getOtrosDatos().getCestadoAseg1().equals(ConstantsModulos.CTE_CESTADO_ASEG_A)) {
						varbVIT = varVzc2;
					} else {
						varProyFZC = proyUmic;
						mapVariables.put("VZCASEG", IObtenerConfiguracion.OrdenAsegurado.ASEG1);
						moduloFZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FZC);
						varFZC = (BigDecimal) moduloFZC.execute(varProyFZC, bloqueCorriente, iteracion, fcalc, umic,
								btcUmic, mapVariables, codSubproceso);

						varbVIT = varVzc2.multiply(varFZC);
					}
				}
			}
				
			} else if (umic.getDatosGenerales().getNorden().toString().substring(0, 1)
					.equals(ConstantsFunciones.CTE_3_STRING)) {
				Integer seleccionAseg = 0;
				
				if(null != btcUmic.getTablacalc1aseg5() && null != umic.getAsegurados().getFnacAseg5() && null != umic.getAsegurados().getCsexAseg5() && null != umic.getAsegurados().getEdadAseg5()){
					seleccionAseg = 5;
					if(umic.getDatosGenerales().getNorden().compareTo(new Integer(321)) == 0){
						
						throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_HA);
						
					}
				}else if(null != btcUmic.getTablacalc1aseg4() && null != umic.getAsegurados().getFnacAseg4() && null != umic.getAsegurados().getCsexAseg4() && null != umic.getAsegurados().getEdadAseg4()){
					seleccionAseg = 4;
					
				}else{
					seleccionAseg = 3;
				}
				
				
				sw_btcumic2_copiada = (String) mapVariables.get(CLAVE_VAR_BTCUMIC2_COPIADA);
				if(null == sw_btcumic2_copiada){
					varIndAsegOrigen = seleccionAseg;
					varIndAsegDestino = 1;
					
					if(varIndAsegOrigen.equals(new Integer(5))){
						tablaOrigen = btcUmic.getTablacalc1aseg5();
						fNacAsegOrigen = umic.getAsegurados().getFnacAseg5();
						sexAsegOrigen = umic.getAsegurados().getCsexAseg5();
						edadAsegOrigen = umic.getAsegurados().getEdadAseg5();
						mapVariables.put("VZCASEG", IObtenerConfiguracion.OrdenAsegurado.ASEG5);
					}else if(varIndAsegOrigen.equals(new Integer(4))){
						tablaOrigen = btcUmic.getTablacalc1aseg4();
						fNacAsegOrigen = umic.getAsegurados().getFnacAseg4();
						sexAsegOrigen = umic.getAsegurados().getCsexAseg4();
						edadAsegOrigen = umic.getAsegurados().getEdadAseg4();
						mapVariables.put("VZCASEG", IObtenerConfiguracion.OrdenAsegurado.ASEG4);
					}else{
						tablaOrigen = btcUmic.getTablacalc1aseg3();
						fNacAsegOrigen = umic.getAsegurados().getFnacAseg3();
						sexAsegOrigen = umic.getAsegurados().getCsexAseg3();
						edadAsegOrigen = umic.getAsegurados().getEdadAseg3();
						mapVariables.put("VZCASEG", IObtenerConfiguracion.OrdenAsegurado.ASEG3);
					}
				
					
					
					UtilModulos.fSobreescribirAsegurado(varUmicCopia2, varbtcUmicCopia2, tablaOrigen, varIndAsegOrigen, fNacAsegOrigen, sexAsegOrigen, edadAsegOrigen);
					varbtcUmicCopia2.setTablaBaseExpList(btcUmic.getTablaBaseExpList());
					mapVariables.put(CLAVE_VAR_BTCUMIC_COPIA2, varbtcUmicCopia2);
					mapVariables.put(CLAVE_VAR_BTCUMIC2_COPIADA, "S");
				}
				varProyVZC3 = proyUmic;
				moduloVZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);
				varVzc3 = (BigDecimal) moduloVZC.execute(varProyVZC3, bloqueCorriente, iteracion, fcalc, varUmicCopia2,
						varbtcUmicCopia2, mapVariables, codSubproceso);

				if (!umic.getOtrosDatos().getCestadoAseg1().equals(ConstantsModulos.CTE_CESTADO_ASEG_A)
						&& !umic.getOtrosDatos().getCestadoAseg2().equals(ConstantsModulos.CTE_CESTADO_ASEG_A)) {
					varProyFZC1FZC2 = proyUmic;
					moduloFzc1Fzc2 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FZC1FZC2);
					varFzc1Fzc2 = (BigDecimal) moduloFzc1Fzc2.execute(varProyFZC1FZC2, bloqueCorriente, iteracion, fcalc, umic,
							btcUmic, mapVariables, codSubproceso);

					varbVIT = varVzc3.multiply(varFzc1Fzc2);
					
					if (umic.getDatosGenerales().getNorden().equals(311)) {
						BigDecimal varEdadCalc1 = BigDecimal.ZERO;
						BigDecimal varEdadCalc2 = BigDecimal.ZERO;
						BigDecimal varEdadCalc3 = BigDecimal.ZERO;
						BigDecimal varEdadCalc4 = BigDecimal.ZERO;
						Timestamp varFechaEfecto = null;
						String varCriterioFec = ConstantsFunciones.CTE_CADENA_VACIA;
						String varCriterEdad = ConstantsFunciones.CTE_CADENA_VACIA;
						
						varCriterEdad = UtilModulos.getVarCriEdad(mapVariables,
								"EDAD", umic.getDatosGenerales().getCcartera(), umic
										.getDatosGenerales().getKmodalidad(), umic
										.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(),
								ConstantsModulos.CTE_VA_CRIT_EDA);
						
						if (ConstantsFunciones.CTE_APOR_REDUCIDA.equals(umic.getDatosGenerales().getCsitupol()) &&
								ConstantesSolvencia.NEGOCIO_COLECTIVO.equals(umic.getDatosGenerales().getCnegocio())) {
							varFechaEfecto = umic.getFechas().getFecinisus();
						} else {
							varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, "CLAVE_FEC", umic.getDatosGenerales(), umic.getFechas(), 
									umic.getCapitales().getIsaldo());
						}
						
						varEdadCalc1 = UtilModulos.getVarEdadCalc(mapVariables, CLAVE_VAR_EDAD_CAL + umic.getAsegurados().getFnacAseg1(), proyUmic.get(iteracion-1).getFechaDesde(), umic.getAsegurados().getFnacAseg1(), varCriterEdad, umic.getRentas().getFecIni(), umic.getDatosGenerales().getEdifer());
						varEdadCalc2 = UtilModulos.getVarEdadCalc(mapVariables, CLAVE_VAR_EDAD_CAL + umic.getAsegurados().getFnacAseg2(), proyUmic.get(iteracion-1).getFechaDesde(), umic.getAsegurados().getFnacAseg2(), varCriterEdad, umic.getRentas().getFecIni(), umic.getDatosGenerales().getEdifer());
						varEdadCalc3 = UtilModulos.getVarEdadCalc(mapVariables, CLAVE_VAR_EDAD_CAL + umic.getAsegurados().getFnacAseg3(), proyUmic.get(iteracion-1).getFechaDesde(), umic.getAsegurados().getFnacAseg3(), varCriterEdad, umic.getRentas().getFecIni(), umic.getDatosGenerales().getEdifer());
						varEdadCalc4 = UtilModulos.getVarEdadCalc(mapVariables, CLAVE_VAR_EDAD_CAL + umic.getAsegurados().getFnacAseg4(), proyUmic.get(iteracion-1).getFechaDesde(), umic.getAsegurados().getFnacAseg4(), varCriterEdad, umic.getRentas().getFecIni(), umic.getDatosGenerales().getEdifer());

					}
				} else if (umic.getOtrosDatos().getCestadoAseg1().equals(ConstantsModulos.CTE_CESTADO_ASEG_A)
						&& !umic.getOtrosDatos().getCestadoAseg2().equals(ConstantsModulos.CTE_CESTADO_ASEG_A)) {
					
					sw_btcumic_copiada = (String) mapVariables.get(CLAVE_VAR_BTCUMIC_COPIADA);
					if(null == sw_btcumic_copiada){
						varIndAsegOrigen = 2;
						varIndAsegDestino = 1;
						tablaOrigen = btcUmic.getTablacalc1aseg2();
						fNacAsegOrigen = umic.getAsegurados().getFnacAseg2();
						sexAsegOrigen = umic.getAsegurados().getCsexAseg2();
						edadAsegOrigen = umic.getAsegurados().getEdadAseg2();
						UtilModulos.fSobreescribirAsegurado(varUmicCopia, varbtcUmicCopia, tablaOrigen, varIndAsegOrigen, fNacAsegOrigen, sexAsegOrigen, edadAsegOrigen);
						varbtcUmicCopia.setTablaBaseExpList(btcUmic.getTablaBaseExpList());
						mapVariables.put(CLAVE_VAR_BTCUMIC_COPIA, varbtcUmicCopia);
						mapVariables.put(CLAVE_VAR_BTCUMIC_COPIADA, "S");
						mapVariables.put("VZCASEG", IObtenerConfiguracion.OrdenAsegurado.ASEG2);
					}
										
					varProyFZC1 = proyUmic;
					moduloFzc1 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FZC);
					varFzc2 = (BigDecimal) moduloFzc1.execute(varProyFZC1, bloqueCorriente, iteracion, fcalc, varUmicCopia,
							varbtcUmicCopia, mapVariables, codSubproceso);

					varbVIT = varVzc3.multiply(varFzc2);
				}else if (!umic.getOtrosDatos().getCestadoAseg1().equals(ConstantsModulos.CTE_CESTADO_ASEG_A)
						&& umic.getOtrosDatos().getCestadoAseg2().equals(ConstantsModulos.CTE_CESTADO_ASEG_A)) {
					
					sw_btcumic_copiada = (String) mapVariables.get(CLAVE_VAR_BTCUMIC_COPIADA);
					if(null == sw_btcumic_copiada){
						varIndAsegOrigen = 2;
						varIndAsegDestino = 1;
						tablaOrigen = btcUmic.getTablacalc1aseg2();
						fNacAsegOrigen = umic.getAsegurados().getFnacAseg2();
						sexAsegOrigen = umic.getAsegurados().getCsexAseg2();
						edadAsegOrigen = umic.getAsegurados().getEdadAseg2();
						UtilModulos.fSobreescribirAsegurado(varUmicCopia, varbtcUmicCopia, tablaOrigen, varIndAsegOrigen, fNacAsegOrigen, sexAsegOrigen, edadAsegOrigen);
						varbtcUmicCopia.setTablaBaseExpList(btcUmic.getTablaBaseExpList());
						mapVariables.put(CLAVE_VAR_BTCUMIC_COPIA, varbtcUmicCopia);
						mapVariables.put(CLAVE_VAR_BTCUMIC_COPIADA, "S");
						mapVariables.put("VZCASEG", IObtenerConfiguracion.OrdenAsegurado.ASEG2);
					}
										
					varProyFZC1 = proyUmic;
					moduloFzc1 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FZC);
					varFzc1 = (BigDecimal) moduloFzc1.execute(varProyFZC1, bloqueCorriente, iteracion, fcalc, varUmicCopia,
							varbtcUmicCopia, mapVariables, codSubproceso);

					varbVIT = varVzc3.multiply(varFzc1);
				}else if (umic.getOtrosDatos().getCestadoAseg1().equals(ConstantsModulos.CTE_CESTADO_ASEG_A)
						&& umic.getOtrosDatos().getCestadoAseg2().equals(ConstantsModulos.CTE_CESTADO_ASEG_A)) {
					varbVIT = varVzc3;
				}
			}
		
		if (ModuloVARBVIT.LOG.isTraceEnabled()) {
			ModuloVARBVIT.LOG.trace("Fin función << ModuloVARBVIT >> de la clase ModuloVARBVIT");
		}

		return varbVIT;
	}
}
