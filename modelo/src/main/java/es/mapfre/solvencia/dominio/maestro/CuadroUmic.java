package es.mapfre.solvencia.dominio.maestro;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class CuadroUmic {
	
	public CuadroUmic(){}
	
	private Integer varMes;
	private Timestamp varFecha;
	private BigDecimal vardeltaj;
	private BigDecimal varCuotaj;
	private BigDecimal varCpj;
	private BigDecimal varSicuota;
	
	
	public Integer getVarMes() {
		return varMes;
	}
	public void setVarMes(Integer varMes) {
		this.varMes = varMes;
	}
	
	public Timestamp getVarFecha() {
		return varFecha;
	}
	public void setVarFecha(Timestamp varFecha) {
		this.varFecha = varFecha;
	}
	
	public BigDecimal getVardeltaj() {
		return vardeltaj;
	}
	public void setVardeltaj(BigDecimal vardeltaj) {
		this.vardeltaj = vardeltaj;
	}
	
	public BigDecimal getVarCuotaj() {
		return varCuotaj;
	}
	public void setVarCuotaj(BigDecimal varCuotaj) {
		this.varCuotaj = varCuotaj;
	}
	
	public BigDecimal getVarCpj() {
		return varCpj;
	}
	public void setVarCpj(BigDecimal varCpj) {
		this.varCpj = varCpj;
	}
	
	public BigDecimal getVarSicuota() {
		return varSicuota;
	}
	public void setVarSicuota(BigDecimal varSicuota) {
		this.varSicuota = varSicuota;
	}
	
}
