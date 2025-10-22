package es.mapfre.solvencia.utils.beanio.handlers;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Properties;

import org.beanio.types.ConfigurableTypeHandler;
import org.beanio.types.TypeConversionException;
import org.beanio.types.TypeHandler;

public class DoubleDecimalsTypeHandler implements TypeHandler, ConfigurableTypeHandler {
	private static final int BASE_10 = 10;
	private String format = "#";
	private DecimalFormat dfParser;
	private DecimalFormat dfFormatter;
	
	private char formatDecimalSeparator = '.';
	private int decimales = 0;
	
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public DecimalFormat getDfParser() {
		return dfParser;
	}
	public void setDfParser(DecimalFormat dfParser) {
		this.dfParser = dfParser;
	}
	public DecimalFormat getDfFormatter() {
		return dfFormatter;
	}
	public void setDfFormatter(DecimalFormat dfFormatter) {
		this.dfFormatter = dfFormatter;
	}
	
	public char getFormatDecimalSeparator() {
		return formatDecimalSeparator;
	}
	public void setFormatDecimalSeparator(char formatDecimalSeparator) {
		this.formatDecimalSeparator = formatDecimalSeparator;
	}

	public int getDecimales() {
		return decimales;
	}
	public void setDecimales(int decimales) {
		this.decimales = decimales;
	}

	public Object parse(String text) throws TypeConversionException {
		try {
			Number num = dfParser.parse(text.replace('+', ' ').trim());
			if (decimales > 0 && num != null) {
				return num.doubleValue();
			}
			return num;
		} catch (ParseException e) {
			throw new TypeConversionException(e);
		}
    }
    public String format(Object value) {
    	return dfFormatter.format(value != null ? value : 0);
    }
    public Class<?> getType() {
        return Double.class;
    }

	@Override
	public TypeHandler newInstance(Properties arg0)
			throws IllegalArgumentException {
		DoubleDecimalsTypeHandler ddth = new DoubleDecimalsTypeHandler();
		
		ddth.setFormat(arg0.getProperty(FORMAT_SETTING));
		
		Integer nuevosDecimales = 0;
		
		if (ddth.getFormat() != null && ddth.getFormat().lastIndexOf(formatDecimalSeparator) > -1) {
			nuevosDecimales = ddth.getFormat().length() - ddth.getFormat().lastIndexOf(formatDecimalSeparator) - 1;
			ddth.setDecimales(nuevosDecimales);
		}
		
		DecimalFormat defParser = new DecimalFormat(ddth.getFormat().replace('+', ' ').trim());
		defParser.setMultiplier((int) Math.pow(BASE_10, nuevosDecimales));
		ddth.setDfParser(defParser);
		DecimalFormat defFormatter = new DecimalFormat(ddth.getFormat());
		defFormatter.setMultiplier((int) Math.pow(BASE_10, nuevosDecimales));
		ddth.setDfFormatter(defFormatter);
		
		return ddth;
	}
}