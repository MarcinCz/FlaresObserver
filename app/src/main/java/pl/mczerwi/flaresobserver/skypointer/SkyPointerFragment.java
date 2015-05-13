package pl.mczerwi.flaresobserver.skypointer;

import android.app.Fragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.mczerwi.flaresobserver.R;
import pl.mczerwi.flaresobserver.model.ParcelableIridiumFlare;
import pl.mczerwi.flarespredict.IridiumFlare;

/**
 * Created by marcin on 2015-05-11.
 */
public class SkyPointerFragment extends Fragment {

    public final static String TAG = "SkyPointerFragment";

    private final static String FLARE = "flare";

    private IridiumFlare mIridiumFlare;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private PhoneOrientationChangeHandler mOrientationChangeHandler;

    public static SkyPointerFragment newInstance(IridiumFlare flare) {
        SkyPointerFragment instance = new SkyPointerFragment();
        Bundle args = new Bundle();
        args.putParcelable(FLARE, new ParcelableIridiumFlare(flare));
        instance.setArguments(args);

        return instance;
    }

    public SkyPointerFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(mOrientationChangeHandler, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(mOrientationChangeHandler, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mOrientationChangeHandler);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_skypointer, container, false);

        mIridiumFlare = ((ParcelableIridiumFlare) getArguments().getParcelable(FLARE)).getIridiumFlare();
        mOrientationChangeHandler = new PhoneOrientationChangeHandler(view, mIridiumFlare);
        return view;
    }




}
