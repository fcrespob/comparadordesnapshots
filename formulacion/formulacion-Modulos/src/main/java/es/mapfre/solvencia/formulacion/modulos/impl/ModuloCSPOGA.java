package es.mapfre.solvencia.formulacion.modulos.impl;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.TabOGA;
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
public class ModuloCSPOGA implements Modulo {

	/** Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSPOGA.class);
	
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSPOGA;
	private static final String CLAVE_IPC = ConstantsModulos.CTE_VAR_IPC;
	private static final String CLAVE_VAR_IPC = ConstantsModulos.CTE_VAR_FDIFER.concat(CLAVE_MODULO);
	private static final String CLAVE_OGA = ConstantsModulos.CTE_VAR_OGA;

	@Override
	public String getNombreServicio() {
		return ConstantsFactorias.MODULO_CSPOGA;
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
			
			if (ModuloCSPOGA.LOG.isTraceEnabled()) {
				ModuloCSPOGA.LOG.trace("Inicio de execute en clase ModuloCSPOGA");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloCSPOGA
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
			resultado = moduloCSPOGA(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloCSPOGA.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSPOGA.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSPOGA.LOG.isTraceEnabled()) {
			ModuloCSPOGA.LOG.trace("Fin de execute en clase ModuloCSPOGA");
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
	private BigDecimal moduloCSPOGA(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, 
			final Integer iteracion, final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic,
			final Map<String, Object> mapVariables, String codSubproceso) {
		//Variables locales
		BigDecimal cspOGA = BigDecimal.ZERO;
		IObtenerConfiguracion obtenerConfiguracion = FachadaServicios.getObtenerConfiguracion();
		Timestamp fecOGA;
		Integer it = 0;
		BigDecimal sumCom = BigDecimal.ZERO;
		BigDecimal prim = BigDecimal.ZERO;
		final IAlmacenarDatos almacenarDatos = FachadaServicios.getAlmacenarDatos();
		//Fin variables locales

		if (ModuloCSPOGA.LOG.isTraceEnabled()) {
			ModuloCSPOGA.LOG.trace("Inicio función << moduloCSPOGA >> de la clase ModuloCSPOGA, para la iteracion = {}",iteracion);
		}
		
		fecOGA = (Timestamp) mapVariables.get("FEC_OGA");
		
		if (bloqueCorriente.getFechaDevengo() == null 
				|| fecOGA != null) {
			proyUmic.get(iteracion-1).setOga(true);
			return cspOGA;
		}
		
		mapVariables.put("FEC_OGA", bloqueCorriente.getFechaDevengo());
		
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, fcalc, umic, btcUmic);
		
		if (umic.getDatosNiif17().getkcarcontacto() == null 
				|| umic.getDatosNiif17().getkcarcontacto().isEmpty()) {
			proyUmic.get(iteracion-1).setOga(true);
			almacenarDatos.almacenarProyeccion(proyUmic);
			return cspOGA;
		}
		
		// Recuperamos los datos de la tabla OGA
		TabOGA OGA = obtenerConfiguracion.recuperarTabOGA(umic.getDatosNiif17().getkcarcontacto(), umic.getDatosGenerales().getFecCierre());
		
		if (null == OGA) {
			Incidencia aviso = Solvencia2ExcepcionHelper.crearAviso(btcUmic.getBaseTec(), umic.getDatosGenerales().getCcanal(), umic.getDatosGenerales().getCcartera(), umic.getKey(), btcUmic.getFecCierre(), umic.getDatosGenerales().getCnegocio(), this.getNombreServicio(), ConstantsFunciones.CTE_COD_ERROR_DN, new Object[]{CLAVE_OGA});
			almacenarDatos.almacenarIncidencias(aviso);
			proyUmic.get(iteracion-1).setOga(true);
			almacenarDatos.almacenarProyeccion(proyUmic);
			return cspOGA;
		}
		
		if (proyUmic.get(iteracion - 1).getBloqueComi() == null) {
			sumCom = BigDecimal.ZERO;
			proyUmic.get(iteracion-1).setOga(true);
		} else {
			for (int i = 0; i <= proyUmic.size() - 1; i++) {
				sumCom = sumCom.add(proyUmic.get(i).getBloqueComi().getImpFlujoActualizado());
				proyUmic.get(i).setOga(true);
			}
		}
		
		if (umic.getPrimas().getCformpago().equals("9") ) {
			if(null != umic.getPrimas()){
				if(null != umic.getPrimas().getIprimatarada()){
					prim = umic.getPrimas().getIprimatarada()
							.multiply(umic.getDatosCoaseguro().getPcoaseg().divide(BigDecimal.valueOf(100), ConstantsFunciones.MATH_CONTEXT));
					prim = prim.multiply(
							umic.getComisiones().getPcomisiona1().divide(BigDecimal.valueOf(100), ConstantsFunciones.MATH_CONTEXT));
					sumCom = sumCom.add(prim);
				}
			}
		} else {
			int fracc = 1;
			switch (umic.getPrimas().getCformpago()) {
				case "1":
					fracc=1;
					break;
				case "2":
					fracc=2;
					break;
				case "3":
					fracc=4;
					break;
				case "4":
					fracc=12;
					break;
			}
			
			if(null != umic.getPrimas()){
				if(null != umic.getPrimas().getIprimatarada()){
					prim = umic.getPrimas().getIprimatarada().divide(BigDecimal.valueOf(fracc), ConstantsFunciones.MATH_CONTEXT);
					prim = prim.multiply(
							umic.getComisiones().getPcomisiona1().divide(BigDecimal.valueOf(100), ConstantsFunciones.MATH_CONTEXT));
					sumCom = sumCom.add(prim);
				}
			}
		}
		
		cspOGA = OGA.getOga().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01).multiply(sumCom);
		
		
		if (ModuloCSPOGA.LOG.isTraceEnabled()) {
			ModuloCSPOGA.LOG.trace("Finc función << moduloCSPOGA >> de la clase ModuloCSPOGA, para la iteracion = {}, con resultado csp238 = {}", iteracion, cspOGA);
		}
		
		almacenarDatos.almacenarProyeccion(proyUmic);
		
		return cspOGA;
	}

}
