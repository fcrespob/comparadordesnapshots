package es.mapfre.solvencia.dominio.parametrizacionGeneral;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.InfoPtipoKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class InfoPtipo implements EntidadBase<InfoPtipoKey> {

	public static final int IND_KMODALIDAD = 0;
	public static final int IND_KGARANTIA = 1;
	public static final int IND_KPRESTACION = 2;
	public static final int IND_NUMUMICS = 3;
	public static final int IND_TOTALUMICS = 4;


	@PortableProperty(IND_KMODALIDAD)
	private Integer kmodalidad;
	@PortableProperty(IND_KGARANTIA)
	private Integer kgarantia;
	@PortableProperty(IND_KPRESTACION)
	private String kprestacion;
	@PortableProperty(IND_NUMUMICS)
	private Integer numUmics = 0;
	@PortableProperty(IND_TOTALUMICS)
	private Integer totalUmics;

	public Integer getKmodalidad() {
		return kmodalidad;
	}

	public void setKmodalidad(Integer kmodalidad) {
		this.kmodalidad = kmodalidad;
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


	public Integer getNumUmics() {
		return numUmics;
	}


	public void setNumUmics(Integer numUmics) {
		this.numUmics = numUmics;
	}
	
	public Integer addNumUmics(){
		return this.numUmics++;
	}

	public Integer getTotalUmics() {
		return totalUmics;
	}


	public void setTotalUmics(Integer totalUmics) {
		this.totalUmics = totalUmics;
	}
	
	public boolean cabenMas(){
		return cabenMas(Boolean.TRUE);
	}

	public boolean cabenMas(Boolean forzar){
		return forzar ? totalUmics > numUmics : numUmics == 0;
	}

	@Override
	public InfoPtipoKey getKey() {
		return new InfoPtipoKey(kmodalidad, kgarantia, kprestacion);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InfoPtipo [kmodalidad=");
		builder.append(kmodalidad);
		builder.append(", kgarantia=");
		builder.append(kgarantia);
		builder.append(", kprestacion=");
		builder.append(kprestacion);
		builder.append(", totalUmics=");
		builder.append(totalUmics);
		builder.append(", numUmicsPtipo=");
		builder.append(numUmics);
		builder.append("]");
		return builder.toString();
	}
}
