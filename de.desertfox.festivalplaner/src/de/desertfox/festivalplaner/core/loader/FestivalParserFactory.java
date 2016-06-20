/**
 * 
 */
package de.desertfox.festivalplaner.core.loader;

import de.desertfox.festivalplaner.api.IFestivalParser;

/**
 * @author desertfox94
 *
 */
public class FestivalParserFactory {

	public enum FestivalIdentifier {
		WACKEN;
	}

	public static IFestivalParser createFestivalParser(FestivalIdentifier identifier) {
		if (identifier == null) {
			return null;
		}
		switch (identifier) {
		case WACKEN:
			return new WackenFestivalParser();
		default:
			return null;
		}
	}

}
