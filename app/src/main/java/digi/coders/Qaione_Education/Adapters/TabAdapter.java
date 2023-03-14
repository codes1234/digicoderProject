package digi.coders.Qaione_Education.Adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import digi.coders.Qaione_Education.Fragments.AboutMeFragment;
import digi.coders.Qaione_Education.Fragments.FragmentHome;


public class TabAdapter extends FragmentPagerAdapter {


    private Context myContext;
    int totalTabs;

    public TabAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                AboutMeFragment homeFragment = new AboutMeFragment();
                return homeFragment;
            case 1:
                FragmentHome sportFragment = new FragmentHome();
                return sportFragment;
            case 2:
                FragmentHome wishListFragment = new FragmentHome();
                return wishListFragment;
            case 3:
                FragmentHome wishListFragment1 = new FragmentHome();
                return wishListFragment1;

            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}
