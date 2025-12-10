package es.mapfre.coaseguro.tirea.modulos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.channels.ReadableByteChannel;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang3.StringUtils;

import es.mapfre.coaseguro.tirea.dao.entidades.DatosCoaDao;
import es.mapfre.coaseguro.tirea.dao.entidades.DatosEspecificDao;
import es.mapfre.coaseguro.tirea.dao.entidades.FlujPMaCoaDao;
import es.mapfre.coaseguro.tirea.dao.entidades.FlujPmdCoaBtcoaDao;
import es.mapfre.coaseguro.tirea.dao.entidades.FlujPmdCoaBtcoatfDao;
import es.mapfre.coaseguro.tirea.dao.entidades.PagosPlanDao;
import es.mapfre.coaseguro.tirea.dao.entidades.FlujPMdCoaDao;
import es.mapfre.coaseguro.tirea.dao.entidades.FlujPmaCoaBtcoaDao;
import es.mapfre.coaseguro.tirea.dao.entidades.FlujPmaCoaBtcoatfDao;
import es.mapfre.coaseguro.tirea.dao.entidades.Tab35012Dao;
import es.mapfre.coaseguro.tirea.dao.entidades.Tab35013Dao;
import es.mapfre.coaseguro.tirea.dao.entidades.Tab35014Dao;
import es.mapfre.coaseguro.tirea.dao.entidades.Tab35015Dao;
import es.mapfre.coaseguro.tirea.dao.entidades.TotPMaCoaDao;
import es.mapfre.coaseguro.tirea.dao.entidades.TotPmaCoaBtcoaDao;
import es.mapfre.coaseguro.tirea.dao.entidades.TotPmaCoaBtcoatfDao;
import es.mapfre.coaseguro.tirea.dominio.entidades.BaseTecnica;
import es.mapfre.coaseguro.tirea.dominio.entidades.BaseTecnicaPMF;
import es.mapfre.coaseguro.tirea.dominio.entidades.BasesTecnicas;
import es.mapfre.coaseguro.tirea.dominio.entidades.BasesTecnicasPMF;
import es.mapfre.coaseguro.tirea.dominio.entidades.Beneficiario;
import es.mapfre.coaseguro.tirea.dominio.entidades.Beneficiarios;
import es.mapfre.coaseguro.tirea.dominio.entidades.Cabecera;
import es.mapfre.coaseguro.tirea.dominio.entidades.Crecimiento;
import es.mapfre.coaseguro.tirea.dominio.entidades.CuadroCoaseguro;
import es.mapfre.coaseguro.tirea.dominio.entidades.DatosCoa;
import es.mapfre.coaseguro.tirea.dominio.entidades.DatosCrecimientos;
import es.mapfre.coaseguro.tirea.dominio.entidades.DatosDiferimientos;
import es.mapfre.coaseguro.tirea.dominio.entidades.DatosEspecific;
import es.mapfre.coaseguro.tirea.dominio.entidades.DatosEspecificos;
import es.mapfre.coaseguro.tirea.dominio.entidades.DatosFlujo;
import es.mapfre.coaseguro.tirea.dominio.entidades.DatosFlujos;
import es.mapfre.coaseguro.tirea.dominio.entidades.DatosPMA;
import es.mapfre.coaseguro.tirea.dominio.entidades.DatosPMD;
import es.mapfre.coaseguro.tirea.dominio.entidades.DatosPMDs;
import es.mapfre.coaseguro.tirea.dominio.entidades.DatosPoliza;
import es.mapfre.coaseguro.tirea.dominio.entidades.DatosRevalorizaciones;
import es.mapfre.coaseguro.tirea.dominio.entidades.DatosReversion;
import es.mapfre.coaseguro.tirea.dominio.entidades.Diferimiento;
import es.mapfre.coaseguro.tirea.dominio.entidades.FichaProceso;
import es.mapfre.coaseguro.tirea.dominio.entidades.FlujPMaCoa;
import es.mapfre.coaseguro.tirea.dominio.entidades.FlujPMdCoa;
import es.mapfre.coaseguro.tirea.dominio.entidades.FlujPmdCoaBtcoa;
import es.mapfre.coaseguro.tirea.dominio.entidades.FlujPmdCoaBtcoatf;
import es.mapfre.coaseguro.tirea.dominio.entidades.GarantiaPrestacion;
import es.mapfre.coaseguro.tirea.dominio.entidades.GarantiasPrestaciones;
import es.mapfre.coaseguro.tirea.dominio.entidades.FlujPmaCoaBtcoa;
import es.mapfre.coaseguro.tirea.dominio.entidades.FlujPmaCoaBtcoatf;
import es.mapfre.coaseguro.tirea.dominio.entidades.Gasto;
import es.mapfre.coaseguro.tirea.dominio.entidades.Gastos;
import es.mapfre.coaseguro.tirea.dominio.entidades.Incidencia;
import es.mapfre.coaseguro.tirea.dominio.entidades.Interes;
import es.mapfre.coaseguro.tirea.dominio.entidades.Movimiento;
import es.mapfre.coaseguro.tirea.dominio.entidades.MovimientoDatosEspecificos;
import es.mapfre.coaseguro.tirea.dominio.entidades.MovimientoPMD;
import es.mapfre.coaseguro.tirea.dominio.entidades.MovimientoPMF;
import es.mapfre.coaseguro.tirea.dominio.entidades.MovimientoRentasIrregulares;
import es.mapfre.coaseguro.tirea.dominio.entidades.MovimientosCoaseguro;
import es.mapfre.coaseguro.tirea.dominio.entidades.MovimientosCoaseguroDatosEspecificos;
import es.mapfre.coaseguro.tirea.dominio.entidades.MovimientosCoaseguroPMD;
import es.mapfre.coaseguro.tirea.dominio.entidades.MovimientosCoaseguroPMF;
import es.mapfre.coaseguro.tirea.dominio.entidades.MovimientosCoaseguroRentasIrregulares;
import es.mapfre.coaseguro.tirea.dominio.entidades.PMA;
import es.mapfre.coaseguro.tirea.dominio.entidades.PMD;
import es.mapfre.coaseguro.tirea.dominio.entidades.PMF;
import es.mapfre.coaseguro.tirea.dominio.entidades.PagoPlanificado;
import es.mapfre.coaseguro.tirea.dominio.entidades.PagosPlan;
import es.mapfre.coaseguro.tirea.dominio.entidades.PagosPlanificados;
import es.mapfre.coaseguro.tirea.dominio.entidades.ParticipacionEntidad;
import es.mapfre.coaseguro.tirea.dominio.entidades.RentaIrregular;
import es.mapfre.coaseguro.tirea.dominio.entidades.Revalorizacion;
import es.mapfre.coaseguro.tirea.dominio.entidades.Reversion;
import es.mapfre.coaseguro.tirea.dominio.entidades.Tab35012;
import es.mapfre.coaseguro.tirea.dominio.entidades.Tab35013;
import es.mapfre.coaseguro.tirea.dominio.entidades.Tab35014;
import es.mapfre.coaseguro.tirea.dominio.entidades.Tab35015;
import es.mapfre.coaseguro.tirea.dominio.entidades.Tabla;
import es.mapfre.coaseguro.tirea.dominio.entidades.Tablas;
import es.mapfre.coaseguro.tirea.dominio.entidades.TiposInteres;
import es.mapfre.coaseguro.tirea.dominio.entidades.TotPMaCoa;
import es.mapfre.coaseguro.tirea.dominio.entidades.TotPmaCoaBtcoa;
import es.mapfre.coaseguro.tirea.dominio.entidades.TotPmaCoaBtcoatf;
import es.mapfre.coaseguro.tirea.dominio.entidades.Variable;
import es.mapfre.coaseguro.tirea.dominio.entidades.Variables;
import es.mapfre.coaseguro.tirea.dominio.keys.DatosCoaKey;
import es.mapfre.coaseguro.tirea.dominio.keys.Tab35012Key;
import es.mapfre.coaseguro.tirea.dominio.keys.Tab35013Key;
import es.mapfre.coaseguro.tirea.dominio.keys.Tab35014Key;
import es.mapfre.coaseguro.tirea.dominio.keys.Tab35015Key;
import es.mapfre.coaseguro.tirea.excepcion.Solvencia2Excepcion;
import es.mapfre.coaseguro.tirea.gestores.GestorFichaProceso;
import es.mapfre.coaseguro.tirea.gestores.GestorIncidenciasGen;
import es.mapfre.coaseguro.tirea.utils.CargaDatos;
import es.mapfre.coaseguro.tirea.utils.ConstantesSolvencia;
import es.mapfre.coaseguro.tirea.utils.LoggerManager;
import es.mapfre.coaseguro.tirea.utils.BtUtils;

public class PrincipalTIREA {
	
	public PrincipalTIREA() {
	}
	
	static String gfecCierre = "";
	static Long gpoliza = new Long(0);
	static Integer gsubpoliza = 0;
	static Integer gnsuscri = 0;
	static Integer gkcerti = 0;

