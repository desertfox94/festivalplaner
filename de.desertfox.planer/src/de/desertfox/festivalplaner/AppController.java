package de.desertfox.festivalplaner;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.graphics.Image;

import de.desertfox.festivalplaner.core.FestivalDescriptor;
import de.desertfox.festivalplaner.core.loader.FestivalLoader;
import de.desertfox.festivalplaner.core.loader.FestivalLoaderFactory;
import de.desertfox.festivalplaner.model.Artist;
import de.desertfox.festivalplaner.model.Festival;
import de.desertfox.festivalplaner.util.WebContentLoader;

public class AppController {

    private static final AppController        controller    = new AppController();
    private Map<FestivalDescriptor, Festival> festivals     = new HashMap<FestivalDescriptor, Festival>();
    private Map<String, Image>                imageRegistry = new HashMap<String, Image>();

    private AppController() {
    }

    public static AppController getInstance() {
        return controller;
    }

    public Collection<Festival> reloadFestivals() {
        if (festivals == null) {
            return Collections.emptySet();
        }
        festivals.clear();
        return loadFestivals();
    }

    public Festival getFestival(FestivalDescriptor descriptor) {
        loadFestivals();
        return festivals.get(descriptor);
    }

    public Collection<Festival> loadFestivals() {
        if (festivals.isEmpty()) {
            for (FestivalDescriptor festivalDescriptor : FestivalDescriptor.values()) {
                festivals.put(festivalDescriptor, new Festival(festivalDescriptor));
            }
        }
        return festivals.values();
    }

    public Image getArtistImage(Artist artist) {
        String imageUrl = artist.getImageLink();
        if (imageUrl == null || imageUrl.isEmpty()) {
            return null;
        }
        Image image = imageRegistry.get(imageUrl);
        if (image == null) {
            image = WebContentLoader.loadImage(imageUrl);
            imageRegistry.put(imageUrl, image);
        }
        return image;
    }

    public void loadArtists(Festival festival) {
        FestivalLoader festivalLoader = FestivalLoaderFactory.getFestivalLoader(festival.getDescriptor());
        try {
            Set<Artist> artists = festivalLoader.loadArtists();
            festival.addAllArtists(artists);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadImages(Festival festival) {
        FestivalLoader festivalLoader = FestivalLoaderFactory.getFestivalLoader(festival.getDescriptor());
        Set<Artist> artists = festival.getArtists();
        if (artists.isEmpty()) {
            loadArtists(festival);
        }
        try {
            festivalLoader.loadBandImageLinks(artists);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
