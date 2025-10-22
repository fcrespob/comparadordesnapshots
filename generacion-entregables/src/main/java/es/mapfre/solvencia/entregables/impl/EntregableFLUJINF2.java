/* MODIFICACION:TAR00400971-NECESIDADES NUEVO SISTEMA DE PROCESOS TÉCNICOS 
   FECHA: 17/12/2018 Se incluye campo KBENCON,SPCOM, y KMODEXT
   AUTOR: INDRA
*/

package es.mapfre.solvencia.entregables.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.coherence.patterns.processing.task.TaskExecutionEnvironment;
import com.tangosol.io.pof.reflect.SimplePofPath;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.InvocableMap;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.aggregator.BigDecimalSum;
import com.tangosol.util.aggregator.CompositeAggregator;
import com.tangosol.util.aggregator.GroupAggregator;
import com.tangosol.util.extractor.MultiExtractor;
import com.tangosol.util.extractor.PofExtractor;
import com.tangosol.util.filter.EqualsFilter;

import es.mapfre.solvencia.dominio.entregables.DetalleCorrienteEntregables;
import es.mapfre.solvencia.dominio.entregables.FlujInf2;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.entregables.EntregableGenerico;
import es.mapfre.solvencia.entregables.util.ConstantsEntregables;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el cálculo del Entregable FLUJINF2.
 * 
 */
public class EntregableFLUJINF2 extends EntregableGenerico  {
	
	final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();
	
	private static final Logger LOG = LoggerFactory.getLogger(EntregableFLUJINF2.class);
	
	private static final String CACHE_DETALLE_CORRIENTE_ENTREGABLES = ConstantsEntregables.CACHE_DETALLE_CORRIENTE_ENTREGABLES;

	@Override
	public String getNombreEntregable() {
		
		return ConstantsFactorias.ENTREGABLE_FLUJINF2;
	}
	

	@Override
	public void execute(String kbasetec, TaskExecutionEnvironment oEnvironment) {
		this.initProgress(oEnvironment);
		
		NamedCache detalleEntregables = CacheFactory.getCache(CACHE_DETALLE_CORRIENTE_ENTREGABLES);
		
		ValueExtractor[] rows = new ValueExtractor[] {
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_BT),
				new PofExtractor(Timestamp.class, DetalleCorrienteEntregables.IND_FCIERRE),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_CNEGOCIO),
				new PofExtractor(Integer.class, DetalleCorrienteEntregables.IND_CCANAL),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_KCARTERAINV),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_GAPACT),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_KRAMO),
				new PofExtractor(Integer.class, new SimplePofPath(new int [] { DetalleCorrienteEntregables.IND_UMICKEY, 
			            DatosGenerales.IND_EKMODALIDAD })),
			    new PofExtractor(Integer.class, new SimplePofPath(new int [] { DetalleCorrienteEntregables.IND_UMICKEY,
			    		DatosGenerales.IND_EKGARANTIA })),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_PRESTCAL),
				new PofExtractor(Boolean.class, DetalleCorrienteEntregables.IND_NUEVAPRODUC),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_SEGMENTO1),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_TIPOSUBRIESGO),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_CURVATI),
				new PofExtractor(Timestamp.class, DetalleCorrienteEntregables.IND_FECHADESDE),
//INI-TAR00400971	
//				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_GESTIONIT)};
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_GESTIONIT),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_SPCOM),
				new PofExtractor(Integer.class, DetalleCorrienteEntregables.IND_KMODEXT),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_KBENCON)};
