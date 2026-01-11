package org.demo.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.demo.entity.base.ModifiableEntity;
import org.hibernate.type.YesNoConverter;

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
    @Column(name = "ID", nullable = false)
    private String id;

    @NotBlank
    @Size(max = 255) 
    @Column(name = "SET_CODE", nullable = false)
    private String setCode;

    @NotBlank
    @Size(max = 255) 
    @Column(name = "PRODUCT_TYPE", nullable = false)
    private String productType;

    @NotNull
    @Column(name = "PURCHASE_PRICE", nullable = false)
    private BigDecimal purchasePrice;

    @Override
    protected void onInsert() {
        super.onInsert();

        if (id == null) id = java.util.UUID.randomUUID().toString(); 
    }
}