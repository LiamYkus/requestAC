package com.example.servlet;

import com.example.entity.Role;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Role role) {
        if (role == null) {
            return null; // Trả về null nếu Role không được gán
        }
        return role.ordinal(); // Lấy giá trị ordinal của Enum
    }

    @Override
    public Role convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null; // Trả về null nếu giá trị trong database là null
        }
        try {
            return Role.values()[dbData]; // Chuyển đổi Integer thành Enum
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Invalid role value: " + dbData, e); // Ném lỗi nếu không hợp lệ
        }
    }
}
