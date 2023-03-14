package digi.coders.Qaione_Education.Adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import digi.coders.Qaione_Education.Fragments.AttemptedFragment;
import digi.coders.Qaione_Education.Fragments.UnAttemptedFragment;

public class QuizTabAdapter extends FragmentStatePagerAdapter {


    private Context myContext;
    int totalTabs;

    public QuizTabAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                AttemptedFragment attemptedFragment=new AttemptedFragment();
                return attemptedFragment;
            case 1:
                UnAttemptedFragment unAttemptedFragment=new UnAttemptedFragment();
                return unAttemptedFragment;
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
            tabtitle = "Attempted";
        } else if (position == 1)
        {
            tabtitle="Unattempted";
        }
        return tabtitle;

    }
}
