package com.studybuddy.storage;

import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studybuddy.models.auth.Token;

import java.io.IOException;

public class TokenHandler {
    private final static String tokenPath = "auth";

    public static Token getToken(Context context) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String content = StorageHandler.readFromFile(context, tokenPath);
        return mapper.readValue(content, Token.class);
    }
}
