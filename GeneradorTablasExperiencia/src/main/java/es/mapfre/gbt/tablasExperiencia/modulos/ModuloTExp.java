//MODIFICACION: TAR00433819
//FECHA: 24/09/2018
//DESCRIP: SE PERMITE QUE LAS TABLAS COMPLEMENTARIAS GENEREN VALORES SIN DAR INCIDENCIA.
//

package es.mapfre.gbt.tablasExperiencia.modulos;

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

import es.mapfre.gbt.tablasExperiencia.dao.entidades.CabeceraTablaExperienciaDao;
import es.mapfre.gbt.tablasExperiencia.dao.entidades.ConvTablasExpRealDao;
import es.mapfre.gbt.tablasExperiencia.dao.entidades.TablaExperienciaDao;
import es.mapfre.gbt.tablasExperiencia.dominio.entidades.CabeceraTablaExperiencia;
import es.mapfre.gbt.tablasExperiencia.dominio.entidades.ConvTablasExpReal;
import es.mapfre.gbt.tablasExperiencia.dominio.entidades.Incidencia;
import es.mapfre.gbt.tablasExperiencia.dominio.entidades.Incidencias;
import es.mapfre.gbt.tablasExperiencia.dominio.entidades.Salida;
import es.mapfre.gbt.tablasExperiencia.dominio.entidades.TablaExperiencia;
import es.mapfre.gbt.tablasExperiencia.excepcion.Solvencia2Excepcion;
import es.mapfre.gbt.tablasExperiencia.gestores.GestorFicheroSalida;
import es.mapfre.gbt.tablasExperiencia.gestores.GestorIncidencias;
import es.mapfre.gbt.tablasExperiencia.utils.ConstantesSolvencia;
import es.mapfre.gbt.tablasExperiencia.utils.LoggerManager;

public class ModuloTExp {

	private static final Logger LOG = LoggerFactory.getLogger(ModuloTExp.class);
	private static final String COD_NO_GENERACIONAL = "1";
	private static final String COD_GENERACIONAL = "2";
	private static final String COD_COMPLEMENTARIA = "11";
	private static final String TIPO_NO_GENERACIONAL = "NG";
	private static final String TIPO_GENERACIONAL = "GE";
	private int contRegistros = 0;
	private String strCurrentDate = "";
	
	public String getNombreServicio() {
		return "ModuloTExp";
	}

