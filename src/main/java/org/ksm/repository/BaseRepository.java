package org.ksm.repository;

import java.util.List;

import jakarta.transaction.Transactional;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;

import lombok.extern.jbosslog.JBossLog;

/**
 * Abstract base repository class that provides common functionality for all repositories. This class implements
 * PanacheRepositoryBase and adds utility methods that are commonly used across different repositories.
 *
 * @param <T> The entity type
 * @param <ID> The ID type of the entity
 */
@Transactional
@JBossLog
public abstract class BaseRepository<T, ID> implements PanacheRepositoryBase<T, ID> {

    /**
     * Checks if an entity exists with the given ID. Uses a count query for better performance instead of loading the
     * entire entity.
     *
     * @param id The ID to check
     * @return true if an entity exists with the given ID, false otherwise
     */
    public boolean existsById(ID id) {
        return count("id = ?1", id) > 0;
    }

    /**
     * Lists all entities sorted by the given field.
     *
     * @param sortField The field to sort by
     * @return List of all entities sorted by the specified field
     */
    public List<T> listAllSorted(String sortField) {
        return listAll(Sort.by(sortField));
    }

    /**
     * Refreshes the state of the given entity from the database. This method is useful when you need to ensure the
     * entity state matches the database. Then flushes all pending changes to the database using the EntityManager for
     * the <Entity> entity class.
     *
     * @param entity The entity to refresh
     */
    public void flushAndRefresh(T entity) {
        flush();
        refresh(entity);
    }

    /**
     * Merges the state of the given entity into the current persistence context. This method is useful when you need to
     * update an entity that might have been detached.
     *
     * @param entity The entity to merge
     * @return The managed entity instance
     */
    public T merge(T entity) {
        return getEntityManager().merge(entity);
    }

    /**
     * Merges the state of the given entity into the current persistence context. This method is useful when you need to
     * update an entity that might have been detached.
     *
     * @param entity The entity to merge
     * @return The managed entity instance
     */
    public T mergeAndFlush(T entity) {
        T result = merge(entity);
        flush();
        return result;
    }

    /**
     * Refreshes the state of the given entity from the database. This method is useful when you need to ensure the
     * entity state matches the database.
     *
     * @param entity The entity to refresh
     */
    public void refresh(T entity) {
        getEntityManager().refresh(entity);
    }

    /**
     * Detaches the given entity from the persistence context. This method is useful when you need to work with an
     * entity in a detached state, for example when you want to modify it without affecting the database.
     *
     * @param entity The entity to detach
     */
    public void detach(T entity) {
        getEntityManager().detach(entity);
    }

    // /**
    //  * Persists a new entity into the persistence context. This method is useful when you need to create a new entity to
    //  * insert into the database when the persistence context is synchronized. Additionally, this method will nullify
    //  * the @version field as persist would fail otherwise.
    //  *
    //  * @param entity the entity to persist
    //  */
    // public void persist(T entity) {
    //     if (entity instanceof ModifiableEntity<?> modifiableEntity) {
    //         // modifiedDate is used as a version field and cannot be set when persisting a new entity
    //         modifiableEntity.setModifiedDate((java.time.LocalDateTime) null);
    //     }
    //     getEntityManager().persist(entity);
    // }

    /**
     * Persists a new entity into the persistence context then flushes the changes immediately. This method is useful
     * when you need to create a new entity to insert into the database immediately.
     *
     * @param entity the entity to persist
     */
    public void persistAndFlush(T entity) {
        persist(entity);
        flush();
    }
}
