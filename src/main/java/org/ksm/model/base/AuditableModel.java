package org.ksm.model.base;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.ksm.entity.converter.ZonedDateTimeConverter;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public abstract class AuditableModel {

    @JsonProperty(index = 995)
    @Schema(examples = "ADMIN", description = "Created by user")
    public String createdBy;

    @JsonProperty(index = 997)
    @Schema(examples = "ADMIN", description = "Last modified by user")
    public String modifiedBy;

    @Convert(converter = ZonedDateTimeConverter.class)
    @JsonProperty(index = 996)
    @Schema(description = "Creation time of the entity")
    public ZonedDateTime createdDate;

    @Column(name = "MODIFIED_DATE")
    @JsonProperty(index = 998)
    @Schema(description = "Last modified by time")
    public ZonedDateTime modifiedDate;
}
