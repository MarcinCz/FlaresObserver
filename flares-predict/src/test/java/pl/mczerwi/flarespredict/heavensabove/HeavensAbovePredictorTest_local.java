package pl.mczerwi.flarespredict.heavensabove;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import pl.mczerwi.flarespredict.IridiumFlare;
import pl.mczerwi.flarespredict.altitude.AltitudeProvider;

import static org.junit.Assert.assertEquals;

/**
 * Created by marcin on 2015-04-11.
 */
public class HeavensAbovePredictorTest_local extends HeavensAbovePredictorTestBase {

    @Override
    protected void assertFlaresAreValid(List<IridiumFlare> flares) throws ParseException {
        assertEquals(29, flares.size());
        IridiumFlare firstFlare = flares.get(0);
        assertEquals(-3.6, firstFlare.getBrightness(), 1);
        assertEquals(35, firstFlare.getAltitude());
        assertEquals(58, firstFlare.getAzimuth());
        assertEquals(2016, firstFlare.getDate().getYear());
        assertEquals(4, firstFlare.getDate().getMonthOfYear());
        assertEquals(11, firstFlare.getDate().getDayOfMonth());
        assertEquals(21, firstFlare.getDate().getHourOfDay());
        assertEquals(58, firstFlare.getDate().getMinuteOfHour());
        assertEquals(54, firstFlare.getDate().getSecondOfMinute());

    }

    @Override
    public HeavensAboveScraper getScraper() {
        return new HeavensAboveScraper() {
            @Override
            public Document getPage(Map<String, String> params) {
                return Jsoup.parse(getWebPage());
            }
        };
    }

    @Override
    protected AltitudeProvider getAltitudeProvider() {
        return new AltitudeProvider() {
            @Override
            public double getAltitude(double latitude, double longitude) {
                return 0;
            }
        };
    }

    private String getWebPage() {
        try(InputStream is = HeavensAbovePredictorTest_local.class.getClassLoader().getResourceAsStream("HeavensAboveFlares.html")) {
            BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String str;
            StringBuilder sb = new StringBuilder(8192);
            while ((str = r.readLine()) != null) {
                sb.append(str);
            }
            return sb.toString();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

    }

}
