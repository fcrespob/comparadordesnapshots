/* MODIFICACION:MU-2019-053081: Se incluyen campos tabla1 y gestionit en la clave
   FECHA: 26/08/2019
   AUTOR: INDRA
*/

package es.mapfre.solvencia.dominio.entregables;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.entregables.FlujSuscriKey;
import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.dominio.EntidadConBaseTec;

@Portable
public class FlujSuscri implements EntidadBase<FlujSuscriKey>, EntidadConBaseTec {

	public static final int IND_BT = 0;
	public static final int IND_FECCIERRE = 1;
	public static final int IND_CNEGOCIO = 2;
	public static final int IND_CCANAL = 3;
	public static final int IND_KRAMO = 4;
	public static final int IND_KMODALIDAD = 5;
	public static final int IND_KMODEXT = 6;
	public static final int IND_KGARANTIA = 7;
	public static final int IND_KCARTERAINV = 8;
	public static final int IND_GAPACT = 9;
	public static final int IND_SEGMENTO1 = 10;
	public static final int IND_TIPOSUBRIESGO = 11;
	public static final int IND_KCARTERA_CONTRATO = 12;
	public static final int IND_KPOLIZA = 13;
	public static final int IND_KSUBPOLIZA = 14;
	public static final int IND_NSUSCRI = 15;
	public static final int IND_SWCASADO = 16;
	public static final int IND_FINISUSC = 17;
	public static final int IND_ITCALC = 18;
	public static final int IND_FECDESDE = 19;
	public static final int IND_TOTFPVIDA = 20;
	public static final int IND_TOTOCOLAVIDA = 21;
	public static final int IND_TOTFACTVIDA = 22;
	public static final int IND_TOTFPFALL = 23;
	public static final int IND_TOTCOLAFALL = 24;
	public static final int IND_TOTFACTFALL = 25;
	public static final int IND_TOTFPCOMPL = 26;
	public static final int IND_TOTCOLACOMPL = 27;
	public static final int IND_TOTFACTCOMPL = 28;
	public static final int IND_TOTFPRTE = 29;
	public static final int IND_TOTCOLARTE = 30;
	public static final int IND_TOTFACTRTE = 31;
	public static final int IND_TOTFPGTO = 32;
	public static final int IND_TOTCOLAGTO = 33;
	public static final int IND_TOTFACTGTO = 34;
	public static final int IND_TOTFPCOM = 34;
	public static final int IND_TOTCOLACOM = 36;
	public static final int IND_TOTFACTCOM = 37;
	public static final int IND_TOTFPGTOAD = 38;
	public static final int IND_TOTCOLAGTOAD = 39;
	public static final int IND_TOTFACTGTOAD = 40;
	public static final int IND_TOTFPPRIM = 41;
	public static final int IND_TOTCOLAPRIM = 42;
	public static final int IND_TOTFACTPRIM = 43;
	public static final int IND_TOTFPPRESTACIONES = 44;
	public static final int IND_TOTCOLAPRESTACIONES = 45;
	public static final int IND_TOTFACTPRESTACIONES = 46;
	public static final int IND_TOTFPROB = 47;
	public static final int IND_TOTCOLA = 48;
	public static final int IND_TOTPROVISION = 49;
	
	


	@PortableProperty(IND_BT)
	private String bt;
	@PortableProperty(IND_FECCIERRE)
	private Timestamp feccierre;
	@PortableProperty(IND_CNEGOCIO)
	private String cnegocio;
	@PortableProperty(IND_CCANAL)
	private Integer ccanal;
	@PortableProperty(IND_KRAMO)
	private String kramo;
	@PortableProperty(IND_KMODALIDAD)
	private Integer kmodalidad;
	@PortableProperty(IND_SEGMENTO1)
	private String segmento1;
	@PortableProperty(IND_TIPOSUBRIESGO)
	private String tiposubriesgo;
	@PortableProperty(IND_KCARTERAINV)
	private String kcarterainv;
	@PortableProperty(IND_GAPACT)
	private String gapact;
	@PortableProperty(IND_KMODEXT)
	private Integer kmodext;
	@PortableProperty(IND_KGARANTIA)
	private Integer kgarantia;

