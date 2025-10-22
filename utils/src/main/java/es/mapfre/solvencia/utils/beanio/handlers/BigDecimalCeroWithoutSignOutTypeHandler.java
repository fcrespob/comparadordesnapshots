package es.mapfre.solvencia.utils.beanio.handlers;

import java.math.BigDecimal;

import org.beanio.types.BigDecimalTypeHandler;

public class BigDecimalCeroWithoutSignOutTypeHandler extends BigDecimalTypeHandler {

	public BigDecimal createNumber(String text) {
		return new BigDecimal(text + "1");
	}

	public String format(Object value) {

		if (value instanceof BigDecimal) {
			BigDecimal valor = (BigDecimal) value;
			Integer sign = valor.signum();
			if (sign.equals(1)) {
				return "+";
			}

			if (sign.equals(-1)) {
				return "-";
			}
		}
		return " ";
	}

	public Class<?> getType() {
		return BigDecimal.class;
	}

}