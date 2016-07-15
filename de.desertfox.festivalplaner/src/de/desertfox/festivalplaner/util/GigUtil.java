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

    public static Gig findFirst(Collection<Gig> gigs) {
        Gig first = null;
        for (Gig gig : gigs) {
            if (first == null) {
                first = gig;
                continue;
            }
            if (gig.getStartTime().before(first.getStartTime())) {
                first = gig;
            }
        }
        return first;
    }

    public static Gig findLast(Collection<Gig> gigs) {
        Gig last = null;
        for (Gig gig : gigs) {
            if (last == null) {
                last = gig;
                continue;
            }
            if (gig.getEndTime().after(last.getEndTime())) {
                last = gig;
            }
        }
        return last;
    }

}
