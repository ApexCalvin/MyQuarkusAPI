package org.ksm.service;

import java.util.List;

import org.ksm.entity.HeroResponse;
import org.ksm.model.HeroRequest;
import org.ksm.repository.HeroRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
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

    public HeroRequest getHero(@PathParam("id") Long id) {
        HeroResponse entity = heroRepository.findExistingById(id);
        return new HeroRequest();
    }

    public List<HeroRequest> getHeros() {
        List<HeroResponse> entities = heroRepository.findHeros();
        return List.of(new HeroRequest());
    }
}
