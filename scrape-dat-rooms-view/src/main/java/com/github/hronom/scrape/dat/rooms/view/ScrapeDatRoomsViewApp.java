package com.github.hronom.scrape.dat.rooms.view;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.github.hronom.scrape.dat.rooms.core.parsers.motel6.Motel6HtmlParser;
import com.github.hronom.scrape.dat.rooms.core.parsers.redlion.RedLionHtmlParser;
import com.github.hronom.scrape.dat.rooms.core.parsers.redroof.RedRoofHtmlParser;
import com.github.hronom.scrape.dat.rooms.core.parsers.RoomInfo;
import com.github.hronom.scrape.dat.rooms.core.parsers.RoomPhotoDownloader;
import com.github.hronom.scrape.dat.rooms.core.utils.NetworkUtils;
import com.github.hronom.scrape.dat.rooms.core.utils.PathsUtils;
import com.github.hronom.scrape.dat.rooms.core.grabbers.JxBrowserGrabber;
import com.github.hronom.scrape.dat.rooms.view.controllers.GrabberSelectionController;
import com.github.hronom.scrape.dat.rooms.view.controllers.ScrapeButtonController;
import com.github.hronom.scrape.dat.rooms.view.controllers.WebsiteUrlTypingController;
import com.github.hronom.scrape.dat.rooms.view.views.ScrapeMainView;
import com.github.hronom.scrape.dat.rooms.view.views.ScrapeView;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ScrapeDatRoomsViewApp {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        logger.info(ScrapeDatRoomsViewApp.class.getSimpleName());
        printSystemInfo();

        ScrapeView scrapeView = new ScrapeView();
        new ScrapeButtonController(scrapeView);
        new WebsiteUrlTypingController(scrapeView);
        new GrabberSelectionController(scrapeView);
        new ScrapeMainView(scrapeView);

//        testMotel6();
//        testRedRoof();
//        testRedLion();
    }

    private static void printSystemInfo() {
        logger.info("Java version: " + System.getProperty("java.version"));
        logger.info("Java vendor: " + System.getProperty("java.vendor"));
        logger.info("Java vendor url: " + System.getProperty("java.vendor.url"));
        logger.info("Java home: " + System.getProperty("java.home"));
        logger.info("Java vm specification version: " +
                    System.getProperty("java.vm.specification.version"));
        logger.info(
            "Java vm specification vendor: " + System.getProperty("java.vm.specification.vendor"));
        logger.info(
            "Java vm specification name: " + System.getProperty("java.vm.specification.name"));
        logger.info("Java vm version: " + System.getProperty("java.vm.version"));
        logger.info("Java vm vendor: " + System.getProperty("java.vm.vendor"));
        logger.info("Java vm name: " + System.getProperty("java.vm.name"));
        logger.info(
            "Java specification version: " + System.getProperty("java.specification.version"));
        logger
            .info("Java specification vendor: " + System.getProperty("java.specification.vendor"));
        logger.info("Java specification name: " + System.getProperty("java.specification.name"));
        logger.info("Java class.version: " + System.getProperty("java.class.version"));
        logger.info("Java class.path: " + System.getProperty("java.class.path"));
        logger.info("Java library.path: " + System.getProperty("java.library.path"));
        logger.info("Java io.tmpdir: " + System.getProperty("java.io.tmpdir"));
        logger.info("Java compiler: " + System.getProperty("java.compiler"));
        logger.info("Java ext.dirs: " + System.getProperty("java.ext.dirs"));
        logger.info("OS name: " + System.getProperty("os.name"));
        logger.info("OS arch: " + System.getProperty("os.arch"));
        logger.info("OS version: " + System.getProperty("os.version"));

        // Total number of processors or cores available to the JVM.
        logger.info("Available processors (cores): " + Runtime.getRuntime().availableProcessors());
        // Total amount of free memory available to the JVM.
        logger.info("Free memory (bytes): " + Runtime.getRuntime().freeMemory());
        // This will return Long.MAX_VALUE if there is no preset limit.
        long maxMemory = Runtime.getRuntime().maxMemory();
        // Maximum amount of memory the JVM will attempt to use.
        logger.info(
            "Maximum memory (bytes): " + (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));
        // Total memory currently in use by the JVM.
        logger.info("Total memory (bytes): " + Runtime.getRuntime().totalMemory());
        // Get a list of all filesystem roots on this system.
        File[] roots = File.listRoots();
        // For each filesystem root, print some info.
        for (File root : roots) {
            logger.info("File system root: " + root.getAbsolutePath());
            logger.info("Total space (bytes): " + root.getTotalSpace());
            logger.info("Free space (bytes): " + root.getFreeSpace());
            logger.info("Usable space (bytes): " + root.getUsableSpace());
        }
    }

    private static void testMotel6() {
        final Path resultsDir = Paths.get("motel6");
        final Path resultsPhotosDir = resultsDir.resolve("photos");

        try {
            PathsUtils.deletePathIfExists(resultsDir);
            PathsUtils.createDirectoryIfNotExists(resultsDir);
            PathsUtils.createDirectoryIfNotExists(resultsPhotosDir);
        } catch (IOException e) {
            logger.fatal(e);
        }

        JxBrowserGrabber jxBrowserGrabber = new JxBrowserGrabber();
        String html = jxBrowserGrabber.grabContent(
            "https://www.motel6.com/en/motels.nv.las-vegas.8612.html#?propertyId=8612&numGuests=1&checkinDate=2015-11-26&numNights=1&corporatePlusNumber=CP555996&travelAgentNumber=7724054");
        Motel6HtmlParser dataProvider = new Motel6HtmlParser();
        ArrayList<RoomInfo> roomInfos = dataProvider.parse(html, new RoomPhotoDownloader() {
            @Override
            public Path download(String url) {
                try {
                    return NetworkUtils.downloadImage(url, resultsPhotosDir).toAbsolutePath();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });

        save(roomInfos, resultsDir);
    }

    private static void testRedRoof() {
        final Path resultsDir = Paths.get("redroof");
        final Path resultsPhotosDir = resultsDir.resolve("photos");

        try {
            PathsUtils.deletePathIfExists(resultsDir);
            PathsUtils.createDirectoryIfNotExists(resultsDir);
            PathsUtils.createDirectoryIfNotExists(resultsPhotosDir);
        } catch (IOException e) {
            logger.fatal(e);
        }

        JxBrowserGrabber jxBrowserGrabber = new JxBrowserGrabber();
        String html = jxBrowserGrabber.grabContent(
            "https://www.redroof.com/search/index.cfm?children=0&adults=1&SearchTerm=RRI570&checkin=11/30/15&checkout=12/31/15&rooms=1");
        RedRoofHtmlParser parser = new RedRoofHtmlParser();
        ArrayList<RoomInfo> roomInfos = parser.parse(html, new RoomPhotoDownloader() {
            @Override
            public Path download(String url) {
                try {
                    return NetworkUtils.downloadImage(url, resultsPhotosDir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });

        save(roomInfos, resultsDir);
    }

    private static void testRedLion() {
        final Path resultsDir = Paths.get("redlion");
        final Path resultsPhotosDir = resultsDir.resolve("photos");

        try {
            PathsUtils.deletePathIfExists(resultsDir);
            PathsUtils.createDirectoryIfNotExists(resultsDir);
            PathsUtils.createDirectoryIfNotExists(resultsPhotosDir);
        } catch (IOException e) {
            logger.fatal(e);
        }

        JxBrowserGrabber jxBrowserGrabber = new JxBrowserGrabber();
        String html = jxBrowserGrabber.grabContent(
            "https://reservations.redlion.com/ibe/index.aspx?dt1=5367&hgID=280&hotelID=2072&langID=1&checkin=11%2F22%2F2015&checkout=11%2F23%2F2015&adults=1&children=0&destination=Settle%20Inn%20%26%20Suites%20Harlan#ws-rsftr-0");
        RedLionHtmlParser parser = new RedLionHtmlParser();
        ArrayList<RoomInfo> roomInfos = parser.parse(html, new RoomPhotoDownloader() {
            @Override
            public Path download(String url) {
                try {
                    return NetworkUtils.downloadImage(url, resultsPhotosDir).toAbsolutePath();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });

        save(roomInfos, resultsDir);
    }

    private static void save(ArrayList<RoomInfo> roomInfos, Path resultsPath) {
        CsvMapper mapper = new CsvMapper();
        mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);

        CsvSchema schema = mapper.schemaFor(RoomInfo.class).withColumnSeparator(',').withHeader();

        try (BufferedWriter bw = Files.newBufferedWriter(resultsPath.resolve("results.csv"))) {
            mapper.writer(schema).writeValues(bw).writeAll(roomInfos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
