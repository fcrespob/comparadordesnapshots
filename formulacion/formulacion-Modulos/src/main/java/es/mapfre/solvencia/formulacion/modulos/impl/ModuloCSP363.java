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
import es.mapfre.solvencia.formulacion.util.FuncionesActualizacionFinanciera;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada del cálculo del módulo CSP363.
 * La expresión matemática para su determinación es la siguiente:
 * 			CSP363 = {Sumatorio desde j=fcierta hasta fvto} CSP238(j) * VRTA363(j) * VVIDA(fcalc,actj)
 * 
 * @author apedro
 *
 */
public class ModuloCSP363 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP363.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP363;
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_M = ConstantsModulos.CTE_VAR_M.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC;
	// Fin de las variables estáticas usadas para agilizar operaciones.

	

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
			if (ModuloCSP363.LOG.isTraceEnabled()) {
				ModuloCSP363.LOG.trace("Inicio de execute en clase ModuloCSP363");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloCSP363
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de los datos.
			
			//Invocamos a la función de calculo CSP363
			resultado = moduloCSP363(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloCSP363.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP363.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP363.LOG.isTraceEnabled()) {
			ModuloCSP363.LOG.trace("Fin de execute en clase ModuloCSP363");
		}
		
		return resultado;
	}
	
	/**
	 * Modulo de cálculo que retorna la cuantía de fallecimiento de la garantía principal de 
	 * Modalidades Rentas de Jubilación con Reembolso de Reservas (Mod. 363)
	 * La expresión matemática para su determinación es la siguiente:
	 * 			CSP363 = {Sumatorio desde j=fcierta hasta fvto} CSP238(j) * VRTA363(j) * VVIDA(fcalc,actj)
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
	 * @param codSubproceso
	 * 			Código del subproceso que se está ejecutando
	 */
	private BigDecimal moduloCSP363(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		//Variables locales
		String varCriEdad = ConstantsFunciones.CTE_CADENA_VACIA;
		String varCriFec = ConstantsFunciones.CTE_CADENA_VACIA;
		BigDecimal csp363 = BigDecimal.ZERO;
		BigDecimal varI1;
		BigDecimal varI2;
		BigDecimal varM;
		Timestamp varFcierta;
		Timestamp varfecJ1;
		Timestamp varFechaEfecto;
		BigDecimal varJ1;
		BigDecimal varTCyvida;
		BigDecimal varVVida = BigDecimal.ZERO;
		BigDecimal varVrta363 = BigDecimal.ZERO;
		BigDecimal varCSP238;
		//Fin variables locales
		
		if (ModuloCSP363.LOG.isTraceEnabled()) {
			ModuloCSP363.LOG.trace("Inicio función << moduloCSP363 >> de la clase ModuloCSP363, para la iteracion = {}", iteracion);
		}
			
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		// Cálculo de las variables de apoyo.
		varCriFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);		
		varCriEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIEDAD);
		// Fin del cálculo de las variables de apoyo.
		
		// Validación de las variables de apoyo.
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriFec);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriEdad);
		} // Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
		// Fin de la validación de las variables de apoyo.		
			
		if(bloqueCorriente.getFechaDevengo() == null) {
			return csp363;
		}
		
		//Cálculo de variables necesarias para el cálculo del módulo
		varI1 = btcUmic.getItcalc().get(0);
		varI2 = btcUmic.getItcalc().get(1);
		BigDecimal varI1PorcentajeMasUno = UtilModulos.getNumPorcentajeMasUno(varI1, mapVariables, CLAVE_MODULO);
		BigDecimal varI2PorcentajeMasUno = UtilModulos.getNumPorcentajeMasUno(varI2, mapVariables, CLAVE_MODULO);
		
		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		varM = UtilModulos.getVarM(mapVariables, CLAVE_VAR_M, varFechaEfecto, umic.getBti().getFecFinTramo1(), varCriFec);
		
//		if (fcalc.after(umic.getRentas().getFecIni())){
//			varFcierta = fcalc;
//		}else{
//			varFcierta = umic.getRentas().getFecIni();
//		}
		
		if (proyUmic.get(iteracion-1).getFechaDesde().after(umic.getRentas().getFecIni())){
			varFcierta = proyUmic.get(iteracion-1).getFechaDesde();
		} else {
			varFcierta = umic.getRentas().getFecIni();
		}
		
		
		Modulo moduloCSP238 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP238);
		Modulo moduloVRTA363 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VRTA363);
		int iteracionBucleModulos = iteracion;
		
		varTCyvida = FuncionesAuxiliares.nAnnos(varFechaEfecto, bloqueCorriente.getFechaDevengo(), varCriFec); 
		
		// Sumatorio desde j hasta proy.umic.size-1
		for (int i = iteracion - 1 ; i < proyUmic.size(); i++ ) {
			BloqueCorriente bloqCorriente = proyUmic.get(i).getBloqueVida();
			varCSP238 = (BigDecimal) moduloCSP238.execute(proyUmic, bloqCorriente, iteracionBucleModulos, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
			if (!varCSP238.equals(BigDecimal.ZERO)){
				varfecJ1 = bloqCorriente.getFechaDevengo(); 
				varJ1 = FuncionesAuxiliares.nAnnos(varFechaEfecto, varfecJ1, varCriFec);
	
				varVVida = FuncionesActualizacionFinanciera.vVida(varJ1, varI1, varI2, varM, varTCyvida, varI1PorcentajeMasUno, varI2PorcentajeMasUno);

				varVrta363 =  (BigDecimal) moduloVRTA363.execute(proyUmic, bloqCorriente, iteracionBucleModulos, varFcierta, umic, btcUmic, mapVariables, codSubproceso);
			
				//Se realiza el cálculo varCSP238(j) * varVrta363 (j) * varVVida(j) y se añade a csp363(j)
				csp363 = csp363.add(varCSP238.multiply(varVrta363).multiply(varVVida));
				
			} //Si varCSP238 es 0, el resultado del término es 0 y no es necesario añadirlo al sumatorio
	
			iteracionBucleModulos++; 
		}
		
		
		if (ModuloCSP363.LOG.isTraceEnabled()) {
			ModuloCSP363.LOG.trace("Fin función << moduloCSP363 >> de la clase ModuloCSP363, para la iteracion = {}, con resultado csp363 = {}", iteracion, csp363);
		}
		
		return csp363;
	}
}
