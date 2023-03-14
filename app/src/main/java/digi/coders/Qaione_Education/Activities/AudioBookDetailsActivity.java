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

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import digi.coders.Qaione_Education.Adapters.PlayListAdapter;
import digi.coders.Qaione_Education.Adapters.RatingAdapter;
import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Abookdetails;
import digi.coders.Qaione_Education.models.AuthorDetails;
import digi.coders.Qaione_Education.models.Review;
import digi.coders.Qaione_Education.models.Reviewdetails;
import digi.coders.Qaione_Education.models.TopicListModel;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.models.WishlistItem;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AudioBookDetailsActivity extends AppCompatActivity {

    RecyclerView coursereview, recycler_view;
    Button back;
    TextView ebookAuthorName,authorCategory,noReviewTxt,enrollTxt,rupee;
    private NestedScrollView nested;
    LinearLayout bottom, line_enroll, line_author;
    private String abookId,url;
    ElasticButton addFaveroite;
    public static WishlistItem items;
    private RelativeLayout layout;
    private ShimmerFrameLayout shimmer,shimmerForReview;
    private SingleTask singleTask;
    private String d;
    ImageView image, share;
    private CircleImageView authorCircularIcon;
    private TextView cutPrice,netPrice,ebookTitle,ebookShortDesc,ratingAverage,reviewCount, no_playlist_text;
    private Abookdetails abookdetails;

    WebView thisincludes,whatilearn,description;
    CardView cardIncludes,cardWhatILearn, cardDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_book);

        initView();
        abookId=getIntent().getStringExtra("abookId");
        loadAbookDetails();
        loadWishlist();
        netPrice.setText(Html.fromHtml("&#8377")+"2000.00");
        cutPrice.setText(Html.fromHtml("<strike>&#8377 2600.00</strike>"));

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

        share=findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, "आइए भारत की विश्वव्यापी संस्कृत  संस्कृति और संस्कारों को  कर्मसू , एप के माध्यम से जन जन तक पहुंचाने का मार्ग प्रशस्त करे \n" +
                        "DOWNLOAD करें KARMASU APP और पाएं महान धार्मिक संतो एवम् विद्वानों की वाणी और अनेक धार्मिक ग्रंथों , मंत्रो एवम् अनेक प्रकार के संस्कृत विश्वविद्यालय कोर्स जो आपको और आपके साथ जुड़े लोगों को अपने धर्म के बारे में बताने का प्रयास करेगा \n" +
                        "\n" +
                        "DOWNLOAD करिए KARMASU APP \uD83D\uDC47\n" +
                        "\n" +
                        "https://play.google.com/store/apps/details?id=digi.coders.karmasu\n" +
                        "\n" +
                        "आपके द्वारा शेयर किया गया एक लिंक हमे अन्य लोगों के साथ जोड़ सकता है");
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                startActivity(Intent.createChooser(intent,"Share via"));

            }
        });
        rupee.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        line_enroll.setVisibility(View.VISIBLE);
                    }
                }
        );

    }

    private void loadReview() {
        Reviewdetails reviewdetaill=abookdetails.getReviewdetails();

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
                            Toast.makeText(AudioBookDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();

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

        Call<JsonArray> call=myApi.addWishlist(user.getId(),abookId,"Abook");
        Log.e("as",abookId);
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
                        Toast.makeText(AudioBookDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    private void loadAbookDetails() {
        MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        String json = singleTask.getValue("user");
        User user = new Gson().fromJson(json, User.class);
        Call<JsonArray> call;

        if (user != null)
        {
            call = myApi.getAudioBookFullDetails(abookId,user.getId(),user.getNumber());
        }
        else
        {
            call = myApi.getAudioBookFullDetails(abookId,"","");
        }

//        Log.e("sad",ebookId);
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
            Log.i("resrkguyfhs", response.body().toString());
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
                abookdetails =new Gson().fromJson(jsonArray1.getJSONObject(0).toString(), Abookdetails.class);

                setResponseInThis(abookdetails);
                Log.i("tuerteqruytiye", "1");
            }
            else
            {
                Toast.makeText(AudioBookDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,HomeActivity.class));
                finish();
            }


        } catch (JSONException e) {
            Log.i("tuerteqruytiye", e.getMessage().toString());
            e.printStackTrace();
        }
    }

    private void setResponseInThis(Abookdetails abookdetails) {
        OrderSummaryActivity.abookdetails=abookdetails;
        AuthorDetails authorDetails=abookdetails.getAuthordetails();
        ebookTitle.setText(abookdetails.getName());
        ebookShortDesc.setText(abookdetails.getShortdesc());
        Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.ABOOK+abookdetails.getBanner()).into(image);

        String pdfUrl=Constraints.BASE_IMAGE_URL+Constraints.EBOOK+abookdetails.getSample();
        Log.e("sdsd",pdfUrl+"");

        if (abookdetails.getAbookInclude().equals("")){
            cardIncludes.setVisibility(View.GONE);
        }
        if (abookdetails.getWillLearn().equals("")){
            cardWhatILearn.setVisibility(View.GONE);
        }
        if (abookdetails.getDescription().equals("")){
            cardDescription.setVisibility(View.GONE);
        }

        description.loadData(abookdetails.getDescription(), "text/html", "UTF-8");
        whatilearn.loadData(abookdetails.getWillLearn(), "text/html", "UTF-8");
        thisincludes.loadData(abookdetails.getAbookInclude(), "text/html", "UTF-8");
        ratingAverage.setText(abookdetails.getReviewdetails().getAverage()+"");
        reviewCount.setText("("+abookdetails.getReviewdetails().getCount()+" Review )");
        ebookAuthorName.setText(authorDetails.getName());
        authorCategory.setText(authorDetails.getDesignation());
        Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.ABOOK+authorDetails.getPhoto()).placeholder(R.drawable.user).into(authorCircularIcon);
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
                        Intent in=new Intent(AudioBookDetailsActivity.this,OrderSummaryActivity.class);
                        OrderSummaryActivity.abookdetails=abookdetails;
                        in.putExtra("key","2");
                        startActivity(in);
                    }
                }
        );
        if(abookdetails.getType().equals("Paid"))
        {
            netPrice.setText(Html.fromHtml("&#8377")+abookdetails.getOfferprice());
            if(abookdetails.getDiscountpercent().equals(0))
            {
                cutPrice.setVisibility(View.GONE);

            }
            else
            {
                cutPrice.setText(Html.fromHtml("<strike>&#8377 "+abookdetails.getPrice()+""+"</strike>"));

            }

        }
        else
        {
            netPrice.setVisibility(View.GONE);
            cutPrice.setVisibility(View.GONE);
            enrollTxt.setText("Get it free");
        }

        List<TopicListModel> list=new ArrayList<>();
        TopicListModel[] s=abookdetails.getTopicListModels();

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
                PlayListAdapter playListAdapter =new PlayListAdapter(this, list,abookdetails,status);
                recycler_view.setAdapter(playListAdapter);
            }
        }

        d=abookdetails.getWishliststatus();

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
        singleTask=(SingleTask)getApplication();

        image=findViewById(R.id.image);
        authorCircularIcon=findViewById(R.id.author_circular_icon);
        description=findViewById(R.id.description);
        whatilearn=findViewById(R.id.whatilearn);
        thisincludes=findViewById(R.id.includes);
        cardIncludes=findViewById(R.id.cardInclude);
        cardWhatILearn=findViewById(R.id.cardWhatILearn);
        cardDescription=findViewById(R.id.cardDescription);
        rupee=findViewById(R.id.rupee);
        line_enroll=findViewById(R.id.line_enroll);
        enrollTxt=findViewById(R.id.enroll_txt);
        noReviewTxt=findViewById(R.id.no_review_text);
        coursereview=findViewById(R.id.course_review);
        addFaveroite=findViewById(R.id.addFaveroite);
        cutPrice=findViewById(R.id.cut_price);
        bottom=findViewById(R.id.bottom);
        netPrice=findViewById(R.id.netPrice);
        ebookTitle=findViewById(R.id.eb_title);
        ebookShortDesc=findViewById(R.id.eb_short_desc);
        ebookAuthorName=findViewById(R.id.ebook_author_name);
        line_author=findViewById(R.id.line_author);
        authorCategory=findViewById(R.id.authorCategory);
        shimmer=findViewById(R.id.ebookShimmerContainer);
        nested=findViewById(R.id.nested);
        shimmerForReview=findViewById(R.id.shimmerForReview);
        reviewCount=findViewById(R.id.review_count);
        ratingAverage=findViewById(R.id.rating_average);
        layout=findViewById(R.id.main_layout);

        no_playlist_text=findViewById(R.id.no_playlist_text);
        recycler_view=findViewById(R.id.recycler_view);

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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        singleTask.remove("status");
    }
}