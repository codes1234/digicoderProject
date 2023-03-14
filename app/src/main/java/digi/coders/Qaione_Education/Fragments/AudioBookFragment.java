package digi.coders.Qaione_Education.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.skydoves.elasticviews.ElasticLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.Qaione_Education.Adapters.AudioBookAdapter;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Abook;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AudioBookFragment extends Fragment {

    private SingleTask singleTask;
    private RecyclerView audioBooks;
    private List<Abook> list;
    private ShimmerFrameLayout shimmer;
    private TextView noEbookAvailable;
    private ElasticLayout purchaseNow;
    private RelativeLayout purchaseLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_audio_book, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        loadEbookApi();
    }

    private void initView(View view){
        singleTask=(SingleTask)getActivity().getApplication();
        audioBooks = view.findViewById(R.id.audioBooks);;
        shimmer = view.findViewById(R.id.ebook_fragment_shimmer);
        purchaseNow = view.findViewById(R.id.purchasenow);
        purchaseLayout = view.findViewById(R.id.purchase_now_layout);
        noEbookAvailable=view.findViewById(R.id.no_item);

    }

    private void loadEbookApi() {
        MyApi myApi= RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        String json=singleTask.getValue("user");
        Call<JsonArray> call;
        User user=new Gson().fromJson(json,User.class);
        if(user!=null)
        {
            call = myApi.getAudiobookList(user.getId(),user.getNumber());
        }
        else
        {
            call = myApi.getAudiobookList("","");
        }

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                Log.i("getaudiobookList", response.body().toString());
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
        shimmer.stopShimmer();
        shimmer.setVisibility(View.GONE);
        audioBooks.setVisibility(View.VISIBLE);
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
                    Abook abook=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),Abook.class);
                    list.add(abook);
                }
                setResponseInEbookAdapter(list);
            }
            else
            {
                shimmer.stopShimmer();
                shimmer.setVisibility(View.GONE);
                noEbookAvailable.setVisibility(View.VISIBLE);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setResponseInEbookAdapter(List<Abook> list) {
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        audioBooks.setLayoutManager(layoutManager);
        audioBooks.setHasFixedSize(true);
        AudioBookAdapter audioBookAdapter=new AudioBookAdapter(list);
        audioBooks.setAdapter(audioBookAdapter);
    }
}