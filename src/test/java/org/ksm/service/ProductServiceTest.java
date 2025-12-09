package org.ksm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ksm.entity.Product;
import org.ksm.model.Collectable;
import org.ksm.repository.ProductRepository;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@QuarkusTest
public class ProductServiceTest {
    
    @Inject
    ProductService service;

    @InjectMock
    ProductRepository productRepository;

    @Test
    @DisplayName("Get all products - success")
    void getProducts_success() {
        //Arrange
        Product entity = new Product();
        entity.setId("prod-1234");
        entity.setSetName("Alpha Set");
        entity.setProductType("Box");
        entity.setReleasePrice(BigDecimal.valueOf(200.00));
        entity.setPurchasePrice(BigDecimal.valueOf(164.99));
        entity.setSpecialEdition(false);

        LocalDate ld_release = LocalDate.of(2025, 1, 1);
        LocalDate ld_purchase = LocalDate.of(2025, 2, 2);
        entity.setReleaseDate(ld_release);
        entity.setPurchaseDate(ld_purchase);


        when(productRepository.findProducts()).thenReturn(List.of(entity));

        // Act
        List<Collectable> actual = service.getProducts();

        // Assert
        assertNotNull(actual);
        assertEquals(1, actual.size());

        Collectable result = actual.getFirst();
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getSetName(), result.getSetName());
        assertEquals(entity.getProductType(), result.getType());
        assertEquals(entity.getReleasePrice(), result.getReleasePrice());
        assertEquals(entity.getPurchasePrice(), result.getPurchasePrice());
        assertEquals(entity.getReleaseDate(), result.getReleaseDate());
        assertEquals(entity.getPurchaseDate(), result.getPurchaseDate());
        assertEquals(entity.isSpecialEdition(), result.isSpecialEdition());

