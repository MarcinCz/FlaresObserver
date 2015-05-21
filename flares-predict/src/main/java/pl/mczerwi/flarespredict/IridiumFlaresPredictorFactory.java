package pl.mczerwi.flarespredict;

import pl.mczerwi.flarespredict.altitude.GoogleAltitudeProvider;
import pl.mczerwi.flarespredict.heavensabove.HeavensAbovePredictor;
import pl.mczerwi.flarespredict.heavensabove.HeavensAboveScraperImpl;

/**
 * Created by marcin on 2015-04-11.
 */
public class IridiumFlaresPredictorFactory {

    public static IridiumFlaresPredictor getInstance() {
        HeavensAbovePredictor heavensAbovePredictor = new HeavensAbovePredictor(new HeavensAboveScraperImpl(), new GoogleAltitudeProvider());
//        return new HeavensAbovePredictor(new HeavensAboveScraperImpl(), new GoogleAltitudeProvider());
        return new IridiumFlaresPredictorStub();
    }
}
