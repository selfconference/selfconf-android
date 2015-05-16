package org.selfconference.android;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public final class FileLoader {

    @NonNull
    public static Reader loadJson(final String filename) {
        try {
            final File file = new File(System.getProperty("user.dir") + "/app/src/test/resources/" + filename);
            final InputStream inputStream = new FileInputStream(file);
            return new InputStreamReader(inputStream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private FileLoader() {
        throw new AssertionError("No instances.");
    }
}
