package fpt.dps.dtms.service.util;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class CommonFunction {

	public static String GetTimeBetweenTwoDays(Instant startDate, Instant endDate, String strZone) {
		// get system's default timezone
		ZoneId zone = strZone == null ? AppConstants.SYSTEM_ZONE_ID : ZoneId.of(strZone);

		// start and end working hours
		LocalTime workStart = LocalTime.of(8, 0);
		LocalTime workEnd = LocalTime.of(17, 0);

		// start and end dates
		ZonedDateTime start = startDate.atZone(zone);// ZonedDateTime.of(2017, 7, 10, 15, 0, 0, 0, zone);
		ZonedDateTime end = endDate.atZone(zone);// ZonedDateTime.of(2017, 7, 12, 13, 0, 0, 0, zone);

		long totalHours = 0;
		ZonedDateTime startHour = start;
		// if start is before 8AM or 5PM, adjust it
		if (start.toLocalTime().isBefore(workStart)) { // before 8 AM
			startHour = start.with(workStart); // set time to 8 AM
		} else if (start.toLocalTime().isAfter(workEnd)) { // after 5 PM
			startHour = start.with(workEnd); // set time to 5 PM
		}
		ZonedDateTime endHour = end;
		// if end is before 8AM or 5PM, adjust it
		if (end.toLocalTime().isAfter(workEnd)) { // after 5 PM
			endHour = end.with(workEnd); // set time to 5 PM
		} else if (end.toLocalTime().isBefore(workStart)) { // before 8 AM
			endHour = end.with(workStart); // set time to 8 AM
		}

		while (startHour.isBefore(endHour)) {
			if (startHour.toLocalDate().equals(endHour.toLocalDate())) { // same day
				totalHours += ChronoUnit.HOURS.between(startHour, endHour);
				break;
			} else {
				ZonedDateTime endOfDay = startHour.with(workEnd); // 6PM of the day
				totalHours += ChronoUnit.HOURS.between(startHour, endOfDay);
				startHour = startHour.plusDays(1).with(workStart); // go to next day
			}
		}

		long days = totalHours / 8;
		long hours = totalHours % 8;
		long minutes = totalHours % 60;
		return days + "d " + hours + "h " + minutes + "m";
	}

	public static int GetDurationBetweenTwoDays(Instant startTime, Instant endTime) {
		Duration between = Duration.between(startTime, endTime);
		long milliseconds = between.abs().toMillis();
		float _minutes = (Float.valueOf(milliseconds) / 1000) / 60;
		//long _minutes = between.abs().toMinutes();
		return (int) Math.ceil(_minutes);
	}

	public static Instant convertLocalDateToInstant(String value) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AppConstants.DATETIME_PATTERN);
		// convert String to LocalDate
		Instant instantDate = null;
		if (!value.equals("")) {
			LocalDate fromDate = LocalDate.parse(value, formatter);
			instantDate = fromDate.atStartOfDay(AppConstants.SYSTEM_ZONE_0).toInstant();
		}
		return instantDate;
	}

	public static Instant convertLocalDateToInstantPlusOne(String value) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AppConstants.DATETIME_PATTERN);
		// convert String to LocalDate
		Instant instantDate = null;
		if (StringUtils.isNotEmpty(value)) {
			LocalDate fromDate = LocalDate.parse(value, formatter);
			instantDate = fromDate.atStartOfDay(AppConstants.SYSTEM_ZONE_0).toInstant().plus(23, ChronoUnit.HOURS);
			;
		}
		return instantDate;
	}

	public static LocalDateTime convertLocalDateToLocalDateTime(String value) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AppConstants.DATETIME_PATTERN);
		// convert String to LocalDate
		LocalDateTime dateTime = null;
		if (StringUtils.isNotEmpty(value)) {
			LocalDate date = LocalDate.parse(value, formatter);
			dateTime = date.atTime(00, 00, 00);
		}
		return dateTime;
	}

	public static int convertTimeStringToMinute(String timeString) throws NumberFormatException {
		int weeks = CommonFunction.getNumberOfTime(timeString, "w") * 60 * 8 * 5;
		int days = CommonFunction.getNumberOfTime(timeString, "d") * 60 * 8;
		int hours = CommonFunction.getNumberOfTime(timeString, "h") * 60;
		int mins = CommonFunction.getNumberOfTime(timeString, "m");
		return weeks + days + hours + mins;
	}

	private static int getNumberOfTime(String timeString, String condition) throws NumberFormatException {
		// Pattern pattern = Pattern.compile("[w,d,h,m,m]");
		Pattern pattern = Pattern.compile("\\d*"+ condition);
		Matcher m = pattern.matcher(timeString);
		while (m.find()) {
			String time = timeString.substring(m.start(), m.end() - 1);
			return Integer.parseInt(time);
		}
		return 0;

	}
}
