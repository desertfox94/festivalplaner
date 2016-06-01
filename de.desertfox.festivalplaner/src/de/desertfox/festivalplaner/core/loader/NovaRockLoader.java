package de.desertfox.festivalplaner.core.loader;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.http.client.utils.URIUtils;
import org.eclipse.osgi.util.NLS;

import de.desertfox.festivalplaner.core.FestivalDescriptor;
import de.desertfox.festivalplaner.model.Artist;
import de.desertfox.festivalplaner.util.WebContentLoader;

public class NovaRockLoader extends FestivalLoader {

    NovaRockLoader(FestivalDescriptor festivalDescriptor) {
        super(festivalDescriptor);
    }

    @Override
    protected Set<String> loadBandNames() throws IOException {
        Set<String> bandNames = super.loadBandNames();
        Set<String> names = new HashSet<String>();
        for (String name : bandNames) {
            name = name.replace("<h3 class=\"item__title\">", "");
            name = name.replace("</h3>", "");
            names.add(name);
        }
        return names;
    }

    @Override
    public void loadBandImageLinks(Set<Artist> artists) throws IOException {
        String sourceCode = WebContentLoader.loadSourceCode(festivalDescriptor.getLineUp());
        String name;
        for (Artist artist : artists) {
            name = artist.getName();
            if (name.contains("(") || name.contains(")")) {
                name = name.replace("(", "\\(");
                name = name.replace(")", "\\)");
            }
            String match = findFirst(Pattern.compile(NLS.bind(festivalDescriptor.getBandImageRegex(), name)), sourceCode);
            if (match == null) {
                continue;
            }
            try {
                
                String substring = match.substring(match.lastIndexOf("../fms") + 3, match.indexOf(".jpeg\" border"));
                
                artist.setImageLink("http://www.novarock.at/jart/prj3/" + substring + ".jpeg");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

}
