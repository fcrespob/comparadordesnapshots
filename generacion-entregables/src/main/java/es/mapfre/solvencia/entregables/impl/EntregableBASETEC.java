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
import com.tangosol.util.aggregator.BigDecimalAverage;
import com.tangosol.util.aggregator.CompositeAggregator;
import com.tangosol.util.aggregator.GroupAggregator;
import com.tangosol.util.extractor.MultiExtractor;
import com.tangosol.util.extractor.PofExtractor;
import com.tangosol.util.filter.EqualsFilter;

import es.mapfre.solvencia.dominio.entregables.Basetec;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.entregables.EntregableGenerico;
import es.mapfre.solvencia.entregables.aggregators.BasetecPgastExtAggregator;
import es.mapfre.solvencia.entregables.aggregators.BasetecTablaCalc1CurvaTiAggregator;
import es.mapfre.solvencia.entregables.aggregators.ListBigDecimalAverageAggregator;
import es.mapfre.solvencia.entregables.aggregators.ListBigDecimalMaxAggregator;
import es.mapfre.solvencia.entregables.aggregators.ListBigDecimalMinAggregator;
import es.mapfre.solvencia.entregables.aggregators.NotCeroBigDecimalAverageAggregator;
import es.mapfre.solvencia.entregables.util.ConstantsEntregables;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el cálculo del Entregable BASETEC.
 * 
 */
public class EntregableBASETEC extends EntregableGenerico {

	final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();

	private static final Logger LOG = LoggerFactory.getLogger(EntregableBASETEC.class);
	private static final String CACHE_DETALLE_BASETECNICA = ConstantsEntregables.CACHE_DETALLE_BASETECNICA;

	private final IObtenerConfiguracion servicioConfiguracion = FachadaServicios.getObtenerConfiguracion();

	@Override
	public String getNombreEntregable() {
		return ConstantsFactorias.ENTREGABLE_BASETEC;
	}

