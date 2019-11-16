package fpt.dps.dtms.web.rest;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fpt.dps.dtms.service.TimeZoneService;
import fpt.dps.dtms.web.rest.vm.TimeZoneVM;
import io.github.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api")
public class TimeZoneResource {
private final Logger log = LoggerFactory.getLogger(TimeZoneResource.class);
	
	private final TimeZoneService timeZoneService;
	
	public TimeZoneResource(TimeZoneService timeZoneService) {
		this.timeZoneService = timeZoneService;
	} 
	 
	@GetMapping("/timezones")
    @Timed
    public ResponseEntity<List<TimeZoneVM>> getAllTimeZones() {
        log.debug("REST request to get all time zones");
        List<TimeZoneVM> res =  timeZoneService.findAll();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }
	
	@GetMapping("/timezones/**")
    @Timed
    public ResponseEntity<TimeZoneVM> getTimeZone(HttpServletRequest request) {
		String requestURL = request.getRequestURL().toString();
	    String id = requestURL.split("/timezones/")[1];
        log.debug("REST request to get Timezone : {}", id);
        TimeZoneVM timezone = timeZoneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(timezone));
    }
}
