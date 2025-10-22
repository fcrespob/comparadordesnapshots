package es.mapfre.solvencia.coherence.keys.entregables;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.dominio.entregables.PrvInf1;

@Portable	
public class PrvInf1Key implements Comparable<PrvInf1Key> {

	@PortableProperty(PrvInf1.IND_BT) private String bt;
	@PortableProperty(PrvInf1.IND_CNEGOCIO) private String cnegocio;
	@PortableProperty(PrvInf1.IND_CCANAL) private Integer ccanal;
	@PortableProperty(PrvInf1.IND_KCARTERAINV) private String kcarterainv;
	@PortableProperty(PrvInf1.IND_GAPACT) private String gapAct;
	@PortableProperty(PrvInf1.IND_KRAMO) private String kramo;
	@PortableProperty(PrvInf1.IND_KMODALIDAD) private Integer kmodalidad;
	@PortableProperty(value = PrvInf1.IND_INTFECCALC, codec = BigDecimalSolvenciaCodec.class) private java.math.BigDecimal intfeccal;
	@PortableProperty(PrvInf1.IND_CTIPOPROVI) private String ctipoprovi;
	@PortableProperty(PrvInf1.IND_SPCOM) private String spcom;
	@PortableProperty(PrvInf1.IND_KOFICONT) private String koficont;
	@PortableProperty(PrvInf1.IND_KMODEXT) private Integer kmodext;
	@PortableProperty(PrvInf1.IND_GESTIONIT) private String gestionit;
	
	
	public PrvInf1Key() {
		super();
	}
	
	
	public PrvInf1Key(String bt, String cnegocio, Integer ccanal, String kcarterainv, String gapAct, String kramo, Integer kmodalidad,
			BigDecimal intfeccal, String ctipoprovi, String spcom, String koficont, Integer kmodext, String gestionit) {
		super();
		this.bt = bt;
		this.cnegocio = cnegocio;
		this.ccanal = ccanal;
		this.kcarterainv = kcarterainv;
		this.gapAct = gapAct;
		this.kramo = kramo;
		this.kmodalidad = kmodalidad;
		this.intfeccal = intfeccal;
		this.ctipoprovi = ctipoprovi;
		this.spcom = spcom;
		this.koficont = koficont;
		this.kmodext = kmodext;		
		this.gestionit = gestionit;						
	}
	

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result	+ ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result	+ ((ctipoprovi == null) ? 0 : ctipoprovi.hashCode());
		result = prime * result + ((gapAct == null) ? 0 : gapAct.hashCode());
		result = prime * result + ((intfeccal == null) ? 0 : intfeccal.hashCode());
		result = prime * result	+ ((kcarterainv == null) ? 0 : kcarterainv.hashCode());
		result = prime * result	+ ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result	+ ((koficont == null) ? 0 : koficont.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result + ((kmodext == null) ? 0 : kmodext.hashCode());
		result = prime * result + ((gestionit == null) ? 0 : gestionit.hashCode());
		result = prime * result + ((spcom == null) ? 0 : spcom.hashCode());
		return result;
	}


	@Override //NOSONAR
	public boolean equals(Object obj) { //NOSONAR
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrvInf1Key other = (PrvInf1Key) obj;
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
		if (ctipoprovi == null) {
			if (other.ctipoprovi != null)
				return false;
		} else if (!ctipoprovi.equals(other.ctipoprovi))
			return false;
		if (gapAct == null) {
			if (other.gapAct != null)
				return false;
		} else if (!gapAct.equals(other.gapAct))
			return false;
		if (intfeccal == null) {
			if (other.intfeccal != null)
				return false;
		} else if (!intfeccal.equals(other.intfeccal))
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
		if (koficont == null) {
			if (other.koficont != null)
				return false;
		} else if (!koficont.equals(other.koficont))
			return false;
		if (kramo == null) {
			if (other.kramo != null)
				return false;
		} else if (!kramo.equals(other.kramo))
			return false;
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
		if (gestionit == null) {
			if (other.gestionit != null)
				return false;
		} else if (!gestionit.equals(other.gestionit))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "PrvInf1Key [bt=" + bt + ", cnegocio=" + cnegocio + ", ccanal="
				+ ccanal + ", kcarterainv=" + kcarterainv + ", gapAct="
				+ gapAct + ", kramo=" + kramo + ", kmodalidad=" + kmodalidad
				+ ", intfeccal=" + intfeccal + ", ctipoprovi=" + ctipoprovi
				+ ", spcom=" + spcom + ", koficont=" + koficont + ", kmodext="
				+ kmodext + ", gestionit=" + gestionit + "]";
	}


	public int compareTo(PrvInf1Key o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.bt, o.bt);
		compareToBuilder.append(this.cnegocio, o.cnegocio);
		compareToBuilder.append(this.ccanal, o.ccanal);
		compareToBuilder.append(this.kramo, o.kramo);
		compareToBuilder.append(this.kmodalidad, o.kmodalidad);
		compareToBuilder.append(this.kcarterainv, o.kcarterainv);
		compareToBuilder.append(this.gapAct, o.gapAct);
		compareToBuilder.append(this.intfeccal, o.intfeccal);
		compareToBuilder.append(this.ctipoprovi, o.ctipoprovi);
		compareToBuilder.append(this.spcom, o.spcom);
		compareToBuilder.append(this.koficont, o.koficont);
		compareToBuilder.append(this.kmodext, o.kmodext);	
		compareToBuilder.append(this.gestionit, o.gestionit);		

		return compareToBuilder.toComparison();
	}	
	

	
	
	

}