	@Override
	public void execute(String kbasetec, TaskExecutionEnvironment oEnvironment) {
		this.initProgress(oEnvironment);
		
		NamedCache detalleBT = CacheFactory.getCache(CACHE_DETALLE_BASETECNICA);

		// detalleBaseTecnica cache extractors
		// La agrupación se realiza a nivel modalidad-garantia
		ValueExtractor[] rows = new ValueExtractor[] { new PofExtractor(String.class, DetalleBaseTecnica.IND_BASETEC),
				new PofExtractor(Timestamp.class, DetalleBaseTecnica.IND_FECCIERRE),
				new PofExtractor(String.class, DetalleBaseTecnica.IND_CNEGOCIO),
				new PofExtractor(Integer.class, DetalleBaseTecnica.IND_CCANAL),
				new PofExtractor(String.class, DetalleBaseTecnica.IND_KRAMO),
				new PofExtractor(Integer.class,
						new SimplePofPath(
								new int[] { DetalleBaseTecnica.IND_UMICKEY, DatosGenerales.IND_EKMODALIDAD })),
				new PofExtractor(Integer.class,
						new SimplePofPath(new int[] { DetalleBaseTecnica.IND_UMICKEY, DatosGenerales.IND_EKGARANTIA })),
				new PofExtractor(BigDecimal.class, DetalleBaseTecnica.IND_FACTOR1),
				new PofExtractor(List.class, DetalleBaseTecnica.IND_FACTOR2), };

		// Rows
		ValueExtractor multiExtractor = new MultiExtractor(rows);

		InvocableMap.EntryAggregator[] values;

		if (kbasetec.equals(ConstantsEntregables.CTE_BT_BEL) || 
				kbasetec.equals(ConstantsEntregables.CTE_BT_BELCOA) || 
				kbasetec.equals(ConstantsEntregables.CTE_BT_BELCLR) || 
				kbasetec.equals(ConstantsEntregables.CTE_BT_SCRTIU) || 
				kbasetec.equals(ConstantsEntregables.CTE_BT_SCRTID) || 
				kbasetec.equals(ConstantsEntregables.CTE_BT_SCRGTO) || 
				kbasetec.equals(ConstantsEntregables.CTE_BT_SCRMFE) || 
				kbasetec.equals(ConstantsEntregables.CTE_BT_SCRMMI) || 
				kbasetec.equals(ConstantsEntregables.CTE_BT_SCRMCF) || 
				kbasetec.equals(ConstantsEntregables.CTE_BT_SCRMCI) || 
				kbasetec.equals(ConstantsEntregables.CTE_BT_SCRLFE) || 
				kbasetec.equals(ConstantsEntregables.CTE_BT_SCRLMI) || 
				kbasetec.equals(ConstantsEntregables.CTE_BT_SCRINC) || 
				kbasetec.equals(ConstantsEntregables.CTE_BT_SCRVM)  || 
				kbasetec.equals(ConstantsEntregables.CTE_BT_SCRAEP) || 
				kbasetec.equals(ConstantsEntregables.CTE_BT_SCRAEN) || 
				kbasetec.equals(ConstantsEntregables.CTE_BT_SCRAIP) || 
				kbasetec.equals(ConstantsEntregables.CTE_BT_SCRAIN) || 
				kbasetec.equals(ConstantsEntregables.CTE_BT_SCRANM)) {

			values = new InvocableMap.EntryAggregator[] {
					new BigDecimalAverage(new PofExtractor(BigDecimal.class, DetalleBaseTecnica.IND_GTO_UNI)), // modalidad
																												// 221
					new NotCeroBigDecimalAverageAggregator(
							new PofExtractor(BigDecimal.class, DetalleBaseTecnica.IND_GTOROSSP_CAP)),
					new NotCeroBigDecimalAverageAggregator(
							new PofExtractor(BigDecimal.class, DetalleBaseTecnica.IND_GTOROSSP_PRIMA)),
					new BasetecPgastExtAggregator(),
					new BigDecimalAverage(new PofExtractor(BigDecimal.class, DetalleBaseTecnica.IND_GTO_PROV)),
					new ListBigDecimalMaxAggregator(ConstantsFunciones.CTE_0,
							new PofExtractor(List.class, DetalleBaseTecnica.IND_ITCALC)),
					new ListBigDecimalMinAggregator(ConstantsFunciones.CTE_0,
							new PofExtractor(List.class, DetalleBaseTecnica.IND_ITCALC)),
					new ListBigDecimalAverageAggregator(ConstantsFunciones.CTE_0,
							new PofExtractor(List.class, DetalleBaseTecnica.IND_ITCALC)),
					new ListBigDecimalMaxAggregator(ConstantsFunciones.CTE_1,
							new PofExtractor(List.class, DetalleBaseTecnica.IND_ITCALC)),
					new ListBigDecimalMinAggregator(ConstantsFunciones.CTE_1,
							new PofExtractor(List.class, DetalleBaseTecnica.IND_ITCALC)),
					new ListBigDecimalAverageAggregator(ConstantsFunciones.CTE_1,
							new PofExtractor(List.class, DetalleBaseTecnica.IND_ITCALC)),
					new BasetecTablaCalc1CurvaTiAggregator(
							new PofExtractor(List.class, DetalleBaseTecnica.IND_TABLA_BASEEXP)),
					new BasetecTablaCalc1CurvaTiAggregator(
							new PofExtractor(String.class, DetalleBaseTecnica.IND_CURVA_TI)),
					new BasetecTablaCalc1CurvaTiAggregator(
							new PofExtractor(String.class, DetalleBaseTecnica.IND_TABLA_TANUL)) 
			};

		} else {

			values = new InvocableMap.EntryAggregator[] {
					new BigDecimalAverage(new PofExtractor(BigDecimal.class, DetalleBaseTecnica.IND_GTO_UNI)),
					new NotCeroBigDecimalAverageAggregator(
							new PofExtractor(BigDecimal.class, DetalleBaseTecnica.IND_GTOROSSP_CAP)),
					new NotCeroBigDecimalAverageAggregator(
							new PofExtractor(BigDecimal.class, DetalleBaseTecnica.IND_GTOROSSP_PRIMA)),
					new BasetecPgastExtAggregator(),
					new BigDecimalAverage(new PofExtractor(BigDecimal.class, DetalleBaseTecnica.IND_GTO_PROV)),
					new ListBigDecimalMaxAggregator(ConstantsFunciones.CTE_0,
							new PofExtractor(List.class, DetalleBaseTecnica.IND_ITCALC)),
					new ListBigDecimalMinAggregator(ConstantsFunciones.CTE_0,
							new PofExtractor(List.class, DetalleBaseTecnica.IND_ITCALC)),
					new ListBigDecimalAverageAggregator(ConstantsFunciones.CTE_0,
							new PofExtractor(List.class, DetalleBaseTecnica.IND_ITCALC)),
					new ListBigDecimalMaxAggregator(ConstantsFunciones.CTE_1,
							new PofExtractor(List.class, DetalleBaseTecnica.IND_ITCALC)),
					new ListBigDecimalMinAggregator(ConstantsFunciones.CTE_1,
							new PofExtractor(List.class, DetalleBaseTecnica.IND_ITCALC)),
					new ListBigDecimalAverageAggregator(ConstantsFunciones.CTE_1,
							new PofExtractor(List.class, DetalleBaseTecnica.IND_ITCALC)),
					new BasetecTablaCalc1CurvaTiAggregator(
							new PofExtractor(String.class, DetalleBaseTecnica.IND_TABLACALC1ASEG1)),
					new BasetecTablaCalc1CurvaTiAggregator(
							new PofExtractor(String.class, DetalleBaseTecnica.IND_CURVA_TI)),
					new BasetecTablaCalc1CurvaTiAggregator(
							new PofExtractor(String.class, DetalleBaseTecnica.IND_TABLA_TANUL)) 
					};
		}
		// Create Values Aggregator
		CompositeAggregator valuesAggregator = CompositeAggregator.createInstance(values);

		// Create Entry Aggregator
		InvocableMap.EntryAggregator pivotAggregator = GroupAggregator.createInstance(multiExtractor, valuesAggregator);

		// Filtrar entradas por kbasetec
		EqualsFilter isKbasetec = new EqualsFilter(new PofExtractor(String.class, DetalleBaseTecnica.IND_BASETEC),
				kbasetec);

		// Do the query
		Map<Object, Object> pivotResults = (Map<Object, Object>) detalleBT.aggregate(isKbasetec, pivotAggregator);

		this.setProgress(pivotResults.size(), 0);
		
		List<Basetec> basetecs = transformResults(kbasetec, pivotResults);

		// Almacenar en cache
		servicio.almacenarEntregableBasetec(basetecs);

		this.setProgress(pivotResults.size(), pivotResults.size());		
	}

