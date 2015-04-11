package pl.mczerwi.flarespredict;

import java.util.Date;

/**
 * Created by marcin on 2015-04-09.
 */
public interface IridiumFlare {

    public double getBrightness();
    public int getAltitude();
    public int getAzimuth();
    public Date getDate();
}
