/**
 * 
 */
package es.mapfre.solvencia.open.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.DataSources;

import es.mapfre.solvencia.open.comun.Constantes;
import es.mapfre.solvencia.open.comun.SolvenciaUtils;
import es.mapfre.solvencia.open.enums.TipoFichero;
import es.mapfre.solvencia.open.exception.SolvenciaRuntimeException;

/**
 * @author amdepedro
 * 
 */
public final class ProceduresDAO {

	public static final int TIMEOUT_POOL = 10;

	private static Logger logger = LoggerFactory.getLogger(ProceduresDAO.class);

	private static final String DB_CONNECTION = SolvenciaUtils.getDBUrl();
	private static final String DB_USER = SolvenciaUtils.getDBUser();
	private static final String DB_PASSWORD = SolvenciaUtils.getDBPass();
	private static final String DB_SCHEME = SolvenciaUtils.getDBScheme();
	
	private static final Integer MAX_REINTENTOS = 5;
	private static final Integer ESPERA_REINTENTOS = 500;

	private static DataSource pooledDataSource = null;

	private static Boolean usePooledConnection = SolvenciaUtils.isParalelismo();

	private ProceduresDAO() {

	}

	public static void closeConnections() {
		try {
			if (pooledDataSource != null) {
				DataSources.destroy(pooledDataSource);
			}
		} catch (SQLException e) {
			logger.error("Error cerrando la conexión a BBDD", e);
		}
	}

	/**
	 * Método que construye el procedimiento almacenado a invocar dependiendo de
	 * si es una carga o una extracción y del tipo de fichero a cara o a extraer
	 * respectivamente.
	 * 
	 * @param tipo
	 *            tipo de fichero que puede ser MAESBTC, FLUJOSTOT o INCIDENCIAS
	 * @param isCargar
	 *            <code> true </code> indica que se trata de un procedimiento de
	 *            carga y <code> false </code> indica que se trata de un
	 *            procedimiento de extracción
	 * @param isCierre
	 *            <code> true </code> indica que se trata de un procedimiento de
	 *            cierre y <code> false </code> indica que se trata de un bote o
	 *            reproceso
	 * @return
	 */
	public static String getProcedure(TipoFichero tipo, Boolean isCargar, Boolean isCierre) {
		StringBuffer tipoProcedure = new StringBuffer("{call");
		tipoProcedure.append(StringUtils.SPACE).append(DB_SCHEME).append(".");
		if (TipoFichero.FLUJOSTOT.equals(tipo)) {
			if (isCargar) {
				if (isCierre) {
					tipoProcedure.append(Constantes.PROCEDIMIENTO_CARGAR_TOTALES_FLUJOS);
				} else {
					tipoProcedure.append(Constantes.PROCEDIMIENTO_ACTUALIZAR_TOTALES_FLUJOS);
				}
			} else {
				tipoProcedure.append(Constantes.PROCEDIMIENTO_EXTRAER_TOTALES_FLUJOS);
			}
		} else if (TipoFichero.MAESBTC.equals(tipo)) {
			if (isCargar) {
				if (isCierre) {
					tipoProcedure.append(Constantes.PROCEDIMIENTO_CARGAR_DETALLE_BASE_TECNICAS);
				} else {
					tipoProcedure.append(Constantes.PROCEDIMIENTO_ACTUALIZAR_DETALLE_BASE_TECNICAS);
				}
			} else {
				tipoProcedure.append(Constantes.PROCEDIMIENTO_EXTRAER_DETALLE_BASE_TECNICAS);
			}
		} else if (TipoFichero.INCIDENCIAS.equals(tipo)) {
			if (isCargar) {
				tipoProcedure.append(Constantes.PROCEDIMIENTO_CARGAR_INCIDENCIAS);
			} else {
				tipoProcedure.append(Constantes.PROCEDIMIENTO_EXTRAER_INCIDENCIAS);
			}
		} else {
			// TODO: gestionar excepciones
			logger.error("No se ha definido el tipo de procedimiento.");
			throw new SolvenciaRuntimeException("No se ha definido el tipo de procedimiento.");
		}
		// Se añaden los parámetros esperados en la invocación.
		if (isCargar) {
			tipoProcedure.append("(?,?)}");
		} else {
			tipoProcedure.append("(?,?,?,?,?,?)}");
		}
		logger.debug("ejecutando {}...", tipoProcedure.toString());
		return tipoProcedure.toString();
	}

