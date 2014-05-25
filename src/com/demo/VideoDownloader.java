package com.demo;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class VideoDownloader {

    /**
     * Saves the content of the {@code url} to a file with the name
     * {@code filename}
     * 
     * @param filename
     * @param url
     */
    public void saveVideo(String filename, String url) {
        FileOutputStream fout = null;
        try {
            BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
            final byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1) {
                fout.write(data, 0, count);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
