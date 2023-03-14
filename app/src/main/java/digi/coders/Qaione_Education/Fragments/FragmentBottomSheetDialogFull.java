package digi.coders.Qaione_Education.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import digi.coders.Qaione_Education.Adapters.TabAdapter;
import digi.coders.Qaione_Education.R;


public class FragmentBottomSheetDialogFull extends BottomSheetDialogFragment {

    private BottomSheetBehavior mBehavior;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        final View view = View.inflate(getContext(), R.layout.fragment_bottom_sheet_dialog_full, null);

        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        mBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
//        FunTabLayout tabLayout = (FunTabLayout) view.findViewById(R.id.tablayout);
//        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
//        viewPager.setAdapter(new TabAdapter(getActivity(),getFragmentManager(),3));
//        PopTabAdapter.Builder builder = new PopTabAdapter.Builder(getActivity()).
//                setViewPager(viewPager).
//                setTabPadding(24, 24, 24, 24).
//               // setTabTextAppearance(R.style.PopTabText).
//                setTabBackgroundResId(R.drawable.rounded_corners_bg).
//                setTabIndicatorColor(Color.GREEN).
//                setIconFetcher(new PopTabAdapter.IconFetcher() {
//                    @Override
//                    public int getIcon(int position) {
//                         return R.drawable.ic_home_black_24dp;
//                    }
//                }).
//                setIconDimension(70).
//                setDefaultIconColor(Color.WHITE).
//                setPopDuration(2000);
//        tabLayout.setUpWithAdapter(builder.build());
       TabLayout tabLayout=(TabLayout)view.findViewById(R.id.tablayout);
        final ViewPager viewPager=(ViewPager)view.findViewById(R.id.viewpager);

        tabLayout.addTab(tabLayout.newTab().setText("About Me"));
        tabLayout.addTab(tabLayout.newTab().setText("My Courses"));
        tabLayout.addTab(tabLayout.newTab().setText("Settings"));
        tabLayout.addTab(tabLayout.newTab().setText("New"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final TabAdapter adapter = new TabAdapter(getActivity(),getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
}
