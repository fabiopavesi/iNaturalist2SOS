package it.cnr.irea.inatsos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SchedulerService {
	@Autowired
	private MainService service;
	private static int count = 0;
	
	@Scheduled(fixedDelay=1000)
	public void retrieve() {
		long howMany = Long.parseLong(service.getSetting("interval", "30"));
		if ( ++count > howMany ) {
			count = 0;
			service.retrieveObservations();
		}
	}
}
