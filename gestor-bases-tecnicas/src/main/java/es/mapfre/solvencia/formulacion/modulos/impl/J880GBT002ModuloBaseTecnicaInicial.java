package es.mapfre.solvencia.formulacion.modulos.impl;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import es.mapfre.solvencia.dominio.gbt.AsigInteresTecnico;
import es.mapfre.solvencia.dominio.gbt.RentaGAP;
import es.mapfre.solvencia.dao.impl.gbt.AsigInteresTecnicoDao;
import es.mapfre.solvencia.dao.impl.gbt.RentaGAPDao;
import es.mapfre.solvencia.dominio.maestro.Asegurados;
import es.mapfre.solvencia.dominio.maestro.BaseTecnicaInicial;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;															
import es.mapfre.solvencia.dominio.salidaCalculo.TablaConversion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;																	  
import es.mapfre.solvencia.excepcionGBT.GestorBasesTecnicasException;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.gbt.util.ConstantesErrores;
import es.mapfre.solvencia.gbt.util.ConstantesGBT;
import es.mapfre.solvencia.gbt.util.ConstantesModulosGBT;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;													 																   


public class J880GBT002ModuloBaseTecnicaInicial implements Modulo {
	
	GestorBasesTecnicasException exc = new GestorBasesTecnicasException();
	
	
	@Override
	public Object execute(Object... args) {
		
		
		final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantesGBT.PARAM_BTC];
		final Umic umic = (Umic) args[ConstantesGBT.PARAM_UMI_BASE_TEC];
		
			
			
		
		try{
			exc.setGeneradorError(getNombreServicio());
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_MOD_NO_CONT);
			exc.setTipoError(ConstantesErrores.CTE_ERROR);
			
			//Calculo de base tecnica
	
			// Base tecnica inicial de la umic y Datos generales
			final BaseTecnicaInicial bti = umic.getBti();
			final DatosGenerales datosGenerales = umic.getDatosGenerales();
			final Asegurados asegurados = umic.getAsegurados();

			// No se comprueban los campos
			//comprobarDatos(umic,ficha);
			
			//btcUmic.setBaseTec(ficha.getBaseTec());
			btcUmic.setFecCierre(umic.getDatosGenerales().getFecCierre());
			
			btcUmic.setCcanal(umic.getDatosGenerales().getCcanal());
			btcUmic.setCnegocio(umic.getDatosGenerales().getCnegocio());
			// Cartera
			btcUmic.setCcartera(datosGenerales.getCcartera());
			// Establecer fecIniFinTramoX
			
			establecerFecIniFinTramoX(btcUmic, bti);
			
			// Interes calculado
			final List<BigDecimal> itcalc = new ArrayList<BigDecimal>();
			itcalc.add(bti.getPintertecnI1());
			itcalc.add(bti.getPintertecnI2());
			itcalc.add(bti.getPintertecnI3());
			itcalc.add(bti.getPintertecnI4());
			itcalc.add(bti.getPintertecnI5());
			btcUmic.setItcalc(itcalc);
			// Grupo Activo Pasivo
			final List<String> grupoAP = new ArrayList<String>();
			grupoAP.add(bti.getGapI1());
			grupoAP.add(bti.getGapI2());
			grupoAP.add(bti.getGapI3());
			grupoAP.add(bti.getGapI4());
			grupoAP.add(bti.getGapI5());
			btcUmic.setGrupoActivoPasivo(grupoAP);
			// SWCasado
			final List<String> lstSwcasado = new ArrayList<String>();
			lstSwcasado.add(bti.getSwcasadoI1());
			lstSwcasado.add(bti.getSwcasadoI2());
			lstSwcasado.add(bti.getSwcasadoI3());
			lstSwcasado.add(bti.getSwcasadoI4());
			lstSwcasado.add(bti.getSwcasadoI5());
			btcUmic.setSwcasado(lstSwcasado);
			// Modalidad
			final Integer modalidad = datosGenerales.getKmodalidad();
			// Tabla asegurado
			establecerTablaAseg(btcUmic, bti, modalidad, asegurados);
			// GTO ROSSP
			btcUmic.setGtorosspPrima(bti.getPgastgesin2I());
			btcUmic.setGtorosspCap(bti.getPgastgesin1I());
			btcUmic.setGtorosspProv(bti.getPgastgesin3I());
			
			btcUmic.setCtipoaport(umic.getKey().getCtipoaport());
			
			btcUmic.setUmicKey(datosGenerales.getKey());

