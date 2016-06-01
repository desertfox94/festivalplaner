package de.desertfox.festivalplaner.core.loader;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.osgi.util.NLS;

import de.desertfox.festivalplaner.core.FestivalDescriptor;
import de.desertfox.festivalplaner.model.Artist;
import de.desertfox.festivalplaner.util.WebContentLoader;

public class WackenLoader extends FestivalLoader {

    private WackenLoader(FestivalDescriptor festivalDescriptor) {
        super(festivalDescriptor);
    }
    
    WackenLoader() {
        this(FestivalDescriptor.WACKEN);
    }

    @Override
    protected Set<String> loadBandNames() throws IOException {
        Set<String> rawBandNames = super.loadBandNames();
        Set<String> bandNames = new HashSet<String>();
        for (String raw : rawBandNames) {
            String name = raw.substring(raw.indexOf("»") + 2);
            name = name.substring(0, name.indexOf("Â«"));
            bandNames.add(name);
        }
        return bandNames;
    }

    @Override
    public void loadBandImageLinks(Set<Artist> artists) throws IOException {
        String sourceCode = WebContentLoader.loadSourceCode(festivalDescriptor.getLineUp());
        String bandImageRegex = festivalDescriptor.getBandImageRegex();
        for (Artist artist : artists) {
            String link = findFirst(Pattern.compile(NLS.bind(bandImageRegex, artist.getName().replace("?",""))), sourceCode);
            if (link == null) {
                System.err.println(artist.getName());
                continue;
            }
            link = link.substring(link.indexOf("\"><img src=\""));
            link = link.replaceFirst("\"><img src=\"", "");
            link = link.replaceFirst("\" width=\"200\"", "");
            artist.setImageLink(link);
        }
    }

}
