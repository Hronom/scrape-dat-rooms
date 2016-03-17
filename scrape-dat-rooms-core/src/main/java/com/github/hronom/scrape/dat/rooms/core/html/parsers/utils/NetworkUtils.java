package com.github.hronom.scrape.dat.rooms.core.html.parsers.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public final class NetworkUtils {
    private NetworkUtils() {
    }

    public static Path downloadImage(String src, Path saveFolder) throws IOException {
        Path saveFilePath = srcImageToSavePath(src, saveFolder);

        // Open a URL Stream.
        URL url = new URL(src);
        try (InputStream in = url.openStream();
             OutputStream out = Files.newOutputStream(saveFilePath)) {
            for (int b; (b = in.read()) != -1; ) {
                out.write(b);
            }
        }

        return saveFilePath;
    }

    public static Path srcImageToSavePath(String src, Path saveFolder) {
        // Extract the name of the image from the src attribute.
        int indexName = src.lastIndexOf("/");

        if (indexName == src.length()) {
            src = src.substring(1, indexName + 1);
        }

        indexName = src.lastIndexOf("/") + 1;
        String name = src.substring(indexName, src.length());

        return saveFolder.resolve(name);
    }
}
