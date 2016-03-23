package com.github.hronom.scrape.dat.rooms.core.parsers.booking;

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

public class BookingHtmlParser implements HtmlParser {
    private static final Logger logger = LogManager.getLogger();

    private final String baseUri = "http://www.booking.com/";

    @Override
    public ArrayList<RoomInfo> parse(String content, RoomPhotoDownloader downloader) {
        ArrayList<RoomInfo> results = new ArrayList<>();

        Document doc = Jsoup.parse(content, baseUri);

        Elements selectedElements = doc.select("li[class^=\"room_block\"]");
        if (selectedElements.isEmpty()) {
            logger.error("Not valid HTML for Booking website!");
            return null;
        }

        for (Element element : selectedElements) {
            RoomInfo roomInfo = parseRoom(doc, element, downloader);
            results.add(roomInfo);
        }

        return results;
    }

    private RoomInfo parseRoom(Document doc, Element element, RoomPhotoDownloader downloader) {
        RoomInfo roomInfo = new RoomInfo();
        parsePhoto(doc, element, roomInfo, downloader);
        parseRate(element, roomInfo);
        parseAmenities(element, roomInfo);
        return roomInfo;
    }

    private void parsePhoto(Document doc, Element element, RoomInfo roomInfo, RoomPhotoDownloader downloader) {
        String roomId = element.attr("id").replace("block-", "");
        Element photoElement = doc.select("img[data-room-id=\"" + roomId + "\"]").first();
        if (photoElement != null) {
            String photoUrl = photoElement.absUrl("data-src");
            Path savePath = downloader.download(photoUrl);
            if (savePath != null) {
                roomInfo.roomPhotoPath = savePath.toString();
            }
        }
    }

    /*
    <span class="price " data-component="track" data-track="view" data-hash="PYNNQADAUOEQWUfbKe" data-stage="1">
US$135.98
</span>
     */
    private void parseRate(Element element, RoomInfo roomInfo) {
        Element currencyElement = element.select("span[class^=\"price\"]").first();
        if (currencyElement != null) {
            roomInfo.rate = currencyElement.ownText().trim();
        }
    }

    private void parseAmenities(Element element, RoomInfo roomInfo) {
        Element descriptionElement = element.select("h2[class^=\"room_title_\"]").first();
        if (descriptionElement != null) {
            roomInfo.amenities = descriptionElement.text().trim();
        }
    }
}