package digi.coders.Qaione_Education.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
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
import com.skydoves.elasticviews.ElasticButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import digi.coders.Qaione_Education.Adapters.RatingAdapter;
import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.AuthorDetails;
import digi.coders.Qaione_Education.models.Ebookdetails;
import digi.coders.Qaione_Education.models.Review;
import digi.coders.Qaione_Education.models.Reviewdetails;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.models.WishlistItem;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EbookDetailActivity extends AppCompatActivity {
    RecyclerView course_details,coursereview;
    Button back;
    TextView ebookAuthorName,authorCategory,noReviewTxt,enrollTxt,rupee;
    private NestedScrollView nested;
    LinearLayout bottom, readSample, line_enroll, line_author;
    private String ebookId,url;
    ElasticButton addFaveroite;
    public static WishlistItem items;
    private RelativeLayout layout;
    private ShimmerFrameLayout shimmer,shimmerForReview;
    private SingleTask singleTask;
    private String d;
    ImageView image, play_button, share;
    private CircleImageView authorCircularIcon;
    private TextView play_demo,cutPrice,netPrice,ebookTitle,ebookShortDesc,shortDis,ratingAverage,reviewCount;
    private Ebookdetails ebook;
    CardView cardIncludes,cardWhatILearn,cardDescription;
    WebView whatilearn,description,thisincludes;

//    WebView webviewpdf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebook_detail);
        initView();
        ebookId=getIntent().getStringExtra("ebookid");
        loadEbookDetails();
        loadWishlist();
        netPrice.setText(Html.fromHtml("&#8377")+"2000.00");
        cutPrice.setText(Html.fromHtml("<strike>&#8377 2600.00</strike>"));

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

        String str1="<html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<div >\n" +
                "  <h2 style=\"color:black;\">Sample Book Title</h2>\n" +
                "  <p style=\"color:gray;\">London is the capital city of England. It is the most populous city in the United Kingdom, with a metropolitan area of over 13 million inhabitants.</p>\n" +
                "  <p style=\"color:gray;\">Standing on the River Thames, London has been a major settlement for two millennia, its history going back to its founding by the Romans, who named it Londinium.</p>\n" +
                "  <p style=\"color:gray;\">Standing on the River Thames, London has been a major settlement for two millennia, its history going back to its founding by the Romans, who named it Londinium.</p>\n" +
                "</div> \n" +
                "\n" +
                "</body>\n" +
                "</html>\n";

        description.loadDataWithBaseURL(null,str ,"text/html", "UTF-8",null);
        whatilearn.loadDataWithBaseURL(null,str ,"text/html", "UTF-8",null);
        thisincludes.loadDataWithBaseURL(null,str ,"text/html", "UTF-8",null);
        //webviewpdf.loadData
        //WithBaseURL(null,str1 ,"text/html", "UTF-8",null);

        play_demo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(),PlayVideoActivity.class));
                    }
                }
        );
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
        addFaveroite.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String status = singleTask.getStatus("status");
                        if (status.equals("true")){
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
                        line_enroll.setVisibility(View.VISIBLE);
                    }
                }
        );

        /*course_details=findViewById(R.id.course_detail);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        course_details.setLayoutManager(layoutManager);
        course_details.setHasFixedSize(true);*/
    }

    private void loadReview() {
        Reviewdetails reviewdetaill=ebook.getReviewdetails();

        Review[] d=reviewdetaill.getList();
        if(d.length>0) {
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
            noReviewTxt.setVisibility(View.VISIBLE);
            shimmerForReview.stopShimmer();
            shimmerForReview.setVisibility(View.GONE);
            coursereview.setVisibility(View.GONE);
        }
    }

    private void loadWishlist() {
        String json=singleTask.getValue("user");
        User user=new Gson().fromJson(json,User.class);

        MyApi myApi= RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
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
                                items=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),WishlistItem.class);

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

    private void loadRemoveWishlistApi() {
        MyApi myApi=RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
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
                            Toast.makeText(EbookDetailActivity.this, msg, Toast.LENGTH_SHORT).show();

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

        Call<JsonArray> call=myApi.addWishlist(user.getId(),ebookId,"Ebook");
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
                        Toast.makeText(EbookDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    private void loadEbookDetails() {
        MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        String json = singleTask.getValue("user");
        User user = new Gson().fromJson(json, User.class);
        Call<JsonArray> call;

        if (user != null)
        {
            call = myApi.getEbookFullDetails(ebookId,user.getId(),user.getNumber());
        }
        else
        {
            call = myApi.getEbookFullDetails(ebookId,"","");
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

        try {
            Log.i("ebookres", response.body().toString());
            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
            JSONObject jsonObject=jsonArray.getJSONObject(0);
            String res=jsonObject.getString("res");
            String msg=jsonObject.getString("msg");
            if(res.equalsIgnoreCase("success"))
            {
                shimmer.stopShimmer();
                shimmer.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);

                JSONArray jsonArray1=jsonObject.getJSONArray("data");
                ebook =new Gson().fromJson(jsonArray1.getJSONObject(0).toString(), Ebookdetails.class);

                setResponseInThis(ebook);
            }
            else
            {
                Toast.makeText(EbookDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,HomeActivity.class));
                finish();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setResponseInThis(Ebookdetails ebook) {
        //ebook1=ebook;
        OrderSummaryActivity.ebook=ebook;
        AuthorDetails authorDetails=ebook.getAuthordetails();
        ebookTitle.setText(ebook.getName());
        ebookShortDesc.setText(ebook.getShortdesc());
        shortDis.setText(ebook.getShortdesc());
        Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.EBOOK+ebook.getBanner()).into(image);

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
        String pdfUrl=Constraints.BASE_IMAGE_URL+Constraints.EBOOK+ebook.getSample();
        Log.e("sdsd",pdfUrl+"");
//        webviewpdf.loadUrl( "https://drive.google.com/gview?embedded=true&url="+pdfUrl);
//        webviewpdf.getSettings().setSupportZoom(true);
//        webviewpdf.getSettings().setJavaScriptEnabled(true);
//        webviewpdf.setWebViewClient(new WebViewClient() {
//            //once the page is loaded get the html element by class or id and through javascript hide it.
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                //pdfProgress.setVisibility(View.GONE);
//                webviewpdf.setVisibility(View.VISIBLE);
//                webviewpdf.loadUrl("javascript:(function() { " +
//                        "document.getElementsByClassName('ndfHFb-c4YZDc-GSQQnc-LgbsSe ndfHFb-c4YZDc-to915-LgbsSe VIpgJd-TzA9Ye-eEGnhe ndfHFb-c4YZDc-LgbsSe')[0].style.visibility='hidden'; })()");
//
//            }
//        });
//        webviewpdf.setWebChromeClient(new WebChromeClient());
        ratingAverage.setText(ebook.getReviewdetails().getAverage()+"");
        reviewCount.setText("("+ebook.getReviewdetails().getCount()+" Review )");
        ebookAuthorName.setText(authorDetails.getName());
        authorCategory.setText(authorDetails.getDesignation());
        Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.TUTOR+authorDetails.getPhoto()).placeholder(R.drawable.user).into(authorCircularIcon);
        line_author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), EducatorActivity.class);
                intent.putExtra("id",authorDetails.getId());
                startActivity(intent);
            }
        });

        url=authorDetails.getSocialLink();
        bottom.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent in=new Intent(EbookDetailActivity.this,OrderSummaryActivity.class);
                        OrderSummaryActivity.ebook=ebook;
                        in.putExtra("key","2");
                        startActivity(in);
                    }
                }
        );
        if(ebook.getType().equals("Paid"))
        {
            netPrice.setText(Html.fromHtml("&#8377")+ebook.getOfferprice());
            if(ebook.getDiscountpercent().equals(0))
            {
                cutPrice.setVisibility(View.GONE);

            }
            else
            {
                cutPrice.setText(Html.fromHtml("<strike>&#8377 "+ebook.getPrice()+""+"</strike>"));

            }

        }
        else
        {
            netPrice.setVisibility(View.GONE);
            cutPrice.setVisibility(View.GONE);
            enrollTxt.setText("Get it free");
        }
        /*cutPrice.setText(Html.fromHtml("<strike>&#8377 "+ebook.getPrice()+""+"</strike>"));
        netPrice.setText(Html.fromHtml("&#8377")+ebook.getOfferprice());*/

        readSample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(EbookDetailActivity.this,ReadPdfActivity.class);
                in.putExtra("sample",ebook.getSample());
                in.putExtra("ebookname",ebook.getName());
                in.putExtra("status","1");
                startActivity(in);
            }
        });

        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(EbookDetailActivity.this,ReadPdfActivity.class);
                in.putExtra("sample",ebook.getSample());
                in.putExtra("ebookname",ebook.getName());
                in.putExtra("status","1");
                startActivity(in);
            }
        });


        d=ebook.getWishliststatus();

        if(d.equals("true"))
        {
            addFaveroite.setBackgroundResource(R.drawable.ic_bookmark_black_24dp);
            singleTask.addStatus(d);
        }
        else
        {
            addFaveroite.setBackgroundResource(R.drawable.ic_bookmark_border_black_24dp);
            singleTask.addStatus(d);
        }

    }

    private void initView() {
        image=findViewById(R.id.image);
        authorCircularIcon=findViewById(R.id.author_circular_icon);
        description=findViewById(R.id.description);
        whatilearn=findViewById(R.id.whatilearn);
        thisincludes=findViewById(R.id.includes);
        cardIncludes=findViewById(R.id.cardInclude);
        cardWhatILearn=findViewById(R.id.cardWhatILearn);
        cardDescription=findViewById(R.id.cardDescription);
        singleTask=(SingleTask)getApplication();
        rupee=findViewById(R.id.rupee);
        line_enroll=findViewById(R.id.line_enroll);
        enrollTxt=findViewById(R.id.enroll_txt);
        noReviewTxt=findViewById(R.id.no_review_text);
        readSample=findViewById(R.id.read_sample);
        play_button=findViewById(R.id.play);
        share=findViewById(R.id.share);
        coursereview=findViewById(R.id.course_review);
        addFaveroite=findViewById(R.id.addFaveroite);
        play_demo=findViewById(R.id.play_demo);
        cutPrice=findViewById(R.id.cut_price);
        bottom=findViewById(R.id.bottom);
        netPrice=findViewById(R.id.netPrice);
        ebookTitle=findViewById(R.id.eb_title);
        ebookShortDesc=findViewById(R.id.eb_short_desc);
        shortDis=findViewById(R.id.shortDis);
        line_author=findViewById(R.id.line_author);
        ebookAuthorName=findViewById(R.id.ebook_author_name);
        authorCategory=findViewById(R.id.authorCategory);
        shimmer=findViewById(R.id.ebookShimmerContainer);
        nested=findViewById(R.id.nested);
        shimmerForReview=findViewById(R.id.shimmerForReview);
        reviewCount=findViewById(R.id.review_count);
        ratingAverage=findViewById(R.id.rating_average);
        layout=findViewById(R.id.main_layout);
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        singleTask.remove("status");
    }
/*

    @Override
    protected void onRestart() {
        super.onRestart();
        singleTask.remove("status");
    }

*/

    /*@Override
    protected void onResume() {
        super.onResume();
        singleTask.remove("status");
    }*/
}
