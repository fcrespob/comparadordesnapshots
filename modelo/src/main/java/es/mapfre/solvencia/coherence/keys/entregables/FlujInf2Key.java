/* MODIFICACION:TAR00400971-NECESIDADES NUEVO SISTEMA DE PROCESOS TÉCNICOS 
   FECHA: 18/01/2019 Se incluye campo KBENCON,SPCOM, y KMODEXT
   AUTOR: INDRA
*/

package es.mapfre.solvencia.coherence.keys.entregables;

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.entregables.FlujInf1;
import es.mapfre.solvencia.dominio.entregables.FlujInf2;

//EN LAS CLAVES HE PREFERIDO METER ATRIBUTOS DE MÁS. UNA VEZ SE TENGAN MÁS CLARAS LAS AGREGACIONES, SE ELIMINARÁ
//TODO EL QUE NO SEA VITAL PARA DEFINIS DE MANERA UNÍVOCA CADA INSTANCIA DE LA CLASE ENTREGABLE

@Portable
public class FlujInf2Key implements Comparable<FlujInf2Key> {

	@PortableProperty(FlujInf2.IND_BT)
	private String bt;
	@PortableProperty(FlujInf2.IND_FECCIERRE)
	private Timestamp feccierre;
	@PortableProperty(FlujInf2.IND_CNEGOCIO)
	private String cnegocio;
	@PortableProperty(FlujInf2.IND_CCANAL)
	private Integer ccanal;
	@PortableProperty(FlujInf2.IND_KCARTERAINV)
	private String kcarterainv;
	@PortableProperty(FlujInf2.IND_GAPACT)
	private String gapAct;
	@PortableProperty(FlujInf2.IND_GESTIONIT)
	private String gestionit;
	@PortableProperty(FlujInf2.IND_KRAMO)
	private String kramo;
	@PortableProperty(FlujInf2.IND_KMODALIDAD)
	private Integer kmodalidad;
	@PortableProperty(FlujInf2.IND_KGARANTIA)
	private Integer kgarantia;
	@PortableProperty(FlujInf2.IND_KPRESTCAL)
	private String kprestcal;
	@PortableProperty(FlujInf2.IND_NUEVAPRODUC)
	private String nuevaproduc;
	@PortableProperty(FlujInf2.IND_SEGMENTO1)
	private String segmento1;
	@PortableProperty(FlujInf2.IND_TIPOSUBRIESGO)
	private String tiposubriesgo;
	@PortableProperty(FlujInf2.IND_CURVATI)
	private String curvati;
	@PortableProperty(FlujInf2.IND_FECDESDE)
	private Timestamp fecdesde;
//INI-TAR00400971
	@PortableProperty(FlujInf2.IND_SPCOM)
	private String spcom;	
	@PortableProperty(FlujInf2.IND_KMODEXT)
	private Integer kmodext;
	@PortableProperty(FlujInf2.IND_KBENCON)
	private String kbencon;
//FIN-TAR00400971
	
	public FlujInf2Key() {
		super();
	}

	public FlujInf2Key(String bt, Timestamp feccierre, String cnegocio, Integer ccanal, String kcarterainv,
			String gapAct, String gestionit, String kramo, Integer kmodalidad, Integer kgarantia, String kprestcal,
			String nuevaproduc, String segmento1, String tiposubriesgo, String curvati,
			Timestamp fecdesde, String spcom, Integer kmodext, String kbencon) {
		super();
		this.bt = bt;
		this.feccierre = feccierre;
		this.cnegocio = cnegocio;
		this.ccanal = ccanal;
		this.kcarterainv = kcarterainv;
		this.gapAct = gapAct;
		this.gestionit = gestionit;
		this.kramo = kramo;
		this.kmodalidad = kmodalidad;
		this.kgarantia = kgarantia;
		this.kprestcal = kprestcal;
		this.nuevaproduc = nuevaproduc;
		this.segmento1 = segmento1;
		this.tiposubriesgo = tiposubriesgo;
		this.curvati = curvati;
		this.fecdesde = fecdesde;
//INI-TAR00400971
		this.spcom = spcom;
		this.kmodext = kmodext;
		this.kbencon = kbencon;
//FIN-TAR00400971
	}

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

	public String getGapAct() {
		return gapAct;
	}

