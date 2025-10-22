package es.mapfre.scr.tablasExperiencia.modulos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.scr.tablasExperiencia.dao.entidades.CabeceraTablaExperienciaDao;
import es.mapfre.scr.tablasExperiencia.dao.entidades.ConvTablasExpRealDao;
import es.mapfre.scr.tablasExperiencia.dao.entidades.LongevidadModInternoDao;
import es.mapfre.scr.tablasExperiencia.dao.entidades.TablaExperienciaDao;
import es.mapfre.scr.tablasExperiencia.dao.entidades.ValoresEstresDao;
import es.mapfre.scr.tablasExperiencia.dominio.entidades.CabeceraTablaExperiencia;
import es.mapfre.scr.tablasExperiencia.dominio.entidades.ConvTablasExpReal;
import es.mapfre.scr.tablasExperiencia.dominio.entidades.Incidencia;
import es.mapfre.scr.tablasExperiencia.dominio.entidades.Incidencias;
import es.mapfre.scr.tablasExperiencia.dominio.entidades.LongevidadModInterno;
import es.mapfre.scr.tablasExperiencia.dominio.entidades.Salida;
import es.mapfre.scr.tablasExperiencia.dominio.entidades.TablaExperiencia;
import es.mapfre.scr.tablasExperiencia.dominio.entidades.ValoresEstres;
import es.mapfre.scr.tablasExperiencia.excepcion.Solvencia2Excepcion;
import es.mapfre.scr.tablasExperiencia.gestores.GestorFicheroSalida;
import es.mapfre.scr.tablasExperiencia.gestores.GestorIncidencias;
import es.mapfre.scr.tablasExperiencia.utils.ConstantesSolvencia;
import es.mapfre.scr.tablasExperiencia.utils.LoggerManager;

public class ModuloTExpSCR {

	private static final ValoresEstresDao valoresEstresDao = new ValoresEstresDao();
	private static final LongevidadModInternoDao longevidadModInternoDao = new LongevidadModInternoDao();
	
	private static final Logger LOG = LoggerFactory.getLogger(ModuloTExpSCR.class);
	private static final String COD_NO_GENERACIONAL = "1";
	private static final String COD_GENERACIONAL = "2";
	private static final String TIPO_NO_GENERACIONAL = "NG";
	private static final String TIPO_GENERACIONAL = "GE";
	private int contRegistros = 0;
	private String strCurrentDate = "";
	private BigDecimal valEstres = BigDecimal.ZERO;
	
	public String getNombreServicio() {
		return "ModuloTExpSCR";
	}

