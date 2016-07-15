package de.desertfox.festivalplaner.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtil {

    /**
     * Reads a complete file into a string. <br>
     * The file must not be <code>null</code>.
     * 
     * @param file
     * @return The content-string
     * @throws IOException
     */
    public static String readCompleteFile(File file) throws IOException {
        int len = (int) (file.length());
        FileInputStream fis = new FileInputStream(file);
        byte buf[] = new byte[len];
        fis.read(buf);
        fis.close();
        return new String(buf);
    }
    
}
