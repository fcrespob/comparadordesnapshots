/* MODIFICACION:TAR00302248-Contabilidad SolvenciaII en SSAA 
   FECHA: 25/09/2017
   AUTOR: INDRA
 */
package es.mapfre.solvencia.servicios.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.DefinicionProcesosDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.DisenoProcesosDao;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionProcesos;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DisenoProcesos;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.ElementoSubproceso;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.servicios.IGestionarProceso;

public class GestionarProceso implements IGestionarProceso {
	
	private DefinicionProcesosDao definicionProcesosDao = new DefinicionProcesosDao();
	private DisenoProcesosDao disenoProcesosDao = new DisenoProcesosDao();
	
	
	/* (non-Javadoc)
	 * @see es.mapfre.solvencia.servicios.IGestionarProceso#obtenerDisenoProceso(java.lang.String, java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String)
	 */
	@Override
	public DisenoProcesos obtenerDisenoProceso(String proceso,
			Integer kcompania, String kramo, Integer kmodalidad,
			Integer kgarantia, String kbasetec, String kclaveadic)  { //NOSONAR
		
		DefinicionProcesos defProceso = definicionProcesosDao.get(proceso);
		
		DisenoProcesos disenoProceso = null;
		List<DisenoProcesos> disenoProcesos = null;
		
		if ((defProceso!=null) && (defProceso.getKestado())) {
			// Caso de proceso base-tec, se buscará solo por kbastec y kcompania
			// Entregables : para los subprocesos se busca por kbasetec y kcompania
			///*TAR00302248- se añade el entregable de CONTAB*/ 
			if (proceso.equalsIgnoreCase(ConstantesSolvencia.CTE_BASE_TEC)
					|| proceso.equalsIgnoreCase(ConstantesSolvencia.CTE_PRVAGREGA)
					|| ((kbasetec.equals(ConstantesSolvencia.BASE_SCRVM) || kbasetec.equals(ConstantesSolvencia.BASE_SCR) || kbasetec.equals(ConstantesSolvencia.BASE_NIIF17) ||
							kbasetec.equals(ConstantesSolvencia.BASE_NIF17LIR) ||
							kbasetec.equals(ConstantesSolvencia.BASE_N17LIRIN) ||
							kbasetec.equals(ConstantesSolvencia.BASE_NIFF17OCI) ||
							kbasetec.equals(ConstantesSolvencia.BASE_NIIF17IF) ||
							kbasetec.equals(ConstantesSolvencia.BASE_N17CLIR)) &&
							(proceso.equalsIgnoreCase(ConstantesSolvencia.CTE_ENTREGABLES)))
					|| proceso.equalsIgnoreCase(ConstantesSolvencia.CTE_FLUJOTOT)
					|| proceso.equalsIgnoreCase(ConstantesSolvencia.CTE_PROVICOA)
					|| proceso.equalsIgnoreCase(ConstantesSolvencia.CTE_SSAA)){
				
				if(kbasetec.equals(ConstantesSolvencia.BASE_NIIF17) ||
						kbasetec.equals(ConstantesSolvencia.BASE_NIF17LIR) ||
						kbasetec.equals(ConstantesSolvencia.BASE_N17LIRIN) ||
						kbasetec.equals(ConstantesSolvencia.BASE_NIFF17OCI) ||
						kbasetec.equals(ConstantesSolvencia.BASE_NIIF17IF) ||
						kbasetec.equals(ConstantesSolvencia.BASE_N17CLIR)){
					disenoProcesos = disenoProcesosDao.obtenerDisenoProcesos(proceso,kcompania, ConstantesSolvencia.BASE_NIIF17);
				}else{
					if (kbasetec.equals(ConstantesSolvencia.BASE_ROSSPTE)
							|| kbasetec.equals(ConstantesSolvencia.BASE_ROSSPTI)
							|| kbasetec.equals(ConstantesSolvencia.BASE_ROSSPGA)) {
						disenoProcesos = disenoProcesosDao.obtenerDisenoProcesos(proceso,kcompania, ConstantesSolvencia.BASE_ROSSP);
					} else {
						disenoProcesos = disenoProcesosDao.obtenerDisenoProcesos(proceso,kcompania, kbasetec);
					}
				}
				
			} else {		
				if(kbasetec.equals(ConstantesSolvencia.BASE_NIIF17) ||
						kbasetec.equals(ConstantesSolvencia.BASE_NIF17LIR) ||
						kbasetec.equals(ConstantesSolvencia.BASE_N17LIRIN) ||
						kbasetec.equals(ConstantesSolvencia.BASE_NIFF17OCI) ||
						kbasetec.equals(ConstantesSolvencia.BASE_NIIF17IF) ||
						kbasetec.equals(ConstantesSolvencia.BASE_N17CLIR)){
				disenoProcesos = disenoProcesosDao.obtenerDisenoProcesos(proceso,kcompania, kramo, kmodalidad, 
					kgarantia, ConstantesSolvencia.BASE_NIIF17, kclaveadic);
				}else{
					if (kbasetec.equals(ConstantesSolvencia.BASE_ROSSPTE)
							|| kbasetec.equals(ConstantesSolvencia.BASE_ROSSPTI)
							|| kbasetec.equals(ConstantesSolvencia.BASE_ROSSPGA)) {
						disenoProcesos = disenoProcesosDao.obtenerDisenoProcesos(proceso,kcompania, kramo, kmodalidad, 
								kgarantia, ConstantesSolvencia.BASE_ROSSP, kclaveadic);
					}else if (kbasetec.equals(ConstantesSolvencia.BASE_SCRTIU) || kbasetec.equals(ConstantesSolvencia.BASE_SCRTID) 
							|| kbasetec.equals(ConstantesSolvencia.BASE_SCRGTO) || kbasetec.equals(ConstantesSolvencia.BASE_SCRMFE)
							|| kbasetec.equals(ConstantesSolvencia.BASE_SCRMMI) || kbasetec.equals(ConstantesSolvencia.BASE_SCRMCF)
							|| kbasetec.equals(ConstantesSolvencia.BASE_SCRMCI) || kbasetec.equals(ConstantesSolvencia.BASE_SCRLFE)
							|| kbasetec.equals(ConstantesSolvencia.BASE_SCRLMI) || kbasetec.equals(ConstantesSolvencia.BASE_SCRINC)
							|| kbasetec.equals(ConstantesSolvencia.BASE_SCRVM) || kbasetec.equals(ConstantesSolvencia.BASE_SCRAEN)
							|| kbasetec.equals(ConstantesSolvencia.BASE_SCRAEP) || kbasetec.equals(ConstantesSolvencia.BASE_SCRAIN)
							|| kbasetec.equals(ConstantesSolvencia.BASE_SCRAIP) || kbasetec.equals(ConstantesSolvencia.BASE_SCRANM)
							|| kbasetec.equals(ConstantesSolvencia.BASE_SCR)) {
						disenoProcesos = disenoProcesosDao.obtenerDisenoProcesos(proceso,kcompania, ConstantesSolvencia.BASE_BEL);
					} else {
						disenoProcesos = disenoProcesosDao.obtenerDisenoProcesos(proceso,kcompania, kramo, kmodalidad, 
								kgarantia, kbasetec, kclaveadic);
					}
				}
				
				if (proceso.equalsIgnoreCase(ConstantesSolvencia.CTE_PROYECCION)){
					if (disenoProcesos != null && disenoProcesos.size() > 0) {
						List<ElementoSubproceso> hijos = disenoProcesos.get(0).getElementosSubprocesos();
						if( 	!hijos.get(0).getCelement().equals(ConstantesSolvencia.CTE_BASE_TEC)
								|| (hijos.size() > 1 && !hijos.get(1).getCelement().equals(ConstantesSolvencia.CTE_PERIODOS))	
								|| hijos.size() < 3
							){
							
							throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_ERROR_06, new String[]{});
						}
					}
				}
			}
		} else {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_ERROR_02, new String[]{proceso.toUpperCase()});
		}
		
		if (disenoProcesos == null || disenoProcesos.isEmpty()) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_ERROR_03, new String[]{proceso.toUpperCase()});
		}
		
		if (disenoProcesos.size() > 1) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.CTE_ERROR_03, new String[]{proceso.toUpperCase()});
		}
		
		if (disenoProcesos != null && disenoProcesos.size() > 0) {
			disenoProceso = disenoProcesos.get(0);
		}
		
