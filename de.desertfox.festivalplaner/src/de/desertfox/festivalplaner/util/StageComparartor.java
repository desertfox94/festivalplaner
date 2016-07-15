package de.desertfox.festivalplaner.util;

import java.util.Comparator;

import de.desertfox.festivalplaner.model.Stage;

public class StageComparartor implements Comparator<Stage> {

    private AlphanumComparator comparator = new AlphanumComparator();

    @Override
    public int compare(Stage s1, Stage s2) {
        return comparator.compare(s1.getName(), s2.getName());
    }

}
