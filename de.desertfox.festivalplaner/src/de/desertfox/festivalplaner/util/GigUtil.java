package de.desertfox.festivalplaner.util;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.desertfox.festivalplaner.model.Gig;

public class GigUtil {

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
    
    public static boolean fitsGap(Gig gig1, Gig gig2, int gapInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(gig1.getEndTime());
        cal.add(Calendar.MINUTE, gapInMinutes);
        return cal.getTime().compareTo(gig2.getStartTime()) < 0 || gig1.getStage().equals(gig2.getStage());
    }
    
}
