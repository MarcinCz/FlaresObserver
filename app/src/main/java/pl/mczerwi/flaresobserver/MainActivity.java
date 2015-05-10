package pl.mczerwi.flaresobserver;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import pl.mczerwi.flaresobserver.flares.FlaresFragment;

public class MainActivity extends AppCompatActivity {

    private FlaresFragment mFlaresFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getFragmentManager();
        mFlaresFragment = (FlaresFragment) fragmentManager.findFragmentByTag(FlaresFragment.TAG);
        if(mFlaresFragment == null) {
            mFlaresFragment = FlaresFragment.newInstance();
        }
        fragmentManager.beginTransaction()
                .replace(R.id.container, mFlaresFragment, FlaresFragment.TAG)
                .commit();
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

}
