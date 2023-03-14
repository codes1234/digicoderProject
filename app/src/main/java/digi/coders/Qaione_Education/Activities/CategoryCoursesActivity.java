package digi.coders.Qaione_Education.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.Qaione_Education.Adapters.CoursesByCategoryAdapter;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Courses;
import digi.coders.Qaione_Education.models.Ebook;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryCoursesActivity extends AppCompatActivity {

    private RecyclerView courses;
    private Button back;
    private TextView toolbarText;
    private LinearLayout no_data;
     private String  cateogoryId;
     private TextView noCourseTxt;
    private SingleTask singleTask;
    private List<Courses> coursesList;
    private List<Ebook> ebooksList;
    private ShimmerFrameLayout shimmer;
    private NestedScrollView lay;
    private String catrogryName;
    private int status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_courses);
        initView();

        status=getIntent().getIntExtra("status",0);
        if(status==1)
        {
            cateogoryId=getIntent().getStringExtra("id");
            loadCategoryCourses();

        }
        else if(status==3)
        {
            cateogoryId=getIntent().getStringExtra("id");
            loadEducatorsCourse();

        }
        else
        {
            cateogoryId=getIntent().getStringExtra("id");
            loadCategoryEbook();
        }
        Log.e("sdsd",status+"");

        toolbarText.setText(getIntent().getStringExtra("title"));

        courses=findViewById(R.id. course);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*CorsesAdapter corsesAdapter=new CorsesAdapter(getApplicationContext(),2);
        courses.setAdapter(corsesAdapter);*/

    }

    private void loadCategoryEbook() {
        Log.e("course","ebook call");
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi= RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call;
        if(user!=null)
        {
            call = myApi.ebookByCategory(cateogoryId,user.getId());
        }
        else
        {
            call = myApi.ebookByCategory(cateogoryId,"");
        }
        Log.e("id",cateogoryId);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    int i;
                    try {
                        JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        String res=jsonObject.getString("res");
                        String msg=jsonObject.getString("msg");
                        ebooksList=new ArrayList<>();
                        if(res.equals("success"))
                        {
                            shimmer.stopShimmer();
                            shimmer.setVisibility(View.GONE);
                            lay.setVisibility(View.VISIBLE);
                            JSONArray jsonArray1=jsonObject.getJSONArray("data");

                            for(i=0;i<jsonArray1.length();i++)
                            {
                                Ebook ebook =new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),Ebook.class);
                                ebooksList.add(ebook);
                            }

                            setEbookInRecycler(ebooksList);


                        }
                        else
                        {
                            shimmer.stopShimmer();
                            shimmer.setVisibility(View.GONE);
                            noCourseTxt.setVisibility(View.VISIBLE);

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

    private void setEbookInRecycler(List<Ebook> ebooksList) {
        Log.e("sdsd","ebook data"+ebooksList.size());
        coursesList=new ArrayList<>();
        courses.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        CoursesByCategoryAdapter coursesByCategoryAdapter=new CoursesByCategoryAdapter(coursesList,this,status,ebooksList);
        courses.setAdapter(coursesByCategoryAdapter);

    }

    private void initView() {
        singleTask=(SingleTask)getApplication();
        shimmer=findViewById(R.id.shimmer_view_containerc);
        lay=findViewById(R.id.layout13);
        toolbarText=findViewById(R.id.toolbar_text);
        noCourseTxt=findViewById(R.id.no_course_txt);

    }

    private void loadCategoryCourses() {
        Log.e("course","course call");
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call;

        if(user!=null)
        {
            call = myApi.coursesByCategory(cateogoryId,user.getId());

        }
        else
        {
            call = myApi.coursesByCategory(cateogoryId,"");

        }

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    setResponse(response);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });


    }

    private void loadEducatorsCourse() {
        Log.e("educator","educator call");
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call;

        if(user!=null)
        {
            call = myApi.courseByEducators(cateogoryId,user.getId());

        }
        else
        {
            call = myApi.courseByEducators(cateogoryId,"");

        }

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    setResponse(response);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });


    }

    private void setResponse(Response<JsonArray> response) {
        int i;
        try {

            Log.e("educatorres", response.body().toString());
            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
            JSONObject jsonObject=jsonArray.getJSONObject(0);
            String res=jsonObject.getString("res");
            String msg=jsonObject.getString("msg");
            coursesList=new ArrayList<>();
            if(res.equals("success"))
            {
                Courses course = null;
                JSONArray jsonArray1=jsonObject.getJSONArray("data");

                /*if(jsonArray1.length()==0)
                {
                    shimmer.stopShimmer();
                    shimmer.setVisibility(View.GONE);
                    noCourseTxt.setVisibility(View.VISIBLE);
                }
                else {*/
                shimmer.stopShimmer();
                shimmer.setVisibility(View.GONE);
                lay.setVisibility(View.VISIBLE);
                    for(i=0;i<jsonArray1.length();i++)
                    {
                        course=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),Courses.class);
                        coursesList.add(course);
                    }
                    setResponseInRecycler(coursesList);

            }
            else
            {
                shimmer.stopShimmer();
                shimmer.setVisibility(View.GONE);
                noCourseTxt.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setResponseInRecycler(List<Courses> coursesList) {
        ebooksList=new ArrayList<>();
        Log.e("sads","course set");
        courses.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        Log.i("status",coursesList+" "+status);
        CoursesByCategoryAdapter coursesByCategoryAdapter=new CoursesByCategoryAdapter(coursesList,this,status,ebooksList);
        courses.setAdapter(coursesByCategoryAdapter);

    }
}
