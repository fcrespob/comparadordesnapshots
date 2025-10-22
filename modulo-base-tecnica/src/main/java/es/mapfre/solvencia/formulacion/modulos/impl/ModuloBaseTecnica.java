package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Asegurados;
import es.mapfre.solvencia.dominio.maestro.BaseTecnicaInicial;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.formulacion.util.ConstantsBT;
import es.mapfre.solvencia.formulacion.util.UtilBT;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada de implementar el modulo encargado de establecer la base técnica de cálculo
 * necesaria para realizar las operaciones que obtienen los flujos de las distintas corrientes.
 * Como resultado obtendremos una estructura de datos con el detalle de la base técnica de cálculo, btcUmic.
 * 
 * @author jguijarro
 */
public class ModuloBaseTecnica implements Modulo {

	/** Cte para log. */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloBaseTecnica.class);
	
	
	@Override
	public String getNombreServicio() {
		return ConstantsFactorias.MODULO_BASE_TEC;
	}

	/** Función encargada de obtener los parámetros necesarios y de realizar la llamada a la función que realiza los calculos del modulo. */
	public final Object execute(final Object... args) throws Solvencia2Excepcion {
		
		if (ModuloBaseTecnica.LOG.isTraceEnabled()) {
			ModuloBaseTecnica.LOG.trace("Inicio de execute en clase ModuloBaseTecnica");
		}
		
		try {
			//Calculo de base tecnica
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsBT.PARAM_BTC];
			final Umic umic = (Umic) args[ConstantsBT.PARAM_UMI_BASE_TEC];
			
			// Realizar la conversion de BTI a ROSSP o BEL
			moduloBaseTecnica(btcUmic, umic);
			
		} catch (Solvencia2Excepcion e) {
			ModuloBaseTecnica.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloBaseTecnica.LOG.error(e.getMessage(), e);
			throw e;
		}
		
		if (ModuloBaseTecnica.LOG.isTraceEnabled()) {
			ModuloBaseTecnica.LOG.trace("Fin de execute en clase ModuloBaseTecnica");
		}
		
		return null;
	}

	/**
	 * Si la base técnica de la ficha de proceso es BTI, se vuelcan los datos de base técnica de la umic
	 * en la estructura de base técnica de salida sin realizar conversión.
	 * 
	 * Si la base técnica de la ficha de proceso es ROSSP, se vuelcan los datos de la estructura detalleBaseTecnica relativos a ROSSP
	 * y el resto de los datos o se inicializan o se obtienen de la umic.
	 * 
	 * Si la base técnica de la ficha de proceso es BEL, se vuelcan los datos de la estructura detalleBaseTecnica relativos a BEL
	 * y el resto de los datos o se inicializan o se obtienen de la umic.
	 * 
	 * @param umic
	 * @param detalleBT
	 */
	private void moduloBaseTecnica(final DetalleBaseTecnica detalleBT, final Umic umic) {
						
		// Base técnica inicial de la umic y Datos generales
		final BaseTecnicaInicial bti = umic.getBti();
		final DatosGenerales datosGenerales = umic.getDatosGenerales();
		final Asegurados asegurados = umic.getAsegurados();
		
		// Tipo base tecnica y modalidad
		final String tipoBT = detalleBT.getBaseTec();
		final Integer modalidad = datosGenerales.getKmodalidad();
		
		detalleBT.setFecCierre(datosGenerales.getFecCierre());
		detalleBT.setCcartera(datosGenerales.getCcartera());
		
		detalleBT.setUmicKey(datosGenerales.getKey());
		
		
		// Edad del asegurado
		final List<BigDecimal> edadAseg = new ArrayList<BigDecimal>();
		edadAseg.add(new BigDecimal(asegurados.getEdadAseg1()));
		edadAseg.add(new BigDecimal(asegurados.getEdadAseg2()));
		edadAseg.add(new BigDecimal(asegurados.getEdadAseg3()));
		edadAseg.add(new BigDecimal(asegurados.getEdadAseg4()));
		edadAseg.add(new BigDecimal(asegurados.getEdadAseg5()));
		detalleBT.setEdadcalc(edadAseg);
		
		// Tabla asegurado
		establecerTablaAseg(detalleBT, bti, tipoBT, modalidad, asegurados);
		
		// Factor 1
		detalleBT.setFactor1(BigDecimal.ZERO);
		
		// Factor 2
		final List<BigDecimal> factor2 = Arrays.asList(new BigDecimal[] {
				BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
				BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO});
		detalleBT.setFactor2(factor2);
		
		// Fec It ROSSP
		detalleBT.setFitrossp(null);
		
		// Para ROSSP. Para intereses si el tramo es casado se respeta el valor de BTI. Si el tramo es no casado se convierte en función del tipo de interés.
		// Para BEL. detalleBaseTecnica.setItcalcX(0)
		final List<BigDecimal> itcalc = new ArrayList<BigDecimal>();
		itcalc.add(establecerItcalc(tipoBT, modalidad, bti.getSwcasadoI1(), bti.getPintertecnI1()));
		itcalc.add(establecerItcalc(tipoBT, modalidad, bti.getSwcasadoI2(), bti.getPintertecnI2()));
		itcalc.add(establecerItcalc(tipoBT, modalidad, bti.getSwcasadoI3(), bti.getPintertecnI3()));
		itcalc.add(establecerItcalc(tipoBT, modalidad, bti.getSwcasadoI4(), bti.getPintertecnI4()));
		itcalc.add(establecerItcalc(tipoBT, modalidad, bti.getSwcasadoI5(), bti.getPintertecnI5()));
		detalleBT.setItcalc(itcalc);
		
		// Establecer el valor de los datos de Curva Ti, Curva An y Gto
		establecerDatosCurvaYGto(detalleBT, bti, tipoBT, modalidad);
		
		// Establecer fecIniFinTramoX
		establecerFecIniFinTramoX(detalleBT, bti, tipoBT);
		
		// Establecer swcasado
		establecerSwcasado(detalleBT, bti, tipoBT);
		
		// Establecer swcasado
		establecerTipoTablasExperiencia(detalleBT, tipoBT);
		
		// Almacenar detalleBT
		//almacenarDatos.almacenarDetalleBaseTecnica(detalleBT);
	}
	
	private void establecerTipoTablasExperiencia(DetalleBaseTecnica detalleBT, String tipoBT) {
		if (ConstantsBT.CTE_VAL_BASE_BEL.equals(tipoBT) || 
				ConstantsBT.CTE_VAL_BELCOA.equals(tipoBT) || 
				ConstantsBT.CTE_VAL_BELCLR.equals(tipoBT) || 
				ConstantsBT.CTE_VAL_SCRTIU.equals(tipoBT) || 
				ConstantsBT.CTE_VAL_SCRTID.equals(tipoBT) || 
				ConstantsBT.CTE_VAL_SCRGTO.equals(tipoBT) || 
				ConstantsBT.CTE_VAL_SCRMFE.equals(tipoBT) || 
				ConstantsBT.CTE_VAL_SCRMMI.equals(tipoBT) || 
				ConstantsBT.CTE_VAL_SCRMCF.equals(tipoBT) || 
				ConstantsBT.CTE_VAL_SCRMCI.equals(tipoBT) || 
				ConstantsBT.CTE_VAL_SCRLFE.equals(tipoBT) || 
				ConstantsBT.CTE_VAL_SCRLMI.equals(tipoBT) || 
				ConstantsBT.CTE_VAL_SCRINC.equals(tipoBT) || 
				ConstantsBT.CTE_VAL_SCRVM.equals(tipoBT)  || 
				ConstantsBT.CTE_VAL_SCRAEP.equals(tipoBT) || 
				ConstantsBT.CTE_VAL_SCRAEN.equals(tipoBT) || 
				ConstantsBT.CTE_VAL_SCRAIP.equals(tipoBT) || 
				ConstantsBT.CTE_VAL_SCRAIN.equals(tipoBT) || 
				ConstantsBT.CTE_VAL_SCRANM.equals(tipoBT)) {
			detalleBT.setIndTabExp(ConstantsBT.CTE_TABLA_REALISTA);			
		} else {
			detalleBT.setIndTabExp(ConstantsBT.CTE_TABLA_TRADICIONAL);						
		}
	}


	/**
	 * Establece el valor de TablaXAsegY
	 * @param detalleBT
	 * @param bti
	 * @param tipoBT
	 * @param modalidad
	 * @param asegurados
	 */
	private void establecerTablaAseg(final DetalleBaseTecnica detalleBT, final BaseTecnicaInicial bti, final String tipoBT,
			final Integer modalidad, final Asegurados asegurados) {
		
		detalleBT.setTablacalc1aseg1(establerTablaAsegMod(tipoBT, modalidad, bti.getTabla1Aseg1(), asegurados.getCsexAseg1()));
		detalleBT.setTablacalc2aseg1(establerTablaAsegMod(tipoBT, modalidad, bti.getTabla2Aseg1(), asegurados.getCsexAseg1()));
		detalleBT.setTablacalc3aseg1(establerTablaAsegMod(tipoBT, modalidad, bti.getTabla3Aseg1(), asegurados.getCsexAseg1()));
		detalleBT.setTablacalc1aseg2(establerTablaAsegMod(tipoBT, modalidad, bti.getTabla1Aseg2(), asegurados.getCsexAseg2()));
		detalleBT.setTablacalc2aseg2(establerTablaAsegMod(tipoBT, modalidad, bti.getTabla2Aseg2(), asegurados.getCsexAseg2()));
		detalleBT.setTablacalc3aseg2(establerTablaAsegMod(tipoBT, modalidad, bti.getTabla3Aseg2(), asegurados.getCsexAseg2()));
		detalleBT.setTablacalc1aseg3(establerTablaAsegMod(tipoBT, modalidad, bti.getTabla1Aseg3(), asegurados.getCsexAseg3()));
		detalleBT.setTablacalc2aseg3(establerTablaAsegMod(tipoBT, modalidad, bti.getTabla2Aseg3(), asegurados.getCsexAseg3()));
		detalleBT.setTablacalc3aseg3(establerTablaAsegMod(tipoBT, modalidad, bti.getTabla3Aseg3(), asegurados.getCsexAseg3()));
		detalleBT.setTablacalc1aseg4(establerTablaAsegMod(tipoBT, modalidad, bti.getTabla1Aseg4(), asegurados.getCsexAseg4()));
		detalleBT.setTablacalc2aseg4(establerTablaAsegMod(tipoBT, modalidad, bti.getTabla2Aseg4(), asegurados.getCsexAseg4()));
		detalleBT.setTablacalc3aseg4(establerTablaAsegMod(tipoBT, modalidad, bti.getTabla3Aseg4(), asegurados.getCsexAseg4()));
		detalleBT.setTablacalc1aseg5(establerTablaAsegMod(tipoBT, modalidad, bti.getTabla1Aseg5(), asegurados.getCsexAseg5()));
		detalleBT.setTablacalc2aseg5(establerTablaAsegMod(tipoBT, modalidad, bti.getTabla2Aseg5(), asegurados.getCsexAseg5()));
		detalleBT.setTablacalc3aseg5(establerTablaAsegMod(tipoBT, modalidad, bti.getTabla3Aseg5(), asegurados.getCsexAseg5()));
	}
	
	/**
	 * Para BTI en todas las modalidades:
	 * - detalleBaseTecnica.tablaAsegYX = umic.baseTecIni.tablaYAsegX
	 * 
	 * Para ROSSP en modalidad 362:
	 * - Si umic.Asegurados.csexAsegX = "H"
	 * 		- Si umic.BaseTecIni.tablaYAsegX <> ceros o espacios
	 * 			- detalleBaseTecnica.tablaAsegYX = "00740"
	 * - Si umic.Asegurados.csexAsegX = "M"
	 * 		- Si umic.BaseTecIni.tabla1AsegX <> ceros o espacios
	 * 			- detalleBaseTecnica.tablaAsegYX = "00741"
	 * 
	 * Para ROSSP en modalidad 209 o 852:
	 * - Si umic.BaseTecIni.tablaXAseg1 <> ceros o espacios
	 * 		- detalleBaseTecnica.tablaAsegYX = "00040"
	 * 
	 * Para BEL en todas las modalidades:
	 * - detalleBaseTecnica.tablaAsegYX = "00039"
	 * 
	 * @param tipoBT  
	 * @param modalidad
	 * @param tablaYAsegX
	 * @param csexAseg
	 * @return
	 */
	private String establerTablaAsegMod(final String tipoBT, final Integer modalidad, final String tablaYAsegX, final String csexAseg) {
		String salida = null;
		
		// Para ROSSP en modalidad 209 o 852 y umic.BaseTecIni.tabla1AsegX <> ceros o espacios
		final boolean modRossp209o825 = ((ConstantsBT.CTE_VAL_ROSSP.equals(tipoBT) || ConstantsBT.CTE_VAL_BTCOA.equals(tipoBT) || ConstantsBT.CTE_VAL_ROSSEAR.equals(tipoBT) || ConstantsBT.CTE_VAL_ROSSEARC.equals(tipoBT)) &&
				(ArrayUtils.contains(ConstantsBT.FAMILIA_209, modalidad) || ArrayUtils.contains(ConstantsBT.FAMILIA_852, modalidad)));
		
		// Para ROSSP en modalidad 362 y umic.BaseTecIni.tabla1AsegX <> ceros o espacios
		final boolean modRossp362 = (ConstantsBT.CTE_VAL_ROSSP.equals(tipoBT) || ConstantsBT.CTE_VAL_BTCOA.equals(tipoBT) || ConstantsBT.CTE_VAL_ROSSEAR.equals(tipoBT) || ConstantsBT.CTE_VAL_ROSSEARC.equals(tipoBT)) && ArrayUtils.contains(ConstantsBT.FAMILIA_362, modalidad);
				
			
		if (ConstantsBT.CTE_VAL_BTI.equals(tipoBT)) {
			
			// BTI en todas las modalidades
			salida = tablaYAsegX;
			
		}else if(ConstantsBT.CTE_VAL_BTIPROY.equals(tipoBT)){
			
			salida = tablaYAsegX;
		} else if (modRossp209o825) {
			
			// En principio se asignan las de la base técnica inicial de la umic, pero se convierten las que tengan contenido
			salida = tablaYAsegX;
			
			// ROSSP en modalidad 209 o 852
			salida = ConstantsBT.TABLA_ASEG_00721;
			
		} else if (modRossp362) {
			
			// En principio se asignan las de la base técnica inicial de la umic, pero se convierten las que tengan contenido
			salida = tablaYAsegX;
			
			// Para ROSSP en modalidad 362
			if (ConstantsBT.CTE_SX_H.equals(csexAseg) && UtilBT.campoDistintoCerosOEspacios(tablaYAsegX)) {
				salida = ConstantsBT.TABLA_ASEG_00740;
			} else if (ConstantsBT.CTE_SX_M.equals(csexAseg) && UtilBT.campoDistintoCerosOEspacios(tablaYAsegX)) {
				salida = ConstantsBT.TABLA_ASEG_00741;
			}
			
		} else if (ConstantsBT.CTE_VAL_BASE_BEL.equals(tipoBT) ||
				ConstantsBT.CTE_VAL_BELCOA.equals(tipoBT) ||
				ConstantsBT.CTE_VAL_BELCLR.equals(tipoBT) ||
				ConstantsBT.CTE_VAL_SCRTID.equals(tipoBT) ||
				ConstantsBT.CTE_VAL_SCRTIU.equals(tipoBT) || 
				ConstantsBT.CTE_VAL_SCRGTO.equals(tipoBT) || 
				ConstantsBT.CTE_VAL_SCRMFE.equals(tipoBT) || 
				ConstantsBT.CTE_VAL_SCRMMI.equals(tipoBT) || 
				ConstantsBT.CTE_VAL_SCRMCF.equals(tipoBT) || 
				ConstantsBT.CTE_VAL_SCRMCI.equals(tipoBT) || 
				ConstantsBT.CTE_VAL_SCRLFE.equals(tipoBT) || 
				ConstantsBT.CTE_VAL_SCRLMI.equals(tipoBT) || 
				ConstantsBT.CTE_VAL_SCRINC.equals(tipoBT) || 
				ConstantsBT.CTE_VAL_SCRVM.equals(tipoBT)  || 
				ConstantsBT.CTE_VAL_SCRAEP.equals(tipoBT) || 
				ConstantsBT.CTE_VAL_SCRAEN.equals(tipoBT) || 
				ConstantsBT.CTE_VAL_SCRAIP.equals(tipoBT) || 
				ConstantsBT.CTE_VAL_SCRAIN.equals(tipoBT) || 
				ConstantsBT.CTE_VAL_SCRANM.equals(tipoBT)) {
			// BEL en todas las modalidades
			salida = tablaYAsegX;
			
			if ((null != csexAseg && (ConstantsBT.CTE_SX_H.equals(csexAseg) || ConstantsBT.CTE_SX_M.equals(csexAseg))) 
					&& UtilBT.campoDistintoCerosOEspacios(tablaYAsegX)) {
				salida = ConstantsBT.TABLA_ASEG_00039;
			}
					
		}
		
		return salida;
	}
	
	/**
	 * Para intereses si el tramo es casado se respeta el valor de BTI.
	 * Si el tramo es no casado se convierte en función del tipo de interés.
	 * 
	 * Para BTI en todas las modalidades, o ROSSP en modalidad 362:
	 * - detalleBaseTecnica.itcalcX = umic.baseTecIni.pintertecnIX;
	 * 
	 * Para ROSSP en modalidad 362:
	 * - Si umic.BaseTecIni.swcasadoIX = "S"
	 * 		- detalleBaseTecnica.itcalcX = umic.baseTecIni.pintertecnIX
	 * - Si umic.BaseTecIni.swcasadoIX = "N"
	 * 		- Si umic.baseTecIni.pintertecnIX < 2,73%
	 * 			- detalleBaseTecnica.itcalcX = umic.baseTecIni.pintertecnIX
	 * 		- Si umic.baseTecIni.pintertecnIX >= 2,73%
	 * 			- detalleBaseTecnica.itcalcX = 2,73%
	 * 
	 * Para BEL en todas las modalidades:
	 * - detalleBaseTecnica.itcalcX = 0;
	 * 
	 * @param tipoBT
	 * @param modalidad
	 * @param swcasadoIX
	 * @param pintertecnIX
	 * @return
	 */
	private BigDecimal establecerItcalc(final String tipoBT, final Integer modalidad, final String swcasadoIX, final BigDecimal pintertecnIX) {
		
		BigDecimal itcalcX = null;
		
		if (ConstantsBT.CTE_VAL_ROSSP.equals(tipoBT) && ArrayUtils.contains(ConstantsBT.FAMILIA_362, modalidad) &&
				ConstantsBT.CTE_N.equals(swcasadoIX) && pintertecnIX.compareTo(ConstantsBT.CTE_OPER_2_PUNTO_37) >= 0) {
			itcalcX = ConstantsBT.CTE_OPER_2_PUNTO_37;
		} else if (ConstantsBT.CTE_VAL_BASE_BEL.equals(tipoBT) ||
						ConstantsBT.CTE_VAL_BELCOA.equals(tipoBT) ||
						ConstantsBT.CTE_VAL_BELCLR.equals(tipoBT) ||
						ConstantsBT.CTE_VAL_SCRTIU.equals(tipoBT) ||
						ConstantsBT.CTE_VAL_SCRTID.equals(tipoBT) || 
						ConstantsBT.CTE_VAL_SCRGTO.equals(tipoBT) || 
						ConstantsBT.CTE_VAL_SCRMFE.equals(tipoBT) || 
						ConstantsBT.CTE_VAL_SCRMMI.equals(tipoBT) || 
						ConstantsBT.CTE_VAL_SCRMCF.equals(tipoBT) || 
						ConstantsBT.CTE_VAL_SCRMCI.equals(tipoBT) || 
						ConstantsBT.CTE_VAL_SCRLFE.equals(tipoBT) || 
						ConstantsBT.CTE_VAL_SCRLMI.equals(tipoBT) || 
						ConstantsBT.CTE_VAL_SCRINC.equals(tipoBT) || 
						ConstantsBT.CTE_VAL_SCRVM.equals(tipoBT)  || 
						ConstantsBT.CTE_VAL_SCRAEP.equals(tipoBT) || 
						ConstantsBT.CTE_VAL_SCRAEN.equals(tipoBT) || 
						ConstantsBT.CTE_VAL_SCRAIP.equals(tipoBT) || 
						ConstantsBT.CTE_VAL_SCRAIN.equals(tipoBT) || 
						ConstantsBT.CTE_VAL_SCRANM.equals(tipoBT)) {
			itcalcX = BigDecimal.ZERO;
		} else {
			itcalcX = pintertecnIX;
		}
		
		return itcalcX;
	}
	
	/**
	 * Establecer el valor de los datos de Curva Ti, Curva An y Gto.
	 * 
	 * @param detalleBT
	 * @param bti
	 * @param tipoBT
	 * @param modalidad
	 */
	private void establecerDatosCurvaYGto(final DetalleBaseTecnica detalleBT, final BaseTecnicaInicial bti, final String tipoBT, final Integer modalidad) {
		
		if (ConstantsBT.CTE_VAL_BTI.equals(tipoBT) || ConstantsBT.CTE_VAL_BTIPROY.equals(tipoBT) || ConstantsBT.CTE_VAL_ROSSP.equals(tipoBT)) {
			
			detalleBT.setFcurvaTi(null);
			detalleBT.setCurvaTi(null);
			detalleBT.setPerTransRossp(null);
			detalleBT.setMetodoPtRossp(null);
			detalleBT.setFtablaAn(null);
			detalleBT.setTablaTanul(null);
			detalleBT.setGtorosspPrima(bti.getPgastgesin2I());
			detalleBT.setGtorosspCap(bti.getPgastgesin1I());
			detalleBT.setGtorosspProv(BigDecimal.ZERO);
			detalleBT.setGtoUni(BigDecimal.ZERO);
			detalleBT.setGtoprov(BigDecimal.ZERO);
			
		} else if (ConstantsBT.CTE_VAL_BASE_BEL.equals(tipoBT)  ||
					ConstantsBT.CTE_VAL_BELCOA.equals(tipoBT) ||
					ConstantsBT.CTE_VAL_BELCLR.equals(tipoBT) ||
					ConstantsBT.CTE_VAL_SCRTIU.equals(tipoBT) ||
					ConstantsBT.CTE_VAL_SCRTID.equals(tipoBT) || 
					ConstantsBT.CTE_VAL_SCRGTO.equals(tipoBT) || 
					ConstantsBT.CTE_VAL_SCRMFE.equals(tipoBT) || 
					ConstantsBT.CTE_VAL_SCRMMI.equals(tipoBT) || 
					ConstantsBT.CTE_VAL_SCRMCF.equals(tipoBT) || 
					ConstantsBT.CTE_VAL_SCRMCI.equals(tipoBT) || 
					ConstantsBT.CTE_VAL_SCRLFE.equals(tipoBT) || 
					ConstantsBT.CTE_VAL_SCRLMI.equals(tipoBT) || 
					ConstantsBT.CTE_VAL_SCRINC.equals(tipoBT) || 
					ConstantsBT.CTE_VAL_SCRVM.equals(tipoBT)  || 
					ConstantsBT.CTE_VAL_SCRAEP.equals(tipoBT) || 
					ConstantsBT.CTE_VAL_SCRAEN.equals(tipoBT) || 
					ConstantsBT.CTE_VAL_SCRAIP.equals(tipoBT) || 
					ConstantsBT.CTE_VAL_SCRAIN.equals(tipoBT) || 
					ConstantsBT.CTE_VAL_SCRANM.equals(tipoBT)) {
			
			// 31/12/2013
			final GregorianCalendar curvaTi = new GregorianCalendar(2013, 11, 31, 0, 0, 0);
			detalleBT.setFcurvaTi(new Timestamp(curvaTi.getTimeInMillis()));
			detalleBT.setCurvaTi("CLRC0");
			detalleBT.setPerTransRossp(false);
			detalleBT.setMetodoPtRossp(null);
			// 31/12/2013
			final GregorianCalendar curvaAn = new GregorianCalendar(2013, 11, 31, 0, 0, 0);
			detalleBT.setFtablaAn(new Timestamp(curvaAn.getTimeInMillis()));
			detalleBT.setGtorosspPrima(BigDecimal.ZERO);
			detalleBT.setGtorosspCap(BigDecimal.ZERO);
			detalleBT.setGtorosspProv(BigDecimal.ZERO);
			
			if (ArrayUtils.contains(ConstantsBT.FAMILIA_362, modalidad)) {
				detalleBT.setTablaTanul("TA00001");
				detalleBT.setGtoUni(new BigDecimal("97.45"));
				detalleBT.setGtoprov(BigDecimal.ZERO);
			} else {
				detalleBT.setTablaTanul("TA00002");
				detalleBT.setGtoUni(new BigDecimal("54.84"));
				detalleBT.setGtoprov(new BigDecimal("0.15"));
			}
		}
	}
	
	/**
	 * Establecer fecIniTramoX y fecFinTramoX. Para los tipos distintos de BEL:
	 * 		- detalleBaseTecnica.fecIniTramoX = umic.baseTecIni.fecIniTramoX
	 * 		- detalleBaseTecnica.fecFinTramoX = umic.baseTecIni.fecFinTramoX
	 * 
	 * @param detalleBT
	 * @param bti
	 * @param tipoBT
	 */
	private void establecerFecIniFinTramoX(final DetalleBaseTecnica detalleBT, final BaseTecnicaInicial bti, final String tipoBT) {
	
		if (!ConstantsBT.CTE_VAL_BASE_BEL.equals(tipoBT) &&
				!ConstantsBT.CTE_VAL_BELCOA.equals(tipoBT) &&
				!ConstantsBT.CTE_VAL_BELCLR.equals(tipoBT) &&
				!ConstantsBT.CTE_VAL_SCRTIU.equals(tipoBT) &&
				!ConstantsBT.CTE_VAL_SCRTID.equals(tipoBT) && 
				!ConstantsBT.CTE_VAL_SCRGTO.equals(tipoBT) && 
				!ConstantsBT.CTE_VAL_SCRMFE.equals(tipoBT) && 
				!ConstantsBT.CTE_VAL_SCRMMI.equals(tipoBT) && 
				!ConstantsBT.CTE_VAL_SCRMCF.equals(tipoBT) && 
				!ConstantsBT.CTE_VAL_SCRMCI.equals(tipoBT) && 
				!ConstantsBT.CTE_VAL_SCRLFE.equals(tipoBT) && 
				!ConstantsBT.CTE_VAL_SCRLMI.equals(tipoBT) && 
				!ConstantsBT.CTE_VAL_SCRINC.equals(tipoBT) && 
				!ConstantsBT.CTE_VAL_SCRVM.equals(tipoBT)  && 
				!ConstantsBT.CTE_VAL_SCRAEP.equals(tipoBT) && 
				!ConstantsBT.CTE_VAL_SCRAEN.equals(tipoBT) && 
				!ConstantsBT.CTE_VAL_SCRAIP.equals(tipoBT) && 
				!ConstantsBT.CTE_VAL_SCRAIN.equals(tipoBT) && 
				!ConstantsBT.CTE_VAL_SCRANM.equals(tipoBT)) {
			
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
			
			detalleBT.setFecInitramo(lstFecIniTramo);
			detalleBT.setFecfintramo(lstFecFinTramo);
		}
	}
	

	/**
	 * Establecer swcasadoX. Para los tipos distintos de BEL:
	 * 		- detalleBaseTecnica.swcasadoX = umic.baseTecIni.swcasadoX
	 * 
	 * @param detalleBT
	 * @param bti
	 * @param tipoBT
	 */
	private void establecerSwcasado(final DetalleBaseTecnica detalleBT, final BaseTecnicaInicial bti, final String tipoBT) {
	
		if (!ConstantsBT.CTE_VAL_BASE_BEL.equals(tipoBT) &&
				!ConstantsBT.CTE_VAL_BELCOA.equals(tipoBT) &&
				!ConstantsBT.CTE_VAL_BELCLR.equals(tipoBT) &&
				!ConstantsBT.CTE_VAL_SCRTIU.equals(tipoBT) &&
				!ConstantsBT.CTE_VAL_SCRTID.equals(tipoBT) && 
				!ConstantsBT.CTE_VAL_SCRGTO.equals(tipoBT) && 
				!ConstantsBT.CTE_VAL_SCRMFE.equals(tipoBT) && 
				!ConstantsBT.CTE_VAL_SCRMMI.equals(tipoBT) && 
				!ConstantsBT.CTE_VAL_SCRMCF.equals(tipoBT) && 
				!ConstantsBT.CTE_VAL_SCRMCI.equals(tipoBT) && 
				!ConstantsBT.CTE_VAL_SCRLFE.equals(tipoBT) && 
				!ConstantsBT.CTE_VAL_SCRLMI.equals(tipoBT) && 
				!ConstantsBT.CTE_VAL_SCRINC.equals(tipoBT) && 
				!ConstantsBT.CTE_VAL_SCRVM.equals(tipoBT)  && 
				!ConstantsBT.CTE_VAL_SCRAEP.equals(tipoBT) && 
				!ConstantsBT.CTE_VAL_SCRAEN.equals(tipoBT) && 
				!ConstantsBT.CTE_VAL_SCRAIP.equals(tipoBT) && 
				!ConstantsBT.CTE_VAL_SCRAIN.equals(tipoBT) && 
				!ConstantsBT.CTE_VAL_SCRANM.equals(tipoBT)) {
			
			final List<String> lstSwcasado = new ArrayList<String>();
			
			lstSwcasado.add(bti.getSwcasadoI1());
			lstSwcasado.add(bti.getSwcasadoI2());
			lstSwcasado.add(bti.getSwcasadoI3());
			lstSwcasado.add(bti.getSwcasadoI4());
			lstSwcasado.add(bti.getSwcasadoI5());
			
			detalleBT.setSwcasado(lstSwcasado);
		}
	}
}
