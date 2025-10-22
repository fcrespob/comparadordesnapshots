package es.mapfre.solvencia.coherence.serialization.codec;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tangosol.io.pof.PofConstants;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.reflect.Codec;

/**
 * Codec que rebaja la precisión de los valores BigDecimal para ajustar la
 * precisión al máximo soportado por Coherence
 * 
 * @author Indra
 * 
 */
public class BigDecimalSolvenciaCodec implements Codec {
	private static final Logger log = LoggerFactory.getLogger(BigDecimalSolvenciaCodec.class);

	// La longitud máxima debería ser 34
	private static final int MAX_PRECISION = PofConstants.MAX_DECIMAL128_UNSCALED.toString().length();

	@Override
	public Object decode(PofReader in, int index) throws IOException {
		return in.readBigDecimal(index);
	}

	@Override
	public void encode(PofWriter out, int index, Object value) throws IOException {
		if (value instanceof BigDecimal) {
			BigDecimal bd = (BigDecimal) value;

			try {
				if (bd != null && bd.precision() > MAX_PRECISION) {
					int parteEntera = bd.precision() - Math.max(0, bd.scale());
					int nuevaEscala = Math.min(MAX_PRECISION - parteEntera, bd.scale());

					bd = bd.setScale(nuevaEscala, RoundingMode.DOWN);
				}

				out.writeBigDecimal(index, bd);

			} catch (Exception e) {
				log.error("Error guardando el valor en cache", e);
			}
		} else {
			out.writeObject(index, value);
		}
	}

}
