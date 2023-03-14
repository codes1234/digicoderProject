package digi.coders.Qaione_Education.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import digi.coders.Qaione_Education.Adapters.QuizTabAdapter;
import digi.coders.Qaione_Education.R;

public class QuizFragment extends Fragment {

    private TabLayout tabLayout;
    QuizFragment binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_quiz, container, false);

        tabLayout= view.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Attempted"));
        tabLayout.addTab(tabLayout.newTab().setText("Unattempted"));
        ViewPager viewPager= view.findViewById(R.id.viewpagr);
        QuizTabAdapter quizTabAdapter=new QuizTabAdapter(getActivity(),getChildFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(quizTabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}