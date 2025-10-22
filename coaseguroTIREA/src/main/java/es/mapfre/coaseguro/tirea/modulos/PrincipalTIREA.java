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
import es.mapfre.coaseguro.tirea.dao.entidades.FlujPMaCoaDao;
import es.mapfre.coaseguro.tirea.dao.entidades.Tab35012Dao;
import es.mapfre.coaseguro.tirea.dao.entidades.TotPMaCoaDao;
import es.mapfre.coaseguro.tirea.dominio.entidades.BaseTecnica;
import es.mapfre.coaseguro.tirea.dominio.entidades.BaseTecnicaPMF;
import es.mapfre.coaseguro.tirea.dominio.entidades.BasesTecnicas;
import es.mapfre.coaseguro.tirea.dominio.entidades.BasesTecnicasPMF;
import es.mapfre.coaseguro.tirea.dominio.entidades.Cabecera;
import es.mapfre.coaseguro.tirea.dominio.entidades.CuadroCoaseguro;
import es.mapfre.coaseguro.tirea.dominio.entidades.DatosCoa;
import es.mapfre.coaseguro.tirea.dominio.entidades.DatosFlujo;
import es.mapfre.coaseguro.tirea.dominio.entidades.DatosFlujos;
import es.mapfre.coaseguro.tirea.dominio.entidades.DatosPMA;
import es.mapfre.coaseguro.tirea.dominio.entidades.DatosPoliza;
import es.mapfre.coaseguro.tirea.dominio.entidades.FichaProceso;
import es.mapfre.coaseguro.tirea.dominio.entidades.FlujPMaCoa;
import es.mapfre.coaseguro.tirea.dominio.entidades.Gasto;
import es.mapfre.coaseguro.tirea.dominio.entidades.Gastos;
import es.mapfre.coaseguro.tirea.dominio.entidades.Incidencia;
import es.mapfre.coaseguro.tirea.dominio.entidades.Interes;
import es.mapfre.coaseguro.tirea.dominio.entidades.Movimiento;
import es.mapfre.coaseguro.tirea.dominio.entidades.MovimientoPMF;
import es.mapfre.coaseguro.tirea.dominio.entidades.MovimientosCoaseguro;
import es.mapfre.coaseguro.tirea.dominio.entidades.MovimientosCoaseguroPMF;
import es.mapfre.coaseguro.tirea.dominio.entidades.PMA;
import es.mapfre.coaseguro.tirea.dominio.entidades.PMF;
import es.mapfre.coaseguro.tirea.dominio.entidades.ParticipacionEntidad;
import es.mapfre.coaseguro.tirea.dominio.entidades.Tab35012;
import es.mapfre.coaseguro.tirea.dominio.entidades.Tabla;
import es.mapfre.coaseguro.tirea.dominio.entidades.Tablas;
import es.mapfre.coaseguro.tirea.dominio.entidades.TiposInteres;
import es.mapfre.coaseguro.tirea.dominio.entidades.TotPMaCoa;
import es.mapfre.coaseguro.tirea.dominio.keys.DatosCoaKey;
import es.mapfre.coaseguro.tirea.dominio.keys.Tab35012Key;
import es.mapfre.coaseguro.tirea.excepcion.Solvencia2Excepcion;
import es.mapfre.coaseguro.tirea.gestores.GestorFichaProceso;
import es.mapfre.coaseguro.tirea.gestores.GestorIncidenciasGen;
import es.mapfre.coaseguro.tirea.gestores.GestorIncidenciasPres;
import es.mapfre.coaseguro.tirea.utils.CargaDatos;
import es.mapfre.coaseguro.tirea.utils.ConstantesSolvencia;
import es.mapfre.coaseguro.tirea.utils.LoggerManager;
import es.mapfre.coaseguro.tirea.utils.BtUtils;

public class PrincipalTIREA {
	
