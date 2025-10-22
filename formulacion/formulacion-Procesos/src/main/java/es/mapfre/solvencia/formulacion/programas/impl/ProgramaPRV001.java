package es.mapfre.solvencia.formulacion.programas.impl;

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
import es.mapfre.solvencia.formulacion.util.UtilProcesos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * El programa PRV001 – CALCULO DE LA PROVISION MATEMATICA se encargará de calcular el importe final de la provisión matemática
 * a partir de la información de todas las corrientes calculadas, en base al supuesto de base técnica marcado.
 * 
 * Calcula también los totales por umic y BTC de la estructura de salida totalFlujosUmic,
 * además de los totales del bloque totalFlujoProyeccion de la proyección calculada proyUmic
 * 
 * @author jguijarro
 */
public class ProgramaPRV001 extends ProgramaGenerico {

	private static final Logger LOG = LoggerFactory.getLogger(ProgramaPRV001.class);

	@Override
	public void executeImpl(final Umic umic, final FichaProceso fichp, final DetalleBaseTecnica detalleBT, final List<DetalleCorriente> detallesCorriente, final String subProcesoActual) throws Solvencia2Excepcion {
		
		if (ProgramaPRV001.LOG.isTraceEnabled()) {
			ProgramaPRV001.LOG.trace("Inicio función << executeImpl >> de la clase ProgramaPRV001");
		}
		
		try {
			// Llamamos a la función obtenerProvisionMatematica
			obtenerProvisionMatematica(umic, fichp, detalleBT, detallesCorriente);
		} catch (Solvencia2Excepcion e) {
			ProgramaPRV001.LOG.error(e.getIncidencia().getTextoError(), e);
			UtilProcesos.exceptionControladaPrograma(detalleBT, umic, getNombrePrograma(), e);
		} catch (Exception e) {
			ProgramaPRV001.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ProgramaPRV001.LOG.isTraceEnabled()) {
			ProgramaPRV001.LOG.trace("Fin función << executeImpl >> de la clase ProgramaPRV001");
		}
		
	}
	
	@Override
	public String getNombrePrograma() {
		return ConstantsFactorias.PROGRAMA_PRV001;
	}
	
	private void obtenerProvisionMatematica(final Umic umic, final FichaProceso fichaProceso, final DetalleBaseTecnica detalleBT,
			final List<DetalleCorriente> detallesCorriente) {
		
		//Variables locales
		Modulo moduloProvMat = null;
		//Fin variables locales
		
		//Obtenemos el módulo de provisión matemática a recuperar
		moduloProvMat = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_PROV_MAT);
		moduloProvMat.execute(detallesCorriente, fichaProceso.getFcalc(), umic, detalleBT, this.getMapVariables());
	}
}
