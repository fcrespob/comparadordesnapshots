package es.mapfre.solvencia.formulacion.util;

import java.math.BigDecimal;
import java.util.Map;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;

/**
 * Clase de utilidades de la Umic Principal
 * 
 */
public class UtilUmicPrincipal {
	
	/**
	 * Devuelve la provisión matemática obtenida del detalle corriente
	 * @param detalleCorriente
	 * @return
	 */
	public static BigDecimal getCSP050(DetalleCorriente detalleCorriente){
		BigDecimal csp050 = detalleCorriente.getTotalFlujoProyeccion().getProvbtiproy();
		
		return csp050;
	}
	
	/**
	 * Devuelve la clave de la Umic Principal. Si no está almacenada la calcula, la setea y la devuelve.
	 * @param mapVariables
	 * @param claveVariable
	 * @param datosGenerales
	 * @return
	 */
	public static UmicKey getUmicPrincipal(final Map<String, Object> mapVariables, final String claveVariable, final DatosGenerales datosGenerales){
		final String clave = claveVariable;
		final Object preSalida = mapVariables.get(clave);
		UmicKey salida = null;
		
		if (null == preSalida) {
			IObtenerDatos servicio = FachadaServicios.getObtenerDatos();
			salida = servicio.recuperarUmicPrincipal(datosGenerales.getKmodalidad(), datosGenerales.getKpoliza(), 
					datosGenerales.getKsubpoliza(), datosGenerales.getKcertificado(), datosGenerales.getNsuscri(),
					datosGenerales.getCtipoaport());
			mapVariables.put(clave, salida);
		} else {
			salida = (UmicKey) preSalida;
		}
		
		return salida;
	}
	public static UmicKey getUmicPrincipalGBT(final String claveVariable, final DatosGenerales datosGenerales){
		UmicKey salida = null;
		
			IObtenerDatos servicio = FachadaServicios.getObtenerDatos();
			salida = servicio.recuperarUmicPrincipal(datosGenerales.getKmodalidad(), datosGenerales.getKpoliza(), 
					datosGenerales.getKsubpoliza(), datosGenerales.getKcertificado(), datosGenerales.getNsuscri(),
					datosGenerales.getCtipoaport());
		
		return salida;
	}																									
}
