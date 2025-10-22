package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.FuncionesVBX;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada del cálculo que devuelve la cuantía nominal en cada momento de la Umic.
 * La expresión matemática para su determinación es la siguiente:
 * 			CSP(362,tc)= PUCCAPdifer(TC,gamma)
 * 
 * @author agonzalezgar
 *
 */
public class ModuloCSP362 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP362.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP362;
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_MODBETA = ConstantsModulos.CTE_VA_MODBETA.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_NR = ConstantsModulos.CTE_VAR_NR.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PROXRENOVA = ConstantsModulos.CTE_VAR_PROXRENOVA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TCY = ConstantsModulos.CTE_VAR_TCY.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TCY1 = ConstantsModulos.CTE_VAR_TCY1.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_PUCCAPDIFER = ConstantsModulos.CTE_VAR_PUCCAPDIFER.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_NRM = ConstantsModulos.CTE_VAR_NRM.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_M = ConstantsModulos.CTE_VAR_M.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_DIFER_COL = ConstantsModulos.CTE_VAR_DIFER_COL.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 
	
	// Fin de las variables estáticas usadas para agilizar operaciones.
	
	// Variables usadas para mejorar el rendimiento en casos puntuales.
	private static final BigDecimal CTE_0_PUNTO_01 = ConstantsFunciones.CTE_OPER_0_PUNTO_01;
	

	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}
	
	/* (non-Javadoc)
	 * @see es.mapfre.solvencia.formulacion.modulos.Modulo#execute(java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {
		//Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		//Fin variables locales
		
		try {
			if (ModuloCSP362.LOG.isTraceEnabled()) {
				ModuloCSP362.LOG.trace("Inicio de execute en clase ModuloCSP362");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloCSP362
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			// Fin de la recuperación de los datos.
			
			//Invocamos a la función de calculo CSP362
			resultado = moduloCSP362(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloCSP362.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP362.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP362.LOG.isTraceEnabled()) {
			ModuloCSP362.LOG.trace("Fin de execute en clase ModuloCSP362");
		}
		
		return resultado;
	}
	
	/**
	 * Modulo de cálculo que devuelve la cuantía nominal en cada momento de la Umic.
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
	private BigDecimal moduloCSP362(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables locales
		String varApoyoBeta = ConstantsFunciones.CTE_CADENA_VACIA;
		String varCriFec = ConstantsFunciones.CTE_CADENA_VACIA;
		BigDecimal csp362 = BigDecimal.ZERO;
		BigDecimal varI1Bti;
		BigDecimal varI2Bti;
		BigDecimal varPU;
		int varNR;
		BigDecimal varB1 = BigDecimal.ZERO;
		BigDecimal varB2 = BigDecimal.ZERO;
		Timestamp varFechaEfecto;
		//Fin variables locales
		
		if (ModuloCSP362.LOG.isTraceEnabled()) {
			ModuloCSP362.LOG.trace("Inicio función << moduloCSP362 >> de la clase ModuloCSP362, para la iteracion = {}", iteracion);
		}
			
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		/**
			 La llamada a las funciones de apoyo no cambia con cada periodo. Sin embargo, cabe tener en cuenta:
			 	- En la primera iteración se calcularán las variables de apoyo y se setearán en el hashmap.
			 	- En las iteraciones posteriores los mismos métodos usados para setear devolverán los valores del Hashmap.
				Variables de Apoyo
				-	VarBeta --> obtenerConfiguracion.recuperarVariableApoyo(MOD_BETA)
				-	VarCriterFec --> obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
				-	Si alguna de las variables de apoyo  retornadas es nulo se devuelve error funcional 005 - 
					No se ha encontrado la Variable de Apoyo &NombreVariableApoyo, finalizando el proceso para la UMIC.
				Variables Módulo
				-	varI1Bti--> btcUmic.itcalc1
				-	varI2Bti --> btcUmic.itcalc2
				-	varPU --> umic.primas.iprimanetaini 
				-	varM = nanos(btcUmic.fecFinTramo1, btcUmic.fecIniTramo1, VarCriterFec)
				-	varNRM = parte entera (nanos(umic.fechas.fecinisus, btcUmic.fecFinTramo1, VarCriterFec))
				-	varNR = umic.duraciones.nrenovaciones
				-	varDifercol = nanos(umic.fechas.fecinisus,umic.renta.fecini, VarCriterFec)
				-	Si varDifercol > 1 -->  Se sobreescribirá la variable con varDifercol = 1
				-	varFinicio = umic.rentas.fecini – 1 dia
				-	VarBeta =  --> me dirá  que tipo de modalidad es para calcular varB1 y varB2 
				-	VarBeta   --> me dice  que tipo de modalidad es para calcular varB1 y varB2
					o	Si VarBeta  = CRP
						-	varBeta1 = varI1Bti 
						-	varBeta2= varI2Bti
					o	Si VarBeta  = CRPI
						-	varBeta1 = 0
						-	varBeta2= 0
					o	Si VarBeta  = SR
						-	varBeta1 = 1 + varI1Bti
						-	varBeta2= 1 + varI2Bti
				-	varTCY = nanos (umic.fechas.fecinisus, fcalc, VarCriterFec)
				varPUCCAPdifer = PUCCAPdifer (varPU, varI1Bti ,varI2Bti ,varNR ,varNRM ,varM ,varB1 ,varB2, varDifercol ,varTCY)
				Para cualquier periodo j (j >= 1)  se calculará el capital en el periodo como se describe a continuación: 
				(Se calculará hasta la Fecha Fin Cobertura de Riesgo, que en este caso es la fecha de inicio del pago de renta (dia excluído))
				-	Si periodosUmic(j).fechaDesde  <= varFinicio
				 	CSP362(j) =  varPUCCAPdifer
				-	Si periodosUmic(j).fechaDesde > varFinicio
				            CSP362(j) =  0
				Finalmente asignaremos la cuantía nominal del periodo como: 
				proyUmic(j).impFlujoNominal = CSP362 (j)
			 */
		// Cálculo de las variables de apoyo.
		varCriFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);		
		varApoyoBeta = UtilModulos.getsetStringRecuperarDefinicionAuxiliar(mapVariables, CLAVE_VAR_MODBETA, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), ConstantsModulos.CTE_VA_MODBETA);
		// Fin del cálculo de las variables de apoyo.
		
		// Validación de las variables de apoyo.
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriFec);
			ValidacionesComunesModulos.validarVariableDeApoyoVarModBeta(varApoyoBeta);
		} // Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
		// Fin de la validación de las variables de apoyo.		
			
		if(bloqueCorriente.getFechaDevengo() == null) {
			return csp362;
		}
		
		if (umic.getRentas().getFecIni() == null) {
			if(ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
					// Solo se almacena una incidencia 
				final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();
				Incidencia aviso = Solvencia2ExcepcionHelper.crearAviso(btcUmic.getBaseTec(), umic.getDatosGenerales().getCcanal(), umic.getDatosGenerales().getCcartera(), umic.getKey(), btcUmic.getFecCierre(), umic.getDatosGenerales().getCnegocio(), this.getNombreServicio(), ConstantsFunciones.CTE_COD_ERROR_I001, ArrayUtils.EMPTY_OBJECT_ARRAY);
				servicio.almacenarIncidencias(aviso);									
			}			
			return csp362;			
		}
		
		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		varI1Bti = btcUmic.getItcalc().get(0);
		varI2Bti = btcUmic.getItcalc().get(1);
		varPU = umic.getPrimas().getIprimanetaini();
		varNR = UtilModulos.getVarNR(mapVariables, CLAVE_VAR_NR, umic.getDuraciones().getNrenovaciones());
		
		final Timestamp fecIniTramo1 = btcUmic.getFecInitramo().get(0);
		final Timestamp fecFinTramo1 = btcUmic.getFecfintramo().get(0);
		
		if (ConstantsModulos.CTE_CRP.equals(varApoyoBeta)) { 
			varB1 = varI1Bti.multiply(CTE_0_PUNTO_01, ConstantsFunciones.MATH_CONTEXT);
			varB2 = varI2Bti.multiply(CTE_0_PUNTO_01, ConstantsFunciones.MATH_CONTEXT);
		} else if (ConstantsModulos.CTE_CRPI.equals(varApoyoBeta)) {
			varB1 = BigDecimal.ZERO;
			varB2 = BigDecimal.ZERO;
		} else if (ConstantsModulos.CTE_SR.equals(varApoyoBeta)) {
			varB1 = BigDecimal.ONE.add((varI1Bti).multiply(CTE_0_PUNTO_01, ConstantsFunciones.MATH_CONTEXT));
			varB2 = BigDecimal.ZERO.add((varI2Bti).multiply(CTE_0_PUNTO_01, ConstantsFunciones.MATH_CONTEXT));
		}
		
		final DetalleCorriente proyUmicJ = proyUmic.get(iteracion - 1);

		// Se almacena el valor de varProxRenova (si ya está almacenado en el HashMap lo recupera).
		Timestamp varProxRenova = UtilModulos.getVarProxRenova(mapVariables, CLAVE_VAR_PROXRENOVA, umic.getFechas().getFechastarenova());
		
		BigDecimal varTCy = UtilModulos.getVarTCy(mapVariables, CLAVE_VAR_TCY, varFechaEfecto, fcalc, varCriFec);
		
		if (ConstantsModulos.CTE_FIRST_ITER == iteracion ) {		
			csp362 = UtilModulos.setVarPUCCAPdifer(mapVariables, CLAVE_VAR_PUCCAPDIFER, FuncionesVBX.puccapDifer(varPU, varI1Bti, varI2Bti, varNR,
					UtilModulos.getVarNRM(mapVariables, CLAVE_VAR_NRM, varFechaEfecto, fecFinTramo1, varCriFec),
					UtilModulos.getVarMCSP362(mapVariables, CLAVE_VAR_M, fecFinTramo1, fecIniTramo1, varCriFec), varB1, varB2,
					UtilModulos.getVarDiferCol(mapVariables, CLAVE_VAR_DIFER_COL, BigDecimal.ONE, varFechaEfecto, umic.getRentas().getFecIni(), varCriFec),
					varTCy));			
		} else {
			//Si proyUmic(j).fechaDesde  < fecRentaIni
				if (proyUmicJ.getFechaDesde().before(umic.getRentas().getFecIni())) {
					//Si proyUmic(j).fechaDesde  <= varProxRenova < proyUmic(j).fechaHasta 
					if (!varProxRenova.before(proyUmicJ.getFechaHasta())) {
						csp362 = UtilModulos.getVarPUCCAPdifer(mapVariables, CLAVE_VAR_PUCCAPDIFER, BigDecimal.ZERO);
					} else {
						// Incrementa var_ProxRenova en UNO (también lo almacena en el HashMap).
						varProxRenova = UtilModulos.incrementaVarProxRenova(mapVariables, CLAVE_VAR_PROXRENOVA);
						// Incrementa VarNR en uno (también lo almacena en el HashMap).
						varNR = UtilModulos.incrementaVarNR(mapVariables, CLAVE_VAR_NR);
						BigDecimal varTCy1 = UtilModulos.setVarTCy1(mapVariables, CLAVE_VAR_TCY1, varTCy.add(FuncionesAuxiliares.nAnnos(fcalc, varProxRenova, varCriFec)));				
						
						csp362 = UtilModulos.setVarPUCCAPdifer(mapVariables, CLAVE_VAR_PUCCAPDIFER, FuncionesVBX.puccapDifer(varPU, varI1Bti, varI2Bti, varNR,
								UtilModulos.getVarNRM(mapVariables, CLAVE_VAR_NRM, varFechaEfecto, fecFinTramo1, varCriFec),
								UtilModulos.getVarMCSP362(mapVariables, CLAVE_VAR_M, fecFinTramo1, fecIniTramo1, varCriFec), varB1, varB2,
								UtilModulos.getVarDiferCol(mapVariables, CLAVE_VAR_DIFER_COL, BigDecimal.ONE, varFechaEfecto, umic.getRentas().getFecIni(), varCriFec),
								varTCy1));				
					}
				}
		}
		
		if (ModuloCSP362.LOG.isTraceEnabled()) {
			ModuloCSP362.LOG.trace("Fin función << moduloCSP362 >> de la clase ModuloCSP362, para la iteracion = {}, con resultado csp362 = {}", iteracion, csp362);
		}
		
		return csp362;
	}
}
