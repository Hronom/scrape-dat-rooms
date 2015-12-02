package com.github.hronom.scrape.dat.rooms.core.html.parsers;

import java.nio.file.Path;

public interface RoomPhotoDownloader {
    Path download(String url);
}
