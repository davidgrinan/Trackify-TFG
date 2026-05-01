package com.example.trackify.Enum;

import lombok.Getter;

@Getter
public enum RoleType
{
    ADMIN(1),
    USER(2);

    private int value;

    private RoleType(int value) {
        this.value = value;
    }
}
