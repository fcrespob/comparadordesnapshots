package es.mapfre.scr.gastos.modulos;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.scr.gastos.dao.entidades.GastosRealesDao;
import es.mapfre.scr.gastos.dao.entidades.ValoresEstresDao;
import es.mapfre.scr.gastos.dominio.entidades.GastosReales;
import es.mapfre.scr.gastos.dominio.entidades.Incidencia;
import es.mapfre.scr.gastos.dominio.entidades.SalidaGRE;
import es.mapfre.scr.gastos.dominio.entidades.ValoresEstres;
import es.mapfre.scr.gastos.excepcion.Solvencia2Excepcion;
import es.mapfre.scr.gastos.gestores.GestorFicheroSalida;
import es.mapfre.scr.gastos.gestores.GestorIncidencias;
import es.mapfre.scr.gastos.utils.ConstantesSolvencia;

public class ModuloNF17GRE {

	private static final Logger LOG = LoggerFactory.getLogger(ModuloNF17GRE.class);
	private static BigDecimal valEstres;
	
	public String getNombreServicio() {
		return "ModuloSCRGRE";
	}
	
	public void execute(String fecha) {
		
		// Se obtiene la fecha del d�a
		GestorIncidencias gi = GestorIncidencias.getInstance(ConstantesSolvencia.CTE_GTO_UMIC_NIIF17);
		GestorFicheroSalida gfs = GestorFicheroSalida.getInstance(ConstantesSolvencia.CTE_GTO_UMIC_NIIF17);
		
		try {
			if (ModuloNF17GRE.LOG.isTraceEnabled()) {
				ModuloNF17GRE.LOG.trace("Inicio de execute en clase ModuloNF17GRE");
			}
			
			List<GastosReales> gastosList = recuperarGastos(fecha, ConstantesSolvencia.BASETEC_BEL);
		
			if (null == gastosList || gastosList.isEmpty()){
				Incidencia inci = new Incidencia();
				inci.setBt(ConstantesSolvencia.BASETEC_GTO);
				inci.setFecCierre(fecha);
				inci.setCodigoRetorno("09");
				inci.setInfAmpliada("No existen valores de gasto a estresar para la fecha indicada.");
				gi.write(inci);	
				throw new Solvencia2Excepcion("No existen valores de gasto a estresar para la fecha indicada." , inci);
			} 
			
			ValoresEstresDao valoresEstres = new ValoresEstresDao();
			List<ValoresEstres> valores = valoresEstres.obtenerValoresEstres(fecha, ConstantesSolvencia.BASETEC_GTO_NIIF17);
			if (null == valores || valores.isEmpty()){
				Incidencia inci = new Incidencia();
				inci.setBt(ConstantesSolvencia.BASETEC_GTO_NIIF17);
				inci.setFecCierre(fecha);
				inci.setCodigoRetorno("09");
				inci.setInfAmpliada("No existen datos de estrés para la fecha de cierre indicada.");
				gi.write(inci);	
				throw new Solvencia2Excepcion("No existen datos de estrés para la fecha de cierre indicada." , inci);
			}
			if (valores.get(0).getVariable().equals(ConstantesSolvencia.CTE_GTO_UMIC)){
				valEstres = valores.get(0).getValor();
			} else if (valores.get(1).getVariable().equals(ConstantesSolvencia.CTE_GTO_UMIC)){
				valEstres = valores.get(1).getValor();
			} else {
				Incidencia inci = new Incidencia();
				inci.setBt(ConstantesSolvencia.BASETEC_GTO_NIIF17);
				inci.setFecCierre(fecha);
				inci.setCodigoRetorno("09");
				inci.setInfAmpliada("No existen datos de estr�s de gastos para la fecha de cierre indicada.");
				gi.write(inci);	
				throw new Solvencia2Excepcion("No existen datos de estr�s de gastos para la fecha de cierre indicada." , inci);
			}
			Iterator<GastosReales> i = gastosList.iterator();
			while (i.hasNext()){
				GastosReales gasto = i.next();
				if(gasto == null || gasto.equals("")){
					i.remove();
				}else{
					gfs.write(estresarGastos(gasto));
				}
			}
			
		} catch(Solvencia2Excepcion solvExc){
			ModuloNF17GRE.LOG.error(solvExc.getMessage(), solvExc);
			gi.write(solvExc.getIncidencia());
			throw new Solvencia2Excepcion(solvExc.getIncidencia().getInfAmpliada() , solvExc.getIncidencia());
		} catch (Exception e) {
			Incidencia inci = new Incidencia();
			inci.setFecCierre(fecha);
			inci.setBt(ConstantesSolvencia.BASETEC_GTO_NIIF17);
			inci.setCodigoRetorno("02");
			inci.setInfAmpliada("Error gen�rico en la conversi�n de gastos.");
			gi.write(inci);	
			throw new Solvencia2Excepcion("Error gen�rico en la conversi�n de gastos." , inci);
		}
	}

	private SalidaGRE estresarGastos(GastosReales gre) {
		
		BigDecimal resultado;
		String formato;
		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setDecimalSeparator('.');
		DecimalFormat dfgastoPorUmic = new DecimalFormat("00000.00",simbolos);
		DecimalFormat dfpctGastoProv = new DecimalFormat("00.00000",simbolos);
		
		SalidaGRE elementoSalida = new SalidaGRE();
		elementoSalida.setCcanal(gre.getCcanal());
		elementoSalida.setCnegocio(gre.getCnegocio());
		elementoSalida.setFecDesde(gre.getFecDesde());
		elementoSalida.setFecHasta(gre.getFecHasta());
		elementoSalida.setKmodalidad(gre.getKmodalidad());
		elementoSalida.setKramo(gre.getKramo());
		elementoSalida.setKtipobt(ConstantesSolvencia.BASETEC_GTO_NIIF17);
		elementoSalida.setMatching(gre.getMatching());
		
		resultado =  gre.getPctGastoProv().multiply(BigDecimal.ONE.add(valEstres.multiply(ConstantesSolvencia.CTE_OPER_0_PUNTO_01)));
		
		formato = dfpctGastoProv.format(resultado.setScale(5, BigDecimal.ROUND_HALF_UP));
		elementoSalida.setPctGastoProv(formato.replace(".", ""));
		
		resultado =  gre.getGastoPorUmic().multiply(BigDecimal.ONE.add(valEstres.multiply(ConstantesSolvencia.CTE_OPER_0_PUNTO_01)));

		formato = dfgastoPorUmic.format(resultado.setScale(2, BigDecimal.ROUND_HALF_UP));
		elementoSalida.setGastoPorUmic(formato.replace(".", ""));
		
		return elementoSalida;
	}

	private List<GastosReales> recuperarGastos(String fecha, String bt) {
		
		GastosRealesDao dao = new GastosRealesDao();
		List<GastosReales> gastos = dao.getGastosReales(fecha, bt);
		
		if(gastos == null || gastos.isEmpty()){
			Incidencia inci = new Incidencia();
			inci.setFecCierre(fecha);
			inci.setCodigoRetorno("05");
			inci.setInfAmpliada("No se han encontrado registros a estresar.");
			throw new Solvencia2Excepcion(inci);
		}
		
		return gastos;
	}
}