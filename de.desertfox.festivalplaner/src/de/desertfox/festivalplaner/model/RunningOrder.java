package de.desertfox.festivalplaner.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.desertfox.festivalplaner.util.GigComparator;

public class RunningOrder {

    public static final SimpleDateFormat DATE_FORMAT= new SimpleDateFormat("dd.MM.yyyy");
    private Map<Artist, Set<Gig>>  artistToGigs        = new HashMap<Artist, Set<Gig>>();
    private Map<String, List<Gig>> dayOfFestivalToGigs = new HashMap<>();
    private List<Gig>              gigsOrdered         = new ArrayList<>();
    private GigComparator          comparator          = new GigComparator();

    public void put(Artist artist, Set<Gig> gigs) {
        addGigs(gigs);
        artistToGigs.put(artist, gigs);
    }

    public void put(Artist artist, Gig... gigs) {
        if (artistToGigs.get(artist) == null) {
            artistToGigs.put(artist, new HashSet<Gig>());
        }
        addGigsToRunningOrder(gigs);
        for (Gig gig : gigs) {
            artistToGigs.get(artist).add(gig);
        }
    }

    protected void addGigsToRunningOrder(Gig... gigs) {
        for (Gig gig : gigs) {
            gigsOrdered.add(gig);
            addDayOfFestival(gig.getDayOfFestival());
            addGigToDayOfFestival(gig);
        }
        Collection<List<Gig>> values = dayOfFestivalToGigs.values();
        for (List<Gig> gigsByDay : values) {
            Collections.sort(gigsByDay, comparator);
        }
        Collections.sort(gigsOrdered, comparator);
    }

    protected void addGigToDayOfFestival(Gig gig) {
        Date dayOfFestival = gig.getDayOfFestival();
        addDayOfFestival(dayOfFestival);
        dayOfFestivalToGigs.get(DATE_FORMAT.format(dayOfFestival)).add(gig);
    }

    protected void addGigs(Set<Gig> gigs) {
        addGigsToRunningOrder(gigs.toArray(new Gig[0]));
    }

    public Set<Gig> getGigs(Artist artist) {
        if (artistToGigs.get(artist) != null) {
            return artistToGigs.get(artist);
        }
        Set<Entry<Artist, Set<Gig>>> entrySet = artistToGigs.entrySet();
        for (Entry<Artist, Set<Gig>> entry : entrySet) {
            if (artist.equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return artistToGigs.get(artist);
    }

    public void addDayOfFestival(Date date) {
        String format = DATE_FORMAT.format(date);
        if (!dayOfFestivalToGigs.containsKey(format)) {
            dayOfFestivalToGigs.put(format, new ArrayList<Gig>());
        }
    }

    public void setDaysOfFestival(Collection<Date> days) {
        for (Date day : days) {
            addDayOfFestival(day);
        }
    }
    
    public List<Date> getDaysOfFestival() {
        List<Date> result = new ArrayList<>();
        for (String dayString : dayOfFestivalToGigs.keySet()) {
            try {
                result.add(DATE_FORMAT.parse(dayString));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Collections.sort(result, new Comparator<Date>() {
            @Override
            public int compare(Date o1, Date o2) {
                if (o1.before(o2)) {
                    return -1;
                } else if (o1.after(o2)) {
                    return 1;
                }
                return 0;
            }
        });
        return result;
    }

    public List<Gig> getGigsByDay(Date day) {
        return dayOfFestivalToGigs.get(DATE_FORMAT.format(day));
    }
    
    public Map<String, List<Gig>> getGigsByDay() {
        return dayOfFestivalToGigs;
    }

    public Set<Entry<Artist, Set<Gig>>> getArtistsWithGigs() {
        return artistToGigs.entrySet();
    }

    public Map<Artist, Set<Gig>> getArtistToGigs() {
        return artistToGigs;
    }

    public List<Gig> getGigsOrdered() {
        return gigsOrdered;
    }

}
