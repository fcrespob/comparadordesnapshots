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
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo RT001.
 * La expresión matemática para su determinación es la siguiente:
 * 		RT(001,tc) =  K1 * Bdx(Tc,Beta)
 * @author apedro
 *
 */
public class ModuloRT001 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloRT001.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_RT001;
	private static final String CLAVE_LST_PROY = ConstantsModulos.CTE_LST_PROY.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 
	private IObtenerConfiguracion obtenerConfiguracion = FachadaServicios.getObtenerConfiguracion();
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
			if (ModuloRT001.LOG.isTraceEnabled()) {
				ModuloRT001.LOG.trace("Inicio de execute en clase ModuloRT001");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloGZC001
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			
			//Invocamos a la función moduloRT001
			resultado = moduloRT001(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloRT001.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloRT001.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloRT001.LOG.isTraceEnabled()) {
			ModuloRT001.LOG.trace("Fin de execute en clase ModuloRT001");
		}
		
		return resultado;
	}
	/**
	 * Módulo de cálculo de la cuantía por Rescate de la garantía de bono en cada punto necesario. 
	 * La expresión matemática para su determinación es la siguiente:
	 *				 RT(001,tc) =  K1 * Bdx(Tc,Beta)
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
	private BigDecimal moduloRT001(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal varBdx = BigDecimal.ZERO;
		Timestamp varfechaEfecto;
		BigDecimal rt001 = BigDecimal.ZERO;
		List<DetalleCorriente> varListaCorrienteUmic;
		//Fin variables locales
		
		if (ModuloRT001.LOG.isTraceEnabled()) {
			ModuloRT001.LOG.trace("Inicio función << moduloRT001 >> de la clase ModuloRT001, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		
		if (bloqueCorriente.getFechaDevengo() == null) {
			return rt001;
		}
		
		/**
		 * Constantes de Rescates
		 * Para obtener el valor real de las constantes de rescate hay que realizar un servicio de consulta con el código y la duración de la constante.
		 * 
		 * -	codk1 = umic.rescates.krescate1
		 * -	Si el código de la constante  empieza por  KT
		 * 		o	durk1 = TCM(umic.fechas.fecinisus, proyUmic(j).varBloque.fecDevengo);
		 * -	Si el código de la constante empieza por  KC
		 * 		o	durk1 = 999
		 * -	Si el código de la constante empieza por  KM
		 * 		o	durk1 =  umic.duraciones.ndurprima/12
		 * -	Si el código de la constante empieza por  KN
		 * 		o	durk1 = umic.duraciones.ndursemes
		 * -	vark1 -> obtenerConfiguracion.recuperarCtesRescates(codk1, durk1)
		 * 		o	Si el valor de la constante  retornada es nulo se devuelve
		 * 		error funcional 005 - No se ha encontrado la Constante de Rescate  codK1, finalizando el proceso para la UMIC.
		 */
		
		varfechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		final String codk1 = umic.getRescates().getKrescate1();
		final Integer durk1 = UtilModulos.obtenerDuracionCodKX(codk1, varfechaEfecto, bloqueCorriente.getFechaDevengo(), 
				umic.getDuraciones().getNdurprima(), umic.getDuraciones().getNdursegmes());
		
		final BigDecimal vark1 = UtilModulos.getCteRescate(mapVariables, codk1, durk1);
		
		// vark1 depende de durk1; durk1 puede variar entre periodos luego la validación hay que hacerla en cada iteración.
		ValidacionesComunesModulos.validarCteRescateVarkx(codk1, vark1);
		
		
		//Se Recuperará la proyección previamente calculada en BTI para la umic
		varListaCorrienteUmic = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PROY, ConstantsModulos.CTE_BTI, btcUmic.getFecCierre(), umic.getKey());
		
		FlujosProbables flujoProbable = obtenerConfiguracion.recuperarConfProv(umic.getDatosGenerales().getKmodalidad(),
				umic.getDatosGenerales().getKgarantia(), umic.getDatosAdicionales().getPrestCal(), ConstantsModulos.CTE_BTI);
		
		if(null == varListaCorrienteUmic || varListaCorrienteUmic.isEmpty()
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
			varBdx = varListaCorrienteUmic.get(iteracion - 1).getTotalFlujoProyeccion().getProvbtiproy();
		} else {
			varBdx = varListaCorrienteUmic.get(iteracion - 1).getTotalFlujoProyeccion().getTerminalAnterior();
		}
		if (varBdx == null){
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DD);
		}
		
		/**
		 * Se calculará el importe de rescate correspondiente al periodo j como:
		 * RT001 (j) = vark1 * varBdx
		 */
		rt001 = vark1.multiply(varBdx);
		
		if (ModuloRT001.LOG.isTraceEnabled()) {
			ModuloRT001.LOG.trace("Fin función << moduloRT001 >> de la clase ModuloRT001, para la iteracion = {} con resultado rt001 = {}", iteracion, rt001);
		}
			
		return rt001;
	}
}
