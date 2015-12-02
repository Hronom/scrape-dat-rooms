package com.github.hronom.scrape.dat.rooms.core.html.parsers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class EbookersHtmlParser implements HtmlParser {
    private static final Logger logger = LogManager.getLogger();

    private final String baseUri = "http://www.ebookers.com/";

    @Override
    public ArrayList<RoomInfo> parse(String html, RoomPhotoDownloader downloader) {
        ArrayList<RoomInfo> results = new ArrayList<>();

        Document doc = Jsoup.parse(html, baseUri);
        Elements selectedElements =
            doc.select("div[class^=\"container-card roomRate photo-gallery-images\"]");
        if (selectedElements.isEmpty()) {
            logger.error("Not valid HTML for Ebookers website!");
            return null;
        }

        for (Element element : selectedElements) {
            RoomInfo roomInfo = parseRoom(element);
            results.add(roomInfo);
        }

        return results;
    }

    private RoomInfo parseRoom(Element element) {
        RoomInfo roomInfo = new RoomInfo();
        parseRate(element, roomInfo);
        parseAmenities(element, roomInfo);
        return roomInfo;
    }

    private void parseRate(Element element, RoomInfo roomInfo) {
        Element currencyElement = element.select("strong").first();
        if (currencyElement != null) {
            roomInfo.rate = currencyElement.ownText().trim();
        }
    }

    private void parseAmenities(Element element, RoomInfo roomInfo) {
        Element descriptionElement = element.select("ul[class=\"room-description\"]").first();
        if (descriptionElement != null) {
            roomInfo.amenities = descriptionElement.text().trim();
        }
    }
}