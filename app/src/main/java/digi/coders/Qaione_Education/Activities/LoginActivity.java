package digi.coders.Qaione_Education.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import digi.coders.Qaione_Education.EducatorloginActivity;
import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Button next_btn;
    TextView tandc,any_trubols, createAccount, forgetPass,educatLogin;
    private EditText mobile;
    private EditText password;
    private SingleTask singleTask;
    private ProgressDialog progressDialog;
    private ImageView img_passShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Waiting");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        next_btn=findViewById(R.id.next_btn);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(valid()){
                    loginUser();
                }

            }
        });
        educatLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EducatorloginActivity.class);
                startActivity(intent);
            }
        });

        createAccount=findViewById(R.id.createAccount);
//        any_trubols=findViewById(R.id.any_trubols);
        forgetPass=findViewById(R.id.forgetPass);
//        tandc=findViewById(R.id.tandc);

//        tandc.setText(Html.fromHtml("<span>By proceeding you will agree to our  <span style='color:blue'><u>Terms And Conditions.</u></span></span>"));
//        createAccount.setText(Html.fromHtml("<span>New User?  <span style='color:black'><u>Create an Account</u></span></span>"));

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
            }
        });

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgetPasswordActivity.class));
                finish();
            }
        });

        img_passShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                    img_passShow.setImageResource(R.drawable.ic_baseline_visibility_off);

                    //Show Password
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    img_passShow.setImageResource(R.drawable.ic_baseline_visibility);

                    //Hide Password
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }

            }
        });

       /* tandc.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getApplicationContext(), WebActivity.class);
                        intent.putExtra("link","https://karmasu.co.in/");
                        intent.putExtra("tittle","Terms & Conditions");
                        startActivity(intent);
                    }
                }
        );*/

        /*any_trubols.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getApplicationContext(), WebActivity.class);
                        intent.putExtra("link","https://karmasu.co.in/");
                        intent.putExtra("tittle","Contact Us");
                        startActivity(intent);
                    }
                }
        );*/

    }

    private void userLogin() {
        progressDialog.show();
        MyApi myApi=RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call=myApi.userLogin(mobileNo,userPass);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                Log.i("dasdasd",response.body().toString());
                if(response.isSuccessful())
                {
                    setLoginReponse(response);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                progressDialog.hide();
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("sd",t.getMessage());
            }
        });

    }

    private void loginUser() {
        MyApi myApi=RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call=myApi.userLogin(mobile.getText().toString(),password.getText().toString());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                //Log.i("asad",response.body().toString()+"  "+mobile.getText().toString()+","+password.getText().toString());
                if(response.isSuccessful())
                {
                    try {
                        JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        String res=jsonObject.getString("res");
                        String msg=jsonObject.getString("msg");
                        if(res.equals("success"))
                        {
                            progressDialog.hide();
                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                            JSONArray jsonArray1=jsonObject.getJSONArray("data");
                            JSONObject jsonObject1=jsonArray1.getJSONObject(0);
                            singleTask.addValue("user",jsonObject1);
                            Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                            startActivity(intent);
                            finish();

                        }
                        else
                        {
                            progressDialog.hide();
                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        });


    }

    private void setLoginReponse(Response<JsonArray> response) {
        try {
            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
            JSONObject jsonObject=jsonArray.getJSONObject(0);

            String res=jsonObject.getString("res");
            String msg=jsonObject.getString("msg");
            progressDialog.show();
            if(res.equalsIgnoreCase("success"))
            {

                //regLoad.setVisibility(View.GONE);

                progressDialog.hide();
                sendOnHomeActivity();

            }
            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {

            e.printStackTrace();
        }


    }

    private void sendOnHomeActivity() {
        Intent intent=new Intent(this,HomeActivity.class);
        startActivity(intent);
    }

    private void initView() {
        mobile=findViewById(R.id.mobile);
        password=findViewById(R.id.password);
        next_btn=findViewById(R.id.sendOTP_btn);
        img_passShow=findViewById(R.id.img_passShow);
        educatLogin=findViewById(R.id.educatLogin);
        singleTask=(SingleTask)getApplication();


    }

    String mobileNo, userPass;
    private boolean valid()
    {
        mobileNo=mobile.getText().toString();
        userPass=password.getText().toString();
        if(TextUtils.isEmpty(mobileNo))
        {
            mobile.setError("Please enter your User ID");
            mobile.requestFocus();
            return false;
        }
      /*  if(mobileNo.length()!=10)
        {
            mobile.setError("Please enter 10 digit mobile number");
            mobile.requestFocus();
            return false;

        }*/
        if(TextUtils.isEmpty(userPass))
        {
            password.setError("Please enter your password");
            password.requestFocus();
            return false;
        }
        if(userPass.length()<6)
        {
            password.setError("Please enter more than 6 digit password");
            password.requestFocus();
            return false;

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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

}