	/** Ejecución del proceso de creación de tablas de experiencia reales */
	public final void execute(String feccierre, String bt) {
		
		// Se obtiene la fecha del día
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date currentDate = new Date();
		this.strCurrentDate = sdf.format(currentDate);
		
		List<ValoresEstres> valoresEstres = null;
		LoggerManager log = LoggerManager.getInstance(bt);
		GestorIncidencias gi = GestorIncidencias.getInstance(bt);

		try {
			if (ModuloTExpSCR.LOG.isTraceEnabled()) {
				ModuloTExpSCR.LOG.trace("Inicio de execute en clase ModuloTExpSCR");
			}

			List<ConvTablasExpReal> listaConversiones = leerConversionTablasExperienciaReal(feccierre, ConstantesSolvencia.BT_BEL);

			log.writeLog(ConstantesSolvencia.LOG_REG_TRA + listaConversiones.size());
			
			Iterator<ConvTablasExpReal> it = listaConversiones.iterator();
			
			if (!bt.equals(ConstantesSolvencia.BT_SCRLMI)){
				valoresEstres = valoresEstresDao.obtenerValoresEstres(feccierre, bt);
				if (valoresEstres.isEmpty()){
					Incidencia inci = new Incidencia();
					inci.setBt(bt);
					inci.setFecCierre(feccierre);
					inci.setCodigoRetorno("01");
					inci.setInfAmpliada("No se han encontrado valores de estrés.");
					gi.write(inci);
				} else if (valoresEstres.size()>1){
					Incidencia inci = new Incidencia();
					inci.setBt(bt);
					inci.setFecCierre(feccierre);
					inci.setCodigoRetorno("02");
					inci.setInfAmpliada("Se ha encontrado más de un valor de estrés.");
					gi.write(inci);
				} else {
					valEstres = valoresEstres.get(0).getValor();
				}
			}
			
			while(it.hasNext()){
				ConvTablasExpReal i = it.next();
				if(i == null || i.equals("")){
					it.remove();
				}else{
					if(i.getKedaddesde() > i.getEdadhasta()){
						Incidencias inci = new Incidencias();
						inci.setKbasetec(i.getKbasetec());
						inci.setKcompania(i.getKcompania());
						inci.setKnegocio(i.getKnegocio());
						inci.setKriesgo(i.getKriesgo());
						inci.setKsexo(i.getKsexo());
						inci.setKcateg(i.getKcateg());
						inci.setKedaddesde(i.getKedaddesde());
						inci.setKmodalidad(i.getKmodalidad());
						inci.setKfdesde(i.getKfdesde());
						inci.setEdadhasta(i.getEdadhasta());
						inci.setFhasta(i.getFhasta());
						inci.setCtabbase(i.getCtabbase());
						inci.setCodigoerror("04");
						inci.setDescerror("Edad Desde no puede ser superior a Edad Hasta.");
						gi.write(inci);
					}else{
						if(i.getCtabbase() == null || i.getCtabbase().equals("") || i.getCtabbase().equals(0) ){
							Incidencias inci = new Incidencias();
							inci.setKbasetec(i.getKbasetec());
							inci.setKcompania(i.getKcompania());
							inci.setKnegocio(i.getKnegocio());
							inci.setKriesgo(i.getKriesgo());
							inci.setKsexo(i.getKsexo());
							inci.setKcateg(i.getKcateg());
							inci.setKedaddesde(i.getKedaddesde());
							inci.setKmodalidad(i.getKmodalidad());
							inci.setKfdesde(i.getKfdesde());
							inci.setEdadhasta(i.getEdadhasta());
							inci.setFhasta(i.getFhasta());
							inci.setCtabbase(i.getCtabbase());
							inci.setCodigoerror("03");
							inci.setDescerror("La tabla base no esta informada.");
							gi.write(inci);	
						}else{
							if(i.getKfdesde() == null){
								Incidencias inci = new Incidencias();
								inci.setKbasetec(i.getKbasetec());
								inci.setKcompania(i.getKcompania());
								inci.setKnegocio(i.getKnegocio());
								inci.setKriesgo(i.getKriesgo());
								inci.setKsexo(i.getKsexo());
								inci.setKcateg(i.getKcateg());
								inci.setKedaddesde(i.getKedaddesde());
								inci.setKmodalidad(i.getKmodalidad());
								inci.setKfdesde(i.getKfdesde());
								inci.setEdadhasta(i.getEdadhasta());
								inci.setFhasta(i.getFhasta());
								inci.setCtabbase(i.getCtabbase());
								inci.setCodigoerror("20");
								inci.setDescerror("El campo fecha desde tiene un formato incorrecto.");
								gi.write(inci);	
							}else{
								try{
									if (i.getKriesgo().equals(ConstantesSolvencia.CTE_AHOR) ||
											(i.getKriesgo().equals(ConstantesSolvencia.CTE_LONG) && 
													(bt.equals(ConstantesSolvencia.BT_SCRLFE) || bt.equals(ConstantesSolvencia.BT_SCRMFE) || bt.equals(ConstantesSolvencia.BT_SCRLMI))) ||
											(i.getKriesgo().equals(ConstantesSolvencia.CTE_FALL) && 
													(bt.equals(ConstantesSolvencia.BT_SCRMCF) || bt.equals(ConstantesSolvencia.BT_SCRMCI) ||
													 bt.equals(ConstantesSolvencia.BT_SCRMFE) || bt.equals(ConstantesSolvencia.BT_SCRMMI) ))) {
										
										CabeceraTablaExperiencia elementoCabecera = comprobarCabeceraTXP(i.getCtabbase());
										convertirValoresCTM(i, elementoCabecera, feccierre, bt);
									}
								} catch(Solvencia2Excepcion solvExc){
									Incidencias inci = new Incidencias();
									inci.setKbasetec(i.getKbasetec());
									inci.setKcompania(i.getKcompania());
									inci.setKnegocio(i.getKnegocio());
									inci.setKriesgo(i.getKriesgo());
									inci.setKsexo(i.getKsexo());
									inci.setKcateg(i.getKcateg());
									inci.setKedaddesde(i.getKedaddesde());
									inci.setKmodalidad(i.getKmodalidad());
									inci.setKfdesde(i.getKfdesde());
									inci.setEdadhasta(i.getEdadhasta());
									inci.setFhasta(i.getFhasta());
									inci.setCtabbase(i.getCtabbase());
									inci.setCodigoerror(solvExc.getIncidencia().getCodigoRetorno());
									inci.setDescerror(solvExc.getIncidencia().getInfAmpliada());
									gi.write(inci);	
								} catch (Exception e) {
									Incidencias inci = new Incidencias();
									inci.setKbasetec(i.getKbasetec());
									inci.setKcompania(i.getKcompania());
									inci.setKnegocio(i.getKnegocio());
									inci.setKriesgo(i.getKriesgo());
									inci.setKsexo(i.getKsexo());
									inci.setKcateg(i.getKcateg());
									inci.setKedaddesde(i.getKedaddesde());
									inci.setKmodalidad(i.getKmodalidad());
									inci.setKfdesde(i.getKfdesde());
									inci.setEdadhasta(i.getEdadhasta());
									inci.setFhasta(i.getFhasta());
									inci.setCtabbase(i.getCtabbase());
									inci.setCodigoerror("02");
									inci.setDescerror("Error genérico en la conversión de la tabla de experiencia.");
									gi.write(inci);	
								}
							}
						}
					}
				}
			}

			log.writeLog(ConstantesSolvencia.LOG_REG_GEN + contRegistros);
		} catch (Solvencia2Excepcion e) {
			ModuloTExpSCR.LOG.error(e.getMessage(), e);
			gi.write(e.getIncidencia());
			throw new Solvencia2Excepcion(e.getIncidencia().getInfAmpliada() , e.getIncidencia());
		} catch (Exception e) {
			ModuloTExpSCR.LOG.error(e.getMessage(), e);
			Incidencia inci = new Incidencia();
			inci.setTipoError("Error");
			inci.setInfAmpliada("Error genérico en la conversión de Tablas de Experiencia Real");
			inci.setCodigoRetorno("01");
			inci.setGeneradorError("ModuloTExpSCR");
			gi.write(inci);
			throw new Solvencia2Excepcion("Error genérico en la conversión de Tablas de Experiencia Real" , inci);
		}
	}

