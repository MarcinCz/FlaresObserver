package pl.mczerwi.flaresobserver.skypointer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import pl.mczerwi.flaresobserver.R;
import pl.mczerwi.flarespredict.IridiumFlare;

class PhoneOrientationChangeHandler implements SensorEventListener {

    private final TextView mPhoneAltitudeTextView;
    private final TextView mPhoneAzimuthTextView;
    private final IridiumFlare mIridiumFlare;
    private final ImageView mArrowCircle;
    private int mCurrentCircleArrow;
    private PhoneOrientation mLastPhoneOrientation;
    private float[] mGravity;
    private float[] mGeomagnetic;
    private CircularFifoQueue<PhoneOrientation> mLastPhoneOrientations = new CircularFifoQueue<>(10);

    public PhoneOrientationChangeHandler(View view, IridiumFlare flare) {
        mPhoneAltitudeTextView = (TextView) view.findViewById(R.id.skypointer_altitude_phone);
        mPhoneAzimuthTextView = (TextView) view.findViewById(R.id.skypointer_azimuth_phone);
        mArrowCircle = (ImageView) view.findViewById(R.id.sky_pointer_arrow_circle);
        mIridiumFlare = flare;
        mCurrentCircleArrow = R.drawable.arrow_circle_red;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, null, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                float remapMatrix[] = new float[9];
                SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_X, SensorManager.AXIS_Z, remapMatrix);
                SensorManager.getOrientation(remapMatrix, orientation);
                onPhoneOrientationChanged(new PhoneOrientation(orientation));
            }
        }
    }

    private void onPhoneOrientationChanged(PhoneOrientation phoneOrientation) {

        phoneOrientation = getSmoothedOrientation(phoneOrientation);

        double altitudeDifference = mIridiumFlare.getAltitude() - phoneOrientation.getAltitude();
        double azimuthDifference = SphereHelper.getAzimuthDifference(mIridiumFlare.getAzimuth(), phoneOrientation.getAzimuth());
        double distanceToFlare = SphereHelper.getGreatCircleDistance(mIridiumFlare.getAltitude(), phoneOrientation.getAltitude(), mIridiumFlare.getAzimuth(), phoneOrientation.getAzimuth());

        adjustArrowCircleGraphic(distanceToFlare);
        mPhoneAltitudeTextView.setText(String.valueOf(Math.round(phoneOrientation.getAltitude())) + "°");
        long azimuth = Math.round(phoneOrientation.getAzimuth());
        if(azimuth < 0) {
            azimuth += 360;
        }
        mPhoneAzimuthTextView.setText(String.valueOf(azimuth) + "°");

        double angle = Math.toDegrees(Math.atan2(azimuthDifference, altitudeDifference));
        mArrowCircle.setRotation((float) angle);

    }

    void setLastPhoneOrientations(CircularFifoQueue<PhoneOrientation> lastPhoneOrientations) {
        this.mLastPhoneOrientations = lastPhoneOrientations;
    }

    PhoneOrientation getSmoothedOrientation(PhoneOrientation orientation) {
        mLastPhoneOrientations.add(orientation);
        double smoothAzimuth = 0;
        double smoothAltitude = 0;

        double singleAzimuth = orientation.getAzimuth();
        for(PhoneOrientation lastPhoneOrientation: mLastPhoneOrientations) {
            if(lastPhoneOrientation == null) {
                break;
            }
            smoothAltitude += lastPhoneOrientation.getAltitude();
            if(Math.abs(Math.round(lastPhoneOrientation.getAzimuth() - singleAzimuth)) < 20) {
                smoothAzimuth += lastPhoneOrientation.getAzimuth();
            }
        }

        smoothAltitude /= mLastPhoneOrientations.size();
        smoothAzimuth /= mLastPhoneOrientations.size();
        return new PhoneOrientation(smoothAzimuth, smoothAltitude);
    }

    private boolean isChangeBigEnough(PhoneOrientation phoneOrientation) {
        if(mLastPhoneOrientation == null
                || Math.abs(phoneOrientation.getAltitude() - mLastPhoneOrientation.getAltitude()) > 2
                || Math.abs(phoneOrientation.getAzimuth() - mLastPhoneOrientation.getAzimuth()) > 5) {
            mLastPhoneOrientation = phoneOrientation;
            return true;
        } else {
            return false;
        }
    }

    private void adjustArrowCircleGraphic(double distanceToFlare) {
        if(distanceToFlare > SphereHelper.SPHERE_BIG_DISTANCE) {
            changeArrowCircleIfNecessary(R.drawable.arrow_circle_red);
        } else if(distanceToFlare > SphereHelper.SPHERE_MEDIUM_DISTANCE ) {
            changeArrowCircleIfNecessary(R.drawable.arrow_circle_orange);
        } else if(distanceToFlare > SphereHelper.SPHERE_SMALL_DISTANCE) {
            changeArrowCircleIfNecessary(R.drawable.arrow_circle_yellow);
        } else {
            changeArrowCircleIfNecessary(R.drawable.arrow_circle_green);
        }
    }

    private void changeArrowCircleIfNecessary(int arrowCircle) {
        if(mCurrentCircleArrow != arrowCircle) {
            mArrowCircle.setImageResource(arrowCircle);
            mCurrentCircleArrow = arrowCircle;
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
