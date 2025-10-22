package es.mapfre.solvencia.dominio.gbt;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.gbt.ErrorOrquestadorKey;
import es.mapfre.solvencia.dominio.EntidadBase;

/* MODIFICACION : 100405260 SOLVENCIA II - C¡LCULO Y GENERACI”N DE FLUJOS FASE V
FECHA : 20/11/2015
DESCRIPCION:SOLV2VIDA Cambio de tipo de dato en kpoliza y GBT 
AUTOR : JVV */

@Portable
public class ErrorOrquestador implements EntidadBase<ErrorOrquestadorKey> {
	public static final int IND_BASETEC = 0;
	public static final int IND_CCANAL = 1;
	public static final int IND_FCIERRE = 2;
	public static final int IND_EKMODALIDAD = 3;
	public static final int IND_KPOLIZA = 4;
	public static final int IND_EKSUBPOLIZA = 5;
	public static final int IND_KCERTIFICADO = 6;
	public static final int IND_ENSUSCRI = 7;
	public static final int IND_ENORDEN = 8;
	public static final int IND_EKGARANTIA = 9;
	public static final int IND_KPRESTACION = 10;
	public static final int IND_EKAJUSTE = 11;
	public static final int IND_CTIPOAPORT = 12;
	public static final int IND_NMODULO = 13;
	public static final int IND_DESCERR = 14;
	
	@PortableProperty(IND_BASETEC) private String basetec;
	@PortableProperty(IND_CCANAL) private Integer ccanal;
	@PortableProperty(IND_FCIERRE) private Timestamp fecCierre;
	@PortableProperty(IND_EKMODALIDAD) private Integer kmodalidad;
//100405260-INI
//	@PortableProperty(IND_KPOLIZA) private Integer kpoliza;
	@PortableProperty(IND_KPOLIZA) private Long kpoliza;
//100405260-FIN
	@PortableProperty(IND_EKSUBPOLIZA) private Integer ksubpoliza;
	@PortableProperty(IND_KCERTIFICADO) private Integer kcertificado;
	@PortableProperty(IND_ENSUSCRI) private Integer nsuscri;
	@PortableProperty(IND_ENORDEN) private Integer norden;
	@PortableProperty(IND_EKGARANTIA) private Integer kgarantia;
	@PortableProperty(IND_KPRESTACION) private String kprestacion;
	@PortableProperty(IND_EKAJUSTE) private Integer kajuste;
	@PortableProperty(IND_CTIPOAPORT) private String ctipoaport;
	@PortableProperty(IND_NMODULO) private String nomModulo;
	@PortableProperty(IND_DESCERR) private String descripcionErr;
	
	public Timestamp getFecCierre() {
		return fecCierre;
	}

	public void setFecCierre(Timestamp fecCierre) {
		this.fecCierre = fecCierre;
	}

	public String getBasetec() {
		return basetec;
	}

