package es.mapfre.coaseguro.tirea.dominio.entidades;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "idGarantia",
    "idGarantiaPrincipal",
    "codGarantia",
    "fechaInicio",
    "fechaFin",
    "primaTotal",
    "primaNeta",
    "capital",
    "sobreMortalidad",
    "sobreRiesgo",
    "extraPrima",
    "tipoPrestacion",
    "formaPago",
    "fechaInicioRentaCierta",
    "fechaFinRentaCierta",
    "tipoRenta",
    "criterioEdad",
    "edadMaxGarantia",
    "crecimiento",
    "datosCrecimientos",
    "revalorizacion",
    "datosRevalorizaciones",
    "diferimiento",
    "datosDiferimientos",
    "mesesPagaExtra",
    "periodicidadRentaDevengo",
    "tipoPagoRenta",
    "diaPago",
    "prorrata",
    "pbFinanciera",
    "importeProvision",
    "reversion",
    "datosReversion"
})
@XmlRootElement(name = "GarantiaPrestacion")
public class GarantiaPrestacion {

	
	private String IdGarantia;
	private String IdGarantiaPrincipal;
	private String CodGarantia;
	private String FechaInicio;
	private String FechaFin;
	private String PrimaTotal;
	private String PrimaNeta;
	private String Capital;
	private String SobreMortalidad;
	private String SobreRiesgo;
	private String ExtraPrima;
	private String TipoPrestacion;
	private String FormaPago;
	private String FechaInicioRentaCierta;
	private String FechaFinRentaCierta;
	private String TipoRenta;
	private String CriterioEdad;
	private String EdadMaxGarantia;
	private String Crecimiento;
	private DatosCrecimientos DatosCrecimientos;
	private String Revalorizacion;
	private DatosRevalorizaciones DatosRevalorizaciones;
	private String Diferimiento;
	private DatosDiferimientos DatosDiferimientos;
	private String MesesPagaExtra;
	private String PeriodicidadRentaDevengo;
	private String TipoPagoRenta;
	private String DiaPago;
	private String Prorrata;
	private String PbFinanciera;
	private String ImporteProvision;
	private String Reversion;
	private DatosReversion DatosReversion;
	
	public GarantiaPrestacion() {
		super();
	}

	public String getIdGarantia() {
		return IdGarantia;
	}

	@XmlElement(name = "IdGarantia")
	public void setIdGarantia(String idGarantia) {
		IdGarantia = idGarantia;
	}

	public String getIdGarantiaPrincipal() {
		return IdGarantiaPrincipal;
	}

	@XmlElement(name = "IdGarantiaPrincipal")
	public void setIdGarantiaPrincipal(String idGarantiaPrincipal) {
		IdGarantiaPrincipal = idGarantiaPrincipal;
	}

	public String getCodGarantia() {
		return CodGarantia;
	}

	@XmlElement(name = "CodGarantia")
	public void setCodGarantia(String codGarantia) {
		CodGarantia = codGarantia;
	}

	public String getFechaInicio() {
		return FechaInicio;
	}

	@XmlElement(name = "FechaInicio")
	public void setFechaInicio(String fechaInicio) {
		FechaInicio = fechaInicio;
	}

	public String getFechaFin() {
		return FechaFin;
	}

	@XmlElement(name = "FechaFin")
	public void setFechaFin(String fechaFin) {
		FechaFin = fechaFin;
	}

	public String getPrimaTotal() {
		return PrimaTotal;
	}

	@XmlElement(name = "PrimaTotal")
	public void setPrimaTotal(String primaTotal) {
		PrimaTotal = primaTotal;
	}

	public String getPrimaNeta() {
		return PrimaNeta;
	}

	@XmlElement(name = "PrimaNeta")
	public void setPrimaNeta(String primaNeta) {
		PrimaNeta = primaNeta;
	}

	public String getCapital() {
		return Capital;
	}

	@XmlElement(name = "Capital")
	public void setCapital(String capital) {
		Capital = capital;
	}

	public String getSobreMortalidad() {
		return SobreMortalidad;
	}

	@XmlElement(name = "SobreMortalidad")
	public void setSobreMortalidad(String sobreMortalidad) {
		SobreMortalidad = sobreMortalidad;
	}

	public String getSobreRiesgo() {
		return SobreRiesgo;
	}

	@XmlElement(name = "SobreRiesgo")
	public void setSobreRiesgo(String sobreRiesgo) {
		SobreRiesgo = sobreRiesgo;
	}

	public String getExtraPrima() {
		return ExtraPrima;
	}

	@XmlElement(name = "ExtraPrima")
	public void setExtraPrima(String extraPrima) {
		ExtraPrima = extraPrima;
	}

	public String getTipoPrestacion() {
		return TipoPrestacion;
	}

	@XmlElement(name = "TipoPrestacion")
	public void setTipoPrestacion(String tipoPrestacion) {
		TipoPrestacion = tipoPrestacion;
	}

	public String getFormaPago() {
		return FormaPago;
	}

	@XmlElement(name = "FormaPago")
	public void setFormaPago(String formaPago) {
		FormaPago = formaPago;
	}

