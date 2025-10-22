package es.mapfre.solvencia.excepcion.helper;

import java.sql.Timestamp;
import java.text.MessageFormat;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.maestro.ErrorSolvencia2Key;
import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.ErrorSolvencia2Dao;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.ErrorSolvencia2;
import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.Solvencia2NoStackExcepcion;

public final class Solvencia2ExcepcionHelper {

	private static final String PARENTESIS_DER = ")";
	private static final String PARENTESIS_IZQ = "(";
	private static final String EMPTY_STRING = "";
	private static final String FLECHA_IZQ = "<-";
	private static final String EXT_JAVA = ".java";
	private static final String MAPFRE = "mapfre";
	
	private static final String KAPLICACION = "880";
	private static final String KIDPROGRAMA = null;

	private static final ErrorSolvencia2Dao errorSolvencia2Dao = new ErrorSolvencia2Dao();
	private static Logger log = LoggerFactory.getLogger(Solvencia2ExcepcionHelper.class);

	private Solvencia2ExcepcionHelper() {
		super();
	}
	
	public static Solvencia2Excepcion crearExcepcion(String kretorno){
		return Solvencia2ExcepcionHelper.crearExcepcion(kretorno, null, null);
	}

	public static Solvencia2Excepcion crearExcepcion(Throwable cause) {
		if (cause instanceof Solvencia2Excepcion) {
			Solvencia2Excepcion ex = (Solvencia2Excepcion) cause;
			Incidencia incidencia = ex.getIncidencia();
			return Solvencia2ExcepcionHelper.crearExcepcion(incidencia.getCodigoRetorno() ,
			incidencia.getArgs(), incidencia.getGeneradorError(), incidencia.getBt(),
			incidencia.getCcanal(), incidencia.getCcartera(), incidencia.getClaveUmic(),
			incidencia.getFecCierre(), incidencia.getCnegocio(), ex);
		}
		return Solvencia2ExcepcionHelper.crearExcepcion(null, null, cause);
	}
	
	public static Solvencia2Excepcion crearExcepcion(String kretorno, Object[] args){
		return Solvencia2ExcepcionHelper.crearExcepcion(kretorno, args, null);
	}

	public static Solvencia2Excepcion crearExcepcion(String kidprograma, Throwable cause) {
		Incidencia incidencia = null;
		if (cause instanceof Solvencia2Excepcion) {
			Solvencia2Excepcion ex = (Solvencia2Excepcion) cause;
			incidencia = ex.getIncidencia();
		} else {
			incidencia = new Incidencia();
		}
		
		incidencia = Solvencia2ExcepcionHelper.crearIncidencia(KAPLICACION, kidprograma,
				incidencia.getBt(), incidencia.getCcanal(), incidencia.getCcartera(),
				incidencia.getClaveUmic(), incidencia.getFecCierre(),
				incidencia.getCnegocio(), incidencia.getCodigoRetorno(), incidencia.getArgs(), cause);
		
		if (log.isDebugEnabled()) {
			return new Solvencia2Excepcion(incidencia.getTextoError(), incidencia, cause);
		}

		return new Solvencia2NoStackExcepcion(incidencia.getTextoError(), incidencia, cause);
	}

	public static Solvencia2Excepcion crearExcepcion(String kretorno,
			Object[] args, Throwable cause) {
		return Solvencia2ExcepcionHelper.crearExcepcion(kretorno, args, null,
				cause);
	}

	public static Solvencia2Excepcion crearExcepcion(String kretorno,
			Object[] args, String kidprograma, Throwable cause) {
		return Solvencia2ExcepcionHelper.crearExcepcion(kretorno, args,
				kidprograma, null, null, null, null, null, null, cause);
	}

	public static Solvencia2Excepcion crearExcepcion(String kretorno,
			Object[] args, String kidprograma, String baseTecnica,
			Integer canal, Integer carteraOrigen, UmicKey claveUmic,
			Timestamp fecCierre, String negocio, Throwable cause) {

		Incidencia incidencia = Solvencia2ExcepcionHelper.crearIncidencia(KAPLICACION, kidprograma,
				baseTecnica, canal, carteraOrigen, claveUmic, fecCierre,
				negocio, kretorno, args, cause);

		if (log.isDebugEnabled()) {
			return new Solvencia2Excepcion(incidencia.getTextoError(), incidencia, cause);
		}
		
		return new Solvencia2NoStackExcepcion(incidencia.getTextoError(), incidencia, cause);
	}

	public static Incidencia crearAviso(String baseTecnica, Integer canal,
			Integer carteraOrigen, UmicKey claveUmic, Timestamp fecCierre,
			String negocio, String programa, String codError, Object[] args) {

		return crearIncidencia(KAPLICACION, programa, baseTecnica, canal,
				carteraOrigen, claveUmic, fecCierre, negocio, codError, args, null);
	}

