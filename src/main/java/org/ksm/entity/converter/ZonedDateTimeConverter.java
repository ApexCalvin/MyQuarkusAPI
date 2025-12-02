package org.ksm.entity.converter;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * JPA Attribute Converter for handling ZonedDateTime with Oracle DATE type.
 *
 * <p>This converter manages the conversion between Java's ZonedDateTime and Oracle's DATE type. Oracle DATE type does
 * not store timezone information, so this converter: 1. Converts ZonedDateTime to Timestamp (which maps to Oracle DATE)
 * by extracting the instant 2. Converts Timestamp back to ZonedDateTime using the system's default timezone
 *
 * <p>Note: When converting from database to entity, the timezone information is lost and the system's default timezone
 * is used. This is because Oracle DATE type does not preserve timezone information.
 *
 * @see java.time.ZonedDateTime
 * @see java.sql.Timestamp
 */
@Converter(autoApply = true)
public class ZonedDateTimeConverter implements AttributeConverter<ZonedDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) {
            return null;
        }
        return Timestamp.from(zonedDateTime.toInstant());
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return ZonedDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault());
    }
}
