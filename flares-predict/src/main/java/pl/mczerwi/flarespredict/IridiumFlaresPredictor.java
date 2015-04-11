package pl.mczerwi.flarespredict;

/**
 * Created by marcin on 2015-04-09.
 */
public interface IridiumFlaresPredictor {
    /**
     * Predict based on latitude and longitude. Altitude is automatically checked for given location.
     */
    public IridiumFlares predict(double latitude, double longitude);
    public IridiumFlares predict(double latitude, double longitude, double altitude);
}
