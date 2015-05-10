package pl.mczerwi.flarespredict;

/**
 * Created by marcin on 2015-04-11.
 */
public class IridiumFlaresPredictorFactory {

    public static IridiumFlaresPredictor getInstance() {
//        return new HeavensAbovePredictor(new HeavensAboveScraperImpl(), new GoogleAltitudeProvider());
        return new IridiumFlaresPredictorStub();
    }
}
