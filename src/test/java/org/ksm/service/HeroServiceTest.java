package org.ksm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ksm.entity.HeroResponse;
import org.ksm.model.HeroRequest;
import org.ksm.repository.HeroRepository;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@QuarkusTest
public class HeroServiceTest {

    @Inject
    HeroService service;

    @InjectMock
    HeroRepository heroRepository;

    @Test
    @DisplayName("Get hero by Id - success")
    void getHero_success() {
        // Arrange
        Long id = 123L;

        HeroResponse entity = new HeroResponse();
        entity.setId(id);
        entity.setAlias("Spider-Man");
        entity.setName("Peter Parker");
        entity.setFlyable(false);

        when(heroRepository.findExistingById(id)).thenReturn(entity);

        // Act
        HeroRequest actual = service.getHero(id);

        // Assert
        assertEquals(entity.getId(), actual.getId());
        assertEquals(entity.getAlias(), actual.getAlias());
        assertEquals(entity.getName(), actual.getName());
        assertFalse(actual.isFlyable());
        verify(heroRepository).findExistingById(id);
    }

    @Test
    @DisplayName("Get hero by Id - hero not found")
    void getHero_notFound() {
        // Arrange
        Long id = 123L;
        String message = "Hero Not Found";

        when(heroRepository.findExistingById(id)).thenThrow(new NotFoundException(message));

        // Act & Assert
        NotFoundException exception = 
            assertThrows(NotFoundException.class, () -> service.getHero(id));
        assertEquals(message, exception.getMessage());
        verify(heroRepository).findExistingById(id);
    }

    @Test
    @DisplayName("Get all heroes - success")
    void getHeroes_success() {
        // Arrange
        HeroResponse entity = new HeroResponse(123L, "Batman", "Bruce Wayne", false);

        when(heroRepository.findHeros()).thenReturn(List.of(entity));

        // Act
        List<HeroRequest> actual = service.getHeroes();

        // Assert
        assertNotNull(actual);
        assertEquals(1, actual.size());

        HeroRequest model = actual.getFirst();
        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getAlias(), model.getAlias());
        assertEquals(entity.getName(), model.getName());
        assertFalse(model.isFlyable());
        
        verify(heroRepository).findHeros();
    }
    
    @Test
    @DisplayName("")
    void template() {
        // Arrange
        
        // Act

        // Assert
    }
}
