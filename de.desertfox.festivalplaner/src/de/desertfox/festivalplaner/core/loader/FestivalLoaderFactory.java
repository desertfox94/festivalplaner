package de.desertfox.festivalplaner.core.loader;

import de.desertfox.festivalplaner.core.FestivalDescriptor;

public class FestivalLoaderFactory {

    public static FestivalLoader getFestivalLoader(FestivalDescriptor festivalDescriptor) {
        switch (festivalDescriptor) {
            case WACKEN:
                return new WackenLoader();
            case NOVA_ROCK:
                return new NovaRockLoader(festivalDescriptor);
            default:
                return null;
        }
    }
    
}
