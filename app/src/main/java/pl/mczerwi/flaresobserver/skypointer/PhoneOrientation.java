package pl.mczerwi.flaresobserver.skypointer;

/**
 * Created by marcin on 2015-05-12.
 */
class PhoneOrientation {

    private final double altitude;
    private final double azimuth;

    public PhoneOrientation(float[] orientation) {
        this.altitude = getAdjustedAltitude(orientation);
        this.azimuth = getAdjustedAzimuth(orientation);
    }

    private double getAdjustedAzimuth(float[] orientation) {
        double azimuth = Math.toDegrees(orientation[0]);
        if(azimuth < 0) {
            azimuth += 360f;
        }
        return azimuth;
    }

    private double getAdjustedAltitude(float[] orientation) {
        double altitude = Math.toDegrees(orientation[1]);
        altitude *= -1;
        return altitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public double getAzimuth() {
        return azimuth;
    }

}
