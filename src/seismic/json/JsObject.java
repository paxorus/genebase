package seismic.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	
	public String string(String key) {
		if (jsonString != null) {
			process();
		}
		return map.get(key);
	}
	
	public JsObject object(String key) {
		return new JsObject(key);
	}
	
	public int integer(String key) {
		return Integer.parseInt(string(key));
	}
	
	public double number(String key) {
		return Double.parseDouble(string(key));
	}
	
	public boolean bool(String key) {
		String value = string(key);
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
		s = s.substring(1, s.length() - 1);
		
		// ensure not inside a string or array, and object depth of 1
		int curlyCount = 0;
		int squareCount = 0;
		boolean insideString = false;
		
		// if duplicate key, latter overwrites
		List<String> list = new ArrayList<String>();
		StringBuffer buffer = new StringBuffer();	

		for (int i = 0; i < s.length(); i ++) {
			char c = s.charAt(i);
						
			// too many closing } or ]
			if (curlyCount < 0 || squareCount < 0) {
				throw new RuntimeException("Malformed JSON");
			}
			
//			boolean isDelimiter = false;
			if (c != ',' || (curlyCount > 0 || squareCount > 0 || insideString)) {
				buffer.append(c);
//				break;
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
				case '\'':
					insideString = !insideString;
					break;
				case ',':
					if (buffer.length() > 0 && curlyCount == 0 && squareCount == 0 && !insideString) {
						list.add(buffer.toString());
//						isDelimiter = true;
					}
					buffer = new StringBuffer();
					break;
			}
		}
		
		// too many opening { or [
		if (curlyCount > 0 || squareCount > 0 || insideString) {
			throw new RuntimeException("Malformed JSON");
		}
		// flush the last item, which will not have a comma following it
		if (buffer.length() > 0) {
			list.add(buffer.toString());
		}
		
		// convert "_:_" list to map
		for (String entry : list) {
			int colonIdx = entry.indexOf(":");
			
			String blah = list.get(0);
			if (colonIdx == -1) throw new RuntimeException(blah);
			String key = entry.substring(0, colonIdx).trim();
			// ignore single or double quotes on key
			int length = key.length();
			if (key.charAt(0) == '"' && key.charAt(length - 1) == '"') {
				key = key.substring(1, length - 1);
			}
			if (key.charAt(0) == '\'' && key.charAt(length - 1) == '\'') {
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
		Set<String> entries = new HashSet<String>();

		for (String key : map.keySet()) {
			String value = map.get(key);
			entries.add(key + ":" + value);
		}
		String[] array = entries.toArray(new String[entries.size()]);
		Arrays.sort(array);
		
		return '{' + join(array, ",") + '}';
	}
	
	private String join(String[] array, String delimiter) {
		if (array.length == 0) {
			return "";
		}
		String s = array[0];
		for (int i = 1; i < array.length; i ++) {
			s += "," + array[i];
		}
		return s;
	}
}
