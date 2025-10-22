package es.mapfre.solvencia.formulacion.util;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;

public class ValidacionesFuncionesRentas {
	
	/** Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ValidacionesFuncionesRentas.class);
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad de los campos de la función vrta (parte 1)
	 * 
	 * @param tipoISegundoTramo
	 * 				Tipo de interés segundo tramo
	 * @param edadActuarial
	 * 				Edad actuarial a fecha efecto técnico.
	 * @param lstValoresTabMort
	 * 				Valores de la tabla de mortalidad
	 * @param numPagos
	 * 				Nº de Pagos por año de la renta
	 * @param porcenRevalRen
	 * 				Porcentaje revalorización de la renta
	 */
	public static void validacionCamposFuncionVRtaParte2( final BigDecimal tipoISegundoTramo,
			final Integer edadActuarial, final List<BigDecimal> lstValoresTabMort, final Integer numPagos,
			final BigDecimal porcenRevalRen) {
		
		if (null == tipoISegundoTramo) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_TI_SEGUNDO_TR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_TI_SEGUNDO_TR});
		}
		
		if (null == edadActuarial) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_EDAD_ACTUARIA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_EDAD_ACTUARIA});
		}
		
		if (null == lstValoresTabMort || lstValoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		if (null == numPagos) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NUM_PAGOS));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NUM_PAGOS});
		}
		
		if (null == porcenRevalRen) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_REVAL_RENTA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_REVAL_RENTA});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad de los campos de la función vrta
	 * @param mesesCompletos
	 * 				Meses completos transcurridos desde efecto (técnico) póliza a fecha cálculo
	 * @param diferRenta
	 * 				Diferimiento de la renta (años)
	 * @param duracionRenta
	 * 				Duración de la renta (desde fecha efecto técnico (años)
	 * @param tipoIPrimerTramo
	 * 				Tipo de interés primer tramo
	 * @param duracionPrimerTra
	 * 				Duración primer tramo
	
	 */
	public static void validacionCamposFuncionVRtaParte1(final Integer mesesCompletos, final Integer diferRenta,final Integer duracionRenta, 
			final BigDecimal tipoIPrimerTramo, final Integer duracionPrimerTra) {
		
		if (null == mesesCompletos) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_MESES_COMPLET));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_MESES_COMPLET});
		}
		
		if (null == diferRenta) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DIFER_RENTA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DIFER_RENTA});
		}
		
		if (null == duracionRenta) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DUR_RENTA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DUR_RENTA});
		}
		
		if (null == tipoIPrimerTramo) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_TI_PRIMER_TRA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_TI_PRIMER_TRA});
		}
		
		if (null == duracionPrimerTra) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DUR_PRIMER_TR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DUR_PRIMER_TR});
		}
	}
	
	/**
	 *  Función encargada de validar los parametros de entrada de la función ax.
	 * @param difer
	 * 
	 * @param edadAsegurado
	 * 					Edad x del asegurado
	 * @param desRenta
	 * 				Desplazamiento de la renta
	 * @param durSeguro
	 * 				Duración del seguro
	 * @param inTramo1
	 * 				Tramo de interés 1
	 * @param lstValoresTabMort
	 * 				Valores de la tabla de mortalidad
	 */
	public static void validarParamEntradaFuncionAx(final Integer difer, final Integer edadAsegurado, final Integer desRenta, final Integer durSeguro, final BigDecimal inTramo1, 
			final List<BigDecimal> lstValoresTabMort) {
		
		if (null == difer) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DIFER));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DIFER});
		}
		
		if (null == edadAsegurado) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_EDAD_ASEG));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_EDAD_ASEG});
		}
		
		if (null == desRenta) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DES_RENTA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DES_RENTA});
		}
		
		if (null == durSeguro) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DUR_SEGURO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DUR_SEGURO});
		}
		
		if (null == inTramo1) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_IN_TRAMO1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_IN_TRAMO1});
		}
		
		if (null == lstValoresTabMort || lstValoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
	}
	
	/**
	 * Función Función encargada de validar los parametros de entrada de la función axcg.
	 * 
	 * @param varx
	 * @param varp
	 * @param varm
	 * @param vari1
	 * @param difer
	 * @param lstValoresTabMort Valores de la tabla de mortalidad
	 */
	public static void validarParamEntradaFuncionAxcg(final Integer varx, final Integer varp, final Integer varm, 
			final BigDecimal vari1, final Integer difer, final BigDecimal prp, final List<BigDecimal> lstValoresTabMort) {
		
		if (null == varx) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == varp) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARP});
		}
		
		if (null == varm) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		
		if (null == vari1) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == difer) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_DIFER));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_DIFER});
		}
		
		if (null == prp) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PRP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PRP});
		}
		
		if (null == lstValoresTabMort || lstValoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Renta
	 * @param proyUmic Estructura detalleCorrientes de la umic.
	 * @param bloqueCorriente Indica el periodo de proyección J que se está calculando de entre todos los periodos de proyección de la umic (proyUmic).
	 * @param umic Contiene los datos de la Umic que se está procesando.
	 * @param btcUmic Contiene el detalle de la base técnica de cálculo para la umic.
	 * @param x363 Edad actuarial a la fecha de inicio de pago de renta del titular.
	 * @param lxIni Probabilidad supervivencia a  Edad X363ini.
	 * @param tCy Resultado de la función naños() en base al criterio establecido entre la fecha de efecto de la suscripción y la fecha de cálculo.
	 * @param m Duración en que aplica el primer interés técnico.
	 * @param lstValoresTabMort Valores de la tabla de mortalidad.
	 * @param iteracion
	 * @param varCriterFec
	 */
	public static void validarParamEntradaFuncionRenta(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, 
			final Umic umic, final DetalleBaseTecnica btcUmic, final Integer x363, final BigDecimal lxIni, final BigDecimal tCy, 
			final BigDecimal m, final List<BigDecimal> lstValoresTabMort, final String varCriterFec){
		if (null == proyUmic || proyUmic.isEmpty()) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PROY_UMIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PROY_UMIC});
		}
		
		if (null == bloqueCorriente){
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BLOQUE_CORR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BLOQUE_CORR});
		}
		
		if (null == umic){
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_UMIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_UMIC});
		}
		
		if (null == btcUmic){
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BTC_UMIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BTC_UMIC});
		}
		
		if (null == x363){
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_X363));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_X363});
		}
		
		if (null == lxIni){
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_LX_INI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_LX_INI});
		}
		
		if (null == tCy){
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TCY));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TCY});
		}
		
		if (null == m){
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		
		if (null == lstValoresTabMort || lstValoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		if (null == varCriterFec){
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CRIT_FEC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CRIT_FEC});
		}
		
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Rentgeo2it
	 * @param x Edad actuarial a fecha de efecto.
	 * @param t Anualidad del cálculo.
	 * @param n Duración del seguro.
	 * @param prg Porcentaje de la revalorización geométrica.
	 * @param lstValoresTabMort Valores de la tabla de mortalidad.
	 * @param m Número de años en que aplicamos un primer interés técnico.
	 * @param unoMasI1Entre100 Primer Interés técnico mas 1 entre 100.
	 * @param unoMasI2Entre100 Segundo Interés técnico mas 1 entre 100.
	 */
	public static void validarParamEntradaFuncionRentgeo2it(final BigDecimal x, final Integer t, final Integer n, final BigDecimal prg,
			final List<BigDecimal> lstValoresTabMort, final BigDecimal m, final BigDecimal unoMasI1Entre100, final BigDecimal unoMasI2Entre100){
		
		if (null == x) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == t) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_T));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_T});
		}
		
		if (null == n) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
		
		if (null == prg) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PRG));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PRG});
		}
		
		if (null == lstValoresTabMort || lstValoresTabMort.isEmpty()) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		if (null == m){
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		
		if (null == unoMasI1Entre100) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == unoMasI2Entre100) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI2});
		}
		
		
	}
	
	public static void validacionCamposFuncionVRtafParte1(final Integer mesesCompletos, final Integer diferRenta,final Integer duracionRenta, 
			final BigDecimal tipoIPrimerTramo, final Integer duracionPrimerTra) {
		
		if (null == mesesCompletos) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_MESES_COMPLET));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_MESES_COMPLET});
		}
		
		if (null == diferRenta) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DIFER_RENTA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DIFER_RENTA});
		}
		
		if (null == duracionRenta) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DUR_RENTA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DUR_RENTA});
		}
		
		if (null == tipoIPrimerTramo) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_TI_PRIMER_TRA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_TI_PRIMER_TRA});
		}
		
		if (null == duracionPrimerTra) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DUR_PRIMER_TR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DUR_PRIMER_TR});
		}
	}
	
	public static void validacionCamposFuncionVRtafParte2( final BigDecimal tipoISegundoTramo, final Integer numPagos,
			final BigDecimal porcenRevalRen) {
		
		if (null == tipoISegundoTramo) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_TI_SEGUNDO_TR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_TI_SEGUNDO_TR});
		}
		
		if (null == numPagos) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NUM_PAGOS));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NUM_PAGOS});
		}
		
		if (null == porcenRevalRen) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_REVAL_RENTA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_REVAL_RENTA});
		}
	}
	//validacionCamposFuncionVRtafParte3(tcm, ttm, beta);

		public static void validacionCamposFuncionVRtafParte3( final Integer tcm,final Integer ttm, final Integer beta) {
		
		
		if (null == tcm) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_TCM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_TCM});
		}
		
		if (null == ttm) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_TTM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_TTM});
		}
		
		if (null == beta) {
			if (ValidacionesFuncionesRentas.LOG.isDebugEnabled()) {
				ValidacionesFuncionesRentas.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_BETA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_BETA});
		}
	}


}
