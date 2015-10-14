package com.mobile.android.client.utilities;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;

public class DateUtilities {

	public static String getReadableString(DateTime start, DateTime end) {
		Period timePeriod = new Interval(start, end).toPeriod();

		int seconds = timePeriod.getSeconds();
		int minutes = timePeriod.getMinutes();
		int hours = timePeriod.getHours();
		
		int days = Days.daysBetween(start, end).getDays();

		StringBuilder timerText = new StringBuilder();

		if (days > 0) {
			timerText.append(days).append(" day");
			if (days > 1) {
				timerText.append('s');
			}

			timerText.append(' ');
		}

		if (hours > 0 || days > 0) {
			timerText.append(hours).append(" hour");
			if (hours != 1) {
				timerText.append('s');
			}

			timerText.append(' ');
		}

		timerText.append(minutes).append(" minute");
		if (minutes != 1) {
			timerText.append('s');
		}

		timerText.append(' ').append(seconds).append(" second");
		if (seconds != 1) {
			timerText.append('s');
		}

		return timerText.toString();
	}
	
	public static String getFormattedString(Period timePeriod) {
		int seconds = timePeriod.getSeconds();
		int minutes = timePeriod.getMinutes();
		int hours = timePeriod.getHours();
		int days = timePeriod.getDays();

		StringBuilder timerText = new StringBuilder();

		if (days > 0) {
			timerText.append(days).append(':');
		}

		if (hours > 0 || days > 0) {
			timerText.append(String.format("%02d", hours)).append(':');
		}

		timerText.append(String.format("%02d", minutes)).append(":");

		timerText.append(String.format("%02d", seconds));

		return timerText.toString();
	}
	
	public static boolean isSameDate(LocalDateTime date, LocalDateTime comparandDate) {
		return date.getYear() == comparandDate.getYear() && date.getMonthOfYear() == comparandDate.getMonthOfYear() && date.getDayOfMonth() == comparandDate.getDayOfMonth();
	}
	
}
