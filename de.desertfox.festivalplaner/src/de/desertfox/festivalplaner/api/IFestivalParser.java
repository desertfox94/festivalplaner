/**
 * Filename: IWebFestivalParser.java
 * Copyright: hsp Handels-Software-Partner GmbH - 2016
 * Purpose: 
 * 
 * Author: Dustin Donges
 * Revision: 
 * Created: 17.06.2016, 16:41:29
 */
package de.desertfox.festivalplaner.api;

import de.desertfox.festivalplaner.model.Festival;
import de.desertfox.festivalplaner.model.LineUp;
import de.desertfox.festivalplaner.model.RunningOrder;

/**
 * @author d.donges
 *
 */
public interface IFestivalParser {

    public Festival parseFestival() throws FestivalParseException;

    public LineUp parseLineUp();

    public RunningOrder parseRunningOrder();
    
    public boolean isRunningOrderAvailable();

    public boolean isLineUpAvailable();

    public void reload() throws FestivalParseException;
    
    public Festival getFestival();
    
}
