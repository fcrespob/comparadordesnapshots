package es.mapfre.scr.gastos.modulos;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.scr.gastos.dao.entidades.IPCGeneralFuturoDao;
import es.mapfre.scr.gastos.dao.entidades.ValoresEstresDao;
import es.mapfre.scr.gastos.dominio.entidades.IPCGeneralFuturo;
import es.mapfre.scr.gastos.dominio.entidades.Incidencia;
import es.mapfre.scr.gastos.dominio.entidades.SalidaIPC;
import es.mapfre.scr.gastos.dominio.entidades.ValoresEstres;
import es.mapfre.scr.gastos.excepcion.Solvencia2Excepcion;
import es.mapfre.scr.gastos.gestores.GestorFicheroSalida;
import es.mapfre.scr.gastos.gestores.GestorIncidencias;
import es.mapfre.scr.gastos.utils.ConstantesSolvencia;
import net.sf.ehcache.Element;

public class ModuloSCRIPC {

	private static final Logger LOG = LoggerFactory.getLogger(ModuloSCRIPC.class);
	private static BigDecimal valEstres;
	
	public String getNombreServicio() {
		return "ModuloSCRIPC";
	}
	
	public void execute(String fecha) {
		
		// Se obtiene la fecha del día
		GestorIncidencias gi = GestorIncidencias.getInstance(ConstantesSolvencia.CTE_IPC);
		GestorFicheroSalida gfs = GestorFicheroSalida.getInstance(ConstantesSolvencia.CTE_IPC);
		IPCGeneralFuturo ipc;

		try {
			if (ModuloSCRIPC.LOG.isTraceEnabled()) {
				ModuloSCRIPC.LOG.trace("Inicio de execute en clase ModuloSCRIPC");
			}
			
			Collection<Element> ipcList = recuperarIPC();

			if (null == ipcList || ipcList.isEmpty()){
				Incidencia inci = new Incidencia();
				inci.setBt(ConstantesSolvencia.BASETEC_GTO);
				inci.setFecCierre(fecha);
				inci.setCodigoRetorno("09");
				inci.setInfAmpliada("No existen valores de ipc a estresar para la fecha indicada.");
				gi.write(inci);	
				throw new Solvencia2Excepcion("No existen valores de ipc a estresar para la fecha indicada." , inci);
			} 
			
			ValoresEstresDao valoresEstres = new ValoresEstresDao();
			List<ValoresEstres> valores = valoresEstres.obtenerValoresEstres(fecha, ConstantesSolvencia.BASETEC_GTO);

			if (null == valores || valores.isEmpty()){
				Incidencia inci = new Incidencia();
				inci.setBt(ConstantesSolvencia.BASETEC_GTO);
				inci.setFecCierre(fecha);
				inci.setCodigoRetorno("09");
				inci.setInfAmpliada("No existen datos de estrés para la fecha de cierre indicada.");
				gi.write(inci);	
				throw new Solvencia2Excepcion("No existen datos de estrés para la fecha de cierre indicada." , inci);
			}
			if (valores.get(0).getVariable().equals(ConstantesSolvencia.CTE_IPC)){
				valEstres = valores.get(0).getValor();
			} else if (valores.get(1).getVariable().equals(ConstantesSolvencia.CTE_IPC)){
				valEstres = valores.get(1).getValor();
			} else {
				Incidencia inci = new Incidencia();
				inci.setBt(ConstantesSolvencia.BASETEC_GTO);
				inci.setFecCierre(fecha);
				inci.setCodigoRetorno("09");
				inci.setInfAmpliada("No existen datos de estrés de IPC para la fecha de cierre indicada.");
				gi.write(inci);	
				throw new Solvencia2Excepcion("No existen datos de estrés de IPC para la fecha de cierre indicada." , inci);
			}
			
			Iterator<Element> it= ipcList.iterator();
			while(it.hasNext()){
				ipc = (IPCGeneralFuturo) it.next().getObjectValue();
				gfs.write(estresarIPCGasto(ipc, valEstres));
			}
			
		} catch(Solvencia2Excepcion solvExc){
			ModuloSCRIPC.LOG.error(solvExc.getMessage(), solvExc);
			gi.write(solvExc.getIncidencia());
			throw new Solvencia2Excepcion(solvExc.getIncidencia().getInfAmpliada() , solvExc.getIncidencia());
		} catch (Exception e) {
			Incidencia inci = new Incidencia();
			inci.setFecCierre(fecha);
			inci.setBt(ConstantesSolvencia.BASETEC_GTO);
			inci.setCodigoRetorno("02");
			inci.setInfAmpliada("Error genérico en la conversión del ipc de gastos.");
			gi.write(inci);	
			throw new Solvencia2Excepcion("Error genérico en la conversión del ipc de gastos." , inci);
		}
	}

	private SalidaIPC estresarIPCGasto(IPCGeneralFuturo ipc, BigDecimal valEstres2) {
		
		BigDecimal resultado;
		String formato;
		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setDecimalSeparator('.');
		DecimalFormat dfFormatter = new DecimalFormat("+000.00;-000.00",simbolos);
		
		SalidaIPC elementoSalida = new SalidaIPC();
		elementoSalida.setFinicio(ipc.getFinicio());
		elementoSalida.setFfin(ipc.getFfin());
		elementoSalida.setPipcleg(dfFormatter.format(ipc.getPipcleg()).replace(".", ""));
		
		resultado =  ipc.getPipcgas().add(valEstres);
		
		formato = dfFormatter.format(resultado.setScale(2, BigDecimal.ROUND_HALF_UP));
		elementoSalida.setPipcgas(formato.replace(".", ""));
		
		return elementoSalida;
	}

	private Collection<Element> recuperarIPC() {
		
		IPCGeneralFuturoDao dao = new IPCGeneralFuturoDao();
		Collection<Element> ipc = dao.values();
		
		if(ipc == null || ipc.isEmpty()){
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("05");
			inci.setInfAmpliada("No se han encontrado registros a estresar.");
			throw new Solvencia2Excepcion(inci);
		}
		
		return ipc;
	}
}