package com.github.hronom.scrape.dat.rooms.core.parsers;

import java.nio.file.Path;

public interface RoomPhotoDownloader {
    Path download(String url);
}
