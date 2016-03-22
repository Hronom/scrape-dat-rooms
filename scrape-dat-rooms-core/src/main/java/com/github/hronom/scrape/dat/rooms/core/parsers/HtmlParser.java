package com.github.hronom.scrape.dat.rooms.core.parsers;

import java.util.ArrayList;

public interface HtmlParser {
    ArrayList<RoomInfo> parse(String content, RoomPhotoDownloader downloader);
}
