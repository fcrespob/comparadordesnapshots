package es.mapfre.solvencia.utils.beanio.handlers;

import java.math.BigDecimal;

import org.beanio.types.BigDecimalTypeHandler;

public class BigDecimalSignOutTypeHandler extends BigDecimalTypeHandler {
	
	public BigDecimal createNumber(String text) {
		return new BigDecimal(text+"1");
	}
	
    public String format(Object value) {
    	
    	if (value instanceof BigDecimal) {
    		BigDecimal valor = (BigDecimal)value;
   			return valor.signum() < 0 ? "-" : "+";
    	}
    	return "+";
    }
    public Class<?> getType() {
        return BigDecimal.class;
    }

}