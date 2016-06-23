package de.desertfox.festivalplaner.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.desertfox.festivalplaner.util.ArtistComparator;

public class LineUp {

    private List<Artist> artists       = new ArrayList<>();
    private List<Gig>    specialEvents = new ArrayList<>();

    public void add(Artist artist) {
        artists.add(artist);
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public void addSpecialEvent(Gig gig) {
        specialEvents.add(gig);
    }

    public List<Gig> getSpecialEvents() {
        return specialEvents;
    }

    public List<Artist> getArtistsAndSpecialEvents() {
        ArrayList<Artist> result = new ArrayList<>(artists);
        for (Gig specialEvent : specialEvents) {
            result.add(specialEvent.getArtist());
        }
        Collections.sort(result, new ArtistComparator());
        return result;
    }

}