	private void convertirValoresCTM(ConvTablasExpReal elementoConversion, CabeceraTablaExperiencia elementoCabecera, String fechaEfecto, String bt) {
		String tipoValor = elementoCabecera.getK2tipotabla();
		BigDecimal ceroBD = new BigDecimal(0.0);
		if(Integer.parseInt(tipoValor) == 1){
			// Tabla no generacional
			convertirCTM(COD_NO_GENERACIONAL , ceroBD, ceroBD, ceroBD, elementoConversion, TIPO_NO_GENERACIONAL, fechaEfecto, bt);
		}else{
			if(Integer.parseInt(tipoValor) == 2){
				// Tabla complementaria
				Incidencia inci = new Incidencia();
				inci.setCodigoRetorno("00");
				inci.setInfAmpliada("Tabla base de tipo complementaria");
				throw new Solvencia2Excepcion(inci);
			}else{
				if(Integer.parseInt(tipoValor) == 3){
					// Tabla generacional
					convertirCTM(COD_GENERACIONAL , ceroBD, ceroBD, ceroBD, elementoConversion, TIPO_GENERACIONAL, fechaEfecto, bt);
				}
			}
		}
	}

	private CabeceraTablaExperiencia comprobarCabeceraTXP(Integer tablaBase) {

		CabeceraTablaExperienciaDao cabteDao = new CabeceraTablaExperienciaDao();
		List<CabeceraTablaExperiencia> cabte = cabteDao.getValueTabla(tablaBase);

		if(cabte == null || cabte.isEmpty()){
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("00");
			inci.setGeneradorError("ModuloTExpSCR");
			inci.setInfAmpliada("No existe registro asociado a la tabla base en el fichero de Cabecera de Tablas de Mortalidad.");
			throw new Solvencia2Excepcion(inci);
		}else{
			if(cabte.size() > 1){
				Incidencia inci = new Incidencia();
				inci.setCodigoRetorno("00");
				inci.setGeneradorError("ModuloTExpSCR");
				inci.setInfAmpliada("Existen varios registros asociados a la tabla base en el fichero de Cabecera de Tablas de Mortalidad.");
				throw new Solvencia2Excepcion(inci);
			}else{
				return cabte.get(0);
			}
		}
	}

