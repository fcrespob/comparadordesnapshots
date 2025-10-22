package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.formulacion.CriterioFechas;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalFlujoProyeccion;
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
 * Clase encargada del cálculo de la cuantía por ANULACIONES(RESCATES) de la garantía principal de esta modalidad.
 *
 * @author apedro
 */
public class ModuloRT363 implements Modulo {

	/**
	 * cte. para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloRT363.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_RT363;
	
	private static final String CLAVE_VAR_TERMINAL = ConstantsModulos.CTE_VAR_TERMINAL.concat(CLAVE_MODULO);
	
	private static final String CLAVE_CTE_RESCATE_VARK1 = ConstantsModulos.CTE_RESCATE_VARK1;
	private static final String CLAVE_RESCATE_VARK1 = CLAVE_CTE_RESCATE_VARK1.concat(CLAVE_MODULO);
	private static final String CLAVELISTACORRUMIC = ConstantsModulos.CTE_LST_CORR_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 
	// Fin de las variables estáticas usadas para agilizar operaciones.

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
			if (ModuloRT363.LOG.isTraceEnabled()) {
				ModuloRT363.LOG.trace("Inicio de execute en clase ModuloRT362");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloRT363
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de los datos.
			
			//Invocamos a la función de calculo RT363
			resultado = moduloRT363(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloRT363.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloRT363.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloRT363.LOG.isTraceEnabled()) {
			ModuloRT363.LOG.trace("Fin de execute en clase ModuloRT362");
		}
		
		return resultado;
	}
	
	/**
	 * Clase encargada del cálculo de la cuantía por ANULACIONES(RESCATES) de la garantía principal de esta modalidad
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
	@SuppressWarnings("unchecked")
	private BigDecimal moduloRT363(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		// Variables locales
		BigDecimal rt363 = BigDecimal.ZERO;
		BigDecimal varK1;
		Timestamp varFechaEfecto = null;
		CriterioFechas varCriterioFechas;
		final IObtenerConfiguracion obtenerConf = FachadaServicios.getObtenerConfiguracion();
		final TotalFlujoProyeccion varVx363;
		List<DetalleCorriente> lstCorrUmic;
		// Fin variables locales
		
		if (ModuloRT363.LOG.isTraceEnabled()) {
			ModuloRT363.LOG.trace("Inicio función << moduloRT363 >> de la clase ModuloRT363, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		/**
		 * Si estoy en el primer periodo (j=1):
		 * Constantes de Rescates
		 * - codk1 = umic.rescates.krescate1
		 * - Si el código de la constante empieza por KT
		 * 		- durk1 = TCM(umic.fechas.fecinisus, proyUmic(j).varBloque.fecDevengo);
		 * - Si el código de la constante empieza por KC
		 * 		- durk1 = 999
		 * - Si el código de la constante empieza por KM
		 * 		- durk1 = umic.duraciones.ndurprima/12
		 * - Si el código de la constante empieza por KN
		 * 		- durk1 = umic.duraciones.ndursemes
		 * - vark1 --> obtenerConfiguracion.recuperarCtesRescates(codk1, durk1)
		 * 		- Si el valor de la constante  retornada es nulo se devuelve error funcional 005 -
		 * 		No se ha encontrado la Constante de Rescate codK1, finalizando el proceso para la UMIC.
		 */
		
		final String varTerminal = UtilModulos.getVarTerminal(mapVariables, CLAVE_VAR_TERMINAL, umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), umic.getDatosAdicionales().getPrestCal(), btcUmic.getBaseTec());
		
		varK1 = UtilModulos.getVarK1(mapVariables, CLAVE_RESCATE_VARK1, umic.getRescates().getKrescate1(), varFechaEfecto,
				bloqueCorriente.getFechaDevengo(), umic.getDuraciones().getNdurprima(), umic.getDuraciones().getNdursegmes());
		
			// Validación de las variables de apoyo.
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarCteRescateVarkx(umic.getRescates().getKrescate1(), varK1);
		} // Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
		// Fin de la validación de las variables de apoyo.
		
		
		if(bloqueCorriente.getFechaDevengo() == null) {
			return rt363;
		}

		/** 
		 * Para cualquier periodo j se calculará:
		 * - varVx363 = VBX363(proyUmic, periodoProyeccion, fcalc, umic, btcUmic, codSubproceso, varTerminal)
		 * - Si el código de la constante empieza por KT
		 * 		- durk1 = TCM(umic.fechas.fecinisus, proyUmic(j).varBloque.fecDevengo);
		 * - vark1 --> obtenerConfiguracion.recuperarCtesRescates(codk1, durk1)
		 * 		sobreescribo la variable en memoria, disponible para el subproceso de la umic.
		 * 		- Si el valor de la constante retornada es nulo se devuelve error funcional 005 -
		 * 		No se ha encontrado la Constante de Rescate codK1, finalizando el proceso para la UMIC.
		 */
		
		if((lstCorrUmic = (List<DetalleCorriente>) mapVariables.get("lstCorrUmic")) == null){
			lstCorrUmic = UtilModulos.getListaCorrienteUmic(mapVariables, CLAVELISTACORRUMIC, ConstantsModulos.CTE_BTI, btcUmic.getFecCierre(), umic.getKey());
			if(null == lstCorrUmic || lstCorrUmic.size() == 0){
				mapVariables.remove(CLAVELISTACORRUMIC);
				lstCorrUmic = UtilModulos.getListaCorrienteUmic(mapVariables, CLAVELISTACORRUMIC, ConstantsModulos.CTE_BTI_PROY, btcUmic.getFecCierre(), umic.getKey());
			}
			mapVariables.put("lstCorrUmic", lstCorrUmic);
			
		}
		
		if ((varCriterioFechas = (CriterioFechas) mapVariables.get("varCriterioFechas")) == null){
			varCriterioFechas = obtenerConf.recuperarCriterioFechas(umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), 
					umic.getDatosGenerales().getKprestacion(), umic.getDatosAdicionales().getPrestCal(), "PROY_RESC");
			mapVariables.put("varCriterioFechas", varCriterioFechas);
		}
		
		final Modulo moduloVBX363 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VBX363);
		
		if(varCriterioFechas.getFecDevengo().equals("INIP")){
			varVx363 = lstCorrUmic.get(iteracion -1).getTotalFlujoProyeccion();
		}else if(varCriterioFechas.getFecDevengo().equals("FINP")){
			if(iteracion-1 >= lstCorrUmic.size()-1){
				rt363 = BigDecimal.ZERO;
				return rt363;
			}else{
				varVx363 = lstCorrUmic.get(iteracion).getTotalFlujoProyeccion();
			}
		}else{
			varVx363 = (TotalFlujoProyeccion) moduloVBX363.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso, varTerminal);
		}
		
		
		if(varVx363.getProvbtiproy() != null) {
			/**
			 * Se calculará el importe de rescate correspondiente al periodo j como:
			 * RT363 (j) = varVx363 * vark1
			 */
			rt363 = varVx363.getProvbtiproy().multiply(varK1);
		}
		
		if (ModuloRT363.LOG.isTraceEnabled()) {
			ModuloRT363.LOG.trace("Fin función << moduloRT363 >> de la clase ModuloRT363, para la iteracion = {}, con resultado rt363 = {}", iteracion, rt363);
		}
		
		return rt363;
	}
}
