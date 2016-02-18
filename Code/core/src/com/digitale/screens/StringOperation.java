package com.digitale.screens;

public class StringOperation {
public static String firstLetterToUpper(String inputString){
	String outputString=(inputString.substring(0, 1).toUpperCase())+inputString.substring(1, inputString.length());
	return outputString;
	
}
}
