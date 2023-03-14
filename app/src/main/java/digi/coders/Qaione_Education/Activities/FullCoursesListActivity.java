package digi.coders.Qaione_Education.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.Qaione_Education.Adapters.FullCoursesListAdapter;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Courses;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FullCoursesListActivity extends AppCompatActivity {

    private TextView toolbarText;
    private Button back;
    private int s;
    private RecyclerView coursesFullList;
    private SingleTask singleTask;
    private List<Courses> listOfCourses;
    private ShimmerFrameLayout shimmer;
    private NestedScrollView lay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_courses_list);
        initView();
        String status=getIntent().getStringExtra("status");
         s=Integer.parseInt(status);

        if(s==0)
        {
            toolbarText.setText("Trending Courses");
            loadTrendingCourses();

        }
         if(s==1)
        {
            toolbarText.setText("Top Courses");
            loadTopCourses();

        }
        if(s==2)
        {
            toolbarText.setText("Latest Courses");
            loadLatestCourses();

        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void loadLatestCourses() {
        loadLatestCoursesApi();
    }

    private void loadLatestCoursesApi() {
        MyApi myApi= RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        String json=singleTask.getValue("user");
        User user=new Gson().fromJson(json,User.class);
        Call<JsonArray> call;
        if(user!=null)
        {
            call = myApi.getLatestCourses(user.getId(),user.getNumber());
        }
        else
        {
            call = myApi.getLatestCourses("","");

        }

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    setLatestCourseResponse(response);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });


    }

    private void setLatestCourseResponse(Response<JsonArray> response) {
        JSONArray jsonArray= null;
        try {
            jsonArray = new JSONArray(new Gson().toJson(response.body()));
            JSONObject jsonObject=jsonArray.getJSONObject(0);
            String res=jsonObject.getString("res");
            String msg=jsonObject.getString("msg");
            if(res.equalsIgnoreCase("success"))
            {
                shimmer.stopShimmer();
                shimmer.setVisibility(View.GONE);
                lay.setVisibility(View.VISIBLE);
                listOfCourses=new ArrayList<>();
                //LatestCourseModel latestCourseModel;
                Courses courses;
                JSONArray jsonArray1=jsonObject.getJSONArray("data");
                for(int i=0;i<jsonArray1.length();i++)
                {

                    courses=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),Courses.class);
                    listOfCourses.add(courses);

                }
                setInRecyclerLatestCourses(listOfCourses);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setInRecyclerLatestCourses(List<Courses> listOfCourses) {
        coursesFullList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        coursesFullList.setAdapter(new FullCoursesListAdapter(listOfCourses,this));
    }

    private void loadTopCourses() {
        Log.e("as","Top");
        loadTopCoursesApi();
    }

    private void loadTopCoursesApi() {
        MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        String json = singleTask.getValue("user");
        User user = new Gson().fromJson(json, User.class);
        Call<JsonArray> call;
        if (user != null) {
            call = myApi.getTopCourses(user.getId(),user.getNumber());
        }
        else
        {
            call = myApi.getTopCourses("","");
        }


        Log.e("we","topapi");
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    setTopCourseResponse(response);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("topcousesexception",t.getMessage());

            }
        });
    }

    private void setTopCourseResponse(Response<JsonArray> response) {
        try {
            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
            JSONObject jsonObject=jsonArray.getJSONObject(0);
            String res=jsonObject.getString("res");
            String msg=jsonObject.getString("msg");

            if(res.equalsIgnoreCase("success"))
            {
                shimmer.stopShimmer();
                shimmer.setVisibility(View.GONE);
                lay.setVisibility(View.VISIBLE);
                listOfCourses=new ArrayList<>();
                Courses courses;
                JSONArray jsonArray1=jsonObject.getJSONArray("data");
                for(int i=0;i<jsonArray1.length();i++)
                {
                    courses=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), Courses.class);
                    listOfCourses.add(courses);

                }
                setInRecyclerTopCourses(listOfCourses);

            }
            Log.e("sds",msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setInRecyclerTopCourses(List<Courses> listOfCourses) {
        coursesFullList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        coursesFullList.setAdapter(new FullCoursesListAdapter(listOfCourses,this));
    }

    private void loadTrendingCourses() {
        loadTrendingCoursesApi();
    }

    private void loadTrendingCoursesApi() {
        MyApi myApi=RetrofitInstanse.getRetrofitInstance().create(MyApi.class);

        String json=singleTask.getValue("user");
        User user=new Gson().fromJson(json,User.class);
        Call<JsonArray> call;
        if(user!=null)
        {
            call = myApi.getTrendingCourses(user.getId(),user.getNumber());
        }
        else
        {
            call = myApi.getTrendingCourses("","");
        }

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    setTrendingResponse(response);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("trendingcourses",t.getMessage());

            }
        });

    }

    private void setTrendingResponse(Response<JsonArray> response) {
        try {
            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
            JSONObject jsonObject=jsonArray.getJSONObject(0);
            String res=jsonObject.getString("res");
            String msg=jsonObject.getString("msg");

            if(res.equalsIgnoreCase("success"))
            {
                shimmer.stopShimmer();
                shimmer.setVisibility(View.GONE);
                lay.setVisibility(View.VISIBLE);
                listOfCourses=new ArrayList<>();
                JSONArray jsonArray1=jsonObject.getJSONArray("data");
                Courses courses;
                for(int i=0;i<jsonArray1.length();i++)
                {
                    courses=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), Courses.class);
                    listOfCourses.add(courses);
                    setInRecyclerTendingCourses(listOfCourses);


                }


            }
            Log.e("tredning",msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setInRecyclerTendingCourses(List<Courses> listOfCourses) {
        coursesFullList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        coursesFullList.setAdapter(new FullCoursesListAdapter(listOfCourses,this));
    }

    private void initView() {
        toolbarText=findViewById(R.id.toolbar_tex);
        back=findViewById(R.id.back_but);
        coursesFullList=findViewById(R.id.courses);
        singleTask=(SingleTask)getApplication();
        shimmer=findViewById(R.id.shimmer_view_containe);
        lay=findViewById(R.id.layout12);
    }
}