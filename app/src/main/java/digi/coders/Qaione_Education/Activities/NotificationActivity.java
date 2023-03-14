package digi.coders.Qaione_Education.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.Qaione_Education.Adapters.NotificationAdapter;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Notification;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    RecyclerView notification;
    LinearLayout no_data;
    Button back;
    private SingleTask singleTask;
    private Notification noti;
    private List<Notification> listOfNotification;
    private NestedScrollView lay;
    private ShimmerFrameLayout shimmer;
    private TextView notification_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Intent intent=getIntent();
        Toast.makeText(this, intent.getStringExtra("data"), Toast.LENGTH_SHORT).show();

        lay=findViewById(R.id.layout1);
        shimmer=findViewById(R.id.shimmer_con_notification);
        notification_txt=findViewById(R.id.no_notification_txt);
        back=findViewById(R.id.back);
        singleTask=(SingleTask)getApplication();
        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        loadNotification();
        no_data=findViewById(R.id.layout2);
        notification=findViewById(R.id.notification);
    }

    private void loadNotification() {
        String json=singleTask.getValue("user");
        User user=new Gson().fromJson(json,User.class);
        MyApi myApi= RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call=myApi.getNotification(user.getId());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    Log.i("nofication", response.body().toString());

                    try {
                        JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        String res=jsonObject.getString("res");
                        String msg=jsonObject.getString("msg");
                        if(res.equals("success"))
                        {
                            shimmer.stopShimmer();
                            shimmer.setVisibility(View.GONE);
                            lay.setVisibility(View.VISIBLE);
                            int i;
                            listOfNotification=new ArrayList<>();
                            JSONArray jsonArray1=jsonObject.getJSONArray("data");
                            for(i=0;i<jsonArray1.length();i++)
                            {
                                noti=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),Notification.class);
                                listOfNotification.add(noti);

                            }
                            setNotificationInRecycler(listOfNotification);
                        }
                        else
                        {
                            shimmer.stopShimmer();
                            shimmer.setVisibility(View.GONE);
                            notification_txt.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });

    }

    private void setNotificationInRecycler(List<Notification> listOfNotification) {
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        notification.setLayoutManager(layoutManager);
        notification.setHasFixedSize(true);
        NotificationAdapter notificationAdapter=new NotificationAdapter(listOfNotification,this);
        notification.setAdapter(notificationAdapter);
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus){
//        super.onWindowFocusChanged(hasFocus);
//        getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//    }

}
