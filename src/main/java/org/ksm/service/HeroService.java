package org.ksm.service;

import java.util.List;
import java.util.stream.Collectors;

import org.ksm.entity.HeroResponse;
import org.ksm.model.HeroRequest;
import org.ksm.repository.HeroRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.PathParam;
import lombok.extern.jbosslog.JBossLog;

/**
 * Service class for managing hero-related operations. 
 * This service provides functionality to retrieve and process hero information.
 */
@ApplicationScoped
@Transactional
@JBossLog
public class HeroService {
    
    @Inject
    HeroRepository heroRepository;

    public void loadDummyData() {
        HeroResponse h1 = new HeroResponse();
        h1.setAlias("Batman");
        h1.setName("Bruce Wayne");
        h1.setFlyable(false);
        HeroResponse h2 = new HeroResponse();
        h2.setAlias("Ironman");
        h2.setName("Tony Stark");
        h2.setFlyable(true);
        HeroResponse h3 = new HeroResponse();
        h3.setAlias("Spider-man");
        h3.setName("Peter Parker");
        h3.setFlyable(false);
        heroRepository.persist(List.of(h1, h2, h3));
        log.info("Dummy data successfully loaded");
    }

     /**
     * Retrieves hero with specified identifier
     *
     * @param id the hero identifier
     * @return the {@link HeroRequest} model of the matching identified hero
     * @throws NotFoundException if the hero does not exist
     */
    public HeroRequest getHero(@PathParam("id") String id) {
        log.infof("Retrieving hero with id %s", id);
        HeroResponse entity = heroRepository.findExistingById(id);
        return convertHeroEntityToModel(entity);
    }

    /**
     * Retrieves all heroes
     *
     * @return A list of {@link HeroRequest} models of heroes
     */
    public List<HeroRequest> getHeroes() {
        log.infof("Retrieving all heroes");
        List<HeroResponse> entities = heroRepository.findHeros();
        return entities.stream().map(this::convertHeroEntityToModel).collect(Collectors.toList());
    }

    /**
     * Saves the hero model after converting to entity
     *
     * @param model the hero to be converted and saved
     * @return a {@link HeroRequest} model of the persisted hero entity
     */
    public HeroRequest createHero(HeroRequest model) {
        HeroResponse entity = new HeroResponse();
        entity.setAlias(model.getAlias());
        entity.setName(model.getName());
        entity.setFlyable(model.getFlyable() != null ? model.getFlyable() : Boolean.FALSE);
        heroRepository.persist(entity);
        return convertHeroEntityToModel(entity);
    }

    /**
     * Updates all assignments (crew and/or members) assigned for the specified aircraft
     *
     * @param id The hero identifier
     * @param model The model containing fields to update the hero with
     * @return a {@link HeroRequest} model that contains updated hero information
     * @throws NotFoundException if the hero does not exist
     */
    public HeroRequest partialUpdateHero(String id, HeroRequest model) {
        log.infof("Retrieving hero: %s", id);
        HeroResponse entity = heroRepository.findExistingById(id);

        log.infof("Updating hero: %s", id);
        if (model.getAlias() != null) entity.setAlias(model.getAlias());
        if (model.getName() != null) entity.setName(model.getName());
        if (model.getFlyable() != null) entity.setFlyable(model.getFlyable());
        
        // merge
        heroRepository.getEntityManager().merge(entity);
        
        // flush and refresh
        heroRepository.flush();
        heroRepository.getEntityManager().refresh(entity);

        return convertHeroEntityToModel(entity);
    }

    //   * @param model The updated model for the database table to reflect

    /**
     * Delete an existing hero contract with the specified identifier
     *
     * @param id The hero identifier
     * @throws NotFoundException if the hero does not exist
     */
    public void deleteHero(String id) {
        log.infof("Deleting hero: %s", id);
        HeroResponse existing = heroRepository.findExistingById(id);
        heroRepository.delete(existing);
    }

      /**
     * Converts a {@link HeroResponse} entity to a {@link HeroRequest} model.
     *
     * @param entity The hero entity to convert
     * @return the hero model containing the data from the entity
     */
    private HeroRequest convertHeroEntityToModel(HeroResponse entity) {
        final HeroRequest model = new HeroRequest();
        model.setId(entity.getId());
        model.setAlias(entity.getAlias());
        model.setName(entity.getName());
        model.setFlyable(entity.getFlyable());
        return model;
    }        
}
