package es.mapfre.scr.tablasExperiencia.dominio.entidades;

import java.util.List;

import es.mapfre.scr.tablasExperiencia.dominio.EntidadBase;
import es.mapfre.scr.tablasExperiencia.dominio.keys.SalidaKey;

public class Salida implements EntidadBase<SalidaKey> {

	public static final int IND_KFCHCIERRE = 0;
	public static final int IND_KBASETEC = 1;
	public static final int IND_KCOMPANIA = 2;
	public static final int IND_KNEGOCIO = 3;
	public static final int IND_KRIESGO = 4;
	public static final int IND_KSEXO = 5;
	public static final int IND_KCATEG = 6;
	public static final int IND_KEDADFIJA = 7;
	public static final int IND_KMODALIDAD = 8;
	public static final int IND_CTABBASE = 9;
	public static final int IND_KGENERACION = 10;
	public static final int IND_QX = 11;
	public static final int IND_LX = 12;
	public static final int IND_USUARIOALTA = 13;
	public static final int IND_FECHAALTA = 14;

	private String kfchcierre;
	private String kbasetec;
	private Integer kcompania;
	private String knegocio;
	private String kriesgo;
	private String ksexo;
	private String kcateg;
	private Integer kedadfija;
	private Integer kmodalidad;
	private Integer ctabbase;
	private int kgeneracion;
	private List<String> qx;
	private List<String> lx;
	private String usuarioalta;
	private String fechaalta;


	@Override
	public SalidaKey getKey() {
		return new SalidaKey(kfchcierre, kbasetec, kcompania,knegocio, kriesgo, ksexo,kcateg, kedadfija,kmodalidad );
	}


	public String getKfchcierre() {
		return kfchcierre;
	}

	public void setKfchcierre(String kfchcierre) {
		this.kfchcierre = kfchcierre;
	}

	public String getKbasetec() {
		return kbasetec;
	}

	public void setKbasetec(String kbasetec) {
		this.kbasetec = kbasetec;
	}

	public Integer getKcompania() {
		return kcompania;
	}

	public void setKcompania(Integer kcompania) {
		this.kcompania = kcompania;
	}

	public String getKnegocio() {
		return knegocio;
	}

	public void setKnegocio(String knegocio) {
		this.knegocio = knegocio;
	}

	public String getKriesgo() {
		return kriesgo;
	}

	public void setKriesgo(String kriesgo) {
		this.kriesgo = kriesgo;
	}

	public String getKsexo() {
		return ksexo;
	}

	public void setKsexo(String ksexo) {
		this.ksexo = ksexo;
	}

	public String getKcateg() {
		return kcateg;
	}

	public void setKcateg(String kcateg) {
		this.kcateg = kcateg;
	}

	public Integer getKedadfija() {
		return kedadfija;
	}

	public void setKedadfija(Integer kedadfija) {
		this.kedadfija = kedadfija;
	}

	public Integer getKmodalidad() {
		return kmodalidad;
	}

	public void setKmodalidad(Integer kmodalidad) {
		this.kmodalidad = kmodalidad;
	}

	public Integer getCtabbase() {
		return ctabbase;
	}

	public void setCtabbase(Integer ctabbase) {
		this.ctabbase = ctabbase;
	}

	public int getKgeneracion() {
		return kgeneracion;
	}

	public void setKgeneracion(int kgeneracion) {
		this.kgeneracion = kgeneracion;
	}

	public List<String> getQx() {
		return qx;
	}

	public void setQx(List<String> qx) {
		this.qx = qx;
	}

	public List<String> getLx() {
		return lx;
	}

	public void setLx(List<String> lx) {
		this.lx = lx;
	}

	public String getUsuarioalta() {
		return usuarioalta;
	}


	public void setUsuarioalta(String usuarioalta) {
		this.usuarioalta = usuarioalta;
	}


	public String getFechaalta() {
		return fechaalta;
	}


