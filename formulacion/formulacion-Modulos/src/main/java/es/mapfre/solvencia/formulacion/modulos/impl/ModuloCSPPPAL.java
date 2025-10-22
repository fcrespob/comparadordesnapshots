package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dominio.maestro.CuadroUmic;
import es.mapfre.solvencia.dominio.maestro.CuadrosAmortizacion;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.modulos.impl.ValidacionesComunesModulos;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.formulacion.util.UtilUmicPrincipal;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

public class ModuloCSPPPAL implements Modulo {

	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSPPPAL.class);
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSPPPAL;
	
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_UMICPRINCIPAL = ConstantsModulos.CLAVE_VAR_CLAVEPRINCIPAL;
	
	private static final String CLAVE_CUADRO_AMORT = ConstantsModulos.CTE_CUADRO_AMORT;
	private static final String CLAVE_VAR_CUADRO_AMORT = CLAVE_CUADRO_AMORT.concat(CLAVE_MODULO);

	private static final String CLAVELISTACORRUMIC = ConstantsModulos.CTE_LST_CORR_UMIC.concat(CLAVE_MODULO);
																				
	
	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}
	
	/**
	 * Función encargada de obtener los parámetros necesarios y de realizar la llamada a la función que realiza los calculos del modulo.
	 */
	
	@Override
	public Object execute(Object... args) throws Solvencia2Excepcion {
		//Variables locales
		BigDecimal cspppal = BigDecimal.ZERO;
		//Fin variables locales
		
		try {
			if (ModuloCSPPPAL.LOG.isTraceEnabled()) {
				ModuloCSPPPAL.LOG.trace("Inicio de execute en clase ModuloCSPPPAL");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloCSPAMORT
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			//Invocamos a la función de calculo CSPAMORT
			cspppal = moduloCSPPPAL(proyUmic,iteracion,fcalc, umic, btcUmic, codSubproceso, bloqueCorriente,mapVariables);
		} catch (Solvencia2Excepcion e) {
			ModuloCSPPPAL.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSPPPAL.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSPPPAL.LOG.isTraceEnabled()) {
			ModuloCSPPPAL.LOG.trace("Fin de execute en clase ModuloCSPPPAL");
		}
		
		return cspppal;
	}
	
	
	/**
	 * Módulo para el cálculo de los capitales vivos del préstamo y cálculo de los capitales asegurados.
	 * @param proyUmic
	 * @param iteracion
	 * @param fcalc
	 * @param umic
	 * @param btcUmic
	 * @param codSubproceso
	 * @param bloqueCorriente
	 * @param mapVariables
	 * @return cspamort
	 */
	
	
	@SuppressWarnings("unchecked")
	private BigDecimal moduloCSPPPAL(List<DetalleCorriente> proyUmic, int iteracion, Timestamp fcalc, Umic umic,
			DetalleBaseTecnica btcUmic, String codSubproceso, BloqueCorriente bloqueCorriente,
			Map<String, Object> mapVariables) {
		
		//Variables locales
		final IObtenerDatos servicioDatos = FachadaServicios.getObtenerDatos();
		BigDecimal cspppal = BigDecimal.ZERO;
		String varCriterFec;
		List<DetalleCorriente> varProyUmicPrincipal;
		BigDecimal varCSPFallecj = BigDecimal.ZERO; 
		UmicKey varClavePrincipal;
		Umic umicPrincipal;
		
		CuadrosAmortizacion varCuadroAmort;
		//Fin variables locales
		
		if (ModuloCSPPPAL.LOG.isTraceEnabled()) {
			ModuloCSPPPAL.LOG.trace("Inicio función << moduloCSPPPAL >> para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);

		// Definición de variables auxiliares para agilizar las operaciones (se llaman varias veces).
		final Integer ccartera = umic.getDatosGenerales().getCcartera();
		final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		final Integer kgarantia = umic.getDatosGenerales().getKgarantia();
		// Fin de la definición de las variables auxiliares.		
		
		
		//Variables de apoyo
		varCriterFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIFEC);
		
		//Validacion variables de apoyo
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterFec);
		}
		//Fin variables de apoyo
		
		DatosGenerales datosGenerales = umic.getDatosGenerales();
		if (ConstantsModulos.CTE_UMIC_PRINCIPAL.equals(datosGenerales.getSpcom())){
			//Se devuelve error funcional AJ – No se puede recuperar la clave de la garantía principal ya que la umic de 
			//entrada está marcada como garantía principal en el maestro & umic.claveUmic, finalizando el proceso para la UMIC.
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AJ);
		}
		//Se obtiene la clave de la UMIC principal
		varClavePrincipal = UtilUmicPrincipal.getUmicPrincipal(mapVariables, CLAVE_UMICPRINCIPAL, datosGenerales);
		
		//Se obtiene la lista de corrientes de la UMIC principal
		//varProyUmicPrincipal = servicioDatos.recuperarUmicPrincipal(kmodalidad, kpoliza, ksubpoliza, kcertificado, nsuscri, ctipoaport)
		varProyUmicPrincipal = UtilModulos.getListaCorrienteUmic(mapVariables, CLAVELISTACORRUMIC,
				ConstantsModulos.CTE_BTI, btcUmic.getFecCierre(),
				varClavePrincipal);
		if(null == varProyUmicPrincipal || varProyUmicPrincipal.size() == 0){
			mapVariables.remove(CLAVELISTACORRUMIC);
			varProyUmicPrincipal = UtilModulos.getVarLstProyeccion(mapVariables, CLAVELISTACORRUMIC, ConstantsModulos.CTE_BTI_PROY, btcUmic.getFecCierre(), umic.getKey());
		}
		
		if(bloqueCorriente.getFechaDevengo().equals(null)){
			
			cspppal = BigDecimal.ZERO;
			
		}else{
			varCSPFallecj = varProyUmicPrincipal.get(iteracion-1).getBloqueFall().getImpFlujoNominal();
			cspppal = varCSPFallecj;
		}	
		
		if (ModuloCSPPPAL.LOG.isTraceEnabled()) {
			ModuloCSPPPAL.LOG.trace("Fin función << ModuloCSPPPAL >> de la clase ModuloCSPPPAL, para la iteracion = {}, con resultado cspppal = {}", iteracion, cspppal);
		}
		
		return cspppal;
	}
	
}
