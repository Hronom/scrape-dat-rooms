package com.github.hronom.scrape.dat.rooms.core.parsers.motel6;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hronom.scrape.dat.rooms.core.parsers.HtmlParser;
import com.github.hronom.scrape.dat.rooms.core.parsers.RoomInfo;
import com.github.hronom.scrape.dat.rooms.core.parsers.RoomPhotoDownloader;
import com.github.hronom.scrape.dat.rooms.core.parsers.motel6.pojos.Motel6JsonAvailableRatePojo;
import com.github.hronom.scrape.dat.rooms.core.parsers.motel6.pojos.Motel6JsonResponsePojo;
import com.github.hronom.scrape.dat.rooms.core.parsers.motel6.pojos.Motel6JsonRoomRatePojo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class Motel6JsonParser implements HtmlParser {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public ArrayList<RoomInfo> parse(String content, RoomPhotoDownloader downloader) {
        ArrayList<RoomInfo> results = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
            Motel6JsonResponsePojo motel6JsonResponsePojo = mapper
                .readValue(content, Motel6JsonResponsePojo.class);
            for (Motel6JsonAvailableRatePojo availableRatePojo : motel6JsonResponsePojo.data.availability.best_available_rates) {
                for (Motel6JsonRoomRatePojo roomRatePojo : availableRatePojo.rate_by_rooms) {
                    RoomInfo roomInfo = parseRoom(roomRatePojo, downloader);
                    results.add(roomInfo);
                }
            }
        } catch (IOException exception) {
            logger.error("Not valid JSON", exception);
            return null;
        }

        return results;
    }

    private RoomInfo parseRoom(
        Motel6JsonRoomRatePojo roomRatePojo,
        RoomPhotoDownloader downloader
    ) {
        RoomInfo roomInfo = new RoomInfo();
        //parsePhoto(element, roomInfo, downloader);
        parseRate(roomRatePojo, roomInfo);
        parseDescription(roomRatePojo, roomInfo);
        parseAmenities(roomRatePojo, roomInfo);
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

    private void parseRate(Motel6JsonRoomRatePojo roomRatePojo, RoomInfo roomInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append(roomRatePojo.average_best_rate);
        sb.append(roomRatePojo.currency_code);
        roomInfo.rate = sb.toString();
    }

    private void parseDescription(Motel6JsonRoomRatePojo roomRatePojo, RoomInfo roomInfo) {
        roomInfo.description = roomRatePojo.long_description.trim();
    }

    private void parseAmenities(Motel6JsonRoomRatePojo roomRatePojo, RoomInfo roomInfo) {

        roomInfo.amenities = roomRatePojo.room_description.trim();
    }
}