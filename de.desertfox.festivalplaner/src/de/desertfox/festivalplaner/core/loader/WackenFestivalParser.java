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

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
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

	private static final Logger	logger				= LoggerFactory.getLogger(WackenFestivalParser.class);
	public static final String	LINE_UP_URL			= "http://www.wacken.com/de/bands/bands-billing/";
	public static final String	RUNNING_ORDER_URL	= "http://www.wacken.com/de/bands/running-order/";

	/* LineUp member variables */
	private LineUp				lineUp;
	private RawArtist			raw;
	private boolean				inAnchor;
	private boolean				inArtistDiv;

	/* Runnnig Order member variables */
	private RunningOrder		runningOrder;
	private boolean				inGigDiv;
	private Stage[]				stages				= new Stage[] { new Stage("Black Stage"),
			new Stage("True Metal Stage"), new Stage("Party Stage"), new Stage("W:E:T Stage"),
			new Stage("Headbanges Stage"), new Stage("Wackinger Stage"), new Stage("Beer Garden Stage"),
			new Stage("Wasteland Stage") };
	private int					currentStageIndex;
	private Stage				currentStage;
	private int					currentDay;
	private String[]			days				= new String[] { "03.08.2016", "04.08.2016", "05.08.2016",
			"06.08.2016" };
	private SimpleDateFormat	format				= new SimpleDateFormat("HH:mm dd.MM.yyyy");

	public static void main(String[] args) {
		WackenFestivalParser parser = new WackenFestivalParser();
		LineUp lineUp = parser.parseLineUp();
		List<Artist> artists = lineUp.getArtists();
		for (Artist artist : artists) {
			System.out.println(artist);
		}
	}

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

	private void loadGigs(Node node) throws Exception {
		Node nodeClass = node.getAttributes().getNamedItem("class");
		if (inGigDiv) {
			Gig gig = createGig(new String(node.getNodeValue().getBytes(), "UTF-8"));
			if (gig != null) {
				runningOrder.put(gig.getArtist(), gig);
			}
		} else if (nodeClass != null && ("col-sm-80 rosc_time odd".equals(nodeClass.getNodeValue())
				|| "col-sm-80 rosc_time even".equals(nodeClass.getNodeValue()))) {
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
		Artist artist = getArtistFromLineUp(artistName);
		if (artist == null) {
			System.err.println(artistName);
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
	
	private Stage nextStage() {
		currentStageIndex++;
		if (currentStageIndex >= stages.length) {
			currentStageIndex = 0;
		}
		currentStage = stages[currentStageIndex];
		return currentStage;
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
