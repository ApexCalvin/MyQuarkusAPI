package org.ksm.service;

import java.util.List;
import java.util.stream.Collectors;

import org.ksm.entity.Product;
import org.ksm.model.Collectable;
import org.ksm.repository.ProductRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.PathParam;
import lombok.extern.jbosslog.JBossLog;

/** Service class for managing product-related operations. **/
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

    /**
     * Retrieves product with specified identifier
     *
     * @param id the product identifier
     * @return the {@link Collectable} model of the matching identified product
     * @throws NotFoundException if the product does not exist
     */
    public Collectable getProduct(@PathParam("id") String id) {
        log.infof("Retrieving product with ID: %s", id);
        Product entity = productRepository.findExistingById(id);
        return convertProductEntityToModel(entity);
    }

    /**
     * Saves the product model after converting to entity
     *
     * @param model the product to be converted and saved
     * @return a {@link Collectable} model of the persisted product entity
     */
    public Collectable createProduct(Collectable model) {
        log.infof("Creating new product: %s", model);
        Product entity = convertCollectableModelToEntity(model);
        productRepository.persist(entity);
        return convertProductEntityToModel(entity);
    }

    /**
     * Delete an existing product with the specified identifier
     *
     * @param id The product identifier
     * @throws NotFoundException if the product does not exist
     */
    public void deleteProduct(String id) {
        log.infof("Deleting product: %s", id);
        Product existing = productRepository.findExistingById(id);
        productRepository.delete(existing);
    }

    public Collectable updateProduct(String id, Collectable request) {
        productRepository.findExistingById(id);

        log.infof("Updating product: %s", id);
        Product entity = convertCollectableModelToEntity(request);
        productRepository.mergeAndFlush(entity);
        
        return convertProductEntityToModel(entity);
    }

    private Collectable convertProductEntityToModel(Product entity) {
        Collectable model = new Collectable();
        model.setId(entity.getId());
        model.setSetName(entity.getSetName());
        model.setType(entity.getProductType());
        model.setReleaseDate(entity.getReleaseDate());
        model.setPurchaseDate(entity.getPurchaseDate());
        model.setReleasePrice(entity.getReleasePrice());
        model.setPurchasePrice(entity.getPurchasePrice());
        model.setSpecialEdition(entity.isSpecialEdition());
        return model;
    }

    private Product convertCollectableModelToEntity(Collectable model) {
        Product entity = new Product();
        entity.setId(model.getId());
        entity.setSetName(model.getSetName());
        entity.setProductType(model.getType());
        entity.setReleaseDate(model.getReleaseDate());
        entity.setPurchaseDate(model.getPurchaseDate());
        entity.setReleasePrice(model.getReleasePrice());
        entity.setPurchasePrice(model.getPurchasePrice());
        entity.setSpecialEdition(model.isSpecialEdition());
        return entity;
    }
}
