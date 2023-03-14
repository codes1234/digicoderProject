package digi.coders.Qaione_Education.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.skydoves.elasticviews.ElasticFloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.LiveSession;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.models.UserJoinLiveData;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveClassJoinActivity extends AppCompatActivity {

    private String liveId;
    private Button back;
    private EditText uname,uemail,umobile;
    private ElasticFloatingActionButton submit;
    private ImageView thumbnil;
    public static LiveSession liveSession;
    private TextView tag,title,time,desc,authorName;
    private CircleImageView authorImage;
    private SingleTask singleTask;
    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_class_join);
        initView();
        setLiveData();
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Log.e("dsf",liveId+"");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (user != null) {
//            if (user.getProfilePhoto() != "") {
//                Picasso.get().load(Constraints.BASE_IMAGE_URL + Constraints.PROFILE_PHOTO + user.getProfilePhoto()).placeholder(shimmerDrawable).into(imageView);
//            }
            uname.setText(user.getName());
            uemail.setText(user.getEmail());
            umobile.setText(user.getCourse());
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(valid())
                {
                    progressDialog.show();
                    MyApi myApi= RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
                    Call<JsonArray> call=myApi.liveJoin(liveSession.getId(),name,email,mobile);
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
                                        JSONArray jsonArray1=jsonObject.getJSONArray("data");
                                        UserJoinLiveData user=new Gson().fromJson(jsonArray1.getJSONObject(0).toString(),UserJoinLiveData.class);
                                        progressDialog.hide();
                                        LiveSessionDetailsActivity.liveSession=liveSession;
                                        startActivity(new Intent(LiveClassJoinActivity.this,LiveSessionDetailsActivity.class));
                                        finish();

                                        /*userId.setText(user.getUserid());
                                        password.setText(user.getPassword());*/
                                        // show the popup window
                                        // which view you pass in doesn't matter, it is only used for the window tolken
                                    }
                                    Toast.makeText(LiveClassJoinActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    progressDialog.hide();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonArray> call, Throwable t) {
                            progressDialog.hide();
                        }
                    });



                }
            }
        });
    }

    private String name,email,mobile;
    private boolean valid() {
        name = uname.getText().toString();
        email = uemail.getText().toString();
        mobile = umobile.getText().toString();
        if (TextUtils.isEmpty(name)) {
            uname.setError("Please Enter your name");
            uname.requestFocus();
            return false;
        }
        else if (TextUtils.isEmpty(email))
        {

            uemail.setError("Please Enter your email");
            uemail.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(mobile))
        {
            umobile.setError("Please Enter your mobile no");
            umobile.requestFocus();

            return false;
        }

        else
        {
            return true;
        }

    }


    private void setLiveData() {

        //tag.setText(liveSession.getTags());
        Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
                .setDuration(1800) // how long the shimmering animation takes to do one full sweep
                .setBaseAlpha(0.7f) //the alpha of the underlying children
                .setHighlightAlpha(0.6f) // the shimmer alpha amount
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build();
        ShimmerDrawable shimmerDrawable=new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);
//        Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.LIVE_VIDEO+liveSession.getThumbnail()).placeholder(shimmerDrawable).into(thumbnil);
//        Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.TUTOR+liveSession.getAuthor().getPhoto()).placeholder(shimmerDrawable).into(authorImage);
//        time.setText("Started at "+liveSession.getTiming()+" , "+liveSession.getDuration()+" mins");
//        title.setText(liveSession.getTitle());
//        authorName.setText(liveSession.getAuthor().getName());
    }

    private void initView() {

            back=findViewById(R.id.back);
            uname=findViewById(R.id.uname);
            uemail=findViewById(R.id.uemail);
            umobile=findViewById(R.id.umobile);
            submit=findViewById(R.id.submit_detail);
            thumbnil=findViewById(R.id.live_thumbnil);
            tag=findViewById(R.id.live_tags);
            title=findViewById(R.id.live_title);
            desc=findViewById(R.id.live_desc);
            time=findViewById(R.id.live_time);
            authorImage=findViewById(R.id.live_author_image);
            authorName=findViewById(R.id.live_author_name);
            singleTask=(SingleTask)getApplication();

    }

}