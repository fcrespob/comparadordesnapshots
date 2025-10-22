package es.mapfre.solvencia.coherence.keys.entregables;

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.dominio.entregables.FlujosTN17;

@Portable
public class FlujosTN17Key implements Comparable<FlujosTN17Key> {

	@PortableProperty(FlujosTN17.IND_CNEGOCIO)
	private String cnegocio;
	@PortableProperty(FlujosTN17.IND_CCANAL)
	private Integer ccanal;
	@PortableProperty(FlujosTN17.IND_CCARTERA)
	private Integer ccartera;
	@PortableProperty(FlujosTN17.IND_FCIERRE)
	private Timestamp fcierre;
	@PortableProperty(FlujosTN17.IND_BT)
	private String bt;
	@PortableProperty(FlujosTN17.IND_UOA)
	private String uoa;
	@PortableProperty(FlujosTN17.IND_KCONTRATO)
	private String kcontrato;
	@PortableProperty(FlujosTN17.IND_KRAMO)
	private String kramo;
	@PortableProperty(FlujosTN17.IND_KMODALIDAD)
	private Integer kmodalidad;
	@PortableProperty(FlujosTN17.IND_KPOLIZA)
	private Long kpoliza;
	@PortableProperty(FlujosTN17.IND_KSUBPOLIZA)
	private Integer ksubpoliza;
	@PortableProperty(FlujosTN17.IND_KCERTIFICADO)
	private Integer kcertificado;
	@PortableProperty(FlujosTN17.IND_NSUSCRI)
	private Integer nsuscri;
	@PortableProperty(FlujosTN17.IND_NORDEN)
	private Integer norden;
	@PortableProperty(FlujosTN17.IND_KGARANTIA)
	private Integer kgarantia;
	@PortableProperty(FlujosTN17.IND_KPRESTACION)
	private String kprestacion;
	@PortableProperty(FlujosTN17.IND_KAJUSTE)
	private Integer kajuste;
	@PortableProperty(FlujosTN17.IND_CTIPOAPORT)
	private String ctipoaport;
	@PortableProperty(FlujosTN17.IND_KCARTERAINV)
	private String kcarterainv;
	@PortableProperty(FlujosTN17.IND_GAPACT)
	private String gapact;
	@PortableProperty(FlujosTN17.IND_KMODEXT)
	private Integer kmodext;
	@PortableProperty(FlujosTN17.IND_SPCOM)
	private String spcom;

	public FlujosTN17Key() {
		super();
	}

