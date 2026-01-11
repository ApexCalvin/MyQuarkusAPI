package org.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/** Entity class for Product Type */
@Entity
@Table(name = ProductType.TABLE_NAME)
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class ProductType {

    public static final String TABLE_NAME = "PRODUCT_TYPE";

    @Id
    @Size(max = 255)
    @Column(name = "NAME", nullable = false)
    String name;

    @Column(name = "MSRP")
    Double msrp;

    @Column(name = "BOOSTER_COUNT")
    Integer boosterCount;
}
