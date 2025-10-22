package es.mapfre.solvencia.formulacion.modulos.impl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.gbt.util.ConstantesGBT;
import es.mapfre.solvencia.gbt.util.ConstantesModulosGBT;
import es.mapfre.solvencia.modulos.Modulo;


public class J880GBT999OrdenesProceso implements Modulo {
	
	private static final Logger LOG = LoggerFactory.getLogger(J880GBT999OrdenesProceso.class);
	
	@Override
	public Object execute(Object... args) {
		//Calculo de base tecnica
		DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantesGBT.PARAM_BTC];
		Umic umic = (Umic) args[ConstantesGBT.PARAM_UMI_BASE_TEC];
		Boolean simulacion = (Boolean) args[ConstantesGBT.PARAM_SIMUL];
		
		if( J880GBT999OrdenesProceso.LOG.isErrorEnabled() ){
			J880GBT999OrdenesProceso.LOG.trace("INICIO - Módulo " + getNombreServicio());
			J880GBT999OrdenesProceso.LOG.trace("DetalleBaseTecnica: " + btcUmic);
			J880GBT999OrdenesProceso.LOG.trace("Umic: " + umic);
			J880GBT999OrdenesProceso.LOG.trace("Simulación: " + simulacion);
			J880GBT999OrdenesProceso.LOG.trace("FIN - Módulo " + getNombreServicio());
		}
		
		return btcUmic;
	}
	
	@Override
	public String getNombreServicio() {
		return ConstantesModulosGBT.MOD_OP;
	}
	
}