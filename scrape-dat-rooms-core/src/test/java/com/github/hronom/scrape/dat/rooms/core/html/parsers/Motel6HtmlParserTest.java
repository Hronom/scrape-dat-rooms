package com.github.hronom.scrape.dat.rooms.core.html.parsers;

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

public class Motel6HtmlParserTest {
    private Motel6HtmlParser parser;
    @Before
    public void setUp() throws Exception {
        parser = new Motel6HtmlParser();
    }

    @Test
    public void testParse() throws Exception {
        String inputHtml = new Scanner(
            this.getClass().getResourceAsStream("Motel6_1.html"),
            StandardCharsets.UTF_8.toString())
            .useDelimiter("\\Z")
            .next();

        ArrayList<RoomInfo> roomInfos = parser.parse(inputHtml, new RoomPhotoDownloader() {
            @Override
            public Path download(String url) {
                return Paths.get(url);
            }
        });

        Iterator<RoomInfo> iter = roomInfos.iterator();

        // 1.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertEquals(roomInfo.roomPhotoPath, "https:/www.motel6.com/content/dam/g6/hotel-assets/room-images/m6_0000_single1.jpg");
            assertEquals(roomInfo.rate, "58.49$");
            assertEquals(roomInfo.description, "Non-Smoking");
            assertEquals(roomInfo.amenities, "1 King Bed");
        }

        // 2.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertEquals(roomInfo.roomPhotoPath, "https:/www.motel6.com/content/dam/g6/hotel-assets/room-images/m6_0000_double2.jpg");
            assertEquals(roomInfo.rate, "62.99$");
            assertEquals(roomInfo.description, "Non-Smoking");
            assertEquals(roomInfo.amenities, "2 Queen Beds");
        }
    }
}