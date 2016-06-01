package de.desertfox.festivalplaner.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Set;

import de.desertfox.festivalplaner.model.Artist;
import de.desertfox.festivalplaner.model.Festival;
import de.desertfox.festivalplaner.model.Gig;
import de.desertfox.festivalplaner.model.Stage;

public class Exporter {
    private BufferedWriter writer;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd.M.yyyy HH:mm");

    public Exporter() throws IOException {
        writer = new BufferedWriter(new FileWriter(new File("C:\\Users\\d.donges\\Desktop\\wacsakenDescFile.xml")));
        writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
    }

    public void writeToDisk() throws IOException {
        writer.close();
    }

    public void writeFestivals(Set<Festival> festivals) throws IOException {
        for (Festival festival : festivals) {
            writeFestival(festival);
        }
    }
    
    public void writeStages(Set<Stage> stages) throws IOException {
        writer.append("\t<Stages>");
        writer.newLine();
        for (Stage stage : stages) {
            writeStage(stage);
        }
        writer.append("\t</Stages>");
        writer.newLine();
    }
    
    private void writeStage(Stage stage) throws IOException {
        writer.append("\t\t<Stage name=\"" + stage.getName() + "\"></Stage>");
        writer.newLine();
    }

    public void writeGig(Gig gig) throws IOException {
        writer.append("\t\t<Gig stage=\"" + gig.getStage().getName() + "\" start=\"" + formatter.format(gig.getStartTime()) + "\" end=\"" + formatter.format(gig.getEndTime()) + "\" bandname=\"" + gig.getArtist().getName() + "\"/>");
        writer.newLine();
    }
     
    public void writeGigs(Set<Gig> gigs) throws IOException {
        writer.append("\t<Gigs>");
        writer.newLine();
        
        for (Gig gig : gigs) {
            writeGig(gig);
        }
        writer.append("\t</Gigs>");
        writer.newLine();
    }
    
    public void writeFestival(Festival festival) throws IOException {
        writer.append("<Festival name=\"" + festival.getName() + "\">");
        writer.newLine();

        writeStages(festival.getStages());
        writeGigs(festival.getGigs());
        writeArtists(festival.getArtists());

        writer.append("</Festival>");
        writer.newLine();
    }

    public void writeArtists(Set<Artist> artists) throws IOException {
        writer.append("\t<Artists>");
        writer.newLine();
        for (Artist artist : artists) {

            writer.append("\t\t<Artist name=\"" + artist.getName() + "\"></Artist>");
            writer.newLine();
        }
        writer.append("\t</Artists>");
        writer.newLine();
    }
}
