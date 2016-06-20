package de.desertfox.festivalplaner.model;

import java.util.ArrayList;
import java.util.List;

public class LineUp {

	private List<Artist> artists = new ArrayList<>();

	public void add(Artist artist) {
		artists.add(artist);
	}
	
	public List<Artist> getArtists() {
		return artists;
	}

	public void setArtists(List<Artist> artists) {
		this.artists = artists;
	}

}
