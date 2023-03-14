package digi.coders.Qaione_Education.Adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import digi.coders.Qaione_Education.Fragments.AllCoursesFragment;
import digi.coders.Qaione_Education.Fragments.MyCoursesFragment;

public class CourseTabAdapter extends FragmentStatePagerAdapter {

    private Context myContext;
    int totalTabs;

    public CourseTabAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MyCoursesFragment myCoursesFragment=new MyCoursesFragment();
                return myCoursesFragment;
            case 1:
                AllCoursesFragment allCoursesFragment=new AllCoursesFragment();
                return allCoursesFragment;
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
            tabtitle = "My Courses";
        } else if (position == 1)
        {
            tabtitle="All Courses";
        }
        return tabtitle;

    }
}
