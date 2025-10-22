/* MODIFICACION:MU-2019-053081: Se incluyen campos tabla1 y gestionit en la clave
   FECHA: 26/08/2019
   AUTOR: INDRA
*/
package es.mapfre.solvencia.coherence.keys.entregables;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.dominio.entregables.FlujCoaSeg;
import es.mapfre.solvencia.dominio.entregables.ProvCoaSeg;

//EN LAS CLAVES HE PREFERIDO METER ATRIBUTOS DE MÁS. UNA VEZ SE TENGAN MÁS CLARAS LAS AGREGACIONES, SE ELIMINARÁ
//TODO EL QUE NO SEA VITAL PARA DEFINIS DE MANERA UNÍVOCA CADA INSTANCIA DE LA CLASE ENTREGABLE

@Portable
public class FlujCoaSegKey implements Comparable<FlujCoaSegKey> {

	@PortableProperty(FlujCoaSeg.IND_BT)
	private String bt;
	@PortableProperty(FlujCoaSeg.IND_FECCIERRE)
	private Timestamp feccierre;
	@PortableProperty(FlujCoaSeg.IND_CNEGOCIO)
	private String cnegocio;
	@PortableProperty(FlujCoaSeg.IND_CCANAL)
	private Integer ccanal;
	@PortableProperty(FlujCoaSeg.IND_GAPACT)
	private String gapact;
	@PortableProperty(FlujCoaSeg.IND_KRAMO)
	private String kramo;
	@PortableProperty(FlujCoaSeg.IND_KMODALIDAD)
	private Integer kmodalidad;
	@PortableProperty(FlujCoaSeg.IND_SEGMENTO1)
	private String ksegmento;
	@PortableProperty(FlujCoaSeg.IND_TIPOSUBRIESGO)
	private String tiposubriesgo;
	@PortableProperty(FlujCoaSeg.IND_KCARTERAINV)
	private String kcarterainv;
	@PortableProperty(FlujCoaSeg.IND_KPOLIZA)
	private Long kpoliza;
	@PortableProperty(FlujCoaSeg.IND_KSUBPOLIZA)
	private Integer ksubpoliza;
	@PortableProperty(FlujCoaSeg.IND_NSUSCRI)
	private Integer nsuscri;
	@PortableProperty(FlujCoaSeg.IND_FECDESDE)
	private Timestamp fecdesde;
	@PortableProperty(ProvCoaSeg.IND_GESTIONIT)
	private String gestionit;
	@PortableProperty(ProvCoaSeg.IND_TABLA1)
	private String tabla1;
	

	public FlujCoaSegKey() {
		super();
	}

	public FlujCoaSegKey(String bt, Timestamp feccierre, String cnegocio, Integer ccanal, String gapact, String kramo,
			Integer kmodalidad, String ksegmento, String tiposubriesgo, String kcarterainv, Long kpoliza,
			Integer ksubpoliza, Integer nsuscri, Timestamp fecdesde, String gestionit, String tabla1) {
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
		this.gestionit = gestionit;
		this.tabla1 = tabla1;
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

	public String getGapact() {
		return gapact;
	}

	public void setGapact(String gapact) {
		this.gapact = gapact;
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

	public String getKsegmento() {
		return ksegmento;
	}

	public void setKsegmento(String ksegmento) {
		this.ksegmento = ksegmento;
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

	public Timestamp getFecdesde() {
		return fecdesde;
	}

	public void setFecdesde(Timestamp fecdesde) {
		this.fecdesde = fecdesde;
	}

	
	public void getGestionit(String gestionit) {
		this.gestionit = gestionit;
	}	

	public void setGestionit(String gestionit) {
		this.gestionit = gestionit;
	}	

	public void getTabla1(String tabla1) {
		this.tabla1 = tabla1;
	}	

	public void setTabla1(String tabla1) {
		this.tabla1 = tabla1;
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
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result + ((ksegmento == null) ? 0 : ksegmento.hashCode());
		result = prime * result + ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
		result = prime * result + ((nsuscri == null) ? 0 : nsuscri.hashCode());
		result = prime * result + ((tiposubriesgo == null) ? 0 : tiposubriesgo.hashCode());
		result = prime * result + ((tabla1 == null) ? 0 : tabla1.hashCode());
		result = prime * result + ((gestionit == null) ? 0 : gestionit.hashCode());
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
		FlujCoaSegKey other = (FlujCoaSegKey) obj;
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
		if (tabla1 == null) {
			if (other.tabla1 != null)
				return false;
		} else if (!tabla1.equals(other.tabla1))
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
		return "FlujCoaSegKey [bt=" + bt + ", feccierre=" + feccierre + ", cnegocio=" + cnegocio + ", ccanal=" + ccanal
				+ ", gapact=" + gapact + ", kramo=" + kramo + ", kmodalidad=" + kmodalidad + ", ksegmento=" + ksegmento
				+ ", tiposubriesgo=" + tiposubriesgo + ", kcarterainv=" + kcarterainv + ", kpoliza=" + kpoliza
				+ ", ksubpoliza=" + ksubpoliza + ", nsuscri=" + nsuscri + ", fecdesde=" + fecdesde +  ", gestionit=" + gestionit + ", tabla1=" + tabla1 + "]";
	}

	@Override
	public int compareTo(FlujCoaSegKey o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.bt, o.bt);
		compareToBuilder.append(this.feccierre, o.feccierre);
		compareToBuilder.append(this.cnegocio, o.cnegocio);
		compareToBuilder.append(this.ccanal, o.ccanal);
		compareToBuilder.append(this.kpoliza, o.kpoliza);
		compareToBuilder.append(this.ksubpoliza, o.ksubpoliza);
		compareToBuilder.append(this.nsuscri, o.nsuscri);
		compareToBuilder.append(this.kmodalidad, o.kmodalidad);
		compareToBuilder.append(this.fecdesde, o.fecdesde);
		compareToBuilder.append(this.kramo, o.kramo);
		compareToBuilder.append(this.gapact, o.gapact);
		compareToBuilder.append(this.ksegmento, o.ksegmento);
		compareToBuilder.append(this.tiposubriesgo, o.tiposubriesgo);
		compareToBuilder.append(this.kcarterainv, o.kcarterainv);
		compareToBuilder.append(this.tabla1, o.tabla1);
		compareToBuilder.append(this.gestionit, o.gestionit);

		return compareToBuilder.toComparison();
	}
}
