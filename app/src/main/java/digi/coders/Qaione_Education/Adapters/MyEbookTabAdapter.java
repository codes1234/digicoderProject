package digi.coders.Qaione_Education.Adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import digi.coders.Qaione_Education.Fragments.MyAbookFragment;
import digi.coders.Qaione_Education.Fragments.MyEbookFragment;

public class MyEbookTabAdapter extends FragmentStatePagerAdapter {

    private Context myContext;
    int totalTabs;

    public MyEbookTabAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
//        switch (position) {
////            case 0:
////                MyAbookFragment myAbookFragment=new MyAbookFragment();
////                return myAbookFragment;
//            case 1:
                MyEbookFragment myEbookFragment=new MyEbookFragment();
                return myEbookFragment;
//            default:
//                return null;
//        }
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
//        if (position == 0) {
//            tabtitle="Audio Book";
//        } else if (position == 1) {
            tabtitle="PDF Book";
//        }
        return tabtitle;

    }
}
