package pl.mczerwi.flarespredict.altitude;

/**
 * Created by marcin on 2015-04-11.
 */
public interface AltitudeProvider {
    public double getAltitude(double latitude, double longitude);
}
