package digi.coders.Qaione_Education.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import digi.coders.Qaione_Education.Helper.InternetCheck;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private SingleTask singleTask;
    LinearLayout splashLayout;
    TextView title;
    ImageView image;
//    PlayerView playerView;
    SimpleExoPlayer simpleExoPlayer;
    FloatingActionButton skip;
    Button login, createAccount;


//    Handler handler = new Handler();
//    Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//
//            if(new InternetCheck().isNetworkAvailable(getApplicationContext()))
//            {
//
//                String jsonObject = singleTask.getValue("user");
//                User user = new Gson().fromJson(jsonObject, User.class);
//                if (user != null) {
//                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
//                    finish();
//                }
//
//                else {
//                    startActivity(new Intent(getApplicationContext(), StartActivity.class));
//                    finish();
//
//                }
//            } else {
//                startActivity(new Intent(MainActivity.this,NoInternetActivity.class));
//            }
//        }
//    };

    int currentPosition = 0;

    private RelativeLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //handler.postDelayed(runnable, 60000);
        mainLayout=findViewById(R.id.main);
        splashLayout = findViewById(R.id.splash_layout);
        title = findViewById(R.id.title);
        image = findViewById(R.id.image);
        singleTask = (SingleTask) getApplication();
//        playerView=findViewById(R.id.player_view);
        skip=findViewById(R.id.skip);
        login=findViewById(R.id.login);
        createAccount=findViewById(R.id.createAccount);

//        SplashScreen();
        skip.setVisibility(View.VISIBLE);
        login.setVisibility(View.GONE);
        createAccount.setVisibility(View.GONE);

        if(new InternetCheck().isNetworkAvailable(getApplicationContext()))
        {

            String jsonObject = singleTask.getValue("user");
            User user = new Gson().fromJson(jsonObject, User.class);
            if (user != null) {
                skip.setVisibility(View.VISIBLE);
                login.setVisibility(View.GONE);
                createAccount.setVisibility(View.GONE);
//                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
//                        finish();
            }

            else {
                skip.setVisibility(View.GONE);
                login.setVisibility(View.VISIBLE);
                createAccount.setVisibility(View.VISIBLE);
//                        startActivity(new Intent(getApplicationContext(), StartActivity.class));
//                        finish();

            }
        } else {
            startActivity(new Intent(MainActivity.this,NoInternetActivity.class));
        }

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    simpleExoPlayer.setPlayWhenReady(false);
                } catch (Exception e){

                }
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    simpleExoPlayer.setPlayWhenReady(false);
                } catch (Exception e){

                }
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    simpleExoPlayer.setPlayWhenReady(false);
                } catch (Exception e){

                }
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
            }
        });


    }

/*
    public void SplashScreen (){
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.show();
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi= RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call=myApi.SplashScreen();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                try{

                    Log.i("splash", response.body().toString());

                    JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    String status=jsonObject.getString("res");
                    if(status.equalsIgnoreCase("success")){

                        JSONObject jsonObject1=jsonObject.getJSONObject("data");
                        title.setText(jsonObject1.getString("title"));
                        if (jsonObject1.getString("type").equals("Image")){
                            image.setVisibility(View.VISIBLE);
                            playerView.setVisibility(View.GONE);
                            Picasso.get().load(jsonObject1.getString("screen")).into(image);
                        }else if(jsonObject1.getString("type").equals("Video")){
                            image.setVisibility(View.GONE);
//                            simpleExoPlayer= ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector((TrackSelection.Factory) new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter())), new DefaultLoadControl());
                            simpleExoPlayer=  ExoPlayerFactory.newSimpleInstance(getApplicationContext());
                            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(jsonObject1.getString("screen")), new DefaultHttpDataSourceFactory("exoplayer_video"), new DefaultExtractorsFactory(), (Handler) null, (ExtractorMediaSource.EventListener) null);
                            playerView.setPlayer(simpleExoPlayer);
                            playerView.setKeepScreenOn(true);
                            simpleExoPlayer.prepare(mediaSource);
                            simpleExoPlayer.setPlayWhenReady(true);
                            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                                    .setUsage(C.USAGE_MEDIA)
                                    .setContentType(C.CONTENT_TYPE_MOVIE)
                                    .build();
                            simpleExoPlayer.setAudioAttributes(audioAttributes,true);

                        }


                    }else {
                        //Toast.makeText(getApplicationContext(),jsonObject.getString("msg"), Toast.LENGTH_LONG).show();

                    }
                }catch (Exception e){
                    //Toast.makeText(getApplicationContext(), e.getMessage().toString(),Toast.LENGTH_LONG).show();
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
*/

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
}