	public void setGapAct(String gapAct) {
		this.gapAct = gapAct;
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

	public Integer getKgarantia() {
		return kgarantia;
	}

	public void setKgarantia(Integer kgarantia) {
		this.kgarantia = kgarantia;
	}

	public String getKprestcal() {
		return kprestcal;
	}

	public void setKprestcal(String kprestcal) {
		this.kprestcal = kprestcal;
	}

	public String getNuevaproduc() {
		return nuevaproduc;
	}

	public void setNuevaproduc(String nuevaproduc) {
		this.nuevaproduc = nuevaproduc;
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

	public String getCurvati() {
		return curvati;
	}

	public void setCurvati(String curvati) {
		this.curvati = curvati;
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
		result = prime * result + ((curvati == null) ? 0 : curvati.hashCode());
		result = prime * result + ((feccierre == null) ? 0 : feccierre.hashCode());
		result = prime * result + ((fecdesde == null) ? 0 : fecdesde.hashCode());
		result = prime * result + ((gapAct == null) ? 0 : gapAct.hashCode());
		result = prime * result + ((gestionit == null) ? 0 : gestionit.hashCode());
		result = prime * result + ((kcarterainv == null) ? 0 : kcarterainv.hashCode());
		result = prime * result + ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kprestcal == null) ? 0 : kprestcal.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result + ((nuevaproduc == null) ? 0 : nuevaproduc.hashCode());
		result = prime * result + ((segmento1 == null) ? 0 : segmento1.hashCode());
		result = prime * result + ((tiposubriesgo == null) ? 0 : tiposubriesgo.hashCode());
//INI-TAR00400971
		result = prime * result + ((spcom == null) ? 0 : spcom.hashCode());
		result = prime * result + ((kmodext == null) ? 0 : kmodext.hashCode());
		result = prime * result + ((kbencon == null) ? 0 : kbencon.hashCode());
//FIN-TAR00400971
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
		FlujInf2Key other = (FlujInf2Key) obj;
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
		if (gapAct == null) {
			if (other.gapAct != null)
				return false;
		} else if (!gapAct.equals(other.gapAct))
			return false;
		if (gestionit == null) {
			if (other.gestionit != null)
				return false;
		} else if (!gestionit.equals(other.gestionit))
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
		if (kprestcal == null) {
			if (other.kprestcal != null)
				return false;
		} else if (!kprestcal.equals(other.kprestcal))
			return false;
		if (kramo == null) {
			if (other.kramo != null)
				return false;
		} else if (!kramo.equals(other.kramo))
			return false;
		if (nuevaproduc == null) {
			if (other.nuevaproduc != null)
				return false;
		} else if (!nuevaproduc.equals(other.nuevaproduc))
			return false;
		if (segmento1 == null) {
			if (other.segmento1 != null)
				return false;
		} else if (!segmento1.equals(other.segmento1))
			return false;
		if (tiposubriesgo == null) {
			if (other.tiposubriesgo != null)
				return false;
		} else if (!tiposubriesgo.equals(other.tiposubriesgo))
			return false;
//INI-TAR00400971		
		if (spcom == null) {
			if (other.spcom != null)
					return false;
		} else if (!spcom.equals(other.spcom))
				return false;
		if (kmodext == null) {
			if (other.kmodext != null)
					return false;
		} else if (!kmodext.equals(other.kmodext))
				return false;
		if (kbencon == null) {
			if (other.kbencon != null)
					return false;
		} else if (!kbencon.equals(other.kbencon))
			return false;
//FIN-TAR00400971
		return true;
	}

	@Override
	public String toString() {
		return "FlujInf2Key [bt=" + bt + ", feccierre=" + feccierre + ", cnegocio=" + cnegocio + ", ccanal=" + ccanal
				+ ", kcarterainv=" + kcarterainv + ", gapAct=" + gapAct + ", gestionit=" + gestionit + ", kramo="
				+ kramo + ", kmodalidad=" + kmodalidad + ", kgarantia=" + kgarantia + ", kprestcal=" + kprestcal
				+ ", nuevaproduc=" + nuevaproduc + ", segmento1=" + segmento1 + ", tiposubriesgo=" + tiposubriesgo
				+ ", curvati=" + curvati + ", fecdesde=" + fecdesde + ", spcom=" + spcom
				+ ", kmodext=" + kmodext + ", kbencon=" + kbencon + "]";
	}

	public int compareTo(FlujInf2Key o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.bt, o.bt);
		compareToBuilder.append(this.feccierre, o.feccierre);
		compareToBuilder.append(this.cnegocio, o.cnegocio);
		compareToBuilder.append(this.ccanal, o.ccanal);
		compareToBuilder.append(this.kramo, o.kramo);
		compareToBuilder.append(this.kmodalidad, o.kmodalidad);
		compareToBuilder.append(this.kgarantia, o.kgarantia);
		compareToBuilder.append(this.kprestcal, o.kprestcal);
		compareToBuilder.append(this.kcarterainv, o.kcarterainv);
		compareToBuilder.append(this.gapAct, o.gapAct);
		compareToBuilder.append(this.gestionit, o.gestionit);
		compareToBuilder.append(this.nuevaproduc, o.nuevaproduc);
		compareToBuilder.append(this.fecdesde, o.fecdesde);
		compareToBuilder.append(this.segmento1, o.segmento1);
		compareToBuilder.append(this.tiposubriesgo, o.tiposubriesgo);
		compareToBuilder.append(this.curvati, o.curvati);
		compareToBuilder.append(this.spcom, o.spcom);
		compareToBuilder.append(this.kmodext, o.kmodext);
		compareToBuilder.append(this.kbencon, o.kbencon);

		return compareToBuilder.toComparison();
	}

}