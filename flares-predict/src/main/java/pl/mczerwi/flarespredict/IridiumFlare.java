package pl.mczerwi.flarespredict;

import org.joda.time.DateTime;

/**
 * Created by marcin on 2015-04-09.
 */
public interface IridiumFlare {

    public double getBrightness();
    public int getAltitude();
    public int getAzimuth();
    public DateTime getDate();
}
