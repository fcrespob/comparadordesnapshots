package es.mapfre.solvencia.filtros;

import java.util.ArrayList;
import java.util.Collections;
// JBMARTA - PYAM0025 - INI
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.tangosol.net.partition.PartitionSet;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
//JBMARTA - PYAM0025 - FIN

import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.filtros.impl.FiltroBaseTec;
import es.mapfre.solvencia.filtros.impl.FiltroModalidad;
import es.mapfre.solvencia.filtros.impl.FiltroNegocioCanal;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;

public class FiltroHelper {
	
	// JBMARTA - PYAM0025 - INI
	/**
	 * Método que obtiene el filtro que engloba a todos los filtros definidos en los registros
	 * de la ficha de proceso:
	 * 1) Filtro CNAL : Está presente en todas las fichas de proceso.
	 * 2) Filtros de tipo 2: Filtros MODA (aplicables sobre modalidad) si estuvieran presentes.
	 * 3) Filtros de tipo 3: Filtros para las bases técnicas de coaseguro.
	 * 4) Filtros de tipo 4: Filtros adicionales sobre otros ámbitos definidos, si estuvieran
	 * presentes.
	 * 
	 * @param partition
	 * @param fichaProceso
	 * @return
	 */
	public Set<UmicKey> filtrarUmics(final PartitionSet partition, final FichaProceso fichaProceso) {

		// Controlamos si se devuelve listaClavesUmics o listaClavesUmicParcial
		boolean parcial = false;
		
		// Se ordena la ejecución para que se calculen primero las UMICs
		// principales.
		Set<UmicKey> listaClavesUmic = new TreeSet<UmicKey>(umicsOrdered());
		Set<UmicKey> listaClavesUmicParcial = new TreeSet<UmicKey>();

		// FILTRO 01
		final Filtro filtroCNAL = FactoriaFiltros.getFiltro(FiltroNegocioCanal.ID_FILTRO);
		listaClavesUmic =  filtroCNAL.filtrar(null, partition, fichaProceso);
		
		List<UmicKey> listaUmics = new ArrayList<UmicKey>();
		listaUmics.addAll(listaClavesUmic);
		
		Collections.sort(listaUmics);
		
		Set<UmicKey> clavesUmic = new TreeSet<UmicKey>();
		
		for(int i = 0; i < listaUmics.size(); i++){
			
			clavesUmic.add(listaUmics.get(i));
			
			
		}
		
		// FILTRO 02
		if (!fichaProceso.getFiltrosAmbito().isEmpty()) {
			parcial = true;
			final Filtro filtroMODA = FactoriaFiltros.getFiltro(FiltroModalidad.ID_FILTRO);
			listaClavesUmicParcial = filtroMODA.filtrar(clavesUmic, partition, fichaProceso);
		}
		
		//FILTRO 03
		if (fichaProceso.getCtipobt().equals(ConstantsModulos.CTE_VAL_BTCOATF) ||
				fichaProceso.getCtipobt().equals(ConstantsModulos.CTE_VAL_BTCOA)   ||
				fichaProceso.getCtipobt().equals(ConstantsModulos.CTE_VAL_BELCOA)  ||
				fichaProceso.getCtipobt().equals(ConstantsModulos.CTE_VAL_MULTICOA)  ){
					Filtro filtroBt = FactoriaFiltros.getFiltro(FiltroBaseTec.ID_FILTRO);
					if (parcial) {
						listaClavesUmicParcial = filtroBt.filtrar(listaClavesUmicParcial, partition, fichaProceso);
					} else {
						listaClavesUmicParcial = filtroBt.filtrar(clavesUmic, partition, fichaProceso);
					}	
					parcial = true;
		}
		
		// FILTROS ADICIONALES
		if (!fichaProceso.getFiltrosAdicionales().isEmpty()) {
			Filtro filtroAdicional = FactoriaFiltros.getFiltro(fichaProceso.getFiltrosAdicionales().get(0).getCtipofiltro());
			if (parcial){
				listaClavesUmicParcial = filtroAdicional.filtrar(listaClavesUmicParcial, partition, fichaProceso);
			} else {
				listaClavesUmicParcial = filtroAdicional.filtrar(clavesUmic, partition, fichaProceso);
			}
			parcial = true;
		}
		
		if (parcial){
			return listaClavesUmicParcial;
		} else {
			return clavesUmic;
		}
	}

	protected Comparator<UmicKey> umicsOrdered() {
		return new Comparator<UmicKey>() {

			@Override
			public int compare(UmicKey uk1, UmicKey uk2) {
				if (uk1.getNorden() != null && uk2.getNorden() != null) {
					if (uk1.getNorden().compareTo(uk2.getNorden()) == 0) {
						return 1;
					} else {
						return uk1.getNorden().compareTo(uk2.getNorden());
					}
				}
				return 0;
			}

		};
	}
	// JBMARTA - PYAM0025 - FIN
}