	public void setFechaalta(String fechaalta) {
		this.fechaalta = fechaalta;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ctabbase == null) ? 0 : ctabbase.hashCode());
		result = prime * result
				+ ((kbasetec == null) ? 0 : kbasetec.hashCode());
		result = prime * result + ((kcateg == null) ? 0 : kcateg.hashCode());
		result = prime * result
				+ ((kcompania == null) ? 0 : kcompania.hashCode());
		result = prime * result
				+ ((kedadfija == null) ? 0 : kedadfija.hashCode());
		result = prime * result
				+ ((kfchcierre == null) ? 0 : kfchcierre.hashCode());
		result = prime * result + kgeneracion;
		result = prime * result
				+ ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result
				+ ((knegocio == null) ? 0 : knegocio.hashCode());
		result = prime * result + ((kriesgo == null) ? 0 : kriesgo.hashCode());
		result = prime * result + ((ksexo == null) ? 0 : ksexo.hashCode());
		result = prime * result + ((lx == null) ? 0 : lx.hashCode());
		result = prime * result + ((qx == null) ? 0 : qx.hashCode());
		result = prime * result	+ ((usuarioalta == null) ? 0 : usuarioalta.hashCode());
		result = prime * result + ((fechaalta == null) ? 0 : fechaalta.hashCode());
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
		Salida other = (Salida) obj;
		if (ctabbase == null) {
			if (other.ctabbase != null)
				return false;
		} else if (!ctabbase.equals(other.ctabbase))
			return false;
		if (kbasetec == null) {
			if (other.kbasetec != null)
				return false;
		} else if (!kbasetec.equals(other.kbasetec))
			return false;
		if (kcateg == null) {
			if (other.kcateg != null)
				return false;
		} else if (!kcateg.equals(other.kcateg))
			return false;
		if (kcompania == null) {
			if (other.kcompania != null)
				return false;
		} else if (!kcompania.equals(other.kcompania))
			return false;
		if (kedadfija == null) {
			if (other.kedadfija != null)
				return false;
		} else if (!kedadfija.equals(other.kedadfija))
			return false;
		if (kfchcierre == null) {
			if (other.kfchcierre != null)
				return false;
		} else if (!kfchcierre.equals(other.kfchcierre))
			return false;
		if (kgeneracion != other.kgeneracion)
			return false;
		if (kmodalidad == null) {
			if (other.kmodalidad != null)
				return false;
		} else if (!kmodalidad.equals(other.kmodalidad))
			return false;
		if (knegocio == null) {
			if (other.knegocio != null)
				return false;
		} else if (!knegocio.equals(other.knegocio))
			return false;
		if (kriesgo == null) {
			if (other.kriesgo != null)
				return false;
		} else if (!kriesgo.equals(other.kriesgo))
			return false;
		if (ksexo == null) {
			if (other.ksexo != null)
				return false;
		} else if (!ksexo.equals(other.ksexo))
			return false;
		if (lx == null) {
			if (other.lx != null)
				return false;
		} else if (!lx.equals(other.lx))
			return false;
		if (qx == null) {
			if (other.qx != null)
				return false;
		} else if (!qx.equals(other.qx))
			return false;
		
		if (usuarioalta == null) {
			if (other.usuarioalta != null)
				return false;
		} else if (!usuarioalta.equals(other.usuarioalta))
			return false;
		
		if (fechaalta == null) {
			if (other.fechaalta != null)
				return false;
		} else if (!fechaalta.equals(other.fechaalta))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Salida [kfchcierre =" + kfchcierre + ", kbasetec=" 
				+ kbasetec + ", kcompania=" + kcompania 
				+ ", knegocio=" + knegocio + ", kriesgo="
				+ kriesgo + ", ksexo=" + ksexo + ", kcateg=" 
				+ kcateg + ", kedadfija=" + kedadfija + ", kmodalidad=" 
				+ kmodalidad + ", ctabbase=" + ctabbase
				+ ", kgeneracion=" + kgeneracion + ", qx=" + qx
				+ ", lx=" + lx +", usuaruialta=" + usuarioalta + ", fechaalta=" + fechaalta + "]";
	}

}
