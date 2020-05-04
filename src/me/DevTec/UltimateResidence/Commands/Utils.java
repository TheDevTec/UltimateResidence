package me.DevTec.UltimateResidence.Commands;

import java.util.regex.Pattern;

public class Utils {

	public static boolean specialSymbol(String string) {
		Pattern p = Pattern.compile("[^A-Za-z0-9]");
	     return p.matcher(string).find();
	}

}
