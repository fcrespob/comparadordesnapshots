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
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;

public class ModuloORFZ3X2Y1 implements Modulo {

	private static final Logger LOG = LoggerFactory.getLogger(ModuloORFZ3X2Y1.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_ORFZ3X2Y1;

	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}

	@Override
	public Object execute(Object... args) throws Solvencia2Excepcion {

		BigDecimal orfz3x2y1 = BigDecimal.ZERO;

		try {
			if (ModuloORFZ3X2Y1.LOG.isTraceEnabled()) {
				ModuloORFZ3X2Y1.LOG.trace("Inicio de execute en clase ModuloORFZ3X2Y1");
			}

			// Recuperamos los datos que le pasaremos a la función ModuloCSPVIU
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];

			orfz3x2y1 = moduloORFZ3X2Y1(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso,
					mapVariables);

		} catch (Solvencia2Excepcion e) {
			ModuloORFZ3X2Y1.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloORFZ3X2Y1.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloORFZ3X2Y1.LOG.isTraceEnabled()) {
			ModuloORFZ3X2Y1.LOG.trace("Fin de execute en clase ModuloORFZ3X2Y1");
		}

		return orfz3x2y1;
	}

	private BigDecimal moduloORFZ3X2Y1(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, String codSubproceso,
			Map<String, Object> mapVariables) {

		BigDecimal varORFZ3X2Y1 = BigDecimal.ZERO;
		BigDecimal varVZC3 = BigDecimal.ZERO, varVZC2 = BigDecimal.ZERO, varmoduloFPTOZC, varmoduloVZC333_2, varmoduloFZC = BigDecimal.ZERO;
		String varTitular;
		List<DetalleCorriente> varProyFptozc = proyUmic;
		Modulo moduloVZC3, moduloVZC2, moduloFPTOZC, moduloVZC333_2, moduloFZC, moduloFZCHM1;
		BigDecimal formula = BigDecimal.ZERO;
		Timestamp auxFecDev;
		Timestamp auxFecPago;
		BigDecimal sumatorioCom;
		BigDecimal sumatorioFPTOZC;
		BigDecimal varFzchm1;
		List<DetalleCorriente> varProyFzchm1 = null;
		String auxFec = (String) mapVariables.get("FEC_AUX_ORFZ3X2Y1");
		boolean varHijoMinusvalido = false;
		

		moduloVZC3 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC3);
		moduloVZC2 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC2);
		moduloFZCHM1 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FZCHM1);
		
		if (auxFec == null) {
			for (int i=0; i<proyUmic.size(); i++) {
				if (iteracion > i) {
					int auxi = iteracion;
					auxFecDev = bloqueCorriente.getFechaDevengo();
					auxFecPago = bloqueCorriente.getFechaPago();
					while (auxi > i) {
						auxFecDev = UtilFechas.decreMeses(auxFecDev, 1);
						auxFecPago = UtilFechas.decreMeses(auxFecPago, 1);
						auxi --;
					}
				} else if(i > iteracion) {
					auxFecDev = UtilFechas.plusMeses(proyUmic.get(i-1).getBloqueBySubproceso(codSubproceso).getFechaDevengo(), 1);
					auxFecPago = UtilFechas.plusMeses(proyUmic.get(i-1).getBloqueBySubproceso(codSubproceso).getFechaPago(), 1);
				} else {
					auxFecDev = bloqueCorriente.getFechaDevengo();
					auxFecPago = bloqueCorriente.getFechaPago();
				}

				if (i != iteracion -1 ) {
					mapVariables.put("varFecDevAntFPTOZC" + i, auxFecDev);
				}
				if (proyUmic.get(i).getBloqueBySubproceso(codSubproceso).getFechaDevengo() == null) {
					proyUmic.get(i).getBloqueBySubproceso(codSubproceso).setFechaDevengo(auxFecDev);
					proyUmic.get(i).getBloqueBySubproceso(codSubproceso).setFechaPago(auxFecPago);
				}
				mapVariables.put("FEC_AUX_ORFZ3X2Y1", "S");
			}
		}
		
		if (umic.getAsegurados().getFnacAseg3() != null && umic.getAsegurados().getCsexAseg3() != null
				&& umic.getAsegurados().getEdadAseg3() != null) {
			if (umic.getOtrosDatos().getCestadoAseg3().equals(ConstantsModulos.CTE_CESTADO_ASEG_M)) {
				varHijoMinusvalido = true;
			}
		}
		if (umic.getAsegurados().getFnacAseg4() != null && umic.getAsegurados().getCsexAseg4() != null
				&& umic.getAsegurados().getEdadAseg4() != null) {
			if (umic.getOtrosDatos().getCestadoAseg4().equals(ConstantsModulos.CTE_CESTADO_ASEG_M)) {
				if ( umic.getAsegurados().getFnacAseg4().after(umic.getAsegurados().getFnacAseg3())) {
					varHijoMinusvalido = true;
				}
			}
		}
		if (umic.getAsegurados().getFnacAseg5() != null && umic.getAsegurados().getCsexAseg5() != null
				&& umic.getAsegurados().getEdadAseg5() != null) {
			if (umic.getOtrosDatos().getCestadoAseg5().equals(ConstantsModulos.CTE_CESTADO_ASEG_M)) {
				if ( umic.getAsegurados().getFnacAseg5().after(umic.getAsegurados().getFnacAseg4())) {
					varHijoMinusvalido = true;
				}
			}
		}
		
		
		if(varHijoMinusvalido==true) {
			varProyFzchm1 = proyUmic;
			varFzchm1 = (BigDecimal) moduloFZCHM1.execute(varProyFzchm1, bloqueCorriente, iteracion, fcalc, umic,
					btcUmic, mapVariables, codSubproceso);

		}else {
			varFzchm1 = BigDecimal.ONE;
		}
		
		varVZC3 = BigDecimal.ONE.subtract(varFzchm1);
		
		// Preparación fórmula cálculo.
