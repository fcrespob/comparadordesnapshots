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
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Usaremos este módulo para el cálculo de la cuantía nominal de gastos sobre prima. 
 * Este módulo aplica de forma genérica a la mayor parte de este negociado, cuando 
 * la operación tiene gastos internos que giran sobre la prima comercial.
 *
 */
public class ModuloGZC017 implements Modulo {

	private static final Logger LOG = LoggerFactory.getLogger(ModuloGZC017.class);
	
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_GZC017;
	
	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}

	@SuppressWarnings("unchecked")
	public Object execute(Object... args) throws Solvencia2Excepcion {
		//Variables locales
		BigDecimal gzc017 = BigDecimal.ZERO;
		//Fin variables locales

		try {
			if (ModuloGZC017.LOG.isTraceEnabled()) {
				ModuloGZC017.LOG.trace("Inicio de execute en clase ModuloGZC017");
			}

			//Recuperamos los datos que le pasaremos a la función moduloGZC017
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			//Invocamos a la función de calculo GZC017
			gzc017 = moduloGZC017(proyUmic,iteracion,fcalc, umic, btcUmic, codSubproceso, bloqueCorriente,mapVariables);
		} catch (Solvencia2Excepcion e) {
			ModuloGZC017.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloGZC017.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloGZC017.LOG.isTraceEnabled()) {
			ModuloGZC017.LOG.trace("Fin de execute en clase ModuloGZC017");
		}

		return gzc017;
	}

	
	/**
	 * Usaremos este módulo para el cálculo de la cuantía nominal de gastos sobre prima. 
	 * Este módulo aplica de forma genérica a la mayor parte de este negociado, cuando la 
	 * operación tiene gastos internos que giran sobre la prima comercial.
	 * 
	 * @param proyUmic
	 * @param iteracion
	 * @param fcalc
	 * @param umic
	 * @param btcUmic
	 * @param codSubproceso
	 * @param bloqueCorriente
	 * @param mapVariables
	 * @return
	 */
	private BigDecimal moduloGZC017(final List<DetalleCorriente> proyUmic, 
			                        final int iteracion, 
			                        final Timestamp fcalc, 
			                        final Umic umic,
			                        final DetalleBaseTecnica btcUmic, 
			                        final String codSubproceso, 
			                        final BloqueCorriente bloqueCorriente,
			                        final Map<String, Object> mapVariables) {
		
		BigDecimal gzc017 = BigDecimal.ZERO; 
		BigDecimal varGipc;
		BigDecimal varFlujoNominal_Vida;
		BigDecimal varFlujoNominal_Fallecimiento;
		BigDecimal varFlujoNominal_Prestacion;
		
		if (ModuloGZC017.LOG.isTraceEnabled()) {
			ModuloGZC017.LOG.trace("Inicio función << moduloGZC017 >> para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		/**
		 * Se realizará la validación de los parámetros de entrada marcados como obligatorios. 
		 * En caso de error se devuelve error funcional 001 - Parámetro obligatorio no informado &NombreAtributoEntrada.
		 * 
		 * Si estoy en el primer periodo (j=1) se recuperarán los datos de la umic necesarios para el cálculo que no varían por periodo, 
		 * así como las varibales internas que tampoco varían por periodo, y que se dejarán accesibles para su uso en el subproceso por 
		 * los siguientes periodos a calcular. 
		 * 
		 * Variables Módulo
		 * 
		 * varGipc = btcUmic.gtoRosspPrima
		 * Para cualquier periodo j se calculará: 
		 * 
         * varFlujoNominal_Vida(j) = proyUmic (j).corrienteVida.impFlujoNominal
		 * 
		 * varFlujoNominal_Fallecimiento(j) = proyUmic (j).corrienteFallecimiento.impFlujoNominal
		 * 
		 * varFlujoNominal_Prestacion (j) =  VarFlujoNominal_Vida +  VarFlujoNominal_Fallecimiento 
		 * 
		 * GZC017(j)=varFlujoNominal_Prestacion*[1/(1-varGIPC)-1]
		 * 
		 */
		//Variables de módulo
		varGipc = btcUmic.getGtorosspPrima().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
			
		if(null == proyUmic.get(iteracion-1).getBloqueVida()){
			varFlujoNominal_Vida = BigDecimal.ZERO;
		}else{
			if(null == proyUmic.get(iteracion-1).getBloqueVida().getImpFlujoNominal()){
				varFlujoNominal_Vida = BigDecimal.ZERO;
			}else{
				varFlujoNominal_Vida = proyUmic.get(iteracion-1).getBloqueVida().getImpFlujoNominal();
			}
		}
		
		if(null == proyUmic.get(iteracion-1).getBloqueFall()){
			varFlujoNominal_Fallecimiento = BigDecimal.ZERO;
		}else{
			if(null == proyUmic.get(iteracion-1).getBloqueFall().getImpFlujoNominal()){
				varFlujoNominal_Fallecimiento = BigDecimal.ZERO;
			}else{
			varFlujoNominal_Fallecimiento = proyUmic.get(iteracion-1).getBloqueFall().getImpFlujoNominal();
			}
		}
		
		varFlujoNominal_Prestacion = varFlujoNominal_Vida.add(varFlujoNominal_Fallecimiento);
		
		gzc017 = varFlujoNominal_Prestacion.multiply(BigDecimal.ONE.divide(BigDecimal.ONE.subtract(varGipc),ConstantsFunciones.MATH_CONTEXT).subtract(BigDecimal.ONE));
		
		if (ModuloGZC017.LOG.isTraceEnabled()) {
			ModuloGZC017.LOG.trace("Fin función << moduloGZC017 >> para la iteracion = {}", iteracion);
		}
		
		return gzc017;
	}

}
