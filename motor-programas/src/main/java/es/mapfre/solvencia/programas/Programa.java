package es.mapfre.solvencia.programas;

import java.util.List;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.services.Servicio;



/**
 * Interfaz de los Programas
 * @author Indra
 *
 */
public interface Programa extends Servicio {
	
	/**
	 * @return El nombre del programa
	 */
	String getNombrePrograma();
	
	/**
	 * @param umic
	 * @param fp
	 * @param btc
	 * @param subProcesoActual
	 * @throws Exception
	 */
	void execute(Umic umic, FichaProceso fp, final DetalleBaseTecnica detalleBaseTecnica, final List<DetalleCorriente> detallesCorriente, String subProcesoActual) ;
	
}
