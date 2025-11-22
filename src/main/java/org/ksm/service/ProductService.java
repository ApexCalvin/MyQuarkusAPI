package org.ksm.service;

import java.util.List;
import java.util.stream.Collectors;

import org.ksm.entity.Product;
import org.ksm.model.Collectable;
import org.ksm.repository.ProductRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.jbosslog.JBossLog;

/** Service class for managing hero-related operations. **/
@ApplicationScoped
@Transactional
@JBossLog
public class ProductService {

    @Inject
    ProductRepository productRepository;

    /**
     * Retrieves all produccts
     *
     * @return A list of {@link Collectable} models of products
     */
    public List<Collectable> getProducts() {
        log.infof("Retrieving all products");
        List<Product> entities = productRepository.findProducts();
        return entities.stream().map(this::convertProductEntityToModel).collect(Collectors.toList());
    }

    private Collectable convertProductEntityToModel(Product entity) {
        Collectable model = new Collectable();
        model.setId(entity.getId());
        model.setSet(entity.getSetName());
        model.setType(entity.getProductType());
        model.setReleaseDate(entity.getReleaseDate());
        model.setPurchaseDate(entity.getPurchaseDate());
        model.setReleasePrice(entity.getReleasePrice());
        model.setPurchasePrice(entity.getPurchasePrice());
        model.setSpecialEdition(entity.isSpecialEdition());
        return model;
    }
    
}
