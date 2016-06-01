package de.desertfox.festivalplaner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.desertfox.festivalplaner.core.Exporter;
import de.desertfox.festivalplaner.model.Artist;
import de.desertfox.festivalplaner.model.Festival;
import de.desertfox.festivalplaner.model.Gig;
import de.desertfox.festivalplaner.model.Stage;
import de.desertfox.festivalplaner.ui.AppWindow;

public class Main {

    public static void main(String[] args) {

//                FestivalManager manager = new FestivalManager();
//                Set<Festival> festivals = manager.createFestivalFromFile(new File("C:\\Users\\d.donges\\Desktop\\festivalplaner\\descFile.xml"));
//                for (Festival festival : festivals) {
//                    for (Artist artist : festival.getArtists()) {
//                        System.out.println(artist.getName());
//                    }
//                }
                
                AppWindow window = new AppWindow();
                
                window.open();
                
                
        //      parse Artist from file        
        //      writeArtists(loadArtists());
        
//        readGigs();
        
//        calendar.set
//        calendar.set(2015, 7, 29, 11, 0);
//        System.out.println(calendar.getTime());
        
    }
    
//    private static void readGigs() {
//        SimpleDateFormat formatter = new SimpleDateFormat("dd.M.yyyy HH:mm");
//        try {
//            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\d.donges\\Desktop\\festivalplaner\\gigs.txt"));
//            String day = null;
//            Stage stage = null;
//            String line;
//            int rowIndex = 0;
//            String bandName;
//            Date start = null;
//            Date end = null;
//            Set<Gig> gigs = new HashSet<Gig>();
//            while ((line = reader.readLine()) != null) {
//                if (line.contains("*")) {
//                    day = line.replace("*", "");
//                    continue;
//                }
//                if (line.contains("#")) {
//                    stage = new Stage(new Festival("Wacken"), line.replace("#", "").trim());
//                    continue;
//                }
//                line = line.trim();
//                switch (rowIndex) {
//                    case 0:
//                        start = formatter.parse(day + " " + line);
//                        rowIndex++;
//                        break;
//                    case 1:
//                        end = formatter.parse(day + " " + line);
//                        rowIndex++;
//                        break;
//                    case 2:
//                        bandName = line;
//                        Gig gig = new Gig(stage, start, end, new Artist(bandName));
//                        gigs.add(gig);
//                        rowIndex = 0;
//                        break;
//
//                    default:
//                        break;
//                }
//            }
//            reader.close();
//            Exporter exporter = new Exporter();
//            exporter.writeGigs(gigs);
//            exporter.writeToDisk();
//            
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//    }

    
    
    private static Set<Artist> loadArtists() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\d.donges\\Desktop\\wacken2015.txt"));
            Set<Artist> artists = new HashSet<Artist>();
            String line;
            while ((line = reader.readLine()) != null) {
                artists.add(new Artist(line.trim()));
                System.out.println(line);
            }
            reader.close();
            return artists;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
