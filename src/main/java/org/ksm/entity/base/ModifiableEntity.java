package org.ksm.entity.base;

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
}

