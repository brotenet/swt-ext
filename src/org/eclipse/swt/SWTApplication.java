package org.eclipse.swt;

import java.util.HashMap;

public abstract class SWTApplication {
	
	public static HashMap<String, String> collectArguments(String[] arguments, String key_value_separator){
		HashMap<String, String> output = new HashMap<>();
		for(String argument : arguments) {
			output.put(argument.split(key_value_separator,2)[0].trim(), argument.split(key_value_separator,2)[1].trim());
		}
		return output;
	}
	
	public static HashMap<String, String> collectArguments(String[] arguments){
		return collectArguments(arguments, "=");
	}

}
