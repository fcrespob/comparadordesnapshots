package es.mapfre.solvencia.filtros.impl;

import java.util.Set;

import com.tangosol.net.partition.PartitionSet;
import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.extractor.PofExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.EqualsFilter;
import com.tangosol.util.filter.InKeySetFilter;
import com.tangosol.util.filter.PartitionedFilter;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dao.impl.maestro.DatosGeneralesDao;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.filtros.Filtro;

/**
 * 
 * Clase que recoge la implementación del filtro de ficha de proceso que actúa
 * sobre los campos cnegocio y ccanal de datos generales.
 * Este filtro es común a todas las fichas de proceso, y obtiene los datos para
 * su construcción del registro tipo1 (o cabecera) de la ficha de proceso.
 * 
 * @author indra
 *
 */
 // JBMARTA - PYAM0025 - INI
public class FiltroNegocioCanal implements Filtro {
	/**
	 * Identificador de filtro empleado para recuperar este tipo de filtro
	 * utilizando la factoría.
	 */
	public static final String ID_FILTRO = "CNAL";
	private DatosGeneralesDao datosGeneralesDao = new DatosGeneralesDao();

	@Override
	public String getNombreServicio() {
		return FiltroNegocioCanal.ID_FILTRO;
	}

	@Override
	public Set<UmicKey> filtrar(final Set<UmicKey> umicSet, final PartitionSet partitionSet,
			final FichaProceso fichaProceso) {

		/**
		 * Extractores utilizados para construir los filtros de coherence para
		 * este tipo de filtro.
		 */
		final ValueExtractor ccanalExtractor = new PofExtractor(Integer.class, DatosGenerales.IND_CCANAL);
		final ValueExtractor cnegocioExtractor = new PofExtractor(String.class, DatosGenerales.IND_CNEGOCIO);

		/**
		 * Si alguno de los valores no viene informado en la cabecera de la
		 * ficha de proceso, lanzamos una excepción
		 */
		if (fichaProceso.getCcanal() == null || fichaProceso.getCnegocio() == null) {
			throw Solvencia2ExcepcionHelper.crearExcepcion("F2");
		}
		/**
		 * Construcción de filtros que se engloban en un filtro conjunto,
		 * utilizando la AND de los mismos.
		 */
		final Filter ccanalFilter = new EqualsFilter(ccanalExtractor, fichaProceso.getCcanal());
		final Filter cnegocioFilter = new EqualsFilter(cnegocioExtractor, fichaProceso.getCnegocio());
		final Filter allFilter = new AllFilter(new Filter[] { ccanalFilter, cnegocioFilter });

		final Filter filterPart = new PartitionedFilter(allFilter, partitionSet);

		Filter filtroAcumulado = null;
		if (umicSet == null) {
			filtroAcumulado = filterPart;
		} else {
			filtroAcumulado = new InKeySetFilter(filterPart, umicSet);
		}

		return this.datosGeneralesDao.keySet(filtroAcumulado);
	}
}
// JBMARTA - PYAM0025 - FIN