package pl.mczerwi.flaresobserver.skypointer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import pl.mczerwi.flaresobserver.FragmentFactory;
import pl.mczerwi.flaresobserver.R;
import pl.mczerwi.flaresobserver.model.ParcelableIridiumFlare;

public class SkyPointerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sky_pointer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sky_pointer, menu);

        ParcelableIridiumFlare parcelableIridiumFlare = getIntent().getExtras().getParcelable(getString(R.string.EXTRA_FLARE));

        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = FragmentFactory.getInstance().getSkyPointerFragment(parcelableIridiumFlare.getIridiumFlare());
        fragmentManager.beginTransaction()
                .replace(R.id.activity_sky_pointer_container, fragment, SkyPointerFragment.TAG)
                .commit();
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
}
