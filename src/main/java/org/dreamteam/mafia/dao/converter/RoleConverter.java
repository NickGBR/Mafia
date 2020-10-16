/*package org.dreamteam.mafia.dao.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<RoleEnum, String> {
    @Override
    public String convertToDatabaseColumn(RoleEnum role) {
        if (role == null) {
            return null;
        }
        return role.getCode();
    }

    @Override
    public RoleEnum convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(RoleEnum.values())
                .filter( r -> r.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

    }
}*/
