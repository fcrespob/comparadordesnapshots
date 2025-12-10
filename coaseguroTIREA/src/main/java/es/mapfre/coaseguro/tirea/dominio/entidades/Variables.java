package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "variable"
})
@XmlRootElement(name = "Variables")
public class Variables {
		
	private List<Variable> Variable;
	
	public Variables() {
		super();
	}

	public List<Variable> getVariable() {
		return Variable;
	}

	@XmlElement(name = "Variable")
	public void setVariable(List<Variable> variable) {
		Variable = variable;
	}

}
