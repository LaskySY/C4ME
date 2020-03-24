package com.c4me.server.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.c4me.server.config.constant.Const.Filenames.ALL_HIGH_SCHOOLS_FILE;
import static com.c4me.server.config.constant.Const.Filenames.TEST_HIGH_SCHOOL_SEARCH_URL;
import static com.c4me.server.config.constant.Const.Ranges.MAX_QUERY_SIZE;
import static com.c4me.server.config.constant.Const.STATES.STATES_LIST;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-22-2020
 */

public class SearchHSUtils {

    //test whether search result url matches niche.com high school url template
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

    // get links from google results
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

    // scraping google results is effective, but dangerous (we don't want to get blacklisted by google)
    // so, we will search through niche's sitemap.xml instead (preprocessed to only contain high school pages)
    /* TODO: this is a bit ad-hoc. I don't know how to do it properly, with actually searching for relevance etc.
        Probably should use Levenshtein metric on substrings or something like that
     */
    public static HashMap<String, Integer> searchForNicheUrl(String query) throws IOException {
        query = parseQuery(query);
        File all_hs = TestingDataUtils.findFile(ALL_HIGH_SCHOOLS_FILE);
        HashMap<String, Integer> matches = new HashMap<>();
        if(all_hs != null) {
            System.out.println("found hs file");
            List<String> lines = TestingDataUtils.readFile(all_hs);
            for(String line : lines) {
                if(line.contains(query)) {
                    System.out.println(line);
                    matches.put(line, line.indexOf(query));
                }
            }

            //if no matches found, we will try to match just the longest word of the query
            if(matches.size() == 0) {
                String[] words = query.split("-");
                if(words.length > 1) {
                    String longest = words[0];
                    int longest_len = words[0].length();
                    for(String w : words) {
                        if(w.length() > longest_len) {
                            longest_len = w.length();
                            longest = w;
                        }
                    }
                    if(!(longest.equals("school") || longest.equals("high") || longest.equals("academy"))) {
                        for (String line : lines) {
                            if (line.contains(longest)) {
                                System.out.println(line);
                                matches.put(line, line.indexOf(longest));
                            }
                        }
                    }
                }
            }

            //if still no matches found, try removing "high school" from the end of the query
            if(matches.size() == 0) {
                String old = query;
                if(query.endsWith("high-school")) {
                    query = query.substring(0, query.lastIndexOf("high-school"));
                }
                else if (query.endsWith("school")) {
                    query = query.substring(0, query.lastIndexOf("school"));
                }
                else if(query.endsWith("hs")) {
                    query = query.substring(0, query.lastIndexOf("hs"));
                }
                if(!old.equals(query)) {
                    for (String line : lines) {
                        if (line.contains(query)) {
                            System.out.println(line);
                            matches.put(line, line.indexOf(query));
                        }
                    }
                }
            }

            // give up. just return null
            if(matches.size() == 0) return null;

            return matches;
        }
        return null;
    }

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
