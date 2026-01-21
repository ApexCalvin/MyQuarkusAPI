package org.demo.repository;

import java.util.List;

import org.demo.entity.ProductType;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.jbosslog.JBossLog;

/** Repository class for managing {@link ProductType} entities. */
@ApplicationScoped
@Transactional
@JBossLog
public class ProductTypeRepository extends BaseRepository<ProductType, String> {
    
    /**
     * Finds all product types
     *
     * @return the list of product types
     */
    public List<ProductType> findAllProductTypes() {
        log.debug("Loading all product types");
        return findAll().list();
    }

    /**
     * Finds an existing product type by its Id
     * 
     * @param id the product type identifier
     * @return the found product type entity
     * @throws NotFoundException if entity is not found
     */
    public ProductType findExistingById(String id) {
        return findByIdOptional(id).orElseThrow(() -> new NotFoundException("Product type not found: " + id));
    }
}
