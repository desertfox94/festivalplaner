/**
 * Filename: PersonalRunnigOrderBuilder.java
 * Copyright: hsp Handels-Software-Partner GmbH - 2016
 * Purpose: 
 * 
 * Author: Dustin Donges
 * Revision: 
 * Created: 20.06.2016, 15:12:03
 */
package de.desertfox.festivalplaner.core;

import java.util.List;
import java.util.Set;

import de.desertfox.festivalplaner.api.IFestivalParser;
import de.desertfox.festivalplaner.model.Artist;
import de.desertfox.festivalplaner.model.Gig;
import de.desertfox.festivalplaner.model.PersonalRunnnigOrder;
import de.desertfox.festivalplaner.model.RunningOrder;
import de.desertfox.festivalplaner.util.GigUtil;

/**
 * @author d.donges
 *
 */
public class PersonalRunnigOrderBuilder {

    public static PersonalRunnnigOrder buildRunningOrder(List<Artist> artists, IFestivalParser festivalParser) {
        return buildRunningOrder(artists, festivalParser.parseRunningOrder());
    }

    public static PersonalRunnnigOrder buildRunningOrder(List<Artist> artists, RunningOrder runningOrder) {
        PersonalRunnnigOrder personalRunnnigOrder = new PersonalRunnnigOrder();
        personalRunnnigOrder.setDaysOfFestival(runningOrder.getDaysOfFestival());
        for (Artist artist : artists) {
            Set<Gig> gigs = runningOrder.getGigs(artist);
            if (gigs == null) {
                System.err.println(artist);
                continue;
            }
            for (Gig gig : gigs) {
                Set<Gig> collisions = GigUtil.findCollisions(gig, personalRunnnigOrder.getGigsOrdered());
                personalRunnnigOrder.add(gig, collisions);
            }
        }
        Gig previous = null;
        for (Gig gig : personalRunnnigOrder.getGigsOrdered()) {
            if (previous == null) {
                previous = gig;
                continue;
            }
            if (!GigUtil.fitsGap(previous, gig, 15)) {
                personalRunnnigOrder.addGigWithGapProblems(gig);
            }
            previous = gig;
        }
        return personalRunnnigOrder;
    }

}
