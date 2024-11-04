package com.br.BookStoreAPI.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum UserStatus {
    ACTIVE("A", "Active"),
    INACTIVE("I", "Inactive"),
    PENDING("P", "Pending");

    private final String code;
    private final String description;

    private static final Map<String, UserStatus> CODE_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(UserStatus::getCode, status -> status));

    @JsonValue
    public String getCode() {
        return code;
    }

    @JsonCreator
    public static UserStatus fromCode(String code) {
        return CODE_MAP.getOrDefault(code, null);
    }
}
