package es.mapfre.solvencia.utils.beanio.handlers;

import org.beanio.types.TypeConversionException;
import org.beanio.types.TypeHandler;

public class YNTypeHandler implements TypeHandler {
	private String yesLiteral = "S";
	private String noLiteral = "N";

	public String getYesLiteral() {
		return yesLiteral;
	}

	public void setYesLiteral(String yesLiteral) {
		this.yesLiteral = yesLiteral;
	}

	public String getNoLiteral() {
		return noLiteral;
	}

	public void setNoLiteral(String noLiteral) {
		this.noLiteral = noLiteral;
	}

	public Object parse(String text) throws TypeConversionException {
		Boolean result = yesLiteral.equals(text);
		if (!result && noLiteral != null && !noLiteral.isEmpty()) {
			result = noLiteral.equals(text);
			if (result) {
				result = Boolean.FALSE;
				
			}else if(text == null || text.trim().isEmpty()){
				result = null;
			} else {
				throw new TypeConversionException(new StringBuffer(
						"Text must match one of ").append(yesLiteral)
						.append("/").append(noLiteral).append(". Was ")
						.append(text).toString());
			}
		}
		return result;
	}

	public String format(Object value) {
		return value != null && ((Boolean) value).booleanValue() ? yesLiteral
				: noLiteral;
	}

	public Class<?> getType() {
		return Boolean.class;
	}
}