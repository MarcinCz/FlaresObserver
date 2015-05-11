package pl.mczerwi.flaresobserver.skypointer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.mczerwi.flaresobserver.R;
import pl.mczerwi.flaresobserver.flares.ParcelableIridiumFlare;
import pl.mczerwi.flarespredict.IridiumFlare;

/**
 * Created by marcin on 2015-05-11.
 */
public class SkyPointerFragment extends Fragment {

    public final static String TAG = "SkyPointerFragment";

    private final static String FLARE = "flare";

    private TextView mTextView;
    private IridiumFlare mIridiumFlare;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_skypointer, container, false);

        mIridiumFlare = ((ParcelableIridiumFlare) getArguments().getParcelable(FLARE)).getIridiumFlare();
        mTextView = (TextView) view.findViewById(R.id.skypointertext);
        mTextView.setText(mIridiumFlare.getDate().toString());
        return view;
    }
}
