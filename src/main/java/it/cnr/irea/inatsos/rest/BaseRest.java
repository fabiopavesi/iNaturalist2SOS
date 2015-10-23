package it.cnr.irea.inatsos.rest;

import java.io.IOException;
import java.util.List;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import it.cnr.irea.inatsos.model.Observation;
import it.cnr.irea.inatsos.model.User;
import it.cnr.irea.inatsos.service.MainService;

@RestController
public class BaseRest {
	@Autowired
	private MainService service;
	
	@Autowired
	private EntityManager em;
	
	@RequestMapping(method=RequestMethod.GET, value="/test", produces=MediaType.APPLICATION_JSON_VALUE)
	public Observation[] retrieveObservations() {
		return service.retrieveObservations();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/sensors", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<User> listSensors() {
		return service.allUsers();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/sensors/xml", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<String> createSensors() {
		return service.fillInSensors();
	}

	@RequestMapping(method=RequestMethod.GET, value="/sensors/{id}/xml", produces=MediaType.APPLICATION_XML_VALUE)
	public String createSensor(@PathVariable long id) throws IOException {
		User u = em.find(User.class, id);
		if ( u != null ) {
			return service.fillInSensor(u);
		}
		return null;
	}

	@RequestMapping(method=RequestMethod.GET, value="/observations/xml", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<String> createObservations() {
		return service.fillInObservations();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/observations/{id}/xml", produces=MediaType.APPLICATION_XML_VALUE)
	public String createObservation(@PathVariable long id) throws IOException {
		Observation o = em.find(Observation.class, id);
		if ( o != null ) {
			return service.fillInObservation(o);
		}
		return null;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/clear", produces=MediaType.APPLICATION_JSON_VALUE)
	public void removeObservations() {
		service.clearObservations();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/observations", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Observation> getObservations() {
		return em.createNamedQuery("Observation.findAll", Observation.class).getResultList();
	}

}
