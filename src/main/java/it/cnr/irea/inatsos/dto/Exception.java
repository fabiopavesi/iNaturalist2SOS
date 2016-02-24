package it.cnr.irea.inatsos.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace="http://www.opengis.net/ows/1.1", name="Exception")
public class Exception {
	@XmlAttribute(name="ExceptionCode")
	public String exceptionCode;
	@XmlElement(namespace="http://www.opengis.net/ows/1.1", name="ExceptionText")
	public String exceptionText; 
}
