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
        HeroResponse h1 = new HeroResponse(123L, "Batman", "Bruce Wayne", false);
        HeroResponse h2 = new HeroResponse(234L, "Ironman", "Tony Stark", true);
        HeroResponse h3 = new HeroResponse(345L, "Spider-man", "Peter Parker", false);
        heroRepository.persist(List.of(h1, h2, h3));
    }

     /**
     * Retrieves hero with specified identifier
     *
     * @param id the hero identifier
     * @return the {@link HeroRequest} model of the matching identified hero
     * @throws NotFoundException if the hero does not exist
     */
    public HeroRequest getHero(@PathParam("id") Long id) {
        log.infof("Retrieving hero with id %d", id);
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
        model.setFlyable(entity.isFlyable());
        return model;
    }        
}
