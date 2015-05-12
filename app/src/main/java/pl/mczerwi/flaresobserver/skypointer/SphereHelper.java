package pl.mczerwi.flaresobserver.skypointer;

/**
 * Created by marcin on 2015-05-12.
 */
class SphereHelper {

    public final static double SPHERE_BIG_DISTANCE = Math.PI / 2;
    public final static double SPHERE_MEDIUM_DISTANCE = Math.PI / 5;
    public final static double SPHERE_SMALL_DISTANCE = Math.PI / 15;

    /**
     * Calculates great-circle distance based on angles in degrees.
     * Return
     */
    public static double getGreatCircleDistance(double altitude1, double altitude2, double azimuth1, double azimuth2) {
        altitude1 = Math.toRadians(altitude1);
        altitude2 = Math.toRadians(altitude2);
        azimuth1 = Math.toRadians(azimuth1);
        azimuth2 = Math.toRadians(azimuth2);
        return Math.acos(
                (Math.sin(altitude1) * Math.sin(altitude2))
                + (Math.cos(altitude1) * Math.cos(altitude2) * Math.cos(Math.abs(getAzimuthDifference(azimuth1, azimuth2))))
        );
    }

    public static double getAzimuthDifference(double azimuth1, double azimuth2) {
        double azimuthDifference = azimuth1 - azimuth2;
        if(azimuthDifference > 180) {
            azimuthDifference -= 360;
        } else if (azimuthDifference < - 180) {
            azimuthDifference += 360;
        }
        return azimuthDifference;
    }
}
