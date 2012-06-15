package com.shastram.web8085.client;

public class Utils {

	public static String toHex4Digits(int value) {
	    String[] zeroPrefixStr = new String[] { "", "0", "00", "000", "0000" };
	    value = value & 0xffff;
	    String str = Integer.toHexString(value);
	    int zeroPrefix = 4 - str.length();
	    if (zeroPrefix > 0) {
	        str = zeroPrefixStr[zeroPrefix] + str;
	    }
	    return str;
	}

	public static String toHex2Digits(int i) {
	    String str = Integer.toHexString(i);
	    if (str.length() == 1) {
	        str = "0" + str;
	    }
	    str = str.toUpperCase();
	    return str;
	}

}
