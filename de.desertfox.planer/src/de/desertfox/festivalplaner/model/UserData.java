package de.desertfox.festivalplaner.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class UserData implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Set<Artist>       favorites        = new HashSet<Artist>();

    public Set<Artist> getFavorites() {
        return favorites;
    }

    public void setFavorites(Set<Artist> favorites) {
        this.favorites = favorites;
    }

    public void addFavorite(Artist artist) {
        if (artist == null) {
            return;
        }
        favorites.add(artist);
    }

}
