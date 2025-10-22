package es.mapfre.solvencia.formulacion.programas.impl;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
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
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * @author NFQ
 *
 */
public class ProgramaRA001 extends ProgramaGenerico {

	private static final Logger LOG = LoggerFactory.getLogger(ProgramaRA001.class);

	@Override
	protected void executeImpl(final Umic umic, final FichaProceso fichaProceso, final DetalleBaseTecnica btcUmic,
			final List<DetalleCorriente> proyUmic, final String codSubproceso) throws Solvencia2Excepcion {

		if (ProgramaRA001.LOG.isTraceEnabled()) {
			ProgramaRA001.LOG.trace("Inicio función << executeImpl >> de la clase ProgramaRA001");
		}

		try {
			// Llamamos a la función que obtiene el importe del flujo actualizado
			calcularAjusteDeRiesgo(umic, fichaProceso, btcUmic, proyUmic, codSubproceso);
		} catch (Solvencia2Excepcion e) {
			ProgramaRA001.LOG.error(e.getIncidencia().getTextoError(), e);
			UtilProcesos.exceptionControladaPrograma(btcUmic, umic, getNombrePrograma(), e);
		} catch (Exception e) {
			ProgramaRA001.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ProgramaRA001.LOG.isTraceEnabled()) {
			ProgramaRA001.LOG.trace("Fin función << executeImpl >> de la clase ProgramaRA001");
		}

	}

	/**
	 * 
	 * Realiza el ajuste de riesgo en la BT NIIF17. Este programa, no ejecutará
	 * ningún cálculo para las bases técnicas que no sean NIIF17.
	 * 
	 * @param umic
	 * @param fichaProceso
	 * @param detalleBT
	 * @param detallesCorriente
	 * @param subProcesoActual
	 */
	private void calcularAjusteDeRiesgo(Umic umic, FichaProceso fichaProceso, DetalleBaseTecnica btcUmic,
			List<DetalleCorriente> proyUmic, String codSubproceso) {

		BigDecimal varRa = BigDecimal.ZERO;
		BigDecimal auxCalculo = BigDecimal.ZERO;
	
		final IAlmacenarDatos almacenarDatos = FachadaServicios.getAlmacenarDatos();

		if (btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NIIF17) ||
				btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NIF17LIR) ||
				btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_N17LIRIN) ||
				btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NIFF17OCI) ||
				btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NIIF17IF) ||
				btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_N17CLIR)) {

			for (int i = 0; i < proyUmic.size(); i++) {

				auxCalculo = proyUmic.get(i).getTotalFlujoProyeccion().getSumfprobtanul().setScale(2, BigDecimal.ROUND_HALF_EVEN);

				varRa = auxCalculo.multiply(umic.getDatosNiif17().getproxyra());

				proyUmic.get(i).setRaumic(varRa);
			}
			
			almacenarDatos.almacenarProyeccion(proyUmic);
		}
	}

	@Override
	public String getNombrePrograma() {
		return ConstantsFactorias.PROGRAMA_RA001;
	}
}