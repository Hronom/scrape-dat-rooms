package com.github.hronom.scrape.dat.rooms.core.parsers.redroof;

import com.github.hronom.scrape.dat.rooms.core.parsers.HtmlParser;
import com.github.hronom.scrape.dat.rooms.core.parsers.RoomInfo;
import com.github.hronom.scrape.dat.rooms.core.parsers.RoomPhotoDownloader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class RedRoofHtmlParser implements HtmlParser {
    private static final Logger logger = LogManager.getLogger();

    private final String baseUri = "https://www.redroof.com";

    @Override
    public ArrayList<RoomInfo> parse(String content, RoomPhotoDownloader downloader) {
        ArrayList<RoomInfo> results = new ArrayList<>();

        Document doc = Jsoup.parse(content, baseUri);
        Element roomsContainerElement = doc.select("div[class=\"ratetypes\"][ratecode=\"BAR\"]").first();
        if (roomsContainerElement != null) {
            Elements roomsElements = roomsContainerElement.select("div[class=\"rtype\"]");
            for (Element roomElement : roomsElements) {
                RoomInfo roomInfo = parseRoom(roomElement);
                results.add(roomInfo);
            }
        }
        else {
            logger.error("Not valid HTML for RedRoof website!");
            return null;
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
        StringBuilder sb = new StringBuilder();
        Element currencyElement = element.select("span[class=\"right\"]").first();
        if (currencyElement != null) {
            // Price.
            Element rateTypeElem = currencyElement.select("span[class=\"price\"]").first();
            if (rateTypeElem != null) {
                sb.append(rateTypeElem.ownText());
            }

            // Price type.
            Element additionalValuePartElem =
                currencyElement.select("span[class=\"currency\"]").first();
            if (additionalValuePartElem != null) {
                sb.append(additionalValuePartElem.ownText());
            }
        }
        roomInfo.rate = sb.toString();
    }

    private void parseAmenities(Element element, RoomInfo roomInfo) {
        Element amenitiesElement = element.select("p").first();
        roomInfo.amenities = amenitiesElement.text().trim();
    }
}
