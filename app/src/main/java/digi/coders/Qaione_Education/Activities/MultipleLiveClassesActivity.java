        package digi.coders.Qaione_Education.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import digi.coders.Qaione_Education.Adapters.LiveClassesAdapter;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.LiveSession;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MultipleLiveClassesActivity extends AppCompatActivity {

    RecyclerView liveclassess, recycler_view;
    Button back;
    ImageView share;
    private SingleTask singleTask;
    private List<LiveSession> liveSessionList;
    private LiveSession liveSession;
    private ProgressBar pro;
    private TextView noLiveSession;

    int currentPage = 0;
    Timer timer;
    int DELAY_MS=3000,PERIOD_MS=3000;
    Runnable runnable;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_live_classes);

        singleTask=(SingleTask)getApplication();
        back=findViewById(R.id.back);
        share=findViewById(R.id.share);
        noLiveSession=findViewById(R.id.no_live_session_txt);
        pro=findViewById(R.id.live_progress);
        liveclassess=findViewById(R.id.liveclassess);
        recycler_view=findViewById(R.id.recycler_view);

        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        handler = new Handler();
        timer = new Timer();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MultipleLiveClassesActivity.this,HomeActivity.class));
                finish();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, "Join Qaione Education Platform "+
                        "\n"+"Download app now:"+"\n"+
                        "https://play.google.com/store/apps/details?id=digi.coders.Qaione_Education");
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                startActivity(Intent.createChooser(intent,"Share via"));

            }
        });

        loadLiveClasses();


    }

    private void loadLiveClasses() {
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi= RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call=myApi.liveSessionList(user.getNumber());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    try {
                        Log.i("rtuw", response.body().toString());
                        JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        String res=jsonObject.getString("res");
                        String msg=jsonObject.getString("msg");
                        if(res.equals("success"))
                        {
                            pro.setVisibility(View.GONE);
                            int i;
                            JSONArray jsonArray1=jsonObject.getJSONArray("data");
                            if(jsonArray1.length()>0) {
                                liveSessionList = new ArrayList<>();
                                for (i = 0; i < jsonArray1.length(); i++) {
                                    liveSession = new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), LiveSession.class);
                                    liveSessionList.add(liveSession);
                                }
                                setResponseInRecycler(liveSessionList);
                            }
                            else
                            {
                                noLiveSession.setVisibility(View.VISIBLE);
                                pro.setVisibility(View.GONE);
                            }
                        }
                        else {
                            noLiveSession.setVisibility(View.VISIBLE);
                            pro.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("sda",t.getMessage());
                pro.setVisibility(View.GONE);
            }
        });
    }

    private void setResponseInRecycler(List<LiveSession> liveSessionList) {
        liveclassess.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        liveclassess.setLayoutManager(layoutManager);
        liveclassess.setHasFixedSize(true);
        LiveClassesAdapter liveClassesAdapter=new LiveClassesAdapter(this,liveSessionList,singleTask, 0);
        liveclassess.setAdapter(liveClassesAdapter);

        runnable = new Runnable() {
            public void run() {
                liveclassess.smoothScrollToPosition(currentPage);
                if (currentPage == liveSessionList.size()) {
                    currentPage = 0;
                }else {
                    currentPage++;
                }
            }
        };
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, DELAY_MS, PERIOD_MS);

        recycler_view.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager layoutManager1=new LinearLayoutManager(getApplicationContext());
        recycler_view.setLayoutManager(layoutManager1);
        recycler_view.setHasFixedSize(true);
        LiveClassesAdapter liveClassesAdapter1=new LiveClassesAdapter(this,liveSessionList,singleTask, 1);
        recycler_view.setAdapter(liveClassesAdapter1);
    }
}
