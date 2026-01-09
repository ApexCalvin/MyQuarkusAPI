package org.demo.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.demo.exception.UnprocessableEntityException;
import org.demo.model.Collectable;
import org.demo.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

    @Test
    @DisplayName("Get product by ID - success")
    void getProduct_success() {
        // Arrange
        String id = "1234-abcd";

        Collectable model = new Collectable();
        model.setId(id);
        model.setSetName("Steam Siege");
        model.setType("Booster Box");
        model.setReleasePrice(BigDecimal.valueOf(164.99));
        model.setPurchasePrice(BigDecimal.valueOf(120.00));
        model.setSpecialEdition(false);

        LocalDate ld_release = LocalDate.of(2025, 1, 1);
        LocalDate ld_purchase = LocalDate.of(2025, 2, 2);
        model.setReleaseDate(ld_release);
        model.setPurchaseDate(ld_purchase);

        when(productService.getProduct(id)).thenReturn(model);

        // Act
        Response response = resource.getProduct(id);

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(model, response.getEntity());
        verify(productService).getProduct(id);
        verifyNoMoreInteractions(productService);
    }

    @Test
    @DisplayName("Get product by ID - product not found")
    void getProduct_notFound() {
        // Arrange
        String id = "1234-abcd";
        String message = "Product Not Found";

        when(productService.getProduct(id)).thenThrow(new NotFoundException(message));

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, 
            () -> resource.getProduct(id));
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), exception.getResponse().getStatus());
        assertEquals(message, exception.getMessage());
        verify(productService).getProduct(id);
        verifyNoMoreInteractions(productService);
    }

    @Test
    @DisplayName("Create product - success")
    void createProduct_success() {
        // Arrange
        Collectable request = new Collectable();
        request.setSetName("Roaring Skies");
        request.setType("Elite Trainer Box");
        request.setReleasePrice(BigDecimal.valueOf(149.99));
        request.setPurchasePrice(BigDecimal.valueOf(130.00));
        request.setSpecialEdition(true);

        LocalDate ld_release = LocalDate.of(2024, 5, 15);
        LocalDate ld_purchase = LocalDate.of(2024, 6, 1);
        request.setReleaseDate(ld_release);
        request.setPurchaseDate(ld_purchase);

        Collectable responseModel = new Collectable();
        responseModel.setId("efgh-5678-abcd-1234");
        responseModel.setSetName(request.getSetName());
        responseModel.setType(request.getType());
        responseModel.setReleasePrice(request.getReleasePrice());
        responseModel.setPurchasePrice(request.getPurchasePrice());
        responseModel.setSpecialEdition(request.isSpecialEdition());
        responseModel.setReleaseDate(request.getReleaseDate());
        responseModel.setPurchaseDate(request.getPurchaseDate());

        when(productService.createProduct(request)).thenReturn(responseModel);

        // Act
        Response response = resource.createProduct(request);

        // Assert
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(responseModel, response.getEntity());
        verify(productService).createProduct(request);
        verifyNoMoreInteractions(productService);
    }

    @Test
    @DisplayName("Create product - ID must be empty")
    void createProduct_IDProvided() {
        // Arrange
        Collectable request = new Collectable();
        request.setId("should-not-be-set");
        request.setSetName("Roaring Skies");
        request.setType("Elite Trainer Box");

        // Act & Assert
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class, 
            () -> resource.createProduct(request));
        assertEquals("ID should not be provided when creating a new product.", exception.getMessage());
        verify(productService, never()).createProduct(request);
        verifyNoMoreInteractions(productService);
    }

    @Test
    @DisplayName("Delete product - success")
    void deleteProduct_success() {
        // Arrange
        String id = "abcd-1234";

        // Act
        Response response = resource.deleteProduct(id);

        // Assert
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(productService).deleteProduct(id);
        verifyNoMoreInteractions(productService);
    }

    @Test
    @DisplayName("Delete product - product not found")
    void deleteProduct_notFound() {
        // Arrange
        String id = "abcd-1234";
        String message = "Product Not Found";

        doThrow(new NotFoundException(message)).when(productService).deleteProduct(id);

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, 
            () -> resource.deleteProduct(id));
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), exception.getResponse().getStatus());
        assertEquals(message, exception.getMessage());
        verify(productService).deleteProduct(id);
        verifyNoMoreInteractions(productService);
    }

    @Test
    @DisplayName("Update product - success")
    void updateProduct_success() {
        // Arrange
        String id = "abcd-1234";

        Collectable request = new Collectable();
        request.setId(id);
        request.setSetName("Ancient Origins");
        request.setType("Booster Bundle");
        request.setReleasePrice(BigDecimal.valueOf(99.99));
        request.setPurchasePrice(BigDecimal.valueOf(85.00));
        request.setSpecialEdition(false);

        LocalDate ld_release = LocalDate.of(2023, 11, 20);
        LocalDate ld_purchase = LocalDate.of(2023, 12, 5);
        request.setReleaseDate(ld_release);
        request.setPurchaseDate(ld_purchase);

        Collectable responseModel = new Collectable();
        responseModel.setId(id);
        responseModel.setSetName(request.getSetName());
        responseModel.setType(request.getType());
        responseModel.setReleasePrice(request.getReleasePrice());
        responseModel.setPurchasePrice(request.getPurchasePrice());
        responseModel.setSpecialEdition(request.isSpecialEdition());
        responseModel.setReleaseDate(request.getReleaseDate());
        responseModel.setPurchaseDate(request.getPurchaseDate());

        when(productService.updateProduct(id, request)).thenReturn(responseModel);

        // Act
        Response response = resource.updateProduct(id, request);

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(responseModel, response.getEntity());
        verify(productService).updateProduct(id, request);
        verifyNoMoreInteractions(productService);
    }

    @Test
    @DisplayName("Update product - ID mismatch")
    void updateProduct_IDMismatch() {
        // Arrange
        String pathId = "abcd-1234";
        String bodyId = "wxyz-5678";

        Collectable request = new Collectable();
        request.setId(bodyId);
        request.setSetName("Ancient Origins");
        request.setType("Booster Bundle");

        // Act & Assert
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class, 
            () -> resource.updateProduct(pathId, request));
        assertEquals("Product ID in the path must match the ID in the request body.", exception.getMessage());
        verify(productService, never()).updateProduct(pathId, request);
        verifyNoMoreInteractions(productService);
    }

    @Test
    @DisplayName("Update product - product not found")
    void updateProduct_notFound() {
        // Arrange
        String id = "abcd-1234";

        Collectable request = new Collectable();
        request.setId(id);
        request.setSetName("Ancient Origins");
        request.setType("Booster Bundle");

        String message = "Product Not Found";
        when(productService.updateProduct(id, request)).thenThrow(new NotFoundException(message));

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, 
            () -> resource.updateProduct(id, request));
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), exception.getResponse().getStatus());
        assertEquals(message, exception.getMessage());
        verify(productService).updateProduct(id, request);
        verifyNoMoreInteractions(productService);
    }
}    
