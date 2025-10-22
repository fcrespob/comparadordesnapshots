package es.mapfre.solvencia.filtros.impl;

import java.util.Set;

import com.tangosol.net.partition.PartitionSet;
import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.extractor.PofExtractor;
import com.tangosol.util.filter.InKeySetFilter;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dao.impl.maestro.DatosGeneralesDao;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;

/**
 * 
 * Clase que recoge la implementación del filtro de ficha de proceso que actúa
 * sobre el campo kmodalidad de datos generales.
 * Los registros de filtros de tipo 2 de una ficha de proceso, de existir, siempre
 * serán de este tipo.
 * 
 * @author indra
 *
 */
 // JBMARTA - PYAM0025 - INI
public class FiltroModalidad extends FiltroInteger {

	/**
	 * Identificador de filtro empleado para recuperar este tipo de filtro
	 * utilizando la factoría.
	 */

	public static final String ID_FILTRO = "MODA";
	private DatosGeneralesDao datosGeneralesDao = new DatosGeneralesDao();

	@Override
	public String getNombreServicio() {
		return FiltroModalidad.ID_FILTRO;
	}

	@Override
	public Set<UmicKey> filtrar(Set<UmicKey> umicSet, PartitionSet partitionSet, FichaProceso fichaProceso) {

		/**
		 * Extractor utilizado para construir el filtro de coherence para este
		 * tipo de filtro de proceso.
		 */

		final ValueExtractor kmodalidadExtractor = new PofExtractor(Integer.class, DatosGenerales.IND_EKMODALIDAD);

		final Filter filtroModalidad = this.crearFiltroInteger(fichaProceso, kmodalidadExtractor);
		Filter filtroAcumulado = null;
		if (umicSet == null) {
			filtroAcumulado = filtroModalidad;
		} else {
			filtroAcumulado = new InKeySetFilter(filtroModalidad, umicSet);
		}

		return this.datosGeneralesDao.keySet(filtroAcumulado);
	}
}
// JBMARTA - PYAM0025 - FIN