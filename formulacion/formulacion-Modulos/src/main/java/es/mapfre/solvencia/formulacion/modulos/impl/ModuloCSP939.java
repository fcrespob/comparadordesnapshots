package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
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
import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesActualizacionFinanciera;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.FuncionesVBX;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * 
 * Módulo de cuantía nominal.
 * @author 
 *
 */
public class ModuloCSP939 implements Modulo {

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP939.class);
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP939;
	
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 	
	private static final String CLAVE_VZC2 = ConstantsModulos.CTE_VZC2;
	private static final String CLAVE_VAR_VZC2 = CLAVE_VZC2.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLA2000 = ConstantsModulos.CTE_TABLA2000;
	private static final String CLAVE_VAR_TABLA2000 = CLAVE_TABLA2000.concat(CLAVE_MODULO);
	private static final String CLAVE_E3SAL = ConstantsModulos.CTE_E3SAL;
	private static final String CLAVE_VAR_E3SAL = CLAVE_E3SAL.concat(CLAVE_MODULO);
	private static final String CLAVE_PORREDUC = ConstantsModulos.CTE_PORREDUC;
	private static final String CLAVE_VAR_PORREDUC = CLAVE_PORREDUC.concat(CLAVE_MODULO);

	
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
			if (ModuloCSP939.LOG.isTraceEnabled()) {
				ModuloCSP939.LOG.trace("Inicio de execute en clase ModuloCSP939");
			}
			
			//Inicio recuperación de datos que le pasaremos a la función moduloCSP939
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			//Fin de la recuperación de los datos que se pasarán a la función moduloCSP939.
			
			//Invocamos a la función de calculo CSP939
			resultado = moduloCSP939(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloCSP939.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP939.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP939.LOG.isTraceEnabled()) {
			ModuloCSP939.LOG.trace("Fin de execute en clase ModuloCSP939");
		}
		
		return resultado;

	}

	/** 
	 * Módulo de cuantía nominal.
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
	private BigDecimal moduloCSP939(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		//Variables locales

		Timestamp varFechaEfecto = null;
		BigDecimal csp939 = BigDecimal.ZERO;
		BigDecimal varIRENTINI;
		BigDecimal varPct;
		BigDecimal varTabla2000,porreduc;
		String varNva,varPORREDUC ;
		Integer varAnoInico;
		BigDecimal varIncr;
		BigDecimal auxiliar,auxiliarDiv;
		//Fin variables locales
		
		if (ModuloCSP939.LOG.isTraceEnabled()) {
			ModuloCSP939.LOG.trace("Inicio función << moduloCSP939 >> de la clase ModuloCSP939, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if ( null == bloqueCorriente.getFechaDevengo() ) {
			return csp939;
		}
		
		varIRENTINI = umic.getRentas().getRentini();
		
		varPct = ConstantsModulos.CTE_OPER_25.divide(ConstantsModulos.CTE_OPER_100);
		
		varNva = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_E3SAL, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_E3SAL);

		if(null == varNva){
			
			varNva = ConstantsModulos.CTE_0;
			
			final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();
			Incidencia aviso = Solvencia2ExcepcionHelper.crearAviso(btcUmic.getBaseTec(), umic.getDatosGenerales().getCcanal(), umic.getDatosGenerales().getCcartera(), umic.getKey(), btcUmic.getFecCierre(), umic.getDatosGenerales().getCnegocio(), this.getNombreServicio(), ConstantsFunciones.CTE_COD_ERROR_DN, new Object[]{CLAVE_E3SAL});
			servicio.almacenarIncidencias(aviso);
			
		}
				
		varTabla2000 = UtilModulos.getVarTabla2000(mapVariables, CLAVE_VAR_TABLA2000, varNva);

		
		if(null == varTabla2000){
			
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_TABLA2000});
			
		}
			
		varPORREDUC = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_PORREDUC, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_PORREDUC);
		if(null == varPORREDUC){	
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_PORREDUC});
		}else{
			porreduc = UtilModulos.StringToBigDecimal(varPORREDUC);	
		}	
	
		if(null == umic.getDatosGenerales().getKbencon()){			
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_G5);
		}
		
		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());

		varAnoInico = UtilFechas.getAnio(varFechaEfecto);
		
		varIncr = FuncionesAuxiliares.INCR(iteracion-1, bloqueCorriente.getFechaDevengo(), varTabla2000, umic.getDatosGenerales().getKbencon(), varAnoInico);
		
		auxiliar = porreduc.multiply(varIncr.multiply(varPct));
		auxiliarDiv = auxiliar.divide(ConstantsModulos.CTE_OPER_14,ConstantsFunciones.MATH_CONTEXT);
		csp939 = varIRENTINI.add(auxiliarDiv);
		
		if (ModuloCSP939.LOG.isTraceEnabled()) {
			ModuloCSP939.LOG.trace("Fin función << moduloCSP939 >> de la clase ModuloCSP939, para la iteracion = {}, con resultado csp939 = {}", iteracion, csp939);
		}
				
		return csp939;
	}
	
}
