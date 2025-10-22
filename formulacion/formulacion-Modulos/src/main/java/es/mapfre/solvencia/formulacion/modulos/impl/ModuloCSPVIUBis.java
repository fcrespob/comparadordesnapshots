package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.FlujosProbablesKey;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.FlujosProbablesDao;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FlujosProbables;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;

public class ModuloCSPVIUBis implements Modulo {
	
	
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSPVIUBis.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSPVIUBis;	
	
	private static final String CLAVE_C2S = ConstantsModulos.CTE_C2S;
	private static final String CLAVE_VAR_CSPVIUBis = ConstantsModulos.CTE_VAR_CSPVIUBis.concat(CLAVE_MODULO);

	
	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}

	/**
	 * Módulo de cuantía nominal para el huérfano minusválido.
	 */
	
	@Override
	public Object execute(Object... args) throws Solvencia2Excepcion {
		
		BigDecimal cspviubis = BigDecimal.ZERO;
		
		try {
			if (ModuloCSPVIUBis.LOG.isTraceEnabled()) {
				ModuloCSPVIUBis.LOG.trace("Inicio de execute en clase ModuloCSPVIUBis");
			} 
			
			//Recuperamos los datos que le pasaremos a la función ModuloCSPVIU
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			
			cspviubis = moduloCSPVIUBis(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloCSPVIUBis.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) { 
			ModuloCSPVIUBis.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
	
		if (ModuloCSPVIUBis.LOG.isTraceEnabled()) {
			ModuloCSPVIUBis.LOG.trace("Fin de execute en clase ModuloCSPVIUBis");
		}
		
		return cspviubis;
	}

	
	/**
	 * Módulo de cuantía nominal para el huérfano minusválido
	 * 
	 * @param proyUmic
	 * @param bloqueCorriente
	 * @param iteracion
	 * @param fcalc
	 * @param umic
	 * @param btcUmic
	 * @param codSubproceso
	 * @param mapVariables
	 * @return
	 */
	
	private BigDecimal moduloCSPVIUBis(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, String codSubproceso,
			Map<String, Object> mapVariables) {
		
		final IObtenerDatos servicioDatos = FachadaServicios.getObtenerDatos();
		final IObtenerConfiguracion obtenerConfiguracion = FachadaServicios.getObtenerConfiguracion();
		String varModNom = null;
		List<DetalleCorriente> varProyFptoviu = proyUmic;  
		List<DetalleCorriente> varProyC2S = proyUmic;
		List<DetalleCorriente> varProyUmicTitular = null; //proyUmic;
		BigDecimal varFptoviu = BigDecimal.ZERO;
		Modulo moduloFPTOVIU;
		BigDecimal  varModC2S = BigDecimal.ZERO;
		BigDecimal  varModC2SM = BigDecimal.ZERO;
		BigDecimal varcspviuBis = BigDecimal.ZERO;
		String varModuloC2S = null;
		BigDecimal varcspviuant = null;
		Umic umicTitular;
		String varTitular;
		Umic umicMensual;
		Modulo ModuloC2S = null;
		FlujosProbablesDao flujosProbablesDao = new FlujosProbablesDao();
		List<DetalleCorriente> calculoProyUmicMensual;
		Modulo moduloNominalUmicMensual = null;
		BigDecimal varC1sant = null;
		Modulo moduloNominalUmicTitular = null;
		List<DetalleCorriente> calculoProyUmicTitular;

		
		
		if (ModuloCSPVIUBis.LOG.isTraceEnabled()) {
			ModuloCSPVIUBis.LOG.trace("Inicio función << moduloCSPVIUBis >> de la clase ModuloCSPVIUBis, para la  iteracion = {}", iteracion);
		}
		
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);	
		
		//System.out.println("Iteracion de CSPVIUBis: " + iteracion);
		
		if(bloqueCorriente.getFechaDevengo() == null){
			return varcspviuBis;	
		} 
		
		//Varibales de apoyo
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			varProyFptoviu = proyUmic;
			varProyC2S = proyUmic;
		}
		
		varTitular = umic.getDatosGenerales().getKbencon();
		if(null == varTitular){
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_G5);
		}
		if(umic.getOtrosDatos().getCestadoAseg2().equals("A")){	
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_G8);
		}
		if(varTitular.substring(0, 1).equals("3")){
			
			varModuloC2S = ConstantsFactorias.MODULO_CSP939;
		}else{ 
		
			switch (umic.getDatosGenerales().getKprestacion()) {
		       case "REV20":  
		       case "REV21":  
		       case "REV22":   
		       case "REV25":   
		       case "REV26":   
		       case "REV27":
		    	  varModuloC2S = ConstantsFactorias.MODULO_VIUE01;
		          break;
		       case "RS216":
		       case "RS217":
		       case "RS218":	   
		    	  varModuloC2S = ConstantsFactorias.MODULO_VIUS01;
		          break;
		       case "REV42":		    	 
		       case "REV45":
		    	   varModuloC2S = ConstantsFactorias.MODULO_VIUE102;
		          break;
		       case "REV23":
		    	   varModuloC2S = ConstantsFactorias.MODULO_VIUE02;
		    	   break;
		       case "REV24":
		    	   varModuloC2S = ConstantsFactorias.MODULO_VIUE03;
		    	   break;
		       case "REO00":
		       case "REO01":
		       case "REO02":
		       case "REO05":
		       case "REO06":
		       case "REO07":
		       case "REO13":
		       case "REO14":		    
		       case "REO15":
		    	   varModuloC2S = ConstantsFactorias.MODULO_ORFE01;
		    	   break;
		       }
		}
			
		if(null == varModuloC2S)
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_C2S});
		else{
			
			ModuloC2S = FactoriaModulos.getModulo(varModuloC2S);
			if (!umic.getRentas().getCpagrenta().equals("4")) {
				
				umicMensual = servicioDatos.recuperarUmicMensual(umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKpoliza(), 
					umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), 
					umic.getDatosGenerales().getNsuscri(), umic.getDatosGenerales().getKgarantia(), 
					umic.getDatosGenerales().getKajuste(), umic.getDatosGenerales().getCtipoaport(), umic.getDatosGenerales().getKbencon(), ConstantsFunciones.CTE_4_STRING);
			
				varProyC2S = servicioDatos.recuperarProyeccion(btcUmic.getBt(),umic.getDatosGenerales().getFecCierre(), umicMensual.getKey());
				if (varProyC2S.size() == ConstantsFunciones.CTE_0) {
					FlujosProbablesKey key = new FlujosProbablesKey(umicMensual.getDatosGenerales().getKmodalidad(),
							umicMensual.getDatosGenerales().getKgarantia(),
							umicMensual.getDatosGenerales().getKprestacion(), btcUmic.getBt());
					FlujosProbables fp = flujosProbablesDao.get(key);
					if (null == fp) {
						key = new FlujosProbablesKey(umicMensual.getDatosGenerales().getKmodalidad(),
								umicMensual.getDatosGenerales().getKgarantia(),
								umicMensual.getDatosAdicionales().getPrestCal(), btcUmic.getBt());
						fp = flujosProbablesDao.get(key);
					}

					calculoProyUmicMensual = proyUmic;
					moduloNominalUmicMensual = FactoriaModulos.getModulo(fp.getVida().getNominal());
					if (null != moduloNominalUmicMensual) {
						varModC2SM = (BigDecimal) moduloNominalUmicMensual.execute(calculoProyUmicMensual,
								bloqueCorriente, iteracion, fcalc, umicMensual, btcUmic, mapVariables,
								codSubproceso);
						varcspviuBis = varModC2SM;
					}
					if (null == varModC2SM) {

						varModC2SM = BigDecimal.ZERO;
						varcspviuBis = varModC2SM;

					}

				} else {
					varModC2SM = varProyC2S.get(iteracion - 1).getBloqueBySubproceso(codSubproceso)
							.getImpFlujoNominal();
					varcspviuBis = varModC2SM;
				}	
								
			}else {
				varModC2S = (BigDecimal) ModuloC2S.execute(varProyC2S, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
				
				//Parte funcional
				moduloFPTOVIU = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FPTOVIU);
				if(umic.getDatosGenerales().getKbencon().equals(ConstantsModulos.CTE_KBENCON_301)){
					varFptoviu = BigDecimal.ONE; 
				}else{
					varFptoviu = (BigDecimal) moduloFPTOVIU.execute(varProyFptoviu, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables,codSubproceso);
				}
				
				varcspviuant = (BigDecimal) mapVariables.get(CLAVE_VAR_CSPVIUBis); 
				if(varcspviuant == null){
					varcspviuBis = varModC2S.multiply(varFptoviu);
				}else{
					varcspviuBis = varcspviuant.add(varModC2S.multiply(varFptoviu)); 
				}				
			}
			mapVariables.put(CLAVE_VAR_CSPVIUBis, varcspviuBis);
		}
					
		if (ModuloCSPVIUBis.LOG.isTraceEnabled()) {
			ModuloCSPVIUBis.LOG.trace("Fin de la función << moduloCSPVIUBis >> de la clase ModuloCSPVIUBis, para la iteración = {}", iteracion);
		}
			
		return varcspviuBis;
	}

}
