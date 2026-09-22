package ru.project.calculations.util;

import java.security.SecureRandom;

public class PassGenerator {

	private PassGenerator() {
	}

	public static String randomPass(){
		String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890!@$%^&*";
		SecureRandom rnd = new SecureRandom();
		int len = 12;
		StringBuilder pass = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			pass.append(symbols.charAt(rnd.nextInt(symbols.length())));
		}
		return pass.toString();
	}

}