	private List<ConvTablasExpReal> leerConversionTablasExperienciaReal(String fecCierre, String bt) {
		ConvTablasExpRealDao cteDao = new ConvTablasExpRealDao();
		List<ConvTablasExpReal> cte = cteDao.obtenerTablasExp(fecCierre, bt);

		if(cte == null || cte.isEmpty()){
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("05");
			inci.setGeneradorError("ModuloTExpSCR");
			inci.setInfAmpliada("El proceso de generación de tablas de experiencia reales no ha generado ningún registro.");
			throw new Solvencia2Excepcion(inci);
		}
		return cte;
	}

	private void convertirCTM(String tipoValor, BigDecimal interes, BigDecimal  sobremortalidad, BigDecimal sobreriesgo, ConvTablasExpReal elementCTE, String tipo, String fechaEfecto, String bt) {
		Integer tablaBase = elementCTE.getCtabbase();
		TablaExperienciaDao teDao = new TablaExperienciaDao();
		List<TablaExperiencia> te = teDao.obtenerRegistro(tablaBase, tipoValor, interes, sobremortalidad, sobreriesgo);
		TablaExperiencia elementTE = null;

		if(te == null || te.isEmpty()){
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("06");
			inci.setGeneradorError("ModuloTExpSCR");
			inci.setInfAmpliada("No existen registros asociados a la tabla base en el fichero con los valores de Tablas de mortalidad.");
			throw new Solvencia2Excepcion(inci);
		}else{
			if(te.size() > 1){
				if(tipo == TIPO_NO_GENERACIONAL){
					Incidencia inci = new Incidencia();
					inci.setCodigoRetorno("07");
					inci.setGeneradorError("ModuloTExpSCR");
					inci.setInfAmpliada("Existen varios registros asociados a la tabla base en el fichero con los valores de Tablas de mortalidad.");
					throw new Solvencia2Excepcion(inci);
				}else{
					for(int i = 0;i<te.size();i++){
						elementTE = te.get(i);
						calcularValores(elementTE, elementCTE, tipo, fechaEfecto, bt);
					}
				}
			}else{
				elementTE = te.get(0);
				calcularValores(elementTE, elementCTE, tipo, fechaEfecto, bt);
			}
		}

	}


