package com.edson.core;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.edson.entity.SKU;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Component
@Transactional
public class SKUService {

	private static String CREATE_NOTIFICATION_TYPE = "criacao_sku";

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private EpiconService epiconService;

	@Autowired
	private SKUHelper skuHelper;

	public void createFromNotifications(String notifications) throws InterruptedException {
		Type listType = new TypeToken<List<Notification>>() {
		}.getType();
		List<Notification> notificationList = new Gson().fromJson(notifications, listType);

		if (CollectionUtils.isEmpty(notificationList)) {
			return;
		}

		Executor executor = Executors.newCachedThreadPool();
		CompletionService<SKU> completionService = new ExecutorCompletionService<SKU>(executor);

		int submited = 0;
		for (Notification notification : notificationList) {
			if (notification == null || !CREATE_NOTIFICATION_TYPE.equals(notification.getTipo())) {
				continue;
			}
			final String productId = notification.getParametros().getIdProduto();
			final String skuId = notification.getParametros().getIdSku();

			submited++;
			completionService.submit(new Callable<SKU>() {
				public SKU call() throws Exception {
					return epiconService.getSku(productId, skuId);
				}
			});
		}

		int received = 0;
		boolean errors = false;
		List<SKU> skus = new ArrayList<SKU>();

		while (received < submited && !errors) {
			Future<SKU> resultFuture = completionService.take();
			try {
				SKU result = resultFuture.get();
				skus.add(result);
				System.out.println(result.getId());
				received++;
			} catch (Exception e) {
				errors = true;
			}
		}

		for (SKU sku : skus) {
			this.em.persist(sku);
		}
	}

	public String createFromJson(String skuJson) {
		SKU sku = skuHelper.JsonToSku(skuJson);
		sku.setId(null);
		this.em.persist(sku);
		return sku.getId();
	}

	public void updateFromJson(String skuJson) {
		SKU sku = skuHelper.JsonToSku(skuJson);
		findSKU(sku.getId()); // verifies if there is a sku with the given id
		this.em.merge(sku);
	}

	public void deleteSku(String id) {
		SKU sku = findSKU(id);
		this.em.remove(sku);
	}

	public String find(String id) {
		SKU sku = findSKU(id);
		return skuHelper.SkuToJson(sku);
	}	

	@SuppressWarnings("unchecked")
	public String list() {
		String qString = String.format("SELECT x FROM %s x", SKU.class.getSimpleName());
		Query q = this.em.createQuery(qString);
		List<SKU> skus = q.getResultList();
		return skuHelper.SkusToJson(skus);
	}
	
	@SuppressWarnings("unchecked")
	public String available(){
		String qString = String.format("SELECT x FROM %s x where x.disponivel = :disponivel order by x.preco ASC", SKU.class.getSimpleName());
		Query q = this.em.createQuery(qString);
		q.setParameter("disponivel", true);
		List<SKU> skus = q.getResultList();
		return skuHelper.SkusToJson(skus);
	}

	private SKU findSKU(String id) {
		if (StringUtils.isEmpty(id)) {
			throw new RuntimeException("SKU id is missing.");
		}

		SKU dbSKU = this.em.find(SKU.class, id);
		if (dbSKU == null) {
			throw new RuntimeException(String.format("SKU with id %s not found.", id));
		}
		return dbSKU;
	}
}
