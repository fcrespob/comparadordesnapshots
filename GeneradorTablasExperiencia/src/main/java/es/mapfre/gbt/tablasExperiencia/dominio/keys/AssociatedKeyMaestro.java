package es.mapfre.gbt.tablasExperiencia.dominio.keys;

/* MODIFICACION : 100405260 SOLVENCIA II - CÁLCULO Y GENERACIÓN DE FLUJOS FASE V
FECHA : 20/11/2015
DESCRIPCION:SOLV2VIDA Cambio de tipo de dato en kpoliza y GBT 
AUTOR : JVV */

public class AssociatedKeyMaestro {

//100405260-INI	
//	private Integer kpoliza;
	private Long kpoliza;
//100405260-FIN
	private Integer ksubpoliza;
	private Integer kgarantia;
	private Integer kajuste;
	private Integer nsuscri;
	private Integer kcertificado;
	private String kprestacion;

//100405260-INI	
//	public AssociatedKeyMaestro(Integer kpoliza, Integer ksubpoliza,	Integer kgarantia, Integer kajuste, Integer nsuscri, Integer kcertificado, String kprestacion) {
	public AssociatedKeyMaestro(Long kpoliza, Integer ksubpoliza,	Integer kgarantia, Integer kajuste, Integer nsuscri, Integer kcertificado, String kprestacion) {
//100405260-FIN	
		super();
		this.kpoliza = kpoliza;
		this.ksubpoliza = ksubpoliza;
		this.kgarantia = kgarantia;
		this.kajuste = kajuste;
		this.nsuscri = nsuscri;
		this.kcertificado = kcertificado;
		this.kprestacion = kprestacion;
	}

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kajuste == null) ? 0 : kajuste.hashCode());
		result = prime * result
				+ ((kcertificado == null) ? 0 : kcertificado.hashCode());
		result = prime * result
				+ ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result
				+ ((kprestacion == null) ? 0 : kprestacion.hashCode());
		result = prime * result
				+ ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
		result = prime * result + ((nsuscri == null) ? 0 : nsuscri.hashCode());
		return result;
	}

	@Override //NOSONAR
	public boolean equals(Object obj) { //NOSONAR
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AssociatedKeyMaestro other = (AssociatedKeyMaestro) obj;
		if (kajuste == null) {
			if (other.kajuste != null) {
				return false;
			}
		} else if (!kajuste.equals(other.kajuste)) {
			return false;
		}
		if (kcertificado == null) {
			if (other.kcertificado != null) {
				return false;
			}
		} else if (!kcertificado.equals(other.kcertificado)) {
			return false;
		}
		if (kgarantia == null) {
			if (other.kgarantia != null) {
				return false;
			}
		} else if (!kgarantia.equals(other.kgarantia)) {
			return false;
		}
		if (kpoliza == null) {
			if (other.kpoliza != null) {
				return false;
			}
		} else if (!kpoliza.equals(other.kpoliza)) {
			return false;
		}
		if (kprestacion == null) {
			if (other.kprestacion != null) {
				return false;
			}
		} else if (!kprestacion.equals(other.kprestacion)) {
			return false;
		}
		if (ksubpoliza == null) {
			if (other.ksubpoliza != null) {
				return false;
			}
		} else if (!ksubpoliza.equals(other.ksubpoliza)) {
			return false;
		}
		if (nsuscri == null) {
			if (other.nsuscri != null) {
				return false;
			}
		} else if (!nsuscri.equals(other.nsuscri)) {
			return false;
		}
		return true;
	}

	
}
