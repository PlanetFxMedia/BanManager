package de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	
	public static String getBannedTime(Long BannedTime) {
		String str = getDatum(BannedTime) + " " + getTimeHHMMSS(BannedTime);
		return str;
	}
	
	public static Long getTimestampNow() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.getTimeInMillis();
	}
	
	public static String getDatum(Long timestamp) {
		Date date = new Date(timestamp);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int Day = c.get(Calendar.DAY_OF_MONTH);
		String Days = null;
		if (Day == 1) {
			Days = "01";
		} else if (Day == 2) {
			Days = "02";
		} else if (Day == 3) {
			Days = "03";
		} else if (Day == 4) {
			Days = "04";
		} else if (Day == 5) {
			Days = "05";
		} else if (Day == 6) {
			Days = "06";
		} else if (Day == 7) {
			Days = "07";
		} else if (Day == 8) {
			Days = "08";
		} else if (Day == 9) {
			Days = "09";
		} else {
			Days = String.valueOf(Day);
		}
		int Month = c.get(Calendar.MONTH);
		String Monat = null;
		if (Month == 0) {
			Monat = "01";
		} else if (Month == 1) {
			Monat = "02";
		} else if (Month == 2) {
			Monat = "03";
		} else if (Month == 3) {
			Monat = "04";
		} else if (Month == 4) {
			Monat = "05";
		} else if (Month == 5) {
			Monat = "06";
		} else if (Month == 6) {
			Monat = "07";
		} else if (Month == 7) {
			Monat = "08";
		} else if (Month == 8) {
			Monat = "09";
		} else if (Month == 9) {
			Monat = "10";
		} else if (Month == 10) {
			Monat = "11";
		} else if (Month == 11) {
			Monat = "12";
		}
		int Jahr = c.get(Calendar.YEAR);
		String str = Days + "." + Monat + "." + Jahr;
		return str;
	}
	
	public static String getTimeHHMMSS(Long timestamp) {
		Date date = new Date(timestamp);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int Stunde = c.get(Calendar.HOUR_OF_DAY);
		String Stunden = null;
		if (Stunde == 0) {
			Stunden = "00";
		} else if (Stunde == 1) {
			Stunden = "01";
		} else if (Stunde == 2) {
			Stunden = "02";
		} else if (Stunde == 3) {
			Stunden = "03";
		} else if (Stunde == 4) {
			Stunden = "04";
		} else if (Stunde == 5) {
			Stunden = "05";
		} else if (Stunde == 6) {
			Stunden = "06";
		} else if (Stunde == 7) {
			Stunden = "07";
		} else if (Stunde == 8) {
			Stunden = "08";
		} else if (Stunde == 9) {
			Stunden = "09";
		} else {
			Stunden = String.valueOf(Stunde);
		}
		int Minute = c.get(Calendar.MINUTE);
		String Minuten = null;
		if (Minute == 0) {
			Minuten = "00";
		} else if (Minute == 1) {
			Minuten = "01";
		} else if (Minute == 2) {
			Minuten = "02";
		} else if (Minute == 3) {
			Minuten = "03";
		} else if (Minute == 4) {
			Minuten = "04";
		} else if (Minute == 5) {
			Minuten = "05";
		} else if (Minute == 6) {
			Minuten = "06";
		} else if (Minute == 7) {
			Minuten = "07";
		} else if (Minute == 8) {
			Minuten = "08";
		} else if (Minute == 9) {
			Minuten = "09";
		} else {
			Minuten = String.valueOf(Minute);
		}
		int Sekunde = c.get(Calendar.SECOND);
		String Sekunden = null;
		if (Sekunde == 0) {
			Sekunden = "00";
		} else if (Sekunde == 1) {
			Sekunden = "01";
		} else if (Sekunde == 2) {
			Sekunden = "02";
		} else if (Sekunde == 3) {
			Sekunden = "03";
		} else if (Sekunde == 4) {
			Sekunden = "04";
		} else if (Sekunde == 5) {
			Sekunden = "05";
		} else if (Sekunde == 6) {
			Sekunden = "06";
		} else if (Sekunde == 7) {
			Sekunden = "07";
		} else if (Sekunde == 8) {
			Sekunden = "08";
		} else if (Sekunde == 9) {
			Sekunden = "09";
		} else {
			Sekunden = String.valueOf(Sekunde);
		}
		String str = Stunden + ":" + Minuten + ":" + Sekunden;
		return str;
	}
}