//FIN-TAR00400971					
		
		
		ValueExtractor multiExtractor = new MultiExtractor(rows);
		
		InvocableMap.EntryAggregator[] values = new InvocableMap.EntryAggregator[] {
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_TOTALFLUJOPROYECCION_SUMFPROBTANUL)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_TOTALFLUJOPROYECCION_SUMPROVISION)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_TOTALFLUJOPROYECCION_SUMCOLA))
		};
		
		CompositeAggregator valuesAggregator = CompositeAggregator.createInstance(values);
				
		InvocableMap.EntryAggregator pivotAggregator = GroupAggregator.createInstance(multiExtractor, valuesAggregator);
				
		EqualsFilter isKbasetec = new EqualsFilter(new PofExtractor(String.class, DetalleCorrienteEntregables.IND_BT), kbasetec);
				
		Map<Object, Object> pivotResults = (Map<Object, Object>) detalleEntregables.aggregate(isKbasetec, pivotAggregator);
		
		this.setProgress(pivotResults.size(), 0);

		List<FlujInf2> flujinf2s = transformResults(pivotResults);
		
		// Almacenar en cache
		servicio.almacenarEntregableFlujInf2(flujinf2s);
		
		this.setProgress(pivotResults.size(), pivotResults.size());

	}

	/**
	 * Extraer los resultados de la agregación
	 * @param pivotResults
	 * @return
	 */
	private List<FlujInf2> transformResults(Map<Object, Object> pivotResults) {
		List<FlujInf2> flujinf2s= new ArrayList<FlujInf2>();
		
		for (Entry<Object, Object> entry : pivotResults.entrySet()) {
			flujinf2s.add(transformEntry(new FlujInf2(), entry));			
		}
		
		return flujinf2s;
	}


	/**
	 * Mapea cada entrada de los resultados a la cache flujinf1
	 * @param flujInf1
	 * @param entry
	 * @return
	 */
	private FlujInf2 transformEntry(FlujInf2 flujInf2, Entry<Object, Object> entry) {

		List key = (List) entry.getKey();
		flujInf2.setBt((String) key.get(ConstantsFunciones.CTE_0));
		flujInf2.setFeccierre((Timestamp) key.get(ConstantsFunciones.CTE_1));
		flujInf2.setCnegocio((String) key.get(ConstantsFunciones.CTE_2));
		flujInf2.setCcanal((Integer) key.get(ConstantsFunciones.CTE_3));
		flujInf2.setKcarterainv((String) key.get(ConstantsFunciones.CTE_4));
		flujInf2.setGapact((String) key.get(ConstantsFunciones.CTE_5));
		flujInf2.setKramo((String) key.get(ConstantsFunciones.CTE_6));
		flujInf2.setKmodalidad((Integer) key.get(ConstantsFunciones.CTE_7));
		flujInf2.setKgarantia((Integer) key.get(ConstantsFunciones.CTE_8));
		flujInf2.setKprestcal((String) key.get(ConstantsFunciones.CTE_9));
		if (key.get(ConstantsFunciones.CTE_10) != null && (boolean) key.get(ConstantsFunciones.CTE_10)) {
			flujInf2.setNuevaproduc(ConstantsEntregables.CTE_NUEVAPRODUC_NP);
		} else {
			flujInf2.setNuevaproduc(ConstantsEntregables.CTE_NUEVAPRODUC_CA);			
		}
		
		flujInf2.setSegmento1((String) key.get(ConstantsFunciones.CTE_11));
		flujInf2.setTiposubriesgo((String) key.get(ConstantsFunciones.CTE_12));
		flujInf2.setCurvati((String) key.get(ConstantsFunciones.CTE_13));
		flujInf2.setFecdesde((Timestamp) key.get(ConstantsFunciones.CTE_14));
		flujInf2.setGestionit((String) key.get(ConstantsFunciones.CTE_15));
		//INI-TAR00400971	
		flujInf2.setSpcom((String) key.get(ConstantsFunciones.CTE_16));
		flujInf2.setKmodext((Integer) key.get(ConstantsFunciones.CTE_17));
		flujInf2.setKbencon((String) key.get(ConstantsFunciones.CTE_18));
		//FIN-TAR00400971	

		
		List values = (List) entry.getValue();
		flujInf2.setTotflujprov((BigDecimal) values.get(ConstantsFunciones.CTE_0));
		flujInf2.setTotflujact((BigDecimal) values.get(ConstantsFunciones.CTE_1));
		flujInf2.setTotprovi((BigDecimal) values.get(ConstantsFunciones.CTE_2));
		
		return flujInf2;
	}
	
}