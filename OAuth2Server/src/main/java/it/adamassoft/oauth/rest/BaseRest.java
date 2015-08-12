package it.adamassoft.oauth.rest;

import it.adamassoft.oauth.service.BaseService;

import java.security.Principal;

import javax.persistence.EntityManager;

import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseRest {
	@Autowired
	private BaseService service;
	@Autowired
	private EntityManager em;
	
	@RequestMapping(method = RequestMethod.GET, value = "fabio/token", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getToken() throws JoseException {
	    return service.getToken();
	}
	
	@RequestMapping("/user")
	public Principal user(Principal user) {
	    return user;
	}
	
}
