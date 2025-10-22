package es.mapfre.proxy.prestaciones.modulos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import es.mapfre.proxy.prestaciones.dao.entidades.FlujosRealesDao;
import es.mapfre.proxy.prestaciones.dao.entidades.FlujosRealesSalidaDao;
import es.mapfre.proxy.prestaciones.dao.entidades.PesosBtColDao;
import es.mapfre.proxy.prestaciones.dao.entidades.PesosBtIndDao;
import es.mapfre.proxy.prestaciones.dominio.entidades.FichaProceso;
import es.mapfre.proxy.prestaciones.dominio.entidades.FlujosReales;
import es.mapfre.proxy.prestaciones.dominio.entidades.FlujosRealesSalida;
import es.mapfre.proxy.prestaciones.dominio.entidades.Incidencia;
import es.mapfre.proxy.prestaciones.dominio.entidades.PesosBt;
import es.mapfre.proxy.prestaciones.excepcion.Solvencia2Excepcion;
import es.mapfre.proxy.prestaciones.gestores.GestorFichaProceso;
import es.mapfre.proxy.prestaciones.gestores.GestorFicherosReales;
import es.mapfre.proxy.prestaciones.gestores.GestorIncidenciasGen;
import es.mapfre.proxy.prestaciones.gestores.GestorIncidenciasPres;
import es.mapfre.proxy.prestaciones.utils.CargaDatos;
import es.mapfre.proxy.prestaciones.utils.ConstantesSolvencia;
import es.mapfre.proxy.prestaciones.utils.LoggerManager;

public class PrincipalProxy {
	
	private static Set<Integer> modalidadesEsp = new HashSet<Integer>();
	
	public PrincipalProxy() {
		modalidadesEsp.add(73);
		modalidadesEsp.add(173);
		modalidadesEsp.add(307);
		modalidadesEsp.add(308);
		modalidadesEsp.add(309);
		modalidadesEsp.add(311);
		modalidadesEsp.add(316);
		modalidadesEsp.add(317);
		modalidadesEsp.add(347);
		modalidadesEsp.add(401);
		modalidadesEsp.add(404);
		modalidadesEsp.add(405);
		modalidadesEsp.add(407);
		modalidadesEsp.add(408);
		modalidadesEsp.add(410);
		modalidadesEsp.add(411);
		modalidadesEsp.add(419);
		modalidadesEsp.add(508);
		modalidadesEsp.add(517);
		modalidadesEsp.add(660);
		modalidadesEsp.add(837);
		modalidadesEsp.add(838);
		modalidadesEsp.add(839);
		modalidadesEsp.add(840);
		modalidadesEsp.add(845);
		modalidadesEsp.add(846);
		modalidadesEsp.add(847);
		modalidadesEsp.add(848);
		modalidadesEsp.add(849);
		modalidadesEsp.add(853);
		modalidadesEsp.add(854);
	}

