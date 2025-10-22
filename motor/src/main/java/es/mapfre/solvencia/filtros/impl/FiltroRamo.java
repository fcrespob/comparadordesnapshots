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
// JBMARTA - PYAM0025 - INI
public class FiltroRamo extends FiltroString {

	// JBMARTA - PYAM0025 - INI
	// public static final String ID_FILTRO = "RAMO";
	public static final String ID_FILTRO = "02";
	private DatosGeneralesDao datosGeneralesDao = new DatosGeneralesDao();
	// JBMARTA - PYAM0025 - FIN

	@Override
	public String getNombreServicio() {
		return FiltroRamo.ID_FILTRO;
	}

	@Override
	public Set<UmicKey> filtrar(Set<UmicKey> umicSet, PartitionSet partitionSet, FichaProceso fichaProceso) {

		final ValueExtractor ramoExtractor = new PofExtractor(String.class, DatosGenerales.IND_KRAMO);
		final Filter filtroRamo = this.crearFiltroString(fichaProceso, ramoExtractor);

		Filter filtroAcumulado = null;
		if (umicSet == null) {
			filtroAcumulado = filtroRamo;
		} else {
			filtroAcumulado = new InKeySetFilter(filtroRamo, umicSet);
		}

		return this.datosGeneralesDao.keySet(filtroAcumulado);
	}
}
// JBMARTA - PYAM0025 - FIN