package pl.mczerwi.flaresobserver;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import pl.mczerwi.flaresobserver.model.ParcelableIridiumFlare;
import pl.mczerwi.flarespredict.IridiumFlare;

/**
 * Created by marcin on 2015-05-10.
 */
public class FlaresFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener{

    public static final String TAG = "flaresFragment";

    public interface OnIridiumFlareSelectedListener {
        void onIridiumFlareSelected(IridiumFlare flare);
    }

    private static final String FLARES = "flares";

    private AbsListView mFlareListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<IridiumFlare> mFlaresList;
    private ProgressBar mProgressView;
    private OnIridiumFlareSelectedListener mOnIridiumFlareSelectedListener;

    public static FlaresFragment newInstance() {
        FlaresFragment instance = new FlaresFragment();
        Bundle args = new Bundle();
        instance.setArguments(args);

        return instance;
    }

    public FlaresFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mOnIridiumFlareSelectedListener = (OnIridiumFlareSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnIridiumFlareSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flares_list, container, false);

        mFlareListView = (AbsListView) view.findViewById(R.id.flares_list_view);
        mFlareListView.setEmptyView(view.findViewById(android.R.id.empty));
        mFlareListView.setOnItemClickListener(this);

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mOnIridiumFlareSelectedListener.onIridiumFlareSelected(mFlaresList.get(position));
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

        FlarePredictionTask task = new FlarePredictionTask(getActivity()) {
            @Override
            protected void onPostTaskExecute(List<IridiumFlare> flares) {
                showProgress(false);
                mFlaresList = flares;
                mFlareListView.setAdapter(new FlaresAdapter(getActivity(), android.R.layout.simple_list_item_1, mFlaresList));
            }
        };
        task.execute();
    }

}