	public String getFechaInicioRentaCierta() {
		return FechaInicioRentaCierta;
	}

	@XmlElement(name = "FechaInicioRentaCierta")
	public void setFechaInicioRentaCierta(String fechaInicioRentaCierta) {
		FechaInicioRentaCierta = fechaInicioRentaCierta;
	}

	public String getFechaFinRentaCierta() {
		return FechaFinRentaCierta;
	}

	@XmlElement(name = "FechaFinRentaCierta")
	public void setFechaFinRentaCierta(String fechaFinRentaCierta) {
		FechaFinRentaCierta = fechaFinRentaCierta;
	}

	public String getTipoRenta() {
		return TipoRenta;
	}

	@XmlElement(name = "TipoRenta")
	public void setTipoRenta(String tipoRenta) {
		TipoRenta = tipoRenta;
	}

	public String getCriterioEdad() {
		return CriterioEdad;
	}

	@XmlElement(name = "CriterioEdad")
	public void setCriterioEdad(String criterioEdad) {
		CriterioEdad = criterioEdad;
	}

	public String getEdadMaxGarantia() {
		return EdadMaxGarantia;
	}

	@XmlElement(name = "EdadMaxGarantia")
	public void setEdadMaxGarantia(String edadMaxGarantia) {
		EdadMaxGarantia = edadMaxGarantia;
	}

	public String getCrecimiento() {
		return Crecimiento;
	}

	@XmlElement(name = "Crecimiento")
	public void setCrecimiento(String crecimiento) {
		Crecimiento = crecimiento;
	}

	public DatosCrecimientos getDatosCrecimientos() {
		return DatosCrecimientos;
	}

	@XmlElement(name = "DatosCrecimientos")
	public void setDatosCrecimientos(DatosCrecimientos datosCrecimientos) {
		DatosCrecimientos = datosCrecimientos;
	}

	public String getRevalorizacion() {
		return Revalorizacion;
	}

	@XmlElement(name = "Revalorizacion")
	public void setRevalorizacion(String revalorizacion) {
		Revalorizacion = revalorizacion;
	}

	public DatosRevalorizaciones getDatosRevalorizaciones() {
		return DatosRevalorizaciones;
	}

	@XmlElement(name = "DatosRevalorizaciones")
	public void setDatosRevalorizaciones(DatosRevalorizaciones datosRevalorizaciones) {
		DatosRevalorizaciones = datosRevalorizaciones;
	}

	public String getDiferimiento() {
		return Diferimiento;
	}

	@XmlElement(name = "Diferimiento")
	public void setDiferimiento(String diferimiento) {
		Diferimiento = diferimiento;
	}

	public DatosDiferimientos getDatosDiferimientos() {
		return DatosDiferimientos;
	}

	@XmlElement(name = "DatosDiferimientos")
	public void setDatosDiferimientos(DatosDiferimientos datosDiferimientos) {
		DatosDiferimientos = datosDiferimientos;
	}

	public String getMesesPagaExtra() {
		return MesesPagaExtra;
	}

	@XmlElement(name = "MesesPagaExtra")
	public void setMesesPagaExtra(String mesesPagaExtra) {
		MesesPagaExtra = mesesPagaExtra;
	}

	public String getPeriodicidadRentaDevengo() {
		return PeriodicidadRentaDevengo;
	}

	@XmlElement(name = "PeriodicidadRentaDevengo")
	public void setPeriodicidadRentaDevengo(String periodicidadRentaDevengo) {
		PeriodicidadRentaDevengo = periodicidadRentaDevengo;
	}

	public String getTipoPagoRenta() {
		return TipoPagoRenta;
	}

	@XmlElement(name = "TipoPagoRenta")
	public void setTipoPagoRenta(String tipoPagoRenta) {
		TipoPagoRenta = tipoPagoRenta;
	}

	public String getDiaPago() {
		return DiaPago;
	}

	@XmlElement(name = "DiaPago")
	public void setDiaPago(String diaPago) {
		DiaPago = diaPago;
	}

	public String getProrrata() {
		return Prorrata;
	}

	@XmlElement(name = "Prorrata")
	public void setProrrata(String prorrata) {
		Prorrata = prorrata;
	}

	public String getPbFinanciera() {
		return PbFinanciera;
	}

	@XmlElement(name = "PbFinanciera")
	public void setPbFinanciera(String pbFinanciera) {
		PbFinanciera = pbFinanciera;
	}

	public String getImporteProvision() {
		return ImporteProvision;
	}

	@XmlElement(name = "ImporteProvision")
	public void setImporteProvision(String importeProvision) {
		ImporteProvision = importeProvision;
	}

	public String getReversion() {
		return Reversion;
	}

	@XmlElement(name = "Reversion")
	public void setReversion(String reversion) {
		Reversion = reversion;
	}

	public DatosReversion getDatosReversion() {
		return DatosReversion;
	}

	@XmlElement(name = "DatosReversion")
	public void setDatosReversion(DatosReversion datosReversion) {
		DatosReversion = datosReversion;
	}
	
}

