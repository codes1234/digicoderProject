package digi.coders.Qaione_Education.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import digi.coders.Qaione_Education.Adapters.QuestionAdapter;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.databinding.ActivityPlayQuizBinding;
import digi.coders.Qaione_Education.databinding.QuestionDesignBinding;
import digi.coders.Qaione_Education.models.Questio;
import digi.coders.Qaione_Education.models.QuestionListData;
import digi.coders.Qaione_Education.models.Quiz;
import digi.coders.Qaione_Education.models.QuizLData;
import digi.coders.Qaione_Education.models.ResponseQues;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayQuizActivity extends AppCompatActivity {

    ActivityPlayQuizBinding binding;

    private SingleTask singleTask;
    private int quizId;
    private  static int questionCounter;
    private int questionCountTotal;
    private Questio currentQuestio;
    private long backPressedTime;
    private String ansOption;
    private long timeLeftInMillis;
    private CountDownTimer countDownTimer;
    private int total=0;
    private int tryOption=0;
    QuestionDesignBinding queBinding;
    List<ResponseQues> responseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_play_quiz);

        binding = ActivityPlayQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        singleTask = (SingleTask) getApplication();
        //loadQuestion
        quizId = Integer.parseInt(getIntent().getStringExtra("id"));
        laodQuestionList();

    }

    private void laodQuestionList() {
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.attendQuiz(quizId,user.getId());
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
                            JSONObject jsonObject1=jsonObject.getJSONObject("data");
                            QuestionListData questionListData=new Gson().fromJson(jsonObject1.toString(),QuestionListData.class);
                            setInQuestionAdapter(questionListData);

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

    private void setInQuestionAdapter(QuestionListData questionListData) {
        responseList=new ArrayList<>();
        binding.lottieAnimtaion.setVisibility(View.GONE);
        binding.questionList.setVisibility(View.VISIBLE);
        Quiz[] quizs=questionListData.getQuizData();
        String time=quizs[0].getTiming();
        long t=Long.parseLong(time);
        timeLeftInMillis=t*60*1000;
        startCountDown();
        Questio[] questios = questionListData.getQuestionslist();
        questionCountTotal = questios.length;

        if (questionCounter < questionCountTotal) {
            currentQuestio = questios[questionCounter];
            binding.rightAns.setVisibility(View.GONE);
            questionCounter++;
            binding.textViewQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
            binding.quesNo.setText("Question: " + questionCounter);
            //answered = false;
            binding.submit.setText("Next");
            binding.questionList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            QuestionAdapter adapter=new QuestionAdapter(currentQuestio,questionCounter);

            adapter.getSelectedOption(new QuestionAdapter.GetOption() {
                @Override
                public void click(QuestionDesignBinding binding, int position) {
                    queBinding=binding;
                    binding.aOptionLayout.setOnClickListener(new View.OnClickListener() {

                        @SuppressLint("UseCompatLoadingForDrawables")
                        @Override
                        public void onClick(View v) {
                            ansOption="a";
                            binding.aOptionRadioImage.setImageDrawable(getResources().getDrawable(R.drawable.checked_radio_button));
                            binding.bOptionRadioImage.setImageDrawable(getResources().getDrawable(R.drawable.unchecked_radio_btn));
                            binding.cOptionRadioImage.setImageDrawable(getResources().getDrawable(R.drawable.unchecked_radio_btn));
                            binding.dOptionRadioImage.setImageDrawable(getResources().getDrawable(R.drawable.unchecked_radio_btn));
                            Toast.makeText(PlayQuizActivity.this, "a option", Toast.LENGTH_SHORT).show();
                        }
                    });

                    binding.bOptionLayout.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("UseCompatLoadingForDrawables")
                        @Override
                        public void onClick(View v) {

                            ansOption="b";

                            Toast.makeText(PlayQuizActivity.this, "b option", Toast.LENGTH_SHORT).show();


                            binding.aOptionRadioImage.setImageDrawable(getResources().getDrawable(R.drawable.unchecked_radio_btn));
                            binding.bOptionRadioImage.setImageDrawable(getResources().getDrawable(R.drawable.checked_radio_button));
                            binding.cOptionRadioImage.setImageDrawable(getResources().getDrawable(R.drawable.unchecked_radio_btn));
                            binding.dOptionRadioImage.setImageDrawable(getResources().getDrawable(R.drawable.unchecked_radio_btn));
                        }
                    });
                    binding.cOptionLayout.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("UseCompatLoadingForDrawables")
                        @Override
                        public void onClick(View v) {
                            ansOption="c";
                            Toast.makeText(PlayQuizActivity.this, "c option", Toast.LENGTH_SHORT).show();

                            binding.aOptionRadioImage.setImageDrawable(getResources().getDrawable(R.drawable.unchecked_radio_btn));
                            binding.bOptionRadioImage.setImageDrawable(getResources().getDrawable(R.drawable.unchecked_radio_btn));
                            binding.cOptionRadioImage.setImageDrawable(getResources().getDrawable(R.drawable.checked_radio_button));
                            binding.dOptionRadioImage.setImageDrawable(getResources().getDrawable(R.drawable.unchecked_radio_btn));
                        }
                    });
                    binding.dOptionLayout.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("UseCompatLoadingForDrawables")
                        @Override
                        public void onClick(View v) {
                            ansOption="d";
                            Toast.makeText(PlayQuizActivity.this, "d option", Toast.LENGTH_SHORT).show();
                            binding.aOptionRadioImage.setImageDrawable(getResources().getDrawable(R.drawable.unchecked_radio_btn));
                            binding.bOptionRadioImage.setImageDrawable(getResources().getDrawable(R.drawable.unchecked_radio_btn));
                            binding.cOptionRadioImage.setImageDrawable(getResources().getDrawable(R.drawable.unchecked_radio_btn));
                            binding.dOptionRadioImage.setImageDrawable(getResources().getDrawable(R.drawable.checked_radio_button));
                        }
                    });
                }
            });

            binding.submit.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    if(binding.submit.getText().toString().equals("Next"))
                    {
                        if(ansOption==null || ansOption.equals(""))
                        {

                            Toast.makeText(singleTask, "Please Select Answer", Toast.LENGTH_SHORT).show();

                        }
                        else if(tryOption==0)
                        {
                            Log.e("sdsd",ansOption+"");
                            if(!ansOption.isEmpty())
                            {
                                Log.e("cure", currentQuestio.getAnswer());
                                if(currentQuestio.getAnswer().equals(ansOption))
                                {
                                    if(questionCounter==questionCountTotal)
                                    {
                                        binding.submit.setText("Finish");

                                    }
                                    responseList.add(new ResponseQues(currentQuestio.getId(),ansOption));
                                    if(ansOption.equals("a"))
                                    {
                                        queBinding.aOptionLayout.setCardBackgroundColor(getResources().getColor(R.color.green));
                                        queBinding.aoptionText.setTextColor(getResources().getColor(R.color.white));
                                        queBinding.aOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                                    }
                                    else if(ansOption.equals("b"))
                                    {
                                        queBinding.bOptionLayout.setCardBackgroundColor(getResources().getColor(R.color.green));
                                        queBinding.boptionText.setTextColor(getResources().getColor(R.color.white));
                                        queBinding.bOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                                    }
                                    else if(ansOption.equals("c"))
                                    {
                                        queBinding.cOptionLayout.setCardBackgroundColor(getResources().getColor(R.color.green));
                                        queBinding.coptionText.setTextColor(getResources().getColor(R.color.white));
                                        queBinding.cOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                                    }
                                    else
                                    {
                                        queBinding.dOptionLayout.setCardBackgroundColor(getResources().getColor(R.color.green));
                                        queBinding.doptionText.setTextColor(getResources().getColor(R.color.white));
                                        queBinding.dOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                                    }

                                    queBinding.aOptionLayout.setClickable(false);
                                    queBinding.bOptionLayout.setClickable(false);
                                    queBinding.cOptionLayout.setClickable(false);
                                    queBinding.dOptionLayout.setClickable(false);
                                    Toast.makeText(PlayQuizActivity.this, "Welldone Right Answer", Toast.LENGTH_SHORT).show();

                                    tryOption=2;
                                }
                                else
                                {
                                    if(tryOption==0)
                                    {
                                        Toast.makeText(PlayQuizActivity.this, " Your answer is Wrong try again!", Toast.LENGTH_SHORT).show();
                                        binding.submit.setText("Try Again");
                                        if(ansOption.equals("a"))
                                        {
                                            queBinding.aOptionLayout.setCardBackgroundColor(getResources().getColor(R.color.red));
                                            queBinding.aoptionText.setTextColor(getResources().getColor(R.color.white));
                                            queBinding.aOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                                        }
                                        else if(ansOption.equals("b"))
                                        {
                                            queBinding.bOptionLayout.setCardBackgroundColor(getResources().getColor(R.color.red));
                                            queBinding.boptionText.setTextColor(getResources().getColor(R.color.white));
                                            queBinding.bOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                                        }
                                        else if(ansOption.equals("c"))
                                        {
                                            queBinding.cOptionLayout.setCardBackgroundColor(getResources().getColor(R.color.red));
                                            queBinding.coptionText.setTextColor(getResources().getColor(R.color.white));
                                            queBinding.cOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                                        }
                                        else
                                        {
                                            queBinding.dOptionLayout.setCardBackgroundColor(getResources().getColor(R.color.red));
                                            queBinding.doptionText.setTextColor(getResources().getColor(R.color.white));
                                            queBinding.dOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                                        }
                                        tryOption=1;
                                    }
                                }
                            }
                        }

                        else
                        {
                            if(tryOption==2)
                            {
                                tryOption=0;
                                Log.e("ques",questionCounter+"ssdsd"+questionCountTotal);
                                //check Answer
                                binding.lottieAnimtaion.setVisibility(View.VISIBLE);
                                binding.questionList.setVisibility(View.GONE);
                                if(currentQuestio.getAnswer().equals(ansOption))
                                {
                                    total=total+1;
                                    Log.e("right Answr",total+"");

                                }

                                //           showNextQuestion();
                                ansOption="";
                                if (questionCounter < questionCountTotal) {

                                    Log.e("try",tryOption+"");
                                    /*queBinding.aOptionLayout.setCardBackgroundColor(getResources().getColor(R.color.white));
                                    queBinding.aOptionLayout.setCardBackgroundColor(getResources().getColor(R.color.white));
                                    queBinding.aOptionLayout.setCardBackgroundColor(getResources().getColor(R.color.white));
                                    queBinding.aOptionLayout.setCardBackgroundColor(getResources().getColor(R.color.white));*/
                                    binding.lottieAnimtaion.setVisibility(View.VISIBLE);
                                    binding.questionList.setVisibility(View.GONE);
                                    currentQuestio = questios[questionCounter];
                                    binding.rightAns.setVisibility(View.GONE);
                                    questionCounter++;
                                    binding.textViewQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
                                    binding.quesNo.setText("Question: " + questionCounter);

                                    //answered = false;
                                    //binding.submit.setText("Confirm");
                                    binding.questionList.setLayoutManager(new LinearLayoutManager(PlayQuizActivity.this, LinearLayoutManager.VERTICAL, false));
                                    QuestionAdapter adapter=new QuestionAdapter(currentQuestio,questionCounter);
                                    binding.lottieAnimtaion.setVisibility(View.GONE);
                                    binding.questionList.setVisibility(View.VISIBLE);
                                    adapter.getSelectedOption(new QuestionAdapter.GetOption() {
                                        @Override
                                        public void click(QuestionDesignBinding binding, int position) {
                                            queBinding=binding;
                                            binding.aOptionLayout.setOnClickListener(new View.OnClickListener() {
                                                @SuppressLint("UseCompatLoadingForDrawables")
                                                @Override
                                                public void onClick(View v) {
                                                    ansOption="a";
                                                    binding.aOptionRadioImage.setImageDrawable(getResources().getDrawable(R.drawable.checked_radio_button));
                                                    binding.bOptionRadioImage.setImageDrawable(getResources().getDrawable(R.drawable.unchecked_radio_btn));
                                                    binding.cOptionRadioImage.setImageDrawable(getResources().getDrawable(R.drawable.unchecked_radio_btn));
                                                    binding.dOptionRadioImage.setImageDrawable(getResources().getDrawable(R.drawable.unchecked_radio_btn));
                                                    Toast.makeText(PlayQuizActivity.this, "a option", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            binding.bOptionLayout.setOnClickListener(new View.OnClickListener() {
                                                @SuppressLint("UseCompatLoadingForDrawables")
                                                @Override
                                                public void onClick(View v) {

                                                    ansOption="b";

                                                    Toast.makeText(PlayQuizActivity.this, "b option", Toast.LENGTH_SHORT).show();


                                                    binding.aOptionRadioImage.setImageDrawable(getResources().getDrawable(R.drawable.unchecked_radio_btn));
                                                    binding.bOptionRadioImage.setImageDrawable(getResources().getDrawable(R.drawable.checked_radio_button));
                                                    binding.cOptionRadioImage.setImageDrawable(getResources().getDrawable(R.drawable.unchecked_radio_btn));
                                                    binding.dOptionRadioImage.setImageDrawable(getResources().getDrawable(R.drawable.unchecked_radio_btn));
                                                }
                                            });
                                            binding.cOptionLayout.setOnClickListener(new View.OnClickListener() {
                                                @SuppressLint("UseCompatLoadingForDrawables")
                                                @Override
                                                public void onClick(View v) {
                                                    ansOption="c";
                                                    Toast.makeText(PlayQuizActivity.this, "c option", Toast.LENGTH_SHORT).show();

                                                    binding.aOptionRadioImage.setImageDrawable(getResources().getDrawable(R.drawable.unchecked_radio_btn));
                                                    binding.bOptionRadioImage.setImageDrawable(getResources().getDrawable(R.drawable.unchecked_radio_btn));
                                                    binding.cOptionRadioImage.setImageDrawable(getResources().getDrawable(R.drawable.checked_radio_button));
                                                    binding.dOptionRadioImage.setImageDrawable(getResources().getDrawable(R.drawable.unchecked_radio_btn));
                                                }
                                            });
                                            binding.dOptionLayout.setOnClickListener(new View.OnClickListener() {
                                                @SuppressLint("UseCompatLoadingForDrawables")
                                                @Override
                                                public void onClick(View v) {
                                                    ansOption="d";
                                                    Toast.makeText(PlayQuizActivity.this, "d option", Toast.LENGTH_SHORT).show();
                                                    binding.aOptionRadioImage.setImageDrawable(getResources().getDrawable(R.drawable.unchecked_radio_btn));
                                                    binding.bOptionRadioImage.setImageDrawable(getResources().getDrawable(R.drawable.unchecked_radio_btn));
                                                    binding.cOptionRadioImage.setImageDrawable(getResources().getDrawable(R.drawable.unchecked_radio_btn));
                                                    binding.dOptionRadioImage.setImageDrawable(getResources().getDrawable(R.drawable.checked_radio_button));
                                                }
                                            });


                                        }


                                    });
                                    if(questionCounter==questionCountTotal)
                                    {

                                        if(tryOption==2) {
                                            binding.submit.setText("Finish");
                                        }

                                    }
                                    binding.questionList.setAdapter(adapter);
                                }


                            }
                            else
                            {
                                if(currentQuestio.getAnswer().equals(ansOption))
                                {
                                    if(questionCounter==questionCountTotal)
                                    {
                                        binding.submit.setText("Finish");

                                    }
                                    Toast.makeText(PlayQuizActivity.this, "Welldone Right Answer", Toast.LENGTH_SHORT).show();
                                    binding.submit.setText("Next");
                                    if(ansOption.equals("a"))
                                    {
                                        queBinding.aOptionLayout.setCardBackgroundColor(getResources().getColor(R.color.red));
                                        queBinding.aoptionText.setTextColor(getResources().getColor(R.color.white));
                                        queBinding.aOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                                    }
                                    else if(ansOption.equals("b"))
                                    {
                                        queBinding.bOptionLayout.setCardBackgroundColor(getResources().getColor(R.color.red));
                                        queBinding.boptionText.setTextColor(getResources().getColor(R.color.white));
                                        queBinding.bOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                                    }
                                    else if(ansOption.equals("c"))
                                    {
                                        queBinding.cOptionLayout.setCardBackgroundColor(getResources().getColor(R.color.red));
                                        queBinding.coptionText.setTextColor(getResources().getColor(R.color.white));
                                        queBinding.cOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));


                                    }
                                    else
                                    {
                                        queBinding.dOptionLayout.setCardBackgroundColor(getResources().getColor(R.color.red));
                                        queBinding.doptionText.setTextColor(getResources().getColor(R.color.white));
                                        queBinding.dOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                                    }
                                    queBinding.aOptionLayout.setClickable(false);
                                    queBinding.bOptionLayout.setClickable(false);
                                    queBinding.cOptionLayout.setClickable(false);
                                    queBinding.dOptionLayout.setClickable(false);
                                    tryOption=2;
                                }

                            }

                        }

                    }
                    else if(binding.submit.getText().toString().equals("Finish"))
                    {

                        if(currentQuestio.getAnswer().equals(ansOption))
                        {
                            total=total+1;
                            Log.e("right Answr",total+"");

                        }
                        if(binding.submit.getText().equals("Finish"))
                        {


                            submitQuiz(total,questionListData,questionCountTotal);
                            //         Toast.makeText(singleTask, "Finish Quiz", Toast.LENGTH_SHORT).show();
                        }
                    }


                    else if(binding.submit.getText().toString().equals("Try Again"))
                    {

                        if(tryOption==1)
                        {
                            queBinding.aOptionLayout.setCardBackgroundColor(getResources().getColor(R.color.white));
                            queBinding.bOptionLayout.setCardBackgroundColor(getResources().getColor(R.color.white));
                            queBinding.cOptionLayout.setCardBackgroundColor(getResources().getColor(R.color.white));
                            queBinding.dOptionLayout.setCardBackgroundColor(getResources().getColor(R.color.white));

                            queBinding.aoptionText.setTextColor(getResources().getColor(R.color.black));
                            queBinding.aOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.navy_blue)));
                            queBinding.boptionText.setTextColor(getResources().getColor(R.color.black));
                            queBinding.bOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.navy_blue)));
                            queBinding.coptionText.setTextColor(getResources().getColor(R.color.black));
                            queBinding.cOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.navy_blue)));
                            queBinding.doptionText.setTextColor(getResources().getColor(R.color.black));
                            queBinding.dOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.navy_blue)));
                            if(currentQuestio.getAnswer().equals(ansOption))
                            {
                                responseList.add(new ResponseQues(currentQuestio.getId(),ansOption));
                                if(questionCounter==questionCountTotal)
                                {
                                    binding.submit.setText("Finish");

                                }
                                else
                                {
                                    binding.submit.setText("Next");
                                }
                                Toast.makeText(PlayQuizActivity.this, "Welldone Right Answer", Toast.LENGTH_SHORT).show();


                                if(ansOption.equals("a"))
                                {
                                    queBinding.aOptionLayout.setCardBackgroundColor(getResources().getColor(R.color.green));
                                    queBinding.aoptionText.setTextColor(getResources().getColor(R.color.white));
                                    queBinding.aOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                                }
                                else if(ansOption.equals("b"))
                                {
                                    queBinding.bOptionLayout.setCardBackgroundColor(getResources().getColor(R.color.green));
                                    queBinding.boptionText.setTextColor(getResources().getColor(R.color.white));
                                    queBinding.bOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));


                                }
                                else if(ansOption.equals("c"))
                                {
                                    queBinding.cOptionLayout.setCardBackgroundColor(getResources().getColor(R.color.green));
                                    queBinding.coptionText.setTextColor(getResources().getColor(R.color.white));
                                    queBinding.cOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));


                                }
                                else
                                {
                                    queBinding.dOptionLayout.setCardBackgroundColor(getResources().getColor(R.color.green));
                                    queBinding.doptionText.setTextColor(getResources().getColor(R.color.white));
                                    queBinding.dOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));


                                }
                                queBinding.aOptionLayout.setClickable(false);
                                queBinding.bOptionLayout.setClickable(false);
                                queBinding.cOptionLayout.setClickable(false);
                                queBinding.dOptionLayout.setClickable(false);

                                tryOption=2;
                            }
                            else {
                                if(questionCounter==questionCountTotal)
                                {
                                    binding.submit.setText("Finish");

                                }
                                else {
                                    binding.submit.setText("Next");
                                }
                                responseList.add(new ResponseQues(currentQuestio.getId(),ansOption));
                                if (ansOption.equals("a")) {
                                    queBinding.aOptionLayout.setCardBackgroundColor(getResources().getColor(R.color.red));
                                    queBinding.aoptionText.setTextColor(getResources().getColor(R.color.white));
                                    queBinding.aOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                                } else if (ansOption.equals("b")) {
                                    queBinding.bOptionLayout.setCardBackgroundColor(getResources().getColor(R.color.red));
                                    queBinding.boptionText.setTextColor(getResources().getColor(R.color.white));
                                    queBinding.bOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                                } else if (ansOption.equals("c")) {
                                    queBinding.cOptionLayout.setCardBackgroundColor(getResources().getColor(R.color.red));
                                    queBinding.coptionText.setTextColor(getResources().getColor(R.color.white));
                                    queBinding.cOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                                } else {
                                    queBinding.dOptionLayout.setCardBackgroundColor(getResources().getColor(R.color.red));
                                    queBinding.doptionText.setTextColor(getResources().getColor(R.color.white));
                                    queBinding.dOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                                }

                                queBinding.aOptionLayout.setClickable(false);
                                queBinding.bOptionLayout.setClickable(false);
                                queBinding.cOptionLayout.setClickable(false);
                                queBinding.dOptionLayout.setClickable(false);
                                if (currentQuestio.getAnswer().equals("a")) {
                                    queBinding.aOptionLayout.setCardBackgroundColor(getResources().getColor(R.color.green));
                                    queBinding.aoptionText.setTextColor(getResources().getColor(R.color.white));
                                    queBinding.aOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                                } else if (currentQuestio.getAnswer().equals("b")) {
                                    queBinding.bOptionLayout.setCardBackgroundColor(getResources().getColor(R.color.green));
                                    queBinding.boptionText.setTextColor(getResources().getColor(R.color.white));
                                    queBinding.bOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                                } else if (currentQuestio.getAnswer().equals("c")) {
                                    queBinding.cOptionLayout.setCardBackgroundColor(getResources().getColor(R.color.green));
                                    queBinding.coptionText.setTextColor(getResources().getColor(R.color.white));
                                    queBinding.cOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                                } else {
                                    queBinding.dOptionLayout.setCardBackgroundColor(getResources().getColor(R.color.green));
                                    queBinding.doptionText.setTextColor(getResources().getColor(R.color.white));
                                    queBinding.dOptionRadioImage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                                }
                                tryOption = 2;
                            }


                        }


                    }
                    else
                    {

                    }

                }
            });
            binding.questionList.setAdapter(adapter);
            //timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            //startCountDown();
        }

    }

    private void submitQuiz(int total, QuestionListData questionListData, int questionCountTotal) {
        Log.e("sdsd",responseList.size()+"");
        Gson gson = new GsonBuilder().create();
        JsonArray myCustomArray = gson.toJsonTree(responseList).getAsJsonArray();

        //Log.e("sdsd",myCustomArray.toString());
        ProgressDialog pr=new ProgressDialog(this);
        pr.setMessage("Loading...");
        pr.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pr.show();
        Quiz[] quiz=questionListData.getQuizData();
        String wrong=String.valueOf(questionCountTotal-total);
        String right=String.valueOf(total);
        int s=Integer.parseInt(quiz[0].getPerQuestionNo())*total;
        String score=String.valueOf(s);
        QuizLData[] we=questionListData.getList();
        int quizId=Integer.parseInt(we[0].getId());
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Log.e("quizid",quizId+"");
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        Call<JsonArray> call=myApi.submitQuiz(quizId,user.getId(),right,wrong,myCustomArray.toString(),score);
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
                        JSONObject jsonObject1=jsonObject.getJSONObject("data");
                        QuizLData quizLData=new Gson().fromJson(jsonObject1.toString(), QuizLData.class);
                        QuizScoreActivity.quizLData=quizLData;
                        startActivity(new Intent(PlayQuizActivity.this,QuizScoreActivity.class));
                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //Log.e("response",response.body()+"");

                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });



    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finish();
            tryOption=0;
            questionCounter=0;
        } else {
            Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
            }
        }.start();

    }

    private void updateCountDownText() {
        Log.e("sdsd",timeLeftInMillis+"");
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        binding.textViewCountdown.setText(timeFormatted);

        if (timeLeftInMillis < 10000) {
            binding.textViewCountdown.setTextColor(Color.RED);
        } else {
            binding.textViewCountdown.setTextColor(getResources().getColor(R.color.white));
        }

    }

    public void goBack(View view) {
        finish();
        tryOption=0;
        questionCounter=0;
    }
}