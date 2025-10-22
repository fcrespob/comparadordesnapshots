package es.mapfre.gbt.tablasExperiencia.dominio.entidades;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import es.mapfre.gbt.tablasExperiencia.dominio.keys.UmicKey;

public class Umic {

	public static final int IND_DATOSGENERALES = 1;
	public static final int IND_DATOSCOASEGURO = 2;
	public static final int IND_DURACIONES = 3;
	public static final int IND_ASEGURADOS = 4;
	public static final int IND_PRIMAS = 5;
	public static final int IND_CAPITALES = 6;
	public static final int IND_RENTAS = 7;
	public static final int IND_FECHAS = 8;
	public static final int IND_RESCATES = 9;
	public static final int IND_COMISIONES = 10;
	public static final int IND_BTI = 11;
	public static final int IND_OTROSDATOS = 12;

	
	private UmicKey idUmic;

	
	private DatosGenerales datosGenerales;

	
	private DatosCoaseguro datosCoaseguro;

	
	private Duraciones duraciones;

	
	private Asegurados asegurados;

	
	private Primas primas;

	
	private Capitales capitales;

	
	private Rentas rentas;

	
	private Fechas fechas;

	
	private Rescates rescates;

	
	private Comisiones comisiones;

	
	private BaseTecnicaInicial bti;
	
	private OtrosDatos otrosDatos;

	private List<PlanPagos> lstPlanPagos = new ArrayList<PlanPagos>();

	private Timestamp fnac;

	@Override
	public String toString() {
		return "Umic [IdUmic()=" + getKey() + "]";
	}

	public String getIdUmic() {
		return getKey().toString();
	}

	public UmicKey getKey() {

		if (datosGenerales != null) {
			return datosGenerales.getKey();
		}

		// Siendo estrictos, se devolvería una excepción. NO puede existir una
		// umic sin datos generales
		return null;
	}

	public DatosGenerales getDatosGenerales() {
		OtrosDatos od = this.getOtrosDatos();
		datosGenerales.setGestionit(od.getGestionit());
		datosGenerales.setIndinval(od.getIndinval());
		datosGenerales.setPregrupo(od.getPregrupo());
		return datosGenerales;
	}

	public void setDatosGenerales(DatosGenerales datosGenerales) {
		this.datosGenerales = datosGenerales;
	}

	public DatosCoaseguro getDatosCoaseguro() {
		return datosCoaseguro;
	}

	public void setDatosCoaseguro(DatosCoaseguro datosCoaseguro) {
		this.datosCoaseguro = datosCoaseguro;
	}

	public Duraciones getDuraciones() {
		return duraciones;
	}

	public void setDuraciones(Duraciones duraciones) {
		this.duraciones = duraciones;
	}

	public Asegurados getAsegurados() {
		return asegurados;
	}

	public void setAsegurados(Asegurados asegurados) {
		this.asegurados = asegurados;
	}

	public Primas getPrimas() {
		return primas;
	}

	public void setPrimas(Primas primas) {
		this.primas = primas;
	}

	public Capitales getCapitales() {
		return capitales;
	}

	public void setCapitales(Capitales capitales) {
		this.capitales = capitales;
	}

	public Rentas getRentas() {
		return rentas;
	}

	public void setRentas(Rentas rentas) {
		this.rentas = rentas;
	}

	public Fechas getFechas() {
		return fechas;
	}

	public void setFechas(Fechas fechas) {
		this.fechas = fechas;
	}

	public Rescates getRescates() {
		return rescates;
	}

	public void setRescates(Rescates rescates) {
		this.rescates = rescates;
	}

	public Comisiones getComisiones() {
		return comisiones;
	}

	public void setComisiones(Comisiones comisiones) {
		this.comisiones = comisiones;
	}

	public List<PlanPagos> getLstPlanPagos() {
		return lstPlanPagos;
	}

	public void setLstPlanPagos(List<PlanPagos> lstPlanPagos) {
		this.lstPlanPagos = lstPlanPagos;
	}

	public Timestamp getFnac() {
		return fnac;
	}

	public void setFnac(Timestamp fnac) {
		this.fnac = fnac;
	}

	public BaseTecnicaInicial getBti() {
		OtrosDatos od = this.getOtrosDatos();
		if (od.getSigpitertecn1().equals("-") &&
				bti.getPintertecnI1().signum() > 0){
			bti.setPintertecnI1(bti.getPintertecnI1().negate());
		}
		if (od.getSigpitertecn2().equals("-") &&
				bti.getPintertecnI2().signum() > 0){
			bti.setPintertecnI2(bti.getPintertecnI2().negate());
		}
		if (od.getSigpitertecn3().equals("-") &&
				bti.getPintertecnI3().signum() > 0){
			bti.setPintertecnI3(bti.getPintertecnI3().negate());
		}
		if (od.getSigpitertecn4().equals("-") &&
				bti.getPintertecnI4().signum() > 0){
			bti.setPintertecnI4(bti.getPintertecnI4().negate());
		}
		if (od.getSigpitertecn5().equals("-") &&
				bti.getPintertecnI5().signum() > 0){
			bti.setPintertecnI5(bti.getPintertecnI5().negate());
		}
		return bti;
	}

	public void setBti(BaseTecnicaInicial bti) {
		this.bti = bti;
	}

	public OtrosDatos getOtrosDatos() {
		return otrosDatos;
	}

	public void setOtrosDatos(OtrosDatos otrosDatos) {
		this.otrosDatos = otrosDatos;
	}
	
}
