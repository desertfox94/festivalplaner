package de.desertfox.festivalplaner.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.desertfox.festivalplaner.model.Gig;

public class CollisionFinder {

    public static Set<Gig> findCollisions(Gig gig, Collection<Gig> gigs) {
        Set<Gig> collisions = new HashSet<>();
        for (Gig gigOfCollection : gigs) {
            if (gigOfCollection.equals(gig)) {
                continue;
            }
            if (DateUtil.arePeriodsColiding(gig, gigOfCollection)) {
                collisions.add(gigOfCollection);
            }
        }
        return collisions;
    }
    
}
