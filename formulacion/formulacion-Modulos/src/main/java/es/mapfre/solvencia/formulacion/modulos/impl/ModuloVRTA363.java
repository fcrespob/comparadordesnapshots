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
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo VRTA363.
 * La expresión matemática para su determinación es la siguiente:
 * 		  VRTA363 = Vzc1(fcierta, j) + REVER * [Vzc2(fcierta,j) - Vzc1zc2(fcierta,j)]
 * @author apedro
 *
 */
public class ModuloVRTA363 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVRTA363.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VRTA363;
	private static final String CLAVE_VAR_REVER = ConstantsModulos.CTE_VAR_REVER.concat(CLAVE_MODULO);
	//private static final String CLAVE_VAR_FCIERTA = ConstantsModulos.CTE_VAR_FCIERTA.concat(CLAVE_MODULO);
	private static final String CLAVE_UMIC2 = ConstantsModulos.CTE_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_BTC_UMIC2 = ConstantsModulos.CTE_BTC_UMIC.concat(CLAVE_MODULO);
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
			if (ModuloVRTA363.LOG.isTraceEnabled()) {
				ModuloVRTA363.LOG.trace("Inicio de execute en clase ModuloVRTA363");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloVRTA363
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			
			//Invocamos a la función ModuloVRTA363
			resultado = moduloVRTA363(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloVRTA363.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVRTA363.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloVRTA363.LOG.isTraceEnabled()) {
			ModuloVRTA363.LOG.trace("Fin de execute en clase ModuloVRTA363");
		}
		
		return resultado;
	}
	/**
	 * Modulo de cálculo que devuelve los factores de probabilización a aplicar en  el cálculo de la cuantía 
	 * probable de la garantía principal de Modalidades Rentas de Jubilación con Reembolso de Reservas (Mod. 363)   		
	 * La expresión matemática para su determinación es la siguiente:	   
	 *			VRTA363 = Vzc1(fcierta, j) + REVER * [Vzc2(fcierta,j) - Vzc1zc2(fcierta,j)]
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
	private BigDecimal moduloVRTA363(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		//Variables locales
		BigDecimal vrta363 = BigDecimal.ZERO;
		BigDecimal varRever;
		Timestamp varFcierta;
		BigDecimal varVzc;
		Modulo moduloVZC;
		Umic varUmic2;
		DetalleBaseTecnica varBtcUmic2;
		BigDecimal varVzc2;
		//Fin variables locales
		
		if (ModuloVRTA363.LOG.isTraceEnabled()) {
			ModuloVRTA363.LOG.trace("Inicio función << ModuloVRTA363 >> de la clase ModuloVRTA363, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		
		if (bloqueCorriente.getFechaDevengo() == null){
			return vrta363;
		}
		
		//Se calculan y almacenan las variables del módulo que no camian entre periodos
		varRever = (BigDecimal) mapVariables.get(CLAVE_VAR_REVER);
		if (varRever == null){
			varRever = umic.getRentas().getPreversion().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
			mapVariables.put(CLAVE_VAR_REVER, varRever);
		}

		if (fcalc.after(umic.getRentas().getFecIni())){
			varFcierta = fcalc;
		} else {
			varFcierta = umic.getRentas().getFecIni();
		}
			
		moduloVZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);
		varVzc = (BigDecimal) moduloVZC.execute(proyUmic, bloqueCorriente, iteracion, varFcierta, umic, btcUmic, mapVariables, codSubproceso);
		
		//Si existe un segundo asegurado:
		if (umic.getAsegurados().getFnacAseg2()!= null){
			varUmic2 = (Umic) mapVariables.get(CLAVE_UMIC2);
			varBtcUmic2 = (DetalleBaseTecnica) mapVariables.get(CLAVE_BTC_UMIC2);
			if (varUmic2 == null){
				varUmic2 = new Umic();
				varBtcUmic2 = new DetalleBaseTecnica();
				try {
					//Se clona la Umic y la btcUmic para asignar los datos del asegurado2 al asegurado1 de la nueva Umic 
					// y btcUmic (con este método no se clonan las listas ni los sub-objetos)
					PropertyUtils.copyProperties(varUmic2, umic);
					PropertyUtils.copyProperties(varBtcUmic2, btcUmic);
				} catch (Exception e) {
					ModuloVRTA363.LOG.error(e.getMessage());
				}
				//Se asignan los datos de asegurado2 al asegurado1 de la nueva Umic y btcUmic
				Asegurados asegurados2 = new Asegurados();
				asegurados2.setFnacAseg1(umic.getAsegurados().getFnacAseg2());
				asegurados2.setCsexAseg1(umic.getAsegurados().getCsexAseg2());
				asegurados2.setEdadAseg1(umic.getAsegurados().getEdadAseg2());
				varUmic2.setAsegurados(asegurados2);
				
				varBtcUmic2.setTablacalc1aseg1(btcUmic.getTablacalc1aseg2());
				
				if ((!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_BEL)) && 
						(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCOA)) && 
						(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCLR)) && 
						(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTIU)) && 
						(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTID)) && 
						(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRGTO)) && 
						(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMFE)) && 
						(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMMI)) && 
						(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCF)) && 
						(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCI)) && 
						(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLFE)) && 
						(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLMI)) && 
						(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRINC)) && 
						(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRVM))  && 
						(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEP)) && 
						(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEN)) && 
						(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIP)) && 
						(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIN)) && 
						(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRANM))){
					
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
				
				//Se almacena umic2 y varUmic2 (solo es necesario clonar y asignar los datos la primera vez)
				mapVariables.put(CLAVE_UMIC2, varUmic2);
				mapVariables.put(CLAVE_BTC_UMIC2, varBtcUmic2);
			}
			
			varVzc2 = (BigDecimal) moduloVZC.execute(proyUmic, bloqueCorriente, iteracion, varFcierta, varUmic2, varBtcUmic2, mapVariables, codSubproceso);
			
			//Se realiza el cálculo de Vrta363(j) = varVzc(j) + varRever * (varVzc2(j) - varVzc2(j)* varVzc(j)))
			BigDecimal vaVzc2PorVarVzc = varVzc2.multiply(varVzc);
			vrta363 = varVzc.add(varRever.multiply(varVzc2.subtract(vaVzc2PorVarVzc)));
		} else {
			//Si no existe segundo asegurado Vrta363 (j) = varVzc(j)
			vrta363 = varVzc;
		}
		
		
		if (ModuloVRTA363.LOG.isTraceEnabled()) {
			ModuloVRTA363.LOG.trace("Fin función << ModuloVRTA363 >> de la clase ModuloVRTA363, para la iteracion = {} con resultado vrta363 = {}", iteracion, vrta363);
		}
			
		return vrta363;
	}
}
