package es.mapfre.solvencia.entregables.processors;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.InvocableMap.Entry;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.extractor.MultiExtractor;
import com.tangosol.util.extractor.PofExtractor;
import com.tangosol.util.processor.AbstractProcessor;
import com.tangosol.util.processor.ExtractorProcessor;

import es.mapfre.solvencia.coherence.keys.entregables.DetalleCorrienteEntregablesKey;
import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.coherence.keys.salidaCalculo.DetalleBaseTecnicaKey;
import es.mapfre.solvencia.coherence.keys.salidaCalculo.IncidenciaKey;
import es.mapfre.solvencia.coherence.keys.salidaCalculo.TotalesFlujosKey;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;
import es.mapfre.solvencia.entregables.util.ConstantsEntregables;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;

/**
 * Calcula el campo durTrCasado para cada una de las entradas de la cache de
 * totales
 *
 */
public class PrvBtdurTrCasadoProcessor extends AbstractProcessor implements PortableObject {

	private static final long serialVersionUID = -1250443230036934886L;

	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private final IObtenerConfiguracion servicioConfiguracion = FachadaServicios.getObtenerConfiguracion();

	private String bt;

	/**
	 * 
	 */
	public PrvBtdurTrCasadoProcessor() {
		super();
	}

	/**
	 * 
	 * @param bt
	 */
	public PrvBtdurTrCasadoProcessor(String bt) {
		super();
		this.bt = bt;
	}

	@Override
	public Object process(Entry entry) {

		List<String> swcasados;
		Integer ccartera;

		Object key = entry.getKey();
		UmicKey umicKey = null;

		if (key.getClass() == TotalesFlujosKey.class) {
			umicKey = ((TotalesFlujosKey) key).getUmicKey();
		} else if (key.getClass() == DetalleCorrienteEntregablesKey.class) {
			umicKey = ((DetalleCorrienteEntregablesKey) key).getUmicKey();
		} else if (key instanceof UmicKey) {
			umicKey = (UmicKey) key;
		}

		DetalleBaseTecnicaKey detalleKey = new DetalleBaseTecnicaKey(umicKey, bt);

		List<Object> values = getSwcasadoAndCcartera(detalleKey);

		swcasados = (List<String>) values.get(ConstantsFunciones.CTE_0);
		ccartera = (Integer) values.get(ConstantsFunciones.CTE_1);

		/*
		 * - prvBt.durTrCasado: Se buscará en los datos de la umic el tramo de
		 * interés casado. Para ello, de entre los tramos de interés de la umic
		 * (tramos de 1 a 5), se buscará el que cumpla con las siguientes
		 * condiciones: o detalleBaseTecnica. swcasadoX = ‘S’
		 * 
		 * Si no existe ningún tramo que cumpla las condiciones: o
		 * prvBt.durTrCasado = 0 Si existe tramo casado se calculará la duración
		 * cómo: o prvBt.durTrCasado = nannos(umic.baseTecIni.fecIniTramoX,
		 * umic.baseTecIni.fecFinTramoX, VarCriterFec); donde VarCriterFec
		 * obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
		 */
		if (swcasados != null && ccartera != null) {
			for (int i = 0; i < swcasados.size(); i++) {
				if (swcasados.get(i) != null && swcasados.get(i).equals(ConstantsEntregables.CTE_S)) {

					List<Timestamp> fecTramo = getfecInifintramo(umicKey, i);

					String varCriterFec = (String) servicioConfiguracion.recuperarDefinicionAuxiliar(ccartera,
							umicKey.getKmodalidad(), umicKey.getKgarantia(), this.bt, CLAVE_CRIFEC);
					
					if (fecTramo.size() >= 2 && fecTramo.get(ConstantsFunciones.CTE_0) != null
							&& fecTramo.get(ConstantsFunciones.CTE_1) != null)
						return FuncionesAuxiliares.nAnnos(fecTramo.get(ConstantsFunciones.CTE_0),
								fecTramo.get(ConstantsFunciones.CTE_1), varCriterFec);
				}
			}
		} else {

			Exception e = new Solvencia2Excepcion("No se ha encontrado la cartera o el swcasado", new Incidencia());
			e.printStackTrace();
		}
		return BigDecimal.ZERO;

	}
	 
	
	public BigDecimal getDurTramoCasado(UmicKey umicKey) {

		List<String> swcasados;
		Integer ccartera;

		DetalleBaseTecnicaKey detalleKey = new DetalleBaseTecnicaKey(umicKey, bt);

		List<Object> values = getSwcasadoAndCcartera(detalleKey);

		swcasados = (List<String>) values.get(ConstantsFunciones.CTE_0);
		ccartera = (Integer) values.get(ConstantsFunciones.CTE_1);

		/*
		 * - prvBt.durTrCasado: Se buscará en los datos de la umic el tramo de
		 * interés casado. Para ello, de entre los tramos de interés de la umic
		 * (tramos de 1 a 5), se buscará el que cumpla con las siguientes
		 * condiciones: o detalleBaseTecnica. swcasadoX = ‘S’
		 * 
		 * Si no existe ningún tramo que cumpla las condiciones: o
		 * prvBt.durTrCasado = 0 Si existe tramo casado se calculará la duración
		 * cómo: o prvBt.durTrCasado = nannos(umic.baseTecIni.fecIniTramoX,
		 * umic.baseTecIni.fecFinTramoX, VarCriterFec); donde VarCriterFec
		 * obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
		 */
		if (swcasados != null && ccartera != null) {
			for (int i = 0; i < swcasados.size(); i++) {
				if (swcasados.get(i) != null && swcasados.get(i).equals(ConstantsEntregables.CTE_S)) {

					List<Timestamp> fecTramo = getfecInifintramo(umicKey, i);

					String varCriterFec = (String) servicioConfiguracion.recuperarDefinicionAuxiliar(ccartera,
							umicKey.getKmodalidad(), umicKey.getKgarantia(), bt, CLAVE_CRIFEC);

					if (fecTramo.size() >= 2 && fecTramo.get(ConstantsFunciones.CTE_0) != null
							&& fecTramo.get(ConstantsFunciones.CTE_1) != null)
						return FuncionesAuxiliares.nAnnos(fecTramo.get(ConstantsFunciones.CTE_0),
								fecTramo.get(ConstantsFunciones.CTE_1), varCriterFec);
				}
			}
		} else {

			Exception e = new Solvencia2Excepcion("No se ha encontrado la cartera o el swcasado", new Incidencia());
			e.printStackTrace();
		}
		return BigDecimal.ZERO;

	}

