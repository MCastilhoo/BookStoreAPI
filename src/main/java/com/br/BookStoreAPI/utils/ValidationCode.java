package com.br.BookStoreAPI.utils;

import java.util.HashMap;
import java.util.Map;

public class ValidationCode {
    private static final Map<String, String> codesConfirmation = new HashMap<>();

    public static void saveCode(String email, String code) {
        codesConfirmation.put(email, code);
    }

    public static boolean validateCode(String email, String code) {
        return code.equals(codesConfirmation.get(email));
    }
}
