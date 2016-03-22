package com.github.hronom.scrape.dat.rooms.core.parsers.windsurfercrs;

import com.github.hronom.scrape.dat.rooms.core.parsers.RoomInfo;
import com.github.hronom.scrape.dat.rooms.core.parsers.RoomPhotoDownloader;
import com.github.hronom.scrape.dat.rooms.core.utils.NetworkUtils;
import com.github.hronom.scrape.dat.rooms.core.parsers.windsurfercrs.WindsurfercrsHtmlParser;

import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNull;

public class WindsurfercrsHtmlParserTest {
    private WindsurfercrsHtmlParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new WindsurfercrsHtmlParser();
    }

    @Test
    public void testParse() throws Exception {
        String inputHtml = new Scanner(this.getClass().getResourceAsStream("Windsurfercrs_1.html"),
            StandardCharsets.UTF_8.toString()
        ).useDelimiter("\\Z").next();

        ArrayList<RoomInfo> roomInfos = parser.parse(inputHtml, new RoomPhotoDownloader() {
            @Override
            public Path download(String url) {
                return NetworkUtils.srcImageToSavePath(url, Paths.get(""));
            }
        });

        Iterator<RoomInfo> iter = roomInfos.iterator();

        // 1.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertEquals("ShergillGrandHotelCityViewsmall.jpg", roomInfo.roomPhotoPath);
            assertEquals("143.65$", roomInfo.rate);
            assertNull(roomInfo.description);
            assertEquals("2 Queens City View", roomInfo.amenities);
        }

        // 2.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertEquals("ShergillGrandHotelPVsmall.jpg", roomInfo.roomPhotoPath);
            assertEquals("152.15$", roomInfo.rate);
            assertNull(roomInfo.description);
            assertEquals("2 Queens Poolview", roomInfo.amenities);
        }

        // 3.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertEquals("ShergillGrandHotelEQ1small.jpg", roomInfo.roomPhotoPath);
            assertEquals("152.15$", roomInfo.rate);
            assertNull(roomInfo.description);
            assertEquals("1 Queen Tower Room", roomInfo.amenities);
        }

        // 4.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertEquals("ShergillGrandHotelKingRoomEK.jpg", roomInfo.roomPhotoPath);
            assertEquals("160.65$", roomInfo.rate);
            assertNull(roomInfo.description);
            assertEquals("1 King Tower Room", roomInfo.amenities);
        }

        // 5.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertEquals("ShergillGrandHotelEQQsmall.jpg", roomInfo.roomPhotoPath);
            assertEquals("160.65$", roomInfo.rate);
            assertNull(roomInfo.description);
            assertEquals("2 Queens Tower Room", roomInfo.amenities);
        }

        // 6.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertEquals("ShergillGrandHotelEKJsmall.jpg", roomInfo.roomPhotoPath);
            assertEquals("169.15$", roomInfo.rate);
            assertNull(roomInfo.description);
            assertEquals("King Jacuzzi Tower Room", roomInfo.amenities);
        }
    }
}