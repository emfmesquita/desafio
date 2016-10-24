package com.edson.core;

import java.util.List;

import org.springframework.stereotype.Component;

import com.edson.entity.SKU;
import com.google.gson.Gson;

@Component
public class SKUHelper {

	private static String FAIL_TO_PARSE_MESSAGE = "Fail to parse sku from:\n%s";
	private static String FAIL_TO_GENERATE_JSON_MESSAGE = "Fail to generate JSON.";

	public SKU JsonToSku(String jsonData) {
		Gson gson = new Gson();
		SKU sku = null;
		try {
			sku = gson.fromJson(jsonData, SKU.class);
		} catch (Exception e) {
			throw new RuntimeException(String.format(FAIL_TO_PARSE_MESSAGE, jsonData), e);
		}
		return sku;
	}

	public String SkuToJson(SKU sku) {
		return toJson(sku);
	}

	public String SkusToJson(List<SKU> skus) {
		return toJson(skus);
	}

	private String toJson(Object object) {
		Gson gson = new Gson();
		String json = null;
		try {
			json = gson.toJson(object);
		} catch (Exception e) {
			throw new RuntimeException(FAIL_TO_GENERATE_JSON_MESSAGE, e);
		}
		return json;
	}
}
