package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.formulacion.CriterioFechas;
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
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo RT022.
 * La expresión matemática para su determinación es la siguiente:
 * 			Si la umic es casada entonces:
 *				RT(022,tc) = Bx(TC) * [mínimo[ k(tc),efplusmininver(tc) ]
 *
 * @author agonzalezgar
 */
public class ModuloRT022 implements Modulo {

	/** Cte para log. */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloRT022.class);
	
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_RT022;
	
	private static final String CLAVELISTACORRUMIC = ConstantsModulos.CTE_LST_CORR_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 
	private IObtenerConfiguracion obtenerConfiguracion = FachadaServicios.getObtenerConfiguracion();

	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}
	
	/** Función encargada de obtener los parámetros necesarios y de realizar la llamada a la función que realiza los calculos del modulo. */
	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {
		//Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		//Fin variables locales
		
		try {
			if (ModuloRT022.LOG.isTraceEnabled()) {
				ModuloRT022.LOG.trace("Inicio de execute en clase ModuloRT022");
			}
			
			//Inicio recuperación de datos que le pasaremos a la función moduloRT022
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			//Fin de la recuperación de los datos que se pasarán a la función moduloCSP071.
			
			//Invocamos a la función de calculo RT022
			resultado = moduloRT022(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloRT022.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloRT022.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloRT022.LOG.isTraceEnabled()) {
			ModuloRT022.LOG.trace("Fin de execute en clase ModuloRT022");
		}
		
		return resultado;
	}
	
	/**
	 * RT(022,tc) = Bx(TC) * [mínimo[ k(tc),efplusmininver(tc) ]
	 * Modulo de cálculo que devuelve el importe utilizando la formula RT022
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
	private BigDecimal moduloRT022(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		// Variables locales
		BigDecimal rt022 = BigDecimal.ZERO; // Mantendrá este valor por defecto si la UMIC no es casada.
		CriterioFechas varCriterioFechas;
		BigDecimal varProvMat = BigDecimal.ZERO;
		List<DetalleCorriente> lstCorrUmic;
		final IObtenerConfiguracion obtenerConf = FachadaServicios.getObtenerConfiguracion();
		Timestamp varFechaEfecto;
		// Fin variables locales
		
		if (ModuloRT022.LOG.isTraceEnabled()) {
			ModuloRT022.LOG.trace("Inicio función << moduloRT022 >> de la clase ModuloRT022, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		// Introducido por nuevos criterios de generación de fechas de pago y devengo (corte de fechas). Si no existe fecha devengo no se realiza el calculo
		if (null == bloqueCorriente.getFechaDevengo() ) {
			return rt022;
		}
		
		/**
		 * Se calculará el importe de rescate correspondiente al periodo j como:
		 * Si la umic es casada, es decir, umic.datosGenerales.swcasada = "S"
		 * 		RT022 (j) = varProvMat(j) * vark1
		 * Si la umic es no casada, es decir, umic.datosGenerales.swcasada <> "S"
		 * 		RT022 (j) = 0
		 */
		if (ConstantsModulos.CTE_S.equals(umic.getDatosGenerales().getSwcasado())) {
			// Si no se cumple la condición el valor de RT022 será CERO.
			
			varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
			
			/**
			  * Constantes de Rescates
				  - codk1 = umic.rescates.krescate1
				  - Si el código de la constante empieza por KT
				  		- durk1 = TCM(umic.fechas.fecinisus, proyUmic(j).varBloque.fecDevengo);
				  - Si el código de la constante empieza por KC
				  		- durk1 = 999
				  - Si el código de la constante empieza por KM
				  		- durk1 = umic.duraciones.ndurprima/12
				  - Si el código de la constante empieza por KN
				  		- durk1 = umic.duraciones.ndursemes
				  - vark1 --> obtenerConfiguracion.recuperarCtesRescates(codk1, durk1)
				  		- Si el valor de la constante  retornada es nulo se devuelve error funcional 005 -
				  		No se ha encontrado la Constante de Rescate codK1, finalizando el proceso para la UMIC.
				  
				  Para cualquier periodo j se calculará:
				  - Si el código de la constante empieza por KT
				  		- durk1 = TCM(umic.fechas.fecinisus, proyUmic(j).varBloque.fecDevengo);
				  - vark1 --> obtenerConfiguracion.recuperarCtesRescates(codk1, durk1)
				  		sobreescribo la variable en memoria, disponible para el subproceso de la umic.
				  		- Si el valor de la constante retornada es nulo se devuelve error funcional 005 -
				  		No se ha encontrado la Constante de Rescate codK1, finalizando el proceso para la UMIC.
			 */

			final String codK1 = umic.getRescates().getKrescate1();
			final int durk1 = UtilModulos.obtenerDuracionCodKX(codK1, varFechaEfecto, bloqueCorriente.getFechaDevengo(), 
						umic.getDuraciones().getNdurprima(), umic.getDuraciones().getNdursegmes());
			
			/**
			 	Si estoy en el primer periodo (j=1):
			 	- Se obtendrá la corriente de provisión matemática (PROY_PRV) previamente calculada en BTI para la umic. Para ello se llamará a la operación:
			 		- listaCorr
			 */
 
			FlujosProbables flujoProbable = obtenerConfiguracion.recuperarConfProv(umic.getDatosGenerales().getKmodalidad(),
					umic.getDatosGenerales().getKgarantia(), umic.getDatosAdicionales().getPrestCal(), ConstantsModulos.CTE_BTI_PROY);
			
			if((lstCorrUmic = (List<DetalleCorriente>) mapVariables.get("lstCorrUmic")) == null){
				lstCorrUmic = UtilModulos.getListaCorrienteUmic(mapVariables, CLAVELISTACORRUMIC, ConstantsModulos.CTE_BTI, btcUmic.getFecCierre(), umic.getKey());
				flujoProbable = obtenerConfiguracion.recuperarConfProv(umic.getDatosGenerales().getKmodalidad(),
						umic.getDatosGenerales().getKgarantia(), umic.getDatosAdicionales().getPrestCal(), ConstantsModulos.CTE_BTI);
				if(null == lstCorrUmic || lstCorrUmic.size() == 0
						|| lstCorrUmic.get(iteracion - 1).getBt().equals(ConstantsModulos.CTE_BTI_PROY)){
					mapVariables.remove(CLAVELISTACORRUMIC);
					lstCorrUmic = UtilModulos.getListaCorrienteUmic(mapVariables, CLAVELISTACORRUMIC, ConstantsModulos.CTE_BTI_PROY, btcUmic.getFecCierre(), umic.getKey());
					flujoProbable = obtenerConfiguracion.recuperarConfProv(umic.getDatosGenerales().getKmodalidad(),
							umic.getDatosGenerales().getKgarantia(), umic.getDatosAdicionales().getPrestCal(), ConstantsModulos.CTE_BTI_PROY);
				}
				mapVariables.put("lstCorrUmic", lstCorrUmic);
			} 
			
			if ((varCriterioFechas = (CriterioFechas) mapVariables.get("varCriterioFechas")) == null){
				varCriterioFechas = obtenerConf.recuperarCriterioFechas(umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), 
						umic.getDatosGenerales().getKprestacion(), umic.getDatosAdicionales().getPrestCal(), "PROY_RESC");
				mapVariables.put("varCriterioFechas", varCriterioFechas);
			}
			
			final BigDecimal varK1 = UtilModulos.getCteRescate(mapVariables, codK1, durk1);
			ValidacionesComunesModulos.validarCteRescateVarkx(codK1, varK1);
			
			/** - varProvMat = listaCorrienteUmic(j).totalFlujoProyeccion.provbtiproy */
			
			
			
			if(varCriterioFechas.getFecDevengo().equals("INIP")){
				if (flujoProbable.getProvNominal().equals(ConstantsFactorias.MODULO_PMRR01)
						|| flujoProbable.getProvNominal().equals(ConstantsFactorias.MODULO_PMRR02)) {
					varProvMat = lstCorrUmic.get(iteracion - 1).getTotalFlujoProyeccion().getProvbtiproy();
				} else {
					varProvMat = lstCorrUmic.get(iteracion - 1).getTotalFlujoProyeccion().getTerminalAnterior();
				}
			}else if(varCriterioFechas.getFecDevengo().equals("FINP")){
				if(iteracion-1 >= lstCorrUmic.size()-1){
					varProvMat = BigDecimal.ZERO;
					return varProvMat;
				}else{
					if (flujoProbable.getProvNominal().equals(ConstantsFactorias.MODULO_PMRR01)
							|| flujoProbable.getProvNominal().equals(ConstantsFactorias.MODULO_PMRR02)) {
						varProvMat = lstCorrUmic.get(iteracion - 1).getTotalFlujoProyeccion().getProvbtiproy();
					} else {
						varProvMat = lstCorrUmic.get(iteracion - 1).getTotalFlujoProyeccion().getTerminalAnterior();
					}
				}
			}
			
			rt022 = varProvMat.multiply(varK1, ConstantsFunciones.MATH_CONTEXT);
		}
		
		if (ModuloRT022.LOG.isTraceEnabled()) {
			ModuloRT022.LOG.trace("Fin función << moduloRT022 >> de la clase ModuloRT022, para la iteracion = {} con resultado rt022 = {}", iteracion, rt022);
		}
		
		return rt022;
	}
}
