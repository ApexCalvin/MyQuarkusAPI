package org.ksm;

import com.mysql.cj.util.StringUtils;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
@Transactional
public class HeroRepository implements PanacheRepository<Hero> {

    /**
     * Finds an existing hero by its composite ID.
     * 
     * @param heroId The hero ID
     * @return The matching Hero entity
     * @throws NotFoundException if no Hero is found with the given id
     */
    public Hero findExistingById(String heroId) {
        Long id;
        try {
            id = Long.valueOf(heroId);
        } catch (Exception e) {
            throw new BadRequestException("Invalid hero ID format: " + heroId);
        }
        return findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Hero ID " + heroId+ " not found."));
    }
}