package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
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

public class ModuloCSP206 implements Modulo {
	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP206.class);
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP206;
	private static final String CLAVE_VAR_PRC = ConstantsModulos.CTE_VAR_PRC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_GEDADMAX = ConstantsModulos.CTE_VAR_GEDADMAX;
	private static final String CLAVE_VAR_GEDADMAX = CLAVE_GEDADMAX.concat(CLAVE_MODULO);
	
	private static final String CLAVE_C1FDI= ConstantsModulos.CTE_VAR_C1FDI;
	private static final String CLAVE_VAR_C1FDI = CLAVE_C1FDI.concat(CLAVE_MODULO);
	
																				
	// Fin de las variables estáticas usadas para agilizar operaciones.
	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}
	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {
		//Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		//Fin variables locales
		try {
			if (ModuloCSP206.LOG.isTraceEnabled()) {
				ModuloCSP206.LOG.trace("Inicio de execute en clase ModuloCSP206");
			}
			//Recuperamos los datos que le pasaremos a la función ModuloCSP202
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			//Invocamos a la función moduloCSP202
			resultado = moduloCSP206(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso ,mapVariables);
		} catch (Solvencia2Excepcion e) {
			ModuloCSP206.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP206.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		if (ModuloCSP206.LOG.isTraceEnabled()) {
			ModuloCSP206.LOG.trace("Fin de execute en clase ModuloCSP206");
		}
		return resultado;
	}
	/**
	 * 
	 * Usaremos este módulo para el cálculo de la cuantía nominal de 
	 * la prestación de fallecimiento en Seguros mixtos Individuales
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
	 * @param codSubproceso
	 * 			Código del subproceso que se está ejecutando.
	 * @param mapVariables 
	 * 			mapa con las variables de memoria necesarias
	 */
	private static BigDecimal moduloCSP206(final List<DetalleCorriente> proyUmic, 
									       final BloqueCorriente bloqueCorriente, 
										   final int iteracion,
										   final Timestamp fcalc, 
										   final Umic umic, 
										   final DetalleBaseTecnica btcUmic, 
										   final String codSubproceso,
										   final Map<String, Object> mapVariables) {
		BigDecimal csp206 = BigDecimal.ZERO;
		BigDecimal varGEdadMax = BigDecimal.ZERO;	
		String varC1FDI = ConstantsFunciones.CTE_CADENA_VACIA;
		Timestamp varFecIni1 = null;
		
		if (ModuloCSP206.LOG.isTraceEnabled()) {
			ModuloCSP206.LOG.trace("Inicio de la función << moduloCSP206 >> de la clase ModuloCSP206, para la iteración = {}", iteracion);
		}
		
		/**
		 * Se realizará la validación de los parámetros de entrada marcados como obligatorios. 
		 * En caso de error se devuelve error funcional 001 - Parámetro obligatorio no informado &NombreAtributoEntrada.
		 * 
		 * Variables Módulo
		 * 
		 * 	varPrc= umic.capitales.porevalcap/100  dejo la variable en memoria, disponible para el subproceso de la umic.
		 * 
		 * 	varIcapAct = umic.capitales.icapact Se guarda la variable en memoria para el subproceso de la umic.
		 * 
		 * 	varFecSus = umic.fechas.fecsuscripcion
		 * 
		 * 	varFecIni = umic.fechas.fecini
		 * 
		 * 	varFecIni1 = Primer día de varFecIni (me llevo varFecIni a primer día del mes)
		 * 
		 * 	varNc1Fdi = Max(varFecSus, varFecIni1)
		 * 
		 * 	Si proyUmic(j).varBloque.fecDevengo es <=  varNc1Fdi: 
		 *          
		 * 	varCAPINIASE = 0
		 * 
		 * 	Si proyUmic(j).varBloque.fecDevengo es >  varNc1Fdi:
		 * 
		 * 	varCAPINIASE = varICAPACT
		 * 
		 * Finalmente se calculará: 
		 * 
		 * CSP206 = varCAPINIASE * (1+ varPrc)^((año(fcalc)-año(varNc1Fdi )) )
		 */
		
		// Definición de variables auxiliares para agilizar las operaciones (se llaman varias veces).
		final Integer ccartera = umic.getDatosGenerales().getCcartera();
		final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		final Integer kgarantia = umic.getDatosGenerales().getKgarantia();
		// Fin de la definición de las variables auxiliares.
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		//Variables Modulo
		BigDecimal varPrc = UtilModulos.getPorcentaje(mapVariables, CLAVE_VAR_PRC, umic.getCapitales().getPorevalcap());
		BigDecimal varIcapAct = umic.getCapitales().getIcapact();
		Timestamp varFecSus = umic.getFechas().getFecinisus();
		Timestamp varFecIni = umic.getFechas().getFecefecini();
				
		varC1FDI = UtilModulos.getDatosEspecificosUmic(mapVariables, 
				CLAVE_VAR_C1FDI,
				umic.getDatosGenerales().getKpoliza()	 , 
				umic.getDatosGenerales().getKsubpoliza()  ,
				umic.getDatosGenerales().getKcertificado(),
				umic.getDatosGenerales().getNsuscri(),
				CLAVE_C1FDI);

		if(null == varC1FDI){

			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_C1FDI});

		}else{
			
			String fechaCadena = varC1FDI.substring(5, 13);
			int anyo = Integer.parseInt(fechaCadena.substring(0, 4));
			int mes = Integer.parseInt(fechaCadena.substring(4, 6));
			int dia = Integer.parseInt(fechaCadena.substring(6, 8));
			varFecIni1 = new Timestamp(new GregorianCalendar(anyo,mes-1,dia,ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
			varFecIni1 = UtilFechas.getPrimerDiaDelMes(varFecIni1);
		}
		
		Timestamp varNc1Fdi = UtilFechas.obtenerFechaMayor(varFecSus, varFecIni1);
		if (proyUmic.get(iteracion-1).getFechaDesde().after(varNc1Fdi) || proyUmic.get(iteracion-1).getFechaDesde().equals(varNc1Fdi)){
			csp206 = varIcapAct.multiply(BigDecimal.ONE.add(varPrc).pow(UtilFechas.getAnio(proyUmic.get(iteracion-1).getFechaDesde())-UtilFechas.getAnio(varNc1Fdi)));
		}
		
		
		return csp206;
	}

}
