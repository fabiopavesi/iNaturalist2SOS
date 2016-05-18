package it.cnr.irea.inatsos.dto;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GetCapabilitiesRequestJSONDTO {
	public String request;
	public ArrayList<String> sections;
	public String service = "SOS";
}
