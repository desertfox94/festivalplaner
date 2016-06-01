package de.desertfox.festivalplaner.core.loader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.graphics.Image;

import de.desertfox.festivalplaner.core.FestivalDescriptor;
import de.desertfox.festivalplaner.model.Artist;
import de.desertfox.festivalplaner.model.Festival;
import de.desertfox.festivalplaner.util.AlphanumComparator;
import de.desertfox.festivalplaner.util.WebContentLoader;

public abstract class FestivalLoader {

    protected FestivalDescriptor festivalDescriptor;

    public static void main(String[] args) throws IOException {
        
        FestivalDescriptor festival = FestivalDescriptor.WACKEN;
        FestivalLoader festivalLoader = FestivalLoaderFactory.getFestivalLoader(festival);
        Set<Artist> createArtists = festivalLoader.loadArtists();
        festivalLoader.loadBandImageLinks(createArtists);
        List<String> names = new ArrayList<String>();
        File folder = new File("C:\\Users\\d.donges\\Desktop\\test\\" + festival.getName());
        for (Artist artist : createArtists) {
            try {
                folder.mkdirs();
                String name = artist.getName().trim();
                names.add(name);
                File file = new File(folder.getPath() + "\\" + name + ".jpeg");
                file.canWrite();
                saveImage(artist.getImageLink(), file);
            } catch (Exception e) {
                System.out.println(artist.getName());
                System.out.println(artist.getImageLink());
                e.printStackTrace();
            }
        }
        FileWriter w = new FileWriter(folder.getPath() + "\\_bands.txt");
        BufferedWriter writer = new BufferedWriter(w);
        Collections.sort(names, new AlphanumComparator());
        for (String bandName : names) {
            writer.write(bandName);
            writer.append("\n");
        }
        writer.close();
        
//        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_38);
//        HtmlPage page = webClient.getPage(FestivalDescriptor.HIGHFIELD.getLineUp());
//        System.out.println(page.asXml());
//        FestivalLoader wackenLoader = FestivalLoaderFactory.createFestivalLoader(FestivalDescriptor.WACKEN);
//        Festival festival = wackenLoader.loadFestival();
//        Set<Artist> artists = festival.getArtists();
//        wackenLoader.loadBandImageLinks(artists);
//        for (Artist artist : artists) {
//            try {
//                saveImage(artist.getImageLink(), "C:\\Users\\d.donges\\Desktop\\test\\" + artist.getName() + ".png");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }
    
    protected Image loadImage(Artist artist) {
        String imageLink = artist.getImageLink();
        if (imageLink == null) {
            return null;
        }
        try {
            URL url = new URL(imageLink);
            InputStream is = url.openStream();
            Image image = new Image(null, is);
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveImage(String imageUrl, File file) throws IOException {
        URL url;
        try {
            url = new URL(imageUrl);            
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return;
        }
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(file);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }
        is.close();
        os.close();
    }

    FestivalLoader(FestivalDescriptor festivalDescriptor) {
        super();
        this.festivalDescriptor = festivalDescriptor;
    }

    protected Set<String> findAllMatching(String regex, URL source) throws IOException {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(WebContentLoader.loadSourceCode(source));
        Set<String> matches = new HashSet<String>();
        while (matcher.find()) {
            matches.add(matcher.group(0));
        }
        return matches;
    }

    protected String findFirst(Pattern pattern, String source) {
        Matcher matcher = pattern.matcher(source);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }

    protected Set<String> loadBandNames() throws IOException {
        String bandNameRegex = festivalDescriptor.getBandNameRegex();
        Set<String> matches = findAllMatching(bandNameRegex, festivalDescriptor.getLineUp());
        return matches;
    }

    public abstract void loadBandImageLinks(Set<Artist> artists) throws IOException;

    public Set<Artist> loadArtists() throws IOException {
        Set<String> bandNames = loadBandNames();
        Set<Artist> artists = new HashSet<Artist>();
        for (String name : bandNames) {
            artists.add(new Artist(name));
        }
        return artists;
    }

    public Festival loadFestivalComplete() {
        Festival festival = null;
        try {
            festival = new Festival(festivalDescriptor);
            Set<Artist> artists = loadArtists();
            festival.addAllArtists(artists);
            loadBandImageLinks(artists);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return festival;
    }

}
