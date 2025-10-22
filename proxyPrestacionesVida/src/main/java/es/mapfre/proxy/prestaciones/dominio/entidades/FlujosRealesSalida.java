package es.mapfre.proxy.prestaciones.dominio.entidades;

import es.mapfre.proxy.prestaciones.dominio.EntidadBase;
import es.mapfre.proxy.prestaciones.dominio.keys.FlujosRealesSalidaKey;

public class FlujosRealesSalida implements EntidadBase<FlujosRealesSalidaKey>{

	public static final int IND_CODCIA = 0;
	public static final int IND_NUMEXP = 1;
	public static final int IND_ANIOMES = 2;
	public static final int IND_NUMMVTO = 3;
	public static final int IND_CODCOB = 4;
	public static final int IND_CODCTORVA = 5;
	public static final int IND_CODPRODUCTOR = 6;
	public static final int IND_CLASEPRODUCTOR = 7;
	public static final int IND_CLASEMEDIADOR = 8;
	public static final int IND_TIPDOCUM = 9;
	public static final int IND_CODDOCUM = 10;
	public static final int IND_OFDIRECTA = 11;
	public static final int IND_CODTERCEROAGT = 12;
	public static final int IND_FECMOVEXP = 13;
	public static final int IND_TIPEXP = 14;
	public static final int IND_CODTIPCGRPTIPEXP = 15;
	public static final int IND_FECOCURSINI = 16;
	public static final int IND_FECAPERTEXP = 17;
	public static final int IND_REAPERTEXP = 18;
	public static final int IND_TERMEXP = 19;
	public static final int IND_CODMON = 20;
	public static final int IND_CLASEEXP = 21;
	public static final int IND_NUMPOLIZA = 22;
	public static final int IND_NUMAPLI = 23;
	public static final int IND_NUMRIESGO = 24;
	public static final int IND_MODALIDAD = 25;
	public static final int IND_CODRAMO = 26;
	public static final int IND_CODSECTOR = 27;
	public static final int IND_TIPCOA = 28;
	public static final int IND_TIPREA = 29;
	public static final int IND_PCTCOA = 30;
	public static final int IND_TIPOMOV = 31;
	public static final int IND_TIPOOPER = 32;
	public static final int IND_VALTITALRESERVAMES = 33;
	public static final int IND_PAGOANUAL = 34;
	public static final int IND_IMPORTEVALORACION = 35;
	public static final int IND_IMPORTEPAGO = 36;
	public static final int IND_FECMOVECO = 37;
	public static final int IND_CODMONPAGO = 38;
	public static final int IND_COHORTE = 39;
	public static final int IND_FECEFECTOINI = 40;
	public static final int IND_FECEFECTOSPTO = 41;
	public static final int IND_FECSUSCRIPCION = 42;
	public static final int IND_CODLOBBIS = 43;
	public static final int IND_PCTDESGLOSE = 44;
	public static final int IND_SISTEMA = 45;
	public static final int IND_CLAVEINCCORR = 46;
	public static final int IND_FECHAESTADO = 47;
	public static final int IND_MCAVIGENTE = 48;
	
	private String codCia;
	private String numExp;
	private String anioMes;
	private String numMvto;
	private String codCob;
	private String codCtoRva;
	private String codProductor;
	private String claseProductor;
	private String claseMediador;
	private String tipDocum;
	private String codDocum;
	private String ofDirecta;
	private String codTerceroAgt;
	private String fecMovExp;
	private String tipExp;
	private String codTipcGrpTipExp;
	private String fecOcurSini;
	private String fecApertExp;
	private String fecReapertExp;
	private String fecTermExp;
	private String codMon;
	private String claseExp;
	private String numPoliza;
	private String numApli;
	private String numRiesgo;
	private String modalidad;
	private String codRamo;
	private String codSector;
	private String tipCoa;
	private String tipRea;
	private String pctCoa;
	private String tipoMov;
	private String tipoOper;
	private String valTotalReservaMes;
	private String pagoAnual;
	private String importeValoracion;
	private String importePago;
	private String fecMovEco;
	private String codMonPago;
	private String cohorte;
	private String fecEfectoIni;
	private String fecEfectoSpto;
	private String fecSuscripcion;
	private String codLobBis;
	private String pctDesglose;
	private String sistema;
	private String claveIncCorr;
	private String fechaEstado;
	private String mcaVigente;

