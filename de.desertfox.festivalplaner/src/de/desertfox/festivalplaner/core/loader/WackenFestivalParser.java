/**
 * Filename: WackenFestivalParser.java
 * Copyright: hsp Handels-Software-Partner GmbH - 2016
 * Purpose: 
 * 
 * Author: Dustin Donges
 * Revision: 
 * Created: 17.06.2016, 16:42:02
 */
package de.desertfox.festivalplaner.core.loader;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.desertfox.festivalplaner.core.factory.ArtistFactory;
import de.desertfox.festivalplaner.core.factory.RawArtist;
import de.desertfox.festivalplaner.model.Artist;
import de.desertfox.festivalplaner.model.Gig;
import de.desertfox.festivalplaner.model.LineUp;
import de.desertfox.festivalplaner.model.RunningOrder;
import de.desertfox.festivalplaner.model.Stage;

/**
 * @author d.donges
 *
 */
public class WackenFestivalParser extends AbstractFestivalLoader {

    private static final Logger logger            = LoggerFactory.getLogger(WackenFestivalParser.class);
    public static final String  LINE_UP_URL       = "http://www.wacken.com/de/bands/bands-billing/";
    public static final String  RUNNING_ORDER_URL = "http://www.wacken.com/de/bands/running-order/";

    private Stage               wackinger         = new Stage("Wackinger Stage");
    private Stage               trueMetal         = new Stage("True Metal Stage");
    private Stage               black             = new Stage("Black Stage");
    private Stage               wasteland         = new Stage("Wasteland Stage");
    private Stage               wet               = new Stage("W:E:T Stage");
    private Stage               party             = new Stage("Party Stage");
    private Stage               beerGarden        = new Stage("Beer Garden Stage");
    private Stage               headbangers       = new Stage("Headbanges Stage");

    /* LineUp member variables */
    private LineUp              lineUp;
    private RawArtist           raw;
    private boolean             inAnchor;
    private boolean             inArtistDiv;

    /* Runnnig Order member variables */
    private RunningOrder        runningOrder;
    private boolean             inGigDiv;
    private Stage               currentStage;
    private int                 currentDay        = -1;
    private String[]            days              = new String[] { "03.08.2016", "04.08.2016", "05.08.2016", "06.08.2016" };
    private SimpleDateFormat    format            = new SimpleDateFormat("HH:mm dd.MM.yyyy");
    private SimpleDateFormat    dayFormat         = new SimpleDateFormat("dd.MM.yyyy");

    @Override
    public LineUp parseLineUp() {
        lineUp = null;
        try {
            lineUp = new LineUp();
            loadArtists(loadDocument(LINE_UP_URL));
        } catch (Exception e) {
            logger.error("Error while parsing the LineUp of Wacken Open Air", e);
        }
        return lineUp;
    }

    private void loadArtists(Node node) throws Exception {
        Node nodeClass = node.getAttributes().getNamedItem("class");
        if (inArtistDiv && "a".equals(node.getLocalName())) {
            inAnchor = true;
            enterNode(node);
            inAnchor = false;
        } else if (inAnchor && node.getNodeValue() != null && !node.getNodeValue().isEmpty()) {
            raw.name = new String(node.getNodeValue().getBytes(), "UTF-8");
        } else if (inAnchor && "img".equals(node.getLocalName())) {
            Node srcNode = node.getAttributes().getNamedItem("src");
            if (srcNode != null) {
                raw.imageLink = srcNode.getNodeValue();
            }
        } else if (nodeClass != null) {
            createArtist();
            inArtistDiv = "col-sm-20".equals(nodeClass.getNodeValue());
            if (inArtistDiv) {
                raw = new RawArtist();
            }
            enterNode(node);
        } else {
            enterNode(node);
        }
    }

