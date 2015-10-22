package it.cnr.irea.inatsos.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Setting {
	@Id
	private String code;
	private String value;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
