package pl.mczerwi.flaresobserver;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import pl.mczerwi.flaresobserver.flares.FlaresFragment;
import pl.mczerwi.flaresobserver.skypointer.SkyPointerFragment;
import pl.mczerwi.flarespredict.IridiumFlare;

public class MainActivity extends AppCompatActivity implements FlaresFragment.OnIridiumFlareSelectedListener{

    private enum FragmentEnum {
        FLARES,
        SKY_POINTER
    }

    private FragmentEnum mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
//                if(getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                } else {
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
//                }
            }
        });
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
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = FragmentFactory.getInstance().getSkyPointerFragment(flare);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.container, fragment, SkyPointerFragment.TAG);
            transaction.hide(FragmentFactory.getInstance().getFlaresFragment(fragmentManager));
            transaction.addToBackStack(null);
            transaction.commit();
        mCurrentFragment = FragmentEnum.SKY_POINTER;
    }

    private void showFlaresFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = FragmentFactory.getInstance().getFlaresFragment(getFragmentManager());
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment, FlaresFragment.TAG)
                .commit();
        mCurrentFragment = FragmentEnum.FLARES;
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } else {
            super.onBackPressed();
        }
    }
}
