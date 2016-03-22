package com.github.hronom.scrape.dat.rooms.core.parsers.redlion;

import com.github.hronom.scrape.dat.rooms.core.parsers.RoomInfo;
import com.github.hronom.scrape.dat.rooms.core.parsers.RoomPhotoDownloader;
import com.github.hronom.scrape.dat.rooms.core.parsers.redlion.RedLionHtmlParser;
import com.github.hronom.scrape.dat.rooms.core.utils.NetworkUtils;

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
        String inputHtml = new Scanner(this.getClass().getResourceAsStream("RedLion_1.html"),
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
            assertEquals("DSCN0265.JPG", roomInfo.roomPhotoPath);
            assertEquals("55.00$", roomInfo.rate);
            assertNull(roomInfo.description);
            assertEquals("Two Double Beds", roomInfo.amenities);
        }

        // 2.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertEquals("DSCN0262.JPG", roomInfo.roomPhotoPath);
            assertEquals("55.00$", roomInfo.rate);
            assertNull(roomInfo.description);
            assertEquals("One King Bed", roomInfo.amenities);
        }

        // 3.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertEquals("DSCN0274.JPG", roomInfo.roomPhotoPath);
            assertEquals("98.73$", roomInfo.rate);
            assertNull(roomInfo.description);
            assertEquals("King Bed Whirlpool Suite", roomInfo.amenities);
        }

        // 4.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertEquals("22_A1K.jpg", roomInfo.roomPhotoPath);
            assertEquals("55.00$", roomInfo.rate);
            assertNull(roomInfo.description);
            assertEquals("Accessible King Bed with Roll In Shower", roomInfo.amenities);
        }

        // 5.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertEquals("22_N1K.jpg", roomInfo.roomPhotoPath);
            assertEquals("55.00$", roomInfo.rate);
            assertNull(roomInfo.description);
            assertEquals("Accessible King Bed", roomInfo.amenities);
        }

        // 6.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertEquals("DSCN0271.JPG", roomInfo.roomPhotoPath);
            assertEquals("98.73$", roomInfo.rate);
            assertNull(roomInfo.description);
            assertEquals("King Bed Family Suite", roomInfo.amenities);
        }

        // 7.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertNull(roomInfo.roomPhotoPath);
            assertEquals("85.23$", roomInfo.rate);
            assertNull(roomInfo.description);
            assertEquals("Executive Suite", roomInfo.amenities);
        }
    }
}