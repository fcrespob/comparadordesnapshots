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
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * 
 * @author Szilard Toth
 *
 */

public class ModuloPRI007RSR implements Modulo {
	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloPRI007RSR.class);
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_PRI007RSR;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);

	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC;
	private static final String CLAVE_VAR_FEC_EFEC = CLAVE_FEC_EFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_TC = ConstantsModulos.CTE_VAR_TC;
	private static final String CLAVE_VAR_TC = CLAVE_TC.concat(CLAVE_MODULO);
	
	// Fin de las variables estáticas usadas para agilizar operaciones.
	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}

	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {
		// Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		// Fin variables locales
		try {
			if (ModuloPRI007RSR.LOG.isTraceEnabled()) {
				ModuloPRI007RSR.LOG.trace("Inicio de execute en clase ModuloPRI007RSR");
			}
			// Recuperamos los datos que le pasaremos a la función ModuloPRI007R
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			
			// Invocamos a la función moduloPRI007R
			resultado = moduloPRI007RSR(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso,
					mapVariables);
		} catch (Solvencia2Excepcion e) {
			ModuloPRI007RSR.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloPRI007RSR.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		if (ModuloPRI007RSR.LOG.isTraceEnabled()) {
			ModuloPRI007RSR.LOG.trace("Fin de execute en clase ModuloPRI007RSR");
		}
		return resultado;
	}

	/**
	 * 
	 * Usaremos este módulo cómo módulo de prima de fallecimiento en
	 * renovaciones para la garantía complementaria.
	 * 
	 * @param proyUmic
	 *            Estructura detalleCorrientes de la umic
	 * @param bloqueCorriente
	 *            Bloque de Trabajo de la corriente
	 * @param iteracion
	 *            Indica el periodo de proyección J que se está calculando de
	 *            entre todos los periodos de proyección de la umic (proyUmic)
	 * @param fcalc
	 *            Fecha de calculo
	 * @param umic
	 *            Contiene los datos de la Umic que se está procesando
	 * @param btcUmic
	 *            Contiene el detalle de la base técnica de cálculo para la umic
	 * @param codSubproceso
	 *            Código del subproceso que se está ejecutando.
	 * @param mapVariables
	 *            mapa con las variables de memoria necesarias
	 */
	@SuppressWarnings({ "unused" })
	private BigDecimal moduloPRI007RSR(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente,
			final Integer iteracion, final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic,
			final String codSubproceso, final Map<String, Object> mapVariables) {
		
		// variables locales
		BigDecimal pri007SR = BigDecimal.ZERO;
		BigDecimal varPNA0 = BigDecimal.ZERO;
		BigDecimal varPRP = BigDecimal.ZERO;
		BigDecimal varRPF = BigDecimal.ZERO;
		BigDecimal varFPDecimal = BigDecimal.ZERO;
		BigDecimal varPDecimal = BigDecimal.ZERO;
		String varCriterFec;
		String varFormaPago;
		Integer varFP = 0;
		Integer varTC = 0;
		Integer varP = 0;
		Timestamp varFecRenova = null;
		Timestamp varFechaEfecto = null;
		// Fin variables locales
		
		if (ModuloPRI007RSR.LOG.isTraceEnabled()) {
			ModuloPRI007RSR.LOG.trace(
					"Inicio de la función << moduloPRI007RSR >> de la clase ModuloPRI007RSR, para la iteración = {}",
					iteracion);
		}
		// Validamos los parametros de entrada

		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		

		/**
		 * Si estoy en el primer periodo, desde la umic se recuperan los datos
		 * necesarios para el cálculo que no varíen en función del periodo, asi
		 * como las variables internas que tampoco varíen por periodo, y que se
		 * dejarán accesibles para su uso en el subproceso por los siguientes
		 * periodos a calcular.
		 * 
		 * Si umic.primas.cformpago = 9 no se calculará nada, acabando así el
		 * módulo para la umic. Si Si umic.primas.cformpago <> 9:
		 * 
		 * Variables de Apoyo
		 * 
		 * VarCriterFec
		 * obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL) Si la
		 * variable de apoyo retornada es nula se devuelve error funcional A6 -
		 * No se ha encontrado la Variable de Apoyo & ID-TEMPORAL, finalizando
		 * el proceso para la UMIC. Variables Módulo
		 * 
		 * varPNA0 = umic.primas.iprimanetaini  dejo la variable en memoria
		 * disponible para el procesado de la umic. varPRP =
		 * umic.primas.prevprima dejo la varible en momoria disponible para el
		 * procesado de la umic. VarRPF = umic.primas.precargfrac  dejo la
		 * variable en memoria disponible para el procesado de la umic.
		 * VarFormaPago = umic.primas.cformpago dejo la variable en memoria
		 * disponible para el procesado de la umic. Si VarFormaPago = 1 varFP =
		 * 1 Si VarFormaPago = 2 varFP = 2 Si VarFormaPago = 3 varFP = 4 Si
		 * VarFormaPago = 4 varFP = 12
		 * 
		 * varFecRenova = umic.fechas.fdesderenova dejo la variable en memoria
		 * procesado de la umic.
		 * 
		 * Si la póliza no está reducida, es decir,
		 * umic.datosGenerales.csitupol= ‘VI’ varfechaEfecto = umic.fechas.
		 * fecinisus;
		 * 
		 * Si la póliza está reducida, es decir, umic.datosGenerales.csitupol=
		 * ‘RE’ varfechaEfecto = umic.fechas.fecefecred Si
		 * umic.fechas.fecefecred es nula y umic.datosGenerales.cnegocio = ‘C’:
		 * varfechaEfecto = umic.fechas.fecfinpagprim
		 * 
		 * Para cualquier periodo j se calculará:
		 * 
		 * Si proyUmic(j).varBloque.fecPago = null
		 * 
		 * PRI007R = 0
		 * 
		 * Si proyUmic(j).varBloque.fecPago <> null Si
		 * proyUmic(j).varBloque.fecPago >= umic.fechas.fecefecfin
		 * 
		 * PRI007R = 0
		 * 
		 * En caso contrario: Si proyUmic(j).fecDesde < umic.fechas.fecefecfin
		 * 
		 * varTc = TC(varfechaEfecto, proyUmic(j).varBloque.fecPago) 
		 * sobreescribo la variable en memoria, disponible para el subproceso.
		 * 
		 * Si mes(proyUmic(j). varBloque.fecPago) = Mes(varFecRenova) varP = 1
		 * En caso contrario: varP = 0
		 * 
		 * PRI007R = varPNA0 * (1+ VarPRP/100)^varTC*(1+
		 * VarRPF/100)*(VarP/VarFP)
		 * 
		 */
		
		if(umic.getPrimas().getCformpago() != ConstantsModulos.CTE_CFORMPAG_UNICA){
			
			// Calculo de la variable de apoyo varCriterFec
			varCriterFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);
			
			// Variables módulo
			varPNA0 = umic.getPrimas().getIprimanetaini();
			varPRP = umic.getPrimas().getPrevprima();
			varFormaPago = umic.getPrimas().getCformpago();
			
			Map<Integer, Integer> fp = ConstantsFunciones.FORPAGRENT;
			varFP= fp.get(Integer.valueOf(varFormaPago));
			
			varFecRenova = umic.getFechas().getFecdesderenova();
			
			varFechaEfecto = UtilModulos.getVarFechaEfecto(mapVariables, CLAVE_VAR_FEC_EFEC, umic);
		
			if(bloqueCorriente.getFechaPago() == null){
				pri007SR = BigDecimal.ZERO;
			} else {
				if(!bloqueCorriente.getFechaPago().before(umic.getFechas().getFecefecfin()) || 
						bloqueCorriente.getFechaPago().after(umic.getFechas().getFecfinpagprim())){
					pri007SR = BigDecimal.ZERO;
				} else {
						
					varTC = UtilModulos.getVarTC(mapVariables, CLAVE_VAR_TC, varFechaEfecto, bloqueCorriente.getFechaPago());
				
					if(UtilFechas.getMes(bloqueCorriente.getFechaPago()) == UtilFechas.getMes(varFecRenova)){
						varP = 1;
					} else {
						varP = 0;
					}
					
				}
			}
			
			varPDecimal = BigDecimal.valueOf(varP);
			varFPDecimal = BigDecimal.valueOf(varFP);
			pri007SR = varPNA0.multiply((BigDecimal.ONE.add(varPRP.divide(ConstantsFunciones.CTE_OPER_100,ConstantsFunciones.MATH_CONTEXT))).pow(varTC).multiply(varPDecimal.divide(varFPDecimal,ConstantsFunciones.MATH_CONTEXT)));
		}else {
			if (btcUmic.getBt().equals(ConstantsModulos.CTE_BT_N17LIRIN)) {
				pri007SR = umic.getPrimas().getIprimanetaini();
			}
		}

		if (ModuloPRI007RSR.LOG.isTraceEnabled()) {
			ModuloPRI007RSR.LOG.trace(
					"Fin de la función << moduloPRI007RSR >> de la clase ModuloPRI007RSR, para la iteración = {}",
					iteracion);
		}
		return pri007SR;
	}
}


