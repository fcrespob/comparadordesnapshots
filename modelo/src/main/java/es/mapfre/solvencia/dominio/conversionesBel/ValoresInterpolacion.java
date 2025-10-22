package es.mapfre.solvencia.dominio.conversionesBel;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.conversionesBel.ValoresInterpolacionKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
@Deprecated //A eliminar en 10 dias a partir del 22/04/2014
public class ValoresInterpolacion implements EntidadBase<ValoresInterpolacionKey> {
	
	public static final int IND_DIASPLAZOANT = 0;
	public static final int IND_DIASPLAZOPOST = 1;
	public static final int IND_PORCENTAJEINTERESANT = 2;
	public static final int IND_PORCENTAJEINTERESPOST = 3;
	
	@PortableProperty(IND_DIASPLAZOANT) private Integer diasPlazoAnt;
	@PortableProperty(IND_DIASPLAZOPOST) private Integer diasPlazoPost;
	@PortableProperty(IND_PORCENTAJEINTERESANT) private Integer porcentajeInteresAnt;
	@PortableProperty(IND_PORCENTAJEINTERESPOST) private Integer porcentajeInteresPost;
	
	public ValoresInterpolacion() {
		super();
	}

	public Integer getDiasPlazoAnt() {
		return diasPlazoAnt;
	}

	public void setDiasPlazoAnt(Integer diasPlazoAnt) {
		this.diasPlazoAnt = diasPlazoAnt;
	}

	public Integer getDiasPlazoPost() {
		return diasPlazoPost;
	}

	public void setDiasPlazoPost(Integer diasPlazoPost) {
		this.diasPlazoPost = diasPlazoPost;
	}

	public Integer getPorcentajeInteresAnt() {
		return porcentajeInteresAnt;
	}

	public void setPorcentajeInteresAnt(Integer porcentajeInteresAnt) {
		this.porcentajeInteresAnt = porcentajeInteresAnt;
	}

	public Integer getPorcentajeInteresPost() {
		return porcentajeInteresPost;
	}

	public void setPorcentajeInteresPost(Integer porcentajeInteresPost) {
		this.porcentajeInteresPost = porcentajeInteresPost;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((diasPlazoAnt == null) ? 0 : diasPlazoAnt.hashCode());
		result = prime * result
				+ ((diasPlazoPost == null) ? 0 : diasPlazoPost.hashCode());
		result = prime
				* result
				+ ((porcentajeInteresAnt == null) ? 0 : porcentajeInteresAnt
						.hashCode());
		result = prime
				* result
				+ ((porcentajeInteresPost == null) ? 0 : porcentajeInteresPost
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ValoresInterpolacion other = (ValoresInterpolacion) obj;
		if (diasPlazoAnt == null) {
			if (other.diasPlazoAnt != null) {
				return false;
			}
		} else if (!diasPlazoAnt.equals(other.diasPlazoAnt)) {
			return false;
		}
		if (diasPlazoPost == null) {
			if (other.diasPlazoPost != null) {
				return false;
			}
		} else if (!diasPlazoPost.equals(other.diasPlazoPost)) {
			return false;
		}
		if (porcentajeInteresAnt == null) {
			if (other.porcentajeInteresAnt != null) {
				return false;
			}
		} else if (!porcentajeInteresAnt.equals(other.porcentajeInteresAnt)) {
			return false;
		}
		if (porcentajeInteresPost == null) {
			if (other.porcentajeInteresPost != null) {
				return false;
			}
		} else if (!porcentajeInteresPost.equals(other.porcentajeInteresPost)) {
			return false;
		}
		return true;
	}

	@Override
	public ValoresInterpolacionKey getKey() {
		// No existe la posibilidad de crear una UmicKey a partir de los datos
		return null;
	}

	
}
