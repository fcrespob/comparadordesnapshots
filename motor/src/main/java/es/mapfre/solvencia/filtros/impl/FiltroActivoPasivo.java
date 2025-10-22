package es.mapfre.solvencia.filtros.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.tangosol.net.partition.PartitionSet;
import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.extractor.PofExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.InKeySetFilter;
import com.tangosol.util.filter.NotFilter;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dao.impl.maestro.DatosGeneralesDao;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FiltroFichaProcesoAdicional;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;

// JBMARTA - PYAM0025 - INI
public class FiltroActivoPasivo extends FiltroInteger{

	// JBMARTA - PYAM0025 - INI
	// public static final String ID_FILTRO = "KGAP";
	public static final String ID_FILTRO = "01";
	private DatosGeneralesDao datosGeneralesDao = new DatosGeneralesDao();
	// JBMARTA - PYAM0025 - FIN

	@Override
	public String getNombreServicio() {
		return FiltroActivoPasivo.ID_FILTRO;
	}
	
	private Filter crearFiltro(final FichaProceso fichaProceso, final ValueExtractor gapActExtractor,
			final ValueExtractor carteraInversionExtractor) throws Solvencia2Excepcion {

		validarParametros(fichaProceso);

		String inclusivo = null;

		final List<Filter> filtros = new ArrayList<Filter>();

		Boolean incluyente = false;
		String cartInv = null;

		for (FiltroFichaProcesoAdicional filtro : fichaProceso.getFiltrosAdicionales()) {

			Filter allFilterFicha = null;

			// El valor del campo incluyente/excluyente ha de ser el mismo para
			// todos los filtros de un mismo tipo (ámbito/tipo2 o
			// adicionales/tipo3)
			if (inclusivo == null) {
				inclusivo = filtro.getGclasefil();
				incluyente = inclusivo.equalsIgnoreCase(ConstantesSolvencia.INCLUSIVO_ADICIONAL);
			} else {
				if (!inclusivo.equalsIgnoreCase(filtro.getGclasefil())) {
					throw Solvencia2ExcepcionHelper.crearExcepcion(
							ConstantesSolvencia.CTE_FILTRO_VALOR_INC_EXC_NO_HOMOGENEO,
							new Object[] { inclusivo, filtro.getGclasefil() });
				}
			}
			// para cada extractor se crea un nuevo filtro
			if (filtro.getGoperdesde() != null && filtro.getGopdesdegar() != null) {
				// Filtro gap - cartera
				// En caso de realizar filtro por ambos,
				//sera obligatorio que la cartera de inversion sea la misma para los posibles filtros que se definan
				if (cartInv == null) {
					cartInv = filtro.getValordesde();
				} else {
					if (!cartInv.equals(filtro.getValordesde())) {
						throw Solvencia2ExcepcionHelper.crearExcepcion(
								ConstantesSolvencia.CTE_FILTRO_GAP_CARTERA_GAP_NO_HOMOGENEO,
								new Object[] { cartInv, filtro.getValordesde() });
					}
				}
				final Filter fitrActivoPasivo = createAndFilter(
						getFiltroStringCI(filtro, carteraInversionExtractor, true),
						getFiltroStringGAP(filtro, gapActExtractor, true)
						);
				if (incluyente) {
					filtros.add(fitrActivoPasivo);
				} else {
					filtros.add(new NotFilter(fitrActivoPasivo));
				}
			} else if (filtro.getGoperdesde() != null) {
				// Filtro sólo gap
				filtros.add(getFiltroStringCI(filtro, carteraInversionExtractor, incluyente));
			} else{
				// Filtro sólo cartera inversión
				filtros.add(getFiltroStringGAP(filtro, gapActExtractor, incluyente));
			}

		}

		Filter allFilter = null;
		// Se tiene en cuenta si los valores a filtrar son excluyentes o
		// incluyentes (un sólo tipo) a la hora de crear el filtro conjunto con
		// los filtros de cada registro.
		if (incluyente) {
			for (Filter filtro : filtros) {
				allFilter = createOrFilter(allFilter, filtro);
			}
		} else {
			Filter[] arrayFiltros = (Filter[]) filtros.toArray(new Filter[0]);
			allFilter = new AllFilter(arrayFiltros);
		}
		return allFilter;
	}
	
	/*
	 * Chequeamos que los dos valores por los que se filtra vengan rellenos
	 */
	private void validarParametros(final FichaProceso fichaProceso) {

		for (FiltroFichaProcesoAdicional filtroAdicional : fichaProceso.getFiltrosAdicionales()) {
			if (filtroAdicional.getGoperdesde() == null && filtroAdicional.getGopdesdegar() == null) {
				// TODO hay que dar de alta este error en bbdd
				throw Solvencia2ExcepcionHelper
						.crearExcepcion(ConstantesSolvencia.CTE_FILTRO_MOD_GARANTIA_PARAM_NO_INFORMADO);
			}
		}
	}

	@Override
	public Set<UmicKey> filtrar(Set<UmicKey> umicSet, PartitionSet partitionSet, FichaProceso fichaProceso) {

		/**
		 * Extractor utilizado para construir el filtro de coherence para este
		 * tipo de filtro de proceso.
		 */
		final ValueExtractor gapActExtractor = new PofExtractor(String.class, DatosGenerales.IND_GAPACT);
		final ValueExtractor carteraInversionExtractor = new PofExtractor(String.class, DatosGenerales.IND_KCARTERAINV);
		final Filter filtroActivoPasivo = this.crearFiltro(fichaProceso, gapActExtractor, carteraInversionExtractor);

		Filter filtroAcumulado = null;
		if (umicSet == null) {
			filtroAcumulado = filtroActivoPasivo;
		} else {
			filtroAcumulado = new InKeySetFilter(filtroActivoPasivo, umicSet);
		}

		return this.datosGeneralesDao.keySet(filtroAcumulado);
	}
}
// JBMARTA - PYAM0025 - FIN