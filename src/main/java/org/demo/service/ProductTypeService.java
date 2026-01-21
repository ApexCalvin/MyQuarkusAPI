package org.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.demo.entity.ProductType;
import org.demo.model.SealedProductType;
import org.demo.repository.ProductTypeRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.jbosslog.JBossLog;

/** Service class for managing product type-related operations. **/
@ApplicationScoped
@Transactional
@JBossLog
public class ProductTypeService {
    
    @Inject
    ProductTypeRepository typeRepository;

    /**
     * Retrieves all product types
     *
     * @return A list of {@link SealedProductType} models of product types
     */
    public List<SealedProductType> getAllProductTypes() {
        log.infof("Retrieving all product types");
        List<ProductType> entities = typeRepository.findAllProductTypes();
        return entities.stream().map(this::convertEntityToModel).collect(Collectors.toList());
    }

    /**
     * Retrieves product type with specified identifier
     *
     * @param name the product type identifier
     * @return the {@link SealedProductType} model of the matching identified product type
     * @throws NotFoundException if the product type does not exist
     */
    public SealedProductType getProductTypeById(String name) {
        log.infof("Retrieving product type with name: %s", name);
        ProductType entity = typeRepository.findExistingById(name);
        return convertEntityToModel(entity);
    }

    /**
     * Saves the product type model after converting to entity
     *
     * @param model the product type to be converted and saved
     * @return a {@link SealedProductType} model of the persisted product type entity
     */
    public SealedProductType createProductType(SealedProductType model) {
        log.infof("Creating new product type: %s", model);
        ProductType entity = convertModelToEntity(model);
        typeRepository.persist(entity);
        return convertEntityToModel(entity);
    }

    /**
     * Delete an existing product type with the specified identifier
     *
     * @param id The product type identifier
     * @throws NotFoundException if the product type does not exist
     */
    public void deleteProductType(String id) {
        log.warnf("Deleting product type: %s", id);
        ProductType existing = typeRepository.findExistingById(id);
        typeRepository.delete(existing);
    }

    public SealedProductType updateProductType(String id, SealedProductType request) {
        typeRepository.findExistingById(id);

        log.infof("Updating product type: %s", id);
        ProductType entity = convertModelToEntity(request);
        typeRepository.mergeAndFlush(entity);

        return convertEntityToModel(entity);
    }

    private SealedProductType convertEntityToModel(ProductType entity) {
        SealedProductType model = new SealedProductType();
        model.setName(entity.getName());
        model.setMsrp(entity.getMsrp());
        model.setBoosterCount(entity.getBoosterCount());
        return model;
    }

    private ProductType convertModelToEntity(SealedProductType model) {
        ProductType entity = new ProductType();
        entity.setName(model.getName());
        entity.setMsrp(model.getMsrp());
        entity.setBoosterCount(model.getBoosterCount());
        return entity;
    }
}
