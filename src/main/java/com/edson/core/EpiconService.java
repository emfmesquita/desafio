package com.edson.core;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.edson.entity.SKU;

@Component
public class EpiconService {

	private static String BASE_REQUEST_URL = "https://sandboxmhubapi.epicom.com.br/v1/marketplace/produtos/%s/skus/%s";
	private static String USERNAME = "897F8D21A9F5A";
	private static String PASSWORD = "Ip15q6u7X15EP22GS36XoNLrX2Jz0vqq";

	private static String RESPONSE_FAIL_MESSAGE = "Response fail with status %d:\n%s";
	
	@Autowired
	private SKUHelper skuHelper;

	public SKU getSku(String productId, String skuId) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpget = buildRequest(productId, skuId);
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				int statusCode = response.getStatusLine().getStatusCode();
				String responseData = EntityUtils.toString(response.getEntity());
				if (200 != statusCode) {
					throw new RuntimeException(String.format(RESPONSE_FAIL_MESSAGE, statusCode, responseData));
				}
				return skuHelper.JsonToSku(responseData);
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			throw new RuntimeException("Fail to get sku info from epicon", e);
		} catch (IOException e) {
			throw new RuntimeException("Fail to get sku info from epicon", e);
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				// only tries to close
			}
		}
	}

	private HttpGet buildRequest(String productId, String skuId) {
		String url = String.format(BASE_REQUEST_URL, productId, skuId);
		HttpGet httpget = new HttpGet(url);
		httpget.addHeader("Authorization", buildAuthHeader());
		return httpget;
	}

	private String buildAuthHeader() {
		return "Basic " + Base64.encodeBase64String((USERNAME + ":" + PASSWORD).getBytes());
	}
}
