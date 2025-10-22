package es.mapfre.solvencia.formulacion.programas.impl;

import java.math.BigDecimal;
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
import es.mapfre.solvencia.formulacion.util.UtilProcesos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * @author NFQ
 *
 */
public class ProgramaCSM003 extends ProgramaGenerico {

	private static final String CLAVE_VA_PROVCSM = ConstantsModulos.CTE_VA_PROVCSM;
	private static final String CLAVE_VA_FECTRANSICION = ConstantsModulos.CTE_VA_FECTRANSICION;

	private static final Logger LOG = LoggerFactory.getLogger(ProgramaCSM003.class);

	@Override
	public void executeImpl(final Umic umic, final FichaProceso fichaProceso, final DetalleBaseTecnica btcUmic,
			final List<DetalleCorriente> proyUmic, final String codSubproceso) throws Solvencia2Excepcion {

		if (ProgramaCSM003.LOG.isTraceEnabled()) {
			ProgramaCSM003.LOG.trace("Inicio función << executeImpl >> de la clase ProgramaCSM003");
		}

		try {
			// llamamos al metodo calcularPatronAmortizacion
			calcularPatronAmortizacion(umic, fichaProceso, btcUmic, proyUmic, codSubproceso);
		} catch (Solvencia2Excepcion e) {
			ProgramaCSM003.LOG.error(e.getIncidencia().getTextoError(), e);
			UtilProcesos.exceptionControladaPrograma(btcUmic, umic, getNombrePrograma(), e);
		} catch (Exception e) {
			ProgramaCSM003.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ProgramaCSM003.LOG.isTraceEnabled()) {
			ProgramaCSM003.LOG.trace("Fin función << executeImpl >> de la clase ProgramaCSM003");
		}
	}

	/**
	 * Realiza el cálculo del Patrón de Amortización de CSM. Este programa, no
	 * ejecutará ningún cálculo para las bases técnicas que no sean NIIF17.
	 * 
	 * @param umic
	 * @param fichaProceso
	 * @param btcUmic
	 * @param proyUmic
	 * @param codSubproceso
	 */
	private void calcularPatronAmortizacion(final Umic umic, final FichaProceso fichaProceso,
			final DetalleBaseTecnica btcUmic, final List<DetalleCorriente> proyUmic, final String codSubproceso) {

		final IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
		final IObtenerDatos servicioDatos = FachadaServicios.getObtenerDatos();
		final IAlmacenarDatos almacenarDatos = FachadaServicios.getAlmacenarDatos();
		String varProvCSM;
		String varProvNIIF17;
		List<DetalleCorriente> varProyROSSPCSM;
		List<DetalleCorriente> varProyNIIF17;
		BigDecimal varCSM = BigDecimal.ZERO;
		BigDecimal varPatronCSM = BigDecimal.ZERO;
		BigDecimal varProvNF17 = BigDecimal.ZERO;
		BigDecimal varProvROSSPCSM = BigDecimal.ZERO;
		BigDecimal varResultadoRA = BigDecimal.ZERO;
		BigDecimal ra = BigDecimal.ZERO;
		BigDecimal raUmic = BigDecimal.ZERO;
		if (ProgramaCSM003.LOG.isTraceEnabled()) {
			ProgramaCSM003.LOG.trace("Inicio función << calcularPatronAmortizacion >> de la clase ProgramaCSM003");
		}
		
		if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIF17LIR)) {
			
			varProvCSM = ConstantsModulos.CTE_BT_ROSSPCSM;
			varProyROSSPCSM = servicioDatos.recuperarProyeccion(varProvCSM, btcUmic.getFecCierre(), umic.getKey());
			
			varProvNIIF17 = ConstantsModulos.CTE_BT_NIF17LIR;	
			varProyNIIF17 = servicioDatos.recuperarProyeccion(varProvNIIF17, btcUmic.getFecCierre(), umic.getKey());
			BigDecimal aux = BigDecimal.ZERO;
			for (int i = 0; i < varProyROSSPCSM.size(); i++) {
					
				varProvROSSPCSM = varProyROSSPCSM.get(i).getTotalFlujoProyeccion().getProvbtiproy();
				
				//Guardar en detalle
					
				//varResultadoRA = proyUmic.get(i).getRaumic();
				raUmic = umic.getDatosNiif17().getproxyra().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
				varResultadoRA = varProyNIIF17.get(i).getTotalFlujoProyeccion().getProvbtiproy().multiply(raUmic);
				
				varProvNF17 = varProyNIIF17.get(i).getTotalFlujoProyeccion().getProvbtiproy();
				
				ra = BigDecimal.ONE.add(raUmic);
				aux = varProvNF17.multiply(ra);
				
				varCSM = varProvROSSPCSM.subtract(aux);
				
				proyUmic.get(i).setRaumic(varResultadoRA);
				proyUmic.get(i).setRosspCSM(varCSM);
				proyUmic.get(i).setProvBelCSM(varProvNF17);
				proyUmic.get(i).setProvRosspCSM(varProvROSSPCSM);
			}
 
			almacenarDatos.almacenarProyeccion(proyUmic);

		}

		if (ProgramaCSM003.LOG.isTraceEnabled()) {
			ProgramaCSM003.LOG.trace("Fin función << calcularPatronAmortizacion >> de la clase ProgramaCSM003");
		}
	}

	@Override
	public String getNombrePrograma() {
		return ConstantsFactorias.PROGRAMA_CSM003;
	}

}
