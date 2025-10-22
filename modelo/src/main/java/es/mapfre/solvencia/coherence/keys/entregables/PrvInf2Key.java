package es.mapfre.solvencia.coherence.keys.entregables;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.entregables.PrvInf2;

@Portable	
public class PrvInf2Key implements Comparable<PrvInf2Key> {

	@PortableProperty(PrvInf2.IND_BT) private String bt;
	@PortableProperty(PrvInf2.IND_CNEGOCIO) private String cnegocio;
	@PortableProperty(PrvInf2.IND_CCANAL) private Integer ccanal;
	@PortableProperty(PrvInf2.IND_KMODALIDAD) private Integer kmodalidad;
	@PortableProperty(PrvInf2.IND_KGARANTIA) private Integer kgarantia;
	@PortableProperty(PrvInf2.IND_KCARTERAINV) private String kcarterainv;
	@PortableProperty(PrvInf2.IND_GAPACT) private String gapAct;
	@PortableProperty(PrvInf2.IND_CTIPRAMO) private String ctipramo;
	@PortableProperty(PrvInf2.IND_KRAMO) private String kramo;
	@PortableProperty(PrvInf2.IND_SEGMENTO1) private String segmento1;
	@PortableProperty(PrvInf2.IND_TIPOSUBRIESGO) private String tiposubriesgo;
	@PortableProperty(PrvInf2.IND_NUEVAPRODUC) private String nuevaproduc;
	@PortableProperty(PrvInf2.IND_INDICRESCATE) private String indicrescate;
	@PortableProperty(PrvInf2.IND_CURVATI) private String curvati;
	
	public PrvInf2Key() {
		super();
	}
	
	
	public PrvInf2Key(String bt, String cnegocio, Integer ccanal, String ctipramo, String kcarterainv, String gapAct, String kramo,
			Integer kmodalidad, Integer kgarantia, String segmento1, String tiposubriesgo, String nuevaproduc, String indicrescate, String curvati) {
		super();
		this.bt = bt;
		this.cnegocio = cnegocio;
		this.ccanal = ccanal;
		this.ctipramo = ctipramo;
		this.kcarterainv = kcarterainv;
		this.gapAct = gapAct;
		this.kramo = kramo;
		this.kmodalidad = kmodalidad;
		this.kgarantia = kgarantia;
		this.segmento1 = segmento1;
		this.tiposubriesgo = tiposubriesgo;
		this.nuevaproduc = nuevaproduc;
		this.indicrescate = indicrescate;
		this.curvati = curvati;		
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result	+ ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result	+ ((ctipramo == null) ? 0 : ctipramo.hashCode());
		result = prime * result + ((curvati == null) ? 0 : curvati.hashCode());
		result = prime * result + ((gapAct == null) ? 0 : gapAct.hashCode());
		result = prime * result	+ ((indicrescate == null) ? 0 : indicrescate.hashCode());
		result = prime * result	+ ((kcarterainv == null) ? 0 : kcarterainv.hashCode());
		result = prime * result	+ ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result	+ ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result	+ ((nuevaproduc == null) ? 0 : nuevaproduc.hashCode());
		result = prime * result	+ ((segmento1 == null) ? 0 : segmento1.hashCode());
		result = prime * result	+ ((tiposubriesgo == null) ? 0 : tiposubriesgo.hashCode());
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
		PrvInf2Key other = (PrvInf2Key) obj;
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
		if (ctipramo == null) {
			if (other.ctipramo != null)
				return false;
		} else if (!ctipramo.equals(other.ctipramo))
			return false;
		if (curvati == null) {
			if (other.curvati != null)
				return false;
		} else if (!curvati.equals(other.curvati))
			return false;
		if (gapAct == null) {
			if (other.gapAct != null)
				return false;
		} else if (!gapAct.equals(other.gapAct))
			return false;
		if (indicrescate == null) {
			if (other.indicrescate != null)
				return false;
		} else if (!indicrescate.equals(other.indicrescate))
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
		return true;
	}


	public int compareTo(PrvInf2Key o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.bt, o.bt);
		compareToBuilder.append(this.cnegocio, o.cnegocio);
		compareToBuilder.append(this.ccanal, o.ccanal);
		compareToBuilder.append(this.kramo, o.kramo);
		compareToBuilder.append(this.kmodalidad, o.kmodalidad);
		compareToBuilder.append(this.kgarantia, o.kgarantia);
		compareToBuilder.append(this.kcarterainv, o.kcarterainv);
		compareToBuilder.append(this.gapAct, o.gapAct);
		compareToBuilder.append(this.ctipramo, o.ctipramo);
		compareToBuilder.append(this.segmento1, o.segmento1);
		compareToBuilder.append(this.tiposubriesgo, o.tiposubriesgo);
		compareToBuilder.append(this.nuevaproduc, o.nuevaproduc);
		compareToBuilder.append(this.indicrescate, o.indicrescate);
		compareToBuilder.append(this.curvati, o.curvati);
		return compareToBuilder.toComparison();
	}	

}
