/**
 * MU-2019-038078:NO APLICACION CORRECTA LIMITES FALLECIMIENTO
 *  Se corrige la condicion del if para que no cancele
 */
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
 * Clase que implementa el modulo PRI003R.
 * Este módulo es el módulo de cálculo de la cuantía nominal de primas. Este módulo es idéntico al 
 * PRI003 que se ha definido e implementado en fases anteriores. Sin embargo, dónde p=1 es en las 
 * renovaciones en lugar de los aniversarios.
 */
public class ModuloPRI003R implements Modulo {

	private static final Logger LOG = LoggerFactory.getLogger(ModuloPRI003R.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_PRI003R;
	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PRP = ConstantsModulos.CTE_VAR_PRP.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_GE = ConstantsModulos.CTE_VAR_GE.concat(CLAVE_MODULO);
	
	@Override
	public String getNombreServicio() {
		
		return CLAVE_MODULO;
	}

	/**
	 * Usaremos este módulo cómo módulo de cálculo de la cuantía nominal de primas.
	 */
	@SuppressWarnings("unchecked")
	public Object execute(Object... args) throws Solvencia2Excepcion {

		BigDecimal resultado = BigDecimal.ZERO;
		
		try {
			if (ModuloPRI003R.LOG.isTraceEnabled()) {
				ModuloPRI003R.LOG.trace("Inicio de execute en clase ModuloPRI003R");
			}
			
			//Recuperamos los datos que le pasaremos a la función ModuloPRI003R
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			
			resultado = moduloPRI003R(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloPRI003R.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloPRI003R.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
	
		if (ModuloPRI003R.LOG.isTraceEnabled()) {
			ModuloPRI003R.LOG.trace("Fin de execute en clase ModuloPRI003R");
		}
		
		return resultado;
	}

	
	/**
	 * Usaremos este módulo cómo módulo de cálculo de la cuantía nominal de primas.
	 * 
	 * @param proyUmic
	 * @param bloqueCorriente
	 * @param iteracion
	 * @param fcalc
	 * @param umic
	 * @param btcUmic
	 * @param codSubproceso
	 * @param mapVariables
	 * @return
	 */
	private BigDecimal moduloPRI003R(final List<DetalleCorriente> proyUmic       , 
			                         final BloqueCorriente        bloqueCorriente, 
			                         final int                    iteracion      ,
			                         final Timestamp              fcalc          , 
			                         final Umic                   umic           , 
			                         final DetalleBaseTecnica     btcUmic        , 
			                         final String                 codSubproceso  ,
			                         final Map<String, Object>    mapVariables   ) {
		
		BigDecimal pri003R = BigDecimal.ZERO;
		
		String     varCriEdad    ;
		String     varCriFec     ;
		BigDecimal varPNA0       ;
		BigDecimal varPRP        ;
		BigDecimal varGE         ;
		Timestamp  varFecRenova  ;
		Timestamp  varFechaEfecto;
		Integer    varTC         ;
		
		/**
		 * Se realizará la validación de los parámetros de entrada marcados como obligatorios. 
		 *	En caso de error se devuelve error funcional 001 - Parámetro obligatorio no informado &NombreAtributoEntrada.
		 *
		 *Si estoy en el primer periodo, desde la umic se recuperan los datos necesarios para el cálculo que no varíen en función del periodo, asi como las variables internas que tampoco varíen por periodo, y que se dejarán accesibles para su uso en el subproceso por los siguientes periodos a calcular. 
		 *
		 *Si umic.primas.cformpago = 9 no se calculará nada, acabando así el módulo para la umic.
		 *Si Si umic.primas.cformpago <>  9: 
		 *Variables de Apoyo
		 *
		 *	VarCriterFec obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
		 *	Si la variable de apoyo  retornada es nula se devuelve error funcional A6 - No se ha encontrado la Variable de Apoyo & ID-TEMPORAL, finalizando el proceso para la UMIC.
		 *
		 *Variables Módulo
		 *
		 *	varPNA0 = umic.primas.iprimanetaini  dejo la variable en memoria disponible para el procesado de la umic.
		 *	varPRP = umic.primas.prevprima dejo la varible en momoria disponible para el procesado de la umic.
		 *	varGE = umic.baseTecIni.pgastgesex1I dejo la variable en memoria 
		 *procesado de la umic.
		 *	varFecRenova = umic.fechas.fdesderenova dejo la variable en memoria 
		 *procesado de la umic.
		 *	Si la póliza no está reducida, es decir,  umic.datosGenerales.csitupol=  ‘VI’
		 *	varfechaEfecto = umic.fechas. fecinisus;
		 *	Si la póliza está reducida, es decir  umic.datosGenerales.csitupol=  ‘RE’
		 *	varfechaEfecto = umic.fechas.fecefecred
		 *	Si umic.fechas.fecefecred es nula y umic.datosGenerales.cnegocio = ‘C’:
		 *	varfechaEfecto = umic.fechas.fecfinpagprim
		 *
		 *Para cualquier periodo j se calculará: 
		 *
		 *	Si proyUmic(j).varBloque.fecPago  = null 
		 *
 		 *           PRI003R = 0
		 *
		 *	Si proyUmic(j).varBloque.fecPago  <> null
		 *	Si proyUmic(j).varBloque.fecPago >= umic.fechas.fecefecfin 
		 *
		 *            		PRI003R = 0
		 *
		 *	En caso contrario: 
		 *	Si proyUmic(j).fecDesde < umic.fechas.fecefecfin
		 *	
		 *	varTC = TC(varfechaEfecto,
		 *proyUmic(j).varBloque.fecPago)
		 *  sobreescribo la variable en memoria, disponible para el subproceso.
		 *
		 *	Si mes(proyUmic(j). varBloque.fecPago) = Mes(varFecRenova)
		 *	varP = 1
		 *	En caso contrario:
		 *	varP = 0
		 *
		 *PRI003R = varPNA0 *  (1+ VarPRP/100)^(varTC )*(1- VarGE/100)* VarP
		 */
		
		
		if (ModuloPRI003R.LOG.isTraceEnabled()) {
			ModuloPRI003R.LOG.trace("Inicio función << moduloPRI003R >> de la clase ModuloPRI003R, para la  iteracion = {}", iteracion);
		}
		
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if (!umic.getPrimas().getCformpago().equals(ConstantsModulos.CTE_CFORMPAG_UNICA)){
			
			// Definición de variables auxiliares para agilizar las operaciones (se llaman varias veces).
			final Integer ccartera = umic.getDatosGenerales().getCcartera();
			final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
			final Integer kgarantia = umic.getDatosGenerales().getKgarantia();
			
			// Cálculo de las variables de apoyo.
			varCriEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIEDAD);
			varCriFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIFEC);
			
			// Validación de las variables de apoyo.
			// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
			if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
				ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriEdad);
				ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriFec);
			}
			
