package pl.mczerwi.flaresobserver;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import pl.mczerwi.flaresobserver.flares.FlaresFragment;
import pl.mczerwi.flaresobserver.model.ParcelableIridiumFlare;
import pl.mczerwi.flaresobserver.skypointer.SkyPointerActivity;
import pl.mczerwi.flarespredict.IridiumFlare;

public class MainActivity extends AppCompatActivity implements FlaresFragment.OnIridiumFlareSelectedListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showFlaresFragment();
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

    @Override
    public void onIridiumFlareSelected(IridiumFlare flare) {
        Intent intent = new Intent(MainActivity.this, SkyPointerActivity.class);
        intent.putExtra(getString(R.string.EXTRA_FLARE), new ParcelableIridiumFlare(flare));
        startActivity(intent);
    }

    private void showFlaresFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = FragmentFactory.getInstance().getFlaresFragment(getFragmentManager());
        fragmentManager.beginTransaction()
                .replace(R.id.activity_main_container, fragment, FlaresFragment.TAG)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
