package org.demo.entity.base;

/**
 * Interface for objects with an identification field.
 *
 * @param <T> Type of the ID.
 */
public interface IdentifiableEntity<T> {
    /** ID field name for the {@link IdentifiableEntity}. */
    String ID = "id";

    /**
     * Gets the id for this record.
     *
     * @return The id for this record.
     */
    T getId();    
}
