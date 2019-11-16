package fpt.dps.dtms.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fpt.dps.dtms.web.rest.vm.TimeZoneVM;


@Service
public class TimeZoneService {
	
	private final Logger log = LoggerFactory.getLogger(TimeZoneService.class);

	/*
	* Get all time zones
	*/
	public List<TimeZoneVM> findAll() {
		log.debug("Request to get all Time Zones");
		List<TimeZoneVM> result = new ArrayList<TimeZoneVM>();

		String[] ids = TimeZone.getAvailableIDs();
		for (String id : ids) {
			TimeZoneVM timezone = new TimeZoneVM();
			TimeZone zone = TimeZone.getTimeZone(id);
			timezone.setTimeZoneId(id);
			timezone.setTimeZoneContent(prettifyTimeZone(zone));
			timezone.setTimeZoneOffset(zone.getRawOffset());
			result.add(timezone);
		}

		// Sort time zone list base on time offset
		Collections.sort(result, new Comparator<TimeZoneVM>() {
			public int compare(TimeZoneVM s1, TimeZoneVM s2) {
				return s1.getTimeZoneOffset() - s2.getTimeZoneOffset();
			}
		});
		return result;
	}

	/*
	* Get one time zones by Id
	*/
	public TimeZoneVM findOne(String id) {

		log.debug("Request to get all Time Zones");

		TimeZoneVM result = new TimeZoneVM();

		if (timeZoneIdIsValid(id)) {
			TimeZone zone = TimeZone.getTimeZone(id);
			result.setTimeZoneId(id);
			result.setTimeZoneContent(prettifyTimeZone(zone));
			result.setTimeZoneOffset(zone.getRawOffset());
		} else {
			result = null;
		}
		return result;
	}

	/*
	* Check if time zone id is a valid ID
	*/
	private static boolean timeZoneIdIsValid(String inputId) {
		boolean result = false;
		String[] ids = TimeZone.getAvailableIDs();
		for (String id : ids) {
			if (id.equals(inputId)) {
				result = true;
				break;
			}
		}
		return result;
	}

	private static String prettifyTimeZone(TimeZone tz) {

		long hours = TimeUnit.MILLISECONDS.toHours(tz.getRawOffset());
		long minutes = TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset()) - TimeUnit.HOURS.toMinutes(hours);
		// avoid -4:-30 issue
		minutes = Math.abs(minutes);

		String result = "";
		if (hours > 0) {
			result = String.format("(UTC+%d:%02d) %s", hours, minutes, tz.getID());
		} else {
			result = String.format("(UTC%d:%02d) %s", hours, minutes, tz.getID());
		}

		return result;

	}

}
