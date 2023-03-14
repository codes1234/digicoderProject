package digi.coders.Qaione_Education.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

import digi.coders.Qaione_Education.Adapters.RatingAdapter;
import digi.coders.Qaione_Education.Adapters.TopicListAdapter;
import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.AuthorDetails;
import digi.coders.Qaione_Education.models.Ebook;
import digi.coders.Qaione_Education.models.Ebookdetails;
import digi.coders.Qaione_Education.models.Review;
import digi.coders.Qaione_Education.models.Reviewdetails;
import digi.coders.Qaione_Education.models.TopicListModel;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyEbookDetailsActivity extends AppCompatActivity  {

    RecyclerView recycler_view, coursereview;
    Button back;
    public static Ebookdetails ebookdetails;
    TextView ebookAuthorName, authorCategory, ebookTitle, ebookShortDesc, shortDis, no_playlist_text;
    LinearLayout  line_ebook, line_author;

    ImageView addReview;
    private ImageView authorRoundIcon, image;
    TextView play_demo, write_review;
    private String ebookid;
    private ProgressBar pdfProgress;
    private NestedScrollView lay;
    private SingleTask singleTask;
    private Ebook ebook;
    private int pageno;
    private RelativeLayout layout;
    private String url,pdfUrl;
    private WebView pdfWebview;
    WebView whatilearn, description, thisincludes;
    CardView cardIncludes,cardWhatILearn,cardDescription;
    LinearLayout readEbook;
    LinearLayout bottom;
    private ShimmerFrameLayout shimmer;

    @Override
    protected void onStart() {
        super.onStart();
        loadMyEbookDetails();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ebook_details);
        initView();
        ebookid = getIntent().getStringExtra("ebookid");
        loadMyEbookDetails();

        String str = "<html>\n" +
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

        description.loadDataWithBaseURL(null, str, "text/html", "UTF-8", null);
        whatilearn.loadDataWithBaseURL(null, str, "text/html", "UTF-8", null);
        thisincludes.loadDataWithBaseURL(null, str, "text/html", "UTF-8", null);

        write_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final AlertDialog dialogBuilder = new AlertDialog.Builder(MyEbookDetailsActivity.this).create();
                    LayoutInflater inflater = LayoutInflater.from(MyEbookDetailsActivity.this);
                    View dialogView = inflater.inflate(R.layout.write_review_dialog, null);
                    RatingBar rating=dialogView.findViewById(R.id.ratingBar);
                    EditText review=dialogView.findViewById(R.id.user_review);
                    Button submit = dialogView.findViewById(R.id.submit);
                    ImageView image=dialogView.findViewById(R.id.review_close_btn);
                    image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                        }
                    });
                    dialogBuilder.setCanceledOnTouchOutside(false);
                    ProgressBar progressBar=dialogView.findViewById(R.id.progress_for_review);
                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progressBar.setVisibility(View.VISIBLE);
                            String json=singleTask.getValue("user");
                            User user=new Gson().fromJson(json,User.class);

                            MyApi myApi= RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
                            Call<JsonArray> call=myApi.addReview(user.getId(),ebookid,"Ebook",rating.getRating()+"",review.getText().toString());
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

                                                write_review.setVisibility(View.GONE);
                                                addReview.setVisibility(View.GONE);
                                                Toast.makeText(MyEbookDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
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

                } catch (Exception e) {

                }
            }
        });

        play_demo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), PlayVideoActivity.class));
                    }
                }
        );
        addReview.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("sad", "asds");
                        try {
                            final AlertDialog dialogBuilder = new AlertDialog.Builder(MyEbookDetailsActivity.this).create();
                            LayoutInflater inflater = LayoutInflater.from(MyEbookDetailsActivity.this);
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
                                    MyApi myApi=RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
                                    Call<JsonArray> call=myApi.addReview(user.getId(),ebookid,"Ebook",rating.getRating()+"",review.getText().toString());
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

                                                        addReview.setVisibility(View.GONE);
                                                        write_review.setVisibility(View.GONE);
                                                        Toast.makeText(MyEbookDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
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

                        } catch (Exception e) {

                        }
                    }
                }
        );

        back = findViewById(R.id.back);
        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        /*course_details=findViewById(R.id.course_detail);
        course_details.setFocusable(false);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        course_details.setLayoutManager(layoutManager);
        course_details.setHasFixedSize(true);
        CourseDetailsAdapter courseDetailsAdapter=new CourseDetailsAdapter(new String[]{"","","","",""},getApplicationContext());
        course_details.setAdapter(courseDetailsAdapter)*/
        ;

