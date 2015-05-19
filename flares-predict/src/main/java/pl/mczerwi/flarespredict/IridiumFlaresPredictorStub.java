package pl.mczerwi.flarespredict;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

class IridiumFlaresPredictorStub implements IridiumFlaresPredictor {
    @Override
    public IridiumFlaresPredictorResult predict(double latitude, double longitude) {
        return predict(latitude, longitude, 300);
    }

    @Override
    public IridiumFlaresPredictorResult predict(double latitude, double longitude, double altitude) {
        final List<IridiumFlare> flares = new ArrayList<>();

        DateTime now = DateTime.now();
        for(int i = 0; i < 10; i++) {
            IridiumFlare.IridiumFlareBuilder builder = new IridiumFlare.IridiumFlareBuilder();
            builder.altitude(i*10);
            builder.azimuth((10 - i) * 20);
            builder.brightness(i - 8);
            builder.date(now.plusHours(i).plusMinutes(i));
            flares.add(builder.build());
        }
        return new IridiumFlaresPredictorResult() {
            @Override
            public List<IridiumFlare> getFlares() {
                return flares;
            }

            @Override
            public double getLatitude() {
                return 11;
            }

            @Override
            public double getLongitude() {
                return 12;
            }

            @Override
            public double getAltitude() {
                return 300;
            }
        };
    }
}
