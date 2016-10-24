package com.edson.test;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.edson.core.EpiconService;
import com.edson.core.SKUService;
import com.edson.entity.SKU;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-spring-config.xml")
public class NotificationTest {

	@Autowired
	private SKUService skuService;
	
	private EpiconService epiconServiceMock;
	private EntityManager emMock;
	
	@Test
	public void test() throws Exception {
		epiconServiceMock = Mockito.mock(EpiconService.class);
		emMock = Mockito.mock(EntityManager.class);
		ReflectionTestUtils.setField(skuService, "epiconService", epiconServiceMock);
		ReflectionTestUtils.setField(skuService, "em", emMock);
		
		
		SKU sku1 = new SKU();
		sku1.setNome("Produto 1");
		sku1.setEpiconId("322358");
		Mockito.when(epiconServiceMock.getSku("270229", "322358")).thenReturn(sku1);
		
		SKU sku2 = new SKU();
		sku2.setNome("Produto 2");
		sku2.setEpiconId("322359");
		Mockito.when(epiconServiceMock.getSku("270230", "322359")).thenReturn(sku2);
		
		String notification = TestUtils.getContent("notification.json");
		skuService.createFromNotifications(notification);
		
		Mockito.verify(emMock).persist(sku1);		
		Mockito.verify(emMock).persist(sku2);
    }
}
