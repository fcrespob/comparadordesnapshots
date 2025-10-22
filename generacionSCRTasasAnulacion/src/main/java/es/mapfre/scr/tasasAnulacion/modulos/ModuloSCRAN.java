package es.mapfre.scr.tasasAnulacion.modulos;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.scr.tasasAnulacion.dao.entidades.TasasAnulacionDao;
import es.mapfre.scr.tasasAnulacion.dao.entidades.ValoresEstresDao;
import es.mapfre.scr.tasasAnulacion.dominio.entidades.Incidencia;
import es.mapfre.scr.tasasAnulacion.dominio.entidades.Salida;
import es.mapfre.scr.tasasAnulacion.dominio.entidades.TasasAnulacion;
import es.mapfre.scr.tasasAnulacion.dominio.entidades.ValoresEstres;
import es.mapfre.scr.tasasAnulacion.excepcion.Solvencia2Excepcion;
import es.mapfre.scr.tasasAnulacion.gestores.GestorFicheroSalida;
import es.mapfre.scr.tasasAnulacion.gestores.GestorIncidencias;
import es.mapfre.scr.tasasAnulacion.utils.ConstantesSolvencia;

public class ModuloSCRAN {

	private static final Logger LOG = LoggerFactory.getLogger(ModuloSCRAN.class);
	private static BigDecimal valEstres;
	
	public String getNombreServicio() {
		return "ModuloSCRAN";
	}
	
	public void execute(String fecha, String bt) {
		
		// Se obtiene la fecha del día
		GestorIncidencias gi = GestorIncidencias.getInstance(bt);
		
		try {
			if (ModuloSCRAN.LOG.isTraceEnabled()) {
				ModuloSCRAN.LOG.trace("Inicio de execute en clase ModuloSCRAN");
			}
			
			List<TasasAnulacion> tasasList = recuperarTasas(fecha);
		
			if (null == tasasList || tasasList.isEmpty()){
				Incidencia inci = new Incidencia();
				inci.setBt(bt);
				inci.setFecCierre(fecha);
				inci.setCodigoRetorno("09");
				inci.setInfAmpliada("No existen valores de tasas de anulación a estresar para la fecha indicada.");
				gi.write(inci);	
				throw new Solvencia2Excepcion("No existen valores de tasas de anulación a estresar para la fecha indicada." , inci);
			} 
			
			ValoresEstresDao valoresEstres = new ValoresEstresDao();
			List<ValoresEstres> valores = valoresEstres.obtenerValoresEstres(fecha, bt);
			if (null == valores || valores.isEmpty()){
				Incidencia inci = new Incidencia();
				inci.setBt(bt);
				inci.setFecCierre(fecha);
				inci.setCodigoRetorno("09");
				inci.setInfAmpliada("No existen datos de estrés para la fecha de cierre indicada.");
				gi.write(inci);	
				throw new Solvencia2Excepcion("No existen datos de estrés para la fecha de cierre indicada." , inci);
			}
			
			valEstres = valores.get(0).getValor();
			
			estresarTasas(tasasList, bt);
			
		} catch(Solvencia2Excepcion solvExc){
			ModuloSCRAN.LOG.error(solvExc.getMessage(), solvExc);
			gi.write(solvExc.getIncidencia());
			throw new Solvencia2Excepcion(solvExc.getIncidencia().getInfAmpliada() , solvExc.getIncidencia());
		} catch (Exception e) {
			Incidencia inci = new Incidencia();
			inci.setFecCierre(fecha);
			inci.setBt(bt);
			inci.setCodigoRetorno("02");
			inci.setInfAmpliada("Error genérico en la conversión de tasas de anulación.");
			gi.write(inci);	
			throw new Solvencia2Excepcion("Error genérico en la conversión de tasas de anulación." , inci);
		}
	}

	private void estresarTasas(List<TasasAnulacion> tasas, String bt) {

		BigDecimal polizasVig,probAnu;
		List<Salida> listaSal = new ArrayList<Salida>();
		BigDecimal anioIni, aniofin;
		Integer posicion = 0;
		String formato;
		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setDecimalSeparator('.');
		DecimalFormat dfProbAnu = new DecimalFormat("0.0000000000",simbolos);
		
		GestorFicheroSalida gfs = GestorFicheroSalida.getInstance(bt);
		
		Iterator<TasasAnulacion> it = tasas.iterator();
		while(it.hasNext()){
			TasasAnulacion i = it.next();
			
			anioIni = i.getKaniosdesde();
			aniofin = i.getNanioshasta();

			if (aniofin.compareTo(BigDecimal.valueOf(130)) == 1)
				aniofin = BigDecimal.valueOf(130);
			for (int a = anioIni.intValue(); a < aniofin.intValue(); a++){
				Salida sal = new Salida();
				sal.setKaniosdesde(new BigDecimal(a).setScale(3, BigDecimal.ROUND_HALF_UP));
				sal.setFecCierre(i.getKfcierre());
				sal.setCodTabla(i.getKtablaanu());
				if (bt.equals(ConstantesSolvencia.BT_SCRAEP) || bt.equals(ConstantesSolvencia.BT_SCRAIP)){
					probAnu = i.getPprobanu().multiply(BigDecimal.ONE.add(valEstres.multiply(ConstantesSolvencia.CTE_OPER_0_PUNTO_01)));
					if (probAnu.compareTo(BigDecimal.valueOf(100)) == 1){
						probAnu = BigDecimal.valueOf(100);
					}
				} else {
					probAnu = i.getPprobanu().multiply(BigDecimal.ONE.subtract(valEstres.multiply(ConstantesSolvencia.CTE_OPER_0_PUNTO_01)));
					if (probAnu.compareTo(BigDecimal.valueOf(-20)) == -1){
						probAnu = BigDecimal.valueOf(-20);
					}
				}
				formato = dfProbAnu.format(probAnu.multiply(ConstantesSolvencia.CTE_OPER_0_PUNTO_01).setScale(10, BigDecimal.ROUND_HALF_UP));
				sal.setProbabAnul(formato.replace(".", ""));
				
				if (a == 0) {
					polizasVig = new BigDecimal("1000000.00");
				} else {
					probAnu = new BigDecimal(listaSal.get(posicion-1).getProbabAnul()).multiply(ConstantesSolvencia.CTE_OPER_0_PUNTO_0000000001);
					polizasVig = listaSal.get(posicion-1).getPolizaVigentes().multiply(BigDecimal.ONE.subtract(probAnu));
				}
				sal.setPolizaVigentes(polizasVig.setScale(2, BigDecimal.ROUND_HALF_UP));
				
				listaSal.add(sal);
				gfs.write(sal);
				posicion++;
			}
		}
	}

	private List<TasasAnulacion> recuperarTasas(String fecha) {
		
		TasasAnulacionDao dao = new TasasAnulacionDao();
		List<TasasAnulacion> tasas = dao.getValuesFecha(fecha);
		
		if(tasas == null || tasas.isEmpty()){
			Incidencia inci = new Incidencia();
			inci.setFecCierre(fecha);
			inci.setCodigoRetorno("05");
			inci.setInfAmpliada("No se han encontrado registros a estresar.");
			throw new Solvencia2Excepcion(inci);
		}
		
		return tasas;
	}
	
}
