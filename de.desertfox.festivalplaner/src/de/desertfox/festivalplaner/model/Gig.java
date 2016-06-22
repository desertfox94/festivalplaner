package de.desertfox.festivalplaner.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Gig implements Serializable {
    
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM HH:mm");
    
    private static final long serialVersionUID = 1L;
    private Stage stage;
    private Date dayOfFestival;
    private Date startTime;
    private Date endTime;
    
    public Gig(Stage stage, Date startTime, Date endTime, Artist artist) {
        this.stage = stage;
        this.startTime = startTime;
        this.endTime = endTime;
        this.artist = artist;
    }
    
    public Gig() {
    }
    
    private Artist artist;
    
    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Gig) {
            Gig gig = (Gig) obj;
            if (gig.getArtist() == null || !gig.getArtist().equals(artist)) {
                return false;
            }
            return gig.getStartTime().equals(startTime) && gig.getEndTime().equals(endTime);
        } else {
            return super.equals(obj);
        }
    }
    
    @Override
    public String toString() {
//        return DATE_FORMAT.format(startTime) + "-" + DATE_FORMAT.format(endTime) + " " + artist.getName();
        return TIME_FORMAT.format(startTime) + "-" + TIME_FORMAT.format(endTime) + " " + artist.getName();
    }

    public Date getDayOfFestival() {
        return dayOfFestival;
    }

    public void setDayOfFestival(Date dayOfFestival) {
        this.dayOfFestival = dayOfFestival;
    }
    
}
