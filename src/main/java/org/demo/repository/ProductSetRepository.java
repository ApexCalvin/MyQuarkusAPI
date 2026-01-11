package org.demo.repository;

import org.demo.entity.ProductSet;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.extern.jbosslog.JBossLog;

/** Repository class for managing {@link ProductSet} entities. */
@ApplicationScoped
@Transactional
@JBossLog
public class ProductSetRepository extends BaseRepository<ProductSet, String>{
    
}
