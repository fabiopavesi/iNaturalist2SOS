package it.cnr.irea.inatsos.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GetCapabilitiesResponseJSONDTO {
	public String request = "GetCapabilities";
	public String version = "2.0.0";
	public String service = "SOS";
	
}
