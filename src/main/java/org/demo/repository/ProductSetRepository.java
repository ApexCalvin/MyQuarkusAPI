package org.demo.repository;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.demo.entity.ProductSet;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.jbosslog.JBossLog;

/** Repository class for managing {@link ProductSet} entities. */
@ApplicationScoped
@Transactional
@JBossLog
public class ProductSetRepository extends BaseRepository<ProductSet, String>{

    /**
     * Finds all product sets
     *
     * @return the list of product sets
     */
    public List<ProductSet> findAllSets() {
        log.debug("Loading all product sets");
        return findAll().list();
    }

    /**
     * Finds an existing product set by its Id
     * 
     * @param code the product set identifier
     * @return the found product set entity
     * @throws NotFoundException if entity is not found
     */
    public ProductSet findExistingById(String code) {
        return findByIdOptional(code).orElseThrow(() -> new NotFoundException("Set not found: " + code));
    }

    public ProductSet findExistingById(String id, LockModeType lockModeType) {
        return findByIdOptional(StringUtils.lowerCase(id), lockModeType)
                .orElseThrow(() -> new NotFoundException("Product set not found: " + id));
    }
}
