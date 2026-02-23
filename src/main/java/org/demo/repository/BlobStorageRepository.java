package org.demo.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

import org.demo.entity.BlobStorage;
import org.demo.entity.BlobStorageId;

import lombok.extern.jbosslog.JBossLog;

/** Repository class for managing {@link BlobStorage} entities. */
@ApplicationScoped
@Transactional
@JBossLog
public class BlobStorageRepository extends BaseRepository<BlobStorage, BlobStorageId> {
    
    public List<BlobStorage> findByBlobNumber(Long blobNumber) {
        return find("id.blobNo", blobNumber).list();
    }

    public Long getNextBlobLine(Long blobNumber) {
        return find("select max(b.id.blobLine) from BlobStorage b where b.id.blobNo = ?1", blobNumber)
                        .project(Long.class)
                        .firstResultOptional()
                        .orElse(0L)
                + 1;
    }
}
