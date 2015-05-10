package pl.mczerwi.flarespredict.heavensabove;

import org.junit.Test;

import java.text.ParseException;
import java.util.List;

import pl.mczerwi.flarespredict.IridiumFlare;
import pl.mczerwi.flarespredict.IridiumFlaresPredictorResult;
import pl.mczerwi.flarespredict.altitude.AltitudeProvider;

import static org.junit.Assert.assertEquals;

/**
 * Created by marcin on 2015-04-08.
 */
public abstract class HeavensAbovePredictorTestBase {

    @Test
    public void shouldGetFlares() throws ParseException {
        HeavensAbovePredictor predictor = new HeavensAbovePredictor(getScraper(), getAltitudeProvider());
        IridiumFlaresPredictorResult flares = predictor.predict(51.40274, 21.1471, 0);

        assertEquals(51.40274, flares.getLatitude(), 1);
        assertEquals(21.1471, flares.getLongitude(), 1);
        assertFlaresAreValid(flares.getFlares());
    }

    protected abstract void assertFlaresAreValid(List<IridiumFlare> flares) throws ParseException;

    protected abstract HeavensAboveScraper getScraper();

    protected abstract AltitudeProvider getAltitudeProvider();
}

