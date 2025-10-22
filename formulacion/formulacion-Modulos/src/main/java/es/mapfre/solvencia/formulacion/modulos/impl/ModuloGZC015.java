package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
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
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;


/**
 * Clase que implementa el modulo GZC015.
 * Este módulo es el módulo de cálculo de la cuantía nominal de gastos de 
 * prima periódica cuando estas giran sobre primas temporales. 
 *
 */
public class ModuloGZC015 implements Modulo {

	private static final Logger LOG = LoggerFactory.getLogger(ModuloGZC015.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_GZC015;
	private static final String CLAVE_NPP = ConstantsModulos.CTE_VA_NPP;
	private static final String CLAVE_VAR_NPP = CLAVE_NPP.concat(CLAVE_MODULO);
	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_M = ConstantsModulos.CTE_VAR_M.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_X = ConstantsModulos.CTE_VAR_X.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_W = ConstantsModulos.CTE_VAR_W.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_RENT_GEO_2IT = ConstantsModulos.CTE_VAR_RENT_GEO_2IT.concat(CLAVE_MODULO);
	
	@Override
	public String getNombreServicio() {
		
		return CLAVE_MODULO;
	}

	/**
	 * Función encargada de la cuantía nominal de gastos de 
	 * prima periódica cuando estas giran sobre primas temporales.
	 */
	@SuppressWarnings("unchecked")
	public Object execute(Object... args) throws Solvencia2Excepcion {

		BigDecimal resultado = BigDecimal.ZERO;
		
		try {
			if (ModuloGZC015.LOG.isTraceEnabled()) {
				ModuloGZC015.LOG.trace("Inicio de execute en clase ModuloGZC015");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloGZC001
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			
			resultado = moduloGZC015(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloGZC015.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloGZC015.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
	
		if (ModuloGZC015.LOG.isTraceEnabled()) {
			ModuloGZC015.LOG.trace("Fin de execute en clase ModuloGZC015");
		}
		
		return resultado;
	}

	/**
	 * Usaremos este módulo cómo módulo de cálculo de la cuantía nominal de gastos de 
	 * prima periódica cuando estas giran sobre primas temporales.
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
	private BigDecimal moduloGZC015(final List<DetalleCorriente> proyUmic       ,
			                        final BloqueCorriente        bloqueCorriente, 
			                        final int                    iteracion      ,
			                        final Timestamp              fcalc          , 
			                        final Umic                   umic           , 
			                        final DetalleBaseTecnica     btcUmic        , 
			                        final String                 codSubproceso  ,
			                        final Map<String, Object>    mapVariables   ) {
		
		BigDecimal             gzc015 = BigDecimal.ZERO;
		BigDecimal             varGipc;
		BigDecimal             varNpp;
		List<DetalleCorriente> varProyNominal;
		String                 varCriterEdad;
		Timestamp              varfechaEfecto;
		BigDecimal             varI1;
		BigDecimal             varI2;
		Timestamp			   varfecfintramo1;
		String                 varCriterFec;
		BigDecimal             varM;
		Integer                varAnoNac;
		BigDecimal             varX;
		Integer                varEdifer;
		Integer                varW;
		String                 varTabMort;
		List<BigDecimal>	   varValoresTabMort;
		Integer                varN;
		BigDecimal             varRentgeo2it;
		Modulo                 moduloPRI005;
		BigDecimal             varPRI005j;
		Timestamp              varFecVcto;
		BigDecimal varFactor1,varFactor2;
						   
		
		if (ModuloGZC015.LOG.isTraceEnabled()) {
			ModuloGZC015.LOG.trace("Inicio función << moduloGZC015 >> de la clase ModuloGZC015, para la  iteracion = {}", iteracion);
		}
		
		/**
		 * Se realizará la validación de los parámetros de entrada marcados como obligatorios. 
		 * 	En caso de error se devuelve error funcional 001 - Parámetro obligatorio no informado &NombreAtributoEntrada.
		 * 
		 * Si estoy en el primer periodo, desde la umic se recuperan los datos necesarios para el cálculo que no varíen en función del periodo, asi como las variables internas que tampoco varíen por periodo, y que se dejarán accesibles para su uso en el subproceso por los siguientes periodos a calcular. 
		 * 
		 * Variables Módulo
		 * 
		 * 	VarGipc = btcUmic.gtoRosspPrima
		 * 	VarNpp = obtenerConfiguracion.recuperarVariableApoyo(NPP)
		 * Si  VarNpp así obtenido es nulo, se asignará VarNpp = 12
		 * 	varProyNominal = proyUmic
		 * 	VarCriterEdad  obtenerConfiguracion.recuperarVariableApoyo(ID-CRITERIO)
		 * 
		 * 	Si la póliza no está reducida, es decir  umic.datosGenerales.csitupol=  ‘VI’
		 * 
		 * 	Si umic.datosGenerales.cnegocio = ‘I’ 
		 * 	varfechaEfecto = umic.fechas.fecefecIni
		 * 	En caso contrario: 
		 * 	varfechaEfecto = umic.fechas.fecinisus
		 * 
		 * 	Si la póliza está reducida, es decir  umic.datosGenerales.csitupol=  ‘RE’
		 * 	varfechaEfecto = umic.fechas.fecefecred
		 * 
		 * 	Si umic.fechas.fecefecred es nula y umic.datosGenerales.cnegocio = ‘C’:
		 * 	varfechaEfecto = umic.fechas.fecfinpagprim
		 * 	varI1= btcUmic.itcalc1
		 * 	varI2= btcUmic.itcalc2
		 * 	varfecfintramo1 = btcUmic.fecFinTramo1
		 * 	varM = nanos (varfechaEfecto, varfecfintramo1, VarCriterFec)
		 * 	varAnoNac = Año (umic.asegurados.fnacAseg1);
		 * 	varX= nedad(varfechaEfecto, umic.asegurados.fnacAseg1, VarCriterEdad, umic.rentas.fecini)
		 * 	varW = obtenerConfiguracion.recuperarEdadMax (umic.datosgenerales.fecCierre, btcUmic.tabla1Aseg1, varAnoNac, varI1, umic.baseTecIni.psobremort, umic.baseTecIni.priesgo)
		 * 	varTabMort = btcUmic.tabla1Aseg1  Dejo la variable disponible en memoria para el subproceso de la umic.
		 * 	varValoresTabMort= obtenerConfiguracion.recuperarValoresExperiencia(btcUmic.fecCierre, varTabMort, varAnoNac, btcUmic. itcalc1, umic.baseTecIni. psobremort, umic.baseTecIni. priesgo)
		 * 	varN = umic.duraciones.ndursegano
		 * 	varRentgeo_2it = RENTGEO_2IT(varX,0, varN,0, varValoresTabMort, varM, varI1, varI2)
 		 *    
		 * Para cualquier periodo j (j >= 1), se calculará el importe correspondiente al periodo j como: 
		 * 
		 * Se invocará al módulo PRI005.
		 * 
		 * VarPRI005j  = PRI005 (varProyNominal, periodoProyeccion , fcalc, umic,btcUmic,codSubproceso)
		 * 
		 * 	Si proyUmic(j).fecDevengo <= varfecvcto ó proyUmic(j).fecDevengo es nula
		 * 
		 * Gzc015(j)= 0  
		 * 
		 * 	En caso contrario: 
		 * 
		 * 	Gzc015(j)= [(VarGipc *VarPRI005j   )/(varRentgeo_2it )]/varNpp
		 * 
		 */
		
		// Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		// Definición de variables auxiliares para agilizar las operaciones (se llaman varias veces).
		final Integer ccartera = umic.getDatosGenerales().getCcartera();
		final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		final Integer kgarantia = umic.getDatosGenerales().getKgarantia();
		varCriterFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIFEC);
		varEdifer = umic.getDatosGenerales().getEdifer();
		
		// Variables Módulo
		varGipc = btcUmic.getGtorosspPrima();
		varGipc = varGipc.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);																				  
		varNpp = UtilModulos.getVarNpp(mapVariables, CLAVE_VAR_NPP, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_NPP);
		varProyNominal = proyUmic;
		varCriterEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIEDAD);
		varfechaEfecto = UtilModulos.getVarFechaEfecto(mapVariables, CLAVE_FEC_EFEC, umic);
		varI1 = btcUmic.getItcalc().get(0);
		varI2 = btcUmic.getItcalc().get(1);
		varfecfintramo1 = btcUmic.getFecfintramo().get(0);
		varM = UtilModulos.getVarM(mapVariables, CLAVE_VAR_M, varfechaEfecto, varfecfintramo1, varCriterFec);
		varAnoNac = UtilFechas.getAnio(umic.getAsegurados().getFnacAseg1());
		varX = UtilModulos.getVarX(mapVariables, CLAVE_VAR_X, varfechaEfecto, umic.getAsegurados().getFnacAseg1(), varCriterEdad, umic.getRentas().getFecIni(), varEdifer);
		varW = UtilModulos.getVarEdadMaxima2(mapVariables, CLAVE_VAR_W, varAnoNac, umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG1);
		varTabMort = btcUmic.getTablacalc1aseg1();
		varValoresTabMort = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT, umic, btcUmic, 
				IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriterEdad, ConstantesSolvencia.CTE_TABMORT_L);
		varN = umic.getDuraciones().getNdursegano();
		varFactor1 = BigDecimal.ONE.add(varI1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		varFactor2 = BigDecimal.ONE.add(varI2.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		varRentgeo2it = UtilModulos.getVarRentgeo2it(mapVariables, CLAVE_VAR_RENT_GEO_2IT ,varX, 0, varN, BigDecimal.ZERO, varValoresTabMort, varM, varFactor1, varFactor2);
		moduloPRI005 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_PRI005);
		varPRI005j = (BigDecimal) moduloPRI005.execute(varProyNominal, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
		varFecVcto = umic.getFechas().getFecefecfin();
		
		if (!bloqueCorriente.getFechaDevengo().equals(null) &&
				bloqueCorriente.getFechaDevengo().compareTo(varFecVcto) > 0){
			
			gzc015 = BigDecimal.ZERO;
		
		}else{
			
			gzc015 = varGipc.multiply(varPRI005j,ConstantsFunciones.MATH_CONTEXT).divide(varRentgeo2it,ConstantsFunciones.MATH_CONTEXT).divide(varNpp,ConstantsFunciones.MATH_CONTEXT);
		
		}
		if (ModuloGZC015.LOG.isTraceEnabled()) {
			ModuloGZC015.LOG.trace("Fin función << moduloGZC015 >> de la clase ModuloGZC015, para la iteracion = {}, con resultado vbx308 = {}", iteracion, gzc015);
		}
		
		return gzc015;
	}

}