			// Variables de módulo
			varPNA0 = umic.getPrimas().getIprimanetaini();
			varPRP = umic.getPrimas().getPrevprima();
			varGE = umic.getBti().getPgastgesex1I();
			varFecRenova = umic.getFechas().getFecdesderenova();
			
			varFechaEfecto = UtilModulos.getVarFechaEfecto(mapVariables, CLAVE_FEC_EFEC, umic);
			
			// Únicamente calcularemos cuando varP es 1. En cualquier otro caso, la salida será 0
//			if (!bloqueCorriente.getFechaPago().equals(null) && 
			if (null != bloqueCorriente.getFechaPago() && 
			   bloqueCorriente.getFechaPago().before(umic.getFechas().getFecefecfin()) &&
			   proyUmic.get(iteracion - 1).getFechaDesde().before(umic.getFechas().getFecefecfin()) &&
			   UtilFechas.getMes(bloqueCorriente.getFechaPago()) == UtilFechas.getMes(varFecRenova)){
					varTC = FuncionesAuxiliares.tc(varFechaEfecto, bloqueCorriente.getFechaPago());
					pri003R = varPNA0.multiply(UtilModulos.getVarUnoMasPrp(mapVariables, CLAVE_VAR_PRP, varPRP).pow(varTC.intValue()).multiply(UtilModulos.getVarUnoMenosGepc(mapVariables, CLAVE_VAR_GE, varGE)));
			}
		}

		if (ModuloPRI003R.LOG.isTraceEnabled()) {
			ModuloPRI003R.LOG.trace("Fin función << moduloPRI003R >> de la clase ModuloPRI003R, para la iteracion = {}, con resultado vbx308 = {}", iteracion, pri003R);
		}
		
		return pri003R;
		
	}

}
