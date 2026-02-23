package org.demo.entity;

import org.demo.entity.base.ModifiableEntity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** Entity class for Blob Store */
@Entity
@Table(name = BlobStorage.TABLE_NAME)
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false, onlyExplicitlyIncluded = true)
public class BlobStorage extends ModifiableEntity {

    public static final String TABLE_NAME = "BLOB_STORE";

    @EmbeddedId
    @EqualsAndHashCode.Include
    private BlobStorageId id;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "BLOB_ITEM", columnDefinition = "MEDIUMBLOB")
    @ToString.Exclude
    private byte[] blobItem;

    @Size(max = 500)
    @Column(name = "BLOB_NAME", length = 500)
    private String blobName;

    @Size(max = 10)
    @Column(name = "BLOB_TYPE", length = 10)
    private String blobType;

    @Size(max = 10)
    @Column(name = "DOC_TYPE", length = 10)
    private String docType;

    @Size(max = 120)
    @Column(name = "MIME_TYPE", length = 120)
    private String mimeType;

    @Size(max = 100)
    @Column(name = "FROM_TABLE", length = 100)
    private String fromTable;
}