	public static void main(String[] args) {
		
		try {
			
			new PrincipalTIREA();
			CargaDatos cargaDatos = new CargaDatos();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat sdfechaInteres = new SimpleDateFormat("yyyy-MM-dd");
			
			GestorIncidenciasGen giGeneral = GestorIncidenciasGen.getInstance();
			try{
				cargaDatos.cargaFichaProceso();
				// Obtenemos las fichas a ejecutar
				GestorFichaProceso gfp = GestorFichaProceso.getInstance();
				List<FichaProceso> fichas = gfp.getValues();
				Iterator<FichaProceso> it = fichas.iterator();
				
				while (it.hasNext()) {
					FichaProceso ficha = it.next();
					String fecCierre = sdf.format(ficha.getFecCierre());
					
					cargaDatos.cargaFicheros(fecCierre);
					
					DatosCoaDao datosCoaDao = new DatosCoaDao();
					TotPMaCoaDao totPmaDao = new TotPMaCoaDao();
					FlujPMaCoaDao flujPmaDao = new FlujPMaCoaDao();
					FlujPMdCoaDao flujPmdDao = new FlujPMdCoaDao();
					FlujPmdCoaBtcoaDao flujPmdBtcoaDao = new FlujPmdCoaBtcoaDao();
					FlujPmdCoaBtcoatfDao flujPmdBtcoatfDao = new FlujPmdCoaBtcoatfDao();
					TotPmaCoaBtcoaDao totPmaBtcoaDao = new TotPmaCoaBtcoaDao();
					FlujPmaCoaBtcoaDao flujPmaBtcoaDao = new FlujPmaCoaBtcoaDao();
					TotPmaCoaBtcoatfDao totPmaBtcoatfDao = new TotPmaCoaBtcoatfDao();
					FlujPmaCoaBtcoatfDao flujPmaBtcoatfDao = new FlujPmaCoaBtcoatfDao();
					Tab35012Dao tab35012Dao = new Tab35012Dao();
					Tab35013Dao tab35013Dao = new Tab35013Dao();
					Tab35014Dao tab35014Dao = new Tab35014Dao();
					Tab35015Dao tab35015Dao = new Tab35015Dao();
					DatosEspecificDao datosEspecificDao = new DatosEspecificDao();
					PagosPlanDao pagosPlanDao = new PagosPlanDao();
					List<DatosCoa> listaDatosCoa = datosCoaDao.getValues();
					List<Tab35012> listaTab35012 = tab35012Dao.getValues();
					List<TotPMaCoa> listaTotPmaCoa = new ArrayList<TotPMaCoa>();
					listaTotPmaCoa = totPmaDao.getValues();
					Collections.sort(listaTotPmaCoa, new Comparator<TotPMaCoa>() {
						public int compare(TotPMaCoa o1, TotPMaCoa o2) {
							return o1.getKpoliza().compareTo(o2.getKpoliza());
						}
					});
					Collections.sort(listaTotPmaCoa, new Comparator<TotPMaCoa>() {
						public int compare(TotPMaCoa o1, TotPMaCoa o2) {
							return o1.getKsubpoliza().compareTo(o2.getKsubpoliza());
						}
					});
					Collections.sort(listaTotPmaCoa, new Comparator<TotPMaCoa>() {
						public int compare(TotPMaCoa o1, TotPMaCoa o2) {
							return o1.getNsuscri().compareTo(o2.getNsuscri());
						}
					});
					List<TotPmaCoaBtcoa> listaTotPmaCoaBtcoa = totPmaBtcoaDao.getValues();
					Collections.sort(listaTotPmaCoaBtcoa, new Comparator<TotPmaCoaBtcoa>() {
						public int compare(TotPmaCoaBtcoa o1, TotPmaCoaBtcoa o2) {
							return o1.getKpoliza().compareTo(o2.getKpoliza());
						}
					});
					Collections.sort(listaTotPmaCoaBtcoa, new Comparator<TotPmaCoaBtcoa>() {
						public int compare(TotPmaCoaBtcoa o1, TotPmaCoaBtcoa o2) {
							return o1.getKsubpoliza().compareTo(o2.getKsubpoliza());
						}
					});
					Collections.sort(listaTotPmaCoaBtcoa, new Comparator<TotPmaCoaBtcoa>() {
						public int compare(TotPmaCoaBtcoa o1, TotPmaCoaBtcoa o2) {
							return o1.getNsuscri().compareTo(o2.getNsuscri());
						}
					});
					List<TotPmaCoaBtcoatf> listaTotPmaCoaBtcoatf = totPmaBtcoatfDao.getValues();
					Collections.sort(listaTotPmaCoaBtcoatf, new Comparator<TotPmaCoaBtcoatf>() {
						public int compare(TotPmaCoaBtcoatf o1, TotPmaCoaBtcoatf o2) {
							return o1.getKpoliza().compareTo(o2.getKpoliza());
						}
					});
					Collections.sort(listaTotPmaCoaBtcoatf, new Comparator<TotPmaCoaBtcoatf>() {
						public int compare(TotPmaCoaBtcoatf o1, TotPmaCoaBtcoatf o2) {
							return o1.getKsubpoliza().compareTo(o2.getKsubpoliza());
						}
					});
					Collections.sort(listaTotPmaCoaBtcoatf, new Comparator<TotPmaCoaBtcoatf>() {
						public int compare(TotPmaCoaBtcoatf o1, TotPmaCoaBtcoatf o2) {
							return o1.getNsuscri().compareTo(o2.getNsuscri());
						}
					});
					/////////////////////////////////PMA/////////////////////
					Iterator<TotPMaCoa> itPma = listaTotPmaCoa.iterator();
					Iterator<TotPmaCoaBtcoa> itPmaBtcoa = listaTotPmaCoaBtcoa.iterator();
					Iterator<TotPmaCoaBtcoatf> itPmaBtcoatf = listaTotPmaCoaBtcoatf.iterator();
					
					String mensaje = "";
					
					MovimientosCoaseguro movimientosPma =  new MovimientosCoaseguro();
					int indice = 0;
					List<Movimiento> listMovi = new ArrayList<>();
					
					FlujPmaCoaBtcoa sigRegBtcoa = new FlujPmaCoaBtcoa();
					FlujPmaCoaBtcoatf sigRegBtcoatf = new FlujPmaCoaBtcoatf();
					
					TotPmaCoaBtcoa sigRegPmaBtcoa = new TotPmaCoaBtcoa();
					TotPmaCoaBtcoatf sigRegPmaBtcoatf = new TotPmaCoaBtcoatf();
					int iteracion = 0;
					while (itPma.hasNext()) {
						if(iteracion == 500){
							int a = 0;
						}
						if(iteracion == 1000){
							int a = 0;
						}
						if(iteracion == 1500){
							int a = 0;
						}
						if(iteracion == 2000){
							int a = 0;
						}
						if(iteracion == 2500){
							int a = 0;
						}
						if(iteracion == 36){
							int a = 0;
						}
						if(iteracion == 37){
							int a = 0;
						}
						
						TotPMaCoa flujo = itPma.next();
						
						if(itPmaBtcoa.hasNext()){
							sigRegPmaBtcoa = itPmaBtcoa.next();
						}
						
						if(itPmaBtcoatf.hasNext()){
							sigRegPmaBtcoatf = itPmaBtcoatf.next();
						}
						
						try {
							DatosCoaKey datCoa = new DatosCoaKey(flujo.getKpoliza(),flujo.getKsubpoliza()); 
							DatosCoa datoscoaseg = datosCoaDao.get(datCoa);
							if(indice == 0){
							Cabecera cab = new Cabecera();
							SimpleDateFormat sdfecha = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
							String fechaComoCadena = sdfecha.format(new Date());
							cab.setFecha(fechaComoCadena);
							cab.setIdEmisor("C0511");
							SimpleDateFormat sdfAux = new SimpleDateFormat("yyyyMMddHHmm");
							String fechaIdFichero = sdfAux.format(new Date());
							cab.setIdFichero(fechaIdFichero);
							
							cab.setIdReceptor("T0001");
							
							movimientosPma.setCabecera(cab);
							indice++;
							}
							
							Movimiento mov = new Movimiento();
							
							mov.setTipoMovimiento(6);
							
							String fechaAux = fecCierre.substring(0,6);
							if(fechaAux.substring(4).equals("01") || fechaAux.substring(4).equals("03") || fechaAux.substring(4).equals("05") 
								|| fechaAux.substring(4).equals("07") || fechaAux.substring(4).equals("08") || fechaAux.substring(4).equals("10") || fechaAux.substring(4).equals("12") ){
								fechaAux = fecCierre.substring(0,4) + "-" + fecCierre.substring(4,6) + "-" + "31";
							}else if(fechaAux.substring(4).equals("02")){
								fechaAux = fecCierre.substring(0,4) + "-" + fecCierre.substring(4,6) + "-" + "28";
							}else{
								fechaAux = fecCierre.substring(0,4) + "-" + fecCierre.substring(4,6) + "-" + "30";
							}
							
							mov.setFecha(fechaAux);
							
							PMA pma = new PMA();
							DatosPoliza datosPoliza = new DatosPoliza();
							
							datosPoliza.setEntidadAbridora(datoscoaseg.getkCoaseOri().substring(0,1) + "0" + datoscoaseg.getkCoaseOri().substring(1));
							
							if(datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase1())){
								datosPoliza.setParticipacionAbridora(datoscoaseg.getpCoase1().toString());
							}else if(datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase2())){
								datosPoliza.setParticipacionAbridora(datoscoaseg.getpCoase2().toString());
							}else if(datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase3())){
								datosPoliza.setParticipacionAbridora(datoscoaseg.getpCoase3().toString());
							}else if(datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase4())){
								datosPoliza.setParticipacionAbridora(datoscoaseg.getpCoase4().toString());
							}else if(datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase5())){
								datosPoliza.setParticipacionAbridora(datoscoaseg.getpCoase5().toString());
							}
							
							int widthPoliza = 10;
							int widthSubPoliza = 4;
							 
					        String formattedPoliza = String.format("%0" + widthPoliza + "d", Integer.valueOf(flujo.getKpoliza().toString()));
					        String formattedSubPoliza = String.format("%0" + widthSubPoliza + "d", Integer.valueOf(flujo.getKsubpoliza().toString()));
							
							datosPoliza.setNumeroPoliza(formattedPoliza + formattedSubPoliza);
							datosPoliza.setNumeroSuplemento(flujo.getNsuscri().toString());
							
							CuadroCoaseguro cuadroCoaseg = new CuadroCoaseguro();
							
							List<ParticipacionEntidad> partEntidad = new ArrayList<>();
							
							if(null != datoscoaseg.getKCoase1() && !datoscoaseg.getKCoase1().equals("")){
								
								if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase1())){
								
									ParticipacionEntidad ent1 = new ParticipacionEntidad();
									ent1.setEntidadAceptante(datoscoaseg.getKCoase1().substring(0,1) + "0" + datoscoaseg.getKCoase1().substring(1));								
									ent1.setParticipacionAceptante(datoscoaseg.getpCoase1().toString());
									partEntidad.add(ent1);
								}
							}
							
							if(null != datoscoaseg.getKCoase2() && !datoscoaseg.getKCoase2().equals("")){
								
								if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase2())){
								ParticipacionEntidad ent2 = new ParticipacionEntidad();
								ent2.setEntidadAceptante(datoscoaseg.getKCoase2().substring(0,1) + "0" + datoscoaseg.getKCoase2().substring(1));
								ent2.setParticipacionAceptante(datoscoaseg.getpCoase2().toString());
								partEntidad.add(ent2);
								}
							}
							
							if(null != datoscoaseg.getKCoase3() && !datoscoaseg.getKCoase3().equals("")){
								if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase3())){
							
								ParticipacionEntidad ent3 = new ParticipacionEntidad();
								ent3.setEntidadAceptante(datoscoaseg.getKCoase3().substring(0,1) + "0" + datoscoaseg.getKCoase3().substring(1));
								ent3.setParticipacionAceptante(datoscoaseg.getpCoase3().toString());
								partEntidad.add(ent3);
								}
							}
							
							if(null != datoscoaseg.getKCoase4() && !datoscoaseg.getKCoase4().equals("")){
								if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase4())){
							
								ParticipacionEntidad ent4 = new ParticipacionEntidad();
								ent4.setEntidadAceptante(datoscoaseg.getKCoase4().substring(0,1) + "0" + datoscoaseg.getKCoase4().substring(1));
								ent4.setParticipacionAceptante(datoscoaseg.getpCoase4().toString());
								partEntidad.add(ent4);
								}
							}
							
							if(null != datoscoaseg.getKCoase5() && !datoscoaseg.getKCoase5().equals("")){
								if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase5())){
								ParticipacionEntidad ent5 = new ParticipacionEntidad();
								ent5.setEntidadAceptante(datoscoaseg.getKCoase5().substring(0,1) + "0" + datoscoaseg.getKCoase5().substring(1));
								ent5.setParticipacionAceptante(datoscoaseg.getpCoase5().toString());
								partEntidad.add(ent5);
								}
							}
							
							cuadroCoaseg.setParticipacionEntidad(partEntidad);
							datosPoliza.setCuadroCoaseguro(cuadroCoaseg);
							SimpleDateFormat sdfechaEfecto = new SimpleDateFormat("yyyy-MM-dd");
							String fechaEfecto = sdfechaEfecto.format(flujo.getFecefecini());
							datosPoliza.setFechaEfecto(fechaEfecto);
							datosPoliza.setMetodoSuplemento("INCREMENTAL");
							pma.setDatosPoliza(datosPoliza);
							
							/////
							
							DatosPMA datosPMA = new DatosPMA();
							
							BasesTecnicas basesTecnicas = new BasesTecnicas();
							
							List<BaseTecnica> listBt = new ArrayList<>();
							
							for(int i = 0; i < 3; i++){
								BaseTecnica bt = new BaseTecnica();
								
								if(i==0){
									bt.setTipoTablaExperiencia("BT");
									
									if (flujo.getTotprovision().toString().indexOf(".") == -1) {
										bt.setImporteProvision(flujo.getTotprovision().toString().concat(".00"));
									} else {
										bt.setImporteProvision(flujo.getTotprovision().toString());
									}
									bt.setImporteProvision(flujo.getTotprovision().setScale(2).toString());
									
									Tablas tablas = new Tablas();
									List<Tabla> tab = new ArrayList<>();
									Tabla t = new Tabla();
									Tab35012Key t35012 = new Tab35012Key(StringUtils.leftPad(flujo.getTablacalc1aseg1(),5,"0")); 
									Tab35012 tab35 = tab35012Dao.get(t35012);
									t.setCodigoTabla(tab35.getTablaTirea().replace(" ", ""));
									if(t.getCodigoTabla().equalsIgnoreCase("GRM80") 
											&& StringUtils.leftPad(flujo.getTablacalc1aseg1(),5,"0").equalsIgnoreCase("00705")){
										t.setDesplazamiento("-2");
									}else if(t.getCodigoTabla().equalsIgnoreCase("GRM95")
											&& (StringUtils.leftPad(flujo.getTablacalc1aseg1(),5,"0").equalsIgnoreCase("00365")
													|| StringUtils.leftPad(flujo.getTablacalc1aseg1(),5,"0").equalsIgnoreCase("00721"))){
										t.setDesplazamiento("-1");
									}else{
										t.setDesplazamiento("0");
									}
									t.setPjeTabla(tab35.getPorcentajeTabla().toString().concat(".00"));
									tab.add(t);
									tablas.setTabla(tab);
									bt.setTablas(tablas);
									
									TiposInteres tiposInteres = new TiposInteres();
									List<Interes> listInt = new ArrayList<>();
									Interes inter = new Interes();
									inter.setTipoInteres(flujo.getPintertecnI1().toString());
									String fechaInteresIni = sdfechaInteres.format(flujo.getFecIniTramo1());
									inter.setFechaInicio(fechaInteresIni);
									String fechaInteresFin = sdfechaInteres.format(flujo.getFecFinTramo1());
									inter.setFechaFin(fechaInteresFin);
									listInt.add(inter);
									if(!flujo.getPintertecnI2().equals(BigDecimal.ZERO)){
										Interes inter2 = new Interes();
										inter2.setTipoInteres(flujo.getPintertecnI2().toString());
										String fechaInteresIni2 = sdfechaInteres.format(flujo.getFecIniTramo2());
										inter2.setFechaInicio(fechaInteresIni2);
										String fechaInteresFin2 = sdfechaInteres.format(flujo.getFecFinTramo2());
										inter2.setFechaFin(fechaInteresFin2);
										listInt.add(inter2);
									}
									tiposInteres.setInteres(listInt);
									
									bt.setTiposInteres(tiposInteres);
									if (flujo.getPgastgesin1I().equals(BigDecimal.ZERO)
											|| flujo.getPgastgesin2I().equals(BigDecimal.ZERO)
											|| flujo.getPgastgesex1I().equals(BigDecimal.ZERO)
											|| flujo.getPgastgesex2I().equals(BigDecimal.ZERO)) {
										
										Gastos gastos = new Gastos();
										List<Gasto> listGasto = new ArrayList<>();
										
										if(!flujo.getPgastgesin1I().equals(BigDecimal.ZERO)) {
											Gasto g = new Gasto();
											g.setConcepto("GGI");
											g.setBase("PRIMA");
											if (flujo.getPgastgesin1I().toString().contains(".")) {
												g.setPorcentaje(flujo.getPgastgesin1I().toString());
											} else {
												g.setPorcentaje(flujo.getPgastgesin1I().toString().concat(".00"));
											}
											g.setImporte(flujo.getTotfactgto().toString());
											listGasto.add(g);
										}
										if(!flujo.getPgastgesin2I().equals(BigDecimal.ZERO)) {
											Gasto g = new Gasto();
											g.setConcepto("GGI");
											g.setBase("CAPITAL");
											if (flujo.getPgastgesin2I().toString().contains(".")) {
												g.setPorcentaje(flujo.getPgastgesin2I().toString());
											} else {
												g.setPorcentaje(flujo.getPgastgesin2I().toString().concat(".00"));
											}
											g.setImporte(flujo.getTotfactgto().toString());
											listGasto.add(g);
										}
										if(!flujo.getPgastgesex1I().equals(BigDecimal.ZERO)) {
											Gasto g = new Gasto();
											g.setConcepto("GGE");
											g.setBase("CAPITAL");
											if (flujo.getPgastgesex1I().toString().contains(".")) {
												g.setPorcentaje(flujo.getPgastgesex1I().toString());
											} else {
												g.setPorcentaje(flujo.getPgastgesex1I().toString().concat(".00"));
											}
											g.setImporte(flujo.getTotfactgto().toString());
											listGasto.add(g);
										}
										if(!flujo.getPgastgesex2I().equals(BigDecimal.ZERO)) {
											Gasto g = new Gasto();
											g.setConcepto("GGE");
											g.setBase("PRIMAS");
											if (flujo.getPgastgesex2I().toString().contains(".")) {
												g.setPorcentaje(flujo.getPgastgesex2I().toString());
											} else {
												g.setPorcentaje(flujo.getPgastgesex2I().toString().concat(".00"));
											}
											g.setImporte(flujo.getTotfactgto().toString());
											listGasto.add(g);
										}
										
										if (flujo.getPgastgesin1I().equals(BigDecimal.ZERO)
												&& flujo.getPgastgesin2I().equals(BigDecimal.ZERO)
												&& flujo.getPgastgesex1I().equals(BigDecimal.ZERO)
												&& flujo.getPgastgesex2I().equals(BigDecimal.ZERO)) {
											Gasto g = new Gasto();
											g.setConcepto("GGI");
											g.setBase("PRIMA");
											g.setPorcentaje("0.00");
											g.setImporte("0.00");
											
											listGasto.add(g);
										}
										gastos.setGasto(listGasto);
										bt.setGastos(gastos);
									}
									
									listBt.add(bt);
									
								}else if(i==1){
									bt.setTipoTablaExperiencia("DGS");
									
									if(null != sigRegPmaBtcoatf && sigRegPmaBtcoatf.getKpoliza().equals(flujo.getKpoliza())
											&& sigRegPmaBtcoatf.getKsubpoliza().equals(flujo.getKsubpoliza()) && 
											sigRegPmaBtcoatf.getNsuscri().equals(flujo.getNsuscri())){
											
										bt.setImporteProvision(sigRegPmaBtcoatf.getTotprovision().toString());
										if (sigRegPmaBtcoatf.getTotprovision().toString().indexOf(".") == -1) {
											bt.setImporteProvision(sigRegPmaBtcoatf.getTotprovision().toString().concat(".00"));
										} else {
											bt.setImporteProvision(sigRegPmaBtcoatf.getTotprovision().toString());
										}
										
										Tablas tablas = new Tablas();
										List<Tabla> tab = new ArrayList<>();
										Tabla t = new Tabla();
										t.setCodigoTabla("");
										t.setDesplazamiento("0");
										t.setPjeTabla("0.00");
										Tab35012Key t35012 = new Tab35012Key(StringUtils.leftPad(sigRegPmaBtcoatf.getTablacalc1aseg1(),5,"0")); 
										
										if(null != t35012){
											Tab35012 tab35 = tab35012Dao.get(t35012);
											t.setCodigoTabla(tab35.getTablaTirea().replace(" ", ""));
											if(t.getCodigoTabla().equalsIgnoreCase("GRM80") 
													&& StringUtils.leftPad(flujo.getTablacalc1aseg1(),5,"0").equalsIgnoreCase("00705")){
												t.setDesplazamiento("-2");
											}else if(t.getCodigoTabla().equalsIgnoreCase("GRM95")
													&& (StringUtils.leftPad(flujo.getTablacalc1aseg1(),5,"0").equalsIgnoreCase("00365")
															|| StringUtils.leftPad(flujo.getTablacalc1aseg1(),5,"0").equalsIgnoreCase("00721"))){
												t.setDesplazamiento("-1");
											}else{
												t.setDesplazamiento("0");
											}
											t.setPjeTabla(tab35.getPorcentajeTabla().toString().concat(".00"));
										}
										
										tab.add(t);
										tablas.setTabla(tab);
										bt.setTablas(tablas);
										
										TiposInteres tiposInteres = new TiposInteres();
										List<Interes> listInt = new ArrayList<>();
										Interes inter = new Interes();
										inter.setTipoInteres(sigRegPmaBtcoatf.getPintertecnI1().toString());
										String fechaInteresIni = sdfechaInteres.format(sigRegPmaBtcoatf.getFecIniTramo1());
										inter.setFechaInicio(fechaInteresIni);
										String fechaInteresFin = sdfechaInteres.format(sigRegPmaBtcoatf.getFecFinTramo1());
										inter.setFechaFin(fechaInteresFin);
										listInt.add(inter);
										if(!sigRegPmaBtcoatf.getPintertecnI2().equals(BigDecimal.ZERO)){
											Interes inter2 = new Interes();
											inter2.setTipoInteres(sigRegPmaBtcoatf.getPintertecnI2().toString());
											String fechaInteresIni2 = sdfechaInteres.format(sigRegPmaBtcoatf.getFecIniTramo2());
											inter2.setFechaInicio(fechaInteresIni2);
											String fechaInteresFin2 = sdfechaInteres.format(sigRegPmaBtcoatf.getFecFinTramo2());
											inter2.setFechaFin(fechaInteresFin2);
											listInt.add(inter2);
										}
										tiposInteres.setInteres(listInt);
										bt.setTiposInteres(tiposInteres);
										
										if (sigRegPmaBtcoa.getPgastgesin1I().equals(BigDecimal.ZERO)
												|| sigRegPmaBtcoa.getPgastgesin2I().equals(BigDecimal.ZERO)
												|| sigRegPmaBtcoa.getPgastgesex1I().equals(BigDecimal.ZERO)
												|| sigRegPmaBtcoa.getPgastgesex2I().equals(BigDecimal.ZERO)) {
											
											Gastos gastos = new Gastos();
											List<Gasto> listGasto = new ArrayList<>();
											
											if(!sigRegPmaBtcoatf.getPgastgesin1I().equals(BigDecimal.ZERO)) {
												Gasto g = new Gasto();
												g.setConcepto("GGI");
												g.setBase("PRIMA");
												if (sigRegPmaBtcoatf.getPgastgesin1I().toString().contains(".")) {
													g.setPorcentaje(sigRegPmaBtcoatf.getPgastgesin1I().toString());
												} else {
													g.setPorcentaje(sigRegPmaBtcoatf.getPgastgesin1I().toString().concat(".00"));
												}
												g.setImporte(sigRegPmaBtcoatf.getTotfactgto().toString());
												listGasto.add(g);
											}
											if(!sigRegPmaBtcoatf.getPgastgesin2I().equals(BigDecimal.ZERO)) {
												Gasto g = new Gasto();
												g.setConcepto("GGI");
												g.setBase("CAPITAL");
												if (sigRegPmaBtcoatf.getPgastgesin2I().toString().contains(".")) {
													g.setPorcentaje(sigRegPmaBtcoatf.getPgastgesin2I().toString());
												} else {
													g.setPorcentaje(sigRegPmaBtcoatf.getPgastgesin2I().toString().concat(".00"));
												}
												g.setImporte(sigRegPmaBtcoatf.getTotfactgto().toString());
												listGasto.add(g);
											}
											if(!sigRegPmaBtcoatf.getPgastgesex1I().equals(BigDecimal.ZERO)) {
												Gasto g = new Gasto();
												g.setConcepto("GGE");
												g.setBase("CAPITAL");
												if (sigRegPmaBtcoatf.getPgastgesex1I().toString().contains(".")) {
													g.setPorcentaje(sigRegPmaBtcoatf.getPgastgesex1I().toString());
												} else {
													g.setPorcentaje(sigRegPmaBtcoatf.getPgastgesex1I().toString().concat(".00"));
												}
												g.setImporte(sigRegPmaBtcoatf.getTotfactgto().toString());
												listGasto.add(g);
											}
											if(!sigRegPmaBtcoatf.getPgastgesex2I().equals(BigDecimal.ZERO)) {
												Gasto g = new Gasto();
												g.setConcepto("GGE");
												g.setBase("PRIMAS");
												if (sigRegPmaBtcoatf.getPgastgesex2I().toString().contains(".")) {
													g.setPorcentaje(sigRegPmaBtcoatf.getPgastgesex2I().toString());
												} else {
													g.setPorcentaje(sigRegPmaBtcoatf.getPgastgesex2I().toString().concat(".00"));
												}
												g.setImporte(sigRegPmaBtcoatf.getTotfactgto().toString());
												listGasto.add(g);
											}
											
											if (sigRegPmaBtcoatf.getPgastgesin1I().equals(BigDecimal.ZERO)
													&& sigRegPmaBtcoatf.getPgastgesin2I().equals(BigDecimal.ZERO)
													&& sigRegPmaBtcoatf.getPgastgesex1I().equals(BigDecimal.ZERO)
													&& sigRegPmaBtcoatf.getPgastgesex2I().equals(BigDecimal.ZERO)) {
												Gasto g = new Gasto();
												g.setConcepto("GGI");
												g.setBase("PRIMA");
												g.setPorcentaje("0.00");
												g.setImporte("0.00");
												
												listGasto.add(g);
											}
											gastos.setGasto(listGasto);
											bt.setGastos(gastos);
										}
										
										listBt.add(bt);
											
									}else{
										
										Tablas tablas = new Tablas();
										List<Tabla> tab = new ArrayList<>();
										Tabla t = new Tabla();
										t.setCodigoTabla("");
										t.setDesplazamiento("0");
										t.setPjeTabla("0.00");
										tab.add(t);
										tablas.setTabla(tab);
										bt.setTablas(tablas);
											
										TiposInteres tiposInteres = new TiposInteres();
										List<Interes> listInt = new ArrayList<>();
										Interes inter = new Interes();
										inter.setTipoInteres("0.00");
										inter.setFechaInicio("");
										inter.setFechaFin("");
										listInt.add(inter);
										tiposInteres.setInteres(listInt);
										bt.setTiposInteres(tiposInteres);
										Gastos gastos = new Gastos();
										List<Gasto> listGasto = new ArrayList<>();
										Gasto g = new Gasto();
										g.setConcepto("GGI");
										g.setBase("PRIMA");
										g.setPorcentaje("0.00");
										g.setImporte("0.00");
										listGasto.add(g);
										gastos.setGasto(listGasto);
										bt.setGastos(gastos);
										listBt.add(bt);
										
										}							
								}else{
									bt.setTipoTablaExperiencia("PC");
									
									if(null != sigRegPmaBtcoa && sigRegPmaBtcoa.getKpoliza().equals(flujo.getKpoliza())
											&& sigRegPmaBtcoa.getKsubpoliza().equals(flujo.getKsubpoliza()) && 
											sigRegPmaBtcoa.getNsuscri().equals(flujo.getNsuscri())){
											
										//bt.setImporteProvision(sigRegPmaBtcoa.getTotprovision().toString());
										if (sigRegPmaBtcoa.getTotprovision().toString().indexOf(".") == -1) {
											bt.setImporteProvision(sigRegPmaBtcoa.getTotprovision().toString().concat(".00"));
										} else {
											bt.setImporteProvision(sigRegPmaBtcoa.getTotprovision().toString());
										}
										
										Tablas tablas = new Tablas();
										List<Tabla> tab = new ArrayList<>();
										Tabla t = new Tabla();
										t.setCodigoTabla("");
										t.setDesplazamiento("0");
										t.setPjeTabla("0.00");
										Tab35012Key t35012 = new Tab35012Key(StringUtils.leftPad(sigRegPmaBtcoa.getTablacalc1aseg1(),5,"0")); 
										
										if(null != t35012 && null != tab35012Dao.get(t35012)){
											Tab35012 tab35 = tab35012Dao.get(t35012);
											t.setCodigoTabla(tab35.getTablaTirea().replace(" ", ""));
											if(t.getCodigoTabla().equalsIgnoreCase("GRM80") 
													&& StringUtils.leftPad(flujo.getTablacalc1aseg1(),5,"0").equalsIgnoreCase("00705")){
												t.setDesplazamiento("-2");
											}else if(t.getCodigoTabla().equalsIgnoreCase("GRM95")
													&& (StringUtils.leftPad(flujo.getTablacalc1aseg1(),5,"0").equalsIgnoreCase("00365")
															|| StringUtils.leftPad(flujo.getTablacalc1aseg1(),5,"0").equalsIgnoreCase("00721"))){
												t.setDesplazamiento("-1");
											}else{
												t.setDesplazamiento("0");
											}
											t.setPjeTabla(tab35.getPorcentajeTabla().toString().concat(".00"));
										}
										tab.add(t);
										tablas.setTabla(tab);
										bt.setTablas(tablas);
										
										TiposInteres tiposInteres = new TiposInteres();
										List<Interes> listInt = new ArrayList<>();
										Interes inter = new Interes();
										inter.setTipoInteres(sigRegPmaBtcoa.getPintertecnI1().toString());
										String fechaInteresIni = sdfechaInteres.format(sigRegPmaBtcoa.getFecIniTramo1());
										inter.setFechaInicio(fechaInteresIni);
										String fechaInteresFin = sdfechaInteres.format(sigRegPmaBtcoa.getFecFinTramo1());
										inter.setFechaFin(fechaInteresFin);
										listInt.add(inter);
										if(!sigRegPmaBtcoa.getPintertecnI2().equals(BigDecimal.ZERO)){
											Interes inter2 = new Interes();
											inter2.setTipoInteres(sigRegPmaBtcoa.getPintertecnI2().toString());
											String fechaInteresIni2 = sdfechaInteres.format(sigRegPmaBtcoa.getFecIniTramo2());
											inter2.setFechaInicio(fechaInteresIni2);
											String fechaInteresFin2 = sdfechaInteres.format(sigRegPmaBtcoa.getFecFinTramo2());
											inter2.setFechaFin(fechaInteresFin2);
											listInt.add(inter2);
										}
										tiposInteres.setInteres(listInt);
										bt.setTiposInteres(tiposInteres);
										
										if (sigRegPmaBtcoa.getPgastgesin1I().equals(BigDecimal.ZERO)
												|| sigRegPmaBtcoa.getPgastgesin2I().equals(BigDecimal.ZERO)
												|| sigRegPmaBtcoa.getPgastgesex1I().equals(BigDecimal.ZERO)
												|| sigRegPmaBtcoa.getPgastgesex2I().equals(BigDecimal.ZERO)) {
											
											Gastos gastos = new Gastos();
											List<Gasto> listGasto = new ArrayList<>();
											
											if(!sigRegPmaBtcoa.getPgastgesin1I().equals(BigDecimal.ZERO)) {
												Gasto g = new Gasto();
												g.setConcepto("GGI");
												g.setBase("PRIMA");
												if (sigRegPmaBtcoa.getPgastgesin1I().toString().contains(".")) {
													g.setPorcentaje(sigRegPmaBtcoa.getPgastgesin1I().toString());
												} else {
													g.setPorcentaje(sigRegPmaBtcoa.getPgastgesin1I().toString().concat(".00"));
												}
												g.setImporte(sigRegPmaBtcoa.getTotfactgto().toString());
												listGasto.add(g);
											}
											if(!sigRegPmaBtcoa.getPgastgesin2I().equals(BigDecimal.ZERO)) {
												Gasto g = new Gasto();
												g.setConcepto("GGI");
												g.setBase("CAPITAL");
												if (sigRegPmaBtcoa.getPgastgesin2I().toString().contains(".")) {
													g.setPorcentaje(sigRegPmaBtcoa.getPgastgesin2I().toString());
												} else {
													g.setPorcentaje(sigRegPmaBtcoa.getPgastgesin2I().toString().concat(".00"));
												}
												g.setImporte(sigRegPmaBtcoa.getTotfactgto().toString());
												listGasto.add(g);
											}
											if(!sigRegPmaBtcoa.getPgastgesex1I().equals(BigDecimal.ZERO)) {
												Gasto g = new Gasto();
												g.setConcepto("GGE");
												g.setBase("CAPITAL");
												if (sigRegPmaBtcoa.getPgastgesex1I().toString().contains(".")) {
													g.setPorcentaje(sigRegPmaBtcoa.getPgastgesex1I().toString());
												} else {
													g.setPorcentaje(sigRegPmaBtcoa.getPgastgesex1I().toString().concat(".00"));
												}
												g.setImporte(sigRegPmaBtcoa.getTotfactgto().toString());
												listGasto.add(g);
											}
											if(!sigRegPmaBtcoa.getPgastgesex2I().equals(BigDecimal.ZERO)) {
												Gasto g = new Gasto();
												g.setConcepto("GGE");
												g.setBase("PRIMAS");
												if (sigRegPmaBtcoa.getPgastgesex2I().toString().contains(".")) {
													g.setPorcentaje(sigRegPmaBtcoa.getPgastgesex2I().toString());
												} else {
													g.setPorcentaje(sigRegPmaBtcoa.getPgastgesex2I().toString().concat(".00"));
												}
												g.setImporte(sigRegPmaBtcoa.getTotfactgto().toString());
												listGasto.add(g);
											}
											
											if (sigRegPmaBtcoa.getPgastgesin1I().equals(BigDecimal.ZERO)
													&& sigRegPmaBtcoa.getPgastgesin2I().equals(BigDecimal.ZERO)
													&& sigRegPmaBtcoa.getPgastgesex1I().equals(BigDecimal.ZERO)
													&& sigRegPmaBtcoa.getPgastgesex2I().equals(BigDecimal.ZERO)) {
												Gasto g = new Gasto();
												g.setConcepto("GGI");
												g.setBase("PRIMA");
												g.setPorcentaje("0.00");
												g.setImporte("0.00");
												
												listGasto.add(g);
											}
											gastos.setGasto(listGasto);
											bt.setGastos(gastos);
										}
										
										listBt.add(bt);
											
										}else{
										
											Tablas tablas = new Tablas();
											List<Tabla> tab = new ArrayList<>();
											Tabla t = new Tabla();
											t.setCodigoTabla("");
											t.setDesplazamiento("0");
											t.setPjeTabla("0.00");
											tab.add(t);
											tablas.setTabla(tab);
											bt.setTablas(tablas);
											
											TiposInteres tiposInteres = new TiposInteres();
											List<Interes> listInt = new ArrayList<>();
											Interes inter = new Interes();
											inter.setTipoInteres("0.00");
											inter.setFechaInicio("");
											inter.setFechaFin("");
											listInt.add(inter);
											tiposInteres.setInteres(listInt);
											bt.setTiposInteres(tiposInteres);
											Gastos gastos = new Gastos();
											List<Gasto> listGasto = new ArrayList<>();
											Gasto g = new Gasto();
											g.setConcepto("GGI");
											g.setBase("PRIMA");
											g.setPorcentaje("0.00");
											g.setImporte("0.00");
											listGasto.add(g);
											gastos.setGasto(listGasto);
											bt.setGastos(gastos);
											listBt.add(bt);
											
										}								
								}
								
							}
							
							basesTecnicas.setBaseTecnica(listBt);
							datosPMA.setBasesTecnicas(basesTecnicas);	
							pma.setDatosPMA(datosPMA);
							mov.setPMA(pma);
							
							listMovi.add(mov);
						} catch (Exception e) {
							
							Incidencia inci = new Incidencia();
							inci.setFecCierre(fecCierre);
							inci.setCodigoRetorno("11");
							inci.setInfAmpliada("Error" + mensaje + " - " + e.getMessage());
							//gestorIncidenciasPres.write(inci);
							giGeneral.write(inci);
						}
						iteracion++;
					}
					movimientosPma.setMovimiento(listMovi);
				
					try {
					    JAXBContext jaxbContext = JAXBContext.newInstance(MovimientosCoaseguro.class);
					    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
					    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
					
					    File file = new File("XML-PMA.xml");
					    jaxbMarshaller.marshal(movimientosPma, file);
					    
					    Incidencia inci = null;
						InputStream in = null;
						OutputStream out = null;
						BtUtils btUtils = new BtUtils();
					    
					    File destination = new File(btUtils.getRutaFicherosProperty(ConstantesSolvencia.RUTA_EXPORTA_XML) + fecCierre.substring(0,6) + File.separator + "TIREA" + File.separator + "XML-PMA.XML");
						if (file.exists()) {
				            try {
				                in = new FileInputStream(file);
				                try {
				                    out = new FileOutputStream(destination);
				                    try {
				                    	String cierre = ">";
				                    	char separador = '"';
				                    	
				                    	String cabeceraEntrada1 = "<?xml version=" + separador + "1.0" + separador + "encoding=" + separador + "UTF-8" + separador + "standalone=" + separador + "yes" + separador + "?>" + "/n";
				                    	String cabeceraEntrada2 = "<MovimientosCoaseguro>" + "/n";
				                    	
				                    	byte[] bufEntrada1 = cabeceraEntrada1.getBytes();
				                        byte[] bufEntrada2 = cabeceraEntrada2.getBytes();
				                        
				                    	String cabeceraXml = "<?xml version=" + separador + "1.0" + separador + " encoding=" + separador + "UTF-8" + separador + "?>" + "\n";
				                    	String cabeceraXsd = "<MovimientosCoaseguro xmlns=" + separador + "http://www.tirea.es/Coaseguro/MovimientosCoaseguro" + separador +  
				                    		" xmlns:xsi=" + separador + "http://www.w3.org/2001/XMLSchema-instance" + separador 
				                    			+ " xsi:schemaLocation=" + separador + "http://www.tirea.es/Coaseguro/MovimientosCoaseguro MovimientosCoaseguro-v5-6-1_V01.xsd"
				                    		+ separador + cierre + "\n";
				                        byte[] buf = new byte[1024];
				                        byte[] bufxml = cabeceraXml.getBytes();
				                        byte[] bufxsd = cabeceraXsd.getBytes();
				                        int len;
				                        int indiceXsd = 0;
				                        
				                        in.read(bufEntrada1);
				                        out.write(bufxml,0,bufxml.length);
				                        in.read(bufEntrada2);
				                        out.write(bufxsd,0,bufxsd.length);
				                        
				                        
				        				while ((len = in.read(buf)) > 0) {
				        				    	out.write(buf, 0, len);
				        				}
				                        in.close();
				                        out.close();
				        			} catch (IOException e) {
				                    	inci = new Incidencia();
				                    	inci.setCodigoRetorno("12");
				                    	inci.setInfAmpliada("Error al copiar el fichero " + file + " al fichero " + destination + " - " + e.getMessage());
				        			}
				                } catch (FileNotFoundException e) {
				                	inci = new Incidencia();
				                	inci.setCodigoRetorno("12");
				                	inci.setInfAmpliada("Error al crear el fichero " + destination + " - " + e.getMessage());
				                }
				            } catch (FileNotFoundException e) {
				            	inci = new Incidencia();
				            	inci.setCodigoRetorno("12");
				            	inci.setInfAmpliada("Error al cargar el fichero " + file + " - " + e.getMessage());
				            }
				        } else {
				        	inci = new Incidencia();
				        	inci.setCodigoRetorno("12");
				        	inci.setInfAmpliada("No se encuentra el fichero " + file);
				        }
					    

					} catch (JAXBException e) {
					    e.printStackTrace();
					}
		
					//////////////////////PMF////////////////////////	
					List<FlujPMaCoa> listaFlujPmaCoa = flujPmaDao.getValues();
					Collections.sort(listaFlujPmaCoa, new Comparator<FlujPMaCoa>() {
						public int compare(FlujPMaCoa o1, FlujPMaCoa o2) {
							return o1.getKey().compareTo(o2.getKey());
						}
					});
					List<FlujPmaCoaBtcoa> listaFlujPmaCoaBtcoa = flujPmaBtcoaDao.getValues();
					Collections.sort(listaFlujPmaCoaBtcoa, new Comparator<FlujPmaCoaBtcoa>() {
						public int compare(FlujPmaCoaBtcoa o1, FlujPmaCoaBtcoa o2) {
							return o1.getKey().compareTo(o2.getKey());
						}
					});
					
					List<FlujPmaCoaBtcoatf> listaFlujPmaCoaBtcoatf = flujPmaBtcoatfDao.getValues();
					Collections.sort(listaFlujPmaCoaBtcoatf, new Comparator<FlujPmaCoaBtcoatf>() {
						public int compare(FlujPmaCoaBtcoatf o1, FlujPmaCoaBtcoatf o2) {
							return o1.getKey().compareTo(o2.getKey());
						}
					});
					
					Iterator<FlujPMaCoa> itPmf = listaFlujPmaCoa.iterator();
					Iterator<FlujPmaCoaBtcoa> itPmfBtcoa = listaFlujPmaCoaBtcoa.iterator();
					Iterator<FlujPmaCoaBtcoatf> itPmfBtcoatf = listaFlujPmaCoaBtcoatf.iterator();
					
					MovimientosCoaseguroPMF movimientosPmf =  new MovimientosCoaseguroPMF();

					BigDecimal auxCalculoFlujos = BigDecimal.ZERO;
					int indicePmf = 0;
					Long poliza = new Long(0);
					Integer subpoliza = 0;
					Integer nsuscri = 0;
					String kcerti = "";
					
					Cabecera cab = new Cabecera();
					
					SimpleDateFormat sdfecha = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
					String fechaComoCadena = sdfecha.format(new Date());
					cab.setFecha(fechaComoCadena);
					cab.setIdEmisor("C0511");
					SimpleDateFormat sdfAux = new SimpleDateFormat("yyyyMMddHHmm");
					String fechaIdFichero = sdfAux.format(new Date());
					cab.setIdFichero(fechaIdFichero);
					cab.setIdReceptor("T0001");
					movimientosPmf.setCabecera(cab);
					
					List<DatosFlujo> listDatosFlujo = new ArrayList<>();
					List<MovimientoPMF> listMoviPmf = new ArrayList<>();
					DatosFlujos datosFlujos = new DatosFlujos();
					PMF pmf = new PMF();
					MovimientoPMF movPMF = new MovimientoPMF();
					int contador = 0;
					while (itPmf.hasNext()) {
						contador++;
						
						FlujPMaCoa flujo = itPmf.next();
						
						if (flujo.getTotflujoprobdegastos().scale() < 2) {
							flujo.setTotflujoprobdegastos(flujo.getTotflujoprobdegastos().setScale(2));
						}
						if (flujo.getTotflujoprobsingastos().scale() < 2) {
							flujo.setTotflujoprobsingastos(flujo.getTotflujoprobsingastos().setScale(2));
						}
						
						if(itPmfBtcoa.hasNext()){
							sigRegBtcoa = itPmfBtcoa.next();
						}
						
						if (sigRegBtcoa.getTotflujoprobdegastos().scale() < 2) {
							sigRegBtcoa.setTotflujoprobdegastos(sigRegBtcoa.getTotflujoprobdegastos().setScale(2));
						}
						if (sigRegBtcoa.getTotflujoprobsingastos().scale() < 2) {
							sigRegBtcoa.setTotflujoprobsingastos(sigRegBtcoa.getTotflujoprobsingastos().setScale(2));
						}
						
						if(itPmfBtcoatf.hasNext()){
							sigRegBtcoatf = itPmfBtcoatf.next();
						}
						
						if (sigRegBtcoatf.getTotflujoprobdegastos().scale() < 2) {
							sigRegBtcoatf.setTotflujoprobdegastos(sigRegBtcoatf.getTotflujoprobdegastos().setScale(2));
						}
						if (sigRegBtcoatf.getTotflujoprobsingastos().scale() < 2) {
							sigRegBtcoatf.setTotflujoprobsingastos(sigRegBtcoatf.getTotflujoprobsingastos().setScale(2));
						}
	
						if(indicePmf == 0){
							
							try {
								
								DatosCoaKey datCoa = new DatosCoaKey(flujo.getKpoliza(),flujo.getKsubpoliza()); 
								DatosCoa datoscoaseg = datosCoaDao.get(datCoa);
								
								movPMF = new MovimientoPMF();
								
								movPMF.setTipoMovimiento(7);
								
								String fechaAux = fecCierre.substring(0,6);
								if(fechaAux.substring(4).equals("01") || fechaAux.substring(4).equals("03") || fechaAux.substring(4).equals("05") 
									|| fecCierre.substring(4).equals("07") || fechaAux.substring(4).equals("08") || fechaAux.substring(4).equals("10") || fechaAux.substring(4).equals("12") ){
									fechaAux = fecCierre.substring(0,4) + "-" + fecCierre.substring(4,6) + "-" + "31";
								}else if(fechaAux.substring(4).equals("02")){
									fechaAux = fecCierre.substring(0,4) + "-" + fecCierre.substring(4,6) + "-" + "28";
								}else{
									fechaAux = fecCierre.substring(0,4) + "-" + fecCierre.substring(4,6) + "-" + "30";
								}
								
								movPMF.setFecha(fechaAux);
								
								pmf = new PMF();
								DatosPoliza datosPoliza = new DatosPoliza();
								
								datosPoliza.setEntidadAbridora(datoscoaseg.getkCoaseOri().substring(0,1) + "0" + datoscoaseg.getkCoaseOri().substring(1));
								
								if(datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase1())){
									datosPoliza.setParticipacionAbridora(datoscoaseg.getpCoase1().toString());
								}else if(datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase2())){
									datosPoliza.setParticipacionAbridora(datoscoaseg.getpCoase2().toString());
								}else if(datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase3())){
									datosPoliza.setParticipacionAbridora(datoscoaseg.getpCoase3().toString());
								}else if(datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase4())){
									datosPoliza.setParticipacionAbridora(datoscoaseg.getpCoase4().toString());
								}else if(datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase5())){
									datosPoliza.setParticipacionAbridora(datoscoaseg.getpCoase5().toString());
								}
								
								int widthPoliza = 10;
								int widthSubPoliza = 4;
								 
						        String formattedPoliza = String.format("%0" + widthPoliza + "d", Integer.valueOf(flujo.getKpoliza().toString()));
						        String formattedSubPoliza = String.format("%0" + widthSubPoliza + "d", Integer.valueOf(flujo.getKsubpoliza().toString()));
								
								datosPoliza.setNumeroPoliza(formattedPoliza + formattedSubPoliza);
								datosPoliza.setNumeroSuplemento(flujo.getNsuscri().toString());
								
								CuadroCoaseguro cuadroCoaseg = new CuadroCoaseguro();
								
								List<ParticipacionEntidad> partEntidad = new ArrayList<>();
								
								if(null != datoscoaseg.getKCoase1() && !datoscoaseg.getKCoase1().equals("")){
								
									if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase1())){
									
										ParticipacionEntidad ent1 = new ParticipacionEntidad();
										ent1.setEntidadAceptante(datoscoaseg.getKCoase1().substring(0,1) + "0" + datoscoaseg.getKCoase1().substring(1));
										ent1.setParticipacionAceptante(datoscoaseg.getpCoase1().toString());
										partEntidad.add(ent1);
									}
								}
								
								if(null != datoscoaseg.getKCoase2() && !datoscoaseg.getKCoase2().equals("")){
									
									if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase2())){
									ParticipacionEntidad ent2 = new ParticipacionEntidad();
									ent2.setEntidadAceptante(datoscoaseg.getKCoase2().substring(0,1) + "0" + datoscoaseg.getKCoase2().substring(1));
									ent2.setParticipacionAceptante(datoscoaseg.getpCoase2().toString());
									partEntidad.add(ent2);
									}
								}
								
								if(null != datoscoaseg.getKCoase3() && !datoscoaseg.getKCoase3().equals("")){
									if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase3())){
								
									ParticipacionEntidad ent3 = new ParticipacionEntidad();
									ent3.setEntidadAceptante(datoscoaseg.getKCoase3().substring(0,1) + "0" + datoscoaseg.getKCoase3().substring(1));
									ent3.setParticipacionAceptante(datoscoaseg.getpCoase3().toString());
									partEntidad.add(ent3);
									}
								}
								
								if(null != datoscoaseg.getKCoase4() && !datoscoaseg.getKCoase4().equals("")){
									if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase4())){
								
									ParticipacionEntidad ent4 = new ParticipacionEntidad();
									ent4.setEntidadAceptante(datoscoaseg.getKCoase4().substring(0,1) + "0" + datoscoaseg.getKCoase4().substring(1));
									ent4.setParticipacionAceptante(datoscoaseg.getpCoase4().toString());
									partEntidad.add(ent4);
									}
								}
								
								if(null != datoscoaseg.getKCoase5() && !datoscoaseg.getKCoase5().equals("")){
									if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase5())){
									ParticipacionEntidad ent5 = new ParticipacionEntidad();
									ent5.setEntidadAceptante(datoscoaseg.getKCoase5().substring(0,1) + "0" + datoscoaseg.getKCoase5().substring(1));
									ent5.setParticipacionAceptante(datoscoaseg.getpCoase5().toString());
									partEntidad.add(ent5);
									}
								}
								
								cuadroCoaseg.setParticipacionEntidad(partEntidad);
								datosPoliza.setCuadroCoaseguro(cuadroCoaseg);
								
								SimpleDateFormat sdfechaEfecto = new SimpleDateFormat("yyyy-MM-dd");
								String fechaEfecto = sdfechaEfecto.format(flujo.getFecefecini());
								datosPoliza.setFechaEfecto(fechaEfecto);
								
								
								datosPoliza.setMetodoSuplemento("INCREMENTAL");
								pmf.setDatosPoliza(datosPoliza);

								datosFlujos = new DatosFlujos();
								
								BasesTecnicasPMF basesTecnicasPmf = new BasesTecnicasPMF();
								
								List<BaseTecnicaPMF> listBtPmf = new ArrayList<>();
								
								for(int i = 0; i < 3; i++){
									BaseTecnicaPMF btPmf = new BaseTecnicaPMF();
									
									if(i==0){
										btPmf.setTipoTablaExperiencia("BT");
										btPmf.setTipoInteres(flujo.getPintertecnI1().toString());
										
										if(null == flujo.getTotflujoprobsingastos()){
											btPmf.setImporteFlujo("0.00");
										}else{
											btPmf.setImporteFlujo(flujo.getTotflujoprobsingastos().toString());
										}
										
										if(null == flujo.getTotflujoprobdegastos()){
											if(null == flujo.getTotflujoprobsingastos()){
												btPmf.setImporteFlujoGastos("0.00");
											}else{
												btPmf.setImporteFlujoGastos(flujo.getTotflujoprobsingastos().toString());
											}
											
										}else{
											if(null == flujo.getTotflujoprobsingastos()){
												btPmf.setImporteFlujoGastos(flujo.getTotflujoprobdegastos().toString());
											}else{
												auxCalculoFlujos = flujo.getTotflujoprobsingastos().add(flujo.getTotflujoprobdegastos());
												btPmf.setImporteFlujoGastos(auxCalculoFlujos.toString());									
											}
										}
										
										listBtPmf.add(btPmf);
									}else if(i==1){
										
										btPmf.setTipoTablaExperiencia("DGS");
										
										if(null != sigRegBtcoatf && sigRegBtcoatf.getKpoliza().equals(flujo.getKpoliza())
												&& sigRegBtcoatf.getKsubpoliza().equals(flujo.getKsubpoliza()) && 
												sigRegBtcoatf.getNsuscri().equals(flujo.getNsuscri()) && sigRegBtcoatf.getFdesde().equals(flujo.getFdesde())){
												
											btPmf.setTipoInteres(sigRegBtcoatf.getPintertecnI1().toString());
												
											if(null == sigRegBtcoatf.getTotflujoprobsingastos()){
												btPmf.setImporteFlujo("0.00");
											}else{
												btPmf.setImporteFlujo(sigRegBtcoatf.getTotflujoprobsingastos().toString()); 
											}
												
											if(null == sigRegBtcoatf.getTotflujoprobdegastos()){
												if(null == sigRegBtcoatf.getTotflujoprobsingastos()){
													btPmf.setImporteFlujoGastos("0.00");
												}else{
													btPmf.setImporteFlujoGastos(sigRegBtcoatf.getTotflujoprobsingastos().toString());
												}
												
											}else{
												if(null == sigRegBtcoatf.getTotflujoprobsingastos()){
													btPmf.setImporteFlujoGastos(sigRegBtcoatf.getTotflujoprobdegastos().toString());
												}else{
													auxCalculoFlujos = sigRegBtcoatf.getTotflujoprobsingastos().add(sigRegBtcoatf.getTotflujoprobdegastos());
													btPmf.setImporteFlujoGastos(auxCalculoFlujos.toString());									
												}
											}
													
										}else{
												
											btPmf.setTipoInteres("0.00");
											btPmf.setImporteFlujo("0.00");
											btPmf.setImporteFlujoGastos("0.00");
												
										}
										
										listBtPmf.add(btPmf);
									}else{
										
										
										btPmf.setTipoTablaExperiencia("PC");
										
										if(null != sigRegBtcoa && sigRegBtcoa.getKpoliza().equals(flujo.getKpoliza())
												&& sigRegBtcoa.getKsubpoliza().equals(flujo.getKsubpoliza()) && 
												sigRegBtcoa.getNsuscri().equals(flujo.getNsuscri()) && sigRegBtcoa.getFdesde().equals(flujo.getFdesde())){
												
											btPmf.setTipoInteres(sigRegBtcoa.getPintertecnI1().toString());
												
											if(null == sigRegBtcoa.getTotflujoprobsingastos()){
												btPmf.setImporteFlujo("0.00");
											}else{
												btPmf.setImporteFlujo(sigRegBtcoa.getTotflujoprobsingastos().toString());
											}
												
											if(null == sigRegBtcoa.getTotflujoprobdegastos()){
												if(null == sigRegBtcoa.getTotflujoprobsingastos()){
													btPmf.setImporteFlujoGastos("0.00");
												}else{
													btPmf.setImporteFlujoGastos(sigRegBtcoa.getTotflujoprobsingastos().toString());
												}
												
											}else{
												if(null == sigRegBtcoa.getTotflujoprobsingastos()){
													btPmf.setImporteFlujoGastos(sigRegBtcoa.getTotflujoprobdegastos().toString());
												}else{
													auxCalculoFlujos = sigRegBtcoa.getTotflujoprobsingastos().add(sigRegBtcoa.getTotflujoprobdegastos());
													btPmf.setImporteFlujoGastos(auxCalculoFlujos.toString());									
												}
											}
	
										}else{
												
											btPmf.setTipoInteres("0.00");
											btPmf.setImporteFlujo("0.00");
											btPmf.setImporteFlujoGastos("0.00");
												
										}
										
										listBtPmf.add(btPmf);
									}
								}
								
								DatosFlujo datosFlujo = new DatosFlujo();
								if(null == flujo.getFdesde()){
									datosFlujo.setFechaFlujo("");
								}else{
									String fechaFlujo = flujo.getFdesde().substring(2) + "-" + flujo.getFdesde().substring(0,2) + "-" + "01";
									datosFlujo.setFechaFlujo(fechaFlujo);
								}
								
								basesTecnicasPmf.setBaseTecnica(listBtPmf);
								datosFlujo.setBasesTecnicas(basesTecnicasPmf);
								listDatosFlujo.add(datosFlujo);		
								
							} catch (Exception e) {
								Incidencia inci = new Incidencia();
								inci.setFecCierre(fecCierre);
								inci.setCodigoRetorno("11");
								inci.setInfAmpliada("Error al obtener los pesos para la prestaci�n. " + mensaje + " - " + e.getMessage());
								giGeneral.write(inci);
							}
							
							poliza = flujo.getKpoliza();
							subpoliza = flujo.getKsubpoliza();
							nsuscri = flujo.getNsuscri();
		
							indicePmf++;
							
							
						}else if(indicePmf != 0 && poliza.equals(flujo.getKpoliza()) && subpoliza.equals(flujo.getKsubpoliza())
								&& nsuscri.equals(flujo.getNsuscri())){
							
							BasesTecnicasPMF basesTecnicasPmf = new BasesTecnicasPMF();
							
							List<BaseTecnicaPMF> listBtPmf = new ArrayList<>();
							
							for(int i = 0; i < 3; i++){
								BaseTecnicaPMF btPmf = new BaseTecnicaPMF();
								
								if(i==0){
									btPmf.setTipoTablaExperiencia("BT");
									btPmf.setTipoInteres(flujo.getPintertecnI1().toString());
									
									if(null == flujo.getTotflujoprobsingastos()){
										btPmf.setImporteFlujo("0.00");
									}else{
										btPmf.setImporteFlujo(flujo.getTotflujoprobsingastos().toString());
									}
									
									if(null == flujo.getTotflujoprobdegastos()){
										if(null == flujo.getTotflujoprobsingastos()){
											btPmf.setImporteFlujoGastos("0.00");
										}else{
											btPmf.setImporteFlujoGastos(flujo.getTotflujoprobsingastos().toString());
										}
										
									}else{
										if(null == flujo.getTotflujoprobsingastos()){
											btPmf.setImporteFlujoGastos(flujo.getTotflujoprobdegastos().toString());
										}else{
											auxCalculoFlujos = flujo.getTotflujoprobsingastos().add(flujo.getTotflujoprobdegastos());
											btPmf.setImporteFlujoGastos(auxCalculoFlujos.toString());									
										}
									}
									
									listBtPmf.add(btPmf);
								}else if(i==1){
									
									btPmf.setTipoTablaExperiencia("DGS");
									
									if(null != sigRegBtcoatf && sigRegBtcoatf.getKpoliza().equals(flujo.getKpoliza())
											&& sigRegBtcoatf.getKsubpoliza().equals(flujo.getKsubpoliza()) && 
											sigRegBtcoatf.getNsuscri().equals(flujo.getNsuscri()) && sigRegBtcoatf.getFdesde().equals(flujo.getFdesde())){
											
										btPmf.setTipoInteres(sigRegBtcoatf.getPintertecnI1().toString());
											
										if(null == sigRegBtcoatf.getTotflujoprobsingastos()){
											btPmf.setImporteFlujo("0.00");
										}else{
											btPmf.setImporteFlujo(sigRegBtcoatf.getTotflujoprobsingastos().toString()); 
										}
											
										if(null == sigRegBtcoatf.getTotflujoprobdegastos()){
											if(null == sigRegBtcoatf.getTotflujoprobsingastos()){
												btPmf.setImporteFlujoGastos("0.00");
											}else{
												btPmf.setImporteFlujoGastos(sigRegBtcoatf.getTotflujoprobsingastos().toString());
											}
											
										}else{
											if(null == sigRegBtcoatf.getTotflujoprobsingastos()){
												btPmf.setImporteFlujoGastos(sigRegBtcoatf.getTotflujoprobdegastos().toString());
											}else{
												auxCalculoFlujos = sigRegBtcoatf.getTotflujoprobsingastos().add(sigRegBtcoatf.getTotflujoprobdegastos());
												btPmf.setImporteFlujoGastos(auxCalculoFlujos.toString());									
											}
										}
										
									}else{
											
										btPmf.setTipoInteres("0.00");
										btPmf.setImporteFlujo("0.00");
										btPmf.setImporteFlujoGastos("0.00");
											
									}
									
									listBtPmf.add(btPmf);
								}else{
									
									
									btPmf.setTipoTablaExperiencia("PC");
									
									if(null != sigRegBtcoa && sigRegBtcoa.getKpoliza().equals(flujo.getKpoliza())
											&& sigRegBtcoa.getKsubpoliza().equals(flujo.getKsubpoliza()) && 
											sigRegBtcoa.getNsuscri().equals(flujo.getNsuscri()) && sigRegBtcoa.getFdesde().equals(flujo.getFdesde())){
											
										btPmf.setTipoInteres(sigRegBtcoa.getPintertecnI1().toString());
											
										if(null == sigRegBtcoa.getTotflujoprobsingastos()){
											btPmf.setImporteFlujo("0.00");
										}else{
											btPmf.setImporteFlujo(sigRegBtcoa.getTotflujoprobsingastos().toString());
										}
											
										
										if(null == sigRegBtcoa.getTotflujoprobdegastos()){
											if(null == sigRegBtcoa.getTotflujoprobsingastos()){
												btPmf.setImporteFlujoGastos("0.00");
											}else{
												btPmf.setImporteFlujoGastos(sigRegBtcoa.getTotflujoprobsingastos().toString());
											}
											
										}else{
											if(null == sigRegBtcoa.getTotflujoprobsingastos()){
												btPmf.setImporteFlujoGastos(sigRegBtcoa.getTotflujoprobdegastos().toString());
											}else{
												auxCalculoFlujos = sigRegBtcoa.getTotflujoprobsingastos().add(sigRegBtcoa.getTotflujoprobdegastos());
												btPmf.setImporteFlujoGastos(auxCalculoFlujos.toString());									
											}
										}
											
									}else{
											
										btPmf.setTipoInteres("0.00");
										btPmf.setImporteFlujo("0.00");
										btPmf.setImporteFlujoGastos("0.00");
											
									}
										
									listBtPmf.add(btPmf);
								}
							}
							
							DatosFlujo datosFlujo = new DatosFlujo();
							if(null == flujo.getFdesde()){
								datosFlujo.setFechaFlujo("");
							}else{
								String fechaFlujo = flujo.getFdesde().substring(2) + "-" + flujo.getFdesde().substring(0,2) + "-" + "01";
								datosFlujo.setFechaFlujo(fechaFlujo);
							}
							//datosFlujo.setFechaFlujo(flujo.getFdesde().toString());
							basesTecnicasPmf.setBaseTecnica(listBtPmf);
							datosFlujo.setBasesTecnicas(basesTecnicasPmf);
							listDatosFlujo.add(datosFlujo);			
							
							poliza = flujo.getKpoliza();
							subpoliza = flujo.getKsubpoliza();
							nsuscri = flujo.getNsuscri();

							indicePmf++;
							
						}else if(indicePmf != 0 && (!poliza.equals(flujo.getKpoliza()) || !subpoliza.equals(flujo.getKsubpoliza())
								|| !nsuscri.equals(flujo.getNsuscri()))){
							datosFlujos.setDatosFlujo(listDatosFlujo);
							pmf.setDatosFlujos(datosFlujos);
							movPMF.setPMF(pmf);
							listMoviPmf.add(movPMF);
							listDatosFlujo = new ArrayList<>();
							DatosCoaKey datCoa = new DatosCoaKey(flujo.getKpoliza(),flujo.getKsubpoliza()); 
							DatosCoa datoscoaseg = datosCoaDao.get(datCoa);
							
							movPMF = new MovimientoPMF();
							
							movPMF.setTipoMovimiento(7);
							
							String fechaAux = fecCierre.substring(0,6);
							if(fechaAux.substring(4).equals("01") || fechaAux.substring(4).equals("03") || fechaAux.substring(4).equals("05") 
								|| fecCierre.substring(4).equals("07") || fechaAux.substring(4).equals("08") || fechaAux.substring(4).equals("10") || fechaAux.substring(4).equals("12") ){
								fechaAux = fecCierre.substring(0,4) + "-" + fecCierre.substring(4,6) + "-" + "31";
							}else if(fechaAux.substring(4).equals("02")){
								fechaAux = fecCierre.substring(0,4) + "-" + fecCierre.substring(4,6) + "-" + "28";
							}else{
								fechaAux = fecCierre.substring(0,4) + "-" + fecCierre.substring(4,6) + "-" + "30";
							}
							
							movPMF.setFecha(fechaAux);
							
							pmf = new PMF();
							DatosPoliza datosPoliza = new DatosPoliza();
							
							datosPoliza.setEntidadAbridora(datoscoaseg.getkCoaseOri().substring(0,1) + "0" + datoscoaseg.getkCoaseOri().substring(1));

							if(datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase1())){
								datosPoliza.setParticipacionAbridora(datoscoaseg.getpCoase1().toString());
							}else if(datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase2())){
								datosPoliza.setParticipacionAbridora(datoscoaseg.getpCoase2().toString());
							}else if(datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase3())){
								datosPoliza.setParticipacionAbridora(datoscoaseg.getpCoase3().toString());
							}else if(datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase4())){
								datosPoliza.setParticipacionAbridora(datoscoaseg.getpCoase4().toString());
							}else if(datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase5())){
								datosPoliza.setParticipacionAbridora(datoscoaseg.getpCoase5().toString());
							}
							
							int widthPoliza = 10;
							int widthSubPoliza = 4;
							 
					        String formattedPoliza = String.format("%0" + widthPoliza + "d", Integer.valueOf(flujo.getKpoliza().toString()));
					        String formattedSubPoliza = String.format("%0" + widthSubPoliza + "d", Integer.valueOf(flujo.getKsubpoliza().toString()));
							
							datosPoliza.setNumeroPoliza(formattedPoliza + formattedSubPoliza);
							datosPoliza.setNumeroSuplemento(flujo.getNsuscri().toString());
							
							CuadroCoaseguro cuadroCoaseg = new CuadroCoaseguro();
							
							List<ParticipacionEntidad> partEntidad = new ArrayList<>();
							
							if(null != datoscoaseg.getKCoase1() && !datoscoaseg.getKCoase1().equals("")){
								
								if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase1())){
								
									ParticipacionEntidad ent1 = new ParticipacionEntidad();
									ent1.setEntidadAceptante(datoscoaseg.getKCoase1().substring(0,1) + "0" + datoscoaseg.getKCoase1().substring(1));								
									ent1.setParticipacionAceptante(datoscoaseg.getpCoase1().toString());
									partEntidad.add(ent1);
								}
							}
							
							if(null != datoscoaseg.getKCoase2() && !datoscoaseg.getKCoase2().equals("")){
								
								if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase2())){
								ParticipacionEntidad ent2 = new ParticipacionEntidad();
								ent2.setEntidadAceptante(datoscoaseg.getKCoase2().substring(0,1) + "0" + datoscoaseg.getKCoase2().substring(1));								
								ent2.setParticipacionAceptante(datoscoaseg.getpCoase2().toString());
								partEntidad.add(ent2);
								}
							}
							
							if(null != datoscoaseg.getKCoase3() && !datoscoaseg.getKCoase3().equals("")){
								if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase3())){
							
								ParticipacionEntidad ent3 = new ParticipacionEntidad();
								ent3.setEntidadAceptante(datoscoaseg.getKCoase3().substring(0,1) + "0" + datoscoaseg.getKCoase3().substring(1));								
								ent3.setParticipacionAceptante(datoscoaseg.getpCoase3().toString());
								partEntidad.add(ent3);
								}
							}
							
							if(null != datoscoaseg.getKCoase4() && !datoscoaseg.getKCoase4().equals("")){
								if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase4())){
							
								ParticipacionEntidad ent4 = new ParticipacionEntidad();
								ent4.setEntidadAceptante(datoscoaseg.getKCoase4().substring(0,1) + "0" + datoscoaseg.getKCoase4().substring(1));								
								ent4.setParticipacionAceptante(datoscoaseg.getpCoase4().toString());
								partEntidad.add(ent4);
								}
							}
							
							if(null != datoscoaseg.getKCoase5() && !datoscoaseg.getKCoase5().equals("")){
								if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase5())){
								ParticipacionEntidad ent5 = new ParticipacionEntidad();
								ent5.setEntidadAceptante(datoscoaseg.getKCoase5().substring(0,1) + "0" + datoscoaseg.getKCoase5().substring(1));								
								ent5.setParticipacionAceptante(datoscoaseg.getpCoase5().toString());
								partEntidad.add(ent5);
								}
							}
							
							cuadroCoaseg.setParticipacionEntidad(partEntidad);
							datosPoliza.setCuadroCoaseguro(cuadroCoaseg);
							SimpleDateFormat sdfechaEfecto = new SimpleDateFormat("yyyy-MM-dd");
							String fechaEfecto = sdfechaEfecto.format(flujo.getFecefecini());
							datosPoliza.setFechaEfecto(fechaEfecto);
							datosPoliza.setMetodoSuplemento("INCREMENTAL");
							pmf.setDatosPoliza(datosPoliza);

							datosFlujos = new DatosFlujos();
							
							BasesTecnicasPMF basesTecnicasPmf = new BasesTecnicasPMF();
							
							List<BaseTecnicaPMF> listBtPmf = new ArrayList<>();
							
							for(int i = 0; i < 3; i++){
								BaseTecnicaPMF btPmf = new BaseTecnicaPMF();
								
								if(i==0){
									btPmf.setTipoTablaExperiencia("BT");
									btPmf.setTipoInteres(flujo.getPintertecnI1().toString());
									
									if(null == flujo.getTotflujoprobsingastos()){
										btPmf.setImporteFlujo("0.00");
									}else{
										btPmf.setImporteFlujo(flujo.getTotflujoprobsingastos().toString());
									}
									
									if(null == flujo.getTotflujoprobdegastos()){
										if(null == flujo.getTotflujoprobsingastos()){
											btPmf.setImporteFlujoGastos("0.00");
										}else{
											btPmf.setImporteFlujoGastos(flujo.getTotflujoprobsingastos().toString());
										}
										
									}else{
										if(null == flujo.getTotflujoprobsingastos()){
											btPmf.setImporteFlujoGastos(flujo.getTotflujoprobdegastos().toString());
										}else{
											auxCalculoFlujos = flujo.getTotflujoprobsingastos().add(flujo.getTotflujoprobdegastos());
											btPmf.setImporteFlujoGastos(auxCalculoFlujos.toString());									
										}
									}
									
									listBtPmf.add(btPmf);
								}else if(i==1){
									
									btPmf.setTipoTablaExperiencia("DGS");
									
									if(null != sigRegBtcoatf && sigRegBtcoatf.getKpoliza().equals(flujo.getKpoliza())
											&& sigRegBtcoatf.getKsubpoliza().equals(flujo.getKsubpoliza()) && 
											sigRegBtcoatf.getNsuscri().equals(flujo.getNsuscri()) && sigRegBtcoatf.getFdesde().equals(flujo.getFdesde())){
											
										btPmf.setTipoInteres(sigRegBtcoatf.getPintertecnI1().toString());
											
										if(null == sigRegBtcoatf.getTotflujoprobsingastos()){
											btPmf.setImporteFlujo("0.00");
										}else{
											btPmf.setImporteFlujo(sigRegBtcoatf.getTotflujoprobsingastos().toString()); 
										}
											
										if(null == sigRegBtcoatf.getTotflujoprobdegastos()){
											if(null == sigRegBtcoatf.getTotflujoprobsingastos()){
												btPmf.setImporteFlujoGastos("0.00");
											}else{
												btPmf.setImporteFlujoGastos(sigRegBtcoatf.getTotflujoprobsingastos().toString());
											}
											
										}else{
											if(null == sigRegBtcoatf.getTotflujoprobsingastos()){
												btPmf.setImporteFlujoGastos(sigRegBtcoatf.getTotflujoprobdegastos().toString());
											}else{
												auxCalculoFlujos = sigRegBtcoatf.getTotflujoprobsingastos().add(sigRegBtcoatf.getTotflujoprobdegastos());
												btPmf.setImporteFlujoGastos(auxCalculoFlujos.toString());									
											}
										}

									}else{
											
										btPmf.setTipoInteres("0.00");
										btPmf.setImporteFlujo("0.00");
										btPmf.setImporteFlujoGastos("0.00");
											
									}
									
									listBtPmf.add(btPmf);
								}else{
									
									
									btPmf.setTipoTablaExperiencia("PC");
									
									if(null != sigRegBtcoa && sigRegBtcoa.getKpoliza().equals(flujo.getKpoliza())
											&& sigRegBtcoa.getKsubpoliza().equals(flujo.getKsubpoliza()) && 
											sigRegBtcoa.getNsuscri().equals(flujo.getNsuscri()) && sigRegBtcoa.getFdesde().equals(flujo.getFdesde())){
											
										btPmf.setTipoInteres(sigRegBtcoa.getPintertecnI1().toString());
											
										if(null == sigRegBtcoa.getTotflujoprobsingastos()){
											btPmf.setImporteFlujo("0.00");
										}else{
											btPmf.setImporteFlujo(sigRegBtcoa.getTotflujoprobsingastos().toString());
										}
											
										if(null == sigRegBtcoa.getTotflujoprobdegastos()){
											if(null == sigRegBtcoa.getTotflujoprobsingastos()){
												btPmf.setImporteFlujoGastos("0.00");
											}else{
												btPmf.setImporteFlujoGastos(sigRegBtcoa.getTotflujoprobsingastos().toString());
											}
											
										}else{
											if(null == sigRegBtcoa.getTotflujoprobsingastos()){
												btPmf.setImporteFlujoGastos(sigRegBtcoa.getTotflujoprobdegastos().toString());
											}else{
												auxCalculoFlujos = sigRegBtcoa.getTotflujoprobsingastos().add(sigRegBtcoa.getTotflujoprobdegastos());
												btPmf.setImporteFlujoGastos(auxCalculoFlujos.toString());									
											}
										}
			
									}else{
											
										btPmf.setTipoInteres("0.00");
										btPmf.setImporteFlujo("0.00");
										btPmf.setImporteFlujoGastos("0.00");
											
									}
									
									listBtPmf.add(btPmf);
								}
							}
							
							DatosFlujo datosFlujo = new DatosFlujo();
							if(null == flujo.getFdesde()){
								datosFlujo.setFechaFlujo("");
							}else{
								String fechaFlujo = flujo.getFdesde().substring(2) + "-" + flujo.getFdesde().substring(0,2) + "-" + "01";
								datosFlujo.setFechaFlujo(fechaFlujo);
							}
							
							basesTecnicasPmf.setBaseTecnica(listBtPmf);
							datosFlujo.setBasesTecnicas(basesTecnicasPmf);
							listDatosFlujo.add(datosFlujo);		
							
							poliza = flujo.getKpoliza();
							subpoliza = flujo.getKsubpoliza();
							nsuscri = flujo.getNsuscri();
							indicePmf++;
							
						}						
					}
					
					datosFlujos.setDatosFlujo(listDatosFlujo);
					pmf.setDatosFlujos(datosFlujos);
					movPMF.setPMF(pmf);
					listMoviPmf.add(movPMF);
					movimientosPmf.setMovimientopmf(listMoviPmf);
					
					try {
					    JAXBContext jaxbContext = JAXBContext.newInstance(MovimientosCoaseguroPMF.class);
					    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
					    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

					    File file = new File("XML-PMF.xml");
					    jaxbMarshaller.marshal(movimientosPmf, file);
					    
					    Incidencia inci = null;
						InputStream in = null;
						OutputStream out = null;
						BtUtils btUtils = new BtUtils();
						
					    File destination = new File(btUtils.getRutaFicherosProperty(ConstantesSolvencia.RUTA_EXPORTA_XML) + fecCierre.substring(0,6) + File.separator + "TIREA" + File.separator + "XML-PMF.XML");
						if (file.exists()) {
				            try {
				                in = new FileInputStream(file);
				                try {
				                    out = new FileOutputStream(destination);
				                    try {
				                    	String cierre = ">";
				                    	char separador = '"';
				                    	String apertura = "<";
				                    	
				                    	String cabeceraEntrada1 = "<?xml version=" + separador + "1.0" + separador + "encoding=" + separador + "UTF-8" + separador + "standalone=" + separador + "yes" + separador + "?>" + "/n";
				                    	String cabeceraEntrada2 = "<MovimientosCoaseguro>" + "/n";
				                    	
				                    	byte[] bufEntrada1 = cabeceraEntrada1.getBytes();
				                        byte[] bufEntrada2 = cabeceraEntrada2.getBytes();
				             
				                    	String cabeceraXml = "<?xml version=" + separador + "1.0" + separador + " encoding=" + separador + "UTF-8" + separador + "?>" + "\n";
				                    	String cabeceraXsd = "<MovimientosCoaseguro xmlns=" + separador + "http://www.tirea.es/Coaseguro/MovimientosCoaseguro" + separador +  
				                    		" xmlns:xsi=" + separador + "http://www.w3.org/2001/XMLSchema-instance" + separador 
				                    			+ " xsi:schemaLocation=" + separador + "http://www.tirea.es/Coaseguro/MovimientosCoaseguro MovimientosCoaseguro_V05-6.xsd"
				                    		+ separador + cierre + "\n";
				                    	
				                        byte[] buf = new byte[1024];
				                        byte[] bufxml = cabeceraXml.getBytes();
				                        byte[] bufxsd = cabeceraXsd.getBytes();
				                        int len;
				                        
				                        in.read(bufEntrada1);
				                        out.write(bufxml,0,bufxml.length);
				                        in.read(bufEntrada2);
				                        out.write(bufxsd,0,bufxsd.length);
				                      
				                           
				        				while ((len = in.read(buf)) > 0) {
				        				    out.write(buf, 0, len);
				        				}
				        				
				                        in.close();
				                        out.close();
				        			} catch (IOException e) {
				                    	inci = new Incidencia();
				                    	inci.setCodigoRetorno("12");
				                    	inci.setInfAmpliada("Error al copiar el fichero " + file + " al fichero " + destination + " - " + e.getMessage());
				        			}
				                } catch (FileNotFoundException e) {
				                	inci = new Incidencia();
				                	inci.setCodigoRetorno("12");
				                	inci.setInfAmpliada("Error al crear el fichero " + destination + " - " + e.getMessage());
				                }
				            } catch (FileNotFoundException e) {
				            	inci = new Incidencia();
				            	inci.setCodigoRetorno("12");
				            	inci.setInfAmpliada("Error al cargar el fichero " + file + " - " + e.getMessage());
				            }
				        } else {
				        	inci = new Incidencia();
				        	inci.setCodigoRetorno("12");
				        	inci.setInfAmpliada("No se encuentra el fichero " + file);
				        }

					} catch (JAXBException e) {
					    e.printStackTrace();
					}	
					
					
