package org.ksm;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Hero extends PanacheEntity{

    @Size(max = 10, message = "Alias cannot exceed 10 characters")
    @NotBlank
    @Column(name = "alias", nullable = false, length = 10)
    private String alias;

    @NotBlank
    private String name;

    @NotNull
    @Column(name = "can_fly", nullable = false)
    private Boolean canFly;
}
