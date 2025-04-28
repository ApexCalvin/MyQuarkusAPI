package org.ksm;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HeroRepository implements PanacheRepository<Hero> {

}