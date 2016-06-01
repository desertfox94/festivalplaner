/**
 * Filename: JTidyLoader.java
 * Copyright: hsp Handels-Software-Partner GmbH - 2016
 * Purpose: 
 * 
 * Author: Dustin Donges
 * Revision: 
 * Created: 01.06.2016, 15:36:00
 */
package de.desertfox.festivalplaner;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

import de.desertfox.festivalplaner.model.Artist;
import de.desertfox.festivalplaner.model.Gig;
import de.desertfox.festivalplaner.model.Stage;
import de.desertfox.festivalplaner.util.DateUtil;

/**
 * @author d.donges
 *
 */
public class JTidyLoader {

    /**
     * @param args
     */
    public static void main(String[] args) {
        JTidyLoader jTidyLoader = new JTidyLoader();
        List<Artist> artists = jTidyLoader.loadArtists();
        for (Artist artist : artists) {
            System.out.println(artist.getName());
        }
        jTidyLoader.loadGigs(artists, "http://www.wacken.com/de/bands/running-order/");
    }

    public List<Artist> loadGigs(List<Artist> artists, String urlString) {
        try {
            gigs = new ArrayList<Gig>();
            Tidy tidy = new Tidy();
            URL url = new URL(urlString);
            InputStream openStream = url.openStream();
            Document document = tidy.parseDOM(openStream, System.out);
            loadGigs(document);
//            for (Gig gig : gigs) {
//                System.out.println(gig);
//            }
            
//            Random rnd = new Random();
//            Gig g1;
//            Gig g2;
//            for (int i = 0; i < 120; i++) {
//                g1 = gigs.get(rnd.nextInt(gigs.size()));
//                g2 = gigs.get(rnd.nextInt(gigs.size()));
//                if (DateUtil.arePeriodsColiding(g1.getStartTime(), g1.getEndTime(), g2.getStartTime(), g2.getEndTime())) {
//                    System.out.println();
//                    System.err.println(g1);
//                    System.err.println(g2);
//                }
//            }
            
            for (Gig gig : gigs) {
				for (Artist artist : artists) {
					if (artist.equals(gig.getArtist())) {
						artist.addGig(gig);
						gig.setArtist(artist);
					}
				}
			}
            
            return artists;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return artists;
    }
    
    private boolean inGigDiv;
    private Stage[] stages = new Stage[] {new Stage("Black Stage"), new Stage("True Metal Stage"), new Stage("Party Stage"), new Stage("W:E:T Stage"), new Stage("Headbanges Stage"), new Stage("Wackinger Stage"), new Stage("Beer Garden Stage"), new Stage("Wasteland Stage") };
    private int currentStageIndex;
    private Stage currentStage;
    private int currentDay;
    private String[] days = new String[] {"03.08.2016", "04.08.2016", "05.08.2016", "06.08.2016"};
    private SimpleDateFormat format = new SimpleDateFormat("HH:mm dd.MM.yyyy");
    private List<Gig> gigs;
    
    private void loadGigs(Node node) throws Exception {
        Node nodeClass = node.getAttributes().getNamedItem("class");
        if (inGigDiv) {
            Gig gig = createGig(new String(node.getNodeValue().getBytes(), "UTF-8"));
            if (gig != null) {
                gigs.add(gig);
            }
        } else if (nodeClass != null && ("col-sm-80 rosc_time odd".equals(nodeClass.getNodeValue()) || "col-sm-80 rosc_time even".equals(nodeClass.getNodeValue()))) {
            inGigDiv = true;
            enterGigNode(node);
            inGigDiv = false;
        } else if (nodeClass != null && "col-sm-38".equals(nodeClass.getNodeValue())) {
            nextStage();
            enterGigNode(node);
        } else {
            enterGigNode(node);
        }
    }
    
    private Gig createGig(String gigString) throws Exception {
        if (gigString.startsWith("Diese")) {
            return null;
        }
        gigString = gigString.replaceFirst(" ", "").replaceFirst(" ", "");
        String[] times = gigString.substring(0, gigString.indexOf(' ') + 1).trim().split("-");
        Gig gig = new Gig();
        gig.setStartTime(format.parse(times[0] + " " + days[currentDay]));
        gig.setEndTime(format.parse(times[1] + " " + days[currentDay]));
        String artistName = gigString.substring(gigString.indexOf(' ') + 1).trim();
        gig.setArtist(new Artist(artistName));
        gig.setStage(currentStage);
        return gig;
    }
    
    
    
    private Stage nextStage() {
        currentStageIndex++;
        if (currentStageIndex >= stages.length) {
            currentStageIndex = 0;
        }
        currentStage = stages[currentStageIndex];
        return currentStage;
    }
    
    public List<Artist> loadArtists() {
        try {
            artists = new ArrayList<Artist>();
            Tidy tidy = new Tidy();
            URL url = new URL("http://www.wacken.com/de/bands/bands-billing/");
            InputStream openStream = url.openStream();
            Document document = tidy.parseDOM(openStream, System.out);
            loadArtists(document);
            return artists;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    private List<Artist> artists;
    private String       name;
    private String       imageLink;
    private boolean      inAnchor;
    private boolean      inArtistDiv;

    private void loadArtists(Node node) throws Exception {
        //        System.out.println(node.getLocalName());
        Node nodeClass = node.getAttributes().getNamedItem("class");

        if (inArtistDiv && "a".equals(node.getLocalName())) {
            inAnchor = true;
            enterAritstNode(node);
            inAnchor = false;
        } else if (inAnchor && node.getNodeValue() != null && !node.getNodeValue().isEmpty()) {
            name = new String(node.getNodeValue().getBytes(), "UTF-8");
        } else if (inAnchor && "img".equals(node.getLocalName())) {
            Node srcNode = node.getAttributes().getNamedItem("src");
            if (srcNode != null) {
                imageLink = srcNode.getNodeValue();
            }
        } else if (nodeClass != null) {
            createArtist();
            inArtistDiv = "col-sm-20".equals(nodeClass.getNodeValue());
            enterAritstNode(node);
        } else {
            enterAritstNode(node);
        }
    }

    private void createArtist() {
        if (name != null && imageLink != null) {
            Artist artist = new Artist(name);
            artist.setImageLink(imageLink);
            artists.add(artist);
            name = null;
            imageLink = null;
        }
    }

    private void enterAritstNode(Node node) throws Exception {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            loadArtists(childNodes.item(i));
        }
    }

    private void enterGigNode(Node node) throws Exception {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            loadGigs(childNodes.item(i));
        }
    }
}
