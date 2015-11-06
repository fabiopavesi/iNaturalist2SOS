package it.cnr.irea.inatsos.rest;

import java.io.IOException;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.cnr.irea.inatsos.model.User;
import it.cnr.irea.inatsos.service.MainService;

@RestController
@RequestMapping("kvp")
public class SOSRest {
	@Autowired
	private MainService service;
	
	@Autowired
	private EntityManager em;
	
	@RequestMapping(method=RequestMethod.GET, value="", produces=MediaType.APPLICATION_XML_VALUE)
	public String sosMethod(@RequestParam(required=false) String ogcService, @RequestParam String request, @RequestParam(required=false) long procedure) throws IOException {
		if ( ogcService != null && !ogcService.equalsIgnoreCase("sos") ) {
			return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
					"<ExceptionReport version=\"1.0\">" +
					"<Exception exceptionCode=\"MissingParameterValue\" locator=\"request\"/>" +
					"</ExceptionReport>";
		}
		if ( request.equalsIgnoreCase("describeSensor") ) {
			User u = em.find(User.class, procedure);
			if ( u != null ) {
				return service.fillInSensorDescribe(u);
			}
		}
		return null;
	}
}
