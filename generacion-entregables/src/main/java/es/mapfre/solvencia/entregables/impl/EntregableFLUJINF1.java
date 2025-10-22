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
import es.mapfre.solvencia.dominio.entregables.FlujInf1;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.entregables.EntregableGenerico;
import es.mapfre.solvencia.entregables.util.ConstantsEntregables;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el cálculo del Entregable FLUJINF1.
 * 
 */
public class EntregableFLUJINF1 extends EntregableGenerico {

	final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();
	
	private static final Logger LOG = LoggerFactory.getLogger(EntregableFLUJINF1.class);

	private static final String CACHE_DETALLE_CORRIENTE_ENTREGABLES = ConstantsEntregables.CACHE_DETALLE_CORRIENTE_ENTREGABLES;

	@Override
	public String getNombreEntregable() {

		return ConstantsFactorias.ENTREGABLE_FLUJINF1;
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
				new PofExtractor(Integer.class,
						new SimplePofPath(
								new int[] { DetalleCorrienteEntregables.IND_UMICKEY, DatosGenerales.IND_EKMODALIDAD })),
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
				new BigDecimalSum(new PofExtractor(BigDecimal.class,
						DetalleCorrienteEntregables.IND_TOTALFLUJOPROYECCION_SUMFPROB)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class,
						DetalleCorrienteEntregables.IND_TOTALFLUJOPROYECCION_SUMPROVISION)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class,
						DetalleCorrienteEntregables.IND_TOTALFLUJOPROYECCION_SUMCOLA)), 
				new BigDecimalSum(new PofExtractor(BigDecimal.class,
						DetalleCorrienteEntregables.IND_BLOQUEFALL_IMPFLUJOPROBABLE)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class,
						DetalleCorrienteEntregables.IND_BLOQUEVIDA_IMPFLUJOPROBABLE)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class,
						DetalleCorrienteEntregables.IND_BLOQUECOMPL_IMPFLUJOPROBABLE)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class,
						DetalleCorrienteEntregables.IND_BLOQUEPRIM_IMPFLUJOPROBABLE)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class,
						DetalleCorrienteEntregables.IND_BLOQUEGTO_IMPFLUJOPROBABLE)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class,
						DetalleCorrienteEntregables.IND_BLOQUEFALL_IMPFLUJOACTUALIZADO)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class,
						DetalleCorrienteEntregables.IND_BLOQUEVIDA_IMPFLUJOACTUALIZADO)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class,
						DetalleCorrienteEntregables.IND_BLOQUECOMPL_IMPFLUJOACTUALIZADO)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class,
						DetalleCorrienteEntregables.IND_BLOQUEPRIM_IMPFLUJOACTUALIZADO)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class,
						DetalleCorrienteEntregables.IND_BLOQUEGTO_IMPFLUJOACTUALIZADO)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class,
						DetalleCorrienteEntregables.IND_BLOQUEGTOAD_IMPFLUJOPROBABLE)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class,
						DetalleCorrienteEntregables.IND_BLOQUEGTOAD_IMPFLUJOACTUALIZADO))
				};

		CompositeAggregator valuesAggregator = CompositeAggregator.createInstance(values);

		InvocableMap.EntryAggregator pivotAggregator = GroupAggregator.createInstance(multiExtractor, valuesAggregator);

		EqualsFilter isKbasetec = new EqualsFilter(new PofExtractor(String.class, DetalleCorrienteEntregables.IND_BT),
				kbasetec);

		Map<Object, Object> pivotResults = (Map<Object, Object>) detalleEntregables.aggregate(isKbasetec,
				pivotAggregator);

		this.setProgress(pivotResults.size(), 0);

		List<FlujInf1> flujinf1s = transformResults(pivotResults);

		// Almacenar en cache
		servicio.almacenarEntregableFlujInf1(flujinf1s);

		this.setProgress(pivotResults.size(), pivotResults.size());

	}

	/**
	 * Extraer los resultados de la agregación
	 * 
	 * @param pivotResults
	 * @return
	 */
	private List<FlujInf1> transformResults(Map<Object, Object> pivotResults) {
		List<FlujInf1> flujinf1s = new ArrayList<FlujInf1>();

		for (Entry<Object, Object> entry : pivotResults.entrySet()) {
			flujinf1s.add(transformEntry(new FlujInf1(), entry));
		}

		return flujinf1s;
	}

	/**
	 * Mapea cada entrada de los resultados a la cache flujinf1
	 * 
	 * @param flujInf1
	 * @param entry
	 * @return
	 */
	private FlujInf1 transformEntry(FlujInf1 flujInf1, Entry<Object, Object> entry) {

		List key = (List) entry.getKey();
		flujInf1.setBt((String) key.get(ConstantsFunciones.CTE_0));
		flujInf1.setFeccierre((Timestamp) key.get(ConstantsFunciones.CTE_1));
		flujInf1.setCnegocio((String) key.get(ConstantsFunciones.CTE_2));
		flujInf1.setCcanal((Integer) key.get(ConstantsFunciones.CTE_3));
		flujInf1.setKcarterainv((String) key.get(ConstantsFunciones.CTE_4));
		flujInf1.setGapact((String) key.get(ConstantsFunciones.CTE_5));
		flujInf1.setKramo((String) key.get(ConstantsFunciones.CTE_6));
		flujInf1.setKmodalidad((Integer) key.get(ConstantsFunciones.CTE_7));
		flujInf1.setFecdesde((Timestamp) key.get(ConstantsFunciones.CTE_8));
		flujInf1.setGestionit((String) key.get(ConstantsFunciones.CTE_9));
//INI-TAR00400971	
		flujInf1.setSpcom((String) key.get(ConstantsFunciones.CTE_10));
		flujInf1.setKmodext((Integer) key.get(ConstantsFunciones.CTE_11));
		flujInf1.setKbencon((String) key.get(ConstantsFunciones.CTE_12));
//FIN-TAR00400971		
		List values = (List) entry.getValue();
		flujInf1.setTotflujprov((BigDecimal) values.get(ConstantsFunciones.CTE_0));
		flujInf1.setTotflujact((BigDecimal) values.get(ConstantsFunciones.CTE_1));
		flujInf1.setTotprovi((BigDecimal) values.get(ConstantsFunciones.CTE_2));

		flujInf1.setTotflujprovpres(((BigDecimal)values.get(ConstantsFunciones.CTE_3))
				.add((BigDecimal)values.get(ConstantsFunciones.CTE_4))
				.add((BigDecimal)values.get(ConstantsFunciones.CTE_5)));
		flujInf1.setTotflujprovprim((BigDecimal)values.get(ConstantsFunciones.CTE_6));
		flujInf1.setTotflujprovgas((BigDecimal)values.get(ConstantsFunciones.CTE_7));
		flujInf1.setTotflujactpres(((BigDecimal)values.get(ConstantsFunciones.CTE_8))
				.add((BigDecimal)values.get(ConstantsFunciones.CTE_9))
				.add((BigDecimal)values.get(ConstantsFunciones.CTE_10)));
		flujInf1.setTotflujactprim((BigDecimal)values.get(ConstantsFunciones.CTE_11));
		flujInf1.setTotflujactgas((BigDecimal)values.get(ConstantsFunciones.CTE_12));
		
		flujInf1.setTotflujprovgasad((BigDecimal)values.get(ConstantsFunciones.CTE_13));
		flujInf1.setTotflujactgasad((BigDecimal)values.get(ConstantsFunciones.CTE_14));
		
		return flujInf1;
	}

}