package pl.mczerwi.flaresobserver.flares;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Fragment;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import pl.mczerwi.flaresobserver.R;
import pl.mczerwi.flarespredict.IridiumFlare;
import pl.mczerwi.flarespredict.IridiumFlaresPredictor;
import pl.mczerwi.flarespredict.IridiumFlaresPredictorFactory;
import pl.mczerwi.flarespredict.IridiumFlaresPredictorResult;

/**
 * Created by marcin on 2015-05-10.
 */
public class FlaresFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    public static final String TAG = "flaresFragment";

    private static final String FLARES = "flares";

    private AbsListView mFlareListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<IridiumFlare> mFlaresList;
    private ProgressBar mProgressView;

    public static FlaresFragment newInstance() {
        FlaresFragment instance = new FlaresFragment();
        Bundle args = new Bundle();
        instance.setArguments(args);

        return instance;
    }

    public FlaresFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.flare_list, container, false);

        mFlareListView = (AbsListView) view.findViewById(R.id.flares_list_view);
        mFlareListView.setEmptyView(view.findViewById(android.R.id.empty));

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.flares_swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mProgressView = (ProgressBar) view.findViewById(android.R.id.progress);

        if(savedInstanceState != null) {
            ArrayList<ParcelableIridiumFlare> parcelableFlares = savedInstanceState.getParcelableArrayList(FLARES);
            restoreFlareList(parcelableFlares);
        } else {
            mFlaresList = null;
        }

        setHasOptionsMenu(true);

        return view;
    }

    private void restoreFlareList(ArrayList<ParcelableIridiumFlare> parcelableFlares) {
        if(parcelableFlares == null) {
            return;
        }

        mFlaresList = new ArrayList<>();
        for(ParcelableIridiumFlare parcelableIridiumFlare: parcelableFlares) {
            mFlaresList.add(parcelableIridiumFlare.getIridiumFlare());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        showFlaresList(false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        ArrayList<ParcelableIridiumFlare> parcelableFlares = new ArrayList<>();
        for (IridiumFlare flare: mFlaresList) {
            parcelableFlares.add(new ParcelableIridiumFlare(flare));
        }
        outState.putParcelableArrayList(FLARES, parcelableFlares);
        super.onSaveInstanceState(outState);

    }


    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showFlaresList(true);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 500);
    }

    /**
     * Shows the progress UI and hides the list.
     */
    public void showProgress(final boolean show) {

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mSwipeRefreshLayout.setVisibility(show ? View.GONE : View.VISIBLE);
        mFlareListView.getEmptyView().setVisibility(show ? View.GONE : View.VISIBLE);
        mFlareListView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mFlareListView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
    private void showFlaresList(boolean forceRefresh) {

        if(!forceRefresh && mFlaresList != null) {
            mFlareListView.setAdapter(new FlaresAdapter(getActivity(), android.R.layout.simple_list_item_1, mFlaresList));
            return;
        }
        showProgress(true);

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

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
    }


    private class ShowFlaresPredictionsTask extends AsyncTask<Location, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Location... params) {
            Location location = params[0];
            IridiumFlaresPredictor predictor = IridiumFlaresPredictorFactory.getInstance();
            IridiumFlaresPredictorResult result = predictor.predict(location.getLatitude(), location.getLongitude());
            mFlaresList = result.getFlares();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            showProgress(false);

            mFlareListView.setAdapter(new FlaresAdapter(getActivity(), android.R.layout.simple_list_item_1, mFlaresList));
        }
    }
}
