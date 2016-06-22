/**
 * Filename: Artist.java
 * Copyright: hsp Handels-Software-Partner GmbH - 2015
 * Purpose: 
 * 
 * Author: Dustin Donges
 * Revision: 
 * Created: 08.04.2015, 15:27:26
 */
package de.desertfox.festivalplaner.model;

import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author d.donges
 *
 */
public class Artist implements Serializable {

    private static final long       serialVersionUID = 1L;
    private String                  imageLink;
    private boolean                 favorite;
    private URL                     imageUrl;
    private String                  name;
    private Set<Gig>                gigs             = new HashSet<Gig>();
    private HashMap<String, Object> meta             = new HashMap<String, Object>();

    public Artist(String name) {
        this.name = name;
    }

    public Artist() {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Artist) {
            return ((Artist) obj).getName().toUpperCase().equals(name.toUpperCase());
        }
        return super.equals(obj);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public void addMeta(String key, Object value) {
        meta.put(key, value);
    }

    @Override
    public String toString() {
        return name;
    }

    public HashMap<String, Object> getMeta() {
        return meta;
    }

    public URL getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(URL imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

}
