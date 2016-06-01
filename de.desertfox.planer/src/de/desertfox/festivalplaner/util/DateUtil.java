/**
 * Filename: DateUtil.java
 * Copyright: hsp Handels-Software-Partner GmbH - 2016
 * Purpose: 
 * 
 * Author: Dustin Donges
 * Revision: 
 * Created: 01.06.2016, 17:59:32
 */
package de.desertfox.festivalplaner.util;

import java.util.Date;

/**
 * @author d.donges
 *
 */
public class DateUtil {

    public static boolean arePeriodsColiding(Date startDate1, Date endDate1, Date startDate2, Date endDate2) {
        if (startDate1.after(startDate2) && startDate1.before(endDate2)) {
            return true;
        } else if (startDate2.after(startDate1) && startDate2.before(endDate1)) {
            return true;
        } else if (endDate1.after(startDate2) && endDate1.before(endDate2)) {
            return true;
        } else if (endDate2.after(startDate1) && endDate2.before(endDate1)) {
            return true;
        }
        return false;
    }
    
}
