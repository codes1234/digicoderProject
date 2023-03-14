package digi.coders.Qaione_Education.Adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import digi.coders.Qaione_Education.Fragments.MyCoursesFragment;
import digi.coders.Qaione_Education.Fragments.MyEbookFragment;

public class MyCoursesTabAdapter extends FragmentStatePagerAdapter {


    private Context myContext;
    int totalTabs;

    public MyCoursesTabAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MyCoursesFragment my=new MyCoursesFragment();
                return my;
            case 1:
                MyEbookFragment myEbookFragment=new MyEbookFragment();
                return myEbookFragment;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String tabtitle="";
        if (position == 0) {
            tabtitle = "Courses";
        } else if (position == 1)
        {
            tabtitle="Ebook";
        }
        return tabtitle;

    }
}
