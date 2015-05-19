package pl.mczerwi.flaresobserver.skypointer;

import android.app.Fragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.DateTime;

import pl.mczerwi.flaresobserver.R;
import pl.mczerwi.flaresobserver.model.ParcelableIridiumFlare;
import pl.mczerwi.flarespredict.IridiumFlare;

public class SkyPointerFragment extends Fragment {

    public final static String TAG = "SkyPointerFragment";

    private final static String FLARE = "flare";

    private IridiumFlare mIridiumFlare;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private PhoneOrientationChangeHandler mOrientationChangeHandler;
    private TextView mFlareAltitudeTextView;
    private TextView mFlareAzimuthTextView;
    private TextView mFlareBrightnessTextView;
    private TextView mFlareTimeLeftTextView;


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
        mSensorManager.registerListener(mOrientationChangeHandler, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(mOrientationChangeHandler, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
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

        mFlareAltitudeTextView = (TextView) view.findViewById(R.id.skypointer_flare_altitude);
        mFlareAzimuthTextView = (TextView) view.findViewById(R.id.skypointer_flare_azimuth);
        mFlareBrightnessTextView = (TextView) view.findViewById(R.id.skypointer_flare_brightness);
        mFlareTimeLeftTextView = (TextView) view.findViewById(R.id.skypointer_time_left);

        mFlareBrightnessTextView.setText(String.valueOf(mIridiumFlare.getBrightness()));
        mFlareAzimuthTextView.setText(String.valueOf(mIridiumFlare.getAzimuth()) + "°");
        mFlareAltitudeTextView.setText(String.valueOf(mIridiumFlare.getAltitude()) + "°");

        startCountDownTimer();
        return view;
    }

    private void startCountDownTimer() {
        new CountDownTimer(mIridiumFlare.getDate().getMillis() - DateTime.now().getMillis(),1000) {

            @Override
            public void onTick(long millis) {
                int seconds = (int) (millis / 1000) % 60 ;
                int minutes = (int) ((millis / (1000*60)) % 60);
                int hours   = (int) ((millis / (1000*60*60)) % 24);
                String text = String.format("%02d:%02d:%02d",hours,minutes,seconds);
                mFlareTimeLeftTextView.setText(text);
            }

            @Override
            public void onFinish() {
                mFlareTimeLeftTextView.setText("-");
            }
        }.start();

    }




}
