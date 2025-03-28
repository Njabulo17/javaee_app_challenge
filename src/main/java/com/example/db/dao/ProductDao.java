package com.example.db.dao;

import com.example.db.entity.ProductEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@Stateless
@Transactional
public class ProductDao {

    @PersistenceContext(unitName = "pu")
    private EntityManager em;

    public ProductEntity createProduct(ProductEntity productEntity) {
        em.persist(productEntity);
        return productEntity;
    }

    public ProductEntity updateProduct(ProductEntity productEntity) {
        em.merge(productEntity);
        return productEntity;
    }

    public ProductEntity getProductById(Long id) {
        return em.find(ProductEntity.class, id);
    }

    public List<ProductEntity> getAllProducts() {
        return em.createQuery("SELECT p FROM ProductEntity p", ProductEntity.class).getResultList();
    }

    public void deleteProductById(Long id) {
        ProductEntity product = em.find(ProductEntity.class, id);
        if (product != null) {
            em.remove(product);
        }
    }
}