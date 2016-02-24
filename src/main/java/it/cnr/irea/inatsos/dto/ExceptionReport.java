package it.cnr.irea.inatsos.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace="http://www.opengis.net/ows/1.1", name="ExceptionReport")
public class ExceptionReport implements GenericResponse {
	public Exception exception;
}
