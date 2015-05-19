package pl.mczerwi.flaresobserver.skypointer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import pl.mczerwi.flaresobserver.R;
import pl.mczerwi.flaresobserver.model.ParcelableIridiumFlare;
import pl.mczerwi.flarespredict.IridiumFlare;

public class SkyPointerActivity extends AppCompatActivity {

    private IridiumFlare mIridiumFlare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sky_pointer);
        ParcelableIridiumFlare parcelableIridiumFlare = getIntent().getExtras().getParcelable(getString(R.string.EXTRA_FLARE));
        mIridiumFlare = parcelableIridiumFlare.getIridiumFlare();

        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = SkyPointerFragment.newInstance(mIridiumFlare);
        fragmentManager.beginTransaction()
                .replace(R.id.activity_sky_pointer_container, fragment, SkyPointerFragment.TAG)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sky_pointer, menu);
        return true;
    }

}
