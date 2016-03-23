package com.github.hronom.scrape.dat.rooms.core.parsers.booking;

import com.github.hronom.scrape.dat.rooms.core.parsers.RoomInfo;
import com.github.hronom.scrape.dat.rooms.core.parsers.RoomPhotoDownloader;

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

public class BookingHtmlParserTest {
    private BookingHtmlParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new BookingHtmlParser();
    }

    @Test
    public void testParse() throws Exception {
        String inputHtml = new Scanner(this.getClass().getResourceAsStream("Booking_1.html"),
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
            assertEquals("http:/q-ec.bstatic.com/images/hotel/840x460/391/39106125.jpg", roomInfo.roomPhotoPath);
            assertEquals(roomInfo.rate, "US$135.98");
            assertNull(roomInfo.description);
            assertEquals(roomInfo.amenities, "Standard Double Room with Two Double Beds (No Resort Fees)");
        }

        // 2.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertEquals("http:/r-ec.bstatic.com/images/hotel/840x460/391/39106237.jpg", roomInfo.roomPhotoPath);
            assertEquals(roomInfo.rate, "US$135.98");
            assertNull(roomInfo.description);
            assertEquals(roomInfo.amenities, "Standard Queen Room (No Resort Fees)");
        }

        // 3.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertEquals("http:/q-ec.bstatic.com/images/hotel/840x460/391/39106091.jpg", roomInfo.roomPhotoPath);
            assertEquals(roomInfo.rate, "US$135.98");
            assertNull(roomInfo.description);
            assertEquals(roomInfo.amenities, "Standard Room - Disability Access (No Resort Fees)");
        }
    }
}