//////////////////////PMD////////////////////////	
					List<FlujPMdCoa> listaFlujPmdCoa = flujPmdDao.getValues();
					Collections.sort(listaFlujPmdCoa, new Comparator<FlujPMdCoa>() {
						public int compare(FlujPMdCoa o1, FlujPMdCoa o2) {
							return o1.getKey().compareTo(o2.getKey());
						}
					});

					Iterator<FlujPMdCoa> itPmd = listaFlujPmdCoa.iterator();
					

					MovimientosCoaseguroPMD movimientosPmd =  new MovimientosCoaseguroPMD();

					auxCalculoFlujos = BigDecimal.ZERO;
					int indicePmd = 0;
					poliza = new Long(0);
					subpoliza = 0;
					nsuscri = 0;
					kcerti = "";
					
					Cabecera cabPMD = new Cabecera();
					
					sdfecha = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
					fechaComoCadena = sdfecha.format(new Date());
					cabPMD.setFecha(fechaComoCadena);
					cabPMD.setIdEmisor("C0511");
					fechaIdFichero = sdfAux.format(new Date());
					cabPMD.setIdFichero(fechaIdFichero);
					cabPMD.setIdReceptor("T0001");
					movimientosPmd.setCabecera(cabPMD);
					
					List<DatosPMD> listDatosPMD = new ArrayList<>();
					List<MovimientoPMD> listMoviPmd = new ArrayList<>();
					DatosPMDs datosPMDs = new DatosPMDs();
					PMD pmd = new PMD();
					MovimientoPMD movPMD = new MovimientoPMD();
					contador = 0;
					while (itPmd.hasNext()) {
						
						contador++;
							
						FlujPMdCoa flujo = itPmd.next();
						
						if (flujo.getTotfactgto().scale() < 2) {
							flujo.setTotfactgto(flujo.getTotfactgto().setScale(2));
						}
						
						if (flujo.getTotprovision().scale() < 2) {
							flujo.setTotprovision(flujo.getTotprovision().setScale(2));
						}
						
						if (flujo.getIprimanetaact().scale() < 2) {
							flujo.setIprimanetaact(flujo.getIprimanetaact().setScale(2));
						}
						
						if (flujo.getIprimanetaini().scale() < 2) {
							flujo.setIprimanetaini(flujo.getIprimanetaini().setScale(2));
						}
						
						if (flujo.getIprimatarada().scale() < 2) {
							flujo.setIprimatarada(flujo.getIprimatarada().setScale(2));
						}
							
						if (flujo.getRentini().scale() < 2) {
							flujo.setRentini(flujo.getRentini().setScale(2));
						}
						
						if (flujo.getIcapact().scale() < 2) {
							flujo.setIcapact(flujo.getIcapact().setScale(2));
						}
						
						if (flujo.getIcapini().scale() < 2) {
							flujo.setIcapini(flujo.getIcapini().setScale(2));
						}
						
						if (flujo.getIsaldo().scale() < 2) {
							flujo.setIsaldo(flujo.getIsaldo().setScale(2));
						}
						
						try {
							
							DatosCoaKey datCoa = new DatosCoaKey(flujo.getKpoliza(),flujo.getKsubpoliza()); 
							DatosCoa datoscoaseg = datosCoaDao.get(datCoa);
							gpoliza = flujo.getKpoliza();
							gsubpoliza = flujo.getKsubpoliza();
							gnsuscri = flujo.getNsuscri();
							gkcerti = flujo.getKcertificado();
							
							if (flujo.getKpoliza().equals(new Long(422851)) && flujo.getKsubpoliza().equals(4) && flujo.getKcertificado().equals(92911) && flujo.getNsuscri().equals(2577362)) {
								System.out.println(contador);
							}
							
							if (poliza.equals(new Long(0)) 
									&& subpoliza.equals(0)
									&& nsuscri.equals(0)) {
								poliza = flujo.getKpoliza();
								subpoliza = flujo.getKsubpoliza();
								nsuscri = flujo.getNsuscri();
								
								movPMD = new MovimientoPMD();
							}
							
							if (!poliza.equals(flujo.getKpoliza()) 
									|| !subpoliza.equals(flujo.getKsubpoliza())
									|| !nsuscri.equals(flujo.getNsuscri())) {
								poliza = flujo.getKpoliza();
								subpoliza = flujo.getKsubpoliza();
								nsuscri = flujo.getNsuscri();
							
								pmd.setDatosPMDs(datosPMDs);
								movPMD.setPMD(pmd);
								listMoviPmd.add(movPMD);
								
								listDatosPMD = new ArrayList<>();
								datosPMDs = new DatosPMDs();
								pmd = new PMD();
								movPMD = new MovimientoPMD();
							}
							
							movPMD.setTipoMovimiento(8);
							
							String fechaAux = fecCierre.substring(0,6);
							if(fechaAux.substring(4).equals("01") || fechaAux.substring(4).equals("03") || fechaAux.substring(4).equals("05") 
								|| fecCierre.substring(4).equals("07") || fechaAux.substring(4).equals("08") || fechaAux.substring(4).equals("10") || fechaAux.substring(4).equals("12") ){
								fechaAux = fecCierre.substring(0,4) + "-" + fecCierre.substring(4,6) + "-" + "31";
							}else if(fechaAux.substring(4).equals("02")){
								fechaAux = fecCierre.substring(0,4) + "-" + fecCierre.substring(4,6) + "-" + "28";
							}else{
								fechaAux = fecCierre.substring(0,4) + "-" + fecCierre.substring(4,6) + "-" + "30";
							}
							
							movPMD.setFecha(fechaAux);
							
							//pmd = new PMD();
							DatosPoliza datosPoliza = new DatosPoliza();
							
							datosPoliza.setEntidadAbridora(datoscoaseg.getkCoaseOri().substring(0,1) + "0" + datoscoaseg.getkCoaseOri().substring(1));
							
							if(datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase1())){
								datosPoliza.setParticipacionAbridora(datoscoaseg.getpCoase1().toString());
							}else if(datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase2())){
								datosPoliza.setParticipacionAbridora(datoscoaseg.getpCoase2().toString());
							}else if(datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase3())){
								datosPoliza.setParticipacionAbridora(datoscoaseg.getpCoase3().toString());
							}else if(datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase4())){
								datosPoliza.setParticipacionAbridora(datoscoaseg.getpCoase4().toString());
							}else if(datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase5())){
								datosPoliza.setParticipacionAbridora(datoscoaseg.getpCoase5().toString());
							}
							
							int widthPoliza = 10;
							int widthSubPoliza = 4;
							 
					        String formattedPoliza = String.format("%0" + widthPoliza + "d", Integer.valueOf(flujo.getKpoliza().toString()));
					        String formattedSubPoliza = String.format("%0" + widthSubPoliza + "d", Integer.valueOf(flujo.getKsubpoliza().toString()));
							
							datosPoliza.setNumeroPoliza(formattedPoliza + formattedSubPoliza);
							datosPoliza.setNumeroSuplemento(flujo.getNsuscri().toString());
							
							CuadroCoaseguro cuadroCoaseg = new CuadroCoaseguro();
							
							List<ParticipacionEntidad> partEntidad = new ArrayList<>();
							
							if(null != datoscoaseg.getKCoase1() && !datoscoaseg.getKCoase1().equals("")){
							
								if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase1())){
								
									ParticipacionEntidad ent1 = new ParticipacionEntidad();
									ent1.setEntidadAceptante(datoscoaseg.getKCoase1().substring(0,1) + "0" + datoscoaseg.getKCoase1().substring(1));
									ent1.setParticipacionAceptante(datoscoaseg.getpCoase1().toString());
									partEntidad.add(ent1);
								}
							}
							
							if(null != datoscoaseg.getKCoase2() && !datoscoaseg.getKCoase2().equals("")){
								
								if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase2())){
								ParticipacionEntidad ent2 = new ParticipacionEntidad();
								ent2.setEntidadAceptante(datoscoaseg.getKCoase2().substring(0,1) + "0" + datoscoaseg.getKCoase2().substring(1));
								ent2.setParticipacionAceptante(datoscoaseg.getpCoase2().toString());
								partEntidad.add(ent2);
								}
							}
							
							if(null != datoscoaseg.getKCoase3() && !datoscoaseg.getKCoase3().equals("")){
								if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase3())){
							
								ParticipacionEntidad ent3 = new ParticipacionEntidad();
								ent3.setEntidadAceptante(datoscoaseg.getKCoase3().substring(0,1) + "0" + datoscoaseg.getKCoase3().substring(1));
								ent3.setParticipacionAceptante(datoscoaseg.getpCoase3().toString());
								partEntidad.add(ent3);
								}
							}
							
							if(null != datoscoaseg.getKCoase4() && !datoscoaseg.getKCoase4().equals("")){
								if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase4())){
							
								ParticipacionEntidad ent4 = new ParticipacionEntidad();
								ent4.setEntidadAceptante(datoscoaseg.getKCoase4().substring(0,1) + "0" + datoscoaseg.getKCoase4().substring(1));
								ent4.setParticipacionAceptante(datoscoaseg.getpCoase4().toString());
								partEntidad.add(ent4);
								}
							}
							
							if(null != datoscoaseg.getKCoase5() && !datoscoaseg.getKCoase5().equals("")){
								if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase5())){
								ParticipacionEntidad ent5 = new ParticipacionEntidad();
								ent5.setEntidadAceptante(datoscoaseg.getKCoase5().substring(0,1) + "0" + datoscoaseg.getKCoase5().substring(1));
								ent5.setParticipacionAceptante(datoscoaseg.getpCoase5().toString());
								partEntidad.add(ent5);
								}
							}
							
							cuadroCoaseg.setParticipacionEntidad(partEntidad);
							datosPoliza.setCuadroCoaseguro(cuadroCoaseg);
							
							SimpleDateFormat sdfechaEfecto = new SimpleDateFormat("yyyy-MM-dd");
							String fechaEfecto = sdfechaEfecto.format(flujo.getFecefecini());
							datosPoliza.setFechaEfecto(fechaEfecto);
							
							
							datosPoliza.setMetodoSuplemento("INCREMENTAL");
							pmd.setDatosPoliza(datosPoliza);
							
							DatosPMD datosPMD = new DatosPMD();
							int widthCertificado = 6;
							 
					        String formattedCertificado = String.format("%0" + widthCertificado + "d", Integer.valueOf(flujo.getKcertificado().toString()));
							String idAplicacion = formattedPoliza + formattedSubPoliza + formattedCertificado;
					        String fechaNacimiento = sdfechaEfecto.format(flujo.getFnacAseg1());
							String sexo = flujo.getCsexAseg1();
							if (sexo.equals("H")) {
								sexo = "M";
							} else {
								sexo = "F";
							}

							String estado = flujo.getCestadoAseg1();
							if (estado.equals("V")) {
								estado = "VIGOR";
							} else if (estado.equals("A")) {
								estado = "FALLECIDO";
							} else if (estado.equals("S")) {
								estado = "SUSPENSO";
							} else if (estado.equals("M")) {
								estado = "MINUSVALIDO";
							} else {
								estado = "";
							}
							
							datosPMD.setIdAplicacion(idAplicacion);
							datosPMD.setIdAplicacionOrigen(idAplicacion);
							datosPMD.setFechaNacimiento(fechaNacimiento);
							datosPMD.setSexo(sexo);
							datosPMD.setEstado(estado);
							
							
							
							Tablas tablas = new Tablas();
							List<Tabla> tab = new ArrayList<>();
							Tabla t = new Tabla();
							Tab35012Key t35012 = new Tab35012Key(StringUtils.leftPad(flujo.getTablacalc1aseg1(),5,"0")); 
							Tab35012 tab35 = tab35012Dao.get(t35012);
							t.setCodigoTabla(tab35.getTablaTirea().replace(" ", ""));
							if(t.getCodigoTabla().equalsIgnoreCase("GRM80") 
									&& StringUtils.leftPad(flujo.getTablacalc1aseg1(),5,"0").equalsIgnoreCase("00705")){
								t.setDesplazamiento("-2");
							}else if(t.getCodigoTabla().equalsIgnoreCase("GRM95")
									&& (StringUtils.leftPad(flujo.getTablacalc1aseg1(),5,"0").equalsIgnoreCase("00365")
											|| StringUtils.leftPad(flujo.getTablacalc1aseg1(),5,"0").equalsIgnoreCase("00721"))){
								t.setDesplazamiento("-1");
							}else{
								t.setDesplazamiento("0");
							}
							t.setPjeTabla(tab35.getPorcentajeTabla().toString().concat(".00"));
							tab.add(t);
							tablas.setTabla(tab);
							datosPMD.setTablas(tablas);
							
							TiposInteres tiposInteres = new TiposInteres();
							List<Interes> listInt = new ArrayList<>();
							Interes inter = new Interes();
							inter.setTipoInteres(flujo.getPintertecnI1().toString());
							String fechaInteresIni = sdfechaInteres.format(flujo.getFecIniTramo1());
							inter.setFechaInicio(fechaInteresIni);
							String fechaInteresFin = sdfechaInteres.format(flujo.getFecFinTramo1());
							inter.setFechaFin(fechaInteresFin);
							listInt.add(inter);
							if(!flujo.getPintertecnI2().equals(BigDecimal.ZERO)){
								Interes inter2 = new Interes();
								inter2.setTipoInteres(flujo.getPintertecnI2().toString());
								String fechaInteresIni2 = sdfechaInteres.format(flujo.getFecIniTramo2());
								inter2.setFechaInicio(fechaInteresIni2);
								String fechaInteresFin2 = sdfechaInteres.format(flujo.getFecFinTramo2());
								inter2.setFechaFin(fechaInteresFin2);
								listInt.add(inter2);
							}
							tiposInteres.setInteres(listInt);
							
							datosPMD.setTiposInteres(tiposInteres);
							if (flujo.getPgastgesin1I().equals(BigDecimal.ZERO)
									|| flujo.getPgastgesin2I().equals(BigDecimal.ZERO)
									|| flujo.getPgastgesex1I().equals(BigDecimal.ZERO)
									|| flujo.getPgastgesex2I().equals(BigDecimal.ZERO)) {
								
								Gastos gastos = new Gastos();
								List<Gasto> listGasto = new ArrayList<>();
								
								if(!flujo.getPgastgesin1I().equals(BigDecimal.ZERO)) {
									Gasto g = new Gasto();
									g.setConcepto("GGI");
									g.setBase("CAPITAL");
									if (flujo.getPgastgesin1I().toString().contains(".")) {
										g.setPorcentaje(flujo.getPgastgesin1I().toString());
									} else {
										g.setPorcentaje(flujo.getPgastgesin1I().toString().concat(".00"));
									}
									g.setImporte(flujo.getTotfactgto().toString());
									listGasto.add(g);
								}
								if(!flujo.getPgastgesin2I().equals(BigDecimal.ZERO)) {
									Gasto g = new Gasto();
									g.setConcepto("GGI");
									g.setBase("PRIMA");
									if (flujo.getPgastgesin2I().toString().contains(".")) {
										g.setPorcentaje(flujo.getPgastgesin2I().toString());
									} else {
										g.setPorcentaje(flujo.getPgastgesin2I().toString().concat(".00"));
									}
									g.setImporte(flujo.getTotfactgto().toString());
									listGasto.add(g);
								}
								if(!flujo.getPgastgesex1I().equals(BigDecimal.ZERO)) {
									Gasto g = new Gasto();
									g.setConcepto("GGE");
									g.setBase("CAPITAL");
									if (flujo.getPgastgesex1I().toString().contains(".")) {
										g.setPorcentaje(flujo.getPgastgesex1I().toString());
									} else {
										g.setPorcentaje(flujo.getPgastgesex1I().toString().concat(".00"));
									}
									g.setImporte(flujo.getTotfactgto().toString());
									listGasto.add(g);
								}
								if(!flujo.getPgastgesex2I().equals(BigDecimal.ZERO)) {
									Gasto g = new Gasto();
									g.setConcepto("GGE");
									g.setBase("PRIMA");
									if (flujo.getPgastgesex2I().toString().contains(".")) {
										g.setPorcentaje(flujo.getPgastgesex2I().toString());
									} else {
										g.setPorcentaje(flujo.getPgastgesex2I().toString().concat(".00"));
									}
									g.setImporte(flujo.getTotfactgto().toString());
									listGasto.add(g);
								}
								
								if (flujo.getPgastgesin1I().equals(BigDecimal.ZERO)
										&& flujo.getPgastgesin2I().equals(BigDecimal.ZERO)
										&& flujo.getPgastgesex1I().equals(BigDecimal.ZERO)
										&& flujo.getPgastgesex2I().equals(BigDecimal.ZERO)) {
									Gasto g = new Gasto();
									g.setConcepto("GGI");
									g.setBase("PRIMA");
									g.setPorcentaje("0.00");
									g.setImporte("0.00");
									
									listGasto.add(g);
								}
								gastos.setGasto(listGasto);
								datosPMD.setGastos(gastos);
							}
							
							
							GarantiasPrestaciones garantiasPrestaciones = new GarantiasPrestaciones();
							
							GarantiaPrestacion garantiaPrestacion = new GarantiaPrestacion();
							
							if (flujo.getKmodalidad().equals(317) && flujo.getKsubpoliza().equals(4)) {
								System.out.println();
							}
							
							Tab35015Key t35015 = new Tab35015Key(flujo.getKmodalidad(), flujo.getKgarantia(), flujo.getKprestacion()); 
							Tab35015 tab35015 = tab35015Dao.get(t35015);
							if (tab35015 == null) {
								t35015 = new Tab35015Key(flujo.getKmodalidad(), flujo.getKgarantia(), "");
								tab35015 = tab35015Dao.get(t35015);
								if (tab35015 == null) {
									t35015 = new Tab35015Key(flujo.getKmodalidad(), 0, "");
									tab35015 = tab35015Dao.get(t35015);
									if (tab35015 == null) {
										t35015 = new Tab35015Key(0, flujo.getKgarantia(), flujo.getKprestacion());
										tab35015 = tab35015Dao.get(t35015);
										if (tab35015 == null) {
											t35015 = new Tab35015Key(0, flujo.getKgarantia(), "");
											tab35015 = tab35015Dao.get(t35015);
											if (tab35015 == null) {
												t35015 = new Tab35015Key(0, 0, flujo.getKprestacion());
												tab35015 = tab35015Dao.get(t35015);
												if (tab35015 == null) {
													t35015 = new Tab35015Key(0, 0, "");
													tab35015 = tab35015Dao.get(t35015);
												}
											}
										}
									}
								}
							}
							if (tab35015 == null) {
								if (flujo.getTempVit().equals(ConstantesSolvencia.L)) {
									garantiaPrestacion.setIdGarantia(ConstantesSolvencia.RENTA_L.replace(" ", ""));
									garantiaPrestacion.setIdGarantiaPrincipal(ConstantesSolvencia.RENTA_L.replace(" ", ""));
									garantiaPrestacion.setCodGarantia(ConstantesSolvencia.L.replace(" ", ""));
								} else if (flujo.getTempVit().equals(ConstantesSolvencia.T)) {
									garantiaPrestacion.setIdGarantia(ConstantesSolvencia.RENTA_T.replace(" ", ""));
									garantiaPrestacion.setIdGarantiaPrincipal(ConstantesSolvencia.RENTA_T.replace(" ", ""));
									garantiaPrestacion.setCodGarantia(ConstantesSolvencia.T.replace(" ", ""));
								} else if (flujo.getTempVit().equals(ConstantesSolvencia.V)) {
									garantiaPrestacion.setIdGarantia(ConstantesSolvencia.RENTA_V.replace(" ", ""));
									garantiaPrestacion.setIdGarantiaPrincipal(ConstantesSolvencia.RENTA_V.replace(" ", ""));
									garantiaPrestacion.setCodGarantia(ConstantesSolvencia.V.replace(" ", ""));
								} else {
									garantiaPrestacion.setIdGarantia("");
									garantiaPrestacion.setIdGarantiaPrincipal("");
									garantiaPrestacion.setCodGarantia("");
								}
							} else {
								garantiaPrestacion.setIdGarantia(tab35015.getDescripcionGarantiaTirea().replace(" ", ""));
								garantiaPrestacion.setIdGarantiaPrincipal(tab35015.getDescripcionGarantiaTirea().replace(" ", ""));
								garantiaPrestacion.setCodGarantia(tab35015.getGarantiaTirea().replace(" ", ""));
							}
							

							if (garantiaPrestacion.getCodGarantia().replace(" ", "").contains("RENTA-V") && flujo.getFecIni() != null){
								String fechaIni = sdfechaInteres.format(flujo.getFecIni());
								String fechaFin = sdfechaInteres.format(flujo.getFecFin());
								garantiaPrestacion.setFechaInicio(fechaIni);
								garantiaPrestacion.setFechaFin(fechaFin);
							} else {
								String fechaIni = sdfechaInteres.format(flujo.getFecinisus());
								String fechaFin = sdfechaInteres.format(flujo.getFecFin());
								garantiaPrestacion.setFechaInicio(fechaIni);
								garantiaPrestacion.setFechaFin(fechaFin);
							}
							
							if(null == flujo.getIprimatarada()){
								garantiaPrestacion.setPrimaTotal("0.00");
							}else{
								garantiaPrestacion.setPrimaTotal(flujo.getIprimatarada().toString());
							}
							
							if(null == flujo.getIprimanetaact()){
								garantiaPrestacion.setPrimaNeta("0.00");
							}else{
								garantiaPrestacion.setPrimaNeta(flujo.getIprimanetaact().toString());
							}
							
							if(null == flujo.getRentini()){
								garantiaPrestacion.setCapital("0.00");
							}else{
								garantiaPrestacion.setCapital(flujo.getRentini().toString());
							}
							
							if(null == flujo.getPsobremort()){
								garantiaPrestacion.setSobreMortalidad("0.00");
							}else{
								garantiaPrestacion.setSobreMortalidad(flujo.getPsobremort().toString());
							}
							
							if(null == flujo.getPriesgo()){
								garantiaPrestacion.setSobreRiesgo("0.00");
							}else{
								garantiaPrestacion.setSobreRiesgo(flujo.getPriesgo().toString());
							}
							
							if(null == flujo.getPsobremort()){
								garantiaPrestacion.setExtraPrima("0.00");
							}else{
								garantiaPrestacion.setExtraPrima(flujo.getPsobremort().toString());
							}
							
							
							Tab35014Key t35014 = new Tab35014Key(flujo.getKgarantia(), flujo.getKprestacion()); 
							Tab35014 tab35014 = tab35014Dao.get(t35014);
							if (tab35014 == null) {
								t35014 = new Tab35014Key(flujo.getKgarantia(), ""); 
								tab35014 = tab35014Dao.get(t35014);
							}
							garantiaPrestacion.setTipoPrestacion(tab35014.getTipoPrestacion());
							garantiaPrestacion.setFormaPago(tab35014.getPagoPrestacion());
							
							if (null != flujo.getNpergaran() &&  flujo.getFecIni() != null) {
								String fechaIniRenta = sdfechaInteres.format(flujo.getFecIni());
								String fechaFinRenta = sdfechaInteres.format(flujo.getFecFin());
								garantiaPrestacion.setFechaInicioRentaCierta(fechaIniRenta);
								garantiaPrestacion.setFechaFinRentaCierta(fechaFinRenta);
							}
							
							if (null == flujo.getRentini() && tab35015.getDescripcionGarantiaTirea().replace(" ", "").contains("RENTA")) {
								garantiaPrestacion.setTipoRenta("IRREGULAR");
							} else {
								garantiaPrestacion.setTipoRenta("REGULAR");
							}
							
							Tab35013Key t35013 = new Tab35013Key(flujo.getKpoliza(), flujo.getKsubpoliza(), flujo.getKmodalidad()); 
							Tab35013 tab35013 = tab35013Dao.get(t35013);
							if (tab35013 == null) {
								garantiaPrestacion.setCriterioEdad("REAL");
							} else {
								garantiaPrestacion.setCriterioEdad(tab35013.getCriterio());
							}
							
							
							if (null != flujo.getGedadmax()) {
								garantiaPrestacion.setEdadMaxGarantia(flujo.getGedadmax().toString());
							} else {
								garantiaPrestacion.setEdadMaxGarantia("0");
							}
							
							// Datos Crecimiento
							DatosCrecimientos datosCrecimientos = new DatosCrecimientos();
							if (flujo.getPrevprima().equals(BigDecimal.ZERO)) {
								garantiaPrestacion.setCrecimiento("No");
							} else {
								garantiaPrestacion.setCrecimiento("Si");
								List<Crecimiento> listCrecimiento = new ArrayList<>();
								Crecimiento crecimiento = new Crecimiento();
								crecimiento.setTipo(flujo.getCformarevprim());
								String fechaIniPrim = sdfechaInteres.format(flujo.getFecinipagprim());
								String fechaFinPrim = sdfechaInteres.format(flujo.getFecfinpagprim());
								crecimiento.setFechaInicio(fechaIniPrim);
								crecimiento.setFechaFin(fechaFinPrim);
								crecimiento.setPorcentaje(flujo.getPrevprima().toString());
								switch (flujo.getCformpago()){
									case "12": 
										crecimiento.setPeriodo("MENSUAL");
										break;
									case "4":
										crecimiento.setPeriodo("TRIMESTRAL");
										break;
									case "3": 
										crecimiento.setPeriodo("CUATRIMES");
										break;
									case "2": 
										crecimiento.setPeriodo("SEMESTRAL");
										break;
									case "1": 
										crecimiento.setPeriodo("ANUAL");
										break;
									case "9":
										crecimiento.setPeriodo("UNICA");
										break;
								}
								listCrecimiento.add(crecimiento);
								datosCrecimientos.setCrecimiento(listCrecimiento);
								garantiaPrestacion.setDatosCrecimientos(datosCrecimientos);
							}
							
							// Datos Revalorizacion
							DatosRevalorizaciones datosRevalorizaciones = new DatosRevalorizaciones();
							if (flujo.getPrevrenta().equals(BigDecimal.ZERO)) {
								garantiaPrestacion.setRevalorizacion("No");
							} else {
								garantiaPrestacion.setRevalorizacion("Si");
								List<Revalorizacion> listRevalorizacion = new ArrayList<>();
								Revalorizacion revalorizacion = new Revalorizacion();
								revalorizacion.setTipo(flujo.getCformarevprim());
								String fechaIni = sdfechaInteres.format(flujo.getFecIni());
								String fechaFin = sdfechaInteres.format(flujo.getFecFin());
								revalorizacion.setFechaInicio(fechaIni);
								revalorizacion.setFechaFin(fechaFin);
								revalorizacion.setPorcentaje(flujo.getPrevrenta().toString());
								revalorizacion.setPeriodo(tab35013.getPeriodicidadRenta());
								
								listRevalorizacion.add(revalorizacion);
								datosRevalorizaciones.setRevalorizacion(listRevalorizacion);
								garantiaPrestacion.setDatosRevalorizaciones(datosRevalorizaciones);
							}
							
							// Datos Diferimiento
							DatosDiferimientos datosDiferimientos = new DatosDiferimientos();
							if (tab35013 == null || tab35013.getTipoReversion().isEmpty()) {
								garantiaPrestacion.setDiferimiento("No");
							} else {
								garantiaPrestacion.setDiferimiento("Si");
								List<Diferimiento> listDiferimiento = new ArrayList<>();
								Diferimiento diferimiento = new Diferimiento();
								if (tab35013 != null && !tab35013.getDiferimiento().isEmpty()) {
									diferimiento.setTipo(tab35013.getDiferimiento());
								} else {
									diferimiento.setTipo("ACTUARIAL");
								}
								
								if (flujo.getFecIni() != null) {
									String fechaIni = sdfechaInteres.format(flujo.getFecIni());
									String fechaFin = sdfechaInteres.format(flujo.getFecFin());
									diferimiento.setFechaInicio(fechaIni);
									diferimiento.setFechaFin(fechaFin);
								} else {
									String fechaIni = sdfechaInteres.format(flujo.getFecinisus());
									String fechaFin = sdfechaInteres.format(flujo.getFecFin());
									diferimiento.setFechaInicio(fechaIni);
									diferimiento.setFechaFin(fechaFin);
								}
								
								
								listDiferimiento.add(diferimiento);
								datosDiferimientos.setDiferimiento(listDiferimiento);
								garantiaPrestacion.setDatosRevalorizaciones(datosRevalorizaciones);
							}
							
							
							
							garantiaPrestacion.setMesesPagaExtra("0");
							
							switch (flujo.getForpagrent()){
								case 12: 
									garantiaPrestacion.setPeriodicidadRentaDevengo("MENSUAL");
									break;
								case 4:
									garantiaPrestacion.setPeriodicidadRentaDevengo("TRIMESTRAL");
									break;
								case 3: 
									garantiaPrestacion.setPeriodicidadRentaDevengo("CUATRIMES");
									break;
								case 2: 
									garantiaPrestacion.setPeriodicidadRentaDevengo("SEMESTRAL");
									break;
								case 1: 
									garantiaPrestacion.setPeriodicidadRentaDevengo("ANUAL");
									break;
								case 9:
									garantiaPrestacion.setPeriodicidadRentaDevengo("UNICA");
									break;
							}
							
							String diafechaIni = "";
							if (flujo.getFecIni() != null) {
								diafechaIni = sdfechaInteres.format(flujo.getFecIni()).substring(8);
							} else {
								diafechaIni = sdfechaInteres.format(flujo.getFecinisus()).substring(8);
							}
							
							if (diafechaIni.equals("01")) {
								garantiaPrestacion.setTipoPagoRenta("PREPAG");
							} else {
								garantiaPrestacion.setTipoPagoRenta("POSTPAG");
							}
							
							String fechaAuxPMD = fecCierre.substring(0,6);
							if(fechaAuxPMD.substring(4).equals("01") || fechaAuxPMD.substring(4).equals("03") || fechaAuxPMD.substring(4).equals("05") 
								|| fechaAuxPMD.substring(4).equals("07") || fechaAuxPMD.substring(4).equals("08") || fechaAuxPMD.substring(4).equals("10") || fechaAuxPMD.substring(4).equals("12") ){
								if (diafechaIni.equals("31")) {
									garantiaPrestacion.setDiaPago("99");
								} else if(diafechaIni.equals("15")) {
									garantiaPrestacion.setDiaPago("50");
								} else {
									garantiaPrestacion.setDiaPago(diafechaIni);
								}
							} else if(fechaAuxPMD.substring(4).equals("02")){
								if (diafechaIni.equals("28")) {
									garantiaPrestacion.setDiaPago("99");
								} else if(diafechaIni.equals("15")) {
									garantiaPrestacion.setDiaPago("50");
								} else {
									garantiaPrestacion.setDiaPago(diafechaIni);
								}
							} else{
								if (diafechaIni.equals("30")) {
									garantiaPrestacion.setDiaPago("99");
								} else if(diafechaIni.equals("15")) {
									garantiaPrestacion.setDiaPago("50");
								} else {
									garantiaPrestacion.setDiaPago(diafechaIni);
								}
							}
							
							garantiaPrestacion.setProrrata("No");
							if (flujo.getPb().equals("S") && flujo.getTipopb().equals("F")) {
								if(null == flujo.getTotprovision()){
									garantiaPrestacion.setPbFinanciera("0.00");
								}else{
									garantiaPrestacion.setPbFinanciera(flujo.getTotprovision().toString());
								}
							} else {
								garantiaPrestacion.setPbFinanciera("0.00");
							}
							
							if(null == flujo.getTotprovision()){
								garantiaPrestacion.setImporteProvision("0.00");
							}else{
								garantiaPrestacion.setImporteProvision(flujo.getTotprovision().toString());
							}
							
							// Datos Reversion
							DatosReversion datosReversion = new DatosReversion();
							if (null == flujo.getPreversion()
									|| flujo.getPreversion().equals(BigDecimal.ZERO)) {
								garantiaPrestacion.setReversion("No");
							} else {
								garantiaPrestacion.setReversion("Si");
								List<Reversion> listReversion = new ArrayList<>();
								Reversion reversion = new Reversion();
								reversion.setTipo(tab35013.getTipoReversion());
								String fechaIni = sdfechaInteres.format(flujo.getFecIni());
								String fechaFin = sdfechaInteres.format(flujo.getFecFin());
								reversion.setFechaInicio(fechaIni);
								reversion.setFechaFin(fechaFin);
								reversion.setPorcentaje(flujo.getPreversion().toString());
								reversion.setImporte(flujo.getRentini().toString());
								
								Beneficiarios beneficiarios = new Beneficiarios();
								List<Beneficiario> listBeneficiario = new ArrayList<>();
								Beneficiario beneficiario = new Beneficiario();
								
								if (null != flujo.getCestadoAseg2()) {
									String fechaAseg2 = sdfechaInteres.format(flujo.getFnacAseg2());
									beneficiario.setFechaNacimiento(fechaAseg2);
									if (flujo.getCsexAseg2().equals("H")) {
										beneficiario.setSexo("M");
									} else {
										beneficiario.setSexo("F");
									}
									
									if (flujo.getCestadoAseg2().equals("V")) {
										beneficiario.setEstado("VIGOR");
									} else if (flujo.getCestadoAseg2().equals("A")) {
										beneficiario.setEstado("FALLECIDO");
									} else if (flujo.getCestadoAseg2().equals("M")) {
										beneficiario.setEstado("MINUSVALIDO");
									} else {
										beneficiario.setEstado("SUSPENSO");
									}
									beneficiario.setPorcentaje(flujo.getPreversion().toString());
									beneficiario.setImporte(flujo.getRentini().toString());
									beneficiario.setParentesco("CONYUGE");
									beneficiario.setOrfandad(tab35013.getOrfandad());
									String fechaFinAseg = sdfechaInteres.format(flujo.getFecFin());
									beneficiario.setLimiteOrfandad(fechaFinAseg);
									listBeneficiario.add(beneficiario);
								}
								
								if (null != flujo.getCestadoAseg3()) {
									String fechaAseg3 = sdfechaInteres.format(flujo.getFnacAseg3());
									beneficiario.setFechaNacimiento(fechaAseg3);
									if (flujo.getCsexAseg3().equals("H")) {
										beneficiario.setSexo("M");
									} else {
										beneficiario.setSexo("F");
									}
									if (flujo.getCestadoAseg3().equals("V")) {
										beneficiario.setEstado("VIGOR");
									} else if (flujo.getCestadoAseg3().equals("A")) {
										beneficiario.setEstado("FALLECIDO");
									} else if (flujo.getCestadoAseg3().equals("M")) {
										beneficiario.setEstado("MINUSVALIDO");
									} else {
										beneficiario.setEstado("SUSPENSO");
									}
									beneficiario.setPorcentaje(flujo.getPreversion().toString());
									beneficiario.setImporte(flujo.getRentini().toString());
									beneficiario.setParentesco("HIJO");
									beneficiario.setOrfandad(tab35013.getOrfandad());
									String fechaFinAseg = sdfechaInteres.format(flujo.getFecFin());
									beneficiario.setLimiteOrfandad(fechaFinAseg);
									listBeneficiario.add(beneficiario);
								}
								
								if (null != flujo.getCestadoAseg4()) {
									String fechaAseg4 = sdfechaInteres.format(flujo.getFnacAseg4());
									beneficiario.setFechaNacimiento(fechaAseg4);
									if (flujo.getCsexAseg4().equals("H")) {
										beneficiario.setSexo("M");
									} else {
										beneficiario.setSexo("F");
									}
									if (flujo.getCestadoAseg4().equals("V")) {
										beneficiario.setEstado("VIGOR");
									} else if (flujo.getCestadoAseg4().equals("A")) {
										beneficiario.setEstado("FALLECIDO");
									} else if (flujo.getCestadoAseg4().equals("M")) {
										beneficiario.setEstado("MINUSVALIDO");
									} else {
										beneficiario.setEstado("SUSPENSO");
									}
									beneficiario.setPorcentaje(flujo.getPreversion().toString());
									beneficiario.setImporte(flujo.getRentini().toString());
									beneficiario.setParentesco("HIJO");
									beneficiario.setOrfandad(tab35013.getOrfandad());
									String fechaFinAseg = sdfechaInteres.format(flujo.getFecFin());
									beneficiario.setLimiteOrfandad(fechaFinAseg);
									listBeneficiario.add(beneficiario);
								}
								
								if (null != flujo.getCestadoAseg5()) {
									String fechaAseg5 = sdfechaInteres.format(flujo.getFnacAseg5());
									beneficiario.setFechaNacimiento(fechaAseg5);
									if (flujo.getCsexAseg5().equals("H")) {
										beneficiario.setSexo("M");
									} else {
										beneficiario.setSexo("F");
									}
									if (flujo.getCestadoAseg5().equals("V")) {
										beneficiario.setEstado("VIGOR");
									} else if (flujo.getCestadoAseg5().equals("A")) {
										beneficiario.setEstado("FALLECIDO");
									} else if (flujo.getCestadoAseg5().equals("M")) {
										beneficiario.setEstado("MINUSVALIDO");
									} else {
										beneficiario.setEstado("SUSPENSO");
									}
									beneficiario.setPorcentaje(flujo.getPreversion().toString());
									beneficiario.setImporte(flujo.getRentini().toString());
									beneficiario.setParentesco("HIJO");
									beneficiario.setOrfandad(tab35013.getOrfandad());
									String fechaFinAseg = sdfechaInteres.format(flujo.getFecFin());
									beneficiario.setLimiteOrfandad(fechaFinAseg);
									listBeneficiario.add(beneficiario);
								}
								
								beneficiarios.setBeneficiario(listBeneficiario); 
								reversion.setBeneficiarios(beneficiarios);
								datosReversion.setReversion(reversion);
								garantiaPrestacion.setDatosReversion(datosReversion);
							}
							
							garantiasPrestaciones.setGarantiaPrestacion(garantiaPrestacion);
							datosPMD.setGarantiasPrestaciones(garantiasPrestaciones);
							listDatosPMD.add(datosPMD);	
							datosPMDs.setDatosPMD(listDatosPMD);
							
							
