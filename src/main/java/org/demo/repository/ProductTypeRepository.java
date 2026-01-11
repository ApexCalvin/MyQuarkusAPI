package org.demo.repository;

import org.demo.entity.ProductType;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.extern.jbosslog.JBossLog;

/** Repository class for managing {@link ProductType} entities. */
@ApplicationScoped
@Transactional
@JBossLog
public class ProductTypeRepository extends BaseRepository<ProductType, String> {
    
}
