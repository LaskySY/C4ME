package com.c4me.server.utils;

import org.apache.commons.text.similarity.FuzzyScore;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.c4me.server.config.constant.Const.Filenames.ALL_HIGH_SCHOOLS_FILE;
import static com.c4me.server.config.constant.Const.Filenames.TEST_HIGH_SCHOOL_SEARCH_URL;
import static com.c4me.server.config.constant.Const.States.STATES_LIST;

/**
 * @Description: Utility functions for searching for high school urls
 * @Author: Maciej Wlodek
 * @CreateDate: 03-22-2020
 */

public class SearchHSUtils {

    /**
     * Test whether a url matches the niche.com high school url template
     * @deprecated
     * @param url {@link String} search result url
     * @return boolean - whether it matches or not
     */
    private static boolean urlMatches(String url) {
        String prefix = "https://www.niche.com/k12/";
        if(!url.startsWith(prefix)) return false;
        String suffix = url.substring(prefix.length());
        if(suffix.length() < 5) return false;
        String suffix_begin = suffix.substring(0, suffix.length()-4);
        String suffix_end = suffix.substring(suffix.length()-4);
        if(suffix_begin.contains("/")) return false;
        //if(!suffix_begin.contains("high-school")) return false;
        //if(suffix_begin.contains("junior-high-school")) return false;

        if(suffix_end.charAt(0) != '-' || suffix_end.charAt(3) != '/') return false;
        String state = suffix_end.substring(1,3);
        if(!STATES_LIST.contains(state.toUpperCase())) return false;
        return true;
    }

    /**
     * Get a list of hrefs from a google search results page
     * @deprecated
     * @param resultPage {@link Document} jsoup google search result page
     * @return {@link List} of hrefs
     */
    private static List<String> getHrefs(Document resultPage) {
        ArrayList<String> hrefs = new ArrayList<>();

        Elements results = resultPage.select("div.r");
        for(Element e : results) {
            Elements subelements = e.select("a");
            for(Element e2 : subelements) {
                if(e2.hasAttr("href")) hrefs.add(e2.attr("href"));
            }
        }
        return hrefs;
    }

    /**
     * Test a scrape of google search results to get the niche url
     * @deprecated
     * @throws IOException
     */
    public static void testNicheSearchScrape() throws IOException {
        System.out.println("testing niche search scrape");

        //File file = new File("testSearchResultsFile");
        //file.createNewFile();

        System.out.println("connecting with jsoup");
        Document resultPage = Jsoup.connect(TEST_HIGH_SCHOOL_SEARCH_URL).get();
        List<String> urls = getHrefs(resultPage);
        List<String> matchingUrls = urls.stream().filter(SearchHSUtils::urlMatches).collect(Collectors.toList());

        System.out.println("num matching schools = " + matchingUrls.size());
        matchingUrls.stream().forEach(System.out::println);
    }

    /**
     * Build a google search query from a high school name query
     * @deprecated
     * @param query {@link String}
     * @return {@link String}
     */
    private static String buildQueryURL(String query) {
        String prefix = "https://www.google.com/search?q=site%3Awww.niche.com";
        query = query.toLowerCase();
        query = query.replace(" ", "+");
        return prefix + "+" + query;
    }



    //scrape google results to find the correct niche.com site
//    public static String searchForNicheUrl(String query) throws IOException {
//        //if(query.length() > MAX_QUERY_SIZE) throw new InvalidQueryException("high school name is too long");
//        //Document resultPage = Jsoup.connect(buildQueryURL(query)).get();
//        List<String> urls = getHrefs(resultPage);
//        List<String> matchingUrls = urls.stream().filter(SearchHSUtils::urlMatches).collect(Collectors.toList());
//        return matchingUrls.get(0); // return the top matching result
//    }

    /**
     * Preprocess a niche url so it conforms with fuzzy searching standard
     * @param line {@link String}
     * @return {@link String}
     */
    public static String preprocess(String line) {
        String newLine = line.replace("-", " ");
        newLine = newLine.replaceAll(" +", " ");
        newLine = newLine.trim();
        newLine = newLine.replaceAll("[^A-Za-z0-9 ]", "");
        return newLine.toLowerCase();
    }

    /**
     * Get the state from a processed niche high school url
     * @param processedLine {@link String}
     * @return {@link String}
     */
    private static String getState(String processedLine) {
        String[] words = processedLine.split(" ");
        if(words.length == 0) return null;
        return words[words.length-1];
    }

    /**
     * Check if a state is valid
     * @param state {@link String}
     * @return boolean
     */
    private static boolean isValidState(String state) {
        if(STATES_LIST.contains(state.toUpperCase())) return true;
        return false;
    }


    /**
     * Search for the correct niche.com url to scrape from, requiring that the state matches exactly
     * @param query {@link String} - can be anything, but optimally should be name city state
     * @return {@link String} - return the best matched url
     * @throws IOException
     */
    // scraping google results is effective, but dangerous (we don't want to get blacklisted by google)
    // so, we will search through niche's sitemap.xml instead (preprocessed to only contain high school pages)
    public static String searchForNicheUrl(String query) throws IOException {
        return searchForNicheUrl(query, true);
    }


    /**
     * Search for the correct niche.com url to scrape from
     * @param query {@link String}
     * @param requireStateMatch boolean - whether or not the state must match exactly
     * @return
     * @throws IOException
     */
    public static String searchForNicheUrl(String query, boolean requireStateMatch) throws IOException {
//        query = parseQuery(query);
        String processedQuery = preprocess(query);
        String state = getState(processedQuery);
        if(state == null) state = "";
        boolean validState = isValidState(state);

        File all_hs = TestingDataUtils.findFile(ALL_HIGH_SCHOOLS_FILE);

        FuzzyScore fuzzyScore = new FuzzyScore(Locale.US);

        Integer maxScore = Integer.MIN_VALUE;
        String bestMatch = null;
        if(all_hs != null) {
            System.out.println("found hs file");
            List<String> lines = TestingDataUtils.readFile(all_hs);
            for(String line : lines) {
                String processedLine = preprocess(line);
                if(processedLine.equals(processedQuery)) {
                    return line;
                }
                String theirState = getState(processedLine);
                if(requireStateMatch && validState && !(state.equals(theirState))) continue;

                Integer score = fuzzyScore.fuzzyScore(processedLine, processedQuery);
//                System.out.println("pl = " + processedLine + ", pq = " + processedQuery + ", score = " + score);
                if(score > maxScore) {
                    maxScore = score;
                    bestMatch = line;
                }
            }
        }
//        System.out.println("bestMatch = " + bestMatch);
        return bestMatch;
    }

    /**
     * Parse a query
     * @deprecated
     * @param query {@link String}
     * @return {@link String}
     */
    private static String parseQuery(String query) {
        query = query.toLowerCase().trim();
//        if(query.endsWith("high school")) {
//            query = query.substring(0, query.lastIndexOf("high school"));
//        }
//        else if(query.endsWith("hs")) {
//            query = query.substring(0, query.lastIndexOf("hs"));
//        }
        query = query.replace(" ", "-");
        return query;
    }

}
