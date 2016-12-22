package seismic.json.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seismic.json.JsObject;
import seismic.json.JsArray;

public class JsonTest {
	
	@Test
	public void testProcess() {
		
		// handle empty string
		JsObject obj = new JsObject("");
		assertEquals("{}", obj.toString());
		
		// remove whitespace
		obj = new JsObject(" { a : 4 } ");
		assertEquals("{\"a\":4}", obj.toString());
		
		// use latter duplicate
		obj = new JsObject("{a:4,a:8}");
		assertEquals("{\"a\":8}", obj.toString());
		
		// don't add quotes on double-quoted keys
		obj = new JsObject("{'a':4,b:10,\"c\":16}");
		assertEquals("{\"'a'\":4,\"b\":10,\"c\":16}", obj.toString());
		
		// handle commas in strings
		obj = new JsObject("{a:\",\",b:\",\"}");
		assertEquals("{\"a\":\",\",\"b\":\",\"}", obj.toString());
		
		// handle colons in strings
		obj = new JsObject("{a:\":\"}");
		assertEquals("{\"a\":\":\"}", obj.toString());
		
		// nested getObject, no quotes on nested strings
		obj = new JsObject("{c:{d:9,e:18},a:{b:5}}");
		assertEquals("{\"a\":{b:5},\"c\":{d:9,e:18}}", obj.toString());
	}
	
	@Test
	public void testToString() {
		// alphabetize by key
		JsObject obj = new JsObject("{a:4,c:16,b:10}");
		assertEquals("{\"a\":4,\"b\":10,\"c\":16}", obj.toString());		
	}
	
	@Test
	public void testGetType() {
		// getInt(), number()
		JsObject obj = new JsObject("{a:4,b:10.5,c:true,d:false}");
		assertEquals("4", obj.getString("a"));
		assertEquals(4, obj.getInt("a"));
		assertEquals("10.5", obj.getString("b"));
		assertTrue(10.5 == obj.getDouble("b"));
		assertEquals("true", obj.getString("c"));
		assertTrue(obj.getBoolean("c"));
		assertFalse(obj.getBoolean("d"));
		
		obj = new JsObject("{a:{b:7}}");
		assertEquals("{\"b\":7}", obj.getObject("a").toString());
		assertEquals(7, obj.getObject("a").getInt("b"));
	}
	
	@Test
	public void testArray() {
		
		JsArray arr = new JsArray("[3,4,5]");
		assertEquals("[3,4,5]", arr.toString());
		
		arr = new JsArray("[\"seismic\", 10, 15, false]");
		assertEquals("[\"seismic\",10,15,false]", arr.toString());
		assertEquals("\"seismic\"", arr.getString(0));
		assertEquals(10, arr.getInt(1));
		assertEquals(15, arr.getInt(2));
		assertFalse(arr.getBoolean(3));
	}
}
