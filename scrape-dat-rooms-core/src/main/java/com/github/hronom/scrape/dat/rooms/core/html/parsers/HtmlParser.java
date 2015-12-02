package com.github.hronom.scrape.dat.rooms.core.html.parsers;

import java.util.ArrayList;

public interface HtmlParser {
    ArrayList<RoomInfo> parse(String html, RoomPhotoDownloader downloader);
}
