package org.ksm.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.ksm.model.HeroRequest;
import org.ksm.service.HeroService;

@QuarkusTest
class HeroResourceTest {

    @Inject
    HeroResource resource;

    @InjectMock
    HeroService heroService;

    @Test
    @DisplayName("Get hero by Id - success")
    void getHero_success() {
        // Arrange
        Long id = 123L;

        HeroRequest model = new HeroRequest();
        model.setId(id);
        model.setAlias("Spider-Man");
        model.setName("Peter Parker");
        model.setFlyable(false);

        when(heroService.getHero(id)).thenReturn(model);

        // Act
        Response response = resource.getHero(id);

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(model, response.getEntity());
        verify(heroService).getHero(id);
    }

    @Test
    @DisplayName("Get hero by Id - hero not found")
    void getHero_notFound() {
        // Arrange
        Long id = 123L;
        String message = "Hero Not Found";

        when(heroService.getHero(id)).thenThrow(new NotFoundException(message));

        // Act & Assert
        NotFoundException exception = 
            assertThrows(NotFoundException.class, () -> resource.getHero(id));
        assertEquals(message, exception.getMessage());
        verify(heroService).getHero(id);
    }

    @Test
    @DisplayName("Get all heroes - success")
    void getHeroes_success() {
        // Arrange
        List<HeroRequest> models = createTestHeroes();

        when(heroService.getHeroes()).thenReturn(models);

        // Act
        Response response = resource.getHeroes();

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(models, response.getEntity());
        verify(heroService).getHeroes();
    }

    private List<HeroRequest> createTestHeroes() {
        HeroRequest h1 = new HeroRequest(123L, "Batman", "Bruce Wayne", false);
        HeroRequest h2 = new HeroRequest(234L, "Ironman", "Tony Stark", true);
        HeroRequest h3 = new HeroRequest(345L, "Spider-man", "Peter Parker", false);
        return List.of(h1, h2, h3);
    }

    @Test
    @DisplayName("")
    void template() {
        // Arrange
        
        // Act

        // Assert
    }
}