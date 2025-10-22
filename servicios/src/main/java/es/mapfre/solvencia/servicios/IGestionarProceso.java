package es.mapfre.solvencia.servicios;

import es.mapfre.solvencia.dominio.parametrizacionGeneral.DisenoProcesos;

public interface IGestionarProceso {

	/**
	 * Almacenará los pagos de rentas calculados en base a la definción de la
	 * umic
	 * 
	 * @param proceso
	 * @param kcompania
	 * @param kramo
	 * @param kmodalidad
	 * @param kgarantia
	 * @param kbasetec
	 * @param kclaveadic
	 * @return
	 */
	DisenoProcesos obtenerDisenoProceso(String proceso, Integer kcompania, String kramo, Integer kmodalidad,
			Integer kgarantia, String kbasetec, String kclaveadic);
}
