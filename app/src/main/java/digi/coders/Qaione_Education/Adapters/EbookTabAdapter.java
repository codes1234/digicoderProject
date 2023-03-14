package digi.coders.Qaione_Education.Adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import digi.coders.Qaione_Education.Fragments.AudioBookFragment;
import digi.coders.Qaione_Education.Fragments.PDFBookFragment;

public class EbookTabAdapter extends FragmentStatePagerAdapter {


    private Context myContext;
    int totalTabs;

    public EbookTabAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                AudioBookFragment audioBookFragment=new AudioBookFragment();
                return audioBookFragment;
            case 1:
                PDFBookFragment pdfBookFragment=new PDFBookFragment();
                return pdfBookFragment;
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
            tabtitle = "Audio Book";
        } else if (position == 1)
        {
            tabtitle="PDF Book";
        }
        return tabtitle;

    }
}
