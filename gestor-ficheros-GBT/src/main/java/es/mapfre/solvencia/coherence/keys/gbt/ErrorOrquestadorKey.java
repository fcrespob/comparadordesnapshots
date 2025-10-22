package es.mapfre.solvencia.coherence.keys.gbt;

/* MODIFICACION : 100405260 SOLVENCIA II - CÁLCULO Y GENERACIÓN DE FLUJOS FASE V
FECHA : 20/11/2015
DESCRIPCION:SOLV2VIDA Cambio de tipo de dato en kpoliza y GBT 
AUTOR : JVV */

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.gbt.ErrorOrquestador;

@Portable
public class ErrorOrquestadorKey  {
	@PortableProperty(ErrorOrquestador.IND_EKMODALIDAD)
	private Integer kmodalidad;
	@PortableProperty(ErrorOrquestador.IND_KPOLIZA)
//100405260-INI
//	private Integer kpoliza;
	private Long kpoliza;
//100405260-FIN
	@PortableProperty(ErrorOrquestador.IND_EKSUBPOLIZA)
	private Integer ksubpoliza;
	@PortableProperty(ErrorOrquestador.IND_KCERTIFICADO)
	private Integer kcertificado;
	@PortableProperty(ErrorOrquestador.IND_ENSUSCRI)
	private Integer nsuscri;
	@PortableProperty(ErrorOrquestador.IND_ENORDEN)
	private Integer norden;
	@PortableProperty(ErrorOrquestador.IND_EKGARANTIA)
	private Integer kgarantia;
	@PortableProperty(ErrorOrquestador.IND_KPRESTACION)
	private String kprestacion;
	@PortableProperty(ErrorOrquestador.IND_EKAJUSTE)
	private Integer kajuste;
	@PortableProperty(ErrorOrquestador.IND_CTIPOAPORT)
	private String ctipoaport;
	@PortableProperty(ErrorOrquestador.IND_NMODULO)
	private String nomModulo;
	@PortableProperty(ErrorOrquestador.IND_DESCERR)
	private String descripcionErr;
	
	public ErrorOrquestadorKey(){
		super();
	}

//100405260-INI
//	public ErrorOrquestadorKey(Integer kmodalidad, Integer kpoliza,
//			Integer ksubpoliza, Integer kcertificado, Integer nsuscri,
//			Integer norden, Integer kgarantia, String kprestacion,
//			Integer kajuste, String ctipoaport, String nomModulo,
//			String descripcionErr) {
	public ErrorOrquestadorKey(Integer kmodalidad, Long kpoliza,
			Integer ksubpoliza, Integer kcertificado, Integer nsuscri,
			Integer norden, Integer kgarantia, String kprestacion,
			Integer kajuste, String ctipoaport, String nomModulo,
			String descripcionErr) {
//100405260-FIN
		super();
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ctipoaport == null) ? 0 : ctipoaport.hashCode());
		result = prime * result
				+ ((descripcionErr == null) ? 0 : descripcionErr.hashCode());
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
		ErrorOrquestadorKey other = (ErrorOrquestadorKey) obj;
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
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ErrorOrquestadorKey [kmodalidad=");
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
		


	
}
