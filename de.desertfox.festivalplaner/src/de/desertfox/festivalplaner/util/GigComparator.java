package de.desertfox.festivalplaner.util;

import java.util.Comparator;

import de.desertfox.festivalplaner.model.Gig;

public class GigComparator implements Comparator<Gig> {

    @Override
    public int compare(Gig gig1, Gig gig2) {
        return gig1.getStartTime().compareTo(gig2.getStartTime());
    }

}
