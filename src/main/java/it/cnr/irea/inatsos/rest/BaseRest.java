package it.cnr.irea.inatsos.rest;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import it.cnr.irea.inatsos.model.Observation;
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
	
	@RequestMapping(method=RequestMethod.GET, value="/clear", produces=MediaType.APPLICATION_JSON_VALUE)
	public void removeObservations() {
		service.clearObservations();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/observations", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Observation> getObservations() {
		return em.createNamedQuery("Observation.findAll", Observation.class).getResultList();
	}

}
