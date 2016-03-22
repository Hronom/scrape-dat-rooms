package com.github.hronom.scrape.dat.rooms.core.html.parsers;

import com.github.hronom.scrape.dat.rooms.core.html.parsers.utils.NetworkUtils;

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

public class Motel6JsonParserTest {
    private Motel6JsonParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new Motel6JsonParser();
    }

    @Test
    public void testParse() throws Exception {
        String inputHtml = new Scanner(this.getClass().getResourceAsStream("Motel6_1.json"),
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
            //assertEquals("m6_0000_single1.jpg", roomInfo.roomPhotoPath);
            assertEquals("43.99USD", roomInfo.rate);
            assertEquals("32\" LCD TV WITH AV CONNECTIONS FOR GAMING, WOOD-EFFECT FLOORS, BATHROOM WITH GRANITE COUNTERTOPS AND A RAISED WASH BASIN.", roomInfo.description);
            assertEquals("1 QUEEN BED |", roomInfo.amenities);
        }

        // 2.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            //assertEquals("m6_0000_double2.jpg", roomInfo.roomPhotoPath);
            assertEquals("43.99USD", roomInfo.rate);
            assertEquals("32\" LCD TV WITH AV CONNECTIONS FOR GAMING, WOOD-EFFECT FLOORS, BATHROOM WITH GRANITE COUNTERTOPS AND A RAISED WASH BASIN.", roomInfo.description);
            assertEquals("2 FULL BEDS |", roomInfo.amenities);
        }

        // 3.
        assertTrue(iter.hasNext());
        {
            RoomInfo roomInfo = iter.next();
            //assertEquals("m6_0000_double2.jpg", roomInfo.roomPhotoPath);
            assertEquals("43.99USD", roomInfo.rate);
            assertEquals("32\" LCD TV WITH AV CONNECTIONS FOR GAMING, WOOD-EFFECT FLOORS, BATHROOM WITH GRANITE COUNTERTOPS AND A RAISED WASH BASIN.", roomInfo.description);
            assertEquals("1 FULL BED | ADA ACCESSIBLE", roomInfo.amenities);
        }
    }
}