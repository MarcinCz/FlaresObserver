package pl.mczerwi.flaresobserver;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import pl.mczerwi.flaresobserver.model.ParcelableIridiumFlare;
import pl.mczerwi.flaresobserver.notifications.FlarePredictionReceiver;
import pl.mczerwi.flaresobserver.skypointer.SkyPointerActivity;
import pl.mczerwi.flarespredict.IridiumFlare;

public class MainActivity extends AppCompatActivity implements FlaresFragment.OnIridiumFlareSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FlarePredictionReceiver.enableFlarePredictionNotifications(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.primary_dark_material_dark));
        }
        showFlaresFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
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
        Fragment flaresFragment = fragmentManager.findFragmentByTag(FlaresFragment.TAG);
        if(flaresFragment == null) {
            flaresFragment = FlaresFragment.newInstance();
        }
        fragmentManager.beginTransaction()
                .replace(R.id.activity_main_container, flaresFragment, FlaresFragment.TAG)
                .commit();
    }
}
