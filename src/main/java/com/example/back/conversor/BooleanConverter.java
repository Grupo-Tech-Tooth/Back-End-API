package com.example.back.conversor;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BooleanConverter implements AttributeConverter<Boolean, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Boolean attribute) {
        if (attribute == null) {
            return 0;
        }
        return attribute ? 1 : 0;
    }

    @Override
    public Boolean convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return false;
        }
        return dbData == 1;
    }
}