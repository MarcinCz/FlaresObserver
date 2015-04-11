package pl.mczerwi.flarespredict.altitude;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GoogleAltitudeProviderTest {

    @Test
    public void testGetAltitude() throws Exception {
        AltitudeProvider provider = new GoogleAltitudeProvider();
        double altitude = provider.getAltitude(51.0, 21.0);
        assertEquals(300, altitude, 10);
    }
}