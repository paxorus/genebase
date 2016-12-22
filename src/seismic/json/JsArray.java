package seismic.json;

import java.util.ArrayList;
import java.util.List;

public class JsArray {

	private List<String> items;
	
	public JsArray(String arrayString) {
		arrayString = arrayString.trim();
		int end = arrayString.length();
		if (arrayString.charAt(0) != '[' || arrayString.charAt(end - 1) != ']') {
			throw new RuntimeException("Malformed JSON Array");
		}
		
		items = new ArrayList<String>();
		for (String item : arrayString.substring(1, end - 1).split(",")) {
			items.add(item.trim());
		}
	}
	
	public String getString(int idx) {
		return items.get(idx);
	}
	
	public JsObject getObject(int idx) {
		return new JsObject(getString(idx));
	}
	
	public JsArray getArray(int idx) {
		return new JsArray(getString(idx));
	}
	
	public int getInt(int idx) {
		return Integer.parseInt(getString(idx));
	}
	
	public double getDouble(int idx) {
		return Double.parseDouble(getString(idx));
	}
	
	public boolean getBoolean(int idx) {
		String value = getString(idx);
		if (value.equals("true")) {
			return true;
		} else if (value.equals("false")) {
			return false;
		}
		throw new RuntimeException("Not a Boolean value");
	}
		
	public String toString() {
		return "[" + join(items) + "]";
	}
	
	static String join(List<String> list) {
		if (list.size() == 0) {
			return "";
		}
		String s = list.get(0);
		for (int i = 1; i < list.size(); i ++) {
			s += "," + list.get(i);
		}
		return s;
	}
}
