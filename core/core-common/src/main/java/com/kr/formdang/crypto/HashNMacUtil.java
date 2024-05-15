package com.kr.formdang.crypto;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HashNMacUtil {

	public static String getHashSHA256(String text) throws NoSuchAlgorithmException {

		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(text.getBytes());
		byte[] result = md.digest();

		return Base64.getEncoder().encodeToString(result);
	}
	
}