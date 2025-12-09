package org.ksm.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.hibernate.type.YesNoConverter;
import org.ksm.entity.base.ModifiableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/** Entity class for Product */
@Entity
@Table(name = Product.TABLE_NAME)
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class Product extends ModifiableEntity {
    
    public static final String TABLE_NAME = "PRODUCT";

    @Id
    @Size(max = 36)
    @Column(name = "ID")
    private String id;

    @NotBlank(message = "Set is required") 
    @Size(max = 50, message = "Set must be less than 50 characters") 
    @Column(name = "SET_NAME")
    private String setName;

    @NotBlank(message = "Type is required") 
    @Size(max = 50, message = "Type must be less than 50 characters") 
    @Column(name = "PRODUCT_TYPE")
    private String productType;

    @NotEmpty(message = "Release Date is required") 
    @Column(name = "RELEASE_DATE")
    private LocalDate releaseDate;

    @NotEmpty(message = "Purchase Date is required") 
    @Column(name = "PURCHASE_DATE")
    private LocalDate purchaseDate;

    @NotNull(message = "Release price is required") 
    @Column(name = "RELEASE_PRICE")
    private BigDecimal releasePrice;

    @NotNull(message = "Purchase price is required") 
    @Column(name = "PURCHASE_PRICE")
    private BigDecimal purchasePrice;

    @NotNull(message = "Special Edition is required")
    @Convert(converter = YesNoConverter.class)
    @Column(name = "SPECIAL_EDITION")
    private boolean specialEdition;

    @Override
    protected void onInsert() {
        super.onInsert();

        if (id == null) id = java.util.UUID.randomUUID().toString(); 
    }
}