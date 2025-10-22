package es.mapfre.solvencia.ficheros;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.data.Exporter;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DisenoProcesos;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.ElementoSubproceso;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.files.FileDescriptor;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.servicios.IGestionarProceso;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.utils.BtUtils;

public class EntregablesExporter implements Exporter {
	private static Logger log = LoggerFactory.getLogger(EntregablesExporter.class);
	
	private static final String NOMBRE_EXPORTER = "ENTREGABLES";

	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config-out.xml";
	
	private BtUtils btUtils = new BtUtils();
	private IGestionarProceso gestionarProceso = FachadaServicios.getGestionarProceso();
	ArrayList<String> daos = new ArrayList<String>();

	
	@Override
	public String getNombreServicio() {
		return NOMBRE_EXPORTER;
	}

	@Override
	public List<FileDescriptor> getFileDescriptors(String rutaBase, String fecCierre, FichaProceso fichaProceso) {
		return getFileDescriptors(rutaBase, fichaProceso, ConstantesSolvencia.CTE_ENTREGABLES);
	}
	
	public List<FileDescriptor> getFileDescriptors(String rutaBase, FichaProceso fichaProceso, String proceso) {
		List<FileDescriptor> descriptores = new ArrayList<FileDescriptor>();

		 // Obtener Daos de entregables a exportar para una ficha de proceso
		 List<String> BTs = btUtils.getBts(fichaProceso.getCtipobt());
		 for (String bt : BTs) {
			getDaosEntregables(proceso, fichaProceso, bt);
		 }

		for (int i = 0; i < daos.size(); i++) {
			String nombref = btUtils.getCargaFicherosProperty(daos.get(i));
			FileDescriptor descriptor = new FileDescriptor(nombref, daos.get(i), BEANIO_CONFIG_XML,ConstantsFunciones.CTE_2);
			descriptores.add(descriptor);
		}
		if (fichaProceso.getKsistema().equals(ConstantesSolvencia.CTE_SISTEMA) && fichaProceso.getKprotecnico().equals(ConstantesSolvencia.CTE_PROYECTO_TECNICO) && fichaProceso.getKuejecucion().equals(ConstantesSolvencia.CTE_UMIC_EJECUCION)) {
			FileDescriptor aux = new FileDescriptor("FLUJOSTOTP", "flujostotp", BEANIO_CONFIG_XML,ConstantsFunciones.CTE_2);
			descriptores.add(aux);
		}
		
		return descriptores;
	}

	/*
	 * Obtener los daos de los entregables a exportar para una ficha de proceso
	 */
	private ArrayList<String> getDaosEntregables(String proceso, FichaProceso fichaProceso, String bt) {
		DisenoProcesos disenoProceso;
		
		try {
			if (bt.equals(ConstantsModulos.CTE_VAL_SCRMFE) ||
					bt.equals(ConstantsModulos.CTE_VAL_SCRMMI) ||
					bt.equals(ConstantsModulos.CTE_VAL_SCRLFE) ||
					bt.equals(ConstantsModulos.CTE_VAL_SCRLMI) ||
					bt.equals(ConstantsModulos.CTE_VAL_SCRMCF) ||
					bt.equals(ConstantsModulos.CTE_VAL_SCRMCI) ||
					bt.equals(ConstantsModulos.CTE_VAL_SCRINC) ||
					bt.equals(ConstantsModulos.CTE_VAL_SCRGTO) ||
					bt.equals(ConstantsModulos.CTE_VAL_SCRTIU) ||
					bt.equals(ConstantsModulos.CTE_VAL_SCRTID) ||
					bt.equals(ConstantsModulos.CTE_VAL_SCRAEP) ||
					bt.equals(ConstantsModulos.CTE_VAL_SCRAEN) ||
					bt.equals(ConstantsModulos.CTE_VAL_SCRAIP) ||
					bt.equals(ConstantsModulos.CTE_VAL_SCRAIN) ||
					bt.equals(ConstantsModulos.CTE_VAL_SCRANM)) {
				disenoProceso = gestionarProceso.obtenerDisenoProceso(proceso, fichaProceso.getCcanal(), null, null, null, ConstantsModulos.CTE_BT_SCR, null);
			} else {
				disenoProceso = gestionarProceso.obtenerDisenoProceso(proceso, fichaProceso.getCcanal(), null, null, null, bt, null);
			}
			 
			if(disenoProceso.getCgestespeci()!= null && !disenoProceso.getCgestespeci().isEmpty()) {
				if (!daos.contains(disenoProceso.getCgestespeci().toLowerCase())) {
					daos.add(disenoProceso.getCgestespeci().toLowerCase());
				}
			}else {	
				for (ElementoSubproceso elementoSubproceso : disenoProceso.getElementosSubprocesos()) {
					if(elementoSubproceso.getSsubproc()) {
						getDaosEntregables(elementoSubproceso.getCelement(), fichaProceso, bt);
					} else {
						if (!daos.contains(elementoSubproceso.getCelement().toLowerCase())) {
							daos.add(elementoSubproceso.getCelement().toLowerCase());
						}
					}
				}
			}
		} catch (Exception e) {
			log.warn("Error buscando daos para exportar:", e);
		}
		
		return daos;
	}
}
	


