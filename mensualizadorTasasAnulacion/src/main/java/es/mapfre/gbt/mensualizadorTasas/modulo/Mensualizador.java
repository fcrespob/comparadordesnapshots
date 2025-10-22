package es.mapfre.gbt.mensualizadorTasas.modulo;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import net.sf.ehcache.Element;

import org.beanio.UnexpectedRecordException;
import org.beanio.UnidentifiedRecordException;

import es.mapfre.gbt.mensualizadorTasas.dao.FichaProcesoDao;
import es.mapfre.gbt.mensualizadorTasas.dao.TasasAnulacionDao;
import es.mapfre.gbt.mensualizadorTasas.dominio.FichaProceso;
import es.mapfre.gbt.mensualizadorTasas.dominio.TasaMensualizada;
import es.mapfre.gbt.mensualizadorTasas.dominio.TasasAnulacion;
import es.mapfre.gbt.mensualizadorTasas.exception.GestorIncidencias;
import es.mapfre.gbt.mensualizadorTasas.exception.IncidenciaMens;
import es.mapfre.gbt.mensualizadorTasas.key.TasaMensualizadaKey;
import es.mapfre.gbt.mensualizadorTasas.logger.LoggerManager;
import es.mapfre.gbt.mensualizadorTasas.utils.BeanIOReader;
import es.mapfre.gbt.mensualizadorTasas.utils.BtUtils;
import es.mapfre.gbt.mensualizadorTasas.utils.ConstantesMensualizador;

public class Mensualizador {

	private Properties properties = null;
	
	private BtUtils btUtils = new BtUtils();
	private String strCurrentDate = "";
	
	public Mensualizador() throws IOException {
		super();
		try {
			properties = new Properties();
			properties.load(ClassLoader.getSystemResourceAsStream("errores.properties"));
		} catch (IOException e) {
			throw e;
		}
	}

	private Collection<Element> getFichas() throws IOException, UnexpectedRecordException, UnidentifiedRecordException{
		BeanIOReader readerFicha = new BeanIOReader(ConstantesMensualizador.BEANIO_CONFIG,btUtils.getCargaFicherosProperty(ConstantesMensualizador.CACHE_R340T000), ConstantesMensualizador.CACHE_R340T000);
		FichaProcesoDao daoFP = new FichaProcesoDao();
		daoFP.loadCache(readerFicha);

		return daoFP.values();
	}
	
