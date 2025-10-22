package es.mapfre.gbt.tablasExperiencia.utils.beanio.handlers;

import org.beanio.types.TypeConversionException;
import org.beanio.types.TypeHandler;

public class StringTypeHandler implements TypeHandler {
	private String separador = ".";

	public Object parse(String text) throws TypeConversionException {
		// No se utiliza
		return text;
	}

	public String format(Object value) {
		String result = (String) value;
		return result.replace(separador, "");
	}

	public Class<?> getType() {
		return Boolean.class;
	}
}