    private void enterNode(Node node) throws Exception {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            loadArtists(childNodes.item(i));
        }
    }

    private void createArtist() {
        if (raw == null) {
            return;
        }
        Artist artist = ArtistFactory.createArtist(raw);
        if (artist != null) {
            lineUp.add(artist);
        }
        raw = null;
    }

    private String getDay(int i) {
        if (i == days.length) {
            return "07.08.2016";
        }
        return days[i];
    }

    private boolean possibleStageDivOrDayDiv;

    private void loadGigs(Node node) throws Exception {
        Node nodeClass = node.getAttributes().getNamedItem("class");
        if (inGigDiv) {
            Gig gig = createGig(new String(node.getNodeValue().getBytes(), "UTF-8"));
            if (gig != null) {
                runningOrder.put(gig.getArtist(), gig);
            }
        } else if (nodeClass != null && ("col-sm-80 rosc_time odd".equals(nodeClass.getNodeValue()) || "col-sm-80 rosc_time even".equals(nodeClass.getNodeValue()))) {
            inGigDiv = true;
            enterGigNode(node);
            inGigDiv = false;
        } else if (nodeClass != null && "col-sm-80".equals(nodeClass.getNodeValue())) {
            possibleStageDivOrDayDiv = true;
            enterGigNode(node);
            possibleStageDivOrDayDiv = false;
        } else if (possibleStageDivOrDayDiv && "img".equals(node.getLocalName())) {
            if (node.getAttributes().getNamedItem("src").getNodeValue().contains("grau.jpg")) {
                currentStage = getStage(node.getAttributes().getNamedItem("src").getNodeValue());
            } else if (isDayDiv(node.getAttributes().getNamedItem("src").getNodeValue()) != null) {
                currentDay++;
            }
        } else {
            enterGigNode(node);
        }
    }

    private String[] festivalDays          = new String[] { "Mittwoch", "Donnerstag", "Freitag", "Samstag" };

    private String   dayUrlWithPlacehodler = "http://www.wacken.com/uploads/tx_woalineupmanagement/plugin_pi3/%%_de.jpg";

    private String isDayDiv(String imgUrl) {
        String day;
        for (int i = 0; i < festivalDays.length; i++) {
            day = festivalDays[i];
            if (dayUrlWithPlacehodler.replace("%%", day).equals(imgUrl)) {
                return days[i];
            }
        }
        return null;
    }

    private Stage getStage(String imgUrl) {
        if ("http://www.wacken.com/uploads/tx_woalineupmanagement/plugin_pi3/wackinger_stage_grau.jpg".equals(imgUrl)) {
            return wackinger;
        } else if ("http://www.wacken.com/uploads/tx_woalineupmanagement/plugin_pi3/Beer_Garden_grau.jpg".equals(imgUrl)) {
            return beerGarden;
        } else if ("http://www.wacken.com/uploads/tx_woalineupmanagement/plugin_pi3/wasteland_stage_grau.jpg".equals(imgUrl)) {
            return wasteland;
        } else if ("http://www.wacken.com/uploads/tx_woalineupmanagement/plugin_pi3/black_stage_grau.jpg".equals(imgUrl)) {
            return black;
        } else if ("http://www.wacken.com/uploads/tx_woalineupmanagement/plugin_pi3/true_metal_stage_grau_02.jpg".equals(imgUrl)) {
            return trueMetal;
        } else if ("http://www.wacken.com/uploads/tx_woalineupmanagement/plugin_pi3/Party_Stage_grau.jpg".equals(imgUrl)) {
            return party;
        } else if ("http://www.wacken.com/uploads/tx_woalineupmanagement/plugin_pi3/WET_Stage_grau.jpg".equals(imgUrl)) {
            return wet;
        } else if ("http://www.wacken.com/uploads/tx_woalineupmanagement/plugin_pi3/Headbangers_Stage_grau.jpg".equals(imgUrl)) {
            return headbangers;
        }
        return null;
    }

    private Gig createGig(String gigString) throws Exception {
        if (gigString.startsWith("Diese")) {
            return null;
        }
        gigString = gigString.replaceFirst(" ", "").replaceFirst(" ", "");
        String[] times = gigString.substring(0, gigString.indexOf(' ') + 1).trim().split("-");
        Gig gig = new Gig();
        gig.setDayOfFestival(dayFormat.parse(getDay(currentDay)));

        Calendar startTime = Calendar.getInstance();
        startTime.setTime(format.parse(times[0] + " " + getDay(currentDay)));
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(format.parse(times[1] + " " + getDay(currentDay)));
        if (startTime.get(Calendar.HOUR_OF_DAY) < 5) {
            startTime.add(Calendar.DAY_OF_YEAR, 1);
        }
        if (endTime.get(Calendar.HOUR_OF_DAY) < 5) {
            endTime.add(Calendar.DAY_OF_YEAR, 1);
        }
        gig.setStartTime(startTime.getTime());
        gig.setEndTime(endTime.getTime());
        String artistName = gigString.substring(gigString.indexOf(' ') + 1).trim();
        Artist artist = getArtistFromLineUp(artistName);
        if (artist == null) {
//            System.err.println(artistName);
            return null;
        }
        gig.setArtist(artist);
        gig.setStage(currentStage);
        return gig;
    }

    private Artist getArtistFromLineUp(String name) {
        List<Artist> artists = lineUp.getArtists();
        for (Artist artist : artists) {
            if (artist.getName().equals(name)) {
                return artist;
            }
        }
        return null;
    }

    private void enterGigNode(Node node) throws Exception {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            loadGigs(childNodes.item(i));
        }
    }

    @Override
    public RunningOrder parseRunningOrder() {
        runningOrder = new RunningOrder();
        currentDay = -1;
        try {
            if (lineUp == null) {
                parseLineUp();
            }
            loadGigs(loadDocument(RUNNING_ORDER_URL));
            return runningOrder;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
