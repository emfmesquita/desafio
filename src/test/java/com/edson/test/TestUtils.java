package com.edson.test;

import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

public class TestUtils {
	public static String getContent(String resource) throws Exception{
		ClassLoader classLoader = TestUtils.class.getClassLoader();
		return IOUtils.toString(classLoader.getResource("json/" + resource).openStream(), Charset.defaultCharset());
	}
}
