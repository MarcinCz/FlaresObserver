package pl.mczerwi.flaresobserver;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import pl.mczerwi.flarespredict.IridiumFlare;
import pl.mczerwi.flarespredict.IridiumFlares;
import pl.mczerwi.flarespredict.IridiumFlaresPredictor;
import pl.mczerwi.flarespredict.IridiumFlaresPredictorFactory;


public class MainActivity extends ActionBarActivity {

    private TextView view;
    private TextView viewFlares;
    private ListView flareListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = (TextView) findViewById(R.id.testTextView);
        viewFlares = (TextView) findViewById(R.id.textViewFlares);
        flareListView = (ListView) findViewById(R.id.flares_list_view);

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(final Location location) {
                ShowFlaresPredictionsTask task = new ShowFlaresPredictionsTask();
                task.execute(location);
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        Criteria criteria = new Criteria();
        criteria.setAltitudeRequired(true);
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        locationManager.requestSingleUpdate(criteria, locationListener, null);
        locationManager.getBestProvider(criteria, true);


// Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(locationManager.getBestProvider(criteria, true), 60000, 0, locationListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ShowFlaresPredictionsTask extends AsyncTask<Location, Void, IridiumFlares> {

        @Override
        protected IridiumFlares doInBackground(Location... params) {
            Location location = params[0];
            IridiumFlaresPredictor predictor = IridiumFlaresPredictorFactory.getInstance();
            return predictor.predict(location.getLatitude(), location.getLongitude());
        }

        @Override
        protected void onPostExecute(IridiumFlares iridiumFlares) {
            IridiumFlare flare = iridiumFlares.getFlares().get(0);
            view.setText("alt: " + iridiumFlares.getAltitude() + " lat: " + iridiumFlares.getLatitude() + " lnt: " + iridiumFlares.getLongitude());
            viewFlares.setText("Flare: " + flare.getDate() + " " + flare.getAltitude() + " " + flare.getAzimuth() + " " + flare.getBrightness());
            flareListView.setAdapter(new FlareAdapter(getBaseContext(), iridiumFlares.getFlares()));
        }
    };
}
