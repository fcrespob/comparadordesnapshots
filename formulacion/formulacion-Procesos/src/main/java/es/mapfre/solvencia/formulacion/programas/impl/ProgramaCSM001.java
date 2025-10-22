package es.mapfre.solvencia.formulacion.programas.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.programas.ProgramaGenerico;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.formulacion.util.UtilProcesos;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Realiza el calculo del CSM a cierre. Como resultado obtendremos una corriente
 * de factores de probabilizacion e importes no anulados para cada periodo de
 * proyeccion de la corriente. Este programa, no ejecutara ningun calculo para
 * las bases tecnicas que no sean NIIF17.
 * 
 * @author NFQ
 *
 */
public class ProgramaCSM001 extends ProgramaGenerico {

	private static final Logger LOG = LoggerFactory.getLogger(ProgramaCSM001.class);

	@Override
	public String getNombrePrograma() {
		return ConstantsFactorias.PROGRAMA_CSM001;
	}

	@Override
	public void executeImpl(final Umic umic, final FichaProceso fichaProceso, final DetalleBaseTecnica btcUmic,
			final List<DetalleCorriente> proyUmic, final String codSubproceso) throws Solvencia2Excepcion {

		if (ProgramaCSM001.LOG.isTraceEnabled()) {
			ProgramaCSM001.LOG.trace("Inicio función << executeImpl >> de la clase ProgramaCSM001");
		}

		try {
			calcularCSM(umic, fichaProceso, btcUmic, proyUmic, codSubproceso);
		} catch (Solvencia2Excepcion e) {
			ProgramaCSM001.LOG.error(e.getIncidencia().getTextoError(), e);
			UtilProcesos.exceptionControladaPrograma(btcUmic, umic, getNombrePrograma(), e);
		} catch (Exception e) {
			ProgramaCSM001.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ProgramaCSM001.LOG.isTraceEnabled()) {
			ProgramaCSM001.LOG.trace("Fin función << executeImpl >> de la clase ProgramaCSM001");
		}
	}

