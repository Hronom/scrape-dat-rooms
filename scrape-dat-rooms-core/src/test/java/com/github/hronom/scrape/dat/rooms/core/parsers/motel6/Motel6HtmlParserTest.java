package com.github.hronom.scrape.dat.rooms.core.parsers.motel6;

import com.github.hronom.scrape.dat.rooms.core.parsers.RoomInfo;
import com.github.hronom.scrape.dat.rooms.core.parsers.RoomPhotoDownloader;
import com.github.hronom.scrape.dat.rooms.core.parsers.motel6.Motel6HtmlParser;
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

public class Motel6HtmlParserTest {
    private Motel6HtmlParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new Motel6HtmlParser();
    }

    @Test
    public void testParse() throws Exception {
        String inputHtml = new Scanner(this.getClass().getResourceAsStream("Motel6_1.html"),
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
            assertEquals("m6_0000_single1.jpg", roomInfo.roomPhotoPath);
            assertEquals("58.49$", roomInfo.rate);
            assertEquals("Non-Smoking", roomInfo.description);
            assertEquals("1 King Bed", roomInfo.amenities);
        }

        // 2.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            assertEquals("m6_0000_double2.jpg", roomInfo.roomPhotoPath);
            assertEquals("62.99$", roomInfo.rate);
            assertEquals("Non-Smoking", roomInfo.description);
            assertEquals("2 Queen Beds", roomInfo.amenities);
        }
    }
}