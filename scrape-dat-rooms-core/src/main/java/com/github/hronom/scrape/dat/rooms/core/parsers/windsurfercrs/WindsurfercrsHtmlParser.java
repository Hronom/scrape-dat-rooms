package com.github.hronom.scrape.dat.rooms.core.parsers.windsurfercrs;

import com.github.hronom.scrape.dat.rooms.core.parsers.HtmlParser;
import com.github.hronom.scrape.dat.rooms.core.parsers.RoomInfo;
import com.github.hronom.scrape.dat.rooms.core.parsers.RoomPhotoDownloader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.nio.file.Path;
import java.util.ArrayList;

public class WindsurfercrsHtmlParser implements HtmlParser {
    private static final Logger logger = LogManager.getLogger();

    private final String baseUri = "https://res.windsurfercrs.com";

    @Override
    public ArrayList<RoomInfo> parse(String content, RoomPhotoDownloader downloader) {
        ArrayList<RoomInfo> results = new ArrayList<>();

        Document doc = Jsoup.parse(content, baseUri);
        Element roomsContainerElement =
            doc.select("div[id=\"dvWsResultRooms\"][class=\"ws-results\"]").first();
        if (roomsContainerElement != null) {
            Elements roomsElements = roomsContainerElement.select("article[id^=\"ws-rsrm-\"]");
            for (Element roomElement : roomsElements) {
                RoomInfo roomInfo = parseRoom(roomElement, downloader);
                results.add(roomInfo);
            }
        }
        else {
            logger.error("Not valid HTML for RedLion website!");
            return null;
        }

        return results;
    }

    private RoomInfo parseRoom(Element element, RoomPhotoDownloader downloader) {
        RoomInfo roomInfo = new RoomInfo();
        parsePhoto(element, roomInfo, downloader);
        parseRate(element, roomInfo);
        parseAmenities(element, roomInfo);
        return roomInfo;
    }

    private void parsePhoto(Element element, RoomInfo roomInfo, RoomPhotoDownloader downloader) {
        // <img src="https://reservations.redlion.com/CrsMedia/P2072/rm/DSCN0265.JPG" class="coverme" alt="Room">
        Element photoElement = element.select("img[class=\"coverme\"]").first();
        if (photoElement != null) {
            String photoUrl = photoElement.absUrl("src");
            if(!photoUrl.contains("default.png")) {
                Path savePath = downloader.download(photoUrl);
                if (savePath != null) {
                    roomInfo.roomPhotoPath = savePath.toString();
                }
            }
        }
    }

    private void parseRate(Element element, RoomInfo roomInfo) {
        // <span class="ws-number" ref="55.00">$55</span>
        Element currencyElement = element.select("span[class=\"ws-number\"]").first();
        if (currencyElement != null) {
            roomInfo.rate = currencyElement.attr("ref") + '$';
        }
    }

    private void parseAmenities(Element element, RoomInfo roomInfo) {
        // <h1>
        Element amenitiesElement = element.select("h1").first();
        if (amenitiesElement != null) {
            roomInfo.amenities = amenitiesElement.ownText().trim();
        }
    }
}