//		if(proceso.equalsIgnoreCase(ConstantesSolvencia.CTE_FLUJOTOT)){
//			
//			if(kbasetec.equals(ConstantesSolvencia.BASE_NIIF17) ||
//				kbasetec.equals(ConstantesSolvencia.BASE_N17LIRIN) ||
//				kbasetec.equals(ConstantesSolvencia.BASE_NIIF17IF)) {
//				
//				List<ElementoSubproceso> elementosSubprocesos = new ArrayList<ElementoSubproceso>();
//				ElementoSubproceso flujinf3 = new ElementoSubproceso();
//				flujinf3.setCelement("FLUJINF3");
//				flujinf3.setSsubproc(false);
//				ElementoSubproceso flujinf4 = new ElementoSubproceso();
//				flujinf4.setCelement("FLUJINF4");
//				flujinf4.setSsubproc(false);
//				ElementoSubproceso fpsl = new ElementoSubproceso();
//				fpsl.setCelement("FPSL");
//				fpsl.setSsubproc(false);
//				ElementoSubproceso totn17 = new ElementoSubproceso();
//				totn17.setCelement("FLUJOSTN17");
//				totn17.setSsubproc(false);
//				ElementoSubproceso totpv = new ElementoSubproceso();
//				totpv.setCelement("FLUJOSTOTPV");
//				totpv.setSsubproc(false);
//				elementosSubprocesos.add(flujinf3);
//				elementosSubprocesos.add(flujinf4);
//				elementosSubprocesos.add(fpsl);
//				elementosSubprocesos.add(totn17);
//				elementosSubprocesos.add(totpv);
//				disenoProceso.setElementosSubprocesos(elementosSubprocesos );
//			}else if(
//				
//				kbasetec.equals(ConstantesSolvencia.BASE_NIF17LIR) ||
//				kbasetec.equals(ConstantesSolvencia.BASE_NIFF17OCI) ||
//				kbasetec.equals(ConstantesSolvencia.BASE_N17CLIR)){
//				List<ElementoSubproceso> elementosSubprocesos = new ArrayList<ElementoSubproceso>();
//				disenoProceso.setElementosSubprocesos(elementosSubprocesos );
//			}
//		}
		
		return disenoProceso;
	}
}