	/**
	 * Extrae los resultados de la agregación
	 * 
	 * @param kbasetec
	 * @param pivotResults
	 * @return
	 */
	private List<Basetec> transformResults(String kbasetec, Map<Object, Object> pivotResults) {
		List<Basetec> basetecs = new ArrayList<Basetec>();

		for (Entry<Object, Object> entry : pivotResults.entrySet()) {
			basetecs.add(transformEntry(kbasetec, new Basetec(), entry));
		}

		return basetecs;
	}

	/**
	 * Mapea cada entrada de los resultados de la agregación a la cache de
	 * Basetec
	 * 
	 * @param kbasetec
	 * @param basetec
	 * @param entry
	 * @return
	 */
	private Basetec transformEntry(String kbasetec, Basetec basetec, Entry<Object, Object> entry) {

		// Obtención de los valores sobre los que se realizó la agregación
		List key = (List) entry.getKey();
		basetec.setBt((String) key.get(ConstantsFunciones.CTE_0));
		basetec.setFcierre((Timestamp) key.get(ConstantsFunciones.CTE_1));
		basetec.setCnegocio((String) key.get(ConstantsFunciones.CTE_2));
		basetec.setCcanal((Integer) key.get(ConstantsFunciones.CTE_3));
		basetec.setKramo((String) key.get(ConstantsFunciones.CTE_4));
		basetec.setKmodalidad((Integer) key.get(ConstantsFunciones.CTE_5));
		basetec.setKgarantia((Integer) key.get(ConstantsFunciones.CTE_6));
		basetec.setFactor1((BigDecimal) key.get(ConstantsFunciones.CTE_7));
		List<BigDecimal> factor2 = (List<BigDecimal>) key.get(ConstantsFunciones.CTE_8);
		basetec.setIndfactor2(factor2Value(factor2));

		basetec.setIpc(servicioConfiguracion.recuperarIpcFuturo((Timestamp) key.get(ConstantsFunciones.CTE_1), (String) key.get(ConstantsFunciones.CTE_0)));

		// Obtención de los resultados de la agregación
		List value = (List) entry.getValue();
		basetec.setGastreal((BigDecimal) value.get(ConstantsFunciones.CTE_4));
		basetec.setGastrealunitario((BigDecimal) value.get(ConstantsFunciones.CTE_0));

		if ((!basetec.getBt().equals(ConstantsEntregables.CTE_BT_BEL)) && (!basetec.getBt().equals(ConstantsEntregables.CTE_BT_BELCOA)) && (!basetec.getBt().equals(ConstantsEntregables.CTE_BT_BELCLR))) {
			basetec.setPgastgesin1((BigDecimal) value.get(ConstantsFunciones.CTE_1));
			basetec.setPgastgesin2((BigDecimal) value.get(ConstantsFunciones.CTE_2));
			basetec.setPgastgesex((BigDecimal) value.get(ConstantsFunciones.CTE_3));
			basetec.setPintertecn1max((BigDecimal) value.get(ConstantsFunciones.CTE_5));
			basetec.setPintertecn1min((BigDecimal) value.get(ConstantsFunciones.CTE_6));
			basetec.setPintertecn1med((BigDecimal) value.get(ConstantsFunciones.CTE_7));
			basetec.setPintertecn2max((BigDecimal) value.get(ConstantsFunciones.CTE_8));
			basetec.setPintertecn2min((BigDecimal) value.get(ConstantsFunciones.CTE_9));
			basetec.setPintertecn2med((BigDecimal) value.get(ConstantsFunciones.CTE_10));
		}
		
		List tablaRepre = (List) value.get(ConstantsFunciones.CTE_11);
		List tablasOrdenadas = ordenarTablas(tablaRepre);

		if (tablasOrdenadas.size() > ConstantsFunciones.CTE_0) {
			basetec.setTablarepre1((String) tablasOrdenadas.get(ConstantsFunciones.CTE_0));
			basetec.setPorcprovrepre1((BigDecimal) tablasOrdenadas.get(ConstantsFunciones.CTE_1));
		}
		if (tablasOrdenadas.size() > ConstantsFunciones.CTE_2) {
			basetec.setTablarepre2((String) tablasOrdenadas.get(ConstantsFunciones.CTE_2));
			basetec.setPorcprovrepre2((BigDecimal) tablasOrdenadas.get(ConstantsFunciones.CTE_3));
		}
		if (tablasOrdenadas.size() > ConstantsFunciones.CTE_4) {
			basetec.setTablarepre3((String) tablasOrdenadas.get(ConstantsFunciones.CTE_4));
			basetec.setPorcprovrepre3((BigDecimal) tablasOrdenadas.get(ConstantsFunciones.CTE_5));
		}

		List curvaTi = (List) value.get(ConstantsFunciones.CTE_12);
		if (curvaTi.size() > ConstantsFunciones.CTE_0) {
			basetec.setCurvati1((String) curvaTi.get(ConstantsFunciones.CTE_0));
			basetec.setPorcprovcurvati1((BigDecimal) curvaTi.get(ConstantsFunciones.CTE_1));
		}
		if (curvaTi.size() > ConstantsFunciones.CTE_2) {
			basetec.setCurvati2((String) curvaTi.get(ConstantsFunciones.CTE_2));
			basetec.setPorcprovcurvati2((BigDecimal) curvaTi.get(ConstantsFunciones.CTE_3));
		}
		if (curvaTi.size() > ConstantsFunciones.CTE_4) {
			basetec.setCurvati3((String) curvaTi.get(ConstantsFunciones.CTE_4));
			basetec.setPorcprovcurvati3((BigDecimal) curvaTi.get(ConstantsFunciones.CTE_5));
		}

		List tablaTanul = (List) value.get(ConstantsFunciones.CTE_13);
		if (tablaTanul.size() > ConstantsFunciones.CTE_0) {
			basetec.setTablatanul1((String) tablaTanul.get(ConstantsFunciones.CTE_0));
			basetec.setPorcprovtanul1((BigDecimal) tablaTanul.get(ConstantsFunciones.CTE_1));
		}
		if (tablaTanul.size() > ConstantsFunciones.CTE_2) {
			basetec.setTablatanul2((String) tablaTanul.get(ConstantsFunciones.CTE_2));
			basetec.setPorcprovtanul2((BigDecimal) tablaTanul.get(ConstantsFunciones.CTE_3));
		}
		if (tablaTanul.size() > ConstantsFunciones.CTE_4) {
			basetec.setTablatanul3((String) tablaTanul.get(ConstantsFunciones.CTE_4));
			basetec.setPorcprovtanul3((BigDecimal) tablaTanul.get(ConstantsFunciones.CTE_5));
		}

		return basetec;
	}

