package es.mapfre.scr.gastos.dao.services;

import java.util.Map;

import es.mapfre.scr.gastos.dao.Dao;
import es.mapfre.scr.gastos.services.FactoriaServicios;

public class FactoriaDao extends FactoriaServicios<Dao> {
		
	private static Map<String, Dao> daos = null;

	private static FactoriaDao instance;
	
	private FactoriaDao() {
		super(Dao.class);
	}

	static {
		instance = new FactoriaDao();
	}
	
	/**
	 * Devuelve un Dao en funci�n del identificador de cach� solicitado
	 * 
	 * @param idDao
	 * @return el dao buscado
	 */
	public static Dao getDao(String idDao) {
		return (Dao) instance.getServicio(idDao);
	}

	@Override
	protected Map<String, Dao> getServicios() {
		return FactoriaDao.daos;
	}
	
	@Override
	protected void setServicios(Map<String, Dao> daos) {
		FactoriaDao.daos = daos;
	}
}