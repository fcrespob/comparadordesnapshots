/* MODIFICACION:MU-2019-053081: Se incluyen campos tabla1 y gestionit en la clave
   FECHA: 26/08/2019
   AUTOR: INDRA
*/
package es.mapfre.solvencia.coherence.keys.entregables;

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.entregables.FlujSuscri;


@Portable
public class FlujSuscriKey implements Comparable<FlujSuscriKey> {

	@PortableProperty(FlujSuscri.IND_BT)
	private String bt;
	@PortableProperty(FlujSuscri.IND_FECCIERRE)
	private Timestamp feccierre;
	@PortableProperty(FlujSuscri.IND_CNEGOCIO)
	private String cnegocio;
	@PortableProperty(FlujSuscri.IND_CCANAL)
	private Integer ccanal;
	@PortableProperty(FlujSuscri.IND_GAPACT)
	private String gapact;
	@PortableProperty(FlujSuscri.IND_KRAMO)
	private String kramo;
	@PortableProperty(FlujSuscri.IND_KMODALIDAD)
	private Integer kmodalidad;
	@PortableProperty(FlujSuscri.IND_SEGMENTO1)
	private String ksegmento;
	@PortableProperty(FlujSuscri.IND_TIPOSUBRIESGO)
	private String tiposubriesgo;
	@PortableProperty(FlujSuscri.IND_KCARTERAINV)
	private String kcarterainv;
	@PortableProperty(FlujSuscri.IND_KPOLIZA)
	private Long kpoliza;
	@PortableProperty(FlujSuscri.IND_KSUBPOLIZA)
	private Integer ksubpoliza;
	@PortableProperty(FlujSuscri.IND_NSUSCRI)
	private Integer nsuscri;
	@PortableProperty(FlujSuscri.IND_FECDESDE)
	private Timestamp fecdesde;
	@PortableProperty(FlujSuscri.IND_KGARANTIA)
	private Integer kgarantia;

	

	public FlujSuscriKey() {
		super();
	}

	public FlujSuscriKey(String bt, Timestamp feccierre, String cnegocio, Integer ccanal, String gapact, String kramo,
			Integer kmodalidad, String ksegmento, String tiposubriesgo, String kcarterainv, Long kpoliza,
			Integer ksubpoliza, Integer nsuscri, Timestamp fecdesde, Integer kgarantia) {
		super();
		this.bt = bt;
		this.feccierre = feccierre;
		this.cnegocio = cnegocio;
		this.ccanal = ccanal;
		this.gapact = gapact;
		this.kramo = kramo;
		this.kmodalidad = kmodalidad;
		this.ksegmento = ksegmento;
		this.tiposubriesgo = tiposubriesgo;
		this.kcarterainv = kcarterainv;
		this.kpoliza = kpoliza;
		this.ksubpoliza = ksubpoliza;
		this.nsuscri = nsuscri;
		this.fecdesde = fecdesde;
		this.kgarantia = kgarantia;
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
		result = prime * result + ((gapact == null) ? 0 : gapact.hashCode());
		result = prime * result + ((kcarterainv == null) ? 0 : kcarterainv.hashCode());
		result = prime * result + ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result + ((ksegmento == null) ? 0 : ksegmento.hashCode());
		result = prime * result + ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
		result = prime * result + ((nsuscri == null) ? 0 : nsuscri.hashCode());
		result = prime * result + ((tiposubriesgo == null) ? 0 : tiposubriesgo.hashCode());
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
		FlujSuscriKey other = (FlujSuscriKey) obj;
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
		if (gapact == null) {
			if (other.gapact != null)
				return false;
		} else if (!gapact.equals(other.gapact))
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
		if (ksegmento == null) {
			if (other.ksegmento != null)
				return false;
		} else if (!ksegmento.equals(other.ksegmento))
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
		if (tiposubriesgo == null) {
			if (other.tiposubriesgo != null)
				return false;
		} else if (!tiposubriesgo.equals(other.tiposubriesgo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FlujSuscriKey [bt=" + bt + ", feccierre=" + feccierre + ", cnegocio=" + cnegocio + ", ccanal=" + ccanal
				+ ", gapact=" + gapact + ", kramo=" + kramo + ", kmodalidad=" + kmodalidad + ", ksegmento=" + ksegmento
				+ ", tiposubriesgo=" + tiposubriesgo + ", kcarterainv=" + kcarterainv + ", kpoliza=" + kpoliza
				+ ", ksubpoliza=" + ksubpoliza + ", nsuscri=" + nsuscri + ", fecdesde=" + fecdesde + ", kgarantia="
				+ kgarantia + "]";
	}

	@Override
	public int compareTo(FlujSuscriKey o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.bt, o.bt);
		compareToBuilder.append(this.feccierre, o.feccierre);
		compareToBuilder.append(this.cnegocio, o.cnegocio);
		compareToBuilder.append(this.ccanal, o.ccanal);
		compareToBuilder.append(this.kpoliza, o.kpoliza);
		compareToBuilder.append(this.ksubpoliza, o.ksubpoliza);
		compareToBuilder.append(this.nsuscri, o.nsuscri);
		compareToBuilder.append(this.kmodalidad, o.kmodalidad);
		compareToBuilder.append(this.kgarantia, o.kgarantia);
		compareToBuilder.append(this.fecdesde, o.fecdesde);
		compareToBuilder.append(this.kramo, o.kramo);
		compareToBuilder.append(this.gapact, o.gapact);
		compareToBuilder.append(this.ksegmento, o.ksegmento);
		compareToBuilder.append(this.tiposubriesgo, o.tiposubriesgo);
		compareToBuilder.append(this.kcarterainv, o.kcarterainv);

		return compareToBuilder.toComparison();
	}
}
