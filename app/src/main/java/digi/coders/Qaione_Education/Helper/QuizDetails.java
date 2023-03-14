package digi.coders.Qaione_Education.Helper;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import digi.coders.Qaione_Education.models.QuizData;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizDetails {
    private SingleTask singleTask;
    private QuizData quizData;
    QuizFetch quizFetch;

    public QuizDetails(SingleTask singleTask) {
        this.singleTask = singleTask;
    }

    public  void getQuiz()
    {
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js, User.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Log.e("userid",user.getId());
        Call<JsonArray> call=myApi.quiz(user.getId());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
//                if(response.isSuccessful())
//                {
                    try {
                        Log.i("hgfdhfd", response.body().toString());

                        JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        String res=jsonObject.getString("res");
                        String msg=jsonObject.getString("msg");
                        if(res.equals("success"))
                        {
                            JSONObject jsonObject1=jsonObject.getJSONObject("data");
                            quizData=new Gson().fromJson(jsonObject1.toString(),QuizData.class);
                            quizFetch.getQuiz(quizData,msg);
                        }
                        else
                        {
//                            Log.i("hgfdhfde", e.getMessagdde());
                            quizFetch.getQuiz(quizData,msg);

                        }
                    } catch (JSONException e) {
//                        e.printStackTrace();

                        Log.i("hgfdhfde", e.getMessage());

                    }

//                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

                Log.i("hgfdhfdt", t.getMessage());
            }
        });

    }


    public void  getQuizData(QuizFetch quizFetch)
    {
        this.quizFetch=quizFetch;
    }


    public interface  QuizFetch
    {
        void getQuiz(QuizData quizData, String message);
    }

}
