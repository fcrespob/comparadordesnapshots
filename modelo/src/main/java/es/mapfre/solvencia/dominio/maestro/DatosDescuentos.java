package es.mapfre.solvencia.dominio.maestro;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dominio.EntidadBase;


@Portable
public class DatosDescuentos implements EntidadBase<UmicKey> {

	
	public static final int IND_CHCH = 0;
	public static final int IND_MP = 1;
	public static final int IND_GLM = 2;
	public static final int IND_FEC_JUBILACION = 3;

	@PortableProperty(IND_GLM)
	private java.math.BigDecimal glm;
	@PortableProperty(IND_CHCH)
	private java.math.BigDecimal chch;
	@PortableProperty(IND_MP)
	private java.math.BigDecimal mp;
	@PortableProperty(IND_FEC_JUBILACION)
	private Timestamp fecJubilacion;

	

	public java.math.BigDecimal getGlm() {
		return glm;
	}



	public void setGlm(java.math.BigDecimal glm) {
		this.glm = glm;
	}



	public java.math.BigDecimal getChch() {
		return chch;
	}



	public void setChch(java.math.BigDecimal chch) {
		this.chch = chch;
	}



	public java.math.BigDecimal getMp() {
		return mp;
	}



	public void setMp(java.math.BigDecimal mp) {
		this.mp = mp;
	}
	
	
	public Timestamp getFecJubilacion() {
		return fecJubilacion;
	}



	public void setFecJubilacion(Timestamp fecJubilacion) {
		this.fecJubilacion = fecJubilacion;
	}



	@Override
	public UmicKey getKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
