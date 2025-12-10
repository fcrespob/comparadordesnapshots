package es.mapfre.solvencia.formulacion.modulos.impl;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.servicios.impl.ObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;



/**
 * Clase encargada del cálculo que devuelve la Renta del Período conforme al plan de rentas de la Umic.
 * 
 * @author agonzalezgar
 *
 */
public class ModuloCSP238IPC implements Modulo {

	/** Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP238IPC.class);
	
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP238IPC;
	private static final String CLAVE_IPC = ConstantsModulos.CTE_VAR_IPC;
	private static final String CLAVE_VAR_IPC = ConstantsModulos.CTE_VAR_FDIFER.concat(CLAVE_MODULO);

	@Override
	public String getNombreServicio() {
		return ConstantsFactorias.MODULO_CSP238IPC;
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
			
			if (ModuloCSP238IPC.LOG.isTraceEnabled()) {
				ModuloCSP238IPC.LOG.trace("Inicio de execute en clase ModuloCSP238IPC");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloCSP238
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			// Fin de la recuperación de los datos.
			
			//Invocamos a la funciónde calculo CSP238
			resultado = moduloCSP238IPC(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloCSP238IPC.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP238IPC.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP238IPC.LOG.isTraceEnabled()) {
			ModuloCSP238IPC.LOG.trace("Fin de execute en clase ModuloCSP238IPC");
		}
		
		return resultado;

	}


	/** 
	 * Modulo de cálculo que devuelve la Renta del Período conforme al plan de rentas de la Umic multiplicandole por el IPC.
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
	private BigDecimal moduloCSP238IPC(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, 
			final Integer iteracion, final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic,
			final Map<String, Object> mapVariables, String codSubproceso) {
		//Variables locales
		BigDecimal csp238IPC = BigDecimal.ZERO;
		BigDecimal impPago;
		BigDecimal IPC;
		String aux_IPC = ConstantsFunciones.CTE_CADENA_VACIA;
		String aux_IPC_Cadena = ConstantsFunciones.CTE_CADENA_VACIA;
		IObtenerConfiguracion obtenerConfiguracion = FachadaServicios.getObtenerConfiguracion();
		BigDecimal aux_impPago = BigDecimal.ZERO;
		BigDecimal aux_impPago_IPC = BigDecimal.ZERO;
		BigDecimal aux_impPagoMod = BigDecimal.ZERO;
		Boolean salida = false;
		Timestamp fecIni;
		Integer it = 0;
		//Fin variables locales

		if (ModuloCSP238IPC.LOG.isTraceEnabled()) {
			ModuloCSP238IPC.LOG.trace("Inicio función << moduloCSP238IPC >> de la clase ModuloCSP238IPC, para la iteracion = {}",iteracion);
		}
		
		if (bloqueCorriente.getFechaDevengo() == null) {
			return csp238IPC;
		}
		
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, fcalc, umic, btcUmic);
		
		// Recuperamos los datos especificos para la umic con el codigo
		aux_IPC = UtilModulos.getDatosEspecificosUmic(mapVariables, 
				   CLAVE_VAR_IPC,
				   umic.getDatosGenerales().getKpoliza()	 , 
				   umic.getDatosGenerales().getKsubpoliza()  ,
				   umic.getDatosGenerales().getKcertificado(),
				   umic.getDatosGenerales().getNsuscri(),
				   CLAVE_IPC);
		
		if(null == aux_IPC){
			
			//throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_P3TOP});
			IPC = new BigDecimal(100);
			aux_IPC = "1,00";
			final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();
			Incidencia aviso3 = Solvencia2ExcepcionHelper.crearAviso(btcUmic.getBaseTec(), umic.getDatosGenerales().getCcanal(), umic.getDatosGenerales().getCcartera(), umic.getKey(), btcUmic.getFecCierre(), umic.getDatosGenerales().getCnegocio(), this.getNombreServicio(), ConstantsFunciones.CTE_COD_ERROR_DQ, new Object[]{CLAVE_IPC});
			servicio.almacenarIncidencias(aviso3);
			
		}
		IPC =  (BigDecimal) mapVariables.get("IPC_CSP238IPC");
		if (IPC == null) {
			for (int j = 0; j < aux_IPC.length(); j++) {
				if (aux_IPC.substring(j,j+1).equals(",")) {
					aux_IPC_Cadena = aux_IPC_Cadena + ".";
				} else {
					aux_IPC_Cadena = aux_IPC_Cadena + aux_IPC.substring(j, j+1);
				}
			}
			aux_IPC = aux_IPC_Cadena;
			IPC = new BigDecimal(aux_IPC);
			mapVariables.put("IPC_CSP238IPC", IPC);
		}
		
		/* csp238 = proyUmic.impPago */
		
