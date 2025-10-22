package es.mapfre.solvencia.filtros.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tangosol.net.partition.PartitionSet;
import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.extractor.PofExtractor;
import com.tangosol.util.filter.AndFilter;
import com.tangosol.util.filter.InKeySetFilter;
import com.tangosol.util.filter.NotFilter;
import com.tangosol.util.filter.OrFilter;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dao.impl.maestro.BaseTecnicaInicialDao;
import es.mapfre.solvencia.dao.impl.maestro.FechasDao;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.BaseTecnicaInicial;
import es.mapfre.solvencia.dominio.maestro.Fechas;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FiltroFichaProcesoAdicional;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.UtilFechas;

/**
 * 
 * @author indra Este filtro no permitirá la inclusión de diferentes valores de
 *         filtro. Es un filtro predefinido que seleccionará para el cálculo
 *         todas aquellas UMIC que cumplan las siguientes condiciones: • UMIC
 *         posteriores al Reglamento ROSSP. o En operaciones CASADAS, todas
 *         aquellas cuyo tipo de interés 2 sea superior al tipo de interés
 *         introducido por el usuario. o En operaciones NO CASADAS, todas
 *         aquellas cuyo tipo de interés 1 sea superior al tipo de interés
 *         introducido por el usuario.
 *
 * 
 */
 // JBMARTA - PYAM0025 - INI
public class FiltroTipoMaximoDGS extends FiltroAbstract {

	public static final String ID_FILTRO = "07";
	private FechasDao<UmicKey, Fechas> fechasDao = new FechasDao<UmicKey, Fechas>();
	private BaseTecnicaInicialDao<UmicKey, BaseTecnicaInicial> baseTecnicaInicialDao = new BaseTecnicaInicialDao<UmicKey, BaseTecnicaInicial>();

	@Override
	public String getNombreServicio() {
		return FiltroTipoMaximoDGS.ID_FILTRO;
	}

	@Override
	public Set<UmicKey> filtrar(final Set<UmicKey> umicSet, final PartitionSet partitionSet,
			final FichaProceso fichaProceso) {

		// Se ordena la ejecución para que se calculen primero las UMICs
		// principales.
		Set<UmicKey> listaClavesUmic = new HashSet<UmicKey>();
		final Integer kejecucion = fichaProceso.getKejecucion();

		for (FiltroFichaProcesoAdicional filtro : fichaProceso.getFiltrosAdicionales()) {

			Set<UmicKey> listaClavesUmicParcial = umicSet;
			
			// Filtramos por UMIC posteriores al Reglamento ROSSP
			final Filter filtroReglamentoROSSP = this.crearFiltroRossp(filtro);
			Filter filtroAcumulado = null;
			if (umicSet == null) {
				filtroAcumulado = filtroReglamentoROSSP;
			} else {
				filtroAcumulado = new InKeySetFilter(filtroReglamentoROSSP, umicSet);
			}
			listaClavesUmicParcial = this.fechasDao.keySet(filtroAcumulado);

			// Filtramos por casados/no casados y tipo de interes
			// Será casado/no casado en función de si la fecha de cierra cae en
			// el intervalo de fechas del tramo en cuestión
			final Timestamp fecCierre = this.getFecha(kejecucion);
			final Filter filtroCasadoNoCasadoInteres = this.crearFiltroCasadosNoCasadosTipoInteres(filtro, fecCierre);
			filtroAcumulado = new InKeySetFilter(filtroCasadoNoCasadoInteres, listaClavesUmicParcial);
			listaClavesUmicParcial = this.baseTecnicaInicialDao.keySet(filtroAcumulado);

			listaClavesUmic.addAll(listaClavesUmicParcial);
		}

		return listaClavesUmic;
	}

	private Timestamp getFecha(final Integer kejecucion) {

		final DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		String kejecucionString = kejecucion.toString();
		kejecucionString = kejecucionString.substring(0, 6);
		Timestamp fechaCierre = null;
		try {
			fechaCierre = new Timestamp(dateFormat.parse(kejecucionString).getTime());
			fechaCierre = UtilFechas.getUltimoDiaDelMes(fechaCierre);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fechaCierre;
	}

	private Filter crearFiltroRossp(final FiltroFichaProcesoAdicional filtroFichaProcesoAdicional)
			throws Solvencia2Excepcion {

		final ValueExtractor fecefeciniExtractor = new PofExtractor(Timestamp.class, Fechas.IND_FECINISUS);

		// FILTRO reglamento ROSSP
		final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Timestamp fechaRossp = null;
		try {
			fechaRossp = new Timestamp(dateFormat.parse(ConstantesSolvencia.CTE_FECHA_REGLAMENTO_ROSSP).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getFiltroGreaterTimestamp(fechaRossp, fecefeciniExtractor, true);
	}

	private Filter crearFiltroCasadosNoCasadosTipoInteres(final FiltroFichaProcesoAdicional filtroFichaProcesoAdicional,
			final Timestamp fecCierre) throws Solvencia2Excepcion {

		final ValueExtractor fIniRamo1Extractor = new PofExtractor(Timestamp.class,
				BaseTecnicaInicial.IND_FECINITRAMO1);
		final ValueExtractor fFinRamo1Extractor = new PofExtractor(Timestamp.class,
				BaseTecnicaInicial.IND_FECFINTRAMO1);
		final ValueExtractor swCasadosRamo1Extractor = new PofExtractor(String.class,
				BaseTecnicaInicial.IND_SWCASADOI1);
		final ValueExtractor pintertecnI2Extractor = new PofExtractor(BigDecimal.class,
				BaseTecnicaInicial.IND_PINTERTECNI2);
		final Filter filtroCasadosTramo1 = new AndFilter(
				new AndFilter(getFiltroGreaterTimestamp(fecCierre, fIniRamo1Extractor, false),
						getFiltroGreaterTimestamp(fecCierre, fFinRamo1Extractor, true)),
				getFiltroEqualsString(ConstantsModulos.CTE_S, swCasadosRamo1Extractor, true));
		final Filter filtroCasadosTramo1Interes = new AndFilter(filtroCasadosTramo1,
				getFiltroGreaterBigDecimal((filtroFichaProcesoAdicional.getGtipinteres()).divide(new BigDecimal(100)),
						pintertecnI2Extractor, true));

		final ValueExtractor pintertecnI1Extractor = new PofExtractor(BigDecimal.class,
				BaseTecnicaInicial.IND_PINTERTECNI1);
		final Filter filtroCasadosTramo2Interes = new AndFilter(new NotFilter(filtroCasadosTramo1),
				getFiltroGreaterBigDecimal((filtroFichaProcesoAdicional.getGtipinteres()).divide(new BigDecimal(100)),
						pintertecnI1Extractor, true));

		return new OrFilter(filtroCasadosTramo1Interes, filtroCasadosTramo2Interes);
	}
}
// JBMARTA - PYAM0025 - FIN