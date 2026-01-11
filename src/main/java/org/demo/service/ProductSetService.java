package org.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.demo.entity.ProductSet;
import org.demo.model.EnglishSet;
import org.demo.repository.ProductSetRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.jbosslog.JBossLog;

/** Service class for managing product set-related operations. **/
@ApplicationScoped
@Transactional
@JBossLog
public class ProductSetService {

    @Inject
    ProductSetRepository setRepository;

    /**
     * Retrieves all product sets
     *
     * @return A list of {@link EnglishSet} models of product sets
     */
    public List<EnglishSet> getAllSets() {
        log.infof("Retrieving all sets");
        List<ProductSet> entities = setRepository.findAllSets();
        return entities.stream().map(this::convertEntityToModel).collect(Collectors.toList());
    }

    /**
     * Retrieves product set with specified identifier
     *
     * @param code the product set identifier
     * @return the {@link EnglishSet} model of the matching identified product set
     * @throws NotFoundException if the product set does not exist
     */
    public EnglishSet getSetByCode(String code) {
        log.infof("Retrieving set with code: %s", code);
        ProductSet entity = setRepository.findExistingById(code);
        return convertEntityToModel(entity);
    }

    /**
     * Saves the product set model after converting to entity
     *
     * @param model the product set to be converted and saved
     * @return a {@link EnglishSet} model of the persisted product set entity
     */
    public EnglishSet createSet(EnglishSet model) {
        log.infof("Creating new set: %s", model);
        ProductSet entity = convertModelToEntity(model);
        setRepository.persist(entity);
        return convertEntityToModel(entity);
    }

    /**
     * Delete an existing product set with the specified identifier
     *
     * @param code The product set identifier
     * @throws NotFoundException if the product set does not exist
     */
    public void deleteSet(String code) {
        log.warnf("Deleting set: %s", code);
        ProductSet existing = setRepository.findExistingById(code);
        setRepository.delete(existing);
    }

    public EnglishSet updateSet(String code, EnglishSet request) {
        setRepository.findExistingById(code);

        log.infof("Updating set: %s", code);
        ProductSet entity = convertModelToEntity(request);
        setRepository.mergeAndFlush(entity);

        return convertEntityToModel(entity);
    }

    private EnglishSet convertEntityToModel(ProductSet entity) {
        EnglishSet model = new EnglishSet();
        model.setCode(entity.getCode());
        model.setName(entity.getName());
        model.setSpecialty(entity.isSpecialty());
        model.setReleaseDate(entity.getReleaseDate());
        model.setSeries(entity.getSeries());
        return model;
    }

    private ProductSet convertModelToEntity(EnglishSet model) {
        ProductSet entity = new ProductSet();
        entity.setCode(model.getCode());
        entity.setName(model.getName());
        entity.setSpecialty(model.isSpecialty());
        entity.setReleaseDate(model.getReleaseDate());
        entity.setSeries(model.getSeries());
        return entity;
    }
}