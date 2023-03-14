package digi.coders.Qaione_Education.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.skydoves.elasticviews.ElasticButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import digi.coders.Qaione_Education.Adapters.CourseDetailsAdapter;
import digi.coders.Qaione_Education.Adapters.RatingAdapter;
import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.AuthorDetails;
import digi.coders.Qaione_Education.models.Courses;
import digi.coders.Qaione_Education.models.Review;
import digi.coders.Qaione_Education.models.Reviewdetails;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.models.VideoDetails;
import digi.coders.Qaione_Education.models.WishlistItem;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseDetailsActivity extends AppCompatActivity {

    private RecyclerView course_details,coursereview,reviewList;
    private Button back;
    private RelativeLayout lay,mainLay;
    private ShimmerFrameLayout shimmer,shimmerForReview;
    private TextView course_name,course_short_des,shortDis,close,author_name,authorCategory,rupee;
    private ImageView image, play, share;
    private Courses courses;
    private YouTubePlayer youTubePlaye;
    private CircleImageView authorRoundIcon;
    private LinearLayout line_enroll, line_introduction, line_author;
    private ElasticButton addFaveroite;
    private TextView cutPrice,netPrice,ratingAverage,reviewCount,noVideosText,noReview;
    private YouTubePlayerView youTubeVideo, youtubeVideoDemo;
    private TextView enrollText;
    WishlistItem items;
    private WebView whatilearn,description,requirment,thisincludes;
    CardView cardIncludes,cardWhatILearn,cardDescription,cardRequirement;
    private SingleTask singleTask;
        private String courseid;
        private String url,d;
        private String videolink;
        private String videoID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        initView();
        courseid=getIntent().getStringExtra("courseid");
        loadWishlist();
        loadCourseDetails();

        netPrice.setText(Html.fromHtml("&#8377")+"2000.00");
        cutPrice.setText(Html.fromHtml("<strike>&#8377 2600.00</strike>"));

        line_introduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                introVideoDialog();

            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                introVideoDialog();

            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(), "clicked!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, "Join Qaione Education Platform "+
                        "\n"+"Download app now:"+"\n"+
                        "https://play.google.com/store/apps/details?id=digi.coders.Qaione_Education");
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                startActivity(Intent.createChooser(intent,"Share via"));
            }
        });

        line_enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(CourseDetailsActivity.this,OrderSummaryActivity.class);
                OrderSummaryActivity.course=courses;
                in.putExtra("key","1");
                startActivity(in);


            }
        });

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

        addFaveroite.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String status = singleTask.getStatus("status");
                        Log.e("asd",d+"");
                        if (status.equals("true") ){
                            singleTask.addStatus("false");
                            loadRemoveWishlistApi();
                            addFaveroite.setBackgroundResource(R.drawable.ic_bookmark_border_black_24dp);
                            //  Toast.makeText(singleTask, "Ok dsf", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            singleTask.addStatus("true");
                            //Toast.makeText(singleTask, "asd", Toast.LENGTH_SHORT).show();
                            wishlistApi();
                            addFaveroite.setBackgroundResource(R.drawable.ic_bookmark_black_24dp);
                            //Toast.makeText(EbookDetailActivity.this, "dsfsf", Toast.LENGTH_SHORT).show();
                        }
                       // Toast.makeText(CourseDetailsActivity.this, "asds", Toast.LENGTH_SHORT).show();
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

        rupee.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //line_enroll.setVisibility(View.VISIBLE);
                        loadProfileDetails();
                    }
                }
        );

