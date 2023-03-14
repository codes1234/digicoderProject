package digi.coders.Qaione_Education.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import digi.coders.Qaione_Education.Adapters.AssignmentListAdapter;
import digi.coders.Qaione_Education.Adapters.CourseDetailsAdapter;
import digi.coders.Qaione_Education.Adapters.QuestionAnswerAdapter;
import digi.coders.Qaione_Education.Helper.Constraints;
import digi.coders.Qaione_Education.Helper.FilePath;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.Assignment;
import digi.coders.Qaione_Education.models.Coursedetails;
import digi.coders.Qaione_Education.models.Courses;
import digi.coders.Qaione_Education.models.Question;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.models.VideoDetails;
import digi.coders.Qaione_Education.models.VideoPlaylist;
import digi.coders.Qaione_Education.singletask.SingleTask;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static digi.coders.Qaione_Education.R.drawable.arrow_down_for_video;
import static digi.coders.Qaione_Education.R.drawable.forward_arrow;

public class PlayVideoActivity extends AppCompatActivity {

    private AssignmentListAdapter.MyHolder myHolder;

    private RecyclerView course_details, course_details2;
    private TextView noVideosText, noVideosText2;

    public static VideoDetails videoDetails;
    SimpleExoPlayer player;
    PlayerView playerView;
    ImageView fullscreenButton, assignImg, quesImg;
    boolean fullscreen = false;
    String link = "";
    ProgressBar progressBar;
    private TextView subject, video_name, descOfVideo, toolbarText, postQues, author_name, authorCategory;
    CircleImageView author_circular_icon;
    private LinearLayout assign, postQas, titleLayout;
    private RelativeLayout questionLayout;
    private Button back;
    private YouTubePlayerView yPlayerView;
    private SingleTask singleTask;
    private YouTubePlayer youTube;
    private Coursedetails coursedetails;
    private String courseid, videoId;
    private MultipartBody.Part assignmentAnswer;
    private RelativeLayout noAssignmentText,assignmentLayout;
    public static Courses coursesw;
    private RecyclerView assignmentList, questionAnsList;
    private VideoPlaylist videoPlaylist;
    private CircleImageView userImage;
    private List<Assignment[]> playlistList;
    int i = 0;
    String type;
    private ImageView downloadNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        setInitView();
        //yPlayerView.enableBackgroundPlayback(false);
        /*courseid=getIntent().getStringExtra("courseid");
        videoId=videoDetails.getId();
        type=videoDetails.getType();*/
        courseid = getIntent().getStringExtra("courseid");
        videoId = getIntent().getStringExtra("videoid");
        type = getIntent().getStringExtra("type");
        if (type.equals("Internal")) {
            link = Constraints.BASE_IMAGE_URL + Constraints.DEMO_VIDEO + getIntent().getStringExtra("link");
        } else {
            link = getIntent().getStringExtra("link");
        }
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        if(!user.getProfilePhoto().isEmpty())
        {
            Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.PROFILE_PHOTO+user.getProfilePhoto()).placeholder(R.drawable.user).into(userImage);
        }

        fullscreenButton = playerView.findViewById(R.id.exo_fullscreen_icon);
        player = ExoPlayerFactory.newSimpleInstance(getApplicationContext());

        loadAssignment();
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);

        postQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                ProgressDialog progressDialog = new ProgressDialog(PlayVideoActivity.this,R.style.CustomProgressBarTheme);
                progressDialog.setMessage("Waiting");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PlayVideoActivity.this, R.style.myBottomSheetDialogTheme);
                View view1 = LayoutInflater.from(PlayVideoActivity.this).inflate(R.layout.post_question_answer_bottom_layout, (RelativeLayout) findViewById(R.id.bottom_sheet_container), false);
                EditText text = view1.findViewById(R.id.question);
                ImageView send = view1.findViewById(R.id.sendText);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressDialog.show();
                        String message = text.getText().toString();
                        String js = singleTask.getValue("user");
                        User user = new Gson().fromJson(js, User.class);
                        MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
                        if (TextUtils.isEmpty(message)) {
                            Toast.makeText(PlayVideoActivity.this, "Please Write your Question here....", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("sdsws", courseid + user.getId() + videoPlaylist.getVideo().getId());

                            Call<JsonArray> call = myApi.postQuestion(courseid, user.getId(), videoPlaylist.getVideo().getId(), message, "");
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

                                                Toast.makeText(PlayVideoActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                loadAssignment();
                                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                                }
                                                bottomSheetDialog.dismiss();
                                                progressDialog.hide();

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                }

                                @Override
                                public void onFailure(Call<JsonArray> call, Throwable t) {
                                    Log.e("sdsd", t.getMessage());
                                    progressDialog.show();
                                }
                            });


                        }


                    }

                });
                text.requestFocus();
                bottomSheetDialog.setContentView(view1);
                bottomSheetDialog.show();

            }

        });

        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) {
                    assignmentLayout.setVisibility(View.VISIBLE);
                    assignmentList.setVisibility(View.VISIBLE);

                    if (!courseid.isEmpty()) {
                        loadAssignment();
                    }
                    else
                    {
                        noAssignmentText.setVisibility(View.VISIBLE);

                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        assignImg.setImageDrawable(getDrawable(arrow_down_for_video));
                    }
                    i++;
                } else if (i == 1) {
                    assignmentLayout.setVisibility(View.GONE);
                    assignmentList.setVisibility(View.GONE);
                    noAssignmentText.setVisibility(View.GONE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        assignImg.setImageDrawable(getDrawable(forward_arrow));
                    }

                    i = 0;
                }

            }
        });

        postQas.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                if (i == 0) {
                    questionLayout.setVisibility(View.GONE);
//                    quesTxt.setTextColor(Color.BLACK);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        quesImg.setImageDrawable(getDrawable(forward_arrow));
                    }
                    i++;
                } else if (i == 1) {
                    questionLayout.setVisibility(View.VISIBLE);
//                    quesTxt.setTextColor(Color.BLUE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        quesImg.setImageDrawable(getDrawable(arrow_down_for_video));

                    }
                    i = 0;

                }


            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //loadVideoDetails();
        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fullscreen) {
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(PlayVideoActivity.this, R.drawable.ic_fullscreen_open));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    titleLayout.setVisibility(View.VISIBLE);
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
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(PlayVideoActivity.this, R.drawable.ic_fullscreen_close));
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

        if (type.equals("External")) {
            yPlayerView.setVisibility(View.VISIBLE);
            playerView.setVisibility(View.GONE);
            final String[] str = link.split("/");

            yPlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(YouTubePlayer youTubePlayer) {
                    super.onReady(youTubePlayer);
                    youTube=youTubePlayer;
                    youTubePlayer.cueVideo(str[str.length - 1], 0);
                    //youTubePlayer.loadVideo(str[str.length - 1], 0);
                    youTubePlayer.play();


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
                    titleLayout.setVisibility(View.GONE);
                    yPlayerView.setLayoutParams(params);
                    fullscreen = true;
                }

                @Override
                public void onYouTubePlayerExitFullScreen() {
                    titleLayout.setVisibility(View.VISIBLE);
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(PlayVideoActivity.this, R.drawable.ic_fullscreen_open));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    titleLayout.setVisibility(View.VISIBLE);
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
            });
        } else {

            yPlayerView.setVisibility(View.GONE);
            playerView.setVisibility(View.VISIBLE);
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), Util.getUserAgent(getApplicationContext(), getApplicationContext().getString(R.string.app_name)));
            Log.e("link", link + "");
            if (link != null) {
                Log.e("link1", link + "");

                String sds="https://www.codersadda.com/uploads/video/Motivational.mp4";
                MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(link));
                player.prepare(videoSource);
                player.setPlayWhenReady(true);
            }

        }
    }

    private void setInitView() {
        userImage=findViewById(R.id.user_image);
        yPlayerView = findViewById(R.id.youtube_video_player);
        playerView = findViewById(R.id.player);
        toolbarText = findViewById(R.id.toolbar_tex);
        questionAnsList = findViewById(R.id.question_list);
        titleLayout = findViewById(R.id.line1);
        author_name = findViewById(R.id.author_name);
        authorCategory = findViewById(R.id.authorCategory);
        author_circular_icon = findViewById(R.id.author_circular_icon);
        assignImg = findViewById(R.id.assign_im);
        quesImg = findViewById(R.id.question_im);
        postQues = findViewById(R.id.post_ques);
        assignmentList = findViewById(R.id.assignment_list);
        singleTask = (SingleTask) getApplication();
        back = findViewById(R.id.back_but);
        progressBar = findViewById(R.id.progressBar);
        subject = findViewById(R.id.subject);
        video_name = findViewById(R.id.video_name);
        descOfVideo = findViewById(R.id.video_description);
        assignmentLayout=findViewById(R.id.assignment_layout);
        // assignmentLayout=findViewById(R.id.assignment_layout);
        questionLayout = findViewById(R.id.post_question);
        assign = findViewById(R.id.assignment);
        postQas = findViewById(R.id.postQas);
        noAssignmentText=findViewById(R.id.no_assignment_lay);
        course_details=findViewById(R.id.course_detail);
        course_details2=findViewById(R.id.course_detail2);
        noVideosText=findViewById(R.id.no_videos_text);
        noVideosText2=findViewById(R.id.no_videos_text2);
        downloadNotes=findViewById(R.id.imageDownloadNotes);

    }

    private void loadQuestionAndReply() {
        Question[] e = videoPlaylist.getQuestion();
        Log.e("sadsd", e.length + "");
        //QuestionAnswerAdapter.videoPlaylist = videoPlaylist;
        questionAnsList.setLayoutManager(new LinearLayoutManager(PlayVideoActivity.this, LinearLayoutManager.VERTICAL, false));
        questionAnsList.setAdapter(new QuestionAnswerAdapter(e, singleTask));


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadAssignment();
        courseid = getIntent().getStringExtra("courseid");
        videoId = getIntent().getStringExtra("videoid");
        type = getIntent().getStringExtra("type");
        if (type.equals("Internal")) {
            link = Constraints.BASE_IMAGE_URL + Constraints.DEMO_VIDEO + getIntent().getStringExtra("link");
        } else {
            link = getIntent().getStringExtra("link");
        }

    }

