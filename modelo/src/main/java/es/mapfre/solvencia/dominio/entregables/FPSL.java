package es.mapfre.solvencia.dominio.entregables;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.entregables.FPSLKey;
import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.dominio.EntidadConBaseTec;

@Portable
public class FPSL implements EntidadBase<FPSLKey> {

	public static final int IND_FCIERRE = 0;
	public static final int IND_KUOA = 1;
	public static final int IND_TEXTRACCION = 2;
	public static final int IND_TPASIVO = 3;
	public static final int IND_TNEGOCIO = 4;
	public static final int IND_TNEGOCIOLRC = 5;
	public static final int IND_PVENTA = 6;
	public static final int IND_CODREASEG = 7;
	public static final int IND_FPROYFLUJEST = 8;
	public static final int IND_SIG_TOTFPNAPRIM = 9;
	public static final int IND_TOTFPNAPRIM = 10;
	public static final int IND_MON_PRIM = 11;
	public static final int IND_SIG_TOTFPNARTE = 12;
	public static final int IND_TOTFPNARTE = 13;
	public static final int IND_MON_RTE = 14;
	public static final int IND_SIG_TOTFPNAFALL = 15;
	public static final int IND_TOTFPNAFALL = 16;
	public static final int IND_MON_FALL = 17;
	public static final int IND_SIG_TOTFPNAVIDA = 18;
	public static final int IND_TOTFPNAVIDA = 19;
	public static final int IND_MON_VIDA = 20;
	public static final int IND_SIG_TOTFPNAGTOAD = 21;
	public static final int IND_TOTFPNAGTOAD = 22;
	public static final int IND_MON_GTOADQ = 23;
	public static final int IND_SIG_TOTFPNAGTO = 24;
	public static final int IND_TOTFPNAGTO = 25;
	public static final int IND_MON_GTO = 26;
	public static final int IND_SIG_PB = 27;
	public static final int IND_PB = 28;
	public static final int IND_MON_PB = 29;
	public static final int IND_SEPARADOR = 30;
	public static final int IND_SIG_TOTFPNACOMI = 31;
	public static final int IND_TOTFPNACOMIM = 32;
	public static final int IND_MON_COMI = 33;
	public static final int IND_TXT_CABECERA = 34;
	public static final int IND_SIG_PB_TECNICA = 35;
	public static final int IND_PB_TECNICA = 36;
	public static final int IND_MON_PB_TECNICA = 37;
	public static final int IND_SIG_IBNR = 38;
	public static final int IND_IBNR = 39;
	public static final int IND_MON_IBNR = 40;

	
	@PortableProperty(FPSL.IND_SEPARADOR)
	private String separador;
	