	/** Ejecuci�n del proceso de creaci�n de tablas de experiencia reales */
	public final void execute(String fechaEfecto) {
		
		// Se obtiene la fecha del d�a
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date currentDate = new Date();
		this.strCurrentDate = sdf.format(currentDate);
		
		
		LoggerManager log = LoggerManager.getInstance();
		GestorIncidencias gi = GestorIncidencias.getInstance();

		try {
			if (ModuloTExp.LOG.isTraceEnabled()) {
				ModuloTExp.LOG.trace("Inicio de execute en clase ModuloTExp");
			}

			//recogerDatosEntrada();
			List<ConvTablasExpReal> listaConversiones = leerConversionTablasExperienciaReal(fechaEfecto);

			log.writeLog(ConstantesSolvencia.LOG_REG_TRA + listaConversiones.size());
			
			Iterator<ConvTablasExpReal> it = listaConversiones.iterator();
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
									CabeceraTablaExperiencia elementoCabecera = comprobarCabeceraTXP(i.getCtabbase());
									convertirValoresCTM(i, elementoCabecera, fechaEfecto);
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
									inci.setDescerror("Error gen�rico en la conversi�n de la tabla de experiencia.");
									gi.write(inci);	
								}
							}
						}
					}
				}
			}

			log.writeLog(ConstantesSolvencia.LOG_REG_GEN + contRegistros);
		} catch (Solvencia2Excepcion e) {
			ModuloTExp.LOG.error(e.getMessage(), e);
			gi.write(e.getIncidencia());
			throw new Solvencia2Excepcion(e.getIncidencia().getInfAmpliada() , e.getIncidencia());
		} catch (Exception e) {
			ModuloTExp.LOG.error(e.getMessage(), e);
			Incidencia inci = new Incidencia();
			inci.setTipoError("Error");
			inci.setInfAmpliada("Error gen�rico en la conversi�n de Tablas de Experiencia Real");
			inci.setCodigoRetorno("01");
			inci.setGeneradorError("ModuloTExp");
			gi.write(inci);
			throw new Solvencia2Excepcion("Error gen�rico en la conversi�n de Tablas de Experiencia Real" , inci);
		}


	}

	private void convertirValoresCTM(ConvTablasExpReal elementoConversion, CabeceraTablaExperiencia elementoCabecera, String fechaEfecto) {
		String tipoValor = elementoCabecera.getK2tipotabla();
		BigDecimal ceroBD = new BigDecimal(0.0);
		if(Integer.parseInt(tipoValor) == 1){
			// Tabla no generacional
			convertirCTM(COD_NO_GENERACIONAL , ceroBD, ceroBD, ceroBD, elementoConversion, TIPO_NO_GENERACIONAL, fechaEfecto);
		}else{
			if(Integer.parseInt(tipoValor) == 2){
				
				// Tabla complementaria
//INI-TAR00433819: Permitimos generar valores para las tablas complentarias, sin generar incidencia.
//				Incidencia inci = new Incidencia();
//				inci.setCodigoRetorno("00");
//				inci.setInfAmpliada("Tabla base de tipo complementaria");
//				throw new Solvencia2Excepcion(inci);
				convertirCTM(COD_COMPLEMENTARIA , ceroBD, ceroBD, ceroBD, elementoConversion, TIPO_NO_GENERACIONAL, fechaEfecto);
//FIN-TAR00433819
			}else{
				if(Integer.parseInt(tipoValor) == 3){
					// Tabla generacional
					convertirCTM(COD_GENERACIONAL , ceroBD, ceroBD, ceroBD, elementoConversion, TIPO_GENERACIONAL, fechaEfecto);
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
			inci.setGeneradorError("ModuloTExp");
			inci.setInfAmpliada("No existe registro asociado a la tabla base en el fichero de Cabecera de Tablas de Mortalidad.");
			throw new Solvencia2Excepcion(inci);
		}else{
			if(cabte.size() > 1){
				Incidencia inci = new Incidencia();
				inci.setCodigoRetorno("00");
				inci.setGeneradorError("ModuloTExp");
				inci.setInfAmpliada("Existen varios registros asociados a la tabla base en el fichero de Cabecera de Tablas de Mortalidad.");
				throw new Solvencia2Excepcion(inci);
			}else{
				return cabte.get(0);
			}
		}
	}

	private List<ConvTablasExpReal> leerConversionTablasExperienciaReal(String fecCierre) {
		ConvTablasExpRealDao cteDao = new ConvTablasExpRealDao();
		List<ConvTablasExpReal> cte = cteDao.obtenerTablasExp(fecCierre);

		if(cte == null || cte.isEmpty()){
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("05");
			inci.setGeneradorError("ModuloTExp");
			inci.setInfAmpliada("El proceso de generaci�n de tablas de experiencia reales no ha generado ning�n registro.");
			throw new Solvencia2Excepcion(inci);
		}
		return cte;
	}

	private void convertirCTM(String tipoValor, BigDecimal interes, BigDecimal  sobremortalidad, BigDecimal sobreriesgo, ConvTablasExpReal elementCTE, String tipo, String fechaEfecto) {
		Integer tablaBase = elementCTE.getCtabbase();
		TablaExperienciaDao teDao = new TablaExperienciaDao();
		List<TablaExperiencia> te = teDao.obtenerRegistro(tablaBase, tipoValor, interes, sobremortalidad, sobreriesgo);
		TablaExperiencia elementTE = null;

		if(te == null || te.isEmpty()){
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("06");
			inci.setGeneradorError("ModuloTExp");
			inci.setInfAmpliada("No existen registros asociados a la tabla base en el fichero con los valores de Tablas de mortalidad.");
			throw new Solvencia2Excepcion(inci);
		}else{
			if(te.size() > 1){
				if(tipo == TIPO_NO_GENERACIONAL){
					Incidencia inci = new Incidencia();
					inci.setCodigoRetorno("07");
					inci.setGeneradorError("ModuloTExp");
					inci.setInfAmpliada("Existen varios registros asociados a la tabla base en el fichero con los valores de Tablas de mortalidad.");
					throw new Solvencia2Excepcion(inci);
				}else{
					for(int i = 0;i<te.size();i++){
						elementTE = te.get(i);
						calcularValores(elementTE, elementCTE, tipo, fechaEfecto);
					}
				}
			}else{
				elementTE = te.get(0);
				calcularValores(elementTE, elementCTE, tipo, fechaEfecto);


			}
		}

	}


	private void calcularValores(TablaExperiencia elementTE, ConvTablasExpReal elementCTE, String tipo, String fechaEfecto) {
		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setDecimalSeparator('.');
		DecimalFormat dfFormatter = new DecimalFormat("000000000.00000000",simbolos);
		
		GestorFicheroSalida gfs = GestorFicheroSalida.getInstance();
		GestorIncidencias gi = GestorIncidencias.getInstance();

		Integer tablaBase = elementCTE.getCtabbase();
		BigDecimal resultado = null;
		BigDecimal resultadoL = new BigDecimal("1000000");
		int edadDesde = elementCTE.getKedaddesde();
		List<java.math.BigDecimal> qGen = new ArrayList<java.math.BigDecimal>();
		BigDecimal unidad = new BigDecimal(1);

		Salida elementoSalida = new Salida();
		elementoSalida.setKfchcierre(fechaEfecto);
		elementoSalida.setKbasetec(elementCTE.getKbasetec());
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
			int j;
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
				
				i=0;
				for(j=edadDesde;j<130;j++){
					
					BigDecimal auxQx;
					switch (i){
					case(0):
						resultado = qGen.get(j).multiply(elementCTE.getPfactor1().divide(new BigDecimal("100")));
						resultado = resultado.multiply((elementCTE.getPfactor2a()).divide(new BigDecimal("100")));
						
						if (elementCTE.getKriesgo().equals("INCA")
								|| elementCTE.getKriesgo().equals("FACC")) {
							if(resultado.compareTo(BigDecimal.valueOf(100)) == 1){
									resultado = BigDecimal.ONE;
								}
						} else {
							if(resultado.compareTo(BigDecimal.ONE) == 1){
								resultado = BigDecimal.ONE;
							}
						}
						
						resultadoL = new BigDecimal("1000000");
						
						elementoSalida.getQx().set(j, dfFormatter.format(resultado.setScale(8, BigDecimal.ROUND_HALF_UP)));
						
						break;
					case(1):
						resultado = qGen.get(j).multiply(elementCTE.getPfactor1().divide(new BigDecimal("100")));
						resultado = resultado.multiply((elementCTE.getPfactor2b()).divide(new BigDecimal("100")));
						
						if (elementCTE.getKriesgo().equals("INCA")
								|| elementCTE.getKriesgo().equals("FACC")) {
							if(resultado.compareTo(BigDecimal.valueOf(100)) == 1){
								resultado = BigDecimal.ONE;
							}
								
							auxQx = resultado.divide(new BigDecimal("100"));
							resultadoL = resultadoL.multiply(new BigDecimal("1").subtract(auxQx));
						} else {
							if(resultado.compareTo(BigDecimal.ONE) == 1){
								resultado = BigDecimal.ONE;
							}
							resultadoL = resultadoL.multiply(new BigDecimal("1").subtract(new BigDecimal(elementoSalida.getQx().get(j-1))));
						}
						
						elementoSalida.getQx().set(j, dfFormatter.format(resultado.setScale(8, BigDecimal.ROUND_HALF_UP)));
						
						break;
					case(2):
						resultado = qGen.get(j).multiply(elementCTE.getPfactor1().divide(new BigDecimal("100")));
						resultado = resultado.multiply((elementCTE.getPfactor2c()).divide(new BigDecimal("100")));
						
						if (elementCTE.getKriesgo().equals("INCA")
								|| elementCTE.getKriesgo().equals("FACC")) {
							if(resultado.compareTo(BigDecimal.valueOf(100)) == 1){
								resultado = BigDecimal.ONE;
							}
								
							auxQx = resultado.divide(new BigDecimal("100"));
							resultadoL = resultadoL.multiply(new BigDecimal("1").subtract(auxQx));
						} else {
							if(resultado.compareTo(BigDecimal.ONE) == 1){
								resultado = BigDecimal.ONE;
							}
							resultadoL = resultadoL.multiply(new BigDecimal("1").subtract(new BigDecimal(elementoSalida.getQx().get(j-1))));
						}
						
						elementoSalida.getQx().set(j, dfFormatter.format(resultado.setScale(8, BigDecimal.ROUND_HALF_UP)));
					
						break;
					case(3):
						resultado = qGen.get(j).multiply(elementCTE.getPfactor1().divide(new BigDecimal("100")));
						resultado = resultado.multiply((elementCTE.getPfactor2d()).divide(new BigDecimal("100")));
						
						if (elementCTE.getKriesgo().equals("INCA")
								|| elementCTE.getKriesgo().equals("FACC")) {
							if(resultado.compareTo(BigDecimal.valueOf(100)) == 1){
								resultado = BigDecimal.ONE;
							}
								
							auxQx = resultado.divide(new BigDecimal("100"));
							resultadoL = resultadoL.multiply(new BigDecimal("1").subtract(auxQx));
						} else {
							if(resultado.compareTo(BigDecimal.ONE) == 1){
								resultado = BigDecimal.ONE;
							}
							resultadoL = resultadoL.multiply(new BigDecimal("1").subtract(new BigDecimal(elementoSalida.getQx().get(j-1))));
						}
						
						elementoSalida.getQx().set(j, dfFormatter.format(resultado.setScale(8, BigDecimal.ROUND_HALF_UP)));
						
						break;
					case(4):
						resultado = qGen.get(j).multiply(elementCTE.getPfactor1().divide(new BigDecimal("100")));
						resultado = resultado.multiply((elementCTE.getPfactor2e()).divide(new BigDecimal("100")));
						
						if (elementCTE.getKriesgo().equals("INCA")
								|| elementCTE.getKriesgo().equals("FACC")) {
							if(resultado.compareTo(BigDecimal.valueOf(100)) == 1){
								resultado = BigDecimal.ONE;
							}
								
							auxQx = resultado.divide(new BigDecimal("100"));
							resultadoL = resultadoL.multiply(new BigDecimal("1").subtract(auxQx));
						} else {
							if(resultado.compareTo(BigDecimal.ONE) == 1){
								resultado = BigDecimal.ONE;
							}
							resultadoL = resultadoL.multiply(new BigDecimal("1").subtract(new BigDecimal(elementoSalida.getQx().get(j-1))));
						}
						
						elementoSalida.getQx().set(j, dfFormatter.format(resultado.setScale(8, BigDecimal.ROUND_HALF_UP)));
						
						break;
					case(5):
						resultado = qGen.get(j).multiply(elementCTE.getPfactor1().divide(new BigDecimal("100")));
						resultado = resultado.multiply((elementCTE.getPfactor2f()).divide(new BigDecimal("100")));
						
						if (elementCTE.getKriesgo().equals("INCA")
								|| elementCTE.getKriesgo().equals("FACC")) {
							if(resultado.compareTo(BigDecimal.valueOf(100)) == 1){
								resultado = BigDecimal.ONE;
							}
								
							auxQx = resultado.divide(new BigDecimal("100"));
							resultadoL = resultadoL.multiply(new BigDecimal("1").subtract(auxQx));
						} else {
							if(resultado.compareTo(BigDecimal.ONE) == 1){
								resultado = BigDecimal.ONE;
							}
							
							resultadoL = resultadoL.multiply(new BigDecimal("1").subtract(new BigDecimal(elementoSalida.getQx().get(j-1))));	
						}
						
						elementoSalida.getQx().set(j, dfFormatter.format(resultado.setScale(8, BigDecimal.ROUND_HALF_UP)));
						
						break;
					case(6):
						resultado = qGen.get(j).multiply(elementCTE.getPfactor1().divide(new BigDecimal("100")));
						resultado = resultado.multiply((elementCTE.getPfactor2g()).divide(new BigDecimal("100")));
						
						if (elementCTE.getKriesgo().equals("INCA")
								|| elementCTE.getKriesgo().equals("FACC")) {
							if(resultado.compareTo(BigDecimal.valueOf(100)) == 1){
								resultado = BigDecimal.ONE;
							}
								
							auxQx = resultado.divide(new BigDecimal("100"));
							resultadoL = resultadoL.multiply(new BigDecimal("1").subtract(auxQx));
						} else {
							if(resultado.compareTo(BigDecimal.ONE) == 1){
								resultado = BigDecimal.ONE;
							}
							
							resultadoL = resultadoL.multiply(new BigDecimal("1").subtract(new BigDecimal(elementoSalida.getQx().get(j-1))));
						}
						
						elementoSalida.getQx().set(j, dfFormatter.format(resultado.setScale(8, BigDecimal.ROUND_HALF_UP)));
						
						break;
					case(7):
						resultado = qGen.get(j).multiply(elementCTE.getPfactor1().divide(new BigDecimal("100")));
						resultado = resultado.multiply((elementCTE.getPfactor2h()).divide(new BigDecimal("100")));
						
						if (elementCTE.getKriesgo().equals("INCA")
								|| elementCTE.getKriesgo().equals("FACC")) {
							if(resultado.compareTo(BigDecimal.valueOf(100)) == 1){
								resultado = BigDecimal.ONE;
							}
								
							auxQx = resultado.divide(new BigDecimal("100"));
							resultadoL = resultadoL.multiply(new BigDecimal("1").subtract(auxQx));
						} else {
							if(resultado.compareTo(BigDecimal.ONE) == 1){
								resultado = BigDecimal.ONE;
							}
							
							resultadoL = resultadoL.multiply(new BigDecimal("1").subtract(new BigDecimal(elementoSalida.getQx().get(j-1))));
						}
						
						elementoSalida.getQx().set(j, dfFormatter.format(resultado.setScale(8, BigDecimal.ROUND_HALF_UP)));
						
						break;
					case(8):
						resultado = qGen.get(j).multiply(elementCTE.getPfactor1().divide(new BigDecimal("100")));
						resultado = resultado.multiply((elementCTE.getPfactor2i()).divide(new BigDecimal("100")));
						
						if (elementCTE.getKriesgo().equals("INCA")
								|| elementCTE.getKriesgo().equals("FACC")) {
							if(resultado.compareTo(BigDecimal.valueOf(100)) == 1){
								resultado = BigDecimal.ONE;
							}
								
							auxQx = resultado.divide(new BigDecimal("100"));
							resultadoL = resultadoL.multiply(new BigDecimal("1").subtract(auxQx));
						} else {
							if(resultado.compareTo(BigDecimal.ONE) == 1){
								resultado = BigDecimal.ONE;
							}
							
							resultadoL = resultadoL.multiply(new BigDecimal("1").subtract(new BigDecimal(elementoSalida.getQx().get(j-1))));
						}	
						
						elementoSalida.getQx().set(j, dfFormatter.format(resultado.setScale(8, BigDecimal.ROUND_HALF_UP)));
						
						break;
					default:
						
						if (elementCTE.getKriesgo().equals("INCA")
								|| elementCTE.getKriesgo().equals("FACC")) {
							if (resultado == null) {
								auxQx = new BigDecimal(elementoSalida.getQx().get(j-1)).divide(new BigDecimal("100")).multiply(elementCTE.getPfactor1().divide(new BigDecimal("100")));
							} else {
								auxQx = resultado.divide(new BigDecimal("100"));
							}
							
							resultadoL = resultadoL.multiply(new BigDecimal("1").subtract(auxQx));
						} else {
							resultadoL = resultadoL.multiply(new BigDecimal("1").subtract(new BigDecimal(elementoSalida.getQx().get(j-1))));
						}
						
						if(k==0){
							resultado = qGen.get(j).multiply(elementCTE.getPfactor1().divide(new BigDecimal("100")));
							resultado = resultado.multiply((elementCTE.getPfactor2j()).divide(new BigDecimal("100")));
							
							if (elementCTE.getKriesgo().equals("INCA")
									|| elementCTE.getKriesgo().equals("FACC")) {
								if(resultado.compareTo(BigDecimal.valueOf(100)) == 1){
									resultado = BigDecimal.ONE;
								}
							} else {
								if(resultado.compareTo(BigDecimal.ONE) == 1){
									resultado = BigDecimal.ONE;
								}
							}
							
							elementoSalida.getQx().set(j, dfFormatter.format(resultado.setScale(8, BigDecimal.ROUND_HALF_UP)));
							
						}
						break;
					}
					elementoSalida.getLx().set(j, dfFormatter.format(resultadoL.setScale(8, BigDecimal.ROUND_HALF_UP)));	
					++i;
				}
				gfs.write(elementoSalida);
				contRegistros++;
				edadDesde = edadDesde + 1;
			}
			LoggerManager log = LoggerManager.getInstance();
		}else{
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("09");
			inci.setInfAmpliada("No existen los 130 valores necesarios de q(x).");
			gi.write(inci);
			throw new Solvencia2Excepcion(inci);
		}		
	}
}