	@Override
	public FlujosRealesSalidaKey getKey() {
		return new FlujosRealesSalidaKey(codCia, numExp, anioMes, numMvto, codCob, codCtoRva);	
	}
	
	public String getCodCia() {
		return codCia;
	}
	public void setCodCia(String codCia) {
		this.codCia = codCia;
	}
	public String getNumExp() {
		return numExp;
	}
	public void setNumExp(String numExp) {
		this.numExp = numExp;
	}
	public String getAnioMes() {
		return anioMes;
	}
	public void setAnioMes(String anioMes) {
		this.anioMes = anioMes;
	}
	public String getNumMvto() {
		return numMvto;
	}
	public void setNumMvto(String numMvto) {
		this.numMvto = numMvto;
	}
	public String getCodCob() {
		return codCob;
	}
	public void setCodCob(String codCob) {
		this.codCob = codCob;
	}
	public String getCodCtoRva() {
		return codCtoRva;
	}
	public void setCodCtoRva(String codCtoRva) {
		this.codCtoRva = codCtoRva;
	}
	public String getCodProductor() {
		return codProductor;
	}
	public void setCodProductor(String codProductor) {
		this.codProductor = codProductor;
	}
	public String getClaseProductor() {
		return claseProductor;
	}
	public void setClaseProductor(String claseProductor) {
		this.claseProductor = claseProductor;
	}
	public String getClaseMediador() {
		return claseMediador;
	}
	public void setClaseMediador(String claseMediador) {
		this.claseMediador = claseMediador;
	}
	public String getTipDocum() {
		return tipDocum;
	}
	public void setTipDocum(String tipDocum) {
		this.tipDocum = tipDocum;
	}
	public String getCodDocum() {
		return codDocum;
	}
	public void setCodDocum(String codDocum) {
		this.codDocum = codDocum;
	}
	public String getOfDirecta() {
		return ofDirecta;
	}
	public void setOfDirecta(String ofDirecta) {
		this.ofDirecta = ofDirecta;
	}
	public String getCodTerceroAgt() {
		return codTerceroAgt;
	}
	public void setCodTerceroAgt(String codTerceroAgt) {
		this.codTerceroAgt = codTerceroAgt;
	}
	public String getFecMovExp() {
		return fecMovExp;
	}
	public void setFecMovExp(String fecMovExp) {
		this.fecMovExp = fecMovExp;
	}
	public String getTipExp() {
		return tipExp;
	}
	public void setTipExp(String tipExp) {
		this.tipExp = tipExp;
	}
	public String getCodTipcGrpTipExp() {
		return codTipcGrpTipExp;
	}
	public void setCodTipcGrpTipExp(String codTipcGrpTipExp) {
		this.codTipcGrpTipExp = codTipcGrpTipExp;
	}
	public String getFecOcurSini() {
		return fecOcurSini;
	}
	public void setFecOcurSini(String fecOcurSini) {
		this.fecOcurSini = fecOcurSini;
	}
	public String getFecApertExp() {
		return fecApertExp;
	}
	public void setFecApertExp(String fecApertExp) {
		this.fecApertExp = fecApertExp;
	}
	public String getFecReapertExp() {
		return fecReapertExp;
	}
	public void setFecReapertExp(String fecReapertExp) {
		this.fecReapertExp = fecReapertExp;
	}
	public String getFecTermExp() {
		return fecTermExp;
	}
	public void setFecTermExp(String fecTermExp) {
		this.fecTermExp = fecTermExp;
	}
	public String getCodMon() {
		return codMon;
	}
	public void setCodMon(String codMon) {
		this.codMon = codMon;
	}
	public String getClaseExp() {
		return claseExp;
	}
	public void setClaseExp(String claseExp) {
		this.claseExp = claseExp;
	}
	public String getNumPoliza() {
		return numPoliza;
	}
	public void setNumPoliza(String numPoliza) {
		this.numPoliza = numPoliza;
	}
	public String getNumApli() {
		return numApli;
	}
	public void setNumApli(String numApli) {
		this.numApli = numApli;
	}
	public String getNumRiesgo() {
		return numRiesgo;
	}
	public void setNumRiesgo(String numRiesgo) {
		this.numRiesgo = numRiesgo;
	}
	public String getModalidad() {
		return modalidad;
	}
	public void setModalidad(String modalidad) {
		this.modalidad = modalidad;
	}
	public String getCodRamo() {
		return codRamo;
	}
	public void setCodRamo(String codRamo) {
		this.codRamo = codRamo;
	}
	public String getCodSector() {
		return codSector;
	}
	public void setCodSector(String codSector) {
		this.codSector = codSector;
	}
	public String getTipCoa() {
		return tipCoa;
	}
	public void setTipCoa(String tipCoa) {
		this.tipCoa = tipCoa;
	}
	public String getTipRea() {
		return tipRea;
	}
	public void setTipRea(String tipRea) {
		this.tipRea = tipRea;
	}
	public String getPctCoa() {
		return pctCoa;
	}
	public void setPctCoa(String pctCoa) {
		this.pctCoa = pctCoa;
	}
	public String getTipoMov() {
		return tipoMov;
	}
	public void setTipoMov(String tipoMov) {
		this.tipoMov = tipoMov;
	}
	public String getTipoOper() {
		return tipoOper;
	}
	public void setTipoOper(String tipoOper) {
		this.tipoOper = tipoOper;
	}
	public String getValTotalReservaMes() {
		return valTotalReservaMes;
	}
	public void setValTotalReservaMes(String valTotalReservaMes) {
		this.valTotalReservaMes = valTotalReservaMes;
	}
	public String getPagoAnual() {
		return pagoAnual;
	}
	public void setPagoAnual(String pagoAnual) {
		this.pagoAnual = pagoAnual;
	}
	public String getImporteValoracion() {
		return importeValoracion;
	}
	public void setImporteValoracion(String importeValoracion) {
		this.importeValoracion = importeValoracion;
	}
	public String getImportePago() {
		return importePago;
	}
	public void setImportePago(String importePago) {
		this.importePago = importePago;
	}
	public String getFecMovEco() {
		return fecMovEco;
	}
	public void setFecMovEco(String fecMovEco) {
		this.fecMovEco = fecMovEco;
	}
	public String getCodMonPago() {
		return codMonPago;
	}
	public void setCodMonPago(String codMonPago) {
		this.codMonPago = codMonPago;
	}
	public String getCohorte() {
		return cohorte;
	}
	public void setCohorte(String cohorte) {
		this.cohorte = cohorte;
	}
	public String getFecEfectoIni() {
		return fecEfectoIni;
	}
	public void setFecEfectoIni(String fecEfectoIni) {
		this.fecEfectoIni = fecEfectoIni;
	}
	public String getFecEfectoSpto() {
		return fecEfectoSpto;
	}
	public void setFecEfectoSpto(String fecEfectoSpto) {
		this.fecEfectoSpto = fecEfectoSpto;
	}
	public String getFecSuscripcion() {
		return fecSuscripcion;
	}
	public void setFecSuscripcion(String fecSuscripcion) {
		this.fecSuscripcion = fecSuscripcion;
	}
	public String getCodLobBis() {
		return codLobBis;
	}
	public void setCodLobBis(String codLobBis) {
		this.codLobBis = codLobBis;
	}
	public String getPctDesglose() {
		return pctDesglose;
	}