	private void calcularValores(TablaExperiencia elementTE, ConvTablasExpReal elementCTE, String tipo, String fechaEfecto, String bt) {
		BigDecimal valorIncr = BigDecimal.ONE;
		BigDecimal valorCat = BigDecimal.ZERO;
		List<BigDecimal> varValoresLMI = null;
		
		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setDecimalSeparator('.');
		DecimalFormat dfFormatter = new DecimalFormat("000000000.00000000",simbolos);
		
		GestorFicheroSalida gfs = GestorFicheroSalida.getInstance(bt);
		GestorIncidencias gi = GestorIncidencias.getInstance(bt);

		Integer tablaBase = elementCTE.getCtabbase();
		BigDecimal resultado;
		BigDecimal resultadoL = new BigDecimal("1000000");
		int edadDesde = elementCTE.getKedaddesde();
		List<java.math.BigDecimal> qGen = new ArrayList<java.math.BigDecimal>();
		BigDecimal unidad = new BigDecimal(1);

		Salida elementoSalida = new Salida();
		elementoSalida.setKfchcierre(fechaEfecto);
		elementoSalida.setKbasetec(bt);
		elementoSalida.setKcompania(elementCTE.getKcompania());
		elementoSalida.setKnegocio(elementCTE.getKnegocio());
		elementoSalida.setKriesgo(elementCTE.getKriesgo());
		elementoSalida.setKsexo(elementCTE.getKsexo());
		elementoSalida.setKcateg(elementCTE.getKcateg());
		elementoSalida.setKmodalidad(elementCTE.getKmodalidad());
		elementoSalida.setCtabbase(tablaBase);
		// Se informan el usuario y fecha de alta
		elementoSalida.setUsuarioalta(elementCTE.getCusuaralta());
		//elementoSalida.setFechaalta(elementCTE.getFalta());
		elementoSalida.setFechaalta(strCurrentDate);

		if (bt.equals(ConstantesSolvencia.BT_SCRMFE) || bt.equals(ConstantesSolvencia.BT_SCRMMI)){
			valorIncr = BigDecimal.ONE.add(valEstres.multiply(ConstantesSolvencia.CTE_OPER_0_PUNTO_01));
			valorCat = BigDecimal.ZERO;
		} else if (bt.equals(ConstantesSolvencia.BT_SCRLFE)){
			valorIncr = BigDecimal.ONE.subtract(valEstres.multiply(ConstantesSolvencia.CTE_OPER_0_PUNTO_01));
			valorCat = BigDecimal.ZERO;
		} else if (bt.equals(ConstantesSolvencia.BT_SCRMCF) || bt.equals(ConstantesSolvencia.BT_SCRMCI)){
			valorIncr = BigDecimal.ONE;
			valorCat = valEstres.multiply(ConstantesSolvencia.CTE_OPER_0_PUNTO_01);
		} 
		
		int valores;
		if(tipo == TIPO_GENERACIONAL){
			elementoSalida.setKgeneracion(Integer.parseInt(elementTE.getKnacimiento()));
			BigDecimal lxGen;

			for(int i = 0;i<130;i++){
				lxGen = elementTE.getGvalor().get(i);
				if( lxGen.doubleValue() == 0 ){
					qGen.add(unidad);
				}else{
					BigDecimal resQx = unidad.subtract((elementTE.getGvalor().get(i+1)).divide(lxGen,32, RoundingMode.HALF_UP));
					qGen.add(resQx);
				}
			}
		}else{
			elementoSalida.setKgeneracion(0);
			for(int i = 0;i<elementTE.getGvalor().size();i++){
				qGen.add(elementTE.getGvalor().get(i));
			}			
		}
		if(elementCTE.getEdadhasta()<130)
			valores = elementCTE.getEdadhasta() - elementCTE.getKedaddesde() + 1;
		else{
			valores = 129 - elementCTE.getKedaddesde() + 1;
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("08");
			inci.setInfAmpliada("La edad hasta no puede ser mayor de 129.");
			throw new Solvencia2Excepcion(inci);
		}
		if(qGen.size() == 130){
			elementoSalida.setQx(new ArrayList<String>());
			elementoSalida.setLx(new ArrayList<String>());
			int j,a;
			int i = 0;
			for(j = 0;j<qGen.size();j++){
				elementoSalida.getLx().add(dfFormatter.format(new BigDecimal("0.0")));
				elementoSalida.getQx().add(dfFormatter.format(new BigDecimal("0.0")));
			}
			for(int k=0;k<valores;k++){
				elementoSalida.setKedadfija(edadDesde);
				
				for(j = 0;j<edadDesde;j++){
					elementoSalida.getLx().set(j, dfFormatter.format(new BigDecimal("0.0")));
					elementoSalida.getQx().set(j, dfFormatter.format(new BigDecimal("0.0")));
				}
				
				List<LongevidadModInterno> lmi = longevidadModInternoDao.obtenerLongevidadModInterno(fechaEfecto,elementCTE.getKsexo());
				
				a=0;
				i=0;
				for(j=edadDesde;j<130;j++){
					if (bt.equals(ConstantesSolvencia.BT_SCRLMI)){
						varValoresLMI = lmi.get(j).getPestres();
						valorIncr = BigDecimal.ONE.subtract(varValoresLMI.get(a));
						valorCat = BigDecimal.ZERO;
					}
					
					switch (i){
					case(0):
						resultado = qGen.get(j).multiply(elementCTE.getPfactor1().multiply(ConstantesSolvencia.CTE_OPER_0_PUNTO_01));
						resultado = resultado.multiply((elementCTE.getPfactor2a()).multiply(ConstantesSolvencia.CTE_OPER_0_PUNTO_01));
						resultado = resultado.multiply(valorIncr).add(valorCat);
						resultadoL = new BigDecimal("1000000");
						elementoSalida.getQx().set(j, dfFormatter.format(resultado.setScale(8, BigDecimal.ROUND_HALF_UP)));
						break;
					case(1):
						resultado = qGen.get(j).multiply(elementCTE.getPfactor1().multiply(ConstantesSolvencia.CTE_OPER_0_PUNTO_01));
						resultado = resultado.multiply((elementCTE.getPfactor2b()).multiply(ConstantesSolvencia.CTE_OPER_0_PUNTO_01));
						resultado = resultado.multiply(valorIncr).add(valorCat);
						resultadoL = resultadoL.multiply(new BigDecimal("1").subtract(new BigDecimal(elementoSalida.getQx().get(j-1))));
						elementoSalida.getQx().set(j, dfFormatter.format(resultado.setScale(8, BigDecimal.ROUND_HALF_UP)));
						break;
					case(2):
						resultado = qGen.get(j).multiply(elementCTE.getPfactor1().multiply(ConstantesSolvencia.CTE_OPER_0_PUNTO_01));
						resultado = resultado.multiply((elementCTE.getPfactor2c()).multiply(ConstantesSolvencia.CTE_OPER_0_PUNTO_01));
						resultado = resultado.multiply(valorIncr).add(valorCat);
						resultadoL = resultadoL.multiply(new BigDecimal("1").subtract(new BigDecimal(elementoSalida.getQx().get(j-1))));
						elementoSalida.getQx().set(j, dfFormatter.format(resultado.setScale(8, BigDecimal.ROUND_HALF_UP)));
						break;
					case(3):
						resultado = qGen.get(j).multiply(elementCTE.getPfactor1().multiply(ConstantesSolvencia.CTE_OPER_0_PUNTO_01));
						resultado = resultado.multiply((elementCTE.getPfactor2d()).multiply(ConstantesSolvencia.CTE_OPER_0_PUNTO_01));
						resultado = resultado.multiply(valorIncr).add(valorCat);
						resultadoL = resultadoL.multiply(new BigDecimal("1").subtract(new BigDecimal(elementoSalida.getQx().get(j-1))));
						elementoSalida.getQx().set(j, dfFormatter.format(resultado.setScale(8, BigDecimal.ROUND_HALF_UP)));
						break;
					case(4):
						resultado = qGen.get(j).multiply(elementCTE.getPfactor1().multiply(ConstantesSolvencia.CTE_OPER_0_PUNTO_01));
						resultado = resultado.multiply((elementCTE.getPfactor2e()).multiply(ConstantesSolvencia.CTE_OPER_0_PUNTO_01));
						resultado = resultado.multiply(valorIncr).add(valorCat);
						resultadoL = resultadoL.multiply(new BigDecimal("1").subtract(new BigDecimal(elementoSalida.getQx().get(j-1))));
						elementoSalida.getQx().set(j, dfFormatter.format(resultado.setScale(8, BigDecimal.ROUND_HALF_UP)));
						break;
					case(5):
						resultado = qGen.get(j).multiply(elementCTE.getPfactor1().multiply(ConstantesSolvencia.CTE_OPER_0_PUNTO_01));
						resultado = resultado.multiply((elementCTE.getPfactor2f()).multiply(ConstantesSolvencia.CTE_OPER_0_PUNTO_01));
						resultado = resultado.multiply(valorIncr).add(valorCat);
						resultadoL = resultadoL.multiply(new BigDecimal("1").subtract(new BigDecimal(elementoSalida.getQx().get(j-1))));
						elementoSalida.getQx().set(j, dfFormatter.format(resultado.setScale(8, BigDecimal.ROUND_HALF_UP)));
						break;
					case(6):
						resultado = qGen.get(j).multiply(elementCTE.getPfactor1().multiply(ConstantesSolvencia.CTE_OPER_0_PUNTO_01));
						resultado = resultado.multiply((elementCTE.getPfactor2g()).multiply(ConstantesSolvencia.CTE_OPER_0_PUNTO_01));
						resultado = resultado.multiply(valorIncr).add(valorCat);
						resultadoL = resultadoL.multiply(new BigDecimal("1").subtract(new BigDecimal(elementoSalida.getQx().get(j-1))));
						elementoSalida.getQx().set(j, dfFormatter.format(resultado.setScale(8, BigDecimal.ROUND_HALF_UP)));
						break;
					case(7):
						resultado = qGen.get(j).multiply(elementCTE.getPfactor1().multiply(ConstantesSolvencia.CTE_OPER_0_PUNTO_01));
						resultado = resultado.multiply((elementCTE.getPfactor2h()).multiply(ConstantesSolvencia.CTE_OPER_0_PUNTO_01));
						resultado = resultado.multiply(valorIncr).add(valorCat);
						resultadoL = resultadoL.multiply(new BigDecimal("1").subtract(new BigDecimal(elementoSalida.getQx().get(j-1))));
						elementoSalida.getQx().set(j, dfFormatter.format(resultado.setScale(8, BigDecimal.ROUND_HALF_UP)));
						break;
					case(8):
						resultado = qGen.get(j).multiply(elementCTE.getPfactor1().multiply(ConstantesSolvencia.CTE_OPER_0_PUNTO_01));
						resultado = resultado.multiply((elementCTE.getPfactor2i()).multiply(ConstantesSolvencia.CTE_OPER_0_PUNTO_01));
						resultado = resultado.multiply(valorIncr).add(valorCat);
						resultadoL = resultadoL.multiply(new BigDecimal("1").subtract(new BigDecimal(elementoSalida.getQx().get(j-1))));
						elementoSalida.getQx().set(j, dfFormatter.format(resultado.setScale(8, BigDecimal.ROUND_HALF_UP)));
						break;
					default:
						resultadoL = resultadoL.multiply(new BigDecimal("1").subtract(new BigDecimal(elementoSalida.getQx().get(j-1))));
						if(k==0 || bt.equals(ConstantesSolvencia.BT_SCRLMI)){
							resultado = qGen.get(j).multiply(elementCTE.getPfactor1().multiply(ConstantesSolvencia.CTE_OPER_0_PUNTO_01));
							resultado = resultado.multiply((elementCTE.getPfactor2j()).multiply(ConstantesSolvencia.CTE_OPER_0_PUNTO_01));
							resultado = resultado.multiply(valorIncr).add(valorCat);
							elementoSalida.getQx().set(j, dfFormatter.format(resultado.setScale(8, BigDecimal.ROUND_HALF_UP)));
						}
						break;
					}
					
					elementoSalida.getLx().set(j, dfFormatter.format(resultadoL.setScale(8, BigDecimal.ROUND_HALF_UP)));	
					++i;
					++a;
				}
				gfs.write(elementoSalida);
				contRegistros++;
				edadDesde = edadDesde + 1;
			}
		}else{
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("09");
			inci.setInfAmpliada("No existen los 130 valores necesarios de q(x).");
			gi.write(inci);
			throw new Solvencia2Excepcion(inci);
		}		
	}
}

