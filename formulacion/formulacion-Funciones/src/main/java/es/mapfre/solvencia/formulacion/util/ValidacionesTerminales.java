package es.mapfre.solvencia.formulacion.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;

public class ValidacionesTerminales {
	
	/** Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ValidacionesTerminales.class);
	
	/**
	 * Función encargada de realizar la validación de la segunda parte de los parametros de entrada de la función "vt002DcrrPb"
	 * 
	 * @param interes1
	 * @param difer
	 * @param varx
	 * @param varp
	 * @param varn
	 */
	public static void validarCamposEntradaFuncionvt002DcrrPbParte2(final BigDecimal interes1, final Integer difer, final Integer varx, final Integer varp, final Integer varn) {
		
		if (null == interes1) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_INTERES1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_INTERES1});
		}
		
		if (null == difer) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_DIFER));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_DIFER});
		}
		
		if (null == varx) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == varp) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARP});
		}
		
		
		if (null == varn) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de la primera parte de los parametros de entrada de la función "vt002DcrrPb"
	 * 
	 * @param capitalGar
	 * @param lstValoresTabMort
	 * @param gic
	 */
	public static void validarCamposEntradaFuncionvt002DcrrPbParte1(final BigDecimal capitalGar, final List<BigDecimal> lstValoresTabMort, final BigDecimal gic) {
		
		if (null == capitalGar) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CAP_GAR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CAP_GAR});
		}
		
		if (null == lstValoresTabMort || lstValoresTabMort.isEmpty()) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		if (null == gic) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_GIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_GIC});
		}
	}
	
	/**
	 * Función encargada de realizar la validación de obligatoriedad para la función Vtx003
	 * @param capitalGar Capital de la garantía.
	 * @param lstValoresTabMort Valores de la tabla de mortalidad.
	 * @param I1 Interés 1 de la base contable tratada.
	 * @param x Edad x del asegurado.
	 * @param p Desplazamiento de la renta.
	 * @param n Duración del seguro en años.
	 * @param m Duración en años del pago de primas.
	 * @param gipc Gastos de gestión interna sobre la prima.
	 * @param gepc Gastos de gestión externa.
	 * @param Pna0 Prima neta.
	 * @param prp Porcentaje de revalorización de Primas.
	 * @param Ifal Variable de Apoyo IFAL.
	 * @param Iant Variable de Apoyo IANT.
	 */
	public static void validarParamEntradaFuncionVtx003 (final BigDecimal capitalGar, final List<BigDecimal> lstValoresTabMort, final BigDecimal i1,
			final Integer x, final Integer p, final Integer n, final Integer m, final BigDecimal gipc, final BigDecimal gepc, final BigDecimal pna0,
			final BigDecimal prp, final BigDecimal ifal, final BigDecimal iant){
		if (null == capitalGar){
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CAPITAL_GAR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CAPITAL_GAR});
		}
		
		if (null == lstValoresTabMort || lstValoresTabMort.isEmpty()) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		if (null == i1){
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_INTERES1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_INTERES1});
		}
		
		if (null == x){
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
		if (null == p){
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARP});
		}
		
		if (null == n){
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
		
		if (null == m){
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		
		if (null == gipc){
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_GIPC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_GIPC});
		}
		
		if (null == gepc){
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_GEPC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_GEPC});
		}
		
		if (null == pna0){
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PNA0));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PNA0});
		}
		
		if (null == prp){
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PRP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PRP});
		}
		
		if (null == ifal){
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_IFAL));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_IFAL});
		}
		
		if (null == iant){
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_IANT));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_IANT});
		}
	}
	
	/**
	 * Función encargada de validar la obligatoriedad de la primera parte de los parametros de entrada de la función vtx05
	 * 
	 * @param capitalGar Capital de la garantía
	 * @param prc Porcentaje de revalorización de Capital
	 * @param lstValoresTabMort Valores de la tabla de mortalidad
	 * @param vari1 Interés 1 de la base contable tratada
	 * @param differ Diferimiento de la renta
	 * @param varx Edad x del asegurado
	 */
	public static void validarParamEntradaFuncionesVtx005Vtx004Parte1(final BigDecimal capitalGar, final BigDecimal prc, final List<BigDecimal> lstValoresTabMort,
			final BigDecimal vari1, final Integer varx) {
		
		if (null == capitalGar) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CAPITAL_GAR));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CAPITAL_GAR});
		}
		
		if (null == prc) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PRP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PRP});
		}
		

		if (null == lstValoresTabMort || lstValoresTabMort.isEmpty()) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		if (null == vari1) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == varx) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
		
	}
	
	/**
	 * Función encargada de validar la obligatoriedad de la segunda parte de los parametros de entrada de la función vtx05
	 * 
	 * @param varp Desplazamiento de la renta
	 * @param varn Duración del seguro en años
	 * @param varm Duración del pago de primas en años
	 * @param gic Gastos de gestión interna sobre Capital
	 * @param gepc Gastos de gestión externa
	 * @param pna0 Prima neta
	 */
	public static void validarParamEntradaFuncionesVtx005Vtx004Parte2(final Integer varp, final Integer varn,final Integer varm, final BigDecimal gic, final BigDecimal gepc, final BigDecimal pna0) {
		
		if (null == varp) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARP});
		}
		
		if (null == varn) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
		
		if (null == varm) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		
		if (null == gic) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_GIC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_GIC});
		}
		
		if (null == gepc) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_GEPC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_GEPC});
		}
		
		if (null == pna0) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PNA0));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PNA0});
		}
	}
	
	/**
	 * Función encargada de validar la obligatoriedad de la tercera parte de los parametros de entrada de la función vtx05
	 * 
	 * @param prp Porcentaje de revalorización de Primas
	 * @param csitupol Situación de la póliza
	 * @param ctipoaport Tipo aportación primas 
	 * @param ifal
	 * @param fallRed0 Capital inicial de fallecimiento a efecto reducción recibido
	 */
	public static void validarParamEntradaFuncionesVtx005Vtx004Parte3(final BigDecimal prp, final String csitupol, final String ctipoaport, final BigDecimal ifal, final BigDecimal fallRed0) {
		
		if (null == prp) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PRP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PRP});
		}
		
		if (null == csitupol || csitupol.isEmpty()) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CSITUPOL));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CSITUPOL});
		}
		
		if (null == ctipoaport || ctipoaport.isEmpty()) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CTIPOAPORT));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CTIPOAPORT});
		}
		
		if (null == ifal) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_IFAL));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_IFAL});
		}
		
		if (null == fallRed0) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_FALLRED));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_FALLRED});
		}
		
		if (ConstantsFunciones.CTE_APOR_PERIO.equals(ctipoaport) && ConstantsFunciones.CTE_APOR_REDUCIDA.equals(csitupol)){
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionAA());
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AA, new String[]{ConstantsFunciones.CTE_APOR_REDUCIDA});
		}
	}
	
	/**
	 * Función encargada de validar la obligatoriedad de la tercera parte de los parametros de entrada de la función vtx05
	 * 
	 * @param iant Variable de Apoyo IANT
	 * @param pas Variable de Apoyo PAS
	 * @param fut Variable de Apoyo FUT
	 */
	public static void validarParamEntradaFuncionesVtx005Vtx004Parte4(final BigDecimal iant, final BigDecimal pas, final BigDecimal fut) {
		if (null == iant) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_IANT));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_IANT});
		}
		
		if (null == pas) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PAS));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PAS});
		}
		
		if (null == fut) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FUT));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FUT});
		}
	}
	

	/**
	 * Función encargada de validar la obligatoriedad de la primera parte de los parametros de entrada de la función vtx009
	 * 
	 * @param renta1 Renta garantizada primer tramo
	 * @param renta2 Renta mínima garantizada
	 * @param vark Meses completos transcurridos desde efecto (técnico) póliza a fecha cálculo
	 * @param vard Diferimiento de la renta (años)
	 * @param varn Duración de la renta (desde fecha efecto técnico (años)
	 * @param vari1 Tipo de interés primer tramo
	 * @param varl Duración primer tramo
	 * @param vari2 Tipo de interés segundo tramo
	 */
	public static void validarParamEntradaFuncionVtx009Parte1(final BigDecimal renta1, final BigDecimal renta2, final Integer vark, final Integer vard, final Integer varn, final BigDecimal vari1,
			final Integer varl, final BigDecimal vari2) {
		
		if (null == renta1) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_RENTA_1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_RENTA_1});
		}
				
		if (null == renta2) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_RENTA_2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_RENTA_2});
		}
		
		if (null == vark) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARK));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARK});
		}
		
		if (null == vard) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARD));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARD});
		}
		
		if (null == varn) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
				
		if (null == vari1) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == varl) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_L));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_L});
		}
		
		if (null == vari2) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI2});
		}
		
	}
	
	/**
	 * Función encargada de validar la obligatoriedad de la segunda parte de los parametros de entrada de la función vtx009
	 * 
	 * @param varx Edad actuarial a fecha efecto técnico.
	 * @param lstValoresTabMort Valores de la tabla de mortalidad
	 * @param varm Nº de Pagos por año de la renta
	 * @param ppr Porcentaje revalorización de la renta
	 * @param prima
	 */
	public static void validarParamEntradaFuncionVtx009Parte2(final Integer varx, final List<BigDecimal> lstValoresTabMort, final Integer varm, final BigDecimal ppr, final BigDecimal prima) {
		
		if (null == varx) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}
				
		if (null == lstValoresTabMort || lstValoresTabMort.isEmpty()) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
		if (null == varm) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		
		if (null == ppr) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_PRP));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_PRP});
		}
		
		if (null == prima) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PRIMA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PRIMA});
		}
	}
	
	/**
	 * Función encargada de validar la obligatoriedad de la segunda parte de los parametros de entrada de la función vtx009
	 * 
	 * @param crm Capital en riesgo máximo
	 * @param gamma % sobre prima para capital fallecimiento
	 * @param mescaren Meses carencia
	 * @param gic Gastos de gestión interna sobre Capital
	 * @param gipc Gastos de gestión interna sobre Prima
	 */
	public static void validarParamEntradaFuncionVtx009Parte3(final BigDecimal gamma, final Integer mescaren, final BigDecimal gic, final BigDecimal gipc) {
				
		if (null == gamma) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_GAMMA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_GAMMA});
		}
		
		if (null == mescaren) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_MESCAREN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_MESCAREN});
		}
		if (null == gic) {
			ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_GIC));
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_GIC});
		}
		if (null == gipc) {
			ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_GIPC));
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_GIPC});
		}
	}
	
	/**
	 * Función encargada de validar la obligatoriedad de la primera parte de los parametros de entrada de las funciones VtxTpfpu y VtxVepu
	 * @param fnac Fecha de Nacimiento.
	 * @param poliza Póliza
	 * @param subpoliza Subpóliza
	 * @param certificado Certificado
	 * @param nsuscripcion	Suscripcion
	 * @param x Edad actuarial a fecha de efecto.
	 * @param tc Anualidad del cálculo.
	 * @param n Duración del seguro.
	 * @param prg Porcentaje de la revalorización geométrica.
	 * @param lstValoresTabMort Valores de la tabla de mortalidad.
	 */
	public static void validarParamEntradaFuncionesVtxTpfpuVtxVepuParte1(final Timestamp fnac, final Long poliza, final Integer subpoliza, final Integer certificado,
			final Integer nsuscripcion, final BigDecimal x, final Integer tc, final Integer n, final BigDecimal prg, final List<BigDecimal> lstValoresTabMort){
		
		if (null == fnac) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_FEC_NACI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_FEC_NACI});
		}
		
		if (null == poliza){
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_POLIZA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_POLIZA});
		}
		
		if (null == subpoliza){
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_SUBPOLIZA));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_SUBPOLIZA});
		}
		
		if (null == certificado){
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CERTIFICADO));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CERTIFICADO});
		}
		
		if (null == nsuscripcion){
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_NSUSCRIPCION));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_NSUSCRIPCION});
		}
		
		if (null == x) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARX));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARX});
		}

		if (null == tc) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VAR_TC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VAR_TC});
		}
		
		if (null == n) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARN});
		}
		
		if (null == prg){
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PRG));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PRG});
		}
		
		if (null == lstValoresTabMort || lstValoresTabMort.isEmpty()) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VALORES_TM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VALORES_TM});
		}
		
	}
	
	/**
	 * Función encargada de validar la obligatoriedad de la segunda parte de los parametros de entrada de las funciones VtxTpfpu y VtxVepu
	 * @param m Número de años en que aplicamos un primer interés técnico.
	 * @param i1 Primer Interés técnico.
	 * @param i2 Segundo Interés técnico.
	 * @param icapini Capital inicial de la umic recibido en el maestro.
	 * @param iprimanetaini Prima neta inicial, recibida en el maestro.
	 * @param pgastgesin1 GI porcentaje de gastos sobre el capital. Recibido en el maestro en tanto por 100.
	 * @param pgastgesin2 GIPC, porcentaje de gastos sobre la prima. Recibido en el maestro en tanto por 100.
	 * @param criterioFecha
	 */
	public static void validarParamEntradaFuncionesVtxTpfpuVtxVepuParte2(final Integer m, final BigDecimal i1, final BigDecimal i2, final BigDecimal icapini, final BigDecimal iprimanetaini,
			final BigDecimal pgastgesin1, final BigDecimal pgastgesin2, final String criterioFecha){
		
		if (null == m) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARM));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARM});
		}
		
		if (null == i1) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI1});
		}
		
		if (null == i2) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_VARI2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_VARI2});
		}
		
		if (null == icapini){
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CAPITAL_INI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CAPITAL_INI});
		}
		
		if (null == iprimanetaini){
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_IPRIMANETA_INI));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_IPRIMANETA_INI});
		}
		
		if (null == pgastgesin1){
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PGASTGESIN_1));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PGASTGESIN_1});
		}
		
		if (null == pgastgesin2){
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_PGASTGESIN_2));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_PGASTGESIN_2});
		}
		
		if (null == criterioFecha || criterioFecha.isEmpty()) {
			if (ValidacionesTerminales.LOG.isDebugEnabled()) {
				ValidacionesTerminales.LOG.debug(Util.errorValidacionA1(ConstantsFunciones.CTE_CRITERIO_FEC));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A1, new String[]{ConstantsFunciones.CTE_CRITERIO_FEC});
		}
	}

}
