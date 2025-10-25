package org.ksm.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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
        String id = "1234-abcd";

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
        verifyNoMoreInteractions(heroService);
    }

    @Test
    @DisplayName("Get hero by Id - hero not found")
    void getHero_notFound() {
        // Arrange
        String id = "1234-abcd";
        String message = "Hero Not Found";

        when(heroService.getHero(id)).thenThrow(new NotFoundException(message));

        // Act & Assert
        NotFoundException exception = 
            assertThrows(NotFoundException.class, () -> resource.getHero(id));
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), exception.getResponse().getStatus());
        assertEquals(message, exception.getMessage());
        verify(heroService).getHero(id);
        verifyNoMoreInteractions(heroService);
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
        verifyNoMoreInteractions(heroService);
    }

    @Test
    @DisplayName("Delete hero - success")
    void deleteHero_success() {
        // Arrange
        String id = "abcd-1234-efgh-5678";

        // Act
        Response response = resource.deleteHero(id);

        // Assert
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(heroService).deleteHero(id);
        verifyNoMoreInteractions(heroService);
    }

    @Test
    @DisplayName("Delete hero - hero not found")
    void deleteHero_notFound() {
        // Arrange
        String id = "abcd-1234-efgh-5678";

        doThrow(new NotFoundException("Hero not found"))
            .when(heroService)
            .deleteHero(id);
        
        // Act & Assert
        NotFoundException exception = 
            assertThrows(NotFoundException.class, () -> resource.deleteHero(id));
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), exception.getResponse().getStatus());
        assertEquals("Hero not found", exception.getMessage());
        verify(heroService).deleteHero(id);
        verifyNoMoreInteractions(heroService);
    }

    private List<HeroRequest> createTestHeroes() {
        HeroRequest h1 = new HeroRequest();
        h1.setAlias("Batman");
        h1.setName("Bruce Wayne");
        h1.setFlyable(false);
        HeroRequest h2 = new HeroRequest();
        h2.setAlias("Ironman");
        h2.setName("Tony Stark");
        h2.setFlyable(true);
        HeroRequest h3 = new HeroRequest();
        h3.setAlias("Spider-man");
        h3.setName("Peter Parker");
        h3.setFlyable(false);
        return List.of(h1, h2, h3);
    }

    @Test
    @DisplayName("template")
    void template() {
        // Arrange
        
        // Act

        // Assert
    }
}