			// Edad del asegurado
			final List<BigDecimal> edadAseg = new ArrayList<BigDecimal>();
			edadAseg.add(new BigDecimal(asegurados.getEdadAseg1()));
			edadAseg.add(new BigDecimal(asegurados.getEdadAseg2()));
			edadAseg.add(new BigDecimal(asegurados.getEdadAseg3()));
			edadAseg.add(new BigDecimal(asegurados.getEdadAseg4()));
			edadAseg.add(new BigDecimal(asegurados.getEdadAseg5()));
			btcUmic.setEdadcalc(edadAseg);
			
		} catch (Exception e) {		
			final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();
			Incidencia aviso = Solvencia2ExcepcionHelper.crearAviso(btcUmic.getBaseTec(), umic.getDatosGenerales().getCcanal(), umic.getDatosGenerales().getCcartera(), umic.getKey(), btcUmic.getFecCierre(), umic.getDatosGenerales().getCnegocio(), this.getNombreServicio(), ConstantsFunciones.CTE_COD_ERROR_I006, ArrayUtils.EMPTY_OBJECT_ARRAY);
			String vacio = "";
			if(null != exc.getInfAmpliada() && !vacio.equals(exc.getInfAmpliada())){
				String error = exc.getInfAmpliada();
				Incidencia incidencia = Solvencia2ExcepcionHelper.crearAviso(btcUmic.getBaseTec(), umic.getDatosGenerales().getCcanal(), umic.getDatosGenerales().getCcartera(), umic.getKey(), btcUmic.getFecCierre(), umic.getDatosGenerales().getCnegocio(), this.getNombreServicio(), ConstantsFunciones.CTE_COD_ERROR_I007, new Object[] {error});
				servicio.almacenarIncidencias(incidencia);
			}
			servicio.almacenarIncidencias(aviso);
		} 
		return null;
	}
	
	/*
	private void comprobarDatos(Umic umic, DetalleBaseTecnica btcUmic){
		if(btcUmic.getBaseTec() == null || btcUmic.getBaseTec().equals("")){
			exc.setInfAmpliada(ConstantesErrores.BASETEC_VACIA_AMPLI);
			throw exc;
		}
		if(umic.getDatosGenerales().getFecCierre() == null){
			exc.setInfAmpliada(ConstantesErrores.FCHCIERRE_VACIA_AMPLI);
			throw exc;
		}
		if(umic.getDatosGenerales().getCcanal() == null){
			exc.setInfAmpliada(ConstantesErrores.CANAL_VACIO_AMPLI);
			throw exc;
		}
		if(umic.getDatosGenerales().getCnegocio() == null || umic.getDatosGenerales().getCnegocio().equals("")){
			exc.setInfAmpliada(ConstantesErrores.NEGOCIO_VACIO_AMPLI);
			throw exc;
		}
		if(umic.getDatosGenerales().getCcartera() == null){
			exc.setInfAmpliada(ConstantesErrores.CARTERA_VACIO_AMPLI);
			throw exc;
		}
		if(umic.getBti().getFecIniTramo1() == null || umic.getBti().getFecIniTramo2() == null
				|| umic.getBti().getFecIniTramo3() == null || umic.getBti().getFecIniTramo4() == null || umic.getBti().getFecIniTramo5() == null){
			exc.setInfAmpliada(ConstantesErrores.FINITRAMO_VACIO_AMPLI);
			throw exc;
		}
		if(umic.getBti().getFecFinTramo1()==null || umic.getBti().getFecFinTramo2()==null || umic.getBti().getFecFinTramo3()==null
				|| umic.getBti().getFecFinTramo4()==null || umic.getBti().getFecFinTramo5()==null){
			exc.setInfAmpliada(ConstantesErrores.FFINTRAMO_VACIO_AMPLI);
			throw exc;
		}
		if(umic.getBti().getPintertecnI1()==null || umic.getBti().getPintertecnI2()==null || umic.getBti().getPintertecnI3()==null
				|| umic.getBti().getPintertecnI4()==null || umic.getBti().getPintertecnI5()==null){
			exc.setInfAmpliada(ConstantesErrores.PINTTEC_VACIO_AMPLI);
			throw exc;
		}
		if(umic.getBti().getGapI1()==null || umic.getBti().getGapI1().equals("")
				|| umic.getBti().getGapI2()==null || umic.getBti().getGapI2().equals("")
				|| umic.getBti().getGapI3()==null || umic.getBti().getGapI3().equals("")
				|| umic.getBti().getGapI4()==null || umic.getBti().getGapI4().equals("")
				|| umic.getBti().getGapI5()==null || umic.getBti().getGapI4().equals("")){
			exc.setInfAmpliada(ConstantesErrores.CTE_DES_ERROR_GAP);
			throw exc;
		}
		if(umic.getBti().getSwcasadoI1()==null || umic.getBti().getSwcasadoI1().equals("")
				|| umic.getBti().getSwcasadoI2()==null || umic.getBti().getSwcasadoI2().equals("")
				|| umic.getBti().getSwcasadoI3()==null || umic.getBti().getSwcasadoI3().equals("")
				|| umic.getBti().getSwcasadoI4()==null || umic.getBti().getSwcasadoI4().equals("")
				|| umic.getBti().getSwcasadoI5()==null || umic.getBti().getSwcasadoI5().equals("")){
			exc.setInfAmpliada(ConstantesErrores.CTE_DES_ERROR_SWCAS);
			throw exc;
		}
		if(umic.getDatosGenerales().getKmodalidad() == null){
			exc.setInfAmpliada(ConstantesErrores.CTE_DES_ERROR_MOD);
			throw exc;
		}
		if(umic.getBti().getTabla1Aseg1() == null || umic.getBti().getTabla1Aseg1().equals("")
				|| umic.getBti().getTabla2Aseg1() == null || umic.getBti().getTabla2Aseg1().equals("")
				|| umic.getBti().getTabla3Aseg1() == null || umic.getBti().getTabla3Aseg1().equals("")
				|| umic.getBti().getTabla1Aseg2() == null || umic.getBti().getTabla1Aseg2().equals("")
				|| umic.getBti().getTabla2Aseg2() == null || umic.getBti().getTabla2Aseg2().equals("")
				|| umic.getBti().getTabla3Aseg2() == null || umic.getBti().getTabla3Aseg2().equals("")
				|| umic.getBti().getTabla1Aseg3() == null || umic.getBti().getTabla1Aseg3().equals("")
				|| umic.getBti().getTabla2Aseg3() == null || umic.getBti().getTabla2Aseg3().equals("")
				|| umic.getBti().getTabla3Aseg3() == null || umic.getBti().getTabla3Aseg3().equals("")
				|| umic.getBti().getTabla1Aseg4() == null || umic.getBti().getTabla1Aseg4().equals("")
				|| umic.getBti().getTabla2Aseg4() == null || umic.getBti().getTabla2Aseg4().equals("")
				|| umic.getBti().getTabla3Aseg4() == null || umic.getBti().getTabla3Aseg4().equals("")
				|| umic.getBti().getTabla1Aseg5() == null || umic.getBti().getTabla1Aseg5().equals("")
				|| umic.getBti().getTabla2Aseg5() == null || umic.getBti().getTabla2Aseg5().equals("")
				|| umic.getBti().getTabla3Aseg5() == null || umic.getBti().getTabla3Aseg5().equals("")){
			exc.setInfAmpliada(ConstantesErrores.CTE_DES_ERROR_TABASEG);
			throw exc;
		}
		if(umic.getBti().getPgastgesin1I() == null
				|| umic.getBti().getPgastgesin2I() == null
				|| umic.getBti().getPgastgesin3I() == null){
			exc.setInfAmpliada(ConstantesErrores.CTE_DES_ERROR_PGAS);
			throw exc;
		}
		if(umic.getKey().getCtipoaport()==null || umic.getKey().getCtipoaport().equals("")){
			exc.setInfAmpliada(ConstantesErrores.CTE_DES_ERROR_CTIPO);
			throw exc;
		}
		if(umic.getDatosGenerales().getKey() == null){
			exc.setInfAmpliada(ConstantesErrores.CTE_DES_ERROR_UMICKEY);
			throw exc;
		}
		if(umic.getAsegurados().getEdadAseg1() == null
				|| umic.getAsegurados().getEdadAseg2() == null
				|| umic.getAsegurados().getEdadAseg3() == null
				|| umic.getAsegurados().getEdadAseg4() == null
				|| umic.getAsegurados().getEdadAseg5() == null){
			exc.setInfAmpliada(ConstantesErrores.CTE_DES_ERROR_EDAD);
			throw exc;
		}
	}
	*/

	public void establecerTablaAseg(DetalleBaseTecnica btcUmic,
			BaseTecnicaInicial bti, Integer modalidad,
			Asegurados asegurados) {
		btcUmic.setTablacalc1aseg1(bti.getTabla1Aseg1());
		btcUmic.setTablacalc2aseg1(bti.getTabla2Aseg1());
		btcUmic.setTablacalc3aseg1(bti.getTabla3Aseg1());
		btcUmic.setTablacalc1aseg2(bti.getTabla1Aseg2());
		btcUmic.setTablacalc2aseg2(bti.getTabla2Aseg2());
		btcUmic.setTablacalc3aseg2(bti.getTabla3Aseg2());
		btcUmic.setTablacalc1aseg3(bti.getTabla1Aseg3());
		btcUmic.setTablacalc2aseg3(bti.getTabla2Aseg3());
		btcUmic.setTablacalc3aseg3(bti.getTabla3Aseg3());
		btcUmic.setTablacalc1aseg4(bti.getTabla1Aseg4());
		btcUmic.setTablacalc2aseg4(bti.getTabla2Aseg4());
		btcUmic.setTablacalc3aseg4(bti.getTabla3Aseg4());
		btcUmic.setTablacalc1aseg5(bti.getTabla1Aseg5());
		btcUmic.setTablacalc2aseg5(bti.getTabla2Aseg5());
		btcUmic.setTablacalc3aseg5(bti.getTabla3Aseg5());
		
		List<List<TablaConversion>> tablaConvTotal = new ArrayList<List<TablaConversion>>();
		
		List<TablaConversion> tablasConv = new ArrayList<TablaConversion>();
		tablasConv.add(crearTablaConversion(bti.getTabla1Aseg1(), bti.getTabla1Aseg1()));
		tablasConv.add(crearTablaConversion(bti.getTabla2Aseg1(), bti.getTabla2Aseg1()));
		tablasConv.add(crearTablaConversion(bti.getTabla3Aseg1(), bti.getTabla3Aseg1()));
		tablaConvTotal.add(tablasConv);
		
		tablasConv = new ArrayList<TablaConversion>();
		tablasConv.add(crearTablaConversion(bti.getTabla1Aseg2(), bti.getTabla1Aseg2()));
		tablasConv.add(crearTablaConversion(bti.getTabla2Aseg2(), bti.getTabla2Aseg2()));
		tablasConv.add(crearTablaConversion(bti.getTabla3Aseg2(), bti.getTabla3Aseg2()));
		tablaConvTotal.add(tablasConv);
		
		tablasConv = new ArrayList<TablaConversion>();
		tablasConv.add(crearTablaConversion(bti.getTabla1Aseg3(), bti.getTabla1Aseg3()));
		tablasConv.add(crearTablaConversion(bti.getTabla2Aseg3(), bti.getTabla2Aseg3()));
		tablasConv.add(crearTablaConversion(bti.getTabla3Aseg3(), bti.getTabla3Aseg3()));
		tablaConvTotal.add(tablasConv);
		
		tablasConv = new ArrayList<TablaConversion>();
		tablasConv.add(crearTablaConversion(bti.getTabla1Aseg4(), bti.getTabla1Aseg4()));
		tablasConv.add(crearTablaConversion(bti.getTabla2Aseg4(), bti.getTabla2Aseg4()));
		tablasConv.add(crearTablaConversion(bti.getTabla3Aseg4(), bti.getTabla3Aseg4()));
		tablaConvTotal.add(tablasConv);
		
		tablasConv = new ArrayList<TablaConversion>();
		tablasConv.add(crearTablaConversion(bti.getTabla1Aseg5(), bti.getTabla1Aseg5()));
		tablasConv.add(crearTablaConversion(bti.getTabla2Aseg5(), bti.getTabla2Aseg5()));
		tablasConv.add(crearTablaConversion(bti.getTabla3Aseg5(), bti.getTabla3Aseg5()));
		tablaConvTotal.add(tablasConv);
		
		btcUmic.setTablasConversionAsegurado(tablaConvTotal);
		
	}
	
	private TablaConversion crearTablaConversion(String tablaIni, String tablaFin){
		TablaConversion tablaConversion = new TablaConversion();
		tablaConversion.setTablaInicio(tablaIni);
		tablaConversion.setTablaFin(tablaFin);
		
		return tablaConversion;
	}

	public void establecerFecIniFinTramoX(DetalleBaseTecnica btcUmic,
			BaseTecnicaInicial bti) {
		
		final List<Timestamp> lstFecIniTramo = new ArrayList<Timestamp>();
		final List<Timestamp> lstFecFinTramo = new ArrayList<Timestamp>();
		
		lstFecIniTramo.add(bti.getFecIniTramo1());
		lstFecIniTramo.add(bti.getFecIniTramo2());
		lstFecIniTramo.add(bti.getFecIniTramo3());
		lstFecIniTramo.add(bti.getFecIniTramo4());
		lstFecIniTramo.add(bti.getFecIniTramo5());
		lstFecFinTramo.add(bti.getFecFinTramo1());
		lstFecFinTramo.add(bti.getFecFinTramo2());
		lstFecFinTramo.add(bti.getFecFinTramo3());
		lstFecFinTramo.add(bti.getFecFinTramo4());
		lstFecFinTramo.add(bti.getFecFinTramo5());
		
		btcUmic.setFecInitramo(lstFecIniTramo);
		btcUmic.setFecfintramo(lstFecFinTramo);
		
	}
	
	@Override
	public String getNombreServicio() {
		return ConstantesModulosGBT.BT_INICIAL;
	}

}