//        line_introduction.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        line_about.setVisibility(View.VISIBLE);
//                    }
//                }
//        );
        course_details=findViewById(R.id.course_detail);

    }

    private void loadReview() {
       Reviewdetails reviewdetaill=courses.getReviewdetails();
       Review[] d=reviewdetaill.getList();
       if(d.length>0) {
           noReview.setVisibility(View.GONE);
           shimmerForReview.stopShimmer();
           shimmerForReview.setVisibility(View.GONE);
           coursereview.setVisibility(View.VISIBLE);
           RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext());
           coursereview.setLayoutManager(layoutManager1);
           coursereview.setHasFixedSize(true);
           RatingAdapter courseDetailsAdapter1 = new RatingAdapter(d, getApplicationContext());
           coursereview.setAdapter(courseDetailsAdapter1);
       }
       else
       {
           shimmerForReview.stopShimmer();
           shimmerForReview.setVisibility(View.GONE);
           coursereview.setVisibility(View.GONE);
           noReview.setVisibility(View.VISIBLE);
       }
    }

    private void loadRemoveWishlistApi() {
        MyApi myApi= RetrofitInstanse.getRetrofitInstance().create(MyApi.class);

        Call<JsonArray> call=myApi.removeWishlist(items.getId());
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
                            Toast.makeText(CourseDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("er",t.getMessage());
            }
        });
    }


    private void wishlistApi() {
        String jsonObject=singleTask.getValue("user");
        User user=new Gson().fromJson(jsonObject.toString(), User.class);
        MyApi myApi=RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call=myApi.addWishlist(user.getId(),courseid,"Course");
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    try {
                        JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject1=jsonArray.getJSONObject(0);
                        String res=jsonObject1.getString("res");
                        String msg=jsonObject1.getString("msg");
                        if(res.equals("success")) {
                            //Toast.makeText(EbookDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(CourseDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("sds",t.getMessage());
            }
        });
    }
    private void loadWishlist() {
        String json=singleTask.getValue("user");
        User user=new Gson().fromJson(json,User.class);
        MyApi myApi=RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call=myApi.getWishlist(user.getId(),"");
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
                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                            for (i = 0; i < jsonArray1.length(); i++)
                            {
                                // JSONArray jsonArray2=jsonArray1.getJSONArray(0);
                                items=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(), WishlistItem.class);
                            }
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
    private void loadCourseDetails() {
        MyApi myApi=RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        String json=singleTask.getValue("user");
        User user=new Gson().fromJson(json,User.class);

        Call<JsonArray> call;
        if(user!=null) {
            call = myApi.courseFullDetails(courseid,user.getId(),user.getNumber());
        } else {
            call = myApi.courseFullDetails(courseid,"","");
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

    public void introVideoDialog(){
        try {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(CourseDetailsActivity.this).create();
        LayoutInflater inflater = LayoutInflater.from(CourseDetailsActivity.this);
        View dialogView = inflater.inflate(R.layout.indro_video_dialog, null);

            dialogBuilder.setCanceledOnTouchOutside(false);
            youtubeVideoDemo=dialogView.findViewById(R.id.youtube_video_demo);
            TextView close=dialogView.findViewById(R.id.close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();

                }
            });

            final String[] str=videolink.split("/");
            youtubeVideoDemo.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady( YouTubePlayer youTubePlayer) {
                    super.onReady(youTubePlayer);
                    youTubePlaye=youTubePlayer;
//                youTubePlayer.cueVideo(str[str.length-1],0);
                    //youTubePlayer.cueVideo(str[str.length-1],0);
                    youTubePlayer.loadVideo(str[str.length-1],0);

                }
            });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

        } catch (Exception e) {

        }
    }

    private void setResponse(Response<JsonArray> response) {
        try {
            Log.i("fkhdsfhsgaf", response.body().toString());
            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
            JSONObject jsonObject=jsonArray.getJSONObject(0);
            String res=jsonObject.getString("res");
            String msg=jsonObject.getString("msg");
            if(res.equalsIgnoreCase("success"))
            {

                JSONArray jsonArray1=jsonObject.getJSONArray("data");
                courses=new Gson().fromJson(jsonArray1.getJSONObject(0).toString(), Courses.class);
                setResponseInThis(courses);

            }
            else
            {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setResponseInThis(Courses courses) {
        shimmer.stopShimmer();
        shimmer.setVisibility(View.GONE);
        lay.setVisibility(View.GONE);
        mainLay.setVisibility(View.VISIBLE);
        Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.COURSES+courses.getBanner()).into(image);
        course_name.setText(courses.getName());
        course_short_des.setText(courses.getShortdesc());
        shortDis.setText(courses.getShortdesc());
        String[] as=courses.getAuthor().split(" ");
        ratingAverage.setText(courses.getReviewdetails().getAverage()+"");
        reviewCount.setText("("+courses.getReviewdetails().getCount()+" Review )");
        author_name.setText(as[0]);

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
        //Double discount =Double.parseDouble(courses.getPrice())-(Double.parseDouble(courses.getPrice())*Double.parseDouble(sp[0])/100);
        //long d=Math.round(discount);
        if (courses.getUpcoming().equals("true")){
            netPrice.setVisibility(View.GONE);
            cutPrice.setVisibility(View.GONE);
            enrollText.setText("Get after "+courses.getTiming());
            line_enroll.setEnabled(false);
            line_enroll.setClickable(false);
        }else {
            if(courses.getType().equals("Paid"))
            {
                netPrice.setText(Html.fromHtml("&#8377")+courses.getOfferprice());
                if(courses.getDiscountpercent().equals(0))
                {
                    cutPrice.setVisibility(View.GONE);
                }
                else
                {
                    cutPrice.setText(Html.fromHtml("<strike>&#8377 "+courses.getPrice()+""+"</strike>"));
                }

            }
            else
            {
                netPrice.setVisibility(View.GONE);
                cutPrice.setVisibility(View.GONE);
                enrollText.setText("Get it free");
            }
        }

        videolink=courses.getDemovedio();
        videoID=courses.getYoutubeId();
        d=courses.getWishlistStatus();
        if(d.equals("true"))
        {
            addFaveroite.setBackgroundResource(R.drawable.ic_bookmark_black_24dp);
            singleTask.addStatus(d);
        }

        if(d.equals("false"))
        {
            addFaveroite.setBackgroundResource(R.drawable.ic_bookmark_border_black_24dp);
            singleTask.addStatus(d);
        }

        AuthorDetails authorDetails=courses.getAuthorDetails();
        author_name.setText(authorDetails.getName());
        authorCategory.setText(authorDetails.getDesignation());
        url=authorDetails.getSocialLink();
        Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.TUTOR+authorDetails.getPhoto()).into(authorRoundIcon);
        line_author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), EducatorActivity.class);
                intent.putExtra("id",authorDetails.getId());
                startActivity(intent);
            }
        });

        List<VideoDetails> list=new ArrayList<>();
        if(sp.length>0)
        {

            VideoDetails[] s=courses.getVideoDetails();

            if(s.length==0)
            {
                noVideosText.setVisibility(View.VISIBLE);
                course_details.setVisibility(View.GONE);
            }
            else
            {
                noVideosText.setVisibility(View.GONE);
                int i;
                for(i=0;i<s.length;i++)
                {
                    list.add(s[i]);
                }
                if(list.size()>0) {
                    course_details.setVisibility(View.VISIBLE);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    course_details.setLayoutManager(layoutManager);
                    course_details.setHasFixedSize(true);
                    int status=1;
                    CourseDetailsAdapter courseDetailsAdapter =new CourseDetailsAdapter(this, list,courses,status);
                    course_details.setAdapter(courseDetailsAdapter);
                }
            }

        }
        final String[] str=videolink.split("/");
        youTubeVideo.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady( YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                youTubePlaye=youTubePlayer;
//                youTubePlayer.cueVideo(str[str.length-1],0);
                youTubePlayer.cueVideo(videoID,0);
                //youTubePlayer.loadVideo(str[str.length-1],0);

            }
        });
