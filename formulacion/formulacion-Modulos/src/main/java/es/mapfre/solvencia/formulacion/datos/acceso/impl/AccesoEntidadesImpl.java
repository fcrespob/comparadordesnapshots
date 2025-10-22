package es.mapfre.solvencia.formulacion.datos.acceso.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.formulacion.Periodo;
import es.mapfre.solvencia.dominio.formulacion.ResultadoFallecimiento;
import es.mapfre.solvencia.dominio.formulacion.ResultadoProvision;
import es.mapfre.solvencia.dominio.formulacion.ResultadoVida;
import es.mapfre.solvencia.formulacion.datos.acceso.CriteriosFiltrado;
import es.mapfre.solvencia.formulacion.datos.acceso.TipoEntidad;
import es.mapfre.solvencia.formulacion.gestion.AccesoEntidades;



/**
 * Implementacion del acceso a las entidades
 * @author agonzalezgar
 *
 */
public class AccesoEntidadesImpl implements AccesoEntidades {
	
	private static final Logger LOG = LoggerFactory.getLogger(AccesoEntidadesImpl.class);
	
	private final transient Map<String, Periodo[]> periodos = new HashMap<String, Periodo[]>();  
	private final transient Map<String, ResultadoVida> resultadosVida = new HashMap<String, ResultadoVida>();  
	private final transient Map<String, ResultadoFallecimiento> resulFallec = new HashMap<String, ResultadoFallecimiento>();  
	private final transient Map<String, ResultadoProvision> resulProvision = new HashMap<String, ResultadoProvision>();  

	/**
	 * Implementacion del metodo de obtener las entidades segun los filtros
	 */
	public Object obtener(final TipoEntidad tipoEntidad, final CriteriosFiltrado filtros) {
		Object resultado = null;
		switch (tipoEntidad) {
		case PERIODOS:
			resultado = this.periodos.get(filtros.getClave());
			break;
		case RES_VIDA:
			resultado = this.resultadosVida.get(filtros.getClave());
			break;
		case RES_FALL:
			resultado = this.resulFallec.get(filtros.getClave());
			break;
		case RES_PROV:
			resultado = this.resulProvision.get(filtros.getClave());
			break;
		default:
			AccesoEntidadesImpl.LOG.debug("Opción no contemplada");
		}
		
		return resultado;
	}

	/**
	 * Implementacion de la operacion de guardar las entidades
	 */
	public void guardar(final TipoEntidad tipoEntidad, final String clave, final Object valor) {
		switch (tipoEntidad) {
		case PERIODOS:
			this.periodos.put(clave, (Periodo[]) valor);
			break;
		case RES_VIDA:
			this.resultadosVida.put(clave, (ResultadoVida) valor);
			break;
		case RES_FALL:
			this.resulFallec.put(clave, (ResultadoFallecimiento) valor);
			break;
		case RES_PROV:
			this.resulProvision.put(clave, (ResultadoProvision) valor);
			break;
		default:
		}
	}

}