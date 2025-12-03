package org.ksm.repository;

import java.util.List;

import org.ksm.entity.Product;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.jbosslog.JBossLog;

/** Repository class for managing {@link Product} entities. */
@ApplicationScoped
@Transactional
@JBossLog
public class ProductRepository extends BaseRepository<Product, String>{
    
    /**
     * Finds an existing product by its Id
     * 
     * @param id the product identifier
     * @return the found product entity
     * @throws NotFoundException if entity is not found
     */
    public Product findExistingById(String id) {
        return findByIdOptional(id).orElseThrow(() -> new NotFoundException("Product not found: " + id));
    }

    /**
     * Finds all products
     *
     * @return the list of products
     */
    public List<Product> findProducts() {
        log.debug("Loading all products");
        return findAll().list();
    }
}
