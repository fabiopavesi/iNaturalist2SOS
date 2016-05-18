package it.cnr.irea.inatsos.rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.oxm.XmlMappingException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.cnr.irea.inatsos.dto.DescribeSensorRequestDTO;
import it.cnr.irea.inatsos.dto.Exception;
import it.cnr.irea.inatsos.dto.ExceptionReport;
import it.cnr.irea.inatsos.dto.GenericResponse;
import it.cnr.irea.inatsos.dto.GetFoiRequestDTO;
import it.cnr.irea.inatsos.dto.GetObservationRequestDTO;
import it.cnr.irea.inatsos.model.Observation;
import it.cnr.irea.inatsos.model.User;
import it.cnr.irea.inatsos.service.MainService;

@RestController
public class SOSRest {
	@Autowired
	private MainService service;
	
	@RequestMapping(method=RequestMethod.GET, value="/GetCapabilities", produces=MediaType.APPLICATION_XML_VALUE)
	public String getCapabilities() throws IOException {
		return service.getCapabilities();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/observations", produces=MediaType.APPLICATION_XML_VALUE)
	public String getObservations() throws IOException {
		Observation[] observations = service.retrieveAllObservations();
		
		return service.fillInObservations(observations);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/test", consumes=MediaType.APPLICATION_XML_VALUE, produces=MediaType.APPLICATION_XML_VALUE)
	public GetFoiRequestDTO test(@RequestBody GetFoiRequestDTO in) {
		return in;
	}

	@RequestMapping(method=RequestMethod.POST, value="/json", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public String jsonInterface(@RequestBody String dtoString) throws JAXBException, XmlMappingException, UnsupportedEncodingException, IOException {
		
		return null;
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/pox", consumes=MediaType.APPLICATION_XML_VALUE, produces=MediaType.APPLICATION_XML_VALUE)
	public String describeSensor(@RequestBody String dtoString) throws JAXBException, XmlMappingException, UnsupportedEncodingException, IOException {
		System.out.println(dtoString);
		
		JAXBContext jc = null;
		if ( dtoString.contains("<swes:DescribeSensor") ) {
			jc = JAXBContext.newInstance(DescribeSensorRequestDTO.class, ExceptionReport.class);
		} else if ( dtoString.contains("<sos:GetObservation") ) {
			jc = JAXBContext.newInstance(GetObservationRequestDTO.class, ExceptionReport.class);
		} else if ( dtoString.contains("<sos:GetFeatureOfInterest") ) {
			jc = JAXBContext.newInstance(GetFoiRequestDTO.class, ExceptionReport.class);
		} else {
			Exception e = new Exception();
			e.exceptionCode = "UnknownRequest";
			e.exceptionText = "This service cannot recognize this request";
			ExceptionReport r = new ExceptionReport();
			r.exception = e;
			
		}
		//Create unmarshaller
		Unmarshaller um = (Unmarshaller) jc.createUnmarshaller();
		Object dto = um.unmarshal(new ByteArrayInputStream(dtoString.getBytes("utf-8")));

		if ( dto instanceof DescribeSensorRequestDTO ) {
			System.out.println("describeSensor");
			User u = service.retrieveUser(((DescribeSensorRequestDTO) dto).procedure);
			String s = service.fillInSensor(u);
			return s;
//			return new ResponseEntity<GenericResponse>((DescribeSensorRequestDTO)dto, HttpStatus.OK);
		} else if ( dto instanceof GetObservationRequestDTO ) {
			Observation[] observations = service.retrieveAllObservations((GetObservationRequestDTO) dto);
			
			return service.fillInObservations(observations);
		} else if ( dto instanceof GetFoiRequestDTO ) {
			Observation[] observations = service.retrieveAllObservations((GetFoiRequestDTO) dto);
			
			return service.fillInFoi(observations);
		}
		
		Exception e = new Exception();
		e.exceptionCode = "UnknownRequest";
		e.exceptionText = "This service cannot recognize this request";
		ExceptionReport r = new ExceptionReport();
		r.exception = e;
		return "<picche/>"; // new ResponseEntity<GenericResponse>(r, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@RequestMapping(method=RequestMethod.GET, value="/service", /*consumes=MediaType.APPLICATION_XML_VALUE, */produces=MediaType.APPLICATION_XML_VALUE)
	public DescribeSensorRequestDTO describeSensorGET() {
		return new DescribeSensorRequestDTO("pippo", null);
	}
}
