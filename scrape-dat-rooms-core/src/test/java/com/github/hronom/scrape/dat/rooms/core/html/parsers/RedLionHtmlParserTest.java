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

public class RedLionHtmlParserTest {
    private RedLionHtmlParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new RedLionHtmlParser();
    }

    @Test
    public void testParse() throws Exception {
        String inputHtml = new Scanner(
            this.getClass().getResourceAsStream("RedLion_1.html"),
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
            assertEquals(roomInfo.roomPhotoPath, "https:/reservations.redlion.com/CrsMedia/P2072/rm/DSCN0265.JPG");
            assertEquals(roomInfo.rate, "55.00$");
            assertNull(roomInfo.description);
            assertEquals(roomInfo.amenities, "Two Double Beds");
        }

        // 2.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertEquals(roomInfo.roomPhotoPath, "https:/reservations.redlion.com/CrsMedia/P2072/rm/DSCN0262.JPG");
            assertEquals(roomInfo.rate, "55.00$");
            assertNull(roomInfo.description);
            assertEquals(roomInfo.amenities, "One King Bed");
        }

        // 3.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertEquals(roomInfo.roomPhotoPath, "https:/reservations.redlion.com/CrsMedia/P2072/rm/DSCN0274.JPG");
            assertEquals(roomInfo.rate, "98.73$");
            assertNull(roomInfo.description);
            assertEquals(roomInfo.amenities, "King Bed Whirlpool Suite");
        }

        // 4.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertEquals(roomInfo.roomPhotoPath, "https:/reservations.redlion.com/CrsMedia/P2072/rm/22_A1K.jpg");
            assertEquals(roomInfo.rate, "55.00$");
            assertNull(roomInfo.description);
            assertEquals(roomInfo.amenities, "Accessible King Bed with Roll In Shower");
        }

        // 5.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertEquals(roomInfo.roomPhotoPath, "https:/reservations.redlion.com/CrsMedia/P2072/rm/22_N1K.jpg");
            assertEquals(roomInfo.rate, "55.00$");
            assertNull(roomInfo.description);
            assertEquals(roomInfo.amenities, "Accessible King Bed");
        }

        // 6.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertEquals(roomInfo.roomPhotoPath, "https:/reservations.redlion.com/CrsMedia/P2072/rm/DSCN0271.JPG");
            assertEquals(roomInfo.rate, "98.73$");
            assertNull(roomInfo.description);
            assertEquals(roomInfo.amenities, "King Bed Family Suite");
        }

        // 7.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertNull(roomInfo.roomPhotoPath);
            assertEquals(roomInfo.rate, "85.23$");
            assertNull(roomInfo.description);
            assertEquals(roomInfo.amenities, "Executive Suite");
        }
    }
}