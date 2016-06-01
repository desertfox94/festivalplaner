package de.desertfox.festivalplaner.core;

import java.net.MalformedURLException;
import java.net.URL;

public enum FestivalDescriptor {

    WACKEN("Wacken", "http://www.wacken.com/de/bands/bands-billing/", "itle=\"Mehr über ».*?«\"><img src=\"ht", "{0}«\"><img src=\"http:.*?\" width=\"200\""),
    HIGHFIELD("Highfield", "http://www.highfield.de/de/line-up/", "", ""),
    NOVA_ROCK("Nova Rock", "http://www.novarock.at/jart/prj3/novarock/main.jart?content-id=1262873609302&rel=de&reserve-mode=active", "<h3 class=\"item__title\">.*?<\\/h3>", "<h3 class=\"item__title\">{0}<\\/h3>.*?src=\"images.*?.jpeg\" border=\"0\">"),
    HOERNERFEST("H�rnerfest", "http://veranstalter.wix.com/hoernerfest#!alle-bands/c5fz", "", "");

    private String name;
    private URL lineUp;
    private URL runningOrder;
    private String bandNameRegex;
    private String bandImageRegex;

    private FestivalDescriptor(String name, String lineUpLink, String regexPattern, String bandImageRegex) {
        try {
            this.lineUp = new URL(lineUpLink);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.bandNameRegex = regexPattern;
        this.bandImageRegex = bandImageRegex;
        this.name = name;
    }

    public URL getLineUp() {
        return lineUp;
    }

    public URL getRunningOrder() {
        return runningOrder;
    }

    public String getName() {
        return name;
    }

    public String getBandNameRegex() {
        return bandNameRegex;
    }

    public String getBandImageRegex() {
        return bandImageRegex;
    }
}
