package digi.coders.Qaione_Education.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.Qaione_Education.Adapters.EbookTabAdapter;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Ebook;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EBookFragment extends Fragment {

    private SingleTask singleTask;
    private List<Ebook> list;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.ebook_fragment, container, false);

       // tabLayout= view.findViewById(R.id.tablayoute);
//        tabLayout.addTab(tabLayout.newTab().setText("Audio Book"));
       // tabLayout.addTab(tabLayout.newTab().setText("PDF Book"));
      /* ViewPager viewPager= view.findViewById(R.id.viewpagr);
        EbookTabAdapter ebookTabAdapter=new EbookTabAdapter(getActivity(),getChildFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(ebookTabAdapter);
       tabLayout.setupWithViewPager(viewPager);*/


        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        loadEbookApi();
    }

    private void initView(View view) {
        singleTask=(SingleTask)getActivity().getApplication();

    }

    private void loadEbookApi() {
        MyApi myApi= RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        String json=singleTask.getValue("user");
        Call<JsonArray> call;
        User user=new Gson().fromJson(json,User.class);
        if(user!=null)
        {
            call = myApi.getEbookList(user.getId(),user.getNumber(),"");
        }
        else
        {
            call = myApi.getEbookList("","","");
        }


        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    setEbookResponse(response);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    private void setEbookResponse(Response<JsonArray> response) {
//        shimmer.stopShimmer();
//        shimmer.setVisibility(View.GONE);
//        ebooks.setVisibility(View.VISIBLE);
        try {
            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
            JSONObject jsonObject=jsonArray.getJSONObject(0);
            String res=jsonObject.getString("res");
            String msg=jsonObject.getString("msg");
            int i;
            if(res.equals("success"))
            {

                JSONArray jsonArray1=jsonObject.getJSONArray("data");
                list=new ArrayList<>();
                for(i=0;i<jsonArray1.length();i++)
                {
                    Ebook ebook=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),Ebook.class);
                    list.add(ebook);
                }
                setResponseInEbookAdapter(list);
            }
            else
            {
//                shimmer.stopShimmer();
//                shimmer.setVisibility(View.GONE);
//                noEbookAvaliable.setVisibility(View.VISIBLE);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setResponseInEbookAdapter(List<Ebook> list) {
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
//        ebooks.setLayoutManager(layoutManager);
//        ebooks.setHasFixedSize(true);
//        EBookAdapter eBookAdapter=new EBookAdapter(list);
//        ebooks.setAdapter(eBookAdapter);
    }

}
