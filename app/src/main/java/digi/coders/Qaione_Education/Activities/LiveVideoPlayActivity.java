package digi.coders.Qaione_Education.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import digi.coders.Qaione_Education.Adapters.QuestionAnswerAdapter;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.LiveSession;
import digi.coders.Qaione_Education.models.Question;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static digi.coders.Qaione_Education.R.drawable.arrow_down_for_video;
import static digi.coders.Qaione_Education.R.drawable.forward_arrow;

public class LiveVideoPlayActivity extends AppCompatActivity {

    private YouTubePlayer youTubePlayer;
    private YouTubePlayerView youTubePlayerView;
    String link="", id="";
    TextView title, video_name, shortDis, description, postQues;
    ImageView share;
    LinearLayout postQas, longDes;
    RelativeLayout questionLayout, desLayout;
    RecyclerView questionAnsList;
    int i=0;
    ImageView quesImg, desImg;
    private SingleTask singleTask;

    private LiveSession liveSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_video_play);

        singleTask= (SingleTask)getApplication();
        title=findViewById(R.id.title);
        share=findViewById(R.id.share);
        video_name=findViewById(R.id.video_name);
        shortDis=findViewById(R.id.shortDis);
        description=findViewById(R.id.description);
        youTubePlayerView=findViewById(R.id.youtube_video_player);
        postQas=findViewById(R.id.postQas);
        questionLayout=findViewById(R.id.post_question);
        quesImg = findViewById(R.id.question_im);
        longDes = findViewById(R.id.longDes);
        desLayout = findViewById(R.id.desLayout);
        desImg = findViewById(R.id.desImg);
        postQues = findViewById(R.id.post_ques);
        questionAnsList = findViewById(R.id.question_list);

        id=(getIntent().getStringExtra("id"));
        loadFullLiveDetails();

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "aakash", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, "Join Qaione Education Platform "+
                        "\n"+"Download app now:"+"\n"+
                        "https://play.google.com/store/apps/details?id=digi.coders.Qaione_Education");
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                startActivity(Intent.createChooser(intent,"Share via"));

            }
        });

        final String[] str=link.split("/");
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady( YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                youTubePlayer.cueVideo(str[str.length-1],0);
                youTubePlayer.play();
                //youTubePlayer.loadVideo(str[str.length-1],0);


            }
        });

        longDes.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                if (i == 0) {
                    desLayout.setVisibility(View.GONE);
//                    quesTxt.setTextColor(Color.BLACK);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        desImg.setImageDrawable(getDrawable(forward_arrow));
                    }
                    i++;
                } else if (i == 1) {
                    desLayout.setVisibility(View.VISIBLE);
//                    quesTxt.setTextColor(Color.BLUE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        desImg.setImageDrawable(getDrawable(arrow_down_for_video));

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

        postQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                ProgressDialog progressDialog = new ProgressDialog(LiveVideoPlayActivity.this,R.style.CustomProgressBarTheme);
                progressDialog.setMessage("Waiting");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(LiveVideoPlayActivity.this, R.style.myBottomSheetDialogTheme);
                View view1 = LayoutInflater.from(LiveVideoPlayActivity.this).inflate(R.layout.post_question_answer_bottom_layout, (RelativeLayout) findViewById(R.id.bottom_sheet_container), false);
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
                            Toast.makeText(LiveVideoPlayActivity.this, "Please Write your Question here....", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("sdsws", "" + user.getId() + id);

                            Call<JsonArray> call = myApi.liveQuestion(id, user.getId(), message);
                            call.enqueue(new Callback<JsonArray>() {
                                @Override
                                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                                    if (response.isSuccessful()) {
                                        try {
                                            Log.i("dhfd", response.body().toString());
                                            JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                                            String res = jsonObject.getString("res");
                                            String msg = jsonObject.getString("msg");
                                            if (res.equals("success")) {

                                                Toast.makeText(LiveVideoPlayActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                loadFullLiveDetails();
                                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                                }
                                                bottomSheetDialog.dismiss();
                                                progressDialog.hide();

                                            }
                                        } catch (JSONException e) {
                                            Toast.makeText(LiveVideoPlayActivity.this, "e"+e, Toast.LENGTH_SHORT).show();
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
    }

    private void loadFullLiveDetails() {
        String json=singleTask.getValue("user");
        User user=new Gson().fromJson(json,User.class);

        MyApi myApi= RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call=myApi.liveSessionFullDetails(id,user.getNumber());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    try {
                        Log.i("liverres", response.body().toString());
                        JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        String res=jsonObject.getString("res");
                        String msg=jsonObject.getString("msg");
                        if(res.equals("success"))
                        {
                            JSONArray jsonArray1=jsonObject.getJSONArray("data");
                            JSONObject jsonObject1=jsonArray1.getJSONObject(0);
                            liveSession=new Gson().fromJson(jsonObject1.toString(),LiveSession.class);
                            //arrayList.add(liveSession1);
                            loadQuestionAndReply();
                            title.setText(liveSession.getTitle());
                            video_name.setText(liveSession.getTitle());
                            shortDis.setText(liveSession.getDescription());
                            description.setText(liveSession.getDescription());
                            link =liveSession.getLink();

                        }

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage().toString()+"e", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage().toString()+"t", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void loadQuestionAndReply() {
        Question[] e = liveSession.getQuestion();
        Log.e("sadsd", e.length + "");
//        QuestionAnswerAdapter.videoPlaylist = videoPlaylist;
        questionAnsList.setLayoutManager(new LinearLayoutManager(LiveVideoPlayActivity.this, LinearLayoutManager.VERTICAL, false));
        questionAnsList.setAdapter(new QuestionAnswerAdapter(e, singleTask));


    }


}