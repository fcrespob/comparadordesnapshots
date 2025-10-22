package es.mapfre.solvencia.formulacion.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;

public final class ValidacionesProgramas {
	
	private static final Logger LOG = LoggerFactory.getLogger(ValidacionesProgramas.class);
	
	private ValidacionesProgramas() {
		
	}
	
	public static void validarParamEntrada(final Umic umic, final FichaProceso fichaProceso, final DetalleBaseTecnica btc, final List<DetalleCorriente> detallesCorrientes, final String subProcesoActual) {
		if (null == umic) {
			if (ValidacionesProgramas.LOG.isDebugEnabled()) {
				ValidacionesProgramas.LOG.debug(Util.errorValidacionA1(ConstantsModulos.CTE_UMIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsModulos.CTE_UMIC});
		}
		
		if (null == fichaProceso) {
			if (ValidacionesProgramas.LOG.isDebugEnabled()) {
				ValidacionesProgramas.LOG.debug(Util.errorValidacionA1(ConstantsModulos.CTE_FICHA_PROCESO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsModulos.CTE_FICHA_PROCESO});
		}
		
		if (null == btc) {
			if (ValidacionesProgramas.LOG.isDebugEnabled()) {
				ValidacionesProgramas.LOG.debug(Util.errorValidacionA1(ConstantsModulos.CTE_BTC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsModulos.CTE_BTC});
		}
		
		if (null == detallesCorrientes) {
			if (ValidacionesProgramas.LOG.isDebugEnabled()) {
				ValidacionesProgramas.LOG.debug(Util.errorValidacionA1(ConstantsModulos.CTE_DET_CORRIENTE));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsModulos.CTE_DET_CORRIENTE});
		}
		
		if (null == subProcesoActual || subProcesoActual.isEmpty()) {
			if (ValidacionesProgramas.LOG.isDebugEnabled()) {
				ValidacionesProgramas.LOG.debug(Util.errorValidacionA1(ConstantsModulos.CTE_SUBPROCESO_ACT));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsModulos.CTE_SUBPROCESO_ACT});
		}
	}
}