	public void setBasetec(String basetec) {
		this.basetec = basetec;
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
//100405260-INI
//	public Integer getKpoliza() {
//		return kpoliza;
//	}
//
//	public void setKpoliza(Integer kpoliza) {
//		this.kpoliza = kpoliza;
//	}
	public Long getKpoliza() {
		return kpoliza;
	}

	public void setKpoliza(Long kpoliza) {
		this.kpoliza = kpoliza;
	}
//100405260-FIN
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

	public String getNomModulo() {
		return nomModulo;
	}

	public void setNomModulo(String nomModulo) {
		this.nomModulo = nomModulo;
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

	public String getDescripcionErr() {
		return descripcionErr;
	}

	public void setDescripcionErr(String descripcionErr) {
		this.descripcionErr = descripcionErr;
	}

	@Override
	public ErrorOrquestadorKey getKey() {
		return new ErrorOrquestadorKey(kmodalidad, kpoliza,	ksubpoliza, kcertificado, nsuscri,norden, kgarantia, kprestacion, kajuste, ctipoaport, nomModulo, descripcionErr);
	}
	
	public ErrorOrquestador() {
		super();
		// TODO Auto-generated constructor stub
	}

	
//100405260-INI
//	public ErrorOrquestador(String basetec, Integer ccanal, Timestamp fecCierre,
//			Integer kmodalidad, Integer kpoliza, Integer ksubpoliza,
//			Integer kcertificado, Integer nsuscri, Integer norden,
//			Integer kgarantia, String kprestacion, Integer kajuste,
//			String ctipoaport, String nomModulo, String descripcionErr) {
	public ErrorOrquestador(String basetec, Integer ccanal, Timestamp fecCierre,
	Integer kmodalidad, Long kpoliza, Integer ksubpoliza,
	Integer kcertificado, Integer nsuscri, Integer norden,
	Integer kgarantia, String kprestacion, Integer kajuste,
	String ctipoaport, String nomModulo, String descripcionErr) {
//100405260-FIN
		super();
		this.basetec = basetec;
		this.ccanal = ccanal;
		this.fecCierre = fecCierre;
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
		this.nomModulo = nomModulo;
		this.descripcionErr = descripcionErr;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ErrorOrquestador [basetec=");
		builder.append(basetec);
		builder.append(", ccanal=");
		builder.append(ccanal);
		builder.append(", fecCierre=");
		builder.append(fecCierre);
		builder.append(", kmodalidad=");
		builder.append(kmodalidad);
		builder.append(", kpoliza=");
		builder.append(kpoliza);
		builder.append(", ksubpoliza=");
		builder.append(ksubpoliza);
		builder.append(", kcertificado=");
		builder.append(kcertificado);
		builder.append(", nsuscri=");
		builder.append(nsuscri);
		builder.append(", norden=");
		builder.append(norden);
		builder.append(", kgarantia=");
		builder.append(kgarantia);
		builder.append(", kprestacion=");
		builder.append(kprestacion);
		builder.append(", kajuste=");
		builder.append(kajuste);
		builder.append(", ctipoaport=");
		builder.append(ctipoaport);
		builder.append(", nomModulo=");
		builder.append(nomModulo);
		builder.append(", descripcionErr=");
		builder.append(descripcionErr);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((basetec == null) ? 0 : basetec.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result
				+ ((ctipoaport == null) ? 0 : ctipoaport.hashCode());
		result = prime * result
				+ ((descripcionErr == null) ? 0 : descripcionErr.hashCode());
		result = prime * result
				+ ((fecCierre == null) ? 0 : fecCierre.hashCode());
		result = prime * result + ((kajuste == null) ? 0 : kajuste.hashCode());
		result = prime * result
				+ ((kcertificado == null) ? 0 : kcertificado.hashCode());
		result = prime * result
				+ ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result
				+ ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result
				+ ((kprestacion == null) ? 0 : kprestacion.hashCode());
		result = prime * result
				+ ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
		result = prime * result
				+ ((nomModulo == null) ? 0 : nomModulo.hashCode());
		result = prime * result + ((norden == null) ? 0 : norden.hashCode());
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
		ErrorOrquestador other = (ErrorOrquestador) obj;
		if (basetec == null) {
			if (other.basetec != null)
				return false;
		} else if (!basetec.equals(other.basetec))
			return false;
		if (ccanal == null) {
			if (other.ccanal != null)
				return false;
		} else if (!ccanal.equals(other.ccanal))
			return false;
		if (ctipoaport == null) {
			if (other.ctipoaport != null)
				return false;
		} else if (!ctipoaport.equals(other.ctipoaport))
			return false;
		if (descripcionErr == null) {
			if (other.descripcionErr != null)
				return false;
		} else if (!descripcionErr.equals(other.descripcionErr))
			return false;
		if (fecCierre == null) {
			if (other.fecCierre != null)
				return false;
		} else if (!fecCierre.equals(other.fecCierre))
			return false;
		if (kajuste == null) {
			if (other.kajuste != null)
				return false;
		} else if (!kajuste.equals(other.kajuste))
			return false;
		if (kcertificado == null) {
			if (other.kcertificado != null)
				return false;
		} else if (!kcertificado.equals(other.kcertificado))
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
		if (ksubpoliza == null) {
			if (other.ksubpoliza != null)
				return false;
		} else if (!ksubpoliza.equals(other.ksubpoliza))
			return false;
		if (nomModulo == null) {
			if (other.nomModulo != null)
				return false;
		} else if (!nomModulo.equals(other.nomModulo))
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
		return true;
	}

	public String camposTabString() {
		String separador = "\t";
		StringBuilder builder = new StringBuilder();
		builder.append("basetec");
		builder.append(separador+"ccanal");
		builder.append(separador+"fecCierre");
		builder.append(separador+"kmodalidad");
		builder.append(separador+"kpoliza");
		builder.append(separador+"ksubpoliza");
		builder.append(separador+"kcertificado");
		builder.append(separador+"nsuscri");
		builder.append(separador+"norden");
		builder.append(separador+"kgarantia");
		builder.append(separador+"kprestacion");
		builder.append(separador+"kajuste");
		builder.append(separador+"ctipoaport");
		builder.append(separador+"m√≥dulo");
		builder.append(separador+"descripci√≥n");
		return builder.toString();
	}

	
}
