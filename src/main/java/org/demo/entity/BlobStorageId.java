package org.demo.entity;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlobStorageId implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "BLOB_NO")
    private long blobNo;

    @Column(name = "BLOB_LINE")
    private long blobLine;
}
