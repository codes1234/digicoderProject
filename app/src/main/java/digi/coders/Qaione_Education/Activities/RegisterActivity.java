package digi.coders.Qaione_Education.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.ReferralCodeModel;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements TextWatcher{

    Button sendOTP_btn;
    TextView alreadyRegister,admissionFee_txt;
    private EditText name;
    private EditText mobile;
    private EditText email;
    private EditText password;
    private EditText referral_Edt;
    private SingleTask singleTask;
    private ProgressDialog progressDialog;
    private ImageView img_passShow;
    private ImageView ref_check;
    private TextView afterRef_name;
    private RadioGroup radioGroup;
    private RadioButton section_A;
    private RadioButton section_B;
    private ProgressBar progressBar;
    String section_type="A";

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Waiting");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        sendOTP_btn=findViewById(R.id.sendOTP_btn);
        //getAppDetails();

        progressBar.setVisibility(View.GONE);

        sendOTP_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(valid())
                {
                    userRegistration();
                  //  singleTask.addValue("mobile",mobileno);
                    //startActivity(new Intent(RegisterActivity.this,HomeActivity.class));
                }

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

        section_A.setChecked(true);
        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if(checkedId==R.id.section_A){
                            section_type="A";
                        }else if(checkedId==R.id.section_B){
                            section_type="B";
                        }
                    }
                }
        );


        alreadyRegister=findViewById(R.id.alreadyRegister);
       /* alreadyRegister.setText(Html.fromHtml("<span>Already register?  <span><u><b>Login here</b></u></span></span>"));*/

        alreadyRegister.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);

                    }
                }
        );

        referral_Edt.addTextChangedListener(this);

    }

    private void userRegistration() {
        progressDialog.show();

        MyApi myApi= RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call=myApi.userRegister(userName,userMobile,userEmail,userPass,userReferral,section_type);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                if(response.isSuccessful()) {
                    setRegistrationResponse(response);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                progressDialog.hide();
//                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("sd",t.getMessage());
            }
        });
    }

    private void setRegistrationResponse(Response<JsonArray> response) {
        try {

//            Toast.makeText(getApplicationContext(),response.body().toString() , Toast.LENGTH_SHORT).show();
            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
            JSONObject jsonObject=jsonArray.getJSONObject(0);

            String res=jsonObject.getString("res");
            String msg=jsonObject.getString("msg");
            progressDialog.show();
            if(res.equalsIgnoreCase("success"))
            {

                //regLoad.setVisibility(View.GONE);

                progressDialog.hide();
                sendOnOtpActivity();

            }
            progressDialog.hide();
//            Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {

            e.printStackTrace();
        }


    }

    private void sendOnOtpActivity() {
        Intent intent=new Intent(this,OtpActivity.class);
        intent.putExtra("mobile",userMobile);
        startActivity(intent);
        finish();
    }

    private void initView() {
        name=findViewById(R.id.name);
        mobile=findViewById(R.id.mobile);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        sendOTP_btn=findViewById(R.id.sendOTP_btn);
        img_passShow=findViewById(R.id.img_passShow);
        singleTask=(SingleTask)getApplication();
        admissionFee_txt=findViewById(R.id.admissionFee_txt);
        referral_Edt=findViewById(R.id.referral_Edt);
        afterRef_name=findViewById(R.id.afterRef_name);
        ref_check=findViewById(R.id.ref_check);
        radioGroup=findViewById(R.id.radio_Group);
        section_A=findViewById(R.id.section_A);
        section_B=findViewById(R.id.section_B);
        progressBar=findViewById(R.id.progressbar);


    }

    String userName, userMobile, userEmail, userPass,userSection,userReferral;
    private boolean valid()
    {
        userName=name.getText().toString();
        userMobile=mobile.getText().toString();
        userEmail=email.getText().toString();
        userPass=password.getText().toString();
        userReferral=referral_Edt.getText().toString();
        if(TextUtils.isEmpty(userName))
        {
            name.setError("Please enter your name");
            name.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(userMobile))
        {
            mobile.setError("Please enter your mobile number");
            mobile.requestFocus();
            return false;
        }
        else if(userMobile.length()!=10)
        {
            mobile.setError("Please enter 10 digit mobile number");
            mobile.requestFocus();
            return false;

        }
        else if (userEmail.length()==0) {
                email.setError("Please enter email");
                email.requestFocus();
                return false;
        }
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            email.setError("Please enter valid email");
            email.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(userPass))
        {
            password.setError("Please enter your password");
            password.requestFocus();
            return false;
        }
        else if(userPass.length()<6)
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
    private void getAppDetails() {
        MyApi myApi= RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call=myApi.getAppDetails();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    try {
                        Log.i("redfhg", response.body().toString());
                        JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        String res=jsonObject.getString("res");
                        if(res.equals("success")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                          /*  help_mobile=jsonObject1.getString("help_mobile");
                            help_email=jsonObject1.getString("help_email");*/
                            admissionFee_txt.setText(jsonObject1.getString("admission_fee"));

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

    @Override
    public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {


    }

    private void getVerifyReferral(String s) {
        Log.e("sds",s);
//        ProgressDialog pr=new ProgressDialog(this);
//        pr.setMessage("Loading...");
//        pr.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        pr.show();
        progressBar.setVisibility(View.VISIBLE);

        MyApi myApi=RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call=myApi.getVerifyReferral(s);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful())
                   // Log.e("TAG", response.body().toString() );
                try {
                    JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    String res = jsonObject.getString("res");
                    String msg=jsonObject.getString("msg");
//                    Log.e("TAG",response.body().toString() );
                    if (res.equals("success")){
//                        pr.dismiss();
                        progressBar.setVisibility(View.GONE);
                        JSONObject jsonObject1=jsonObject.getJSONObject("data");
                        ReferralCodeModel referral=new Gson().fromJson(jsonObject1.toString(),ReferralCodeModel.class);
                        ref_check.setVisibility(View.VISIBLE);
                        afterRef_name.setText(referral.getName());
                    }
                    else{
//                        pr.dismiss();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        ref_check.setVisibility(View.GONE);
                        afterRef_name.setText("");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    @Override
    public void onTextChanged(CharSequence s ,int i, int i1, int i2) {
        if (s.toString().length()>=4)
        {
           // Log.e("sds",s.toString().length()+"");
            getVerifyReferral(s.toString());
        }else {
            ref_check.setVisibility(View.GONE);
            afterRef_name.setText("");
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

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