        verify(productRepository).findProducts();
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    @DisplayName("Get all products - empty list")
    void getProducts_emptyList() {
        // Arrange
        when(productRepository.findProducts()).thenReturn(List.of());

        // Act
        List<Collectable> actual = service.getProducts();

        // Assert
        assertNotNull(actual);
        assertEquals(0, actual.size());

        verify(productRepository).findProducts();
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    @DisplayName("Get product by ID - success")
    void getProduct_success() {
        // Arrange
        String id = "prod-1234";

        Product entity = new Product();
        entity.setId(id);
        entity.setSetName("Alpha Set");
        entity.setProductType("Box");
        entity.setReleasePrice(BigDecimal.valueOf(200.00));
        entity.setPurchasePrice(BigDecimal.valueOf(164.99));
        entity.setSpecialEdition(false);

        LocalDate ld_release = LocalDate.of(2025, 1, 1);
        LocalDate ld_purchase = LocalDate.of(2025, 2, 2);
        entity.setReleaseDate(ld_release);
        entity.setPurchaseDate(ld_purchase);

        when(productRepository.findExistingById(id)).thenReturn(entity);

        // Act
        Collectable actual = service.getProduct(id);

        // Assert
        assertNotNull(actual);
        assertEquals(entity.getId(), actual.getId());
        assertEquals(entity.getSetName(), actual.getSetName());
        assertEquals(entity.getProductType(), actual.getType());
        assertEquals(entity.getReleasePrice(), actual.getReleasePrice());
        assertEquals(entity.getPurchasePrice(), actual.getPurchasePrice());
        assertEquals(entity.getReleaseDate(), actual.getReleaseDate());
        assertEquals(entity.getPurchaseDate(), actual.getPurchaseDate());
        assertEquals(entity.isSpecialEdition(), actual.isSpecialEdition());

        verify(productRepository).findExistingById(id);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    @DisplayName("Get product by ID - not found")
    void getProduct_notFound() {
        // Arrange
        String id = "prod-1234";
        String message = "Product Not Found";

        when(productRepository.findExistingById(id)).thenThrow(new NotFoundException(message));

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, 
            () -> service.getProduct(id));
        assertEquals(message, exception.getMessage());
        verify(productRepository).findExistingById(id);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    @DisplayName("Create product - success")
    void createProduct_success() {
        // Arrange
        Collectable model = new Collectable();
        model.setId("prod-1234");
        model.setSetName("Alpha Set");
        model.setType("Box");
        model.setReleasePrice(BigDecimal.valueOf(200.00));
        model.setPurchasePrice(BigDecimal.valueOf(164.99));
        model.setSpecialEdition(false);

        LocalDate ld_release = LocalDate.of(2025, 1, 1);
        LocalDate ld_purchase = LocalDate.of(2025, 2, 2);
        model.setReleaseDate(ld_release);
        model.setPurchaseDate(ld_purchase);

        // Act
        Collectable actual = service.createProduct(model);

        // Assert
        assertNotNull(actual);
        assertEquals(model.getId(), actual.getId());
        assertEquals(model.getSetName(), actual.getSetName());
        assertEquals(model.getType(), actual.getType());
        assertEquals(model.getReleasePrice(), actual.getReleasePrice());
        assertEquals(model.getPurchasePrice(), actual.getPurchasePrice());
        assertEquals(model.getReleaseDate(), actual.getReleaseDate());
        assertEquals(model.getPurchaseDate(), actual.getPurchaseDate());
        assertEquals(model.isSpecialEdition(), actual.isSpecialEdition());

        verify(productRepository).persist(any(Product.class));
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    @DisplayName("Delete product - success")
    void deleteProduct_success() {
        // Arrange
        String id = "prod-1234";

        Product entity = new Product();
        entity.setId(id);

        when(productRepository.findExistingById(id)).thenReturn(entity);

        // Act
        service.deleteProduct(id);

        // Assert
        verify(productRepository).findExistingById(id);
        verify(productRepository).delete(entity);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    @DisplayName("Delete product - not found")
    void deleteProduct_notFound() {
        // Arrange
        String id = "prod-1234";
        String message = "Product Not Found";

        when(productRepository.findExistingById(id)).thenThrow(new NotFoundException(message));

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, 
            () -> service.deleteProduct(id));
        assertEquals(message, exception.getMessage());
        verify(productRepository).findExistingById(id);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    @DisplayName("Update product - success")
    void updateProduct_success() {
        // Arrange
        String id = "prod-1234";
        
        Collectable request = new Collectable();
        request.setId(id);
        request.setSetName("Beta Set");
        request.setType("Booster Pack");
        request.setReleasePrice(BigDecimal.valueOf(150.00));
        request.setPurchasePrice(BigDecimal.valueOf(120.00));
        request.setSpecialEdition(true);
        LocalDate ld_release = LocalDate.of(2025, 3, 3);
        LocalDate ld_purchase = LocalDate.of(2025, 4, 4);
        request.setReleaseDate(ld_release);
        request.setPurchaseDate(ld_purchase);

        Product existingEntity = new Product();
        existingEntity.setId(id);

        when(productRepository.findExistingById(id)).thenReturn(existingEntity);

        // Act
        Collectable actual = service.updateProduct(id, request);
        
        // Assert
        assertNotNull(actual);
        assertEquals(request.getId(), actual.getId());
        assertEquals(request.getSetName(), actual.getSetName());
        assertEquals(request.getType(), actual.getType());
        assertEquals(request.getReleasePrice(), actual.getReleasePrice());
        assertEquals(request.getPurchasePrice(), actual.getPurchasePrice());
        assertEquals(request.getReleaseDate(), actual.getReleaseDate());
        assertEquals(request.getPurchaseDate(), actual.getPurchaseDate());
        assertEquals(request.isSpecialEdition(), actual.isSpecialEdition());
        
        verify(productRepository).findExistingById(id);
        verify(productRepository).mergeAndFlush(any(Product.class));
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    @DisplayName("Update product - not found")
    void updateProduct_notFound() {
        // Arrange
        String id = "prod-1234";
        String message = "Product Not Found";

        Collectable request = new Collectable();
        request.setId(id);

        when(productRepository.findExistingById(id)).thenThrow(new NotFoundException(message));

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, 
            () -> service.updateProduct(id, request));
        assertEquals(message, exception.getMessage());
        verify(productRepository).findExistingById(id);
        verifyNoMoreInteractions(productRepository);
    }
}
