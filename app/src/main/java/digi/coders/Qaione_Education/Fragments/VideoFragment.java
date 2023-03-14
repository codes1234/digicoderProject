package digi.coders.Qaione_Education.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import digi.coders.Qaione_Education.R;

public class VideoFragment extends Fragment {

    RecyclerView ebooks;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.video_fragment, container, false);
        ebooks=view.findViewById(R.id.ebooks);
        /*RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        ebooks.setLayoutManager(layoutManager);
        ebooks.setHasFixedSize(true);
        MyCoursesAdapter eBookAdapter=new MyCoursesAdapter(new String[]{"","","","","",""},getActivity(),2);
        ebooks.setAdapter(eBookAdapter);*/
        return view;
    }
}
