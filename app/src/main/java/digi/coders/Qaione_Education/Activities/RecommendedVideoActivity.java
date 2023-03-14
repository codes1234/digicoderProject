package digi.coders.Qaione_Education.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.RecommendedVideos;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendedVideoActivity extends AppCompatActivity {

    private Button back;
    private TextView toolbarTitle;
    private String id;
    private SingleTask singleTask;
    private YouTubePlayer youTubePlaye;
    private YouTubePlayerView yPlayerView;
    private SimpleExoPlayer player;
    private ScrollView scrollView;
    String link="";
    boolean fullscreen = false;
    private PlayerView playerView;
    private LinearLayout titleLayout;
    private LinearLayout desc;
    private ProgressBar recommendDesprogress;
    ImageView fullscreenButton;
    private WebView videoDescription;
    private ProgressBar videoLoader;
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_video);
        initView();
        desc=findViewById(R.id.descript);
        id=getIntent().getStringExtra("id");
        type=getIntent().getStringExtra("type");
        if(type.equals("Internal")) {
            link = Constraints.BASE_IMAGE_URL + Constraints.DEMO_VIDEO + getIntent().getStringExtra("link");
        } else {
            link =getIntent().getStringExtra("link");
        }
        Log.e("sdsd",id+"");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loadRecommendedVideo();
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
        player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fullscreen) {
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(RecommendedVideoActivity.this, R.drawable.ic_fullscreen_open));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    titleLayout.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.VISIBLE);
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().show();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    params.height = (int) (200 * getApplicationContext().getResources().getDisplayMetrics().density);
                    playerView.setLayoutParams(params);
                    playerView.setVisibility(View.VISIBLE);
                    fullscreen = false;
                } else {
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(RecommendedVideoActivity.this, R.drawable.ic_fullscreen_close));
                    scrollView.setVisibility(View.GONE);
                    titleLayout.setVisibility(View.GONE);
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

                    if (getSupportActionBar() != null) {
                        getSupportActionBar().hide();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = ConstraintLayout.LayoutParams.MATCH_PARENT;
                    params.height = ConstraintLayout.LayoutParams.MATCH_PARENT;
                    playerView.setLayoutParams(params);
                    fullscreen = true;
                }
            }
        });
        playerView.setPlayer(player);

        if(type.equals("External"))
        {

            yPlayerView.setVisibility(View.VISIBLE);
            playerView.setVisibility(View.GONE);
            final String[] str=link.split("/");
            yPlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady( YouTubePlayer youTubePlayer) {
                    super.onReady(youTubePlayer);
                    youTubePlaye=youTubePlayer;
                    youTubePlayer.cueVideo(str[str.length-1],0);
                    youTubePlayer.play();
                    //youTubePlayer.loadVideo(str[str.length-1],0);


                }
            });
            yPlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
                @Override
                public void onYouTubePlayerEnterFullScreen() {

                    View decorView = getWindow().getDecorView();

                    decorView.setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_IMMERSIVE
                                    // Set the content to appear under the system bars so that the
                                    // content doesn't resize when the system bars hide and show.
                                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    // Hide the nav bar and status bar
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = ConstraintLayout.LayoutParams.MATCH_PARENT;
                    params.height = ConstraintLayout.LayoutParams.MATCH_PARENT;
                    scrollView.setVisibility(View.GONE);
                    titleLayout.setVisibility(View.GONE);
                    yPlayerView.setLayoutParams(params);
                    fullscreen=true;
                }

                @Override
                public void onYouTubePlayerExitFullScreen() {
                    scrollView.setVisibility(View.VISIBLE);
                    titleLayout.setVisibility(View.VISIBLE);
                    Log.e("es","exit");
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(RecommendedVideoActivity.this, R.drawable.ic_fullscreen_open));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    titleLayout.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.VISIBLE);
                    videoDescription.setVisibility(View.VISIBLE);
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().show();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    params.height = (int) (220 * getApplicationContext().getResources().getDisplayMetrics().density);
                    yPlayerView.setLayoutParams(params);
                    loadRecommendedVideo();
                    fullscreen=false;


                }
            });
        }
        else
        {

            yPlayerView.setVisibility(View.GONE);
            playerView.setVisibility(View.VISIBLE);
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), Util.getUserAgent(getApplicationContext(), getApplicationContext().getString(R.string.app_name)));
            Log.e("link",link+"");
            if (link != null) {
                Log.e("link1",link+"");

                MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(link));
                player.prepare(videoSource);
                player.setPlayWhenReady(true);
            }

        }

    }

    private void loadRecommendedVideo() {
        MyApi myApi= RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call=myApi.getRecommendedVideos(id);
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
                            RecommendedVideos recommendedVideo=new Gson().fromJson(jsonArray1.getJSONObject(0).toString(),RecommendedVideos.class);
                            Log.e("titile" ,recommendedVideo.getVideo().getTitle());
                            toolbarTitle.setText(recommendedVideo.getVideo().getTitle());
                            type=recommendedVideo.getVideo().getType();
                            recommendDesprogress.setVisibility(View.GONE);
                            videoDescription.loadData(recommendedVideo.getDescription(),"text/html","UTF-8");

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

    public class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            recommendDesprogress.setVisibility(View.GONE);
            videoDescription.setVisibility(View.VISIBLE);
            videoDescription.loadUrl("javascript:(function() { " +
                    "document.getElementsByClassName('ndfHFb-c4YZDc-GSQQnc-LgbsSe ndfHFb-c4YZDc-to915-LgbsSe VIpgJd-TzA9Ye-eEGnhe ndfHFb-c4YZDc-LgbsSe')[0].style.visibility='hidden'; })()");
        }
    }
    private void initView() {
        back=findViewById(R.id.back_but);
        toolbarTitle=findViewById(R.id.toolbar_tex);
        playerView=findViewById(R.id.player);
        singleTask=(SingleTask)getApplication();
        player = ExoPlayerFactory.newSimpleInstance(getApplicationContext());
        fullscreenButton = playerView.findViewById(R.id.exo_fullscreen_icon);
        scrollView=findViewById(R.id.scroll_view1);
        titleLayout=findViewById(R.id.line1);
        yPlayerView=findViewById(R.id.youtube_video_player);
        videoDescription=findViewById(R.id.video_description);
        recommendDesprogress=findViewById(R.id.recommend_des_progress);

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);

    }
    @Override
    public void onPause() {
        super.onPause();
        player.setPlayWhenReady(false);

        if(youTubePlaye!=null)
        {
            youTubePlaye.pause();
        }
        else {
            yPlayerView.release();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.release();
        yPlayerView.release();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(type.equals("Internal")) {
            if (fullscreen) {
                fullscreenButton.setImageDrawable(ContextCompat.getDrawable(RecommendedVideoActivity.this, R.drawable.ic_fullscreen_open));

                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

                if (getSupportActionBar() != null) {
                    getSupportActionBar().show();
                }

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
                params.width = params.MATCH_PARENT;
                params.height = (int) (200 * getApplicationContext().getResources().getDisplayMetrics().density);
                playerView.setVisibility(View.VISIBLE);
                playerView.setLayoutParams(params);
                titleLayout.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.VISIBLE);
                desc.setVisibility(View.VISIBLE);
                fullscreen = false;
            }
            else
            {
                finish();
                /*fullscreenButton.setImageDrawable(ContextCompat.getDrawable(RecommendedVideoActivity.this, R.drawable.ic_fullscreen_open));

                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

                if (getSupportActionBar() != null) {
                    getSupportActionBar().show();
                }

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
                params.width = params.MATCH_PARENT;
                params.height = (int) (200 * getApplicationContext().getResources().getDisplayMetrics().density);
                playerView.setVisibility(View.VISIBLE);
                playerView.setLayoutParams(params);
                titleLayout.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.VISIBLE);
                desc.setVisibility(View.VISIBLE);
                fullscreen = false;*/
            }
        }
        else
        {
            if(fullscreen) {
                fullscreenButton.setImageDrawable(ContextCompat.getDrawable(RecommendedVideoActivity.this, R.drawable.ic_fullscreen_open));
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    /*titleLayout.setVisibility(View.VISIBLE);
                    openDescription.setVisibility(View.VISIBLE);
                    assign.setVisibility(View.VISIBLE);
                    postQas.setVisibility(View.VISIBLE);
                    assignmentList.setVisibility(View.VISIBLE);
                    questionLayout.setVisibility(View.VISIBLE);
                    questionLayout.setVisibility(View.VISIBLE);*/
                titleLayout.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.VISIBLE);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().show();
                }
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = (int) (210 * getApplicationContext().getResources().getDisplayMetrics().density);
                yPlayerView.setLayoutParams(params);
                fullscreen = false;
            }
            else
            {
                finish();
            }
        }

    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if(type.equals("Internal"))
            {
                fullscreenButton.setImageDrawable(ContextCompat.getDrawable(RecommendedVideoActivity.this, R.drawable.ic_fullscreen_close));
                scrollView.setVisibility(View.GONE);
                titleLayout.setVisibility(View.GONE);
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

                if (getSupportActionBar() != null) {
                    getSupportActionBar().hide();
                }
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
                params.width = ConstraintLayout.LayoutParams.MATCH_PARENT;
                params.height = ConstraintLayout.LayoutParams.MATCH_PARENT;
                playerView.setLayoutParams(params);

                fullscreen = true;

            }
            else
            {
                View decorView = getWindow().getDecorView();

                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_IMMERSIVE
                                // Set the content to appear under the system bars so that the
                                // content doesn't resize when the system bars hide and show.
                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                // Hide the nav bar and status bar
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN);
                //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
                params.width = ConstraintLayout.LayoutParams.MATCH_PARENT;
                params.height = ConstraintLayout.LayoutParams.MATCH_PARENT;
                scrollView.setVisibility(View.GONE);
                titleLayout.setVisibility(View.GONE);
                yPlayerView.setLayoutParams(params);
                fullscreen=true;


            }
        } else {


            if(type.equals("Internal"))
            {
                fullscreenButton.setImageDrawable(ContextCompat.getDrawable(RecommendedVideoActivity.this, R.drawable.ic_fullscreen_open));
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                titleLayout.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.VISIBLE);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().show();
                }
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = (int) (200 * getApplicationContext().getResources().getDisplayMetrics().density);
                playerView.setLayoutParams(params);
                playerView.setVisibility(View.VISIBLE);
                fullscreen = false;

            }
            else
            {
                scrollView.setVisibility(View.VISIBLE);
                titleLayout.setVisibility(View.VISIBLE);
                desc.setVisibility(View.VISIBLE);
                fullscreenButton.setImageDrawable(ContextCompat.getDrawable(RecommendedVideoActivity.this, R.drawable.ic_fullscreen_open));
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                titleLayout.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.VISIBLE);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().show();
                }
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = (int) (210 * getApplicationContext().getResources().getDisplayMetrics().density);
                yPlayerView.setLayoutParams(params);
                fullscreen=false;
            }

        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        if(youTubePlaye!=null)
        {
            youTubePlaye.pause();
        }
        else {
            yPlayerView.release();
        }

    }
}