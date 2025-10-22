package es.mapfre.solvencia.entregables.motor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.coherence.patterns.processing.task.ResumableTask;
import com.oracle.coherence.patterns.processing.task.TaskExecutionEnvironment;
import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DisenoProcesos;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.ElementoSubproceso;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.entregables.Entregable;
import es.mapfre.solvencia.entregables.services.FactoriaEntregables;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.servicios.IGestionarProceso;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.utils.BtUtils;

@Portable
public class ResumableEntregables implements ResumableTask {
	
	private static Logger LOG = LoggerFactory.getLogger(ResumableEntregables.class);
	
	@PortableProperty(0)
	private FichaProceso fichaProceso;
	
	private BtUtils btUtils = new BtUtils();
	private IGestionarProceso gestionarProceso = FachadaServicios.getGestionarProceso();
	
	public ResumableEntregables() {
		super();	
	}
	
	public ResumableEntregables(FichaProceso fichaProceso) {
		super();
		this.fichaProceso = fichaProceso;
	}
	
	
	@Override
	public Object run(TaskExecutionEnvironment oEnvironment) {
		
		 List<String> BTs = btUtils.getBts(fichaProceso.getCtipobt());
		 
		 for (String BT : BTs) {
			 LOG.info("Negocio : " + fichaProceso.getCnegocio() + " Ccanal : " + fichaProceso.getCcanal() + " kbasetec: " + BT);
			 buscarProceso(ConstantesSolvencia.CTE_ENTREGABLES, fichaProceso, BT);		
		 }	
		 
		
		
		return null;

	}
	
	/*
	 * Recorre un diseño de procesos ejecutando los procesos y subprocesos correspondientes a entregables
	 */
	public void buscarProceso(String proceso, FichaProceso fp, String kbasetec){
		DisenoProcesos disenoProceso;
		
		if (kbasetec.equals(ConstantsModulos.CTE_VAL_SCRMFE) ||
				kbasetec.equals(ConstantsModulos.CTE_VAL_SCRMMI) ||
				kbasetec.equals(ConstantsModulos.CTE_VAL_SCRLFE) ||
				kbasetec.equals(ConstantsModulos.CTE_VAL_SCRLMI) ||
				kbasetec.equals(ConstantsModulos.CTE_VAL_SCRMCF) ||
				kbasetec.equals(ConstantsModulos.CTE_VAL_SCRMCI) ||
				kbasetec.equals(ConstantsModulos.CTE_VAL_SCRINC) ||
				kbasetec.equals(ConstantsModulos.CTE_VAL_SCRGTO) ||
				kbasetec.equals(ConstantsModulos.CTE_VAL_SCRTIU) ||
				kbasetec.equals(ConstantsModulos.CTE_VAL_SCRTID) ||
				kbasetec.equals(ConstantsModulos.CTE_VAL_SCRAEP) ||
				kbasetec.equals(ConstantsModulos.CTE_VAL_SCRAEN) ||
				kbasetec.equals(ConstantsModulos.CTE_VAL_SCRAIP) ||
				kbasetec.equals(ConstantsModulos.CTE_VAL_SCRAIN) ||
				kbasetec.equals(ConstantsModulos.CTE_VAL_SCRANM)) {
			disenoProceso = gestionarProceso.obtenerDisenoProceso(proceso, fp.getCcanal(), null, null, null, ConstantsModulos.CTE_BT_SCR, null);
		} else {
			disenoProceso = gestionarProceso.obtenerDisenoProceso(proceso, fp.getCcanal(), null, null, null, kbasetec, null);
		}
		LOG.info(proceso);
			
		if(disenoProceso.getCgestespeci()!= null && !disenoProceso.getCgestespeci().isEmpty()) {				
			ejecutarEntregable(disenoProceso.getCgestespeci(), kbasetec);				
		}else {	
			for (ElementoSubproceso elementoSubproceso : disenoProceso.getElementosSubprocesos()) {
				if(elementoSubproceso.getSsubproc()) {
					buscarProceso(elementoSubproceso.getCelement(), fp, kbasetec);										
				} else {
					ejecutarEntregable(elementoSubproceso.getCelement(), kbasetec);
				}
			}
		}		
	}
	
	
	/*
	 * Ejecuta un Entregable
	 */
	private void ejecutarEntregable(String nombreEntregable, String kbasetec) {
		try {
			LOG.info(nombreEntregable);
			
			Entregable entregable = FactoriaEntregables.getEntregable(nombreEntregable);
		
			Long time1 = System.currentTimeMillis();
			
			entregable.execute(kbasetec, null);
			
			Long time2 = System.currentTimeMillis();
			
			LOG.info("Entregable {} calculado en {} ms",nombreEntregable,time2-time1);
			
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);			
		}
					
	}
	
}
