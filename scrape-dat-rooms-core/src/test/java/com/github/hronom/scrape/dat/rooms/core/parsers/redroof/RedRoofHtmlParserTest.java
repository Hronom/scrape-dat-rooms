package com.github.hronom.scrape.dat.rooms.core.parsers.redroof;

import com.github.hronom.scrape.dat.rooms.core.parsers.RoomInfo;
import com.github.hronom.scrape.dat.rooms.core.parsers.RoomPhotoDownloader;
import com.github.hronom.scrape.dat.rooms.core.parsers.redroof.RedRoofHtmlParser;

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

public class RedRoofHtmlParserTest {
    private RedRoofHtmlParser parser;
    @Before
    public void setUp() throws Exception {
        parser = new RedRoofHtmlParser();
    }

    @Test
    public void testParse() throws Exception {
        String inputHtml = new Scanner(
            this.getClass().getResourceAsStream("RedRoof_1.html"),
            StandardCharsets.UTF_8.toString())
            .useDelimiter("\\Z")
            .next();

        ArrayList<RoomInfo> roomInfos = parser.parse(inputHtml, new RoomPhotoDownloader() {
            @Override
            public Path download(String url) {
                return Paths.get("test.png");
            }
        });

        Iterator<RoomInfo> iter = roomInfos.iterator();

        // 1.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertNull(roomInfo.roomPhotoPath);
            assertEquals(roomInfo.rate, "59.99$");
            assertNull(roomInfo.description);
            assertEquals(roomInfo.amenities, "2 FULL BEDS NON-SMOKING with Free WiFi");
        }

        // 2.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertNull(roomInfo.roomPhotoPath);
            assertEquals(roomInfo.rate, "59.99$");
            assertNull(roomInfo.description);
            assertEquals(roomInfo.amenities, "2 FULL BEDS SMOKING with Free WiFi");
        }

        // 3.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertNull(roomInfo.roomPhotoPath);
            assertEquals(roomInfo.rate, "69.99$");
            assertNull(roomInfo.description);
            assertEquals(roomInfo.amenities, "SUPERIOR KING ROOM NON-SMOKING - Our Best Room with Free WiFi, Microwave & Refrigerator, Free Long Distance*, Larger Work Area, and In-Room Coffee");
        }

        // 4.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertNull(roomInfo.roomPhotoPath);
            assertEquals(roomInfo.rate, "69.99$");
            assertNull(roomInfo.description);
            assertEquals(roomInfo.amenities, "SUPERIOR KING ROOM SMOKING - Our Best Room with Free WiFi, Microwave & Refrigerator, Free Long Distance*, Larger Work Area, and In-Room Coffee Only 1 room available!");
        }

        // 5.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertNull(roomInfo.roomPhotoPath);
            assertEquals(roomInfo.rate, "54.99$");
            assertNull(roomInfo.description);
            assertEquals(roomInfo.amenities, "1 QUEEN BED NON-SMOKING with Free WiFi");
        }

        // 6.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertNull(roomInfo.roomPhotoPath);
            assertEquals(roomInfo.rate, "54.99$");
            assertNull(roomInfo.description);
            assertEquals(roomInfo.amenities, "1 QUEEN BED SMOKING with Free WiFi");
        }
    }
}