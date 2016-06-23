package de.desertfox.festivalplaner.util;

import java.util.Comparator;

import de.desertfox.festivalplaner.model.Artist;

public class ArtistComparator implements Comparator<Artist> {

    private AlphanumComparator comparator = new AlphanumComparator();

    @Override
    public int compare(Artist a1, Artist a2) {
        return comparator.compare(a1.getName().toLowerCase(), a2.getName().toLowerCase());
    }

}
