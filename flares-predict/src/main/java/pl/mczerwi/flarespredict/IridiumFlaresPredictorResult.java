package pl.mczerwi.flarespredict;

import java.util.List;

/**
 * Created by marcin on 2015-04-09.
 */
public interface IridiumFlaresPredictorResult {

    public List<IridiumFlare> getFlares();
    public double getLatitude();
    public double getLongitude();
    public double getAltitude();
}
