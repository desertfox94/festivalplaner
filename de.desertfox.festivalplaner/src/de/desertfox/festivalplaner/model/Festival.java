package de.desertfox.festivalplaner.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.desertfox.festivalplaner.core.FestivalDescriptor;

/**
 * @author d.donges
 *
 */
public class Festival implements Serializable {

    private static final long  serialVersionUID = 1L;
    private Set<Stage>         stages           = new HashSet<Stage>();
    private Set<Artist>        artists          = new HashSet<Artist>();
    private Set<Gig>           gigs             = new HashSet<Gig>();
    private FestivalDescriptor descriptor;

    public Festival(FestivalDescriptor descriptor) {
        super();
        this.descriptor = descriptor;
    }

    public Set<Stage> getStages() {
        return stages;
    }

    public void addStage(Stage stage) {
        stages.add(stage);
    }

    public void setStages(Set<Stage> stages) {
        this.stages = stages;
    }

    public Set<Artist> getArtists() {
        return artists;
    }

    public void setArtists(Set<Artist> artists) {
        this.artists = artists;
    }

    public void addArtist(Artist artist) {
        artists.add(artist);
    }

    public void addAllArtists(Collection<Artist> artists) {
        this.artists.addAll(artists);
    }

    public String getName() {
        return descriptor.getName();
    }

    public Set<Gig> getGigs() {
        return gigs;
    }

    public void addGig(Gig gig) {
        gigs.add(gig);
    }

    public void setGigs(Set<Gig> gigs) {
        this.gigs = gigs;
    }

    public FestivalDescriptor getDescriptor() {
        return descriptor;
    }

}