/*        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext());
        coursereview.setLayoutManager(layoutManager1);
        coursereview.setHasFixedSize(true);
        RatingAdapter courseDetailsAdapter1 = new RatingAdapter(new String[]{"", "", "", "", ""}, getApplicationContext());
        coursereview.setAdapter(courseDetailsAdapter1);*/
      /*  download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //downloadTask();
            }
        });*/
        //downloadTask();
    }

    private void loadReview() {


        Reviewdetails reviewdetaill=ebookdetails.getReviewdetails();

        Review[] d=reviewdetaill.getList();
        if(d.length>0) {
            coursereview.setVisibility(View.VISIBLE);
            RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext());
            coursereview.setLayoutManager(layoutManager1);
            coursereview.setHasFixedSize(true);
            RatingAdapter courseDetailsAdapter1 = new RatingAdapter(d, getApplicationContext());
            coursereview.setAdapter(courseDetailsAdapter1);
        }
    }

    private void downloadTask() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1001);
                return;
            }
            else
            {
                Log.e("sad","download");
                Uri uri= Uri.parse(pdfUrl);
                DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                request.setTitle("Ebook Download");
                request.setDescription("Your Ebook is downloading please wait");
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,System.currentTimeMillis()+"ebook"+".pdf");
                request.setMimeType("*/*");
                downloadManager.enqueue(request);
                Toast.makeText(MyEbookDetailsActivity.this, "Download Started", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void loadMyEbookDetails() {
        Log.e("sd", "load");
        MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        String json=singleTask.getValue("user");
        User user=new Gson().fromJson(json,User.class);

        Call<JsonArray> call;
        if(user!=null)
        {
            call = myApi.enrolledEbook(ebookid,user.getId(),user.getNumber());
        }
        else
        {
            call = myApi.enrolledEbook(ebookid,"","");
        }


        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    setResponse(response);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("sdsd", t.getMessage());
            }
        });
    }

    private void setResponse(Response<JsonArray> response) {
        try {
            Log.i("res", response.body().toString());
            JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String res = jsonObject.getString("res");
            String msg = jsonObject.getString("msg");
            if (res.equalsIgnoreCase("success")) {

                /*ebookShimmerContainer.stopShimmer();
                ebookShimmerContainer.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);*/
                JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                ebook = new Gson().fromJson(jsonArray1.getJSONObject(0).toString(), Ebook.class);
                Ebookdetails eboo = new Gson().fromJson(jsonArray1.getJSONObject(0).toString(), Ebookdetails.class);
                MyEbookDetailsActivity.ebookdetails=eboo;
                setResponseInThis(ebook);

            }

        } catch (JSONException e) {
            Log.i("jfjgfbv", e.getMessage().toString()+"e");
            e.printStackTrace();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setResponseInThis(Ebook ebook) {
        shimmer.stopShimmer();
        shimmer.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);
        AuthorDetails authorDetails = ebook.getAuthordetails();
        ebookTitle.setText(ebook.getName());

        if(ebook.getReviewstatus().equals("true"))
        {
            addReview.setVisibility(View.GONE);
            write_review.setVisibility(View.GONE);
        }
        else
        {

        }
        ebookShortDesc.setText(ebook.getShortdesc());
        shortDis.setText(ebook.getShortdesc());
        if (ebook.getDescription().equals("")){
            cardDescription.setVisibility(View.GONE);
        }
        if (ebook.getWillLearn().equals("")){
            cardWhatILearn.setVisibility(View.GONE);
        }
        if (ebook.getEbookInclude().equals("")){
            cardIncludes.setVisibility(View.GONE);
        }
        description.loadData(ebook.getDescription(), "text/html", "UTF-8");
        whatilearn.loadData(ebook.getWillLearn(), "text/html", "UTF-8");
        thisincludes.loadData(ebook.getEbookInclude(), "text/html", "UTF-8");
        ebookAuthorName.setText(authorDetails.getName());
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
        //Uri path = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/H.pdf");
         pdfUrl = Constraints.BASE_IMAGE_URL + Constraints.EBOOK + ebook.getEbook();

        //pdfUrl="http://www.ddegjust.ac.in/studymaterial/mca-3/ms-11.pdf";
        //URL u = null;

        pdfWebview.loadUrl("https://docs.google.com/gview?embedded=true&url=" + pdfUrl);
        //pdfWebview.loadUrl("file:///android_asset/pdfviewer/index.html?file=" + path);
        pdfWebview.setWebViewClient(new WebViewClient() {
            //once the page is loaded get the html element by class or id and through javascript hide it.

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pdfProgress.setVisibility(View.GONE);
                pdfWebview.setVisibility(View.VISIBLE);
                pdfWebview.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('ndfHFb-c4YZDc-GSQQnc-LgbsSe ndfHFb-c4YZDc-to915-LgbsSe VIpgJd-TzA9Ye-eEGnhe ndfHFb-c4YZDc-LgbsSe')[0].style.visibility='hidden'; })()");

            }
        });
        pdfWebview.getSettings().setSupportZoom(true);
        pdfWebview.getSettings().setBuiltInZoomControls(true);
        pdfWebview.getSettings().setJavaScriptEnabled(true);

        pdfWebview.setHorizontalScrollBarEnabled(true);
        pdfWebview.clearCache(true);

        Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.EBOOK+ebook.getBanner()).into(image);
        Picasso.get().load(Constraints.BASE_IMAGE_URL + Constraints.TUTOR + authorDetails.getPhoto()).into(authorRoundIcon);
        readEbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadPdfActivity.ebook=ebook;
                Intent in=new Intent(MyEbookDetailsActivity.this,ReadPdfActivity.class);
                in.putExtra("status","2");
                startActivity(in);

            }
        });

        List<TopicListModel> list=new ArrayList<>();
        TopicListModel[] s=ebook.getTopicListModels();

        if(s.length==0)
        {
            no_playlist_text.setVisibility(View.VISIBLE);
            recycler_view.setVisibility(View.GONE);
        }
        else
        {
            no_playlist_text.setVisibility(View.GONE);
            int i;
            for(i=0;i<s.length;i++)
            {
                list.add(s[i]);
            }
            if(list.size()>0) {
                recycler_view.setVisibility(View.VISIBLE);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recycler_view.setLayoutManager(layoutManager);
                recycler_view.setHasFixedSize(true);
                int status=1;
                TopicListAdapter topicListAdapter =new TopicListAdapter(this, list,ebook,status);
                recycler_view.setAdapter(topicListAdapter);
            }
        }

    }

    private void initView() {
        description = findViewById(R.id.description);
        whatilearn = findViewById(R.id.whatilearn);
        thisincludes = findViewById(R.id.includes);
        write_review = findViewById(R.id.write_review);
        shimmer = findViewById(R.id.ebookShimmerContainer);

        cardIncludes=findViewById(R.id.cardInclude);
        cardWhatILearn=findViewById(R.id.cardWhatILearn);
        cardDescription=findViewById(R.id.cardDescription);

        line_ebook = findViewById(R.id.line_ebok);
       // download = findViewById(R.id.download);
        ebookTitle = findViewById(R.id.purchase_ebook_name);
        ebookShortDesc = findViewById(R.id.purchase_ebook_shordesc);
        shortDis = findViewById(R.id.shortDis);
        image = findViewById(R.id.image);
        authorRoundIcon = findViewById(R.id.purchase_eauthor_pic_round);
        ebookAuthorName = findViewById(R.id.purchae_ebook_author_name);
        authorCategory = findViewById(R.id.authorCategory);
        line_author = findViewById(R.id.line_author);
        singleTask = (SingleTask) getApplication();
        layout=findViewById(R.id.ebook_main);
        coursereview = findViewById(R.id.course_review);
        addReview = findViewById(R.id.addReview);
        play_demo = findViewById(R.id.play_demo);
        bottom = findViewById(R.id.bottom);
        pdfWebview=findViewById(R.id.pdfWebview);
        pdfProgress=findViewById(R.id.pdfProgress);
        readEbook=findViewById(R.id.read_ebook);

        no_playlist_text=findViewById(R.id.no_playlist_text);
        recycler_view=findViewById(R.id.recycler_view);

        /*pdfView = (PDFView) findViewById(R.id.pdfView);*/
    }

    public void goOnInstagram(View view) {
        openChromeCustomeTab(url);
    }

    public void openChromeCustomeTab(String uri) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent intent = builder.build();
        intent.launchUrl(this, Uri.parse(uri));
    }
}