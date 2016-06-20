package de.desertfox.festivalplaner.core.loader;

import de.desertfox.festivalplaner.api.IFestivalParser;
import de.desertfox.festivalplaner.model.Festival;

public abstract class AbstractFestivalLoader implements IFestivalParser {

    private Festival festival;

    @Override
    public Festival parseFestival() {
        festival = new Festival();
        festival.setLineUp(parseLineUp());
        festival.setRunningOrder(parseRunningOrder());
        return null;
    }

    protected Festival ensureFestivalLoaded() {
        if (festival == null) {
            festival = parseFestival();
        }
        return festival;
    }

    @Override
    public boolean isRunningOrderAvailable() {
        if (ensureFestivalLoaded() == null) {
            return false;
        }
        return festival.getRunningOrder() != null;
    }

    @Override
    public boolean isLineUpAvailable() {
        if (ensureFestivalLoaded() == null) {
            return false;
        }
        return festival.getLineUp() != null;
    }
    
    @Override
    public void reload() {
        parseFestival();
    }
    
    @Override
    public Festival getFestival() {
        return festival;
    }

}
