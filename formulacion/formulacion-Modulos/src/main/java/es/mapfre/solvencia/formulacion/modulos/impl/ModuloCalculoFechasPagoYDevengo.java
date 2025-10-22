package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.formulacion.CriterioFechas;
import es.mapfre.solvencia.dominio.formulacion.PlanPagos;
import es.mapfre.solvencia.dominio.maestro.PagosPlanificados;
import es.mapfre.solvencia.dominio.maestro.Rentas;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilFechas.Fecha;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

public class ModuloCalculoFechasPagoYDevengo implements Modulo {

	private static final Logger LOG = LoggerFactory.getLogger(ModuloCalculoFechasPagoYDevengo.class);

	private static final String CTE_VAR_TC_FECHAS = ConstantsModulos.CTE_VAR_TC
			.concat(ConstantsFactorias.PROGRAMA_FEC001);

	private static final String CLAVE_PCTREVRENT = ConstantsModulos.CTE_PCTREVRENT;
	private static final String CLAVE_VAR_PCTREVRENT = CLAVE_PCTREVRENT.concat(ConstantsFactorias.MODULO_FECHAS_PD);
	
	@Override
	public String getNombreServicio() {
		return ConstantsFactorias.MODULO_FECHAS_PD;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object execute(final Object... args) throws Solvencia2Excepcion {

		try {
			if (ModuloCalculoFechasPagoYDevengo.LOG.isTraceEnabled()) {
				ModuloCalculoFechasPagoYDevengo.LOG.trace("Inicio de execute en clase ModuloCalculoFechasPagoYDevengo");
			}
			// Recuperamos los datos que le pasaremos a la funcion
			// ModuloCalculoFechasPagoYDevengo
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_UMIC_FECHAS];
			final FichaProceso fichaProceso = (FichaProceso) args[ConstantsModulos.PARAM_FIC_PROC_FECHAS];
			// final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica)
			// args[ConstantsModulos.PARAM_BTC_FECHAS];
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_DETALLE_FECHAS];
			final String subProcesoActual = (String) args[ConstantsModulos.PARAM_SUBRPROCESO_FECHAS];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_MAPA_VAR_FECHAS];

