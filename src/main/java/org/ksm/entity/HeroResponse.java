package org.ksm.entity;

import java.util.UUID;

import org.ksm.entity.base.ModifiableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/** Entity class for Hero */
@Entity
@Table(name = HeroResponse.TABLE_NAME)
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class HeroResponse extends ModifiableEntity {

    public static final String TABLE_NAME = "HERO";

    @Id
    @Size(max = 19) 
    @Column(name = "ID")
    private String id;

    @NotBlank(message = "Alias is required") 
    @Size(max = 50, message = "Alias must be less than 50 characters") 
    @Column(name = "ALIAS")
    private String alias;

    @NotBlank(message = "Name is required") 
    @Size(max = 50, message = "Name must be less than 50 characters") 
    @Column(name = "NAME")
    private String name;

    @Column(name = "CAN_FLY")
    private boolean flyable;

    @Override
    protected void onInsert() {
        super.onInsert();

        if (id == null) id = UUID.randomUUID().toString();
    }
}