	public static void main(String[] args) {
		
		try {
			//Cargamos las modalidades flexibles que pueden tener mismo número de póliza para distinta modalidad
			new PrincipalProxy();
			CargaDatos cargaDatos = new CargaDatos();
			ModuloProxy modulo = new ModuloProxy();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			// Se crea el gestor de incidencias
			GestorIncidenciasGen giGeneral = GestorIncidenciasGen.getInstance();
			LoggerManager logGeneral = LoggerManager.getInstance();
			logGeneral.writeLog("", "Comienza la ejecución del proceso", ConstantesSolvencia.LOG_INFO, true);
			try{
				cargaDatos.cargaFichaProceso();
				// Obtenemos las fichas a ejecutar
				GestorFichaProceso gfp = GestorFichaProceso.getInstance();
				List<FichaProceso> fichas = gfp.getValues();
				logGeneral.writeLog("", "Se han obtenido " + fichas.size() +  " fichas.", ConstantesSolvencia.LOG_INFO, true);
				Iterator<FichaProceso> it = fichas.iterator();
				// Recorremos las fichas
				while (it.hasNext()) {
					PesosBtIndDao pesosBtIndDao = null;
					PesosBtColDao pesosBtColDao = null;
					FichaProceso ficha = it.next();
					List<FlujosRealesSalida> flujosRealesSalida = new ArrayList<FlujosRealesSalida>();
					String fecCierre = sdf.format(ficha.getFecCierre());
					String canal = ficha.getCcanal().toString();
					logGeneral.writeLog(ficha.getSistema(), "Comienza la ejecución de la ficha con fecha de cierre " + fecCierre +  " y canal " + canal, ConstantesSolvencia.LOG_INFO, true);
					logGeneral.writeLog(ficha.getSistema(), "Cargamos los ficheros de pesos de flujos esperados.", ConstantesSolvencia.LOG_INFO, true);
					// Cargamos los ficheros de pesos
					String fechaPesos = sdf.format(ficha.getFecPesos());
					cargaDatos.cargaPesosBt(fechaPesos, canal, ConstantesSolvencia.INDIVIDUAL);
					cargaDatos.cargaPesosBt(fechaPesos, canal, ConstantesSolvencia.COLECTIVOS);
					pesosBtIndDao = new PesosBtIndDao();
					pesosBtColDao = new PesosBtColDao();
					//Cargamos el fichero de prestaciones reales
					cargaDatos.cargaDatosReales(ficha.getSistema(), fecCierre, canal);
					GestorIncidenciasPres gestorIncidenciasPres = GestorIncidenciasPres.getInstance(ficha.getSistema(), fecCierre, canal);
					FlujosRealesDao flujosRealesDao = new FlujosRealesDao();
					List<FlujosReales> listaReales = flujosRealesDao.getValues();
					logGeneral.writeLog(ficha.getSistema(), "Se han obtenido " + listaReales.size() + " prestaciones reales.", ConstantesSolvencia.LOG_INFO, true);
					Iterator<FlujosReales> itReales = listaReales.iterator();
					String mensaje = "";
					//Por cada prestación
					while (itReales.hasNext()) {
						FlujosReales flujo = itReales.next();
						List<PesosBt> pesosBt = new ArrayList<PesosBt>();
						try {
							//Tratamos el número de póliza para transformarlo al formato Cloud
							String numPolizaReal = flujo.getNumPoliza();
							if (ficha.getSistema().equals(ConstantesSolvencia.AS400)) {
								String polizaAS400 = numPolizaReal.substring(4,12);
								String certificadoAS400 = numPolizaReal.substring(13,25);
								numPolizaReal = polizaAS400 + certificadoAS400;
							} else if (ficha.getSistema().equals(ConstantesSolvencia.ASEVAL)) {
								String polizann = numPolizaReal.substring(0,2);
								String polizsec = numPolizaReal.substring(2,8);
								String certipol = numPolizaReal.substring(8,12);
								String certiann = numPolizaReal.substring(12,16);
								String certisec = numPolizaReal.substring(16,22);
								numPolizaReal = polizann + polizsec + certipol + certiann.substring(2) + certisec;
							} 
							String poliza = numPolizaReal.substring(0, 10);
							if (flujo.getNumPoliza().length() <= 10) {
								//Sólo viene informado el número de póliza
								mensaje = "[sistema=" + ficha.getSistema() + "; fecha de cierre=" + fechaPesos + "; poliza=" + poliza + "; modalidad=" + flujo.getCodRamo() + "]";
								logGeneral.writeLog(ficha.getSistema(), "Buscamos los pesos " + mensaje, ConstantesSolvencia.LOG_INFO, false);
								//Comprobamos si la modalidad es flexible para incluirla en la búsqueda 
								//Buscamos en el fichero de pesos de individuales.
								pesosBt = pesosBtIndDao.getPesosBt(flujo.getCodRamo(), poliza);
								//Si no se encuentra en individuales, buscamos en colectivos 
								if (pesosBt.size() == 0) {
									pesosBt = pesosBtColDao.getPesosBt(flujo.getCodRamo(), poliza);
								}
							} else {
								//Vienen informados la subpóliza y el certificado 
								String subpoliza = numPolizaReal.substring(10, 14);
								if (modalidadesEsp.contains(Integer.valueOf(flujo.getCodRamo()))) {
									// Hacemos el tratamiento de la subpóliza para pólizas flexibles
									// Si subpóliza = 0001 o subpóliza = 1000
					                // 		subpóliza = 0000
					                // Si subpóliza > 1000
					                // 		subpóliza = subpóliza - 1000 
									if (Integer.valueOf(subpoliza) > 1000) {
										subpoliza = StringUtils.leftPad(String.valueOf(Integer.valueOf(subpoliza) - 1000), 4, "0");
									} else if (Integer.valueOf(subpoliza) == 1000 || Integer.valueOf(subpoliza) == 1) {
										subpoliza = "0000";
									}
								}
								String certificado = numPolizaReal.substring(14, 20);
								mensaje = "[sistema=" + ficha.getSistema() + "; fecha de cierre=" + fechaPesos + "; poliza=" + poliza + "; subpóliza=" + subpoliza + 
										"; certificado=" + certificado + "; modalidad=" + flujo.getCodRamo() + "]";
								logGeneral.writeLog(ficha.getSistema(), "Buscamos los pesos " + mensaje, ConstantesSolvencia.LOG_INFO, false);
								//Comprobamos si la modalidad es flexible para incluirla en la búsqueda 
								//Buscamos en el fichero de pesos de colectivos.
								pesosBt = pesosBtColDao.getPesosBtMod(flujo.getCodRamo(), poliza, subpoliza, certificado);
								//Si no se encuentra en colectivos, buscamos en individuales 
								if (pesosBt.size() == 0) {
									pesosBt = pesosBtIndDao.getPesosBtMod(flujo.getCodRamo(), poliza, subpoliza, certificado);
								}
							}
							logGeneral.writeLog(ficha.getSistema(), "Se han obtenido " + pesosBt.size() + " registros de pesos.", ConstantesSolvencia.LOG_INFO, false);
							if (pesosBt.size() == 0) {
								logGeneral.writeLog(ficha.getSistema(), "No se han encontrado pesos para la prestación. " + mensaje, ConstantesSolvencia.LOG_ERROR, false);
								Incidencia inci = new Incidencia();
								inci.setFecCierre(fecCierre);
								inci.setCodigoRetorno("11");
								inci.setInfAmpliada("No se han encontrado pesos para la prestación. " + mensaje);
								gestorIncidenciasPres.write(inci);
								giGeneral.write(inci);
							}
							
							try {
								//Llamamos al módulo para desglosar la prestación conforme a los pesos obtenidos 
								List<FlujosRealesSalida> frs = modulo.execute(ficha.getSistema(), pesosBt, flujo);
								//Añadimos las prestaciones generadas a la lista de salida
								flujosRealesSalida.addAll(frs);
							} catch (Exception e) {
								logGeneral.writeLog(ficha.getSistema(), "Error en el desglose de la prestación. " + mensaje + " - " + e.getMessage(), ConstantesSolvencia.LOG_ERROR, true);
								Incidencia inci = new Incidencia();
								inci.setFecCierre(fecCierre);
								inci.setCodigoRetorno("12");
								inci.setInfAmpliada("Error en el desglose de la prestación. " + mensaje + " - " + e.getMessage());
								gestorIncidenciasPres.write(inci);
								giGeneral.write(inci);
								System.exit(12);
							}
						} catch (Exception e) {
							logGeneral.writeLog(ficha.getSistema(), "Error al obtener los pesos para la prestación. " + mensaje + " - " + e.getMessage(), ConstantesSolvencia.LOG_ERROR, true);
							Incidencia inci = new Incidencia();
							inci.setFecCierre(fecCierre);
							inci.setCodigoRetorno("11");
							inci.setInfAmpliada("Error al obtener los pesos para la prestación. " + mensaje + " - " + e.getMessage());
							gestorIncidenciasPres.write(inci);
							giGeneral.write(inci);
						}
					}
					logGeneral.writeLog(ficha.getSistema(), "Se han obtenido " + flujosRealesSalida.size() + " registros de prestaciones desglosadas para el sistema " + ficha.getSistema(), ConstantesSolvencia.LOG_INFO, true);
					//Creamos el fichero de salida
					GestorFicherosReales gestorReales = GestorFicherosReales.getInstance(ficha.getSistema(), fecCierre, canal);
					//Escribimos las prestaciones en el fichero de salida
					FlujosRealesSalidaDao dao = new FlujosRealesSalidaDao();
					Collections.sort(flujosRealesSalida, dao.prestOrdered());
					for (FlujosRealesSalida flujoSalida : flujosRealesSalida) {
						gestorReales.write(flujoSalida);
					}
					flujosRealesDao.clear();
					gestorReales.cerrarConectores();
					Incidencia inci = gestorReales.copyToSSAA(ficha.getSistema(), fecCierre);
					if (null != inci) {
						logGeneral.writeLog(ficha.getSistema(), "Error al copiar el fichero a Sistemas Abiertos - " + inci.getInfAmpliada(), ConstantesSolvencia.LOG_ERROR, true);
						inci.setFecCierre(fecCierre);
						gestorIncidenciasPres.write(inci);
						giGeneral.write(inci);
					}
					// Limpiamos las cachés para ejecutar la siguiente ficha
					if (null != pesosBtIndDao) {
						pesosBtIndDao.clear();
					}
					if (null != pesosBtColDao) {
						pesosBtColDao.clear();
					}
					gestorIncidenciasPres.cerrarConector();
				}
				logGeneral.cerrarWriter();
				giGeneral.cerrarConector();
				System.exit(0);
				
			} catch(Solvencia2Excepcion solvExc){
				logGeneral.writeLog("", "Error genérico" + " - " + solvExc.getIncidencia().getInfAmpliada(), ConstantesSolvencia.LOG_ERROR, true);
				logGeneral.cerrarWriter();
				Incidencia inci = solvExc.getIncidencia();
				giGeneral.write(inci);
				giGeneral.cerrarConector();
				System.exit(10);
			} catch (Exception e) {
				logGeneral.writeLog("", "Error genérico" + " - " + e.getMessage(), ConstantesSolvencia.LOG_ERROR, true);
				logGeneral.cerrarWriter();
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
			logGeneral.writeLog("", "Error genérico " + " - " + t.getMessage(), ConstantesSolvencia.LOG_ERROR, true);
			logGeneral.cerrarWriter();
			System.exit(30);
		}	
	}
}
