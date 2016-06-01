package de.desertfox.festivalplaner.core;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import de.desertfox.festivalplaner.model.Artist;
import de.desertfox.festivalplaner.model.Festival;
import de.desertfox.festivalplaner.model.Gig;
import de.desertfox.festivalplaner.model.Stage;

public class FestivalManager {
    
//    
//    public Set<Festival> createFestivalFromFile(String path) {
//        return createFestivalFromFile(new File(path));
//    }
    
//    public Set<Festival> createFestivalFromFile(File xmlDescFile) {
//        
//        try {
//            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
//            FestivalFileHandler dh = new FestivalFileHandler();
//            parser.parse(xmlDescFile, dh);
//            return dh.getFestivals();
//        } catch (ParserConfigurationException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (SAXException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return null;
//    }
    
//    private class FestivalFileHandler extends DefaultHandler {
//        private SimpleDateFormat formatter = new SimpleDateFormat("dd.M.yyyy HH:mm");
//        private Set<Festival> festivals = new HashSet<Festival>();
//        private Map<String, Stage> stageMap = new HashMap<String, Stage>();
//        private Map<String, Artist> artistMap = new HashMap<String, Artist>();
//        private Festival currentFestival;
//        
//        @Override
//        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
//            if (qName.equals("Festival")) {
//                currentFestival = new Festival(attributes.getValue("name"));
//                festivals.add(currentFestival);
//            }
//            if (qName.equals("Artist")) {
//                String name = attributes.getValue("name").toUpperCase();
//                Artist artist = new Artist(name);
//                artistMap.put(name, artist);
//                currentFestival.addArtist(artist);
//            }
//            if (qName.equals("Stage")) {
//                String name = attributes.getValue("name");
//                Stage stage = new Stage(currentFestival, name);
//                stageMap.put(name, stage);
//                currentFestival.addStage(stage);
//            }
//            if (qName.equals("Gig")) {
//                Date end = null;
//                Date start = null;
//                try {
//                    start = formatter.parse(attributes.getValue("start"));
//                    end = formatter.parse(attributes.getValue("end"));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                Gig gig = new Gig(stageMap.get(attributes.getValue("stage")), start, end, artistMap.get(attributes.getValue("bandname")));
//                currentFestival.addGig(gig);
//            }
//            super.startElement(uri, localName, qName, attributes);
//        }
//        
//        @Override
//        public void characters(char[] ch, int start, int length) throws SAXException {
//            // TODO Auto-generated method stub
//            super.characters(ch, start, length);
//        }
//        
//        @Override
//        public void endElement(String uri, String localName, String qName) throws SAXException {
//            // TODO Auto-generated method stub
//            super.endElement(uri, localName, qName);
//        }
//
//        public Set<Festival> getFestivals() {
//            return festivals;
//        }
//        
//        
//    }
    
    
}
