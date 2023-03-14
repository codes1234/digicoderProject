package digi.coders.Qaione_Education.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import digi.coders.Qaione_Education.Helper.MyApi;
import digi.coders.Qaione_Education.Helper.RetrofitInstanse;
import digi.coders.Qaione_Education.R;
import digi.coders.Qaione_Education.models.EbookPaymentFullDetails;
import digi.coders.Qaione_Education.models.FullPaymentData;
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.singletask.SingleTask;
import in.aabhasjindal.otptextview.OtpTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActivity extends AppCompatActivity {

    FloatingActionButton verify_btn;
    private OtpTextView otpTextView;
    private String mobileNo;
    private SingleTask singleTask;
    private RelativeLayout pLay;
    private long pressedTime;
    private ProgressDialog progressDialog;
    private static final String TAG = Payment.class.getSimpleName();
    private User user;

    String admission_fee,order_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        initView();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Waiting");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        verify_btn=findViewById(R.id.verify_btn);
        mobileNo=getIntent().getStringExtra("mobile");
        verify_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(valid())
                        {

                            otpVerficationApi();

                        }

                    }
                }
        );
    }

    private void otpVerficationApi() {
        MyApi myApi= RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call=myApi.userOtpVerification(mobileNo,myOtp);
        progressDialog.show();
        pLay.setClickable(false);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    setOtpVerificationResponse(response);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                progressDialog.hide();
                pLay.setClickable(true);
            }
        });
    }

    private void setOtpVerificationResponse(Response<JsonArray> response) {
        try {
            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
            JSONObject jsonObject=jsonArray.getJSONObject(0);
            String res=jsonObject.getString("res");
            String msg=jsonObject.getString("msg");
            Log.e("res",res);
            if(res.equals("success"))
            {
                Log.e("dsd",response.body().toString());
                progressDialog.hide();
                pLay.setClickable(true);
                Toast.makeText(OtpActivity.this, msg, Toast.LENGTH_SHORT).show();
                JSONArray jsonArray1=jsonObject.getJSONArray("data");
                JSONObject jsonObject1=jsonArray1.getJSONObject(0);
                singleTask.addValue("user",jsonObject1);
                sendToHomeActivity();


            }
            else
            {
                progressDialog.hide();
                    Toast.makeText(OtpActivity.this, msg, Toast.LENGTH_SHORT).show();

            }
            Log.e("msg",msg);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendToHomeActivity() {
        Intent in=new Intent(this,HomeActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);
        finish();
    }

    private String myOtp;
    private boolean valid() {
        myOtp=otpTextView.getOTP();
        if(TextUtils.isEmpty(myOtp))
        {
            Toast.makeText(this, "Please Enter your otp", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void initView() {
        otpTextView=findViewById(R.id.otp_view);
        singleTask=(SingleTask)getApplication();
        pLay=findViewById(R.id.parentLayout);

    }

    public void resendOtp(View view) {
        resend();
    }

    private void resend() {
        progressDialog.show();
        pLay.setClickable(false);
        MyApi myApi= RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call=myApi.resendOtp(mobileNo);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    sendOtpResponse(response);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                progressDialog.hide();
                pLay.setClickable(true);
            }
        });
    }

    private void sendOtpResponse(Response<JsonArray> response) {
        try {
            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
            JSONObject jsonObject=jsonArray.getJSONObject(0);
            String res=jsonObject.getString("res");
            String msg=jsonObject.getString("msg");
            progressDialog.hide();

            pLay.setClickable(true);
            if(res.equals("success"))
            {
                Toast.makeText(OtpActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(OtpActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {

            openDialog();
        } else {
            //Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }


    private void openDialog() {
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setTitle("Confirmation");
        alert.setMessage("Are you sure want to go back");
        AlertDialog alertDialog=alert.create();
        alert.setCancelable(false);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            finish();

            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.hide();
            }
        });
        alert.show();
    }
}
