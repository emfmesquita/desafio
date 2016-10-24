package com.edson.test;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.edson.core.SKUService;
import com.edson.entity.SKU;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-spring-config.xml")
public class ApiTest {

	@Autowired
	private SKUService skuService;
	
	private EntityManager emMock;
	
	@Before
	public void before(){
		emMock = Mockito.mock(EntityManager.class);
		ReflectionTestUtils.setField(skuService, "em", emMock);
	}
	
	
	@Test
	public void createTest() throws Exception {		
		final String dbId = "12345";		
		SKU sku = createSKU(dbId);
		
		Mockito.doAnswer(new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) throws Throwable {
				SKU argSKU = (SKU)invocation.getArguments()[0];
				argSKU.setId(dbId);
				return null;
			}
		}).when(emMock).persist(ArgumentMatchers.any(SKU.class));
		
		String json = TestUtils.getContent("create.json");
		String returnId = skuService.createFromJson(json);
		
		Mockito.verify(emMock).persist(sku);
		Assert.assertEquals(dbId, returnId);
    }
	
	@Test
	public void updateTest() throws Exception {
		
		final String dbId = "1";		
		SKU sku = createSKU(dbId);
		
		Mockito.when(emMock.find(SKU.class, dbId)).thenReturn(new SKU());
		
		String json = TestUtils.getContent("update.json");
		skuService.updateFromJson(json);
		
		Mockito.verify(emMock).find(SKU.class, dbId);
		Mockito.verify(emMock).merge(sku);
    }
	
	@Test
	public void deleteTest() throws Exception {		
		final String dbId = "1";		
		SKU sku = createSKU(dbId);
		
		Mockito.when(emMock.find(SKU.class, dbId)).thenReturn(sku);
		skuService.deleteSku(dbId);
		
		Mockito.verify(emMock).find(SKU.class, dbId);
		Mockito.verify(emMock).remove(sku);
    }
	
	@Test
	public void findTest() throws Exception {		
		final String dbId = "1";		
		SKU sku = createSKU(dbId);
		
		Mockito.when(emMock.find(SKU.class, dbId)).thenReturn(sku);		
		String result = skuService.find(dbId);
		
		String json = TestUtils.getContent("find.json");
		
		Mockito.verify(emMock).find(SKU.class, dbId);
		Assert.assertEquals(json, result);
    }
	
	@Test
	public void listTest() throws Exception {
		Query qMock = Mockito.mock(Query.class);
		
		SKU sku1 = createSKU("1", "Produto 1");
		SKU sku2 = createSKU("2", "Produto 2");
		List<SKU> skus = Arrays.asList(sku1, sku2);
		
		Mockito.when(emMock.createQuery(ArgumentMatchers.anyString())).thenReturn(qMock);
		Mockito.when(qMock.getResultList()).thenReturn(skus);
		String result = skuService.list();
		
		String json = TestUtils.getContent("list.json");
		
		Mockito.verify(emMock).createQuery(ArgumentMatchers.anyString());
		Mockito.verify(qMock).getResultList();
		Assert.assertEquals(json, result);
    }
	
	
	private SKU createSKU(String id){
		return createSKU(id, "Produto 1");
	}
	
	private SKU createSKU(String id, String name){
		SKU sku = new SKU();
		sku.setNome(name);
		sku.setCodigo("SKU1");
		sku.setForaDeLinha(false);
		sku.setPreco(99.99);
		sku.setDisponivel(true);
		sku.setEstoque(9);
		sku.setAtivo(true);
		sku.setId(id);
		return sku;
	}
}
