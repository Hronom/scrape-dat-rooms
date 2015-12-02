package com.github.hronom.scrape.dat.rooms.core.html.parsers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.nio.file.Path;
import java.util.ArrayList;

public class EbookersHtmlParser implements HtmlParser {
    private static final Logger logger = LogManager.getLogger();

    private final String baseUri = "http://www.ebookers.com/";

    @Override
    public ArrayList<RoomInfo> parse(String html, RoomPhotoDownloader downloader) {
        ArrayList<RoomInfo> results = new ArrayList<>();

        Document doc = Jsoup.parse(html, baseUri);
        Elements selectedElements = doc.select("div[class^=\"container-card roomRate photo-gallery-images\"]");
        if (selectedElements.isEmpty()) {
            logger.error("Not valid HTML for Motel6 website!");
            return null;
        }

        for (Element element : selectedElements) {
            RoomInfo roomInfo = parseRoom(element, downloader);
            results.add(roomInfo);
        }

        return results;
    }

    private RoomInfo parseRoom(Element element, RoomPhotoDownloader downloader) {
        RoomInfo roomInfo = new RoomInfo();
        parsePhoto(element, roomInfo, downloader);
        parseRate(element, roomInfo);
        parseDescription(element, roomInfo);
        parseAmenities(element, roomInfo);
        return roomInfo;
    }

    private void parsePhoto(Element element, RoomInfo roomInfo, RoomPhotoDownloader downloader) {
        Element photoElement = element.select("a[class=\"ng-scope\"]").first();
        if (photoElement != null) {
            String photoUrl = photoElement.absUrl("href");
            Path savePath = downloader.download(photoUrl);
            if (savePath != null) {
                roomInfo.roomPhotoPath = savePath.toString();
            }
        }
    }

    private void parseRate(Element element, RoomInfo roomInfo) {
        StringBuilder sb = new StringBuilder();
        Element currencyElement = element
            .select("format-currency[class=\"ng-binding ng-isolate-scope\"]").first();
        if (currencyElement != null) {
            // Main part of value.
            sb.append(currencyElement.ownText());

            sb.append(".");

            // Additional part of value.
            Element additionalValuePartElem = currencyElement.select("sup[class=\"ng-binding\"]")
                .first();
            if (additionalValuePartElem != null) {
                sb.append(additionalValuePartElem.ownText());
            }

            Element rateTypeElem = currencyElement.select("span[class=\"ng-binding\"]").first();
            if (rateTypeElem != null) {
                sb.append(rateTypeElem.ownText());
            }
        }
        roomInfo.rate = sb.toString();
    }

    private void parseDescription(Element element, RoomInfo roomInfo) {
        Element descriptionElement = element
            .select("div[class=\"roomList-body descSection-desktop\"]").first();

        if (descriptionElement != null) {
            Element labelElement = descriptionElement.select("label[class=\"text-col ng-binding\"]")
                .first();
            roomInfo.description = labelElement.ownText().trim();
        }
    }

    private void parseAmenities(Element element, RoomInfo roomInfo) {
        Element amenitiesElement = element.select("span[class=\"roomInfo\"]").first();
        if (amenitiesElement != null) {
            roomInfo.amenities = amenitiesElement.text().trim();
        }
    }
}