package digi.coders.Qaione_Education.Fragments;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.skydoves.elasticviews.ElasticLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.Qaione_Education.Adapters.EBookAdapter;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Ebook;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PDFBookFragment extends Fragment {

    private SingleTask singleTask;
    private RecyclerView ebooks;
    private List<Ebook> list;
    private ShimmerFrameLayout shimmer;
    private TextView noEbookAvailable;
    private ElasticLayout purchaseNow;
    private RelativeLayout purchaseLayout;
    FloatingActionButton fabFilter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_p_d_f_book, container, false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        loadEbookApi("");
        fabFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog();
            }
        });
    }

    private void initView(View view){
        singleTask=(SingleTask)getActivity().getApplication();
        ebooks = view.findViewById(R.id.ebook_recycler);;
        shimmer = view.findViewById(R.id.ebook_fragment_shimmer);
        purchaseNow = view.findViewById(R.id.purchasenow);
        purchaseLayout = view.findViewById(R.id.purchase_now_layout);
        noEbookAvailable=view.findViewById(R.id.no_item);
        fabFilter=view.findViewById(R.id.fabFilter);

    }

    public void filterDialog(){
        final AlertDialog dialogBuilder = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogView = inflater.inflate(R.layout.filter_dialog, null);
        dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView paid = dialogView.findViewById(R.id.paid);
        TextView free = dialogView.findViewById(R.id.free);
        TextView all = dialogView.findViewById(R.id.all);
        paid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadEbookApi("Paid");
                dialogBuilder.dismiss();
            }
        });
        free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadEbookApi("Free");
                dialogBuilder.dismiss();
            }
        });
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadEbookApi("");
                dialogBuilder.dismiss();
            }
        });

        dialogBuilder.setCanceledOnTouchOutside(true);
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();


    }

    private void loadEbookApi(String type) {
        MyApi myApi= RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        String json=singleTask.getValue("user");
        Call<JsonArray> call;
        User user=new Gson().fromJson(json,User.class);
        if(user!=null)
        {
            call = myApi.getEbookList(user.getId(),user.getNumber(),type);
        }
        else
        {
            call = myApi.getEbookList("","","");
        }

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                Log.i("wertyuio", response.body().toString());
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
        ebooks.setVisibility(View.VISIBLE);
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
                shimmer.stopShimmer();
                shimmer.setVisibility(View.GONE);
                noEbookAvailable.setVisibility(View.VISIBLE);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setResponseInEbookAdapter(List<Ebook> list) {
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        ebooks.setLayoutManager(layoutManager);
        ebooks.setHasFixedSize(true);
        EBookAdapter eBookAdapter=new EBookAdapter(list, 0);
        ebooks.setAdapter(eBookAdapter);
    }

}