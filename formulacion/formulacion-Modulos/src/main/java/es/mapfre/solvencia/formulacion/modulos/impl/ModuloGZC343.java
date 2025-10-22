package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.Asegurados;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.TablaConversion;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesActualizacionFinanciera;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.FuncionesRentas;
import es.mapfre.solvencia.formulacion.util.FuncionesVBX;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.impl.ObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Es una variación del módulo CSP363, pero partiendo desde la fcal en vez de
 * una fcierta, y forzando el cálculo del Vzc2 con tabla generacional. La tabla
 * generacional a utilizar será informada en la nueva tabla de Datos
 * Sepi/Endesa. Este módulo debe distinguir si el titular está en vigor o
 * anulado por motivo distinto a 17
 * 
 * @author
 *
 */
public class ModuloGZC343 implements Modulo {

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final Logger LOG = LoggerFactory.getLogger(ModuloGZC343.class);
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_GZC343;

	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);

	private static final String CLAVE_VA_CRIT_EDA = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_VA_CRIT_EDA.concat(CLAVE_MODULO);

	private static final String CLAVE_VAR_TCM = ConstantsModulos.CTE_VAR_TCM.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_VRTA = ConstantsModulos.CTE_VAR_VRTA.concat(CLAVE_MODULO);;

	// Fin de las variables estáticas usadas para agilizar operaciones.

	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}

	/**
	 * Función encargada de obtener los parámetros necesarios y de realizar la
	 * llamada a la función que realiza los calculos del modulo.
	 */
	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {
		// Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		// Fin variables locales

		try {
			if (ModuloGZC343.LOG.isTraceEnabled()) {
				ModuloGZC343.LOG.trace("Inicio de execute en clase ModuloCSP333");
			}

			// Inicio recuperación de datos que le pasaremos a la función moduloCSP333
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de los datos que se pasarán a la función moduloCSP333.

			// Invocamos a la función de calculo CSP333
			resultado = moduloGZC343(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables,
					codSubproceso);

		} catch (Solvencia2Excepcion e) {
			ModuloGZC343.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloGZC343.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloGZC343.LOG.isTraceEnabled()) {
			ModuloGZC343.LOG.trace("Fin de execute en clase ModuloCSP333");
		}

		return resultado;

	}

	/**
	 * Es una variación del módulo CSP363, pero partiendo desde la fcal en vez de
	 * una fcierta, y forzando el cálculo del Vzc2 con tabla generacional. La tabla
	 * generacional a utilizar será informada en la nueva tabla de Datos
	 * Sepi/Endesa.
	 * 
	 * @param proyUmic        Estructura detalleCorrientes de la umic
	 * @param bloqueCorriente Bloque de Trabajo de la corriente
	 * @param iteracion       Indica el periodo de proyección J que se está
	 *                        calculando de entre todos los periodos de proyección
	 *                        de la umic (proyUmic)
	 * @param fcalc           Fecha de calculo
	 * @param umic            Contiene los datos de la Umic que se está procesando
	 * @param btcUmic         Contiene el detalle de la base técnica de cálculo para
	 *                        la umic
	 * @param mapVariables    mapa con las variables de memoria necesarias
	 * @param codSubproceso   Código del subproceso que se está ejecutando
	 */
	private BigDecimal moduloGZC343(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente,
			final Integer iteracion, final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic,
			final Map<String, Object> mapVariables, final String codSubproceso) {
		// Variables locales
		BigDecimal varGipc;
		BigDecimal varI2 = BigDecimal.ZERO;
		BigDecimal varPrima0 = BigDecimal.ZERO;
		BigDecimal varGZC343 = BigDecimal.ZERO;
		BigDecimal aux = BigDecimal.ZERO;
		BigDecimal varVrta;
		BigDecimal varX;
		Integer varBeta=0;
		Integer varTCm ;
		String varCriterFecha = ConstantsFunciones.CTE_CADENA_VACIA;
		String varCriterEdad = ConstantsFunciones.CTE_CADENA_VACIA;
		String varCriterioEdad = ConstantsFunciones.CTE_CADENA_VACIA;
		String varCriFec = ConstantsFunciones.CTE_CADENA_VACIA;
		String varTabMort = ConstantsFunciones.CTE_CADENA_VACIA;
		Timestamp varFechaEfecto = null;
		Timestamp varAnoNac;
		List<BigDecimal> varValoresTabMort;

		// Fin variables locales

		if (ModuloGZC343.LOG.isTraceEnabled()) {
			ModuloGZC343.LOG.trace("Inicio función << moduloGZC343 >> de la clase ModuloGZC343, para la iteracion = {}",
					iteracion);
		}

		// Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);

		// Cálculo de las variables de apoyo.
		varCriterioEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD,
				umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(),
				umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_VA_CRIT_EDA);
		varCriFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(),
				umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);
		// Fin del cálculo de las variables de apoyo.

		// Validación de las variables de apoyo.
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la
		// forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriFec);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriterioEdad);
		} // Si llega a este punto las validaciones anteriores serán válidas para la
		// iteración inicial, luego serán válidas para las siguientes.
		// Fin de la validación de las variables de apoyo.

		/**
		 *	varGipc =btcUmic.gtoRosspPrima/100
		 *	varPrima0 = umic.primas.primanetaini
		 *	varI2 = btcUmic.itcalc2
		 *	varTabMort = btcUmic.tabla1Aseg1
		 *	varAnoNac = Año (umic.asegurados.fnacAseg1);
		 *	varValoresTabMort = obtenerConfiguracion. recuperarValoresExperiencia(btcUmic.fecCierre, varTabMort, varAnoNac, btcUmic. itcalc1, umic.baseTecIni.psobremort, umic.baseTecIni. priesgo)
		 *	Si la póliza no está reducida, es decir,  umic.datosGenerales.csitupol=  ‘VI’
		 *	Si umic.datosGenerales.cnegocio = ‘I’ 
		 *	varfechaEfecto = umic.fechas.fecefecIni
		 *	En caso contrario: 
		 *	varfechaEfecto = umic.fechas.fecinisus
		 *	Si la póliza está reducida, es decir  umic.datosGenerales.csitupol=  ‘RE’
		 *	varfechaEfecto = umic.fechas.fecefecred
		 *	Si umic.fechas.fecefecred es nula y umic.datosGenerales.cnegocio = ‘C’:
		 *	varfechaEfecto = umic.fechas.fecfinpagprim
		 *
		 *	varX = nedad(varfechaEfecto, umic.asegurados.fnacAseg1, varCriterEdad, umic.rentas.fecini)
		 *	VarTcm = TCM (findemes(varfechaEfecto),fcalc);
		 *	varVrta = VRTA (0,0, 99, varI2,99,varI2,VarEdad,varValoresTabMort,12,0, false) 
		 *	Dejamos las variables en memoria, disponibles para el subproceso de la umic.
         *
		 *	Se calcularán las distintas variables internas necesarias para el cálculo, dependientes del periodo j a calcular, como se describe a continuación: 
         *
		 *	Para cualquier periodo J se calculará: 
         *
		 *	Si J = 1
		 *	varBeta= 0
		 *	En caso contrario:
		 *	varBeta= varBeta +1 
         *
	 	 *	Si proyUmic(j).varBloque.fecDevengo es nula: 
         *
    	 *	varGzc343 (j)= 0
         *
		 *	Si proyUmic(j).varBloque.fecDevengo es no nula: 
         *
		 *	varGzc343(j)=(((varGipc*varPrima0)/100))/((1+varVrta))
         *
         *
		 * 
		 * @param codSubproceso
		 * 
		 */

		// Variables de módulo
		if(bloqueCorriente.getFechaDevengo() == null) {
			return varGZC343;
		}

		varGipc = btcUmic.getGtorosspPrima().divide(BigDecimal.valueOf(100.00),ConstantsFunciones.MATH_CONTEXT);
		varPrima0 = umic.getPrimas().getIprimanetaini();
		varI2 = btcUmic.getItcalc().get(1);
		varAnoNac = umic.getAsegurados().getFnacAseg1();
		// Si la póliza no está reducida
		if (umic.getDatosGenerales().getCsitupol().equals("VI")) {
			if (umic.getDatosGenerales().getCnegocio().equals("I")) {
				varFechaEfecto = umic.getFechas().getFecefecini();
			} else {
				varFechaEfecto = umic.getFechas().getFecinisus();
			}
		}
		// Si la póliza está reducida
		if (umic.getDatosGenerales().getCsitupol().equals("RE")) {
			varFechaEfecto = umic.getFechas().getFecefecred();
			if (umic.getFechas().getFecefecred().equals(null) & umic.getDatosGenerales().getCnegocio().equals("C")) {
				varFechaEfecto = umic.getFechas().getFecfinpagprim();
			}
		}

		varTCm = UtilModulos.getVarTCm(mapVariables, CLAVE_VAR_TCM, ConstantsModulos.CTE_FIRST_ITER, varFechaEfecto, fcalc);
		varTabMort = btcUmic.getTablacalc1aseg1();
		varValoresTabMort = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT, umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriterioEdad, ConstantesSolvencia.CTE_TABMORT_L);
		varVrta = (BigDecimal) mapVariables.get(CLAVE_VAR_VRTA); 

		if (varVrta == null) {
			varX = FuncionesAuxiliares.nEdad(fcalc, umic.getAsegurados().getFnacAseg1(), varCriterioEdad, umic.getRentas().getFecIni(), 0);
			varVrta = FuncionesRentas.vrta(0, 0, 99, varI2, 99, varI2, varX.intValue(), varValoresTabMort, 12, BigDecimal.ZERO, false, 0);
			mapVariables.put(CLAVE_VAR_VRTA, varVrta);
		}
		// Si j=1 -> varBeta=0
		// Si j>1 -> varBeta=varBeta+1
		//if (iteracion == 494) {
			//varBeta = iteracion;
		//}
		//varBeta = iteracion - 1;
		
		//varX = FuncionesAuxiliares.nEdad(proyUmic.get(iteracion).getBloqueVida().getFechaDevengo(), umic.getAsegurados().getFnacAseg1(), varCriterioEdad, umic.getRentas().getFecIni(), 0);
		//varVrta = FuncionesRentas.vrta(0, 0, 99, varI2, 99, varI2, varX.intValue(), varValoresTabMort, 12, BigDecimal.ZERO, false, 0);

		varGZC343 = (varGipc.multiply(varPrima0, ConstantsFunciones.MATH_CONTEXT))
							.divide(varVrta.add(BigDecimal.ONE), ConstantsFunciones.MATH_CONTEXT);
		return varGZC343;
	}
}
