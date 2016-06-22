package de.desertfox.festivalplaner.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Stage implements Serializable {

    private static final long serialVersionUID = 1L;
    private Festival          festival;
    private Set<Gig>          gigs             = new HashSet<Gig>();
    private String            name;

    public Stage(Festival festival, String name) {
        super();
        this.festival = festival;
        this.name = name;
    }

    public Stage(String name) {
        this.name = name;
    }

    public Festival getFestival() {
        return festival;
    }

    public void setFestival(Festival festival) {
        this.festival = festival;
    }

    public Set<Gig> getGigs() {
        return gigs;
    }

    public void setGigs(Set<Gig> gigs) {
        this.gigs = gigs;
    }

    public void addGig(Gig gig) {
        gigs.add(gig);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
