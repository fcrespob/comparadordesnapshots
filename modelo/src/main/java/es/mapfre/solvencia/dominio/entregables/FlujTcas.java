package es.mapfre.solvencia.dominio.entregables;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.entregables.FlujTcasKey;
import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.dominio.EntidadConBaseTec;

@Portable
public class FlujTcas implements EntidadBase<FlujTcasKey>, EntidadConBaseTec {

	public static final int IND_BT = 0;
	public static final int IND_FECCIERRE = 1;
	public static final int IND_CNEGOCIO = 2;
	public static final int IND_CCANAL = 3;
	public static final int IND_KCARTERAINV = 4;
	public static final int IND_GAPACT = 5;
	public static final int IND_GESTIONIT = 6;
	public static final int IND_KRAMO = 7;
	public static final int IND_KMODALIDAD = 8;
	public static final int IND_KPOLIZA = 9;
	public static final int IND_KSUBPOLIZA = 10;
	public static final int IND_NSUSCRI = 11;
	public static final int IND_FSUSCRI = 12;
	public static final int IND_FINIT = 13;
	public static final int IND_IT2 = 14;
	public static final int IND_ITDGS = 15;
	public static final int IND_FECDESDE = 16;
	public static final int IND_IMPFLUJPROV = 17;
	public static final int IND_IMPFLUJACT = 18;

	@PortableProperty(IND_BT)
	private String bt;
	@PortableProperty(IND_FECCIERRE)
	private Timestamp feccierre;
	@PortableProperty(IND_CNEGOCIO)
	private String cnegocio;
	@PortableProperty(IND_CCANAL)
	private Integer ccanal;
	@PortableProperty(IND_KCARTERAINV)
	private String kcarterainv;
	@PortableProperty(IND_GAPACT)
	private String gapact;
	@PortableProperty(IND_GESTIONIT)
	private String gestionit;
	@PortableProperty(IND_KRAMO)
	private String kramo;
	@PortableProperty(IND_KMODALIDAD)
	private Integer kmodalidad;
	@PortableProperty(IND_KPOLIZA)
	private Long kpoliza;
	@PortableProperty(IND_KSUBPOLIZA)
	private Integer ksubpoliza;
	@PortableProperty(IND_NSUSCRI)
	private Integer nsuscri;
	@PortableProperty(IND_FSUSCRI)
	private Timestamp fsuscri;
	@PortableProperty(IND_FINIT)
	private Timestamp finit;
	@PortableProperty(value = IND_IT2, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal it2;
	@PortableProperty(value = IND_ITDGS, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal itdgs;
	@PortableProperty(IND_FECDESDE)
	private Timestamp fecdesde;
	@PortableProperty(value = IND_IMPFLUJPROV, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal impflujprob;
	@PortableProperty(value = IND_IMPFLUJACT, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal impflujact;


	

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




	public Timestamp getFsuscri() {
		return fsuscri;
	}




	public void setFsuscri(Timestamp fsuscri) {
		this.fsuscri = fsuscri;
	}




	public Timestamp getFinit() {
		return finit;
	}




	public void setFinit(Timestamp finit) {
		this.finit = finit;
	}




	public java.math.BigDecimal getIt2() {
		return it2;
	}




	public void setIt2(java.math.BigDecimal it2) {
		this.it2 = it2;
	}




	public java.math.BigDecimal getItdgs() {
		return itdgs;
	}




	public void setItdgs(java.math.BigDecimal itdgs) {
		this.itdgs = itdgs;
	}




	public Timestamp getFecdesde() {
		return fecdesde;
	}




	public void setFecdesde(Timestamp fecdesde) {
		this.fecdesde = fecdesde;
	}




	public java.math.BigDecimal getImpflujprob() {
		return impflujprob;
	}




	public void setImpflujprob(java.math.BigDecimal impflujprob) {
		this.impflujprob = impflujprob;
	}




	public java.math.BigDecimal getImpflujact() {
		return impflujact;
	}




	public void setImpflujact(java.math.BigDecimal impflujact) {
		this.impflujact = impflujact;
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
		result = prime * result + ((finit == null) ? 0 : finit.hashCode());
		result = prime * result + ((fsuscri == null) ? 0 : fsuscri.hashCode());
		result = prime * result + ((gapact == null) ? 0 : gapact.hashCode());
		result = prime * result + ((gestionit == null) ? 0 : gestionit.hashCode());
		result = prime * result + ((impflujact == null) ? 0 : impflujact.hashCode());
		result = prime * result + ((impflujprob == null) ? 0 : impflujprob.hashCode());
		result = prime * result + ((it2 == null) ? 0 : it2.hashCode());
		result = prime * result + ((itdgs == null) ? 0 : itdgs.hashCode());
		result = prime * result + ((kcarterainv == null) ? 0 : kcarterainv.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result + ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
		result = prime * result + ((nsuscri == null) ? 0 : nsuscri.hashCode());
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
		FlujTcas other = (FlujTcas) obj;
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
		if (finit == null) {
			if (other.finit != null)
				return false;
		} else if (!finit.equals(other.finit))
			return false;
		if (fsuscri == null) {
			if (other.fsuscri != null)
				return false;
		} else if (!fsuscri.equals(other.fsuscri))
			return false;
		if (gapact == null) {
			if (other.gapact != null)
				return false;
		} else if (!gapact.equals(other.gapact))
			return false;
		if (gestionit == null) {
			if (other.gestionit != null)
				return false;
		} else if (!gestionit.equals(other.gestionit))
			return false;
		if (impflujact == null) {
			if (other.impflujact != null)
				return false;
		} else if (!impflujact.equals(other.impflujact))
			return false;
		if (impflujprob == null) {
			if (other.impflujprob != null)
				return false;
		} else if (!impflujprob.equals(other.impflujprob))
			return false;
		if (it2 == null) {
			if (other.it2 != null)
				return false;
		} else if (!it2.equals(other.it2))
			return false;
		if (itdgs == null) {
			if (other.itdgs != null)
				return false;
		} else if (!itdgs.equals(other.itdgs))
			return false;
		if (kcarterainv == null) {
			if (other.kcarterainv != null)
				return false;
		} else if (!kcarterainv.equals(other.kcarterainv))
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
		return true;
	}


	


	@Override
	public String toString() {
		return "FlujTcas [bt=" + bt + ", feccierre=" + feccierre + ", cnegocio=" + cnegocio + ", ccanal=" + ccanal
				+ ", kcarterainv=" + kcarterainv + ", gapact=" + gapact + ", gestionit=" + gestionit + ", kramo="
				+ kramo + ", kmodalidad=" + kmodalidad + ", kpoliza=" + kpoliza + ", ksubpoliza=" + ksubpoliza
				+ ", nsuscri=" + nsuscri + ", fsuscri=" + fsuscri + ", finit=" + finit + ", it2=" + it2 + ", itdgs="
				+ itdgs + ", fecdesde=" + fecdesde + ", impflujprob=" + impflujprob + ", impflujact=" + impflujact
				+ "]";
	}




	@Override
	public FlujTcasKey getKey() {
		return new FlujTcasKey(bt, feccierre, cnegocio, ccanal, kcarterainv, gapact, kramo, kmodalidad, kpoliza,
				ksubpoliza, nsuscri, fecdesde);
	}

}
