package it.cnr.irea.inatsos.service;

import it.cnr.irea.inatsos.dto.GetFoiRequestDTO;
import it.cnr.irea.inatsos.dto.GetObservationRequestDTO;
import it.cnr.irea.inatsos.dto.ProjectMemberDTO;
import it.cnr.irea.inatsos.model.Harvest;
import it.cnr.irea.inatsos.model.Observation;
import it.cnr.irea.inatsos.model.Setting;
import it.cnr.irea.inatsos.model.User;
import it.cnr.irea.inatsos.utils.LoggingRequestInterceptor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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
	
	public ProjectMemberDTO[] retrieveAllUsers() {
		String encodedCredentials = getSetting("credentials");
		RestTemplate restTemplate = new RestTemplate();
		String url = "https://www.inaturalist.org/projects/lter-italy/members?format=json";
		final HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + encodedCredentials);
		final HttpEntity<String> request = new HttpEntity<String>(headers);
		
		ResponseEntity<ProjectMemberDTO[]> u = restTemplate.exchange(url, HttpMethod.GET, request, ProjectMemberDTO[].class);
		ProjectMemberDTO[] members = u.getBody();
		return members;
	}
	
	public Observation[] retrieveAllObservations() {
		String url = "http://www.inaturalist.org/observations/?quality_grade=any&per_page=200&identifications=any&projects[]=lter-italy&extra=fields&has[]=photos&has[]=geo&format=json";

		RestTemplate restTemplate = new RestTemplate();
		//set interceptors/requestFactory
		ClientHttpRequestInterceptor ri = new LoggingRequestInterceptor();
		List<ClientHttpRequestInterceptor> ris = new ArrayList<ClientHttpRequestInterceptor>();
		ris.add(ri);
		restTemplate.setInterceptors(ris);
		restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
		Observation[] observations = (Observation[]) restTemplate.getForObject(url, Observation[].class);
		return observations;
	}
	
	public Observation[] retrieveAllObservations(GetObservationRequestDTO dto) {
		String filter = "";
		String userName = null;
		if ( dto.procedure != null ) {
			
			userName = dto.procedure.substring(dto.procedure.lastIndexOf("/") + 1);
		}
		if ( dto.spatialFilter != null ) {
			String[] lowerCorner = dto.spatialFilter.bbox.envelope.lowerCorner.split(" ");
			String[] upperCorner = dto.spatialFilter.bbox.envelope.upperCorner.split(" ");
			filter += "&swlon=" + lowerCorner[0] + "&swlat=" + lowerCorner[1];
			filter += "&nelon=" + upperCorner[0] + "&nelat=" + upperCorner[1];
		}
		String url = "http://www.inaturalist.org/observations/?quality_grade=any&per_page=200&identifications=any&projects[]=lter-italy&extra=fields&has[]=photos&has[]=geo" + filter + "&format=json";

		System.out.println(url);
		
		RestTemplate restTemplate = new RestTemplate();
		//set interceptors/requestFactory
		ClientHttpRequestInterceptor ri = new LoggingRequestInterceptor();
		List<ClientHttpRequestInterceptor> ris = new ArrayList<ClientHttpRequestInterceptor>();
		ris.add(ri);
		restTemplate.setInterceptors(ris);
		restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
		Observation[] observations = (Observation[]) restTemplate.getForObject(url, Observation[].class);

		if ( userName != null ) {
			ArrayList<Observation> filtered = new ArrayList<>();
			for ( Observation o : observations ) {
				if ( o.getUserLogin().equalsIgnoreCase(userName) ) {
					filtered.add(o);
				}
			}
			observations = filtered.toArray(new Observation[0]);
		}
		return observations;
	}

	public Observation[] retrieveAllObservations(GetFoiRequestDTO dto) {
		String filter = "";
		String userName = null;
		/*
		if ( dto.procedure != null ) {
			
			userName = dto.procedure.substring(dto.procedure.lastIndexOf("/") + 1);
		}
		*/
		if ( dto.spatialFilter != null ) {
			String[] lowerCorner = dto.spatialFilter.bbox.envelope.lowerCorner.split(" ");
			String[] upperCorner = dto.spatialFilter.bbox.envelope.upperCorner.split(" ");
			filter += "&swlon=" + lowerCorner[0] + "&swlat=" + lowerCorner[1];
			filter += "&nelon=" + upperCorner[0] + "&nelat=" + upperCorner[1];
		}
		String url = "http://www.inaturalist.org/observations/?quality_grade=any&per_page=200&identifications=any&projects[]=lter-italy&extra=fields&has[]=photos&has[]=geo" + filter + "&format=json";

		System.out.println(url);
		
		RestTemplate restTemplate = new RestTemplate();
		//set interceptors/requestFactory
		ClientHttpRequestInterceptor ri = new LoggingRequestInterceptor();
		List<ClientHttpRequestInterceptor> ris = new ArrayList<ClientHttpRequestInterceptor>();
		ris.add(ri);
		restTemplate.setInterceptors(ris);
		restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
		Observation[] observations = (Observation[]) restTemplate.getForObject(url, Observation[].class);

		/*
		if ( userName != null ) {
			ArrayList<Observation> filtered = new ArrayList<>();
			for ( Observation o : observations ) {
				if ( o.getUserLogin().equalsIgnoreCase(userName) ) {
					filtered.add(o);
				}
			}
			observations = filtered.toArray(new Observation[0]);
		}
		*/
		return observations;
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

	public String fillInObservations(Observation[] observations) throws IOException {
		String mainFile = getFileContent("observations.xml");

		String obs = "";
		for ( Observation o : allObservations() ) {
			try {
				obs += fillInObservation(o);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		mainFile = mainFile.replace("$singleObservations$", obs);

		return mainFile;
	}
	
	public String fillInFoi(Observation[] observations) throws IOException {
		String mainFile = getFileContent("foi_response.xml");

		String obs = "";
		for ( Observation o : allObservations() ) {
			try {
				obs += fillInFoi(o);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		mainFile = mainFile.replace("$foiList$", obs);

		return mainFile;
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
			// log.info("field " + field + " is " + f.getType().getName());
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
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
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
	
	public String fillInSensor(String xml, User user) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
		Pattern pattern = Pattern.compile("\\$([a-zA-Z0-9_]+)\\$");
		Matcher m = pattern.matcher(xml);
		
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
			xml = xml.replaceAll("\\$" + m.group(1) + "\\$", value);
		}
		return xml;
	}
	
	public String fillInObservation(Observation observation) throws IOException {
		
		if ( observation.getPhotos() != null && observation.getPhotos().size() > 0 ) {
			log.info("picture " + observation.getPhotos().get(0).getThumbUrl());
			observation.setThumbUrl(observation.getPhotos().get(0).getThumbUrl());
			observation.setSquareUrl(observation.getPhotos().get(0).getSquareUrl());
			observation.setSmallUrl(observation.getPhotos().get(0).getSmallUrl());
			observation.setMediumUrl(observation.getPhotos().get(0).getMediumUrl());
			observation.setLargeUrl(observation.getPhotos().get(0).getLargeUrl());
			log.info("picture destination " + observation.getThumbUrl());
		}

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
		String result = "";
		URL url = this.getClass().getClassLoader().getResource("single_observation.xml");
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
			Class objectType = getFieldType(observation, m.group(1));
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

	public String fillInFoi(Observation observation) throws IOException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
		String result = "";
		URL url = this.getClass().getClassLoader().getResource("single_foi.xml");
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
			Class objectType = getFieldType(observation, m.group(1));
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

	public String getFileContent(String fileName) throws IOException {
		String result = null;
		String fileLocationInClasspath = fileName;
		Resource resource = new ClassPathResource(fileLocationInClasspath);
		InputStream resourceInputStream = resource.getInputStream();
		StringBuilder sb = new StringBuilder();
		int c = 0;
		while ( (c = resourceInputStream.read()) != -1 ) {
			sb.append((char) c);
		}
		return sb.toString();
	}
	
	public String getCapabilities() throws IOException {
		String result = getFileContent("iNat_CapabilitiesResponse.xml");
		String inspireBlock = "";
		String finalBlock = "";
		String procedures = "";
		
		final String inspireOfferings = getFileContent("inspire_offerings.xml");
		final String offerings = getFileContent("single_offering.xml");
		
		ProjectMemberDTO[] members = retrieveAllUsers();
		
		for ( ProjectMemberDTO m : members ) {
			User u = retrieveUser(m.user_id);
			inspireBlock += fillInSensor(inspireOfferings, u);
			finalBlock += fillInSensor(offerings, u);
			procedures += "<ows:Value>" + u.getUri() + "</ows:Value>";
		}
		result = result.replace("$inspireOfferings$", inspireBlock);
		result = result.replace("$offerings$", finalBlock);
		result = result.replace("$procedures$", procedures);
		return result;
	}
}
