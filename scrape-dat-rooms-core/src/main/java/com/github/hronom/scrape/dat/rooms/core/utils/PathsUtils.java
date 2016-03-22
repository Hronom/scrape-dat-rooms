package com.github.hronom.scrape.dat.rooms.core.utils;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Utils for NIO paths.
 */
public final class PathsUtils {
    /**
     * Private constructor for utils.
     */
    private PathsUtils() {
    }

    /**
     * Check specified directory.
     *
     * @param path path to this directory.
     * @throws IOException if directories cannot be created.
     */
    public static void createDirectoryIfNotExists(Path path) throws IOException {
        try {
            Files.createDirectories(path);
        } catch (FileAlreadyExistsException ignored) {
        }
    }

    /**
     * Delete path if exists.
     *
     * @param path path to delete.
     * @throws IOException if path cannot be deleted.
     */
    public static void deletePathIfExists(Path path) throws IOException {
        if (Files.exists(path)) {
            deletePath(path);
        }
    }

    /**
     * Delete path.
     *
     * @param path path to delete.
     * @throws IOException if path cannot be deleted.
     */
    public static void deletePath(Path path) throws IOException {
        Files.walkFileTree(
            path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exception)
                    throws IOException {
                    if (exception == null) {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                    throw exception;
                }
            }
        );
    }
}
