package org.ksm.repository;

import java.util.List;

import org.ksm.entity.HeroResponse;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.jbosslog.JBossLog;

/** Repository class for managing {@link HeroResponse} entities. */
@ApplicationScoped
@Transactional
@JBossLog
public class HeroRepository implements PanacheRepositoryBase<HeroResponse, String> {

    /**
     * Finds an existing hero by its Id
     * 
     * @param id the hero identifier
     * @return the found hero entity
     * @throws NotFoundException if entity is not found
     */
    public HeroResponse findExistingById(String id) {
        return findByIdOptional(id)
            .orElseThrow(() -> new NotFoundException("Hero not found: " + id));
    }

    /**
     * Finds all heroes
     *
     * @return the list of heroes
     */
    public List<HeroResponse> findHeros() {
        log.debug("Loading all heroes");
        return findAll().list();
    }
}