	@PortableProperty(IND_KPOLIZA)
	private Long kpoliza;
	@PortableProperty(IND_KSUBPOLIZA)
	private Integer ksubpoliza;
	@PortableProperty(IND_NSUSCRI)
	private Integer nsuscri;
	@PortableProperty(IND_FINISUSC)
	private Timestamp finisusc;

	@PortableProperty(value = IND_ITCALC, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal itcalc;

	@PortableProperty(IND_FECDESDE)
	private Timestamp fecdesde;

	
	@PortableProperty(value = IND_TOTOCOLAVIDA, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolavida;
	@PortableProperty(value = IND_TOTFPVIDA, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpvida;
	@PortableProperty(value = IND_TOTFACTVIDA, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactvida;
	
	@PortableProperty(value = IND_TOTFPFALL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpfall;
	@PortableProperty(value = IND_TOTFACTFALL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactfall;
	@PortableProperty(value = IND_TOTCOLAFALL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolafall;
	
	@PortableProperty(value = IND_TOTFPCOMPL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpcompl;
	@PortableProperty(value = IND_TOTFACTCOMPL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactcompl;
	@PortableProperty(value = IND_TOTCOLACOMPL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolacompl;
	
	@PortableProperty(value = IND_TOTFPRTE, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfprte;
	@PortableProperty(value = IND_TOTFACTRTE, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactrte;
	@PortableProperty(value = IND_TOTCOLARTE, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolarte;
	
	@PortableProperty(value = IND_TOTFPGTO, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpgto;
	@PortableProperty(value = IND_TOTFACTGTO, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactgto;
	@PortableProperty(value = IND_TOTCOLAGTO, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolagto;
	
	@PortableProperty(value = IND_TOTFPCOM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpcom;
	@PortableProperty(value = IND_TOTFACTCOM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactcom;
	@PortableProperty(value = IND_TOTCOLACOM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolacom;
	
	@PortableProperty(value = IND_TOTFPGTOAD, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpgtoad;
	@PortableProperty(value = IND_TOTFACTGTOAD, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactgtoad;
	@PortableProperty(value = IND_TOTCOLAGTOAD, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolagtoad;
	
	@PortableProperty(value = IND_TOTFPPRIM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpprim;
	@PortableProperty(value = IND_TOTFACTPRIM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactprim;
	@PortableProperty(value = IND_TOTCOLAPRIM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolaprim;
	
	@PortableProperty(value = IND_TOTCOLAPRESTACIONES, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolaprestaciones;
	@PortableProperty(value = IND_TOTFPPRESTACIONES, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpprestaciones;
	@PortableProperty(value = IND_TOTFACTPRESTACIONES, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactprestaciones;
	
	@PortableProperty(value = IND_TOTFPROB, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfprob;
	@PortableProperty(value = IND_TOTCOLA, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcola;
	@PortableProperty(value = IND_TOTPROVISION, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totprovision;
	@PortableProperty(IND_KCARTERA_CONTRATO)
	private String kcarteraContrato;
	@PortableProperty(IND_SWCASADO)
	private String swcasado;

	public String getBt() {
		return bt;
	}





	public Integer getKmodext() {
		return kmodext;
	}





	public void setKmodext(Integer kmodext) {
		this.kmodext = kmodext;
	}





	public Integer getKgarantia() {
		return kgarantia;
	}





	public void setKgarantia(Integer kgarantia) {
		this.kgarantia = kgarantia;
	}





	public java.math.BigDecimal getItcalc() {
		return itcalc;
	}





	public void setItcalc(java.math.BigDecimal itcalc) {
		this.itcalc = itcalc;
	}





	public java.math.BigDecimal getTotcolavida() {
		return totcolavida;
	}





	public void setTotcolavida(java.math.BigDecimal totcolavida) {
		this.totcolavida = totcolavida;
	}





	public java.math.BigDecimal getTotfpvida() {
		return totfpvida;
	}





	public void setTotfpvida(java.math.BigDecimal totfpvida) {
		this.totfpvida = totfpvida;
	}





	public java.math.BigDecimal getTotfactvida() {
		return totfactvida;
	}





	public void setTotfactvida(java.math.BigDecimal totfactvida) {
		this.totfactvida = totfactvida;
	}





	public java.math.BigDecimal getTotfpfall() {
		return totfpfall;
	}





	public void setTotfpfall(java.math.BigDecimal totfpfall) {
		this.totfpfall = totfpfall;
	}





	public java.math.BigDecimal getTotfactfall() {
		return totfactfall;
	}





	public void setTotfactfall(java.math.BigDecimal totfactfall) {
		this.totfactfall = totfactfall;
	}





	public java.math.BigDecimal getTotcolafall() {
		return totcolafall;
	}





	public void setTotcolafall(java.math.BigDecimal totcolafall) {
		this.totcolafall = totcolafall;
	}





	public java.math.BigDecimal getTotfpcompl() {
		return totfpcompl;
	}





	public void setTotfpcompl(java.math.BigDecimal totfpcompl) {
		this.totfpcompl = totfpcompl;
	}





	public java.math.BigDecimal getTotfactcompl() {
		return totfactcompl;
	}





	public void setTotfactcompl(java.math.BigDecimal totfactcompl) {
		this.totfactcompl = totfactcompl;
	}





	public java.math.BigDecimal getTotcolacompl() {
		return totcolacompl;
	}





	public void setTotcolacompl(java.math.BigDecimal totcolacompl) {
		this.totcolacompl = totcolacompl;
	}





	public java.math.BigDecimal getTotfprte() {
		return totfprte;
	}





	public void setTotfprte(java.math.BigDecimal totfprte) {
		this.totfprte = totfprte;
	}





	public java.math.BigDecimal getTotfactrte() {
		return totfactrte;
	}





	public void setTotfactrte(java.math.BigDecimal totfactrte) {
		this.totfactrte = totfactrte;
	}





	public java.math.BigDecimal getTotcolarte() {
		return totcolarte;
	}





	public void setTotcolarte(java.math.BigDecimal totcolarte) {
		this.totcolarte = totcolarte;
	}





	public java.math.BigDecimal getTotfpgto() {
		return totfpgto;
	}





	public void setTotfpgto(java.math.BigDecimal totfpgto) {
		this.totfpgto = totfpgto;
	}





	public java.math.BigDecimal getTotfactgto() {
		return totfactgto;
	}





	public void setTotfactgto(java.math.BigDecimal totfactgto) {
		this.totfactgto = totfactgto;
	}





	public java.math.BigDecimal getTotcolagto() {
		return totcolagto;
	}





	public void setTotcolagto(java.math.BigDecimal totcolagto) {
		this.totcolagto = totcolagto;
	}





	public java.math.BigDecimal getTotfpcom() {
		return totfpcom;
	}





	public void setTotfpcom(java.math.BigDecimal totfpcom) {
		this.totfpcom = totfpcom;
	}





	public java.math.BigDecimal getTotfactcom() {
		return totfactcom;
	}





	public void setTotfactcom(java.math.BigDecimal totfactcom) {
		this.totfactcom = totfactcom;
	}





	public java.math.BigDecimal getTotcolacom() {
		return totcolacom;
	}





	public void setTotcolacom(java.math.BigDecimal totcolacom) {
		this.totcolacom = totcolacom;
	}





	public java.math.BigDecimal getTotfpgtoad() {
		return totfpgtoad;
	}





	public void setTotfpgtoad(java.math.BigDecimal totfpgtoad) {
		this.totfpgtoad = totfpgtoad;
	}





	public java.math.BigDecimal getTotfactgtoad() {
		return totfactgtoad;
	}





	public void setTotfactgtoad(java.math.BigDecimal totfactgtoad) {
		this.totfactgtoad = totfactgtoad;
	}





	public java.math.BigDecimal getTotcolagtoad() {
		return totcolagtoad;
	}





	public void setTotcolagtoad(java.math.BigDecimal totcolagtoad) {
		this.totcolagtoad = totcolagtoad;
	}





	public java.math.BigDecimal getTotfpprim() {
		return totfpprim;
	}





	public void setTotfpprim(java.math.BigDecimal totfpprim) {
		this.totfpprim = totfpprim;
	}





	public java.math.BigDecimal getTotfactprim() {
		return totfactprim;
	}





	public void setTotfactprim(java.math.BigDecimal totfactprim) {
		this.totfactprim = totfactprim;
	}





	public java.math.BigDecimal getTotcolaprim() {
		return totcolaprim;
	}





	public void setTotcolaprim(java.math.BigDecimal totcolaprim) {
		this.totcolaprim = totcolaprim;
	}





	public java.math.BigDecimal getTotcolaprestaciones() {
		return totcolaprestaciones;
	}





	public void setTotcolaprestaciones(java.math.BigDecimal totcolaprestaciones) {
		this.totcolaprestaciones = totcolaprestaciones;
	}





	public java.math.BigDecimal getTotfpprestaciones() {
		return totfpprestaciones;
	}





	public void setTotfpprestaciones(java.math.BigDecimal totfpprestaciones) {
		this.totfpprestaciones = totfpprestaciones;
	}





	public java.math.BigDecimal getTotfactprestaciones() {
		return totfactprestaciones;
	}





	public void setTotfactprestaciones(java.math.BigDecimal totfactprestaciones) {
		this.totfactprestaciones = totfactprestaciones;
	}





	public java.math.BigDecimal getTotfprob() {
		return totfprob;
	}





	public void setTotfprob(java.math.BigDecimal totfprob) {
		this.totfprob = totfprob;
	}





	public java.math.BigDecimal getTotcola() {
		return totcola;
	}





	public void setTotcola(java.math.BigDecimal totcola) {
		this.totcola = totcola;
	}





	public java.math.BigDecimal getTotprovision() {
		return totprovision;
	}





	public void setTotprovision(java.math.BigDecimal totprovision) {
		this.totprovision = totprovision;
	}





	public String getKcarteraContrato() {
		return kcarteraContrato;
	}





	public void setKcarteraContrato(String kcarteraContrato) {
		this.kcarteraContrato = kcarteraContrato;
	}





	public String getSwcasado() {
		return swcasado;
	}





	public void setSwcasado(String swcasado) {
		this.swcasado = swcasado;
	}





	public void setBt(String bt) {
		this.bt = bt;
	}





	public Timestamp getFeccierre() {
		return feccierre;
	}





	public void setFeccierre(Timestamp feccierre) {
		this.feccierre = feccierre;
	}





	public String getCnegocio() {
		return cnegocio;
	}





	public void setCnegocio(String cnegocio) {
		this.cnegocio = cnegocio;
	}





	public Integer getCcanal() {
		return ccanal;
	}





	public void setCcanal(Integer ccanal) {
		this.ccanal = ccanal;
	}





	public String getKramo() {
		return kramo;
	}





	public void setKramo(String kramo) {
		this.kramo = kramo;
	}





	public Integer getKmodalidad() {
		return kmodalidad;
	}





	public void setKmodalidad(Integer kmodalidad) {
		this.kmodalidad = kmodalidad;
	}






	public String getSegmento1() {
		return segmento1;
	}





	public void setSegmento1(String segmento1) {
		this.segmento1 = segmento1;
	}





	public String getTiposubriesgo() {
		return tiposubriesgo;
	}





	public void setTiposubriesgo(String tiposubriesgo) {
		this.tiposubriesgo = tiposubriesgo;
	}





	public String getKcarterainv() {
		return kcarterainv;
	}





	public void setKcarterainv(String kcarterainv) {
		this.kcarterainv = kcarterainv;
	}





	public String getGapact() {
		return gapact;
	}





	public void setGapact(String gapact) {
		this.gapact = gapact;
	}



	public Long getKpoliza() {
		return kpoliza;
	}





	public void setKpoliza(Long kpoliza) {
		this.kpoliza = kpoliza;
	}





	public Integer getKsubpoliza() {
		return ksubpoliza;
	}





	public void setKsubpoliza(Integer ksubpoliza) {
		this.ksubpoliza = ksubpoliza;
	}





	public Integer getNsuscri() {
		return nsuscri;
	}





	public void setNsuscri(Integer nsuscri) {
		this.nsuscri = nsuscri;
	}





	public Timestamp getFinisusc() {
		return finisusc;
	}





	public void setFinisusc(Timestamp finisusc) {
		this.finisusc = finisusc;
	}



	public Timestamp getFecdesde() {
		return fecdesde;
	}





	public void setFecdesde(Timestamp fecdesde) {
		this.fecdesde = fecdesde;
	}




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result + ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result + ((feccierre == null) ? 0 : feccierre.hashCode());
		result = prime * result + ((fecdesde == null) ? 0 : fecdesde.hashCode());
		result = prime * result + ((finisusc == null) ? 0 : finisusc.hashCode());
		result = prime * result + ((gapact == null) ? 0 : gapact.hashCode());
		result = prime * result + ((itcalc == null) ? 0 : itcalc.hashCode());
		result = prime * result + ((kcarteraContrato == null) ? 0 : kcarteraContrato.hashCode());
		result = prime * result + ((kcarterainv == null) ? 0 : kcarterainv.hashCode());
		result = prime * result + ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kmodext == null) ? 0 : kmodext.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result + ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
		result = prime * result + ((nsuscri == null) ? 0 : nsuscri.hashCode());
		result = prime * result + ((segmento1 == null) ? 0 : segmento1.hashCode());
		result = prime * result + ((swcasado == null) ? 0 : swcasado.hashCode());
		result = prime * result + ((tiposubriesgo == null) ? 0 : tiposubriesgo.hashCode());
		result = prime * result + ((totcola == null) ? 0 : totcola.hashCode());
		result = prime * result + ((totcolacom == null) ? 0 : totcolacom.hashCode());
		result = prime * result + ((totcolafall == null) ? 0 : totcolafall.hashCode());
		result = prime * result + ((totcolagto == null) ? 0 : totcolagto.hashCode());
		result = prime * result + ((totcolagtoad == null) ? 0 : totcolagtoad.hashCode());
		result = prime * result + ((totcolaprestaciones == null) ? 0 : totcolaprestaciones.hashCode());
		result = prime * result + ((totcolaprim == null) ? 0 : totcolaprim.hashCode());
		result = prime * result + ((totcolacompl == null) ? 0 : totcolacompl.hashCode());
		result = prime * result + ((totcolarte == null) ? 0 : totcolarte.hashCode());
		result = prime * result + ((totcolavida == null) ? 0 : totcolavida.hashCode());
		result = prime * result + ((totfactcom == null) ? 0 : totfactcom.hashCode());
		result = prime * result + ((totfactcompl == null) ? 0 : totfactcompl.hashCode());
		result = prime * result + ((totfactfall == null) ? 0 : totfactfall.hashCode());
		result = prime * result + ((totfactgto == null) ? 0 : totfactgto.hashCode());
		result = prime * result + ((totfactgtoad == null) ? 0 : totfactgtoad.hashCode());
		result = prime * result + ((totfactprestaciones == null) ? 0 : totfactprestaciones.hashCode());
		result = prime * result + ((totfactprim == null) ? 0 : totfactprim.hashCode());
		result = prime * result + ((totfactrte == null) ? 0 : totfactrte.hashCode());
		result = prime * result + ((totfactvida == null) ? 0 : totfactvida.hashCode());
		result = prime * result + ((totfpcom == null) ? 0 : totfpcom.hashCode());
		result = prime * result + ((totfpcompl == null) ? 0 : totfpcompl.hashCode());
		result = prime * result + ((totfpfall == null) ? 0 : totfpfall.hashCode());
		result = prime * result + ((totfpgto == null) ? 0 : totfpgto.hashCode());
		result = prime * result + ((totfpgtoad == null) ? 0 : totfpgtoad.hashCode());
		result = prime * result + ((totfpprestaciones == null) ? 0 : totfpprestaciones.hashCode());
		result = prime * result + ((totfpprim == null) ? 0 : totfpprim.hashCode());
		result = prime * result + ((totfprob == null) ? 0 : totfprob.hashCode());
		result = prime * result + ((totfprte == null) ? 0 : totfprte.hashCode());
		result = prime * result + ((totfpvida == null) ? 0 : totfpvida.hashCode());
		result = prime * result + ((totprovision == null) ? 0 : totprovision.hashCode());
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
		FlujSuscri other = (FlujSuscri) obj;
		if (bt == null) {
			if (other.bt != null)
				return false;
		} else if (!bt.equals(other.bt))
			return false;
		if (ccanal == null) {
			if (other.ccanal != null)
				return false;
		} else if (!ccanal.equals(other.ccanal))
			return false;
		if (cnegocio == null) {
			if (other.cnegocio != null)
				return false;
		} else if (!cnegocio.equals(other.cnegocio))
			return false;
		if (feccierre == null) {
			if (other.feccierre != null)
				return false;
		} else if (!feccierre.equals(other.feccierre))
			return false;
		if (fecdesde == null) {
			if (other.fecdesde != null)
				return false;
		} else if (!fecdesde.equals(other.fecdesde))
			return false;
		if (finisusc == null) {
			if (other.finisusc != null)
				return false;
		} else if (!finisusc.equals(other.finisusc))
			return false;
		if (gapact == null) {
			if (other.gapact != null)
				return false;
		} else if (!gapact.equals(other.gapact))
			return false;
		if (itcalc == null) {
			if (other.itcalc != null)
				return false;
		} else if (!itcalc.equals(other.itcalc))
			return false;
		if (kcarteraContrato == null) {
			if (other.kcarteraContrato != null)
				return false;
		} else if (!kcarteraContrato.equals(other.kcarteraContrato))
			return false;
		if (kcarterainv == null) {
			if (other.kcarterainv != null)
				return false;
		} else if (!kcarterainv.equals(other.kcarterainv))
			return false;
		if (kgarantia == null) {
			if (other.kgarantia != null)
				return false;
		} else if (!kgarantia.equals(other.kgarantia))
			return false;
		if (kmodalidad == null) {
			if (other.kmodalidad != null)
				return false;
		} else if (!kmodalidad.equals(other.kmodalidad))
			return false;
		if (kmodext == null) {
			if (other.kmodext != null)
				return false;
		} else if (!kmodext.equals(other.kmodext))
			return false;
		if (kpoliza == null) {
			if (other.kpoliza != null)
				return false;
		} else if (!kpoliza.equals(other.kpoliza))
			return false;
		if (kramo == null) {
			if (other.kramo != null)
				return false;
		} else if (!kramo.equals(other.kramo))
			return false;
		if (ksubpoliza == null) {
			if (other.ksubpoliza != null)
				return false;
		} else if (!ksubpoliza.equals(other.ksubpoliza))
			return false;
		if (nsuscri == null) {
			if (other.nsuscri != null)
				return false;
		} else if (!nsuscri.equals(other.nsuscri))
			return false;
		if (segmento1 == null) {
			if (other.segmento1 != null)
				return false;
		} else if (!segmento1.equals(other.segmento1))
			return false;
		if (swcasado == null) {
			if (other.swcasado != null)
				return false;
		} else if (!swcasado.equals(other.swcasado))
			return false;
		if (tiposubriesgo == null) {
			if (other.tiposubriesgo != null)
				return false;
		} else if (!tiposubriesgo.equals(other.tiposubriesgo))
			return false;
		if (totcola == null) {
			if (other.totcola != null)
				return false;
		} else if (!totcola.equals(other.totcola))
			return false;
		if (totcolacom == null) {
			if (other.totcolacom != null)
				return false;
		} else if (!totcolacom.equals(other.totcolacom))
			return false;
		if (totcolafall == null) {
			if (other.totcolafall != null)
				return false;
		} else if (!totcolafall.equals(other.totcolafall))
			return false;
		if (totcolagto == null) {
			if (other.totcolagto != null)
				return false;
		} else if (!totcolagto.equals(other.totcolagto))
			return false;
		if (totcolagtoad == null) {
			if (other.totcolagtoad != null)
				return false;
		} else if (!totcolagtoad.equals(other.totcolagtoad))
			return false;
		if (totcolaprestaciones == null) {
			if (other.totcolaprestaciones != null)
				return false;
		} else if (!totcolaprestaciones.equals(other.totcolaprestaciones))
			return false;
		if (totcolaprim == null) {
			if (other.totcolaprim != null)
				return false;
		} else if (!totcolaprim.equals(other.totcolaprim))
			return false;
		if (totcolacompl == null) {
			if (other.totcolacompl != null)
				return false;
		} else if (!totcolacompl.equals(other.totcolacompl))
			return false;
		if (totcolarte == null) {
			if (other.totcolarte != null)
				return false;
		} else if (!totcolarte.equals(other.totcolarte))
			return false;
		if (totcolavida == null) {
			if (other.totcolavida != null)
				return false;
		} else if (!totcolavida.equals(other.totcolavida))
			return false;
		if (totfactcom == null) {
			if (other.totfactcom != null)
				return false;
		} else if (!totfactcom.equals(other.totfactcom))
			return false;
		if (totfactcompl == null) {
			if (other.totfactcompl != null)
				return false;
		} else if (!totfactcompl.equals(other.totfactcompl))
			return false;
		if (totfactfall == null) {
			if (other.totfactfall != null)
				return false;
		} else if (!totfactfall.equals(other.totfactfall))
			return false;
		if (totfactgto == null) {
			if (other.totfactgto != null)
				return false;
		} else if (!totfactgto.equals(other.totfactgto))
			return false;
		if (totfactgtoad == null) {
			if (other.totfactgtoad != null)
				return false;
		} else if (!totfactgtoad.equals(other.totfactgtoad))
			return false;
		if (totfactprestaciones == null) {
			if (other.totfactprestaciones != null)
				return false;
		} else if (!totfactprestaciones.equals(other.totfactprestaciones))
			return false;
		if (totfactprim == null) {
			if (other.totfactprim != null)
				return false;
		} else if (!totfactprim.equals(other.totfactprim))
			return false;
		if (totfactrte == null) {
			if (other.totfactrte != null)
				return false;
		} else if (!totfactrte.equals(other.totfactrte))
			return false;
		if (totfactvida == null) {
			if (other.totfactvida != null)
				return false;
		} else if (!totfactvida.equals(other.totfactvida))
			return false;
		if (totfpcom == null) {
			if (other.totfpcom != null)
				return false;
		} else if (!totfpcom.equals(other.totfpcom))
			return false;
		if (totfpcompl == null) {
			if (other.totfpcompl != null)
				return false;
		} else if (!totfpcompl.equals(other.totfpcompl))
			return false;
		if (totfpfall == null) {
			if (other.totfpfall != null)
				return false;
		} else if (!totfpfall.equals(other.totfpfall))
			return false;
		if (totfpgto == null) {
			if (other.totfpgto != null)
				return false;
		} else if (!totfpgto.equals(other.totfpgto))
			return false;
		if (totfpgtoad == null) {
			if (other.totfpgtoad != null)
				return false;
		} else if (!totfpgtoad.equals(other.totfpgtoad))
			return false;
		if (totfpprestaciones == null) {
			if (other.totfpprestaciones != null)
				return false;
		} else if (!totfpprestaciones.equals(other.totfpprestaciones))
			return false;
		if (totfpprim == null) {
			if (other.totfpprim != null)
				return false;
		} else if (!totfpprim.equals(other.totfpprim))
			return false;
		if (totfprob == null) {
			if (other.totfprob != null)
				return false;
		} else if (!totfprob.equals(other.totfprob))
			return false;
		if (totfprte == null) {
			if (other.totfprte != null)
				return false;
		} else if (!totfprte.equals(other.totfprte))
			return false;
		if (totfpvida == null) {
			if (other.totfpvida != null)
				return false;
		} else if (!totfpvida.equals(other.totfpvida))
			return false;
		if (totprovision == null) {
			if (other.totprovision != null)
				return false;
		} else if (!totprovision.equals(other.totprovision))
			return false;
		return true;
	}



	


	@Override
	public String toString() {
		return "FlujSuscri [bt=" + bt + ", feccierre=" + feccierre + ", cnegocio=" + cnegocio + ", ccanal=" + ccanal
				+ ", kramo=" + kramo + ", kmodalidad=" + kmodalidad + ", segmento1=" + segmento1 + ", tiposubriesgo="
				+ tiposubriesgo + ", kcarterainv=" + kcarterainv + ", gapact=" + gapact + ", kmodext=" + kmodext
				+ ", kgarantia=" + kgarantia + ", kpoliza=" + kpoliza + ", ksubpoliza=" + ksubpoliza + ", nsuscri="
				+ nsuscri + ", finisusc=" + finisusc + ", itcalc=" + itcalc + ", fecdesde=" + fecdesde
				+ ", totcolavida=" + totcolavida + ", totfpvida=" + totfpvida + ", totfactvida=" + totfactvida
				+ ", totfpfall=" + totfpfall + ", totfactfall=" + totfactfall + ", totcolafall=" + totcolafall
				+ ", totfpcompl=" + totfpcompl + ", totfactcompl=" + totfactcompl + ", totcolaqcompl=" + totcolacompl
				+ ", totfprte=" + totfprte + ", totfactrte=" + totfactrte + ", totcolarte=" + totcolarte + ", totfpgto="
				+ totfpgto + ", totfactgto=" + totfactgto + ", totcolagto=" + totcolagto + ", totfpcom=" + totfpcom
				+ ", totfactcom=" + totfactcom + ", totcolacom=" + totcolacom + ", totfpgtoad=" + totfpgtoad
				+ ", totfactgtoad=" + totfactgtoad + ", totcolagtoad=" + totcolagtoad + ", totfpprim=" + totfpprim
				+ ", totfactprim=" + totfactprim + ", totcolaprim=" + totcolaprim + ", totcolaprestaciones="
				+ totcolaprestaciones + ", totfpprestaciones=" + totfpprestaciones + ", totfactprestaciones="
				+ totfactprestaciones + ", totfprob=" + totfprob + ", totcola=" + totcola + ", totprovision="
				+ totprovision + ", kcarteraContrato=" + kcarteraContrato + ", swcasado=" + swcasado + "]";
	}





	@Override
	public FlujSuscriKey getKey() {
		return new FlujSuscriKey(bt, feccierre, cnegocio, ccanal, gapact, kramo, kmodalidad, segmento1, tiposubriesgo, kcarterainv, kpoliza, ksubpoliza, nsuscri, fecdesde, kgarantia);
	}

}
