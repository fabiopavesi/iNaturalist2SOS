package it.cnr.irea.inatsos.dto;

import javax.xml.bind.annotation.XmlElement;

public class BBOXDTO {
	@XmlElement(namespace="http://www.opengis.net/fes/2.0", name="ValueReference")
	public String valueReference = "sams:shape";
	
	@XmlElement(namespace="http://www.opengis.net/gml/3.2", name="Envelope")
	public EnvelopeDTO envelope;
}
