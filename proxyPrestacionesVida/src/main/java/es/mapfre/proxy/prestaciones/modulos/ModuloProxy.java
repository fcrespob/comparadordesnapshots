package es.mapfre.proxy.prestaciones.modulos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import es.mapfre.proxy.prestaciones.dominio.entidades.FlujosReales;
import es.mapfre.proxy.prestaciones.dominio.entidades.FlujosRealesSalida;
import es.mapfre.proxy.prestaciones.dominio.entidades.PesosBt;
import es.mapfre.proxy.prestaciones.utils.ConstantesSolvencia;
import es.mapfre.proxy.prestaciones.utils.LoggerManager;

public class ModuloProxy {

	public List<FlujosRealesSalida> execute(String sistema, List<PesosBt> pesosBt, FlujosReales flujo) throws Exception {

		List<FlujosRealesSalida> salida = new ArrayList<FlujosRealesSalida>();
		BigDecimal sumImportePago = BigDecimal.ZERO, sumValTotalReservaMes = BigDecimal.ZERO, 
				sumPagoAnual = BigDecimal.ZERO, sumImporteValoracion = BigDecimal.ZERO, 
				sumPorcentaje = BigDecimal.ZERO;
		String pctDesglose;
		LoggerManager logGeneral = LoggerManager.getInstance();
		if (pesosBt.size() > 0) {
			logGeneral.writeLog(sistema, "Comienza el desglose de las prestaciones.", ConstantesSolvencia.LOG_INFO, false);
			for (int i = 0; i < pesosBt.size();i++) {
				logGeneral.writeLog(sistema, "Peso de la suscripción " + pesosBt.get(i).getPesosUoA(), ConstantesSolvencia.LOG_INFO, false);
				FlujosRealesSalida flujoSalida = transformaRegistroSalida(flujo, i + 1);
				flujoSalida.setCohorte(pesosBt.get(i).getCohorte());
				flujoSalida.setCodLobBis(pesosBt.get(i).getSwcasado());
				if (i < pesosBt.size() - 1) {
					sumPorcentaje = sumPorcentaje.add(pesosBt.get(i).getPesosUoA());
					if (pesosBt.get(i).getPesosUoA().compareTo(BigDecimal.ZERO) != -1) {
						pctDesglose = "+" + StringUtils.leftPad(pesosBt.get(i).getPesosUoA().setScale(2).toString().replace(".", ","), 8, "0");
					} else {
						pctDesglose = "-" + StringUtils.leftPad(pesosBt.get(i).getPesosUoA().setScale(2).toString().replace(".", ",").replace("-", ""), 8, "0");
					}
					flujoSalida.setPctDesglose(pctDesglose);
					
					BigDecimal importePagoBD = flujo.getImportePago().multiply(pesosBt.get(i).getPesosUoA()).divide(ConstantesSolvencia.BIG_CIEN).setScale(2, RoundingMode.HALF_UP);
					String importePago = importePagoBD.setScale(2).toString().replace(".", "").replace("-", "");
					String signoImportePago = importePagoBD.compareTo(BigDecimal.ZERO) < 0 ? "-" : "+";
					sumImportePago = sumImportePago.add(importePagoBD);
					flujoSalida.setImportePago(signoImportePago + StringUtils.leftPad(importePago,14,"0"));
					
					BigDecimal valTotalReservaMesBD = flujo.getValTotalReservaMes().multiply(pesosBt.get(i).getPesosUoA()).divide(ConstantesSolvencia.BIG_CIEN).setScale(2, RoundingMode.HALF_UP);
					String valTotalReservaMes = valTotalReservaMesBD.setScale(2).toString().replace(".", "").replace("-", "");
					String signoValTotalReservaMes = valTotalReservaMesBD.compareTo(BigDecimal.ZERO) < 0 ? "-" : "+";
					sumValTotalReservaMes = sumValTotalReservaMes.add(valTotalReservaMesBD);
					flujoSalida.setValTotalReservaMes(signoValTotalReservaMes + StringUtils.leftPad(valTotalReservaMes,14,"0"));
					
					BigDecimal pagoAnualBD = flujo.getPagoAnual().multiply(pesosBt.get(i).getPesosUoA()).divide(ConstantesSolvencia.BIG_CIEN).setScale(2, RoundingMode.HALF_UP);
					String pagoAnual = pagoAnualBD.setScale(2).toString().replace(".", "").replace("-", "");
					String signoPagoAnual = pagoAnualBD.compareTo(BigDecimal.ZERO) < 0 ? "-" : "+";
					sumPagoAnual = sumPagoAnual.add(pagoAnualBD);
					flujoSalida.setPagoAnual(signoPagoAnual + StringUtils.leftPad(pagoAnual,14,"0"));
					
					BigDecimal importeValoracionBD = flujo.getImporteValoracion().multiply(pesosBt.get(i).getPesosUoA()).divide(ConstantesSolvencia.BIG_CIEN).setScale(2, RoundingMode.HALF_UP);
					String importeValoracion = importeValoracionBD.setScale(2).toString().replace(".", "").replace("-", "");
					String signoImporteValoracion = importeValoracionBD.compareTo(BigDecimal.ZERO) < 0 ? "-" : "+";
					sumImporteValoracion = sumImporteValoracion.add(importeValoracionBD);
					flujoSalida.setImporteValoracion(signoImporteValoracion + StringUtils.leftPad(importeValoracion,14,"0"));
				} else {
					BigDecimal pct = ConstantesSolvencia.BIG_CIEN.subtract(sumPorcentaje).setScale(2);
					if (pct.compareTo(BigDecimal.ZERO) != -1) {
						pctDesglose = "+" + StringUtils.leftPad(pct.toString().replace(".", ","), 8, "0");
					} else {
						pctDesglose = "-" + StringUtils.leftPad(pct.toString().replace(".", ",").replace("-", ""), 8, "0");
					}
					flujoSalida.setPctDesglose(pctDesglose);
					
					BigDecimal importePagoBD = flujo.getImportePago().subtract(sumImportePago);
					sumImportePago = importePagoBD.add(sumImportePago);
					String importePago = importePagoBD.setScale(2).toString().replace(".", "").replace("-", "");
					String signoImportePago = importePagoBD.compareTo(BigDecimal.ZERO) < 0 ? "-" : "+";
					flujoSalida.setImportePago(signoImportePago + StringUtils.leftPad(importePago,14,"0"));
					
					BigDecimal valTotalReservaMesBD = flujo.getValTotalReservaMes().subtract(sumValTotalReservaMes);
					sumValTotalReservaMes = valTotalReservaMesBD.add(sumValTotalReservaMes);
					String valTotalReservaMes = valTotalReservaMesBD.setScale(2).toString().replace(".", "").replace("-", "");
					String signoValTotalReservaMes = valTotalReservaMesBD.compareTo(BigDecimal.ZERO) < 0 ? "-" : "+";
					flujoSalida.setValTotalReservaMes(signoValTotalReservaMes + StringUtils.leftPad(valTotalReservaMes,14,"0"));
					
					BigDecimal pagoAnualBD = flujo.getPagoAnual().subtract(sumPagoAnual);
					sumPagoAnual = pagoAnualBD.add(sumPagoAnual);
					String pagoAnual = pagoAnualBD.setScale(2).toString().replace(".", "").replace("-", "");
					String signoPagoAnual = pagoAnualBD.compareTo(BigDecimal.ZERO) < 0 ? "-" : "+";
					flujoSalida.setPagoAnual(signoPagoAnual + StringUtils.leftPad(pagoAnual,14,"0"));
					
					BigDecimal importeValoracionBD = flujo.getImporteValoracion().subtract(sumImporteValoracion);
					sumImporteValoracion = importeValoracionBD.add(sumImporteValoracion);
					String importeValoracion = importeValoracionBD.setScale(2).toString().replace(".", "").replace("-", "");
					String signoImporteValoracion = importeValoracionBD.compareTo(BigDecimal.ZERO) < 0 ? "-" : "+";
					flujoSalida.setImporteValoracion(signoImporteValoracion + StringUtils.leftPad(importeValoracion,14,"0"));

					if (sumImportePago.compareTo(flujo.getImportePago()) != 0 ||
							sumValTotalReservaMes.compareTo(flujo.getValTotalReservaMes()) != 0 ||
							sumPagoAnual.compareTo(flujo.getPagoAnual()) != 0 ||
							sumImporteValoracion.compareTo(flujo.getImporteValoracion()) != 0){
						throw new Exception("La suma de los desgloses no coincide con el importe total.");
					}
				}
				salida.add(flujoSalida);
			}
			logGeneral.writeLog(sistema, "La prestación se ha desglosado en " + salida.size() + " registros.", ConstantesSolvencia.LOG_INFO, false);
		} else {
			logGeneral.writeLog(sistema, "La prestación se mostrará al 100% debido a que no se encuentran pesos.", ConstantesSolvencia.LOG_INFO, false);
			FlujosRealesSalida flujoSalida = transformaRegistroSalida(flujo, 0);
			flujoSalida.setPctDesglose("");
			flujoSalida.setCodLobBis("N");
			salida.add(flujoSalida);
		}
		
		return salida;
	}

