package de.desertfox.festivalplaner.core.loader;

import java.io.InputStream;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

import de.desertfox.festivalplaner.api.FestivalParseException;
import de.desertfox.festivalplaner.api.IFestivalParser;
import de.desertfox.festivalplaner.model.Festival;

public abstract class AbstractFestivalLoader implements IFestivalParser {

	private static final Logger	logger	= LoggerFactory.getLogger(AbstractFestivalLoader.class);
	protected Tidy				loader;
	private Festival			festival;

	public AbstractFestivalLoader() {
		init();
	}

	protected void init() {
		loader = new Tidy();
	}

	@Override
	public Festival parseFestival() throws FestivalParseException {
		festival = new Festival();
		festival.setLineUp(parseLineUp());
		festival.setRunningOrder(parseRunningOrder());
		return null;
	}

	protected Festival ensureFestivalLoaded() throws FestivalParseException {
		if (festival == null) {
			festival = parseFestival();
		}
		return festival;
	}

	@Override
	public boolean isRunningOrderAvailable() {
		try {
			if (ensureFestivalLoaded() == null) {
				return false;
			}
		} catch (FestivalParseException e) {
			logger.error("Failed to ensure festival.", e); //$NON-NLS-1$
			return false;
		}
		return festival.getRunningOrder() != null;
	}

	@Override
	public boolean isLineUpAvailable() {
		try {
			if (ensureFestivalLoaded() == null) {
				return false;
			}
		} catch (FestivalParseException e) {
			logger.error("Failed to ensure festival.", e); //$NON-NLS-1$
			return false;
		}
		return festival.getLineUp() != null;
	}

	@Override
	public void reload() {
		try {
			parseFestival();
		} catch (FestivalParseException e) {
			logger.error("Failed to ensure festival.", e); //$NON-NLS-1$
		}
	}

	protected Document loadDocument(String path) {
		InputStream inputStream = null;
		try {
			URL url = new URL(path);
			inputStream = url.openStream();
			Document doc = loader.parseDOM(inputStream, System.out);
			inputStream.close();
			inputStream = null;
			return doc;
		} catch (Exception e) {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception eIngored) {
				}
			}
			logger.error("Error while loading the document in this location: " + path, e);
		}
		return null;
	}

	@Override
	public Festival getFestival() {
		return festival;
	}
}