	@PortableProperty(FPSL.IND_FCIERRE)
	private Timestamp fcierre;
	@PortableProperty(FPSL.IND_KUOA)
	private String kuoa;
	@PortableProperty(FPSL.IND_TEXTRACCION)
	private String textraccion;
	@PortableProperty(FPSL.IND_TPASIVO)
	private String tpasivo;
	@PortableProperty(FPSL.IND_TNEGOCIO)
	private Integer tnegocio;
	@PortableProperty(FPSL.IND_TNEGOCIOLRC)
	private Integer tnegociolrc;
	@PortableProperty(FPSL.IND_PVENTA)
	private Timestamp pventa;
	@PortableProperty(FPSL.IND_CODREASEG)
	private Integer codreaseg;
	@PortableProperty(FPSL.IND_FPROYFLUJEST)
	private Timestamp fproyflujest;
	@PortableProperty(FPSL.IND_SIG_TOTFPNAPRIM)
	private String sig_totfpnaprim;
	@PortableProperty(FPSL.IND_TOTFPNAPRIM)
	private java.math.BigDecimal totfpnaprim;
	@PortableProperty(FPSL.IND_MON_PRIM)
	private String mon_prim;
	@PortableProperty(FPSL.IND_SIG_TOTFPNARTE)
	private String sig_totfpnarte;
	@PortableProperty(FPSL.IND_TOTFPNARTE)
	private java.math.BigDecimal totfpnarte;
	@PortableProperty(FPSL.IND_MON_RTE)
	private String mon_rte;
	@PortableProperty(FPSL.IND_SIG_TOTFPNAFALL)
	private String sig_totfpnafall;
	@PortableProperty(FPSL.IND_TOTFPNAFALL)
	private java.math.BigDecimal totfpnafall;
	@PortableProperty(FPSL.IND_MON_FALL)
	private String mon_fall;
	@PortableProperty(FPSL.IND_SIG_TOTFPNAVIDA)
	private String sig_totfpnavida;
	@PortableProperty(FPSL.IND_TOTFPNAVIDA)
	private java.math.BigDecimal totfpnavida;
	@PortableProperty(FPSL.IND_MON_VIDA)
	private String mon_vida;
	@PortableProperty(FPSL.IND_SIG_TOTFPNAGTOAD)
	private String sig_totfpnagtoad;
	@PortableProperty(FPSL.IND_TOTFPNAGTOAD)
	private java.math.BigDecimal totfpnagtoad;
	@PortableProperty(FPSL.IND_MON_GTOADQ)
	private String mon_gtoadq;
	@PortableProperty(FPSL.IND_SIG_TOTFPNAGTO)
	private String sig_totfpnagto;
	@PortableProperty(FPSL.IND_TOTFPNAGTO)
	private java.math.BigDecimal totfpnagto;
	@PortableProperty(FPSL.IND_MON_GTO)
	private String mon_gto;
	@PortableProperty(FPSL.IND_SIG_PB)
	private String sig_pb;
	@PortableProperty(FPSL.IND_PB)
	private java.math.BigDecimal pb;
	@PortableProperty(FPSL.IND_MON_PB)
	private String mon_pb;
	@PortableProperty(FPSL.IND_SIG_TOTFPNACOMI)
	private String sig_totfpnacomi;
	@PortableProperty(FPSL.IND_TOTFPNACOMIM)
	private java.math.BigDecimal totfpnacomi;
	@PortableProperty(FPSL.IND_MON_COMI)
	private String mon_comi;
	@PortableProperty(FPSL.IND_TXT_CABECERA)
	private String txtCabecera;
	@PortableProperty(FPSL.IND_SIG_PB_TECNICA)
	private String sig_pb_tecnica;
	@PortableProperty(FPSL.IND_PB_TECNICA)
	private java.math.BigDecimal pb_tecnica;
	@PortableProperty(FPSL.IND_MON_PB_TECNICA)
	private String mon_pb_tecnica;
	@PortableProperty(FPSL.IND_SIG_IBNR)
	private String sig_ibnr;
	@PortableProperty(FPSL.IND_IBNR)
	private java.math.BigDecimal ibnr;
	@PortableProperty(FPSL.IND_MON_IBNR)
	private String mon_ibnr;
	
	public String getSeparador() {
		return separador;
	}

	public void setSeparador(String separador) {
		this.separador = separador;
	}
	
	public String getkuoa() {
		return kuoa;
	}

	public Timestamp getFcierre() {
		return fcierre;
	}

	public void setFcierre(Timestamp fcierre) {
		this.fcierre = fcierre;
	}

	public java.math.BigDecimal getTotfpnaprim() {
		return totfpnaprim;
	}

	public void setTotfpnaprim(java.math.BigDecimal totfpnaprim) {
		this.totfpnaprim = totfpnaprim;
	}

	public java.math.BigDecimal getTotfpnarte() {
		return totfpnarte;
	}

	public void setTotfpnarte(java.math.BigDecimal totfpnarte) {
		this.totfpnarte = totfpnarte;
	}

	public java.math.BigDecimal getTotfpnafall() {
		return totfpnafall;
	}

	public void setTotfpnafall(java.math.BigDecimal totfpnafall) {
		this.totfpnafall = totfpnafall;
	}

