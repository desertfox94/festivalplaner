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

import de.desertfox.festivalplaner.api.IWebFestivalParser;
import de.desertfox.festivalplaner.model.Artist;
import de.desertfox.festivalplaner.model.Gig;
import de.desertfox.festivalplaner.model.PersonalRunnnigOrder;
import de.desertfox.festivalplaner.model.RunningOrder;
import de.desertfox.festivalplaner.util.CollisionFinder;

/**
 * @author d.donges
 *
 */
public class PersonalRunnigOrderBuilder {

    public static PersonalRunnnigOrder buildRunningOrder(List<Artist> artists, IWebFestivalParser festivalParser) {
        return buildRunningOrder(artists, festivalParser.parseRunningOrder());
    }

    public static PersonalRunnnigOrder buildRunningOrder(List<Artist> artists, RunningOrder runningOrder) {
        PersonalRunnnigOrder personalRunnnigOrder = new PersonalRunnnigOrder();
        for (Artist artist : artists) {
            Set<Gig> gigs = runningOrder.getGigs(artist);
            for (Gig gig : gigs) {
                Set<Gig> collisions = CollisionFinder.findCollisions(gig, runningOrder.getGigsOrdered());
                personalRunnnigOrder.add(gig, collisions);
            }
        }
        return personalRunnnigOrder;
    }

}
