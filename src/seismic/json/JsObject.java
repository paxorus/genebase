package seismic.json;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsObject {
	
	private String jsonString;
	private Map<String, String> map;
	
	/**
	 * The JSON string representation is lazily parsed.
	 * @param jsonString
	 */
	public JsObject(String jsonString) {
		this.jsonString = jsonString.trim();
		this.map = new HashMap<String, String>();
	}
	
	public String getString(String key) {
		if (jsonString != null) {
			process();
		}
		return map.get(key);
	}
	
	public JsObject getObject(String key) {
		return new JsObject(getString(key));
	}
	
	public JsArray getArray(String key) {
		return new JsArray(key);
	}
	
	public int getInt(String key) {
		return Integer.parseInt(getString(key));
	}
	
	public double getDouble(String key) {
		return Double.parseDouble(getString(key));
	}
	
	public boolean getBoolean(String key) {
		String value = getString(key);
		if (value.equals("true")) {
			return true;
		} else if (value.equals("false")) {
			return false;
		}
		throw new RuntimeException("Not a Boolean value");
	}
	
	/**
	 * Read jsonString into map, then nullify jsonString.
	 */
	private void process() {
		// cut out initial {}, protects against multiple top-level objects
		String s = jsonString.trim();
		if (s.length() == 0) {
			jsonString = null;
			return;
		}
		if (s.length() == 1) {
			throw new RuntimeException(s);
		}
		s = s.substring(1, s.length() - 1);
		
		// ensure not inside a string or array, and object depth of 1
		int curlyCount = 0;
		int squareCount = 0;
		boolean insideString = false;
		List<Integer> commas = new ArrayList<Integer>();

		for (int i = 0; i < s.length(); i ++) {
			char c = s.charAt(i);
						
			// too many closing } or ]
			if (curlyCount < 0 || squareCount < 0) {
				error();
			}
			
			switch (c) {
				case '{':
					if (!insideString) {
						curlyCount ++;
					}
					break;
				case '}':
					if (!insideString) {
						curlyCount --;
					}
					break;
				case '"':
					insideString = !insideString;
					break;
				case ',':
					if (curlyCount == 0 && squareCount == 0 && !insideString) {
						commas.add(i);
					}
					break;
			}
		}
		
		// too many opening { or [
		if (curlyCount > 0 || squareCount > 0 || insideString) {
			error();
		}
		
		// 
		List<String> list = new ArrayList<String>();
		int previousComma = -1;
		for (int commaIdx : commas) {
			list.add(s.substring(previousComma + 1, commaIdx));
			previousComma = commaIdx;
		}
		list.add(s.substring(previousComma + 1, s.length()));
		
		// convert "_:_" list to map
		for (String entry : list) {
			int colonIdx = entry.indexOf(":");
			
			// some key-value pair does not contain a colon
			if (colonIdx == -1) {
				error();
			}
			String key = entry.substring(0, colonIdx).trim();
			
			// ignore double quotes on key
			int length = key.length();
			if (key.charAt(0) == '"' && key.charAt(length - 1) == '"') {
				key = key.substring(1, length - 1);
			}
			String value = entry.substring(colonIdx + 1).trim();
			map.put(key, value);
		}
		
		jsonString = null;
	}
	
	// sort the keys for predictability
	public String toString() {
		if (jsonString != null) {
			process();
		}
		List<String> entries = new ArrayList<String>();

		for (String key : map.keySet()) {
			String value = map.get(key);
			entries.add("\"" + key + "\":" + value);
		}

		Collections.sort(entries);
		
		return '{' + JsArray.join(entries) + '}';
	}
		
	private void error() {
		throw new RuntimeException("Malformed JSON");
	}
}
