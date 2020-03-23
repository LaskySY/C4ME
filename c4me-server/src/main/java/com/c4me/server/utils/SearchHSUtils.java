package com.c4me.server.utils;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import static com.c4me.server.config.constant.Const.Filenames.TEST_HIGH_SCHOOL_SEARCH_URL;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-22-2020
 */

public class SearchHSUtils {



    public static void testNicheSearchScrape() throws IOException {
        System.out.println("testing niche search scrape");

        File file = new File("testSearchResultsFile");
        file.createNewFile();

        URL url = new URL(TEST_HIGH_SCHOOL_SEARCH_URL);
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:31.0) Gecko/20100101 Firefox/31.0");
        conn.connect();
        InputStream is = conn.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);


        //FileUtils.copyInputStreamToFile(conn.getInputStream(), file);

        //FileUtils.copyURLToFile(url, file);


    }

}
