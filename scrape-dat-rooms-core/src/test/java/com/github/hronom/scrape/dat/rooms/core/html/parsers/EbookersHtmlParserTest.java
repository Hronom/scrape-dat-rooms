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

public class EbookersHtmlParserTest {
    private EbookersHtmlParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new EbookersHtmlParser();
    }

    @Test
    public void testParse() throws Exception {
        String inputHtml = new Scanner(this.getClass().getResourceAsStream("Ebookers_1.html"),
            StandardCharsets.UTF_8.toString()
        ).useDelimiter("\\Z").next();

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
            assertNull(roomInfo.roomPhotoPath);
            assertEquals(roomInfo.rate, "£39.99");
            assertNull(roomInfo.description);
            assertEquals(roomInfo.amenities, "Comfortable guest room with 1 king bed. Smoking permitted.");
        }

        // 2.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertNull(roomInfo.roomPhotoPath);
            assertEquals(roomInfo.rate, "£39.99");
            assertNull(roomInfo.description);
            assertEquals(roomInfo.amenities, "Comfortable guest room with 1 king bed. Non- smoking.");
        }

        // 3.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertNull(roomInfo.roomPhotoPath);
            assertEquals(roomInfo.rate, "£42.96");
            assertNull(roomInfo.description);
            assertEquals(roomInfo.amenities, "Comfortable guest room with 2 queen beds. Non- smoking.");
        }

        // 4.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertNull(roomInfo.roomPhotoPath);
            assertEquals(roomInfo.rate, "£44.43");
            assertNull(roomInfo.description);
            assertEquals(roomInfo.amenities, "Comfortable guest room with 1 king bed. Smoking permitted.");
        }

        // 5.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertNull(roomInfo.roomPhotoPath);
            assertEquals(roomInfo.rate, "£44.43");
            assertNull(roomInfo.description);
            assertEquals(roomInfo.amenities, "Comfortable guest room with 1 king bed. Non- smoking.");
        }

        // 6.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertNull(roomInfo.roomPhotoPath);
            assertEquals(roomInfo.rate, "£47.74");
            assertNull(roomInfo.description);
            assertEquals(roomInfo.amenities, "Comfortable guest room with 2 queen beds. Non- smoking.");
        }
    }
}