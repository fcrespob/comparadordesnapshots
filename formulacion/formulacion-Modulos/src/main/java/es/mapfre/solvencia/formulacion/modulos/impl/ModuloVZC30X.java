package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Asegurados;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.TablaConversion;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Modulo de cálculo que devuelve los factores de probabilización a aplicar en  el cálculo de la cuantía probable de una garantía.  
 * El módulo VZC30X(fcal,j) determina la probabilidad de la prestación, en fución de si el titular de la umic está anulado (VZC para el cónguye) ó en Vigor (VZREVER)
 * @author 
 *
 */
public class ModuloVZC30X implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVZC30X.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VZC30X;
	private static final String CLAVE_UMIC = ConstantsModulos.CTE_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_BTC_UMIC = ConstantsModulos.CTE_BTC_UMIC.concat(CLAVE_MODULO);
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
			if (ModuloVZC30X.LOG.isTraceEnabled()) {
				ModuloVZC30X.LOG.trace("Inicio de execute en clase VZC30X");
			}

			//Recuperamos los datos que le pasaremos a la función moduloVZCVIU
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de datos.

			//Invocamos a la función moduloVZC30X
			resultado = ModuloVZC30X(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso ,mapVariables);

		} catch (Solvencia2Excepcion e) {
			ModuloVZC30X.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVZC30X.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloVZC30X.LOG.isTraceEnabled()) {
			ModuloVZC30X.LOG.trace("Fin de execute en clase VZC30X");
		}

		return resultado;
	}
	/**
	 * Modulo de cálculo que devuelve los factores de probabilización a aplicar en  el cálculo de la cuantía probable de una garantía.  
	 * El módulo VZC30X(fcal,j) determina la probabilidad de la prestación, en fución de si el titular de la umic está anulado (VZC para el cónguye) ó en Vigor (VZREVER) 
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
	private BigDecimal ModuloVZC30X(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, String codSubproceso,
			Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal VZC30X = BigDecimal.ZERO;
		Modulo moduloVZC, moduloVZREVER;
		BigDecimal varVzrever = BigDecimal.ZERO, varVzc2j = BigDecimal.ZERO;
		Umic varUmic2, varUmic1;
		DetalleBaseTecnica  varBtcUmic2;
		List<DetalleCorriente> varProyVzc2 = null;
		List<DetalleCorriente> varProyVzrever = null;
		//Fin variables locales

		if (ModuloVZC30X.LOG.isTraceEnabled()) {
			ModuloVZC30X.LOG.trace("Inicio función << moduloVZC30X >> de la clase moduloVZC30X, para la iteracion = {}", iteracion);
		}

		//validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);

		moduloVZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);
		moduloVZREVER = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZREVER);


		if(umic.getOtrosDatos().getCestadoAseg1().equals(ConstantsModulos.CTE_CESTADO_ASEG_A)){

			if(umic.getDatosGenerales().getKbencon().equals(ConstantsModulos.CTE_KBENCON_301) || (umic.getDatosGenerales().getKbencon().equals(ConstantsModulos.CTE_KBENCON_302) &&
					null != umic.getAsegurados().getFnacAseg2() && !umic.getOtrosDatos().getCestadoAseg2().equals(ConstantsModulos.CTE_CESTADO_ASEG_A) &&
					!umic.getOtrosDatos().getCestadoAseg2().equals(ConstantsModulos.CTE_CESTADO_ASEG_F)  )) {

				varProyVzc2 = proyUmic;
				varUmic2 = (Umic) mapVariables.get(CLAVE_UMIC);

				if (null == varUmic2) {
					//Se clona la Umic y la btcUmic para asignar los datos del asegurado2 al asegurado1 de la nueva Umic y btcUmic 
					//UtilModulos.clonarUmicBtcumic(mapVariables, CLAVE_UMIC, CLAVE_BTC_UMIC, varUmic2, varBtcUmic2);

					varUmic2 = new Umic();
					try {
						//Se clona la Umic y la btcUmic para asignar los datos del asegurado2 al asegurado1 de la nueva Umic 
						// y btcUmic (con este método no se clonan las listas)
						PropertyUtils.copyProperties(varUmic2, umic);

					} catch (Exception e) {
						ModuloVZC30X.LOG.error(e.getMessage());
					}

					Asegurados aseg = new Asegurados();
					aseg.setFnacAseg1(umic.getAsegurados().getFnacAseg2());
					aseg.setCsexAseg1(umic.getAsegurados().getCsexAseg2());
					aseg.setEdadAseg1(umic.getAsegurados().getEdadAseg2());
					varUmic2.setAsegurados(aseg);

					mapVariables.put(CLAVE_UMIC,varUmic2);

				}

				varBtcUmic2 = (DetalleBaseTecnica) mapVariables.get(CLAVE_BTC_UMIC);

				if(null == varBtcUmic2){
					varBtcUmic2 = new DetalleBaseTecnica();

					try {
						//Se clona la Umic y la btcUmic para asignar los datos del asegurado2 al asegurado1 de la nueva Umic 
						// y btcUmic (con este método no se clonan las listas)
						PropertyUtils.copyProperties(varBtcUmic2, btcUmic);

					} catch (Exception e) {
						ModuloVZC30X.LOG.error(e.getMessage());
					}

					varBtcUmic2.setTablacalc1aseg1(btcUmic.getTablacalc1aseg2());

					if ((!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_BEL))  && (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCOA)) && (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCLR))){
						if (btcUmic.getTablasConversionAsegurado() == null || btcUmic.getTablasConversionAsegurado().isEmpty()){
							throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AC, new String[]{null, "tablasConversionAsegurado"});
						}

						List<List<TablaConversion>> tablaConvAseg = new ArrayList<List<TablaConversion>>();
						tablaConvAseg.add(btcUmic.getTablasConversionAsegurado().get(1));

						varBtcUmic2.setTablasConversionAsegurado(tablaConvAseg);
					} else {
						List<Integer> tablaBaseExp = new ArrayList<Integer>();
						tablaBaseExp.add(btcUmic.getTablaBaseExp().get(1));
						varBtcUmic2.setTablaBaseExp(tablaBaseExp);
					}

					mapVariables.put(CLAVE_BTC_UMIC,varBtcUmic2);

				}

				varProyVzc2 = proyUmic;
				varVzc2j = (BigDecimal) moduloVZC.execute(varProyVzc2, bloqueCorriente, iteracion, fcalc, varUmic2, varBtcUmic2, mapVariables, codSubproceso);
			}

			if(!umic.getDatosGenerales().getKbencon().equals(ConstantsModulos.CTE_KBENCON_301) && (null != umic.getAsegurados().getFnacAseg3() && !umic.getOtrosDatos().getCestadoAseg3().equals(ConstantsModulos.CTE_CESTADO_ASEG_A) &&
					!umic.getOtrosDatos().getCestadoAseg3().equals(ConstantsModulos.CTE_CESTADO_ASEG_F))){

				varProyVzc2 = proyUmic;
				varUmic2 = (Umic) mapVariables.get(CLAVE_UMIC);

				if (null == varUmic2) {
					//Se clona la Umic y la btcUmic para asignar los datos del asegurado2 al asegurado1 de la nueva Umic y btcUmic 
					//UtilModulos.clonarUmicBtcumic(mapVariables, CLAVE_UMIC, CLAVE_BTC_UMIC, varUmic2, varBtcUmic2);

					varUmic2 = new Umic();
					try {
						//Se clona la Umic y la btcUmic para asignar los datos del asegurado2 al asegurado1 de la nueva Umic 
						// y btcUmic (con este método no se clonan las listas)
						PropertyUtils.copyProperties(varUmic2, umic);

					} catch (Exception e) {
						ModuloVZC30X.LOG.error(e.getMessage());
					}

					Asegurados aseg = new Asegurados();
					aseg.setFnacAseg1(umic.getAsegurados().getFnacAseg3());
					aseg.setCsexAseg1(umic.getAsegurados().getCsexAseg3());
					aseg.setEdadAseg1(umic.getAsegurados().getEdadAseg3());
					varUmic2.setAsegurados(aseg);

					mapVariables.put(CLAVE_UMIC,varUmic2);

				}

				varBtcUmic2 = (DetalleBaseTecnica) mapVariables.get(CLAVE_BTC_UMIC);

				if(null == varBtcUmic2){
					varBtcUmic2 = new DetalleBaseTecnica();

					try {
						//Se clona la Umic y la btcUmic para asignar los datos del asegurado2 al asegurado1 de la nueva Umic 
						// y btcUmic (con este método no se clonan las listas)
						PropertyUtils.copyProperties(varBtcUmic2, btcUmic);

					} catch (Exception e) {
						ModuloVZC30X.LOG.error(e.getMessage());
					}

					varBtcUmic2.setTablacalc1aseg1(btcUmic.getTablacalc1aseg3());

					if ((!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_BEL))  && (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCOA)) && (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCLR))){
						if (btcUmic.getTablasConversionAsegurado() == null || btcUmic.getTablasConversionAsegurado().isEmpty()){
							throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AC, new String[]{null, "tablasConversionAsegurado"});
						}

						List<List<TablaConversion>> tablaConvAseg = new ArrayList<List<TablaConversion>>();
						tablaConvAseg.add(btcUmic.getTablasConversionAsegurado().get(2));

						varBtcUmic2.setTablasConversionAsegurado(tablaConvAseg);
					} else {
						List<Integer> tablaBaseExp = new ArrayList<Integer>();
						tablaBaseExp.add(btcUmic.getTablaBaseExp().get(2));
						varBtcUmic2.setTablaBaseExp(tablaBaseExp);
					}

					mapVariables.put(CLAVE_BTC_UMIC,varBtcUmic2);

				}

				varProyVzc2 = proyUmic;
				varVzc2j = (BigDecimal) moduloVZC.execute(varProyVzc2, bloqueCorriente, iteracion, fcalc, varUmic2, varBtcUmic2, mapVariables, codSubproceso);

			}	

			VZC30X = varVzc2j;

		}else{

			if(null != umic.getAsegurados().getFnacAseg2() && !umic.getOtrosDatos().getCestadoAseg2().equals(ConstantsModulos.CTE_CESTADO_ASEG_A) &&
					!umic.getOtrosDatos().getCestadoAseg2().equals(ConstantsModulos.CTE_CESTADO_ASEG_F)){

				varProyVzrever = proyUmic;
				varVzrever = (BigDecimal) moduloVZREVER.execute(varProyVzrever, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);

			}else{

				if(null != umic.getAsegurados().getFnacAseg3() && !umic.getOtrosDatos().getCestadoAseg2().equals(ConstantsModulos.CTE_CESTADO_ASEG_A) &&
						!umic.getOtrosDatos().getCestadoAseg2().equals(ConstantsModulos.CTE_CESTADO_ASEG_F)){

					varProyVzc2 = proyUmic;
					varUmic2 = (Umic) mapVariables.get(CLAVE_UMIC);

					if (null == varUmic2) {
						//Se clona la Umic y la btcUmic para asignar los datos del asegurado2 al asegurado1 de la nueva Umic y btcUmic 
						//UtilModulos.clonarUmicBtcumic(mapVariables, CLAVE_UMIC, CLAVE_BTC_UMIC, varUmic2, varBtcUmic2);

						varUmic2 = new Umic();
						try {
							//Se clona la Umic y la btcUmic para asignar los datos del asegurado2 al asegurado1 de la nueva Umic 
							// y btcUmic (con este método no se clonan las listas)
							PropertyUtils.copyProperties(varUmic2, umic);

						} catch (Exception e) {
							ModuloVZC30X.LOG.error(e.getMessage());
						}

						Asegurados aseg = new Asegurados();
						aseg.setFnacAseg2(umic.getAsegurados().getFnacAseg3());
						aseg.setCsexAseg2(umic.getAsegurados().getCsexAseg3());
						aseg.setEdadAseg2(umic.getAsegurados().getEdadAseg3());
						varUmic2.setAsegurados(aseg);

						mapVariables.put(CLAVE_UMIC,varUmic2);

					}

					varBtcUmic2 = (DetalleBaseTecnica) mapVariables.get(CLAVE_BTC_UMIC);

					if(null == varBtcUmic2){
						varBtcUmic2 = new DetalleBaseTecnica();

						try {
							//Se clona la Umic y la btcUmic para asignar los datos del asegurado2 al asegurado1 de la nueva Umic 
							// y btcUmic (con este método no se clonan las listas)
							PropertyUtils.copyProperties(varBtcUmic2, btcUmic);

						} catch (Exception e) {
							ModuloVZC30X.LOG.error(e.getMessage());
						}

						varBtcUmic2.setTablacalc1aseg2(btcUmic.getTablacalc1aseg3());

						if ((!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_BEL))  && (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCOA)) && (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCLR))){
							if (btcUmic.getTablasConversionAsegurado() == null || btcUmic.getTablasConversionAsegurado().isEmpty()){
								throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AC, new String[]{null, "tablasConversionAsegurado"});
							}

							List<List<TablaConversion>> tablaConvAseg = new ArrayList<List<TablaConversion>>();
							tablaConvAseg.add(btcUmic.getTablasConversionAsegurado().get(2));

							varBtcUmic2.setTablasConversionAsegurado(tablaConvAseg);
						} else {
							List<Integer> tablaBaseExp = new ArrayList<Integer>();
							tablaBaseExp.add(btcUmic.getTablaBaseExp().get(2));
							varBtcUmic2.setTablaBaseExp(tablaBaseExp);
						}

						mapVariables.put(CLAVE_BTC_UMIC,varBtcUmic2);

					}
					varProyVzrever = proyUmic;
					varVzrever = (BigDecimal) moduloVZREVER.execute(varProyVzrever, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);

				}

			}

			VZC30X = varVzrever;

		}	

		if (ModuloVZC30X.LOG.isTraceEnabled()) {
			ModuloVZC30X.LOG.trace("Fin función << ModuloVZC30X >> de la clase ModuloVZC30X, para la iteracion = {}, con resultado VZC30X = {}", iteracion, VZC30X);
		}

		return VZC30X;
	}
}