//							pmd.setDatosPMDs(datosPMDs);
//							movPMD.setPMD(pmd);
//							listMoviPmd.add(movPMD);
							
						} catch (Exception e) {
							System.out.println(gpoliza + "; " + gsubpoliza + "; " + gkcerti + "; " + gnsuscri);
							Incidencia inci = new Incidencia();
							inci.setFecCierre(fecCierre);
							inci.setCodigoRetorno("11");
							inci.setInfAmpliada("Error" + mensaje + " - " + e.getMessage());
							//gestorIncidenciasPres.write(inci);
							inci.setCcanal("01");
							giGeneral.write(inci);
						}
						
						
						iteracion++;
					
					
					}
					
					pmd.setDatosPMDs(datosPMDs);
					movPMD.setPMD(pmd);
					listMoviPmd.add(movPMD);
					movimientosPmd.setMovimientopmd(listMoviPmd);			
					
					
					try {
					    JAXBContext jaxbContext = JAXBContext.newInstance(MovimientosCoaseguroPMD.class);
					    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
					    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

					    File file = new File("XML-PMD.xml");
					    jaxbMarshaller.marshal(movimientosPmd, file);
					    
					    Incidencia inci = null;
						InputStream in = null;
						OutputStream out = null;
						BtUtils btUtils = new BtUtils();
						
					    File destination = new File(btUtils.getRutaFicherosProperty(ConstantesSolvencia.RUTA_EXPORTA_XML) + fecCierre.substring(0,6) + File.separator + "TIREA" + File.separator + "XML-PMD.XML");
						if (file.exists()) {
				            try {
				                in = new FileInputStream(file);
				                try {
				                    out = new FileOutputStream(destination);
				                    try {
				                    	String cierre = ">";
				                    	char separador = '"';
				                    	String apertura = "<";
				                    	
				                    	String cabeceraEntrada1 = "<?xml version=" + separador + "1.0" + separador + "encoding=" + separador + "UTF-8" + separador + "standalone=" + separador + "yes" + separador + "?>" + "/n";
				                    	String cabeceraEntrada2 = "<MovimientosCoaseguro>" + "/n";
				                    	
				                    	byte[] bufEntrada1 = cabeceraEntrada1.getBytes();
				                        byte[] bufEntrada2 = cabeceraEntrada2.getBytes();
				             
				                    	String cabeceraXml = "<?xml version=" + separador + "1.0" + separador + " encoding=" + separador + "UTF-8" + separador + "?>" + "\n";
				                    	String cabeceraXsd = "<MovimientosCoaseguro xmlns=" + separador + "http://www.tirea.es/Coaseguro/MovimientosCoaseguro" + separador +  
				                    		" xmlns:xsi=" + separador + "http://www.w3.org/2001/XMLSchema-instance" + separador 
				                    			+ " xsi:schemaLocation=" + separador + "http://www.tirea.es/Coaseguro/MovimientosCoaseguro MovimientosCoaseguro_V05-6.xsd"
				                    		+ separador + cierre + "\n";
				                    	
				                        byte[] buf = new byte[1024];
				                        byte[] bufxml = cabeceraXml.getBytes();
				                        byte[] bufxsd = cabeceraXsd.getBytes();
				                        int len;
				                        
				                        in.read(bufEntrada1);
				                        out.write(bufxml,0,bufxml.length);
				                        in.read(bufEntrada2);
				                        out.write(bufxsd,0,bufxsd.length);
				                      
				                           
				        				while ((len = in.read(buf)) > 0) {
				        				    out.write(buf, 0, len);
				        				}
				        				
				                        in.close();
				                        out.close();
				        			} catch (IOException e) {
				                    	inci = new Incidencia();
				                    	inci.setCodigoRetorno("12");
				                    	inci.setInfAmpliada("Error al copiar el fichero " + file + " al fichero " + destination + " - " + e.getMessage());
				                    	inci.setFecCierre(gfecCierre);
				                    	inci.setCcanal("01");
				        			}
				                } catch (FileNotFoundException e) {
				                	inci = new Incidencia();
				                	inci.setCodigoRetorno("12");
				                	inci.setInfAmpliada("Error al crear el fichero " + destination + " - " + e.getMessage());
				                	inci.setFecCierre(gfecCierre);
			                    	inci.setCcanal("01");
				                }
				            } catch (FileNotFoundException e) {
				            	inci = new Incidencia();
				            	inci.setCodigoRetorno("12");
				            	inci.setInfAmpliada("Error al cargar el fichero " + file + " - " + e.getMessage());
				            	inci.setFecCierre(gfecCierre);
		                    	inci.setCcanal("01");
				            }
				        } else {
				        	inci = new Incidencia();
				        	inci.setCodigoRetorno("12");
				        	inci.setInfAmpliada("No se encuentra el fichero " + file);
				        	inci.setFecCierre(gfecCierre);
	                    	inci.setCcanal("01");
				        }

					} catch (JAXBException e) {
					    e.printStackTrace();
					}	
					
					
					
					
					//////////////////////RENTAS IRREGULARES////////////////////////	
					List<FlujPMdCoa> listaFlujPmdCoaRI = flujPmdDao.getValues();
					Collections.sort(listaFlujPmdCoaRI, new Comparator<FlujPMdCoa>() {
						public int compare(FlujPMdCoa o1, FlujPMdCoa o2) {
							return o1.getKey().compareTo(o2.getKey());
						}
					});
					
					Iterator<FlujPMdCoa> itPmdRI = listaFlujPmdCoaRI.iterator();

					
					MovimientosCoaseguroRentasIrregulares movimientosRentasIrregulares =  new MovimientosCoaseguroRentasIrregulares();

					auxCalculoFlujos = BigDecimal.ZERO;
					int indicePmdRI = 0;
					poliza = new Long(0);
					subpoliza = 0;
					nsuscri = 0;
					
					Cabecera cabRentasIrregulares = new Cabecera();
					
					sdfecha = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
					fechaComoCadena = sdfecha.format(new Date());
					cabRentasIrregulares.setFecha(fechaComoCadena);
					cabRentasIrregulares.setIdEmisor("C0511");
					fechaIdFichero = sdfAux.format(new Date());
					cabRentasIrregulares.setIdFichero(fechaIdFichero);
					cabRentasIrregulares.setIdReceptor("T0001");
					movimientosRentasIrregulares.setCabecera(cabRentasIrregulares);
					
					List<PagoPlanificado> listPagoPlanificado = new ArrayList<>();
					List<MovimientoRentasIrregulares> listMoviRentasIrregulares = new ArrayList<>();
					PagosPlanificados pagosPlanificados = new PagosPlanificados();
					RentaIrregular rentaIrregular = new RentaIrregular();
					List<RentaIrregular> listRentaIrregular = new ArrayList<>();
					MovimientoRentasIrregulares movRentasIrregulares = new MovimientoRentasIrregulares();
					contador = 0;

					while (itPmdRI.hasNext()) {
						
						contador++;
							
						FlujPMdCoa flujo = itPmdRI.next();
							
						try {
							
							if ((flujo.getRentini() == null || flujo.getRentini().toString().equals("0.00") || flujo.getRentini().toString().equals("0")) && !flujo.getTempVit().equals("V")) {
								
								DatosCoaKey datCoa = new DatosCoaKey(flujo.getKpoliza(),flujo.getKsubpoliza()); 
								DatosCoa datoscoaseg = datosCoaDao.get(datCoa);
								
								if (poliza.equals(new Long(0)) 
										&& subpoliza.equals(0)
										&& nsuscri.equals(0)) {
									poliza = flujo.getKpoliza();
									subpoliza = flujo.getKsubpoliza();
									nsuscri = flujo.getNsuscri();
									
									movRentasIrregulares = new MovimientoRentasIrregulares();
								}
								
								if (!poliza.equals(flujo.getKpoliza()) 
										|| !subpoliza.equals(flujo.getKsubpoliza())
										|| !nsuscri.equals(flujo.getNsuscri())) {
									poliza = flujo.getKpoliza();
									subpoliza = flujo.getKsubpoliza();
									nsuscri = flujo.getNsuscri();
									
									rentaIrregular.setPagosPlanificados(pagosPlanificados);
									movRentasIrregulares.setRentaIrregular(rentaIrregular);
									listMoviRentasIrregulares.add(movRentasIrregulares);
									
									listPagoPlanificado = new ArrayList<>();
									pagosPlanificados = new PagosPlanificados();
									rentaIrregular = new RentaIrregular();
									movRentasIrregulares = new MovimientoRentasIrregulares();
								}
								
								
								
								
								movRentasIrregulares.setTipoMovimiento(9);
								
								String fechaAux = fecCierre.substring(0,6);
								if(fechaAux.substring(4).equals("01") || fechaAux.substring(4).equals("03") || fechaAux.substring(4).equals("05") 
									|| fecCierre.substring(4).equals("07") || fechaAux.substring(4).equals("08") || fechaAux.substring(4).equals("10") || fechaAux.substring(4).equals("12") ){
									fechaAux = fecCierre.substring(0,4) + "-" + fecCierre.substring(4,6) + "-" + "31";
								}else if(fechaAux.substring(4).equals("02")){
									fechaAux = fecCierre.substring(0,4) + "-" + fecCierre.substring(4,6) + "-" + "28";
								}else{
									fechaAux = fecCierre.substring(0,4) + "-" + fecCierre.substring(4,6) + "-" + "30";
								}
								
								movRentasIrregulares.setFecha(fechaAux);
								
								rentaIrregular = new RentaIrregular();
								DatosPoliza datosPoliza = new DatosPoliza();
								
								datosPoliza.setEntidadAbridora(datoscoaseg.getkCoaseOri().substring(0,1) + "0" + datoscoaseg.getkCoaseOri().substring(1));
								
								if(datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase1())){
									datosPoliza.setParticipacionAbridora(datoscoaseg.getpCoase1().toString());
								}else if(datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase2())){
									datosPoliza.setParticipacionAbridora(datoscoaseg.getpCoase2().toString());
								}else if(datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase3())){
									datosPoliza.setParticipacionAbridora(datoscoaseg.getpCoase3().toString());
								}else if(datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase4())){
									datosPoliza.setParticipacionAbridora(datoscoaseg.getpCoase4().toString());
								}else if(datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase5())){
									datosPoliza.setParticipacionAbridora(datoscoaseg.getpCoase5().toString());
								}
								
								int widthPoliza = 10;
								int widthSubPoliza = 4;
								 
						        String formattedPoliza = String.format("%0" + widthPoliza + "d", Integer.valueOf(flujo.getKpoliza().toString()));
						        String formattedSubPoliza = String.format("%0" + widthSubPoliza + "d", Integer.valueOf(flujo.getKsubpoliza().toString()));
								
								datosPoliza.setNumeroPoliza(formattedPoliza + formattedSubPoliza);
								datosPoliza.setNumeroSuplemento(flujo.getNsuscri().toString());
								
								CuadroCoaseguro cuadroCoaseg = new CuadroCoaseguro();
								
								List<ParticipacionEntidad> partEntidad = new ArrayList<>();
								
								if(null != datoscoaseg.getKCoase1() && !datoscoaseg.getKCoase1().equals("")){
								
									if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase1())){
									
										ParticipacionEntidad ent1 = new ParticipacionEntidad();
										ent1.setEntidadAceptante(datoscoaseg.getKCoase1().substring(0,1) + "0" + datoscoaseg.getKCoase1().substring(1));
										ent1.setParticipacionAceptante(datoscoaseg.getpCoase1().toString());
										partEntidad.add(ent1);
									}
								}
								
								if(null != datoscoaseg.getKCoase2() && !datoscoaseg.getKCoase2().equals("")){
									
									if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase2())){
									ParticipacionEntidad ent2 = new ParticipacionEntidad();
									ent2.setEntidadAceptante(datoscoaseg.getKCoase2().substring(0,1) + "0" + datoscoaseg.getKCoase2().substring(1));
									ent2.setParticipacionAceptante(datoscoaseg.getpCoase2().toString());
									partEntidad.add(ent2);
									}
								}
								
								if(null != datoscoaseg.getKCoase3() && !datoscoaseg.getKCoase3().equals("")){
									if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase3())){
								
									ParticipacionEntidad ent3 = new ParticipacionEntidad();
									ent3.setEntidadAceptante(datoscoaseg.getKCoase3().substring(0,1) + "0" + datoscoaseg.getKCoase3().substring(1));
									ent3.setParticipacionAceptante(datoscoaseg.getpCoase3().toString());
									partEntidad.add(ent3);
									}
								}
								
								if(null != datoscoaseg.getKCoase4() && !datoscoaseg.getKCoase4().equals("")){
									if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase4())){
								
									ParticipacionEntidad ent4 = new ParticipacionEntidad();
									ent4.setEntidadAceptante(datoscoaseg.getKCoase4().substring(0,1) + "0" + datoscoaseg.getKCoase4().substring(1));
									ent4.setParticipacionAceptante(datoscoaseg.getpCoase4().toString());
									partEntidad.add(ent4);
									}
								}
								
								if(null != datoscoaseg.getKCoase5() && !datoscoaseg.getKCoase5().equals("")){
									if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase5())){
									ParticipacionEntidad ent5 = new ParticipacionEntidad();
									ent5.setEntidadAceptante(datoscoaseg.getKCoase5().substring(0,1) + "0" + datoscoaseg.getKCoase5().substring(1));
									ent5.setParticipacionAceptante(datoscoaseg.getpCoase5().toString());
									partEntidad.add(ent5);
									}
								}
								
								cuadroCoaseg.setParticipacionEntidad(partEntidad);
								datosPoliza.setCuadroCoaseguro(cuadroCoaseg);
								
								SimpleDateFormat sdfechaEfecto = new SimpleDateFormat("yyyy-MM-dd");
								String fechaEfecto = sdfechaEfecto.format(flujo.getFecefecini());
								datosPoliza.setFechaEfecto(fechaEfecto);
								
								
								datosPoliza.setMetodoSuplemento("INCREMENTAL");
								rentaIrregular.setDatosPoliza(datosPoliza);
								
								
								
								int widthCertificado = 6;

						        String formattedCertificado = String.format("%0" + widthCertificado + "d", Integer.valueOf(flujo.getKcertificado().toString()));
								String idAplicacion = formattedPoliza + formattedSubPoliza + formattedCertificado;
								
								Tab35015Key t35015 = new Tab35015Key(flujo.getKmodalidad(), flujo.getKgarantia(), flujo.getKprestacion()); 
								Tab35015 tab35015 = tab35015Dao.get(t35015);
								if (tab35015 == null) {
									t35015 = new Tab35015Key(flujo.getKmodalidad(), flujo.getKgarantia(), "");
									tab35015 = tab35015Dao.get(t35015);
									if (tab35015 == null) {
										t35015 = new Tab35015Key(flujo.getKmodalidad(), 0, "");
										tab35015 = tab35015Dao.get(t35015);
										if (tab35015 == null) {
											t35015 = new Tab35015Key(0, flujo.getKgarantia(), flujo.getKprestacion());
											tab35015 = tab35015Dao.get(t35015);
											if (tab35015 == null) {
												t35015 = new Tab35015Key(0, flujo.getKgarantia(), "");
												tab35015 = tab35015Dao.get(t35015);
												if (tab35015 == null) {
													t35015 = new Tab35015Key(0, 0, flujo.getKprestacion());
													tab35015 = tab35015Dao.get(t35015);
													if (tab35015 == null) {
														t35015 = new Tab35015Key(0, 0, "");
														tab35015 = tab35015Dao.get(t35015);
													}
												}
											}
										}
									}
								}
								
								System.out.println(contador + " " + flujo.getRentini().toString());
								
								if ((flujo.getRentini() == null || flujo.getRentini().toString().equals("0.00") || flujo.getRentini().toString().equals("0")) && !flujo.getTempVit().equals("V")) {
									
									List<PagosPlan> listPagosPlanAll = pagosPlanDao.getValues();
									
									Iterator<PagosPlan> itPP = listPagosPlanAll.iterator();
									int salida = 0;
									
									PagoPlanificado pagoPlanificado = new PagoPlanificado();
									
									while (itPP.hasNext() || salida == 1) {
										
										PagosPlan flujoPP = itPP.next();
										if (flujo.getKpoliza().equals(flujoPP.getKpoliza())
												&& flujo.getKsubpoliza().equals(flujoPP.getKsubpol())
												&& flujo.getKcertificado().equals(flujoPP.getKcerti())
												&& flujo.getNsuscri().equals(flujoPP.getNsuscri())
												&& flujo.getKgarantia().equals(flujoPP.getCgarantia())
												&& flujo.getKprestacion().substring(0,1).equals(flujoPP.getCprestaEntorno())
												&& (flujo.getKprestacion().substring(1, 3).equals(flujoPP.getKpresta())
														|| flujo.getKprestacion().substring(1).equals(flujoPP.getKpresta()))) {
											
											pagoPlanificado.setIdAplicacion(idAplicacion);
											if (tab35015 == null) {
												if (flujo.getTempVit().equals("L")) {
													pagoPlanificado.setIdGarantia(ConstantesSolvencia.RENTA_L.replace(" ", ""));
													pagoPlanificado.setIdGarantiaPrincipal(ConstantesSolvencia.RENTA_L.replace(" ", ""));
													pagoPlanificado.setCodGarantia(ConstantesSolvencia.L.replace(" ", ""));
												} else if (flujo.getTempVit().equals("T")) {
													pagoPlanificado.setIdGarantia(ConstantesSolvencia.RENTA_T.replace(" ", ""));
													pagoPlanificado.setIdGarantiaPrincipal(ConstantesSolvencia.RENTA_T.replace(" ", ""));
													pagoPlanificado.setCodGarantia(ConstantesSolvencia.T.replace(" ", ""));
												} else if (flujo.getTempVit().equals("V")) {
													pagoPlanificado.setIdGarantia(ConstantesSolvencia.RENTA_V.replace(" ", ""));
													pagoPlanificado.setIdGarantiaPrincipal(ConstantesSolvencia.RENTA_V.replace(" ", ""));
													pagoPlanificado.setCodGarantia(ConstantesSolvencia.V.replace(" ", ""));
												} else {
													pagoPlanificado.setIdGarantia("");
													pagoPlanificado.setIdGarantiaPrincipal("");
													pagoPlanificado.setCodGarantia("");
												}
											} else {
												pagoPlanificado.setIdGarantia(tab35015.getDescripcionGarantiaTirea().replace(" ", ""));
												pagoPlanificado.setIdGarantiaPrincipal(tab35015.getDescripcionGarantiaTirea().replace(" ", ""));
												pagoPlanificado.setCodGarantia(tab35015.getGarantiaTirea().replace(" ", ""));
											}

											String fechaEfectoPP = sdfechaEfecto.format(flujoPP.getFplreaEfecto());
											pagoPlanificado.setFechaEfecto(fechaEfectoPP);
											
											pagoPlanificado.setImporteBruto(flujoPP.getEplreaBruto().toString());
											String fechaIni = fechaEfectoPP.substring(0,8) + "01";
											String fechaFin = fechaEfectoPP.substring(0,8);
											if (fechaEfectoPP.substring(5,7).equals("01")
													|| fechaEfectoPP.substring(5,7).equals("03")
													|| fechaEfectoPP.substring(5,7).equals("05")
													|| fechaEfectoPP.substring(5,7).equals("07") 
													|| fechaEfectoPP.substring(5,7).equals("08")
													|| fechaEfectoPP.substring(5,7).equals("10") 
													|| fechaEfectoPP.substring(5,7).equals("12")) {
												fechaFin = fechaFin + "31";
											} else if (fechaEfectoPP.substring(5,7).equals("02")) {
												fechaFin = fechaFin + "28";
											} else {
												fechaFin = fechaFin + "30";
											}
											pagoPlanificado.setFechaInicio(fechaIni);
											pagoPlanificado.setFechaFin(fechaFin);
											
											
											listPagoPlanificado.add(pagoPlanificado);	
											pagosPlanificados.setPagoPlanificado(listPagoPlanificado);
											salida = 1;
										}
										
									}
									
									if (salida == 0) {
										pagoPlanificado.setIdAplicacion(idAplicacion);
										pagoPlanificado.setIdGarantia(tab35015.getDescripcionGarantiaTirea().replace(" ", ""));
										pagoPlanificado.setIdGarantiaPrincipal(tab35015.getDescripcionGarantiaTirea().replace(" ", ""));
										pagoPlanificado.setCodGarantia(tab35015.getGarantiaTirea().replace(" ", ""));
										String fechaEfectoPP = sdfechaEfecto.format(flujo.getFecefecini());
										pagoPlanificado.setFechaEfecto(fechaEfectoPP);
										pagoPlanificado.setImporteBruto(flujo.getRentini().toString());
										String fechaIni = fechaEfectoPP.substring(0,8) + "01";
										String fechaFin = fechaEfectoPP.substring(0,8);
										if (fechaEfectoPP.substring(5,7).equals("01")
												|| fechaEfectoPP.substring(5,7).equals("03")
												|| fechaEfectoPP.substring(5,7).equals("05")
												|| fechaEfectoPP.substring(5,7).equals("07") 
												|| fechaEfectoPP.substring(5,7).equals("08")
												|| fechaEfectoPP.substring(5,7).equals("10") 
												|| fechaEfectoPP.substring(5,7).equals("12")) {
											fechaFin = fechaFin + "31";
										} else if (fechaEfectoPP.substring(5,7).equals("02")) {
											fechaFin = fechaFin + "28";
										} else {
											fechaFin = fechaFin + "30";
										}
										pagoPlanificado.setFechaInicio(fechaIni);
										pagoPlanificado.setFechaFin(fechaFin);
										
										
										listPagoPlanificado.add(pagoPlanificado);	
										pagosPlanificados.setPagoPlanificado(listPagoPlanificado);
									}
								}
							}
							
							
							
							
						} catch (Exception e) {
							
							Incidencia inci = new Incidencia();
							inci.setFecCierre(fecCierre);
							inci.setCodigoRetorno("11");
							inci.setInfAmpliada("Error" + mensaje + " - " + e.getMessage());
							//gestorIncidenciasPres.write(inci);
							inci.setCcanal("01");
							giGeneral.write(inci);
						}
						
						
						iteracion++;
					
					
					}
					
					rentaIrregular.setPagosPlanificados(pagosPlanificados);
					movRentasIrregulares.setRentaIrregular(rentaIrregular);
					listMoviRentasIrregulares.add(movRentasIrregulares);
					movimientosRentasIrregulares.setMovimientorentasirregulares(listMoviRentasIrregulares);			
					
					
					try {
					    JAXBContext jaxbContext = JAXBContext.newInstance(MovimientosCoaseguroRentasIrregulares.class);
					    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
					    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

					    File file = new File("XML-RI.xml");
					    jaxbMarshaller.marshal(movimientosRentasIrregulares, file);
					    
					    Incidencia inci = null;
						InputStream in = null;
						OutputStream out = null;
						BtUtils btUtils = new BtUtils();
						
					    File destination = new File(btUtils.getRutaFicherosProperty(ConstantesSolvencia.RUTA_EXPORTA_XML) + fecCierre.substring(0,6) + File.separator + "TIREA" + File.separator + "XML-RI.XML");
						if (file.exists()) {
				            try {
				                in = new FileInputStream(file);
				                try {
				                    out = new FileOutputStream(destination);
				                    try {
				                    	String cierre = ">";
				                    	char separador = '"';
				                    	String apertura = "<";
				                    	
				                    	String cabeceraEntrada1 = "<?xml version=" + separador + "1.0" + separador + "encoding=" + separador + "UTF-8" + separador + "standalone=" + separador + "yes" + separador + "?>" + "/n";
				                    	String cabeceraEntrada2 = "<MovimientosCoaseguro>" + "/n";
				                    	
				                    	byte[] bufEntrada1 = cabeceraEntrada1.getBytes();
				                        byte[] bufEntrada2 = cabeceraEntrada2.getBytes();
				             
				                    	String cabeceraXml = "<?xml version=" + separador + "1.0" + separador + " encoding=" + separador + "UTF-8" + separador + "?>" + "\n";
				                    	String cabeceraXsd = "<MovimientosCoaseguro xmlns=" + separador + "http://www.tirea.es/Coaseguro/MovimientosCoaseguro" + separador +  
				                    		" xmlns:xsi=" + separador + "http://www.w3.org/2001/XMLSchema-instance" + separador 
				                    			+ " xsi:schemaLocation=" + separador + "http://www.tirea.es/Coaseguro/MovimientosCoaseguro MovimientosCoaseguro_V05-6.xsd"
				                    		+ separador + cierre + "\n";
				                    	
				                        byte[] buf = new byte[1024];
				                        byte[] bufxml = cabeceraXml.getBytes();
				                        byte[] bufxsd = cabeceraXsd.getBytes();
				                        int len;
				                        
				                        in.read(bufEntrada1);
				                        out.write(bufxml,0,bufxml.length);
				                        in.read(bufEntrada2);
				                        out.write(bufxsd,0,bufxsd.length);
				                      
				                           
				        				while ((len = in.read(buf)) > 0) {
				        				    out.write(buf, 0, len);
				        				}
				        				
				                        in.close();
				                        out.close();
				        			} catch (IOException e) {
				                    	inci = new Incidencia();
				                    	inci.setCodigoRetorno("12");
				                    	inci.setInfAmpliada("Error al copiar el fichero " + file + " al fichero " + destination + " - " + e.getMessage());
				                    	inci.setFecCierre(gfecCierre);
				                    	inci.setCcanal("01");
				        			}
				                } catch (FileNotFoundException e) {
				                	inci = new Incidencia();
				                	inci.setCodigoRetorno("12");
				                	inci.setInfAmpliada("Error al crear el fichero " + destination + " - " + e.getMessage());
				                	inci.setFecCierre(gfecCierre);
			                    	inci.setCcanal("01");
				                }
				            } catch (FileNotFoundException e) {
				            	inci = new Incidencia();
				            	inci.setCodigoRetorno("12");
				            	inci.setInfAmpliada("Error al cargar el fichero " + file + " - " + e.getMessage());
				            	inci.setFecCierre(gfecCierre);
		                    	inci.setCcanal("01");
				            }
				        } else {
				        	inci = new Incidencia();
				        	inci.setCodigoRetorno("12");
				        	inci.setInfAmpliada("No se encuentra el fichero " + file);
				        	inci.setFecCierre(gfecCierre);
	                    	inci.setCcanal("01");
				        }

					} catch (JAXBException e) {
					    e.printStackTrace();
					}	
					
					
					//////////////////////DATOS ESPECIFICOS////////////////////////	
					List<FlujPMdCoa> listaFlujPmdCoaDE = flujPmdDao.getValues();
					Collections.sort(listaFlujPmdCoaDE, new Comparator<FlujPMdCoa>() {
						public int compare(FlujPMdCoa o1, FlujPMdCoa o2) {
							return o1.getKey().compareTo(o2.getKey());
						}
					});
					
					Iterator<FlujPMdCoa> itPmdDE = listaFlujPmdCoaDE.iterator();

					
					MovimientosCoaseguroDatosEspecificos movimientosDatosEspecificos =  new MovimientosCoaseguroDatosEspecificos();

					auxCalculoFlujos = BigDecimal.ZERO;
					int indicePmdDE = 0;
					poliza = new Long(0);
					subpoliza = 0;
					nsuscri = 0;
					
					Cabecera cabDatosEspecificos = new Cabecera();
					
					sdfecha = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
					fechaComoCadena = sdfecha.format(new Date());
					cabDatosEspecificos.setFecha(fechaComoCadena);
					cabDatosEspecificos.setIdEmisor("C0511");
					fechaIdFichero = sdfAux.format(new Date());
					cabDatosEspecificos.setIdFichero(fechaIdFichero);
					cabDatosEspecificos.setIdReceptor("T0001");
					movimientosDatosEspecificos.setCabecera(cabDatosEspecificos);
					
					List<Variable> listVariable = new ArrayList<>();
					List<MovimientoDatosEspecificos> listMoviDatosEspecificos = new ArrayList<>();
					Variables variables = new Variables();
					DatosEspecificos datosEspecificos = new DatosEspecificos();
					MovimientoDatosEspecificos movDatosEspecificos = new MovimientoDatosEspecificos();
					contador = 0;
					
					while (itPmdDE.hasNext()) {
						
						contador++;
						System.out.println(contador);
						FlujPMdCoa flujo = itPmdDE.next();
							
						try {
							
							DatosCoaKey datCoa = new DatosCoaKey(flujo.getKpoliza(),flujo.getKsubpoliza()); 
							DatosCoa datoscoaseg = datosCoaDao.get(datCoa);
								
							if (poliza.equals(new Long(0)) 
									&& subpoliza.equals(0)
									&& nsuscri.equals(0)) {
								poliza = flujo.getKpoliza();
								subpoliza = flujo.getKsubpoliza();
								nsuscri = flujo.getNsuscri();
									
								movDatosEspecificos = new MovimientoDatosEspecificos();
							}
								
							if (!poliza.equals(flujo.getKpoliza()) 
									|| !subpoliza.equals(flujo.getKsubpoliza())
									|| !nsuscri.equals(flujo.getNsuscri())) {
								poliza = flujo.getKpoliza();
								subpoliza = flujo.getKsubpoliza();
								nsuscri = flujo.getNsuscri();
										
								variables.setVariable(listVariable);
								datosEspecificos.setVariables(variables);
								movDatosEspecificos.setDatosEspecificos(datosEspecificos);
								listMoviDatosEspecificos.add(movDatosEspecificos);
								
								datosEspecificos = new DatosEspecificos();
								variables = new Variables();
								listVariable = new ArrayList<>();
								movDatosEspecificos = new MovimientoDatosEspecificos();
							}
							
							
							
							movDatosEspecificos.setTipoMovimiento(10);
							
							String fechaAux = fecCierre.substring(0,6);
							if(fechaAux.substring(4).equals("01") || fechaAux.substring(4).equals("03") || fechaAux.substring(4).equals("05") 
								|| fecCierre.substring(4).equals("07") || fechaAux.substring(4).equals("08") || fechaAux.substring(4).equals("10") || fechaAux.substring(4).equals("12") ){
								fechaAux = fecCierre.substring(0,4) + "-" + fecCierre.substring(4,6) + "-" + "31";
							}else if(fechaAux.substring(4).equals("02")){
								fechaAux = fecCierre.substring(0,4) + "-" + fecCierre.substring(4,6) + "-" + "28";
							}else{
								fechaAux = fecCierre.substring(0,4) + "-" + fecCierre.substring(4,6) + "-" + "30";
							}
							
							movDatosEspecificos.setFecha(fechaAux);
							
							datosEspecificos = new DatosEspecificos();
							DatosPoliza datosPoliza = new DatosPoliza();
							
							datosPoliza.setEntidadAbridora(datoscoaseg.getkCoaseOri().substring(0,1) + "0" + datoscoaseg.getkCoaseOri().substring(1));
							
							if(datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase1())){
								datosPoliza.setParticipacionAbridora(datoscoaseg.getpCoase1().toString());
							}else if(datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase2())){
								datosPoliza.setParticipacionAbridora(datoscoaseg.getpCoase2().toString());
							}else if(datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase3())){
								datosPoliza.setParticipacionAbridora(datoscoaseg.getpCoase3().toString());
							}else if(datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase4())){
								datosPoliza.setParticipacionAbridora(datoscoaseg.getpCoase4().toString());
							}else if(datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase5())){
								datosPoliza.setParticipacionAbridora(datoscoaseg.getpCoase5().toString());
							}
							
							int widthPoliza = 10;
							int widthSubPoliza = 4;
							 
					        String formattedPoliza = String.format("%0" + widthPoliza + "d", Integer.valueOf(flujo.getKpoliza().toString()));
					        String formattedSubPoliza = String.format("%0" + widthSubPoliza + "d", Integer.valueOf(flujo.getKsubpoliza().toString()));
							
							datosPoliza.setNumeroPoliza(formattedPoliza + formattedSubPoliza);
							datosPoliza.setNumeroSuplemento(flujo.getNsuscri().toString());
							
							CuadroCoaseguro cuadroCoaseg = new CuadroCoaseguro();
							
							List<ParticipacionEntidad> partEntidad = new ArrayList<>();
							
							if(null != datoscoaseg.getKCoase1() && !datoscoaseg.getKCoase1().equals("")){
							
								if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase1())){
								
									ParticipacionEntidad ent1 = new ParticipacionEntidad();
									ent1.setEntidadAceptante(datoscoaseg.getKCoase1().substring(0,1) + "0" + datoscoaseg.getKCoase1().substring(1));
									ent1.setParticipacionAceptante(datoscoaseg.getpCoase1().toString());
									partEntidad.add(ent1);
								}
							}
							
							if(null != datoscoaseg.getKCoase2() && !datoscoaseg.getKCoase2().equals("")){
								
								if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase2())){
								ParticipacionEntidad ent2 = new ParticipacionEntidad();
								ent2.setEntidadAceptante(datoscoaseg.getKCoase2().substring(0,1) + "0" + datoscoaseg.getKCoase2().substring(1));
								ent2.setParticipacionAceptante(datoscoaseg.getpCoase2().toString());
								partEntidad.add(ent2);
								}
							}
							
							if(null != datoscoaseg.getKCoase3() && !datoscoaseg.getKCoase3().equals("")){
								if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase3())){
							
								ParticipacionEntidad ent3 = new ParticipacionEntidad();
								ent3.setEntidadAceptante(datoscoaseg.getKCoase3().substring(0,1) + "0" + datoscoaseg.getKCoase3().substring(1));
								ent3.setParticipacionAceptante(datoscoaseg.getpCoase3().toString());
								partEntidad.add(ent3);
								}
							}
							
							if(null != datoscoaseg.getKCoase4() && !datoscoaseg.getKCoase4().equals("")){
								if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase4())){
							
								ParticipacionEntidad ent4 = new ParticipacionEntidad();
								ent4.setEntidadAceptante(datoscoaseg.getKCoase4().substring(0,1) + "0" + datoscoaseg.getKCoase4().substring(1));
								ent4.setParticipacionAceptante(datoscoaseg.getpCoase4().toString());
								partEntidad.add(ent4);
								}
							}
							
							if(null != datoscoaseg.getKCoase5() && !datoscoaseg.getKCoase5().equals("")){
								if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase5())){
								ParticipacionEntidad ent5 = new ParticipacionEntidad();
								ent5.setEntidadAceptante(datoscoaseg.getKCoase5().substring(0,1) + "0" + datoscoaseg.getKCoase5().substring(1));
								ent5.setParticipacionAceptante(datoscoaseg.getpCoase5().toString());
								partEntidad.add(ent5);
								}
							}
							
							cuadroCoaseg.setParticipacionEntidad(partEntidad);
							datosPoliza.setCuadroCoaseguro(cuadroCoaseg);
							
							SimpleDateFormat sdfechaEfecto = new SimpleDateFormat("yyyy-MM-dd");
							String fechaEfecto = sdfechaEfecto.format(flujo.getFecefecini());
							datosPoliza.setFechaEfecto(fechaEfecto);
							
							
							datosPoliza.setMetodoSuplemento("INCREMENTAL");
							datosEspecificos.setDatosPoliza(datosPoliza);
							
							
							
							int widthCertificado = 6;
							 
					        String formattedCertificado = String.format("%0" + widthCertificado + "d", Integer.valueOf(flujo.getKcertificado().toString()));
							String idAplicacion = formattedPoliza + formattedSubPoliza + formattedCertificado;
					        
							
							//DatosEspecificKey despecific = new DatosEspecificKey(flujo.getKpoliza(),flujo.getKsubpoliza(), flujo.getKcertificado(), flujo.getNsuscri()); 
