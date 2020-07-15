package io.infintestrike.editor.core;

import java.util.HashMap;

public class Strings extends HashMap<String, String>{
	private static final long serialVersionUID = 5207768836229857025L;

	public void registerString(String key, String value) {
		this.put(key, value);
	};
	
	public String getValue(String key) {
		if(this.containsKey(key)) {
			return this.get(key);
		}else {
			return "Language Error";
		}
	}
}
