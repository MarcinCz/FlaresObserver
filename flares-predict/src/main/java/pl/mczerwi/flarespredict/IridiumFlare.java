package pl.mczerwi.flarespredict;

import org.joda.time.DateTime;

/**
 * Created by marcin on 2015-04-09.
 */
public interface IridiumFlare {

    double getBrightness();
    int getAltitude();
    int getAzimuth();
    DateTime getDate();

    class IridiumFlareBuilder {

        private double brightness;
        private int altitude;
        private int azimuth;
        private DateTime date;

        public IridiumFlareBuilder brightness(double brightness) {
            this.brightness = brightness;
            return this;
        }

        public IridiumFlareBuilder altitude(int altitude) {
            this.altitude = altitude;
            return this;
        }

        public IridiumFlareBuilder azimuth(int azimuth) {
            this.azimuth = azimuth;
            return this;
        }

        public IridiumFlareBuilder date(DateTime date) {
            this.date = date;
            return this;
        }

        public IridiumFlare build() {
            return new IridiumFlare() {
                @Override
                public double getBrightness() {
                    return brightness;
                }

                @Override
                public int getAltitude() {
                    return altitude;
                }

                @Override
                public int getAzimuth() {
                    return azimuth;
                }

                @Override
                public DateTime getDate() {
                    return date;
                }
            };
        }
    }
}
