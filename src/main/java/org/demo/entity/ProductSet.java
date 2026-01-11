package org.demo.entity;

import java.time.LocalDate;

import org.hibernate.type.YesNoConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/** Entity class for Product Set */
@Entity
@Table(name = ProductSet.TABLE_NAME)
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class ProductSet {

    public static final String TABLE_NAME = "PRODUCT_SET";

    @Id
    @Size(max = 10)
    @Column(name = "CODE", nullable = false)
    private String code;

    @NotBlank
    @Size(max = 255)
    @Column(name = "NAME", nullable = false)
    private String name;

    @NotNull
    @Column(name = "SPECIALTY", nullable = false)
    @Convert(converter = YesNoConverter.class)
    private boolean specialty;

    @Column(name = "RELEASE_DATE")
    private LocalDate releaseDate;

    @Size(max = 255)
    @Column(name = "SERIES")
    private String series;
}