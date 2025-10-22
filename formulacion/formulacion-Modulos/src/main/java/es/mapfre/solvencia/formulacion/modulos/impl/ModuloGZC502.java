package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;

/**
 * 
 * @author Aleksandar Plamenov Nedyalkov
 *
 */

public class ModuloGZC502 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloGZC502.class);
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_GZC502;
	private static final String CLAVE_VAR_FEC502G= ConstantsModulos.CTE_VAR_FEC502G;
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);



	@Override
	public String getNombreServicio() {
		return ConstantsFactorias.MODULO_GZC502;
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
			if (ModuloGZC502.LOG.isTraceEnabled()) {
				ModuloGZC502.LOG.trace("Inicio de execute en clase ModuloGZC502");
			}
			
			//Recuperamos los datos que le pasaremos a la función ModuloGZC504
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de los datos.
			
			//Invocamos a la función de calculo
			resultado = moduloGZC502(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloGZC502.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloGZC502.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloGZC502.LOG.isTraceEnabled()) {
			ModuloGZC502.LOG.trace("Fin de execute en clase ModuloGZC502");
		}
		
		return resultado;
	}
	
	/**
	 * Modulo de cálculo que devuelve el saldo de la umic (Saldo actual modalidades basadas en movimientos)
	 * @param proyUmic
	 * @param bloqueCorriente
	 * @param iteracion
	 * @param fcalc
	 * @param umic
	 * @param btcUmic
	 * @param mapVariables
	 * @param codSubproceso
	 * @return
	 */
	private BigDecimal moduloGZC502(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, Map<String, Object> mapVariables,
			String codSubproceso) {
		
		BigDecimal gzc502 = BigDecimal.ZERO;
		BigDecimal varGic = BigDecimal.ZERO;
		BigDecimal varIcapIni;
		Timestamp varfecvcto = null;
		Timestamp Aux_facefecfin = new Timestamp(new GregorianCalendar(9999,12,31,ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		String Aux_varfec502G = null;
		String varCriterFec = ConstantsFunciones.CTE_CADENA_VACIA;
		Timestamp varfec502G = null;
		BigDecimal Numerador = BigDecimal.ZERO;
		BigDecimal Denominador = BigDecimal.ZERO;
		IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
		
		
		if (ModuloGZC502.LOG.isTraceEnabled()) {
			ModuloGZC502.LOG.trace("Inicio función << moduloGZC502 >> de la clase ModuloGZC502, para la iteracion = {}", iteracion);
		}
		
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		varCriterFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC,
				umic.getDatosGenerales().getCcartera(), umic
						.getDatosGenerales().getKmodalidad(), umic
						.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);
		
		if ( null == bloqueCorriente.getFechaDevengo() )
			return gzc502;
		
		varfec502G = (Timestamp) mapVariables.get(CLAVE_VAR_FEC502G);
		if (varfec502G == null){
			Aux_varfec502G  = servicio.recuperarDatosEspecificosUmic(umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_VAR_FEC502G);
			if (Aux_varfec502G == null) {
				varfec502G = new Timestamp(new GregorianCalendar(2019,05,24,ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());	
			}else {
				SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyyMMdd");
				try {
					Date fechaParseada = formatoFecha.parse(Aux_varfec502G);
					varfec502G = new Timestamp(fechaParseada.getTime());
				}
				catch(ParseException e) {
					throw Solvencia2ExcepcionHelper.crearExcepcion("AP", new String[]{Aux_varfec502G, "yyyyMMdd"});
				}
			}
			mapVariables.put(CLAVE_VAR_FEC502G, varfec502G);
		}
		
        varGic = btcUmic.getGtorosspCap();
		
		if(null == varGic || varGic.equals(BigDecimal.ZERO)){
			varGic = BigDecimal.valueOf(1.5);
		}
		varGic = varGic.divide(new BigDecimal(100), ConstantsFunciones.MATH_CONTEXT);
		
		varIcapIni = umic.getCapitales().getIcapini();
		
		if (umic.getFechas().getFecefecfin().before(Aux_facefecfin) || umic.getFechas().getFecefecfin().after(Aux_facefecfin)) {
			varfecvcto = umic.getFechas().getFecefecfin();
		}else {
			varfecvcto = proyUmic.get(proyUmic.size()).getFechaHasta();
		}
		
		
		
		Numerador = varGic.multiply(varIcapIni.multiply(new BigDecimal(FuncionesAuxiliares.nDias(proyUmic.get(iteracion - 1).getFechaHasta(),proyUmic.get(iteracion - 1).getFechaDesde(), varCriterFec))));
		Denominador = new BigDecimal (FuncionesAuxiliares.nDias(varfec502G,varfecvcto, varCriterFec));
		
		gzc502 = Numerador.divide(Denominador,ConstantsFunciones.MATH_CONTEXT);
				
		if (ModuloGZC502.LOG.isTraceEnabled()) {
			ModuloGZC502.LOG.trace("Fin función << moduloGZC502 >> de la clase ModuloGZC502, para la iteracion = {} con resultado gzc502 = {}", iteracion, gzc502);
		}
				
		return gzc502;
	}
	
}
