package de.desertfox.festivalplaner.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PersonalRunnnigOrder extends RunningOrder {

    private Map<Gig, Set<Gig>> gigToCollisions = new HashMap<>();

    public void add(Gig gig, Set<Gig> collisions) {
        addToRunningOrderGigs(gig);
        if (gigToCollisions.put(gig, collisions) != null) {
            addCollision(gig, collisions);            
        }
    }

    public boolean isColliding(Gig gig) {
    	return gigToCollisions.get(gig) != null && !gigToCollisions.get(gig).isEmpty();
    }
    
    public void addCollision(Gig gig, Set<Gig> collisions) {
        gigToCollisions.put(gig, collisions);
    }
}
