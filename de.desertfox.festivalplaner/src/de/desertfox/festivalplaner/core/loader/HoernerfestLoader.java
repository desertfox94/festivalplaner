package de.desertfox.festivalplaner.core.loader;

import java.io.IOException;
import java.util.Set;

import de.desertfox.festivalplaner.core.FestivalDescriptor;
import de.desertfox.festivalplaner.model.Artist;

public class HoernerfestLoader extends FestivalLoader {

    HoernerfestLoader(FestivalDescriptor festivalDescriptor) {
        super(festivalDescriptor);
    }

    @Override
    public void loadBandImageLinks(Set<Artist> artists) throws IOException {
    }

}
