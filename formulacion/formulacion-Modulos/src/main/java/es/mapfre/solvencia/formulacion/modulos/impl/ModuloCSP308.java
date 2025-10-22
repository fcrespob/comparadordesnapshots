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
import es.mapfre.solvencia.formulacion.util.FuncionesVBX;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada del cálculo que devuelve el capital de fallecimiento para productos Flexibles a Prima única.
 * La expresión matemática para su determinación es la siguiente:
 * 		CSP (308,tc) = PUCCAPcol (fcal)
 * @author apedro
 *
 */
public class ModuloCSP308 implements Modulo {

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP308.class);
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP308;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_MODBETA = ConstantsModulos.CTE_VA_MODBETA.concat(CLAVE_MODULO);

	private static final String CLAVE_VAR_ANT_RENOVA = ConstantsModulos.CTE_VAR_ANT_RENOVA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PROX_RENOVA = ConstantsModulos.CTE_VAR_PROX_RENOVA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_NR = ConstantsModulos.CTE_VAR_NR.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 
	// Fin de las variables estáticas usadas para agilizar operaciones.

	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}
	
	/**
	 * Función encargada de obtener los parámetros necesarios y de realizar la llamada a la función que realiza los calculos del modulo.
	 */
	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {
		//Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		//Fin variables locales
		
		try {
			if (ModuloCSP308.LOG.isTraceEnabled()) {
				ModuloCSP308.LOG.trace("Inicio de execute en clase ModuloCSP308");
			}
			
			//Inicio recuperación de datos que le pasaremos a la función moduloCSP308
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			//Fin de la recuperación de los datos que se pasarán a la función moduloCSP308.
			
			//Invocamos a la función de calculo CSP308
			resultado = moduloCSP308(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloCSP308.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP308.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP308.LOG.isTraceEnabled()) {
			ModuloCSP308.LOG.trace("Fin de execute en clase ModuloCSP308");
		}
		
		return resultado;

	}

	/** 
	 * Modulo de cálculo que devuelve el capital de fallecimiento para productos Flexibles a Prima única.
	 * 
	 * @param proyUmic
	 * 			Estructura detalleCorrientes de la umic
	 * @param bloqueCorriente
	 * 			Bloque de Trabajo de la corriente
	 * @param iteracion
	 * 			Indica el periodo de proyección J que se está calculando de entre todos los periodos de proyección de la umic (proyUmic)
	 * @param fcalc
	 * 			Fecha de calculo
	 * @param umic
	 * 			Contiene los datos de la Umic que se está procesando
	 * @param btcUmic
	 * 			Contiene el detalle de la base técnica de cálculo para la umic
	 * @param mapVariables 
	 * 			mapa con las variables de memoria necesarias
	 */
	private BigDecimal moduloCSP308(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables locales
		String varCriterFecha = ConstantsFunciones.CTE_CADENA_VACIA;
		String varBeta = ConstantsFunciones.CTE_CADENA_VACIA;
		Timestamp varfecFin;
		BigDecimal varN;
		Integer varNR;
		BigDecimal varI1Bti;
		BigDecimal varI2Bti;
		BigDecimal varPU;
		BigDecimal varM;
		Integer varNRM;
		Timestamp varAntRenova;
		Timestamp varProxRenova;
		Timestamp varFechaEfecto = null;
		BigDecimal varB1 = BigDecimal.ZERO;
		BigDecimal varB2 = BigDecimal.ZERO;
		BigDecimal varPUCCAPcol;
		BigDecimal csp308 = BigDecimal.ZERO;
		//Fin variables locales
		
		if (ModuloCSP308.LOG.isTraceEnabled()) {
			ModuloCSP308.LOG.trace("Inicio función << moduloCSP308 >> de la clase ModuloCSP308, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if ( null == bloqueCorriente.getFechaDevengo() ) {
			return csp308;
		}
		
		
		// Cálculo y validación de las variables de apoyo.  
		varCriterFecha = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);	
		varBeta = UtilModulos.getsetStringRecuperarDefinicionAuxiliar(mapVariables, CLAVE_VAR_MODBETA, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), ConstantsModulos.CTE_VA_MODBETA);
		
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			// Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterFecha);
			ValidacionesComunesModulos.validarVariableDeApoyoVarModBeta(varBeta);
			// Fin de la validación de las variables de apoyo.
		}
		
		//Variables Modulo
		varfecFin = umic.getFechas().getFecefecfin();
		if (varfecFin == null){
			varfecFin = proyUmic.get(proyUmic.size()-1).getFechaDesde();
		}
		
		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		varN = FuncionesAuxiliares.nAnnos(varFechaEfecto, varfecFin, varCriterFecha);
		varNR = (Integer) mapVariables.get(CLAVE_VAR_NR);
		if (varNR == null){
			varNR = umic.getDuraciones().getNrenovaciones();
			mapVariables.put(CLAVE_VAR_NR, varNR);
		}
		
		varI1Bti = btcUmic.getItcalc().get(0).multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
		varI2Bti = btcUmic.getItcalc().get(1).multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
		varPU = umic.getPrimas().getIprimanetaini();
		varM = FuncionesAuxiliares.nAnnos(varFechaEfecto, umic.getBti().getFecFinTramo1(), varCriterFecha);
		varNRM = varM.intValue();
		
		varAntRenova = UtilModulos.getVarAntRenova(mapVariables, CLAVE_VAR_ANT_RENOVA, umic.getFechas().getFecdesderenova());
		varProxRenova = UtilModulos.getVarProxRenova(mapVariables, CLAVE_VAR_PROX_RENOVA, umic.getFechas().getFechastarenova());
		//Fin Variables Modulo
		
		//VarBeta - indica que tipo de modalidad es para calcular varB1 y varB2
		if (varBeta.equals("CRP")){
			varB1 = varI1Bti;
			varB2 = varI2Bti;
		} else if (varBeta.equals("CRPI")){
			varB1 = BigDecimal.ZERO;
			varB2 = BigDecimal.ZERO;
		} else if (varBeta.equals("SR")){
			varB1 = varI1Bti.add(BigDecimal.ONE);
			varB2 = varI2Bti.add(BigDecimal.ONE);
		}
		
		if (proyUmic.get(iteracion-1).getFechaDesde().after(varAntRenova) && 
				!proyUmic.get(iteracion-1).getFechaDesde().before(varProxRenova)){
			varAntRenova = varProxRenova;
			varProxRenova = UtilFechas.incrAnyo(varProxRenova, 1);
			varNR = varNR + 1;
			mapVariables.put(CLAVE_VAR_ANT_RENOVA, varAntRenova);
			mapVariables.put(CLAVE_VAR_PROX_RENOVA, varProxRenova);
			mapVariables.put(CLAVE_VAR_NR, varNR);
		}
		
		varPUCCAPcol = FuncionesVBX.puccapcol(varPU, varN, varI1Bti, varI2Bti, varNR, varNRM, varM, varB1, varB2, proyUmic.get(iteracion-1).getFechaDesde(), umic.getBti().getFecFinTramo1(), mapVariables, CLAVE_MODULO);
		
		csp308 = varPUCCAPcol;
		
		if (ModuloCSP308.LOG.isTraceEnabled()) {
			ModuloCSP308.LOG.trace("Fin función << moduloCSP308 >> de la clase ModuloCSP308, para la iteracion = {}, con resultado csp308 = {}", iteracion, csp308);
		}
		
		return csp308;
	}
	
}
