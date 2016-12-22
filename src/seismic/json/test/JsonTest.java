package seismic.json.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seismic.json.JsObject;

public class JsonTest {
	
	@Test
	public void testProcess() {
		
		// handle empty string
		JsObject obj = new JsObject("");
		assertEquals("{}", obj.toString());
		
		// remove whitespace
		obj = new JsObject(" { a : 4 } ");
		assertEquals("{a:4}", obj.toString());
		
		// use latter duplicate
		obj = new JsObject("{a:4,a:8}");
		assertEquals("{a:8}", obj.toString());
		
		// handle quoted keys
		obj = new JsObject("{'a':4,b:10,\"c\":16}");
		assertEquals("{a:4,b:10,c:16}", obj.toString());
		
		// handle commas in strings
		obj = new JsObject("{a:',',b:\",\"}");
		assertEquals("{a:',',b:\",\"}", obj.toString());
		
		// nested object
//		obj = new JsObject("{c:{d:9,e:18}},a:{b:5}");
//		assertEquals("{a:{b:5},c:{d:9,e:18}}", obj.toString());
	}
	
	@Test
	public void testToString() {
		// alphabetize by key
		JsObject obj = new JsObject("{a:4,c:16,b:10}");
		assertEquals("{a:4,b:10,c:16}", obj.toString());		
	}
	
	@Test
	public void testGetType() {
		// integer(), number()
		JsObject obj = new JsObject("{a:4,b:10.5,c:true,d:false}");
		assertEquals("4", obj.string("a"));
		assertEquals(4, obj.integer("a"));
		assertEquals("10.5", obj.string("b"));
		assertTrue(10.5 == obj.number("b"));
		assertEquals("true", obj.string("c"));
		assertTrue(obj.bool("c"));
		assertFalse(obj.bool("d"));
		
//		obj = new JsObject("{a:{b:7}}");
//		assertEquals(7, obj.object("a"));
	}
	
	@Test
	public void testNestedGetType() {
		
	}
}
