package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.LimitesCapital;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalFlujoProyeccion;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.FuncionesVBX;
import es.mapfre.solvencia.formulacion.util.Terminales;
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo VBX009.
 * La expresión matemática para su determinación es la siguiente:
 *  			Bx(009,tcm+beta) = VTx(009,tcm+beta) * (1 + it)^alfam
 * @author agonzalezgar
 *
 */
public class ModuloVBX009 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVBX009.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VBX009;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);

	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
	
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 
	private static final String CLAVE_VAR_D = ConstantsModulos.CTE_VAR_D.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_K = ConstantsModulos.CTE_VAR_K.concat(CLAVE_MODULO);
	private static final String CLAVE_VARL = ConstantsModulos.CTE_VARL.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TIPO_ALFA = ConstantsModulos.CTE_VA_TIPO_ALFA.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT;
	private static final String CLAVE_VAR_VAL_TAB_MORT = CLAVE_VAL_TAB_MORT.concat(CLAVE_MODULO);
	
	private static final String CLAVE_MESCAREN = ConstantsModulos.CTE_VA_MESCAREN;
	private static final String CLAVE_VA_MESCAREN = CLAVE_MESCAREN.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_X = ConstantsModulos.CTE_VAR_X.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_LIMITE = ConstantsModulos.CTE_VAR_LIMITE.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_VTX0091 = "Vtx0091".concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_BETA1 = "varBeta1".concat(CLAVE_MODULO);
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
		TotalFlujoProyeccion resultado = null;
		//Fin variables locales
		
		try {
			if (ModuloVBX009.LOG.isTraceEnabled()) {
				ModuloVBX009.LOG.trace("Inicio de execute en clase ModuloVBX009");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloVBX009
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String terminal = (String) args[ConstantsModulos.PARAM_P_TERMINAL];
			// Fin de la recuperación de los datos.
			
			//Invocamos a la función de calculo VBX009
			resultado = moduloVBX009(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, terminal);
			
		} catch (Solvencia2Excepcion e) {
			ModuloVBX009.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVBX009.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloVBX009.LOG.isTraceEnabled()) {
			ModuloVBX009.LOG.trace("Fin de execute en clase ModuloVBX009");
		}
		return resultado;
		
	}
	/**
	 * Módulo que calcula el importe nominal para la proyección de Provisión Matemática por Fórmula Cerrada
	 * La expresión matemática para su determinación es la siguiente:
	 * 				Bx(009,tcm+beta) = VTx(009,tcm+beta) * (1 + it)^alfam.
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
	 * @param terminal
	 * 			Terminal de cálculo
	 */
	private TotalFlujoProyeccion moduloVBX009(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables,
			final String terminal) {
		//Variables locales
		final TotalFlujoProyeccion salida = new TotalFlujoProyeccion();
		BigDecimal vbx009 = BigDecimal.ZERO;
		String varCriFec;
		String varCriEdad;
		Integer varMescaren;
		String varTipoAlfa;
		BigDecimal varRenta1;
		BigDecimal varRenta2;
		BigDecimal varPrima;
		BigDecimal varGipc;
		BigDecimal varGic;
		BigDecimal varX;
		int varK;
		BigDecimal varI1;
		BigDecimal varL = BigDecimal.ZERO;
		BigDecimal varAlfaM;
		BigDecimal varI2;
		int varM = 0;
		BigDecimal varPrr;
		List<LimitesCapital> varListaLimCap;
		BigDecimal varGamma;
		int varD;
		int varBeta = 0;
		int varBeta1;
		BigDecimal varIt;
		BigDecimal varVtx009 = BigDecimal.ZERO;
		Timestamp varAntRenova;
		Timestamp varProxRenova;
		BigDecimal varVtx0091 = BigDecimal.ZERO;
		boolean varRAgravado  = true;
		Integer varNmeses;
		Timestamp varFechaEfecto;
		BigDecimal varCRMax;
		//Fin variables locales
		
		if (ModuloVBX009.LOG.isTraceEnabled()) {
			ModuloVBX009.LOG.trace("Inicio función << moduloVBX009 >> de la clase ModuloVBX009, para la iteracion = {}", iteracion);
		}
		
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		/**
		 * Si estoy en el primer periodo, se recuperarán los datos de la umic necesarios para el cálculo que no varían por periodo, así como las variables internas que tampoco varíen por periodo, y que se dejarán accesibles para su uso en el subproceso por los siguientes periodos a calcular.

				Variables de Apoyo
				-	VarCriterFec--> obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
				-	VarCriterEdad --> obtenerConfiguracion.recuperarVariableApoyo(ID-CRITERIO)
				-	VarMescaren  --> obtenerConfiguracion.recuperarVariableApoyo(MESCAREN)
				-	VarTipoAlfa --> obtenerConfiguracion.recuperarVariableApoyo(TIPO_ALFA)
									
				o	Si alguna de las variables de apoyo  retornadas es nulo se devuelve error funcional 005 - 
				No se ha encontrado la Variable de Apoyo &NombreVariableApoyo, finalizando el proceso para la UMIC.
				
				Variables Módulo
				-	varRenta1 = umic.rentas.rentini
				-	varRenta2 = umic.rentas rentmini
				-	varFecnac = umic.asegurados.fnacAseg1 
				-	varPrima = umic.primas.iprimatarada
				-	varGipc = btcUmic.gtoRosspPrima
				-	varGic = btcUmic.gtoRosspCap
				-	varTm = btcUmic.tabla1Aseg1
				-	varI1= btcUmic.itcalc1
				-	varI2= btcUmic.itcalc2
				-	varPrr = umic.rentas.prevrenta
				-	Si umic.rentas.forpagrent es no nulo ó umic.rentas.forpagrent <> 0
					-	varM = umic.rentas.forpagrent
				-	Si forpagrent es nulo ó forpagrent = 0
					-	Si umic.rentas.forpagrent = 1
							o	varM = 1
					-	Si umic.rentas.forpagrent = 2
							o	varM = 2
					-	Si umic.rentas.forpagrent = 3
							o	varM = 4
					-	Si umic.rentas.forpagrent = 4
							o	varM = 12	
				-	varAlfam = ALFAM (umic.fechas. fecdesderenova, umic.fechas. fechastarenova,umic.datosgenerales.fecCierre, VarTipoAlfa, VarCriterFec);
					
				-	varD = ndias(umic.fechas.fecinisus, umic.fechas.fecefecini , VarCriterFec)
				-	varX= nedad(umic.fechas.fecinisus, umic.asegurados.fnacAseg1, VarCriterEdad)
				-	varK = TCm(umic.fechas.fecinisus, fcalc);
				-	varL = nannos(umic.fechas.fecinisus, btcUmic.fecFinTramo1, VarCriterFec);
				-	varLimCap = obtenerConfiguracion.recuperarLimitesCapital (umic.datosgenerales.kmodalidad,  umic.datosgenerales.kgarantia, umic.fechas.fecinisus, varX, varK);
				-	VarGamma = umic.capitales. porgamma
				Si umic.baseTecIni.ragravado = ‘N’ psobremort > 0 :
					o	VarLimite= varLimCap. Ecaphasta
				Si umic.baseTecIni.ragravado = ‘S’  priesgo> 0 :
					o	VarLimite = varLimCap. ecaphastaAgra
		 */
		// Definición de variables auxiliares para agilizar las operaciones (se llaman varias veces).
		final Integer ccartera = umic.getDatosGenerales().getCcartera();
		final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		final Integer kgarantia = umic.getDatosGenerales().getKgarantia();
		// Fin de la definición de las variables auxiliares.
		
		// Cálculo de las variables de apoyo.
		varCriFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIFEC);
		varCriEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIEDAD);
		varTipoAlfa = UtilModulos.getVarTipoAlfa(mapVariables, CLAVE_VAR_TIPO_ALFA, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec());
		varMescaren = UtilModulos.getVarMescaren(mapVariables, CLAVE_VA_MESCAREN, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_MESCAREN);
		// Fin del cálculo de las variables de apoyo.
		
		// Validación de las variables de apoyo.
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriFec);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriEdad);
			ValidacionesComunesModulos.validarVariableDeApoyoVarMescaren(varMescaren);
			// Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
		}
		// Fin de la validación de las variables de apoyo.
		
		varRenta1 = umic.getRentas().getRentini();
		varRenta2 = umic.getRentas().getRentmini();
		varPrima = umic.getPrimas().getIprimatarada();
		varGipc = btcUmic.getGtorosspPrima();
		varGic = btcUmic.getGtorosspCap();
		varI1 = btcUmic.getItcalc().get(0);
		varI2 = btcUmic.getItcalc().get(1);
		varPrr = umic.getRentas().getPrevrenta();
		varNmeses = umic.getDuraciones().getNdursegmes();
		
		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		final Integer forpagrent = umic.getRentas().getForpagrent();
		if (null == forpagrent || forpagrent.equals(ConstantsFunciones.CTE_0)) {
			varM = ConstantsFunciones.FORPAGRENT.get(Integer.valueOf(umic.getRentas().getCpagrenta()));
		} else {
			varM = forpagrent;
		}
		
		varD = UtilModulos.getVarD(mapVariables, CLAVE_VAR_D, umic.getFechas().getFecinisus(), umic.getFechas().getFecefecini(), varCriFec);
				
		
		varK = UtilModulos.getVarK(mapVariables, CLAVE_VAR_K, UtilFechas.incrMeses(varFechaEfecto, null, 0, true), fcalc);
		varL = UtilModulos.getVarLInteger(mapVariables, CLAVE_VARL, ConstantsModulos.CTE_FIRST_ITER, btcUmic.getFecInitramo().get(0), btcUmic.getFecfintramo().get(0), varCriFec);
		//varAlfaM = UtilModulos.getVarAlfam(mapVariables, CLAVE_VAR_ALFAM, umic.getFechas().getFecdesderenova(), umic.getFechas().getFechastarenova(), umic.getDatosGenerales().getFecCierre(), varTipoAlfa, varCriFec);
		
		List<BigDecimal> lstValoresTabMort = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT, umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriEdad, ConstantesSolvencia.CTE_TABMORT_L);

		Integer varEdifer = umic.getDatosGenerales().getEdifer();
		varX = UtilModulos.getVarX(mapVariables, CLAVE_VAR_X, varFechaEfecto, umic.getAsegurados().getFnacAseg1(), varCriEdad, umic.getRentas().getFecIni(), varEdifer);
	
		
		//varLimCap = obtenerConfiguracion.recuperarLimitesCapital (umic.datosgenerales.kmodalidad,  umic.datosgenerales.kgarantia, umic.fechas.fecinisus, varX, varK)
		varListaLimCap = UtilModulos.getVarListaLimCap(mapVariables, CLAVE_VAR_LIMITE, kmodalidad, kgarantia, umic.getFechas().getFecinisus(), varX);
		
		
		varGamma = (umic.getCapitales().getPorgamma()).multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01, ConstantsFunciones.MATH_CONTEXT);
		varCRMax = umic.getCapitales().getCrmax();
		
		
		if (ConstantsModulos.CTE_N.equals(umic.getBti().getRagravado())) {
			varRAgravado = false;
		} else if (ConstantsModulos.CTE_S.equals(umic.getBti().getRagravado())) {
			varRAgravado = true;
		}
		
		
		/**
		 * Para cualquier periodo j: 
			Si  terminal = VTX009 se calculará:
			
				varβ = TCM(fcalc, proyUmic(j).fecDesde);
				Si    varK + varβ  <= varL *12 
				varIt = varI1 
				Si    varK + varβ  > varL *12 
				varIt = varI2
			varVtx009 = VTX009 (varRenta1, varRenta2,varK + varBeta,varD,varN,varI1,varI2,varX,varValoresTabMort,varM,varPpr,varPrima, varCRm,varGamma,varMescaren)
		 */
		
		if (ConstantsModulos.CTE_VTX009.equals(terminal)) {
			varBeta = FuncionesAuxiliares.tcm(fcalc, proyUmic.get(iteracion - 1).getFechaDesde());
			if (varBeta < 0){
				varBeta = 0;
			}
			
			if (BigDecimal.valueOf(varK + varBeta).compareTo(varL.multiply(ConstantsFunciones.CTE_OPER_12)) <= 0) {
				varIt = varI1;
			} else {
				varIt = varI2;
			}
			
			int nmeses = varK + varBeta;
			//Se translada varAntRenova al último día de mes, si fuese del tipo 29/02/XXXX se llevará hasta el 28/02/XXXX
			varAntRenova = UtilFechas.incrMeses(varFechaEfecto, null, nmeses, true); // + nmeses meses
				
			if (varAntRenova.compareTo(proyUmic.get(iteracion-1).getFechaDesde()) == 1){
				//Se translada varAntRenova al último día de mes, si fuese del tipo 29/02/XXXX se llevará hasta el 28/02/XXXX
				varAntRenova = UtilFechas.incrMeses(varAntRenova, null, -1, true); // -1 mes
			}
			//Se translada varProxRenova al último día de mes, si fuese del tipo 29/02/XXXX se llevará hasta el 28/02/XXXX
			varProxRenova = UtilFechas.incrMeses(varAntRenova, null, 1, true); // + 1 mes
			
			if(varProxRenova.compareTo(proyUmic.get(iteracion-1).getFechaDesde())==0){
				varAntRenova = UtilFechas.incrMeses(varAntRenova, null, 1, true);
				varProxRenova = UtilFechas.incrMeses(varProxRenova, null, 1, true);
			}
			
			
//			if (varBeta <= 0){
			if (!iteracion.equals(ConstantsFunciones.CTE_FIRST_ITER)){
				varAlfaM = BigDecimal.ZERO;
				varVtx0091 = (BigDecimal) mapVariables.get(CLAVE_VAR_VTX0091);
				varVtx009 = varVtx0091;
				varBeta1 = (int)mapVariables.get(CLAVE_VAR_BETA1) + 1;
			}else {
				varAlfaM = FuncionesVBX.mg00xAlfam(varAntRenova, varProxRenova, proyUmic.get(iteracion-1).getFechaDesde(), varTipoAlfa, varCriFec); 
				// Se calcula el terminal anterior únicamente en la primera iteración
				
				//TODO: Cambiar el parámetro varL.intValue() cuando se revise el tipo de dato que deben recibir los parametros de las funciones
				//varVtx009 = VTX009 (varRenta1, varRenta2,varK + varBeta,varD,varL,varI1,varL,varI2,varX,varValoresTabMort,varM,varPpr,varPrima, varCRm,varGamma,varMescaren,varGic,varGipc)
				varBeta = FuncionesAuxiliares.tcm(varFechaEfecto, fcalc);
				varVtx009 = Terminales.vtx009(varRenta1, varRenta2, varBeta, varD, varL.intValue(), varI1, varL.intValue(), varI2, varX.intValue(),
						lstValoresTabMort,
						varM, varPrr, varPrima, varListaLimCap, varGamma, varMescaren, varGic, varGipc, mapVariables, varRAgravado, varNmeses, varCRMax);
				varBeta1 = varBeta + 1;
			}
			
			if (proyUmic.size() != iteracion){
//				varBeta1 = FuncionesAuxiliares.tcm(umic.getFechas().getFecinisus(), proyUmic.get(iteracion).getFechaDesde()); 
				varVtx0091 = Terminales.vtx009(varRenta1, varRenta2, varBeta1, varD, varL.intValue(), varI1, varL.intValue(), varI2, varX.intValue(),
						lstValoresTabMort,
						varM, varPrr, varPrima, varListaLimCap, varGamma, varMescaren, varGic, varGipc, mapVariables, varRAgravado, varNmeses, varCRMax);
				mapVariables.put(CLAVE_VAR_VTX0091,varVtx0091);
				mapVariables.put(CLAVE_VAR_BETA1, varBeta1);
			}else{
				varVtx0091 = BigDecimal.ZERO;
			}
						
			/**
			 * Finalmente se hallará VBX009 como:
			 * 		vbx009(j) = varVtx009 * (1+varIt) ^ varAlfam
			 * 	Y se  retornarán también los terminales:
			 * 		terminalAnterior = varVtx009
			 * 		terminalPosterior= 0
			 */
			if(varAlfaM.signum() == 0) {
				vbx009 = varVtx009;				
			} else {
				vbx009 = varVtx009.multiply(Util.pow(BigDecimal.ONE.add(varIt.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)), varAlfaM));				
			}
			
			salida.setProvbtiproy(vbx009);
			salida.setTerminalAnterior(varVtx009);
			salida.setTerminalPosterior(varVtx0091);
//			LOG.warn(proyUmic.get(iteracion - 1).getFechaDesde() +";" + vbx009 + ";" + varVtx009 + ";" + varVtx0091);

		}

		if (ModuloVBX009.LOG.isTraceEnabled()) {
			ModuloVBX009.LOG.trace("Fin función << moduloVBX009 >> de la clase ModuloVBX009, para la iteracion = {} con resultado vbx009 = {}, terminalAnterior = {}, terminalPosterior = {}", vbx009, varVtx009, 0);
		}
		
		return salida;
	}
	
}