	public static Connection getDBConnection() throws SQLException {
		return getDBConnection(usePooledConnection);
	}
	
	public static Connection getDBConnection(Boolean pooled) throws SQLException {
		synchronized (usePooledConnection) {
			Connection dbConnection = null;
			try {
				logger.debug("Conectando a {} con usuario {}.", DB_CONNECTION, DB_USER);
				if (pooled) {
					if (pooledDataSource == null) {
						logger.info("Usando pooled connection");
						DataSource unpooledDataSource = DataSources.unpooledDataSource(DB_CONNECTION, DB_USER,
								DB_PASSWORD);
						unpooledDataSource.setLoginTimeout(TIMEOUT_POOL);
						pooledDataSource = DataSources.pooledDataSource(unpooledDataSource);
						pooledDataSource.setLoginTimeout(TIMEOUT_POOL);
					}

					dbConnection = pooledDataSource.getConnection();
				} else {
					Integer intentos = 0;
					while (intentos < MAX_REINTENTOS && dbConnection == null) {
						try {
							dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
						} catch (SQLException se) {
							intentos++;
							if (intentos == MAX_REINTENTOS) {
								throw se;
							} else {
								try {
									Thread.sleep(intentos * ESPERA_REINTENTOS);
								} catch (InterruptedException e) {
									
								}
							}
						}
					}
				}

			} catch (SQLException e) {
				logger.error("ProceduresDAO.getDBConnection {}, {}:", DB_CONNECTION, DB_USER, e);
				throw e;
			}
			return dbConnection;
		}
	}

	public static void purgarTablas(String fechaCierre) throws SQLException {
		Connection dbConnection = null;
		CallableStatement callableStatement = null;
		StringBuffer tipoProcedure = new StringBuffer("{call");
		tipoProcedure.append(StringUtils.SPACE);
		tipoProcedure.append(DB_SCHEME);
		tipoProcedure.append(".");
		tipoProcedure.append(Constantes.PROCEDIMIENTO_PURGAR_TABLAS);
		tipoProcedure.append("(?)}");
		String procedimiento = tipoProcedure.toString();
		DateFormat dfAAAAMM = new SimpleDateFormat("yyyyMM");
		Date fechaCierreDate;
		try {
			fechaCierreDate = dfAAAAMM.parse(fechaCierre);
			Calendar fechaCierreCal = GregorianCalendar.getInstance();
			fechaCierreCal.setTime(fechaCierreDate);
			fechaCierreCal.set(Calendar.DAY_OF_MONTH,fechaCierreCal.getActualMaximum(Calendar.DAY_OF_MONTH));
			java.sql.Date fechaCierreSql = new java.sql.Date(fechaCierreCal.getTime().getTime());
			logger.info("Ejecutando {} para {}...", procedimiento, fechaCierreSql);

			try {
				dbConnection = ProceduresDAO.getDBConnection();
				if (dbConnection != null) {
					callableStatement = dbConnection.prepareCall(procedimiento);
					// Fecha de Cierre
					int i = 0;
					callableStatement.setDate(++i, fechaCierreSql);
					callableStatement.executeUpdate();
				}
			} catch (SQLException e) {
				logger.error("ProceduresDAO.purgarTablas: ", e);
			} finally {

				if (callableStatement != null) {
					callableStatement.close();
				}

				if (dbConnection != null) {
					dbConnection.close();
				}

			}
		} catch (ParseException e1) {
			logger.error("Fecha de cierre errónea: {}", fechaCierre);
		}

	}
}
