package digi.coders.Qaione_Education.Fragments;

import android.content.Intent;
import android.os.Bundle;

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

import digi.coders.Qaione_Education.Activities.HomeActivity;
import digi.coders.Qaione_Education.Adapters.ItemListAdapter;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.MyItem;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.models.WishlistItem;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseWishlistFragment extends Fragment {

    private RecyclerView coursesList;
    private List<WishlistItem> list;
    private SingleTask singleTask;
    private TextView noItemInWishlist;
    private List<MyItem> itemList;
    private String sendKey;
    private ShimmerFrameLayout shimmer;
    private ElasticLayout purchaseNow;
    private RelativeLayout purchaseLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_course_wishlist, container, false);

        coursesList=view.findViewById(R.id.courses_recycler);
        singleTask=(SingleTask)getActivity().getApplication();
        shimmer=view.findViewById(R.id.courses_fragment_shimmer);
        purchaseNow=view.findViewById(R.id.purchasenow);
        purchaseLayout=view.findViewById(R.id.purchase_now_layout);
        noItemInWishlist=view.findViewById(R.id.no_item);
        loadWishlist();

        purchaseNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HomeActivity.class));
            }
        });

        return view;
    }

    private void loadWishlist() {
        String json=singleTask.getValue("user");
        User user=new Gson().fromJson(json.toString(),User.class);
        MyApi myApi=RetrofitInstanse.getRetrofitInstance().create(MyApi.class);;
        Call<JsonArray> call=myApi.getWishlist(user.getId(),"Course");
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    setCourseResponse(response);

                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("sad",t.getMessage());
            }
        });

    }

    private void setCourseResponse(Response<JsonArray> response) {
        shimmer.stopShimmer();
        shimmer.setVisibility(View.GONE);
        coursesList.setVisibility(View.VISIBLE);
        try {
            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
            JSONObject jsonObject=jsonArray.getJSONObject(0);
            String res=jsonObject.getString("res");
            String msg=jsonObject.getString("msg");
            if(res.equals("success")) {
                int i;
                JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                list=new ArrayList<>();
                if(jsonArray1.length()==0)
                {

                    noItemInWishlist.setVisibility(View.VISIBLE);
                }
                else
                {
                    for (i = 0; i < jsonArray1.length(); i++)
                    {
                        // JSONArray jsonArray2=jsonArray1.getJSONArray(0);
                        WishlistItem wishlistItem=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),WishlistItem.class);
                        list.add(wishlistItem);
                    }
                    setInItemListAdapter(list);
                    noItemInWishlist.setVisibility(View.GONE);
                }

                //Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
            else
            {
                noItemInWishlist.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setInItemListAdapter(List<WishlistItem> list) {
        //Log.e("size",list.size()+"");
        coursesList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        ItemListAdapter ddf=new ItemListAdapter(list);
        ddf.getMy(new ItemListAdapter.GetMyPosition() {
            @Override
            public void position(View view, int position) {
                list.remove(position);
                coursesList.removeViewAt(position);
                ddf.notifyItemRemoved(position);
                ddf.notifyItemRangeChanged(position,list.size());
            }
        });
        coursesList.setAdapter(ddf);
    }

}