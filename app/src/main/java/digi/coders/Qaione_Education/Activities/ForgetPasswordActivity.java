package digi.coders.Qaione_Education.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.transition.Slide;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import in.aabhasjindal.otptextview.OtpTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity {

    Button Next;
    EditText Mobile, CreatePassword ,ConfirmPassword;
    ProgressDialog pd;
    String mobile, otp, createPass, ConfirmPass, fp_token;
    RelativeLayout A1,A2,A3;
    OtpTextView pinView;
    LinearLayout Resend;
    ImageView back;

    ViewGroup view_group;
    View parentLayout;
    public static Activity forgetPassActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        view_group=findViewById(R.id.view_group);
        Next = findViewById(R.id.next);
        Mobile = findViewById(R.id.mobile);
        A1 = findViewById(R.id.relative1);
        A2 = findViewById(R.id.relative2);
        A3 = findViewById(R.id.relative3);
        pinView = findViewById(R.id.pin_view);
        CreatePassword=findViewById(R.id.new_password);
        ConfirmPassword=findViewById(R.id.confirm_pass);
        parentLayout = findViewById(android.R.id.content);
        Resend = findViewById(R.id.resend);
        back = findViewById(R.id.back);

        A1.setVisibility(View.VISIBLE);
        A2.setVisibility(View.GONE);
        A3.setVisibility(View.GONE);

        Next.setText("Let's go!");

        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Next.getText().equals("Let's go!"))
                        {
                            //finish();
                            Snackbar.make(parentLayout, "You want to Back !", Snackbar.LENGTH_LONG)
                                    .setAction("Yes", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            finish();
                                        }
                                    })
                                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                                    .show();

                        }
                        else if (Next.getText().equals("Verify"))
                        {

                            Snackbar.make(parentLayout, "You want to Back !", Snackbar.LENGTH_LONG)
                                    .setAction("Yes", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            TransitionSet set = new TransitionSet()
                                                    .addTransition(new Slide(Gravity.LEFT))
                                                    .setInterpolator(
                                                            new FastOutLinearInInterpolator());

                                            TransitionManager.beginDelayedTransition(view_group,set);
                                            A1.setVisibility(View.VISIBLE);
                                            A2.setVisibility(View.GONE);
                                            Next.setText("Let's go!");

                                        }
                                    })
                                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                                    .show();

                        }
                        else if (Next.getText().equals("Create Password"))
                        {

                            Snackbar.make(parentLayout, "You want to Back !", Snackbar.LENGTH_LONG)
                                    .setAction("Yes", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            TransitionSet set = new TransitionSet()
                                                    .addTransition(new Slide(Gravity.LEFT))
                                                    .setInterpolator(
                                                            new FastOutLinearInInterpolator());

                                            TransitionManager.beginDelayedTransition(view_group,set);
                                            A2.setVisibility(View.VISIBLE);
                                            A3.setVisibility(View.GONE);
                                            Next.setText("Verify");
                                        }
                                    })
                                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                                    .show();

                        }

                    }
                }
        );

        Resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mobile=Mobile.getText().toString();
                reSendOTP(mobile);
            }
        });


        Next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                if (Next.getText().equals("Let's go!"))
                {

                    mobile=Mobile.getText().toString();
                    validate();

                }
                else if (Next.getText().equals("Verify"))
                {

                    otp=pinView.getOTP().toString();
                    otpVerify();


                }
                else if (Next.getText().equals("Create Password"))
                {

                    createPass=CreatePassword.getText().toString();
                    ConfirmPass=ConfirmPassword.getText().toString();
                    CreatePassword();

                }

            }
        });

    }

    public void validate(){

        if (mobile.equals("")) {
            Mobile.setError("Please Enter Number");
            Mobile.requestFocus();
            return;
        }

        if (mobile.length()!=10) {
            Mobile.setError("Please Enter Valid Number");
            Mobile.requestFocus();
            return;
        }

        forgetPass(mobile);

    }

    public void CreatePassword(){
        String password=CreatePassword.getText().toString();
        String conpass=ConfirmPassword.getText().toString();

        if(password.equals("")){
            CreatePassword.setError("Please Enter password");
            CreatePassword.requestFocus();
            return;
        }
        if(conpass.equals(password)){

        }else{
            ConfirmPassword.setError("Password Does not Match");
            ConfirmPassword.requestFocus();
            return;
        }

        createPass();

    }

    public void forgetPass (final String mobile){
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.show();
        MyApi myApi= RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call=myApi.forgetPass(mobile);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                try{

                    //Toast.makeText(getApplicationContext(),response.body().toString(),Toast.LENGTH_LONG).show();

                    JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    String status=jsonObject.getString("res");
                    if(status.equalsIgnoreCase("success")){

                        TransitionSet set = new TransitionSet()
                                .addTransition(new Slide(Gravity.LEFT))
                                .setInterpolator(
                                        new FastOutLinearInInterpolator());

                        TransitionManager.beginDelayedTransition(view_group,set);
                        A1.setVisibility(View.GONE);
                        A2.setVisibility(View.VISIBLE);
                        Next.setText("Verify");
                        Snackbar.make(parentLayout, "OTP Send !", Snackbar.LENGTH_LONG)
                                .setAction("CLOSE", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                })
                                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                                .show();

                    }else {
                        Toast.makeText(getApplicationContext(),jsonObject.getString("msg"), Toast.LENGTH_LONG).show();

                    }
                }catch (Exception e){
                    //Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();

                }
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),t.getMessage().toString(),Toast.LENGTH_LONG).show();

                pd.dismiss();
            }
        });
    }

    public void otpVerify (){
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.show();
        MyApi myApi= RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call=myApi.otpVerification(mobile,otp);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                try{

                    Toast.makeText(getApplicationContext(),response.body().toString(),Toast.LENGTH_LONG).show();

                    JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    String status=jsonObject.getString("res");
                    if(status.equalsIgnoreCase("success")){

                        JSONObject jsonObject1=jsonObject.getJSONObject("data");
                        fp_token=jsonObject1.getString("fp_token");
                        Toast.makeText(ForgetPasswordActivity.this, fp_token, Toast.LENGTH_SHORT).show();

                        TransitionSet set = new TransitionSet()
                                .addTransition(new Slide(Gravity.LEFT))
                                .setInterpolator(
                                        new FastOutLinearInInterpolator());

                        TransitionManager.beginDelayedTransition(view_group,set);
                        A2.setVisibility(View.GONE);
                        A3.setVisibility(View.VISIBLE);
                        Next.setText("Create Password");

                        Snackbar.make(parentLayout, "OTP Verified !", Snackbar.LENGTH_LONG)
                                .setAction("CLOSE", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                })
                                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                                .show();

                    }else {
                        Toast.makeText(getApplicationContext(),jsonObject.getString("msg"),Toast.LENGTH_LONG).show();

                    }
                }catch (Exception e){
                    //Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();

                }
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),t.getMessage().toString(),Toast.LENGTH_LONG).show();

                pd.dismiss();
            }
        });
    }

    public void reSendOTP(String mobile){
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.show();
        MyApi myApi= RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call=myApi.resendOTP(mobile);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                try{

                    //Toast.makeText(getApplicationContext(),response.body().toString(),Toast.LENGTH_LONG).show();

                    JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    String status=jsonObject.getString("res");
                    if(status.equalsIgnoreCase("success")){

                        Toast.makeText(getApplicationContext(),jsonObject.getString("msg"),Toast.LENGTH_LONG).show();

                    }else {
                        Toast.makeText(getApplicationContext(),jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    //Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();

                }
                pd.dismiss();

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),t.getMessage().toString(),Toast.LENGTH_LONG).show();

                pd.dismiss();
            }
        });
    }

    public void createPass(){
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.show();
        MyApi myApi= RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call=myApi.createPass(mobile,fp_token,ConfirmPass);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                try{

                    //Toast.makeText(getApplicationContext(),response.body().toString(),Toast.LENGTH_LONG).show();

                    JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    String status=jsonObject.getString("res");
                    if(status.equalsIgnoreCase("success")){

                        TransitionSet set = new TransitionSet()
                                .addTransition(new Slide(Gravity.LEFT))
                                .setInterpolator(
                                        new FastOutLinearInInterpolator());

                        TransitionManager.beginDelayedTransition(view_group,set);
                        A3.setVisibility(View.VISIBLE);
                        Snackbar.make(parentLayout, "Password Changed !", Snackbar.LENGTH_LONG)
                                .setAction("CLOSE", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                })
                                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                                .show();

                        Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                        finish();

                    }else {
                        Toast.makeText(getApplicationContext(),jsonObject.getString("msg"),Toast.LENGTH_LONG).show();

                    }
                }catch (Exception e){
                    //Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();

                }
                pd.dismiss();
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),t.getMessage().toString(),Toast.LENGTH_LONG).show();

                pd.dismiss();
            }
        });
    }

}