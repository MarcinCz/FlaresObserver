package pl.mczerwi.flarespredict.heavensabove;

import org.jsoup.nodes.Document;

import java.util.Map;

/**
 * Created by marcin on 2015-04-11.
 */
interface HeavensAboveScraper {
    Document getPage(Map<String, String> params);
}
