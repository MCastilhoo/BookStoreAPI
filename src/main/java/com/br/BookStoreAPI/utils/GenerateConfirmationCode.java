package com.br.BookStoreAPI.utils;

import java.util.Random;

public class GenerateConfirmationCode {
    public static String generate(){
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