	private boolean isPositiveInteger(String s) {
	    int res = -1;
		try { 
	        res = Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    if(res>=0)
	    	return true;
	    else
	    	return false;
	}

	public Collection<Element> getRegistros() throws Exception{

		
		// Se obtiene la fecha del día
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date currentDate = new Date();
		this.strCurrentDate = sdf.format(currentDate);
		
		GestorIncidencias gi = GestorIncidencias.getInstance();
		IncidenciaMens incidencia = new IncidenciaMens("","","");
		
		LoggerManager log = LoggerManager.getInstance();
		
		TasasAnulacionDao dao = new TasasAnulacionDao(); 
		BeanIOReader reader;

		Collection<Element> fichas = null;

		try{
			fichas = getFichas();
			if (fichas.isEmpty()){
				incidencia.setDescripcion(properties.getProperty("error.noFichas"));
				gi.write(incidencia);
				gi.cerrarConector();
				throw new Exception(properties.getProperty("error.noFichas"));
			}
			else if(fichas.size()>1){
				incidencia.setDescripcion(properties.getProperty("error.variasFichas"));
				gi.write(incidencia);
				gi.cerrarConector();
				throw new Exception(properties.getProperty("error.variasFichas"));
			}
		} catch (IOException e){
			incidencia.setDescripcion(MessageFormat.format(properties.getProperty("error.fichero"),btUtils.getCargaFicherosProperty(ConstantesMensualizador.CACHE_R340T000)));
			gi.write(incidencia);
			gi.cerrarConector();
			throw new Exception(MessageFormat.format(properties.getProperty("error.fichero"),btUtils.getCargaFicherosProperty(ConstantesMensualizador.CACHE_R340T000)));
		} catch (UnexpectedRecordException e){
			incidencia.setDescripcion(properties.getProperty("error.noFichas"));
			gi.write(incidencia);
			gi.cerrarConector();
			throw new Exception(properties.getProperty("error.noFichas"));
		} catch (UnidentifiedRecordException e){
			incidencia.setDescripcion(properties.getProperty("error.noFichas"));
			gi.write(incidencia);
			gi.cerrarConector();
			throw new Exception(properties.getProperty("error.noFichas"));
		}


		Element fich = fichas.iterator().next();
		FichaProceso fichP = (FichaProceso) fich.getObjectValue();

		Timestamp fechaEfecto = fichP.getFefecto();
		if(fechaEfecto==null){
			incidencia.setDescripcion(properties.getProperty("error.noFefecto"));
			gi.write(incidencia);
			gi.cerrarConector();
			throw new Exception(properties.getProperty("error.noFefecto"));
		}

		try {
			reader = new BeanIOReader(ConstantesMensualizador.BEANIO_CONFIG, btUtils.getCargaFicherosProperty(ConstantesMensualizador.CACHE_VTA0), ConstantesMensualizador.CACHE_VTA0);
		} catch (IOException e2) {
			incidencia.setDescripcion(MessageFormat.format(properties.getProperty("error.fichero"),btUtils.getCargaFicherosProperty(ConstantesMensualizador.CACHE_VTA0)));
			gi.write(incidencia);
			gi.cerrarConector();
			throw new Exception(MessageFormat.format(properties.getProperty("error.fichero"),btUtils.getCargaFicherosProperty(ConstantesMensualizador.CACHE_VTA0)));
		} catch (Exception e2) {
			incidencia.setDescripcion(MessageFormat.format(properties.getProperty("error.fichero"),btUtils.getCargaFicherosProperty(ConstantesMensualizador.CACHE_VTA0)));
			gi.write(incidencia);
			gi.cerrarConector();
			throw new Exception(MessageFormat.format(properties.getProperty("error.fichero"),btUtils.getCargaFicherosProperty(ConstantesMensualizador.CACHE_VTA0)));
		}

		int anios = ConstantesMensualizador.ANIOS_DEFECTO;
		
		if(fichP.getRegistroParametros()!=null && isPositiveInteger(fichP.getRegistroParametros().getindicadormeses())){
			anios = Integer.parseInt(fichP.getRegistroParametros().getindicadormeses());
		}else{
			anios = ConstantesMensualizador.ANIOS_DEFECTO;
			incidencia.setDescripcion(properties.getProperty("error.noFichAnio"));
			gi.write(incidencia);
		}

		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(fechaEfecto.getTime());
		cal.add(Calendar.MONTH, -anios);
		Date fechaIni = new Date(cal.getTimeInMillis());
		SimpleDateFormat formateador = new SimpleDateFormat("yyyyMMdd");
		String fecha = formateador.format(fechaIni);

		dao.loadCache(reader, fecha);

		Collection<Element> result = new ArrayList<Element>();
		TasasAnulacionDao taDao = new TasasAnulacionDao();
		
		Collection<Element> taColl = taDao.values();
		List<Element> ta = new ArrayList<Element>(taColl);

		Collections.sort(ta, ordenadorValores);
		
		log.writeLog(ConstantesMensualizador.LOG_REG_TRA + ta.size());

		if(ta.isEmpty()){
			incidencia.setDescripcion(properties.getProperty("error.noDetalle"));
			incidencia.setFecha(formateador.format(fechaIni));
			gi.write(incidencia);
			gi.cerrarConector();
			throw new Exception(properties.getProperty("error.noDetalle"));
		}
		Iterator<Element> it = ta.iterator();
		TasaMensualizada tm = null;
		BigDecimal la = new BigDecimal("1000000.000");

		TasasAnulacion elemAnt = null;
		boolean descartado = false;
		BigDecimal tasaAnualAnt = null;

		while(it.hasNext()){
			Element entry = it.next();
			TasasAnulacion elem = (TasasAnulacion) entry.getObjectValue();
			if(elem.getKtablaanu()==null || elem.getKtablaanu().equals("")){
				incidencia.setDescripcion(properties.getProperty("error.noTabla"));
				gi.write(incidencia);
			}else if(elem.getKfcierre()==null){
				incidencia.setDescripcion(properties.getProperty("error.noFcierre"));
				gi.write(incidencia);
			}else if(elem.getPprobanu()==null){
				incidencia.setDescripcion(properties.getProperty("error.noProbForm"));
				incidencia.setFecha(elem.getKfcierre());
				incidencia.setTabla(elem.getKtablaanu());
				gi.write(incidencia);
			}/*else if(elem.getPprobanu().compareTo(new BigDecimal("0"))==0){
				incidencia.setDescripcion(properties.getProperty("error.noProb"));
				incidencia.setFecha(elem.getKfcierre());
				incidencia.setTabla(elem.getKtablaanu());
				gi.write(incidencia);
			}*/else if(elem.getPprobanu().compareTo(new BigDecimal("100"))>0){
				incidencia.setDescripcion(properties.getProperty("error.probSuperior"));
				incidencia.setFecha(elem.getKfcierre());
				incidencia.setTabla(elem.getKtablaanu());
				gi.write(incidencia);
			}else if(elem.getKaniosdesde()==null || elem.getNanioshasta()==null){
				incidencia.setDescripcion(properties.getProperty("error.noAnios"));
				incidencia.setFecha(elem.getKfcierre());
				incidencia.setTabla(elem.getKtablaanu());
				gi.write(incidencia);
			}else if(elem.getKaniosdesde().compareTo(elem.getNanioshasta()) == 1){
				incidencia.setDescripcion(properties.getProperty("error.incons"));
				incidencia.setFecha(elem.getKfcierre());
				incidencia.setTabla(elem.getKtablaanu());
				gi.write(incidencia);
			}else{
				BigDecimal tasaAnual = elem.getPprobanu();
				BigDecimal anioInicio = elem.getKaniosdesde();
				BigDecimal anioFin = elem.getNanioshasta();
				if(elemAnt==null || (!elem.getKtablaanu().equals(elemAnt.getKtablaanu()) || !elem.getKfcierre().equals(elemAnt.getKfcierre()))){
					if(anioInicio.compareTo(BigDecimal.ZERO)==1){
						elemAnt = elem;
						descartado=true;
						incidencia.setDescripcion(properties.getProperty("error.result"));
						incidencia.setFecha(elem.getKfcierre());
						incidencia.setTabla(elem.getKtablaanu());
						gi.write(incidencia);
						continue;
					}else{
						descartado = false;
					}
				}else{
					if(descartado)
						continue;

					descartado = false;

					if(elemAnt.getNanioshasta().compareTo(anioInicio)==1){
						incidencia.setDescripcion(properties.getProperty("error.resultAnio"));
						incidencia.setFecha(elem.getKfcierre());
						incidencia.setTabla(elem.getKtablaanu());
						gi.write(incidencia);
						continue;
					}else if(anioInicio.equals(elemAnt.getNanioshasta().add(BigDecimal.ONE))){
						incidencia.setDescripcion(properties.getProperty("error.resultSalto"));
						incidencia.setFecha(elem.getKfcierre());
						incidencia.setTabla(elem.getKtablaanu());
						gi.write(incidencia);
					}
				}

				if(anioFin.compareTo(new BigDecimal(130))==1){
					incidencia.setDescripcion(properties.getProperty("error.anioMayor"));
					incidencia.setFecha(elem.getKfcierre());
					incidencia.setTabla(elem.getKtablaanu());
					gi.write(incidencia);
					anioFin=new BigDecimal(130);
				}
				BigDecimal i = anioInicio;
				while(i.compareTo(anioFin)==-1){
					if(tasaAnualAnt!=null){
						la = calcularPoliza(elem,tm,tasaAnualAnt.doubleValue(),la);
					}else{
						la = calcularPoliza(elem,tm,tasaAnual.doubleValue(),la);
					}
					TasaMensualizadaKey tmkey = new TasaMensualizadaKey(elem.getKtablaanu(), elem.getKfcierre(), i);
					
					tm = new TasaMensualizada(elem.getKtablaanu(), elem.getKfcierre(), i, tasaAnual, la, elem.getCusuaralta(), this.strCurrentDate );
					result.add(new Element(tmkey, tm));
					tasaAnualAnt = tasaAnual;
					i = i.add(BigDecimal.ONE);
				}
				elemAnt = elem;
			}
		}
		gi.cerrarConector();
		log.writeLog(ConstantesMensualizador.LOG_REG_GEN + result.size());
		log.writeLog(ConstantesMensualizador.LOG_INCI_DET + gi.getNumIncidencias());
		
		return result;
	}
/*
	private BigDecimal calcularTasaMensual(TasasAnulacion periodo){
		BigDecimal result = null;

		double tasaAn_Anual = periodo.getPprobanu().doubleValue()/100;
		double tasaAn_Mensual = 1-Math.pow((1-tasaAn_Anual),(1.0/12));
		double redon = Math.round(tasaAn_Mensual*Math.pow(10,15))/Math.pow(10,15);
		result = new BigDecimal(redon);

		result = new BigDecimal(periodo.getPprobanu().doubleValue());
		return result;
	}
*/

	public BigDecimal calcularPoliza(TasasAnulacion periodo, TasaMensualizada mensual, double tasa, BigDecimal laAnt){
		BigDecimal result = laAnt;
		
		BigDecimal laAux = result.subtract((new BigDecimal(tasa).multiply(result)).divide(new BigDecimal("100")));

		if(mensual==null || (!periodo.getKtablaanu().equals(mensual.getKtablaanu()) || !periodo.getKfcierre().equals(mensual.getKfcierre()))){
			if(periodo.getPprobanu().compareTo(new BigDecimal("100"))==0){
				result = new BigDecimal("0");
			}else{
				result = new BigDecimal("1000000.000");
			}
		}else{
			result = laAux;
		}
		return result;
	}

	private static Comparator<Element> ordenadorValores = new Comparator<Element>() {
		@Override
		public int compare(Element o1E, Element oE) {
			TasasAnulacion o1 = (TasasAnulacion) o1E.getObjectValue();
			TasasAnulacion o = (TasasAnulacion) oE.getObjectValue();
			int result = 0;
			int comp = o1.getKtablaanu().compareTo(o.getKtablaanu());
			if(comp==0){
				comp = o1.getKfcierre().compareTo(o.getKfcierre());
				if(comp==0){
					if(o1.getKaniosdesde()==null && o.getKaniosdesde()==null){
						result = 0;
					}else if(o1.getKaniosdesde()==null){
						result = -1;
					}else if(o.getKaniosdesde()==null){
						result = 1;
					}else{
						comp = o1.getKaniosdesde().compareTo(o.getKaniosdesde());
						result = comp;
					}
				}else{
					result = comp;
				}
			}else{
				result = comp;
			}
			return result;
		}
	};
}