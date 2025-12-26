package org.demo.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import org.apache.commons.lang3.BooleanUtils;

@Converter
public class YesNoConverter implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        return BooleanUtils.toString(attribute, "YES", "NO", "NO");
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        return BooleanUtils.toBoolean(dbData);
    }
}
