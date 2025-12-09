package org.ksm.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ksm.model.Collectable;
import org.ksm.service.ProductService;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class ProductResourceTest {
    
    @Inject
    ProductResource resource;

    @InjectMock
    ProductService productService;

    @Test
    @DisplayName("Get all products - success")
    void getProducts_success() {
    // Arrange
    List<Collectable> models = List.of(
        new Collectable(
            "abcd-1234-efgh-5678",
            "Steam Siege",
            "Booster Box",
            null, // releaseDate
            null, // purchaseDate
            BigDecimal.valueOf(0), // releasePrice
            BigDecimal.valueOf(0), // purchasePrice
            false // specialEdition
        ));

        when(productService.getProducts()).thenReturn(models);

        // Act
        Response response = resource.getProducts();

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(models, response.getEntity());
        verify(productService).getProducts();
        verifyNoMoreInteractions(productService);
    }
}
