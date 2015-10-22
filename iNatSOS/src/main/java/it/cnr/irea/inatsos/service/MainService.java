package it.cnr.irea.inatsos.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import it.cnr.irea.inatsos.model.Harvest;
import it.cnr.irea.inatsos.model.Observation;
import it.cnr.irea.inatsos.model.Setting;
import it.cnr.irea.inatsos.model.User;
import it.cnr.irea.inatsos.utils.LoggingRequestInterceptor;

@Service
@Transactional
public class MainService {
	@Autowired
	private static final Logger log = LoggerFactory.getLogger(MainService.class);
	
	@Autowired
	private EntityManager em;

	public void clearObservations() {
		List<Observation> observations = em.createNamedQuery("Observation.findAll", Observation.class).getResultList();
		for ( Observation o : observations ) {
			em.remove(o);
		}
		List<Harvest> harvests = em.createNamedQuery("Harvest.findAll").getResultList();
		for ( Harvest h : harvests ) {
			em.remove(h);
		}
	}
	
	public Harvest getLatestHarvest() {
		Harvest h = null;
		try {
			h = em.createNamedQuery("Harvest.findLast", Harvest.class).setMaxResults(1).setFirstResult(0).getSingleResult();
		} catch(NoResultException e) {
		
		}
		return h;
	}
	
	public User retrieveUser(long id) {
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://www.inaturalist.org/users/" + id + "?format=json";

		User u = (User) restTemplate.getForObject(url, User.class);
		
		return u;
	}
	
	public Observation[] retrieveObservations() {
		log.info("here I am");
		
		Calendar cal = Calendar.getInstance();
		Harvest h = getLatestHarvest();
		if ( h != null ) {
			cal.setTime(h.getTimestamp());
		} else {
			cal.set(1970, 1, 11, 0, 0, 0);
		}
		
		h = new Harvest();
		h.setTimestamp(new Date());
		
		Date lastCheck = cal.getTime();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
		log.info("since: " + sdf.format(lastCheck));

		RestTemplate restTemplate = new RestTemplate();
		//set interceptors/requestFactory
		ClientHttpRequestInterceptor ri = new LoggingRequestInterceptor();
		List<ClientHttpRequestInterceptor> ris = new ArrayList<ClientHttpRequestInterceptor>();
		ris.add(ri);
		restTemplate.setInterceptors(ris);
		restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));

		// &updated_since=" + sdf.format(lastCheck) + "
		// String url = "http://www.inaturalist.org/observations/?quality_grade=any&identifications=any&projects%5B%5D=lter-italy&extra=fields&has%5B%5D=photos&has%5B%5D=geo&format=json";
		String url = "http://www.inaturalist.org/observations/?quality_grade=any&per_page=200&updated_since=" + sdf.format(lastCheck) + "&identifications=any&projects[]=lter-italy&extra=fields&has[]=photos&has[]=geo&format=json";

		Observation[] observations = (Observation[]) restTemplate.getForObject(url, Observation[].class);
		h.setRetrievedObservations(observations.length);
		em.persist(h);
        log.info(observations.toString());
        
        for ( Observation o : observations ) {
        	Observation o2 = em.find(Observation.class, o.getId());
        	o.setHarvest(h);
        	if ( o2 == null ) {
        		em.persist(o);
        	} else {
        		em.merge(o);
        	}
        }
        
        List<Long> userIds = em.createNamedQuery("Observation.findUsers").getResultList();
        for ( Long l : userIds ) {
        	User u1 = em.find(User.class, l);
        	User u2 = retrieveUser(l);
        	if ( u2 != null ) {
        		u2.setHarvest(h);
        		if ( u1 != null ) {
        			em.merge(u2);
        		} else {
        			em.persist(u2);
        		}
        	}
        }
        
        return observations;
	}
	
	public String getSetting(String code) {
		Setting s = em.find(Setting.class, code);
		if ( s != null ) {
			return s.getValue();
		} else {
			return null;
		}
	}
	
	public String getSetting(String code, String defaultValue) {
		String s = getSetting(code);
		if ( s == null ) {
			s = defaultValue;
			Setting setting = new Setting();
			setting.setCode(code);
			setting.setValue(defaultValue);
			em.persist(setting);
		}
		return s;
	}
}
