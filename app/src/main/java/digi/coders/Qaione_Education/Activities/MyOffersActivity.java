package digi.coders.Qaione_Education.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.Qaione_Education.Adapters.OffersListAdapter;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Coupon;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOffersActivity extends AppCompatActivity {

    private RecyclerView myOffersList;
    private SingleTask singleTask;
    private List<Coupon> listOfCoupons;
    private ProgressBar progressBar;
    private TextView noOfferText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_offers);
        myOffersList=findViewById(R.id.coupon_list);
        noOfferText=findViewById(R.id.no_offers_text);
        progressBar=findViewById(R.id.offer_progress);
        singleTask=(SingleTask)getApplication();
        loadCoupon();
    }
    private void loadCoupon() {
        MyApi myApi= RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call=myApi.getOffers();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    try {
                        JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        String res=jsonObject.getString("res");
                        String msg=jsonObject.getString("msg");
                        if(res.equals("success"))
                        {
                            int i;
                            listOfCoupons=new ArrayList<>();
                            JSONArray jsonArray1=jsonObject.getJSONArray("data");
                            for(i=0;i<jsonArray1.length();i++)
                            {
                                progressBar.setVisibility(View.GONE);
                                myOffersList.setVisibility(View.VISIBLE);
                                Coupon coupon=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),Coupon.class);
                                listOfCoupons.add(coupon);

                            }
                            setResponseInRecycler(listOfCoupons);

                        }
                        else
                        {
                            progressBar.setVisibility(View.GONE);
                            noOfferText.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    private void setResponseInRecycler(List<Coupon> listOfCoupons) {
        myOffersList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        myOffersList.setAdapter(new OffersListAdapter(listOfCoupons));

    }

    public void close(View view) {
        finish();
    }
}