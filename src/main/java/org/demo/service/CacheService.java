package org.demo.service;

import jakarta.enterprise.context.ApplicationScoped;

/** Service class for managing cache operations. */
@ApplicationScoped
public class CacheService {
    
    // Cache name constants
    public static final String CACHE_PRODUCT_TYPE = "product-type";
    public static final String CACHE_PRODUCT_SET = "product-set";
}
