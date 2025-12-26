package org.demo.entity.base;

import java.time.LocalDateTime;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;

@MappedSuperclass
public abstract class ModifiableEntity {
    
    /** Populate the ID field before insertion. */
    @PrePersist
    private void beforeInsert() {
        onInsert();
    }

    protected void onInsert() {
        // override in subclasses if you need @PrePersist
    }

    public void setModifiedDate(LocalDateTime localDateTime) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setModifiedDate'");
    }
}

