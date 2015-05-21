package pl.mczerwi.flaresobserver.skypointer;

class PhoneOrientation {

    private final double altitude;
    private final double azimuth;

    public PhoneOrientation(float[] orientation) {
        this.altitude = getAdjustedAltitude(orientation);
        this.azimuth = getAdjustedAzimuth(orientation);
    }

    public PhoneOrientation(double azimuth, double altitude)  {
        this.azimuth = azimuth;
        this.altitude = altitude;
    }

    private double getAdjustedAzimuth(float[] orientation) {
        return Math.toDegrees(orientation[0]);
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
