package com.studybuddy.storage;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class StorageHandler {
    public static void writeToFile(Context context, String data, String target) throws IOException {
        FileOutputStream fileOutputStream = context.openFileOutput(target, Context.MODE_PRIVATE);
        OutputStreamWriter outputWriter = new OutputStreamWriter(fileOutputStream);

        outputWriter.write(data);
        outputWriter.close();
    }

    public static String readFromFile(Context context, String target) throws IOException {
        StringBuilder data = new StringBuilder();

        FileInputStream fileInputStream = context.openFileInput(target);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

        char[] inputBuffer = new char[100];
        int charRead;

        while ((charRead = inputStreamReader.read(inputBuffer)) > 0) {
            String readString = String.copyValueOf(inputBuffer, 0, charRead);
            data.append(readString);
        }

        return data.toString();
    }
}