	List ordenarTablas(List tablasRepre) {

		int tamaño = tablasRepre.size() / 2;
		List<String> tablas = new ArrayList<String>();
		List<BigDecimal> porcentajes = new ArrayList<BigDecimal>();
		List tablasOrdenadas = new ArrayList();;
		
		for (int i = 0; i < tamaño; i++){
			tablas.add((String) tablasRepre.get(i * 2));
			porcentajes.add((BigDecimal) tablasRepre.get(i * 2 + 1));
		}
		
		for (int i = 0; i < tamaño; i++) {
			
			BigDecimal porcentajeAct = BigDecimal.ZERO;
			int indice = 0;
			
			for (int j = 0; j < porcentajes.size(); j++) {
				if (porcentajeAct.compareTo(porcentajes.get(j)) == -1) {
					porcentajeAct = porcentajes.get(j);
					indice = j;
				}
			}
			
			tablasOrdenadas.add(tablas.get(indice));
			tablasOrdenadas.add(porcentajes.get(indice));
			
			tablas.remove(indice);
			porcentajes.remove(indice);
		}

		
		return tablasOrdenadas;
	}

	private String factor2Value(List<BigDecimal> factor2) {
		for (BigDecimal factor2Entry : factor2) {
			if (factor2Entry != null && !factor2Entry.equals(BigDecimal.valueOf(100))) {
				return ConstantsEntregables.CTE_S;
			}
		}
		return ConstantsEntregables.CTE_N;
	}

}
