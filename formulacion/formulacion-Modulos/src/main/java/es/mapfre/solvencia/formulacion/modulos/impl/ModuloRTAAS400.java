package es.mapfre.solvencia.formulacion.modulos.impl;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.formulacion.CriterioFechas;
import es.mapfre.solvencia.dominio.formulacion.PlanPagos;
import es.mapfre.solvencia.dominio.maestro.Rentas;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.formulacion.util.UtilFechas.Fecha;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;


public class ModuloRTAAS400 implements Modulo {

	/** Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloRTAAS400.class);
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_RTAAS400;
	private static final String CLAVE_IMPPAGO = ConstantsModulos.CTE_UMIC.concat(CLAVE_MODULO);

	@Override
	public String getNombreServicio() {
		return ConstantsFactorias.MODULO_RTAAS400;
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
			
			if (ModuloRTAAS400.LOG.isTraceEnabled()) {
				ModuloRTAAS400.LOG.trace("Inicio de execute en clase ModuloRTAAS400");
			}
			
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			//final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			//final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			// Fin de la recuperación de los datos.
			
			//Invocamos a la funciónde calculo CSP238
			resultado = moduloRTAAS400(proyUmic, iteracion, fcalc, umic, btcUmic, codSubproceso, bloqueCorriente, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloRTAAS400.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloRTAAS400.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloRTAAS400.LOG.isTraceEnabled()) {
			ModuloRTAAS400.LOG.trace("Fin de execute en clase ModuloRTAAS400");
		}
		
		return resultado;

	}


	/** 
	 * Modulo de cálculo que devuelve la Renta del Período conforme al plan de rentas de la Umic.
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
	private BigDecimal moduloRTAAS400(final List<DetalleCorriente> proyUmic, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, String codSubproceso, 
			BloqueCorriente bloqueCorriente, Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal rtaas400 = BigDecimal.ZERO;
		BigDecimal impPago;
		List<PlanPagos> lstUmicPlanPagos = null;
		CriterioFechas criterioFechas = null;
		String criterioFecPago = ConstantsFunciones.CTE_CADENA_VACIA;
		String criterioFecDev = ConstantsFunciones.CTE_CADENA_VACIA;
		Timestamp fechaPago;
		final IObtenerConfiguracion obtenerConf = FachadaServicios.getObtenerConfiguracion();
		//Fin variables locales

		if (ModuloRTAAS400.LOG.isTraceEnabled()) {
			ModuloRTAAS400.LOG.trace("Inicio función << moduloRTAAS400 >> de la clase ModuloRTAAS400, para la iteracion = {}",iteracion);
		}
		
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, fcalc, umic, btcUmic);
		
		criterioFechas = obtenerConf.recuperarCriterioFechas(umic.getDatosGenerales().getKmodalidad(),
				umic.getDatosGenerales().getKgarantia(), umic.getDatosGenerales().getKprestacion(), umic.getDatosAdicionales().getPrestCal(), codSubproceso);

		ValidacionesComunesModulos.validarCriterioFechaRecuperado(criterioFechas,
				umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), umic.getDatosGenerales().getKprestacion());

		// Validamos el criterio de la fecha de pago y la fecha devengo
		criterioFecPago = criterioFechas.getFecPago();
		criterioFecDev = criterioFechas.getFecDevengo();

		ValidacionesComunesModulos.validarCriterioFechaPagoDevengo(criterioFecPago, criterioFecDev,
				codSubproceso);
		
		if (bloqueCorriente.getFechaDevengo() == null) {
			return rtaas400;
		}
		
		impPago = (BigDecimal) mapVariables.get(CLAVE_IMPPAGO);
		if(null == impPago){
			impPago = umic.getRentas().getRentini();
		}
		if (proyUmic.get(iteracion - 1).getBloqueBySubproceso(codSubproceso).getFechaPago() == null) {
			rtaas400 = impPago;
		}else {
			fechaPago = proyUmic.get(iteracion - 1).getBloqueBySubproceso(codSubproceso).getFechaPago();
			if ( fechaPago.before(umic.getFechas().getFecfinpagprim()) ) {
				rtaas400 = impPago;
			}else {
				rtaas400 = importePagoRevalRentaGeoaArit(iteracion, umic, impPago, fechaPago,criterioFecPago,criterioFecDev);
			}
	    }
				
		if (ModuloRTAAS400.LOG.isTraceEnabled()) {
			ModuloRTAAS400.LOG.trace("Finc función << moduloRTAAS400 >> de la clase ModuloRTAAS400, para la iteracion = {}, con resultado rtaas400 = {}", iteracion, rtaas400);
		}
		mapVariables.put(CLAVE_IMPPAGO, rtaas400);
		return rtaas400;
	}
	
	private BigDecimal importePagoRevalRentaGeoaArit(final Integer iteracion, final Umic umic, BigDecimal impPago,
			final Timestamp fechaPPActual, String criterioFecPago, String criterioFecDev) {
		// Variables locales
		BigDecimal importe = BigDecimal.ZERO;

		if(criterioFecPago.equals("PLANB") && criterioFecDev.equals("PLANB")){
			if((ConstantsModulos.CTE_FR_ANIO_NATU.equals(umic.getRentas().getCtipoRevrenta()) ||
					ConstantsModulos.CTE_FR_ANIO_NATU_2.equals(umic.getRentas().getCtipoRevrenta()) ||
					ConstantsModulos.CTE_FR_ANIO_NATU_3.equals(umic.getRentas().getCtipoRevrenta()) ||
					ConstantsModulos.CTE_FR_ANIO_NATU_4.equals(umic.getRentas().getCtipoRevrenta()) ||
					ConstantsModulos.CTE_FR_ANIO_NATU_5.equals(umic.getRentas().getCtipoRevrenta()) ||
					ConstantsModulos.CTE_FR_ANIO_NATU_6.equals(umic.getRentas().getCtipoRevrenta()) ||
					ConstantsModulos.CTE_FR_ANIO_NATU_7.equals(umic.getRentas().getCtipoRevrenta()) ||
					ConstantsModulos.CTE_FR_ANIO_NATU_8.equals(umic.getRentas().getCtipoRevrenta()) || 
					ConstantsModulos.CTE_FR_ANIO_NATU_9.equals(umic.getRentas().getCtipoRevrenta()) ||
					ConstantsModulos.CTE_FR_ANIO_NATU_O.equals(umic.getRentas().getCtipoRevrenta()) ||
					ConstantsModulos.CTE_FR_ANIO_NATU_N.equals(umic.getRentas().getCtipoRevrenta()) ||
					ConstantsModulos.CTE_FR_ANIO_NATU_D.equals(umic.getRentas().getCtipoRevrenta()))){	
					
					
				int mesPago;
				if(ConstantsModulos.CTE_FR_ANIO_NATU_O.equals(umic.getRentas().getCtipoRevrenta())){
					mesPago = 10;
				}else if(ConstantsModulos.CTE_FR_ANIO_NATU_N.equals(umic.getRentas().getCtipoRevrenta())){
					mesPago = 11;
				}else if(ConstantsModulos.CTE_FR_ANIO_NATU_D.equals(umic.getRentas().getCtipoRevrenta())){
					mesPago = 12;
				}else{
					mesPago = Integer.parseInt(umic.getRentas().getCtipoRevrenta());
				}
				
				importe = calculoImportePagoMesNatural(fechaPPActual, criterioFecPago, criterioFecDev, umic, impPago, mesPago);
					
			}else{
				importe = umic.getRentas().getRentini();
			}		
				
		}else{
				
			// Si Ctipo_revrenta = 1 (Anno Natural (Enero))
			if (ConstantsModulos.CTE_FR_ANIO_NATU.equals(umic.getRentas().getCtipoRevrenta())) {
				importe = calculoImportePagoAnioNatural(fechaPPActual, criterioFecPago, criterioFecDev, umic, impPago);
			} else if (ConstantsModulos.CTE_FR_ANIVER.equals(umic.getRentas().getCtipoRevrenta())) {
				// Si Ctipo_revrenta = 2 (Aniversario)
				importe = calculoImportePagoAniversario(fechaPPActual, criterioFecPago, criterioFecDev, umic);
			} else {
				importe = umic.getRentas().getRentini();
			}
		}

	return importe;
	}
	
	private BigDecimal calculoImportePagoAniversario(final Timestamp fechaPPActual, 
			final String criterioFecPago, final String criterioFecDev, final Umic umic) {
		// Variables locales
		BigDecimal importe = BigDecimal.ZERO;
		BigDecimal pagoAnterior = BigDecimal.ZERO;
		BigDecimal pago = BigDecimal.ZERO;
		
		pago = calculoImportePlanPagoSegunTipoRevRenta(umic.getRentas().getCformaRevrenta(), umic.getRentas().getRentini(),
				umic.getRentas().getPrevrenta());


		if (ConstantsModulos.CTE_REV_RENTA_ARI.equals(umic.getRentas().getCformaRevrenta())) {
			importe = pagoAnterior.add(pago);
		} else if ((ConstantsModulos.CTE_REV_RENTA_GEO.equals(umic.getRentas().getCformaRevrenta()))
				|| (ConstantsModulos.CTE_REV_RENTA_NEG.equals(umic.getRentas().getCformaRevrenta()))) {
			importe = pagoAnterior.multiply(pago);
		}

		return importe;

	}

	private BigDecimal calculoImportePagoAnioNatural(final Timestamp fechaPPActual, final String criterioFecPago, final String criterioFecDev, 
			final Umic umic, final BigDecimal pagoAnterior) {
		// Variables locales
		BigDecimal importe = BigDecimal.ZERO;
		BigDecimal pago = BigDecimal.ZERO;

		// Si Anno(Umic.PlanPagos(n).FechaPago) = Anno(Umic.PlanPagos(n-1).FechaPago)
		if (UtilFechas.getAnio(fechaPPActual) == UtilFechas.getAnio(umic.getFechas().getFecfinpagprim())) {
			// Umic.PlanPagos(n).ImportePago = Umic.PlanPagos(n-1).ImportePago
			importe = pagoAnterior;
		} else if (UtilFechas.getAnio(fechaPPActual) > UtilFechas.getAnio(umic.getFechas().getFecfinpagprim())) {

			pago = calculoImportePlanPagoSegunTipoRevRenta(umic.getRentas().getCformaRevrenta(), umic.getRentas().getRentini(),umic.getRentas().getPrevrenta());
			
			if (ConstantsModulos.CTE_REV_RENTA_ARI.equals(umic.getRentas().getCformaRevrenta())) {
				importe = pagoAnterior.add(pago);
			} else if ((ConstantsModulos.CTE_REV_RENTA_GEO.equals(umic.getRentas().getCformaRevrenta()))
					|| (ConstantsModulos.CTE_REV_RENTA_NEG.equals(umic.getRentas().getCformaRevrenta()))) {
				importe = pagoAnterior.multiply(pago);
			}

		}

		return importe;
	}

	private BigDecimal calculoImportePagoMesNatural(final Timestamp fechaPPActual, final String criterioFecPago, 
			  final String criterioFecDev, final Umic umic, final BigDecimal pagoAnterior, int mesPago) {
		// Variables locales
		BigDecimal importe = BigDecimal.ZERO;
		BigDecimal pago = BigDecimal.ZERO;
		
		if (UtilFechas.getMes(fechaPPActual) != mesPago) {
			importe = pagoAnterior;
		}else{

			pago = calculoImportePlanPagoSegunTipoRevRenta(umic.getRentas().getCformaRevrenta(), umic.getRentas().getRentini(),umic.getRentas().getPrevrenta());
			
			if (ConstantsModulos.CTE_REV_RENTA_ARI.equals(umic.getRentas().getCformaRevrenta())) {
				importe = pagoAnterior.add(pago);
			} else if ((ConstantsModulos.CTE_REV_RENTA_GEO.equals(umic.getRentas().getCformaRevrenta()))
					|| (ConstantsModulos.CTE_REV_RENTA_NEG.equals(umic.getRentas().getCformaRevrenta()))) {
				importe = pagoAnterior.multiply(pago);
			}

		}

		return importe;
	}
	
	private BigDecimal calculoImportePlanPagoSegunTipoRevRenta(final String cFormaRevRenta, final BigDecimal rentini,
			final BigDecimal prevrenta) {
		// Variables locales
		BigDecimal importe = BigDecimal.ZERO;

		if (ConstantsModulos.CTE_REV_RENTA_ARI.equals(cFormaRevRenta)) {
			importe = rentini.multiply(prevrenta).divide(ConstantsFunciones.CTE_OPER_100,
					ConstantsFunciones.MATH_CONTEXT);
		} else if (ConstantsModulos.CTE_REV_RENTA_GEO.equals(cFormaRevRenta)) {
			importe = BigDecimal.ONE
					.add(prevrenta.divide(ConstantsFunciones.CTE_OPER_100, ConstantsFunciones.MATH_CONTEXT));
		} else if (ConstantsModulos.CTE_REV_RENTA_NEG.equals(cFormaRevRenta)) {
			// Si cforma-revrenta = G (Geometrica) || Si cforma-revrenta = N (Negativa):
			importe = BigDecimal.ONE
					.subtract(prevrenta.divide(ConstantsFunciones.CTE_OPER_100, ConstantsFunciones.MATH_CONTEXT));

		}

		return importe;
	}


}
