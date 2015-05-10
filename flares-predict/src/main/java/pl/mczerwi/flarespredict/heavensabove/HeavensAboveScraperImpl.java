package pl.mczerwi.flarespredict.heavensabove;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Map;

/**
 * Created by marcin on 2015-04-11.
 */
public class HeavensAboveScraperImpl implements HeavensAboveScraper {
    @Override
    public Document getPage(Map<String, String> params) {
        try {
            return Jsoup.connect(HeavensAboveConstants.PAGE_URL)
                    .data(params)
                    .get();
        } catch (Throwable e) {
            throw new IllegalStateException("Error while getting data from heavens-above.", e);
        }
    }
}
