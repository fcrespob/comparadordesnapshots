/**
 * 
 */
package es.mapfre.solvencia.formulacion.programas;

import java.util.List;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * @author jiaguillo
 *
 */
public abstract class ProgramaFlujo extends ProgramaGenerico {

	/**
	 * Metodo que devuelve el tipo de elemento para encontrar el módulo de cálculo
	 */
	protected abstract String getTipoElemento();
	
	private Modulo modulo;
	
	protected Modulo getModuloCalculo() {
		return modulo;
	}

	private void setModuloCalculo(Modulo modulo) {
		this.modulo = modulo;
	}

	protected void modificarCorriente(Umic umic, FichaProceso fichaProceso,
			DetalleBaseTecnica detBaseTecnica,
			List<DetalleCorriente> detallesCorriente, String subProcesoActual) {
		// Por defecto no hace nada, se implementa en algunos casos donde la salida en ausencia del módulo no debe ser 0
	}


	@Override
	public void execute(Umic umic, FichaProceso fichaProceso,
			DetalleBaseTecnica detBaseTecnica,
			List<DetalleCorriente> detallesCorriente, String subProcesoActual)
			throws Solvencia2Excepcion {
		final IObtenerConfiguracion obtConf = FachadaServicios.getObtenerConfiguracion();
		final String varModuloCalc;
		if(detBaseTecnica.getBaseTec().equals(ConstantesSolvencia.BASE_NIIF17) ||
				detBaseTecnica.getBaseTec().equals(ConstantesSolvencia.BASE_NIF17LIR) ||
				detBaseTecnica.getBaseTec().equals(ConstantesSolvencia.BASE_N17LIRIN) ||
				detBaseTecnica.getBaseTec().equals(ConstantesSolvencia.BASE_NIFF17OCI) ||
				detBaseTecnica.getBaseTec().equals(ConstantesSolvencia.BASE_NIIF17IF) ||
				detBaseTecnica.getBaseTec().equals(ConstantesSolvencia.BASE_N17CLIR)){
			varModuloCalc  = obtConf.recuperarModulo(umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), 
					umic.getDatosAdicionales().getPrestCal(), ConstantesSolvencia.BASE_NIIF17, subProcesoActual, getTipoElemento());
			
		}else{
			varModuloCalc  = obtConf.recuperarModulo(umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), 
					umic.getDatosAdicionales().getPrestCal(), detBaseTecnica.getBaseTec(), subProcesoActual, getTipoElemento());
			
		}
		
		
		// Si no hay módulo de cálculo, entonces no se invoca a la lógica del programa
		if (null == varModuloCalc || varModuloCalc.isEmpty()/* || (!varModuloCalc.equals(ConstantsFactorias.MODULO_PMRR01) && subProcesoActual.equals(ConstantesSolvencia.CTE_PROY_PMRR))*/){
			modificarCorriente(umic, fichaProceso, detBaseTecnica, detallesCorriente, subProcesoActual);
		} else {
			setModuloCalculo(FactoriaModulos.getModulo(varModuloCalc));
			super.execute(umic, fichaProceso, detBaseTecnica, detallesCorriente,
					subProcesoActual);	
		}
	}


}