//							DatosEspecific datosEspecific = datosEspecificDao.get(despecific);
//							List<DatosEspecific> listDatosEspecific = datosEspecificDao.getValues(despecific);
							List<DatosEspecific> listDatosEspecificAll = datosEspecificDao.getValues();
							
							Iterator<DatosEspecific> itDE = listDatosEspecificAll.iterator();
							
							
							while (itDE.hasNext()) {
								
								Variable variable = new Variable();
								variable.setIdAplicacion(idAplicacion);
								DatosEspecific flujoDE = itDE.next();
								if (flujo.getKpoliza().equals(flujoDE.getKpoliza())
										&& flujo.getKsubpoliza().equals(flujoDE.getKsubpol())
										&& flujo.getKcertificado().equals(flujoDE.getKcerti())
										&& flujo.getNsuscri().equals(flujoDE.getNsuscri())) {
									
									variable.setCodigo(flujoDE.getCodigo());
									variable.setValor(flujoDE.getDato());
									
									
									listVariable.add(variable);	
								}
								
							}
							
						} catch (Exception e) {
							
							Incidencia inci = new Incidencia();
							inci.setFecCierre(fecCierre);
							inci.setCodigoRetorno("11");
							inci.setInfAmpliada("Error" + mensaje + " - " + e.getMessage());
							//gestorIncidenciasPres.write(inci);
							inci.setCcanal("01");
							giGeneral.write(inci);
						}
						
						iteracion++;
					
					}
					
					variables.setVariable(listVariable);
					datosEspecificos.setVariables(variables);
					movDatosEspecificos.setDatosEspecificos(datosEspecificos);
					listMoviDatosEspecificos.add(movDatosEspecificos);
					movimientosDatosEspecificos.setMovimientodatosespecificos(listMoviDatosEspecificos);			
					
					
					try {
					    JAXBContext jaxbContext = JAXBContext.newInstance(MovimientosCoaseguroDatosEspecificos.class);
					    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
					    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

					    File file = new File("XML-DE.xml");
					    jaxbMarshaller.marshal(movimientosDatosEspecificos, file);
					    
					    Incidencia inci = null;
						InputStream in = null;
						OutputStream out = null;
						BtUtils btUtils = new BtUtils();
						
					    File destination = new File(btUtils.getRutaFicherosProperty(ConstantesSolvencia.RUTA_EXPORTA_XML) + fecCierre.substring(0,6) + File.separator + "TIREA" + File.separator + "XML-DE.XML");
						if (file.exists()) {
				            try {
				                in = new FileInputStream(file);
				                try {
				                    out = new FileOutputStream(destination);
				                    try {
				                    	String cierre = ">";
				                    	char separador = '"';
				                    	String apertura = "<";
				                    	
				                    	String cabeceraEntrada1 = "<?xml version=" + separador + "1.0" + separador + "encoding=" + separador + "UTF-8" + separador + "standalone=" + separador + "yes" + separador + "?>" + "/n";
				                    	String cabeceraEntrada2 = "<MovimientosCoaseguro>" + "/n";
				                    	
				                    	byte[] bufEntrada1 = cabeceraEntrada1.getBytes();
				                        byte[] bufEntrada2 = cabeceraEntrada2.getBytes();
				             
				                    	String cabeceraXml = "<?xml version=" + separador + "1.0" + separador + " encoding=" + separador + "UTF-8" + separador + "?>" + "\n";
				                    	String cabeceraXsd = "<MovimientosCoaseguro xmlns=" + separador + "http://www.tirea.es/Coaseguro/MovimientosCoaseguro" + separador +  
				                    		" xmlns:xsi=" + separador + "http://www.w3.org/2001/XMLSchema-instance" + separador 
				                    			+ " xsi:schemaLocation=" + separador + "http://www.tirea.es/Coaseguro/MovimientosCoaseguro MovimientosCoaseguro_V05-6.xsd"
				                    		+ separador + cierre + "\n";
				                    	
				                        byte[] buf = new byte[1024];
				                        byte[] bufxml = cabeceraXml.getBytes();
				                        byte[] bufxsd = cabeceraXsd.getBytes();
				                        int len;
				                        
				                        in.read(bufEntrada1);
				                        out.write(bufxml,0,bufxml.length);
				                        in.read(bufEntrada2);
				                        out.write(bufxsd,0,bufxsd.length);
				                      
				                           
				        				while ((len = in.read(buf)) > 0) {
				        				    out.write(buf, 0, len);
				        				}
				        				
				                        in.close();
				                        out.close();
				        			} catch (IOException e) {
				                    	inci = new Incidencia();
				                    	inci.setCodigoRetorno("12");
				                    	inci.setInfAmpliada("Error al copiar el fichero " + file + " al fichero " + destination + " - " + e.getMessage());
				                    	inci.setFecCierre(gfecCierre);
				                    	inci.setCcanal("01");
				        			}
				                } catch (FileNotFoundException e) {
				                	inci = new Incidencia();
				                	inci.setCodigoRetorno("12");
				                	inci.setInfAmpliada("Error al crear el fichero " + destination + " - " + e.getMessage());
				                	inci.setFecCierre(gfecCierre);
			                    	inci.setCcanal("01");
				                }
				            } catch (FileNotFoundException e) {
				            	inci = new Incidencia();
				            	inci.setCodigoRetorno("12");
				            	inci.setInfAmpliada("Error al cargar el fichero " + file + " - " + e.getMessage());
				            	inci.setFecCierre(gfecCierre);
		                    	inci.setCcanal("01");
				            }
				        } else {
				        	inci = new Incidencia();
				        	inci.setCodigoRetorno("12");
				        	inci.setInfAmpliada("No se encuentra el fichero " + file);
				        	inci.setFecCierre(gfecCierre);
	                    	inci.setCcanal("01");
				        }

					} catch (JAXBException e) {
					    e.printStackTrace();
					}	
				}
				
				giGeneral.cerrarConector();
				System.exit(0);
				
			} catch(Solvencia2Excepcion solvExc){
			
				Incidencia inci = solvExc.getIncidencia();
				giGeneral.write(inci);
				giGeneral.cerrarConector();
				System.exit(10);
			} catch (Exception e) {
				
				Incidencia inci = new Incidencia();
				inci.setCodigoRetorno("17");
				inci.setInfAmpliada("Error gen�rico" + " - " + e.getMessage());
				giGeneral.write(inci);
				giGeneral.cerrarConector();
				System.exit(20);
			}
		
		} catch (Throwable t) {
			GestorIncidenciasGen giGeneral = GestorIncidenciasGen.getInstance();
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("17");
			inci.setInfAmpliada("Error gen�rico" + " - " + t.getMessage());
			giGeneral.write(inci);
			giGeneral.cerrarConector();
			LoggerManager logGeneral = LoggerManager.getInstance();
			logGeneral.writeLog("Error gen�rico " + " - " + t.getMessage(), ConstantesSolvencia.LOG_ERROR, true);
			logGeneral.cerrarWriter();
			System.exit(30);
		}	
	}
}