	public void setPctDesglose(String pctDesglose) {
		this.pctDesglose = pctDesglose;
	}

	public String getSistema() {
		return sistema;
	}
	public void setSistema(String sistema) {
		this.sistema = sistema;
	}
	public String getClaveIncCorr() {
		return claveIncCorr;
	}
	public void setClaveIncCorr(String claveIncCorr) {
		this.claveIncCorr = claveIncCorr;
	}
	public String getFechaEstado() {
		return fechaEstado;
	}
	public void setFechaEstado(String fechaEstado) {
		this.fechaEstado = fechaEstado;
	}
	public String getMcaVigente() {
		return mcaVigente;
	}
	public void setMcaVigente(String mcaVigente) {
		this.mcaVigente = mcaVigente;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((anioMes == null) ? 0 : anioMes.hashCode());
		result = prime * result + ((claseExp == null) ? 0 : claseExp.hashCode());
		result = prime * result + ((claseMediador == null) ? 0 : claseMediador.hashCode());
		result = prime * result + ((claseProductor == null) ? 0 : claseProductor.hashCode());
		result = prime * result + ((claveIncCorr == null) ? 0 : claveIncCorr.hashCode());
		result = prime * result + ((codCia == null) ? 0 : codCia.hashCode());
		result = prime * result + ((codCob == null) ? 0 : codCob.hashCode());
		result = prime * result + ((codCtoRva == null) ? 0 : codCtoRva.hashCode());
		result = prime * result + ((codDocum == null) ? 0 : codDocum.hashCode());
		result = prime * result + ((codMon == null) ? 0 : codMon.hashCode());
		result = prime * result + ((codMonPago == null) ? 0 : codMonPago.hashCode());
		result = prime * result + ((codProductor == null) ? 0 : codProductor.hashCode());
		result = prime * result + ((codRamo == null) ? 0 : codRamo.hashCode());
		result = prime * result + ((codSector == null) ? 0 : codSector.hashCode());
		result = prime * result + ((codTerceroAgt == null) ? 0 : codTerceroAgt.hashCode());
		result = prime * result + ((fecMovExp == null) ? 0 : fecMovExp.hashCode());
		result = prime * result + ((codTipcGrpTipExp == null) ? 0 : codTipcGrpTipExp.hashCode());
		result = prime * result + ((fecApertExp == null) ? 0 : fecApertExp.hashCode());
		result = prime * result + ((fecMovEco == null) ? 0 : fecMovEco.hashCode());
		result = prime * result + ((fecOcurSini == null) ? 0 : fecOcurSini.hashCode());
		result = prime * result + ((fecReapertExp == null) ? 0 : fecReapertExp.hashCode());
		result = prime * result + ((fecTermExp == null) ? 0 : fecTermExp.hashCode());
		result = prime * result + ((fechaEstado == null) ? 0 : fechaEstado.hashCode());
		result = prime * result + ((importePago == null) ? 0 : importePago.hashCode());
		result = prime * result + ((importeValoracion == null) ? 0 : importeValoracion.hashCode());
		result = prime * result + ((mcaVigente == null) ? 0 : mcaVigente.hashCode());
		result = prime * result + ((modalidad == null) ? 0 : modalidad.hashCode());
		result = prime * result + ((numApli == null) ? 0 : numApli.hashCode());
		result = prime * result + ((numExp == null) ? 0 : numExp.hashCode());
		result = prime * result + ((numMvto == null) ? 0 : numMvto.hashCode());
		result = prime * result + ((numPoliza == null) ? 0 : numPoliza.hashCode());
		result = prime * result + ((numRiesgo == null) ? 0 : numRiesgo.hashCode());
		result = prime * result + ((ofDirecta == null) ? 0 : ofDirecta.hashCode());
		result = prime * result + ((pagoAnual == null) ? 0 : pagoAnual.hashCode());
		result = prime * result + ((pctCoa == null) ? 0 : pctCoa.hashCode());
		result = prime * result + ((cohorte == null) ? 0 : cohorte.hashCode());
		result = prime * result + ((fecEfectoIni == null) ? 0 : fecEfectoIni.hashCode());
		result = prime * result + ((fecEfectoSpto == null) ? 0 : fecEfectoSpto.hashCode());
		result = prime * result + ((fecSuscripcion == null) ? 0 : fecSuscripcion.hashCode());
		result = prime * result + ((codLobBis == null) ? 0 : codLobBis.hashCode());
		result = prime * result + ((sistema == null) ? 0 : sistema.hashCode());
		result = prime * result + ((tipCoa == null) ? 0 : tipCoa.hashCode());
		result = prime * result + ((tipDocum == null) ? 0 : tipDocum.hashCode());
		result = prime * result + ((tipExp == null) ? 0 : tipExp.hashCode());
		result = prime * result + ((tipRea == null) ? 0 : tipRea.hashCode());
		result = prime * result + ((tipoMov == null) ? 0 : tipoMov.hashCode());
		result = prime * result + ((tipoOper == null) ? 0 : tipoOper.hashCode());
		result = prime * result + ((valTotalReservaMes == null) ? 0 : valTotalReservaMes.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FlujosRealesSalida other = (FlujosRealesSalida) obj;
		if (anioMes == null) {
			if (other.anioMes != null)
				return false;
		} else if (!anioMes.equals(other.anioMes))
			return false;
		if (claseExp == null) {
			if (other.claseExp != null)
				return false;
		} else if (!claseExp.equals(other.claseExp))
			return false;
		if (claseMediador == null) {
			if (other.claseMediador != null)
				return false;
		} else if (!claseMediador.equals(other.claseMediador))
			return false;
		if (claseProductor == null) {
			if (other.claseProductor != null)
				return false;
		} else if (!claseProductor.equals(other.claseProductor))
			return false;
		if (claveIncCorr == null) {
			if (other.claveIncCorr != null)
				return false;
		} else if (!claveIncCorr.equals(other.claveIncCorr))
			return false;
		if (codCia == null) {
			if (other.codCia != null)
				return false;
		} else if (!codCia.equals(other.codCia))
			return false;
		if (codCob == null) {
			if (other.codCob != null)
				return false;
		} else if (!codCob.equals(other.codCob))
			return false;
		if (codCtoRva == null) {
			if (other.codCtoRva != null)
				return false;
		} else if (!codCtoRva.equals(other.codCtoRva))
			return false;
		if (codDocum == null) {
			if (other.codDocum != null)
				return false;
		} else if (!codDocum.equals(other.codDocum))
			return false;
		if (codMon == null) {
			if (other.codMon != null)
				return false;
		} else if (!codMon.equals(other.codMon))
			return false;
		if (codMonPago == null) {
			if (other.codMonPago != null)
				return false;
		} else if (!codMonPago.equals(other.codMonPago))
			return false;
		if (codProductor == null) {
			if (other.codProductor != null)
				return false;
		} else if (!codProductor.equals(other.codProductor))
			return false;
		if (codRamo == null) {
			if (other.codRamo != null)
				return false;
		} else if (!codRamo.equals(other.codRamo))
			return false;
		if (codSector == null) {
			if (other.codSector != null)
				return false;
		} else if (!codSector.equals(other.codSector))
			return false;
		if (codTerceroAgt == null) {
			if (other.codTerceroAgt != null)
				return false;
		} else if (!codTerceroAgt.equals(other.codTerceroAgt))
			return false;
		if (fecMovExp == null) {
			if (other.fecMovExp != null)
				return false;
		} else if (!fecMovExp.equals(other.fecMovExp))
			return false;
		if (codTipcGrpTipExp == null) {
			if (other.codTipcGrpTipExp != null)
				return false;
		} else if (!codTipcGrpTipExp.equals(other.codTipcGrpTipExp))
			return false;
		if (fecApertExp == null) {
			if (other.fecApertExp != null)
				return false;
		} else if (!fecApertExp.equals(other.fecApertExp))
			return false;
		if (fecMovEco == null) {
			if (other.fecMovEco != null)
				return false;
		} else if (!fecMovEco.equals(other.fecMovEco))
			return false;
		if (fecOcurSini == null) {
			if (other.fecOcurSini != null)
				return false;
		} else if (!fecOcurSini.equals(other.fecOcurSini))
			return false;
		if (fecReapertExp == null) {
			if (other.fecReapertExp != null)
				return false;
		} else if (!fecReapertExp.equals(other.fecReapertExp))
			return false;
		if (fecTermExp == null) {
			if (other.fecTermExp != null)
				return false;
		} else if (!fecTermExp.equals(other.fecTermExp))
			return false;
		if (fechaEstado == null) {
			if (other.fechaEstado != null)
				return false;
		} else if (!fechaEstado.equals(other.fechaEstado))
			return false;
		if (importePago == null) {
			if (other.importePago != null)
				return false;
		} else if (!importePago.equals(other.importePago))
			return false;
		if (importeValoracion == null) {
			if (other.importeValoracion != null)
				return false;
		} else if (!importeValoracion.equals(other.importeValoracion))
			return false;
		if (mcaVigente == null) {
			if (other.mcaVigente != null)
				return false;
		} else if (!mcaVigente.equals(other.mcaVigente))
			return false;
		if (modalidad == null) {
			if (other.modalidad != null)
				return false;
		} else if (!modalidad.equals(other.modalidad))
			return false;
		if (numApli == null) {
			if (other.numApli != null)
				return false;
		} else if (!numApli.equals(other.numApli))
			return false;
		if (numExp == null) {
			if (other.numExp != null)
				return false;
		} else if (!numExp.equals(other.numExp))
			return false;
		if (numMvto == null) {
			if (other.numMvto != null)
				return false;
		} else if (!numMvto.equals(other.numMvto))
			return false;
		if (numPoliza == null) {
			if (other.numPoliza != null)
				return false;
		} else if (!numPoliza.equals(other.numPoliza))
			return false;
		if (numRiesgo == null) {
			if (other.numRiesgo != null)
				return false;
		} else if (!numRiesgo.equals(other.numRiesgo))
			return false;
		if (ofDirecta == null) {
			if (other.ofDirecta != null)
				return false;
		} else if (!ofDirecta.equals(other.ofDirecta))
			return false;
		if (pagoAnual == null) {
			if (other.pagoAnual != null)
				return false;
		} else if (!pagoAnual.equals(other.pagoAnual))
			return false;
		if (pctCoa == null) {
			if (other.pctCoa != null)
				return false;
		} else if (!pctCoa.equals(other.pctCoa))
			return false;
		if (cohorte == null) {
			if (other.cohorte != null)
				return false;
		} else if (!cohorte.equals(other.cohorte))
			return false;
		if (fecEfectoIni == null) {
			if (other.fecEfectoIni != null)
				return false;
		} else if (!fecEfectoIni.equals(other.fecEfectoIni))
			return false;
		if (fecEfectoSpto == null) {
			if (other.fecEfectoSpto != null)
				return false;
		} else if (!fecEfectoSpto.equals(other.fecEfectoSpto))
			return false;
		if (fecSuscripcion == null) {
			if (other.fecSuscripcion != null)
				return false;
		} else if (!fecSuscripcion.equals(other.fecSuscripcion))
			return false;
		if (codLobBis == null) {
			if (other.codLobBis != null)
				return false;
		} else if (!codLobBis.equals(other.codLobBis))
			return false;
		if (sistema == null) {
			if (other.sistema != null)
				return false;
		} else if (!sistema.equals(other.sistema))
			return false;
		if (tipCoa == null) {
			if (other.tipCoa != null)
				return false;
		} else if (!tipCoa.equals(other.tipCoa))
			return false;
		if (tipDocum == null) {
			if (other.tipDocum != null)
				return false;
		} else if (!tipDocum.equals(other.tipDocum))
			return false;
		if (tipExp == null) {
			if (other.tipExp != null)
				return false;
		} else if (!tipExp.equals(other.tipExp))
			return false;
		if (tipRea == null) {
			if (other.tipRea != null)
				return false;
		} else if (!tipRea.equals(other.tipRea))
			return false;
		if (tipoMov == null) {
			if (other.tipoMov != null)
				return false;
		} else if (!tipoMov.equals(other.tipoMov))
			return false;
		if (tipoOper == null) {
			if (other.tipoOper != null)
				return false;
		} else if (!tipoOper.equals(other.tipoOper))
			return false;
		if (valTotalReservaMes == null) {
			if (other.valTotalReservaMes != null)
				return false;
		} else if (!valTotalReservaMes.equals(other.valTotalReservaMes))
			return false;
		return true;
	}
}
