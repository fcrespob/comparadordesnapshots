package es.mapfre.solvencia.filtros.impl;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.tangosol.net.partition.PartitionSet;
import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.extractor.PofExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.EqualsFilter;
import com.tangosol.util.filter.InKeySetFilter;
import com.tangosol.util.filter.LessFilter;
import com.tangosol.util.filter.NotEqualsFilter;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dao.impl.maestro.DatosCoaseguroDao;
import es.mapfre.solvencia.dao.impl.maestro.DatosGeneralesDao;
import es.mapfre.solvencia.dominio.maestro.DatosCoaseguro;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.filtros.Filtro;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
/**
 * 
 * Clase que recoge la implementación del filtro de ficha de proceso que actúa
 * sobre la Base Tecnica 
 * 
 * @author Szilard Toth
 *
 */
public class FiltroBaseTec implements Filtro {
	/**
	 *  Identificador de filtro empleado para recuperar este tipo de filtro utilizando la factoría.
	 */
	public static final String ID_FILTRO = "BT";
	private DatosCoaseguroDao<UmicKey, DatosCoaseguro> datosCoaseguroDao = new DatosCoaseguroDao<UmicKey, DatosCoaseguro>();
	private DatosGeneralesDao datosGeneralesDao = new DatosGeneralesDao();
	
	public Set<UmicKey> filtrar(Set<UmicKey> umicSet, PartitionSet partitionSet, FichaProceso fichaProceso) {
		
		Set<UmicKey> listaClavesUmic = new HashSet<UmicKey>();
		Set<UmicKey> listaClavesUmicParcial = umicSet;
		
		/**
		 * Extractores utilizados para construir los filtros de coherence para este tipo de filtro.
		 */
		ValueExtractor cnegocioExtractor = new PofExtractor(String.class, DatosGenerales.IND_CNEGOCIO);
		ValueExtractor kramoExtractor = new PofExtractor(String.class,DatosGenerales.IND_KRAMO);
		
		/**
		 * Construcción de filtros por ramo y negocio
		 */
		Filter cnegocioFilter = new EqualsFilter(cnegocioExtractor, ConstantsModulos.CTE_NEGOCIO_COLECTIVO);
		Filter kramo1Filter = new NotEqualsFilter(kramoExtractor, "117");
		Filter kramo2Filter = new NotEqualsFilter(kramoExtractor, "118");
		Filter kramo3Filter = new NotEqualsFilter(kramoExtractor, "122");
		Filter allFilter = new AllFilter(new Filter[]{cnegocioFilter,kramo1Filter,kramo2Filter,kramo3Filter});
		
		Filter filtroAcumulado = null;
		if (umicSet == null) {
			filtroAcumulado = allFilter;
		} else {
			filtroAcumulado = new InKeySetFilter(allFilter, umicSet);
		}
		
		listaClavesUmicParcial = this.datosGeneralesDao.keySet(filtroAcumulado);
		
		/**
		 * Filtramos por porcentaje de coaseguro
		 */
		ValueExtractor pcoasegExtractor = new PofExtractor(BigDecimal.class,DatosCoaseguro.IND_PCOASEG);
		Filter pcoasegFilter = new LessFilter(pcoasegExtractor, ConstantsFunciones.CTE_OPER_100);
		filtroAcumulado = new InKeySetFilter(pcoasegFilter, listaClavesUmicParcial);
		listaClavesUmicParcial = this.datosCoaseguroDao.keySet(filtroAcumulado);
		
		listaClavesUmic.addAll(listaClavesUmicParcial);
		
		return listaClavesUmic;
	}

	@Override
	public String getNombreServicio() {
		return FiltroBaseTec.ID_FILTRO;
	}

}