//		if (null != umic.getOtrosDatos().getCestadoAseg3()){	
//			varVZC3 = (BigDecimal) moduloVZC3.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic,
//				mapVariables, codSubproceso);
//		}
		
		if (null != umic.getOtrosDatos().getCestadoAseg2()){
			varVZC2 = (BigDecimal) moduloVZC2.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic,
				mapVariables, codSubproceso);
		}
		
		moduloFPTOZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FPTOZC);
		varmoduloFPTOZC = (BigDecimal) moduloFPTOZC.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic,
				mapVariables, codSubproceso);
//		if (auxFPTOZC == null) {
//			varmoduloFPTOZC = (BigDecimal) moduloFPTOZC.execute(proyUmic, bloqueCorriente, 1, fcalc, umic, btcUmic,
//					mapVariables, codSubproceso);
//			mapVariables.put("auxFPTOZC",1);
//		} else {
//			varmoduloFPTOZC = (BigDecimal) moduloFPTOZC.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic,
//					mapVariables, codSubproceso);
//		}
		

		moduloVZC333_2 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC3332);
		varmoduloVZC333_2 = (BigDecimal) moduloVZC333_2.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic,
				btcUmic, mapVariables, codSubproceso);
		
		moduloFZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FZC);
		varmoduloFZC = (BigDecimal) moduloFZC.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic,
				btcUmic, mapVariables, codSubproceso);
		
		sumatorioCom = (BigDecimal) mapVariables.get("ORFZ3X2Y1_SUMATORIO");
		if (sumatorioCom == null) {
			sumatorioCom = BigDecimal.ZERO;
		}
		// Calculamos el sumatorio desde la iteracion 1 hasta la actual
		sumatorioCom = sumatorioCom.add(BigDecimal.ONE.subtract(varVZC2).multiply(varmoduloFPTOZC));
		mapVariables.put("ORFZ3X2Y1_SUMATORIO", sumatorioCom);
		// Formula matemática del módulo
		formula = varVZC3.multiply(sumatorioCom).add(varmoduloVZC333_2);

		// asegurado 2 = conyuge
		// asegurado 3 = hijo

		if ((null != umic.getOtrosDatos().getCestadoAseg1() && umic.getOtrosDatos().getCestadoAseg1().equals(ConstantsModulos.CTE_CESTADO_ASEG_V))
				&& (null != umic.getOtrosDatos().getCestadoAseg2() && umic.getOtrosDatos().getCestadoAseg2().equals(ConstantsModulos.CTE_CESTADO_ASEG_V))
				&& (null != umic.getOtrosDatos().getCestadoAseg3() && (umic.getOtrosDatos().getCestadoAseg3().equals(ConstantsModulos.CTE_CESTADO_ASEG_V)
						|| umic.getOtrosDatos().getCestadoAseg3().equals(ConstantsModulos.CTE_CESTADO_ASEG_M)))) {

			varORFZ3X2Y1 = formula;

		} else if ((null != umic.getOtrosDatos().getCestadoAseg1() && umic.getOtrosDatos().getCestadoAseg1().equals(ConstantsModulos.CTE_CESTADO_ASEG_V))
				&& ((null != umic.getOtrosDatos().getCestadoAseg2() && umic.getOtrosDatos().getCestadoAseg2().equals(ConstantsModulos.CTE_CESTADO_ASEG_A))
						|| null == umic.getOtrosDatos().getCestadoAseg2())
				&& (null != umic.getOtrosDatos().getCestadoAseg3() && (umic.getOtrosDatos().getCestadoAseg3().equals(ConstantsModulos.CTE_CESTADO_ASEG_V)
						|| umic.getOtrosDatos().getCestadoAseg3().equals(ConstantsModulos.CTE_CESTADO_ASEG_M)))) {

			varVZC2 = BigDecimal.ZERO;
			varORFZ3X2Y1 = varVZC3.multiply(varmoduloFZC);
					

		} else if (((null != umic.getOtrosDatos().getCestadoAseg1() && umic.getOtrosDatos().getCestadoAseg1().equals(ConstantsModulos.CTE_CESTADO_ASEG_A))
						|| null == umic.getOtrosDatos().getCestadoAseg1())
				&& (null != umic.getOtrosDatos().getCestadoAseg2() && umic.getOtrosDatos().getCestadoAseg2().equals(ConstantsModulos.CTE_CESTADO_ASEG_V))
				&& (null != umic.getOtrosDatos().getCestadoAseg3() && (umic.getOtrosDatos().getCestadoAseg3().equals(ConstantsModulos.CTE_CESTADO_ASEG_V)
						|| umic.getOtrosDatos().getCestadoAseg3().equals(ConstantsModulos.CTE_CESTADO_ASEG_M)))) {

			varORFZ3X2Y1 = varmoduloVZC333_2;

		}else if (((null != umic.getOtrosDatos().getCestadoAseg1() && umic.getOtrosDatos().getCestadoAseg1().equals(ConstantsModulos.CTE_CESTADO_ASEG_A))
						|| null == umic.getOtrosDatos().getCestadoAseg1())
				&& (null != umic.getOtrosDatos().getCestadoAseg2() && umic.getOtrosDatos().getCestadoAseg2().equals(ConstantsModulos.CTE_CESTADO_ASEG_V))
				&& ((null != umic.getOtrosDatos().getCestadoAseg3() && umic.getOtrosDatos().getCestadoAseg3().equals(ConstantsModulos.CTE_CESTADO_ASEG_A))
						|| null == umic.getOtrosDatos().getCestadoAseg3())) {

			varORFZ3X2Y1 = varmoduloVZC333_2;

		} else if (((null != umic.getOtrosDatos().getCestadoAseg1() && umic.getOtrosDatos().getCestadoAseg1().equals(ConstantsModulos.CTE_CESTADO_ASEG_V)))
				&& (null != umic.getOtrosDatos().getCestadoAseg2() && umic.getOtrosDatos().getCestadoAseg2().equals(ConstantsModulos.CTE_CESTADO_ASEG_V))
				&& ((null != umic.getOtrosDatos().getCestadoAseg3() && umic.getOtrosDatos().getCestadoAseg3().equals(ConstantsModulos.CTE_CESTADO_ASEG_A))
						|| null == umic.getOtrosDatos().getCestadoAseg3())) {

			varORFZ3X2Y1 = varmoduloVZC333_2;
		} else if (((null != umic.getOtrosDatos().getCestadoAseg1() && umic.getOtrosDatos().getCestadoAseg1().equals(ConstantsModulos.CTE_CESTADO_ASEG_A))
					|| null == umic.getOtrosDatos().getCestadoAseg1())
				&& (null != umic.getOtrosDatos().getCestadoAseg2() && umic.getOtrosDatos().getCestadoAseg2().equals(ConstantsModulos.CTE_CESTADO_ASEG_A)
						|| null == umic.getOtrosDatos().getCestadoAseg2())
				&& (null != umic.getOtrosDatos().getCestadoAseg3() && (umic.getOtrosDatos().getCestadoAseg3().equals(ConstantsModulos.CTE_CESTADO_ASEG_V)
						|| umic.getOtrosDatos().getCestadoAseg3().equals(ConstantsModulos.CTE_CESTADO_ASEG_M)))) {

			varORFZ3X2Y1 = varmoduloVZC333_2;

		}
		
		return varORFZ3X2Y1;
	}
}
