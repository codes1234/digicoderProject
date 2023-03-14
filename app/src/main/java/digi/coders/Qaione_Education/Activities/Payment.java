package digi.coders.Qaione_Education.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
import digi.coders.Qaione_Education.models.User;
import digi.coders.Qaione_Education.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Payment extends AppCompatActivity implements PaymentResultListener {

    private static final String TAG = Payment.class.getSimpleName();
    private SingleTask singleTask;
    private User user;
    private String mobileNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        singleTask = (SingleTask) getApplication();
        getRezorPayAPiKey();

    }

    private void startPayment() {
        final Activity activity = this;
        String jsonObject = singleTask.getValue("user");
        user = new Gson().fromJson(jsonObject, User.class);
        mobileNo = user.getNumber();
        //Log.e("user",user.getNumber());
        //Log.e("sds",singleTask.getValue("mobile"));
        if (api_key != null) {
            Log.e("api_key", api_key);
            final com.razorpay.Checkout co = new Checkout();
            co.setKeyID(api_key);
            try {
                JSONObject options = new JSONObject();
                options.put("name", user.getName());
                options.put("description", "Payment  For Registration at http://qaione.digitalcoders.in/");

                options.put("image", "http://qaione.digitalcoders.in/uploads/logo.png");
                options.put("theme.color", "#c300f5");
                options.put("currency", "INR");
//                Log.e("asdsad",getIntent().getStringExtra("orderId"));
                options.put("order_id", getIntent().getStringExtra("orderId"));
                String d = getIntent().getStringExtra("amount");
                Log.e("sd",d);
                Log.e("sds",d);
                Log.e("sds", Double.parseDouble(d) * 100.0 + "");
                options.put("amount", Double.parseDouble(d) * 100.0);
                JSONObject preFill = new JSONObject();
                preFill.put("email", user.getEmail());
                preFill.put("contact", user.getNumber());
                options.put("prefill", preFill);

                co.open(activity, options);

            } catch (Exception e) {
                Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                Log.e("Error in payment", e.getMessage());
                e.printStackTrace();
            }
        } else {
            getRezorPayAPiKey();

        }
    }

    String api_key;

    private void getRezorPayAPiKey() {
        MyApi myApi = RetrofitInstanse.getRetrofitInstance().create(MyApi.class);
        Call<JsonArray> call = myApi.getRezorPayAPIkey();
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
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            String API_KEY = jsonObject1.getString("razorpay_key");
                            Log.e("sdsd", API_KEY);
                            api_key = API_KEY;
                            startPayment();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("excep", t.getMessage());

            }
        });

    }

    int key1;

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            key1 = 1;
            //updatePaymentStatusUpdate(razorpayPaymentID, key1);
            //startActivity(new Intent());
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */

    @Override
    public void onPaymentError(int code, String response) {
        try {
            key1 = 2;
          //  updatePaymentStatusUpdate("", key1);
            Toast.makeText(this, "Payment failed: " + "retry again" + response, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

}