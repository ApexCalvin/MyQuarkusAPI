package org.ksm.entity;

import org.ksm.entity.base.IdentifiableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Entity class for Hero */
@Entity
@Table(name = HeroResponse.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HeroResponse implements IdentifiableEntity<String> {

    public static final String TABLE_NAME = "HERO";

    @Id
    @Size(max = 50) 
    @Column(name = "ID")
    private String id;

    @NotEmpty(message = "Alias is required") 
    @Size(max = 50, message = "Alias must be less than 50 characters") 
    @Column(name = "ALIAS")
    private String alias;

    @NotEmpty(message = "Name is required") 
    @Size(max = 50, message = "Name must be less than 50 characters") 
    @Column(name = "NAME")
    private String name;

    @Column(name = "CAN_FLY")
    private boolean flyable;

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getId'");
    }
}
