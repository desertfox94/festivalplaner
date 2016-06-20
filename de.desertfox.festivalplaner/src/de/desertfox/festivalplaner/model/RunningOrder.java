package de.desertfox.festivalplaner.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.desertfox.festivalplaner.util.GigComparator;

public class RunningOrder {

	private Map<Artist, Set<Gig>>	artistToGigs	= new HashMap<Artist, Set<Gig>>();
	private List<Gig>				gigsOrdered		= new ArrayList<>();
	private GigComparator			comparator		= new GigComparator();

	public void put(Artist artist, Set<Gig> gigs) {
		addGigs(gigs);
		artistToGigs.put(artist, gigs);
	}

	public void put(Artist artist, Gig... gigs) {
		if (artistToGigs.get(artist) == null) {
			artistToGigs.put(artist, new HashSet<Gig>());
		}
		addToRunningOrderGigs(gigs);
		for (Gig gig : gigs) {
			artistToGigs.get(artist).add(gig);
		}
	}

	protected void addToRunningOrderGigs(Gig... gigs) {
		for (Gig gig : gigs) {
			gigsOrdered.add(gig);
		}
		Collections.sort(gigsOrdered, comparator);
	}

	protected void addGigs(Set<Gig> gigs) {
		gigsOrdered.addAll(gigs);
		Collections.sort(gigsOrdered, comparator);
	}

	public Set<Gig> getGigs(Artist artist) {
		if (artistToGigs.get(artist) != null) {
			return artistToGigs.get(artist);
		}
		Set<Entry<Artist, Set<Gig>>> entrySet = artistToGigs.entrySet();
		for (Entry<Artist, Set<Gig>> entry : entrySet) {
			if (artist.equals(entry.getKey())) {
				return entry.getValue();
			}
		}
		return artistToGigs.get(artist);
	}

	public Set<Entry<Artist, Set<Gig>>> getArtistsWithGigs() {
		return artistToGigs.entrySet();
	}

	public Map<Artist, Set<Gig>> getArtistToGigs() {
		return artistToGigs;
	}

	public List<Gig> getGigsOrdered() {
		return gigsOrdered;
	}

}