//        player.initialize("AIzaSyBVtgQ_gPJFuHbocLmOJqbjBisJ_ofZw0g",
//                new YouTubePlayer.OnInitializedListener() {
//                    @Override
//                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
//                                                        YouTubePlayer youTubePlayer, boolean b) {
//
//                        // do any work here to cue video, play video, etc.
//                        youTubePlayer.cueVideo("9I6W7j68yr8");
//                    }
//                    @Override
//                    public void onInitializationFailure(YouTubePlayer.Provider provider,
//                                                        YouTubeInitializationResult youTubeInitializationResult) {
//
//                    }
//                });
        //cutPrice.setText(discount+"");
    }


    private void initView() {
        singleTask=(SingleTask)getApplication();
        description=findViewById(R.id.description);
        whatilearn=findViewById(R.id.whatilearn);
        thisincludes=findViewById(R.id.includes);
        requirment=findViewById(R.id.requirement);
        enrollText=findViewById(R.id.enrolltext);

        cardIncludes=findViewById(R.id.cardInclude);
        cardWhatILearn=findViewById(R.id.cardWhatILearn);
        cardDescription=findViewById(R.id.cardDescription);
        cardRequirement=findViewById(R.id.cardRequirement);

        coursereview=findViewById(R.id.course_review);
        addFaveroite=findViewById(R.id.addFaveroites);
        image=findViewById(R.id.image);
        play=findViewById(R.id.play);
        share=findViewById(R.id.share);

        ratingAverage=findViewById(R.id.rating_average);
        reviewCount=findViewById(R.id.review_count);
        cutPrice=findViewById(R.id.cut_price);
        line_enroll=findViewById(R.id.line_enroll);
        line_introduction=findViewById(R.id.line_introduction);
        netPrice=findViewById(R.id.netPrice);
        course_name=findViewById(R.id.course_name);
        course_short_des=findViewById(R.id.course_short_description);
        shortDis=findViewById(R.id.shortDis);
        author_name=findViewById(R.id.author_name);
        line_author=findViewById(R.id.line_author);
        authorCategory=findViewById(R.id.authorCategory);
        rupee=findViewById(R.id.rupee);
        close=findViewById(R.id.close);

        authorRoundIcon=findViewById(R.id.author_circular_icon);

        lay=findViewById(R.id.full);
        shimmer=findViewById(R.id.shimmerContain);
        mainLay=findViewById(R.id.main_lay);

//        shimmerForPlaylist=findViewById(R.id.shimmer_for_playlist);
        shimmerForReview=findViewById(R.id.shimmerForReview);
        youTubeVideo=findViewById(R.id.youtube_video);
        reviewList=findViewById(R.id.course_review);
        noVideosText=findViewById(R.id.no_videos_text);
        noReview=findViewById(R.id.no_review_text);

    }


    public void goBackw(View view) {

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        singleTask.remove("status");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        singleTask.remove("status");
    }

    @Override
    protected void onResume() {
        super.onResume();
        singleTask.remove("status");
    }

//    public void goOnInstagram(View view) {
//        if(url!=null) {
//            openChromeCustomeTab(url);
//        }
//    }
//    public void openChromeCustomeTab(String uri){
//        CustomTabsIntent.Builder builder=new CustomTabsIntent.Builder();
//        CustomTabsIntent intent=builder.build();
//        intent.launchUrl( this, Uri.parse( uri ) );
//    }

    @Override
    protected void onStop() {
        super.onStop();
        if(youTubePlaye!=null)
        {
            youTubePlaye.pause();
        }
        else
        {
            youTubeVideo.release();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(youTubePlaye!=null)
        {
            youTubePlaye.pause();
        }
        else
        {
            youTubeVideo.release();
        }
    }


    private void loadProfileDetails() {
        String json = singleTask.getValue("user");
        User user = new Gson().fromJson(json, User.class);
        MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call = myApi.userProfile(user.getId());
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
                            User use = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), User.class);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            singleTask.addValue("user", jsonObject1);

                            if (jsonObject1.getString("admission_status").equals("true")) {
                                line_enroll.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(getApplicationContext(), "Take admission first.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(CourseDetailsActivity.this,HomeActivity.class));
                                finish();

                            }

                        }
                        //Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();

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


}
