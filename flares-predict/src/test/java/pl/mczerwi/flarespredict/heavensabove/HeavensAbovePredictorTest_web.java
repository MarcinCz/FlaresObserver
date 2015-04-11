package pl.mczerwi.flarespredict.heavensabove;

import java.text.ParseException;
import java.util.List;

import pl.mczerwi.flarespredict.IridiumFlare;
import pl.mczerwi.flarespredict.IridiumFlareTestUtil;
import pl.mczerwi.flarespredict.altitude.AltitudeProvider;
import pl.mczerwi.flarespredict.altitude.GoogleAltitudeProvider;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by marcin on 2015-04-11.
 */
public class HeavensAbovePredictorTest_web extends HeavensAbovePredictorTestBase {

    @Override
    protected void assertFlaresAreValid(List<IridiumFlare> flares) throws ParseException {
        assertTrue(flares.size() > 0);
        IridiumFlare firstFlare = flares.get(0);
        assertNotNull(firstFlare.getBrightness());
        assertNotNull(firstFlare.getAltitude());
        assertNotNull(firstFlare.getAzimuth());
        assertNotNull(firstFlare.getDate());
        IridiumFlareTestUtil.printIridiumFlare(firstFlare);
    }

    @Override
    public HeavensAboveScraper getScraper() {
        return new HeavensAboveScraperImpl();
    }

    @Override
    protected AltitudeProvider getAltitudeProvider() {
        return new GoogleAltitudeProvider();
    }
}
