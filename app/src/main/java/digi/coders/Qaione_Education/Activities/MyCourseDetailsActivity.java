package digi.coders.Qaione_Education.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.Qaione_Education.Adapters.CourseDetailsAdapter;
import digi.coders.Qaione_Education.Adapters.RatingAdapter;
import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.AuthorDetails;
import digi.coders.Qaione_Education.models.Courses;
import digi.coders.Qaione_Education.models.Review;
import digi.coders.Qaione_Education.models.Reviewdetails;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.models.VideoDetails;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCourseDetailsActivity extends AppCompatActivity {

    RecyclerView course_details,coursereview;
    Button back;

    TextView course_name,course_short_des,authoName, authorCategory;
    public static Courses c;
    ImageView addFaveroite;
    private ImageView authorRoundIcon, image;
    TextView play_demo,write_review,noVideoText,noReviewText;
    private String courseid,enrollid;
    private SingleTask singleTask;
    private Courses courses;
    private String url;
    private RelativeLayout layout;
    private List<VideoDetails> listOfVideo;
    private ShimmerFrameLayout shimmer;
    WebView whatilearn,description,requirment,thisincludes;
    CardView cardIncludes,cardWhatILearn,cardDescription,cardRequirement;
    LinearLayout bottom, line_author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_course_details);
        initView();
        courseid=getIntent().getStringExtra("courseid");
        enrollid=getIntent().getStringExtra("enrollid");
        loadMyCourseDetails();
        //loadPlaylist();
        String str="<html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<div >\n" +
                "  <h2 style=\"color:black;\">London</h2>\n" +
                "  <p style=\"color:gray;\">London is the capital city of England. It is the most populous city in the United Kingdom, with a metropolitan area of over 13 million inhabitants.</p>\n" +
                "  <p style=\"color:gray;\">Standing on the River Thames, London has been a major settlement for two millennia, its history going back to its founding by the Romans, who named it Londinium.</p>\n" +
                "</div> \n" +
                "\n" +
                "</body>\n" +
                "</html>\n";

        description.loadDataWithBaseURL(null,str ,"text/html", "UTF-8",null);
        whatilearn.loadDataWithBaseURL(null,str ,"text/html", "UTF-8",null);
        thisincludes.loadDataWithBaseURL(null,str ,"text/html", "UTF-8",null);
        requirment.loadDataWithBaseURL(null,str ,"text/html", "UTF-8",null);

        write_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final AlertDialog dialogBuilder = new AlertDialog.Builder(MyCourseDetailsActivity.this).create();
                    LayoutInflater inflater =LayoutInflater.from(MyCourseDetailsActivity.this);
                    View dialogView = inflater.inflate(R.layout.write_review_dialog, null);

                    ProgressBar progressBar=dialogView.findViewById(R.id.progress_for_review);
                    Button submit = dialogView.findViewById(R.id.submit);
                    dialogBuilder.setCanceledOnTouchOutside(false);
                    RatingBar rating=dialogView.findViewById(R.id.ratingBar);
                    EditText review=dialogView.findViewById(R.id.user_review);
                    ImageView imageView=dialogView.findViewById(R.id.review_close_btn);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                        }
                    });

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progressBar.setVisibility(View.VISIBLE);
                            String json=singleTask.getValue("user");
                            User user=new Gson().fromJson(json,User.class);
                            MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
                            Call<JsonArray> call=myApi.addReview(user.getId(),courseid,"Course",rating.getRating()+"",review.getText().toString());
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


                                                Toast.makeText(MyCourseDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                dialogBuilder.dismiss();
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        progressBar.setVisibility(View.GONE);

                                    }
                                }

                                @Override
                                public void onFailure(Call<JsonArray> call, Throwable t) {
                                    Log.e("error",t.getMessage());
                                }
                            });

                        }
                    });

                    dialogBuilder.setView(dialogView);
                    dialogBuilder.show();

                } catch(Exception e) {

                }
            }
        });

        bottom.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadCertificate();
                    }
                }
        );
