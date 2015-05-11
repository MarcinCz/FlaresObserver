package pl.mczerwi.flaresobserver;

import android.app.FragmentManager;

import pl.mczerwi.flaresobserver.flares.FlaresFragment;
import pl.mczerwi.flaresobserver.skypointer.SkyPointerFragment;
import pl.mczerwi.flarespredict.IridiumFlare;

/**
 * Created by marcin on 2015-05-11.
 */
class FragmentFactory
{

    private FlaresFragment mFlaresFragment;

    private final static FragmentFactory instance = new FragmentFactory();

    public static FragmentFactory getInstance() {
        return instance;
    }

    private FragmentFactory(){};

    public FlaresFragment getFlaresFragment(FragmentManager fragmentManager) {
        mFlaresFragment = (FlaresFragment) fragmentManager.findFragmentByTag(FlaresFragment.TAG);
            if(mFlaresFragment == null) {
                mFlaresFragment = FlaresFragment.newInstance();
            }

        return mFlaresFragment;
    }

    public SkyPointerFragment getSkyPointerFragment(IridiumFlare flare) {
        return SkyPointerFragment.newInstance(flare);
    }
}
