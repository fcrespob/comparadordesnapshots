package es.mapfre.solvencia.dominio.maestro;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dominio.formulacion.PlanPagos;

@Portable
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
	public static final int IND_DATOSADICIONALES = 12;
	public static final int IND_OTROSDATOS = 13;
	public static final int IND_DATOSNIIF17 = 14;
	public static final int IND_DATOSDESCUENTOS = 15;

	@PortableProperty(0)
	private UmicKey idUmic;

	@PortableProperty(IND_DATOSGENERALES)
	private DatosGenerales datosGenerales;

	@PortableProperty(IND_DATOSCOASEGURO)
	private DatosCoaseguro datosCoaseguro;

	@PortableProperty(IND_DURACIONES)
	private Duraciones duraciones;

	@PortableProperty(IND_ASEGURADOS)
	private Asegurados asegurados;

	@PortableProperty(IND_PRIMAS)
	private Primas primas;

	@PortableProperty(IND_CAPITALES)
	private Capitales capitales;

	@PortableProperty(IND_RENTAS)
	private Rentas rentas;

	@PortableProperty(IND_FECHAS)
	private Fechas fechas;

	@PortableProperty(IND_RESCATES)
	private Rescates rescates;

	@PortableProperty(IND_COMISIONES)
	private Comisiones comisiones;

	@PortableProperty(IND_BTI)
	private BaseTecnicaInicial bti;
	
	@PortableProperty(IND_DATOSADICIONALES)
	private DatosAdicionales datosAdicionales;
	
	@PortableProperty(IND_OTROSDATOS)
	private OtrosDatos otrosDatos;
	@PortableProperty(IND_DATOSNIIF17)
	private DatosNiif17 datosNiif17;
	
	@PortableProperty(IND_DATOSDESCUENTOS)
	private DatosDescuentos datosDescuentos;

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

	public DatosAdicionales getDatosAdicionales() {
		return datosAdicionales;
	}

	public void setDatosAdicionales(DatosAdicionales datosAdicionales) {
		this.datosAdicionales = datosAdicionales;
	}
	
	public OtrosDatos getOtrosDatos() {
		return otrosDatos;
	}

	public void setOtrosDatos(OtrosDatos otrosDatos) {
		this.otrosDatos = otrosDatos;
	}
	
	public DatosNiif17 getDatosNiif17() {
		return datosNiif17;
	}
	
	public void setDatosNiif17(DatosNiif17 datosNiif17) {
		this.datosNiif17 = datosNiif17;
	}
	
	public DatosDescuentos getDatosDescuentos() {
		return datosDescuentos;
	}

	public void setDatosDescuentos(DatosDescuentos datosDescuentos) {
		this.datosDescuentos = datosDescuentos;
	}
}
