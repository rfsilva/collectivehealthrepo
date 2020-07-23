package com.collectivehealth.pollmanagement.model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.collectivehealth.pollmanagement.enums.RequireAuthentication;

@Converter(autoApply = true)
public class RequireAuthenticationConverter implements AttributeConverter<RequireAuthentication, Integer> {

	@Override
	public Integer convertToDatabaseColumn(RequireAuthentication attribute) {
		if (attribute != null) {
			return attribute.getValue();
		}
		return null;
	}

	@Override
	public RequireAuthentication convertToEntityAttribute(Integer dbData) {
		if (dbData != null) {
			return RequireAuthentication.of(dbData);
		}
		return null;
	}

}