	/**
	 * Determina si debe realizarse el calculo del csm para la umic, en caso
	 * afirmativo recupera la provision BTI o la provision ROSSP, realiza el calculo
	 * para cada periodo de proyeccion y guarda los datos en cache
	 * 
	 * @param umic
	 * @param fichaProceso
	 * @param btcUmic
	 * @param proyUmic
	 * @param codSubproceso
	 */
	private void calcularCSM(Umic umic, FichaProceso fichaProceso, DetalleBaseTecnica btcUmic,
			List<DetalleCorriente> proyUmic, String codSubproceso) {
		String bt = btcUmic.getBt();
		String varProvCSM;
		Timestamp varFecTransicion;
		List<DetalleCorriente> varProyCSM;

		if (ProgramaCSM001.LOG.isTraceEnabled()) {
			ProgramaCSM001.LOG.trace("Inicio función << calcularCSM >> de la clase ProgramaCSM001");
		}

		final IAlmacenarDatos almacenarDatos = FachadaServicios.getAlmacenarDatos();
		final IObtenerConfiguracion obtenerConfig = FachadaServicios.getObtenerConfiguracion();
		final IObtenerDatos obtenerDatos = FachadaServicios.getObtenerDatos();

		if (bt.equals("NIIF17LIR") || bt.equals(ConstantsModulos.CTE_BT_N17LIRIN)) {
			
			varProvCSM = (String) obtenerConfig.recuperarDefinicionAuxiliar(ConstantsModulos.CTE_PROVCSM);

			if (varProvCSM == null) {
				varProvCSM = ConstantsModulos.CTE_BTI;
			}

			if (varProvCSM.equals(ConstantsModulos.CTE_BT_ROSSP) || varProvCSM.equals(ConstantsModulos.CTE_BT_ROSSPCSM) || varProvCSM.equals("ROSSPTI") || varProvCSM.equals("ROSSPTE") || varProvCSM.equals("ROSSPGA")) {
				varFecTransicion = (Timestamp) obtenerConfig
						.recuperarDefinicionAuxiliar(ConstantsModulos.CTE_FEC_TRANSICION);
				
				//Inlcuir si no viene la fecha una incidencia que no de error nullpointer
				
				if (!umic.getFechas().getFecinisus().before(varFecTransicion)) {
					varProvCSM = ConstantsModulos.CTE_BTI;
				}
			}
			
			
			if(varProvCSM.equals(ConstantsModulos.CTE_BTI)){
				varProyCSM = obtenerDatos.recuperarProyeccion(varProvCSM, btcUmic.getFecCierre(), umic.getKey());
				if(null == varProyCSM || varProyCSM.size() == 0){
					varProyCSM = obtenerDatos.recuperarProyeccion(ConstantsModulos.CTE_BTI_PROY, btcUmic.getFecCierre(), umic.getKey());
				}
			}else{
				varProyCSM = obtenerDatos.recuperarProyeccion(varProvCSM, btcUmic.getFecCierre(), umic.getKey());
			}
			//si varproycsm es nulo devolver error
			
			BigDecimal varProvbtiproy = null;
			BigDecimal varProvNF17 = null;
			BigDecimal varResultadoRA = null;
			BigDecimal aux;
			BigDecimal tipoPago = null;
			BigDecimal prima = null;
			
				for (int i = 0; i < proyUmic.size(); i++) {
					
					if (null == varProyCSM.get(i).getTotalFlujoProyeccion().getProvbtiproy() || null == varProyCSM.get(i).getTotalFlujoProyeccion()){
						varProvbtiproy = BigDecimal.ZERO;
					}else{
						varProvbtiproy = varProyCSM.get(i).getTotalFlujoProyeccion().getProvbtiproy();
					}
					
					if (null == proyUmic.get(i).getTotalFlujoProyeccion().getProvbtiproy() || null == proyUmic.get(i).getTotalFlujoProyeccion()){
						varProvNF17 = BigDecimal.ZERO;
					}else{
						varProvNF17 = proyUmic.get(i).getTotalFlujoProyeccion().getProvbtiproy();
					}
				
					if (null == proyUmic.get(i).getRaumic()){
						varResultadoRA = BigDecimal.ZERO;
					}else{
						varResultadoRA = proyUmic.get(i).getRaumic(); 
					}	
					
					if(umic.getPrimas().getCformpago().equals(ConstantsModulos.CTE_CFORMPAG_UNICA)){
						prima = umic.getPrimas().getIprimanetaini();
					}else{
						if (umic.getPrimas().getCformpago().equals(ConstantsFunciones.CTE_FORMPAGO_4)){
							tipoPago = BigDecimal.valueOf(ConstantsFunciones.CTE_12);
						} else if (umic.getPrimas().getCformpago().equals(ConstantsFunciones.CTE_FORMPAGO_3)){
							tipoPago = BigDecimal.valueOf(ConstantsFunciones.CTE_4);
						} else if (umic.getPrimas().getCformpago().equals(ConstantsFunciones.CTE_FORMPAGO_1) || umic.getPrimas().getCformpago().equals(ConstantsFunciones.CTE_FORMPAGO_2)){
							tipoPago = new BigDecimal(umic.getPrimas().getCformpago());
						}
							
						/* Si no es estos tipos error de cartera - mostrar*/
						
						prima = umic.getPrimas().getIprimanetaini().divide(tipoPago,ConstantsFunciones.MATH_CONTEXT);
					}
	
					aux = varProvbtiproy.subtract(varProvNF17).subtract(varResultadoRA).add(prima);
					proyUmic.get(i).setCsmumic(aux);
				}

				almacenarDatos.almacenarProyeccion(proyUmic);
		} else {
			for (int i = 0; i < proyUmic.size(); i++) {
				proyUmic.get(i).setCsmumic(BigDecimal.ZERO);
			}

			almacenarDatos.almacenarProyeccion(proyUmic);
		}

		if (ProgramaCSM001.LOG.isTraceEnabled()) {
			ProgramaCSM001.LOG.trace("Fin función << calcularCSM >> de la clase ProgramaCSM001");
		}
	}
}