		fecIni = (Timestamp) mapVariables.get("FECINI_CSP238IPC");
		if (fecIni == null) {	
			fecIni = umic.getRentas().getFecIni();
			it = iteracion;
			
			if (UtilFechas.getAnio(fecIni) != UtilFechas.getAnio(bloqueCorriente.getFechaDevengo())) {
				while(!salida) {		
					
					impPago = proyUmic.get(it - 1).getImpPago();
	
					if ( null != impPago ) {
						aux_impPago = (BigDecimal) mapVariables.get("P_IMPAGO_CSP238IPC");
						aux_impPago_IPC = (BigDecimal) mapVariables.get("IMPAGO_CSP238IPC");
						if (aux_impPago == null) {
							aux_impPago = BigDecimal.ZERO;
						}
						if (aux_impPago_IPC == null) {
							aux_impPago_IPC = impPago;
						}
						
						if (UtilFechas.getMes(fecIni) == 1 && it != 1) {
							aux_impPago = aux_impPago_IPC.multiply(IPC);
							mapVariables.put("P_IMPAGO_CSP238IPC", aux_impPago);
							aux_impPago_IPC = aux_impPago_IPC.add(aux_impPago);
						} 
						csp238IPC = aux_impPago_IPC;
						mapVariables.put("IMPAGO_CSP238IPC", csp238IPC);
					}				
					
					if(UtilFechas.getAnio(fecIni) >= UtilFechas.getAnio(bloqueCorriente.getFechaDevengo())){
						salida = true;
					}
					fecIni = UtilFechas.incrMeses(fecIni, bloqueCorriente.getFechaDevengo(), 1, false);
					mapVariables.put("FECINI_CSP238IPC",fecIni);
					it++;
					if (it > proyUmic.size()) {
						it = proyUmic.size();
					}
				}
			}else {
				mapVariables.put("FECINI_CSP238IPC",fecIni);
				salida = true;
			}
			
		} 
		
		impPago = proyUmic.get(iteracion - 1).getImpPago();
		if ((UtilFechas.getAnio(bloqueCorriente.getFechaDevengo()) < UtilFechas.getAnio(fecIni))
				|| ((UtilFechas.getAnio(bloqueCorriente.getFechaDevengo()) == UtilFechas.getAnio(fecIni))
						&& (UtilFechas.getMes(bloqueCorriente.getFechaDevengo()) < UtilFechas.getMes(fecIni)))
				|| ((UtilFechas.getAnio(bloqueCorriente.getFechaDevengo()) == UtilFechas.getAnio(fecIni))
						&& (UtilFechas.getMes(bloqueCorriente.getFechaDevengo()) == UtilFechas.getMes(fecIni))
						&& (UtilFechas.getDia(bloqueCorriente.getFechaDevengo()) < UtilFechas.getDia(fecIni)))) {
			salida = true;
		}

		if ( null != impPago ) {
			aux_impPago = (BigDecimal) mapVariables.get("P_IMPAGO_CSP238IPC");
			aux_impPago_IPC = (BigDecimal) mapVariables.get("IMPAGO_CSP238IPC");
			if (aux_impPago == null) {
				aux_impPago = BigDecimal.ZERO;
			}
			if (aux_impPago_IPC == null) {
				aux_impPago_IPC = impPago;
			}
			
			if (UtilFechas.getMes(proyUmic.get(iteracion - 1).getFechaDesde()) == 1 && salida == false) {
				aux_impPago = aux_impPago_IPC.multiply(IPC);
				mapVariables.put("P_IMPAGO_CSP238IPC", aux_impPago);
				aux_impPago_IPC = aux_impPago_IPC.add(aux_impPago);	
			} 
			//csp238IPC = impPago.add(aux_impPago);
			csp238IPC = aux_impPago_IPC;
			mapVariables.put("IMPAGO_CSP238IPC", csp238IPC);
		}
				
		if (ModuloCSP238IPC.LOG.isTraceEnabled()) {
			ModuloCSP238IPC.LOG.trace("Finc función << moduloCSP238 >> de la clase ModuloCSP238IPC, para la iteracion = {}, con resultado csp238 = {}", iteracion, csp238IPC);
		}
		
		return csp238IPC;
	}

}