	public java.math.BigDecimal getTotfpnavida() {
		return totfpnavida;
	}

	public void setTotfpnavida(java.math.BigDecimal totfpnavida) {
		this.totfpnavida = totfpnavida;
	}

	public java.math.BigDecimal getTotfpnagto() {
		return totfpnagto;
	}

	public void setTotfpnagto(java.math.BigDecimal totfpnagto) {
		this.totfpnagto = totfpnagto;
	}

	public void setkuoa(String kuoa) {
		this.kuoa = kuoa;
	}

	public String getTextraccion() {
		return textraccion;
	}

	public void setTextraccion(String textraccion) {
		this.textraccion = textraccion;
	}

	public String getTpasivo() {
		return tpasivo;
	}

	public void setTpasivo(String tpasivo) {
		this.tpasivo = tpasivo;
	}

	public Integer getTnegocio() {
		return tnegocio;
	}

	public void setTnegocio(Integer tnegocio) {
		this.tnegocio = tnegocio;
	}

	public Integer getTnegociolrc() {
		return tnegociolrc;
	}

	public void setTnegociolrc(Integer tnegociolrc) {
		this.tnegociolrc = tnegociolrc;
	}

	public Timestamp getPventa() {
		return pventa;
	}

	public void setPventa(Timestamp pventa) {
		this.pventa = pventa;
	}

	public Integer getCodreaseg() {
		return codreaseg;
	}

	public void setCodreaseg(Integer codreaseg) {
		this.codreaseg = codreaseg;
	}

	public Timestamp getFproyflujest() {
		return fproyflujest;
	}

	public void setFproyflujest(Timestamp fproyflujest) {
		this.fproyflujest = fproyflujest;
	}

	public String getSig_totfpnaprim() {
		return sig_totfpnaprim;
	}

	public void setSig_totfpnaprim(String sig_totfpnaprim) {
		this.sig_totfpnaprim = sig_totfpnaprim;
	}

	public String getMon_prim() {
		return mon_prim;
	}

	public void setMon_prim(String mon_prim) {
		this.mon_prim = mon_prim;
	}

	public String getSig_totfpnarte() {
		return sig_totfpnarte;
	}

	public void setSig_totfpnarte(String sig_totfpnarte) {
		this.sig_totfpnarte = sig_totfpnarte;
	}

	public String getMon_rte() {
		return mon_rte;
	}

	public void setMon_rte(String mon_rte) {
		this.mon_rte = mon_rte;
	}

	public String getSig_totfpnafall() {
		return sig_totfpnafall;
	}

	public void setSig_totfpnafall(String sig_totfpnafall) {
		this.sig_totfpnafall = sig_totfpnafall;
	}

	public String getMon_fall() {
		return mon_fall;
	}

	public void setMon_fall(String mon_fall) {
		this.mon_fall = mon_fall;
	}

	public String getSig_totfpnavida() {
		return sig_totfpnavida;
	}

	public void setSig_totfpnavida(String sig_totfpnavida) {
		this.sig_totfpnavida = sig_totfpnavida;
	}

	public String getMon_vida() {
		return mon_vida;
	}

	public void setMon_vida(String mon_vida) {
		this.mon_vida = mon_vida;
	}

	public String getSig_totfpnagtoad() {
		return sig_totfpnagtoad;
	}

	public void setSig_totfpnagtoadq(String sig_totfpnagtoad) {
		this.sig_totfpnagtoad = sig_totfpnagtoad;
	}

	public java.math.BigDecimal getTotfpnagtoad() {
		return totfpnagtoad;
	}

	public void setTotfpnagtoad(java.math.BigDecimal totfpnagtoad) {
		this.totfpnagtoad = totfpnagtoad;
	}

	public String getMon_gtoadq() {
		return mon_gtoadq;
	}

	public void setMon_gtoadq(String mon_gtoadq) {
		this.mon_gtoadq = mon_gtoadq;
	}

	public String getSig_totfpnagto() {
		return sig_totfpnagto;
	}