			// Invocamos a la funcion de calculo calcularFechasPagYDev
			calcularFechasPagYDev(umic, fichaProceso, proyUmic, subProcesoActual, mapVariables);

		} catch (Solvencia2Excepcion e) {
			ModuloCalculoFechasPagoYDevengo.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCalculoFechasPagoYDevengo.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloCalculoFechasPagoYDevengo.LOG.isTraceEnabled()) {
			ModuloCalculoFechasPagoYDevengo.LOG.trace("Fin de execute en clase ModuloCalculoFechasPagoYDevengo");
		}

		return BigDecimal.ZERO;
	}

	/**
	 * Funcion encargada de encapsular las acciones necesarias para el calculo tanto
	 * de la fecha de pago como la fecha devengo
	 * 
	 * @param umic             datos de la umic
	 * @param fichaProceso     ficha proceso
	 * @param btc              base tecnica
	 * @param subProcesoActual subproceso ejecutado
	 */
	private void calcularFechasPagYDev(final Umic umic, final FichaProceso fichaProceso,
			final List<DetalleCorriente> detallesCorriente, final String subProcesoActual,
			final Map<String, Object> mapVariables) {
		// Variables locales
		final IObtenerDatos obtenerDatos = FachadaServicios.getObtenerDatos();
		final IObtenerConfiguracion obtenerConf = FachadaServicios.getObtenerConfiguracion();
		final IAlmacenarDatos almacenarDatos = FachadaServicios.getAlmacenarDatos();
		List<PagosPlanificados> lstPagosPlani = null;
		List<PlanPagos> lstPlanPagos = null;
		int numPeriodos = 0;
		int iteracion = 1;
		Timestamp varfecfinproy;
		CriterioFechas criterioFechas = null;
		String criterioFecPago = ConstantsFunciones.CTE_CADENA_VACIA;
		String criterioFecDev = ConstantsFunciones.CTE_CADENA_VACIA;
		Timestamp varFecIni = null;
		Integer varTc = 0;
		Integer nPeriComi3 = ConstantsFunciones.CTE_0;
		String varCalcFechas = ConstantsModulos.CTE_S;
		// Fin variables locales

		if (ModuloCalculoFechasPagoYDevengo.LOG.isTraceEnabled()) {
			ModuloCalculoFechasPagoYDevengo.LOG
					.trace("Inicio funcion << calcularFechasPagYDev >> de la clase ModuloCalculoFechasPagoYDevengo");
		}

		/**
		 * Si codSubproceso = 'PROY_COMI', se debera comprobar si deben calcualrse fechas y no para la umic en funcion del periodo 
		 * de comisionamiento de la misma.  Para ello se comprobaron los siguientes datos de la umic: 
		 *		- umic.comisiones.npericomi
		 *		- umic.comisiones.npericomi2
		 *		- umic.comisiones.npericomi3
		 *		
		 *		Se inicicializara la variable varCalcFechas = 'S'
		 *		
		 *		Se calculara: 
		 *			varTC = TC(umic.fechas fecinisus, fichaProceso.fecCalc) --> dejo la variable en memoria, disponible para el subproceso de la umic.
		 *				- Si varTC  > 2
		 *					- Si NPERICOMI3=0
		 *						- varCalcFechas = 'N'
		 *					- Si NPERICOMI3=1  y varTC > 3 
		 *						- varCalcFechas = 'N'
		 *					- Si NPERICOMI3=3  y varTC > 6
		 *						- varCalcFechas = 'N'
		 *					- Si NPERICOMI3 <> (1,3)   y varTC > NPERICOMI3
		 *						- varCalcFechas = 'N'
		 *		Si codSubproceso = 'PROY_COMI' y varCalcFechas = 'N'  NO se deberan calcualr las fechas de pago y devengo para la corriente, 
		 *		finalizando asi el programa para la umic. 
		 *		Si codSubproceso <> ('PROY_PRV', 'PROY_COMI') o si codSubproceso = 'PROY_COMI' y varCalcFechas = 'S'
		 */
//		if (ConstantsModulos.CTE_PROY_COMI.equals(subProcesoActual)) {
//			// 4.7.1 comprobarSubprocesoComisiones
//			List<CominisionesParticipadasCOM> varDatosComisiones = null;
//			CominisionesParticipadasCOMDao dao = new CominisionesParticipadasCOMDao();
//			
//			if(umic.getDatosGenerales().getCnegocio().equals(ConstantsModulos.CTE_NEGOCIO_COLECTIVO)) {
//				
//				varDatosComisiones = dao.obtenerCominisionParticipada(
//						umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(),
//						umic.getDatosGenerales().getKgarantia(), fichaProceso.getFcalc(), umic.getFechas().getFecinisus());
//				
//			}else if(umic.getDatosGenerales().getCnegocio().equals(ConstantsModulos.CTE_NEGOCIO_INDIVIDUAL)){
//				
//				varDatosComisiones = dao.obtenerCominisionParticipada(
//						umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(),
//						umic.getDatosGenerales().getKgarantia(), fichaProceso.getFcalc(), umic.getFechas().getFecefecini());
//			}
//			
//			if(varDatosComisiones == null || varDatosComisiones.isEmpty()) {
//				//throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AZ);	
//				nPeriComi3 = umic.getComisiones().getNpericomi3();
//			}else{
//				CominisionesParticipadasCOM comision = varDatosComisiones.get(0);
//				nPeriComi3 = comision.getnPeriComi3();
//			}
//			
//			
//			
//		//umic.getComisiones().getNpericomi3();
//
//			varTc = UtilModulos.getVarTC(mapVariables, ModuloCalculoFechasPagoYDevengo.CTE_VAR_TC_FECHAS,
//					umic.getFechas().getFecinisus(), fichaProceso.getFcalc());
//
//			if (varTc.compareTo(ConstantsFunciones.CTE_2) > 0) {
//				if (nPeriComi3.equals(ConstantsFunciones.CTE_0)
//						|| (nPeriComi3.equals(ConstantsFunciones.CTE_1)
//								&& varTc.compareTo(ConstantsFunciones.CTE_3) > 0)
//						|| (nPeriComi3.equals(ConstantsFunciones.CTE_3)
//								&& varTc.compareTo(ConstantsFunciones.CTE_6) > 0)
//						|| ((!nPeriComi3.equals(ConstantsFunciones.CTE_1)
//								&& !nPeriComi3.equals(ConstantsFunciones.CTE_3)) && varTc.compareTo(nPeriComi3) > 0)) {
//					varCalcFechas = ConstantsModulos.CTE_N;
//				}
//			}
//		}

		if (ConstantsModulos.CTE_S.equals(varCalcFechas)) {
			// Recuperamos el criterio de las fechas
			criterioFechas = obtenerConf.recuperarCriterioFechas(umic.getDatosGenerales().getKmodalidad(),
					umic.getDatosGenerales().getKgarantia(), umic.getDatosGenerales().getKprestacion(), umic.getDatosAdicionales().getPrestCal(),
					subProcesoActual);

			ValidacionesComunesModulos.validarCriterioFechaRecuperado(criterioFechas,
					umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), umic.getDatosGenerales().getKprestacion());

			// Validamos el criterio de la fecha de pago y la fecha devengo
			criterioFecPago = criterioFechas.getFecPago();
			criterioFecDev = criterioFechas.getFecDevengo();

			ValidacionesComunesModulos.validarCriterioFechaPagoDevengo(criterioFecPago, criterioFecDev,
					subProcesoActual);

			// EstablecerFinProyeccion
			numPeriodos = detallesCorriente.size();
			if (numPeriodos == 0 || detallesCorriente.isEmpty()) {
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_GZ,
						new String[] {});
			}
			varfecfinproy = detallesCorriente.get(numPeriodos - 1).getFechaHasta();
			varfecfinproy = UtilModulos.getVarFecFinProy(mapVariables, ConstantsFactorias.MODULO_FECHAS_PD,
					varfecfinproy);


			/**
			 * Si criterioFechas.FecPago y/o criterioFechas.FecDevengo es igual a 'PLANI' o 'EFTEC', se deberan calcular los pagos 
			 * en base al tipo de prestacion de la Umic. 
			 */
			if ((ConstantsModulos.CTE_VAL_PLANI.equals(criterioFecPago)
					|| ConstantsModulos.CTE_VAL_PLANI.equals(criterioFecDev))
					|| (ConstantsModulos.CTE_VAL_PVIDA.equals(criterioFecPago)
							|| ConstantsModulos.CTE_VAL_PVIDA.equals(criterioFecDev))
					|| (ConstantsModulos.CTE_VAL_PLANB.equals(criterioFecPago)
							|| ConstantsModulos.CTE_VAL_PLANB.equals(criterioFecDev))
					|| (ConstantsModulos.CTE_VAL_EFTEC.equals(criterioFecPago)
							|| ConstantsModulos.CTE_VAL_EFTEC.equals(criterioFecDev)
							|| (ConstantsModulos.CTE_VAL_PLAIN.equals(criterioFecPago)
									|| ConstantsModulos.CTE_VAL_PLAIN.equals(criterioFecDev))
							|| (ConstantsModulos.CTE_VAL_EFTIN.equals(criterioFecPago)
									|| ConstantsModulos.CTE_VAL_EFTIN.equals(criterioFecDev))
							|| ConstantsModulos.CTE_VAL_PLNTP.equals(criterioFecDev))
					|| (ConstantsModulos.CTE_VAL_PLANC.equals(criterioFecPago)
							|| ConstantsModulos.CTE_VAL_PLANC.equals(criterioFecDev))
					|| (ConstantsModulos.CTE_VAL_MIJUB.equals(criterioFecPago)
							|| ConstantsModulos.CTE_VAL_FIJUB.equals(criterioFecDev))) {
				ValidacionesFlujoPeriodos.validacionTiposPrestacion(umic.getRentas().getTempVit());

				/**
				 * Se definira la variable varfecini para establecer el primer elemento de la estructura de salida planPagosUmic de la siguiente forma:
				 *	- Si criterioFechas.FecPago y/o criterioFechas.FecDevengo es igual a 'EFTEC'
				 *			Se establecera varfecini como la fecha de Efecto Tecnico, es decir, como el fin de mes del mes de efecto de la UMIC (fechasUmic.fecinisus)
				 *	- Si criterioFechas.FecPago y/o criterioFechas.FecDevengo es igual a 'PLANI'
				 *			Se establecera varfecini como la fecha de inicio de la renta (rentasUmic.fecini)
				 */
				if (ConstantsModulos.CTE_VAL_EFTEC.equals(criterioFecPago)
						|| ConstantsModulos.CTE_VAL_EFTEC.equals(criterioFecDev)
						|| ConstantsModulos.CTE_VAL_EFTIN.equals(criterioFecPago)
						|| ConstantsModulos.CTE_VAL_EFTIN.equals(criterioFecDev)) {
					varFecIni = UtilFechas.getUltimoDiaDelMes(umic.getFechas().getFecinisus());
				} else if (ConstantsModulos.CTE_VAL_PLANI.equals(criterioFecPago)
						|| ConstantsModulos.CTE_VAL_PLANI.equals(criterioFecDev)
						|| ConstantsModulos.CTE_VAL_PVIDA.equals(criterioFecPago)
						|| ConstantsModulos.CTE_VAL_PVIDA.equals(criterioFecDev)
						|| ConstantsModulos.CTE_VAL_PLANB.equals(criterioFecPago)
						|| ConstantsModulos.CTE_VAL_PLANB.equals(criterioFecDev)
						|| (ConstantsModulos.CTE_VAL_PLAIN.equals(criterioFecPago)
								|| ConstantsModulos.CTE_VAL_PLAIN.equals(criterioFecDev))
						|| ConstantsModulos.CTE_VAL_PLNTP.equals(criterioFecPago)
						|| ConstantsModulos.CTE_VAL_PLNTP.equals(criterioFecDev)
						|| ConstantsModulos.CTE_VAL_PLANC.equals(criterioFecPago)
						|| ConstantsModulos.CTE_VAL_PLANC.equals(criterioFecDev)
						|| ConstantsModulos.CTE_VAL_MIJUB.equals(criterioFecPago)
						|| ConstantsModulos.CTE_VAL_FIJUB.equals(criterioFecDev)) {
					if (ConstantesSolvencia.NEGOCIO_INDIVIDUAL.equals(umic.getDatosGenerales().getCnegocio())) {
						varFecIni = umic.getFechas().getFecefecini();
					} else {
						varFecIni = umic.getRentas().getFecIni();
					}
				}

				if (ConstantsModulos.CTE_TIPO_PRES_TEM.equals(umic.getRentas().getTempVit())
						&& (ConstantsModulos.CTE_VAL_PLANI.equals(criterioFecPago)
								|| ConstantsModulos.CTE_VAL_PLAIN.equals(criterioFecPago)
								|| ConstantsModulos.CTE_VAL_PVIDA.equals(criterioFecPago)
								|| ConstantsModulos.CTE_VAL_PLANB.equals(criterioFecPago)
								|| ConstantsModulos.CTE_VAL_PLNTP.equals(criterioFecPago)
								|| ConstantsModulos.CTE_VAL_PLANC.equals(criterioFecPago)
								|| ConstantsModulos.CTE_VAL_MIJUB.equals(criterioFecPago))) {
					lstPagosPlani = obtenerDatos.recuperarPagosPlanificados(umic.getDatosGenerales().getKpoliza(),
							umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(),
							umic.getDatosGenerales().getNsuscri(), umic.getDatosGenerales().getKgarantia(),
							umic.getDatosGenerales().getKprestacion().substring(0, 1),
							umic.getDatosGenerales().getKprestacion().substring(1),
							umic.getDatosGenerales().getKajuste(), fichaProceso.getFcalc());

					if (lstPagosPlani == null || lstPagosPlani.isEmpty()) {
						if (ConstantsModulos.CTE_VAL_PLAIN.equals(criterioFecPago)
								|| ConstantsModulos.CTE_VAL_PLAIN.equals(criterioFecDev)
								|| ConstantsModulos.CTE_VAL_EFTIN.equals(criterioFecPago)
								|| ConstantsModulos.CTE_VAL_EFTIN.equals(criterioFecDev)) {
							lstPlanPagos = genPagosVitaliciosIN(varfecfinproy, umic.getRentas(), varFecIni,
									umic.getDatosGenerales().getCnegocio(), criterioFecPago, criterioFecDev, mapVariables, umic);
						} else {
							lstPlanPagos = genPagosVitalicios(varfecfinproy, umic.getRentas(), varFecIni, criterioFecPago, criterioFecDev, mapVariables, umic);
						}
						
						almacenarDatos.almacenarPlanPagos(umic.getKey(), lstPlanPagos);
					}
				} else if (ConstantsModulos.CTE_TIPO_PRES_VIT.equals(umic.getRentas().getTempVit())
						|| ConstantsModulos.CTE_VAL_EFTEC.equals(criterioFecPago)
						|| ConstantsModulos.CTE_VAL_EFTIN.equals(criterioFecPago)) {
					if (ConstantsModulos.CTE_VAL_PLAIN.equals(criterioFecPago)
							|| ConstantsModulos.CTE_VAL_PLAIN.equals(criterioFecDev)
							|| ConstantsModulos.CTE_VAL_EFTIN.equals(criterioFecPago)
							|| ConstantsModulos.CTE_VAL_EFTIN.equals(criterioFecDev)) {
						lstPlanPagos = genPagosVitaliciosIN(varfecfinproy, umic.getRentas(), varFecIni,
								umic.getDatosGenerales().getCnegocio(), criterioFecPago, criterioFecDev, mapVariables, umic);
					} else {
						lstPlanPagos = genPagosVitalicios(varfecfinproy, umic.getRentas(), varFecIni,criterioFecPago, criterioFecDev, mapVariables, umic);
					}

					almacenarDatos.almacenarPlanPagos(umic.getKey(), lstPlanPagos);
				} else if (ConstantsModulos.CTE_TIPO_PRES_LOCA.equals(umic.getRentas().getTempVit())) {
					lstPagosPlani = obtenerDatos.recuperarPagosPlanificadosLocas(umic.getDatosGenerales().getKpoliza(),
							umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(),
							umic.getDatosGenerales().getNsuscri(), umic.getDatosGenerales().getKgarantia(),
							umic.getDatosGenerales().getKprestacion().substring(0, 1),
							umic.getDatosGenerales().getKprestacion().substring(1,3),
							umic.getDatosGenerales().getKprestacion().substring(3),
							umic.getDatosGenerales().getKajuste(), fichaProceso.getFcalc(),umic.getDatosGenerales().getNorden());

					if (lstPagosPlani == null || lstPagosPlani.isEmpty()) {
						
						throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_GR,
								new String[] {});
						
					}
				}
			}

			final int longDetalCor = detallesCorriente.size();

			for (int i = 0; i < longDetalCor; i++) {

				UtilModulos.calcularFechasPagoDevengo(detallesCorriente.get(i), criterioFecPago, criterioFecDev,
						subProcesoActual, umic, lstPlanPagos, lstPagosPlani, numPeriodos, iteracion, mapVariables,
						fichaProceso.getFcalc());

				iteracion++;
			}

			// almacenamos la proyeccion
			almacenarDatos.almacenarProyeccion(detallesCorriente);
		}

		if (ModuloCalculoFechasPagoYDevengo.LOG.isTraceEnabled()) {
			ModuloCalculoFechasPagoYDevengo.LOG
					.trace("Fin funcion << calcularFechasPagYDev >> de la clase ModuloCalculoFechasPagoYDevengo");
		}
	}

	/** 
	 * Se encargara de generar el plan de pagos de una renta vitalicia en base a la forma de pago de la renta.
	 *
	 * @param fecFinProy Identifica la fecha de fin  de los pagos a generar.
	 * @param umicRenta Identifica la configuracion de
	 * @param varFecIni Indica la fecha que se establecera para el primer pago
	 * la renta para una umic dada
	 * @return Array de los pagos generados para la renta. Atributos:
	 *			-	FechaPago
	 *			-	ImportePago
	 */
	private List<PlanPagos> genPagosVitalicios(final Timestamp fecFinProy, final Rentas umicRenta,
			final Timestamp varFecIni, final String criterioFecPago, final String criterioFecDev, final Map<String, Object> mapVariables, final Umic umic) {
		// Variables locales
		List<PlanPagos> lstUmicPlanPagos = null;
		PlanPagos planPagoActual = null;
		int elemePlanPago = 0;
		Timestamp fechaRentaInicio = null;
		BigDecimal rentaIni = null;
		// Fin variables locales

		if (ModuloCalculoFechasPagoYDevengo.LOG.isTraceEnabled()) {
			ModuloCalculoFechasPagoYDevengo.LOG.trace(
					"Inicio funcion << genPagosVitalicios >> de la clase ModuloCalculoFechasPagoYDevengo, para la entrada fecFinProy = {}",
					fecFinProy);
		}

		// Inicializamos lista a devolver
		lstUmicPlanPagos = new ArrayList<PlanPagos>();

		// Vemos si los campos de entrada estan informados
		ValidacionesFlujoPeriodos.validacionDatosObligatoriosGenPagosVitalicios(fecFinProy, umicRenta);

		// Vemos si el atributo cforma-revrenta, esta bien informado
		// ValidacionesFlujoPeriodos.validacionAtributoCformaRevRenta(umicRenta.getCformaRevrenta());

		// Vemos si el atributo cpagrenta esta bien informado
		ValidacionesFlujoPeriodos.validacionAtributoCPagRenta(umicRenta.getCpagrenta());

		/*
		 * Vemos si el atributo ctipo_revrenta esta bien informado si la forma de la
		 * renta esta definida y no es constante
		 */
		if (umicRenta.getCformaRevrenta() != null
				&& !ConstantsModulos.CTE_REV_RENTA_CON.equals(umicRenta.getCformaRevrenta())) {
			ValidacionesFlujoPeriodos.validacionAtributoCTipoRevRenta(umicRenta.getCtipoRevrenta());
		}

		Timestamp fecIter = varFecIni;

		if (!ConstantsModulos.CTE_CFORMPAG_UNICA.equals(umicRenta.getCpagrenta())) {
			Fecha varFecIniu = UtilFechas.getFecha(varFecIni);
			// Si es el dia 28, se crea una fecha modelo con el dia 30
			LocalDate fecModelo = null;
			if (varFecIniu.getDia() == 28 || varFecIniu.getDia() == 30) {
				fecModelo = new LocalDate(2011, 1, 30);
			}
			
			// Se deberan generar pagos hasta que Umic.PlanPagos(n).FechaPago <= fecfinproy.
			while (!fecFinProy.before(fecIter)) {
				elemePlanPago = lstUmicPlanPagos.size();
				planPagoActual = new PlanPagos();
				// ALMACENAMOS LA FECHA DE PAGO
				// n = 1
				if (elemePlanPago == 0) {
					// Umic.PlanPagos(1).FechaPago = varfecini;
					planPagoActual.setFecPago(varFecIni);
				} else {
					// n > 1
					// Umic.PlanPagos(n).FechaPago = add(Umic.PlanPagos(n-1).FechaPago.MONTH, 12/forpagren)
					// Se establece como fecha modelo el dia 30 (en MAPFRE por politica se paga el
					// 30, salvo en Febrero, que se para el 28)
					if (fecModelo != null) {
						// Si el dia de la fecha de partida es 28 o 30, paso la nueva fecha calculada a
						// 28 o 30 en funcion del mes en el que caiga.
						planPagoActual.setFecPago(UtilFechas.incrMesesPLANI(lstUmicPlanPagos.get(elemePlanPago - 1).getFecPago(), varFecIni, ConstantsFunciones.CTE_12 / ConstantsFunciones.FORPAGRENT.get(Integer.valueOf(umicRenta.getCpagrenta())), false));						
					} else {
						// En cualquier otro caso paso la fecha al mismo dia que el de la fecha de
						// partida y de no existir, al ultimo dia del mes calculado (teniendo en cuenta
						// que si se trata de Febrero siempre sera 28).
						planPagoActual.setFecPago(UtilFechas.incrMeses(
								lstUmicPlanPagos.get(elemePlanPago - 1).getFecPago(), null,
								ConstantsFunciones.CTE_12
										/ ConstantsFunciones.FORPAGRENT.get(Integer.valueOf(umicRenta.getCpagrenta())),
								false));

						if (UtilFechas.getDia(varFecIni) == ConstantsFunciones.CTE_31) {
							planPagoActual.setFecPago(UtilFechas.getUltimoDiaDelMes(planPagoActual.getFecPago()));
						}
//javivil Llevamos al dia de efecto la fecha de pago si es el dis 29
						if (UtilFechas.getDia(varFecIni) == ConstantsFunciones.CTE_29) {
							planPagoActual
									.setFecPago(
											UtilFechas
													.incrMeses(lstUmicPlanPagos.get(elemePlanPago - 1).getFecPago(),
															varFecIniu.toTimestamp(),
															ConstantsFunciones.CTE_12 / ConstantsFunciones.FORPAGRENT
																	.get(Integer.valueOf(umicRenta.getCpagrenta())),
															false));
						}
//javivil
					}
				}

				// ALMACENAMOS IMPORTE DE PAGO
				// Si cforma-revrenta = C (Constante) o no se ha especificado:
				if (umicRenta.getCformaRevrenta() == null || umicRenta.getCformaRevrenta().isEmpty()
						|| ConstantsModulos.CTE_REV_RENTA_CON.equals(umicRenta.getCformaRevrenta())) {
					if(criterioFecPago.equals(ConstantsModulos.CTE_VAL_PVIDA)) {
						planPagoActual.setImpPago(
								importePagoRevalRentaGeoaArit(umicRenta, lstUmicPlanPagos, planPagoActual.getFecPago(),criterioFecPago, criterioFecDev, mapVariables, umic));
					}else {
						planPagoActual.setImpPago(umicRenta.getRentini());
					}
				} else if (ConstantsModulos.CTE_REV_RENTA_ARI.equals(umicRenta.getCformaRevrenta())
						|| ConstantsModulos.CTE_REV_RENTA_GEO.equals(umicRenta.getCformaRevrenta())
						// INI-100405260 
						|| ConstantsModulos.CTE_REV_RENTA_NEG.equals(umicRenta.getCformaRevrenta())) {
					// FIN-100405260 
					// Si cforma-revrenta = A (Aritmetica) || Si cforma-revrenta = G (Geometrica) ||
					// Si cforma-revrenta = N (Negativa):
					planPagoActual.setImpPago(
							importePagoRevalRentaGeoaArit(umicRenta, lstUmicPlanPagos, planPagoActual.getFecPago(),criterioFecPago, criterioFecDev, mapVariables, umic));
				}

				lstUmicPlanPagos.add(planPagoActual);

				fecIter = lstUmicPlanPagos.get(elemePlanPago).getFecPago();

				// LOG.warn(planPagoActual.getFecPago().toString());
			}
		} else {
			/**
			 * Si cpagrenta = 9 se evaluaran los datos de la umic:
			 *	Umic.rentas.fecini
			 *	Umic.rentas.rentini	
			 *	- Si Umic.rentas.fecini y Umic.rentas.rentini son no nulos: 
			 * 		Se generara un unico pago en  la fecha de inicio de la renta de forma que: 
			 *		planPagosUmic(1).fechaPago = Umic.rentas.fecini;
			 *		planPagosUmic(1).importePago = Umic.rentas.rentini;
			 *	- En caso contrario:
			 *	- No se generaran pagos correspondientes a la umic. 
			 */
			fechaRentaInicio = umicRenta.getFecIni();
			rentaIni = umicRenta.getRentini();
			if (null != fechaRentaInicio && null != rentaIni) {
				planPagoActual = new PlanPagos();
				planPagoActual.setFecPago(fechaRentaInicio);
				planPagoActual.setImpPago(rentaIni);
				lstUmicPlanPagos.add(planPagoActual);
			}
		}

		if (ModuloCalculoFechasPagoYDevengo.LOG.isTraceEnabled()) {
			ModuloCalculoFechasPagoYDevengo.LOG
					.trace("Fin funcion << genPagosVitalicios >> de la clase ModuloCalculoFechasPagoYDevengo");
		}

		return lstUmicPlanPagos;
	}

	/**
	 * Se encargara de generar el plan de pagos de una renta vitalicia para negocio
	 * Individuales en base a la forma de pago de la renta.
	 *
	 * @param fecFinProy Identifica la fecha de fin de los pagos a generar.
	 * @param umicRenta  Identifica la configuracion de
	 * @param varFecIni  Indica la fecha que se establecera para el primer pago la
	 *                   renta para una umic dada
	 * @return Array de los pagos generados para la renta. Atributos: 
	 * 					- FechaPago 
	 * 					- ImportePago
	 */
	private List<PlanPagos> genPagosVitaliciosIN(final Timestamp fecFinProy, final Rentas umicRenta,
			final Timestamp varFecIni, final String cNegocio, final String criterioFecPago, final String criterioFecDev, final Map<String, Object> mapVariables, final Umic umic ) {
		// Variables locales
		List<PlanPagos> lstUmicPlanPagos = null;
		PlanPagos planPagoActual = null;
		int elemePlanPago = 0;
		Timestamp fechaRentaInicio = null;
		BigDecimal rentaIni = null;
		Timestamp fecAnt = null;
		// Fin variables locales

		if (ModuloCalculoFechasPagoYDevengo.LOG.isTraceEnabled()) {
			ModuloCalculoFechasPagoYDevengo.LOG.trace(
					"Inicio funcion << genPagosVitalicios >> de la clase ModuloCalculoFechasPagoYDevengo, para la entrada fecFinProy = {}",
					fecFinProy);
		}

		// Inicializamos lista a devolver
		lstUmicPlanPagos = new ArrayList<PlanPagos>();

		// Vemos si los campos de entrada estan informados
		ValidacionesFlujoPeriodos.validacionDatosObligatoriosGenPagosVitalicios(fecFinProy, umicRenta);

		// Vemos si el atributo cforma-revrenta, esta bien informado
		// ValidacionesFlujoPeriodos.validacionAtributoCformaRevRenta(umicRenta.getCformaRevrenta());

		// Vemos si el atributo cpagrenta esta bien informado
		ValidacionesFlujoPeriodos.validacionAtributoCPagRenta(umicRenta.getCpagrenta());

		/*
		 * Vemos si el atributo ctipo_revrenta esta bien informado si la forma de la
		 * renta esta definida y no es constante
		 */
		if (umicRenta.getCformaRevrenta() != null
				&& ConstantsModulos.CTE_REV_RENTA_CON.equals(umicRenta.getCformaRevrenta())) {
			ValidacionesFlujoPeriodos.validacionAtributoCTipoRevRenta(umicRenta.getCtipoRevrenta());
		}

		Timestamp fecIter = varFecIni;

		if (!ConstantsModulos.CTE_CFORMPAG_UNICA.equals(umicRenta.getCpagrenta())) {
			Fecha varFecIniu = UtilFechas.getFecha(varFecIni);
			// Si es el dia 28, se crea una fecha modelo con el dia 30
			LocalDate fecModelo = null;
			if (varFecIniu.getDia() == 28 || varFecIniu.getDia() == 30) {
				fecModelo = new LocalDate(2011, 1, 30);
			}
			// Se deberan generar pagos hasta que Umic.PlanPagos(n).FechaPago <= fecfinproy.
			while (!fecFinProy.before(fecIter)) {
				elemePlanPago = lstUmicPlanPagos.size();
				planPagoActual = new PlanPagos();
				// ALMACENAMOS LA FECHA DE PAGO
				// n = 1
				if (elemePlanPago == 0 && cNegocio.equals(ConstantesSolvencia.NEGOCIO_COLECTIVO)) {
					// Umic.PlanPagos(1).FechaPago = varfecini;
					planPagoActual.setFecPago(varFecIni);
				} else {
					if (elemePlanPago == 0) {
						fecAnt = varFecIni;
					} else {
						fecAnt = lstUmicPlanPagos.get(elemePlanPago - 1).getFecPago();
					}
					// n > 1
					// Umic.PlanPagos(n).FechaPago = add(Umic.PlanPagos(n-1).FechaPago.MONTH, 12/forpagren)
					// Se establece como fecha modelo el dia 30 (en MAPFRE por politica se paga el
					// 30, salvo en Febrero, que se para el 28)
					if (fecModelo != null) {
						// Si el dia de la fecha de partida es 28 o 30, paso la nueva fecha calculada a
						// 28 o 30 en funci?n del mes en el que caiga.
						planPagoActual.setFecPago(UtilFechas.incrMeses(fecAnt,
								new Timestamp(fecModelo.toDate().getTime()),
								ConstantsFunciones.CTE_12
										/ ConstantsFunciones.FORPAGRENT.get(Integer.valueOf(umicRenta.getCpagrenta())),
								false));
					} else {
						// En cualquier otro caso paso la fecha al mismo dia que el de la fecha de
						// partida y de no existir, al ?ltimo d?a del mes calculado (teniendo en cuenta
						// que si se trata de Febrero siempre ser? 28).
						planPagoActual.setFecPago(UtilFechas.incrMeses(fecAnt, null,
								ConstantsFunciones.CTE_12
										/ ConstantsFunciones.FORPAGRENT.get(Integer.valueOf(umicRenta.getCpagrenta())),
								false));

						if (UtilFechas.getDia(varFecIni) == ConstantsFunciones.CTE_31) {
							planPagoActual.setFecPago(UtilFechas.getUltimoDiaDelMes(planPagoActual.getFecPago()));
						}
// javivil Llevamos al dia de efecto la fecha de pago si es el dis 29
						if (UtilFechas.getDia(varFecIni) == ConstantsFunciones.CTE_29) {
							planPagoActual
									.setFecPago(
											UtilFechas
													.incrMeses(fecAnt, varFecIniu.toTimestamp(),
															ConstantsFunciones.CTE_12 / ConstantsFunciones.FORPAGRENT
																	.get(Integer.valueOf(umicRenta.getCpagrenta())),
															false));
						}
// javivil
					}
				}

				// ALMACENAMOS IMPORTE DE PAGO
				// Si cforma-revrenta = C (Constante) o no se ha especificado:
				if (umicRenta.getCformaRevrenta() == null || umicRenta.getCformaRevrenta().isEmpty()
						|| ConstantsModulos.CTE_REV_RENTA_CON.equals(umicRenta.getCformaRevrenta())) {
					planPagoActual.setImpPago(umicRenta.getRentini());
				} else if (ConstantsModulos.CTE_REV_RENTA_ARI.equals(umicRenta.getCformaRevrenta())
						|| ConstantsModulos.CTE_REV_RENTA_GEO.equals(umicRenta.getCformaRevrenta())
						/* INI-100405260 */
						|| ConstantsModulos.CTE_REV_RENTA_NEG.equals(umicRenta.getCformaRevrenta())) {
					/* FIN-100405260 */
					/*
					 * Si cforma-revrenta = A (Aritmetica) || Si cforma-revrenta = G (Geometrica) ||
					 * Si cforma-revrenta = N (Negativa):
					 */
					planPagoActual.setImpPago(
							importePagoRevalRentaGeoaArit(umicRenta, lstUmicPlanPagos, planPagoActual.getFecPago(),criterioFecPago, criterioFecDev, mapVariables, umic));
				}

				lstUmicPlanPagos.add(planPagoActual);

				fecIter = lstUmicPlanPagos.get(elemePlanPago).getFecPago();

				// LOG.warn(planPagoActual.getFecPago().toString());
			}
		} else {
			/**
			 * Si cpagrenta = 9 se evaluaran los datos de la umic:
			 * Umic.rentas.fecini
			 * Umic.rentas.rentini
			 * - Si Umic.rentas.fecini y Umic.rentas.rentini son no nulos: 	
			 *		Se generara un unico pago en  la fecha de inicio de la renta de forma que: 
			 *		planPagosUmic(1).fechaPago = Umic.rentas.fecini;
			 *		planPagosUmic(1).importePago = Umic.rentas.rentini;
			 * - En caso contrario:
			 * - No se generaran pagos correspondientes a la umic. 
			 */
			fechaRentaInicio = umicRenta.getFecIni();
			rentaIni = umicRenta.getRentini();
			if (null != fechaRentaInicio && null != rentaIni) {
				planPagoActual = new PlanPagos();
				planPagoActual.setFecPago(fechaRentaInicio);
				planPagoActual.setImpPago(rentaIni);
				lstUmicPlanPagos.add(planPagoActual);
			}
		}

		if (ModuloCalculoFechasPagoYDevengo.LOG.isTraceEnabled()) {
			ModuloCalculoFechasPagoYDevengo.LOG
					.trace("Fin funcion << genPagosVitalicios >> de la clase ModuloCalculoFechasPagoYDevengo");
		}

		return lstUmicPlanPagos;
	}

	/**
	 * Funcion encargada de calcular el importe a actual en funcion del tipo de
	 * revalorizacion de la renta y la forma de reavalorizacion de la renta.
	 *
	 * @param cFormaRevRenta Forma de revalorizacion de la renta
	 * @param rentini        renta inicial
	 * @param prevrenta      prevrenta
	 * @return importePagoSegunRevRenta
	 */
	private BigDecimal calculoImportePlanPagoSegunTipoRevRenta(final String cFormaRevRenta, final BigDecimal rentini,
			final BigDecimal prevrenta) {
		// Variables locales
		BigDecimal importe = BigDecimal.ZERO;
		// Fin variables locales

		if (ModuloCalculoFechasPagoYDevengo.LOG.isTraceEnabled()) {
			ModuloCalculoFechasPagoYDevengo.LOG.trace(
					"Inicio funcion << calculoImportePlanPagoSegunTipoRevRenta >> de la clase ModuloCalculoFechasPagoYDevengo, para la entrada cFormaRevRenta = {}, rentini = {} y prevrenta = {}",
					cFormaRevRenta, rentini, prevrenta);
		}

		// INI-100405260 Se incluye el tratamiento para REV_RENTA_NEG

		// Si cforma-revrenta = A (Aritmetica):
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
		// FIN-100405260

		if (ModuloCalculoFechasPagoYDevengo.LOG.isTraceEnabled()) {
			ModuloCalculoFechasPagoYDevengo.LOG.trace(
					"Fin funcion << calculoImportePlanPagoSegunTipoRevRenta >> de la clase ModuloCalculoFechasPagoYDevengo, con resultado importePagoSegunRevRenta = {} ",
					importe);
		}

		return importe;
	}

	/**
	 * Funcion encargada de obtener el Pago en funcion si la revalorizacion es
	 * Aritmetica o Geometrica.
	 * 
	 * @param rentaUmic        Renta de la umic
	 * @param lstUmicPlanPagos lista de planes de pagos
	 * @param fechaPPActual    fecha de pago actual
	 * @return importe
	 */
	private BigDecimal importePagoRevalRentaGeoaArit(final Rentas rentaUmic, final List<PlanPagos> lstUmicPlanPagos,
			final Timestamp fechaPPActual, String criterioFecPago, String criterioFecDev, Map<String, Object> mapVariables, Umic umic) {
		// Variables locales
		BigDecimal importe = BigDecimal.ZERO;
		int elemPlanPagos = 0;
		// Fin variables locales

		if (ModuloCalculoFechasPagoYDevengo.LOG.isTraceEnabled()) {
			ModuloCalculoFechasPagoYDevengo.LOG.trace(
					"Inicio funcion << importePagoRevalRentaGeoaArit >> de la clase ModuloCalculoFechasPagoYDevengo, para la entrada fechaPPActual = {} ",
					fechaPPActual);
		}

		elemPlanPagos = lstUmicPlanPagos.size();

		// Para n = 1
		if (elemPlanPagos == 0) {
			// Umic.PlanPagos(1).ImportePago<-- Umic.Renta.Rentini
			importe = rentaUmic.getRentini();
		} else {
			// Para n > 1
			// Si el criterio de fechas es PLANIB se permitira la revalorizacion en cualquier mes
			// en base al campo ctipo-revrenta
			if(criterioFecPago.equals("PLANB") && criterioFecDev.equals("PLANB")){
				if((ConstantsModulos.CTE_FR_ANIO_NATU.equals(rentaUmic.getCtipoRevrenta()) ||
						ConstantsModulos.CTE_FR_ANIO_NATU_2.equals(rentaUmic.getCtipoRevrenta()) ||
						ConstantsModulos.CTE_FR_ANIO_NATU_3.equals(rentaUmic.getCtipoRevrenta()) ||
						ConstantsModulos.CTE_FR_ANIO_NATU_4.equals(rentaUmic.getCtipoRevrenta()) ||
						ConstantsModulos.CTE_FR_ANIO_NATU_5.equals(rentaUmic.getCtipoRevrenta()) ||
						ConstantsModulos.CTE_FR_ANIO_NATU_6.equals(rentaUmic.getCtipoRevrenta()) ||
						ConstantsModulos.CTE_FR_ANIO_NATU_7.equals(rentaUmic.getCtipoRevrenta()) ||
						ConstantsModulos.CTE_FR_ANIO_NATU_8.equals(rentaUmic.getCtipoRevrenta()) || 
						ConstantsModulos.CTE_FR_ANIO_NATU_9.equals(rentaUmic.getCtipoRevrenta()) ||
						ConstantsModulos.CTE_FR_ANIO_NATU_O.equals(rentaUmic.getCtipoRevrenta()) ||
						ConstantsModulos.CTE_FR_ANIO_NATU_N.equals(rentaUmic.getCtipoRevrenta()) ||
						ConstantsModulos.CTE_FR_ANIO_NATU_D.equals(rentaUmic.getCtipoRevrenta()))){	
					
					
					int mesPago;
					if(ConstantsModulos.CTE_FR_ANIO_NATU_O.equals(rentaUmic.getCtipoRevrenta())){
						mesPago = 10;
					}else if(ConstantsModulos.CTE_FR_ANIO_NATU_N.equals(rentaUmic.getCtipoRevrenta())){
						mesPago = 11;
					}else if(ConstantsModulos.CTE_FR_ANIO_NATU_D.equals(rentaUmic.getCtipoRevrenta())){
						mesPago = 12;
					}else{
						mesPago = Integer.parseInt(rentaUmic.getCtipoRevrenta());
					}
				
					importe = calculoImportePagoMesNatural(fechaPPActual, rentaUmic, lstUmicPlanPagos, elemPlanPagos, criterioFecPago, criterioFecDev, mapVariables, umic, mesPago);
					
				}else{
					importe = rentaUmic.getRentini();
				}
				
				
				
				
			}else{
				if (criterioFecPago.equals("PVIDA") && criterioFecDev.equals("PVIDA")){
					importe = calculoImportePagoAnioNatural(fechaPPActual, rentaUmic, lstUmicPlanPagos, elemPlanPagos, criterioFecPago, criterioFecDev, mapVariables, umic);
				}else {
					// Si Ctipo_revrenta = 1 (Anno Natural (Enero))
					if (ConstantsModulos.CTE_FR_ANIO_NATU.equals(rentaUmic.getCtipoRevrenta())) {
						importe = calculoImportePagoAnioNatural(fechaPPActual, rentaUmic, lstUmicPlanPagos, elemPlanPagos, criterioFecPago, criterioFecDev, mapVariables, umic);
					} else if (ConstantsModulos.CTE_FR_ANIVER.equals(rentaUmic.getCtipoRevrenta())) {
						// Si Ctipo_revrenta = 2 (Aniversario)
						importe = calculoImportePagoAniversario(fechaPPActual, rentaUmic, lstUmicPlanPagos, elemPlanPagos, criterioFecPago, criterioFecDev, mapVariables, umic);
					} else {
						importe = rentaUmic.getRentini();
					}
				}
			}	
		}

		if (ModuloCalculoFechasPagoYDevengo.LOG.isTraceEnabled()) {
			ModuloCalculoFechasPagoYDevengo.LOG.trace(
					"Fin funcion << importePagoRevalRentaGeoaArit >> de la clase ModuloCalculoFechasPagoYDevengo, con resultado importePagoRentaGeometrica = {}",
					importe);
		}

		return importe;
	}

	/**
	 * Funcion encargada de calcular el importe de pago cuando la forma de
	 * revalorizacion de la renta es aniversario (codigo 2)
	 * 
	 * @param fechaPPActual    fecha de pago actual
	 * @param rentaUmic        Renta de la umic
	 * @param lstUmicPlanPagos lista de planes de pagos
	 * @param elemPlanPagos    longitud actual de la lista de plan de pagos
	 * @return importe
	 * @return
	 */
	private BigDecimal calculoImportePagoAniversario(final Timestamp fechaPPActual, final Rentas rentaUmic,
			final List<PlanPagos> lstUmicPlanPagos, final int elemPlanPagos, final String criterioFecPago, final String criterioFecDev, final Map<String, Object> mapVariables, final Umic umic) {
		// Variables locales
		BigDecimal importe = BigDecimal.ZERO;
		BigDecimal pagoAnterior = BigDecimal.ZERO;
		BigDecimal pago = BigDecimal.ZERO;
		// Fin variables locales

		if (ModuloCalculoFechasPagoYDevengo.LOG.isTraceEnabled()) {
			ModuloCalculoFechasPagoYDevengo.LOG.trace(
					"Inicio de la funcion << calculoImportePagoAniversario >> de la clase ModuloCalculoFechasPagoYDevengo");
		}

		 // (Mes(Umic.PlanPagos(n).FechaPago)) == (MEs(Umic.PlanPagos(1).FechaPago)) = 1
		if ((UtilFechas.getMes(fechaPPActual) == UtilFechas.getMes(lstUmicPlanPagos.get(0).getFecPago()))) {
			pagoAnterior = lstUmicPlanPagos.get(elemPlanPagos - 1).getImpPago();
			
			if(criterioFecPago.equals("PVIDA") && criterioFecDev.equals("PVIDA")){
				BigDecimal pctRevRenta;
				String revRenta = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_PCTREVRENT, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_PCTREVRENT);
				if(null == revRenta){
					pctRevRenta = BigDecimal.ZERO;
					//throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_P1JUB});
//					final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();
//					Incidencia aviso1 = Solvencia2ExcepcionHelper.crearAviso(btcUmic.getBaseTec(), umic.getDatosGenerales().getCcanal(), umic.getDatosGenerales().getCcartera(), umic.getKey(), btcUmic.getFecCierre(), umic.getDatosGenerales().getCnegocio(), this.getNombreServicio(), ConstantsFunciones.CTE_COD_ERROR_DN, new Object[]{CLAVE_P1JUB});
//					servicio.almacenarIncidencias(aviso1);
					
				}else{
					pctRevRenta = new BigDecimal(revRenta).divide(new BigDecimal("100"));
				}
				
				pago = calculoImportePlanPagoSegunTipoRevRenta(rentaUmic.getCformaRevrenta(), rentaUmic.getRentini(),
						pctRevRenta);
			}else{
				pago = calculoImportePlanPagoSegunTipoRevRenta(rentaUmic.getCformaRevrenta(), rentaUmic.getRentini(),
						rentaUmic.getPrevrenta());
			}
			
		
			

			/*
			 * Vemos si estamos frente a una renta aritmetica o geometrica para determinar
			 * si el pagoAnterior es sumado o multiplicado al pago
			 * INI-100405260 se incluye el tratamiento para la revalorizacion negativa 
			 */

			if (ConstantsModulos.CTE_REV_RENTA_ARI.equals(rentaUmic.getCformaRevrenta())) {
				importe = pagoAnterior.add(pago);
			} else if ((ConstantsModulos.CTE_REV_RENTA_GEO.equals(rentaUmic.getCformaRevrenta()))
					|| (ConstantsModulos.CTE_REV_RENTA_NEG.equals(rentaUmic.getCformaRevrenta()))) {
				importe = pagoAnterior.multiply(pago);
			}

		} else {
			/*
			 * (Mes(Umic.PlanPagos(n).FechaPago)) != (MEs(Umic.PlanPagos(1).FechaPago)) <> 1
			 * Umic.PlanPagos(n).ImportePago = Umic.PlanPagos(n-1).ImportePago
			 */
			importe = lstUmicPlanPagos.get(elemPlanPagos - 1).getImpPago();
		}

		if (ModuloCalculoFechasPagoYDevengo.LOG.isTraceEnabled()) {
			ModuloCalculoFechasPagoYDevengo.LOG.trace(
					"Fin de la funcion << calculoImportePagoAniversario >> de la clase ModuloCalculoFechasPagoYDevengo, con resultado importe = {}",
					importe);
		}

		return importe;

	}

	/**
	 * Funcion encargada de calcular el importe de pago cuando la forma de
	 * revalorizacion de la renta es anio natural (codigo 1)
	 * 
	 * @param fechaPPActual    fecha de pago actual
	 * @param rentaUmic        Renta de la umic
	 * @param lstUmicPlanPagos lista de planes de pagos
	 * @param elemPlanPagos    longitud actual de la lista de plan de pagos
	 * @return importe
	 */
	private BigDecimal calculoImportePagoAnioNatural(final Timestamp fechaPPActual, final Rentas rentaUmic,
			final List<PlanPagos> lstUmicPlanPagos, final int elemPlanPagos, final String criterioFecPago, final String criterioFecDev, final Map<String, Object> mapVariables, final Umic umic) {
		// Variables locales
		BigDecimal importe = BigDecimal.ZERO;
		BigDecimal pagoAnterior = BigDecimal.ZERO;
		BigDecimal pago = BigDecimal.ZERO;
		// Fin variables locales

		if (ModuloCalculoFechasPagoYDevengo.LOG.isTraceEnabled()) {
			ModuloCalculoFechasPagoYDevengo.LOG.trace(
					"Inicio de la funcion << calculoImportePagoAnioNatural >> de la clase ModuloCalculoFechasPagoYDevengo");
		}

		// Si Anno(Umic.PlanPagos(n).FechaPago) = Anno(Umic.PlanPagos(n-1).FechaPago)
		if (UtilFechas.getAnio(fechaPPActual) == UtilFechas
				.getAnio(lstUmicPlanPagos.get(elemPlanPagos - 1).getFecPago())) {
			// Umic.PlanPagos(n).ImportePago = Umic.PlanPagos(n-1).ImportePago
			importe = lstUmicPlanPagos.get(elemPlanPagos - 1).getImpPago();
		} else if (UtilFechas.getAnio(fechaPPActual) > UtilFechas
				.getAnio(lstUmicPlanPagos.get(elemPlanPagos - 1).getFecPago())) {
			// Si Anno(Umic.PlanPagos(n).FechaPago) > Anno(Umic.PlanPagos(n-1).FechaPago) 
			pagoAnterior = lstUmicPlanPagos.get(elemPlanPagos - 1).getImpPago();
			
			if(criterioFecPago.equals("PVIDA") && criterioFecDev.equals("PVIDA")){
				BigDecimal pctRevRenta;
				String revRenta = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_PCTREVRENT, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_PCTREVRENT);
				if(null == revRenta){
					pctRevRenta = BigDecimal.ZERO;
					//throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_P1JUB});
//					final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();
//					Incidencia aviso1 = Solvencia2ExcepcionHelper.crearAviso(btcUmic.getBaseTec(), umic.getDatosGenerales().getCcanal(), umic.getDatosGenerales().getCcartera(), umic.getKey(), btcUmic.getFecCierre(), umic.getDatosGenerales().getCnegocio(), this.getNombreServicio(), ConstantsFunciones.CTE_COD_ERROR_DN, new Object[]{CLAVE_P1JUB});
//					servicio.almacenarIncidencias(aviso1);
					
				}else{
					pctRevRenta = new BigDecimal(revRenta).divide(new BigDecimal("100"));
				}
				pago = calculoImportePlanPagoSegunTipoRevRenta(ConstantsModulos.CTE_REV_RENTA_GEO, rentaUmic.getRentini(),pctRevRenta);
				importe = pagoAnterior.multiply(pago);
				return importe;
			}else{
				pago = calculoImportePlanPagoSegunTipoRevRenta(rentaUmic.getCformaRevrenta(), rentaUmic.getRentini(),rentaUmic.getPrevrenta());
			}
			/*
			 * Vemos si estamos frente a una renta aritmetica o geometrica para determinar
			 * si el pagoAnteriores sumado o multiplicado al pago.
			 * INI-100405260 se incluye el tratamiento para la revalorizacion negativa 
			 */
			if (ConstantsModulos.CTE_REV_RENTA_ARI.equals(rentaUmic.getCformaRevrenta())) {
				importe = pagoAnterior.add(pago);
			} else if ((ConstantsModulos.CTE_REV_RENTA_GEO.equals(rentaUmic.getCformaRevrenta()))
					|| (ConstantsModulos.CTE_REV_RENTA_NEG.equals(rentaUmic.getCformaRevrenta()))) {
				importe = pagoAnterior.multiply(pago);
			}

		}

		if (ModuloCalculoFechasPagoYDevengo.LOG.isTraceEnabled()) {
			ModuloCalculoFechasPagoYDevengo.LOG.trace(
					"Fin de la funcion << calculoImportePagoAnioNatural >> de la clase ModuloCalculoFechasPagoYDevengo, con resultado importe = {}",
					importe);
		}

		return importe;
	}
	/**
	 * Funcion encargada de calcular el importe de pago cuando la forma de
	 * revalorizacion de la renta es anio natural (codigo 1)
	 * 
	 * @param fechaPPActual    fecha de pago actual
	 * @param rentaUmic        Renta de la umic
	 * @param lstUmicPlanPagos lista de planes de pagos
	 * @param elemPlanPagos    longitud actual de la lista de plan de pagos
	 * @return importe
	 */
	private BigDecimal calculoImportePagoMesNatural(final Timestamp fechaPPActual, final Rentas rentaUmic,
			final List<PlanPagos> lstUmicPlanPagos, final int elemPlanPagos, final String criterioFecPago, final String criterioFecDev, final Map<String, Object> mapVariables, final Umic umic, int mesPago) {
		// Variables locales
		BigDecimal importe = BigDecimal.ZERO;
		BigDecimal pagoAnterior = BigDecimal.ZERO;
		BigDecimal pago = BigDecimal.ZERO;
		// Fin variables locales

		if (ModuloCalculoFechasPagoYDevengo.LOG.isTraceEnabled()) {
			ModuloCalculoFechasPagoYDevengo.LOG.trace(
					"Inicio de la funcion << calculoImportePagoAnioNatural >> de la clase ModuloCalculoFechasPagoYDevengo");
		}

		// Si Anno(Umic.PlanPagos(n).FechaPago) = Anno(Umic.PlanPagos(n-1).FechaPago)
		
		if(!rentaUmic.getCpagrenta().equals("4") && UtilFechas.getMes(fechaPPActual) != mesPago && UtilFechas.getAnio(fechaPPActual) > UtilFechas.getAnio(lstUmicPlanPagos.get(elemPlanPagos - 1).getFecPago())){
			pagoAnterior = lstUmicPlanPagos.get(elemPlanPagos - 1).getImpPago();
			pago = calculoImportePlanPagoSegunTipoRevRenta(rentaUmic.getCformaRevrenta(), rentaUmic.getRentini(),rentaUmic.getPrevrenta());
			if (ConstantsModulos.CTE_REV_RENTA_ARI.equals(rentaUmic.getCformaRevrenta())) {
				importe = pagoAnterior.add(pago);
			} else if ((ConstantsModulos.CTE_REV_RENTA_GEO.equals(rentaUmic.getCformaRevrenta()))
					|| (ConstantsModulos.CTE_REV_RENTA_NEG.equals(rentaUmic.getCformaRevrenta()))) {
				importe = pagoAnterior.multiply(pago);
			}
			
		}else 
			if (UtilFechas.getMes(fechaPPActual) != mesPago) {
			// Umic.PlanPagos(n).ImportePago = Umic.PlanPagos(n-1).ImportePago
			importe = lstUmicPlanPagos.get(elemPlanPagos - 1).getImpPago();
		}else{
//		
//		if (UtilFechas.getAnio(fechaPPActual) == UtilFechas
//				.getAnio(lstUmicPlanPagos.get(elemPlanPagos - 1).getFecPago())) {
//			// Umic.PlanPagos(n).ImportePago = Umic.PlanPagos(n-1).ImportePago
//			importe = lstUmicPlanPagos.get(elemPlanPagos - 1).getImpPago();
//		} else if ((UtilFechas.getAnio(fechaPPActual) > UtilFechas
//				.getAnio(lstUmicPlanPagos.get(elemPlanPagos - 1).getFecPago())) && UtilFechas.getMes(fechaPPActual) == mesPago) {
			// Si Anno(Umic.PlanPagos(n).FechaPago) > Anno(Umic.PlanPagos(n-1).FechaPago) 
			pagoAnterior = lstUmicPlanPagos.get(elemPlanPagos - 1).getImpPago();
			
//			if(criterioFecPago.equals("PVIDA") && criterioFecDev.equals("PVIDA")){
//				BigDecimal pctRevRenta;
//				String revRenta = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_PCTREVRENT, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_PCTREVRENT);
//				if(null == revRenta){
//					pctRevRenta = BigDecimal.ZERO;
//					//throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_P1JUB});
////					final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();
////					Incidencia aviso1 = Solvencia2ExcepcionHelper.crearAviso(btcUmic.getBaseTec(), umic.getDatosGenerales().getCcanal(), umic.getDatosGenerales().getCcartera(), umic.getKey(), btcUmic.getFecCierre(), umic.getDatosGenerales().getCnegocio(), this.getNombreServicio(), ConstantsFunciones.CTE_COD_ERROR_DN, new Object[]{CLAVE_P1JUB});
////					servicio.almacenarIncidencias(aviso1);
//					
//				}else{
//					pctRevRenta = new BigDecimal(revRenta).divide(new BigDecimal("100"));
//				}
//				pago = calculoImportePlanPagoSegunTipoRevRenta(rentaUmic.getCformaRevrenta(), rentaUmic.getRentini(),pctRevRenta);
//				
//			}else{
				pago = calculoImportePlanPagoSegunTipoRevRenta(rentaUmic.getCformaRevrenta(), rentaUmic.getRentini(),rentaUmic.getPrevrenta());
			//}
			/*
			 * Vemos si estamos frente a una renta aritmetica o geometrica para determinar
			 * si el pagoAnteriores sumado o multiplicado al pago.
			 * INI-100405260 se incluye el tratamiento para la revalorizacion negativa 
			 */
			if (ConstantsModulos.CTE_REV_RENTA_ARI.equals(rentaUmic.getCformaRevrenta())) {
				importe = pagoAnterior.add(pago);
			} else if ((ConstantsModulos.CTE_REV_RENTA_GEO.equals(rentaUmic.getCformaRevrenta()))
					|| (ConstantsModulos.CTE_REV_RENTA_NEG.equals(rentaUmic.getCformaRevrenta()))) {
				importe = pagoAnterior.multiply(pago);
			}

		}

		if (ModuloCalculoFechasPagoYDevengo.LOG.isTraceEnabled()) {
			ModuloCalculoFechasPagoYDevengo.LOG.trace(
					"Fin de la funcion << calculoImportePagoAnioNatural >> de la clase ModuloCalculoFechasPagoYDevengo, con resultado importe = {}",
					importe);
		}

		if (umic.getDatosGenerales().getKmodalidad() == 412) {
			return importe.setScale(ConstantsFunciones.CTE_2, RoundingMode.HALF_UP);
		} else {
			return importe;
		}
	}
}