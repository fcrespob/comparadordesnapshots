package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
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
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Módulo que calculará la probabilidad de viudedad
 * @author eugenio.torres
 *
 */
public class ModuloCSP421019 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP421019.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP421019;
	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);

	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT;
	private static final String CLAVE_VAR_VAL_TAB_MORT = CLAVE_VAL_TAB_MORT.concat(CLAVE_MODULO);
	
	private static final String CLAVE_C1BBC= ConstantsModulos.CTE_VAR_C1BBC;
	private static final String CLAVE_VAR_C1BBC = CLAVE_C1BBC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_G1CAP= ConstantsModulos.CTE_VAR_G1CAP;
	private static final String CLAVE_VAR_G1CAP = CLAVE_G1CAP.concat(CLAVE_MODULO);
	
	private static final String CLAVE_I1PRI= ConstantsModulos.CTE_VAR_I1PRI;
	private static final String CLAVE_VAR_I1PRI = CLAVE_I1PRI.concat(CLAVE_MODULO);
	
	private static final String CLAVE_F1EXP= ConstantsModulos.CTE_VAR_F1EXP;
	private static final String CLAVE_VAR_F1EXP = CLAVE_F1EXP.concat(CLAVE_MODULO);
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
			if (ModuloCSP421019.LOG.isTraceEnabled()) {
				ModuloCSP421019.LOG.trace("Inicio de execute en clase CSP421019");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloCSP421019
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de datos.
			
			//Invocamos a la función moduloCSP421019
			resultado = moduloCSP421019(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso ,mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloCSP421019.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP421019.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP421019.LOG.isTraceEnabled()) {
			ModuloCSP421019.LOG.trace("Fin de execute en clase CSP421019");
		}

		return resultado;
	}
	/**
	 * Módulo que calculará la probabilidad de viudedad. 
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
	private BigDecimal moduloCSP421019(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, String codSubproceso,
			Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal CSP421019 = BigDecimal.ZERO;
		String varCriFec;
		String varCriEdad;
		BigDecimal varC1BBC = BigDecimal.ZERO, varCG1CAP, varCMCT = null;
		String varC1BBCsts = null, varF1EXPstr, varCTAstr, varCMCTstr;
		BigDecimal varEdadExp, varX, varCTA = BigDecimal.ZERO, varCPP = BigDecimal.ZERO,varCTAaux;
		Timestamp varF1EXP;

		List<BigDecimal> lstValoresTabMort = null;
		
		//Fin variables locales
		
		if (ModuloCSP421019.LOG.isTraceEnabled()) {
			ModuloCSP421019.LOG.trace("Inicio función << moduloCSP421019 >> de la clase moduloCSP421019, para la iteracion = {}", iteracion);
		}
		
		//validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		// Cálculo y validación de las variables de apoyo.
		
		if(null == bloqueCorriente.getFechaDevengo()){
			return CSP421019;
		}
		
		varCriEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIEDAD);
		varCriFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);
				
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriEdad);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriFec);
		}// Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
		// Fin de la validación de las variables de apoyo.
		
		lstValoresTabMort = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT, umic, btcUmic,
				IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriEdad, ConstantesSolvencia.CTE_TABMORT_L);

		varCTA = (BigDecimal) mapVariables.get(CLAVE_VAR_G1CAP);
		varC1BBC = (BigDecimal) mapVariables.get(CLAVE_VAR_C1BBC);
		
		if (null == varCTA && null == varC1BBC) {

			varCTAstr = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_G1CAP,
					umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(),
					umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_G1CAP);

			if (null != varCTAstr) {
				String valorvarCTAsts = varCTAstr.substring(19, 31);
				varCTA = new BigDecimal(valorvarCTAsts).setScale(2, RoundingMode.HALF_UP);
				varCTA = varCTA.divide(new BigDecimal("100"));
				mapVariables.put(CLAVE_VAR_G1CAP, varCTA);
			}
		}
		
		varCMCT = (BigDecimal) mapVariables.get(CLAVE_VAR_I1PRI);
		
		if(null == varCMCT && null == varC1BBC){
		
			varCMCTstr = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_I1PRI,
					umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(),
					umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_I1PRI);

			if (null != varCMCTstr) {
				String valorvarCMCTsts = varCMCTstr.substring(19, 31);
				varCMCT = new BigDecimal(valorvarCMCTsts).setScale(2, RoundingMode.HALF_UP);
				varCMCT = varCMCT.divide(new BigDecimal("100"));
				mapVariables.put(CLAVE_VAR_I1PRI, varCMCT);
			}

		}
		
		varF1EXP = (Timestamp) mapVariables.get(CLAVE_VAR_F1EXP);
		
		
		if(null == varF1EXP){
		
			varF1EXPstr  = UtilModulos.getDatosEspecificosUmic(mapVariables, 
			    										   CLAVE_VAR_F1EXP,
			    										   umic.getDatosGenerales().getKpoliza()	 , 
			    										   umic.getDatosGenerales().getKsubpoliza()  ,
			    										   umic.getDatosGenerales().getKcertificado(),
			    										   umic.getDatosGenerales().getNsuscri(),
			    										   CLAVE_F1EXP);
		
		if(null == varF1EXPstr){
			
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_F1EXP});
			
		}else{
			
			if(varF1EXPstr.substring(0, 3).equals("FSS")){
			
			int anyo = Integer.parseInt(varF1EXPstr.substring(5, 9));
			int mes = Integer.parseInt(varF1EXPstr.substring(9, 11));
			int dia = Integer.parseInt(varF1EXPstr.substring(11, 13));
			varF1EXP = new Timestamp(new GregorianCalendar(anyo,mes-1,dia,ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
			
				
			}else{ 
			
			int anyo = Integer.parseInt(varF1EXPstr.substring(0, 4));
			int mes = Integer.parseInt(varF1EXPstr.substring(4, 6));
			int dia = Integer.parseInt(varF1EXPstr.substring(6, 8));
			varF1EXP = new Timestamp(new GregorianCalendar(anyo,mes-1,dia,ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
			}
			
			mapVariables.put(CLAVE_VAR_F1EXP, varF1EXP);
			
		}
		}
		
		varEdadExp = FuncionesAuxiliares.nEdad(varF1EXP, umic.getAsegurados().getFnacAseg1(), varCriEdad, umic.getRentas().getFecIni(), 0);
		varX = FuncionesAuxiliares.nEdad(fcalc,  umic.getAsegurados().getFnacAseg1(),  varCriEdad,  umic.getRentas().getFecIni(),0);
		
		if(null == varCTA || varCTA.equals(BigDecimal.ZERO)){
			
			varC1BBC = (BigDecimal) mapVariables.get(CLAVE_VAR_C1BBC);

			if(null == varC1BBC){
				varC1BBCsts = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_C1BBC,
						umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(),
						umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_C1BBC);

				if (null == varC1BBCsts) {

					throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ,
							new Object[] { CLAVE_C1BBC });

				} else {
					String valorvarCIBBCsts = varC1BBCsts.substring(25, 31);
					varC1BBC = new BigDecimal(valorvarCIBBCsts).setScale(2, RoundingMode.HALF_UP);
					varC1BBC = varC1BBC.divide(new BigDecimal("100"));
					mapVariables.put(CLAVE_VAR_C1BBC, varC1BBC);
				}
			}
			varCTA = FuncionesAuxiliares.CTA(iteracion,  varC1BBC,  varEdadExp);
		
		}
		
		if(null == varCMCT){
			if(null == varC1BBCsts){
				
				varC1BBC = (BigDecimal) mapVariables.get(CLAVE_VAR_C1BBC);
				if(null == varC1BBC){
					varC1BBCsts = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_C1BBC,
							umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(),
							umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(),
							CLAVE_C1BBC);

					if (null == varC1BBCsts) {

						throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ,
								new Object[] { CLAVE_C1BBC });

					} else {
						String valorvarCIBBCsts = varC1BBCsts.substring(25, 31);
						varC1BBC = new BigDecimal(valorvarCIBBCsts).setScale(2, RoundingMode.HALF_UP);
						varC1BBC = varC1BBC.divide(new BigDecimal("100"));
						mapVariables.put(CLAVE_VAR_C1BBC, varC1BBC);

					}
				}
			}
		}
		
		
		varCPP = FuncionesAuxiliares.CPP(iteracion-1,  varC1BBC,  varEdadExp, lstValoresTabMort , varX, bloqueCorriente.getFechaDevengo(), varCriEdad, umic.getAsegurados().getFnacAseg1(), umic.getRentas().getFecIni(),varCMCT);
		
		CSP421019 = varCTA.subtract(varCPP);
	
		if (ModuloCSP421019.LOG.isTraceEnabled()) {
			ModuloCSP421019.LOG.trace("Fin función << moduloCSP421019 >> de la clase ModuloCSP421019, para la iteracion = {}, con resultado CSP421019 = {}", iteracion, CSP421019);
		}
		
		return CSP421019;
	}
}