	public void setSig_totfpnagto(String sig_totfpnagto) {
		this.sig_totfpnagto = sig_totfpnagto;
	}

	public String getMon_gto() {
		return mon_gto;
	}

	public void setMon_gto(String mon_gto) {
		this.mon_gto = mon_gto;
	}

	public String getSig_pb() {
		return sig_pb;
	}

	public void setSig_pb(String sig_pb) {
		this.sig_pb = sig_pb;
	}

	public java.math.BigDecimal getPb() {
		return pb;
	}

	public void setPb(java.math.BigDecimal pb) {
		this.pb = pb;
	}

	public String getMon_pb() {
		return mon_pb;
	}

	public void setMon_pb(String mon_pb) {
		this.mon_pb = mon_pb;
	}

	public String getSig_comi() {
		return sig_totfpnacomi;
	}

	public void setSig_comi(String sig_comi) {
		this.sig_totfpnacomi = sig_comi;
	}

	public java.math.BigDecimal getTotfpnacomi() {
		return totfpnacomi;
	}

	public void setTotfpnacomi(java.math.BigDecimal totfpnacomi) {
		this.totfpnacomi = totfpnacomi;
	}

	public String getMon_comi() {
		return mon_comi;
	}

	public void setMon_comi(String mon_comi) {
		this.mon_comi = mon_comi;
	}

	public String getTxtCabecera() {
		return txtCabecera;
	}

	public void setTxtCabecera(String txtCabecera) {
		this.txtCabecera = txtCabecera;
	}
	
	public String getSig_pb_tecnica() {
		return sig_pb_tecnica;
	}

	public void setSig_pb_tecnica(String sig_pb_tecnica) {
		this.sig_pb_tecnica = sig_pb_tecnica;
	}

	public java.math.BigDecimal getPb_tecnica() {
		return pb_tecnica;
	}

	public void setPb_tecnica(java.math.BigDecimal pb_tecnica) {
		this.pb_tecnica = pb_tecnica;
	}

	public String getMon_pb_tecnica() {
		return mon_pb_tecnica;
	}

	public void setMon_pb_tecnica(String mon_pb_tecnica) {
		this.mon_pb_tecnica = mon_pb_tecnica;
	}

	public String getSig_ibnr() {
		return sig_ibnr;
	}

	public void setSig_ibnr(String sig_ibnr) {
		this.sig_ibnr = sig_ibnr;
	}

	public java.math.BigDecimal getIbnr() {
		return ibnr;
	}

	public void setIbnr(java.math.BigDecimal ibnr) {
		this.ibnr = ibnr;
	}

	public String getMon_ibnr() {
		return mon_ibnr;
	}