	public FlujosTN17Key(String cnegocio, Integer ccanal, Integer ccartera,
			Timestamp fcierre, String bt, String uoa, String kcontrato, String kramo, Integer kmodalidad,
			Long kpoliza, Integer ksubpoliza, Integer kcertificado,
			Integer nsuscri, Integer norden, Integer kgarantia,
			String kprestacion, Integer kajuste,String ctipoaport, String kcarterainv, String gapact, Integer kmodext, String spcom) {
		super();
		this.cnegocio = cnegocio;
		this.ccanal = ccanal;
		this.ccartera = ccartera;
		this.fcierre = fcierre;
		this.bt = bt;
		this.uoa = uoa;
		this.kcontrato = kcontrato;
		this.kramo = kramo;
		this.kmodalidad = kmodalidad;
		this.kpoliza = kpoliza;
		this.ksubpoliza = ksubpoliza;
		this.kcertificado = kcertificado;
		this.nsuscri = nsuscri;
		this.norden = norden;
		this.kgarantia = kgarantia;
		this.kprestacion = kprestacion;
		this.kajuste = kajuste;
		this.ctipoaport = ctipoaport;
		this.kcarterainv = kcarterainv;
		this.gapact = gapact;
		this.kmodext = kmodext;
		this.spcom = spcom;
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

	public Integer getCcartera() {
		return ccartera;
	}

	public void setCcartera(Integer ccartera) {
		this.ccartera = ccartera;
	}

	public Timestamp getFcierre() {
		return fcierre;
	}

	public void setFcierre(Timestamp fcierre) {
		this.fcierre = fcierre;
	}

	public String getBt() {
		return bt;
	}

	public void setBt(String bt) {
		this.bt = bt;
	}

	public String getUoa() {
		return uoa;
	}

	public void setUoa(String uoa) {
		this.uoa = uoa;
	}

	public String getKcontrato() {
		return kcontrato;
	}

	public void setKcontrato(String kcontrato) {
		this.kcontrato = kcontrato;
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

	public Integer getKcertificado() {
		return kcertificado;
	}

	public void setKcertificado(Integer kcertificado) {
		this.kcertificado = kcertificado;
	}

	public Integer getNsuscri() {
		return nsuscri;
	}

	public void setNsuscri(Integer nsuscri) {
		this.nsuscri = nsuscri;
	}

	public Integer getNorden() {
		return norden;
	}

	public void setNorden(Integer norden) {
		this.norden = norden;
	}

	public Integer getKgarantia() {
		return kgarantia;
	}

	public void setKgarantia(Integer kgarantia) {
		this.kgarantia = kgarantia;
	}

	public String getKprestacion() {
		return kprestacion;
	}

	public void setKprestacion(String kprestacion) {
		this.kprestacion = kprestacion;
	}
	public Integer getKajuste() {
		return kajuste;
	}

	public void setKajuste(Integer kajuste) {
		this.kajuste = kajuste;
	}

	public String getCtipoaport() {
		return ctipoaport;
	}

	public void setCtipoaport(String ctipoaport) {
		this.ctipoaport = ctipoaport;
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

	public Integer getKmodext() {
		return kmodext;
	}

	public void setKmodext(Integer kmodext) {
		this.kmodext = kmodext;
	}

	public String getSpcom() {
		return spcom;
	}

	public void setSpcom(String spcom) {
		this.spcom = spcom;
	}


	@Override
	public String toString() {
		return "FlujosTotNiif17Key [cnegocio=" + cnegocio + ", ccanal=" + ccanal + ", ccartera=" + ccartera
				+ ", fcierre=" + fcierre + ", bt=" + bt + ", uoa=" + uoa + ", kcontrato=" + kcontrato + ", kramo="
				+ kramo + ", kmodalidad=" + kmodalidad + ", kpoliza=" + kpoliza + ", ksubpoliza=" + ksubpoliza
				+ ", kcertificado=" + kcertificado + ", nsuscri=" + nsuscri + ", norden=" + norden + ", kgarantia="
				+ kgarantia + ", kprestacion=" + kprestacion + ", kajuste=" + kajuste + ", ctipoaport=" + ctipoaport +", kcarterainv=" + kcarterainv + ", gapact=" +
				gapact + ", kmodext=" + kmodext + ", spcom=" + spcom + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result + ((ccartera == null) ? 0 : ccartera.hashCode());
		result = prime * result + ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result + ((fcierre == null) ? 0 : fcierre.hashCode());
		result = prime * result + ((kcertificado == null) ? 0 : kcertificado.hashCode());
		result = prime * result + ((kcontrato == null) ? 0 : kcontrato.hashCode());
		result = prime * result + ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((kprestacion == null) ? 0 : kprestacion.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result + ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
		result = prime * result + ((norden == null) ? 0 : norden.hashCode());
		result = prime * result + ((nsuscri == null) ? 0 : nsuscri.hashCode());
		result = prime * result + ((uoa == null) ? 0 : uoa.hashCode());
		result = prime * result + ((kajuste == null) ? 0 : kajuste.hashCode());
		result = prime * result + ((ctipoaport == null) ? 0 : ctipoaport.hashCode());
		result = prime * result + ((kcarterainv == null) ? 0 : kcarterainv.hashCode());
		result = prime * result + ((gapact == null) ? 0 : gapact.hashCode());
		result = prime * result + ((kmodext == null) ? 0 : kmodext.hashCode());
		result = prime * result + ((spcom == null) ? 0 : spcom.hashCode());
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
		FlujosTN17Key other = (FlujosTN17Key) obj;
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
		if (ccartera == null) {
			if (other.ccartera != null)
				return false;
		} else if (!ccartera.equals(other.ccartera))
			return false;
		if (cnegocio == null) {
			if (other.cnegocio != null)
				return false;
		} else if (!cnegocio.equals(other.cnegocio))
			return false;
		if (fcierre == null) {
			if (other.fcierre != null)
				return false;
		} else if (!fcierre.equals(other.fcierre))
			return false;
		if (kcertificado == null) {
			if (other.kcertificado != null)
				return false;
		} else if (!kcertificado.equals(other.kcertificado))
			return false;
		if (kcontrato == null) {
			if (other.kcontrato != null)
				return false;
		} else if (!kcontrato.equals(other.kcontrato))
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
		if (kpoliza == null) {
			if (other.kpoliza != null)
				return false;
		} else if (!kpoliza.equals(other.kpoliza))
			return false;
		if (kprestacion == null) {
			if (other.kprestacion != null)
				return false;
		} else if (!kprestacion.equals(other.kprestacion))
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
		if (norden == null) {
			if (other.norden != null)
				return false;
		} else if (!norden.equals(other.norden))
			return false;
		if (nsuscri == null) {
			if (other.nsuscri != null)
				return false;
		} else if (!nsuscri.equals(other.nsuscri))
			return false;
		if (uoa == null) {
			if (other.uoa != null)
				return false;
		} else if (!uoa.equals(other.uoa))
			return false;
		if (kajuste == null) {
			if (other.kajuste != null)
				return false;
		} else if (!kajuste.equals(other.kajuste))
			return false;
		if (ctipoaport == null) {
			if (other.ctipoaport != null)
				return false;
		} else if (!ctipoaport.equals(other.ctipoaport))
			return false;
		if (kcarterainv == null) {
			if (other.kcarterainv != null)
				return false;
		} else if (!kcarterainv.equals(other.kcarterainv))
			return false;
		if (gapact == null) {
			if (other.gapact != null)
				return false;
		} else if (!gapact.equals(other.gapact))
			return false;
		if (kmodext == null) {
			if (other.kmodext != null)
				return false;
		} else if (!kmodext.equals(other.kmodext))
			return false;
		if (spcom == null) {
			if (other.spcom != null)
				return false;
		} else if (!spcom.equals(other.spcom))
			return false;
		return true;
	}

	@Override
	public int compareTo(FlujosTN17Key o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.cnegocio, o.cnegocio);
		compareToBuilder.append(this.ccanal, o.ccanal);
		compareToBuilder.append(this.ccartera, o.ccartera);
		compareToBuilder.append(this.fcierre, o.fcierre);
		compareToBuilder.append(this.bt, o.bt);
		compareToBuilder.append(this.uoa, o.uoa);
		compareToBuilder.append(this.kcontrato, o.kcontrato);
		compareToBuilder.append(this.kramo, o.kramo);
		compareToBuilder.append(this.kmodalidad, o.kmodalidad);
		compareToBuilder.append(this.kpoliza, o.kpoliza);
		compareToBuilder.append(this.ksubpoliza, o.ksubpoliza);
		compareToBuilder.append(this.kcertificado, o.kcertificado);
		compareToBuilder.append(this.nsuscri, o.nsuscri);
		compareToBuilder.append(this.norden, o.norden);
		compareToBuilder.append(this.kgarantia, o.kgarantia);
		compareToBuilder.append(this.kprestacion, o.kprestacion);
		compareToBuilder.append(this.kajuste, o.kajuste);
		compareToBuilder.append(this.ctipoaport, o.ctipoaport);
		compareToBuilder.append(this.kcarterainv, o.kcarterainv);
		compareToBuilder.append(this.gapact, o.gapact);
		compareToBuilder.append(this.kmodext, o.kmodext);
		compareToBuilder.append(this.spcom, o.spcom);
		return compareToBuilder.toComparison();
	}

}