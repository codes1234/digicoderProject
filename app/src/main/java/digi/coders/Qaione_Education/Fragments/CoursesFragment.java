package digi.coders.Qaione_Education.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

import digi.coders.Qaione_Education.Adapters.CourseTabAdapter;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Ebook;
import digi.coders.Qaione_Education.singletask.SingleTask;

public class CoursesFragment extends Fragment {

    private SingleTask singleTask;
    private List<Ebook> list;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_courses, container, false);

        tabLayout= view.findViewById(R.id.tablayoute);
        tabLayout.addTab(tabLayout.newTab().setText("My Courses"));
        tabLayout.addTab(tabLayout.newTab().setText("All Courses"));
        ViewPager viewPager= view.findViewById(R.id.viewpagr);
        CourseTabAdapter courseTabAdapter=new CourseTabAdapter(getActivity(),getChildFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(courseTabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}