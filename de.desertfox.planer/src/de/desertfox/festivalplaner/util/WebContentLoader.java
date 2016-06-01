/**
 * Filename: WebSiteSourceLoader.java
 * Copyright: hsp Handels-Software-Partner GmbH - 2015
 * Purpose: 
 * 
 * Author: Dustin Donges
 * Revision: 
 * Created: 22.12.2015, 14:36:17
 */
package de.desertfox.festivalplaner.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.eclipse.swt.graphics.Image;

/**
 * @author d.donges
 *
 */
public class WebContentLoader {

    public static String loadSourceCode(URL url) throws IOException {
        URLConnection spoof = url.openConnection();

        //Spoof the connection so we look like a web browser
        spoof.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0;    H010818)");
        BufferedReader in = new BufferedReader(new InputStreamReader(spoof.getInputStream()));
        String strLine = "";
        String finalHTML = "";
        //Loop through every line in the source
        while ((strLine = in.readLine()) != null) {
            finalHTML += strLine + "\n";
        }
        return finalHTML;
    }

    public static Image loadImage(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            InputStream is = url.openStream();
            Image image = new Image(null, is);
            is.close();
            return image;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
