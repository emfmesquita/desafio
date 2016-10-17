package com.edson.core;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.edson.entity.SKU;

@Component
@Transactional
public class SKUService {

	@PersistenceContext
	private EntityManager em;

	public void createSKU() {
		SKU sku = new SKU();
		this.em.persist(sku);
	}

	@SuppressWarnings("unchecked")
	public List<SKU> list() {
		String qString = String.format("SELECT x FROM %s x", SKU.class.getSimpleName());
		Query q = this.em.createQuery(qString);
		return q.getResultList();
	}
}
