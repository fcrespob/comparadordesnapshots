package es.mapfre.solvencia.utils.beanio.handlers;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

import org.beanio.types.ConfigurableTypeHandler;
import org.beanio.types.DateTypeHandler;
import org.beanio.types.TypeConversionException;
import org.beanio.types.TypeHandler;

/*
 * Deal with yyyyMMdd = 00000000 when calendar is null
 */
public class DateRenovacionTypeHandler extends DateTypeHandler implements TypeHandler, ConfigurableTypeHandler {
	private String nullDate = "00000000";
	private String lastDate = "99999999";
	private String lastValiedDate = "99991231";
	
	private String oldDay = "29";
	private String newDay = "28";
	private String febraury = "02";
	
	private String formatDay ="dd";
	private String formatMonth ="MM";
	
	
	@Override
	public Timestamp parse(String text) throws TypeConversionException {
		
		Timestamp returnValue = null;
		Date fecha = null;
		
		if(lastDate.equals(text)){
			fecha = super.parse(this.lastValiedDate);
		}else if (nullDate.equals(text) ) {
			fecha = null;
		}else{
			
			int indexDay = this.getPattern().lastIndexOf(formatDay);
			String day = text.substring(indexDay,indexDay+formatDay.length());
			
			int indexmonth = this.getPattern().lastIndexOf(formatMonth);
			String month = text.substring(indexmonth,indexmonth+formatMonth.length());
			
			if(month.equals(febraury) && day.equals(oldDay)){
				text = text.substring(0, indexDay).concat(newDay).concat(text.substring(indexDay+formatDay.length(),text.length()));
			}
			
			fecha = super.parse(text);
		}
		
		if(fecha != null){
			returnValue = new Timestamp(fecha.getTime());
		}
		
		return returnValue;
	}
	

	@Override
	public String format(Object value) {
		if (value == null) {
			return nullDate;
		}
		return super.format(value);
	}
	
	@Override
	public DateTypeHandler newInstance(Properties prop) throws IllegalArgumentException {
		DateRenovacionTypeHandler drth = new DateRenovacionTypeHandler();
		drth.setPattern(prop.getProperty(FORMAT_SETTING));
		return drth;
	}
	
	@Override
	public Class<?> getType() {
		return Timestamp.class;
	}

	public String getNullDate() {
		return nullDate;
	}

	public void setNullDate(String nullDate) {
		if(nullDate!=null)
			this.nullDate = nullDate;
	}

	public String getLastDate() {
		return lastDate;
	}

	public void setLastDate(String lastDate) {
		if(lastDate!=null)
			this.lastDate = lastDate;
	}

	public String getLastValiedDate() {
		return lastValiedDate;
	}

	public void setLastValiedDate(String lastValiedDate) {
		if(lastValiedDate!=null)
			this.lastValiedDate = lastValiedDate;
	}
	
	

}