	private FlujosRealesSalida transformaRegistroSalida(FlujosReales flujo, Integer secuencia) {
		FlujosRealesSalida flujoSalida = new FlujosRealesSalida();
		//Textos
		flujoSalida.setNumExp(flujo.getNumExp() == null ? "" : flujo.getNumExp());
		flujoSalida.setAnioMes(flujo.getAnioMes() == null ? "" : flujo.getAnioMes().toString());
		flujoSalida.setCodProductor(flujo.getCodProductor() == null ? "" : flujo.getCodProductor());
		flujoSalida.setClaseProductor(flujo.getClaseProductor() == null ? "" : flujo.getClaseProductor());
		flujoSalida.setClaseMediador(flujo.getClaseMediador() == null ? "" : flujo.getClaseMediador());
		flujoSalida.setTipDocum(flujo.getTipDocum() == null ? "" : flujo.getTipDocum());
		flujoSalida.setCodDocum(flujo.getCodDocum() == null ? "" : flujo.getCodDocum());
		flujoSalida.setOfDirecta(flujo.getOfDirecta() == null ? "" : flujo.getOfDirecta());
		flujoSalida.setCodTerceroAgt(flujo.getCodTerceroAgt() == null ? "" : flujo.getCodTerceroAgt());
		flujoSalida.setTipExp(flujo.getTipExp() == null ? "" : flujo.getTipExp());
		flujoSalida.setCodTipcGrpTipExp(flujo.getCodTipcGrpTipExp() == null ? "" : flujo.getCodTipcGrpTipExp());
		flujoSalida.setCodMon(flujo.getCodMon() == null ? "" : flujo.getCodMon());
		flujoSalida.setClaseExp(flujo.getClaseExp() == null ? "" : flujo.getClaseExp());
		flujoSalida.setNumPoliza(flujo.getNumPoliza() == null ? "" : flujo.getNumPoliza());
		flujoSalida.setCodRamo(flujo.getCodRamo() == null ? "" : flujo.getCodRamo());
		flujoSalida.setTipCoa(flujo.getTipCoa() == null ? "" : flujo.getTipCoa());
		flujoSalida.setTipRea(flujo.getTipRea() == null ? "" : flujo.getTipRea());
		flujoSalida.setPctCoa(flujo.getPctCoa() == null ? "" : flujo.getPctCoa());
		flujoSalida.setTipoMov(flujo.getTipoMov() == null ? "" : flujo.getTipoMov());
		flujoSalida.setTipoOper(flujo.getTipoOper() == null ? "" : flujo.getTipoOper());
		flujoSalida.setCodMonPago(flujo.getCodMonPago() == null ? "" : flujo.getCodMonPago());
		flujoSalida.setCodLobBis(flujo.getCodLobBis() == null ? "" : flujo.getCodLobBis());
		flujoSalida.setSistema(flujo.getSistema() == null ? "" : flujo.getSistema());
		flujoSalida.setMcaVigente(flujo.getMcaVigente() == null ? "" : flujo.getMcaVigente());
		flujoSalida.setPctDesglose(flujo.getPctDesglose() == null ? "" : flujo.getPctDesglose());
		
		//Fechas
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		flujoSalida.setFecMovExp(flujo.getFecMovExp() == null ? "00000000" : sdf.format(flujo.getFecMovExp()));
		flujoSalida.setFecOcurSini(flujo.getFecOcurSini() == null ? "00000000" : sdf.format(flujo.getFecOcurSini()));
		flujoSalida.setFecApertExp(flujo.getFecApertExp() == null ? "00000000" : sdf.format(flujo.getFecApertExp()));
		flujoSalida.setFecReapertExp(flujo.getFecReapertExp() == null ? "00000000" : sdf.format(flujo.getFecReapertExp()));
		flujoSalida.setFecTermExp(flujo.getFecTermExp() == null ? "00000000" : sdf.format(flujo.getFecTermExp()));
		flujoSalida.setFecMovEco(flujo.getFecMovEco() == null ? "00000000" : sdf.format(flujo.getFecMovEco()));
		flujoSalida.setFecEfectoIni(flujo.getFecEfectoIni() == null ? "" : sdf.format(flujo.getFecEfectoIni()));
		flujoSalida.setFecEfectoSpto(flujo.getFecEfectoSpto() == null ? "" : sdf.format(flujo.getFecEfectoSpto()));
		flujoSalida.setFecSuscripcion(flujo.getFecSuscripcion() == null ? "" : sdf.format(flujo.getFecSuscripcion()));
		flujoSalida.setFechaEstado(flujo.getFechaEstado() == null ? "00000000" : sdf.format(flujo.getFechaEstado()));
		
		//Números
		String numMvto = flujo.getNumMvto() == null ? "" : flujo.getNumMvto().toString();
		flujoSalida.setNumMvto(StringUtils.leftPad(numMvto, 4, "0") + StringUtils.leftPad(String.valueOf(secuencia), 2, "0"));
		String codCia = flujo.getCodCia() == null ? "" : flujo.getCodCia().toString();
		flujoSalida.setCodCia(StringUtils.leftPad(codCia, 2, "0"));
		String codCob = flujo.getCodCob() == null ? "" : flujo.getCodCob().toString();
		flujoSalida.setCodCob(StringUtils.leftPad(codCob, 8, "0"));
		String codCtoRva = flujo.getCodCtoRva() == null ? "" : flujo.getCodCtoRva().toString();
		flujoSalida.setCodCtoRva(StringUtils.leftPad(codCtoRva, 10, "0"));
		String numApli = flujo.getNumApli() == null ? "" : flujo.getNumApli().toString();
		flujoSalida.setNumApli(StringUtils.leftPad(numApli, 5, "0"));
		String numRiesgo = flujo.getNumRiesgo() == null ? "" : flujo.getNumRiesgo().toString();
		flujoSalida.setNumRiesgo(StringUtils.leftPad(numRiesgo, 6, "0"));
		String modalidad = flujo.getModalidad() == null ? "" : flujo.getModalidad().toString();
		flujoSalida.setModalidad(StringUtils.leftPad(modalidad, 6, "0"));
		String codSector = flujo.getCodSector() == null ? "" : flujo.getCodSector().toString();
		flujoSalida.setCodSector(StringUtils.leftPad(codSector, 4, "0"));
		String claveIncCorr = flujo.getClaveIncCorr() == null ? "" : flujo.getClaveIncCorr().toString();
		flujoSalida.setClaveIncCorr(StringUtils.leftPad(claveIncCorr, 12, "0"));
		flujoSalida.setCohorte(flujo.getCohorte() == null ? "" : flujo.getCohorte().toString());
		
		//Importes
		String valTotalReservaMes = flujo.getValTotalReservaMes() == null ? "" : flujo.getValTotalReservaMes().setScale(2).toString().replace(".", "").replace("-", "");
		String signoValTotalReservaMes = flujo.getValTotalReservaMes() == null ? "+" : flujo.getValTotalReservaMes().compareTo(BigDecimal.ZERO) < 0 ? "-" : "+";
		flujoSalida.setValTotalReservaMes(signoValTotalReservaMes + StringUtils.leftPad(valTotalReservaMes,14,"0"));
		String pagoAnual = flujo.getPagoAnual() == null ? "" : flujo.getPagoAnual().setScale(2).toString().replace(".", "").replace("-", "");
		String signoPagoAnual = flujo.getPagoAnual() == null ? "+" : flujo.getPagoAnual().compareTo(BigDecimal.ZERO) < 0 ? "-" : "+";
		flujoSalida.setPagoAnual(signoPagoAnual + StringUtils.leftPad(pagoAnual,14,"0"));
		String importeValoracion = flujo.getImporteValoracion() == null ? "" : flujo.getImporteValoracion().setScale(2).toString().replace(".", "").replace("-", "");
		String signoImporteValoracion = flujo.getImporteValoracion() == null ? "+" : flujo.getImporteValoracion().compareTo(BigDecimal.ZERO) < 0 ? "-" : "+";
		flujoSalida.setImporteValoracion(signoImporteValoracion + StringUtils.leftPad(importeValoracion,14,"0"));
		String importePago = flujo.getImportePago() == null ? "" : flujo.getImportePago().setScale(2).toString().replace(".", "").replace("-", "");
		String signoImportePago = flujo.getImportePago() == null ? "+" : flujo.getImportePago().compareTo(BigDecimal.ZERO) < 0 ? "-" : "+";
		flujoSalida.setImportePago(signoImportePago + StringUtils.leftPad(importePago,14,"0"));
		
		return flujoSalida;
	}
}