package aklny;

import java.time.LocalTime;

public class Utils {
	
	public static boolean canSubmit() {
		LocalTime now = LocalTime.now();
		// these variables changes according to server time :)
		return now.isBefore(LocalTime.of(10, 0)) || now.isAfter(LocalTime.of(13, 0));
	}
	
	public static boolean isNotValid(String s)
	{
		return s == null || s.equals("");
	}

}
