/**
 * 
 */
package es.mapfre.solvencia.open.dao.extraer;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.open.dao.ProceduresDAO;
import es.mapfre.solvencia.open.dto.ExtraerFicheroDTO;
import es.mapfre.solvencia.open.enums.TipoFichero;

/**
 * @author amdepedro
 * 
 */
public final class ProceduresExtraerDAO {

	private static Logger logger = LoggerFactory.getLogger(ProceduresExtraerDAO.class);

	/**
	 * 
	 */
	private ProceduresExtraerDAO() {

	}

	public static void callOracleStoredProceduresExtraer(TipoFichero tipoFichero, List<ExtraerFicheroDTO> params)
			throws SQLException {

		Map<String, List<ExtraerFicheroDTO>> ficheros = getFicherosPorDirectorioExtraer(params);
		for (Entry<String, List<ExtraerFicheroDTO>> entry : ficheros.entrySet()) {
			callOracleStoredProceduresExtraer(entry.getKey(), entry.getValue(), tipoFichero);
		}
	}

	private static Map<String, List<ExtraerFicheroDTO>> getFicherosPorDirectorioExtraer(List<ExtraerFicheroDTO> params) {
		Map<String, List<ExtraerFicheroDTO>> ficheros = new HashMap<String, List<ExtraerFicheroDTO>>();
		for (ExtraerFicheroDTO extraerFicheroDTO : params) {
			if (ficheros.isEmpty()) {
				List<ExtraerFicheroDTO> list = new ArrayList<ExtraerFicheroDTO>();
				list.add(extraerFicheroDTO);
				ficheros.put(extraerFicheroDTO.getDir(), list);
			} else {
				if (ficheros.containsKey(extraerFicheroDTO.getDir())) {
					List<ExtraerFicheroDTO> list = ficheros.get(extraerFicheroDTO.getDir());
					list.add(extraerFicheroDTO);
					ficheros.put(extraerFicheroDTO.getDir(), list);
				} else {
					List<ExtraerFicheroDTO> list = new ArrayList<ExtraerFicheroDTO>();
					list.add(extraerFicheroDTO);
					ficheros.put(extraerFicheroDTO.getDir(), list);
				}
			}
		}

		return ficheros;
	}

	// Genera los procedimientos para cada uno de los ficheros a extraer
	private static void callOracleStoredProceduresExtraer(String directorio, List<ExtraerFicheroDTO> ficheros,
			TipoFichero tipoFichero) throws SQLException {

		for (ExtraerFicheroDTO extraerFicheroDTO : ficheros) {
			callOracleStoredProcedures(directorio, tipoFichero, extraerFicheroDTO);
		}
	}

	private static void callOracleStoredProcedures(String directorio, TipoFichero tipoFichero, ExtraerFicheroDTO params)
			throws SQLException {

		Connection dbConnection = null;
		CallableStatement callableStatement = null;
		String procedure = ProceduresDAO.getProcedure(tipoFichero, Boolean.FALSE, null);
		try {
			dbConnection = ProceduresDAO.getDBConnection(Boolean.TRUE);
			if (dbConnection != null) {
				callableStatement = dbConnection.prepareCall(procedure);
				int i = 0;
				// Fecha de Cierre
				callableStatement.setDate(++i, params.getFechaCierre());
				// Negocio
				callableStatement.setString(++i, params.getNegocio());
				// Canal
				callableStatement.setInt(++i, params.getCanal());
				// Base Técnica
				callableStatement.setString(++i, params.getBt());
				// Ruta del directorio donde se creará el fichero
				callableStatement.setString(++i, params.getDir());
				// Nombre del fichero a crear
				callableStatement.setString(++i, params.getNombreFichero());
				callableStatement.executeUpdate();
			} else {
				logger.error("No se ha realizado la extracción porque no hay ninguna conexión disponible");
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

}