	/**
	 * Devuelte los valores SWCASADO y CCARTERA de la cache de
	 * datallesBaseTecnica
	 * 
	 * @param detalleKey
	 * @return
	 */
	private List<Object> getSwcasadoAndCcartera(DetalleBaseTecnicaKey detalleKey) {
		NamedCache detalleBaseTecnica = CacheFactory.getCache(ConstantsEntregables.CACHE_DETALLE_BASETECNICA);

		ValueExtractor[] detalleBaseTecnicaExtractor = new ValueExtractor[] {
				new PofExtractor(List.class, DetalleBaseTecnica.IND_SWCASADO),
				new PofExtractor(Integer.class, DetalleBaseTecnica.IND_CCARTERA) };
		ValueExtractor multiExtractor = new MultiExtractor(detalleBaseTecnicaExtractor);

		List<Object> values = (List<Object>) detalleBaseTecnica.invoke(detalleKey,
				new ExtractorProcessor(multiExtractor));

		return values;
	}

	/**
	 * Devuelve los valores FECINITRAMOi y FECFINTRAMOi de la cache
	 * BaseTecnicaInicial
	 * 
	 * @param umicKey
	 * @param i
	 * @return
	 */
	private List<Timestamp> getfecInifintramo(UmicKey umicKey, Integer i) {

		NamedCache baseTecnicaInicial = CacheFactory.getCache(ConstantsEntregables.CACHE_BASETECNICA_INICIAL);

		ValueExtractor[] fecIniFinExtractor = new ValueExtractor[] {
				// FECINITRAMOX
				new PofExtractor(Timestamp.class, ConstantsFunciones.CTE_6 + i),
				// FECFINTRAMOX
				new PofExtractor(Timestamp.class, ConstantsFunciones.CTE_1 + i) };
		ValueExtractor multiExtractor = new MultiExtractor(fecIniFinExtractor);

		List<Timestamp> values = (List<Timestamp>) baseTecnicaInicial.invoke(umicKey,
				new ExtractorProcessor(multiExtractor));

		return values;

	}

	@Override
	public void readExternal(PofReader arg0) throws IOException {
		this.bt = arg0.readString(ConstantsFunciones.CTE_1);
	}

	@Override
	public void writeExternal(PofWriter arg0) throws IOException {
		arg0.writeString(ConstantsFunciones.CTE_1, this.bt);
	}

}