	public PrincipalTIREA() {
	}

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
					Tab35012Dao tab35012Dao = new Tab35012Dao();
					List<DatosCoa> listaDatosCoa = datosCoaDao.getValues();
					List<Tab35012> listaTab35012 = tab35012Dao.getValues();
					List<TotPMaCoa> listaTotPmaCoa = totPmaDao.getValues();
					List<FlujPMaCoa> listaFlujPmaCoa = flujPmaDao.getValues();
					
					/////////////////////////////////PMA/////////////////////
					Iterator<TotPMaCoa> itPma = listaTotPmaCoa.iterator();
					String mensaje = "";
					
					MovimientosCoaseguro movimientosPma =  new MovimientosCoaseguro();
					int indice = 0;
					List<Movimiento> listMovi = new ArrayList<>();
					while (itPma.hasNext()) {
						TotPMaCoa flujo = itPma.next();
						try {
							DatosCoaKey datCoa = new DatosCoaKey(flujo.getKpoliza(),flujo.getKsubpoliza()); 
							DatosCoa datoscoaseg = datosCoaDao.get(datCoa);
							if(indice == 0){
							Cabecera cab = new Cabecera();
							SimpleDateFormat sdfecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
							if(fecCierre.substring(4).equals("01") || fecCierre.substring(4).equals("03") || fecCierre.substring(4).equals("05") 
								|| fecCierre.substring(4).equals("07") || fecCierre.substring(4).equals("08") || fecCierre.substring(4).equals("10") || fecCierre.substring(4).equals("12") ){
								fechaAux = fecCierre.substring(0,4) + "-" + fecCierre.substring(4,6) + "-" + "31";
							}else if(fecCierre.substring(4).equals("02")){
								fechaAux = fecCierre.substring(0,4) + "-" + fecCierre.substring(4,6) + "-" + "28";
							}else{
								fechaAux = fecCierre.substring(0,4) + "-" + fecCierre.substring(4,6) + "-" + "30";
							}
							
							mov.setFecha(fechaAux);
							
							PMA pma = new PMA();
							DatosPoliza datosPoliza = new DatosPoliza();
							
							datosPoliza.setEntidadAbridora(datoscoaseg.getkCoaseOri());
							
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
									ent1.setEntidadAceptante(datoscoaseg.getKCoase1());
									ent1.setParticipacionAceptante(datoscoaseg.getpCoase1().toString());
									partEntidad.add(ent1);
								}
							}
							
							if(null != datoscoaseg.getKCoase2() && !datoscoaseg.getKCoase2().equals("")){
								
								if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase2())){
								ParticipacionEntidad ent2 = new ParticipacionEntidad();
								ent2.setEntidadAceptante(datoscoaseg.getKCoase2());
								ent2.setParticipacionAceptante(datoscoaseg.getpCoase2().toString());
								partEntidad.add(ent2);
								}
							}
							
							if(null != datoscoaseg.getKCoase3() && !datoscoaseg.getKCoase3().equals("")){
								if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase3())){
							
								ParticipacionEntidad ent3 = new ParticipacionEntidad();
								ent3.setEntidadAceptante(datoscoaseg.getKCoase3());
								ent3.setParticipacionAceptante(datoscoaseg.getpCoase3().toString());
								partEntidad.add(ent3);
								}
							}
							
							if(null != datoscoaseg.getKCoase4() && !datoscoaseg.getKCoase4().equals("")){
								if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase4())){
							
								ParticipacionEntidad ent4 = new ParticipacionEntidad();
								ent4.setEntidadAceptante(datoscoaseg.getKCoase4());
								ent4.setParticipacionAceptante(datoscoaseg.getpCoase4().toString());
								partEntidad.add(ent4);
								}
							}
							
							if(null != datoscoaseg.getKCoase5() && !datoscoaseg.getKCoase5().equals("")){
								if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase5())){
								ParticipacionEntidad ent5 = new ParticipacionEntidad();
								ent5.setEntidadAceptante(datoscoaseg.getKCoase5());
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
								}else if(i==1){
									bt.setTipoTablaExperiencia("DGS");
								}else{
									bt.setTipoTablaExperiencia("PC");
								}
								bt.setImporteProvision(flujo.getTotprovision().toString());
								
								Tablas tablas = new Tablas();
								List<Tabla> tab = new ArrayList<>();
								Tabla t = new Tabla();
								Tab35012Key t35012 = new Tab35012Key(flujo.getTablacalc1aseg1()); 
								Tab35012 tab35 = tab35012Dao.get(t35012);
								t.setCodigoTabla(tab35.getTablaTirea());
								t.setDesplazamiento("0");
								t.setPjeTabla("100.00");
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
								if(!flujo.getPintertecnI1().equals(BigDecimal.ZERO)){
									Interes inter2 = new Interes();
									inter2.setTipoInteres(flujo.getPintertecnI2().toString());
									String fechaInteresIni2 = sdfechaInteres.format(flujo.getFecIniTramo2());
									inter2.setFechaInicio(fechaInteresIni2);
									String fechaInteresFin2 = sdfechaInteres.format(flujo.getFecFinTramo2());
									inter2.setFechaFin(fechaInteresFin2);
									listInt.add(inter2);
								}
								tiposInteres.setIntereses(listInt);
								bt.setTiposInteres(tiposInteres);
								listBt.add(bt);
							}
							
							basesTecnicas.setBaseTecnica(listBt);
							datosPMA.setBasesTecnicas(basesTecnicas);
							
							Gastos gastos = new Gastos();
							List<Gasto> listGasto = new ArrayList<>();
							Gasto g = new Gasto();
							g.setConcepto("GGE");
							g.setBase("PRIMA");
							g.setPorcentaje(flujo.getPgastgesin2I().toString());
							g.setImporte(flujo.getTotfactgto().toString());
							listGasto.add(g);
							gastos.setGasto(listGasto);
							datosPMA.setGastos(gastos);
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
					}
					movimientosPma.setMovimiento(listMovi);
					StringWriter sw = new StringWriter();
					try {
					    JAXBContext jaxbContext = JAXBContext.newInstance(MovimientosCoaseguro.class);
					    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
					    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
					    jaxbMarshaller.marshal(movimientosPma, sw);
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
				                    			+ " xsi:schemaLocation=" + separador + "http://www.tirea.es/Coaseguro/MovimientosCoaseguro MovimientosCoaseguro_V05-5.xsd"
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
					Iterator<FlujPMaCoa> itPmf = listaFlujPmaCoa.iterator();
					
					MovimientosCoaseguroPMF movimientosPmf =  new MovimientosCoaseguroPMF();

					int indicePmf = 0;
					Long poliza = new Long(0);
					Integer subpoliza = 0;
					Integer nsuscri = 0;
					
					Cabecera cab = new Cabecera();
					
					SimpleDateFormat sdfecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
					
					while (itPmf.hasNext()) {
						
						FlujPMaCoa flujo = itPmf.next();
	
						if(indicePmf == 0){
							
							try {
								
								DatosCoaKey datCoa = new DatosCoaKey(flujo.getKpoliza(),flujo.getKsubpoliza()); 
								DatosCoa datoscoaseg = datosCoaDao.get(datCoa);
								
								movPMF = new MovimientoPMF();
								
								movPMF.setTipoMovimiento(7);
								
								String fechaAux = fecCierre.substring(0,6);
								if(fecCierre.substring(4).equals("01") || fecCierre.substring(4).equals("03") || fecCierre.substring(4).equals("05") 
									|| fecCierre.substring(4).equals("07") || fecCierre.substring(4).equals("08") || fecCierre.substring(4).equals("10") || fecCierre.substring(4).equals("12") ){
									fechaAux = fecCierre.substring(0,4) + "-" + fecCierre.substring(4,6) + "-" + "31";
								}else if(fecCierre.substring(4).equals("02")){
									fechaAux = fecCierre.substring(0,4) + "-" + fecCierre.substring(4,6) + "-" + "28";
								}else{
									fechaAux = fecCierre.substring(0,4) + "-" + fecCierre.substring(4,6) + "-" + "30";
								}
								
								movPMF.setFecha(fechaAux);
								
								pmf = new PMF();
								DatosPoliza datosPoliza = new DatosPoliza();
								
								datosPoliza.setEntidadAbridora(datoscoaseg.getkCoaseOri());
								
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
										ent1.setEntidadAceptante(datoscoaseg.getKCoase1());
										ent1.setParticipacionAceptante(datoscoaseg.getpCoase1().toString());
										partEntidad.add(ent1);
									}
								}
								
								if(null != datoscoaseg.getKCoase2() && !datoscoaseg.getKCoase2().equals("")){
									
									if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase2())){
									ParticipacionEntidad ent2 = new ParticipacionEntidad();
									ent2.setEntidadAceptante(datoscoaseg.getKCoase2());
									ent2.setParticipacionAceptante(datoscoaseg.getpCoase2().toString());
									partEntidad.add(ent2);
									}
								}
								
								if(null != datoscoaseg.getKCoase3() && !datoscoaseg.getKCoase3().equals("")){
									if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase3())){
								
									ParticipacionEntidad ent3 = new ParticipacionEntidad();
									ent3.setEntidadAceptante(datoscoaseg.getKCoase3());
									ent3.setParticipacionAceptante(datoscoaseg.getpCoase3().toString());
									partEntidad.add(ent3);
									}
								}
								
								if(null != datoscoaseg.getKCoase4() && !datoscoaseg.getKCoase4().equals("")){
									if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase4())){
								
									ParticipacionEntidad ent4 = new ParticipacionEntidad();
									ent4.setEntidadAceptante(datoscoaseg.getKCoase4());
									ent4.setParticipacionAceptante(datoscoaseg.getpCoase4().toString());
									partEntidad.add(ent4);
									}
								}
								
								if(null != datoscoaseg.getKCoase5() && !datoscoaseg.getKCoase5().equals("")){
									if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase5())){
									ParticipacionEntidad ent5 = new ParticipacionEntidad();
									ent5.setEntidadAceptante(datoscoaseg.getKCoase5());
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
								
								/////
								
								datosFlujos = new DatosFlujos();
								
								BasesTecnicasPMF basesTecnicasPmf = new BasesTecnicasPMF();
								
								List<BaseTecnicaPMF> listBtPmf = new ArrayList<>();
								
								for(int i = 0; i < 3; i++){
									BaseTecnicaPMF btPmf = new BaseTecnicaPMF();
									if(i==0){
										btPmf.setTipoTablaExperiencia("BT");
									}else if(i==1){
										btPmf.setTipoTablaExperiencia("DGS");
									}else{
										btPmf.setTipoTablaExperiencia("PC");
									}
									btPmf.setTipoInteres(flujo.getPintertecnI1().toString());
									
									if(null == flujo.getTotflujoprobdegastos()){
										btPmf.setImporteFlujo("0.00");
									}else{
										btPmf.setImporteFlujo(flujo.getTotflujoprobdegastos().toString());
									}
									
									if(null == flujo.getTotflujoprobsingastos()){
										btPmf.setImporteFlujoGastos("0.00");
									}else{
										btPmf.setImporteFlujoGastos(flujo.getTotflujoprobsingastos().toString());									
									}
									
									listBtPmf.add(btPmf);
								}
								DatosFlujo datosFlujo = new DatosFlujo();
								if(null == flujo.getFdesde()){
									datosFlujo.setFechaFlujo("");
								}else{
									datosFlujo.setFechaFlujo(flujo.getFdesde().toString());
								}
								
								basesTecnicasPmf.setBaseTecnica(listBtPmf);
								datosFlujo.setBasesTecnicas(basesTecnicasPmf);
								listDatosFlujo.add(datosFlujo);		
								
							} catch (Exception e) {
								Incidencia inci = new Incidencia();
								inci.setFecCierre(fecCierre);
								inci.setCodigoRetorno("11");
								inci.setInfAmpliada("Error al obtener los pesos para la prestación. " + mensaje + " - " + e.getMessage());
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
								}else if(i==1){
									btPmf.setTipoTablaExperiencia("DGS");
								}else{
									btPmf.setTipoTablaExperiencia("PC");
								}
								btPmf.setTipoInteres(flujo.getPintertecnI1().toString());
								btPmf.setImporteFlujo(flujo.getTotflujoprobdegastos().toString());
								btPmf.setImporteFlujo(flujo.getTotflujoprobsingastos().toString());									
								
								listBtPmf.add(btPmf);
							}
							DatosFlujo datosFlujo = new DatosFlujo();
							datosFlujo.setFechaFlujo(flujo.getFdesde().toString());
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
							
							DatosCoaKey datCoa = new DatosCoaKey(flujo.getKpoliza(),flujo.getKsubpoliza()); 
							DatosCoa datoscoaseg = datosCoaDao.get(datCoa);
							
							movPMF = new MovimientoPMF();
							
							movPMF.setTipoMovimiento(7);
							if(fecCierre.substring(4).equals("01") || fecCierre.substring(4).equals("03") || fecCierre.substring(4).equals("05") 
								|| fecCierre.substring(4).equals("07") || fecCierre.substring(4).equals("08") || fecCierre.substring(4).equals("10") || fecCierre.substring(4).equals("12") ){
								movPMF.setFecha(fecCierre + "31");
							}else if(fecCierre.substring(4).equals("02")){
								movPMF.setFecha(fecCierre + "28");
							}else{
								movPMF.setFecha(fecCierre + "30");
							}
							
							pmf = new PMF();
							DatosPoliza datosPoliza = new DatosPoliza();
							
							datosPoliza.setEntidadAbridora(datoscoaseg.getkCoaseOri());
							
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
									ent1.setEntidadAceptante(datoscoaseg.getKCoase1());
									ent1.setParticipacionAceptante(datoscoaseg.getpCoase1().toString());
									partEntidad.add(ent1);
								}
							}
							
							if(null != datoscoaseg.getKCoase2() && !datoscoaseg.getKCoase2().equals("")){
								
								if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase2())){
								ParticipacionEntidad ent2 = new ParticipacionEntidad();
								ent2.setEntidadAceptante(datoscoaseg.getKCoase2());
								ent2.setParticipacionAceptante(datoscoaseg.getpCoase2().toString());
								partEntidad.add(ent2);
								}
							}
							
							if(null != datoscoaseg.getKCoase3() && !datoscoaseg.getKCoase3().equals("")){
								if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase3())){
							
								ParticipacionEntidad ent3 = new ParticipacionEntidad();
								ent3.setEntidadAceptante(datoscoaseg.getKCoase3());
								ent3.setParticipacionAceptante(datoscoaseg.getpCoase3().toString());
								partEntidad.add(ent3);
								}
							}
							
							if(null != datoscoaseg.getKCoase4() && !datoscoaseg.getKCoase4().equals("")){
								if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase4())){
							
								ParticipacionEntidad ent4 = new ParticipacionEntidad();
								ent4.setEntidadAceptante(datoscoaseg.getKCoase4());
								ent4.setParticipacionAceptante(datoscoaseg.getpCoase4().toString());
								partEntidad.add(ent4);
								}
							}
							
							if(null != datoscoaseg.getKCoase5() && !datoscoaseg.getKCoase5().equals("")){
								if(!datoscoaseg.getkCoaseOri().equals(datoscoaseg.getKCoase5())){
								ParticipacionEntidad ent5 = new ParticipacionEntidad();
								ent5.setEntidadAceptante(datoscoaseg.getKCoase5());
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
							
							/////
							
							datosFlujos = new DatosFlujos();
							
							BasesTecnicasPMF basesTecnicasPmf = new BasesTecnicasPMF();
							
							List<BaseTecnicaPMF> listBtPmf = new ArrayList<>();
							
							for(int i = 0; i < 3; i++){
								BaseTecnicaPMF btPmf = new BaseTecnicaPMF();
								if(i==0){
									btPmf.setTipoTablaExperiencia("BT");
								}else if(i==1){
									btPmf.setTipoTablaExperiencia("DGS");
								}else{
									btPmf.setTipoTablaExperiencia("PC");
								}
								btPmf.setTipoInteres(flujo.getPintertecnI1().toString());
								
								if(null == flujo.getTotflujoprobdegastos()){
									btPmf.setImporteFlujo("0.00");
								}else{
									btPmf.setImporteFlujo(flujo.getTotflujoprobdegastos().toString());
								}
								
								if(null == flujo.getTotflujoprobsingastos()){
									btPmf.setImporteFlujoGastos("0.00");
								}else{
									btPmf.setImporteFlujoGastos(flujo.getTotflujoprobsingastos().toString());									
								}
								
								listBtPmf.add(btPmf);
							}
							DatosFlujo datosFlujo = new DatosFlujo();
							if(null == flujo.getFdesde()){
								datosFlujo.setFechaFlujo("");
							}else{
								datosFlujo.setFechaFlujo(flujo.getFdesde().toString());
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
					
					StringWriter sw2 = new StringWriter();
					try {
					    JAXBContext jaxbContext = JAXBContext.newInstance(MovimientosCoaseguroPMF.class);
					    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
					    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

					    jaxbMarshaller.marshal(movimientosPmf, sw2);
					    
					    File file = new File("XML-PMF.XML");
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
				                    	
				                    	String cabeceraEntrada1 = "<?xml version=" + separador + "1.0" + separador + "encoding=" + separador + "UTF-8" + separador + "standalone=" + separador + "yes" + separador + "?>" + "/n";
				                    	String cabeceraEntrada2 = "<MovimientosCoaseguro>" + "/n";
				                    	
				                    	byte[] bufEntrada1 = cabeceraEntrada1.getBytes();
				                        byte[] bufEntrada2 = cabeceraEntrada2.getBytes();
				                        
				                       
				                    	
				                    	String cabeceraXml = "<?xml version=" + separador + "1.0" + separador + " encoding=" + separador + "UTF-8" + separador + "?>" + "\n";
				                    	String cabeceraXsd = "<MovimientosCoaseguro xmlns=" + separador + "http://www.tirea.es/Coaseguro/MovimientosCoaseguro" + separador +  
				                    		" xmlns:xsi=" + separador + "http://www.w3.org/2001/XMLSchema-instance" + separador 
				                    			+ " xsi:schemaLocation=" + separador + "http://www.tirea.es/Coaseguro/MovimientosCoaseguro MovimientosCoaseguro_V05-5.xsd"
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
				inci.setInfAmpliada("Error genérico" + " - " + e.getMessage());
				giGeneral.write(inci);
				giGeneral.cerrarConector();
				System.exit(20);
			}
		
		} catch (Throwable t) {
			GestorIncidenciasGen giGeneral = GestorIncidenciasGen.getInstance();
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("17");
			inci.setInfAmpliada("Error genérico" + " - " + t.getMessage());
			giGeneral.write(inci);
			giGeneral.cerrarConector();
			LoggerManager logGeneral = LoggerManager.getInstance();
			logGeneral.writeLog("Error genérico " + " - " + t.getMessage(), ConstantesSolvencia.LOG_ERROR, true);
			logGeneral.cerrarWriter();
			System.exit(30);
		}	
	}
}
