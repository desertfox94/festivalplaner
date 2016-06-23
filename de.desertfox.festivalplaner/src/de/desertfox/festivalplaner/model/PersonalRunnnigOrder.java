package de.desertfox.festivalplaner.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PersonalRunnnigOrder extends RunningOrder {

    private Map<Gig, Set<Gig>> gigToCollisions = new HashMap<>();
    private Set<Gig> gigsWithGapProblems = new HashSet<>();

    public void add(Gig gig, Set<Gig> collisions) {
        addGigsToRunningOrder(gig);
        if (gigToCollisions.put(gig, collisions) != null) {
            addCollision(gig, collisions);            
        }
        for (Gig collision : collisions) {
            gigToCollisions.get(collision).add(gig);
        }
    }
    
    public void addGigWithGapProblems(Gig gig) {
        gigsWithGapProblems.add(gig);
    }
    
    public boolean hasGapProblems(Gig gig) {
        return gigsWithGapProblems.contains(gig);
    }

    public boolean isColliding(Gig gig) {
    	return gigToCollisions.get(gig) != null && !gigToCollisions.get(gig).isEmpty();
    }
    
    public void addCollision(Gig gig, Set<Gig> collisions) {
        gigToCollisions.put(gig, collisions);
    }
}
