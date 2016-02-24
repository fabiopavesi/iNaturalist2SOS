package it.cnr.irea.inatsos.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	public User retrieveUser(String id) {
		RestTemplate restTemplate = new RestTemplate();

		User u = (User) restTemplate.getForObject(id, User.class);
		
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
	
	public List<User> allUsers() {
		return em.createNamedQuery("User.findAll").getResultList();
	}
	
	public List<Observation> allObservations() {
		return em.createNamedQuery("Observation.findAll").getResultList();
	}
	
	public List<String> fillInSensors() {
		ArrayList<String> strings = new ArrayList<String>();
		for ( User u : allUsers() ) {
			try {
				strings.add(fillInSensor(u));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return strings;
	}

	public ArrayList<String> fillInObservations() {
		ArrayList<String> strings = new ArrayList<String>();
		for ( Observation o : allObservations() ) {
			try {
				strings.add(fillInObservation(o));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return strings;
	}

	public Object getFieldValue(Object o, String field) {
		Class<?> c = o.getClass();

		Field f;
		try {
			f = c.getDeclaredField(field);
			f.setAccessible(true);

			return f.get(o);
		} catch (NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public Class getFieldType(Object o, String field) {
		Class<?> c = o.getClass();

		Field f;
		try {
			f = c.getDeclaredField(field);
			f.setAccessible(true);
			log.info("field " + field + " is " + f.getType().getName());
			return f.getType();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String fillInSensorDescribe(User user) throws IOException {
		return fillInSensor(user).replaceAll("swes:InsertSensor", "swes:DescribeSensor");
	}
	
	public String fillInSensor(User user) throws IOException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
		String result = "";
		URL url = this.getClass().getClassLoader().getResource("DescribeSensor_Response.xml");
		log.info(url.getPath());
		ArrayList<String> tokens = new ArrayList<>();
		Pattern pattern = Pattern.compile("\\$([a-zA-Z0-9_]+)\\$");
		
		BufferedReader file = new BufferedReader(new FileReader(url.getFile()));
		String s = null;
		while ( (s = file.readLine()) != null ) {
			result += s;
		}
		
		Matcher m = pattern.matcher(result);
		while ( m.find() ) {
			Object objectValue = getFieldValue(user, m.group(1));
			Class objectType = getFieldType(user, m.group(1));
			String value = "";
			if ( objectValue != null ) {
				if ( objectType.equals(Date.class) ) {
					log.info("field " + m.group(1) + " is recognized as date");
					value = df.format(objectValue);
				} else {
					value = objectValue.toString();
				}
			}
			// log.info(m.group(1));
			// log.info(value);
			result = result.replaceAll("\\$" + m.group(1) + "\\$", value);
		}
		
		log.info(result);
		
		return result;
	}
	
	public String fillInObservation(Observation observation) throws IOException {
		String result = "";
		URL url = this.getClass().getClassLoader().getResource("static/InsertObservation.xml");
		log.info(url.getPath());
		ArrayList<String> tokens = new ArrayList<>();
		Pattern pattern = Pattern.compile("\\$([a-zA-Z0-9_]+)\\$");
		
		BufferedReader file = new BufferedReader(new FileReader(url.getFile()));
		String s = null;
		while ( (s = file.readLine()) != null ) {
			result += s;
		}
		
		Matcher m = pattern.matcher(result);
		while ( m.find() ) {
			Object objectValue = getFieldValue(observation, m.group(1));
			String value = "";
			if ( objectValue != null ) {
				value = objectValue.toString();
			}
			// log.info(m.group(1));
			// log.info(value);
			result = result.replaceAll("\\$" + m.group(1) + "\\$", value);
		}
		
		log.info(result);
		
		return result;
	}

}
