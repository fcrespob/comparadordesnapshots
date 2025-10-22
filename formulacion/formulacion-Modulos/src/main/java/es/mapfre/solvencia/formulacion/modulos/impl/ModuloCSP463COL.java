package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.formulacion.PlanPagos;
import es.mapfre.solvencia.dominio.maestro.PagosPlanificados;
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
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo CSP463. Clase encargada del cálculo que
 * devuelve el importe nominal en cada momento según forma de pago de la Umic.
 * La expresión matemática para su determinación es la siguiente: CSP463 =
 * Máximo (varCsp463, 0)
 * 
 * @author ogperez
 *
 */
public class ModuloCSP463COL implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP463.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP463COL;
	// private static final String CLAVE_CALCULO_CSP463 = CLAVE_MODULO;

	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);

	private static final String CLAVE_VAR_TCM = ConstantsModulos.CTE_VAR_TCM.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TCMINI = ConstantsModulos.CTE_VAR_TCMINI.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TCMINICRECIENTE = ConstantsModulos.CTE_VAR_TCMINI.concat(CLAVE_MODULO).concat("_");

	private static final String CLAVE_CSP238N1 = ConstantsModulos.CTE_VAR_CSP238N1;
	private static final String CLAVE_VAR_CSP238N1 = CLAVE_CSP238N1.concat(CLAVE_MODULO);

	// Fin de las variables estáticas usadas para agilizar operaciones.

	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}

	/**
	 * Función encargada de obtener los parámetros necesarios y de realizar la
	 * llamada a la función que realiza los calculos del modulo.
	 */
	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {
		// Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		// Fin variables locales

		try {

			if (ModuloCSP463COL.LOG.isTraceEnabled()) {
				ModuloCSP463COL.LOG.trace("Inicio de execute en clase ModuloCSP463COL");
			}

			// Recuperamos los datos que le pasaremos a la función moduloCSP463
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de los datos.

			// Invocamos a la función de calculo ModuloCSP463
			resultado = moduloCSP463COL(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables,
					codSubproceso);

		} catch (Solvencia2Excepcion e) {
			ModuloCSP463COL.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP463COL.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloCSP463COL.LOG.isTraceEnabled()) {
			ModuloCSP463COL.LOG.trace("Fin de execute en clase ModuloCSP463COL");
		}

		return resultado;
	}

	private BigDecimal moduloCSP463COL(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente,
			Integer iteracion, final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic,
			final Map<String, Object> mapVariables, final String codSubproceso) {
		// Variables locales
		BigDecimal csp463COL = BigDecimal.ZERO;
		String varCriterioFecha;
		Timestamp varFechaEfecto;
		Integer varTCm;
		Integer varTCmini, varTCminiCreciente,varTCminiCte;
		BigDecimal varPU;
		BigDecimal varCsp238N1;
		BigDecimal varCsp238N1Temp;
		BigDecimal varCsp463COL;
		BigDecimal varCsp238N1G = BigDecimal.ZERO;
		Umic umicTitular;
		List<DetalleCorriente> varProyC2S = proyUmic;
		final IObtenerDatos servicioDatos = FachadaServicios.getObtenerDatos();
		final IObtenerDatos obtenerDatos = FachadaServicios.getObtenerDatos();
		Integer cPagRentN1 = 0;
		Integer cPagRentN2 = 0;
		List<PlanPagos> lstPagos = null;
		List<PagosPlanificados> lstPagosPlanificados = null;
		Boolean varAux = false;
		// Fin variables locales

		if (ModuloCSP463COL.LOG.isTraceEnabled()) {
			ModuloCSP463COL.LOG.trace(
					"Inicio función << ModuloCSP463COL >> de la clase ModuloCSP463COL, para la iteracion = {}",
					iteracion);
		}

		// Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if (umic.getDatosGenerales().getNorden().equals(211)) {
			BigDecimal a = BigDecimal.ZERO;
			
		}
		
		Timestamp auxFecha = (Timestamp) mapVariables.get("FEC_AUX_CSP463COL");
		if (auxFecha == null ) {
			if (bloqueCorriente.getFechaDevengo() != null) {
				auxFecha = bloqueCorriente.getFechaDevengo();
			} else {
				auxFecha = UtilFechas.getUltimoDiaDelMes(fcalc);
			}
			mapVariables.put("FEC_AUX_CSP463COL", auxFecha);
		}

		if (bloqueCorriente.getFechaDevengo() == null) {
			//return BigDecimal.ZERO;
			Timestamp auxFecDev = (Timestamp) mapVariables.get("FEC_DEVENGO_CSP463COL");
			if (auxFecDev == null ) {
				auxFecDev = UtilFechas.getUltimoDiaDelMes(fcalc);
			} else {
				auxFecDev = UtilFechas.plusMeses(auxFecDev, 1);
				if (UtilFechas.getDia(auxFecDev) > UtilFechas.getDia(UtilFechas.getUltimoDiaDelMes(auxFecDev))) {
					auxFecDev = UtilFechas.getUltimoDiaDelMes(auxFecDev);
				}
				
				if (UtilFechas.getMes(auxFecDev) == 2) {
					auxFecDev = UtilFechas.getUltimoDiaDelMes(auxFecDev);
				}	
			}
			mapVariables.put("FEC_DEVENGO_CSP463COL", auxFecDev);
			bloqueCorriente.setFechaDevengo(auxFecDev);
			bloqueCorriente.setFechaPago(auxFecDev);
		} else {
			Timestamp auxFecDev = (Timestamp) mapVariables.get("FEC_DEVENGO_CSP463COL");
			mapVariables.put("FEC_DEVENGO_CSP463COL", bloqueCorriente.getFechaDevengo());
			if (!varAux && auxFecDev != null ) {
				varAux = true;
				for (int i = 0; i < iteracion - 1; i++) {
					Timestamp fecDev = proyUmic.get(i).getBloqueFall().getFechaDevengo();
					fecDev = UtilFechas.incrDias(fecDev, UtilFechas.getDia(bloqueCorriente.getFechaDevengo()) - UtilFechas.getDia(fecDev));
					proyUmic.get(i).getBloqueFall().setFechaDevengo(fecDev);
					proyUmic.get(i).getBloqueFall().setFechaPago(fecDev);
				}
			}
		}

		// Obtención y validación del criterio de fecha.
		varCriterioFecha = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC,
				umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(),
				umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);

		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la
			// forma en que se gestiona el paso de umic).
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterioFecha);
			// Fin de la obtención y validación del criterio de fecha.
		}

		if (umic.getDatosGenerales().getCnegocio().equals(ConstantsModulos.CTE_NEGOCIO_INDIVIDUAL)) {
			varFechaEfecto = umic.getFechas().getFecefecini();
		} else {
			varFechaEfecto = umic.getFechas().getFecinisus();
		}

		varTCm = UtilModulos.getVarK(mapVariables, CLAVE_VAR_TCM, varFechaEfecto, fcalc);
		varTCmini = UtilModulos.getVarKini(mapVariables, CLAVE_VAR_TCMINI, varFechaEfecto, fcalc);
	
		varPU = umic.getPrimas().getIprimanetaini();
		umicTitular = obtenerDatos.recuperarUmicTitular(umic.getDatosGenerales().getKmodalidad(),
				umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(),
				umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(),
				ConstantsModulos.CTE_KBENCON_UMIC_PPAL, umic.getRentas().getFecIni(), umic.getRentas().getForpagrent(),
				umic.getRentas().getCpagrenta());
		
		if (umicTitular == null) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DL,
					new String[] { umic.getDatosGenerales().getKmodalidad().toString(),
							umic.getDatosGenerales().getKpoliza().toString(),
							umic.getDatosGenerales().getKsubpoliza().toString(),
							umic.getDatosGenerales().getKcertificado().toString(),
							umic.getDatosGenerales().getNsuscri().toString() });
		}
		
		varTCminiCreciente = UtilModulos.getVarKini(mapVariables, CLAVE_VAR_TCMINICRECIENTE, umicTitular.getRentas().getFecIni(),
				fcalc);
		//Validación de la variable varTCminiCreciente para aquellos casos en los que la fecha de ini sea superior a la de cáclulo.
		if(varTCminiCreciente<0) {
			if (UtilFechas.getAnio(umic.getFechas().getFecinisus()) == UtilFechas.getAnio(umic.getDatosGenerales().getFecCierre())
					&& UtilFechas.getMes(umic.getFechas().getFecinisus()) == UtilFechas.getMes(umic.getDatosGenerales().getFecCierre())) {
				varTCminiCreciente = 0;
			} else {
				varTCminiCreciente = -varTCminiCreciente;
			}
		}
		
		BigDecimal tcmRedondeo = BigDecimal.ZERO;
		BigDecimal tcmCalculo = BigDecimal.ZERO;
		if (umicTitular.getRentas().getCpagrenta().equals("1")){
			varTCminiCreciente = varTCminiCreciente/12;

		}
		if (umicTitular.getRentas().getCpagrenta().equals("3")){
			varTCminiCreciente = varTCminiCreciente/3;
		}
		if (umicTitular.getRentas().getCpagrenta().equals("2")){
			varTCminiCreciente = varTCminiCreciente/6;
		}
		if (umicTitular.getRentas().getCpagrenta().equals("5")){
			varTCminiCreciente = varTCminiCreciente/4;
			
		}

		varCsp238N1 = (BigDecimal) mapVariables.get(CLAVE_VAR_CSP238N1);
		if (varCsp238N1 == null) {
			varCsp238N1 = BigDecimal.ZERO;
			mapVariables.put(CLAVE_VAR_CSP238N1, varCsp238N1);
		}

		String auxIter = (String) mapVariables.get("Primera_Iteracion_CSP463COL");
		
		if (auxIter == null) {
 			mapVariables.put("Primera_Iteracion_CSP463COL", "Realizada");
			String sumator;
			sumator = (String) mapVariables.get("SUMATORIO");
			if (sumator == null) {
				sumator = ConstantsFunciones.CTE_CADENA_VACIA;
				mapVariables.put("SUMATORIO", sumator);
			}
			
			if (umicTitular.getRentas().getTempVit().equals("L")) {
				lstPagosPlanificados = obtenerDatos.recuperarPagosPlanificadosLocas(umicTitular.getDatosGenerales().getKpoliza(),
						umicTitular.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(),
						umicTitular.getDatosGenerales().getNsuscri(), umic.getDatosGenerales().getKgarantia(),
						umicTitular.getDatosGenerales().getKprestacion().substring(0, 1),
						umicTitular.getDatosGenerales().getKprestacion().substring(1,3),
						umicTitular.getDatosGenerales().getKprestacion().substring(3),
						umicTitular.getDatosGenerales().getKajuste(), fcalc,umicTitular.getDatosGenerales().getNorden());
				
				if (lstPagosPlanificados == null || lstPagosPlanificados.isEmpty()) {
					
					throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_GR,
							new String[] {});
					
				}
				for (int j = 1; j <= varTCminiCreciente; j++) {
					
					varCsp238N1Temp = lstPagosPlanificados.get(j - 1).getEplreaBruto();
					//PINTAR PAGOS

					varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
				
				}
			} else {
				
				lstPagos = obtenerDatos.recuperarPlanPagos(umicTitular.getKey());
				
				if (lstPagos == null || lstPagos.isEmpty()) {
					
					throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_GR,
							new String[] {});
				
				}
				
				for (int j = 1; j <= varTCminiCreciente; j++) {
					
					varCsp238N1Temp = lstPagos.get(j - 1).getImpPago();

					varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
					
				}
			
				
				if(!(lstPagos.get(varTCminiCreciente).getFecPago().after(UtilFechas.decreDias(fcalc, 1)))){
					
					varCsp238N1Temp = lstPagos.get(varTCminiCreciente+1).getImpPago();

					varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
					
					sumator = "procesado";
					mapVariables.put("SUMATORIO", sumator);
					
					mapVariables.put(CLAVE_VAR_CSP238N1, varCsp238N1);

					varCsp463COL = varPU.subtract(varCsp238N1);
					// Se realiza el cálculo mediante la formula: CSP463 = Máximo (varCsp463, 0)
					csp463COL = BigDecimal.ZERO.max(varCsp463COL);
					
					return csp463COL;
					
					}
				
				
			}
			
			sumator = "procesado";
			mapVariables.put("SUMATORIO", sumator);

		}
		

		if (umic.getDatosGenerales().getNorden().toString().substring(0, 1).equals(ConstantsFunciones.CTE_2_STRING)) {

			cPagRentN2 = umic.getRentas().getForpagrent();
			cPagRentN1 = umicTitular.getRentas().getForpagrent();

			if (umicTitular.getRentas().getCformaRevrenta().contentEquals("G")) {

				if (!(varTCmini == 0) && !(iteracion == 1)) {
					varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion - 1, fcalc, umic, btcUmic,
							mapVariables, codSubproceso);
					varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
				}
				if (iteracion == 1) {
					if (UtilFechas.getAnio(umic.getFechas().getFecinisus()) == UtilFechas.getAnio(umic.getDatosGenerales().getFecCierre())
							&& UtilFechas.getMes(umic.getFechas().getFecinisus()) == UtilFechas.getMes(umic.getDatosGenerales().getFecCierre())) {
						varCsp238N1 = BigDecimal.ZERO;
					} else {
						varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion, fcalc, umic,
								btcUmic, mapVariables, codSubproceso);
						varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
					}
				}
				if ((varTCmini == 0) && !(iteracion == 1) 
						&& umicTitular.getDatosGenerales().getKmodalidad().equals(412)) {
					varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion - 1, fcalc, umic,
							btcUmic, mapVariables, codSubproceso);
					varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
				}

			} else if (cPagRentN1 != cPagRentN2) {

				if (umicTitular.getRentas().getForpagrent().equals(ConstantsFunciones.CTE_1)) {

					if (!(varTCmini == 0) && !(iteracion == 1)) {
						varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion - 1, fcalc, umic,
								btcUmic, mapVariables, codSubproceso);
						varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
					}
					if (iteracion == 1) {
						if (UtilFechas.getAnio(umic.getFechas().getFecinisus()) == UtilFechas.getAnio(umic.getDatosGenerales().getFecCierre())
								&& UtilFechas.getMes(umic.getFechas().getFecinisus()) == UtilFechas.getMes(umic.getDatosGenerales().getFecCierre())) {
							varCsp238N1 = BigDecimal.ZERO;
						} else {
							varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion, fcalc, umic,
									btcUmic, mapVariables, codSubproceso);
							varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
						}
					}
					if ((varTCmini == 0) && !(iteracion == 1) 
							&& umicTitular.getDatosGenerales().getKmodalidad().equals(412)) {
						varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion - 1, fcalc, umic,
								btcUmic, mapVariables, codSubproceso);
						varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
					}

				} else if (umicTitular.getRentas().getForpagrent().equals(ConstantsFunciones.CTE_2)) {
					
					if (!(varTCmini == 0) && !(iteracion == 1)) {
						varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion - 1, fcalc, umic,
								btcUmic, mapVariables, codSubproceso);
						varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
					} 
					if (iteracion == 1) {
						if (UtilFechas.getAnio(umic.getFechas().getFecinisus()) == UtilFechas.getAnio(umic.getDatosGenerales().getFecCierre())
								&& UtilFechas.getMes(umic.getFechas().getFecinisus()) == UtilFechas.getMes(umic.getDatosGenerales().getFecCierre())) {
							varCsp238N1 = BigDecimal.ZERO;
						} else {
							varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion, fcalc, umic,
									btcUmic, mapVariables, codSubproceso);
							varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
						}
						
					}
					if ((varTCmini == 0) && !(iteracion == 1)) {
						varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion - 1, fcalc, umic,
								btcUmic, mapVariables, codSubproceso);
						varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
					}
				} else if (umicTitular.getRentas().getForpagrent().equals(ConstantsFunciones.CTE_4)){
					if (!(varTCmini == 0) && !(iteracion == 1)) {
						varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion - 1, fcalc, umic,
								btcUmic, mapVariables, codSubproceso);
						varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
					} 
					if (iteracion == 1) {
						if (UtilFechas.getAnio(umic.getFechas().getFecinisus()) <= UtilFechas.getAnio(umic.getDatosGenerales().getFecCierre())
								&& UtilFechas.getMes(umic.getFechas().getFecinisus()) <= UtilFechas.getMes(umic.getDatosGenerales().getFecCierre())) {
							varCsp238N1 = BigDecimal.ZERO;
						} else {
							varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion, fcalc, umic,
									btcUmic, mapVariables, codSubproceso);
							varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
						}
					}
					if ((varTCmini == 0) && !(iteracion == 1)) {
						if (UtilFechas.getAnio(umic.getFechas().getFecinisus()) == UtilFechas.getAnio(umic.getDatosGenerales().getFecCierre())
								&& UtilFechas.getMes(umic.getFechas().getFecinisus()) == UtilFechas.getMes(umic.getDatosGenerales().getFecCierre())) {
							varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion - 1, fcalc, umic,
									btcUmic, mapVariables, codSubproceso);
						} else {
							varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion - 1, fcalc, umic,
									btcUmic, mapVariables, codSubproceso);
						}
						
						varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
					}
				}

			} else { 

				if (umic.getDatosGenerales().getKcertificado() == 3682) {
					if (!(varTCmini == 0) && !(iteracion == 1)) {
						varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion + 1, fcalc, umic,
								btcUmic, mapVariables, codSubproceso);
						varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
					}
					if (iteracion == 1) {
						varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion, fcalc, umic,
								btcUmic, mapVariables, codSubproceso);
						varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
					}
				}else {
					
					if (!(varTCmini == 0) && !(iteracion == 1)) {
						varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic,
								mapVariables, codSubproceso);
						varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
					}
					if (iteracion == 1) {
						if (UtilFechas.getAnio(umic.getFechas().getFecinisus()) <= UtilFechas.getAnio(umic.getDatosGenerales().getFecCierre())
									&& UtilFechas.getMes(umic.getFechas().getFecinisus()) <= UtilFechas.getMes(umic.getDatosGenerales().getFecCierre())) {
							varCsp238N1 = BigDecimal.ZERO;
						} else {
							if (umicTitular.getRentas().getForpagrent().equals(ConstantsFunciones.CTE_12)){
								varCsp238N1Temp = BigDecimal.ZERO;
							} else {
								varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion, fcalc, umic,
										btcUmic, mapVariables, codSubproceso);
							}
							
							varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
						}
					}
					if ((varTCmini == 0) && !(iteracion == 1) ) {
						if (UtilFechas.getAnio(umic.getFechas().getFecinisus()) == UtilFechas.getAnio(umic.getDatosGenerales().getFecCierre())
								&& UtilFechas.getMes(umic.getFechas().getFecinisus()) == UtilFechas.getMes(umic.getDatosGenerales().getFecCierre())) {
							varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion - 1, fcalc, umic,
									btcUmic, mapVariables, codSubproceso);
						} else {
							varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion - 1, fcalc, umic,
									btcUmic, mapVariables, codSubproceso);
						}
						varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
						
					}
				}

			}

		} else {

			if (umic.getRentas().getCformaRevrenta().equals("G")) {

				if (!(varTCmini == 0) && !(iteracion == 1)) {
					umic.getRentas().setForpagrent(ConstantsFunciones.CTE_12);
					varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion - 1, fcalc, umic, btcUmic,
							mapVariables, codSubproceso);
					varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
				}
				if (iteracion == 1) {
					if (UtilFechas.getAnio(umic.getFechas().getFecinisus()) <= UtilFechas.getAnio(umic.getDatosGenerales().getFecCierre())
								&& UtilFechas.getMes(umic.getFechas().getFecinisus()) <= UtilFechas.getMes(umic.getDatosGenerales().getFecCierre())) {
						varCsp238N1 = BigDecimal.ZERO;
					} else {
						varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion, fcalc, umic,
								btcUmic, mapVariables, codSubproceso);
						varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
					}
				}
				if ((varTCmini == 0) && !(iteracion == 1)) {
					varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion - 1, fcalc, umic,
							btcUmic, mapVariables, codSubproceso);
					varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
				}

			} else if (umic.getRentas().getForpagrent().equals(ConstantsFunciones.CTE_1)) {
				
				if (!(varTCmini == 0) && !(iteracion == 1)) {
					
					varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion - 1, fcalc, umic,
							btcUmic, mapVariables, codSubproceso);					
					
					varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
				}
				if (iteracion == 1) {
					if (UtilFechas.getAnio(umic.getFechas().getFecinisus()) == UtilFechas.getAnio(umic.getDatosGenerales().getFecCierre())
							&& UtilFechas.getMes(umic.getFechas().getFecinisus()) == UtilFechas.getMes(umic.getDatosGenerales().getFecCierre())) {
						varCsp238N1 = BigDecimal.ZERO;
					} else {
						varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion, fcalc, umic,
								btcUmic, mapVariables, codSubproceso);
						varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
					}
					
				}
				if ((varTCmini == 0) && !(iteracion == 1)) {
					varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion - 1, fcalc, umic,
							btcUmic, mapVariables, codSubproceso);
					varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
				}
			}  else if (umicTitular.getRentas().getForpagrent().equals(ConstantsFunciones.CTE_2)) {
				
				if (!(varTCmini == 0) && !(iteracion == 1)) {
					varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion - 1, fcalc, umic,
							btcUmic, mapVariables, codSubproceso);
					varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
				} 
				if (iteracion == 1) {
					if (UtilFechas.getAnio(umic.getFechas().getFecinisus()) == UtilFechas.getAnio(umic.getDatosGenerales().getFecCierre())
							&& UtilFechas.getMes(umic.getFechas().getFecinisus()) == UtilFechas.getMes(umic.getDatosGenerales().getFecCierre())) {
						varCsp238N1 = BigDecimal.ZERO;
					} else {
						varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion, fcalc, umic,
								btcUmic, mapVariables, codSubproceso);
						varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
					}
				}
				if ((varTCmini == 0) && !(iteracion == 1)) {
					varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion - 1, fcalc, umic,
							btcUmic, mapVariables, codSubproceso);
					varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
				}
			} else {
				if (!(varTCmini == 0) && !(iteracion == 1)) {
					if (umicTitular.getRentas().getForpagrent().equals(ConstantsFunciones.CTE_3)) {
						varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion - 1, fcalc, umic,
								btcUmic, mapVariables, codSubproceso);
					} else {
						varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion - 1, fcalc, umic,
								btcUmic, mapVariables, codSubproceso);
					}
					varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
				}
				if (!(varTCmini == 0) && iteracion == 1) {
					if (umicTitular.getRentas().getForpagrent().equals(ConstantsFunciones.CTE_3)) {
						if (umicTitular.getDatosGenerales().getKmodalidad().equals(412)
								&& (UtilFechas.getAnio(umic.getFechas().getFecinisus()) <= UtilFechas.getAnio(umic.getDatosGenerales().getFecCierre())
									&& UtilFechas.getMes(umic.getFechas().getFecinisus()) <= UtilFechas.getMes(umic.getDatosGenerales().getFecCierre()))) {
							varCsp238N1 = BigDecimal.ZERO;
						} else {
							varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion, fcalc, umic,
									btcUmic, mapVariables, codSubproceso);
							varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
						}
					} else {
						if (UtilFechas.getAnio(umic.getFechas().getFecinisus()) <= UtilFechas.getAnio(umic.getDatosGenerales().getFecCierre())
									&& UtilFechas.getMes(umic.getFechas().getFecinisus()) <= UtilFechas.getMes(umic.getDatosGenerales().getFecCierre())) {
							varCsp238N1 = BigDecimal.ZERO;
						} else {
							varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion, fcalc, umic,
									btcUmic, mapVariables, codSubproceso);
							varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
						}
					}
				}
				if ((varTCmini == 0) && iteracion == 1) {
					if (UtilFechas.getAnio(umic.getFechas().getFecinisus()) == UtilFechas.getAnio(umic.getDatosGenerales().getFecCierre())
							&& UtilFechas.getMes(umic.getFechas().getFecinisus()) == UtilFechas.getMes(umic.getDatosGenerales().getFecCierre())) {
						varCsp238N1 = BigDecimal.ZERO;
					} else {
						varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion, fcalc, umic,
								btcUmic, mapVariables, codSubproceso);
						varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
					}
					
				}
				if ((varTCmini == 0) && iteracion !=1) {
					if (UtilFechas.getAnio(umic.getFechas().getFecinisus()) == UtilFechas.getAnio(umic.getDatosGenerales().getFecCierre())
							&& UtilFechas.getMes(umic.getFechas().getFecinisus()) == UtilFechas.getMes(umic.getDatosGenerales().getFecCierre())) {
						varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion - 1, fcalc, umic,
								btcUmic, mapVariables, codSubproceso);
					} else {
						varCsp238N1Temp = calcularCSP463COL(proyUmic, bloqueCorriente, iteracion - 1, fcalc, umic,
								btcUmic, mapVariables, codSubproceso);
					}
					varCsp238N1 = varCsp238N1.add(varCsp238N1Temp);
				}
			}
		}
		mapVariables.put(CLAVE_VAR_CSP238N1, varCsp238N1);

		varCsp463COL = varPU.subtract(varCsp238N1);
		// Se realiza el cálculo mediante la formula: CSP463 = Máximo (varCsp463, 0)
		csp463COL = BigDecimal.ZERO.max(varCsp463COL);
		if (ModuloCSP463COL.LOG.isTraceEnabled()) {
			ModuloCSP463COL.LOG.trace(
					"Fin función << ModuloCSP463COL >> de la clase ModuloCSP463COL, para la iteracion = {} con resultado csp463COL = {}",
					iteracion, csp463COL);
		}
		
		return csp463COL;
	}

	private BigDecimal calcularCSP463COL(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente,
			Integer iteracion, Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, Map<String, Object> mapVariables,
			String codSubproceso) {
		Modulo moduloCSP238N1;
		BigDecimal varCsp238N1Temp = null;
		
		moduloCSP238N1 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP238N1);
		varCsp238N1Temp = (BigDecimal) moduloCSP238N1.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic,
				btcUmic, mapVariables, codSubproceso);
		

		return varCsp238N1Temp;
	}

}
