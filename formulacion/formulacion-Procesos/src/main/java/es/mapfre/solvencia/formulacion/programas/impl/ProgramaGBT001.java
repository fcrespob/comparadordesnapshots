package es.mapfre.solvencia.formulacion.programas.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.excepcionGBT.GestorBasesTecnicasException;
import es.mapfre.solvencia.formulacion.programas.ProgramaGenerico;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.UtilProcesos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * El programa GBT001– ESTABLECIMIENTO DE LA BASE TECNICA DE CALCULO es el encargado de establecer la base técnica de cálculo
 * necesaria para realizar las operaciones que obtienen los flujos de las distintas corrientes.
 * Como resultado obtendremos una estructura de datos con el detalle de la base técnica de cálculo, btcUmic.
 * 
 * @author jguijarro
 */
public class ProgramaGBT001 extends ProgramaGenerico {

	private static final Logger LOG = LoggerFactory.getLogger(ProgramaGBT001.class);
	
	@Override
	public void executeImpl(final Umic umic, final FichaProceso fichp, final DetalleBaseTecnica detalleBT, final List<DetalleCorriente> detallesCorriente, final String subProcesoActual) throws Solvencia2Excepcion {
		
		try {
			
			if (ProgramaGBT001.LOG.isTraceEnabled()) {
				ProgramaGBT001.LOG.trace("Inicio función << executeImpl >> de la clase ProgramaGBT001");
			}
			
			// Realizar la conversion de BTI a ROSSP o BEL
			baseTecnica(umic, detalleBT, ConstantesSolvencia.TIPO_EJECUCION_SIMULACION.equals(fichp.getCtipoejec()));
		
		} catch (GestorBasesTecnicasException gbte) {
			ProgramaGBT001.LOG.error(gbte.getTextoError(), gbte);
			Solvencia2Excepcion s2e = Solvencia2ExcepcionHelper.crearExcepcion(gbte.getCodigoRetorno(), null, gbte.getGeneradorError(), detalleBT.getBaseTec(), umic.getDatosGenerales().getCcanal(), umic.getDatosGenerales().getCcartera(), umic.getKey(), umic.getDatosGenerales().getFecCierre(), umic.getDatosGenerales().getCnegocio(), gbte);
			throw s2e;
		} catch (Solvencia2Excepcion e) {
			ProgramaGBT001.LOG.error(e.getIncidencia().getTextoError(), e);
			UtilProcesos.exceptionControladaPrograma(detalleBT, umic, getNombrePrograma(), e);
		} catch (Exception e) {
			ProgramaGBT001.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ProgramaGBT001.LOG.isTraceEnabled()) {
			ProgramaGBT001.LOG.trace("Fin función << executeImpl >> de la clase ProgramaGBT001");
		}
			
	}
	
	/**
	 * @param umic
	 * 				Contiene los datos de la Umic que se está procesando.
	 * @param detalleBT
	 * 				Contiene el detalle de la base técnica de cálculo para la umic.
	 * @param indicadorSimulacion
	 * 				Indica si es una ejecución en simulación (true) o no simulada (false).
	 */
	private void baseTecnica(final Umic umic, final DetalleBaseTecnica detalleBT, final Boolean indicadorSimulacion) {
		//Variables locales
		Modulo moduloBaseTecnica = null;
		final IAlmacenarDatos almacenarDatos = FachadaServicios.getAlmacenarDatos();
		//Fin variables locales
		// TODO: eliminar cuando MAPFRE confirme que los fallos se han solucionado
		modificarUmicPorFallosGeneracionCartera(umic);

		//Obtenemos el módulo de base tecnica a recuperar
		moduloBaseTecnica = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_BASE_TEC);
		moduloBaseTecnica.execute(detalleBT, umic, indicadorSimulacion);
		
		almacenarDatos.almacenarDetalleBaseTecnica(detalleBT);
	}
	
	private void modificarUmicPorFallosGeneracionCartera(Umic umic) {
		// Se establece al 5 el porcentaje de revalorización de la renta en la modalidad 209
		if (ArrayUtils.contains(ConstantsModulos.FAMILIA_209, umic.getDatosGenerales().getKmodalidad())) {
			umic.getCapitales().setPorevalcap(BigDecimal.valueOf(5));
		}

		// Se homogeniza la forma y tipo de revalorización de la renta
		else if (ConstantsModulos.CTE_NEGOCIO_INDIVIDUAL.equals(umic.getDatosGenerales().getCnegocio())) {
			umic.getRentas().setCtipoRevrenta(umic.getRentas().getCformaRevrenta());
			umic.getRentas().setCformaRevrenta(null);
		}
	}
	
	@Override
	public String getNombrePrograma() {
		return ConstantsFactorias.PROGRAMA_GBT001;
	}
}
