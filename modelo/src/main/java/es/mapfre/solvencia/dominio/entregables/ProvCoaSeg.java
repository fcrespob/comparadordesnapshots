/* MODIFICACION:MU-2019-053081: Se incluyen campos tabla1 y gestionit en la clave
   FECHA: 26/08/2019
   AUTOR: INDRA
*/

package es.mapfre.solvencia.dominio.entregables;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.entregables.ProvCoaSegKey;
import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.dominio.EntidadConBaseTec;

@Portable
public class ProvCoaSeg implements EntidadBase<ProvCoaSegKey>, EntidadConBaseTec {

	public static final int IND_BT = 0;
	public static final int IND_FECCIERRE = 1;
	public static final int IND_CNEGOCIO = 2;
	public static final int IND_CCANAL = 3;
	public static final int IND_KRAMO = 4;
	public static final int IND_KMODALIDAD = 5;
	public static final int IND_SEGMENTO1 = 6;
	public static final int IND_TIPOSUBRIESGO = 7;
	public static final int IND_KPOLIZA = 8;
	public static final int IND_KSUBPOLIZA = 9;
	public static final int IND_NSUSCRI = 10;
	public static final int IND_FINISUSC = 11;
	public static final int IND_FECFINTRAMO1 = 12;
	public static final int IND_KCARTERAINV = 13;
	public static final int IND_GAPACT = 14;
	public static final int IND_GESTIONIT = 15;
	public static final int IND_PCOASE = 16;
	public static final int IND_DISTINT = 17;
	public static final int IND_PINTERTECN1 = 18;
	public static final int IND_DURTRCASADO = 19;
	public static final int IND_PINTERTECN2 = 20;
	public static final int IND_TABLA1 = 21;
	public static final int IND_PGASTGESIN1 = 22;
	public static final int IND_PGASTGESIN2 = 23;
	public static final int IND_FACTOR1 = 24;
	public static final int IND_GASTREALUNITARIO = 25;
	public static final int IND_GASTREAL = 26;
	public static final int IND_IPC = 27;
	public static final int IND_CURVATI = 28;
	public static final int IND_TOTFLUJOACTSINGASTOS = 29;
	public static final int IND_TOTFLUJOACTCONGASTOS = 30;
	public static final int IND_KCUADRO = 31;
	public static final int IND_KCOASE1 = 32;
	public static final int IND_KCOASE2 = 33;
	public static final int IND_KCOASE3 = 34;
	public static final int IND_KCOASE4 = 35;
	public static final int IND_KCOASE5 = 36;
	public static final int IND_KCOASE6 = 37;
	public static final int IND_KCOASE7 = 38;
	public static final int IND_KCOASE8 = 39;
	public static final int IND_KCOASE9 = 40;
	public static final int IND_KCOASE10 = 41;
	public static final int IND_KCOASE11 = 42;
	public static final int IND_KCOASE12 = 43;
	public static final int IND_KCOASE13 = 44;
	public static final int IND_KCOASE14 = 45;
	public static final int IND_KCOASE15 = 46;
	public static final int IND_KCOASE16 = 47;
	public static final int IND_KCOASE17 = 48;
	public static final int IND_KCOASE18 = 49;
	public static final int IND_KCOASE19 = 50;
	public static final int IND_KCOASE20 = 51;

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
	@PortableProperty(IND_KPOLIZA)
	private Long kpoliza;
	@PortableProperty(IND_KSUBPOLIZA)
	private Integer ksubpoliza;
	@PortableProperty(IND_NSUSCRI)
	private Integer nsuscri;
	@PortableProperty(IND_FINISUSC)
	private Timestamp finisusc;
	@PortableProperty(IND_FECFINTRAMO1)
	private Timestamp fecfintramo1;
	@PortableProperty(IND_KCARTERAINV)
	private String kcarterainv;
	@PortableProperty(IND_GAPACT)
	private String gapact;
	@PortableProperty(IND_GESTIONIT)
	private String gestionit;
	@PortableProperty(value = IND_PCOASE, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal pcoase;
	@PortableProperty(value = IND_DISTINT, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal distint;
	@PortableProperty(value = IND_PINTERTECN1, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal pintertecn1;
	@PortableProperty(value = IND_DURTRCASADO, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal durtrcasado;
	@PortableProperty(value = IND_PINTERTECN2, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal pintertecn2;
	@PortableProperty(IND_TABLA1)
	private String tabla1;
	@PortableProperty(value = IND_PGASTGESIN1, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal pgastgesin1;
	@PortableProperty(value = IND_PGASTGESIN2, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal pgastgesin2;
	@PortableProperty(value = IND_FACTOR1, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal factor1;
	@PortableProperty(value = IND_GASTREALUNITARIO, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal gastrealunitario;
	@PortableProperty(value = IND_GASTREAL, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal gastreal;
	@PortableProperty(value = IND_IPC, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal ipc;
	@PortableProperty(IND_CURVATI)
	private String curvati;
	@PortableProperty(value = IND_TOTFLUJOACTSINGASTOS, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal totflujoactsingastos;
	@PortableProperty(value = IND_TOTFLUJOACTCONGASTOS, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal totflujoactcongastos;
	@PortableProperty(IND_KCUADRO)
	private Integer kcuadro;
	@PortableProperty(IND_KCOASE1)
	private String kcoase1;
	@PortableProperty(IND_KCOASE2)
	private String kcoase2;
	@PortableProperty(IND_KCOASE3)
	private String kcoase3;
	@PortableProperty(IND_KCOASE4)
	private String kcoase4;
	@PortableProperty(IND_KCOASE5)
	private String kcoase5;
	@PortableProperty(IND_KCOASE6)
	private String kcoase6;
	@PortableProperty(IND_KCOASE7)
	private String kcoase7;
	@PortableProperty(IND_KCOASE8)
	private String kcoase8;
	@PortableProperty(IND_KCOASE9)
	private String kcoase9;
	@PortableProperty(IND_KCOASE10)
	private String kcoase10;
	@PortableProperty(IND_KCOASE11)
	private String kcoase11;
	@PortableProperty(IND_KCOASE12)
	private String kcoase12;
	@PortableProperty(IND_KCOASE13)
	private String kcoase13;
	@PortableProperty(IND_KCOASE14)
	private String kcoase14;
	@PortableProperty(IND_KCOASE15)
	private String kcoase15;
	@PortableProperty(IND_KCOASE16)
	private String kcoase16;
	@PortableProperty(IND_KCOASE17)
	private String kcoase17;
	@PortableProperty(IND_KCOASE18)
	private String kcoase18;
	@PortableProperty(IND_KCOASE19)
	private String kcoase19;
	@PortableProperty(IND_KCOASE20)
	private String kcoase20;

	public String getBt() {
		return bt;
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

	public Timestamp getFecfintramo1() {
		return fecfintramo1;
	}

	public void setFecfintramo1(Timestamp fecfintramo1) {
		this.fecfintramo1 = fecfintramo1;
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

	public String getGestionit() {
		return gestionit;
	}

	public void setGestionit(String gestionit) {
		this.gestionit = gestionit;
	}

	public BigDecimal getPcoase() {
		return pcoase;
	}

	public void setPcoase(BigDecimal pcoase) {
		this.pcoase = pcoase;
	}

	public BigDecimal getDistint() {
		return distint;
	}

	public void setDistint(BigDecimal distint) {
		this.distint = distint;
	}

	public BigDecimal getPintertecn1() {
		return pintertecn1;
	}

	public void setPintertecn1(BigDecimal pintertecn1) {
		this.pintertecn1 = pintertecn1;
	}

	public BigDecimal getDurtrcasado() {
		return durtrcasado;
	}

	public void setDurtrcasado(BigDecimal durtrcasado) {
		this.durtrcasado = durtrcasado;
	}

	public BigDecimal getPintertecn2() {
		return pintertecn2;
	}

	public void setPintertecn2(BigDecimal pintertecn2) {
		this.pintertecn2 = pintertecn2;
	}

	public String getTabla1() {
		return tabla1;
	}

	public void setTabla1(String tabla1) {
		this.tabla1 = tabla1;
	}

	public BigDecimal getPgastgesin1() {
		return pgastgesin1;
	}

	public void setPgastgesin1(BigDecimal pgastgesin1) {
		this.pgastgesin1 = pgastgesin1;
	}

	public BigDecimal getPgastgesin2() {
		return pgastgesin2;
	}

	public void setPgastgesin2(BigDecimal pgastgesin2) {
		this.pgastgesin2 = pgastgesin2;
	}

	public BigDecimal getFactor1() {
		return factor1;
	}

	public void setFactor1(BigDecimal factor1) {
		this.factor1 = factor1;
	}

	public BigDecimal getGastrealunitario() {
		return gastrealunitario;
	}

	public void setGastrealunitario(BigDecimal gastrealunitario) {
		this.gastrealunitario = gastrealunitario;
	}

	public BigDecimal getGastreal() {
		return gastreal;
	}

	public void setGastreal(BigDecimal gastreal) {
		this.gastreal = gastreal;
	}

	public BigDecimal getIpc() {
		return ipc;
	}

	public void setIpc(BigDecimal ipc) {
		this.ipc = ipc;
	}

	public String getCurvati() {
		return curvati;
	}

	public void setCurvati(String curvati) {
		this.curvati = curvati;
	}

	public BigDecimal getTotflujoactsingastos() {
		return totflujoactsingastos;
	}

	public void setTotflujoactsingastos(BigDecimal totflujoactsingastos) {
		this.totflujoactsingastos = totflujoactsingastos;
	}

	public BigDecimal getTotflujoactcongastos() {
		return totflujoactcongastos;
	}

	public void setTotflujoactcongastos(BigDecimal totflujoactcongastos) {
		this.totflujoactcongastos = totflujoactcongastos;
	}

	public Integer getKcuadro() {
		return kcuadro;
	}

	public void setKcuadro(Integer kcuadro) {
		this.kcuadro = kcuadro;
	}

	public String getKcoase1() {
		return kcoase1;
	}

	public void setKcoase1(String kcoase1) {
		this.kcoase1 = kcoase1;
	}

	public String getKcoase2() {
		return kcoase2;
	}

	public void setKcoase2(String kcoase2) {
		this.kcoase2 = kcoase2;
	}

	public String getKcoase3() {
		return kcoase3;
	}

	public void setKcoase3(String kcoase3) {
		this.kcoase3 = kcoase3;
	}

	public String getKcoase4() {
		return kcoase4;
	}

	public void setKcoase4(String kcoase4) {
		this.kcoase4 = kcoase4;
	}

	public String getKcoase5() {
		return kcoase5;
	}

	public void setKcoase5(String kcoase5) {
		this.kcoase5 = kcoase5;
	}

	public String getKcoase6() {
		return kcoase6;
	}

	public void setKcoase6(String kcoase6) {
		this.kcoase6 = kcoase6;
	}

	public String getKcoase7() {
		return kcoase7;
	}

	public void setKcoase7(String kcoase7) {
		this.kcoase7 = kcoase7;
	}

	public String getKcoase8() {
		return kcoase8;
	}

	public void setKcoase8(String kcoase8) {
		this.kcoase8 = kcoase8;
	}

	public String getKcoase9() {
		return kcoase9;
	}

	public void setKcoase9(String kcoase9) {
		this.kcoase9 = kcoase9;
	}

	public String getKcoase10() {
		return kcoase10;
	}

	public void setKcoase10(String kcoase10) {
		this.kcoase10 = kcoase10;
	}

	public String getKcoase11() {
		return kcoase11;
	}

	public void setKcoase11(String kcoase11) {
		this.kcoase11 = kcoase11;
	}

	public String getKcoase12() {
		return kcoase12;
	}

	public void setKcoase12(String kcoase12) {
		this.kcoase12 = kcoase12;
	}

	public String getKcoase13() {
		return kcoase13;
	}

	public void setKcoase13(String kcoase13) {
		this.kcoase13 = kcoase13;
	}

	public String getKcoase14() {
		return kcoase14;
	}

	public void setKcoase14(String kcoase14) {
		this.kcoase14 = kcoase14;
	}

	public String getKcoase15() {
		return kcoase15;
	}

	public void setKcoase15(String kcoase15) {
		this.kcoase15 = kcoase15;
	}

	public String getKcoase16() {
		return kcoase16;
	}

	public void setKcoase16(String kcoase16) {
		this.kcoase16 = kcoase16;
	}

	public String getKcoase17() {
		return kcoase17;
	}

	public void setKcoase17(String kcoase17) {
		this.kcoase17 = kcoase17;
	}

	public String getKcoase18() {
		return kcoase18;
	}

	public void setKcoase18(String kcoase18) {
		this.kcoase18 = kcoase18;
	}

	public String getKcoase19() {
		return kcoase19;
	}

	public void setKcoase19(String kcoase19) {
		this.kcoase19 = kcoase19;
	}

	public String getKcoase20() {
		return kcoase20;
	}

	public void setKcoase20(String kcoase20) {
		this.kcoase20 = kcoase20;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result + ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result + ((curvati == null) ? 0 : curvati.hashCode());
		result = prime * result + ((distint == null) ? 0 : distint.hashCode());
		result = prime * result + ((durtrcasado == null) ? 0 : durtrcasado.hashCode());
		result = prime * result + ((factor1 == null) ? 0 : factor1.hashCode());
		result = prime * result + ((feccierre == null) ? 0 : feccierre.hashCode());
		result = prime * result + ((fecfintramo1 == null) ? 0 : fecfintramo1.hashCode());
		result = prime * result + ((finisusc == null) ? 0 : finisusc.hashCode());
		result = prime * result + ((gapact == null) ? 0 : gapact.hashCode());
		result = prime * result + ((gastreal == null) ? 0 : gastreal.hashCode());
		result = prime * result + ((gastrealunitario == null) ? 0 : gastrealunitario.hashCode());
		result = prime * result + ((gestionit == null) ? 0 : gestionit.hashCode());
		result = prime * result + ((ipc == null) ? 0 : ipc.hashCode());
		result = prime * result + ((kcarterainv == null) ? 0 : kcarterainv.hashCode());
		result = prime * result + ((kcoase1 == null) ? 0 : kcoase1.hashCode());
		result = prime * result + ((kcoase10 == null) ? 0 : kcoase10.hashCode());
		result = prime * result + ((kcoase11 == null) ? 0 : kcoase11.hashCode());
		result = prime * result + ((kcoase12 == null) ? 0 : kcoase12.hashCode());
		result = prime * result + ((kcoase13 == null) ? 0 : kcoase13.hashCode());
		result = prime * result + ((kcoase14 == null) ? 0 : kcoase14.hashCode());
		result = prime * result + ((kcoase15 == null) ? 0 : kcoase15.hashCode());
		result = prime * result + ((kcoase16 == null) ? 0 : kcoase16.hashCode());
		result = prime * result + ((kcoase17 == null) ? 0 : kcoase17.hashCode());
		result = prime * result + ((kcoase18 == null) ? 0 : kcoase18.hashCode());
		result = prime * result + ((kcoase19 == null) ? 0 : kcoase19.hashCode());
		result = prime * result + ((kcoase2 == null) ? 0 : kcoase2.hashCode());
		result = prime * result + ((kcoase20 == null) ? 0 : kcoase20.hashCode());
		result = prime * result + ((kcoase3 == null) ? 0 : kcoase3.hashCode());
		result = prime * result + ((kcoase4 == null) ? 0 : kcoase4.hashCode());
		result = prime * result + ((kcoase5 == null) ? 0 : kcoase5.hashCode());
		result = prime * result + ((kcoase6 == null) ? 0 : kcoase6.hashCode());
		result = prime * result + ((kcoase7 == null) ? 0 : kcoase7.hashCode());
		result = prime * result + ((kcoase8 == null) ? 0 : kcoase8.hashCode());
		result = prime * result + ((kcoase9 == null) ? 0 : kcoase9.hashCode());
		result = prime * result + ((kcuadro == null) ? 0 : kcuadro.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result + ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
		result = prime * result + ((nsuscri == null) ? 0 : nsuscri.hashCode());
		result = prime * result + ((pcoase == null) ? 0 : pcoase.hashCode());
		result = prime * result + ((pgastgesin1 == null) ? 0 : pgastgesin1.hashCode());
		result = prime * result + ((pgastgesin2 == null) ? 0 : pgastgesin2.hashCode());
		result = prime * result + ((pintertecn1 == null) ? 0 : pintertecn1.hashCode());
		result = prime * result + ((pintertecn2 == null) ? 0 : pintertecn2.hashCode());
		result = prime * result + ((segmento1 == null) ? 0 : segmento1.hashCode());
		result = prime * result + ((tabla1 == null) ? 0 : tabla1.hashCode());
		result = prime * result + ((tiposubriesgo == null) ? 0 : tiposubriesgo.hashCode());
		result = prime * result + ((totflujoactcongastos == null) ? 0 : totflujoactcongastos.hashCode());
		result = prime * result + ((totflujoactsingastos == null) ? 0 : totflujoactsingastos.hashCode());
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
		ProvCoaSeg other = (ProvCoaSeg) obj;
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
		if (curvati == null) {
			if (other.curvati != null)
				return false;
		} else if (!curvati.equals(other.curvati))
			return false;
		if (distint == null) {
			if (other.distint != null)
				return false;
		} else if (!distint.equals(other.distint))
			return false;
		if (durtrcasado == null) {
			if (other.durtrcasado != null)
				return false;
		} else if (!durtrcasado.equals(other.durtrcasado))
			return false;
		if (factor1 == null) {
			if (other.factor1 != null)
				return false;
		} else if (!factor1.equals(other.factor1))
			return false;
		if (feccierre == null) {
			if (other.feccierre != null)
				return false;
		} else if (!feccierre.equals(other.feccierre))
			return false;
		if (fecfintramo1 == null) {
			if (other.fecfintramo1 != null)
				return false;
		} else if (!fecfintramo1.equals(other.fecfintramo1))
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
		if (gastreal == null) {
			if (other.gastreal != null)
				return false;
		} else if (!gastreal.equals(other.gastreal))
			return false;
		if (gastrealunitario == null) {
			if (other.gastrealunitario != null)
				return false;
		} else if (!gastrealunitario.equals(other.gastrealunitario))
			return false;
		if (gestionit == null) {
			if (other.gestionit != null)
				return false;
		} else if (!gestionit.equals(other.gestionit))
			return false;
		if (ipc == null) {
			if (other.ipc != null)
				return false;
		} else if (!ipc.equals(other.ipc))
			return false;
		if (kcarterainv == null) {
			if (other.kcarterainv != null)
				return false;
		} else if (!kcarterainv.equals(other.kcarterainv))
			return false;
		if (kcoase1 == null) {
			if (other.kcoase1 != null)
				return false;
		} else if (!kcoase1.equals(other.kcoase1))
			return false;
		if (kcoase10 == null) {
			if (other.kcoase10 != null)
				return false;
		} else if (!kcoase10.equals(other.kcoase10))
			return false;
		if (kcoase11 == null) {
			if (other.kcoase11 != null)
				return false;
		} else if (!kcoase11.equals(other.kcoase11))
			return false;
		if (kcoase12 == null) {
			if (other.kcoase12 != null)
				return false;
		} else if (!kcoase12.equals(other.kcoase12))
			return false;
		if (kcoase13 == null) {
			if (other.kcoase13 != null)
				return false;
		} else if (!kcoase13.equals(other.kcoase13))
			return false;
		if (kcoase14 == null) {
			if (other.kcoase14 != null)
				return false;
		} else if (!kcoase14.equals(other.kcoase14))
			return false;
		if (kcoase15 == null) {
			if (other.kcoase15 != null)
				return false;
		} else if (!kcoase15.equals(other.kcoase15))
			return false;
		if (kcoase16 == null) {
			if (other.kcoase16 != null)
				return false;
		} else if (!kcoase16.equals(other.kcoase16))
			return false;
		if (kcoase17 == null) {
			if (other.kcoase17 != null)
				return false;
		} else if (!kcoase17.equals(other.kcoase17))
			return false;
		if (kcoase18 == null) {
			if (other.kcoase18 != null)
				return false;
		} else if (!kcoase18.equals(other.kcoase18))
			return false;
		if (kcoase19 == null) {
			if (other.kcoase19 != null)
				return false;
		} else if (!kcoase19.equals(other.kcoase19))
			return false;
		if (kcoase2 == null) {
			if (other.kcoase2 != null)
				return false;
		} else if (!kcoase2.equals(other.kcoase2))
			return false;
		if (kcoase20 == null) {
			if (other.kcoase20 != null)
				return false;
		} else if (!kcoase20.equals(other.kcoase20))
			return false;
		if (kcoase3 == null) {
			if (other.kcoase3 != null)
				return false;
		} else if (!kcoase3.equals(other.kcoase3))
			return false;
		if (kcoase4 == null) {
			if (other.kcoase4 != null)
				return false;
		} else if (!kcoase4.equals(other.kcoase4))
			return false;
		if (kcoase5 == null) {
			if (other.kcoase5 != null)
				return false;
		} else if (!kcoase5.equals(other.kcoase5))
			return false;
		if (kcoase6 == null) {
			if (other.kcoase6 != null)
				return false;
		} else if (!kcoase6.equals(other.kcoase6))
			return false;
		if (kcoase7 == null) {
			if (other.kcoase7 != null)
				return false;
		} else if (!kcoase7.equals(other.kcoase7))
			return false;
		if (kcoase8 == null) {
			if (other.kcoase8 != null)
				return false;
		} else if (!kcoase8.equals(other.kcoase8))
			return false;
		if (kcoase9 == null) {
			if (other.kcoase9 != null)
				return false;
		} else if (!kcoase9.equals(other.kcoase9))
			return false;
		if (kcuadro == null) {
			if (other.kcuadro != null)
				return false;
		} else if (!kcuadro.equals(other.kcuadro))
			return false;
		if (kmodalidad == null) {
			if (other.kmodalidad != null)
				return false;
		} else if (!kmodalidad.equals(other.kmodalidad))
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
		if (pcoase == null) {
			if (other.pcoase != null)
				return false;
		} else if (!pcoase.equals(other.pcoase))
			return false;
		if (pgastgesin1 == null) {
			if (other.pgastgesin1 != null)
				return false;
		} else if (!pgastgesin1.equals(other.pgastgesin1))
			return false;
		if (pgastgesin2 == null) {
			if (other.pgastgesin2 != null)
				return false;
		} else if (!pgastgesin2.equals(other.pgastgesin2))
			return false;
		if (pintertecn1 == null) {
			if (other.pintertecn1 != null)
				return false;
		} else if (!pintertecn1.equals(other.pintertecn1))
			return false;
		if (pintertecn2 == null) {
			if (other.pintertecn2 != null)
				return false;
		} else if (!pintertecn2.equals(other.pintertecn2))
			return false;
		if (segmento1 == null) {
			if (other.segmento1 != null)
				return false;
		} else if (!segmento1.equals(other.segmento1))
			return false;
		if (tabla1 == null) {
			if (other.tabla1 != null)
				return false;
		} else if (!tabla1.equals(other.tabla1))
			return false;
		if (tiposubriesgo == null) {
			if (other.tiposubriesgo != null)
				return false;
		} else if (!tiposubriesgo.equals(other.tiposubriesgo))
			return false;
		if (totflujoactcongastos == null) {
			if (other.totflujoactcongastos != null)
				return false;
		} else if (!totflujoactcongastos.equals(other.totflujoactcongastos))
			return false;
		if (totflujoactsingastos == null) {
			if (other.totflujoactsingastos != null)
				return false;
		} else if (!totflujoactsingastos.equals(other.totflujoactsingastos))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProvCoaSeg [bt=" + bt + ", feccierre=" + feccierre + ", cnegocio=" + cnegocio + ", ccanal=" + ccanal
				+ ", kramo=" + kramo + ", kmodalidad=" + kmodalidad + ", segmento1=" + segmento1 + ", tiposubriesgo="
				+ tiposubriesgo + ", kpoliza=" + kpoliza + ", ksubpoliza=" + ksubpoliza + ", nsuscri=" + nsuscri
				+ ", finisusc=" + finisusc + ", fecfintramo1=" + fecfintramo1 + ", kcarterainv=" + kcarterainv
				+ ", gapact=" + gapact + ", gestionit=" + gestionit + ", pcoase=" + pcoase + ", distint=" + distint
				+ ", pintertecn1=" + pintertecn1 + ", durtrcasado=" + durtrcasado + ", pintertecn2=" + pintertecn2
				+ ", tabla1=" + tabla1 + ", pgastgesin1=" + pgastgesin1 + ", pgastgesin2=" + pgastgesin2 + ", factor1="
				+ factor1 + ", gastrealunitario=" + gastrealunitario + ", gastreal=" + gastreal + ", ipc=" + ipc
				+ ", curvati=" + curvati + ", totflujoactsingastos=" + totflujoactsingastos + ", totflujoactcongastos="
				+ totflujoactcongastos + ", kcuadro=" + kcuadro + ", kcoase1=" + kcoase1 + ", kcoase2=" + kcoase2
				+ ", kcoase3=" + kcoase3 + ", kcoase4=" + kcoase4 + ", kcoase5=" + kcoase5 + ", kcoase6=" + kcoase6
				+ ", kcoase7=" + kcoase7 + ", kcoase8=" + kcoase8 + ", kcoase9=" + kcoase9 + ", kcoase10=" + kcoase10
				+ ", kcoase11=" + kcoase11 + ", kcoase12=" + kcoase12 + ", kcoase13=" + kcoase13 + ", kcoase14="
				+ kcoase14 + ", kcoase15=" + kcoase15 + ", kcoase16=" + kcoase16 + ", kcoase17=" + kcoase17
				+ ", kcoase18=" + kcoase18 + ", kcoase19=" + kcoase19 + ", kcoase20=" + kcoase20 + "]";
	}

	@Override
	public ProvCoaSegKey getKey() {
		return new ProvCoaSegKey(bt, feccierre, cnegocio, ccanal, kramo, kmodalidad, segmento1, tiposubriesgo, kpoliza,
				ksubpoliza, nsuscri, kcarterainv, gapact, gestionit, tabla1);
	}

}
