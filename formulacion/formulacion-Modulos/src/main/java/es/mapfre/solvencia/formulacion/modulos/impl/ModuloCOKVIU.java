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

public class ModuloCOKVIU implements Modulo {

	private static final Logger LOG = LoggerFactory.getLogger(ModuloCOKVIU.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_COKVIU;

	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}


	@Override
	public Object execute(Object... args) throws Solvencia2Excepcion {

		BigDecimal cokviu = BigDecimal.ZERO;

		try {
			if (ModuloCOKVIU.LOG.isTraceEnabled()) {
				ModuloCOKVIU.LOG.trace("Inicio de execute en clase ModuloCOKVIU");
			}

			// Recuperamos los datos que le pasaremos a la función ModuloCOKVIU
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];

			cokviu = moduloCokVIU(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso,
					mapVariables);

		} catch (Solvencia2Excepcion e) {
			ModuloCOKVIU.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCOKVIU.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloCOKVIU.LOG.isTraceEnabled()) {
			ModuloCOKVIU.LOG.trace("Fin de execute en clase ModuloCOKVIU");
		}

		return cokviu;
	}

	private BigDecimal moduloCokVIU(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, String codSubproceso,
			Map<String, Object> mapVariables) {

		BigDecimal varcokviu = BigDecimal.ZERO;
		String varTitular;
		Modulo moduloVCVH;
		if (ModuloCOKVIU.LOG.isTraceEnabled()) {
			ModuloCOKVIU.LOG.trace(
					"Inicio función << moduloCokVIU >> de la clase ModuloCOKVIU, para la  iteracion = {}", iteracion);
		}

		// Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);

		if (bloqueCorriente.getFechaDevengo() == null) {
			return varcokviu;
		}
		if(null != umic.getDatosGenerales() && null != umic.getDatosGenerales().getKbencon()){
			varTitular = umic.getDatosGenerales().getKbencon();
		}else{
			return varcokviu;
		}
		varTitular = umic.getDatosGenerales().getKbencon();

		if (varTitular.equals("BNC") || varTitular.equals("")) {
			return varcokviu;
		} else if (varTitular.compareTo(ConstantsModulos.CTE_KBENCON_301) == 0
				|| varTitular.compareTo(ConstantsModulos.CTE_KBENCON_301) == 1) {

			moduloVCVH = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VCVH);
			varcokviu = (BigDecimal) moduloVCVH.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic,
					mapVariables, codSubproceso);
		}

		return varcokviu;

	}

}
