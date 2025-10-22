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
 * Clase que implementa el modulo RT003.
 * La expresión matemática para su determinación es la siguiente:
 * 		RT(003,tc) =  K1 * Capital(n) * ( 1+(I1/100+K2) ) ^ -dpdtes/365
 * @author apedro
 *
 */
public class ModuloRT003 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloRT003.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_RT003;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
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
			if (ModuloRT003.LOG.isTraceEnabled()) {
				ModuloRT003.LOG.trace("Inicio de execute en clase ModuloRT003");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloRT003
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			
			//Invocamos a la función ModuloRT003
			resultado = moduloRT003(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloRT003.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloRT003.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloRT003.LOG.isTraceEnabled()) {
			ModuloRT003.LOG.trace("Fin de execute en clase ModuloRT003");
		}
		
		return resultado;
	}
	/**
	 * Módulo de cálculo de la cuantía por Rescate de las garantías dentro de Solvencia II para las modalidades de MILLÓN VIDA.
	 * La expresión matemática para su determinación es la siguiente:
	 *				 RT(003,tc) =  K1 * Capital(n) * ( 1+(I1/100+K2) ) ^ -dpdtes/365  
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
	 * @param codSubproceso
	 * 			Código del subproceso que se está ejecutando
	 */
	private BigDecimal moduloRT003(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		//Variables locales
		String varCriterFec = ConstantsFunciones.CTE_CADENA_VACIA;
		BigDecimal rt003 = BigDecimal.ZERO;
		Integer varDpdtes;
		Timestamp varFechaEfecto;
		//Fin variables locales
		
		if (ModuloRT003.LOG.isTraceEnabled()) {
			ModuloRT003.LOG.trace("Inicio función << ModuloRT003 >> de la clase ModuloRT003, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		//Si umic.fechas.fecefecfin es nulo se devuelve error funcional A2 -  Valor nulo  incorrecto para el dato umic.fechas.fecefecfin
		if (umic.getFechas().getFecefecfin() == null){
			if (ModuloRT003.LOG.isDebugEnabled()) {
				ModuloRT003.LOG.debug(Util.errorValidacionA2(umic.getFechas().getFecefecfin().toString(), ConstantsModulos.CTE_FEC_EFEC_FIN));
			}
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_A2, new String[]{ConstantsModulos.CTE_FEC_EFEC_FIN});
		}
		
		// Cálculo y validación de las variables de apoyo.
		varCriterFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);
				
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterFec);
		}
		// Fin del cálculo y la validación de las variables de apoyo.
		
		if (bloqueCorriente.getFechaDevengo() == null){
			return rt003;
		}
		
		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		/**
		 * Constantes de Rescates
		 * Para obtener el valor real de las constantes de rescate hay que realizar un servicio de consulta con el código y la duración de la constante.
		 * 
		 * -	codk1 = umic.rescates.krescate1
		 * -	Si el código de la constante  empieza por  KT
		 * 		o	durk1 = TCM(umic.fechas.fecinisus, proyUmic(j).varBloque.fecDevengo);
		 * -	Si el código de la constante empieza por  KC
		 * 		o	durk1 = 999
		 * -	Si el código de la constante empieza por  KM
		 * 		o	durk1 =  umic.duraciones.ndurprima/12
		 * -	Si el código de la constante empieza por  KN
		 * 		o	durk1 = umic.duraciones.ndursemes
		 * -	vark1 -> obtenerConfiguracion.recuperarCtesRescates(codk1, durk1)
		 * 		o	Si el valor de la constante  retornada es nulo se devuelve
		 * 		error funcional A8 - No se ha encontrado la Constante de Rescate  codK1, finalizando el proceso para la UMIC.
		 * 
		 * -	codk2 = umic.rescates.krescate2
		 * -	Si el código de la constante  empieza por  KT
		 * 		o	durk2 = TCM(umic.fechas.fecinisus, proyUmic(j).varBloque.fecDevengo);
		 * -	Si el código de la constante empieza por  KC
		 * 		o	durk2 = 999
		 * -	Si el código de la constante empieza por  KM
		 * 		o	durk2 =  umic.duraciones.ndurprima/12
		 * -	Si el código de la constante empieza por  KN
		 * 		o	durk2 = umic.duraciones.ndursemes
		 * -	vark2 -> obtenerConfiguracion.recuperarCtesRescates(codk2, durk2)
		 * 		o	Si el valor de la constante  retornada es nulo se devuelve
		 * 		error funcional A8 - No se ha encontrado la Constante de Rescate  codK2, finalizando el proceso para la UMIC.
		 * 
		 */
	
		final String codk1 = umic.getRescates().getKrescate1();
		final String codk2 = umic.getRescates().getKrescate2();
		
		final Integer durk1 = UtilModulos.obtenerDuracionCodKX(codk1, varFechaEfecto, proyUmic.get(iteracion-1).getFechaDesde(), 
				umic.getDuraciones().getNdurprima(), umic.getDuraciones().getNdursegmes());
		final Integer durk2 = UtilModulos.obtenerDuracionCodKX(codk2, varFechaEfecto, proyUmic.get(iteracion-1).getFechaDesde(), 
				umic.getDuraciones().getNdurprima(), umic.getDuraciones().getNdursegmes());
		
		final BigDecimal vark1 = UtilModulos.getCteRescate(mapVariables, codk1, durk1);
		final BigDecimal vark2 = UtilModulos.getCteRescate(mapVariables, codk2, durk2);
		
		// Estas validaciones hay que hacerlas en cada iteración porque la fecha de devengo cambiará.
		ValidacionesComunesModulos.validarCteRescateVarkx(codk1, vark1);
		ValidacionesComunesModulos.validarCteRescateVarkx(codk2, vark2);
		
		//Variables necesarias para el cálculo
		final BigDecimal varCapital = umic.getCapitales().getIcapact();
		final BigDecimal varI1 = btcUmic.getItcalc().get(0);

		varDpdtes = FuncionesAuxiliares.nDias(proyUmic.get(iteracion-1).getFechaDesde(), umic.getFechas().getFecefecfin(), varCriterFec);
		
		//Cálculo de la expresión matemática RT(003,tc) =  K1 * Capital(n) * ( 1+(I1/100+K2) ) ^ -dpdtes/365
		final BigDecimal division =varI1.divide(ConstantsFunciones.CTE_OPER_100, ConstantsFunciones.MATH_CONTEXT);
		final BigDecimal divisionMas1Masvark2 = BigDecimal.ONE.add(division).add(vark2);
		final BigDecimal exponente = BigDecimal.valueOf(varDpdtes).multiply(ConstantsFunciones.CTE_OPER_1_PARTIDO_365);
		final BigDecimal potencia = Util.pow(divisionMas1Masvark2, exponente.negate());
		
		rt003 = varCapital.multiply(vark1).multiply(potencia);
		
		if (ModuloRT003.LOG.isTraceEnabled()) {
			ModuloRT003.LOG.trace("Fin función << ModuloRT003 >> de la clase ModuloRT003, para la iteracion = {} con resultado rt003 = {}", iteracion, rt003);
		}
			
		return rt003;
	}
}