//    private void loadVideoDetails() {
//        if (videoDetails != null) {
//
//            AuthorDetails authorDetails=videoDetails.getAuthorDetails();
//            author_name.setText(authorDetails.getName());
//            about.setText(authorDetails.getDesignation()+" , "+authorDetails.getPosition());
//            Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.TUTOR+authorDetails.getPhoto()).into(author_circular_icon);
//
//            subject.setText(videoDetails.getSubject());
//            video_name.setText(videoDetails.getTitle());
//            descOfVideo.setText(videoDetails.getDescription());
//            toolbarText.setText(videoDetails.getTitle());
//      }
//    }

    private void loadAssignment() {
        String js = singleTask.getValue("user");
        User user = new Gson().fromJson(js, User.class);
        MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call;
        if (courseid == null) {
            call = myApi.getVideoPlaylist(coursedetails.getId(), user.getId(), videoId);
        } else {
            call = myApi.getVideoPlaylist(courseid, user.getId(), videoId);
        }

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.i("yreywry", response.body().toString());
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String res = jsonObject.getString("res");
                        String msg = jsonObject.getString("msg");
                        if (res.equals("success")) {
                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                            videoPlaylist = new Gson().fromJson(jsonArray1.getJSONObject(0).toString(), VideoPlaylist.class);

                            coursedetails = videoPlaylist.getCourse();
                            Log.e("za", videoPlaylist.getQuestion().length + "");
                            loadQuestionAndReply();
                            setData(videoPlaylist);
                            Assignment[] assignments = videoPlaylist.getAssignment();
                            if(assignments.length==0)
                            {

                                assignmentList.setVisibility(View.GONE);
                                noAssignmentText.setVisibility(View.VISIBLE);
                            }
                            else {
                                noAssignmentText.setVisibility(View.GONE);
                                assignmentList.setVisibility(View.VISIBLE);

                                setInAssignmentRecycler(assignments,videoDetails,coursedetails);
                            }
                        }

                    } catch (JSONException e) {
                        Log.i("dffhgsdfskhd", e.getMessage().toString()+"e");
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.i("dffhgsdfskhd", t.getMessage().toString()+"t");
            }
        });
    }

    private void setData(VideoPlaylist videoPlaylist) {

        VideoDetails videoDetails = videoPlaylist.getVideo();
        if (videoDetails != null) {
            //link = Constraints.BASE_IMAGE_URL + Constraints.DEMO_VIDEO + videoDetails.getVideo();

            author_name.setText(coursesw.getAuthorDetails().getName());
            authorCategory.setText(coursesw.getAuthorDetails().getDesignation()+" , "+coursesw.getAuthorDetails().getPosition());
            Picasso.get().load(Constraints.BASE_IMAGE_URL+Constraints.TUTOR+coursesw.getAuthorDetails().getPhoto()).into(author_circular_icon);

            subject.setText(videoDetails.getSubject());
            video_name.setText(videoDetails.getTitle());
            descOfVideo.setText(videoDetails.getDescription());
            toolbarText.setText(videoDetails.getTitle());

            downloadNotes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                        {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1001);
                        }
                        else
                        {
                            Log.e("sad","download");
                            Uri uri= Uri.parse(Constraints.BASE_IMAGE_URL+Constraints.NOTES+videoDetails.getNotes());
                            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                            DownloadManager.Request request = new DownloadManager.Request(uri);
                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                            request.setTitle("Download  -"+videoDetails.getTitle());
                            request.setDescription("Your Attachment is downloading please wait");
                            request.allowScanningByMediaScanner();
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,System.currentTimeMillis()+"ebook"+".pdf");
                            request.setMimeType("*/*");
                            downloadManager.enqueue(request);
                            Toast.makeText(getApplicationContext(), "Downloading started", Toast.LENGTH_SHORT).show();


                        }
                    }
                }
            });

            List<VideoDetails> list=new ArrayList<>();
            List<VideoDetails> list2=new ArrayList<>();
            VideoDetails[] s=videoPlaylist.getUpcommingList();
            VideoDetails[] s2=videoPlaylist.getWatchAgainList();

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
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
                    course_details.setLayoutManager(layoutManager);
                    course_details.setHasFixedSize(true);
                    int status=0;
                    CourseDetailsAdapter courseDetailsAdapter =new CourseDetailsAdapter(this, list,coursesw,status);
                    course_details.setAdapter(courseDetailsAdapter);
                }
            }

            if(s2.length==0)
            {
                noVideosText2.setVisibility(View.VISIBLE);
                course_details2.setVisibility(View.GONE);
            }
            else
            {
                noVideosText2.setVisibility(View.GONE);
                int i;
                for(i=0;i<s2.length;i++)
                {
                    list2.add(s2[i]);
                }
                if(list2.size()>0) {
                    course_details2.setVisibility(View.VISIBLE);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
                    course_details2.setLayoutManager(layoutManager);
                    course_details2.setHasFixedSize(true);
                    int status=0;
                    CourseDetailsAdapter courseDetailsAdapter =new CourseDetailsAdapter(this, list2,coursesw,status);
                    course_details2.setAdapter(courseDetailsAdapter);
                }
            }

        }
    }

    private void setInAssignmentRecycler(Assignment[] assignments, VideoDetails videoDetails, Coursedetails coursedetails) {
        assignmentList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        AssignmentListAdapter assignmentListAdapter = new AssignmentListAdapter(assignments,courseid,videoId);
        assignmentList.setAdapter(assignmentListAdapter);
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 103) {
            if (resultCode == RESULT_OK && data != null) {
                loadAssignment();
                Uri uri=data.getData();
                String path=FilePath.getPath(this,uri);
                File file = new File(FilePath.getPath(this, uri));
                if (file != null) {
//                myHolder.pdficon.setVisibility(View.VISIBLE);
                   RequestBody reqFile = RequestBody.create(MediaType.parse("*/*"), file);
                   assignmentAnswer = MultipartBody.Part.createFormData("answer", file.getName(), reqFile);

                }

                //Toast.makeText(PlayVideoActivity.this, "file selected", Toast.LENGTH_SHORT).show();
                Log.e("path",path);
            return;
            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        player.setPlayWhenReady(false);
        if(youTube!=null)
        {
         youTube.pause();
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
        if (type.equals("Internal")) {
            if (fullscreen) {
                fullscreenButton.setImageDrawable(ContextCompat.getDrawable(PlayVideoActivity.this, R.drawable.ic_fullscreen_open));

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
                fullscreen = false;
            } else {
                finish();
            }
        } else {
            if (fullscreen) {
                fullscreenButton.setImageDrawable(ContextCompat.getDrawable(PlayVideoActivity.this, R.drawable.ic_fullscreen_open));
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    titleLayout.setVisibility(View.VISIBLE);
                    assign.setVisibility(View.VISIBLE);
                    postQas.setVisibility(View.VISIBLE);
                    assignmentList.setVisibility(View.VISIBLE);
                    questionLayout.setVisibility(View.VISIBLE);
                    questionLayout.setVisibility(View.VISIBLE);
                titleLayout.setVisibility(View.VISIBLE);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().show();
                }
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) playerView.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = (int) (210 * getApplicationContext().getResources().getDisplayMetrics().density);
                yPlayerView.setLayoutParams(params);

                fullscreen = false;
            } else {
                finish();
            }
        }


    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (type.equals("Internal")) {
                fullscreenButton.setImageDrawable(ContextCompat.getDrawable(PlayVideoActivity.this, R.drawable.ic_fullscreen_close));
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
            } else {
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
                titleLayout.setVisibility(View.GONE);
                yPlayerView.setLayoutParams(params);
                fullscreen = true;


            }
        } else {


            if (type.equals("Internal")) {
                fullscreenButton.setImageDrawable(ContextCompat.getDrawable(PlayVideoActivity.this, R.drawable.ic_fullscreen_open));
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                titleLayout.setVisibility(View.VISIBLE);
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
                titleLayout.setVisibility(View.VISIBLE);
                fullscreenButton.setImageDrawable(ContextCompat.getDrawable(PlayVideoActivity.this, R.drawable.ic_fullscreen_open));
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                titleLayout.setVisibility(View.VISIBLE);
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
        if(youTube!=null)
        {
            youTube.pause();
        }
        else {
            yPlayerView.release();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        loadAssignment();
    }

}