	public static Incidencia crearAviso(String codError, Object[] args) {
		return crearIncidencia(KAPLICACION, KIDPROGRAMA, null, null, null,
				null, null, null, codError, args, null);
	}

	public static Incidencia crearAviso(String codError, Object[] args, String bt, Integer canal,String negocio,UmicKey claveUmic) {
		return crearIncidencia(KAPLICACION, KIDPROGRAMA, bt, canal, null,
				claveUmic, null, negocio, codError, args, null);
	}
	
	private static Incidencia crearIncidencia(String kaplicacion,
			String kidprograma, String baseTecnica, Integer canal,
			Integer carteraOrigen, UmicKey claveUmic, Timestamp fecCierre,
			String negocio, String kretorno, Object[] args, Throwable cause) {

		if(args == null) {
			args = ArrayUtils.EMPTY_OBJECT_ARRAY;
		}
		ErrorSolvencia2 error = errorSolvencia2Dao.get(new ErrorSolvencia2Key(
				kaplicacion, kidprograma, kretorno));
		if (error == null) {
			error = errorSolvencia2Dao.get(new ErrorSolvencia2Key(kaplicacion, null,kretorno));
		}
		if (error == null) {
			error = new ErrorSolvencia2();
			error.setCnivel(ConstantesSolvencia.CTE_ERROR);
			error.setGdesc("ERROR "
					+ (kidprograma != null ? "en programa " + kidprograma : 
					(cause != null && StringUtils.isNotBlank(cause.getMessage()) ? cause.getMessage() : ConstantesSolvencia.MENSAJE_ERROR_TECNICO)));
		}

		Incidencia incidencia = new Incidencia();
		incidencia.setBt(baseTecnica);
		incidencia.setCcanal(canal);
		incidencia.setCcartera(carteraOrigen);
		incidencia.setClaveUmic(claveUmic);
		incidencia.setCodigoRetorno(kretorno);
		incidencia.setTextoError(MessageFormat.format(error.getGdesc(), args).trim());
		incidencia.setFecCierre(fecCierre);
		incidencia.setCnegocio(negocio);
		incidencia.setGeneradorError(kidprograma);
		incidencia.setTipoError(error.getCnivel());
		incidencia.setArgs(args);
		
		// ¿Se debe controlar de alguna forma cuándo escribimos la traza en la incidencia?
		//if (log.isDebugEnabled() ) {
			StackTraceElement[] stElements = null;
			if (cause != null && cause.getStackTrace() != null && cause.getStackTrace().length > 0) {
				stElements = cause.getStackTrace();
			} else {
				stElements = Thread.currentThread().getStackTrace();
			}
			
			incidencia.setInfAmpliada(getStackTraceElements(stElements));
		//}
		
		return incidencia;
	}

	private static String getStackTraceElements(StackTraceElement[] stElements) {
		
		StringBuilder clases = new StringBuilder();
		
		if (stElements != null) {
			boolean init = false;
			for (StackTraceElement stElement : stElements) {
				if (stElement.getClassName().toLowerCase().contains(MAPFRE) && !stElement.getClassName().toLowerCase().contains(Solvencia2ExcepcionHelper.class.getSimpleName().toLowerCase())) {
					if (init) {
						clases.append(FLECHA_IZQ);
					}
					clases.append(stElement.getFileName().replace(EXT_JAVA, EMPTY_STRING));
					clases.append(PARENTESIS_IZQ).append(stElement.getLineNumber()).append(PARENTESIS_DER);
					init = true;
				}
			}

		}
		
		return clases.toString();
	}
	
	
	public static void checkIncidencia(String bt, Integer ccanal, Integer ccartera,
			UmicKey umicKey, Timestamp fecCierre, String cnegocio,
			String kprotecnico, Solvencia2Excepcion s2e) {
		
		if(s2e.getIncidencia().getBt() == null){
			s2e.getIncidencia().setBt(bt);
		}
		if(s2e.getIncidencia().getCcanal() == null){
			s2e.getIncidencia().setCcanal(ccanal);
		}
		if(s2e.getIncidencia().getCcartera() == null){
			s2e.getIncidencia().setCcartera(ccartera);
		}
		if(s2e.getIncidencia().getClaveUmic() == null){
			s2e.getIncidencia().setClaveUmic(umicKey);
		}
		if(s2e.getIncidencia().getFecCierre() == null){
			s2e.getIncidencia().setFecCierre(fecCierre);
		}
		if(s2e.getIncidencia().getCnegocio() == null){
			s2e.getIncidencia().setCnegocio(cnegocio);
		}
		if(s2e.getIncidencia().getGeneradorError() == null){
			s2e.getIncidencia().setGeneradorError(kprotecnico);
		}
	}

}
