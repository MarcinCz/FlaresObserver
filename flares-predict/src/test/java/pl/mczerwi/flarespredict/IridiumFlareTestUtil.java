package pl.mczerwi.flarespredict;

/**
 * Created by marcin on 2015-04-11.
 */
public class IridiumFlareTestUtil {

    public static void printIridiumFlare(IridiumFlare flare) {
        System.out.println("IridiumFlare:");
        System.out.println("Date:" + flare.getDate());
        System.out.println("Brightness:" + flare.getBrightness());
        System.out.println("Azimuth:" + flare.getAzimuth());
        System.out.println("Altitude:" + flare.getAltitude());
    }
}
