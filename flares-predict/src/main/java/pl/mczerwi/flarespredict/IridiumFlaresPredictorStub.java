package pl.mczerwi.flarespredict;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcin on 2015-05-10.
 */
public class IridiumFlaresPredictorStub implements IridiumFlaresPredictor {
    @Override
    public IridiumFlaresPredictorResult predict(double latitude, double longitude) {
        return predict(latitude, longitude, 300);
    }

    @Override
    public IridiumFlaresPredictorResult predict(double latitude, double longitude, double altitude) {
        final List<IridiumFlare> flares = new ArrayList<>();

        for(int i = 0; i < 10; i++) {
            IridiumFlare.IridiumFlareBuilder builder = new IridiumFlare.IridiumFlareBuilder();
            builder.altitude(i*10);
            builder.azimuth((10 - i) * 20);
            builder.brightness(i);
            builder.date(DateTime.now());
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