//        play_demo.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        startActivity(new Intent(getApplicationContext(),PlayVideoActivity.class));
//                    }
//                }
//        );
        addFaveroite.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("sad","asds");
                        try{
                            final AlertDialog dialogBuilder = new AlertDialog.Builder(MyCourseDetailsActivity.this).create();
                            LayoutInflater inflater =LayoutInflater.from(MyCourseDetailsActivity.this);
                            View dialogView = inflater.inflate(R.layout.write_review_dialog, null);
                            ProgressBar progressBar=dialogView.findViewById(R.id.progress_for_review);
                            Button submit=dialogView.findViewById(R.id.submit);
                            dialogBuilder.setCanceledOnTouchOutside(false);
                            RatingBar rating=dialogView.findViewById(R.id.ratingBar);
                            EditText review=dialogView.findViewById(R.id.user_review);
                            ImageView imageView=dialogView.findViewById(R.id.review_close_btn);
                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogBuilder.dismiss();
                                }
                            });
                            submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    progressBar.setVisibility(View.VISIBLE);
                                    String json=singleTask.getValue("user");
                                    User user=new Gson().fromJson(json,User.class);
                                    MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
                                    Call<JsonArray> call=myApi.addReview(user.getId(),courseid,"Course",rating.getRating()+"",review.getText().toString());
                                    call.enqueue(new Callback<JsonArray>() {
                                        @Override
                                        public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                                            if (response.isSuccessful()) {
                                                try {
                                                    JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                                    String res = jsonObject.getString("res");
                                                    String msg = jsonObject.getString("msg");
                                                    if (res.equals("success")) {
                                                        write_review.setVisibility(View.GONE);
                                                        addFaveroite.setVisibility(View.GONE);
                                                        Toast.makeText(MyCourseDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                        dialogBuilder.dismiss();
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                progressBar.setVisibility(View.GONE);

                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<JsonArray> call, Throwable t) {
                                            Log.e("error", t.getMessage());
                                        }
                                    });

                                }
                            });


                            dialogBuilder.setView(dialogView);
                            dialogBuilder.show();

                        } catch(Exception e) {

                        }
                    }
                }
        );

        back=findViewById(R.id.back);
        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        /*if(listOfVideo.size()<0)
        {

        }
        else
        {
            VideoDetails[] videoDetails= courses.getVideoDetails();
            for (i=0;i<videoDetails.length;i++)
            {
                listOfVideo.add(videoDetails[0]);
            }
            course_details=findViewById(R.id.course_detail);
            course_details.setFocusable(false);
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
            course_details.setLayoutManager(layoutManager);
            course_details.setHasFixedSize(true);
            CourseDetailsAdapter courseDetailsAdapter=new CourseDetailsAdapter(getApplicationContext(),listOfVideo);
            course_details.setAdapter(courseDetailsAdapter);
            //loadPlaylist();
        }*/
        /*RecyclerView.LayoutManager layoutManager1=new LinearLayoutManager(getApplicationContext());
        coursereview.setLayoutManager(layoutManager1);
        coursereview.setHasFixedSize(true);
        RatingAdapter courseDetailsAdapter1=new RatingAdapter(new String[]{"","","","",""},getApplicationContext());
        coursereview.setAdapter(courseDetailsAdapter1);*/
    }

    private void loadCertificate() {

        Log.e("sdsd",enrollid+"");
        String url = "https://www.codersadda.com/Home/Certificate/"+enrollid;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    int i;
    private void loadPlaylist() {
        listOfVideo=new ArrayList<>();
       VideoDetails[] videoDetails= courses.getVideoDetails();
       if(videoDetails.length==0)
       {

           noVideoText.setVisibility(View.VISIBLE);
           Log.e("sds","no video");

       }
       else {
           course_details = findViewById(R.id.course_detail);
           noVideoText.setVisibility(View.GONE);

           for (i = 0; i < videoDetails.length; i++) {
               listOfVideo.add(videoDetails[i]);
           }

           course_details.setFocusable(false);
           RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
           course_details.setLayoutManager(layoutManager);
           course_details.setHasFixedSize(true);
           int status=2;
           CourseDetailsAdapter courseDetailsAdapter = new CourseDetailsAdapter(getApplicationContext(), listOfVideo, courses,status);
           course_details.setAdapter(courseDetailsAdapter);
       }
    }

    private void loadReview() {
        Reviewdetails reviewdetaill=c.getReviewdetails();

        Review[] d=reviewdetaill.getList();
        if(d.length>0) {
            coursereview.setVisibility(View.VISIBLE);
            noReviewText.setVisibility(View.GONE);
            RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext());
            coursereview.setLayoutManager(layoutManager1);
            coursereview.setHasFixedSize(true);
            RatingAdapter courseDetailsAdapter1 = new RatingAdapter(d, getApplicationContext());
            coursereview.setAdapter(courseDetailsAdapter1);
        }
        else
        {
            coursereview.setVisibility(View.VISIBLE);
            noReviewText.setVisibility(View.VISIBLE);
        }
    }

    private void loadMyCourseDetails() {
        Log.e("sd","load");
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        String json=singleTask.getValue("user");
        User user=new Gson().fromJson(json,User.class);

        Call<JsonArray> call;
        if(user!=null)
        {
            Log.e("bvjb", courseid+" | "+user.getId()+" | "+user.getNumber());
            call = myApi.enrolledCourse(courseid,user.getId(),user.getNumber());
        }
        else
        {
            call = myApi.enrolledCourse(courseid,"","");

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
                Log.e("sdsd",t.getMessage());
            }
        });
    }
    private void setResponse(Response<JsonArray> response) {
        Log.e("e","asds");
        try {
            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
            JSONObject jsonObject=jsonArray.getJSONObject(0);
            String res=jsonObject.getString("res");
            String msg=jsonObject.getString("msg");
            if(res.equalsIgnoreCase("success"))
            {

                JSONArray jsonArray1=jsonObject.getJSONArray("data");
                courses=new Gson().fromJson(jsonArray1.getJSONObject(0).toString(), Courses.class);
                MyCourseDetailsActivity.c=courses;
                setResponseInThis(courses);
                loadPlaylist();
                loadReview();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setResponseInThis(Courses courses) {
        shimmer.setVisibility(View.GONE);
        shimmer.stopShimmer();
        layout.setVisibility(View.VISIBLE);
        course_name.setText(courses.getName());
        course_short_des.setText(courses.getShortdesc());
        String[] as=courses.getAuthor().split(" ");

        if (courses.getDescription().equals("")){
            cardDescription.setVisibility(View.GONE);
        }
        if (courses.getRequirement().equals("")){
            cardRequirement.setVisibility(View.GONE);
        }
        if (courses.getWill_learn().equals("")){
            cardWhatILearn.setVisibility(View.GONE);
        }
        if (courses.getCourseInclude().equals("")){
            cardIncludes.setVisibility(View.GONE);
        }
        description.loadData(courses.getDescription(),"text/html","UTF-8");
        requirment.loadData(courses.getRequirement(),"text/html","UTF-8");
        whatilearn.loadData(courses.getWill_learn(),"text/html","UTF-8");
        thisincludes.loadData(courses.getCourseInclude(),"text/html","UTF-8");
        String[] sp=courses.getDiscountpercent().split(" ");
        if(courses.getReviewstatus().equals("true"))
        {
            addFaveroite.setVisibility(View.GONE);
            write_review.setVisibility(View.GONE);
        }
        else
        {

        }
        if(courses.getCertificatestatus().equals("true"))
        {
            bottom.setVisibility(View.VISIBLE);
        }
        else
        {
            bottom.setVisibility(View.GONE);

        }
        enrollid=courses.getEnroll().getId();
        //Double discount =Double.parseDouble(courses.getPrice())-(Double.parseDouble(courses.getPrice())*Double.parseDouble(sp[0])/100);
        //long d=Math.round(discount);

        AuthorDetails authorDetails=courses.getAuthorDetails();
        authoName.setText(authorDetails.getName());
        authorCategory.setText(authorDetails.getDesignation());
        url=authorDetails.getSocialLink();
        line_author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), EducatorActivity.class);
                intent.putExtra("id",authorDetails.getId());
                startActivity(intent);
            }
        });

        Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.TUTOR+authorDetails.getPhoto()).into(authorRoundIcon);
        Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.COURSES+courses.getBanner()).into(image);

    }

    private void initView() {
        description=findViewById(R.id.description);
        whatilearn=findViewById(R.id.whatilearn);
        thisincludes=findViewById(R.id.includes);
        requirment=findViewById(R.id.requirement);
        write_review=findViewById(R.id.write_review);

        cardIncludes=findViewById(R.id.cardInclude);
        cardWhatILearn=findViewById(R.id.cardWhatILearn);
        cardDescription=findViewById(R.id.cardDescription);
        cardRequirement=findViewById(R.id.cardRequirement);

        image=findViewById(R.id.image);
        course_name=findViewById(R.id.purchase_course_name);
        course_short_des=findViewById(R.id.purchase_course_shordesc);
        authorRoundIcon=findViewById(R.id.purchase_cauthor_pic_round);
        authoName=findViewById(R.id.purchae_course_author_name);
        authorCategory=findViewById(R.id.authorCategory);
        line_author=findViewById(R.id.line_author);
        singleTask=(SingleTask)getApplication();
        coursereview=findViewById(R.id.course_review);
        addFaveroite=findViewById(R.id.addFaveroitesas);
        bottom=findViewById(R.id.bottom);
        layout=findViewById(R.id.main_layou);

        shimmer=findViewById(R.id.my_shimmer);
        noVideoText=findViewById(R.id.no_videos_text);
        noReviewText=findViewById(R.id.no_review_text);

    }

    public void goOnInstagram(View view) {
        openChromeCustomeTab(url);
    }
    public void openChromeCustomeTab(String uri){
        CustomTabsIntent.Builder builder=new CustomTabsIntent.Builder();
        CustomTabsIntent intent=builder.build();
        intent.launchUrl( this, Uri.parse( uri ) );
    }


    public void goBackw(View view) {
        finish();
    }
}
