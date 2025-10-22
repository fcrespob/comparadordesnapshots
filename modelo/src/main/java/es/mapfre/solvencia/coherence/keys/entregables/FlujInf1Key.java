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


@Portable
public class FlujInf1Key implements Comparable<FlujInf1Key> {

	@PortableProperty(FlujInf1.IND_BT)
	private String bt;
	@PortableProperty(FlujInf1.IND_FECCIERRE)
	private Timestamp feccierre;
	@PortableProperty(FlujInf1.IND_CNEGOCIO)
	private String cnegocio;
	@PortableProperty(FlujInf1.IND_CCANAL)
	private Integer ccanal;
	@PortableProperty(FlujInf1.IND_KCARTERAINV)
	private String kcarterainv;
	@PortableProperty(FlujInf1.IND_GAPACT)
	private String gapAct;
	@PortableProperty(FlujInf1.IND_GESTIONIT)
	private String gestionit;
	@PortableProperty(FlujInf1.IND_KRAMO)
	private String kramo;
	@PortableProperty(FlujInf1.IND_KMODALIDAD)
	private Integer kmodalidad;	
	@PortableProperty(FlujInf1.IND_FECDESDE)
	private Timestamp fecdesde;
//INI-TAR00400971
	@PortableProperty(FlujInf1.IND_SPCOM)
	private String spcom;	
	@PortableProperty(FlujInf1.IND_KMODEXT)
	private Integer kmodext;
	@PortableProperty(FlujInf1.IND_KBENCON)
	private String kbencon;
//FIN-TAR00400971
	
	public FlujInf1Key() {
		super();
	}

	public FlujInf1Key(String bt, Timestamp feccierre, String cnegocio, Integer ccanal, String kcarterainv,
			String gapAct,String gestionit, String kramo, Integer kmodalidad, Timestamp fecdesde, String spcom, Integer kmodext, String kbencon) {
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
		this.fecdesde = fecdesde;
//INI-TAR00400971
		this.spcom = spcom;
		this.kmodext = kmodext;
		this.kbencon = kbencon;
//FIN-TAR00400971
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result	+ ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result	+ ((feccierre == null) ? 0 : feccierre.hashCode());
		result = prime * result	+ ((fecdesde == null) ? 0 : fecdesde.hashCode());
		result = prime * result + ((gapAct == null) ? 0 : gapAct.hashCode());
		result = prime * result + ((gestionit == null) ? 0 : gestionit.hashCode());
		result = prime * result	+ ((kcarterainv == null) ? 0 : kcarterainv.hashCode());
		result = prime * result	+ ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
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
		FlujInf1Key other = (FlujInf1Key) obj;
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
		return "FlujInf1Key [bt=" + bt + ", feccierre=" + feccierre
				+ ", cnegocio=" + cnegocio + ", ccanal=" + ccanal
				+ ", kcarterainv=" + kcarterainv + ", gapAct=" + gapAct
				+ ", gestionit=" + gestionit + ", kramo=" + kramo + ", kmodalidad=" + kmodalidad
				+ ", fecdesde=" + fecdesde + ", spcom=" + spcom
				+ ", kmodext=" + kmodext + ", kbencon=" + kbencon + "]";
	}

	
	public int compareTo(FlujInf1Key o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.bt, o.bt);
		compareToBuilder.append(this.feccierre, o.feccierre);
		compareToBuilder.append(this.cnegocio, o.cnegocio);
		compareToBuilder.append(this.ccanal, o.ccanal);
		compareToBuilder.append(this.kramo, o.kramo);
		compareToBuilder.append(this.kmodalidad, o.kmodalidad);	
		compareToBuilder.append(this.kcarterainv, o.kcarterainv);
		compareToBuilder.append(this.gapAct, o.gapAct);
		compareToBuilder.append(this.gestionit, o.gestionit);
		compareToBuilder.append(this.fecdesde, o.fecdesde);
		compareToBuilder.append(this.spcom, o.spcom);
		compareToBuilder.append(this.kmodext, o.kmodext);
		compareToBuilder.append(this.kbencon, o.kbencon);
		
		return compareToBuilder.toComparison();
	}

}
