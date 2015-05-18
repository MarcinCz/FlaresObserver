package pl.mczerwi.flaresobserver;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import java.util.List;

import pl.mczerwi.flarespredict.IridiumFlare;
import pl.mczerwi.flarespredict.IridiumFlaresPredictor;
import pl.mczerwi.flarespredict.IridiumFlaresPredictorFactory;
import pl.mczerwi.flarespredict.IridiumFlaresPredictorResult;

/**
 * Created by marcin on 2015-05-15.
 */
public abstract class FlarePredictionTask extends AsyncTask<Location, Void, List<IridiumFlare>> {

    private final Context context;

    protected FlarePredictionTask(Context context) {
        this.context = context;
    }

    public void execute() {
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(final Location location) {
                execute(location);
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        Criteria criteria = new Criteria();
        criteria.setAltitudeRequired(true);
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        locationManager.requestSingleUpdate(criteria, locationListener, null);
    }

    @Override
    protected List<IridiumFlare> doInBackground(Location... params) {
        Location location = params[0];
        IridiumFlaresPredictor predictor = IridiumFlaresPredictorFactory.getInstance();
        IridiumFlaresPredictorResult result = predictor.predict(location.getLatitude(), location.getLongitude());
        return result.getFlares();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected abstract void onPostTaskExecute(List<IridiumFlare> flares);

    @Override
    protected void onPostExecute(List<IridiumFlare> flares) {
        onPostTaskExecute(flares);
    }
}
