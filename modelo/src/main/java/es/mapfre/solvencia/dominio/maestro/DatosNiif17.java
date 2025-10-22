package es.mapfre.solvencia.dominio.maestro;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class DatosNiif17 implements EntidadBase<UmicKey> {

	public static final int IND_SWMODONER= 1;
	public static final int IND_SWPOLONER= 2;
	public static final int IND_KCARCONTACTO = 3;
	public static final int IND_METMEDICION = 4;
	public static final int IND_UOA = 5;
	public static final int IND_PROXYRA = 6;
	public static final int IND_PROXYCSM = 7;
	public static final int IND_KCARINV17 = 8;
	public static final int IND_KCURVALIR = 9;
	public static final int IND_KCARINVLIR = 10;
	public static final int IND_COHORTE = 11;
	public static final int IND_PVENTA = 12;
	public static final int IND_FPROYFLUJEST = 13;
	public static final int IND_AGDPG = 14;
	public static final int IND_ARECT = 15;
	public static final int IND_KFONDO = 16;


	@PortableProperty(IND_SWMODONER)
	private String swmodoner;
	@PortableProperty(IND_SWPOLONER)
	private String swpoloner;
	@PortableProperty(IND_KCARCONTACTO)
	private String kcarcontacto;
	@PortableProperty(IND_METMEDICION)
	private String metmedicion;
	@PortableProperty(IND_UOA)
	private String uoa;
	@PortableProperty(IND_PROXYRA)
	private java.math.BigDecimal proxyra;
	@PortableProperty(IND_PROXYCSM)
	private java.math.BigDecimal proxycsm;
	@PortableProperty(IND_KCARINV17)
	private String kcarinv17;
	@PortableProperty(IND_KCURVALIR)
	private String kcurvalir;
	@PortableProperty(IND_KCARINVLIR)
	private String kcarinvlir;
	@PortableProperty(IND_COHORTE)
	private String cohorte;
	@PortableProperty(IND_PVENTA)
	private String pventa;
	@PortableProperty(IND_FPROYFLUJEST)
	private String fproyflujest;
	
	@PortableProperty(IND_AGDPG)
	private String agdpg;
	@PortableProperty(IND_ARECT)
	private Integer arect;
	@PortableProperty(IND_KFONDO)
	private String kfondo;

	public String getswmodoner() {
		return swmodoner;
	}

	public void setswmodoner(String swmodoner) {
		this.swmodoner = swmodoner;
	}

	public String getswpoloner() {
		return swpoloner;
	}

	public void setswpoloner(String swpoloner) {
		this.swpoloner = swpoloner;
	}

	public String getkcarcontacto() {
		return kcarcontacto;
	}

	public void setkcarcontacto(String kcarcontacto) {
		this.kcarcontacto = kcarcontacto;
	}

	public String getmetmedicion() {
		return metmedicion;
	}

	public void setmetmedicion(String metmedicion) {
		this.metmedicion = metmedicion;
	}

	public String getuoa() {
		return uoa;
	}

	public void setuoa(String uoa) {
		this.uoa = uoa;
	}

	public java.math.BigDecimal getproxyra() {
		return proxyra;
	}

	public void setproxyra(java.math.BigDecimal proxyra) {
		this.proxyra = proxyra;
	}

	public java.math.BigDecimal getproxycsm() {
		return proxycsm;
	}

	public void setproxycsm(java.math.BigDecimal proxycsm) {
		this.proxycsm = proxycsm;
	}

	public String getkcarinv17() {
		return kcarinv17;
	}

	public void setkcarinv17(String kcarinv17) {
		this.kcarinv17 = kcarinv17;
	}

	public String getkcurvalir() {
		return kcurvalir;
	}

	public void setkcurvalir(String kcurvalir) {
		this.kcurvalir = kcurvalir;
	}

	public String getkcarinvlir() {
		return kcarinvlir;
	}

	public void setkcarinvlir(String kcarinvlir) {
		this.kcarinvlir = kcarinvlir;
	}

	public String getcohorte() {
		return cohorte;
	}

	public void setcohorte(String cohorte) {
		this.cohorte = cohorte;
	}

	public String getPventa() {
		return pventa;
	}

	public void setPventa(String pventa) {
		this.pventa = pventa;
	}

	public String getFproyflujest() {
		return fproyflujest;
	}

	public void setFproyflujest(String fproyflujest) {
		this.fproyflujest = fproyflujest;
	}
	
	public String getAgdpg() {
		return agdpg;
	}

	public void setAgdpg(String agdpg) {
		this.agdpg = agdpg;
	}

	public Integer getArect() {
		return arect;
	}

	public void setArect(Integer arect) {
		this.arect = arect;
	}
	
	public String getKfondo() {
		return kfondo;
	}

	public void setKfondo(String kfondo) {
		this.kfondo = kfondo;
	}

	@Override
	public UmicKey getKey() {
		return null;
	}
}