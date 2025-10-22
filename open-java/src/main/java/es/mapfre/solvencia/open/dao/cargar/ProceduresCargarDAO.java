/**
 * 
 */
package es.mapfre.solvencia.open.dao.cargar;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.C3P0ProxyStatement;

import es.mapfre.solvencia.open.dao.ProceduresDAO;
import es.mapfre.solvencia.open.dto.CargarFicheroDTO;
import es.mapfre.solvencia.open.enums.TipoFichero;

/**
 * @author indra
 *
 */
public final class ProceduresCargarDAO {
	
	private static Logger logger = LoggerFactory.getLogger(ProceduresCargarDAO.class);

	private ProceduresCargarDAO() {

	}

	
	public static void callOracleStoredProcedures(TipoFichero tipoFichero, List<CargarFicheroDTO> params, Boolean isCierre)
			throws SQLException {

		Map<String, List<String>> ficheros = getFicherosPorDirectorio(params);
		for (Entry<String, List<String>> entry : ficheros.entrySet()) {
			callOracleStoredProcedures(entry.getKey(), entry.getValue(), tipoFichero, isCierre);
		}
	}
	
	private static void callOracleStoredProcedures(String directorio, List<String> ficheros, 
			TipoFichero tipoFichero, Boolean isCierre) throws SQLException {

		Connection dbConnection = null;
		CallableStatement callableStatement = null;
		String procedure = ProceduresDAO.getProcedure(tipoFichero, Boolean.TRUE, isCierre);
		
		try {
			dbConnection = ProceduresDAO.getDBConnection(Boolean.FALSE);
			
			if (dbConnection != null) {
				callableStatement = dbConnection.prepareCall(procedure);
				int i = 0;
				callableStatement.setString(++i, directorio);
				String[] ficherosString = ficheros.toArray(new String[0]);
				if (callableStatement instanceof OracleCallableStatement) {
					((OracleCallableStatement)callableStatement).setPlsqlIndexTable(++i, ficherosString, ficheros.size(), ficheros.size(), OracleTypes.VARCHAR, 0);
				} else if (callableStatement instanceof C3P0ProxyStatement) {
					try {
						Method m = OracleCallableStatement.class.getMethod("setPlsqlIndexTable", new Class[] {int.class, Object.class, int.class, int.class, int.class, int.class});
						((C3P0ProxyStatement)callableStatement).rawStatementOperation(m, C3P0ProxyStatement.RAW_STATEMENT, new Object[] {++i, ficherosString, ficheros.size(), ficheros.size(), OracleTypes.VARCHAR, 0});
						callableStatement.setPoolable(Boolean.FALSE);
					} catch (Exception e) {
						logger.error("Error {}", e.getMessage());
					}
				}
				logger.debug("Invocando al procedimento: {} con parámetros {}, {}", procedure, directorio, ficherosString);
				callableStatement.executeUpdate();
			} else {
				logger.error("No se ha realizado la carga porque no hay ninguna conexión disponible");
			}

		} catch (SQLException e) {
			if (logger.isDebugEnabled()) {
				logger.error("ProceduresDAO.callOracleStoredProcedures: ", e);
			}
			throw e;
		} finally {

			if (callableStatement != null) {
				try {
					callableStatement.close();
				} catch (Exception e) {
				}
			}

			if (dbConnection != null) {
				try {
					dbConnection.close();
				} catch (Exception e) {
				}
			}
		}
	}
	
	private static Map<String, List<String>> getFicherosPorDirectorio(List<CargarFicheroDTO> params) {
		Map<String, List<String>> ficheros = new HashMap<String, List<String>>();
		for (CargarFicheroDTO cargarFicheroDTO : params) {
			if (ficheros.isEmpty()) {
				List<String> list = new ArrayList<String>();
				list.add(cargarFicheroDTO.getFich());
				ficheros.put(cargarFicheroDTO.getDir(), list);
			} else {
				if (ficheros.containsKey(cargarFicheroDTO.getDir())) {
					List<String> list = ficheros.get(cargarFicheroDTO.getDir());
					list.add(cargarFicheroDTO.getFich());
					ficheros.put(cargarFicheroDTO.getDir(), list);
				} else {
					List<String> list = new ArrayList<String>();
					list.add(cargarFicheroDTO.getFich());
					ficheros.put(cargarFicheroDTO.getDir(), list);
				}
			}
		}

		return ficheros;
	}
}
