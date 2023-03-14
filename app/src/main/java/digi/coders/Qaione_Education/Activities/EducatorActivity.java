package digi.coders.Qaione_Education.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.Qaione_Education.Adapters.AudioBookAdapter;
import digi.coders.Qaione_Education.Adapters.CoursesAdapter;
import digi.coders.Qaione_Education.Adapters.EBookAdapter;
import digi.coders.Qaione_Education.Adapters.LiveClassesAdapter;
import digi.coders.Qaione_Education.Adapters.QuzListAdapter;
import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Abook;
import digi.coders.Qaione_Education.models.Courses;
import digi.coders.Qaione_Education.models.Ebook;
import digi.coders.Qaione_Education.models.LiveSession;
import digi.coders.Qaione_Education.models.QuizListData;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EducatorActivity extends AppCompatActivity {

    ImageView image;
    private SingleTask singleTask;
    TextView specialist, name, qualification, university, award, liveIn, cod, language, teaches;
    LinearLayout line_courses, line_about;
    View view_course, view_about;
    TextView course, about;
    RecyclerView courses_recycler, ebook_recycler, abook_recycler, live_recycler, quiz_recycler;
    ArrayList<Courses> coursesList=new ArrayList<>();
    private List<Ebook> ebookList=new ArrayList<>();
    private List<Abook> abookList=new ArrayList<>();
    private List<LiveSession> liveSessionList=new ArrayList<>();
//    private List<QuizListData> quizsList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educator);

        singleTask=(SingleTask)getApplication();
        image=findViewById(R.id.image);
        specialist=findViewById(R.id.specialist);
        name=findViewById(R.id.name);
        qualification=findViewById(R.id.qualification);
        line_courses=findViewById(R.id.line_courses);
        line_about=findViewById(R.id.line_about);
        view_course=findViewById(R.id.view_course);
        view_about=findViewById(R.id.view_about);
        course=findViewById(R.id.course);
        about=findViewById(R.id.about);
        university=findViewById(R.id.university);
        award=findViewById(R.id.award);
        liveIn=findViewById(R.id.liveIn);
        cod=findViewById(R.id.cod);
        language=findViewById(R.id.language);
        teaches=findViewById(R.id.teaches);
        courses_recycler=findViewById(R.id.courses_recycler);
        ebook_recycler=findViewById(R.id.ebook_recycler);
        abook_recycler=findViewById(R.id.abook_recycler);
        live_recycler=findViewById(R.id.live_recycler);
        quiz_recycler=findViewById(R.id.quiz_recycler);

        line_courses.setVisibility(View.VISIBLE);
        view_course.setVisibility(View.VISIBLE);
        line_about.setVisibility(View.GONE);
        view_about.setVisibility(View.GONE);

        course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                line_courses.setVisibility(View.VISIBLE);
                view_course.setVisibility(View.VISIBLE);
                line_about.setVisibility(View.GONE);
                view_about.setVisibility(View.GONE);
                course.setTextColor(Color.parseColor("#585656"));
                about.setTextColor(Color.parseColor("#000000"));
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                line_courses.setVisibility(View.GONE);
                view_course.setVisibility(View.GONE);
                line_about.setVisibility(View.VISIBLE);
                view_about.setVisibility(View.VISIBLE);
                course.setTextColor(Color.parseColor("#000000"));
                about.setTextColor(Color.parseColor("#585656"));
            }
        });

        educatorFullDetails();

    }

    public void educatorFullDetails (){
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.show();
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi= RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call=myApi.educatorFullDetails(getIntent().getExtras().getString("id"));
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                try{

                    Log.i("egfhdvfhjd", response.body().toString()+"  "+getIntent().getExtras().getString("id"));

                    JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    String status=jsonObject.getString("res");
                    if(status.equalsIgnoreCase("success")){

                        JSONObject jsonObject1=jsonObject.getJSONObject("data");
                        Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.TUTOR +jsonObject1.getString("photo"))
                                .placeholder(R.drawable.user).into(image);
                        specialist.setText(jsonObject1.getString("designation"));
                        name.setText(jsonObject1.getString("name"));
                        qualification.setText(Html.fromHtml(jsonObject1.getString("about")));
                        university.setText(Html.fromHtml(jsonObject1.getString("studied_at")));
                        award.setText(Html.fromHtml(jsonObject1.getString("award")));
                        liveIn.setText(Html.fromHtml(jsonObject1.getString("lives_in")));
                        cod.setText(Html.fromHtml(jsonObject1.getString("birthday")));
                        language.setText(Html.fromHtml(jsonObject1.getString("language_known")));
                        teaches.setText(Html.fromHtml("Teaches "+jsonObject1.getString("skills")));
                        //description.setText(jsonObject1.getString("about"));
//                        university, award, liveIn, cod, language, teaches

                        JSONArray jsonArray1=jsonObject1.getJSONArray("courses");
                        for (int i=0;i<jsonArray1.length();i++) {
                            Courses course=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), Courses.class);
                            coursesList.add(course);

                            RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
                            courses_recycler.setLayoutManager(layoutManager);
                            courses_recycler.setHasFixedSize(true);
                            CoursesAdapter coursesByCategoryAdapter=new CoursesAdapter(EducatorActivity.this,2,coursesList);
                            courses_recycler.setItemAnimator(new DefaultItemAnimator());
                            courses_recycler.setAdapter(coursesByCategoryAdapter);

                        }
                        JSONArray jsonArray2=jsonObject1.getJSONArray("live_sessions");
                        Log.i("live", jsonArray2+"");
                        for (int j=0;j<jsonArray2.length();j++) {
                            JSONObject jsonObject2 = jsonArray2.getJSONObject(j);
                            LiveSession liveSession=new Gson().fromJson(jsonObject2.toString(), LiveSession.class);
                            liveSessionList.add(liveSession);

                            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL,false);
                            live_recycler.setLayoutManager(layoutManager);
                            live_recycler.setHasFixedSize(true);
                            LiveClassesAdapter liveClassesAdapter=new LiveClassesAdapter(EducatorActivity.this,liveSessionList,singleTask,0);
                            live_recycler.setItemAnimator(new DefaultItemAnimator());
                            live_recycler.setAdapter(liveClassesAdapter);

                        }
                        JSONArray jsonArray3=jsonObject1.getJSONArray("ebooks");
                        Log.i("ebooks", jsonArray3+"");
                        for (int j=0;j<jsonArray3.length();j++) {
                            JSONObject jsonObject2 = jsonArray3.getJSONObject(j);
                            Ebook ebook=new Gson().fromJson(jsonObject2.toString(), Ebook.class);
                            ebookList.add(ebook);

                            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL,false);
                            ebook_recycler.setLayoutManager(layoutManager);
                            ebook_recycler.setHasFixedSize(true);
                            EBookAdapter eBookAdapter=new EBookAdapter(ebookList,1);
                            ebook_recycler.setItemAnimator(new DefaultItemAnimator());
                            ebook_recycler.setAdapter(eBookAdapter);

                        }
                        JSONArray jsonArray4=jsonObject1.getJSONArray("abooks");
                        Log.i("abooks", jsonArray4+"");
                        for (int j=0;j<jsonArray4.length();j++) {
                            JSONObject jsonObject2 = jsonArray4.getJSONObject(j);
                            Abook abook=new Gson().fromJson(jsonObject2.toString(), Abook.class);
                            abookList.add(abook);

                            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL,false);
                            abook_recycler.setLayoutManager(layoutManager);
                            abook_recycler.setHasFixedSize(true);
                            AudioBookAdapter audioBookAdapter=new AudioBookAdapter(abookList);
                            abook_recycler.setItemAnimator(new DefaultItemAnimator());
                            abook_recycler.setAdapter(audioBookAdapter);

                        }
                        JSONArray jsonArray5=jsonObject1.getJSONArray("quizList");
                        Log.i("quizList", jsonArray5+"");
                        QuizListData[] quizsList = new QuizListData[jsonArray5.length()];
                        for (int j=0;j<jsonArray5.length();j++) {
                            JSONObject jsonObject2 = jsonArray5.getJSONObject(j);
                            QuizListData quizData=new Gson().fromJson(jsonObject2.toString(), QuizListData.class);
//                            quizsList[quizData];
                            quizsList[j]=quizData;
                        }
                        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL,false);
                        quiz_recycler.setLayoutManager(layoutManager);
                        quiz_recycler.setHasFixedSize(true);
                        QuzListAdapter quzListAdapter=new QuzListAdapter(1,quizsList);
                        quiz_recycler.setItemAnimator(new DefaultItemAnimator());
                        quiz_recycler.setAdapter(quzListAdapter);

                    }else {
                        Toast.makeText(getApplicationContext(),jsonObject.getString("msg"), Toast.LENGTH_LONG).show();

                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.getMessage().toString(),Toast.LENGTH_LONG).show();
                        Log.i("check",e.getMessage());
                }
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage().toString(),Toast.LENGTH_LONG).show();
                pd.dismiss();
            }
        });
    }

    public void close(View view) {
        finish();
    }

}