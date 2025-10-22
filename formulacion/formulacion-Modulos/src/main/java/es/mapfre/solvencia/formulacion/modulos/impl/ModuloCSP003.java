//MODIFICACION: TAR00433819
//FECHA: 24/09/2018
//DESCRIP: SE INCLUYEN LOS NUEVOS ESTADOS DE PRORROGAS DE PU Y PP

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
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada del cálculo que devuelve un capital final revalorizable geométricamente.
 * La expresión matemática para su determinación es la siguiente:
 * 			CSP(003,tc) = Capital(0)*(1+PRC/100)^(n)
 */
public class ModuloCSP003 implements Modulo {

	/**
	 * log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP003.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP003;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TC = ConstantsModulos.CTE_VAR_TC.concat(CLAVE_MODULO);
	private static final String CLAVE_NUM_ANUA = ConstantsModulos.CTE_NUM_ANUA.concat(CLAVE_MODULO);
	
	private static final String CLAVE_CALCULO_CSP003 = "csp003";
	
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
		//Fin varaibles locales
		
		try {
			if (ModuloCSP003.LOG.isTraceEnabled()) {
				ModuloCSP003.LOG.trace("Inicio de execute en clase ModuloCSP003");
			}
			
			//Recuperamos los datos que le pasaremos a la función ModuloCSP003
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			
			if (!mapVariables.containsKey(CLAVE_CALCULO_CSP003)) {
				//Invocamos a la función de calculo CSP003
				resultado = moduloCSP003(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);				
			} else {
				resultado = (BigDecimal)mapVariables.get(CLAVE_CALCULO_CSP003);
			}
			
			
		} catch (Solvencia2Excepcion e) {
			ModuloCSP003.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP003.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP003.LOG.isTraceEnabled()) {
			ModuloCSP003.LOG.trace("Fin de execute en clase ModuloCSP003");
		}
		
		return resultado;

	}


	/** 
	 * Modulo de cálculo que devuelve un capital final revalorizable geométricamente.
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
	private BigDecimal moduloCSP003(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		//Variables locales
		BigDecimal capital = BigDecimal.ZERO;
		int numeroAnualidades = 0;
		BigDecimal csp003 = BigDecimal.ZERO;
		Timestamp varFechaEfecto = null;
		String varCriFec;
		BloqueCorriente bloqCorrienteUltimoPer;
		//Fin variables locales

		if (ModuloCSP003.LOG.isTraceEnabled()) {
			ModuloCSP003.LOG.trace("Inicio función << moduloCSP003 >> de la clase ModuloCSP003, para la iteracion = {}", iteracion);
		}
		
		// Validación de los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		/**
		 * Si estoy en el primer periodo, se recuperarán los datos de la umic necesarios para el cálculo que no varían por periodo, 
		 * así como las variables internas que tampoco varíen por periodo, y que se dejarán accesibles para su uso en el subproceso 
		 * por los siguientes periodos a calcular.
		 * 		Variables de Apoyo
				-	VarCriterFec --> obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
				Si VarCriterFec es nulo se devuelve error funcional 005 - No se ha encontrado la Variable de Apoyo ID-TEMPORAL, 
				finalizando el proceso para la UMIC.

				Variables Módulo
				-	Si la póliza está anulada, es decir  umic.datosGenerales.csitupol=  ‘AN’
						o	Se deberá retornar error funcional 004 – Umic en estado Anulado & claveUmic
				-	Si la póliza no está reducida, es decir  umic.datosGenerales.csitupol=  ‘VI’
						o	varfechaEfecto = umic.fechas fecinisus --> dejo la variable en memoria, disponible para el subproceso de la umic.
				-	Si la póliza está reducida, es decir  umic.datosGenerales.csitupol=  ‘RE’
						o	varfechaEfecto = umic.fechas.fecefecred --> dejo la variable en memoria, disponible para el subproceso de la umic.
				Establecido el efecto de  la umic calculamos las anualidades transcurridas desde varfechaEfecto hasta la fecha de cálculo del primer periodo, 
				invocando a  la función auxiliar TC: 
					varTC = TC(varfechaEfecto, fcalc) --> dejo la variable en memoria, disponible para el subproceso de la umic.
				
				Para cualquier periodo j se calculará: 
				varNumAnualidades  = varTC +  naños(fcalc, proyUmic(j).varBloque.fecDevengo, VarCriterFec).
				varNumAnualidades  --> sobreescribo la variable en memoria, disponible para el subproceso
		 */
		
		varCriFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);
		bloqCorrienteUltimoPer = proyUmic.get(proyUmic.size()-1).getBloqueBySubproceso(codSubproceso);
		
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriFec);
		}
		
		// Solo calculará valores de CSP003 si se está en fecha de devengo (para el resto de casos CSP003=cero).
		if (null != bloqueCorriente.getFechaDevengo()) {
			
			// Dado que se hacen varias llamadas se crea esta variable auxiliar para mejorar rendimiento.
			final String csitupol = umic.getDatosGenerales().getCsitupol();
			
			if (ConstantsModulos.CTE_DG_CSITU_ANU.equals(csitupol)) {
				if (ModuloCSP003.LOG.isDebugEnabled()) {
					ModuloCSP003.LOG.debug(Util.errorValidacionA7(umic.getIdUmic()));
				}
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A7, new String[]{umic.getIdUmic()});
			} else {
				capital = umic.getCapitales().getIcapini();
//INI-TAR00433819: Se incluye nuevos estados de prorrogas PP y PU:	
				if (ConstantsModulos.CTE_DG_CSI_NO_RED.equals(csitupol)          ||
				   (ConstantsModulos.CTE_DG_CSITU_PRORROGA_PU.equals(csitupol)   ||
				   (ConstantsModulos.CTE_DG_CSITU_PRORROGA_PP.equals(csitupol)))) {
//FIN-TAR00433819
					csp003 = (BigDecimal)mapVariables.get(CLAVE_CALCULO_CSP003);
					if(csp003 == null) {
						csp003 = capital.multiply(Util.pow(BigDecimal.ONE.add(umic.getCapitales().getPorevalcap().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)), umic.getDuraciones().getNdursegano() - 1));					
						mapVariables.put(CLAVE_CALCULO_CSP003, csp003);
					}
				} else if (ConstantsModulos.CTE_DG_CSITU_RED.equals(csitupol)) {
					varFechaEfecto = umic.getFechas().getFecefecred();
					//varNumAnualidades  = varTC +  naños(fcalc, proyUmic(j).varBloque.fecDevengo, VarCriterFec).
					if (mapVariables.get(CLAVE_NUM_ANUA) == null){
						if (umic.getFechas().getFecefecfin() != null){
							numeroAnualidades = FuncionesAuxiliares.tc(varFechaEfecto, umic.getFechas().getFecefecfin());
						}else{
							numeroAnualidades = FuncionesAuxiliares.tc(varFechaEfecto, bloqCorrienteUltimoPer.getFechaDevengo());
						}
						mapVariables.put(CLAVE_NUM_ANUA, numeroAnualidades);
					}else{
						numeroAnualidades = (int) mapVariables.get(CLAVE_NUM_ANUA);
					}
					
					/*Se calcula el capital final como: * CSP003(j) = vcapital * (1+ (umic.capitales.porevalcap / 100)) ^(varNumAnualidades(j))*/
					csp003 = capital.multiply(Util.pow(BigDecimal.ONE.add(umic.getCapitales().getPorevalcap().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)), numeroAnualidades - 1));
				}
			}		
		}
		
		if (ModuloCSP003.LOG.isTraceEnabled()) {
			ModuloCSP003.LOG.trace("Fin función << moduloCSP003 >> de la clase ModuloCSP003, para la iteracion = {}, con resultado capitalFinalCalculado = {}", iteracion, csp003);
		}
			
		return csp003;
	}

}
