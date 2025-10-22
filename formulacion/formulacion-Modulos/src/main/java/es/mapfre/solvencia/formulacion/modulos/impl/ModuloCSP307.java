package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Months;
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
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada del cálculo que devuelve la cuantía nominal en cada momento de la Umic.
 * La expresión matemática para su determinación es la siguiente:
 * 		CSP307 = varPnaj * (1 + varI1Bti - vaB1)^( NR- varNumAnualidades )
 * @author ogperez
 *
 */
public class ModuloCSP307 implements Modulo {

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP307.class);
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP307;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_MODBETA = ConstantsModulos.CTE_VA_MOD_BETA;
	private static final String CLAVE_VAR_MODBETA = CLAVE_MODBETA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_FP = ConstantsModulos.CTE_VAR_FP.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_ANT_RENOVA = ConstantsModulos.CTE_VAR_ANT_RENOVA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PROX_RENOVA = ConstantsModulos.CTE_VAR_PROX_RENOVA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_NR = ConstantsModulos.CTE_VAR_NR.concat(CLAVE_MODULO);
	
	private static final String CLAVE_OP_VAR_PRP = ConstantsModulos.CTE_VAR_OP_PRP.concat(CLAVE_MODULO);
	private static final String CLAVE_OP_VAR_GE = ConstantsModulos.CTE_VAR_OP_GE.concat(CLAVE_MODULO);
	
	private static final String CLAVE_CSP307 = ConstantsModulos.CTE_VAR_CSP071;
	private static final String CLAVE_VAR_CSP307 = CLAVE_CSP307.concat(CLAVE_MODULO);
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
			if (ModuloCSP307.LOG.isTraceEnabled()) {
				ModuloCSP307.LOG.trace("Inicio de execute en clase ModuloCSP307");
			}
			
			//Inicio recuperación de datos que le pasaremos a la función moduloCSP307
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			//Fin de la recuperación de los datos que se pasarán a la función moduloCSP307.
			
			//Invocamos a la función de calculo CSP307
			resultado = moduloCSP307(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloCSP307.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP307.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP307.LOG.isTraceEnabled()) {
			ModuloCSP307.LOG.trace("Fin de execute en clase ModuloCSP307");
		}
		
		return resultado;

	}

	/** 
	 * Modulo de cálculo que devuelve la cuantía nominal en cada momento de la Umic
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
	private BigDecimal moduloCSP307(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables locales
		String varCriterFecha = ConstantsFunciones.CTE_CADENA_VACIA;
		String varBeta = ConstantsFunciones.CTE_CADENA_VACIA;
		BigDecimal varI1Bti;
		BigDecimal varPNA0;
		BigDecimal varPNAt;
		Timestamp varFechaEfecto = null;
		Timestamp varFechaEfecto2 = null;
		Timestamp varAntRenova = null;
		Timestamp varProxRenova;
		Timestamp renovaActual;
		BigDecimal FDINIPAGPRIM;
		BigDecimal FDDESDERENOVA;
		BigDecimal FMINIPAGPRIM;
		BigDecimal FMDESDERENOVA;
		BigDecimal FDFINPAGPRIMA;
		BigDecimal FMFINPAGPRIMA;
		BigDecimal FMFECEFEC;
		
		BigDecimal varFP;
		BigDecimal varNmeses;
		Integer varDifMeses;
		BigDecimal varPeriodoPtes;
		Integer varNR;
		BigDecimal varB1 = BigDecimal.ZERO;
		BigDecimal varcsp307 = BigDecimal.ZERO;
		BigDecimal varPnaj = BigDecimal.ZERO;
		Integer varNumAnualidades;
		BigDecimal csp307 = BigDecimal.ZERO;
		BigDecimal varGE;
		BigDecimal varPRP;
		BigDecimal opvarGE;
		BigDecimal opVarPRP;
		BigDecimal flujoNominal = BigDecimal.ZERO;
		String varTipFlxPrima;
		Timestamp varfecUltRec;
		Timestamp FecAntRec; 
		Integer varD1;
		Integer varD2;
		Integer varNumRec;
		//Fin variables locales
		
		if (ModuloCSP307.LOG.isTraceEnabled()) {
			ModuloCSP307.LOG.trace("Inicio función << moduloCSP307 >> de la clase ModuloCSP307, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if ( bloqueCorriente.getFechaDevengo() == null ) {
			return csp307;
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
		varI1Bti = btcUmic.getItcalc().get(ConstantsFunciones.CTE_0).multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
		varPNA0 = umic.getPrimas().getIprimanetaini();
		varPNAt = umic.getPrimas().getIprimatarada();
		varGE = umic.getBti().getPgastgesex1I();
		varPRP = umic.getPrimas().getPrevprima();
		FDINIPAGPRIM = new BigDecimal(UtilFechas.getDia(umic.getFechas().getFecinipagprim()));
		FDDESDERENOVA = new BigDecimal(UtilFechas.getDia(umic.getFechas().getFecdesderenova()));
		FMINIPAGPRIM = new BigDecimal(UtilFechas.getMes(umic.getFechas().getFecinipagprim()));
		FMDESDERENOVA = new BigDecimal(UtilFechas.getMes(umic.getFechas().getFecdesderenova()));
		FDFINPAGPRIMA = new BigDecimal(UtilFechas.getDia(umic.getFechas().getFecfinpagprim()));
		FMFINPAGPRIMA = new BigDecimal(UtilFechas.getMes(umic.getFechas().getFecfinpagprim()));
		
		if (ConstantesSolvencia.NEGOCIO_COLECTIVO.equals(umic.getDatosGenerales().getCnegocio()) &&
				ConstantsFunciones.CTE_APOR_REDUCIDA.equals(umic.getDatosGenerales().getCsitupol())) {
			varFechaEfecto = umic.getFechas().getFecinisus();
		} else {
			varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		}
		
		FMFECEFEC = new BigDecimal(UtilFechas.getMes(varFechaEfecto));
		varAntRenova = UtilModulos.getVarAntRenova(mapVariables,CLAVE_VAR_ANT_RENOVA,umic.getFechas().getFecdesderenova());
		varProxRenova = UtilModulos.getVarProxRenova(mapVariables, CLAVE_VAR_PROX_RENOVA, umic.getFechas().getFechastarenova());
		
		varFP = (BigDecimal) mapVariables.get(CLAVE_VAR_FP);
		if (varFP == null){
			if (umic.getPrimas().getCformpago().equals(ConstantsFunciones.CTE_FORMPAGO_4)){
				varFP = BigDecimal.valueOf(ConstantsFunciones.CTE_12);
			} else if (umic.getPrimas().getCformpago().equals(ConstantsFunciones.CTE_FORMPAGO_3)){
				varFP = BigDecimal.valueOf(ConstantsFunciones.CTE_4);
			} else if (umic.getPrimas().getCformpago().equals(ConstantsFunciones.CTE_FORMPAGO_1) || umic.getPrimas().getCformpago().equals(ConstantsFunciones.CTE_FORMPAGO_2)){
				varFP = new BigDecimal(umic.getPrimas().getCformpago());
			}			
			mapVariables.put(CLAVE_VAR_FP, varFP);
		}	
		varNmeses = BigDecimal.valueOf(ConstantsFunciones.CTE_12).divide(varFP, ConstantsFunciones.MATH_CONTEXT);
		DateTime start = new DateTime(varAntRenova.getTime());
		DateTime end = new DateTime(umic.getFechas().getFecfinpagprim().getTime());
		varDifMeses = Math.abs(Months.monthsBetween(start,end).getMonths());
		varPeriodoPtes = new BigDecimal (varDifMeses / varNmeses.intValue());	
		Timestamp varfecvcto = umic.getFechas().getFecefecfin();
		
		varNR = (Integer) mapVariables.get(CLAVE_VAR_NR);
		
		if (varNR == null) {
			
			
			varNR = umic.getDuraciones().getNrenovaciones();
			if(umic.getFechas().getFecinipagprim().before(umic.getFechas().getFecinisus())){
				varNR = varNR + ConstantsFunciones.CTE_1;
				varFechaEfecto = umic.getFechas().getFecinipagprim();
			}
			
			mapVariables.put(CLAVE_VAR_NR, varNR);
		}
		
		if (varBeta.equals(ConstantsModulos.CTE_CRP)) {
			varB1 = varI1Bti;
		} else if (varBeta.equals(ConstantsModulos.CTE_CRPI)) {
			varB1 = BigDecimal.ZERO;
		} else {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AR, new String[]{varBeta});	
		}
					
		//Fin Variables Modulo
		
		varNumAnualidades = FuncionesAuxiliares.tc(varFechaEfecto, proyUmic.get(iteracion-ConstantsFunciones.CTE_1).getFechaDesde());
		varTipFlxPrima = umic.getDatosGenerales().getTipoflexprim();
		
		opVarPRP = (BigDecimal) mapVariables.get(CLAVE_OP_VAR_PRP);
		opvarGE = (BigDecimal) mapVariables.get(CLAVE_OP_VAR_GE);
		if (opVarPRP == null) {
			opVarPRP = BigDecimal.ONE.add(varPRP.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
			opvarGE = BigDecimal.ONE.add(varGE.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
			
			mapVariables.put(CLAVE_OP_VAR_PRP, opVarPRP);
			mapVariables.put(CLAVE_OP_VAR_GE, opvarGE);
		}
		
		varcsp307 = (BigDecimal) mapVariables.get(CLAVE_VAR_CSP307);
				
		if (varcsp307 == null) {
			varcsp307 = BigDecimal.ZERO;
				
			varPnaj = varPNA0.multiply(Util.pow(opVarPRP, varNumAnualidades));

			if (!proyUmic.get(iteracion-ConstantsFunciones.CTE_1).getFechaDesde().before(varProxRenova)) {
				varAntRenova = varProxRenova;
				varProxRenova = UtilFechas.incrAnyo(varProxRenova, ConstantsFunciones.CTE_1);
				varNR = varNR + ConstantsFunciones.CTE_1;
				mapVariables.put(CLAVE_VAR_ANT_RENOVA, varAntRenova);
				mapVariables.put(CLAVE_VAR_PROX_RENOVA,varProxRenova);
				mapVariables.put(CLAVE_VAR_NR, varNR);
			}
			

			if (varPnaj != null) {
				Integer limiteSup;
				if (ConstantsFunciones.CTE_APOR_REDUCIDA.equals(umic.getDatosGenerales().getCsitupol())){
					limiteSup = Math.max(ConstantsFunciones.CTE_0, FuncionesAuxiliares.tc(varFechaEfecto, umic.getFechas().getFecfinpagprim()))-1;
					if (!FMFINPAGPRIMA.equals(FMDESDERENOVA) || ((FMFINPAGPRIMA.equals(FMDESDERENOVA)) && (!FDFINPAGPRIMA.equals(FDDESDERENOVA))) || !FMFECEFEC.equals(FMDESDERENOVA)){
						limiteSup++;
						}
					varNR = limiteSup;
					if(FMDESDERENOVA.equals(FMINIPAGPRIM)) {	
						varFechaEfecto2 = UtilFechas.incrAnyo(varFechaEfecto, limiteSup);
						start = new DateTime(varFechaEfecto2.getTime());
						end = new DateTime(umic.getFechas().getFecfinpagprim().getTime());
					}
					else {
						renovaActual = umic.getFechas().getFecdesderenova();
						while(renovaActual.compareTo(umic.getFechas().getFecfinpagprim())>0) {
							renovaActual = UtilFechas.incrAnyo(renovaActual, -1);
						}
						start = new DateTime(renovaActual.getTime());
						end = new DateTime(umic.getFechas().getFecfinpagprim().getTime());
					}
					varDifMeses = Months.monthsBetween(start,end).getMonths();
					varPeriodoPtes = new BigDecimal (varDifMeses / varNmeses.intValue());
					if(limiteSup == 0 && varPeriodoPtes.equals(BigDecimal.ZERO)) {
						varPeriodoPtes = BigDecimal.ONE;
					}
					if(varTipFlxPrima.equals("E")){
						FecAntRec = UtilFechas.incrMeses(varFechaEfecto2, null, -varNmeses.intValue(), false);
						varfecUltRec = UtilFechas.incrMeses(varFechaEfecto2, null, varDifMeses, false);
						varD1 = FuncionesAuxiliares.nDias(varfecUltRec, umic.getFechas().getFecfinpagprim(), varCriterFecha);
						varD2 = FuncionesAuxiliares.nDias(varFechaEfecto2, varfecUltRec, varCriterFecha);
						
						varPeriodoPtes = varPeriodoPtes.add(new BigDecimal(varD1).divide(new BigDecimal(varD2), ConstantsFunciones.MATH_CONTEXT));
					}
				} else {
					limiteSup = varNumAnualidades;
					}
					
				BigDecimal fn = proyUmic.get(iteracion-ConstantsFunciones.CTE_1).getBloquePrim().getImpFlujoNominal();					
				for (int j = 0; j<=limiteSup; j++){
					if (j==0 && (!FDINIPAGPRIM.equals(FDDESDERENOVA) || !FMINIPAGPRIM.equals(FMDESDERENOVA))) {
						varcsp307 = varcsp307.add(varPNAt);
					}
					else if((j == limiteSup) && (ConstantsFunciones.CTE_APOR_REDUCIDA.equals(umic.getDatosGenerales().getCsitupol())) && (!FDFINPAGPRIMA.equals(FDDESDERENOVA) || !FMFINPAGPRIMA.equals(FMDESDERENOVA))){
						varcsp307 = varcsp307.multiply(Util.pow(BigDecimal.ONE.add(varI1Bti).subtract(varB1), 1)).add(((varPNA0.divide(varFP, ConstantsFunciones.MATH_CONTEXT)).multiply(varPeriodoPtes)));
					
					}else {
						varcsp307 = varcsp307.multiply(Util.pow(BigDecimal.ONE.add(varI1Bti).subtract(varB1), 1)).add(varPNA0.multiply(Util.pow(opVarPRP, j)));
					}
				}
				varcsp307 = varcsp307.multiply(Util.pow(BigDecimal.ONE.add(varI1Bti).subtract(varB1), varNumAnualidades - limiteSup ));
			}
						
		} else {
			if (!proyUmic.get(iteracion-ConstantsFunciones.CTE_1).getFechaDesde().before(varProxRenova)) {	
				varAntRenova = varProxRenova;
				varProxRenova = UtilFechas.incrAnyo(varProxRenova, ConstantsFunciones.CTE_1);
				varNR = varNR + ConstantsFunciones.CTE_1;
				mapVariables.put(CLAVE_VAR_ANT_RENOVA, varAntRenova);
				mapVariables.put(CLAVE_VAR_PROX_RENOVA,varProxRenova);
				mapVariables.put(CLAVE_VAR_NR, varNR);
				
				int i = ConstantsFunciones.CTE_0;
				while (varPnaj.compareTo(BigDecimal.ZERO) <= ConstantsFunciones.CTE_0  && i<proyUmic.size()){
					
				flujoNominal = proyUmic.get(i).getBloquePrim().getImpFlujoNominal();

				if (flujoNominal != null && BigDecimal.ZERO.compareTo(flujoNominal) < ConstantsFunciones.CTE_0){							
					if (UtilFechas.getAnio(proyUmic.get(iteracion-ConstantsFunciones.CTE_1).getBloquePrim().getFechaDevengo()) == UtilFechas.getAnio(proyUmic.get(i).getFechaDesde())){
						
						varPnaj = flujoNominal.divide(
								BigDecimal.ONE.subtract(varGE.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01))
								,ConstantsFunciones.MATH_CONTEXT);							
					}
				}
				i++;
			}
			
			
			BigDecimal interes = BigDecimal.ONE.add(varI1Bti).subtract(varB1);	
			varcsp307 = varPnaj.add(varcsp307.multiply(interes));
			
			}
			
		}
			
		mapVariables.put(CLAVE_VAR_CSP307,varcsp307);
		csp307 = varcsp307;
		
		return csp307;
	}
	
}
