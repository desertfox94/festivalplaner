/**
 * 
 */
package de.desertfox.festivalplaner.core.factory;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.desertfox.festivalplaner.model.Artist;

/**
 * @author desertfox94
 *
 */
public class ArtistFactory {

	private static final Logger logger = LoggerFactory.getLogger(ArtistFactory.class);
	
	public static Artist createArtist(RawArtist raw) {
		if (raw.name == null) {
			return null;
		}
		Artist artist = new Artist();
		artist.setName(raw.name.trim());
		try {
			artist.setImageUrl(new URL(raw.imageLink));
		} catch (MalformedURLException e) {
			logger.error("Could create URL from link: " + raw.imageLink, e);
		}
		return artist;
	}
	
}