	public void setMon_ibnr(String mon_ibnr) {
		this.mon_ibnr = mon_ibnr;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codreaseg == null) ? 0 : codreaseg.hashCode());
		result = prime * result + ((fcierre == null) ? 0 : fcierre.hashCode());
		result = prime * result + ((fproyflujest == null) ? 0 : fproyflujest.hashCode());
		result = prime * result + ((ibnr == null) ? 0 : ibnr.hashCode());
		result = prime * result + ((kuoa == null) ? 0 : kuoa.hashCode());
		result = prime * result + ((mon_comi == null) ? 0 : mon_comi.hashCode());
		result = prime * result + ((mon_fall == null) ? 0 : mon_fall.hashCode());
		result = prime * result + ((mon_gto == null) ? 0 : mon_gto.hashCode());
		result = prime * result + ((mon_gtoadq == null) ? 0 : mon_gtoadq.hashCode());
		result = prime * result + ((mon_ibnr == null) ? 0 : mon_ibnr.hashCode());
		result = prime * result + ((mon_pb == null) ? 0 : mon_pb.hashCode());
		result = prime * result + ((mon_pb_tecnica == null) ? 0 : mon_pb_tecnica.hashCode());
		result = prime * result + ((mon_prim == null) ? 0 : mon_prim.hashCode());
		result = prime * result + ((mon_rte == null) ? 0 : mon_rte.hashCode());
		result = prime * result + ((mon_vida == null) ? 0 : mon_vida.hashCode());
		result = prime * result + ((pb == null) ? 0 : pb.hashCode());
		result = prime * result + ((pb_tecnica == null) ? 0 : pb_tecnica.hashCode());
		result = prime * result + ((pventa == null) ? 0 : pventa.hashCode());
		result = prime * result + ((separador == null) ? 0 : separador.hashCode());
		result = prime * result + ((sig_ibnr == null) ? 0 : sig_ibnr.hashCode());
		result = prime * result + ((sig_pb == null) ? 0 : sig_pb.hashCode());
		result = prime * result + ((sig_pb_tecnica == null) ? 0 : sig_pb_tecnica.hashCode());
		result = prime * result + ((sig_totfpnacomi == null) ? 0 : sig_totfpnacomi.hashCode());
		result = prime * result + ((sig_totfpnafall == null) ? 0 : sig_totfpnafall.hashCode());
		result = prime * result + ((sig_totfpnagto == null) ? 0 : sig_totfpnagto.hashCode());
		result = prime * result + ((sig_totfpnagtoad == null) ? 0 : sig_totfpnagtoad.hashCode());
		result = prime * result + ((sig_totfpnaprim == null) ? 0 : sig_totfpnaprim.hashCode());
		result = prime * result + ((sig_totfpnarte == null) ? 0 : sig_totfpnarte.hashCode());
		result = prime * result + ((sig_totfpnavida == null) ? 0 : sig_totfpnavida.hashCode());
		result = prime * result + ((textraccion == null) ? 0 : textraccion.hashCode());
		result = prime * result + ((tnegocio == null) ? 0 : tnegocio.hashCode());
		result = prime * result + ((tnegociolrc == null) ? 0 : tnegociolrc.hashCode());
		result = prime * result + ((totfpnacomi == null) ? 0 : totfpnacomi.hashCode());
		result = prime * result + ((totfpnafall == null) ? 0 : totfpnafall.hashCode());
		result = prime * result + ((totfpnagto == null) ? 0 : totfpnagto.hashCode());
		result = prime * result + ((totfpnagtoad == null) ? 0 : totfpnagtoad.hashCode());
		result = prime * result + ((totfpnaprim == null) ? 0 : totfpnaprim.hashCode());
		result = prime * result + ((totfpnarte == null) ? 0 : totfpnarte.hashCode());
		result = prime * result + ((totfpnavida == null) ? 0 : totfpnavida.hashCode());
		result = prime * result + ((tpasivo == null) ? 0 : tpasivo.hashCode());
		result = prime * result + ((txtCabecera == null) ? 0 : txtCabecera.hashCode());
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
		FPSL other = (FPSL) obj;
		if (codreaseg == null) {
			if (other.codreaseg != null)
				return false;
		} else if (!codreaseg.equals(other.codreaseg))
			return false;
		if (fcierre == null) {
			if (other.fcierre != null)
				return false;
		} else if (!fcierre.equals(other.fcierre))
			return false;
		if (fproyflujest == null) {
			if (other.fproyflujest != null)
				return false;
		} else if (!fproyflujest.equals(other.fproyflujest))
			return false;
		if (ibnr == null) {
			if (other.ibnr != null)
				return false;
		} else if (!ibnr.equals(other.ibnr))
			return false;
		if (kuoa == null) {
			if (other.kuoa != null)
				return false;
		} else if (!kuoa.equals(other.kuoa))
			return false;
		if (mon_comi == null) {
			if (other.mon_comi != null)
				return false;
		} else if (!mon_comi.equals(other.mon_comi))
			return false;
		if (mon_fall == null) {
			if (other.mon_fall != null)
				return false;
		} else if (!mon_fall.equals(other.mon_fall))
			return false;
		if (mon_gto == null) {
			if (other.mon_gto != null)
				return false;
		} else if (!mon_gto.equals(other.mon_gto))
			return false;
		if (mon_gtoadq == null) {
			if (other.mon_gtoadq != null)
				return false;
		} else if (!mon_gtoadq.equals(other.mon_gtoadq))
			return false;
		if (mon_ibnr == null) {
			if (other.mon_ibnr != null)
				return false;
		} else if (!mon_ibnr.equals(other.mon_ibnr))
			return false;
		if (mon_pb == null) {
			if (other.mon_pb != null)
				return false;
		} else if (!mon_pb.equals(other.mon_pb))
			return false;
		if (mon_pb_tecnica == null) {
			if (other.mon_pb_tecnica != null)
				return false;
		} else if (!mon_pb_tecnica.equals(other.mon_pb_tecnica))
			return false;
		if (mon_prim == null) {
			if (other.mon_prim != null)
				return false;
		} else if (!mon_prim.equals(other.mon_prim))
			return false;
		if (mon_rte == null) {
			if (other.mon_rte != null)
				return false;
		} else if (!mon_rte.equals(other.mon_rte))
			return false;
		if (mon_vida == null) {
			if (other.mon_vida != null)
				return false;
		} else if (!mon_vida.equals(other.mon_vida))
			return false;
		if (pb == null) {
			if (other.pb != null)
				return false;
		} else if (!pb.equals(other.pb))
			return false;
		if (pb_tecnica == null) {
			if (other.pb_tecnica != null)
				return false;
		} else if (!pb_tecnica.equals(other.pb_tecnica))
			return false;
		if (pventa == null) {
			if (other.pventa != null)
				return false;
		} else if (!pventa.equals(other.pventa))
			return false;
		if (separador == null) {
			if (other.separador != null)
				return false;
		} else if (!separador.equals(other.separador))
			return false;
		if (sig_ibnr == null) {
			if (other.sig_ibnr != null)
				return false;
		} else if (!sig_ibnr.equals(other.sig_ibnr))
			return false;
		if (sig_pb == null) {
			if (other.sig_pb != null)
				return false;
		} else if (!sig_pb.equals(other.sig_pb))
			return false;
		if (sig_pb_tecnica == null) {
			if (other.sig_pb_tecnica != null)
				return false;
		} else if (!sig_pb_tecnica.equals(other.sig_pb_tecnica))
			return false;
		if (sig_totfpnacomi == null) {
			if (other.sig_totfpnacomi != null)
				return false;
		} else if (!sig_totfpnacomi.equals(other.sig_totfpnacomi))
			return false;
		if (sig_totfpnafall == null) {
			if (other.sig_totfpnafall != null)
				return false;
		} else if (!sig_totfpnafall.equals(other.sig_totfpnafall))
			return false;
		if (sig_totfpnagto == null) {
			if (other.sig_totfpnagto != null)
				return false;
		} else if (!sig_totfpnagto.equals(other.sig_totfpnagto))
			return false;
		if (sig_totfpnagtoad == null) {
			if (other.sig_totfpnagtoad != null)
				return false;
		} else if (!sig_totfpnagtoad.equals(other.sig_totfpnagtoad))
			return false;
		if (sig_totfpnaprim == null) {
			if (other.sig_totfpnaprim != null)
				return false;
		} else if (!sig_totfpnaprim.equals(other.sig_totfpnaprim))
			return false;
		if (sig_totfpnarte == null) {
			if (other.sig_totfpnarte != null)
				return false;
		} else if (!sig_totfpnarte.equals(other.sig_totfpnarte))
			return false;
		if (sig_totfpnavida == null) {
			if (other.sig_totfpnavida != null)
				return false;
		} else if (!sig_totfpnavida.equals(other.sig_totfpnavida))
			return false;
		if (textraccion == null) {
			if (other.textraccion != null)
				return false;
		} else if (!textraccion.equals(other.textraccion))
			return false;
		if (tnegocio == null) {
			if (other.tnegocio != null)
				return false;
		} else if (!tnegocio.equals(other.tnegocio))
			return false;
		if (tnegociolrc == null) {
			if (other.tnegociolrc != null)
				return false;
		} else if (!tnegociolrc.equals(other.tnegociolrc))
			return false;
		if (totfpnacomi == null) {
			if (other.totfpnacomi != null)
				return false;
		} else if (!totfpnacomi.equals(other.totfpnacomi))
			return false;
		if (totfpnafall == null) {
			if (other.totfpnafall != null)
				return false;
		} else if (!totfpnafall.equals(other.totfpnafall))
			return false;
		if (totfpnagto == null) {
			if (other.totfpnagto != null)
				return false;
		} else if (!totfpnagto.equals(other.totfpnagto))
			return false;
		if (totfpnagtoad == null) {
			if (other.totfpnagtoad != null)
				return false;
		} else if (!totfpnagtoad.equals(other.totfpnagtoad))
			return false;
		if (totfpnaprim == null) {
			if (other.totfpnaprim != null)
				return false;
		} else if (!totfpnaprim.equals(other.totfpnaprim))
			return false;
		if (totfpnarte == null) {
			if (other.totfpnarte != null)
				return false;
		} else if (!totfpnarte.equals(other.totfpnarte))
			return false;
		if (totfpnavida == null) {
			if (other.totfpnavida != null)
				return false;
		} else if (!totfpnavida.equals(other.totfpnavida))
			return false;
		if (tpasivo == null) {
			if (other.tpasivo != null)
				return false;
		} else if (!tpasivo.equals(other.tpasivo))
			return false;
		if (txtCabecera == null) {
			if (other.txtCabecera != null)
				return false;
		} else if (!txtCabecera.equals(other.txtCabecera))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FPSL [separador=" + separador + ", fcierre=" + fcierre + ", kuoa=" + kuoa + ", textraccion="
				+ textraccion + ", tpasivo=" + tpasivo + ", tnegocio=" + tnegocio + ", tnegociolrc=" + tnegociolrc
				+ ", pventa=" + pventa + ", codreaseg=" + codreaseg + ", fproyflujest=" + fproyflujest
				+ ", sig_totfpnaprim=" + sig_totfpnaprim + ", totfpnaprim=" + totfpnaprim + ", mon_prim=" + mon_prim
				+ ", sig_totfpnarte=" + sig_totfpnarte + ", totfpnarte=" + totfpnarte + ", mon_rte=" + mon_rte
				+ ", sig_totfpnafall=" + sig_totfpnafall + ", totfpnafall=" + totfpnafall + ", mon_fall=" + mon_fall
				+ ", sig_totfpnavida=" + sig_totfpnavida + ", totfpnavida=" + totfpnavida + ", mon_vida=" + mon_vida
				+ ", sig_totfpnagtoad=" + sig_totfpnagtoad + ", totfpnagtoad=" + totfpnagtoad + ", mon_gtoadq="
				+ mon_gtoadq + ", sig_totfpnagto=" + sig_totfpnagto + ", totfpnagto=" + totfpnagto + ", mon_gto="
				+ mon_gto + ", sig_pb=" + sig_pb + ", pb=" + pb + ", mon_pb=" + mon_pb + ", sig_totfpnacomi="
				+ sig_totfpnacomi + ", totfpnacomi=" + totfpnacomi + ", mon_comi=" + mon_comi + ", txtCabecera="
				+ txtCabecera + ", sig_pb_tecnica=" + sig_pb_tecnica + ", pb_tecnica=" + pb_tecnica
				+ ", mon_pb_tecnica=" + mon_pb_tecnica + ", sig_ibnr=" + sig_ibnr + ", ibnr=" + ibnr + ", mon_ibnr="
				+ mon_ibnr + "]";
	}

	@Override
	public FPSLKey getKey() {
		return new FPSLKey(fcierre, kuoa, textraccion, tpasivo, tnegocio, tnegociolrc,
				pventa, codreaseg, fproyflujest);
	}

}
