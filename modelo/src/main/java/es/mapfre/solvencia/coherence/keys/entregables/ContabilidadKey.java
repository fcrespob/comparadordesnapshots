/* MODIFICACION:TAR00302248-Contabilidad SolvenciaII en SSAA 
   FECHA: 25/09/2017
   AUTOR: INDRA
*/
 /* MODIFICACION:MU-2019-053597:Código SD01707312: Incidencia en FICHERO CONTAB PARA SEPARAR PTRI DE PROV MATEMATICA (CLOUD)
   FECHA: 26/08/2019 
   AUTOR: INDRA
   DESCRIPCION: Se incluye en campo ptipoprovi en el fichero CONTAB
*/
package es.mapfre.solvencia.coherence.keys.entregables;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.dominio.entregables.Contabilidad;
import es.mapfre.solvencia.dominio.entregables.FlujCoaSeg;
import es.mapfre.solvencia.dominio.entregables.PrvInf1;


//EN LAS CLAVES HE PREFERIDO METER ATRIBUTOS DE MÁS. UNA VEZ SE TENGAN MÁS CLARAS LAS AGREGACIONES, SE ELIMINARÁ
//TODO EL QUE NO SEA VITAL PARA DEFINIS DE MANERA UNÍVOCA CADA INSTANCIA DE LA CLASE ENTREGABLE

@Portable
public class ContabilidadKey implements Comparable<ContabilidadKey> {


	@PortableProperty(Contabilidad.IND_BT)
	private String bt;
	@PortableProperty(Contabilidad.IND_CCANAL)
	private Integer ccanal;
	@PortableProperty(Contabilidad.IND_KMODALIDAD)
	private Integer kmodalidad;
	@PortableProperty(Contabilidad.IND_KPOLIZA)
	private Long kpoliza;
	@PortableProperty(Contabilidad.IND_KSUBPOLIZA)
	private Integer ksubpoliza;
	@PortableProperty(Contabilidad.IND_FECCIERRE)
	private Timestamp feccierre;
	@PortableProperty(Contabilidad.IND_KRAMO)
	private String kramo;
	@PortableProperty(PrvInf1.IND_KOFICONT)
	private String koficont;
//javivil
	@PortableProperty(Contabilidad.IND_KMODEXT)
	private Integer kmodext;
//javivil	
	@PortableProperty(Contabilidad.IND_CTIPOPROVI)
	private String ctipoprovi;
	
	public ContabilidadKey() {
		super();
	}

	public ContabilidadKey(String bt,Integer ccanal,Timestamp feccierre,String kramo,Integer kmodalidad, Long kpoliza,Integer ksubpoliza,String koficont,Integer kmodext, String ctipoprovi) {
		super();
		this.bt = bt;
		this.ccanal = ccanal;
		this.feccierre = feccierre;
		this.kpoliza = kpoliza;
		this.ksubpoliza = ksubpoliza;
		this.kmodalidad = kmodalidad;
		this.kramo = kramo;
		this.koficont = koficont;
//javivil
		this.kmodext = kmodext;
//javivil	
		this.ctipoprovi = ctipoprovi;
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
	public Integer getCcanal() {
		return ccanal;
	}

	public void setCcanal(Integer ccanal) {
		this.ccanal = ccanal;
	}
	public Integer getKmodalidad() {
		return kmodalidad;
	}

	public void setKmodalidad(Integer kmodalidad) {
		this.kmodalidad = kmodalidad;
	}
	
	public String getKramo() {
		return kramo;
	}

	public void setKramo(String kramo) {
		this.kramo = kramo;
	}
	
	public String getKoficont() {
		return koficont;
	}

	public void setKoficont(String koficont) {
		this.koficont = koficont;
	}

//javivil
	public Integer getKmodext() {
		return kmodext;
	}

	public void setKmodext(Integer kmodext) {
		this.kmodext = kmodext;
	}
//javivil	
	
	public String getctipoprovi() {
		return ctipoprovi;
	}

	public void setctipoprovi(String ctipoprovi) {
		this.ctipoprovi = ctipoprovi;
	}	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((feccierre == null) ? 0 : feccierre.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result	+ ((koficont == null) ? 0 : koficont.hashCode());
//javivil
		result = prime * result + ((kmodext == null) ? 0 : kmodext.hashCode());
//javivil
		result = prime * result + ((ctipoprovi == null) ? 0 : ctipoprovi.hashCode());
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
		ContabilidadKey other = (ContabilidadKey) obj;
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
		if (kmodalidad == null) {
			if (other.kmodalidad != null)
				return false;
		} else if (!kmodalidad.equals(other.kmodalidad))
			return false;
		if (feccierre == null) {
			if (other.feccierre != null)
				return false;
		} else if (!feccierre.equals(other.feccierre))
			return false;
		if (kpoliza == null) {
			if (other.kpoliza != null)
				return false;
		} else if (!kpoliza.equals(other.kpoliza))
			return false;
		if (ksubpoliza == null) {
			if (other.ksubpoliza != null)
				return false;
		} else if (!ksubpoliza.equals(other.ksubpoliza))
			return false;
		if (kramo == null) {
			if (other.kramo != null)
				return false;
		} else if (!kramo.equals(other.kramo))
			return false;
		if (koficont == null) {
			if (other.koficont != null)
				return false;
		} else if (!koficont.equals(other.koficont))
			return false;
//javivil
		if (kmodext == null) {
			if (other.kmodext != null)
				return false;
		} else if (!kmodext.equals(other.kmodext))
			return false;		
//javivil		
		if (ctipoprovi == null) {
			if (other.ctipoprovi != null)
				return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ContabilidadKey [bt=" + bt + ", feccierre=" + feccierre + ", ccanal=" + ccanal + ", kpoliza=" + kpoliza
				+ ", ksubpoliza=" + ksubpoliza + ",kmodalidad=" + kmodalidad + ",kramo=" + kramo + ", koficont=" + koficont + ", ctipoprovi=" + ctipoprovi +"]";
	}

	@Override
	public int compareTo(ContabilidadKey o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.kpoliza, o.kpoliza);
		compareToBuilder.append(this.ksubpoliza, o.ksubpoliza);
		compareToBuilder.append(this.kmodalidad, o.kmodalidad);
		compareToBuilder.append(this.bt, o.bt);
		compareToBuilder.append(this.feccierre, o.feccierre);
		compareToBuilder.append(this.ccanal, o.ccanal);
		compareToBuilder.append(this.kramo, o.kramo);
		compareToBuilder.append(this.koficont, o.koficont);
		compareToBuilder.append(this.kmodext, o.kmodext);
		compareToBuilder.append(this.ctipoprovi, o.ctipoprovi);
		return compareToBuilder.toComparison();
	}


	
	
}
