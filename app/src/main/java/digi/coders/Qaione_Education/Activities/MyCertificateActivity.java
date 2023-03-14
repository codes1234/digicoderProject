package digi.coders.Qaione_Education.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.Qaione_Education.Adapters.MyCertificateAdapter;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.MyCertificate;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCertificateActivity extends AppCompatActivity {


    private SingleTask singleTask;
    private List<MyCertificate> listOfCertificate;
    private RecyclerView certificateCourseRecycler;
    private Button back;
    private TextView noCertificate;
    private ProgressBar certificateProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_certificate);
        initView();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loadMyCertificate();

    }

    private void initView() {
        singleTask=(SingleTask)getApplication();
        certificateCourseRecycler=findViewById(R.id.certificateListCourse);
        back=findViewById(R.id.back_but);
        noCertificate=findViewById(R.id.no_certificate);
        certificateProgress=findViewById(R.id.certificate_progress);

    }

    private void loadMyCertificate() {
        Log.e("sdsd","sada");
        String js=singleTask.getValue("user");
        User user=new Gson().fromJson(js,User.class);
        MyApi myApi= RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call=myApi.getMyCertificate(user.getId());
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
                            Log.e("sds","ye bhi");
                            listOfCertificate=new ArrayList<>();
                            JSONArray jsonArray1=jsonObject.getJSONArray("data");
                            int i;
                            for(i=0;i<jsonArray1.length();i++)
                            {
                                certificateProgress.setVisibility(View.GONE);
                                certificateCourseRecycler.setVisibility(View.VISIBLE);
                                noCertificate.setVisibility(View.GONE);
                                MyCertificate myCertificate=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),MyCertificate.class);
                                listOfCertificate.add(myCertificate);
                            }
                            setInCertificateRecycler(listOfCertificate);
                        }
                        else
                        {

                            certificateProgress.setVisibility(View.GONE);
                            noCertificate.setVisibility(View.VISIBLE);
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

    private void setInCertificateRecycler(List<MyCertificate> listOfCertificate) {
        Log.e("sdas",listOfCertificate.size()+"");
        certificateCourseRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        certificateCourseRecycler.setAdapter(new MyCertificateAdapter(listOfCertificate));
    }
}